package com.digi.android.xbee.xbeemanager.dialogs;

import java.util.regex.Pattern;

import com.digi.android.xbee.xbeemanager.R;
import com.digi.android.xbee.xbeemanager.internal.TextValidator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ChangeParameterDialog {

	// Constants.
	public final static int TYPE_TEXT = 0;
	public final static int TYPE_NUMERIC = 1;
	public final static int TYPE_HEXADECIMAL = 2;
	
	private final static String HEXADECIMAL_PATTERN = "[0-9a-fA-F]+";
	
	private final static String ERROR_VALUE_EMPTY = "Value cannot be empty.";
	private final static String ERROR_INVALID_HEX_VALUE = "Invalid hexadecimal value.";
	
	// Variables.
	private Context context;
	
	private String oldText;
	
	private EditText inputText;
	
	private View inputTextDialogView;
	
	private AlertDialog inputTextDialog;
	
	private String textValue;
	
	private int type;
	
	public ChangeParameterDialog(Context context, int type, String oldText) {
		this.context = context;
		this.type = type;
		this.oldText = oldText;
		
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
		switch (type) {
		case TYPE_TEXT:
			inputTextDialogView = layoutInflater.inflate(R.layout.change_text_param_dialog, null);
			break;
		case TYPE_NUMERIC:
			inputTextDialogView = layoutInflater.inflate(R.layout.change_numeric_param_dialog, null);
			break;
		case TYPE_HEXADECIMAL:
			inputTextDialogView = layoutInflater.inflate(R.layout.change_hexadecimal_param_dialog, null);
			break;
		}
		// Configure the input text.
		inputText = (EditText) inputTextDialogView.findViewById(R.id.input_text);
		if (oldText != null)
			inputText.setText(oldText);
		inputText.addTextChangedListener(new TextValidator(inputText) {
			/*
			 * (non-Javadoc)
			 * @see com.digi.android.xbee.xbeemanager.dialogs.SendDataDialog.TextValidator#validate(android.widget.EditText, java.lang.String)
			 */
			public void validate(EditText textView, String text) {
				if (text.length() == 0) {
					inputText.setError(ERROR_VALUE_EMPTY);
					inputTextDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					return;
				}
				if (type == TYPE_HEXADECIMAL && !Pattern.matches(HEXADECIMAL_PATTERN, text)) {
					inputText.setError(ERROR_INVALID_HEX_VALUE);
					inputTextDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					return;
				}
				inputText.setError(null);
				inputTextDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
			}
		});
	}
	
	/**
	 * Creates the alert dialog that will be displayed.
	 */
	private void createDialog() {
		// Setup the dialog window.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(inputTextDialogView);
		alertDialogBuilder.setTitle(R.string.edit_dialog_title);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				textValue = inputText.getText().toString();
				synchronized (ChangeParameterDialog.this) {
					ChangeParameterDialog.this.notify();
				}
			}
		});
		alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int id) {
				dialog.cancel();
				synchronized (ChangeParameterDialog.this) {
					ChangeParameterDialog.this.notify();
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
		// Reset the value.
		textValue = null;
		createDialog();
		inputTextDialog.show();
	}
	
	/**
	 * Returns the input text dialog value.
	 * 
	 * @return The input text dialog value, {@code null} if dialog was
	 *         cancelled or no text was entered.
	 */
	public String getTextValue() {
		return textValue;
	}
}
