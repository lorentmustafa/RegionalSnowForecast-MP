package com.fiek.regionalsnowforecast;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResortsAdapter extends ArrayAdapter<Resorts> {

    private Context context;
    List<Resorts> favorites;
    Utils utils;

    public ResortsAdapter(Context context, List<Resorts> favorites) {
        super(context, R.layout.layout_myresortslistitem, favorites);
        this.context = context;
        this.favorites = favorites;
        utils = new Utils();
    }

    private class ViewHolder {
        CircleImageView favResortImage;
        TextView tvResortName;
        TextView tvResortLocation;
        ImageView btnAddFav;
        RelativeLayout listItem;
    }

    @Override
    public int getCount(){
        return favorites.size();
    }

    @Override
    public Resorts getItem(int position){
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_myresortslistitem, null);
            holder = new ViewHolder();
            holder.favResortImage = (CircleImageView) convertView
                    .findViewById(R.id.myresortimage);
            holder.tvResortName = (TextView) convertView
                    .findViewById(R.id.myresortname);
            holder.tvResortLocation = (TextView) convertView
                    .findViewById(R.id.myresortlocation);
            holder.btnAddFav = (ImageView) convertView
                    .findViewById(R.id.addFav);
            holder.listItem = (RelativeLayout) convertView.findViewById(R.id.resorts_layout_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Resorts favoriteResort = (Resorts) getItem(position);
        holder.favResortImage.setImageResource(favoriteResort.getrImage());
        holder.tvResortName.setText(favoriteResort.getName());
        holder.tvResortLocation.setText(favoriteResort.getLocation());
        holder.listItem.setBackgroundResource(favoriteResort.getrListitemImage());
        if (checkFavoriteResort(favoriteResort)) {
            holder.btnAddFav.setImageResource(R.drawable.ic_remove);
            holder.btnAddFav.setTag("added");
        } else {
            holder.btnAddFav.setImageResource(R.drawable.ic_add);
            holder.btnAddFav.setTag("notAdded");
        }

        return convertView;
    }


    public boolean checkFavoriteResort(Resorts checkResort){
        boolean check = false;
        List<Resorts> favorites = utils.getFavorites(context);
        if(favorites != null) {
            for(Resorts favoriteResort : favorites){
                if(favoriteResort.equals(checkResort)){
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(Resorts favoriteResort){
        super.add(favoriteResort);
        favorites.add(favoriteResort);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Resorts favoriteResort){
        super.remove(favoriteResort);
        favorites.remove(favoriteResort);
        notifyDataSetChanged();
    }
}
