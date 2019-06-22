package com.rental.ryde365_admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdp extends RecyclerView.Adapter<RecycleAdp.MyViewHolder> {

    private Context context;
    private ArrayList<String> veh_brand;
    private ArrayList<String> fromdate;
    private ArrayList<String> todate;
    private ArrayList<String> bookingdate;
    private ArrayList<String> username;
    private ArrayList<String> veh_title;
    private ArrayList<String> messages;
    private ArrayList<String> id;
    private ArrayList<String> veh_image;
    private ArrayList<String> bookingstatus;

    RecycleAdp(Context context1, ArrayList<String> names, ArrayList<String> from_date, ArrayList<String> to_date,
               ArrayList<String> booking_date, ArrayList<String> brand, ArrayList<String> vehiclepics, ArrayList<String>
                       vehicle_title, ArrayList<String> message, ArrayList<String> userid, ArrayList<String> statusofbooking) {
        context=context1;
        username=names;
        fromdate=from_date;
        bookingdate=booking_date;
        veh_brand=brand;
        veh_image=vehiclepics;
        todate=to_date;
        veh_title=vehicle_title;
        messages=message;
        id=userid;
        bookingstatus=statusofbooking;
    }

    @NonNull
    @Override
    public RecycleAdp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customrec,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecycleAdp.MyViewHolder myViewHolder, final int i) {

        Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+veh_image.get(i)).resize(550,300).into(myViewHolder.vehicleimage);
        myViewHolder.brand.setText(veh_title.get(i)+", "+veh_brand.get(i));
        myViewHolder.bookingtodate.setText(todate.get(i));
        myViewHolder.bookingfromdate.setText(fromdate.get(i));
        myViewHolder.bookingdates.setText(bookingdate.get(i));
        myViewHolder.usernames.setText(username.get(i));
        myViewHolder.booking_message.setText(messages.get(i));
        if (bookingstatus.get(i).equals("0")){
            myViewHolder.status.setText("InActive");
        }else if (bookingstatus.get(i).equals("1")){
            myViewHolder.status.setText("Active");
        }
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(context,MainActivity3.class);
                intent.putExtra("id",""+id.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView brand,bookingtodate,bookingfromdate,bookingdates,booking_message,usernames,status;
        LinearLayout linearLayout;
        ImageView vehicleimage;
        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status);
            vehicleimage=itemView.findViewById(R.id.vehicle_img);
            brand=itemView.findViewById(R.id.veh_name);
            usernames=itemView.findViewById(R.id.username);
            bookingtodate=itemView.findViewById(R.id.upto_date);
            bookingfromdate=itemView.findViewById(R.id.from_date);
            bookingdates=itemView.findViewById(R.id.booking_date);
            booking_message=itemView.findViewById(R.id.messages);
            linearLayout=itemView.findViewById(R.id.layoutinfo);
        }
    }
}
