package com.rental.ryde365_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    String url="https://gogoogol.in/android/admin/getbookinginfo.php";
    ArrayList<String> userid=new ArrayList<>();
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> from_date=new ArrayList<>();
    ArrayList<String> to_date=new ArrayList<>();
    ArrayList<String> booking_date=new ArrayList<>();
    ArrayList<String> brand=new ArrayList<>();
    ArrayList<String> pics=new ArrayList<>();
    ArrayList<String> vehicle_title=new ArrayList<>();
    ArrayList<String> messages=new ArrayList<>();
    ArrayList<String> statusofbooking=new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView=findViewById(R.id.recycler);
        fab=findViewById(R.id.fab);

        ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,Add_Vehicle.class);
                startActivity(intent);
            }
        });


        try {
            JSONObject object=new JSONObject();
            object.put("action","get_all_links");
            Get_data get_data=new Get_data();
            get_data.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
    }


    @SuppressLint("StaticFieldLeak")
    private class Get_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(MainActivity2.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait");
            dialog.setTitle("Loading");
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s==null){
                Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet",Snackbar.LENGTH_SHORT);
            }else
            {
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
                            String id=j2.getString("id");

                            userid.add(id);
                            names.add(name);
                            from_date.add(FromDate);
                            to_date.add(ToDate);
                            booking_date.add(bookingdate);
                            brand.add(BrandName);
                            pics.add(vimage);
                            vehicle_title.add(veh_title);
                            messages.add(message);
                            statusofbooking.add(status);

                        }
                        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new RecycleAdp(MainActivity2.this,names,from_date,to_date,
                                booking_date,brand,pics,vehicle_title,messages,userid,statusofbooking));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences settings = getSharedPreferences("Admin", MODE_PRIVATE);
            settings.edit().clear().apply();
            final ProgressDialog progressDialog=new ProgressDialog(MainActivity2.this);
            progressDialog.setMessage("Wait");
            progressDialog.setTitle("Logging Out");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent=new Intent(MainActivity2.this,MainActivity1.class);
                    startActivity(intent);
                    finish();
                }
            },1500);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
