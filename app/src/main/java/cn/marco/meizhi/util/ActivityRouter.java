package cn.marco.meizhi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import cn.marco.meizhi.C;
import cn.marco.meizhi.data.entry.Result;
import cn.marco.meizhi.module.other.AboutActivity;
import cn.marco.meizhi.module.vedio.VideoActivity;
import cn.marco.meizhi.module.category.CategoryActivity;
import cn.marco.meizhi.module.meizhi.MeizhiActivity;
import cn.marco.meizhi.module.other.PictureActivity;
import cn.marco.meizhi.module.other.WebViewActivity;

public final class ActivityRouter {

    public static void gotoGankDetail(Context context, Result result) {
        Intent intent = createIntent(context, WebViewActivity.class);
        fillIntentExtra(intent, result);
        context.startActivity(intent);
    }

    public static void gotoBeauty(Activity context, View view, Result result) {
        Intent intent = createIntent(context, PictureActivity.class);
        fillIntentExtra(intent, result);
        if (view != null) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, C.extra.picture);
            ActivityCompat.startActivity(context, intent, option.toBundle());
            return;
        }
        context.startActivity(intent);
    }

    public static void gotoWelfare(Context context) {
        context.startActivity(createIntent(context, MeizhiActivity.class));
    }

    public static void gotoCategory(Context context) {
        context.startActivity(createIntent(context, CategoryActivity.class));
    }

    public static void gotoAbout(Context context) {
        context.startActivity(createIntent(context, AboutActivity.class));
    }

    public static void gotoVedio(Context context) {
        context.startActivity(createIntent(context, VideoActivity.class));
    }

    private static Intent createIntent(Context context, Class<? extends Activity> clazz) {
        return new Intent(context, clazz);
    }

    private static void fillIntentExtra(Intent intent, Result result) {
        if (result != null) {
            intent.putExtra(C.extra.url, result.url);
            intent.putExtra(C.extra.desc, result.desc);
        }
    }

}
