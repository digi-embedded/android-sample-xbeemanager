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

package com.digi.android.xbee.xbeemanager.fragments;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.digi.android.xbee.xbeemanager.R;
import com.digi.android.xbee.xbeemanager.XBeeConstants;
import com.digi.android.xbee.xbeemanager.dialogs.ChangeOtherParameterDialog;
import com.digi.android.xbee.xbeemanager.dialogs.ChangeParameterDialog;
import com.digi.android.xbee.xbeemanager.dialogs.ReadOtherParameterDialog;
import com.digi.android.xbee.xbeemanager.dialogs.SendDataDialog;
import com.digi.android.xbee.xbeemanager.internal.RemoteXBeeDevicesAdapter;
import com.digi.android.xbee.xbeemanager.managers.XBeeManager;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IDiscoveryListener;
import com.digi.xbee.api.utils.HexUtils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class XBeeDeviceDiscoveryFragment extends AbstractXBeeDeviceFragment implements IDiscoveryListener {

	// Variables.
	private XBeeManager xbeeManager;
	
	private ArrayList<RemoteXBeeDevice> remoteDevices;
	
	private RemoteXBeeDevicesAdapter remoteDevicesAdapter;
	
	private ListView remoteDevicesList;
	
	private Button discoverButton;
	private Button clearButton;
	private Button nodeIdentifierButton;
	private Button changeOtherParameterButton;
	private Button readOtherParameterButton;
	private Button refreshButton;
	private Button sendTextDataButton;
	private Button sendHexDataButton;
	
	private TextView errorText;
	private TextView nodeIdentifierText;
	private TextView firmwareVersionText;
	private TextView hardwareVersionText;
	private TextView xbeeProtocolText;
	private TextView macAddressText;
	private TextView remoteDevicesText;
	
	private ProgressDialog progressDialog;
	
	private final Object remoteDevicesLock = new Object();
	private IncomingHandler handler = new IncomingHandler(this);

	static class IncomingHandler extends Handler {

		private final WeakReference<XBeeDeviceDiscoveryFragment> wActivity;

		IncomingHandler(XBeeDeviceDiscoveryFragment activity) {
			wActivity = new WeakReference<XBeeDeviceDiscoveryFragment>(activity);
		}

		@Override
		public void handleMessage(Message msg) {

			XBeeDeviceDiscoveryFragment xBeeDeviceDiscoveryFragment = wActivity.get();
			if (xBeeDeviceDiscoveryFragment == null) return;

			switch (msg.what) {
				case ACTION_CLEAR_ERROR_MESSAGE:
					xBeeDeviceDiscoveryFragment.errorText.setText("");
					break;
				case ACTION_SET_ERROR_MESSAGE:
					xBeeDeviceDiscoveryFragment.errorText.setTextColor(xBeeDeviceDiscoveryFragment.getResources().getColor(R.color.red));
					xBeeDeviceDiscoveryFragment.errorText.setText((String)msg.obj);
					break;
				case ACTION_SET_SUCCESS_MESSAGE:
					xBeeDeviceDiscoveryFragment.errorText.setTextColor(xBeeDeviceDiscoveryFragment.getResources().getColor(R.color.green));
					xBeeDeviceDiscoveryFragment.errorText.setText((String)msg.obj);
					break;
				case ACTION_SHOW_READ_PROGRESS_DIALOG:
					xBeeDeviceDiscoveryFragment.progressDialog = new
							ProgressDialog(xBeeDeviceDiscoveryFragment.getActivity());
					xBeeDeviceDiscoveryFragment.progressDialog.setCancelable(false);
					xBeeDeviceDiscoveryFragment.progressDialog.setTitle(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.reading_dialog_title));
					xBeeDeviceDiscoveryFragment.progressDialog.setMessage(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.reading_dialog_text));
					xBeeDeviceDiscoveryFragment.progressDialog.show();
					break;
				case ACTION_SHOW_WRITE_PROGRESS_DIALOG:
					xBeeDeviceDiscoveryFragment.progressDialog = new ProgressDialog(xBeeDeviceDiscoveryFragment.getActivity());
					xBeeDeviceDiscoveryFragment.progressDialog.setCancelable(false);
					xBeeDeviceDiscoveryFragment.progressDialog.setTitle(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.writing_dialog_title));
					xBeeDeviceDiscoveryFragment.progressDialog.setMessage(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.writing_dialog_text));
					xBeeDeviceDiscoveryFragment.progressDialog.show();
					break;
				case ACTION_SHOW_SEND_PROGRESS_DIALOG:
					xBeeDeviceDiscoveryFragment.progressDialog = new ProgressDialog(xBeeDeviceDiscoveryFragment.getActivity());
					xBeeDeviceDiscoveryFragment.progressDialog.setCancelable(false);
					xBeeDeviceDiscoveryFragment.progressDialog.setTitle(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.sending_dialog_title));
					xBeeDeviceDiscoveryFragment.progressDialog.setMessage(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.sending_dialog_text));
					xBeeDeviceDiscoveryFragment.progressDialog.show();
					break;
				case ACTION_SHOW_DISCOVER_PROGRESS_DIALOG:
					xBeeDeviceDiscoveryFragment.progressDialog = new ProgressDialog(xBeeDeviceDiscoveryFragment.getActivity());
					xBeeDeviceDiscoveryFragment.progressDialog.setCancelable(false);
					xBeeDeviceDiscoveryFragment.progressDialog.setTitle(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.discovering_dialog_title));
					xBeeDeviceDiscoveryFragment.progressDialog.setMessage(xBeeDeviceDiscoveryFragment.getResources().getString(R.string.discovering_dialog_text));
					xBeeDeviceDiscoveryFragment.progressDialog.show();
					break;
				case ACTION_HIDE_PROGRESS_DIALOG:
					if (xBeeDeviceDiscoveryFragment.progressDialog != null)
						xBeeDeviceDiscoveryFragment.progressDialog.dismiss();
					break;
				case ACTION_UPDATE_LIST_VIEW:
					xBeeDeviceDiscoveryFragment.remoteDevicesAdapter.notifyDataSetChanged();
					xBeeDeviceDiscoveryFragment.remoteDevicesList.invalidateViews();
					this.sendEmptyMessage(ACTION_UPDATE_LIST_TEXT);
					break;
				case ACTION_UPDATE_LIST_TEXT:
					xBeeDeviceDiscoveryFragment.remoteDevicesText.setText(xBeeDeviceDiscoveryFragment.remoteDevices.size() + " " +
							xBeeDeviceDiscoveryFragment.getResources().getString(R.string.remote_devices_found));
					break;
				case ACTION_ENABLE_PARAMETERS_BUTTONS:
					xBeeDeviceDiscoveryFragment.nodeIdentifierButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.readOtherParameterButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.changeOtherParameterButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.refreshButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.sendTextDataButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.sendHexDataButton.setEnabled(true);
					break;
				case ACTION_DISABLE_PARAMETERS_BUTTONS:
					xBeeDeviceDiscoveryFragment.nodeIdentifierButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.readOtherParameterButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.changeOtherParameterButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.refreshButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.sendTextDataButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.sendHexDataButton.setEnabled(false);
					break;
				case ACTION_CLEAR_VALUES:
					xBeeDeviceDiscoveryFragment.nodeIdentifierText.setText("");
					xBeeDeviceDiscoveryFragment.firmwareVersionText.setText("");
					xBeeDeviceDiscoveryFragment.hardwareVersionText.setText("");
					xBeeDeviceDiscoveryFragment.xbeeProtocolText.setText("");
					xBeeDeviceDiscoveryFragment.macAddressText.setText("");
					break;
				case ACTION_ENABLE_DISCOVER_BUTTONS:
					xBeeDeviceDiscoveryFragment.discoverButton.setEnabled(true);
					xBeeDeviceDiscoveryFragment.clearButton.setEnabled(true);
					break;
				case ACTION_DISABLE_DISCOVER_BUTTONS:
					xBeeDeviceDiscoveryFragment.discoverButton.setEnabled(false);
					xBeeDeviceDiscoveryFragment.clearButton.setEnabled(false);
					break;
				case ACTION_ADD_DEVICE_TO_LIST:
					RemoteXBeeDevice remoteDevice = (RemoteXBeeDevice)msg.obj;
					synchronized (xBeeDeviceDiscoveryFragment.remoteDevicesLock) {
						boolean found = false;
						for (RemoteXBeeDevice xbeeDevice:xBeeDeviceDiscoveryFragment.remoteDevices) {
							if (xbeeDevice.get64BitAddress().equals(remoteDevice.get64BitAddress())) {
								found = true;
								break;
							}
						}
						if (!found)
							xBeeDeviceDiscoveryFragment.remoteDevices.add(remoteDevice);
					}
					xBeeDeviceDiscoveryFragment.updateListView();
					break;
			}
		}
	}

	
	/**
	 * Class constructor. Instantiates a new {@code XBeeDevicediscoveryFragment}
	 * object with the given parameters.
	 * 
	 * @param xbeeManager XBee Device manager to interact with XBee devices.
	 */
	public XBeeDeviceDiscoveryFragment(XBeeManager xbeeManager) {
		this.xbeeManager = xbeeManager;
		xbeeManager.addDiscoveryListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.xbee_device_discovery, container, false);
		
		// Configure adapter.
		if (remoteDevices == null) {
			remoteDevices = new ArrayList<RemoteXBeeDevice>();
			remoteDevicesAdapter = new RemoteXBeeDevicesAdapter(this, remoteDevices);
		}
		
		// Initialize all required UI elements.
		initializeUIElements(view);
		
		// Render initial remote devices list.
		updateListView();
		
		return view;
	}
	
	/**
	 * Initializes all the required graphic elements of this fragment. 
	 * 
	 * @param view View to search elements in.
	 */
	private void initializeUIElements(View view) {
		// Devices List.
		remoteDevicesList = (ListView)view.findViewById(R.id.remote_devices_list);
		remoteDevicesList.setAdapter(remoteDevicesAdapter);
		remoteDevicesList.setOnItemClickListener(new OnItemClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				remoteDevicesAdapter.setSelection(arg2);
				handleRemoteDeviceSelected(remoteDevices.get(arg2));
			}
		});
		// Buttons.
		discoverButton = (Button)view.findViewById(R.id.discover_button);
		discoverButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleDiscoverButtonPressed();
			}
		});
		clearButton = (Button)view.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleClearButtonPressed();
			}
		});
		refreshButton = (Button)view.findViewById(R.id.refresh_button);
		refreshButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleRefreshButtonPressed();
			}
		});
		readOtherParameterButton = (Button)view.findViewById(R.id.read_other_param_button);
		readOtherParameterButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleReadOtherParameterButtonPressed();
			}
		});
		changeOtherParameterButton = (Button)view.findViewById(R.id.change_other_param_button);
		changeOtherParameterButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleChangeOtherParameterButtonPressed();
			}
		});
		nodeIdentifierButton = (Button)view.findViewById(R.id.node_identifier_button);
		nodeIdentifierButton.setOnClickListener(parametersChangeButtonListener);
		sendTextDataButton = (Button)view.findViewById(R.id.send_text_data_button);
		sendTextDataButton.setOnClickListener(sendDataButtonListener);
		sendHexDataButton = (Button)view.findViewById(R.id.send_hex_data_button);
		sendHexDataButton.setOnClickListener(sendDataButtonListener);
		// Texts.
		errorText = (TextView)view.findViewById(R.id.error_text);
		nodeIdentifierText = (TextView)view.findViewById(R.id.node_identifier_text);
		firmwareVersionText = (TextView)view.findViewById(R.id.firmware_text);
		hardwareVersionText = (TextView)view.findViewById(R.id.hardware_text);
		xbeeProtocolText = (TextView)view.findViewById(R.id.protocol_text);
		macAddressText = (TextView)view.findViewById(R.id.mac_address_text);
		remoteDevicesText = (TextView)view.findViewById(R.id.remote_devices_text);
	}
	
	/**
	 * Listener used to wait for clicks on the "Change..." parameter buttons.
	 */
	private OnClickListener parametersChangeButtonListener = new OnClickListener() {
		/*
		 * (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(View v) {
			// Remove error message.
			clearErrorMessage();
			// Initialize variables.
			String oldValue = null;
			String parameter = null;
			int type = 0;
			switch (v.getId()) {
			case R.id.node_identifier_button:
				parameter = XBeeConstants.PARAM_NODE_IDENTIFIER;
				oldValue = nodeIdentifierText.getText().toString();
				type = ChangeParameterDialog.TYPE_TEXT;
				break;
			}
			final String parameterFinal = parameter;
			final ChangeParameterDialog changeParameterDialog = new ChangeParameterDialog(XBeeDeviceDiscoveryFragment.this.getActivity(), type, oldValue);
			changeParameterDialog.show();
			// This thread waits until dialog is closed, dialog is notified itself when that happens.
			Thread waitThread = new Thread() {
				public void run() {
					synchronized (changeParameterDialog) {
						try {
							changeParameterDialog.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					String newValue = changeParameterDialog.getTextValue();
					if (newValue != null)
						writeRemoteParameter(parameterFinal, newValue, getSelectedXBeeDevice());
				}
			};
			waitThread.start();
		}
	};

	/**
	 * Listener used to wait for clicks on the Send Data buttons.
	 */
	private OnClickListener sendDataButtonListener = new OnClickListener() {
		/*
		 * (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(View view) {
			// Remove error message.
			clearErrorMessage();
			// Initialize variables.
			int type = 0;
			switch (view.getId()) {
			case R.id.send_text_data_button:
				type = SendDataDialog.TYPE_TEXT;
				break;
			case R.id.send_hex_data_button:
				type = SendDataDialog.TYPE_HEXADECIMAL;
				break;
			}
			final SendDataDialog sendDataDialog = new SendDataDialog(XBeeDeviceDiscoveryFragment.this.getActivity(), type);
			sendDataDialog.show();
			// This thread waits until dialog is closed, dialog is notified itself when that happens.
			Thread waitThread = new Thread() {
				public void run() {
					synchronized (sendDataDialog) {
						try {
							sendDataDialog.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					byte[] data = sendDataDialog.getValue();
					// Sanity checks.
					if (data != null)
						sendData(data, getSelectedXBeeDevice());
				}
			};
			waitThread.start();
		}
	};
	
	/**
	 * Handles what happens when the discover button is pressed.
	 */
	private void handleDiscoverButtonPressed() {
		Thread discoverThread = new Thread() {
			public void run() {
				clearErrorMessage();
				disableDiscoverButtons();
				showDiscoverProgressDialog();
				xbeeManager.startDiscoveryProcess();
				while (xbeeManager.isDiscoveryRunning()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		discoverThread.start();
	}
	
	/**
	 * Handles what happens when the clear button is pressed.
	 */
	private void handleClearButtonPressed() {
		synchronized (remoteDevicesLock) {
			remoteDevices.clear();
		}
		remoteDevicesAdapter.setSelection(RemoteXBeeDevicesAdapter.NOTHING_SELECTED);
		updateListView();
		handleRemoteDeviceSelected(null);
	}
	
	/**
	 * Handles what happens when the refresh button is pressed.
	 */
	private void handleRefreshButtonPressed() {
		handleRemoteDeviceSelected(getSelectedXBeeDevice());
	}
	
	/**
	 * Handles what happens when a remote XBee Device is selected from the list.
	 * 
	 * @param remoteDevice Selected remote XBee Device, may be {@code null}.
	 */
	public void handleRemoteDeviceSelected(final RemoteXBeeDevice remoteDevice) {
		disableUIButtons();
		clearValues();
		if (remoteDevice == null)
			return;
		Thread readThread = new Thread() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			public void run() {
				clearErrorMessage();
				try {
					showReadProgressDialog();
					final HashMap<String, String> parameters = xbeeManager.readBasicRemoteParameters(remoteDevice);
					fillRemoteDeviceParameters(parameters);
					enableUIButtons();
				} catch (XBeeException e) {
					setErrorMessage("Error reading parameters > " + e.getMessage());
				} finally {
					hideProgressDialog();
				}
			}
		};
		readThread.start();
	}
	
	/**
	 * Fills the page fields with the read device parameters.
	 * 
	 * @param parameters Read device parameters.
	 */
	private void fillRemoteDeviceParameters(final HashMap<String, String> parameters) {
		getActivity().runOnUiThread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				firmwareVersionText.setText(parameters.get(XBeeConstants.PARAM_FIRMWARE_VERSION));
				hardwareVersionText.setText(parameters.get(XBeeConstants.PARAM_HARDWARE_VERSION));
				xbeeProtocolText.setText(parameters.get(XBeeConstants.PARAM_XBEE_PROTOCOL));
				macAddressText.setText(parameters.get(XBeeConstants.PARAM_MAC_ADDRESS));
				nodeIdentifierText.setText(parameters.get(XBeeConstants.PARAM_NODE_IDENTIFIER));
			}
		});
	}
	
	/**
	 * Handles what happens when the 'Read Other Parameter' button is pressed.
	 */
	private void handleReadOtherParameterButtonPressed() {
		// Remove error message.
		clearErrorMessage();
		final ReadOtherParameterDialog readOtherParamDialog = new ReadOtherParameterDialog(XBeeDeviceDiscoveryFragment.this.getActivity());
		readOtherParamDialog.show();
		// This thread waits until dialog is closed, dialog is notified itself when that happens.
		Thread waitThread = new Thread() {
			public void run() {
				synchronized (readOtherParamDialog) {
					try {
						readOtherParamDialog.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String parameter = readOtherParamDialog.getParameter();
				// Sanity checks.
				if (parameter == null)
					return;
				if (parameter.length() != 2) {
					setErrorMessage("Invalid Parameter > " + parameter);
					return;
				}
				// Perform read action.
				readRemoteParameter(parameter, getSelectedXBeeDevice());
			}
		};
		waitThread.start();
	}
	
	/**
	 * Handles what happens when the 'Change Other Parameter' button is pressed.
	 */
	private void handleChangeOtherParameterButtonPressed() {
		// Remove error message.
		clearErrorMessage();
		final ChangeOtherParameterDialog changeOtherParamDialog = new ChangeOtherParameterDialog(XBeeDeviceDiscoveryFragment.this.getActivity());
		changeOtherParamDialog.show();
		// This thread waits until dialog is closed, dialog is notified itself when that happens.
		Thread waitThread = new Thread() {
			public void run() {
				synchronized (changeOtherParamDialog) {
					try {
						changeOtherParamDialog.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String parameter = changeOtherParamDialog.getParameter();
				String value = changeOtherParamDialog.getValue();
				// Sanity checks.
				if (parameter == null)
					return;
				if (parameter.length() != 2) {
					setErrorMessage("Invalid Parameter > " + parameter);
					return;
				}
				if (value == null || value.length() == 0) {
					setErrorMessage("Invalid Parameter Value");
					return;
				}
				// Perform write action.
				writeRemoteParameter(parameter, value, getSelectedXBeeDevice());
			}
		};
		waitThread.start();
	}
	
	/**
	 * Sends the given data to the given remote device.
	 * 
	 * @param data Data to send.
	 * @param remoteDevice Remote XBee device to send data to.
	 */
	private void sendData(final byte[] data, final RemoteXBeeDevice remoteDevice) {
		// Create the send thread.
		Thread sendThread = new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				clearErrorMessage();
				showSendProgressDialog();
				try {
					xbeeManager.sendDataToRemote(data, remoteDevice);
					setSuccessMessage("Data successfully sent!");
				} catch (XBeeException e) {
					setErrorMessage("Error sending data  > " + e.getMessage());
				} finally {
					hideProgressDialog();
				}
			}
			
		});
		sendThread.start();
	}
	
	/**
	 * Reads the given parameter from the local XBee Device.
	 * 
	 * @param parameter Parameter to read.
	 * @param remoteDevice Remote XBee device to read parameter from.
	 */
	private void readRemoteParameter(final String parameter, final RemoteXBeeDevice remoteDevice) {
		// Create the read thread.
		Thread readThread = new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				clearErrorMessage();
				showReadProgressDialog();
				try {
					String value = HexUtils.byteArrayToHexString(remoteDevice.getParameter(parameter));
					setSuccessMessage("Value of " + parameter + " > " + value);
				} catch (XBeeException e) {
					setErrorMessage("Error reading parameter " + parameter + " > " + e.getMessage());
				} finally {
					hideProgressDialog();
				}
			}
		});
		readThread.start();
	}
	
	/**
	 * Writes the given parameter to the local XBee Device.
	 * 
	 * @param parameter Parameter to write.
	 * @param newValue Parameter value given by the user.
	 * @param remoteDevice Remote XBee device to set parameter to.
	 */
	private void writeRemoteParameter(final String parameter, final String newValue, final RemoteXBeeDevice remoteDevice) {
		final String atParam = calculateATParam(parameter);
		final byte[] value = calculateParameterValue(parameter, newValue);
		if (value == null) {
			setErrorMessage("Invalid value for parameter " + atParam + " > " + newValue);
			return;
		}
		// Create the write thread.
		Thread writeThread = new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				clearErrorMessage();
				showWriteProgressDialog();
				try {
					remoteDevice.setParameter(atParam, value);
					updateParameterValue(parameter, newValue);
					setSuccessMessage("Parameter successfully written.");
				} catch (XBeeException e) {
					setErrorMessage("Error writing parameter " + atParam + " > " + e.getMessage());
				} finally {
					hideProgressDialog();
				}
			}
		});
		writeThread.start();
	}
	
	/**
	 * Returns the corresponding AT command for the given parameter.
	 * 
	 * @param parameter Parameter to get its AT command.
	 * 
	 * @return The parameter's AT command.
	 */
	private String calculateATParam(String parameter) {
		if (parameter.equals(XBeeConstants.PARAM_NODE_IDENTIFIER))
			return XBeeConstants.AT_COMMAND_NI;
		else
			return parameter;
	}
	
	/**
	 * Calculates the real value to set for the given parameter.
	 * 
	 * @param parameter Parameter to calculate its value.
	 * @param newValue The parameter value given by the user.
	 * 
	 * @return The calculated parameter value based on the given user value.
	 */
	private byte[] calculateParameterValue(String parameter, String newValue) {
		byte[] value;
		try {
			if (parameter.equals(XBeeConstants.PARAM_NODE_IDENTIFIER))
				value = newValue.getBytes();
			else
				value = HexUtils.hexStringToByteArray(newValue);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Updates the given parameter to to the given value.
	 * 
	 * @param parameter Parameter to restore its value.
	 * @param value Parameter value.
	 */
	private void updateParameterValue(final String parameter, final String value) {
		getActivity().runOnUiThread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				if (parameter.equals(XBeeConstants.PARAM_NODE_IDENTIFIER))
					nodeIdentifierText.setText(value);
			}
		});
	}
	
	/**
	 * Returns the selected remote XBee device from the list.
	 * 
	 * @return Selected Remote XBee Device from the list.
	 */
	public RemoteXBeeDevice getSelectedXBeeDevice() {
		if (remoteDevicesAdapter.getSelection() == RemoteXBeeDevicesAdapter.NO_SELECTION)
			return null;
		if (remoteDevicesAdapter.getSelection() >= remoteDevices.size())
			return null;
		return remoteDevices.get(remoteDevicesAdapter.getSelection());
	}
	
	/**
	 * Displays a 'Reading...' progress dialog.
	 */
	private void showReadProgressDialog() {
		handler.sendEmptyMessage(ACTION_SHOW_READ_PROGRESS_DIALOG);
	}

	/**
	 * Displays a 'Writing...' progress dialog.
	 */
	private void showWriteProgressDialog() {
		handler.sendEmptyMessage(ACTION_SHOW_WRITE_PROGRESS_DIALOG);
	}
	
	/**
	 * Displays a 'Discovering...' progress dialog.
	 */
	private void showDiscoverProgressDialog() {
		handler.sendEmptyMessage(ACTION_SHOW_DISCOVER_PROGRESS_DIALOG);
	}
	
	/**
	 * Displays a 'Sending...' progress dialog.
	 */
	private void showSendProgressDialog() {
		handler.sendEmptyMessage(ACTION_SHOW_SEND_PROGRESS_DIALOG);
	}
	
	/**
	 * Hides the progress dialog it is is open.
	 */
	private void hideProgressDialog() {
		handler.sendEmptyMessage(ACTION_HIDE_PROGRESS_DIALOG);
	}
	
	/**
	 * Removes the error message.
	 */
	private void clearErrorMessage() {
		handler.sendEmptyMessage(ACTION_CLEAR_ERROR_MESSAGE);
	}
	
	/**
	 * Enables the UI buttons.
	 */
	private void enableUIButtons() {
		handler.sendEmptyMessage(ACTION_ENABLE_PARAMETERS_BUTTONS);
	}
	
	/**
	 * Disables the UI buttons.
	 */
	private void disableUIButtons() {
		handler.sendEmptyMessage(ACTION_DISABLE_PARAMETERS_BUTTONS);
	}
	
	/**
	 * Clears the text values.
	 */
	private void clearValues() {
		handler.sendEmptyMessage(ACTION_CLEAR_VALUES);
	}
	
	/**
	 * Enables the discover buttons.
	 */
	private void enableDiscoverButtons() {
		handler.sendEmptyMessage(ACTION_ENABLE_DISCOVER_BUTTONS);
	}
	
	/**
	 * Disables the discover buttons.
	 */
	private void disableDiscoverButtons() {
		handler.sendEmptyMessage(ACTION_DISABLE_DISCOVER_BUTTONS);
	}
	
	/**
	 * Sets the given error message in the activity.
	 * 
	 * @param message Error message to show.
	 */
	private void setErrorMessage(String message) {
		if (message == null)
			return;
		Message msg = handler.obtainMessage(ACTION_SET_ERROR_MESSAGE);
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	/**
	 * Updates the list view.
	 */
	public void updateListView() {
		handler.sendEmptyMessage(ACTION_UPDATE_LIST_VIEW);
	}
	
	/**
	 * Sets the given error message in the activity.
	 * 
	 * @param message Error message to show.
	 */
	private void setSuccessMessage(String message) {
		if (message == null)
			return;
		Message msg = handler.obtainMessage(ACTION_SET_SUCCESS_MESSAGE);
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.digi.android.xbee.xbeemanagersample.AbstractXBeeDeviceFragment#getFragmentName()
	 */
	public String getFragmentName() {
		return getResources().getString(R.string.device_discovery_fragment_title);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IDiscoveryListener#deviceDiscovered(com.digi.xbee.api.RemoteXBeeDevice)
	 */
	public void deviceDiscovered(RemoteXBeeDevice remoteDevice) {
		Message msg = handler.obtainMessage(ACTION_ADD_DEVICE_TO_LIST);
		msg.obj = remoteDevice;
		handler.sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IDiscoveryListener#discoveryError(java.lang.String)
	 */
	public void discoveryError(String errorMessage) {
		setErrorMessage("Error discovering devices > " + errorMessage);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IDiscoveryListener#discoveryFinished(java.lang.String)
	 */
	public void discoveryFinished(String errorMessage) {
		if (errorMessage != null)
			setErrorMessage("Error discovering devices > " + errorMessage);
		hideProgressDialog();
		enableDiscoverButtons();
	}
}
