package cn.marco.meizhi.module.vedio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.module.BaseCategoryActivity;
import cn.marco.meizhi.util.ActivityRouter;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;

public class VideoActivity extends BaseCategoryActivity {

    private VideoAdapter mVideoAdapter;

    @Override protected int getContentView() {
        return R.layout.activity_rest_vedio;
    }

    @Override protected String getActionBarTitle() {
        return getString(R.string.aty_video_title);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoAdapter.setOnItemClickListener((view, result) -> ActivityRouter.gotoGankDetail(this, result));
    }

    @Override protected LoadMoreRecyclerView initRecyclerView() {
        LoadMoreRecyclerView videoRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoRecyclerView.setAdapter(mVideoAdapter = new VideoAdapter());
        return videoRecyclerView;
    }

    @Override protected void noMoreData() {
        mVideoAdapter.removeFooterView();
        Utils.showToast(getString(R.string.info_no_more_data));
    }

    @Override public void displayResults(List<Result> results) {
        mVideoAdapter.setFooterView(R.layout.view_loading);
        mVideoAdapter.setDataSource(results);
    }

    @Override protected void addDataSource(List<Result> results) {
        mVideoAdapter.addDataSource(results);
    }

    @Override public String getCategory() {
        return C.category.video;
    }
}
