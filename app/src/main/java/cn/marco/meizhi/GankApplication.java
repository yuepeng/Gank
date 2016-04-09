package cn.marco.meizhi;

import android.app.Application;
import android.content.Context;
import com.litesuits.orm.LiteOrm;

public class GankApplication extends Application {

    public static Context sContext;
    public static LiteOrm sLiteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sLiteOrm = LiteOrm.newSingleInstance(this, C.global.database);
    }
}
