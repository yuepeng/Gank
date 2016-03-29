package cn.marco.meizhi.ui;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import static cn.marco.meizhi.net.GankApiService.*;
import cn.marco.meizhi.R;
import cn.marco.meizhi.adapter.DailyAdapter;
import cn.marco.meizhi.domain.DailyData;
import cn.marco.meizhi.net.GankApi;
import cn.marco.meizhi.net.GankApiService;

public class MainActivity extends BaseActivity {

    private DailyAdapter mDailyAdapter;
    private RecyclerView mDailyRecyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDailyRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mDailyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDailyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDailyAdapter = new DailyAdapter(this);
        mDailyRecyclerView.setAdapter(mDailyAdapter);

        mDailyAdapter.setItemClickListener((view, result) -> {
            if (TextUtils.equals(result.type, GankApiService.TYPE_WELFARE)) {
                startActivity(PictureActivity.getStartIntent(MainActivity.this, result.url, result.desc));
            } else {
                startActivity(WebViewActivity.getStartIntent(MainActivity.this, result.url, result.desc));
            }
        });

        tryShowRefresh();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> loadData());
        }
    }

    @Override
    public void loadData() {
        this.addSubscription(toSubscribe(TYPE_MAIN,
                DailyData.class,
                GankApi.getInstance().getEverydayData(),
                dailyData -> {
                    tryStopRefresh();
                    mDailyAdapter.setDataSource(dailyData.getResults());
                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_ganhuo) {
            intent = CategoryActivity.getStartIntent(this);
        } else if (id == R.id.nav_welfare) {
            intent = MeizhiActivity.getStartIntent(this);
        } else if (id == R.id.nav_vedio) {
            intent = BlindVedioActivity.getStartIntent(this, TYPE_REST_VEDIO);
        } else if (id == R.id.nav_recommend) {
            intent = BlindVedioActivity.getStartIntent(this, TYPE_RECOMMEND);
        } else if (id == R.id.nav_about) {
            intent = AboutActivity.getStartIntent(this);
        }
        if (intent != null) {
            startActivity(intent);
        }
        return true;
    }
}
