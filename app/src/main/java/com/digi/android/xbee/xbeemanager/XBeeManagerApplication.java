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

package com.digi.android.xbee.xbeemanager;

import com.digi.android.xbee.xbeemanager.managers.XBeeManager;

import android.app.Application;

public class XBeeManagerApplication extends Application {

	// Variables.
	private XBeeManager xbeeManager;
	
	private static XBeeManagerApplication instance;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate() {
		super.onCreate();
		// Save application instance.
		instance = this;
		// Initialize Managers.
		initializeManagers();
	}
	
	/**
	 * Returns the application instance.
	 * 
	 * @return The application instance.
	 */
	public static XBeeManagerApplication getInstance() {
		return instance;
	}
	
	/**
	 * Initializes the managers used by this application.
	 */
	private void initializeManagers() {
		xbeeManager = new XBeeManager(this);
	}
	
	/**
	 * Returns the XBee Manager.
	 * 
	 * @return The application XBee Manager.
	 */
	public XBeeManager getXBeeManager() {
		return xbeeManager;
	}
}
