package cn.marco.meizhi.net;

import cn.marco.meizhi.domain.Data;
import cn.marco.meizhi.domain.DailyData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GankApiService {

    String HOST = "http://gank.io/api/";
    String TYPE_ANDROID  = "Android";
    String TYPE_WELFARE  = "福利";
    String TYPE_IOS  = "iOS";
    String TYPE_REST_VEDIO  = "休息视频";
    String TYPE_EXPAND_RESOURCE  = "拓展资源";
    String TYPE_WEB_FRONT  = "前端";
    String TYPE_RECOMMEND = "瞎推荐";
    String TYPE_MAIN = "首页";

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

}
