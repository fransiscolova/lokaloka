package com.lokaloka.frans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lokaloka.frans.config.Config;
import com.lokaloka.frans.helper.JSONParserObj;
import com.lokaloka.frans.helper.SessionManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Login extends Activity implements AnimationListener,
		OnItemSelectedListener {

	EditText txtName;
	EditText txtPrice;
	EditText txtDesc;
	EditText txtCreatedAt;
	Button btnSave;
	Button btnDelete;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParserObj jsonParser = new JSONParserObj();

	// single product url
	private static final String url_login = Config.login_api;

	SessionManager session;

	Animation animBounce;
	ImageView imgPoster;

	EditText name;
	EditText email;
	TextView register;

	String type;
	String username = "";
	String useremail = "";
	String userid = "";
	Spinner spinner;
	int success;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_USER = "user";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().hide();
		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn()) {
			HashMap<String, String> user = session.getUserDetails();
			if (user.get(SessionManager.TYPE).contains("Client")) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
			} else {
				Intent i = new Intent(getApplicationContext(), Getmaps.class);
				startActivity(i);
				finish();
			}
		}

		// load the animation
		imgPoster = (ImageView) findViewById(R.id.logo_antique);
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.sequential);
		animBounce.setAnimationListener(this);
		imgPoster.startAnimation(animBounce);

		Button btlog = (Button) findViewById(R.id.btnLogin);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);

		// Spinner element
		spinner = (Spinner) findViewById(R.id.spinner);

		// Spinner click listener
		spinner.setOnItemSelectedListener(this);

		List<String> categories = new ArrayList<String>();
		categories.add("Set As Type");
		categories.add("Client");
		categories.add("Server");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);

		register = (TextView) findViewById(R.id.link_to_register);
		// Listening to register new account link
		btlog.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (spinner.getSelectedItem().toString().contains("Set As Type")) {
					showAlert("Select Type Client or Server");
				} else {
				new LoginUser().execute();
				}
				}
		});

		register.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						RegisterUser.class);
				startActivity(i);

			}
		});

	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class LoginUser extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Login. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... args) {

			// updating UI from Background Thread
			// runOnUiThread(new Runnable() {
			// public void run() {
			// Check for success tag

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", name.getText()
						.toString()));
				params.add(new BasicNameValuePair("email", email.getText()
						.toString()));

				// getting product details by making HTTP request
				// Note that product details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(url_login, "GET",
						params);

				// check your log for json response
				Log.d("Single Product Details", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully received product details
					JSONArray userObj = json.getJSONArray(TAG_USER); // JSON
																		// Array

					// get first product object from JSON Array
					JSONObject user = userObj.getJSONObject(0);
					session.createLoginSession(user.getString("idUser"),
							user.getString("name"), user.getString("email"),
							spinner.getSelectedItem().toString());

					if (spinner.getSelectedItem().toString().contains("Client")) {
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(i);

						// closing this screen
						finish();
					} else {
						Intent i = new Intent(getApplicationContext(),
								Getmaps.class);
						startActivity(i);
						finish();
					}
				} else {
					// product with pid not found
					success = 0;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// }
			// });

			return null;
		}

		/**
		 * d After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();

			if (success == 0) {
				showAlert("Email or Name not found ");
			}

		}
	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
