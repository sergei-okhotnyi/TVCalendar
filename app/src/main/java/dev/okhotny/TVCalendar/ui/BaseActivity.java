package dev.okhotny.TVCalendar.ui;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import dev.okhotny.TVCalendar.R;

public class BaseActivity extends AppCompatActivity implements BaseActivityOnScrollListener {

    private Toolbar mToolbar;
    private int mToolbarElevation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mToolbarElevation = getResources().getDimensionPixelSize(R.dimen.toolbar_elevation);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbarBar();
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
        ViewCompat.setElevation(mToolbar, mToolbar.getY() >= 0 ? 0 : mToolbarElevation);
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
