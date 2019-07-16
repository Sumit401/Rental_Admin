package com.rental.ryde365_admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecycleAdp_Vehicle_listing extends RecyclerView.Adapter<RecycleAdp_Vehicle_listing.MyViewHolder> {

    private String url="https://gogoogol.in/android/admin/delete_vehicle.php";
    private Context context1;
    private ArrayList<String> vehicle_id;
    private ArrayList<String> vehicle_title;
    private ArrayList<String> vehicle_brand;
    private ArrayList<String> vehicle_overview;
    private ArrayList<String> vehicle_image;
    private ArrayList<String> vehicle_seting_capacity;
    private ArrayList<String> veh_city;
    private ArrayList<String> model;
    private ArrayList<String> fuel;
    private ArrayList<String> price_perday;
    RecycleAdp_Vehicle_listing(Context context, ArrayList<String> veh_id, ArrayList<String> veh_brand,
                               ArrayList<String> seating, ArrayList<String> veh_overview, ArrayList<String> veh_name,
                               ArrayList<String> city, ArrayList<String> veh_img, ArrayList<String> veh_model,
                               ArrayList<String> fuel_type, ArrayList<String> pricePerDay) {
        context1 = context;
        vehicle_id = veh_id;
        vehicle_title = veh_name;
        vehicle_overview = veh_overview;
        vehicle_seting_capacity = seating;
        vehicle_brand = veh_brand;
        vehicle_image = veh_img;
        veh_city =  city;
        model = veh_model;
        fuel = fuel_type;
        price_perday = pricePerDay;
    }

    @NonNull
    @Override
    public RecycleAdp_Vehicle_listing.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicle_listing_adapter,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecycleAdp_Vehicle_listing.MyViewHolder myViewHolder, final int i) {

        Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+vehicle_image.get(i)).resize(300,180).into(myViewHolder.vehicleimage);
        myViewHolder.vehicleimage.setBackgroundResource(R.drawable.backgrounddetails);
        myViewHolder.brand.setText(vehicle_title.get(i)+", "+vehicle_brand.get(i));
        myViewHolder.city.setText(veh_city.get(i));
        myViewHolder.seat.setText(vehicle_seting_capacity.get(i)+" Seater");
        myViewHolder.model.setText(model.get(i)+" Model");
        myViewHolder.fuel.setText(fuel.get(i)+" Vehicle");
        myViewHolder.price.setText("INR "+price_perday.get(i)+" Per Day");
        myViewHolder.overview.setText(vehicle_overview.get(i));
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                 Intent intent=new Intent(context1, Edit_Vehicle.class);
                 intent.putExtra("veh_id",""+vehicle_id.get(i));
                 context1.startActivity(intent);
            }
        });
        myViewHolder.delete_veh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object=new JSONObject();
                try {
                    object.put("veh_id",vehicle_id.get(i));
                    object.put("action","delete");
                    Delete_data deleteData = new Delete_data();
                    deleteData.execute(object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicle_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView brand,seat,model,fuel,overview,price,city;
        LinearLayout linearLayout;
        ImageView vehicleimage;
        ImageButton delete_veh;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleimage=itemView.findViewById(R.id.vehimg);
            brand=itemView.findViewById(R.id.veh_brand);
            city=itemView.findViewById(R.id.veh_city);
            price=itemView.findViewById(R.id.pricing);
            seat=itemView.findViewById(R.id.seating);
            model=itemView.findViewById(R.id.model);
            fuel=itemView.findViewById(R.id.fuel);
            delete_veh=itemView.findViewById(R.id.delete_vehicle);
            overview=itemView.findViewById(R.id.overview);
            linearLayout=itemView.findViewById(R.id.layoutinfo_veh_listing);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Delete_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(context1);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait");
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

            Intent intent=new Intent(context1,Update_Vehicle.class);
            context1.startActivity(intent);
        }
    }
}
