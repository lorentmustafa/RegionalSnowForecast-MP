package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Forecast {
    private String resortname;
    private int temp;
    private int tempnoon;
    private int tempafternoon;
    private double snow;
    private double rain;
    private int humidity;
    private int freezinglevel;
    private int wind;
    public RequestQueue mQueue;


    public Forecast() {
        super();
    }

    public int getTempnoon() {
        return tempnoon;
    }

    public void setTempnoon(int tempnoon) {
        this.tempnoon = tempnoon;
    }

    public int getTempafternoon() {
        return tempafternoon;
    }

    public void setTempafternoon(int tempafternoon) {
        this.tempafternoon = tempafternoon;
    }

    public String getResortname() {
        return resortname;
    }

    public void setResortname(String resortname) {
        this.resortname = resortname;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getFreezinglevel() {
        return freezinglevel;
    }

    public void setFreezinglevel(int freezinglevel) {
        this.freezinglevel = freezinglevel;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public void get_setDataFromApi(final ImageView ivMorning, final ImageView ivNoon, final ImageView ivAfternoon, final Context context, String locationId, String appId, String appKey) {

        String url = "https://api.weatherunlocked.com/api/resortforecast/" + locationId + "?num_of_days=1&app_id=" + appId + "&app_key=" + appKey;
        mQueue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setResortname(response.getString("name"));
                            JSONArray jsonArray = response.getJSONArray("forecast");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject forecast = jsonArray.getJSONObject(i);

                                if (forecast.getString("time").equals("10:00") || forecast.getString("time").equals("11:00")) {
                                    setSnow(forecast.getDouble("snow_mm")/10);
                                    setHumidity((int) forecast.getDouble("hum_pct"));
                                    setFreezinglevel((int) forecast.getDouble("frzglvl_m"));
                                    setRain(forecast.getDouble("rain_mm"));}
                                for (int j = 0; j < forecast.length(); j++) {
                                    JSONObject base = forecast.getJSONObject("base");
                                    setWind((int) base.getDouble("windspd_kmh"));
                                    if(forecast.getString("time").equals("07:00") || forecast.getString("time").equals("08:00")){
                                        JSONObject morningbase = forecast.getJSONObject("base");
                                        CheckWeatherIcon(morningbase, ivMorning, context);
                                        setTemp((int) morningbase.getDouble("temp_c"));
                                    }

                                    if(forecast.getString("time").equals("13:00") || forecast.getString("time").equals("14:00")){
                                        JSONObject noonbase = forecast.getJSONObject("base");
                                        CheckWeatherIcon(noonbase, ivNoon, context);
                                        setTempnoon((int) noonbase.getDouble("temp_c"));
                                    }

                                    if(forecast.getString("time").equals("19:00") || forecast.getString("time").equals("20:00") ){
                                        JSONObject noonbase = forecast.getJSONObject("base");
                                        CheckWeatherIcon(noonbase, ivAfternoon, context);
                                        setTempafternoon((int) noonbase.getDouble("temp_c"));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    public void CheckWeatherIcon(JSONObject object, ImageView iv, Context context) throws JSONException {
        switch(object.getString("wx_icon")){
            case "Blizzard.gif":
                Glide.with(context).load(R.drawable.blizzard).into(iv);
                break;
            case "Clear.gif":
                Glide.with(context).load(R.drawable.clear).into(iv);
                break;
            case "CloudRainThunder.gif":
                Glide.with(context).load(R.drawable.cloudrainthunder).into(iv);
                break;
            case "CloudSleetSnowThunder.gif":
                Glide.with(context).load(R.drawable.cloudsleetsnowthunder).into(iv);
                break;
            case "Cloudy.gif":
                Glide.with(context).load(R.drawable.cloudy).into(iv);
                break;
            case "Fog.gif":
                Glide.with(context).load(R.drawable.fog).into(iv);
                break;
            case "FreezingDrizzle.gif":
                Glide.with(context).load(R.drawable.freezingdrizzle).into(iv);
                break;
            case "FreezingFog.gif":
                Glide.with(context).load(R.drawable.freezingfog).into(iv);
                break;
            case "FreezingRain.gif":
                Glide.with(context).load(R.drawable.freezingrain).into(iv);
                break;
            case "HeavyRain.gif":
                Glide.with(context).load(R.drawable.heavyrain).into(iv);
                break;
            case "HeavyRainSwrsDay.gif":
                Glide.with(context).load(R.drawable.heavyrainswrsday).into(iv);
                break;
            case "HeavyRainSwrsNight.gif":
                Glide.with(context).load(R.drawable.heavyrainswrsnight).into(iv);
                break;
            case "HeavySleetSwrsDay.gif":
                Glide.with(context).load(R.drawable.heavysleetswrsday).into(iv);
                break;
            case "HeavySleetSwrsNight.gif":
                Glide.with(context).load(R.drawable.heavysleetswrsnight).into(iv);
                break;
            case "HeavySnow.gif":
                Glide.with(context).load(R.drawable.heavysnow).into(iv);
                break;
            case "HeavySnowSwrsDay.gif":
                Glide.with(context).load(R.drawable.heavysnowswrsday).into(iv);
                break;
            case "HeavySnowSwrsNight.gif":
                Glide.with(context).load(R.drawable.heavysnowswrsnight).into(iv);
                break;
            case "IsoRainSwrsDay.gif":
                Glide.with(context).load(R.drawable.isorainswrsday).into(iv);
                break;
            case "IsoRainSwrsNight.gif":
                Glide.with(context).load(R.drawable.isorainswrsnight).into(iv);
                break;
            case "IsoSleetSwrsDay.gif":
                Glide.with(context).load(R.drawable.isosleetswrsday).into(iv);
                break;
            case "IsoSleetSwrsNight.gif":
                Glide.with(context).load(R.drawable.isosleetswrsnight).into(iv);
                break;
            case "IsoSnowSwrsDay.gif":
                Glide.with(context).load(R.drawable.isosnowswrsday).into(iv);
                break;
            case "IsoSnowSwrsNight.gif":
                Glide.with(context).load(R.drawable.isosnowswrsnight).into(iv);
                break;
            case "mist.gif":
                Glide.with(context).load(R.drawable.mist).into(iv);
                break;
            case "ModRain.gif":
                Glide.with(context).load(R.drawable.modrain).into(iv);
                break;
            case "ModRainSwrsDay.gif":
                Glide.with(context).load(R.drawable.modrainswrsday).into(iv);
                break;
            case "ModSleet.gif":
                Glide.with(context).load(R.drawable.modsleet).into(iv);
                break;
            case "ModSleetSwrsDay.gif":
                Glide.with(context).load(R.drawable.modsleetswrsday).into(iv);
                break;
            case "ModSleetSwrsNight.gif":
                Glide.with(context).load(R.drawable.modsleetswrsnight).into(iv);
                break;
            case "ModSnow.gif":
                Glide.with(context).load(R.drawable.modsnow).into(iv);
                break;
            case "ModSnowSwrsDay.gif":
                Glide.with(context).load(R.drawable.modsnowswrsday).into(iv);
                break;
            case "ModSnowSwrsNight.gif":
                Glide.with(context).load(R.drawable.modsnowswrsnight).into(iv);
                break;
            case "OccLightRain.gif":
                Glide.with(context).load(R.drawable.occlightrain).into(iv);
                break;
            case "OccLightSleet.gif":
                Glide.with(context).load(R.drawable.occlightsleet).into(iv);
                break;
            case "OccLightSnow.gif":
                Glide.with(context).load(R.drawable.occlightsnow).into(iv);
                break;
            case "Overcast.gif":
                Glide.with(context).load(R.drawable.overcast).into(iv);
                break;
            case "PartCloudRainThunderDay.gif":
                Glide.with(context).load(R.drawable.partcloudrainthunderday).into(iv);
                break;
            case "PartCloudRainThunderNight.gif":
                Glide.with(context).load(R.drawable.partcloudrainthundernight).into(iv);
                break;
            case "PartCloudSleetSnowThunderDay.gif":
                Glide.with(context).load(R.drawable.partcloudsleetsnowthunderday).into(iv);
                break;
            case "PartCloudSleetSnowThunderNight.gif":
                Glide.with(context).load(R.drawable.partcloudsleetsnowthundernight).into(iv);
                break;
            case "PartlyCloudyDay.gif":
                Glide.with(context).load(R.drawable.partlycloudyday).into(iv);
                break;
            case "PartlyCloudyNight.gif":
                Glide.with(context).load(R.drawable.partlycloudynight).into(iv);
                break;
            case "Sunny.gif":
                Glide.with(context).load(R.drawable.sunny).into(iv);
                break;
            default:
                Glide.with(context).load(R.drawable.heavysnow).into(iv);
        }
    }
}
