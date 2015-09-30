package com.digi.android.xbee.xbeemanager.models;

import java.util.Calendar;
import java.util.Date;

import com.digi.xbee.api.models.XBee64BitAddress;

public abstract class AbstractReceivedPacket {
	
	// Variables.
	protected Date receivedDate;
	
	protected XBee64BitAddress sourceAddress;
	
	private PacketType type;
	
	/**
	 * Class constructor. Instantiates a new {@code AbstractReceivedXBeePacket}
	 * object with the given parameters.
	 * 
	 * @param sourceAddress 64-bit address of the device that sent this packet.
	 * @param type Packet type.
	 */
	public AbstractReceivedPacket(XBee64BitAddress sourceAddress, PacketType type) {
		this.sourceAddress = sourceAddress;
		this.type = type;
		receivedDate = new Date();
	}
	
	/**
	 * Returns the date at which packet was received.
	 * 
	 * @return The date at which packet was received.
	 */
	public Date getDate() {
		return receivedDate;
	}
	
	/**
	 * Returns the received time in format HH:mm:ss.
	 * 
	 * @return Received time in format HH:mm:ss.
	 */
	public String getTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(receivedDate);
		StringBuffer timeString = new StringBuffer();
		timeString.append(completeNumber(cal.get(Calendar.HOUR_OF_DAY))).append(':');
		timeString.append(completeNumber(cal.get(Calendar.MINUTE))).append(':');
		timeString.append(completeNumber(cal.get(Calendar.SECOND))).append('.');
		timeString.append(completeMilliseconds(cal.get(Calendar.MILLISECOND)));
		return timeString.toString();
	}
	
	/**
	 * Returns the received date and time in format yyyy-MM-dd HH:mm:ss.
	 * 
	 * @return Received date and time in format yyyy-MM-dd HH:mm:ss.
	 */
	public String getDateAndTimeString() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(receivedDate);
		StringBuffer dateString = new StringBuffer();
		StringBuffer timeString = new StringBuffer();
		dateString.append(cal.get(Calendar.YEAR)).append('-');
		dateString.append(cal.get(Calendar.MONTH) + 1).append('-');
		dateString.append(cal.get(Calendar.DAY_OF_MONTH));
		dateString.append(" ");
		timeString.append(completeNumber(cal.get(Calendar.HOUR_OF_DAY))).append(':');
		timeString.append(completeNumber(cal.get(Calendar.MINUTE))).append(':');
		timeString.append(completeNumber(cal.get(Calendar.SECOND))).append('.');
		timeString.append(completeMilliseconds(cal.get(Calendar.MILLISECOND)));
		return dateString.toString() + timeString.toString();
	}
	
	/**
	 * Returns the received packet source address.
	 * 
	 * @return The received packet 64-bit source address.
	 */
	public XBee64BitAddress getSourceAddress() {
		return sourceAddress;
	}
	
	/**
	 * Returns the packet type.
	 * 
	 * @return The packet type.
	 */
	public PacketType getType() {
		return type;
	}
	
	/**
	 * Returns the packet data in a readable format.
	 * 
	 * @return The packet data in a readable format.
	 */
	public abstract String getPacketData();
	
	/**
	 * Returns the packet data (short format) in a readable format.
	 * 
	 * @return The packet data (short formal) in a readable format.
	 */
	public abstract String getShortPacketData();
	
	/**
	 * Completes the given number by adding a preceding zero if necessary.
	 * 
	 * @param number Number to complete.
	 * @return Completed number.
	 */
	private String completeNumber(int number) {
		if (number < 10)
			return "0" + number;
		return "" + number;
	}
	
	/**
	 * Completes the given milliseconds value by adding '0' until reaching 3
	 * floating point decimals.
	 * 
	 * @param milliseconds Milliseconds number to complete.
	 * @return Completed milliseconds number.
	 */
	private String completeMilliseconds(int milliseconds) {
		if (("" + milliseconds).length() == 3)
			return "" + milliseconds;
		int diff = 3 - ("" + milliseconds).length();
		StringBuilder sb = new StringBuilder();
		sb.append(milliseconds);
		for (int i = 0; i < diff; i++){
			sb.append("0");
		}
		return sb.toString();
	}
}
