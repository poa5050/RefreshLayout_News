package com.demo.flow.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.demo.flow.adapter.SwipeRecyclerViewAdapter;
import com.demo.flow.engine.DataEngine;
import com.demo.flow.model.News;
import com.demo.flow.news.R;
import com.demo.flow.util.ToastUtil;
import com.demo.flow.widget.Divider;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewHolder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 *
 */
public class SwipeRecyclerViewFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private SwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView mDataRv;
    private int mNewPageNumber = 0;
    private int mMorePageNumber = 0;
    private boolean has_more;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_recyclerview);
        mRefreshLayout = getViewById(R.id.rl_recyclerview_refresh);
        mDataRv = getViewById(R.id.rv_recyclerview_data);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setDelegate(this);

        mAdapter = new SwipeRecyclerViewAdapter(mDataRv);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderView(mApp), false);
        BGAMoocStyleRefreshViewHolder refreshViewHolder = new BGAMoocStyleRefreshViewHolder(mApp, true);
        refreshViewHolder.setUltimateColor(getResources().getColor(R.color.colorPrimaryDark));
        refreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.custom_mooc_icon));
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        mDataRv.addItemDecoration(new Divider(mApp));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mApp);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mDataRv.setAdapter(mAdapter);
    }

    @Override
    protected void onUserVisible() {
        mNewPageNumber = 1;
        mMorePageNumber = 1;
        mLoadingDialog.show();
        DataEngine.loadInitDatas(new DataEngine.NewsModelResponseHandler() {
            @Override
            public void onFailure() {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onSuccess(List<News> newsModels, boolean hasMore) {
                mLoadingDialog.dismiss();
                has_more = hasMore;
                mDataRv.removeAllViews();
                mDataRv.smoothScrollToPosition(0);
                mAdapter.clear();
                mAdapter.setDatas(newsModels);
            }
        });
        //onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mNewPageNumber = 1;
        mMorePageNumber = 1;
        /*
        if (mNewPageNumber > 4) {
            mRefreshLayout.endRefreshing();
            showToast("没有最新数据了");
            return;
        }
        */
        //ToastUtil.show(mNewPageNumber+"");
        DataEngine.loadNewData(mNewPageNumber, new DataEngine.NewsModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(List<News> newsModels,boolean hasMore) {
                has_more = hasMore;
                mRefreshLayout.endRefreshing();
                mAdapter.clear();
                mAdapter.addNewDatas(newsModels);
                mDataRv.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (!has_more) {
            mRefreshLayout.endLoadingMore();
            showToast("没有更多数据了");
            return false;
        }else{
            mMorePageNumber++;
        }
        //ToastUtil.show(mMorePageNumber+"");
        DataEngine.loadMoreData(mMorePageNumber, new DataEngine.NewsModelResponseHandler() {
            @Override
            public void onFailure() {
                mRefreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(List<News> newsModels,boolean hasMore) {
                has_more = hasMore;
                mRefreshLayout.endLoadingMore();
                mAdapter.addMoreDatas(newsModels);
            }
        });
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        /*
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            mAdapter.closeOpenedSwipeItemLayoutWithAnim();
            mAdapter.removeItem(position);
        }
        */
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        /*
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).getTitle());
            return true;
        }
        */
        return false;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        showToast("点击了条目 " + mAdapter.getItem(position).getTitle());
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
        showToast("长按了条目 " + mAdapter.getItem(position).getTitle());
        return true;
    }
}