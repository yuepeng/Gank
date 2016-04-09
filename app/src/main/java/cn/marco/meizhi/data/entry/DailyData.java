package cn.marco.meizhi.data.entry;

import java.util.ArrayList;
import java.util.List;

public class DailyData {

    public List<String> category;
    public boolean error;
    public DailyResult results;

    public List<Result> getResults() {
        List<Result> newResults = null;
        if (results != null) {
            newResults = new ArrayList<>();
            if (results.welfareList != null) {
                newResults.addAll(results.welfareList);
            }

            if (results.androidList != null) {
                newResults.addAll(results.androidList);
            }

            if (results.iOSList != null) {
                newResults.addAll(results.iOSList);
            }

            if (results.webFrontEndList != null) {
                newResults.addAll(results.webFrontEndList);
            }

            if (results.expandResList != null) {
                newResults.addAll(results.expandResList);
            }

            if (results.blindRecommendList != null) {
                newResults.addAll(results.blindRecommendList);
            }

            if (results.restVedioList != null) {
                newResults.addAll(results.restVedioList);
            }
        }
        return newResults;
    }

}
