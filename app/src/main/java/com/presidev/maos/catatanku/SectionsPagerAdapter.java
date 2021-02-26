package com.presidev.maos.catatanku;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.presidev.maos.R;
import com.presidev.maos.catatanku.quotes.QuotesFragment;
import com.presidev.maos.catatanku.target.view.TargetFragment;
import com.presidev.maos.catatanku.reminder.view.ReminderFragment;

import org.jetbrains.annotations.NotNull;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_title_1,
            R.string.tab_title_2,
            R.string.tab_title_3
    };

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new TargetFragment();
            case 1: return new ReminderFragment();
            case 2: return new QuotesFragment();
            default: return new Fragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
