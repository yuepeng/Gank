package cn.marco.meizhi.module.meizhi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.module.BaseCategoryActivity;
import cn.marco.meizhi.module.CategoryContract;
import cn.marco.meizhi.util.ActivityRouter;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;

public class MeizhiActivity extends BaseCategoryActivity implements CategoryContract.CategoryView {

    private MeizhiAdapter mMeizhiAdapter;

    @Override protected int getContentView() {
        return R.layout.activity_meizhi;
    }

    @Override protected String getActionBarTitle() {
        return getString(R.string.aty_meizhi_title);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeizhiAdapter.setOnMeizhiClickListener((view, result) -> ActivityRouter.gotoBeauty(this, view, result));
    }

    @Override protected LoadMoreRecyclerView initRecyclerView() {
        LoadMoreRecyclerView meizhiRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        meizhiRecyclerView.setItemAnimator(new DefaultItemAnimator());
        meizhiRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        meizhiRecyclerView.setAdapter(mMeizhiAdapter = new MeizhiAdapter(this));
        return meizhiRecyclerView;
    }

    @Override protected void noMoreData() {
        Utils.showToast(getString(R.string.info_no_more_data));
    }

    @Override protected void addDataSource(List<Result> results) {
        mMeizhiAdapter.addDataSource(results);
    }

    @Override public void displayResults(List<Result> results) {
        mMeizhiAdapter.setWelfares(results);
    }

    @Override public String getCategory() {
        return C.category.welfare;
    }
}
