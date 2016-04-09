package cn.marco.meizhi.data.source;

import java.util.List;

import cn.marco.meizhi.C;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.local.GankLocalDataSource;
import cn.marco.meizhi.data.source.local.GankLocalDataSourceImpl;
import cn.marco.meizhi.data.source.remote.GankRemoteDataSourceImpl;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GankRepository implements GankDataSource {

    private GankDataSource mRemoteDataSource;
    private GankLocalDataSource mLocalDataSource;

    private GankRepository() {
        mRemoteDataSource = new GankRemoteDataSourceImpl();
        mLocalDataSource = new GankLocalDataSourceImpl();
    }

    private static class SingletonHolder {
        private static GankRepository sInstance = new GankRepository();
    }

    public static GankRepository getInstance() {
        return SingletonHolder.sInstance;
    }

    public static void destoryInstance() {
        SingletonHolder.sInstance = null;
    }

    @Override
    public Observable<List<Result>> getDailyResults() {
        Observable<List<Result>> local = mLocalDataSource.getDailyResults();
        Observable<List<Result>> remote = mRemoteDataSource.getDailyResults()
                .doOnNext(results -> mLocalDataSource.saveResults(C.category.main, results));

        return Observable.concat(local, remote)
                .first()
                .compose(applySchedule());
    }

    @Override
    public Observable<List<Result>> getCategoryResults(String category) {
        Observable<List<Result>> local = mLocalDataSource.getCategoryResults(category);
        Observable<List<Result>> remote = mRemoteDataSource.getCategoryResults(category)
                .doOnNext(results -> mLocalDataSource.saveResults(category, results));

        return Observable.concat(local, remote)
                .first()
                .compose(applySchedule());
    }

    @Override
    public Observable<List<Result>> getCategoryResults(String category, int pageNumber) {
        return this.mRemoteDataSource.getCategoryResults(category, pageNumber).compose(applySchedule());
    }

    public <T> Observable.Transformer<T, T> applySchedule() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
