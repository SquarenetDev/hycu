package com.dream.magic.fido.rpclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dream.magic.fido.rpclient.cp.FIDODeRegistration;
import com.dream.magic.fido.rpclient.exception.RPLog;
import com.dream.magic.fido.rpsdk.callback.FIDOCallbackResult;
import com.dream.magic.fido.rpsdk.client.FIDORequestCode;
import com.dream.magic.fido.rpsdk.client.FidoResult;
import com.dream.magic.fido.rpsdk.util.MAGIC_FIDO_TYPE;

/**
 * Deregistration Activity
 *  - FIDO 해지 하기 위한 화면
 **/

public class DeregisterActivity extends Activity {
	
	private EditText edit_userID = null;
	
	// RP SDK Object
	private FIDODeRegistration mDeregist = null;
	private final int ALERT_REQUEST = 0xA3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rp_activity_deregister);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		
		Drawable background = getResources().getDrawable(R.drawable.bg_top); 
		getActionBar().setBackgroundDrawable(background);
		
		edit_userID = (EditText) findViewById(R.id.edit_userID);
		Button btn_f = (Button) findViewById(R.id.btn_fido);
		
		String userName = MainActivity.getAuthID();
		if( userName != null)
			edit_userID.setText(userName);
		
		// RPContext를 위한 세팅				
		
		btn_f.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/**
				 * Step 01 Initialize Deregistration
				 *  -해지 할 등록된 인증서 선택
				 */
				// RP SDK 생성
				mDeregist = new FIDODeRegistration(DeregisterActivity.this,  fidoCallback);
				
				String userID = edit_userID.getText().toString();

				mDeregist.deRegistFIDO(userID);
			}
			
		});
		
	}
	
	private FIDOCallbackResult fidoCallback = new FIDOCallbackResult() {

		@Override
		public void onFIDOResult(int requestCode, boolean result, FidoResult fidoResult) {

			if(requestCode == FIDORequestCode.REQUEST_CODE_DEREG){
				// FIDO Client에 전송하지 못 하였을 경우.
				if(fidoResult.getErrorCode() != FidoResult.RESULT_SUCCESS){
					showDialog("FAIL", fidoResult.getDescription() + "(" +
							fidoResult.getErrorCode() + ")");
				}else{
					
					String token = mDeregist.getToken();
					RPLog.p(getClass(), "Token : " + token);
					
					isSuccess = true;
					MainActivity.setAuthID("");
					showDialog("SUCCESS", "Deregister Success");
				}
			}
		}
	};
	
	//=======================Activity에서 쓰이는 Alert창==============================
	
	private void showDialog(String title, String body) {
		
		Intent alertIntent = new Intent(this, AlertDlg.class);
		alertIntent.putExtra("msg", body);
		startActivityForResult(alertIntent, ALERT_REQUEST);
		overridePendingTransition(0,0);
	}
	
	private boolean isSuccess = false;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == ALERT_REQUEST && isSuccess == true){
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0,0 );
	}


}
