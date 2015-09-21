package com.lsfb.cysj.utils;

 

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
 

public class Show {

	public static void toast(Context con, int id) {
		Toast.makeText(con, con.getResources().getString(id),
				Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context con, String text) {
		Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
	}

//	public static void dialog(Context con, String title, String message,
//			DialogInterface.OnClickListener onClickListener) {
//		new AlertDialog.Builder(con).setTitle(title).setMessage(message)
//				.setPositiveButton(R.string.positive, onClickListener)
//				.setNegativeButton(R.string.cancel, null).show();
//	}
//
//	public static void dialog(Context con, int title, int message,
//			DialogInterface.OnClickListener onClickListener) {
//		new AlertDialog.Builder(con).setTitle(title)
//				.setMessage(con.getString(message))
//				.setPositiveButton(R.string.positive, onClickListener)
//				.setNegativeButton(R.string.cancel, null).show();
//	}
//
//	public static void dialog(Context con, int title, int message) {
//		new AlertDialog.Builder(con).setTitle(title)
//				.setMessage(con.getString(message))
//				.setPositiveButton(R.string.positive, null).show();
//	}
//
//	public static void dialog(Context con, String title, String message) {
//		new AlertDialog.Builder(con).setTitle(title).setMessage(message)
//				.setPositiveButton(R.string.positive, null).show();
//	}

}
