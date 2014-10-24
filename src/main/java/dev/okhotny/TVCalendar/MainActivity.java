package dev.okhotny.TVCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;

import java.util.List;

import dev.okhotny.TVCalendar.database.AgendaModel;

public class MainActivity extends Activity {
    private TextView mEmpty;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Crittercism.initialize(getApplicationContext(), getString(R.string.crittercism_apikey));

        setContentView(R.layout.main);
        setTitle(R.string.app_name);

        mListView = (ListView) findViewById(android.R.id.list);
        mEmpty = (TextView) findViewById(android.R.id.empty);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AgendaModel item = (AgendaModel) adapterView.getAdapter().getItem(i);
                openEpisodeDetails(item);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AgendaModel item = (AgendaModel) adapterView.getAdapter().getItem(i);
                return true;
            }
        });

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        refreshAgendaAdapter();
    }

    private void openEpisodeDetails(AgendaModel item) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                .putExtra(DetailsActivity.ARG_SERIES_ID, item.getSeriesId())
                .putExtra(DetailsActivity.ARG_EPISODE_ID, item.getEpisodeId()));

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void refreshAgendaAdapter() {
//        if (mTask != null) {
//            mTask.cancel(true);
//        }
//
//        mTask = new AsyncTask<Void, Void, List<Card>>() {
//
//            @Override
//            protected void onPreExecute() {
//                mPullToRefreshLayout.setRefreshing(true);
//                if (mAgendaAdapter == null) {
//                    mListView.setVisibility(View.GONE);
//                    mEmpty.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            protected List<Card> doInBackground(Void... voids) {
//                List<AgendaModel> list = AgendaModel.getAgenda(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("agenda_hide_watched", false));
//                if (isCancelled()) {
//                    return null;
//                }
//                List<Card> result = new ArrayList<Card>(list.size());
//                for (final AgendaModel m : list) {
//                    result.add(new AgendaModelCard(MainActivity.this, m));
//                }
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(List<Card> episodes) {
//                if (isFinishing() || isCancelled()) {
//                    return;
//                }
//                if (episodes == null || episodes.isEmpty()) {
//                    mListView.setVisibility(View.GONE);
//                    mEmpty.setVisibility(View.VISIBLE);
//                } else {
//                    mEmpty.setVisibility(View.GONE);
//                    mListView.setVisibility(View.VISIBLE);
////                    AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mAgendaAdapter);
////                    animCardArrayAdapter.setAbsListView(mListView);
////                    animCardArrayAdapter.setInitialDelayMillis(500);
////                    if (mListView != null) {
////                        mListView.setExternalAdapter(animCardArrayAdapter, mAgendaAdapter);
////                    }
//                }
//            }
//        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
