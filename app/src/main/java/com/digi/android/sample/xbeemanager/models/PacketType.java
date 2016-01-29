package com.digi.android.sample.xbeemanager.models;

public enum PacketType {
	
	// Enumeration entries.
	TYPE_DATA(0, "Data"),
	TYPE_IO_SAMPLE(1, "IO Sample"),
	TYPE_MODEM_STATUS(2, "Modem Status");
	
	// Variables.
	private int id;
	
	private String name;
	
	/**
	 * Class constructor. Instantiates a new {@code PacketType} enumeration
	 * entry with the given parameters.
	 * 
	 * @param id ID of the enumeration entry.
	 * @param name Name of the enumeration entry.
	 */
	PacketType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Returns the enumeration ID.
	 * 
	 * @return The enumeration ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the enumeration name.
	 * 
	 * @return The enumeration name.
	 */
	public String getName() {
		return name;
	}
}
