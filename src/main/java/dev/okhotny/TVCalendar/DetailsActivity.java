package dev.okhotny.TVCalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.database.DatabaseHelper;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Actor;
import dev.okhotny.TVCalendar.providers.model.Banner;
import dev.okhotny.TVCalendar.providers.model.Banners;
import dev.okhotny.TVCalendar.providers.model.Comment;
import dev.okhotny.TVCalendar.providers.model.Episode;
import dev.okhotny.TVCalendar.providers.model.Season;
import dev.okhotny.TVCalendar.providers.model.Series;

public class DetailsActivity extends FragmentActivity implements AbsListView.OnScrollListener {

    public static final String ARG_SERIES_ID = "item_id";
    public static final String ARG_EPISODE_ID = "episode_id";
    private ViewPager mViewPager;
    private Series mItem;
    private SparseArray<Season> mSeasonList;
    private List<Actor> mActorList;
    private Banners mBanners;
    private boolean mIsItemFavored;
    private PagerSlidingTabStrip mTabsStrip;
    private View mHeader;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private int mActionBarHeight;
    private TypedValue mTypedValue = new TypedValue();
    private KenBurnsView mHeaderPicture;
    private View mPlaceHolderView;
    private List<Comment> mReviews;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mHeader = findViewById(R.id.header);
        mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
        mPlaceHolderView = findViewById(R.id.header_placeholder);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabsStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_title_strip);
        mTabsStrip.setShouldExpand(true);
        mTabsStrip.setTextColor(Color.WHITE);
        mTabsStrip.setIndicatorColor(Color.WHITE);
        mTabsStrip.setUnderlineColor(Color.WHITE);

        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

        final String seriesID = getIntent().getStringExtra(ARG_SERIES_ID);

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);
        try {
            mIsItemFavored = databaseHelper.getSeriesDao().idExists(seriesID);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        TheTVDBApi.getAllData(getIntent().getStringExtra(ARG_SERIES_ID), false, new SimpleCallback<Series>() {
            @Override
            public void done(Series result) {
                mItem = result;
                setTitle(mItem.getSeriesName());

                mSeasonList = Season.getSeasons(mItem.getEpisodes());

                mViewPager.setAdapter(new ViewPagerAdapter());
                mTabsStrip.setViewPager(mViewPager);

                updateBanner();

                TheTVDBApi.getActors(seriesID, new SimpleCallback<List<Actor>>() {
                    @Override
                    public void done(List<Actor> result) {
                        mActorList = result;
                        mViewPager.getAdapter().notifyDataSetChanged();
                        mTabsStrip.notifyDataSetChanged();
                    }
                });
                TheTVDBApi.getBanners(seriesID, new SimpleCallback<Banners>() {
                    @Override
                    public void done(Banners result) {
                        mBanners = result;
                        mViewPager.getAdapter().notifyDataSetChanged();
                        mTabsStrip.notifyDataSetChanged();
                        updateBanner();
                    }
                });
                TraktTvApi.getSeriesComments(seriesID, new SimpleCallback<List<Comment>>() {
                    @Override
                    public void done(List<Comment> result) {
                        mReviews = result;
                        mViewPager.getAdapter().notifyDataSetChanged();
                        mTabsStrip.notifyDataSetChanged();

                    }
                });
            }
        });

    }

    private void updateBanner() {
        if (TextUtils.isEmpty(mItem.getBanner())) {
        } else if (mBanners == null || mBanners.getFanartList().isEmpty()) {
            mHeaderPicture.setResourceIds(mItem.getBanner());
        } else {
            List<String> urls = new ArrayList<String>(mBanners.getFanartList().size());
            for (Banner banner : mBanners.getFanartList()) {
                urls.add(banner.getUrl());
            }
            mHeaderPicture.setResourceIds(urls);
        }

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog(final MenuItem mMenuItem) {
        if (mItem == null) {
            return;
        }
        new AlertDialog.Builder(DetailsActivity.this)
//                .setIcon(R.drawable.rating_important)
                .setTitle("Favorites")
                .setMessage(mIsItemFavored ? "Remove series from favorites?" : "Add series to favorites?")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        if (!isFinishing()) {
//                                            mMenuItem.setIcon(mIsItemFavored ? R.drawable.rating_important : R.drawable.rating_not_important);
                                            mMenuItem.setActionView(null);
                                        }
                                    }

                                    @Override
                                    protected void onPreExecute() {
                                        mMenuItem.setActionView(new ProgressBar(DetailsActivity.this));
                                    }

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        if (mIsItemFavored) {
                                            DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);
                                            try {
                                                databaseHelper.getSeriesDao().deleteById(mItem.getId());
                                                DeleteBuilder<Episode, String> builder = databaseHelper.getEpisodeDao().deleteBuilder();
                                                builder.where().eq("seriesId", mItem.getId());
                                                databaseHelper.getEpisodeDao().delete(builder.prepare());
                                                mIsItemFavored = false;
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            } finally {
                                                OpenHelperManager.releaseHelper();
                                            }
                                        } else {
                                            DatabaseHelper databaseHelper = OpenHelperManager.getHelper(getBaseContext(), DatabaseHelper.class);
                                            try {
                                                databaseHelper.getSeriesDao().createIfNotExists(mItem);
                                                for (Episode e : mItem.getEpisodes()) {
                                                    databaseHelper.getEpisodeDao().createIfNotExists(e);
                                                }
                                                mIsItemFavored = true;
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            } finally {
                                                OpenHelperManager.releaseHelper();
                                            }
                                        }
                                        return null;
                                    }
                                }.execute();
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        int scrollY = getScrollY(view);
//        if (scrollY != 0) {
//            mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
//            mPlaceHolderView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.max(mPlaceHolderView.getHeight() - scrollY, getActionBarHeight())));
//        }
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight == 0) {
            getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
        }
        return mActionBarHeight + mTabsStrip.getHeight();
    }

    public class ViewPagerAdapter extends PagerAdapter {

        /**
         * Get a View that displays the data at the specified position in the data set.
         *
         * @param position The position of the item within the adapter's data set of the item whose view we want.
         * @param pager    The ViewPager that this view will eventually be attached to.
         * @return A View corresponding to the data at the specified position.
         */
        public View getView(int position, ViewPager pager) {
            if (position == 0) {
                //return createOverviewView();
            }
            position--;
            if (mSeasonList != null && position < mSeasonList.size()) {
                //return createSeasonView(mSeasonList.valueAt(mSeasonList.size() - 1 - (position)).SeasonNumber);
            }
            position -= (mSeasonList != null ? mSeasonList.size() : 0);
            if (position < 1 && mActorList != null) {
                //return createActorsView();
            }
            position--;
            if (position < 1 && mBanners != null) {
                //return createBannersView();
            }
            position--;
            if (position < 1 && mReviews != null) {
                //return createReviewsView();
            }
            return null;
        }

        /**
         * Determines whether a page View is associated with a specific key object as
         * returned by instantiateItem(ViewGroup, int).
         *
         * @param view   Page View to check for association with object
         * @param object Object to check for association with view
         * @return true if view is associated with the key object object.
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return (mItem == null ? 0 : 1) +
                    (mSeasonList != null ? mSeasonList.size() : 0) +
                    (mActorList == null || mActorList.isEmpty() ? 0 : 1) +
                    (mBanners == null || mBanners.isEmpty() ? 0 : 1) +
                    (mReviews == null || mReviews.isEmpty() ? 0 : 1);
        }

        /**
         * Create the page for the given position.
         *
         * @param container The containing View in which the page will be shown.
         * @param position  The page position to be instantiated.
         * @return Returns an Object representing the new page. This does not need
         * to be a View, but can be some other container of the page.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewPager pager = (ViewPager) container;
            View view = getView(position, pager);
            pager.addView(view);
            return view;
        }

        /**
         * Remove a page for the given position.
         *
         * @param container The containing View from which the page will be removed.
         * @param position  The page position to be removed.
         * @param view      The same object that was returned by instantiateItem(View, int).
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Overview".toUpperCase();
            }
            position--;
            if (mSeasonList != null && position < mSeasonList.size()) {
                int index = mSeasonList.size() - 1 - (position);
                int seasonNumber = mSeasonList.valueAt(index).SeasonNumber;
                if (seasonNumber == 0) {
                    return "Extras".toUpperCase();
                }
                return (String.format("Season %s", seasonNumber)).toUpperCase();
            }
            position -= (mSeasonList != null ? mSeasonList.size() : 0);
            if (position < 1 && mActorList != null) {
                return "Actors";
            }
            position--;
            if (position < 1 && mBanners != null) {
                return "Posters";
            }
            position--;
            if (position < 1 && mReviews != null) {
                return "Comments";
            }
            return null;
        }
    }

}