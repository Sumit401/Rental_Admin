package com.rental.ryde365_admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity1 extends AppCompatActivity {

    String url="https://gogoogol.in/android/admin/login.php";
    EditText id,pass;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        SharedPreferences preferences=getSharedPreferences("Admin",MODE_PRIVATE);
        String user=preferences.getString("user",null);

        if (user !=null){
            Intent intent=new Intent(MainActivity1.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }

        id=findViewById(R.id.mailid);
        pass=findViewById(R.id.pass);
        submit=findViewById(R.id.submitdata);
        final JSONObject object=new JSONObject();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().toString().isEmpty() && !id.getText().toString().isEmpty()){
                    try {
                        object.put("user",id.getText().toString().trim());
                        object.put("pass",pass.getText().toString().trim());
                        object.put("action","login");
                        Login_data login_data=new Login_data();
                        login_data.execute(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Login_data extends AsyncTask<String,String,String> {

        ProgressDialog dialog=new ProgressDialog(MainActivity1.this);
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
            }else{
                try {
                    JSONObject object=new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("Success")){
                        SharedPreferences preferences=getSharedPreferences("Admin",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("user",id.getText().toString().trim());
                        editor.apply();
                        Intent intent=new Intent(MainActivity1.this,MainActivity2.class);
                        startActivity(intent);
                        finish();
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
}
