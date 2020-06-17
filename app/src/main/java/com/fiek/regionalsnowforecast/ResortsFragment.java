package com.fiek.regionalsnowforecast;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ResortsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private RelativeLayout relativeLayout;
    private Activity activity;
    private ListView resortsListView;
    private List<Resorts> favorites;
    private ResortsAdapter resortsAdapter;
    private Utils utils;

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
        relativeLayout = (RelativeLayout) view.findViewById(R.id.resortsfragment);
        setFavoriteResorts();

        resortsAdapter = new ResortsAdapter(activity, favorites);
        resortsListView.setAdapter(resortsAdapter);
        resortsListView.setOnItemClickListener(this);
        resortsListView.setOnItemLongClickListener(this);

        return view;
    }

    private void setFavoriteResorts() {
        Resorts resort1 = new Resorts(1, "Brezovica Resort", "Shterpce, Kosovo", R.drawable.brezovicaview, R.drawable.brezovicaview);
        Resorts resort2 = new Resorts(2, "Mavrovo Resort", "Polog, North Macedonia", R.drawable.mavrovoview, R.drawable.mavrovoview);
        Resorts resort3 = new Resorts(3, "Popova Sapka Resort", "Tetovo, North Macedonia", R.drawable.popovasapkaview,R.drawable.popovasapkaview);
        Resorts resort4 = new Resorts(4, "Bansko Resort", "Bansko, Bulgaria", R.drawable.banskoview, R.drawable.banskoview);
        Resorts resort5 = new Resorts(5, "Kolasin Resort", "Kolasin, Montenegro", R.drawable.kolasinview,R.drawable.kolasinview);

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
//        Toast.makeText(activity, favoriteResort.toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, final int position, long arg3) {
        final ImageView button = (ImageView) view.findViewById(R.id.addFav);
        String tag = button.getTag().toString();

        if(tag.equalsIgnoreCase("notAdded")){
            utils.addFavorite(activity, favorites.get(position));
            button.setTag("added");
            button.setImageResource(R.drawable.ic_remove);
//            Toast.makeText(activity, activity.getResources().getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(relativeLayout, "Resort added to Favorites", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            utils.removeFavorite(activity, favorites.get(position));
                            button.setTag("notAdded");
                            button.setImageResource(R.drawable.ic_add);
                            Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
                        }
                    });
            snackbar.show();
        } else {
            utils.removeFavorite(activity, favorites.get(position));
            button.setTag("notAdded");
            button.setImageResource(R.drawable.ic_add);
            Toast.makeText(activity, activity.getResources().getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
        }
        return true;
    }


}
