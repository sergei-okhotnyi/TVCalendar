package dev.okhotny.TVCalendar.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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
import dev.okhotny.TVCalendar.ui.widget.ObservableScrollView;


public class ShowDetailsActivity extends BaseActivity implements ObservableScrollView.OnScrollChangedListener {

    private TvShow mTvShow;
    private View mProgress;
    private ImageView mImage;
    private TextView mOverview;
    private TextView mTitle;
    private TextView mSubtitle;
    private View mHeaderBox;
    private View mDetailsContainer;
    private ObservableScrollView mScrollView;
    private ShareActionProvider mShareActionProvider;
    private int mHeaderHeightPixels;
    private int mPhotoHeightPixels;
    private int mMaxHeaderElevation;
    private Palette mPallete;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            recomputePhotoAndScrollingMetrics();
            if (mPallete == null && mImage.getDrawable() != null) {
                recomputeColors();
            }
        }
    };

    private void recomputeColors() {
        Palette.generateAsync(((BitmapDrawable) mImage.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mPallete = palette;
                int darkVibrantColor = palette.getDarkVibrantColor(getResources().getColor(R.color.theme_primary));
                getToolbarBar().setBackgroundColor(darkVibrantColor);
                supportInvalidateOptionsMenu();
                mHeaderBox.setBackgroundColor(darkVibrantColor);
            }
        });
    }

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
        mMaxHeaderElevation = getResources().getDimensionPixelSize(R.dimen.toolbar_elevation);

        if (mTvShow == null) {
            mTitle.setText(getIntent().getStringExtra("title"));
            Picasso.with(this).load(getIntent().getStringExtra("poster")).into(mImage);
            new LoadTask().execute(getIntent().getIntExtra("tvdbid", 0));
        } else {
            bind(mTvShow);
        }

        mScrollView.addOnScrollChangedListener(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        ViewCompat.setTransitionName(mImage, "photo");

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int height = size.y;
//        ObjectAnimator translationY = ObjectAnimator.ofFloat(findViewById(R.id.details_container), "translationY", height, 0);
//        translationY.setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
//        translationY.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
    }

    private void recomputePhotoAndScrollingMetrics() {
        mHeaderHeightPixels = mHeaderBox.getHeight();
        mPhotoHeightPixels = Math.min(mImage.getHeight(), mScrollView.getHeight() * 2 / 3);

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mDetailsContainer.getLayoutParams();
        if (mlp.topMargin != mHeaderHeightPixels + mPhotoHeightPixels) {
            mlp.topMargin = mHeaderHeightPixels + mPhotoHeightPixels;
            mDetailsContainer.setLayoutParams(mlp);
        }

        onScrollChanged(0, 0);
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // Reposition the header bar -- it's normally anchored to the top of the content,
        // but locks to the top of the screen on scroll
        int scrollY = mScrollView.getScrollY();

        float newTop = Math.max(mPhotoHeightPixels, scrollY);
        mHeaderBox.setTranslationY(newTop);
//        mAddScheduleButton.setTranslationY(newTop + mHeaderHeightPixels- mAddScheduleButtonHeightPixels / 2);

        float gapFillProgress = 1;
        if (mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(getProgress(scrollY, 0, mPhotoHeightPixels), 0), 1);
        }

        ViewCompat.setElevation(mHeaderBox, gapFillProgress * mMaxHeaderElevation);
//        ViewCompat.setElevation(mAddScheduleButton, gapFillProgress * mMaxHeaderElevation
//                + mFABElevation);

        // Move background photo (parallax effect)
        mImage.setTranslationY(scrollY * 0.5f);
    }

    public float getProgress(int value, int min, int max) {
        if (min == max) {
            throw new IllegalArgumentException("Max (" + max + ") cannot equal min (" + min + ")");
        }

        return (value - min) / (float) (max - min);
    }

    private void initView() {
        mProgress = findViewById(R.id.progress);
        mImage = (ImageView) findViewById(R.id.banner);
        mOverview = (TextView) findViewById(R.id.overview);
        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);
        mHeaderBox = findViewById(R.id.header);
        mDetailsContainer = findViewById(R.id.details_container);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_container);
    }

    private void bind(TvShow result) {
        mTvShow = result;
        mTitle.setText(mTvShow.title);
        mSubtitle.setText(String.format("%d", mTvShow.year));
        mOverview.setText(mTvShow.overview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_details, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        if (mTvShow == null) {
            shareItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {
            startActivity(Intent.createChooser(
                    getDefaultIntent(),
                    getString(R.string.abc_shareactionprovider_share_with)));
            return true;
        }
        return super.onContextItemSelected(item);
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
                supportInvalidateOptionsMenu();
            } else {
                Toast.makeText(ShowDetailsActivity.this, mException != null ? mException.getLocalizedMessage() : "TV Show not found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
