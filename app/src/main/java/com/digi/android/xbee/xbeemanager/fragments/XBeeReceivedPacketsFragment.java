package com.digi.android.xbee.xbeemanager.fragments;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.digi.android.xbee.xbeemanager.R;
import com.digi.android.xbee.xbeemanager.internal.ReceivedXBeePacketsAdapter;
import com.digi.android.xbee.xbeemanager.internal.RemoteXBeeDevicesAdapter;
import com.digi.android.xbee.xbeemanager.managers.XBeeManager;
import com.digi.android.xbee.xbeemanager.models.AbstractReceivedPacket;
import com.digi.android.xbee.xbeemanager.models.ReceivedDataPacket;
import com.digi.android.xbee.xbeemanager.models.ReceivedIOSamplePacket;
import com.digi.android.xbee.xbeemanager.models.ReceivedModemStatusPacket;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.io.IOSample;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.listeners.IIOSampleReceiveListener;
import com.digi.xbee.api.listeners.IModemStatusReceiveListener;
import com.digi.xbee.api.models.ModemStatusEvent;
import com.digi.xbee.api.models.XBeeMessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class XBeeReceivedPacketsFragment extends AbstractXBeeDeviceFragment
		implements IDataReceiveListener, IIOSampleReceiveListener, IModemStatusReceiveListener {

	// Variables.
	private XBeeManager xbeeManager;
	private ArrayList<AbstractReceivedPacket> receivedPackets;
	private ReceivedXBeePacketsAdapter receivedPacketsAdapter;

	private TextView receivedPacketsText;
	private TextView dateText;
	private TextView typeText;
	private TextView sourceAddressText;
	private TextView packetDataText;
	
	private final Object receivedPacketsLock = new Object();

    private IncomingHandler handler = new IncomingHandler(this);

    // Handler used to perform actions in the UI thread.
    static class IncomingHandler extends Handler {

        private final WeakReference<XBeeReceivedPacketsFragment> wActivity;

        IncomingHandler(XBeeReceivedPacketsFragment activity) {
            wActivity = new WeakReference<XBeeReceivedPacketsFragment>(activity);
        }

        @Override
		public void handleMessage(Message msg) {

            XBeeReceivedPacketsFragment xBeeReceivedPacketsFragment = wActivity.get();
            if (xBeeReceivedPacketsFragment == null) return;

			switch (msg.what) {
			case ACTION_UPDATE_LIST_VIEW:
				xBeeReceivedPacketsFragment.receivedPacketsAdapter.notifyDataSetChanged();
				this.sendEmptyMessage(ACTION_UPDATE_LIST_TEXT);
				break;
			case ACTION_UPDATE_LIST_TEXT:
				xBeeReceivedPacketsFragment.receivedPacketsText.setText(
                        xBeeReceivedPacketsFragment.receivedPackets.size() + " " +
                                xBeeReceivedPacketsFragment.getResources().getString(R.string.packets_received));
				break;
			case ACTION_CLEAR_VALUES:
				xBeeReceivedPacketsFragment.dateText.setText("");
				xBeeReceivedPacketsFragment.typeText.setText("");
				xBeeReceivedPacketsFragment.sourceAddressText.setText("");
				xBeeReceivedPacketsFragment.packetDataText.setText("");
				break;
			case ACTION_ADD_PACKET_TO_LIST:
				synchronized (xBeeReceivedPacketsFragment.receivedPacketsLock) {
					xBeeReceivedPacketsFragment.receivedPackets.add(0, (AbstractReceivedPacket)msg.obj);
					xBeeReceivedPacketsFragment.updateListView();
					if (xBeeReceivedPacketsFragment.receivedPacketsAdapter.getSelection() != ReceivedXBeePacketsAdapter.NOTHING_SELECTED)
						xBeeReceivedPacketsFragment.receivedPacketsAdapter.setSelection(xBeeReceivedPacketsFragment.receivedPacketsAdapter.getSelection() + 1);
					xBeeReceivedPacketsFragment.updateListView();
				}
				break;
			}
		}
	}
	
	/**
	 * Class constructor. Instantiates a new {@code XBeeReceivedFramesFragment}
	 * object with the given parameters.
	 * 
	 * @param xbeeManager XBee Device manager to interact with XBee devices.
	 */
	public XBeeReceivedPacketsFragment(XBeeManager xbeeManager) {
		this.xbeeManager = xbeeManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.xbee_received_data, container, false);
		
		// Check if we have to initialize the received packets variables.
		if (receivedPackets == null) {
			receivedPackets = new ArrayList<AbstractReceivedPacket>();
			receivedPacketsAdapter = new ReceivedXBeePacketsAdapter(getActivity(), receivedPackets);
		}
		
		// Initialize all required UI elements.
		initializeUIElements(view);
		
		// Render initial remote devices list.
		updateListView();
		
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	public void onStop() {
		super.onStop();
		// Unsubscribe listeners.
		xbeeManager.unsubscribeDataPacketListener(this);
		xbeeManager.unsubscribeIOPacketListener(this);
		xbeeManager.unsubscribeModemStatusPacketListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	public void onStart() {
		super.onStart();
		// Subscribe listeners.
		xbeeManager.subscribeDataPacketListener(this);
		xbeeManager.subscribeIOPacketListener(this);
		xbeeManager.subscribeModemStatusPacketListener(this);
	}
	
	/**
	 * Initializes all the required graphic elements of this fragment. 
	 * 
	 * @param view View to search elements in.
	 */
	private void initializeUIElements(View view) {
		// XBee packet List.
        ListView receivedPacketsList = (ListView)view.findViewById(R.id.received_packets_list);
		receivedPacketsList.setAdapter(receivedPacketsAdapter);
		receivedPacketsList.setOnItemClickListener(new OnItemClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				System.out.println("ITEM SELECTED: " + arg2);
				receivedPacketsAdapter.setSelection(arg2);
				updateListView();
				handlePacketSelected(receivedPackets.get(arg2));
			}
		});
		// Buttons.

        Button clearButton = (Button)view.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			/*
			 * (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				handleClearButtonPressed();
			}
		});
		// Texts.
		receivedPacketsText = (TextView)view.findViewById(R.id.received_packets_text);
		dateText = (TextView)view.findViewById(R.id.date_text);
		typeText = (TextView)view.findViewById(R.id.packet_type_text);
		sourceAddressText = (TextView)view.findViewById(R.id.source_address_text);
		packetDataText = (TextView)view.findViewById(R.id.packet_data_text);
	}

	/**
	 * Handles what happens when a received packet is selected from the list.
	 * 
	 * @param selectedPacket Selected received packet.
	 */
	private void handlePacketSelected(AbstractReceivedPacket selectedPacket) {
		if (selectedPacket == null) {
			clearValues();
			return;
		}
		dateText.setText(selectedPacket.getDateAndTimeString());
		typeText.setText(selectedPacket.getType().getName() + " " + getResources().getString(R.string.packet_suffix));
		sourceAddressText.setText(selectedPacket.getSourceAddress().toString());
		packetDataText.setText(selectedPacket.getPacketData());
	}
	
	/**
	 * Handles what happens when the clear button is pressed.
	 */
	private void handleClearButtonPressed() {
		synchronized (receivedPacketsLock) {
			receivedPackets.clear();
		}
		receivedPacketsAdapter.setSelection(RemoteXBeeDevicesAdapter.NOTHING_SELECTED);
		updateListView();
		handlePacketSelected(null);
	}
	
	/**
	 * Updates the list view.
	 */
	public void updateListView() {
		handler.sendEmptyMessage(ACTION_UPDATE_LIST_VIEW);
	}
	
	/**
	 * Clears the text values.
	 */
	private void clearValues() {
		handler.sendEmptyMessage(ACTION_CLEAR_VALUES);
	}
	
	/**
	 * Adds the given packet to the list of packets.
	 * 
	 * @param receivedPacket Packet to add to the list.
	 */
	private void addPacketToList(AbstractReceivedPacket receivedPacket) {
		Message msg = handler.obtainMessage(ACTION_ADD_PACKET_TO_LIST);
		msg.obj = receivedPacket;
		handler.sendMessage(msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.digi.android.xbee.xbeemanagersample.AbstractXBeeDeviceFragment#getFragmentName()
	 */
	public String getFragmentName() {
		return getResources().getString(R.string.frames_fragment_title);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IDataReceiveListener#dataReceived(com.digi.xbee.api.models.XBeeMessage)
	 */
	public void dataReceived(XBeeMessage xbeeMessage) {
		ReceivedDataPacket receivedPacket = new ReceivedDataPacket(xbeeMessage.getDevice().get64BitAddress(), xbeeMessage.getData());
		addPacketToList(receivedPacket);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IIOSampleReceiveListener#ioSampleReceived(com.digi.xbee.api.RemoteXBeeDevice, com.digi.xbee.api.io.IOSample)
	 */
	public void ioSampleReceived(RemoteXBeeDevice remoteDevice, IOSample ioSample) {
		ReceivedIOSamplePacket receivedPacket = new ReceivedIOSamplePacket(remoteDevice.get64BitAddress(), ioSample);
		addPacketToList(receivedPacket);
	}

	/*
	 * (non-Javadoc)
	 * @see com.digi.xbee.api.listeners.IModemStatusReceiveListener#modemStatusEventReceived(com.digi.xbee.api.models.ModemStatusEvent)
	 */
	public void modemStatusEventReceived(ModemStatusEvent modemStatusEvent) {
		ReceivedModemStatusPacket receivedPacket = new ReceivedModemStatusPacket(xbeeManager.getLocalXBee64BitAddress(), modemStatusEvent);
		addPacketToList(receivedPacket);
	}
}
