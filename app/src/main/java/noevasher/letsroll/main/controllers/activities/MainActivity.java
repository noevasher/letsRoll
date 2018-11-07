package noevasher.letsroll.main.controllers.activities;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.Consts;
import noevasher.letsroll.commons.parents.fragments.BaseFragment;
import noevasher.letsroll.main.controllers.activities.adapters.MainFragmentPagerAdapter;
import noevasher.letsroll.moto.controllers.fragments.MotoFragment;
import noevasher.letsroll.weather.controllers.WeatherFragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements LocationListener {
    
    private static final String TAG = "MainActivity";
    
    private static final int FRAGMENT_MOTO_RESULT = 0;
    private static final int FRAGMENT_WEATHER = 1;
    private static final int FRAGMENT_CHATLIST = 2;
    private static final int FRAGMENT_GENERAL = 3;
    
    private Snackbar mConnectionSnackBar;
    
    private MainViewPager mViewPager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private String currentSearchFragment;
    
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    /*
    @Override
    public void onStart() {
        super.onStart();
        //SE GUARDA EL ID DEL PROCESO
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                         .putInt("processId", android.os.Process.myPid()).apply();
        
        if(getIntent().getExtras() != null && getIntent().getExtras().get("fromChatActivity") != null){
            BottomNavigationViewEx bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setCurrentItem(FRAGMENT_CHATLIST);
            //mViewPager.setCurrentItem(FRAGMENT_CHATLIST);
            getIntent().putExtra("fromChatActivity", (Bundle) null);
        }else if(getIntent().getExtras().getString("fragment") != null){
            BottomNavigationViewEx bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setCurrentItem(FRAGMENT_CHATLIST);
            
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Consts
            .INTENT_PARAMETER_NOTIFICATION_CHATLIST, true)
                             .apply();
        }else{
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Consts
            .INTENT_PARAMETER_NOTIFICATION_CHATLIST, false)
                             .apply();
        }
        
        if(getIntent().getExtras().get(Consts.INTENT_PARAMETER_ENTITY_INFO) != null){
            Intent intent = new Intent(this.getApplicationContext(), ChatActivity.class);
            intent.putExtra(Consts.INTENT_PARAMETER_ENTITY_INFO,
                            getIntent().getSerializableExtra(Consts.INTENT_PARAMETER_ENTITY_INFO));
            intent.putExtra(Consts.INTENT_PARAMETER_NOTIFICATION_CHAT, true);
            this.startActivity(intent);
            
        }
        
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("chatIsActive", false)
                         .putString("entityId", "")
                         .putString("conversationId", "")
                         .apply();
    }
    
    //*/
    LocationManager locationManager;
    String provider;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Set up the login form.
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        
        if(!isTaskRoot()
           && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
           && getIntent().getAction() != null
           && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            
            finish();
            return;
        }
        this.setupFragmentsNavigation();
        configToolBar();
        configDrawer();
        
        this.mViewPager = findViewById(R.id.main_container);
        this.setupViewPager(this.mViewPager);
        this.mViewPager.setOffscreenPageLimit(3);
    }
    
    private void setupViewPager(ViewPager viewPager) {
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        BaseFragment motoFragment = new MotoFragment();
        mainFragmentPagerAdapter.addFragment(motoFragment, "MiMoto");
        mainFragmentPagerAdapter.addFragment(new WeatherFragment(), "Wheather");
        /*
        mainFragmentPagerAdapter.addFragment(new FavoritesFragment(), "Favorites");
        mainFragmentPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        mainFragmentPagerAdapter.addFragment(new GeneralsFragment(), "Generals");
        //*/
        viewPager.setAdapter(mainFragmentPagerAdapter);
    }
    
    /*
    public void replaceLoginFragment(ResultsFragment fragment){
        this.currentSearchFragment = Consts.RESULTS_FRAGMENT;
        mainFragmentPagerAdapter.replaceFragment(fragment, "Results", FRAGMENT_LOGIN_RESULT);
    }
    //*/
    private void setupFragmentsNavigation() {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.setTextVisibility(false);
        bottomNavigationView.enableShiftingMode(false);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.navigation_moto:
                    if(mViewPager.getCurrentItem() == FRAGMENT_MOTO_RESULT) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                    mViewPager.setCurrentItem(FRAGMENT_MOTO_RESULT);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                     .edit()
                                     .putBoolean("fragmentIsActive", false)
                                     .apply();
                    break;
                case R.id.navigation_weather:
                    if(checkLocationPermission()) {
                        mViewPager.setCurrentItem(FRAGMENT_WEATHER);
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                         .edit()
                                         .putBoolean("fragmentIsActive", false);
                        System.out.println("click in weather");
                    }
                    break;
            }
            return true;
        });
    }
    
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    
    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
           != PackageManager.PERMISSION_GRANTED) {
            
            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                   Manifest.permission.ACCESS_FINE_LOCATION)) {
                
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Ttle")
                        .setMessage("Messages!!!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                                                  new String[] {
                                                                          Manifest.permission.ACCESS_FINE_LOCATION },
                                                                  MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                                                  new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                                  MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0
                   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if(ContextCompat.checkSelfPermission(this,
                                                         Manifest.permission.ACCESS_FINE_LOCATION)
                       == PackageManager.PERMISSION_GRANTED) {
                        
                        //Request location updates:
                        String provider = locationManager.getBestProvider(new Criteria(), false);
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }
                } else {
                    
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    
                }
                return;
            }
        }
    }
    
    @Override
    public void onLocationChanged(Location location) {
    
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    
    }
    
    @Override
    public void onProviderEnabled(String provider) {
    
    }
    
    @Override
    public void onProviderDisabled(String provider) {
    
    }
    
    private void configToolBar() {
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.moto_menu_title);
        }
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
}
