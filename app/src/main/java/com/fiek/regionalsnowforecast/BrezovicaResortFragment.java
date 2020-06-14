package com.fiek.regionalsnowforecast;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


public class BrezovicaResortFragment extends Fragment {

    private TextView tvTemp, tvSnow, tvHumidity, tvRain, tvFreezingLevel;
    private ImageView ivWeatherIcon;
    private RequestQueue mQueue;
    Utils utils = new Utils();
    private final String locationId = "54888411";
    private final String appId = "2563c48e";
    private final String appKey = "760b181c8ad4b69f14235007b01cdebf";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brezovica, container, false);

        tvTemp = view.findViewById(R.id.tvTemp);
        tvSnow = view.findViewById(R.id.tvSnow);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvRain = view.findViewById(R.id.tvRain);
        tvFreezingLevel = view.findViewById(R.id.tvFreezingLevel);
        ivWeatherIcon = view.findViewById(R.id.weatherIcon);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        utils.jsonParse(tvSnow, tvHumidity, tvTemp, ivWeatherIcon, getActivity(), locationId, appId, appKey);

    }


}
