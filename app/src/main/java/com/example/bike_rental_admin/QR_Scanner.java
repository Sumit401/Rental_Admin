package com.example.bike_rental_admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QR_Scanner extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    String url="https://gogoogol.in/android/admin/status_change_qr.php";
    String booking_id,status;
    LinearLayout qrchecking;
    RelativeLayout layout_qr;
    int k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        Intent intent=getIntent();
        booking_id=intent.getStringExtra("id");
        status=intent.getStringExtra("status");
        surfaceView = findViewById(R.id.camerapreview);
        textView = findViewById(R.id.cameraresult);
        layout_qr=findViewById(R.id.layout_qr);
        qrchecking=findViewById(R.id.qrcheck);
        k=0;
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1024, 1024).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(QR_Scanner.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes=detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    textView.setText(qrcodes.valueAt(0).displayValue);

                    if (k == 0) {
                        k=1;
                        try {
                            JSONObject object = new JSONObject();
                            object.put("booking_id", booking_id);
                            object.put("veh_id", "" + textView.getText().toString());
                            object.put("status", status);
                            Send_data send_data = new Send_data();
                            send_data.execute(object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Send_data extends AsyncTask<String,String,String> {
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
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
            else {
                layout_qr.setVisibility(View.GONE);
                qrchecking.setVisibility(View.VISIBLE);
            }
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent1=new Intent(QR_Scanner.this,MainActivity3.class);
                    intent1.putExtra("id",""+booking_id);
                    startActivity(intent1);
                    finish();
                }
            },2200);
        }
    }
}
