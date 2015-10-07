/**
 * Copyright (c) 2014-2015 Digi International Inc.,
 * All rights not expressly granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */

package com.digi.android.xbee.xbeemanager.internal;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Helper class used to validate text from Edit Text widgets wrapping the
 * {@code TextWatcher} class.
 */
public abstract class TextValidator implements TextWatcher {

	// Variables.
	private final EditText editText;

	/**
	 * Class constructor. Instantiates a new {@code TextValidator} object
	 * with the given parameters.
	 * 
	 * @param editText Edit Text widget this validator will be for.
	 */
	public TextValidator(EditText editText) {
		this.editText = editText;
	}

	/**
	 * Validates the given text.
	 * 
	 * @param textView Edit Text widget this validator will be for.
	 * @param text Text to validate.
	 */
	public abstract void validate(EditText textView, String text);

	/*
	 * (non-Javadoc)
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	final public void afterTextChanged(Editable s) {
		String text = editText.getText().toString();
		validate(editText, text);
	}

	/*
	 * (non-Javadoc)
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	final public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	final public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Do nothing.
	}
}