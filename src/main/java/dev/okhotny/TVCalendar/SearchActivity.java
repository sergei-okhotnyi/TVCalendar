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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Series;
import dev.okhotny.TVCalendar.providers.thetvdb.TheTVDBApi;
import dev.okhotny.TVCalendar.providers.trakttv.TraktTvApi;

public class SearchActivity extends Activity {

    private SearchView mSearchView;
    private String mSearchString;
    private ListView mGrid;
    private View mProgress;
    private TextView mEmpty;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.search);
        setTitle(R.string.search);

        mGrid = (ListView) findViewById(android.R.id.list);
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
//                        ArrayList<Card> cards = new ArrayList<Card>(result.size());
//                        for (final Series s : result) {
//                            cards.add(new SeriesCard(SearchActivity.this, s));
//                        }
//                        CardArrayAdapter adapter = new CardArrayAdapter(SearchActivity.this, cards);
//                        mGrid.setAdapter(adapter);
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
//                        ArrayList<Card> cards = new ArrayList<Card>(result.size());
//                        for (final Series s : result) {
//                            cards.add(new SeriesCard(SearchActivity.this, s));
//                        }
//                        CardArrayAdapter adapter = new CardArrayAdapter(SearchActivity.this, cards);
//                        mGrid.setAdapter(adapter);
                        mGrid.requestFocus();
                    }
                }
            });
        }
    }


}