package cn.marco.meizhi.net;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.marco.meizhi.GankApplication;
import cn.marco.meizhi.domain.Result;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.util.XLog;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.marco.meizhi.net.GankApiService.TYPE_MAIN;

public class GankApi {

    public static final int PAGE_SIZE = 20;

    private static class SingletonHolder {
        private static GankApi sInstance = new GankApi();
    }

    public static GankApi getInstance() {
        return SingletonHolder.sInstance;
    }

    private GankApiService mApiService;

    private GankApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GankApiService.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiService = retrofit.create(GankApiService.class);
    }

    public Observable<List<Result>> getEverydayData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (week == 1) {
            day -= 2;
        } else if (week == 7) {
            day -= 1;
        } else if (hour < 12){
            day --;
        }

        return mApiService.getEverydayData(year, month + 1, day)
                .map(dailyData -> dailyData.getResults())
                .doOnNext(results -> handleResult(TYPE_MAIN, results));
    }

    public Observable<List<Result>> getData(String type) {
        return getData(type, 0);
    }

    public Observable<List<Result>> getData(String category, int pageNumber) {
        return mApiService.getData(category, PAGE_SIZE, pageNumber).map(data -> data.results);
    }

    public void handleResult(String category, List<Result> results){
        if (results != null && results.size() > 0) {
            for (int i = 0, len = results.size(); i < len; i++) {
                results.get(i).category = category;
            }
            GankApplication.sLiteOrm.delete(WhereBuilder.create(Result.class).where("category = ?", new String[]{category}));
            GankApplication.sLiteOrm.insert(results);
        } else {
            // API 没返回新数据，使用DB缓存
            QueryBuilder queryBuilder = new QueryBuilder(Result.class);
            queryBuilder.where("category = ?", new String[]{category});
            results = GankApplication.sLiteOrm.query(queryBuilder);
        }
    }

    public <T> Observable.Transformer<T, T> applySchedule() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Result>> getDiskCache(final String category) {
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

            XLog.i("使用缓存数据...");
            subscriber.onNext(cacheResults);
        });
    }
}
