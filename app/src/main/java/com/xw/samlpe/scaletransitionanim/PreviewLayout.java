package com.xw.samlpe.scaletransitionanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * PreviewLayout
 * <p/>
 * Created by woxingxiao on 2016-10-24.
 */
public class PreviewLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    public static final long ANIM_DURATION = 300;

    private View mBackgroundView;
    private HackyViewPager mViewPager;
    private ImageView mScalableImageView;

    private List<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
    private int mIndex;
    private Rect mStartBounds = new Rect();
    private Rect mFinalBounds = new Rect();

    public PreviewLayout(Context context) {
        this(context, null);
    }

    public PreviewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_preview, this, true);
        mBackgroundView = findViewById(R.id.background_view);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mScalableImageView = (ImageView) findViewById(R.id.scalable_image_view);

        mScalableImageView.setPivotX(0f);
        mScalableImageView.setPivotY(0f);
        mScalableImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void setData(List<ThumbViewInfo> list, int index) {
        if (list == null || list.isEmpty() || index < 0) {
            return;
        }

        this.mThumbViewInfoList = list;
        this.mIndex = index;
        mStartBounds = mThumbViewInfoList.get(mIndex).getBounds();

        post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(new ImagePagerAdapter());
                mViewPager.setCurrentItem(mIndex);
                mViewPager.addOnPageChangeListener(PreviewLayout.this);

                mScalableImageView.setX(mStartBounds.left);
                mScalableImageView.setY(mStartBounds.top);
                Glide.with(getContext()).load(mThumbViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
            }
        });
    }

    public void triggerScaleUpAnimation() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Point globalOffset = new Point();
                getGlobalVisibleRect(mFinalBounds, globalOffset);
                mFinalBounds.offset(-globalOffset.x, -globalOffset.y);

                LayoutParams lp = new LayoutParams(
                        mStartBounds.width(),
                        mStartBounds.width() * mFinalBounds.height() / mFinalBounds.width()
                );
                mScalableImageView.setLayoutParams(lp);

                startAnim();

                if (Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mStartBounds = mThumbViewInfoList.get(mIndex).getBounds();
        if (mStartBounds == null) {
            return;
        }

        computeStartScale();
    }

    private void startAnim() {
        float startScale = computeStartScale();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mBackgroundView, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left, mFinalBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top, mFinalBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f / startScale),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f / startScale));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mViewPager.setAlpha(0f);
                mScalableImageView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mViewPager.setAlpha(1f);
                mScalableImageView.setVisibility(View.INVISIBLE);
            }
        });
        animatorSet.start();
    }

    private float computeStartScale() {
        float startScale;
        if ((float) mFinalBounds.width() / mFinalBounds.height()
                > (float) mStartBounds.width() / mStartBounds.height()) {
            // Extend start bounds horizontally （以竖直方向为参考缩放）
            startScale = (float) mStartBounds.height() / mFinalBounds.height();
            float startWidth = startScale * mFinalBounds.width();
            float deltaWidth = (startWidth - mStartBounds.width()) / 2;
            mStartBounds.left -= deltaWidth;
            mStartBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically （以水平方向为参考缩放）
            startScale = (float) mStartBounds.width() / mFinalBounds.width();
            float startHeight = startScale * mFinalBounds.height();
            float deltaHeight = (startHeight - mStartBounds.height()) / 2;
            mStartBounds.top -= deltaHeight;
            mStartBounds.bottom += deltaHeight;
        }

        return startScale;
    }

    public void triggerScaleDownAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Glide.with(getContext()).load(mThumbViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
                mScalableImageView.setVisibility(View.VISIBLE);
                mViewPager.setAlpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FrameLayout contentContainer = (FrameLayout) ((Activity) getContext()).findViewById(android.R.id.content);
                contentContainer.removeView(PreviewLayout.this);
            }
        });
        animatorSet.start();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mThumbViewInfoList != null ? mThumbViewInfoList.size() : 0;
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(getContext());

            Glide.with(getContext()).load(mThumbViewInfoList.get(position).getUrl()).into(photoView);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    triggerScaleDownAnimation();
                }
            });

            container.addView(photoView);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
