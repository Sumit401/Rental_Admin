package com.example.bike_rental_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity3 extends AppCompatActivity{

    String url="https://gogoogol.in/android/admin/getidbooking.php";
    String url1="https://gogoogol.in/android/admin/status_change.php";
    TextView veh_name,username,bookto,bookfrom,booking_date,usermessages;
    Switch act3status;
    ImageView vehicle_image;
    ImageButton qrscan;
    Button trackcustomer;
    int switch_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        veh_name=findViewById(R.id.act3vehicle_name);
        username=findViewById(R.id.act3username);
        bookto=findViewById(R.id.act3upto_date);
        trackcustomer=findViewById(R.id.trackcustomer);
        bookfrom=findViewById(R.id.act3from_date);
        booking_date=findViewById(R.id.act3booking_date);
        act3status=findViewById(R.id.act3_status);
        vehicle_image=findViewById(R.id.act3vehicleimg);
        usermessages=findViewById(R.id.act3messages);
        qrscan=findViewById(R.id.qrscan);
        final Intent i=getIntent();
        final String s1=i.getStringExtra("id");
        //Toast.makeText(MainActivity3.this,""+s1,Toast.LENGTH_SHORT).show();
        try {
            JSONObject object=new JSONObject();
            object.put("id",s1);
            object.put("action","get_all_links");

            Get_data get_data =new Get_data();
            get_data.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        act3status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    Toast.makeText(MainActivity3.this,"Checked",Toast.LENGTH_SHORT).show();
                    switch_status=1;
                }else if (isChecked==false){
                    Toast.makeText(MainActivity3.this,"Not Checked",Toast.LENGTH_SHORT).show();
                    switch_status=0;
                }
                try {
                    JSONObject object1=new JSONObject();
                    object1.put("id",s1);
                    object1.put("status",switch_status);
                    Status_change status_change=new Status_change();
                    status_change.execute(object1.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        qrscan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity3.this, new String[]{Manifest.permission.CAMERA}, 1);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                }else {
                    Intent intent1 = new Intent(getApplicationContext(), QR_Scanner.class);
                    intent1.putExtra("id", "" +s1);
                    intent1.putExtra("status",""+switch_status);
                    startActivity(intent1);
                    finish();
                }
            }
        });
        trackcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity3.this,MapsActivity.class);
                intent.putExtra("id",""+s1);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(MainActivity3.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.setTitle("Please Wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s.equalsIgnoreCase("NULL")){
                Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet",Snackbar.LENGTH_SHORT);
            }else {
                try {
                    JSONObject object=new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("Success")){
                        JSONArray array=object.getJSONArray("vehicleinfo");
                        for (int i=0;i<array.length();i++){
                            JSONObject j2 = array.getJSONObject(i);
                            String vimage=j2.getString("Vimage1");
                            String name=j2.getString("FullName");
                            String FromDate=j2.getString("FromDate");
                            String ToDate=j2.getString("ToDate");
                            String bookingdate=j2.getString("PostingDate");
                            String BrandName=j2.getString("BrandName");
                            String veh_title=j2.getString("VehiclesTitle");
                            String message=j2.getString("message");
                            String status=j2.getString("Status");
                            Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+vimage).resize(550,300).into(vehicle_image);
                            username.setText(name);
                            veh_name.setText(veh_title+", "+BrandName);
                            bookfrom.setText(FromDate);
                            bookto.setText(ToDate);
                            booking_date.setText(bookingdate);
                            usermessages.setText(message);
                            if (status.equalsIgnoreCase("1")){
                                act3status.setChecked(true);
                            }else if (status.equalsIgnoreCase("0")){
                                act3status.setChecked(false);
                            }
                        }
                    }else if (s1.equalsIgnoreCase("Failed")){
                        Snackbar.make(getWindow().getDecorView().getRootView(),"Failed",Snackbar.LENGTH_SHORT);
                    }else {
                        Snackbar.make(getWindow().getDecorView().getRootView(),"Error",Snackbar.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Status_change extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url1,params[0]);
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(MainActivity3.this,"No Internet",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to Access Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}