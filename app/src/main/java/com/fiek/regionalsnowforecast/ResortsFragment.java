package com.fiek.regionalsnowforecast;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ResortsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Activity activity;
    ListView resortsListView;
    List<Resorts> favorites;
    ResortsAdapter resortsAdapter;
    Utils utils;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getActivity();
        utils = new Utils();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_resorts, container, false);

        findViewsById(view);

        setFavoriteResorts();

        resortsAdapter = new ResortsAdapter(activity, favorites);
        resortsListView.setAdapter(resortsAdapter);
        resortsListView.setOnItemClickListener(this);
        resortsListView.setOnItemLongClickListener(this);

        return view;
    }


    private void setFavoriteResorts() {
        Resorts resort1 = new Resorts(1, "Brezovica Resort", "Shterpce, Kosovo", R.drawable.brezobackground);
        Resorts resort2 = new Resorts(2, "Mavrovo Resort", "Shterpce, Kosovo", R.drawable.brezobackground);
        Resorts resort3 = new Resorts(3, "Popova Sapka Resort", "Shterpce, Kosovo", R.drawable.brezobackground);
        Resorts resort4 = new Resorts(4, "Bansko Resort", "Shterpce, Kosovo", R.drawable.brezobackground);
        Resorts resort5 = new Resorts(5, "Kolasin Resort", "Shterpce, Kosovo", R.drawable.brezobackground);

        favorites = new ArrayList<Resorts>();
        favorites.add(resort1);
        favorites.add(resort2);
        favorites.add(resort3);
        favorites.add(resort4);
        favorites.add(resort5);

    }

    private void findViewsById(View view) {
        resortsListView = (ListView) view.findViewById(R.id.list_resorts);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Resorts favoriteResort = (Resorts) parent.getItemAtPosition(position);
        Toast.makeText(activity, favoriteResort.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
        ImageView button = (ImageView) view.findViewById(R.id.addFav);
        String tag = button.getTag().toString();
        if(tag.equalsIgnoreCase("notAdded")){
            utils.addFavorite(activity, favorites.get(position));
            Toast.makeText(activity, activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
            button.setTag("added");
            button.setImageResource(R.drawable.ic_remove);
        } else {
            utils.removeFavorite(activity, favorites.get(position));
            button.setTag("notAdded");
            button.setImageResource(R.drawable.ic_add);
            Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
