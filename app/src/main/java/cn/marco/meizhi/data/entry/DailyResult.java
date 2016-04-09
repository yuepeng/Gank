package cn.marco.meizhi.data.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyResult {

    @SerializedName("Android")
    public List<Result> androidList;

    @SerializedName("iOS")
    public List<Result> iOSList;

    @SerializedName("前端")
    public List<Result> webFrontEndList;

    @SerializedName("休息视频")
    public List<Result> restVedioList;

    @SerializedName("福利")
    public List<Result> welfareList;

    @SerializedName("拓展资源")
    public List<Result> expandResList;

    @SerializedName("瞎推荐")
    public List<Result> blindRecommendList;


}
