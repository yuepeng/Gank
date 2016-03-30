package cn.marco.meizhi;

import android.app.Application;
import android.content.Context;
import com.litesuits.orm.LiteOrm;

/**
 * 缓存首页数据。
 * 其他页面的数据在当前12点之前只从缓存中取，下拉加载的内容不缓存
 */
public class GankApplication extends Application {

    public static final String GANK_DB = "Gank.db";
    public static Context sContext;
    public static LiteOrm sLiteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sLiteOrm = LiteOrm.newSingleInstance(this, GANK_DB);
    }
}
