package com.carolinamanzanares.earthquakes.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.carolinamanzanares.earthquakes.Model.Coordinate;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;
import com.carolinamanzanares.earthquakes.database.EarthQuakeDB;

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

/**
 * Created by cursomovil on 25/03/15.
 */
public class DownloadEarthquakesTask extends AsyncTask<String, EarthQuakes, Integer> {

    private EarthQuakeDB earthQuakeDB;

    public interface AddEarthQuakeInterface {
        public void notifyTotal(int total);
    }

    public static final String EARTHQUAKE = "EARTHQUAKE";
    private AddEarthQuakeInterface target;

    public DownloadEarthquakesTask(Context context, AddEarthQuakeInterface target){
        this.target = target;

        earthQuakeDB = new EarthQuakeDB(context);
    }

    @Override
    protected Integer doInBackground(String... urls) {
       int count = 0;
        if (urls.length > 0) {
            count = updateEarthQuakes(urls[0]);
        }
        return count;
    }

    @Override
    protected void onProgressUpdate(EarthQuakes... earthQuakes) {
        super.onProgressUpdate(earthQuakes);
    }

    private Integer updateEarthQuakes(String earthquakesFeed) {

        JSONObject json;
        JSONArray earthquakes = null;

        //String earthquakesFeed = getString(R.string.earthquakes_url);


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
                }
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

    private void processEarthQuakeTask(JSONObject jsonObj) {
        try {
            JSONArray jsonCoords = jsonObj.getJSONObject("geometry").getJSONArray("coordinates");
            Coordinate coords = new Coordinate(jsonCoords.getDouble(0), jsonCoords.getDouble(1), jsonCoords.getDouble(2));

            JSONObject properties = jsonObj.getJSONObject("properties");

            EarthQuakes earthquakes = new EarthQuakes();
            earthquakes.setId(jsonObj.getString("id"));
            earthquakes.setPlace(properties.getString("place"));
            earthquakes.setMagnitude(properties.getDouble("mag"));
            earthquakes.setTime(properties.getLong("time"));
            earthquakes.setURL(properties.getString("url"));
            earthquakes.setCoords(coords);

            Log.d(EARTHQUAKE, earthquakes.toString());

            publishProgress(earthquakes);

            //insert in DB
            earthQuakeDB.insert(earthquakes);





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        target.notifyTotal(integer);
    }
}
