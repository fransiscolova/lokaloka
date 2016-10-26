package com.lokaloka.frans.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import com.lokaloka.frans.helper.GPSTracker;
import com.lokaloka.frans.helper.JSONParser;
import com.lokaloka.frans.helper.JSONParserObj;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


@SuppressLint("NewApi")
public class MyService extends Service {
	int i = 0;
	String id;
	public static final String SERVER_URL = "http://klikrent.com/";
	public static final String event_api = SERVER_URL
			+ "Android/android_connect/PhotoSeru/get_all_event.php";
	public static final String register_api=SERVER_URL + "Android/android_connect/PhotoSeru/register_user.php";
	
	private static final String url_event = event_api;
	String cek;
	String json_text;
	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();
	JSONParserObj jsonParsers = new JSONParserObj();

	ArrayList<HashMap<String, String>> hashtag;

	// albums JSONArray
	JSONArray albums = null;

	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Intent restartServiceIntent = new Intent(getApplicationContext(),
				this.getClass());
		restartServiceIntent.setPackage(getPackageName());

		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					// do your code here
					// json_text="Koneksi Gagal";
					// new download().execute("","","");
					 show(json_text);
					 new Register().execute("","","");
					

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// also call the same runnable to call it at regular
					// interval
					handler.postDelayed(this, 7000);
				}
			}
		};
		handler.postDelayed(runnable, 7000);

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

	}

	public void show(String text) {
		i++;
		getLocation();
		//Toast.makeText(this, text + " " + i, Toast.LENGTH_LONG).show();
	}

	public void getLocation() {
		GPSTracker gps = new GPSTracker(getApplicationContext());
		
		// check if GPS enabled
		if (gps.canGetLocation()) {
			json_text="";
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			json_text = "\nLat: " + latitude + "\nLong: "
					+ longitude;

		} else {			
			gps.showSettingsAlert();
		}
	}

	class download extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		/**
		 * getting Albums JSON
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			id = "1";
			params.add(new BasicNameValuePair("idUser", id));

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(url_event, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Albums JSON: ", "> " + json + "->" + id);

			try {
				albums = new JSONArray(json);

				if (albums != null) {
					// looping through All albums

					json_text = json;

				} else {
					Log.d("Albums: ", "null");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

		}

	}

	
	
	/**
	 * Background Async Task to Create new product
	 * */
	class Register extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", json_text));
			params.add(new BasicNameValuePair("email", "Fransiscolova@gmail"));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParsers.makeHttpRequest(register_api,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				 String TAG_SUCCESS = "success";
				int success = json.getInt(TAG_SUCCESS);
				cek="0";
				if (success == 1) {
					// successfully created product
					cek = "1";
					//json_text="OK";
					
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}


			

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
		
		}

	}
}