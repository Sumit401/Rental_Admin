package com.rental.ryde365_admin;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Update_Vehicle extends AppCompatActivity {

    String url="https://gogoogol.in/android/admin/get_vehicles.php";
    ArrayList<String> veh_id=new ArrayList<>();
    ArrayList<String> veh_brand=new ArrayList<>();
    ArrayList<String> veh_name=new ArrayList<>();
    ArrayList<String> veh_overview=new ArrayList<>();
    ArrayList<String> veh_img=new ArrayList<>();
    ArrayList<String> city=new ArrayList<>();
    ArrayList<String> seating=new ArrayList<>();
    ArrayList<String> fuel_type =new ArrayList<>();
    ArrayList<String> veh_model =new ArrayList<>();
    ArrayList<String> PricePerDay = new ArrayList<>();
    RecyclerView recyclerView_update_vehicle;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_vehicle);
        sharedPreferences=getSharedPreferences("Admin",MODE_PRIVATE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        recyclerView_update_vehicle=findViewById(R.id.update_vehicle_recycler);
        // To get all the vehicles present.............
        JSONObject object=new JSONObject();
        try {
            object.put("vendor_id",sharedPreferences.getString("vendor_id",null));
            Get_Vehicles getVehicles = new Get_Vehicles();
            getVehicles.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_Vehicles extends AsyncTask <String,String,String> {
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
            try {
                JSONObject object=new JSONObject(s);
                JSONArray array=object.getJSONArray("vehicleinfo");
                for (int i=0;i<array.length();i++){
                    JSONObject object1=array.getJSONObject(i);
                    String v_id=object1.getString("id");
                    String VehiclesTitle=object1.getString("VehiclesTitle");
                    String Veh_brand =object1.getString("BrandName");
                    //String VehicleType=object1.getString("VehicleType");
                    String VehiclesOverview=object1.getString("VehiclesOverview");
                    String Vimage1=object1.getString("Vimage1");
                    String CityName=object1.getString("CityName");
                    String fuel = object1.getString("FuelType");
                    String model=object1.getString("ModelYear");
                    String seats =object1.getString("SeatingCapacity");
                    String pricing = object1.getString("PricePerDay");

                    veh_id.add(v_id);
                    veh_brand.add(Veh_brand);
                    veh_name.add(VehiclesTitle);
                    veh_overview.add(VehiclesOverview);
                    veh_img.add(Vimage1);
                    city.add(CityName);
                    seating.add(seats);
                    veh_model.add(model);
                    fuel_type.add(fuel);
                    PricePerDay.add(pricing);
                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(Update_Vehicle.this,
                        LinearLayoutManager.VERTICAL,false);
                recyclerView_update_vehicle.setLayoutManager(layoutManager);
                recyclerView_update_vehicle.setAdapter(new RecycleAdp_Vehicle_listing(getApplicationContext(),veh_id,veh_brand,seating,
                        veh_overview,veh_name,city,veh_img,veh_model,fuel_type,PricePerDay));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}