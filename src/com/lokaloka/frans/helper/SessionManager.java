package com.lokaloka.frans.helper;

import java.util.HashMap;




import com.lokaloka.frans.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "photoseru";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	
	

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String id, String name,String email,String type) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// simpan id
		editor.putString(ID, id);
		editor.putString(NAME, name);
		editor.putString(EMAIL, email);
		editor.putString(TYPE, type);
		

		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}

	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(NAME, pref.getString(NAME, null));
		user.put(EMAIL, pref.getString(EMAIL, null));
		user.put(ID, pref.getString(ID, null));
		user.put(TYPE, pref.getString(TYPE, null));
		
		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// sesudah logout arahkan ke welcome
		Intent i = new Intent(_context, MainActivity.class);

		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// jalankan login activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/

	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
