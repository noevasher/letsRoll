package noevasher.letsroll.main.controllers.activities.adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    
    /*
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
    //*/
    
    /*
    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mFragmentTitleList.get(position));
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        return view;
    }
    
    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mFragmentTitleList.get(position));
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
    //*/
}
