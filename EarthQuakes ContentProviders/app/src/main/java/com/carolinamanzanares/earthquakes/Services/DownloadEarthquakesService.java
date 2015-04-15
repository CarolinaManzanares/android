package com.carolinamanzanares.earthquakes.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import com.carolinamanzanares.earthquakes.MainActivity;
import com.carolinamanzanares.earthquakes.Model.Coordinate;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;
import com.carolinamanzanares.earthquakes.providers.EarthQuakeDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadEarthquakesService extends Service {

    private EarthQuakeDB earthQuakeDB;

    @Override
    public void onCreate() {
        super.onCreate();

        earthQuakeDB = new EarthQuakeDB(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d("EARTHQUAKES", "Received start id " + startId + ": " + intent);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                updateEarthQuakes(getString(R.string.earthquakes_url));
            }
        });
        t.start();

        return Service.START_STICKY;
    }

    private Integer updateEarthQuakes(String earthquakesFeed) {

        JSONObject json;
        JSONArray earthquakes = null;

        int count = 0;

        try {
            URL url = new URL(earthquakesFeed);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader streamReader = new BufferedReader(
                        new InputStreamReader(
                                httpConnection.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                json = new JSONObject(responseStrBuilder.toString());
                earthquakes = json.getJSONArray("features");

                for (int i = earthquakes.length()-1; i >= 0; i--) {
                    processEarthQuakeTask(earthquakes.getJSONObject(i));
                    count++;
                }

                sendNotifications(count);


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return earthquakes.length();
    }

    private void sendNotifications(int count) {

        Intent intentToFire = new Intent(this, MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, intentToFire, 0);

        Notification.Builder builder = new Notification.Builder(DownloadEarthquakesService.this);

        builder.setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            //.setContentText(getString(R.string.count_earthquakes, count))
            .setWhen(System.currentTimeMillis())
            .setDefaults(Notification.DEFAULT_SOUND)
            .setSound(
                    RingtoneManager.getDefaultUri(
                            RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .setContentIntent(notificationIntent);

            Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int NOTIFICATION_REF = 1;
        notificationManager.notify(NOTIFICATION_REF, notification);

    }

    private void processEarthQuakeTask(JSONObject jsonObj) {
        try {
            JSONArray jsonCoords = jsonObj.getJSONObject("geometry").getJSONArray("coordinates");
            Coordinate coords = new Coordinate(jsonCoords.getDouble(1), jsonCoords.getDouble(0), jsonCoords.getDouble(2));

            JSONObject properties = jsonObj.getJSONObject("properties");

            EarthQuakes earthquakes = new EarthQuakes();
            earthquakes.setId(jsonObj.getString("id"));
            earthquakes.setPlace(properties.getString("place"));
            earthquakes.setMagnitude(properties.getDouble("mag"));
            earthquakes.setTime(properties.getLong("time"));
            earthquakes.setURL(properties.getString("url"));
            earthquakes.setCoords(coords);

            //insert in DB
            earthQuakeDB.insert(earthquakes);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
