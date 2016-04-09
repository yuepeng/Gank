package cn.marco.meizhi.module.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.view.OnRVItemClickListener;
import cn.marco.meizhi.view.BaseRecyclerAdapter;

public class CategoryAdapter extends BaseRecyclerAdapter {

    private List<Result> mDataResults;
    private OnRVItemClickListener mListener;


    public void setDataSource(List<Result> dataResults) {
        if (dataResults != null) {
            this.mDataResults = dataResults;
            this.notifyDataSetChanged();
        }
    }

    public void addDataSource(List<Result> dataResults) {
        if (dataResults != null) {
            this.mDataResults.addAll(dataResults);
            this.notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        this.mListener = listener;
    }

    @Override public int getDataSourceCount() {
        return this.mDataResults == null ? 0 : mDataResults.size();
    }

    @Override public void onChildBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result result = mDataResults.get(position);
        ((ViewHolder) holder).tvDesc.setText(result.desc);
        ((ViewHolder) holder).tvDate.setText(result.publishedAt.split("T")[0]);
        ((ViewHolder) holder).tvAuthor.setText(result.who);
        ((ViewHolder) holder).itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, result);
            }
        });
    }

    @Override public RecyclerView.ViewHolder onChildCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(parent.getContext(), R.layout.item_category, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView tvDesc;
        public TextView tvAuthor;
        public TextView tvDate;

        public ViewHolder(View convertView) {
            super(convertView);
            tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
            tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            itemView = convertView.findViewById(R.id.llItemView);
        }
    }

}
