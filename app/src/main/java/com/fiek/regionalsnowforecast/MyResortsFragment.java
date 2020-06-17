package com.fiek.regionalsnowforecast;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;

public class MyResortsFragment extends Fragment {

    private ListView myResortsList;
    private Activity activity;
    private Utils utils;
    private List<Resorts> favorites;
    private ResortsAdapter resortsAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resorts, container, false);
        utils = new Utils();
        favorites = utils.getFavorites(activity);

        if (favorites == null) {
            showAlert(getResources().getString(R.string.no_favorites_items),
                    getResources().getString(R.string.no_favorites_msg));
        } else {
            if (favorites.size() == 0) {
                showAlert(
                        getResources().getString(R.string.no_favorites_items),
                        getResources().getString(R.string.no_favorites_msg));
            }

            myResortsList = (ListView) view.findViewById(R.id.list_resorts);
            if (favorites != null) {
                resortsAdapter = new ResortsAdapter(activity, favorites);
                myResortsList.setAdapter(resortsAdapter);

                myResortsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                        Resorts favoriteResort = (Resorts) parent.getItemAtPosition(position);
                        switch (favoriteResort.getrId()){
                            case 1:
                                (getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new BrezovicaResortFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 2:
                                (getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new MavrovoResortFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 3:
                                (getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new PopovaSapkaResortFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 4:
                                (getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new BanskoResortFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case 5:
                                (getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new KolasinResortFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                myResortsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        ImageView button = (ImageView) view.findViewById(R.id.addFav);
                        String tag = button.getTag().toString();

                        if (tag.equalsIgnoreCase("notAdded")) {
                            utils.addFavorite(activity, favorites.get(position));
                            Toast.makeText(activity, activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
                            button.setTag("added");
                            button.setImageResource(R.drawable.ic_remove);
                        } else {
                            utils.removeFavorite(activity, favorites.get(position));
                            Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
                            button.setTag("notAdded");
                            button.setImageResource(R.drawable.ic_add);
                        }
                        return true;
                    }
                });
            }
        }
        return view;
    }

    public void showAlert(String title, String message) {
        if (activity != null && !activity.isFinishing()) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);

            // setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // activity.finish();
                            getFragmentManager().popBackStackImmediate();
                        }
                    });
            alertDialog.show();
        }
    }
}
