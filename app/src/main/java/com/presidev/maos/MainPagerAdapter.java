package com.presidev.maos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.presidev.maos.bookmark.view.BookmarkFragment;
import com.presidev.maos.dashboard.view.DashboardFragment;
import com.presidev.maos.profile.mitra.MitraProfileFragment;
import com.presidev.maos.profile.user.UserProfileFragment;

import static com.presidev.maos.utils.Constants.LEVEL_MITRA;
import static com.presidev.maos.utils.Constants.LEVEL_USER;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private final String userLevel;

    public MainPagerAdapter(String userLevel, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.userLevel = userLevel;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DashboardFragment();

            case 1:
                if (userLevel == null) return new ProfileFragment();
                else if (userLevel.equals(LEVEL_MITRA) || userLevel.equals(LEVEL_USER)) return new BookmarkFragment();

            case 2:
                if (userLevel == null) return new ProfileFragment();
                else if (userLevel.equals(LEVEL_MITRA)) return new MitraProfileFragment();
                else if (userLevel.equals(LEVEL_USER)) return new UserProfileFragment();

            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
