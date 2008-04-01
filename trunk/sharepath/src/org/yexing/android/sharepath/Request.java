package org.yexing.android.sharepath;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

public class Request extends Activity {
	private static final String LOG_TAG = "SharePath";

//	IGTalkSession mGTalkSession = null;
	String mTextPref;

	EditText etStart;
	EditText etEnd;

	Button mSendButton, mCancelButton;

	static final int RETURN_CHOOSE_BUDDY = 1;
	
	int lat, lon, level;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.v(LOG_TAG, "Request.onCreate");
		setContentView(R.layout.request);

//		// 检查启动参数
//		Intent intent = getIntent();
//		Bundle extras = intent.getExtras();
//		
//		lat = extras.getInt(Domain.Message.CENTER + "lat");
//		lon = extras.getInt(Domain.Message.CENTER + "lon");
//		level = extras.getInt(Domain.Message.LEVEL);
//		
//		Log.v(LOG_TAG, lat + ":" + lon + ":" + level);
		
		etStart = (EditText) findViewById(R.id.request_start);
		etStart.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChanged(android.view.View arg0, boolean arg1) {
				if (arg1 == true) {
					etStart.selectAll();
				}

			}
		});
		etEnd = (EditText) findViewById(R.id.request_end);
		etEnd.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChanged(android.view.View arg0, boolean arg1) {
				if (arg1 == true) {
					etEnd.selectAll();
				}

			}
		});

		mSendButton = (Button) findViewById(R.id.send);
		mSendButton.setOnClickListener(mSendListener);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(mCancelListener);

//		mSendButton.setEnabled(false);

//		bindGTalkService();
	}

	private OnClickListener mSendListener = new OnClickListener() {
		public void onClick(View v) {
			startSubActivity(new Intent(Request.this,ChooseBuddy.class),
					SharePathMap.CODE_CHOOSE_BUDDY);
		}
	};
	private OnClickListener mCancelListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		if (requestCode == SharePathMap.CODE_CHOOSE_BUDDY) {
			if(resultCode == RESULT_OK) {
	            SharedPreferences preferences = getSharedPreferences("SharePath", 0);
	            SharedPreferences.Editor editor = preferences.edit();
	            editor.putString("start", etStart.getText().toString());
	            editor.putString("end", etEnd.getText().toString());
	            if (editor.commit()) {
	                setResult(RESULT_OK);
	            //    finish();
	            }
			}
			finish();
//			if (resultCode == RESULT_CANCELED) {
//				// Log.v("SharePath", "cancel");
//				finish();
//			} else {
//				// Log.v("SharePath", "OK");
//				if (loadPrefs()) {
//					// Log.v("SharePath", mTextPref);
//					if (mGTalkSession == null) {
//						showMessage("gtalk_service_not_connected");
//						return;
//					}
//
//					try {
//						mGTalkSession.sendDataMessage(mTextPref,
//								getIntentToSend());
//						Toast.makeText(this, getText(R.string.send_request_successful),
//								Toast.LENGTH_LONG).show();
//
//					} catch (DeadObjectException ex) {
//						Log.e(LOG_TAG, "caught " + ex);
//						showMessage("found_stale_gtalk_service");
//						mGTalkSession = null;
//						bindGTalkService();
//					}
//				} else {
//					showAlert("Error", 0, "Send Message Failed!", "OK", true);
//				}
//			}
		}
	}

/*
	private final boolean loadPrefs() {
		// Retrieve the current redirect values.
		// NOTE: because this preference is shared between multiple
		// activities, you must be careful about when you read or write
		// it in order to keep from stepping on yourself.
		SharedPreferences preferences = getSharedPreferences("SharePath", 0);

		mTextPref = preferences.getString("text", null);
		if (mTextPref != null) {
			return true;
		}

		return false;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConnection);
	}

	private void bindGTalkService() {
		bindService(
				(new Intent())
						.setComponent(com.google.android.gtalkservice.GTalkServiceConstants.GTALK_SERVICE_COMPONENT),
				mConnection, 0);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the GTalkService has been
			// established, giving us the service object we can use to
			// interact with the service. We are communicating with our
			// service through an IDL interface, so get a client-side
			// representation of that from the raw service object.
			IGTalkService GTalkService = IGTalkService.Stub
					.asInterface(service);

			try {
				mGTalkSession = GTalkService.getDefaultSession();

				if (mGTalkSession == null) {
					// this should not happen.
					showMessage("gtalk_session_not_found");
					return;
				}
			} catch (DeadObjectException ex) {
				Log.e(LOG_TAG, "caught " + ex);
				showMessage("found_stale_gtalk_service");
			}

			mSendButton.setEnabled(true);
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			mGTalkSession = null;
			mSendButton.setEnabled(false);
		}
	};

	private void showMessage(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private Intent getIntentToSend() {
		Intent intent = new Intent(GTalkDataMessageReceiver.ACTION);

		intent.putExtra(Domain.Message.TYPE, 0);
		try {
			intent.putExtra(Domain.Message.FROM, mGTalkSession.getUsername());
		} catch (DeadObjectException e) {
			e.printStackTrace();
		}
		intent.putExtra(Domain.Message.START, etStart.getText().toString());
		intent.putExtra(Domain.Message.END, etEnd.getText().toString());
		intent.putExtra(Domain.Message.LEVEL + "str", "" + level);
		Log.v(LOG_TAG, "request:" + lat + " " + lon + " " + level);
		intent.putExtra(Domain.Message.CENTER + "latstr", "" + lat);
		intent.putExtra(Domain.Message.CENTER + "lonstr", "" + lon);
		intent.putExtra(Domain.Message.DATE + "str", "" + System.currentTimeMillis());

		return intent;
	}
*/
}
