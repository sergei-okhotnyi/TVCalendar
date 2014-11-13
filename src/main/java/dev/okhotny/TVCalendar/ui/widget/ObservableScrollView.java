package dev.okhotny.TVCalendar.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by sergii on 14-11-12.
 */
public class ObservableScrollView extends ScrollView {

    private ArrayList<OnScrollChangedListener> mCallbacks = new ArrayList<OnScrollChangedListener>();

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        for (OnScrollChangedListener c : mCallbacks) {
            c.onScrollChanged(l - oldl, t - oldt);
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        if (!mCallbacks.contains(listener)) {
            mCallbacks.add(listener);
        }
    }

    public static interface OnScrollChangedListener {
        public void onScrollChanged(int deltaX, int deltaY);
    }
}
