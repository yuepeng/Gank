package cn.marco.meizhi.data.source.remote;

import cn.marco.meizhi.data.entry.DailyData;
import cn.marco.meizhi.data.entry.Data;
import cn.marco.meizhi.data.entry.History;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GankService {

    @GET("data/{type}/{pageSize}/{pageNumber}")
    Observable<Data> getData(
            @Path("type") String type,
            @Path("pageSize") int pageSize,
            @Path("pageNumber") int pageNumber
    );

    @GET("day/{year}/{month}/{day}")
    Observable<DailyData> getEverydayData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day
    );

    @GET("day/history")
    Observable<History> getHistory();

}
