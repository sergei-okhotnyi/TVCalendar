package dev.okhotny.TVCalendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TrendingFragment extends Fragment {

    private RecyclerView mlist;

    public static Fragment newInstance() {
        return new TrendingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shows_list, container, false);
        mlist = (RecyclerView) view.findViewById(R.id.list);
        mlist.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        new LoadTask().execute();

        return view;
    }

    public void requestQueryUpdate(String query) {

    }

    public static interface OpenShowCallback {
        void openShow(TvShow show);
    }

    protected static class ShowViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.status)
        TextView status;
        @InjectView(R.id.rating)
        TextView rating;

        public ShowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bind(TvShow tvShow) {
            title.setText(tvShow.title);
            status.setText(String.format("%d", tvShow.year));
            rating.setText(String.format("%d%%", tvShow.ratings.percentage));
            Picasso.with(App.sInstance).load(tvShow.images.poster).into(image);
        }
    }

    private class LoadTask extends AsyncTask<Void, Void, List<TvShow>> {

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            Trakt trakt = new Trakt();
            trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
            ShowService showService = trakt.showService();

            try {
                return showService.trending();
            } catch (Exception ignored) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TvShow> result) {
            if (result != null) {
                mlist.setAdapter(new ShowViewAdapter(result));
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
            ShowViewHolder vh = new ShowViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TvShow tvShow = mData.get(mlist.getChildPosition(view));
                    ((OpenShowCallback) getActivity()).openShow(tvShow);
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
