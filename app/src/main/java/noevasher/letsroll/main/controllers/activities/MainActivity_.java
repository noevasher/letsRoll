package noevasher.letsroll.main.controllers.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.parents.activities.BaseActivity;
import noevasher.letsroll.friends.controllers.fragments.FriendsFragment;
import noevasher.letsroll.main.controllers.activities.adapters.TabAdapter;
import noevasher.letsroll.moto.controllers.fragments.MotoFragment;
import noevasher.letsroll.profile.controllers.fragments.ProfileFragment;
import noevasher.letsroll.rute.controllers.fragments.RouteFragment;
import noevasher.letsroll.weather.controllers.WeatherFragment;

public class MainActivity_ extends BaseActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
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
        
        adapter = new TabAdapter(getSupportFragmentManager());
        setFragments();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupFragmentsNavigation();
    
        highLightCurrentTab(tabLayout.getTabAt(0));
        tabLayout.setOnTabSelectedListener(
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
        configToolBar(toolbar, null);
        configDrawer();
    }
    
    /*
    private void configToolBar() {
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setTitle(R.string.moto_menu_title);
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }
    //*/
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
        NavigationView navigationView = findViewById(R.id.navigationView_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                
                switch(item.getItemId()) {
                    case R.id.terms:
                        Log.d("menu nav", "terms");
                        return true;
                    case R.id.settings:
                        Log.d("menu nav", "settings");
                        return true;
                    case R.id.account:
                        Log.d("menu nav", "account");
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    
    
    private void setupFragmentsNavigation() {
        for(int position = 0; position < tabIcons.length; position++) {
            tabLayout.getTabAt(position).setIcon(tabIcons[position]);
            tabLayout.getTabAt(position).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }
    
    private void setFragments(){
        adapter.addFragment(new MotoFragment(), getString(R.string.moto_menu_title));
        adapter.addFragment(new WeatherFragment(), getString(R.string.weather_menu_title));
        adapter.addFragment(new RouteFragment(), getString(R.string.rutes_menu_title));
        adapter.addFragment(new FriendsFragment(), getString(R.string.friends_menu_title));
        adapter.addFragment(new ProfileFragment(), getString(R.string.profile_menu_title));
    }
    
    
    private void highLightCurrentTab(TabLayout.Tab tab) {
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorSelectedItem);
        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }
    //*/
    
    
}