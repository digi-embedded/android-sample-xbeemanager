/*
 * Copyright (c) 2014-2021, Digi International Inc. <support@digi.com>
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

package com.digi.android.sample.xbeemanager.models;

import com.digi.xbee.api.models.ModemStatusEvent;
import com.digi.xbee.api.models.XBee64BitAddress;

public class ReceivedModemStatusPacket extends AbstractReceivedPacket {

	// Variables.
	private final ModemStatusEvent modemStatusEvent;

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
