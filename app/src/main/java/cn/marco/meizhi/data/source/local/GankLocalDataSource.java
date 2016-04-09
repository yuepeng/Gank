package cn.marco.meizhi.data.source.local;

import java.util.List;

import cn.marco.meizhi.data.entry.Result;
import rx.Observable;

public interface GankLocalDataSource{

    Observable<List<Result>> getDailyResults();

    Observable<List<Result>> getCategoryResults(String category);

    void saveResults(String category, List<Result> results);
}
