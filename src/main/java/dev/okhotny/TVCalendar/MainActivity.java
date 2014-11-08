package dev.okhotny.TVCalendar;

import android.content.Intent;
import android.os.Bundle;

import com.jakewharton.trakt.entities.TvShow;


public class MainActivity extends BaseActivity implements TrendingFragment.OpenShowCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.my_shows);
    }


    @Override
    public void openShow(TvShow show) {
        startActivity(new Intent(this, ShowDetailsActivity.class).putExtra("tvdbid", show.tvdb_id));
    }
}
