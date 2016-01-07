package com.demo.flow.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.flow.model.News;
import com.demo.flow.news.R;
import com.demo.flow.util.ImageLoaderUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Activity activity;
    private View.OnClickListener onClickListener;
    private List<News> newsList;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_swipe,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle().length() > 25 ? news.getTitle().substring(0,25) + "..." : news.getTitle());
        holder.time.setText(news.getDatetime());
        if(news.getAbstractNews() != null && !news.getAbstractNews().isEmpty()) {
            holder.detail.setText(news.getAbstractNews());
            holder.detail.setVisibility(View.VISIBLE);
        }
        if(news.getImg_url() != null && !news.getImg_url().isEmpty()) {
            ImageLoaderUtil.imageLoader(holder.img, news.getImg_url());
        }
        holder.itemView.setTag(news);
    }

    public void addNewDatas(List<News> datas) {
        if (datas != null) {
            newsList.addAll(0, datas);
            notifyItemRangeInserted(0, datas.size());
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public News getItem(int position) {
        return newsList.get(position);
    }

    public void addMoreDatas(List<News> datas) {
        if (datas != null) {
            newsList.addAll(newsList.size(), datas);
            notifyItemRangeInserted(newsList.size(), datas.size());
        }
    }

    public void setDatas(List<News> datas){
        if (datas != null) {
            newsList = datas;
        } else {
            newsList.clear();
        }
        notifyDataSetChanged();
    }

    public void clear(){
        if(newsList != null) {
            newsList.clear();
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView time;
        public TextView detail;
        public ImageView img;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(onClickListener);
            this.title = (TextView)view.findViewById(R.id.tv_item_swipe_title);
            this.time = (TextView)view.findViewById(R.id.tv_item_swipe_time);
            this.detail = (TextView)view.findViewById(R.id.tv_item_swipe_detail);
            this.img = (ImageView)view.findViewById(R.id.tv_item_swipe_image);
        }
    }
}
