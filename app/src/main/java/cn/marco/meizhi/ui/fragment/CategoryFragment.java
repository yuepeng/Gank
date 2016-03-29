package cn.marco.meizhi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.marco.meizhi.R;
import cn.marco.meizhi.adapter.CategoryAdapter;
import cn.marco.meizhi.domain.Data;
import cn.marco.meizhi.domain.Result;
import cn.marco.meizhi.listener.OnRVItemClickListener;
import cn.marco.meizhi.net.GankApi;
import cn.marco.meizhi.net.GankApiService;
import cn.marco.meizhi.ui.CategoryActivity;
import cn.marco.meizhi.ui.WebViewActivity;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CategoryFragment extends Fragment implements OnRVItemClickListener {

    public static final int MAX_PAGE_SIZE = 10;

    public static CategoryFragment newInstance(String type) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(CategoryActivity.KEY_CATEGORY, type);
        fragment.setArguments(args);
        return fragment;
    }

    private String mType;
    private int mPageNumber = 1;
    private boolean mIsLoading = false;
    private LoadMoreRecyclerView mCategoryRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(CategoryActivity.KEY_CATEGORY);
        if (TextUtils.isEmpty(mType)) {
            mType = GankApiService.TYPE_ANDROID;
        }
        mCompositeSubscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initViews(view);
        loadData();
        return view;
    }

    private void initViews(View view) {
        mCategoryRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setOnItemClickListener(this);
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> loadData());

        mCategoryRecyclerView.setOnLoadMoreListener(() -> {
            if (!mIsLoading && mPageNumber < MAX_PAGE_SIZE) {
                mIsLoading = true;
                Subscription subscribe = GankApi.getInstance().getData(mType, ++mPageNumber)
                        .compose(GankApi.getInstance().applySchedule())
                        .subscribe(dataResult -> {
                            mIsLoading = false;
                            mCategoryAdapter.addDataSource(dataResult.results);
                        });
                mCompositeSubscription.add(subscribe);
            } else {
                mCategoryAdapter.removeFooterView();
                Utils.showToast(getString(R.string.info_no_more_data));
            }
        });
    }

    private void loadData() {
        Observable<Data> diskCache = GankApi.getInstance().getDiskCache(mType, Data.class);
        Observable<Data> network = GankApi.getInstance().getNetwork(mType, GankApi.getInstance().getData(mType));
        Subscription subscription = Observable.concat(diskCache, network)
                .first()
                .compose(GankApi.getInstance().applySchedule())
                .subscribe(dataResult -> {
                    mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
                    mCategoryAdapter.setFooterView(R.layout.view_loading);
                    mCategoryAdapter.setDataSource(dataResult.results);
                }, throwable -> Utils.showToast(throwable.getMessage()));
        this.mCompositeSubscription.add(subscription);
    }

    @Override
    public void onItemClick(View view, Result result) {
        Intent intent = WebViewActivity.getStartIntent(getActivity(), result.url, result.desc);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}