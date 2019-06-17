package com.example.bike_rental_admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String id;
    private GoogleMap mMap;
    Marker marker;
    int k=0;
    String url="https://gogoogol.in/android/admin/getloc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,500);
                try {
                    JSONObject object=new JSONObject();
                    object.put("id",id);
                    Get_loc getLoc=new Get_loc();
                    getLoc.execute(object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },500);

    }

    private class Get_loc extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){

            }else {
                try {
                    JSONObject object = new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("success")){
                        String lat1=object.getString("lat");
                        String lng1=object.getString("lng");
                        if (lat1 != null && lng1!=null) {
                            double s2 = Double.parseDouble(lat1);
                            double s3 = Double.parseDouble(lng1);
                            LatLng latLng = new LatLng(s2, s3);

                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Vehicle"));
                            if (k == 0) {
                                k = 1;
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                                mMap.animateCamera(cameraUpdate);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
