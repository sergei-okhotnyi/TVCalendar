package dev.okhotny.TVCalendar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private final Random random = new Random();
    private ArrayList<NetworkImageView> mImageViews = new ArrayList<NetworkImageView>(0);
    private int mActiveImageIndex = -1;
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            swapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
        }
    };

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

    public void setResourceIds(String... resourceIds) {
        for (String url : resourceIds) {
            NetworkImageView imageView = new NetworkImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView, 0);
            mImageViews.add(imageView);
            imageView.setImageUrl(url, App.sInstance.imageLoader);
        }
        swapImage();
    }

    public void setResourceIds(List<String> urls) {
        for (String url : urls) {
            NetworkImageView imageView = new NetworkImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAlpha(0.0f);
            addView(imageView);
            mImageViews.add(imageView);
            imageView.setImageUrl(url, App.sInstance.imageLoader);
        }
        swapImage();
    }

    private void swapImage() {
        if (mImageViews.size() == 0) {
            return;
        }
        if (mActiveImageIndex == -1 || mImageViews.size() == 1) {
            mActiveImageIndex = 0;
            animate(mImageViews.get(mActiveImageIndex));
            return;
        }

        int inactiveIndex = mActiveImageIndex;
        mActiveImageIndex = (++mActiveImageIndex) % mImageViews.size();

        final ImageView activeImageView = mImageViews.get(mActiveImageIndex);
        activeImageView.setAlpha(0.0f);
        ImageView inactiveImageView = mImageViews.get(inactiveIndex);

        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
                ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)
        );
        animatorSet.start();
    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
        propertyAnimator.start();
    }

    private float pickScale() {
        float maxScaleFactor = 1.5F;
        float minScaleFactor = 1.2F;
        return minScaleFactor + this.random.nextFloat() * (maxScaleFactor - minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        float fromScale = pickScale();
        float toScale = pickScale();
        float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
        float toTranslationX = pickTranslation(view.getWidth(), toScale);
        float toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startKenBurnsAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    private void startKenBurnsAnimation() {
        mHandler.post(mSwapImageRunnable);
    }


}
