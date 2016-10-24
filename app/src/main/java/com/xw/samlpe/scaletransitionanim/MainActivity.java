package com.xw.samlpe.scaletransitionanim;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mContentContainer;
    private PreviewLayout mPreviewLayout;

    private List<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
    private LinearLayout.LayoutParams mLayoutParams;
    private int mStatusBarHeight;
    private int mSolidWidth = 0;
    private int mSolidHeight = 0;
    private int mSpanCnt = 3;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mStatusBarHeight = getResources().getDimensionPixelSize(resId);

        mContentContainer = (FrameLayout) findViewById(android.R.id.content);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        mLayoutParams = new LinearLayout.LayoutParams(metrics.widthPixels / 3, metrics.widthPixels / 3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_left_32dpdp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<String> urls = ImageUrlConfig.getUrls();

        for (int i = 0; i < urls.size(); i++) {
            mThumbViewInfoList.add(new ThumbViewInfo(urls.get(i), i));
        }

        mGridLayoutManager = new GridLayoutManager(this, mSpanCnt);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BaseQuickAdapter<String>(R.layout.item_pic_thumb, urls) {
            @Override
            protected void convert(final BaseViewHolder holder, final String url) {
                holder.itemView.setLayoutParams(mLayoutParams);

                final ImageView thumbView = holder.getView(R.id.thumb_iv);
                Glide.with(MainActivity.this)
                        .load(url)
                        .into(thumbView);
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                mPreviewLayout = new PreviewLayout(MainActivity.this);
                mPreviewLayout.setData(mThumbViewInfoList, position);
                mPreviewLayout.triggerScaleUpAnimation();
                mContentContainer.addView(mPreviewLayout);
            }
        });

        recyclerView.addOnScrollListener(new MyRecyclerOnScrollListener());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            assembleDataList();
        }
    }

    private class MyRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                assembleDataList();
            }
        }
    }

    private void assembleDataList() {
        computeBoundsForward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());

        computeBoundsBackward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    /**
     * 从第一个完整可见item顺序遍历
     */
    private void computeBoundsForward(int firstCompletelyVisiblePos) {
        int topOffset = 0;

        for (int i = firstCompletelyVisiblePos; i < mThumbViewInfoList.size(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();

            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.thumb_iv);

                thumbView.getGlobalVisibleRect(bounds);

                if (mSolidWidth * mSolidHeight == 0) {
                    mSolidWidth = bounds.width();
                    mSolidHeight = bounds.height();
                }
                topOffset = mSolidHeight - bounds.height();

                bounds.set(
                        bounds.left + thumbView.getPaddingLeft(),
                        bounds.top + thumbView.getPaddingTop(),
                        bounds.left + mSolidWidth - thumbView.getPaddingLeft(),
                        bounds.top + mSolidHeight - thumbView.getPaddingBottom()
                );
            } else {
                bounds.left = i % 3 * mSolidWidth;
                bounds.top = i / mSpanCnt * mSolidHeight + topOffset;
                bounds.right = bounds.left + mSolidWidth;
                bounds.bottom = bounds.top + mSolidHeight;
            }
            bounds.offset(0, -mStatusBarHeight);

            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    /**
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        int bottomOffset = 0;

        for (int i = firstCompletelyVisiblePos - 1; i >= 0; i--) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();

            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.thumb_iv);

                thumbView.getGlobalVisibleRect(bounds);

                if (bottomOffset == 0) {
                    bottomOffset = bounds.bottom;
                }

                bounds.set(
                        bounds.left + thumbView.getPaddingLeft(),
                        bounds.bottom - mSolidHeight + thumbView.getPaddingTop(),
                        bounds.right - thumbView.getPaddingLeft(),
                        bounds.bottom - thumbView.getPaddingBottom()
                );
            } else {
                bounds.left = i % 3 * mSolidWidth;
                bounds.top = -((firstCompletelyVisiblePos - i) / mSpanCnt * mSolidHeight) -
                        (mSolidHeight - bottomOffset);
                bounds.right = bounds.left + mSolidWidth;
                bounds.bottom = bounds.top + mSolidHeight;
            }
            bounds.offset(0, -mStatusBarHeight);

            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    @Override
    public void onBackPressed() {
        if (mContentContainer.getChildAt(mContentContainer.getChildCount() - 1) instanceof PreviewLayout) {
            mPreviewLayout.triggerScaleDownAnimation();
            return;
        }

        super.onBackPressed();
    }
}