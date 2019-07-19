package com.rental.ryde365_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Edit_Vehicle extends AppCompatActivity {

    SharedPreferences preferences;
    String url="https://gogoogol.in/android/admin/get_vehicle_info.php";
    String url2="https://gogoogol.in/android/admin/update_vehicle.php";
    EditText veh_title,overview,price,model,seating_capacity;
    Spinner veh_brand_spinner,veh_fuel,veh_type,city_spinner;

    int PICK_IMAGE_MULTIPLE = 1;
    CheckBox Air_Conditioner, Power_Door_Locks, AntiLock_Braking_System, Brake_Assist, Power_Steering, Driver_Airbag,
            Passenger_Airbag, Power_Windows, CD_Player, Central_Locking, Crash_Sensor, Leather_Seats;
    ImageView veh_img1,veh_img2,veh_img3,veh_img4,veh_img5;
    Button update_btn,image_btn;
    int veh_brand_position,city_pos;
    ArrayList<String> brandid_array= new ArrayList<>();
    ArrayList<String> brand_name_array = new ArrayList<>();
    ArrayList<String> cityid_array= new ArrayList<>();
    ArrayList<String> city_name_array = new ArrayList<>();

    String vimage1,vimage2,vimage3,vimage4,vimage5;
    int serverResponseCode;
    String imageEncoded;
    ArrayList<String> imagesEncodedList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vehicle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        final String vehicle_id=intent.getStringExtra("veh_id");

        preferences=getSharedPreferences("Admin",MODE_PRIVATE);
        veh_title=findViewById(R.id.update_veh_title);
        overview=findViewById(R.id.update_veh_overview);
        price=findViewById(R.id.update_veh_price);
        model=findViewById(R.id.update_veh_modelyr);
        seating_capacity=findViewById(R.id.update_veh_seating_capacity);
        veh_brand_spinner=findViewById(R.id.update_veh_brand);
        city_spinner=findViewById(R.id.update_veh_city);
        veh_fuel=findViewById(R.id.update_veh_fueltype);
        veh_type=findViewById(R.id.update_veh_type);
        Air_Conditioner=findViewById(R.id.update_veh_ac);
        Power_Door_Locks=findViewById(R.id.update_veh_powerdoor);
        AntiLock_Braking_System=findViewById(R.id.update_veh_abs);
        Brake_Assist=findViewById(R.id.update_veh_break_assist);
        Power_Steering=findViewById(R.id.update_veh_power_steering);
        Driver_Airbag=findViewById(R.id.update_veh_driver_bag);
        Passenger_Airbag=findViewById(R.id.update_veh_pass_airbag);
        Power_Windows=findViewById(R.id.update_veh_power_window);
        CD_Player=findViewById(R.id.update_veh_cd_player);
        Central_Locking=findViewById(R.id.update_veh_central_lock);
        Crash_Sensor=findViewById(R.id.update_veh_crash_sensor);
        Leather_Seats=findViewById(R.id.update_veh_leather_seat);
        veh_img1=findViewById(R.id.update_veh_img1);
        veh_img2=findViewById(R.id.update_veh_img2);
        veh_img3=findViewById(R.id.update_veh_img3);
        veh_img4=findViewById(R.id.update_veh_img4);
        veh_img5=findViewById(R.id.update_veh_img5);
        update_btn=findViewById(R.id.update_veh_btn);
        image_btn = findViewById(R.id.update_veh_images);

        JSONObject object=new JSONObject();
        try {
            object.put("veh_id",vehicle_id);
            Get_vehicle getVehicle=new Get_vehicle();
            getVehicle.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Edit_Vehicle.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_MULTIPLE);
            }
        });


        veh_brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                veh_brand_position=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_pos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!veh_title.getText().toString().trim().isEmpty()|| !overview.getText().toString().isEmpty()|| !model.getText().toString().isEmpty() || !seating_capacity.getText().toString().isEmpty()) {

                    new Upload_img().execute("");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("vendor_id",preferences.getString("vendor_id",null));
                        object.put("veh_title", veh_title.getText().toString().trim());
                        object.put("overview", overview.getText().toString().trim());
                        object.put("price", price.getText().toString().trim());
                        object.put("model_yr", model.getText().toString().trim());
                        object.put("seating", seating_capacity.getText().toString().trim());
                        object.put("veh_brand", brandid_array.get(veh_brand_position));
                        object.put("city",cityid_array.get(city_pos));
                        object.put("fuel", veh_fuel.getSelectedItem());
                        object.put("veh_type", veh_type.getSelectedItem());
                        object.put("v_id",vehicle_id);
                        if (Air_Conditioner.isChecked()) {
                            object.put("ac", "1");
                        } else {
                            object.put("ac", "0");
                        }
                        if (Power_Door_Locks.isChecked()) {
                            object.put("PD", "1");
                        } else {
                            object.put("PD", "0");
                        }
                        if (Passenger_Airbag.isChecked()) {
                            object.put("Pass_air", "1");
                        } else {
                            object.put("Pass_air", "0");
                        }
                        if (AntiLock_Braking_System.isChecked()) {
                            object.put("ABS", "1");
                        } else {
                            object.put("ABS", "0");
                        }
                        if (Brake_Assist.isChecked()) {
                            object.put("Break_Assist", "1");
                        } else {
                            object.put("Break_Assist", "0");
                        }
                        if (Power_Steering.isChecked()) {
                            object.put("PS", "1");
                        } else {
                            object.put("PS", "0");
                        }
                        if (Driver_Airbag.isChecked()) {
                            object.put("DriverAir", "1");
                        } else {
                            object.put("DRiverAir", "0");
                        }
                        if (Power_Windows.isChecked()) {
                            object.put("PowerWindows", "1");
                        } else {
                            object.put("PowerWindows", "0");
                        }
                        if (CD_Player.isChecked()) {
                            object.put("CD", "1");
                        } else {
                            object.put("CD", "0");
                        }
                        if (Central_Locking.isChecked()) {
                            object.put("CentralLocking", "1");
                        } else {
                            object.put("CentralLocking", "0");
                        }
                        if (Crash_Sensor.isChecked()) {
                            object.put("CrashSensor", "1");
                        } else {
                            object.put("CrashSensor", "0");
                        }
                        if (Leather_Seats.isChecked()) {
                            object.put("LeatherSeats", "1");
                        } else {
                            object.put("LeatherSeats", "0");
                        }
                        if (imagesEncodedList.size()>0){
                        for(int i=0;i<imagesEncodedList.size();i++){
                            object.put("img"+i+"", imagesEncodedList.get(0).substring(imagesEncodedList.get(i).lastIndexOf("/") + 1));
                        }
                        }else {
                            object.put("img1", vimage1);
                            object.put("img2", vimage2);
                            object.put("img3", vimage3);
                            object.put("img4", vimage4);
                            object.put("img5", vimage5);
                        }
                        Upload_data uploadData = new Upload_data();
                        uploadData.execute(object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_vehicle extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(Edit_Vehicle.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            try {
                JSONObject jsonObject=new JSONObject(s);
                String s1=jsonObject.getString("response");
                if (s1.equalsIgnoreCase("success")){
                    JSONArray array1=jsonObject.getJSONArray("veh_brands");
                    for (int i=0;i<array1.length();i++){
                        JSONObject object=array1.getJSONObject(i);
                        String brandid=object.getString("id");
                        String BrandName=object.getString("BrandName");
                        brandid_array.add(brandid);
                        brand_name_array.add(BrandName);
                    }

                    JSONArray array2=jsonObject.getJSONArray("city");
                    for (int i=0;i<array2.length();i++){
                        JSONObject object1=array2.getJSONObject(i);
                        String cityid=object1.getString("id");
                        String cityName=object1.getString("CityName");
                        cityid_array.add(cityid);
                        city_name_array.add(cityName);
                    }

                    JSONArray array=jsonObject.getJSONArray("vehicleinfo");
                    for (int i=0;i<array.length();i++){
                        JSONObject obj1=array.getJSONObject(i);
                        veh_title.setText(obj1.getString("VehiclesTitle"));
                        price.setText(obj1.getString("PricePerDay"));
                        model.setText(obj1.getString("ModelYear"));
                        seating_capacity.setText(obj1.getString("SeatingCapacity"));
                        overview.setText(obj1.getString("VehiclesOverview"));
                        String VehicleType=obj1.getString("VehicleType");

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Edit_Vehicle.this, R.array.vehicle_type, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        veh_type.setAdapter(adapter);
                        if (VehicleType != null) {
                            int spinnerPosition = adapter.getPosition(VehicleType);
                            veh_type.setSelection(spinnerPosition);
                        }

                        String FuelType=obj1.getString("FuelType");
                        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(Edit_Vehicle.this, R.array.fuel, android.R.layout.simple_spinner_item);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        veh_fuel.setAdapter(adapter1);
                        if (FuelType != null) {
                            int spinnerPosition = adapter1.getPosition(FuelType);
                            veh_fuel.setSelection(spinnerPosition);
                        }

                        String vehbrand=obj1.getString("BrandName");
                        ArrayAdapter adapter2 =new ArrayAdapter<String>(Edit_Vehicle.this, android.R.layout.simple_spinner_item, brand_name_array);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        veh_brand_spinner.setAdapter(adapter2);
                        if (vehbrand != null) {
                            int spinnerPosition = adapter2.getPosition(vehbrand);
                            veh_brand_spinner.setSelection(spinnerPosition);
                        }


                        String CityName=obj1.getString("CityName");
                        ArrayAdapter adapter3 =new ArrayAdapter<String>(Edit_Vehicle.this, android.R.layout.simple_spinner_item, city_name_array);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(adapter3);
                        if (CityName != null) {
                            int spinnerPosition = adapter3.getPosition(CityName);
                            city_spinner.setSelection(spinnerPosition);
                        }

                        if (obj1.getString("AirConditioner").equalsIgnoreCase("1")){
                            Air_Conditioner.setChecked(true);
                        }
                        if (obj1.getString("PowerDoorLocks").equalsIgnoreCase("1")){
                            Power_Door_Locks.setChecked(true);
                        }
                        if (obj1.getString("AntiLockBrakingSystem").equalsIgnoreCase("1")){
                            AntiLock_Braking_System.setChecked(true);
                        }
                        if (obj1.getString("BrakeAssist").equalsIgnoreCase("1")){
                            Brake_Assist.setChecked(true);
                        }
                        if(obj1.getString("PowerSteering").equalsIgnoreCase("1")){
                            Power_Steering.setChecked(true);
                        }
                        if (obj1.getString("DriverAirbag").equalsIgnoreCase("1")){
                            Driver_Airbag.setChecked(true);
                        }
                        if(obj1.getString("PassengerAirbag").equalsIgnoreCase("1")){
                            Passenger_Airbag.setChecked(true);
                        }
                        if (obj1.getString("PowerWindows").equalsIgnoreCase("1")){
                            Power_Windows.setChecked(true);
                        }
                        if (obj1.getString("CDPlayer").equalsIgnoreCase("1")){
                            CD_Player.setChecked(true);
                        }
                        if (obj1.getString("CentralLocking").equalsIgnoreCase("1")){
                            Central_Locking.setChecked(true);
                        }
                        if (obj1.getString("CrashSensor").equalsIgnoreCase("1")){
                            Crash_Sensor.setChecked(true);
                        }
                        if (obj1.getString("LeatherSeats").equalsIgnoreCase("1")){
                            Leather_Seats.setChecked(true);
                        }
                        vimage1=obj1.getString("Vimage1");
                        vimage2=obj1.getString("Vimage2");
                        vimage3=obj1.getString("Vimage3");
                        vimage4=obj1.getString("Vimage4");
                        vimage5=obj1.getString("Vimage5");
                        Picasso.get().load( "https://gogoogol.in/admin/img/vehicleimages/"+obj1.getString("Vimage1")).resize(300,200).into(veh_img1);
                        Picasso.get().load( "https://gogoogol.in/admin/img/vehicleimages/"+obj1.getString("Vimage2")).resize(300,200).into(veh_img2);
                        Picasso.get().load( "https://gogoogol.in/admin/img/vehicleimages/"+obj1.getString("Vimage3")).resize(300,200).into(veh_img3);
                        Picasso.get().load( "https://gogoogol.in/admin/img/vehicleimages/"+obj1.getString("Vimage4")).resize(300,200).into(veh_img4);
                        Picasso.get().load( "https://gogoogol.in/admin/img/vehicleimages/"+obj1.getString("Vimage5")).resize(300,200).into(veh_img5);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Upload_data extends AsyncTask<String,String,String>{
        ProgressDialog dialog=new ProgressDialog(Edit_Vehicle.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url2,params[0]);
            if (object== null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),"Vehicle Updated",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    imagesEncodedList.add(imageEncoded);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressLint("StaticFieldLeak")
    private class Upload_img extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String...params) {
            try {

                for (int i=0;i<imagesEncodedList.size();i++) {

                    String sourceFileUri = imagesEncodedList.get(i);
                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;
                    File sourceFile = new File(sourceFileUri);

                    if (sourceFile.isFile()) {

                        try {
                            String upLoadServerUri = "https://gogoogol.in/android/admin/upload_img.php";
                            // open a URL connection to the Servlet
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(upLoadServerUri);

                            // Open a HTTP connection to the URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE",
                                    "multipart/form-data");
                            conn.setRequestProperty("Content-Type",
                                    "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("bill", sourceFileUri);
                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + sourceFileUri + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            // create a buffer of maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {

                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            }

                            // send multipart form data necesssary after file
                            // data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            // Responses from the server (code and message)
                            serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();

                            if (serverResponseCode == 200) {

                            }

                            // close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}