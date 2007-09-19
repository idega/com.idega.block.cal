package com.idega.block.cal.business;

public class CalendarLedgersAndTypes {

	private String id = null;
	private String name = null;
	private String type = null;

	public CalendarLedgersAndTypes(String id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
