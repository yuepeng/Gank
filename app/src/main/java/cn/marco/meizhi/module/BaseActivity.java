package cn.marco.meizhi.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Logger;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected abstract int getContentView();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        if (mSwipeRefreshLayout != null) {
            this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void startLoading() {
        if (this.mSwipeRefreshLayout != null) {
            this.mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        }
    }

    @Override public void finishLoading() {
        if (mSwipeRefreshLayout != null) {
            Observable.timer(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(time -> mSwipeRefreshLayout.setRefreshing(false));
        }
    }
}
