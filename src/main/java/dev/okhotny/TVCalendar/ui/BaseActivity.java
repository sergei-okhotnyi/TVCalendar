package dev.okhotny.TVCalendar.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.ui.fragment.NavigationDrawerFragment;

public class BaseActivity extends ActionBarActivity implements BaseActivityOnScrollListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbarBar();
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        NavigationDrawerFragment drawer = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if (drawer != null) {
            drawer.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        }
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
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.action_toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

}
