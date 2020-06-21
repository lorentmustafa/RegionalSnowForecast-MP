package com.fiek.regionalsnowforecast;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class BrezovicaResortFragment extends Fragment {

    private TextView tvSnow, tvHumidity, tvRain, tvFreezingLevel, tvWind;
    private TextView tvMorning, tvNoon, tvAfternoon;
    private ImageView ivMorning, ivNoon, ivAfternoon;
    private TextView tvResortName;
    private ProgressBar progressBar;
    private Forecast forecastobj = new Forecast();
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
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        tvMorning = view.findViewById(R.id.tvMorningTemp);
        tvNoon = view.findViewById(R.id.tvMiddayTemp);
        tvAfternoon = view.findViewById(R.id.tvAfternoonTemp);
        tvSnow = view.findViewById(R.id.tvSnow);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvRain = view.findViewById(R.id.tvRain);
        tvFreezingLevel = view.findViewById(R.id.tvFreezingLevel);
        tvWind = view.findViewById(R.id.tvWind);
        ivMorning = view.findViewById(R.id.morningWeatherIcon);
        ivNoon = view.findViewById(R.id.middayWeatherIcon);
        ivAfternoon = view.findViewById(R.id.afternoonWeatherIcon);
        tvResortName = view.findViewById(R.id.tvResortName);
        progressBar = view.findViewById(R.id.forecastProgressBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAsync(view);
    }

    public void startAsync(View v){
        restAsync restTask = new restAsync();
        restTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class restAsync extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                forecastobj.get_setDataFromApi(ivMorning, ivNoon, ivAfternoon, getActivity(), locationId, appId, appKey);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return getResources().getString(R.string.fetching_data);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            tvResortName.setText(String.valueOf(forecastobj.getResortname()));
            tvMorning.setText(new StringBuilder(forecastobj.getTemp() + "\u00B0" + "C"));
            tvNoon.setText(new StringBuilder(forecastobj.getTempnoon() + "\u00B0" + "C"));
            tvAfternoon.setText(new StringBuilder(forecastobj.getTempafternoon() + "\u00B0" + "C"));
            tvSnow.setText(new StringBuilder(forecastobj.getSnow() + "cm"));
            tvHumidity.setText(new StringBuilder(forecastobj.getHumidity() + "%"));
            tvFreezingLevel.setText(new StringBuilder(forecastobj.getFreezinglevel() + "m"));
            tvRain.setText(new StringBuilder(forecastobj.getRain() + "mm"));
            tvWind.setText(new StringBuilder(forecastobj.getWind()+"km/h"));
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            progressBar.setProgress(0);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0]);
        }
    }
}
