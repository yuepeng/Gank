package cn.marco.meizhi.ui;

import android.graphics.Color;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.lang.reflect.Field;
import cn.marco.meizhi.R;

public abstract class BaseSwipeBackActivity extends BaseActivity {

    public abstract String getToolbarTitle();
    protected Toolbar mToolbar;

    @Override
    public void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null){
            mToolbar.setTitle(getToolbarTitle());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupSlidingPaneLayout();
    }

    protected void setupSlidingPaneLayout() {
        SlidingPaneLayout paneLayout = (SlidingPaneLayout) findViewById(R.id.slidingPaneLayout);
        if (paneLayout != null) {
            try {
                Field overHangSizeField = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                overHangSizeField.setAccessible(true);
                overHangSizeField.set(paneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            paneLayout.setShadowResourceLeft(R.drawable.drawer_shadow);
            paneLayout.setSliderFadeColor(Color.TRANSPARENT);
            paneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                }

                @Override
                public void onPanelOpened(View panel) {
                    finish();
                }

                @Override
                public void onPanelClosed(View panel) {
                }
            });
        }
    }

    @Override
    public void loadData() {}

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_right);
    }

}
