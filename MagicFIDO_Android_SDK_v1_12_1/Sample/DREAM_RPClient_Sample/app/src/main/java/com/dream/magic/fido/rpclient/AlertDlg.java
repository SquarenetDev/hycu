package com.dream.magic.fido.rpclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertDlg extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
	    layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
	    layoutParams.dimAmount = 0.7f;
	    getWindow().setAttributes(layoutParams);
	    
	    setContentView(R.layout.rp_popup);
	    
	    LinearLayout popup_layout = (LinearLayout)findViewById(R.id.popup_btn);
	    popup_layout.setVisibility(View.GONE);
	    
	    LinearLayout alert_layout = (LinearLayout)findViewById(R.id.alert_btn);
	    alert_layout.setVisibility(View.VISIBLE);
	    
	    TextView popup_title = (TextView)findViewById(R.id.popup_title);
	    popup_title.setText("알림");
	    
	    TextView popup_msg = (TextView)findViewById(R.id.popup_msg);
	    popup_msg.setText(getIntent().getStringExtra("msg"));
	    
	    Button alert_btn = (Button)findViewById(R.id.alert_ok);
	    alert_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setResult(RESULT_OK);
				finish();
				
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0,0 );
	}


}
