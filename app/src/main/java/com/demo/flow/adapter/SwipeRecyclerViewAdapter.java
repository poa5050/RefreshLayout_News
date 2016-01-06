package com.demo.flow.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.demo.flow.model.News;
import com.demo.flow.news.R;
import com.demo.flow.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

/**
 *
 */
public class SwipeRecyclerViewAdapter extends BGARecyclerViewAdapter<News> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public SwipeRecyclerViewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_swipe);
    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_swipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        //viewHolderHelper.setItemChildClickListener(R.id.tv_item_swipe_delete);
        //viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_swipe_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, News model) {
        //Log.e("news",model.toString());
        viewHolderHelper.setText(R.id.tv_item_swipe_title, model.getTitle()).setText(R.id.tv_item_swipe_time,model.getDatetime());
        //ImageLoader.getInstance().displayImage(model.getImg_url(), (ImageView)viewHolderHelper.getView(R.id.tv_item_swipe_image), ImageLoaderUtil.displayOptions);
        TextView mDetail = (TextView) viewHolderHelper.getView(R.id.tv_item_swipe_detail);
        if(model.getAbstractNews() != null && !model.getAbstractNews().isEmpty()){
            mDetail.setText(model.getAbstractNews());
            mDetail.setVisibility(View.VISIBLE);
        }

        ImageView imageView = (ImageView) viewHolderHelper.getView(R.id.tv_item_swipe_image);
        if(model.getImg_url() != null && !model.getImg_url().isEmpty()) {
            ImageLoaderUtil.displayImg(imageView, model.getImg_url());
        }
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

}