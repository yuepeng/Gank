package cn.marco.meizhi.module.vedio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.view.OnRVItemClickListener;
import cn.marco.meizhi.view.BaseRecyclerAdapter;

public class VideoAdapter extends BaseRecyclerAdapter {

    private List<Result> mResults;
    private OnRVItemClickListener mListener;

    public void setDataSource(List<Result> results) {
        if(results != null){
            this.mResults = results;
            this.notifyDataSetChanged();
        }
    }

    public void addDataSource(List<Result> results) {
        if(results != null){
            this.mResults.addAll(results);
            this.notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getDataSourceCount() {
        return mResults == null ? 0 : mResults.size();
    }

    @Override
    public void onChildBindViewHolder(RecyclerView.ViewHolder rHolder, int position) {
        Result result = mResults.get(position);
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vedio, parent, false);
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
