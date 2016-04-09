package cn.marco.meizhi.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.GankRepository;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.view.LoadMoreRecyclerView;

public abstract class BaseCategoryActivity extends BaseSwipeBackActivity implements CategoryContract.CategoryView {

    protected CategoryContract.Presenter mCategoryPresenter;
    protected int mPageNumber = 1;
    protected boolean mIsLoading = false;
    protected LoadMoreRecyclerView mCategoryRecyclerView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryPresenter = new CategoryPresenter(this, GankRepository.getInstance());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getActionBarTitle());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpRecyclerView();

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(mCategoryPresenter::refresh);
        }

        mCategoryPresenter.start();
    }

    protected abstract String getActionBarTitle();

    protected void setUpRecyclerView() {
        mCategoryRecyclerView = initRecyclerView();
        mCategoryRecyclerView.setOnLoadMoreListener(() -> {
            if (!mIsLoading && mPageNumber < C.number.max_page_number) {
                mIsLoading = true;
                mCategoryPresenter.loadMore(++mPageNumber);
            }
            else if (mPageNumber >= C.number.max_page_number) {
                noMoreData();
            }
        });
    }

    protected abstract LoadMoreRecyclerView initRecyclerView();

    protected abstract void noMoreData();

    @Override public void loadMoreResults(List<Result> results) {
        mIsLoading = false;
        addDataSource(results);
    }

    @Override public void loadMoreResultsFail(Throwable throwable) {
        mIsLoading = false;
        if(mPageNumber < C.number.max_page_number){
            mPageNumber -- ;
        }
        Utils.showToast(throwable.getMessage());
    }

    @Override public void displayError(String errorMessage) {
        Utils.showToast(errorMessage);
    }

    protected abstract void addDataSource(List<Result> results);
}
