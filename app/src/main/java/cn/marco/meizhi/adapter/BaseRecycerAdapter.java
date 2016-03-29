package cn.marco.meizhi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseRecycerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;

    protected int mHeaderViewLayoutId = -1;
    protected int mFooterViewLayoutId = -1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(mHeaderViewLayoutId, parent, false);
            return new HeaderViewHolder(headerView);
        } else if(viewType == TYPE_FOOTER) {
            View footerView = LayoutInflater.from(parent.getContext()).inflate(mFooterViewLayoutId, parent, false);
            return new FooterViewHolder(footerView);
        }

        return onChildCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            return;
        }

        if (holder instanceof FooterViewHolder) {
            return;
        }

        onChildBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        int itemCount = getDataSourceCount();
        if (this.mHeaderViewLayoutId != -1) {
            itemCount++;
        }

        if (this.mFooterViewLayoutId != -1) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderViewLayoutId != -1) {
            return TYPE_HEADER;
        }

        if (mFooterViewLayoutId != -1) {
            if (mHeaderViewLayoutId != -1 && position == getDataSourceCount() + 1) {
                return TYPE_FOOTER;
            }

            if (mHeaderViewLayoutId == -1 && position == getDataSourceCount()) {
                return TYPE_FOOTER;

            }
        }

        return TYPE_NORMAL;
    }

    public abstract int getDataSourceCount();

    public abstract void onChildBindViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onChildCreateViewHolder(ViewGroup parent, int viewType);

    public void setHeaderView(int layoutId) {
        mHeaderViewLayoutId = layoutId;
    }

    public void setFooterView(int layoutId) {
        mFooterViewLayoutId = layoutId;
    }

    public void removeHeaderView() {
        if(mHeaderViewLayoutId != -1) {
            mHeaderViewLayoutId = -1;
            notifyDataSetChanged();
        }
    }

    public void removeFooterView() {
        if(mFooterViewLayoutId != -1){
            mFooterViewLayoutId = -1;
            notifyDataSetChanged();
        }
    }


    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
