package dev.okhotny.TVCalendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.services.ShowService;

import java.util.List;

public class TrendingFragment extends Fragment {

    private RecyclerView mlist;

    public static Fragment newInstance() {
        return new TrendingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shows_list, container, false);
        mlist = (RecyclerView) view.findViewById(R.id.list);
        mlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        new LoadTask().execute();

        return view;
    }

    private class LoadTask extends AsyncTask<Void, Void, List<TvShow>> {

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            Trakt trakt = new Trakt();
            trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey));
            ShowService showService = trakt.showService();
            return showService.trending();
        }

        @Override
        protected void onPostExecute(List<TvShow> result) {
            mlist.setAdapter(new ShowViewAdapter(result));
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
            return vh;
        }

        @Override
        public void onBindViewHolder(ShowViewHolder showViewHolder, int i) {
            TvShow tvShow = mData.get(i);
            showViewHolder.title.setText(tvShow.title);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private static class ShowViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;

        public ShowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }


    }
}
