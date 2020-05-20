package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyResortsAdapter extends RecyclerView.Adapter<MyResortsAdapter.ViewHolder>{

    private ArrayList<String> mResortNames = new ArrayList<>();
    private ArrayList<String> mResortImages = new ArrayList<>();
    private Context mContext;


    public MyResortsAdapter(Context mContext, ArrayList<String> mResortNames, ArrayList<String> mResortImages) {
        this.mResortNames = mResortNames;
        this.mResortImages = mResortImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_myresortslistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mResortImages.get(position))
                .into(holder.myresortimage);

        holder.myResortName.setText(mResortNames.get(position));

        holder.myResortListLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mResortNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResortNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView myresortimage;
        TextView myResortName;
        RelativeLayout myResortListLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myresortimage = itemView.findViewById(R.id.myresortimage);
            myResortName = itemView.findViewById(R.id.myresortname);
            myResortListLayout = itemView.findViewById(R.id.myresortlistlayout);
        }
    }
}
