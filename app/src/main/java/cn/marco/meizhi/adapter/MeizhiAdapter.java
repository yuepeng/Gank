package cn.marco.meizhi.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import cn.marco.meizhi.R;
import cn.marco.meizhi.domain.Result;

public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.ViewHolder>{

    private Context mContext;
    private List<Result> mWelfares;
    private OnMeizhiClickListener mListener;

    public MeizhiAdapter(Context context){
        this.mContext = context;
    }

    public void setWelfares(List<Result> welfares){
        this.mWelfares = welfares;
        this.notifyDataSetChanged();
    }

    public void addDataSource(List<Result> welfares){
        if(welfares != null){
            this.mWelfares.addAll(welfares);
            this.notifyDataSetChanged();
        }
    }


    public void update(List<Result> welfares){
        this.mWelfares = welfares;
        this.notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_meizhi, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result welfare = this.mWelfares.get(position);
        // 使用Picasso加载图片
        Picasso.with(mContext)
                .load(welfare.url)
//                .transform(new Transformation() {
//                    @Override
//                    public Bitmap transform(Bitmap source) {
//                        int width = source.getWidth();
//                        int height = source.getHeight();
//                        Matrix matrix = new Matrix();
//                        matrix.postScale(0.5f, 0.5f);
//                        Bitmap result = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
//                        if (result != source) {
//                            source.recycle();
//                        }
//                        return result;
//                    }
//
//                    @Override
//                    public String key() {
//                        return welfare.url;
//                    }
//                })
                .into(holder.ivMeizhi);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onMeizhiClick(v, welfare);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWelfares == null ? 0 : mWelfares.size();
    }

    public void setOnMeizhiClickListener(OnMeizhiClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView ivMeizhi;

        public ViewHolder(View itemView) {
            super(itemView);
            ivMeizhi = (ImageView) itemView.findViewById(R.id.ivMeizhi);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnMeizhiClickListener {
        void onMeizhiClick(View view, Result result);
    }

}