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
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Add_Vehicle extends AppCompatActivity {

    EditText veh_title,overview,price,model,seating_capacity;
    Spinner veh_brand,veh_fuel,veh_type,city_spinner;

    CheckBox Air_Conditioner, Power_Door_Locks, AntiLock_Braking_System, Brake_Assist, Power_Steering, Driver_Airbag,
            Passenger_Airbag, Power_Windows, CD_Player, Central_Locking, Crash_Sensor, Leather_Seats;
    String url = "https://gogoogol.in/android/admin/get_vehiclebrand.php";
    String  url2 = "https://gogoogol.in/android/admin/add_vehicle.php";
    int veh_brand_position,city_pos;
    int serverResponseCode;
    Button submit,img1;
    ArrayList<String> brand_id = new ArrayList<>();
    ArrayList<String> brand = new ArrayList<>();
    ArrayList<String> city_id=new ArrayList<>();
    ArrayList<String> city_name=new ArrayList<>();

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    ArrayList<String> imagesEncodedList;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);

        preferences=getSharedPreferences("Admin",MODE_PRIVATE);
        veh_title=findViewById(R.id.veh_title);
        overview=findViewById(R.id.veh_overview);
        price=findViewById(R.id.price);
        model=findViewById(R.id.modelyr);
        seating_capacity=findViewById(R.id.seating_capacity);
        veh_brand=findViewById(R.id.brand);
        city_spinner=findViewById(R.id.city);
        veh_fuel=findViewById(R.id.fuel_type);
        veh_type=findViewById(R.id.veh_type);
        Air_Conditioner=findViewById(R.id.ac);
        Power_Door_Locks=findViewById(R.id.powerdoor);
        AntiLock_Braking_System=findViewById(R.id.abs);
        Brake_Assist=findViewById(R.id.break_assist);
        Power_Steering=findViewById(R.id.power_steering);
        Driver_Airbag=findViewById(R.id.driver_bag);
        Passenger_Airbag=findViewById(R.id.pass_airbag);
        Power_Windows=findViewById(R.id.power_window);
        CD_Player=findViewById(R.id.cd_player);
        Central_Locking=findViewById(R.id.central_lock);
        Crash_Sensor=findViewById(R.id.crash_sensor);
        Leather_Seats=findViewById(R.id.leather_seat);
        submit=findViewById(R.id.submit_btn);
        img1 = findViewById(R.id.img1);



        Get_Veh_Brand get_veh_brand=new Get_Veh_Brand();
        get_veh_brand.execute();

        veh_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Add_Vehicle.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_MULTIPLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!veh_title.getText().toString().trim().isEmpty()|| !overview.getText().toString().isEmpty()|| !model.getText().toString().isEmpty() || !seating_capacity.getText().toString().isEmpty() ||
                        !city_id.get(veh_brand_position).equalsIgnoreCase("NULL")||!brand_id.get(veh_brand_position).equalsIgnoreCase("NULL")|| imagesEncodedList.size()==5) {
                    new Upload_img().execute("");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("vendor_id",preferences.getString("vendor_id",null));
                        object.put("veh_title", veh_title.getText().toString().trim());
                        object.put("overview", overview.getText().toString().trim());
                        object.put("price", price.getText().toString().trim());
                        object.put("model_yr", model.getText().toString().trim());
                        object.put("seating", seating_capacity.getText().toString().trim());
                        object.put("veh_brand", brand_id.get(veh_brand_position));
                        object.put("city",city_id.get(city_pos));
                        object.put("fuel", veh_fuel.getSelectedItem());
                        object.put("veh_type", veh_type.getSelectedItem());
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
                        object.put("img1", imagesEncodedList.get(0).substring(imagesEncodedList.get(0).lastIndexOf("/") + 1));
                        object.put("img2", imagesEncodedList.get(1).substring(imagesEncodedList.get(1).lastIndexOf("/") + 1));
                        object.put("img3", imagesEncodedList.get(2).substring(imagesEncodedList.get(2).lastIndexOf("/") + 1));
                        object.put("img4", imagesEncodedList.get(3).substring(imagesEncodedList.get(3).lastIndexOf("/") + 1));
                        object.put("img5", imagesEncodedList.get(4).substring(imagesEncodedList.get(4).lastIndexOf("/") + 1));

                        Upload_data uploadData = new Upload_data();
                        uploadData.execute(object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    @SuppressLint("StaticFieldLeak")
    private class Get_Veh_Brand extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,"");
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
                brand_id.add("NULL");
                brand.add("Select Brand");
                String s1=object.getString("response");
                if (s1.equalsIgnoreCase("success")){
                    JSONArray array=object.getJSONArray("vehicleinfo");
                    for(int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);
                        String s2=object1.getString("id");
                        String s3=object1.getString("BrandName");
                        brand_id.add(s2);
                        brand.add(s3);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Add_Vehicle.this, android.R.layout.simple_spinner_item, brand);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        veh_brand.setAdapter(dataAdapter);

                        }

                    city_id.add(0,"NULL");
                    city_name.add(0,"Select City");
                    JSONArray array2=object.getJSONArray("cityinfo");
                    for(int i=0;i<array2.length();i++) {
                        JSONObject object2 = array2.getJSONObject(i);
                        String s2 = object2.getString("id");
                        String s3 = object2.getString("CityName");
                        city_id.add(s2);
                        city_name.add(s3);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Add_Vehicle.this, android.R.layout.simple_spinner_item, city_name);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        city_spinner.setAdapter(dataAdapter);
                    }

                    }else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Upload_data extends AsyncTask<String,String,String>{

        ProgressDialog dialog=new ProgressDialog(Add_Vehicle.this);
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
            Toast.makeText(getApplicationContext(),"Vehicle Added",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
