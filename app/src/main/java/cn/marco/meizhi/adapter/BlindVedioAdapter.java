package cn.marco.meizhi.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.marco.meizhi.R;
import cn.marco.meizhi.domain.Result;
import cn.marco.meizhi.listener.OnRVItemClickListener;
import cn.marco.meizhi.net.GankApiService;

public class BlindVedioAdapter extends BaseRecycerAdapter {

    private List<Result> mBlindVedioResults;
    private OnRVItemClickListener mListener;
    private String mType;

    public void setType(String type){
        this.mType = type;
    }

    public void setDataSource(List<Result> blindVedioResults) {
        if(blindVedioResults != null){
            this.mBlindVedioResults = blindVedioResults;
            this.notifyDataSetChanged();
        }
    }

    public void addDataSource(List<Result> vedioList) {
        if(vedioList != null){
            this.mBlindVedioResults.addAll(vedioList);
            this.notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getDataSourceCount() {
        return mBlindVedioResults == null ? 0 : mBlindVedioResults.size();
    }

    @Override
    public void onChildBindViewHolder(RecyclerView.ViewHolder rHolder, int position) {
        Result result = mBlindVedioResults.get(position);
        ViewHolder holder = (ViewHolder) rHolder;
        holder.tvDesc.setText(result.desc);
        holder.tvDate.setText(result.publishedAt.split("T")[0]);
        holder.tvAuthor.setText(result.who);
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, result);
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onChildCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = TextUtils.equals(mType, GankApiService.TYPE_REST_VEDIO) ? R.layout.item_vedio : R.layout.item_category;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDesc;
        TextView tvAuthor;
        TextView tvDate;
        View itemView;

        public ViewHolder(View convertView) {
            super(convertView);
            tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            itemView = convertView.findViewById(R.id.llItemView);

        }
    }

}
