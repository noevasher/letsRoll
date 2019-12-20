package noevasher.letsroll.main.controllers.activities;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.friends.controllers.fragments.FriendsFragment;
import noevasher.letsroll.main.controllers.activities.adapters.TabAdapter;
import noevasher.letsroll.moto.controllers.fragments.MotoFragment;
import noevasher.letsroll.profile.controllers.fragments.ProfileFragment;
import noevasher.letsroll.rute.controllers.fragments.RouteFragment;
import noevasher.letsroll.weather.controllers.WeatherFragment;

public class MainActivity extends ParentActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    private int[] tabIcons = {
            R.drawable.ic_lr_moto,
            R.drawable.ic_lr_wheater,
            R.drawable.ic_lr_rutes,
            R.drawable.ic_lr_friends,
            R.drawable.ic_lr_profile_moto
    };

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);
        ButterKnife.bind(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        initTabLayout();

        configToolBar(toolbar, null);
        configDrawer();
    }


    private void initTabLayout() {
        adapter = new TabAdapter(getSupportFragmentManager());
        setFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupFragmentsNavigation();

        highLightCurrentTab(tabLayout.getTabAt(0));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        highLightCurrentTab(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
    }

    private void configDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.main_content);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                Log.d("main", "onDrawerOpened");
            }

            public void onDrawerClosed(View v) {
                super.onDrawerOpened(v);
                Log.d("main", "onDrawerClosed");
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigationView_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.terms:
                        Log.d("menu nav", "terms");
                        return true;
                    case R.id.settings:
                        Log.d("menu nav", "settings");
                        return true;
                    case R.id.profile:
                        Log.d("menu nav", "account");
                        TabLayout.Tab tab = tabLayout.getTabAt(4);
                        tab.select();
                        return true;
                    case R.id.logout:
                        Log.d("menu nav", "logout");
                        logout();
                        return true;
                    default:
                        return false;
                }
            }
        });

        View header = navigationView.getHeaderView(0);
        textName = header.findViewById(R.id.textView_username);
        textName.setTextColor(getColor(R.color.white));
        imageProfile = header.findViewById(R.id.imageView_profile);
    }


    private void setupFragmentsNavigation() {
        for (int position = 0; position < tabIcons.length; position++) {
            tabLayout.getTabAt(position).setIcon(tabIcons[position]);
            tabLayout.getTabAt(position).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setFragments() {
        adapter.addFragment(new MotoFragment(), getString(R.string.moto_menu_title));
        adapter.addFragment(new WeatherFragment(), getString(R.string.weather_menu_title));
        adapter.addFragment(new RouteFragment(), getString(R.string.rutes_menu_title));
        adapter.addFragment(new FriendsFragment(), getString(R.string.friends_menu_title));
        //adapter.addFragment(new FriendsFragment(), getString(R.string.friends_menu_title));
        adapter.addFragment(new ProfileFragment(), getString(R.string.profile_menu_title));
    }


    private void highLightCurrentTab(TabLayout.Tab tab) {
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorSelectedItem);
        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }
    //*/

    private static final int PERMISSIONS_REQUEST_CAMERA = 555;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //setupContactsPicker();
            System.out.println("PERMISOS onRequest");
            ProfileFragment.profileFragment.takePhotoFromCamera();
        } else {
            System.out.println("PERMISOS onRequest else");

            // We were not granted permission this time, so don't try to show the contact picker
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}