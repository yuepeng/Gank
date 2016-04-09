package cn.marco.meizhi.module.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.view.OnRVItemClickListener;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Result> mDailyResults;
    private Context mContext;
    private OnRVItemClickListener mListener;

    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataSource(List<Result> dailyResults) {
        this.mDailyResults = dailyResults;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(mContext, R.layout.item_daily, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = this.mDailyResults.get(position);
        if (TextUtils.equals(result.type, C.category.welfare)) {
            holder.ivMeizhi.setVisibility(View.VISIBLE);
            holder.categoryView.setVisibility(View.GONE);
            Picasso.with(mContext).load(result.url).into(holder.ivMeizhi);
        } else {
            holder.ivMeizhi.setVisibility(View.GONE);
            holder.categoryView.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(result.type);
            holder.tvAuthor.setText(result.who);
            holder.tvDate.setText(result.publishedAt.split("T")[0]);
        }

        holder.tvDesc.setText(result.desc);
        holder.ivCategoryIcon.setImageResource(getCategoryIcon(result.type));

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, result);
            }
        });

    }

    public void setItemClickListener(OnRVItemClickListener listener) {
        this.mListener = listener;
    }

    private int getCategoryIcon(String type) {
        switch (type) {
            case C.category.android:
                return R.mipmap.icon_android;
            case C.category.ios:
                return R.mipmap.icon_apple;
            case C.category.front:
                return R.mipmap.icon_chrome;
            case C.category.resource:
                return R.mipmap.icon_resource;
            case C.category.video:
                return R.mipmap.icon_media;
            case C.category.recommend:
                return R.mipmap.icon_recommend;
        }
        return R.mipmap.icon_android;
    }

    @Override
    public int getItemCount() {
        return mDailyResults == null ? 0 : mDailyResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMeizhi;
        public ImageView ivCategoryIcon;
        public TextView tvCategory;
        public TextView tvAuthor;
        public TextView tvDate;
        public TextView tvDesc;
        public View categoryView;
        public View itemView;

        public ViewHolder(View convertView) {
            super(convertView);
            ivMeizhi = (ImageView) convertView.findViewById(R.id.ivMeizhi);
            ivCategoryIcon = (ImageView) convertView.findViewById(R.id.ivCategoryIcon);
            categoryView = convertView.findViewById(R.id.llCategory);
            tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
            tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            itemView = convertView.findViewById(R.id.llDaily);
        }
    }
}
