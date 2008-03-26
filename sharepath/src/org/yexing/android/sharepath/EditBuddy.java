package org.yexing.android.sharepath;

import org.yexing.android.sharepath.domain.Domain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBuddy extends Activity {
	private static final String LOG_TAG = "SharePath";

	EditText etName;
	EditText etEmail;
	long id = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Use a custom layout file
		setContentView(R.layout.edit_buddy);

		// 检查启动参数
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		// 取得数据库中路径的id
		if (extras != null)
			id = extras.getLong(Domain.Buddy._ID);

		etName = (EditText) findViewById(R.id.name);
		etEmail = (EditText) findViewById(R.id.email);

		if(id != 0) {
			etName.setText(extras.getString(Domain.Buddy.NAME));
			etEmail.setText(extras.getString(Domain.Buddy.EMAIL));
		}
		// 放弃
		Button btnCancel = (Button) findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		// 确定
		Button btnOK = (Button) findViewById(R.id.ok);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString(Domain.Buddy.NAME, etName.getText().toString());
				bundle.putString(Domain.Buddy.EMAIL, etEmail.getText().toString());
				if(id == 0) {
					bundle.putInt("action", SharePathService.ACTION_NEW_BUDDY);
				} else {
					bundle.putLong(Domain.Buddy._ID, id);
					bundle.putInt("action", SharePathService.ACTION_EDIT_BUDDY);
				}
				startService(new Intent(EditBuddy.this, SharePathService.class), bundle);
				
	            finish();
			}
		});

	}
}