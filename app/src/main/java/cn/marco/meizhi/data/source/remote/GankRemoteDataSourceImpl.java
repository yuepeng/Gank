package cn.marco.meizhi.data.source.remote;

import java.util.List;
import cn.marco.meizhi.C;
import cn.marco.meizhi.data.entry.History;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.data.source.GankDataSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class GankRemoteDataSourceImpl implements GankDataSource{

    private GankService mApiService;

    public GankRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.global.host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiService = retrofit.create(GankService.class);
    }


    @Override
    public Observable<List<Result>> getDailyResults() {
        return this.mApiService.getHistory().flatMap(this::getNearlyDate);
    }

    @Override
    public Observable<List<Result>> getCategoryResults(String category) {
        return this.getCategoryResults(category, 0);
    }

    @Override
    public Observable<List<Result>> getCategoryResults(String category, int pageNumber) {
        return this.mApiService.getData(category, C.number.page_size, pageNumber).map(datas -> datas.results);
    }

    private Observable<List<Result>> getNearlyDate(History history){
        int[] nearDate = history.getNearDate();
        return this.mApiService.getEverydayData(nearDate[0], nearDate[1], nearDate[2])
                .map(dailyData -> dailyData.getResults());
    }
}
