package cn.marco.meizhi;

import android.app.Application;
import android.content.Context;

public class GankApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
