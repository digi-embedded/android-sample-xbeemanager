package com.digi.android.xbee.xbeemanager;

import java.util.ArrayList;

import com.digi.android.xbee.xbeemanager.fragments.AbstractXBeeDeviceFragment;
import com.digi.android.xbee.xbeemanager.fragments.XBeeDeviceDiscoveryFragment;
import com.digi.android.xbee.xbeemanager.fragments.XBeeDeviceInfoFragment;
import com.digi.android.xbee.xbeemanager.fragments.XBeeReceivedPacketsFragment;
import com.digi.android.xbee.xbeemanager.managers.XBeeManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class XBeeTabsActivity extends FragmentActivity {
	
	// Variables.
	private ViewPager viewPager;
    private XBeeManager xbeeManager;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xbee_tabs_activity);
		
		// Retrieve the XBee Manager.
		xbeeManager = XBeeManagerApplication.getInstance().getXBeeManager();
		
		// Setup view pager.
		setupViewPager();
		
		// Configure Action Bar.
		configureTabs();
	}
	
	/**
	 * Configures the view pager that will be used to display the different
	 * activity fragments.
	 */
	private void setupViewPager() {
		viewPager = (ViewPager)findViewById(R.id.pager);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			/*
			 * (non-Javadoc)
			 * @see android.support.v4.view.ViewPager.SimpleOnPageChangeListener#onPageSelected(int)
			 */
			public void onPageSelected(int position) {
				// When swiping between pages, select the corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
			}
		});
		viewPager.setOffscreenPageLimit(2);
		XBeeDevicePagerAdapter xbeePagerAdapter = new XBeeDevicePagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(xbeePagerAdapter);
	}
	
	/**
	 * Configures the activity tabs of the action bar.
	 */
	private void configureTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		XBeeDeviceTabListener xbeeDeviceTabListener = new XBeeDeviceTabListener();

		Tab infoTab = actionBar.newTab();
		infoTab.setText(getResources().getString(R.string.device_info_fragment_title));
		infoTab.setTabListener(xbeeDeviceTabListener);

		Tab discoverTab;discoverTab = actionBar.newTab();
		discoverTab.setText(getResources().getString(R.string.device_discovery_fragment_title));
		discoverTab.setTabListener(xbeeDeviceTabListener);

		Tab framesTab = actionBar.newTab();
		framesTab.setText(getResources().getString(R.string.frames_fragment_title));
		framesTab.setTabListener(xbeeDeviceTabListener);
		
		actionBar.addTab(infoTab);
		actionBar.addTab(discoverTab);
		actionBar.addTab(framesTab);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	protected void onStop() {
		super.onStop();
		// Disconnect the device.
		xbeeManager.closeConnection();
	}
	
	/**
	 * Helper class used to handle the events of the different tab items.
	 */
	private class XBeeDeviceTabListener implements TabListener {

		/*
		 * (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Do nothing.
		}

		/*
		 * (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// When the tab is selected, switch to the corresponding page in the ViewPager.
			viewPager.setCurrentItem(tab.getPosition());
		}

		/*
		 * (non-Javadoc)
		 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
		 */
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// Do nothing.
		}
	}
	
	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	private class XBeeDevicePagerAdapter extends FragmentPagerAdapter {
		
		// Variables.
		private AbstractXBeeDeviceFragment infoFragment;
		private AbstractXBeeDeviceFragment discoverFragment;
		private AbstractXBeeDeviceFragment framesFragment;
		
		private ArrayList<AbstractXBeeDeviceFragment> fragments;
		
		public XBeeDevicePagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new ArrayList<AbstractXBeeDeviceFragment>();
			// Create device information fragment.
			infoFragment = new XBeeDeviceInfoFragment(xbeeManager, XBeeTabsActivity.this);
			fragments.add(infoFragment);
			// Create device discovery fragment.
			discoverFragment = new XBeeDeviceDiscoveryFragment(xbeeManager);
			fragments.add(discoverFragment);
			// Create received frames fragment.
			framesFragment = new XBeeReceivedPacketsFragment(xbeeManager);
			fragments.add(framesFragment);
		}

		/*
		 * (non-Javadoc)
		 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
		 */
		public Fragment getItem(int i) {
			return fragments.get(i);
		}

		/*
		 * (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		public int getCount() {
			return fragments.size();
		}

		/*
		 * (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).getFragmentName();
		}
	}
}
