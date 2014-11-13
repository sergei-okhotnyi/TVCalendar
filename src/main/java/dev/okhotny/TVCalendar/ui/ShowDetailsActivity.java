package dev.okhotny.TVCalendar.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.enumerations.Extended;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Picasso;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.BuildConfig;
import dev.okhotny.TVCalendar.R;


public class ShowDetailsActivity extends BaseActivity {

    private TvShow mTvShow;
    private View mProgress;
    private ImageView mBanner;
    private TextView mOverview;
    private TextView mTitle;
    private TextView mSubtitle;
    private View mHeader;
    private View mDetails;
    private ScrollView mScrollContainer;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        initView();
        setTitle("");
        getToolbarBar().setTitle("");
        getToolbarBar().setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getToolbarBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        ViewCompat.setTransitionName(mBanner, "photo");

        if (mTvShow == null) {
            mTitle.setText(getIntent().getStringExtra("title"));
            Picasso.with(this).load(getIntent().getStringExtra("poster")).into(mBanner);
            new LoadTask().execute(getIntent().getIntExtra("tvdbid", 0));
        } else {
            bind(mTvShow);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(findViewById(R.id.details_container), "translationY", height, 0);
        translationY.setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
        translationY.start();
    }

    private void initView() {
        mProgress = findViewById(R.id.progress);
        mBanner = (ImageView) findViewById(R.id.banner);
        mOverview = (TextView) findViewById(R.id.overview);
        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);
        mHeader = findViewById(R.id.header);
        mDetails = findViewById(R.id.details_container);
        mScrollContainer = (ScrollView) findViewById(R.id.scroll_container);
    }

    private void bind(TvShow result) {
        mTvShow = result;
        mTitle.setText(mTvShow.title);
        mSubtitle.setText(mTvShow.airDay.name());
        mOverview.setText(mTvShow.overview);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_details, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        if (mTvShow != null) {
            mShareActionProvider = (ShareActionProvider)
                    MenuItemCompat.getActionProvider(shareItem);
            mShareActionProvider.setShareIntent(getDefaultIntent());
        } else {
            shareItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mTvShow.title);
        intent.putExtra(Intent.EXTRA_TEXT, mTvShow.url);
        return intent;
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
            if (isCancelled() || isFinishing()) {
                return;
            }
            mProgress.setVisibility(View.GONE);
            if (result != null) {
                bind(result);
                invalidateOptionsMenu();
            } else {
                Toast.makeText(ShowDetailsActivity.this, mException != null ? mException.getLocalizedMessage() : "TV Show not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
