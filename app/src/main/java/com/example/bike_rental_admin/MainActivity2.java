package com.example.bike_rental_admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView=findViewById(R.id.recycler);
        try {
            JSONObject object=new JSONObject();
            object.put("action","get_all_links");
            Get_data get_data=new Get_data();
            get_data.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
