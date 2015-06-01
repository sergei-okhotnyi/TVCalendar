package dev.okhotny.TVCalendar.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.text.format.DateUtils;
import android.util.Log;
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

import dev.okhotny.TVCalendar.BuildConfig;
import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.model.BigData;
import dev.okhotny.TVCalendar.ui.widget.FloatingActionButton;
import dev.okhotny.TVCalendar.ui.widget.ObservableScrollView;


public class ShowDetailsActivity extends BaseActivity implements ObservableScrollView.OnScrollChangedListener {

    private BigData mData = new BigData();

    private boolean mIsWatched;

    private View mProgress;
    private ImageView mImage;
    private TextView mOverview;
    private TextView mTitle;
    private TextView mSubtitle;
    private View mHeaderBox;
    private View mDetailsContainer;
    private ObservableScrollView mScrollView;
    private int mHeaderHeightPixels;
    private int mPhotoHeightPixels;
    private int mMaxHeaderElevation;
    private Palette mPallete;
    private TextView mFirstime;
    private TextView mAirtime;
    private TextView mRuntime;
    private TextView mRating;
    private TextView mRating_liked;
    private TextView mRating_hate;
    private int mAddScheduleButtonHeightPixels;
    private FloatingActionButton mAddScheduleButton;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            recomputePhotoAndScrollingMetrics();
            if (mPallete == null && mImage.getDrawable() != null) {
                recomputeColors();
            }
        }
    };
    private TextView mStatsSeasons;
    private TextView mStatsEpisodes;
    private TextView mStatsTime;
    private TextView mActors;

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    private void recomputeColors() {
        Palette.generateAsync(((BitmapDrawable) mImage.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mPallete = palette;
                int darkVibrantColor = palette.getDarkVibrantColor(getResources().getColor(R.color.theme_primary));
                getToolbarBar().setBackgroundColor(darkVibrantColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(darkenColor(darkVibrantColor));
                }

                mHeaderBox.setBackgroundColor(darkVibrantColor);
                mAddScheduleButton.setColor(mPallete.getDarkMutedColor(R.color.theme_primary));
                mAddScheduleButton.setVisibility(View.VISIBLE);
                setFabDrawable();
                ((ImageView) findViewById(R.id.ic_access_time)).getDrawable().setColorFilter(darkVibrantColor, PorterDuff.Mode.SRC_ATOP);
                ((ImageView) findViewById(R.id.ic_thumbs_up_down)).getDrawable().setColorFilter(darkVibrantColor, PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        initView();
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
        mScrollView.addOnScrollChangedListener(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        ViewCompat.setTransitionName(mImage, "photo");

        if (mData.traktv == null) {
            mData.traktv = (TvShow) getIntent().getSerializableExtra("tvshow");
        }
        Picasso.with(this).load(mData.traktv.images.fanart).into(mImage);
        bind();
        if (System.currentTimeMillis() - mData.lastSync > DateUtils.DAY_IN_MILLIS) {
            new LoadTask().execute(mData);
        }

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int height = size.y;
//        ObjectAnimator translationY = ObjectAnimator.ofFloat(findViewById(R.id.details_container), "translationY", height, 0);
//        translationY.setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
//        translationY.start();

//        if (BuildConfig.DEBUG) {
//            DesignSpec designSpec = DesignSpec.fromResource(mDetailsContainer, R.raw.dspec);
//            mDetailsContainer.getOverlay().add(designSpec);
//        }
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
        mAddScheduleButtonHeightPixels = mAddScheduleButton.getHeight();
        mHeaderHeightPixels = mHeaderBox.getHeight();
        mPhotoHeightPixels = mScrollView.getHeight() / 3;

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
        mAddScheduleButton.setTranslationY(newTop + mHeaderHeightPixels - mAddScheduleButtonHeightPixels / 2);

        float gapFillProgress = 1;
        if (mPhotoHeightPixels != 0) {
            gapFillProgress = Math.min(Math.max(getProgress(scrollY, 0, mPhotoHeightPixels), 0), 1);
        }

        ViewCompat.setElevation(mHeaderBox, gapFillProgress * mMaxHeaderElevation);

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

        mFirstime = (TextView) findViewById(R.id.firstime);
        mAirtime = (TextView) findViewById(R.id.airtime);
        mRuntime = (TextView) findViewById(R.id.runtime);
        mRating = (TextView) findViewById(R.id.rating);
        mRating_liked = (TextView) findViewById(R.id.rating_liked);
        mRating_hate = (TextView) findViewById(R.id.rating_hate);

        mStatsSeasons = (TextView) findViewById(R.id.stats_seasons);
        mStatsEpisodes = (TextView) findViewById(R.id.stats_episode);
        mStatsTime = (TextView) findViewById(R.id.stats_time);

        mActors = (TextView) findViewById(R.id.actors);

        mAddScheduleButton = (FloatingActionButton) findViewById(R.id.fabbutton);
        mAddScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ShowDetailsActivity.class.getSimpleName(), "mAddScheduleButton onClick");
                mIsWatched = !mIsWatched;
                setFabDrawable();
            }
        });
    }

    private void setFabDrawable() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_favorite_white_24dp);
        if (mIsWatched) {
            if (mPallete != null) {
                drawable.setColorFilter(mPallete.getVibrantColor(R.color.theme_accent_2), PorterDuff.Mode.SRC_ATOP);
            } else {
                drawable.setColorFilter(R.color.theme_accent_2, PorterDuff.Mode.SRC_ATOP);
            }
        }
        mAddScheduleButton.setDrawable(drawable);
    }

    private void bind() {
        TvShow traktv = mData.traktv;

        mTitle.setText(traktv.title);
//        mSubtitle.setText(TextUtils.join(", ", traktv.));
        mOverview.setText(traktv.overview);

//        mFirstime.setText(new SimpleDateFormat("EEE, d MMMM yyyy").format(traktv.first_aired));
//        mAirtime.setText(String.format("%s %s on %s", traktv.airs.day, traktv.airs.time, traktv.network));
//        mRuntime.setText(String.format("%d min", traktv.runtime));

//        mRating.setText(String.format("%d%%", traktv.rating));

//        mStatsSeasons.setText(String.format("%s", mData.traktv.a.seasons.size()));

//            int totalEpisodes = traktv..episodes.size();
//            mStatsEpisodes.setText(String.format("%s", totalEpisodes));
//            int total = traktv.runtime * totalEpisodes;
//            mStatsTime.setText(String.format("%d:%02d", total / 60, total % 60));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_details, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        if (mData.traktv == null) {
            shareItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {
            startActivity(Intent.createChooser(
                    getDefaultIntent(),
                    getString(R.string.abc_shareactionprovider_share_with)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mData.traktv.title);
        intent.putExtra(Intent.EXTRA_TEXT, mData.traktv.url);
        return intent;
    }

    private class LoadTask extends AsyncTask<BigData, Void, Void> {

        protected Exception mException;

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(BigData... voids) {
            BigData mData = voids[0];
            try {
                Trakt trakt = new Trakt();
                trakt.setApiKey(getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
                ShowService showService = trakt.showService();
                String showId = String.valueOf(mData.traktv.imdb_id);
                mData.traktv = showService.summary(showId, Extended.EXTENDED);
                // mData.seasons = showService(showId, Extended.FULL);
//                Episodes episodeService = trakt.episodes();
//                mData.episodes = new SparseArray<>(mData.seasons.size());
//                for (Season season : mData.seasons) {
//                    if (season.number == 0) continue;
//                    List<Episode> episodes = new ArrayList<>(season.episode_count);
//                    mData.episodes.put(season.number, episodes);
//                    for (int i = 1; i <= season.episode_count; i++) {
//                        episodes.add(episodeService.summary(showId, season.number, i, Extended.FULLIMAGES));
//                    }
//                }
//                mData.lastSync = System.currentTimeMillis();
            } catch (Exception ignored) {
                ignored.printStackTrace();
                mException = ignored;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isCancelled() || isFinishing()) {
                return;
            }
            mProgress.setVisibility(View.GONE);
            if (mException == null) {
                bind();
                supportInvalidateOptionsMenu();
            } else {
                Toast.makeText(ShowDetailsActivity.this, mException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
