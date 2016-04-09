package cn.marco.meizhi.data.source.local;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.GankApplication;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.util.Logger;
import rx.Observable;

public class GankLocalDataSourceImpl implements GankLocalDataSource {

    @Override
    public Observable<List<Result>> getDailyResults() {
        return this.loadDataSourceFromDB(C.category.main);
    }

    @Override
    public Observable<List<Result>> getCategoryResults(String category) {
        return this.loadDataSourceFromDB(category);
    }

    @Override
    public void saveResults(String category, List<Result> results) {
        if (results != null && results.size() > 0) {
            for (int i = 0, len = results.size(); i < len; i++) {
                results.get(i).category = category;
            }
            GankApplication.sLiteOrm.delete(WhereBuilder.create(Result.class).where("category = ?", new String[]{category}));
            GankApplication.sLiteOrm.insert(results);
        }
    }

    private Observable<List<Result>> loadDataSourceFromDB(String category){
        return Observable.create(subscriber -> {
            QueryBuilder queryBuilder = new QueryBuilder(Result.class);
            queryBuilder.where("category = ?", new String[]{category});
            ArrayList<Result> cacheResults = GankApplication.sLiteOrm.query(queryBuilder);

            if (cacheResults == null || cacheResults.size() == 0) {
                subscriber.onCompleted();
                return;
            }

            Result cacheResult = cacheResults.get(0);
            if (Utils.isNeedToRefresh(cacheResult.publishedAt)) {
                subscriber.onCompleted();
                return;
            }

            Logger.i("使用缓存数据...");
            subscriber.onNext(cacheResults);
        });
    }
}
