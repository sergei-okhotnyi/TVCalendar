package dev.okhotny.TVCalendar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.ui.fragment.TvShowListFragment;

public class TrendingActivity extends BaseActivity {

    private TvShowListFragment mSessionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        mSessionsFragment = (TvShowListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mSessionsFragment.showTrending();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}