package com.digi.android.sample.xbeemanager.models;

import com.digi.xbee.api.models.XBee64BitAddress;

public class ReceivedDataPacket extends AbstractReceivedPacket {

	// Variables.
	private byte[] receivedData;
	
	/**
	 * Class constructor. Instantiates a new {@code ReceivedDataPacket} object
	 * with the given parameters.
	 * 
	 * @param sourceAddress 64-bit address of the device that sent this packet.
	 * @param receivedData Received packet data.
	 */
	public ReceivedDataPacket(XBee64BitAddress sourceAddress, byte[] receivedData) {
		super(sourceAddress, PacketType.TYPE_DATA);
		this.receivedData = receivedData;
	}

	@Override
	public String getShortPacketData() {
		return new String(receivedData);
	}
	
	@Override
	public String getPacketData() {
		return new String(receivedData);
	}
}
