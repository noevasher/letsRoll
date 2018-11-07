package noevasher.letsroll.weather.controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.parents.fragments.BaseFragment;
import noevasher.letsroll.services.WeatherFunction;

public class WeatherFragment extends BaseFragment implements LocationListener {
    
    private static final int REQUEST_CODE_LOCATION = 100;
    
    @BindView(R.id.imageView_icon_weather)
    public TextView weatherIconImageView;
    
    @BindView(R.id.textView_temperature)
    public TextView temperatureTextView;
    
    @BindView(R.id.textView_condition)
    public TextView conditionTextView;
    
    @BindView(R.id.textView_place)
    public TextView locationTextView;
    
    @BindView(R.id.textView_pressure)
    public TextView pressureTextView;
    
    @BindView(R.id.textView_humidity)
    public TextView humidityTextView;
    
    private LocationManager mLocationManager;
    private String provider;
    private Location mLocation;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container,
                                     false); //setContentView(R.layout.fragment_login);
        
        ButterKnife.bind(this, view);
        /*
        getAvailableActivity(new IActivityEnabledListener() {
            @Override
            public void onActivityEnabled(FragmentActivity activity) {
            
            }
        });
        //*/
        /*
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = mLocationManager.getBestProvider(criteria, false);
    
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
           PackageManager.PERMISSION_GRANTED) {
        
            // We have access. Life is good.
            //setupContactsPicker();
            System.out.println("life is good");
            Location location = mLocationManager.getLastKnownLocation(provider);
            doActionWeather(location);
            
            //getWeatherInfo(location);
        } else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                                                      Manifest.permission.ACCESS_FINE_LOCATION)) {
        
            // We've been denied once before. Explain why we need the permission, then ask again.
            //getActivity().showDialog(DIALOG_PERMISSION_REASON);
            System.out.println("We've been denied once before");
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                                              Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_CODE_LOCATION);
        } else {
        
            // We've never asked. Just do it.
            System.out.println("We've never asked. Just do it.");
            //mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            //mLocation = mLocationManager.getLastKnownLocation(provider);
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                                              Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_CODE_LOCATION);
        }
    //*/
        return view;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_LOCATION) {
            System.out.println(permissions[0] + " " + Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
               && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //proceedWithSdCard();
                mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = mLocationManager.getBestProvider(criteria, false);
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                   PackageManager.PERMISSION_GRANTED &&
                   ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                   PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocation = mLocationManager.getLastKnownLocation(provider);
    
    
                doActionWeather(mLocation);
            }
        }
    }
    
    private void getWeatherInfo(Location location){
        System.out.println("location: " + location);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
    
        WeatherFunction.placeIdTask asyncTask = new WeatherFunction.placeIdTask(
                (weather_city, weather_description, weather_temperature, weather_humidity, weather_pressure,
                 weather_updatedOn, weather_iconText, sun_rise) -> {
                
                    locationTextView.setText("ciudad: " + weather_city);
                    //updatedField.setText(weather_updatedOn);
                    conditionTextView.setText(weather_description);
                    temperatureTextView.setText(weather_temperature);
                    humidityTextView.setText("Humidity: "+weather_humidity);
                    pressureTextView.setText("Pressure: "+weather_pressure);
                    weatherIconImageView.setText(Html.fromHtml(weather_iconText));
                    weatherIconImageView.setText(Html.fromHtml("&#9729;"));
    
                });
        String lat = String.valueOf(latitude);
        String lng = String.valueOf(longitude);
        asyncTask.execute(lat, lng); //  asyncTask.execute("Latitude", "Longitude")
    }
    
    private void doActionWeather(Location location){
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            getWeatherInfo(location);
        } else {
            humidityTextView.setText("Location not available");
            pressureTextView.setText("Location not available");
            
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("lat/lng: " + location.getLongitude());
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
}
