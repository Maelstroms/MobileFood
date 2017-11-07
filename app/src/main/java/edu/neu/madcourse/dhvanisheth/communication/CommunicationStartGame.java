package edu.neu.madcourse.dhvanisheth.communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dhvanisheth.R;

//import com.dharammaniar.androidexamples.R;

public class CommunicationStartGame extends Activity {

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_STORED_YES_NO = "stored_name_yes_no";
	private static final String PROPERTY_STORED_NAME = "stored_name";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM Sample Demo";
	TextView mDisplay;
	EditText mMessage;
	GoogleCloudMessaging gcm;
	Context context;
	String regid;
	RemoteClient remoteClient;
	EditText user_name;
	EditText other_player_name;
	Button send_message_button;
	Button register_button;
	Button acknowledgements_button;
    Button unregister_button;
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;

    String reg_device;
    String username;
	private AlertDialog mDialog_acknowledgements;


	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.communication_gcm);
		user_name = (EditText) findViewById(R.id.communication_user_name);
		other_player_name = (EditText) findViewById(R.id.communication_other_player_name);
		send_message_button = (Button) findViewById(R.id.communication_send_message_button);
		register_button = (Button) findViewById(R.id.communication_register_button);
        unregister_button = (Button) findViewById(R.id.communication_unregister_button);
		acknowledgements_button = (Button) findViewById(R.id.communication_acknowledgements_button);
        gcm = GoogleCloudMessaging.getInstance(this);
        context = getApplicationContext();
//        removeRegistrationId(getApplicationContext());
		final SharedPreferences prefs = getGCMPreferences(context);
//        int appVersion = getAppVersion(context);
		boolean already_registered = prefs.getBoolean(PROPERTY_STORED_YES_NO, false);
        username = prefs.getString(PROPERTY_STORED_NAME, "");
		if (already_registered){
			LinearLayout rlayout1 = (LinearLayout) findViewById(R.id.communication_register);
			rlayout1.setVisibility(rlayout1.GONE);
		}
		else{
			LinearLayout rlayout2 = (LinearLayout) findViewById(R.id.communication_send_message);
			rlayout2.setVisibility(rlayout2.GONE);
		}

		remoteClient = new RemoteClient(this);



		register_button.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {

				if(CheckConnectivity(context)) {
                    if (checkPlayServices()) {
                        regid = getRegistrationId(context);
                        if (TextUtils.isEmpty(regid)) {
                            registerInBackground();
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }
			}
		});

        unregister_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if(CheckConnectivity(context)) {

                            unregister();

                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }
            }
        });


        send_message_button.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {

                if (CheckConnectivity(context)) {
                    String message = ((EditText) findViewById(R.id.communication_message_text)).getText().toString();
                    if (message.equals("")) {
                        Toast.makeText(context, "Sending Message Empty!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    remoteClient.fetchValue(other_player_name.getText().toString());
//                if(remoteClient.isDataFetched()) {
//                    String reg_device = remoteClient.getValue(other_player_name.getText().toString());
//                    sendMessage(message, other_player_name.getText().toString(), reg_device);
//                }
//                else{
//                    Toast.makeText(context, "Error",Toast.LENGTH_LONG).show();
//                    return;
//                }

                    startTimer(other_player_name.getText().toString(), message);
//                    sendMessage(message, other_player_name.getText().toString());

                }
                else{
                    Toast.makeText(context, "Can't proceed. No Internet connection available right now.",Toast.LENGTH_LONG).show();
                }
            }
		});


		acknowledgements_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				LayoutInflater inflater = LayoutInflater.from(CommunicationStartGame.this);
				View view1 = inflater.inflate(R.layout.communication_acknowledgements, null);
				TextView communication_acknowledgements = (TextView) view.findViewById(R.id.communication_acknowledgements);
				AlertDialog.Builder builder = new AlertDialog.Builder(CommunicationStartGame.this);
				builder.setView(view1);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.ok_label,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								// nothing
							}
						});
				mDialog_acknowledgements = builder.show();

			}

		});
	}



	private void sendMessage(final String message, final String playerName) {
//		if (regid == null || regid.equals("")) {
//			Toast.makeText(this, "You must register first", Toast.LENGTH_LONG).show();
//			return;
//		}
		if (message.isEmpty()) {
			Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
			return;
		}

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				List<String> regIds = new ArrayList<String>();
//				remoteClient.fetchValue(playerName);
//				String reg_device;
//				if(remoteClient.isDataFetched()){
//					reg_device = remoteClient.getValue(playerName);
					Map<String, String> msgParams;
					msgParams = new HashMap<>();
					msgParams.put("data.message", message);
					GcmNotification gcmNotification = new GcmNotification();
					regIds.clear();
					regIds.add(reg_device);
					gcmNotification.sendNotification(msgParams, regIds,CommunicationStartGame.this);
					return "Message Sent - " + message;

			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		Log.i(TAG, String.valueOf(registeredVersion));
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(CommunicationStartGame.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);

					// implementation to store and keep track of registered devices here
					msg = "Device registered, registration ID=" + regid;
					//sendRegistrationIdToBackend(regid);
					//storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
//				Log.d(TAG, msg);
//				mDisplay.append(msg + "\n");
                sendRegistrationIdToBackend(regid);
                storeRegistrationId(context, regid);
                Toast.makeText(CommunicationStartGame.this, "Click on Communication button again", Toast.LENGTH_SHORT).show();
                finish();
            }
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend(String regid) {
		// Your implementation here.
		String userName = user_name.getText().toString();
		remoteClient.saveValue(userName, regid);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.putBoolean(PROPERTY_STORED_YES_NO, true);
		editor.putString(PROPERTY_STORED_NAME, user_name.getText().toString());
		editor.commit();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void unregister() {
		Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Sent unregistration";
					gcm.unregister();
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
                removeRegistrationIdFromBackend(username);
				removeRegistrationId(getApplicationContext());
//				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//				((TextView) findViewById(R.id.communication_display))
//						.setText(regid);
                finish();
			}
		}.execute();
	}

    private void removeRegistrationIdFromBackend(String username) {
        // Your implementation here.
        remoteClient.deleteValue(username);
    }

	private void removeRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(CommunicationConstants.TAG, "Removig regId on app version "
				+ appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PROPERTY_REG_ID);
		editor.remove(PROPERTY_APP_VERSION);
		editor.remove(PROPERTY_STORED_NAME);
		editor.remove(PROPERTY_STORED_YES_NO);
		editor.commit();
		regid = null;
	}

    public void startTimer(String key, String message) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key, message);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        // The values can be adjusted depending on the performance
//        timer.schedule(timerTask, 5000, 1000);
        timer.schedule(timerTask, 5000, 200);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key, final String message) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
//                    handler.post(new Runnable() {
//
//                        public void run() {
//                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
//                            Toast.makeText(CommunicationStartGame.this, "Value   " + remoteClient.getValue(key), Toast.LENGTH_SHORT).show();
//                        }
//                    });

                    reg_device = remoteClient.getValue(key);

                    sendMessage(message, other_player_name.getText().toString());

                    stoptimertask();
                }

            }
        };
    }

	public boolean CheckConnectivity(final Context c) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivityManager.getActiveNetworkInfo() != null
				&& mConnectivityManager.getActiveNetworkInfo().isAvailable()
				&& mConnectivityManager.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
