package dev.okhotny.TVCalendar.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.ui.fragment.NavigationDrawerFragment;

public class BaseActivity extends ActionBarActivity implements BaseActivityOnScrollListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_drawer_layout);

        mToolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(mToolbar);

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, (ViewGroup) findViewById(R.id.container), true);
    }

    @Override
    public void onScroll(int dx, int dy) {
        float translationY = mToolbar.getTranslationY() - dy;
        if (translationY > 0) {
            translationY = 0;
        } else if (translationY < -mToolbar.getBottom()) {
            translationY = -mToolbar.getBottom();
        }

        mToolbar.setTranslationY(translationY);
    }

    public Toolbar getToolbarBar() {
        return mToolbar;
    }
}
