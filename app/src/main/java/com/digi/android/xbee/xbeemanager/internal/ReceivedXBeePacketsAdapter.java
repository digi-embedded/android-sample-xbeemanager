package com.digi.android.xbee.xbeemanager.internal;

import java.util.ArrayList;

import com.digi.android.xbee.xbeemanager.R;
import com.digi.android.xbee.xbeemanager.models.AbstractReceivedPacket;
import com.digi.xbee.api.utils.ByteUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ReceivedXBeePacketsAdapter extends BaseAdapter {

	public static final int NOTHING_SELECTED = -1;
	
	// Variables.
	private ArrayList<AbstractReceivedPacket> receivedPackets;
	
	private LayoutInflater layoutInflater;
	
	private Context context;
	
	private int selectedItem = NOTHING_SELECTED;
	
	/**
	 * Class constructor. Instantiates a new {@code RemoteXBeeDevicesAdapter}
	 * object with the given parameters.
	 * 
	 * @param context Application context.
	 * @param receivedPackets List of received XBee packets.
	 */
	public ReceivedXBeePacketsAdapter(Context context, ArrayList<AbstractReceivedPacket> receivedPackets) {
		this.context = context;
		this.receivedPackets = receivedPackets;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Sets the selected item position.
	 * 
	 * @param position Selected item position.
	 */
	public void setSelection(int position) {
		selectedItem = position;
	}
	
	/**
	 * Retrieves the selected item position.
	 * 
	 * @return The selected item position.
	 */
	public int getSelection() {
		return selectedItem;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return receivedPackets.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return receivedPackets.get(position);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return ByteUtils.byteArrayToLong(receivedPackets.get(position).getSourceAddress().getValue());
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Inflate the view if required.
		View view = convertView;
		if (convertView == null)
			view = layoutInflater.inflate(R.layout.received_frame_list_item, null);

		// Find view fields.
		RelativeLayout rootLayout = (RelativeLayout)view.findViewById(R.id.root_layout);
		TextView dateText = (TextView)view.findViewById(R.id.date_text);
		TextView typeText = (TextView)view.findViewById(R.id.packet_type_text);
		TextView sourceAddressText = (TextView)view.findViewById(R.id.source_address_text);
		TextView packetDataText = (TextView)view.findViewById(R.id.packet_data_text);
		
		// Retrieve selected packet.
		final AbstractReceivedPacket receivedPacket = receivedPackets.get(position);
		
		// Set background.
		if (position == selectedItem)
			rootLayout.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
		else
			rootLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
		
		// Fill in all fields.
		dateText.setText(receivedPacket.getTimeString());
		typeText.setText(receivedPacket.getType().getName());
		sourceAddressText.setText(receivedPacket.getSourceAddress().toString());
		packetDataText.setText(receivedPacket.getShortPacketData());
		
		return view;
	}
}
