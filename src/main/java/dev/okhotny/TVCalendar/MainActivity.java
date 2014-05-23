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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import com.crashlytics.android.Crashlytics;
import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.database.AgendaModel;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends Activity {
    private CardListView mListView;
    private View mProgress;
    private TextView mEmpty;
    private CardArrayAdapter mAgendaAdapter;
    private AsyncTask<Void, Void, List<Card>> mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        setContentView(R.layout.main);
        setTitle(R.string.app_name);

        mListView = (CardListView) findViewById(android.R.id.list);
        mProgress = findViewById(android.R.id.progress);
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

    private void showProcessIndicator() {
        //To Do change body of created methods use File | Settings | File Templates.
    }

    private void hideProcessindicator() {
        //To Do change body of created methods use File | Settings | File Templates.
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
        if (mTask != null) {
            mTask.cancel(true);
        }

        mTask = new AsyncTask<Void, Void, List<Card>>() {

            @Override
            protected void onPreExecute() {
                if (mAgendaAdapter == null) {
                    mProgress.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.GONE);
                }
                showProcessIndicator();
            }

            @Override
            protected List<Card> doInBackground(Void... voids) {
                List<AgendaModel> list = AgendaModel.getAgenda(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("agenda_hide_watched", false));
                if (isCancelled()) {
                    return null;
                }
                List<Card> result = new ArrayList<Card>(list.size());
                for (final AgendaModel m : list) {
                    result.add(new AgendaModelCard(MainActivity.this, m));
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Card> episodes) {
                if (isFinishing() || isCancelled()) {
                    return;
                }
                mProgress.setVisibility(View.GONE);
                if (episodes == null || episodes.isEmpty()) {
                    mListView.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                } else {
                    mEmpty.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mAgendaAdapter = new CardArrayAdapter(MainActivity.this, episodes);
                    mListView.setAdapter(mAgendaAdapter);
//                    AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mAgendaAdapter);
//                    animCardArrayAdapter.setAbsListView(mListView);
//                    animCardArrayAdapter.setInitialDelayMillis(500);
//                    if (mListView != null) {
//                        mListView.setExternalAdapter(animCardArrayAdapter, mAgendaAdapter);
//                    }
                }
                hideProcessindicator();
            }
        }.execute();
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

    public class AgendaModelCard extends Card {

        private AgendaModel mData;

        public AgendaModelCard(Context context, AgendaModel data) {
            super(context, R.layout.main_card_inner_main);
            mData = data;
            init();
        }

        private void init() {

            CardHeader header = new CardHeader(getContext());
            header.setTitle(mData.getSeriesName());
            header.setButtonExpandVisible(true);
            addCardHeader(header);

            EpisodeCardExpand cardExpand = new EpisodeCardExpand(getContext());
            cardExpand.setTitle(mData.getEpisodeName());
            addCardExpand(cardExpand);

            VolleyCardThumbnail thumbnail = new VolleyCardThumbnail(getContext());
            thumbnail.setUrlResource(mData.getPoster());
            addCardThumbnail(thumbnail);
            thumbnail.setupImagePopup();

            addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    startActivity(new Intent(getBaseContext(), DetailsActivity.class).putExtra(DetailsActivity.ARG_SERIES_ID, mData.getSeriesId()));
                }
            });

            setSwipeable(true);
            setOnSwipeListener(new OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    Toast.makeText(getContext(), "Swipe Listener card", Toast.LENGTH_LONG).show();
                }
            });

            setViewToClickToExpand(ViewToClickToExpand.builder().highlightView(true).setupCardElement(ViewToClickToExpand.CardElementUI.CARD));
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            TextView title = (TextView) view.findViewById(R.id.card_main_inner_episode_title);
            title.setText("S" + mData.getSeasonNumber() + "E" + mData.getEpisodeNumber() + " " + mData.getEpisodeName());

            if (mData.getFirstAired() != null) {
                TextView aired = (TextView) view.findViewById(R.id.card_main_inner_aired_date);
                aired.setText(DateUtils.getRelativeDateTimeString(getContext(), mData.getFirstAired().getTime(), DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_NO_NOON));
            }
        }

        private class EpisodeCardExpand extends CardExpand {

            public EpisodeCardExpand(Context context) {
                super(context, R.layout.main_card_expand);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View view) {

                TextView title = (TextView) view.findViewById(R.id.card_episode_overview);
                title.setText(mData.getOverview());

                String url = mData.getFileName();
                NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.card_thumbnail_image);
                imageView.setImageUrl(url, App.sInstance.imageLoader);

                imageView.setDefaultImageResId(R.drawable.ic_launcher);

            }
        }

    }
}
