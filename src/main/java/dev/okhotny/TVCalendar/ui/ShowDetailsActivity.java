package dev.okhotny.TVCalendar.ui;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.enumerations.Extended;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.BuildConfig;
import dev.okhotny.TVCalendar.R;


public class ShowDetailsActivity extends BaseActivity {

    private TvShow mTvShow;
    private View mProgress;
    private ImageView mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        initView();

        getToolbarBar().setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getToolbarBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setTransitionName(mBanner, "photo");

        if (mTvShow == null) {
            setTitle(getIntent().getStringExtra("title"));
            Picasso.with(this).load(getIntent().getStringExtra("poster")).into(mBanner);
            new LoadTask().execute(getIntent().getIntExtra("tvdbid", 0));
        } else {
            bind(mTvShow);
        }
    }

    private void initView() {
        mProgress = findViewById(R.id.progress);
        mBanner = (ImageView) findViewById(R.id.banner);
    }

    private class LoadTask extends AsyncTask<Integer, Void, TvShow> {

        protected Exception mException;

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected TvShow doInBackground(Integer... voids) {
            Integer tvdbid = voids[0];
            if (tvdbid != 0) {
                Trakt trakt = new Trakt();
                trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
                ShowService showService = trakt.showService();
                try {
                    return showService.summary(tvdbid, Extended.EXTENDED);
                } catch (Exception ignored) {
                    mException = ignored;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(TvShow result) {
            mProgress.setVisibility(View.GONE);
            if (result != null) {
                bind(result);
            } else {
                Toast.makeText(ShowDetailsActivity.this, mException != null ? mException.getLocalizedMessage() : "TV Show not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void bind(TvShow result) {
        mTvShow = result;
        setTitle(mTvShow.title);

    }

}
