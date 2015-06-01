package dev.okhotny.TVCalendar.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FullSizeImageView extends ImageView {

    public FullSizeImageView(Context context) {
        super(context);
    }

    public FullSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullSizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullSizeImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                setMeasuredDimension(0, 0);
            } else {
                int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
                int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (measuredHeight == 0 && measuredWidth == 0) { //Height and width set to wrap_content
                    setMeasuredDimension(measuredWidth, measuredHeight);
                } else if (measuredHeight == 0) { //Height set to wrap_content
                    int height = measuredWidth * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                    setMeasuredDimension(measuredWidth, height);
                } else if (measuredWidth == 0) { //Width set to wrap_content
                    int width = measuredHeight * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
                    setMeasuredDimension(width, measuredHeight);
                } else { //Width and height are explicitly set (either to match_parent or to exact value)
                    setMeasuredDimension(measuredWidth, measuredHeight);
                }
            }
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
