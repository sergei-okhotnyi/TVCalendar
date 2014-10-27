package dev.okhotny.TVCalendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.enumerations.Extended;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowDetailsFragment extends Fragment {

    private TvShow tvShow;
    private ImageView banner;

    public static Fragment newInstance(TvShow tvshow) {
        ShowDetailsFragment instantiate = new ShowDetailsFragment();

        Bundle args = new Bundle();
        args.putInt("tvdb_id", tvshow.tvdb_id);
        instantiate.setArguments(args);

        return instantiate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_details, container, false);

        banner = (ImageView) view.findViewById(R.id.banner);


        int tvdb_id = getArguments().getInt("tvdb_id");
        if (tvShow == null || tvShow.tvdb_id != tvdb_id) {
            new LoadTask().execute(tvdb_id);
        } else {
            bindData();
        }

        return view;
    }


    private class LoadTask extends AsyncTask<Integer, Void, TvShow> {

        @Override
        protected TvShow doInBackground(Integer... tvdb_id) {
            Trakt trakt = new Trakt();
            trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(true);
            return trakt.showService().summary(tvdb_id[0], Extended.EXTENDED);
        }

        @Override
        protected void onPostExecute(TvShow result) {
            if (getActivity() != null) {
                tvShow = result;
                bindData();
            }
        }
    }

    private void bindData() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(tvShow.title);
        Picasso.with(this.getActivity()).load(tvShow.images.fanart).into(banner);
    }
}
