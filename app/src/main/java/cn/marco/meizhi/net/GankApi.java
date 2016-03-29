package cn.marco.meizhi.net;

import java.util.Calendar;

import cn.marco.meizhi.domain.Data;
import cn.marco.meizhi.domain.DailyData;
import cn.marco.meizhi.domain.SpCache;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.util.XLog;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public Observable<DailyData> getEverydayData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        if (week == 1 ) {
            day -= 2;
        } else if (week == 7) {
            day -= 1;
        }

        return mApiService.getEverydayData(year, month + 1, day);
    }

    public <T> Observable.Transformer<T, T> applySchedule() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Data> getData(String type) {
        return getData(type, 0);
    }

    public Observable<Data> getData(String type, int pageNumber) {
        return mApiService.getData(type, PAGE_SIZE, pageNumber);
    }

    public <T> Observable<T> getDiskCache(final String type, final Class<T> clazz) {
        return Observable.create(subscriber -> {
            SpCache spCache = Utils.getObjFromSPFile(type);
            if (spCache == null || spCache.isNeedToRefresh()) {
                subscriber.onCompleted();
                return;
            }

            T t = Utils.parseFromJson(spCache.cacheData, clazz);
            if (t == null) {
                subscriber.onCompleted();
                return;
            }
            XLog.i("LoadData From Disk Cache, Type: " + type);
            subscriber.onNext(t);
        });
    }

    public <T> Observable<T> getNetwork(String type, Observable<T> observable) {
        return observable.doOnNext(tData ->
                        Utils.saveObjToSPFile(type, new SpCache(System.currentTimeMillis(), Utils.serializerToJson(tData)))
        );
    }
}
