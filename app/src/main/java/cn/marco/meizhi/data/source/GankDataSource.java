package cn.marco.meizhi.data.source;

import java.util.List;

import cn.marco.meizhi.data.entry.Result;
import rx.Observable;

public interface GankDataSource {

    Observable<List<Result>> getDailyResults();

    Observable<List<Result>> getCategoryResults(String category);

    Observable<List<Result>> getCategoryResults(String category, int pageNumber);

}
