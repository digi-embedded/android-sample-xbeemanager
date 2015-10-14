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

package com.digi.android.xbeemanager.fragments;

import android.support.v4.app.Fragment;

import com.digi.android.xbeemanager.managers.XBeeManager;

public abstract class AbstractXBeeDeviceFragment extends Fragment {
	
	// Constants.
	protected static final int ACTION_CLEAR_ERROR_MESSAGE = 0;
	protected static final int ACTION_SET_ERROR_MESSAGE = 1;
	protected static final int ACTION_SET_SUCCESS_MESSAGE = 2;
	protected static final int ACTION_SHOW_READ_PROGRESS_DIALOG = 3;
	protected static final int ACTION_SHOW_WRITE_PROGRESS_DIALOG = 4;
	protected static final int ACTION_SHOW_DISCOVER_PROGRESS_DIALOG = 5;
	protected static final int ACTION_SHOW_SEND_PROGRESS_DIALOG = 6;
	protected static final int ACTION_HIDE_PROGRESS_DIALOG = 7;
	protected static final int ACTION_UPDATE_LIST_VIEW = 8;
	protected static final int ACTION_UPDATE_LIST_TEXT = 9;
	protected static final int ACTION_ENABLE_PARAMETERS_BUTTONS = 10;
	protected static final int ACTION_DISABLE_PARAMETERS_BUTTONS = 11;
	protected static final int ACTION_CLEAR_VALUES = 12;
	protected static final int ACTION_ENABLE_DISCOVER_BUTTONS = 13;
	protected static final int ACTION_DISABLE_DISCOVER_BUTTONS = 14;
	protected static final int ACTION_ADD_PACKET_TO_LIST = 15;
	protected static final int ACTION_ADD_DEVICE_TO_LIST = 16;

	// Variables.
	protected XBeeManager xbeeManager;

	/**
	 * Retrieves the fragment name.
	 * 
	 * @return The fragment name.
	 */
	public abstract String getFragmentName();

	/**
	 * Configures the XBee Device manager.
	 *
	 * @param manager XBee Device manager to interact with XBee devices.
	 */
	public void setXBeeManager(XBeeManager manager) {
		xbeeManager = manager;
	}
}
