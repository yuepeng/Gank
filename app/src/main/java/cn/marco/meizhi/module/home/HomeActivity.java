package cn.marco.meizhi.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cn.marco.meizhi.module.BaseActivity;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.GankRepository;
import cn.marco.meizhi.util.ActivityRouter;
import cn.marco.meizhi.util.Utils;


public class HomeActivity extends BaseActivity implements HomeContract.HomeView {

    private long startTime;
    private HomeAdapter mHomeAdapter;
    private HomeContract.Presenter mHomePresenter;

    @Override protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHomePresenter = new HomePresenter(this, GankRepository.getInstance());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpRecyclerView();

        if (this.mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(mHomePresenter::refresh);
        }
        this.mHomePresenter.start();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mHomeAdapter = new HomeAdapter(this));
        mHomeAdapter.setItemClickListener(mHomePresenter::onItemClick);
    }

    @Override public void displayResults(List<Result> results) {
        mHomeAdapter.setDataSource(results);
    }


    @Override public void displayError(String errorMessage) {
        Utils.showToast(errorMessage);
    }

    @Override public void gotoGankDetail(Result result) {
        ActivityRouter.gotoGankDetail(this, result);
    }

    @Override public void gotoBeauty(View view, Result result) {
        ActivityRouter.gotoBeauty(this, view, result);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_ganhuo) {
            ActivityRouter.gotoCategory(this);
        }
        else if (id == R.id.nav_welfare) {
            ActivityRouter.gotoWelfare(this);
        }
        else if (id == R.id.nav_vedio) {
            ActivityRouter.gotoVedio(this);
        }
        else if (id == R.id.nav_about) {
            ActivityRouter.gotoAbout(this);
        }
        return true;
    }

    @Override public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime <= 1500) {
            super.onBackPressed();
        }
        else {
            startTime = System.currentTimeMillis();
            Utils.showToast(getString(R.string.aty_home_exit));
        }
    }

    @Override protected void onDestroy() {
        this.mHomePresenter.destroy();
        super.onDestroy();
    }
}
