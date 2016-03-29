package cn.marco.meizhi.domain;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SpCache {

    public long date;
    public String cacheData;

    public SpCache() {
    }

    public SpCache(long date, String cacheData) {
        this.date = date;
        this.cacheData = cacheData;
    }

    public boolean isNeedToRefresh() {
        if (this.date == -1) {
            return true;
        }

        if (isSameDate()) { // 同一天，不刷新数据
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1 || week == 7) {   // 周末不刷新数据~
            return false;
        }

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {    // 十二点之前，照样不刷新数据
            return false;
        }
        return true;
    }

    private boolean isSameDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforeDate = sdf.format(date);
        String currentDate = sdf.format(System.currentTimeMillis());
        return TextUtils.equals(beforeDate, currentDate);
    }

}
