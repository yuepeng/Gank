package cn.marco.meizhi.module;

import java.util.List;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.GankRepository;

public class CategoryPresenter implements CategoryContract.Presenter {

    private CategoryContract.CategoryView mCategoryView;
    private GankRepository mGankRepository;

    public CategoryPresenter(CategoryContract.CategoryView categoryView, GankRepository gankRepository) {
        this.mCategoryView = categoryView;
        this.mGankRepository = gankRepository;
    }

    @Override public void start() {
        this.loadData();
    }


    @Override public void refresh() {
        this.loadData();
    }

    @Override public void loadMore(int pageNumber) {
        this.mGankRepository.getCategoryResults(mCategoryView.getCategory(), pageNumber)
                .subscribe(mCategoryView::loadMoreResults, mCategoryView::loadMoreResultsFail);
    }

    private void loadData() {
        this.mCategoryView.startLoading();
        this.mGankRepository.getCategoryResults(mCategoryView.getCategory())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(List<Result> results) {
        this.mCategoryView.finishLoading();
        this.mCategoryView.displayResults(results);
    }

    private void onError(Throwable error) {
        this.mCategoryView.finishLoading();
        this.mCategoryView.displayError(error.getMessage());
    }

}
