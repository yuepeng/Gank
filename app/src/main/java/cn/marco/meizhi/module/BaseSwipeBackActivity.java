package cn.marco.meizhi.module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.marco.meizhi.R;
import cn.marco.meizhi.view.SlidingPaneLayout;

public abstract class BaseSwipeBackActivity extends BaseActivity{

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingPaneLayout paneLayout = (SlidingPaneLayout) findViewById(R.id.slidingPaneLayout);
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

    @Override public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_right);
    }
}
