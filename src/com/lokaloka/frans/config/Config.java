package com.lokaloka.frans.config;

public class Config {
	// File upload url (replace the ip with your server address)
	
	public static final String SERVER_URL = "http://klikrent.com/";
	
	//User 
	public static final String login_api=SERVER_URL + "Android/android_connect/PhotoSeru/login.php";
	public static final String register_api=SERVER_URL + "Android/android_connect/PhotoSeru/register_user.php";
	
	
	public static final String photo_upload=SERVER_URL+"Android/android_connect/PhotoSeru/fileUpload.php";
	public static final String hashtag_api=SERVER_URL + "Android/android_connect/PhotoSeru/get_all_hashtag.php";
	public static final String event_api=SERVER_URL + "Android/android_connect/PhotoSeru/get_all_event.php";
	public static final String addevent_api=SERVER_URL + "Android/android_connect/PhotoSeru/addEvent.php";
	public static final String createevent_api=SERVER_URL + "Android/android_connect/PhotoSeru/createEvent.php";

	public static final String photo_api=SERVER_URL + "Android/android_connect/PhotoSeru/get_all_photo.php";
	public static final String photo_folder=SERVER_URL + "Android/android_connect/PhotoSeru/photos/";
	
	
	
	// Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "PhotoSeru";
}
