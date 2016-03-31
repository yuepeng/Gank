package cn.marco.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;
import cn.marco.meizhi.util.XLog;

public class PictureActivity extends BaseSwipeBackActivity {

    private String mUrl;
    private String mDesc;

    protected static Intent getStartIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(KEY_DESC, desc);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_picture;
    }

    @Override
    public String getToolbarTitle() {
        mDesc = getIntent().getStringExtra(KEY_DESC);
        if(TextUtils.isEmpty(mDesc)){
            mDesc = System.currentTimeMillis()+"";
        }
        return mDesc;
    }

    @Override
    public void initViews() {
        super.initViews();
        mUrl = getIntent().getStringExtra(KEY_URL);
        ImageView imageView = (ImageView) findViewById(R.id.ivMeizhi);
        ViewCompat.setTransitionName(imageView, "picture");
        Picasso.with(this).load(mUrl).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizhi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.action_save) {
            saveMeizhiToSdCard();
        }
        return true;
    }

    private void saveMeizhiToSdCard() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Utils.showToast(getString(R.string.info_save_meizhi_error));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                XLog.i("onPrepareLoad...");
            }
        };

        Picasso.with(this).load(mUrl).into(target);
    }

}
