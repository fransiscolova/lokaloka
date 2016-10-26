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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterUser extends Activity implements AnimationListener {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParserObj jsonParser = new JSONParserObj();
	// products JSONArray
	String cek = "";
	Animation animBounce;
	ImageView imgPoster;
	SessionManager session;
	EditText name;
	EditText email;
	TextView loginText;
	String type;
	// url to create new product
	private static String url_create_product = Config.register_api;

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().hide();
		session = new SessionManager(getApplicationContext());
		// load the animation
		imgPoster = (ImageView) findViewById(R.id.logo_antique);
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.sequential);
		animBounce.setAnimationListener(this);
		imgPoster.startAnimation(animBounce);

		Button btReg = (Button) findViewById(R.id.btnReg);
		loginText = (TextView) findViewById(R.id.link_to_log);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);

		// Listening to register new account link
		btReg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				new Register().execute();
			}
		});
		
		
		loginText.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				

				Intent i = new Intent(getApplicationContext(),
						Login.class);
				startActivity(i);
			}
		});
		
		
		
		
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
			pDialog = new ProgressDialog(RegisterUser.this);
			pDialog.setMessage("Register User..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name.getText().toString()));
			params.add(new BasicNameValuePair("email", email.getText()
					.toString()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					cek = "1";
					
				} else {
					// failed to create product
					showAlert("Failed");
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
			pDialog.dismiss();
			if (cek == "1") {
				showAlert("Success register user");
				Intent i = new Intent(getApplicationContext(), Login.class);
				startActivity(i);
				// closing this screen
				finish();
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
}
