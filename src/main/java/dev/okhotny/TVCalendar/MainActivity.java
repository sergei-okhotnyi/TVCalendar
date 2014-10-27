package dev.okhotny.TVCalendar;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        startActivity(new Intent(this, ShowDetailsActivity.class).putExtra("tvshow", show));
    }
}
