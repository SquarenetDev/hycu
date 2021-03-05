package com.dream.magic.fido.rpclient;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class PopupDlg {
	public static void ShowDialog(final Activity context, String title, String content) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				context.onBackPressed();
			}
		});
		builder.create().show();
	}
}
