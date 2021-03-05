package com.dream.magic.fido.rpclient;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.FidoResult;
import com.dream.magic.fido.rpsdk.client.LOCAL_AUTH_TYPE;
import com.dream.magic.fido.rpsdk.client.MagicFIDOUtil;

public class PropertyActivity extends Activity  implements OnClickListener {
	
	Button reqdo , patternChange , patternReset , passcodeChange , passcodeReset;
	EditText edtreq, edtreq1, edtreq2;
	TextView facetinfo , versionTextView;
	CheckBox SSLBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.rp_activity_property);
		this.setTitle("Configuration");

		getActionBar().setDisplayShowHomeEnabled(false);
		
		Drawable background = getResources().getDrawable(R.drawable.bg_top); 
		getActionBar().setBackgroundDrawable(background);

		edtreq = (EditText) findViewById(R.id.edtprop);
		edtreq.setPrivateImeOptions("defaultInputmode=english;");
		
		edtreq1 = (EditText) findViewById(R.id.edtprop1);
		edtreq.setPrivateImeOptions("defaultInputmode=english;");
		
		edtreq2 = (EditText) findViewById(R.id.edtprop2);
		edtreq.setPrivateImeOptions("defaultInputmode=english;");
		
		reqdo = (Button) findViewById(R.id.changedo);
		facetinfo = (TextView) findViewById(R.id.textfacet);
		

        SSLBox = (CheckBox) findViewById(R.id.SSL_Check_mode);
        SSLBox.setChecked(MainActivity.getSSLChecked());

		passcodeChange = (Button) findViewById(R.id.passcodeChange);
		passcodeReset = (Button) findViewById(R.id.passcodeReset);
		patternChange = (Button) findViewById(R.id.patternChange);
		patternReset = (Button) findViewById(R.id.patternReset);

		
		edtreq.setText(PropertyManager.getServerUrl());
		edtreq1.setText(PropertyManager.getRestReq());
		edtreq2.setText(PropertyManager.getRestRes());
		
		reqdo.setOnClickListener(this);
		passcodeChange.setOnClickListener(this);
		passcodeReset.setOnClickListener(this);
		patternChange.setOnClickListener(this);
		patternReset.setOnClickListener(this);

		facetinfo.setText(PropertyManager.getFacetID(this));

		versionTextView = (TextView) findViewById(R.id.versionTextView);
		versionTextView.setText("v" + MagicFIDOUtil.getVersion());
	}
	
	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == reqdo.getId()){
			PropertyManager.setServerUrl(edtreq.getText().toString(), edtreq1
					.getText().toString(), edtreq2.getText().toString());

			boolean sslChecked = this.SSLBox.isChecked();
			MainActivity.setSSLChecked(sslChecked);

			PropertyManager.save();
			showDialog("success", "property change and save..");

		}else {
			MagicFIDOUtil magicFIDOUtil = new MagicFIDOUtil(PropertyActivity.this);
			FIDOCallbackResult fidoCallbackResult = new FIDOCallbackResult() {
				@Override
				public void onFIDOResult(int i, boolean b, FidoResult fidoResult) {
					Toast.makeText(PropertyActivity.this, fidoResult.getDescription() , Toast.LENGTH_SHORT).show();
				}
			};

			if(arg0.getId() == passcodeReset.getId()){
				magicFIDOUtil.resetUserVerification(LOCAL_AUTH_TYPE.LOCAL_PACODE_TYPE , fidoCallbackResult);
			}else if(arg0.getId() == patternReset.getId()){
				magicFIDOUtil.resetUserVerification(LOCAL_AUTH_TYPE.LOCAL_PATTERN_TYPE , fidoCallbackResult);
			}else if(arg0.getId() == passcodeChange.getId()){
				magicFIDOUtil.changeUserVerification(LOCAL_AUTH_TYPE.LOCAL_PACODE_TYPE , fidoCallbackResult , true);
			}else if(arg0.getId() == patternChange.getId()){
				magicFIDOUtil.changeUserVerification(LOCAL_AUTH_TYPE.LOCAL_PATTERN_TYPE , fidoCallbackResult , true);
			}
		}
	}
	
	private void showDialog(String title, String body) {
		Message msg = mActionHandler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("body", body);
		msg.setData(bundle);
		mActionHandler.sendMessage(msg);
	}

	public Handler mActionHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			PopupDlg.ShowDialog(PropertyActivity.this,
					data.getString("title"), data.getString("body"));
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0,0 );
	}


}
