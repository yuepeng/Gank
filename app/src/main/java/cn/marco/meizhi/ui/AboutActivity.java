package cn.marco.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cn.marco.meizhi.R;
import cn.marco.meizhi.util.Utils;

public class AboutActivity extends BaseSwipeBackActivity {

    private CollapsingToolbarLayout mCollapsingToolBar;

    public static Intent getStartIntent(Context context){
        return new Intent(context, AboutActivity.class);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.aty_about_title);
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolBar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSlidingPaneLayout();

        TextView tvIntroduce = (TextView) findViewById(R.id.tvIntroduce);
        tvIntroduce.setMovementMethod(LinkMovementMethod.getInstance());
        tvIntroduce.setText(Html.fromHtml(getString(R.string.aty_about_introduction)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Utils.share(this, getString(R.string.aty_about_share_txt));
                break;
        }
        return true;
    }


    @Override
    public void loadData() { }
}
