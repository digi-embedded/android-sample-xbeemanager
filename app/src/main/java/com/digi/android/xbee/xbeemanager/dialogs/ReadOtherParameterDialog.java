package com.digi.android.xbee.xbeemanager.dialogs;

import com.digi.android.xbee.xbeemanager.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ReadOtherParameterDialog {
	
	// Variables.
	private Context context;
	
	private EditText parameterText;
	
	private View inputTextDialogView;
	
	private AlertDialog inputTextDialog;
	
	private String parameter;
	
	public ReadOtherParameterDialog(Context context) {
		this.context = context;
		
		// Setup the layout.
		setupLayout();
	}
	
	/**
	 * Configures the layout of the input text dialog.
	 */
	@SuppressLint("InflateParams")
	private void setupLayout() {
		// Create the layout.
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		inputTextDialogView = layoutInflater.inflate(R.layout.read_other_param_dialog, null);
		// Configure the input texts.
		parameterText = (EditText) inputTextDialogView.findViewById(R.id.param_text);
	}
	
	/**
	 * Creates the alert dialog that will be displayed.
	 */
	private void createDialog() {
		// Setup the dialog window.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(inputTextDialogView);
		alertDialogBuilder.setTitle(R.string.read_dialog_title);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				parameter = parameterText.getText().toString();
				synchronized (ReadOtherParameterDialog.this) {
					ReadOtherParameterDialog.this.notify();
				}
			}
		});
		alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int id) {
				dialog.cancel();
				synchronized (ReadOtherParameterDialog.this) {
					ReadOtherParameterDialog.this.notify();
				}
			}
		});
		// Create the dialog.
		inputTextDialog = alertDialogBuilder.create();
	}
	
	/**
	 * Displays the input text dialog.
	 */
	public void show() {
		// Reset the dialog values.
		parameter = null;
		createDialog();
		inputTextDialog.show();
	}
	
	/**
	 * Returns the input text dialog value.
	 * 
	 * @return The input text dialog value, {@code null} if dialog was
	 *         cancelled or no text was entered.
	 */
	public String getParameter() {
		return parameter;
	}
}
