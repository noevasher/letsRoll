package noevasher.letsroll.main.controllers.activities.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by holmes on 15/11/17.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();
    }

    public void replaceFragment(Fragment fragment, String title, int position){
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);

        notifyDataSetChanged();
    }

    public void popBackStack(int position){
        if (mFragmentList.get(position)!=null){
            if (mFragmentList.get(position).getView()!=null){
                if(mFragmentList.get(position).getChildFragmentManager().getBackStackEntryCount()>0)
                    mFragmentList.get(position).getChildFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}