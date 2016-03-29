package cn.marco.meizhi.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.marco.meizhi.net.GankApiService.*;
import cn.marco.meizhi.R;
import cn.marco.meizhi.adapter.CategoryViewPagerAdapter;
import cn.marco.meizhi.ui.fragment.CategoryFragment;

public class CategoryActivity extends BaseActivity {

    public static final String KEY_CATEGORY = "Category";

    private ViewPager mCategoryViewPager;

    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context, CategoryActivity.class);
        return intent;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_category;
    }

    @Override
    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(TYPE_ANDROID));
        tabLayout.addTab(tabLayout.newTab().setText(TYPE_IOS));
        tabLayout.addTab(tabLayout.newTab().setText(TYPE_WEB_FRONT));
        tabLayout.addTab(tabLayout.newTab().setText(TYPE_EXPAND_RESOURCE));

        mCategoryViewPager = (ViewPager) findViewById(R.id.vpCategory);
        mCategoryViewPager.setOffscreenPageLimit(4);
        List<CategoryFragment> categoryFragments = new ArrayList<>();
        categoryFragments.add(CategoryFragment.newInstance(TYPE_ANDROID));
        categoryFragments.add(CategoryFragment.newInstance(TYPE_IOS));
        categoryFragments.add(CategoryFragment.newInstance(TYPE_WEB_FRONT));
        categoryFragments.add(CategoryFragment.newInstance(TYPE_EXPAND_RESOURCE));
        CategoryViewPagerAdapter categoryAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), categoryFragments);
        categoryAdapter.setTitles(Arrays.asList(TYPE_ANDROID, TYPE_IOS, TYPE_WEB_FRONT, TYPE_EXPAND_RESOURCE));
        mCategoryViewPager.setAdapter(categoryAdapter);
        tabLayout.setupWithViewPager(mCategoryViewPager);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_right);
    }

    @Override
    public void loadData() { }

}
