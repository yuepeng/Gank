package cn.marco.meizhi.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import java.net.UnknownHostException;
import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.util.XLog;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String KEY_URL = "KEY_URL";
    public static final String KEY_DESC = "KEY_DESC";

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initVariable();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }

        initViews();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initVariable() {
    }

    public abstract void initViews();

    public abstract int getContentViewId();

    public abstract void loadData();

    public void tryShowRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        }
    }

    public void tryStopRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        }
    }

    public Action1<Throwable> onError = throwable -> {
        tryStopRefresh();
        if (throwable instanceof HttpException) {
            Utils.showToast(((HttpException) throwable).code() + "");
        } else if (throwable instanceof UnknownHostException) {
            Utils.showToast(getString(R.string.info_network_error));
        } else {
            Utils.showToast(throwable.getMessage());
        }
    };

    public void addSubscription(Subscription subscription) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
