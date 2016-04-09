package cn.marco.meizhi.module;

import java.util.List;

import cn.marco.meizhi.data.entry.Result;

public interface CategoryContract {

    interface CategoryView extends BaseView {

        void displayResults(List<Result> results);

        void displayError(String errorMessage);

        void loadMoreResults(List<Result> results);

        void loadMoreResultsFail(Throwable throwable);

        String getCategory();

    }

    interface Presenter extends BasePresenter {

        void refresh();

        void loadMore(int pageNumber);

    }

}
