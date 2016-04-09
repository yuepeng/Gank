package cn.marco.meizhi.module.home;

import android.view.View;
import java.util.List;
import cn.marco.meizhi.module.BasePresenter;
import cn.marco.meizhi.module.BaseView;
import cn.marco.meizhi.data.entry.Result;

public interface HomeContract {

    interface HomeView extends BaseView{

        void displayResults(List<Result> results);

        void displayError(String errorMessage);

        void gotoGankDetail(Result result);

        void gotoBeauty(View view, Result result);
    }

    interface Presenter extends BasePresenter{

        void refresh();

        void onItemClick(View view, Result result);

    }

}
