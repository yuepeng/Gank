package cn.marco.meizhi.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.marco.meizhi.GankApplication;

public final class Utils {

    public static final int[] MONTHS = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static void showToast(String message) {
        Toast.makeText(GankApplication.sContext, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNeedToRefresh(String publishAt) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        try {
            Date date = simple.parse(publishAt);
            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            if (hour < 12) {
                return false;
            }

            if (week == 1 || week == 7) {
                return false;
            }

            if (DateUtils.isToday(date.getTime())) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return true;
        }
    }

    public static void share(Context context, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    public static void copyToClipBoard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("复制", text));
        showToast("复制成功~");
    }

    public static void openWithBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            showToast("没有可以打开的浏览器哦~");
        }
    }

    public static int[] getDate() {
        int[] date = new int[3];
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (week == 1) {
            day -= 2;
        } else if (week == 7) {
            day -= 1;
        } else if (hour < 12) {
            day--;
        }

        if (day <= 0) {
            // 进行日期修正 获取上个月的最后一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        date[0] = year;
        date[1] = month + 1;
        date[2] = day;
        return date;
    }
}
