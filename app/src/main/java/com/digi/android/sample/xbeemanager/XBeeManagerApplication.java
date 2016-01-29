/**
 * Copyright (c) 2014-2016, Digi International Inc. <support@digi.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.digi.android.sample.xbeemanager;

import com.digi.android.sample.xbeemanager.managers.XBeeManager;

import android.app.Application;

public class XBeeManagerApplication extends Application {

	// Variables.
	private XBeeManager xbeeManager;
	
	private static XBeeManagerApplication instance;
	
	@Override
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
