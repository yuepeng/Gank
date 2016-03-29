package cn.marco.meizhi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

import cn.marco.meizhi.ui.fragment.CategoryFragment;

public class CategoryViewPagerAdapter extends FragmentPagerAdapter {

    private List<CategoryFragment> mCategoryFragments;
    private List<String> mCategorys;

    public CategoryViewPagerAdapter(FragmentManager fm, List<CategoryFragment> categoryFragments) {
        super(fm);
        this.mCategoryFragments = categoryFragments;
    }

    public void setTitles(List<String> categorys){
        this.mCategorys = categorys;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mCategoryFragments == null ? null : mCategoryFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mCategoryFragments == null ? 0 : mCategoryFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mCategorys == null) {
            return super.getPageTitle(position);
        }
        return mCategorys.get(position);
    }
}
