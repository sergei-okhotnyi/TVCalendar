package dev.okhotny.TVCalendar;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Series;
import dev.okhotny.TVCalendar.providers.thetvdb.TheTVDBApi;
import dev.okhotny.TVCalendar.providers.trakttv.TraktTvApi;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCardArrayAdapter;
import it.gmariotti.cardslib.library.internal.overflowanimation.TwoCardOverlayAnimation;
import it.gmariotti.cardslib.library.view.CardListView;

public class SearchActivity extends Activity {

    private SearchView mSearchView;
    private String mSearchString;
    private CardListView mGrid;
    private View mProgress;
    private TextView mEmpty;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.search);
        setTitle(R.string.search);

        mGrid = (CardListView) findViewById(android.R.id.list);
        mProgress = findViewById(android.R.id.progress);
        mEmpty = (TextView) findViewById(android.R.id.empty);

        handleSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setQuery(mSearchString, false);
        mSearchView.clearFocus();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSearch() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchString = intent.getStringExtra(SearchManager.QUERY);
            if (TextUtils.isEmpty(searchString) || searchString.equals(mSearchString)) {
                return;
            }
            mSearchString = searchString;
            mProgress.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
            mGrid.setVisibility(View.GONE);
            setTitle(R.string.search);

            TheTVDBApi.searchSeries(mSearchString, new SimpleCallback<List<Series>>() {
                @Override
                public void done(List<Series> result) {
                    if (isFinishing()) {
                        return;
                    }
                    mProgress.setVisibility(View.GONE);
                    if (result == null || result.isEmpty()) {
                        mSearchView.requestFocus();
                        mGrid.setVisibility(View.GONE);
                        mEmpty.setVisibility(View.VISIBLE);
                        mEmpty.setText(R.string.no_data);
                    } else {
                        mSearchView.setIconified(false);
                        mSearchView.clearFocus();
                        mEmpty.setVisibility(View.GONE);
                        mGrid.setVisibility(View.VISIBLE);
                        ArrayList<Card> cards = new ArrayList<Card>(result.size());
                        for (final Series s : result) {
                            cards.add(new SeriesCard(SearchActivity.this, s));
                        }
                        CardArrayAdapter adapter = new CardArrayAdapter(SearchActivity.this, cards);
                        mGrid.setAdapter(adapter);
                        mGrid.requestFocus();
                    }
                }
            });
        } else {
            mProgress.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
            mGrid.setVisibility(View.GONE);
            setTitle("Trending");

            TraktTvApi.getTrendingSeries(new SimpleCallback<List<Series>>() {
                @Override
                public void done(List<Series> result) {
                    if (isFinishing()) {
                        return;
                    }
                    mProgress.setVisibility(View.GONE);
                    if (result == null || result.isEmpty()) {
                        mSearchView.requestFocus();
                        mGrid.setVisibility(View.GONE);
                        mEmpty.setVisibility(View.VISIBLE);
                        mEmpty.setText(R.string.no_data);
                    } else {
                        mSearchView.setIconified(false);
                        mSearchView.clearFocus();
                        mEmpty.setVisibility(View.GONE);
                        mGrid.setVisibility(View.VISIBLE);
                        ArrayList<Card> cards = new ArrayList<Card>(result.size());
                        for (final Series s : result) {
                            cards.add(new SeriesCard(SearchActivity.this, s));
                        }
                        CardArrayAdapter adapter = new CardArrayAdapter(SearchActivity.this, cards);
                        mGrid.setAdapter(adapter);
                        mGrid.requestFocus();
                    }
                }
            });
        }
    }

    private class SeriesCard extends Card {

        private Series mData;

        public SeriesCard(Context context, Series data) {
            super(context, R.layout.search_card_inner_main);
            mData = data;

            TheTVDBApi.getSeries(mData.getId(), false, new SimpleCallback<Series>() {
                @Override
                public void done(Series result) {
                    mData = result;
                    ((BaseCardArrayAdapter) mGrid.getAdapter()).notifyDataSetChanged();
                }
            });

            init();
        }

        private void init() {


            //Add Header
            CardHeader header = new CardHeader(getContext());
            header.setTitle(mData.getSeriesName());
//            header.setCustomOverflowAnimation(new SimpleBirthAnimation(SearchActivity.this, this));
//            header.setButtonOverflowVisible(true);
            header.setButtonExpandVisible(true);

            addCardHeader(header);
            //expandable

            SearchExpand cardExpand = new SearchExpand(getBaseContext());
            addCardExpand(cardExpand);

            //Add Thumbnail
            VolleyCardThumbnail thumbnail = new VolleyCardThumbnail(getContext());
            thumbnail.setUrlResource(mData.getPoster());
            addCardThumbnail(thumbnail);
            thumbnail.setupImagePopup();

            addPartialOnClickListener(Card.CLICK_LISTENER_ALL_VIEW, new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    startActivity(new Intent(getBaseContext(), DetailsActivity.class).putExtra(DetailsActivity.ARG_SERIES_ID, mData.getId()));
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView status = (TextView) view.findViewById(R.id.card_main_inner_status);
            status.setText(mData.getStatus());
            if (!"ended".equalsIgnoreCase(mData.getStatus()) && mData.getAirsTime() != null) {
                status.setText(String.format("%s %s %s", mData.getAirsTime(), mData.getAirsDayOfWeek(), mData.getNetwork()));
            }
            if (!TextUtils.isEmpty(mData.getFirstAired())) {
                TextView first_aired = (TextView) view.findViewById(R.id.card_main_inner_first_aired);
                first_aired.setText(mData.getFirstAired().substring(0, 4));
            }
            TextView genre = (TextView) view.findViewById(R.id.card_main_inner_genre);
            genre.setText(mData.getGenres());

            RatingBar rating = (RatingBar) view.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);
            rating.setRating(mData.getRating() / 2);

            if (getCardThumbnail() != null) {
                getCardThumbnail().setUrlResource(mData.getPoster());
            }
        }

        public class SimpleBirthAnimation extends TwoCardOverlayAnimation {

            public SimpleBirthAnimation(Context context, Card card) {
                super(context, card);
            }

            @Override
            protected CardInfoToAnimate setCardToAnimate(Card card) {
                TwoCardToAnimate info = new TwoCardToAnimate() {
                    @Override
                    public int getLayoutIdToAdd() {
                        return R.layout.search_card_overflow_add_to_favorites;
                    }
                };
                return info;
            }
        }

        private class SearchExpand extends CardExpand {
            public SearchExpand(Context context) {
                super(context, R.layout.search_card_expand);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View view) {
                TextView overview = (TextView) view.findViewById(R.id.card_expand_inner_overview);
                overview.setText(mData.getOverview());
                TextView actors = (TextView) view.findViewById(R.id.card_expand_inner_actors);
                actors.setText(mData.getActors());
            }
        }

    }


}