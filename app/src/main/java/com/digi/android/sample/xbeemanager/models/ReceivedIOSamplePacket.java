package com.digi.android.sample.xbeemanager.models;

import com.digi.xbee.api.io.IOLine;
import com.digi.xbee.api.io.IOSample;
import com.digi.xbee.api.models.XBee64BitAddress;

public class ReceivedIOSamplePacket extends AbstractReceivedPacket {

	// Constants.
	private final static String ANALOG_VALUES = "Analog values:\n";
	private final static String ANALOG_VALUES_SHORT = " Analog - ";
	private final static String DIGITAL_VALUES = "Digital values:\n";
	private final static String DIGITAL_VALUES_SHORT = " Digital";
	private final static String BULLET = "    - ";
	private final static String VALUE_SEPARATOR = ": ";
	private final static String END_LINE = "\n";
	
	// Variables.
	private IOSample ioSample;
	
	/**
	 * Class constructor. Instantiates a new {@code ReceivedIOSamplePacket}
	 * object with the given parameters.
	 * 
	 * @param sourceAddress 64-bit address of the device that sent this packet.
	 * @param ioSample Received IO sample.
	 */
	public ReceivedIOSamplePacket(XBee64BitAddress sourceAddress, IOSample ioSample) {
		super(sourceAddress, PacketType.TYPE_IO_SAMPLE);
		this.ioSample = ioSample;
	}

	@Override
	public String getShortPacketData() {
		StringBuilder sb = new StringBuilder();
		if (ioSample.hasAnalogValues())
			sb.append(ioSample.getAnalogValues().size()).append(ANALOG_VALUES_SHORT);
		else
			sb.append("0").append(ANALOG_VALUES_SHORT);
		if (ioSample.hasDigitalValues())
			sb.append(ioSample.getDigitalValues().size()).append(DIGITAL_VALUES_SHORT);
		else 
			sb.append("0").append(DIGITAL_VALUES_SHORT);
		return sb.toString();
	}
	
	@Override
	public String getPacketData() {
		StringBuilder sb = new StringBuilder();
		if (ioSample.hasAnalogValues()) {
			sb.append(ANALOG_VALUES);
			for (IOLine ioLine:ioSample.getAnalogValues().keySet()) {
				sb.append(BULLET);
				sb.append(ioLine.name()).append(VALUE_SEPARATOR);
				sb.append(ioSample.getAnalogValues().get(ioLine));
				sb.append(END_LINE);
			}
			sb.append(END_LINE);
		}
		if (ioSample.hasDigitalValues()) {
			sb.append(DIGITAL_VALUES);
			for (IOLine ioLine:ioSample.getDigitalValues().keySet()) {
				sb.append(BULLET);
				sb.append(ioLine.name()).append(VALUE_SEPARATOR);
				sb.append(ioSample.getDigitalValues().get(ioLine).getName());
				sb.append(END_LINE);
			}
		}
		return sb.toString();
	}
}
