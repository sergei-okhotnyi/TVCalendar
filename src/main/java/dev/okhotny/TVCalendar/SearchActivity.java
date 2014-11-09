package dev.okhotny.TVCalendar;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SearchActivity extends BaseActivity {
    private SearchView mSearchView;
    private TrendingFragment mSessionsFragment;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setTitle(R.string.search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mSearchView != null) {
            mSearchView.setQuery(mQuery, false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView view = (SearchView) searchItem.getActionView();
            mSearchView = view;
            if (view != null) {
                view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                view.setIconified(false);
                view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        view.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if (null != mSessionsFragment) {
                            mSessionsFragment.requestQueryUpdate(s);
                        }
                        return true;
                    }
                });
                view.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        finish();
                        return false;
                    }
                });
                if (!TextUtils.isEmpty(mQuery)) {
                    view.setQuery(mQuery, false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
