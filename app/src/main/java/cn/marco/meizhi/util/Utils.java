package cn.marco.meizhi.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.gson.Gson;
import cn.marco.meizhi.GankApplication;
import cn.marco.meizhi.domain.SpCache;

public final class Utils {

    public static final String SP_NAME = "GankIO";
    private final static Gson sGson = new Gson();

    public static void showToast(String message) {
        Toast.makeText(GankApplication.sContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void saveObjToSPFile(String type, SpCache spCache) {
        SharedPreferences sp = GankApplication.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sGson.toJson(spCache);
        sp.edit().putString(type, json).commit();
    }

    public static SpCache getObjFromSPFile(String type) {
        SharedPreferences sp = GankApplication.sContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(type, null);
        if(TextUtils.isEmpty(json)){
            return null;
        }
        return sGson.fromJson(json, SpCache.class);
    }

    public static <T> T parseFromJson(String json, Class<T> clazz){
        return sGson.fromJson(json, clazz);
    }

    public static String serializerToJson(Object obj){
        return sGson.toJson(obj);
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

}
