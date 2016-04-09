package cn.marco.meizhi.module.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.marco.meizhi.module.BaseActivity;
import cn.marco.meizhi.C;
import cn.marco.meizhi.R;

public class CategoryActivity extends BaseActivity {

    @Override protected int getContentView() {
        return R.layout.activity_category;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(C.category.android));
        tabLayout.addTab(tabLayout.newTab().setText(C.category.ios));
        tabLayout.addTab(tabLayout.newTab().setText(C.category.front));
        tabLayout.addTab(tabLayout.newTab().setText(C.category.resource));
        tabLayout.addTab(tabLayout.newTab().setText(C.category.recommend));

        ViewPager categoryViewPager = (ViewPager) findViewById(R.id.vpCategory);
        categoryViewPager.setOffscreenPageLimit(3);

        List<CategoryFragment> categoryFragments = new ArrayList<>();
        categoryFragments.add(CategoryFragment.newInstance(C.category.android));
        categoryFragments.add(CategoryFragment.newInstance(C.category.ios));
        categoryFragments.add(CategoryFragment.newInstance(C.category.front));
        categoryFragments.add(CategoryFragment.newInstance(C.category.resource));
        categoryFragments.add(CategoryFragment.newInstance(C.category.recommend));

        CategoryViewPagerAdapter categoryAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), categoryFragments);
        categoryAdapter.setTitles(Arrays.asList(
                        C.category.android, C.category.ios, C.category.front,
                        C.category.resource, C.category.recommend));

        categoryViewPager.setAdapter(categoryAdapter);
        tabLayout.setupWithViewPager(categoryViewPager);
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.out_right);
    }

}
