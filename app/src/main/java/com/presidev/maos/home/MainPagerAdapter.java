package com.presidev.maos.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.presidev.maos.bookmark.view.BookmarkFragment;
import com.presidev.maos.dashboard.view.DashboardFragment;
import com.presidev.maos.developer.view.DeveloperFragment;
import com.presidev.maos.profile.mitra.view.MitraProfileFragment;
import com.presidev.maos.profile.user.view.UserProfileFragment;

import static com.presidev.maos.utils.Constants.LEVEL_DEVELOPER;
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
                if (userLevel == null) return new DashboardFragment();
                else if (userLevel.equals(LEVEL_MITRA) || userLevel.equals(LEVEL_USER)) return new DashboardFragment();
                else if (userLevel.equals(LEVEL_DEVELOPER)) return new DeveloperFragment();

            case 1:
                if (userLevel == null) return new UnauthenticatedFragment();
                else if (userLevel.equals(LEVEL_MITRA) || userLevel.equals(LEVEL_USER)) return new BookmarkFragment();
                else if (userLevel.equals(LEVEL_DEVELOPER)) return new DeveloperFragment();

            case 2:
                if (userLevel == null) return new UnauthenticatedFragment();
                else if (userLevel.equals(LEVEL_MITRA)) return new MitraProfileFragment();
                else if (userLevel.equals(LEVEL_USER)) return new UserProfileFragment();
                else if (userLevel.equals(LEVEL_DEVELOPER)) return new DeveloperFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
