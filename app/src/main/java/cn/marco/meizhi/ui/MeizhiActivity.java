package cn.marco.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;

import static cn.marco.meizhi.net.GankApiService.TYPE_WELFARE;

import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cn.marco.meizhi.R;
import cn.marco.meizhi.adapter.MeizhiAdapter;
import cn.marco.meizhi.domain.Data;
import cn.marco.meizhi.domain.Result;
import cn.marco.meizhi.net.GankApi;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;
import rx.Observable;
import rx.Subscription;

public class MeizhiActivity extends BaseSwipeBackActivity implements MeizhiAdapter.OnMeizhiClickListener {

    public static final int MAX_PAGE_SIZE = 10;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MeizhiActivity.class);
        return intent;
    }

    private LoadMoreRecyclerView mMeizhiRecyclerView;
    private MeizhiAdapter mMeizhiAdapter;
    private int mPageNumber = 1;
    private boolean mIsLoading = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_meizhi;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.aty_meizhi_title);
    }

    @Override
    public void initViews() {
        super.initViews();
        tryShowRefresh();
        mMeizhiRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        mMeizhiRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMeizhiRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mMeizhiAdapter = new MeizhiAdapter(this);
        mMeizhiAdapter.setOnMeizhiClickListener(this);
        mMeizhiRecyclerView.setAdapter(mMeizhiAdapter);

        tryShowRefresh();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> loadData());
        }

        mMeizhiRecyclerView.setOnLoadMoreListener(() -> {
            if (!mIsLoading && mPageNumber < MAX_PAGE_SIZE) {
                mIsLoading = true;
                GankApi.getInstance().getData(TYPE_WELFARE, ++mPageNumber)
                        .compose(GankApi.getInstance().applySchedule())
                        .subscribe(data -> {
                            mIsLoading = false;
                            mMeizhiAdapter.addDataSource(data);
                        }, onError);
            } else {
                Utils.showToast(getString(R.string.info_no_more_data));
            }
        });
    }

    @Override
    public void loadData() {
        Observable<List<Result>> cacheDatas = GankApi.getInstance().getDiskCache(TYPE_WELFARE);
        Observable<List<Result>> network = GankApi.getInstance().getData(TYPE_WELFARE)
                .doOnNext(results -> GankApi.getInstance().handleResult(TYPE_WELFARE, results));

        Subscription subscribe = Observable.concat(cacheDatas, network)
                .first()
                .compose(GankApi.getInstance().applySchedule())
                .subscribe(results -> {
                    tryStopRefresh();
                    mMeizhiAdapter.setWelfares(results);
                }, onError);
        this.addSubscription(subscribe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_right);
    }

    @Override
    public void onMeizhiClick(View view, Result result) {
        Intent intent = PictureActivity.getStartIntent(MeizhiActivity.this, result.url, result.desc);
        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "picture");
        ActivityCompat.startActivity(this, intent, option.toBundle());
    }
}
