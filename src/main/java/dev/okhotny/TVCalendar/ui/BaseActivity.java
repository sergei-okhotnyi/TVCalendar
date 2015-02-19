package dev.okhotny.TVCalendar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import dev.okhotny.TVCalendar.R;

public class BaseActivity extends ActionBarActivity implements BaseActivityOnScrollListener {

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
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Tv Shows"),
                        new PrimaryDrawerItem().withName("Calendar").withIcon(FontAwesome.Icon.faw_calendar).setEnabled(false).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.my_shows).withIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp)).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.discover).withIcon(getResources().getDrawable(R.drawable.abc_ic_search_api_mtrl_alpha)).withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(4),
                        new SecondaryDrawerItem().withName("contact").withIcon(FontAwesome.Icon.faw_bullhorn).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch ((int) id) {
                            case 1:
                                break;
                            case 2:
                                startActivity(new Intent(BaseActivity.this, MainActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(BaseActivity.this, TrendingActivity.class));
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                        }
                    }
                })
                .build();
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
