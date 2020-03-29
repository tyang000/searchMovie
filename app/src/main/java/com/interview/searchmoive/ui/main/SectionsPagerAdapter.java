package com.interview.searchmoive.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.interview.searchmoive.R;

/**
 * View pager adapter displays searchMoveFragment and FavoriteMovieFragment
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.search_tab_title, R.string.favorite_tab_title};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchMovieFragment.newInstance();
            case 1:
                return FavoriteMovieFragment.newInstance();
            default:
                // we should only have two tabs, crash early.
                throw new RuntimeException("Unexpected tab index " + position);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}