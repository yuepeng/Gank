package cn.marco.meizhi.module.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.GankRepository;
import cn.marco.meizhi.module.CategoryContract;
import cn.marco.meizhi.module.CategoryPresenter;
import cn.marco.meizhi.util.ActivityRouter;
import cn.marco.meizhi.util.Logger;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryFragment extends Fragment implements CategoryContract.CategoryView {

    private String mCategory;
    private int mPageNumber = 1;
    private CategoryContract.Presenter mCategoryPresenter;
    private boolean mIsLoading = false;
    private CategoryAdapter mCategoryAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static CategoryFragment newInstance(String category) {
        CategoryFragment categoryFragment = new CategoryFragment();
        CategoryContract.Presenter presenter = new CategoryPresenter(categoryFragment, GankRepository.getInstance());
        categoryFragment.mCategoryPresenter = presenter;
        categoryFragment.mCategory = category;
        return categoryFragment;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        setUpRecyclerView(view);
        mCategoryPresenter.start();
        return view;
    }

    private void setUpRecyclerView(View view) {
        final LoadMoreRecyclerView categoryRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryRecyclerView.setAdapter(mCategoryAdapter = new CategoryAdapter());
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        categoryRecyclerView.setOnLoadMoreListener(() -> {
            Logger.i("mIsLoading = " + mIsLoading + " , mPageNumber = " + mPageNumber);
            if (!mIsLoading && mPageNumber < C.number.max_page_number) {
                mIsLoading = true;
                mCategoryPresenter.loadMore(++mPageNumber);
            }
            else if (mPageNumber >= C.number.max_page_number) {
                mCategoryAdapter.removeFooterView();
                Utils.showToast(getString(R.string.info_no_more_data));
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(mCategoryPresenter::refresh);
        mCategoryAdapter.setOnItemClickListener((itemView, result) -> ActivityRouter.gotoGankDetail(getActivity(), result));
    }

    @Override public void displayResults(List<Result> results) {
        mCategoryAdapter.setFooterView(R.layout.view_loading);
        mCategoryAdapter.setDataSource(results);
    }


    @Override public void loadMoreResults(List<Result> results) {
        mIsLoading = false;
        mCategoryAdapter.addDataSource(results);
    }

    @Override public void displayError(String errorMessage) {
        Utils.showToast(errorMessage);
    }

    @Override public void loadMoreResultsFail(Throwable throwable) {
        mIsLoading = false;
        if (mPageNumber < C.number.max_page_number) {
            mPageNumber--;
        }
        Utils.showToast(throwable.getMessage());
    }

    @Override public String getCategory() {
        return mCategory;
    }

    @Override public void startLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(() -> {
                mSwipeRefreshLayout.setRefreshing(true);
            });
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

    @Override public void onDestroy() {
        this.mCategoryPresenter.destroy();
        super.onDestroy();
    }
}