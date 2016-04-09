package cn.marco.meizhi.module.other;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import cn.marco.meizhi.module.BaseSwipeBackActivity;
import cn.marco.meizhi.C;
import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;

public class PictureActivity extends BaseSwipeBackActivity {

    private String mUrl;
    private String mDesc;

    @Override protected int getContentView() {
        return R.layout.activity_picture;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getIntent().getStringExtra(C.extra.url);
        mDesc = getIntent().getStringExtra(C.extra.desc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(mDesc);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView imageView = (ImageView) findViewById(R.id.ivMeizhi);
        ViewCompat.setTransitionName(imageView, C.extra.picture);
        Picasso.with(this).load(mUrl).into(imageView);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.action_save) {
            saveToSdCard();
        }
        return true;
    }

    private void saveToSdCard() {
        Target target = new Target() {

            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(()->{
                    File meizhiDir = new File(Environment.getExternalStorageDirectory(), "MeiZhi");
                    if(!meizhiDir.exists()) {
                        meizhiDir.mkdir();
                    }
                    File file = new File(meizhiDir + File.separator + mDesc + ".jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();
                        Looper.prepare();
                        Utils.showToast(getString(R.string.info_save_meizhi_success));
                        Looper.loop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();
            }

            @Override public void onBitmapFailed(Drawable errorDrawable) {
                Utils.showToast(getString(R.string.info_save_meizhi_error));
            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this).load(mUrl).into(target);
    }

}
