package com.digi.android.sample.xbeemanager.models;

import com.digi.xbee.api.models.ModemStatusEvent;
import com.digi.xbee.api.models.XBee64BitAddress;

public class ReceivedModemStatusPacket extends AbstractReceivedPacket {

	// Variables.
	private ModemStatusEvent modemStatusEvent;
	
	/**
	 * Class constructor. Instantiates a new {@code ReceivedModemStatusPacket}
	 * object with the given parameters.
	 * 
	 * @param sourceAddress 64-bit address of the device that sent this packet.
	 * @param modemStatusEvent Received modem status event.
	 */
	public ReceivedModemStatusPacket(XBee64BitAddress sourceAddress, ModemStatusEvent modemStatusEvent) {
		super(sourceAddress, PacketType.TYPE_MODEM_STATUS);
		this.modemStatusEvent = modemStatusEvent;
	}

	@Override
	public String getShortPacketData() {
		return modemStatusEvent.getDescription();
	}
	
	@Override
	public String getPacketData() {
		return modemStatusEvent.getDescription();
	}
}
