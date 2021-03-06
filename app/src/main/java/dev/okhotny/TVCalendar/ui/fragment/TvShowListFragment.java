package dev.okhotny.TVCalendar.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.services.SearchService;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.BuildConfig;
import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.ui.BaseActivityOnScrollListener;
import dev.okhotny.TVCalendar.ui.ShowDetailsActivity;

public class TvShowListFragment extends Fragment {

    public static final String MODE = "Mode";
    private RecyclerView mlist;
    private ProgressBar mProgress;
    private TextView mMessage;
    private String mQuery;
    private View mErrorContainer;
    private Drawable mErrorDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shows_list, container, false);
        mlist = (RecyclerView) view.findViewById(R.id.list);
        mlist.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_column_count)));
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mErrorContainer = view.findViewById(R.id.error_container);
        mMessage = (TextView) view.findViewById(R.id.message);
        view.findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        mErrorDrawable = getResources().getDrawable(R.drawable.ic_error_white_18dp);
        mErrorDrawable.setColorFilter(getResources().getColor(R.color.theme_accent_2), PorterDuff.Mode.SRC_IN);

        if (getArguments() != null && getArguments().containsKey(MODE)) {
            switch (getArguments().getInt(MODE)) {
                case 0:
                    showTrending();
                    break;
            }
        }
        return view;
    }

    private void retry() {
        if (TextUtils.isEmpty(mQuery)) {
            showTrending();
        } else {
            requestQueryUpdate(mQuery);
        }
    }

    public void requestQueryUpdate(String query) {
        mQuery = query;
        new SearchTask().execute();
    }

    public void showTrending() {
        new TrendingTask().execute();
    }

    public void setOnScrollListener(final BaseActivityOnScrollListener listener) {
        if (listener == null) {
            mlist.setOnScrollListener(null);
        } else {
            mlist.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    listener.onScroll(dx, dy);
                }
            });
        }
    }

    private static class ShowViewHolder extends RecyclerView.ViewHolder {
        private View background;
        private TextView title;
        private ImageView image;
        private TextView status;

        public ShowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.image);
            status = (TextView) itemView.findViewById(R.id.status);
            background = (View) itemView.findViewById(R.id.background);
        }

        public void bind(TvShow tvShow) {
            title.setText(tvShow.title);
            status.setText(String.format("%d", tvShow.year));
            background.setBackgroundColor(Color.WHITE);
            if (tvShow.images != null && tvShow.images.headshot != null) {
                Picasso.with(App.sInstance).load(tvShow.images.headshot).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Palette.generateAsync(((BitmapDrawable) image.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                background.setBackgroundColor(palette.getLightMutedColor(Color.WHITE));
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        }
    }

    private class TrendingTask extends AsyncTask<Void, Void, List<TvShow>> {

        protected Exception mException;

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
            mErrorContainer.setVisibility(View.GONE);
            mlist.setVisibility(View.GONE);
            mlist.setAdapter(null);
        }

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            Trakt trakt = new Trakt();
            trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
            ShowService showService = trakt.showService();

            try {
                List<TvShow> trending = showService.trending();
                return trending;
            } catch (Exception ignored) {
                mException = ignored;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TvShow> result) {
            if (isCancelled() || getActivity() == null || getActivity().isFinishing()) {
                return;
            }
            mProgress.setVisibility(View.GONE);
            if (result != null && !result.isEmpty()) {
                mlist.setVisibility(View.VISIBLE);
                mlist.setAdapter(new ShowViewAdapter(result));
            } else {
                mErrorContainer.setVisibility(View.VISIBLE);
                if (mException == null) {
                    mMessage.setText(R.string.nothing_found);
                    mMessage.setCompoundDrawables(null, null, null, null);
                } else {
                    mMessage.setText(mException.getLocalizedMessage());
                    mMessage.setCompoundDrawables(mErrorDrawable, null, null, null);
                }
            }
        }
    }

    private class SearchTask extends TrendingTask {

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            Trakt trakt = new Trakt();
            trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
            SearchService showService = trakt.searchService();

            try {
                return showService.shows(mQuery);
            } catch (Exception ignored) {
                mException = ignored;
                return null;
            }
        }

    }

    private class ShowViewAdapter extends RecyclerView.Adapter<ShowViewHolder> {

        private final List<TvShow> mData;

        public ShowViewAdapter(List<TvShow> result) {
            mData = result;
        }

        @Override
        public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.show_cardview, viewGroup, false);
            final ShowViewHolder vh = new ShowViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TvShow tvShow = mData.get(mlist.getChildPosition(view));

                    Intent intent = new Intent(getActivity(), ShowDetailsActivity.class)
                            .putExtra("tvshow", tvShow);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat photo = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), vh.image, "photo");
                        getActivity().startActivity(intent, photo.toBundle());
                    } else {
                        getActivity().startActivity(intent);
                    }
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ShowViewHolder showViewHolder, int i) {
            TvShow tvShow = mData.get(i);
            showViewHolder.bind(tvShow);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

    }
}
