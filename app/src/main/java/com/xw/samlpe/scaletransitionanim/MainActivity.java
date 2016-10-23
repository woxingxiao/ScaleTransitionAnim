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

public class MainActivity extends AppCompatActivity {

    private FrameLayout mContentContainer;
    private PreviewLayout mPreviewLayout;

    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
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

        urls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        urls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        urls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");

        urls.add("http://imgcdn.thecover.cn/FlwmxD5lj9aZuAwIfNlTGeg4fbA6?imageMogr2/quality/80/ignore-error/1");
        urls.add("http://i0.itc.cn/20110124/8f7_98cce95e_ae0a_4712_8e65_1789fba80843_0.jpg");
        urls.add("http://simg314.magcasa.com/content_images/2015/10/22/162537/1445450806_8832.jpg");

        urls.add("http://img4.duitang.com/uploads/item/201307/02/20130702113059_UEGL2.jpeg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=985035006,79865976&fm=21&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=1774291582,2563335167&fm=21&gp=0.jpg");
        urls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        urls.add("http://img5.imgtn.bdimg.com/it/u=1511364704,3337189105&fm=21&gp=0.jpg");
        urls.add("http://img3.imgtn.bdimg.com/it/u=2144096677,2391514122&fm=21&gp=0.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");

        urls.add("http://img4.duitang.com/uploads/item/201307/02/20130702113059_UEGL2.jpeg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=985035006,79865976&fm=21&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=1774291582,2563335167&fm=21&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=1511364704,3337189105&fm=21&gp=0.jpg");
        urls.add("http://img3.imgtn.bdimg.com/it/u=2144096677,2391514122&fm=21&gp=0.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");
        urls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");

        urls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        urls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");
        urls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");

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