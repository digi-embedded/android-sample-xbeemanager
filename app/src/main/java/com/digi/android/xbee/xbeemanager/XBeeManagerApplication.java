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
