package com.idega.block.cal.business;

public class CalEntry {
	private String entryName = null;
	private String entryDate = null;
	private String entryEndDate = null;	
	private String entryTime = null;
	private String entryEndTime = null;
	private String repeat = null;
	private String entryTypeName = null;
		
public CalEntry(String entryName, String entryDate, String entryEndDate, String entryTime, String entryEndTime, String repeat, String entryTypeName) {
		super();
		this.entryName = entryName;
		this.entryDate = entryDate;
		this.entryEndDate = entryEndDate;
		this.entryTime = entryTime;
		this.entryEndTime = entryEndTime;
		this.repeat = repeat;
		this.entryTypeName = entryTypeName;
	}
	//	public CalEntry(String entryName, String entryDate, String entryEndDate, String repeat, String entryTypeName) {
//		super();
//		this.entryName = entryName;
//		this.entryDate = entryDate;
//		this.entryEndDate = entryEndDate;
//		this.repeat = repeat;
//		this.entryTypeName = entryTypeName;
//	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getEntryEndDate() {
		return entryEndDate;
	}
	public void setEntryEndDate(String entryEndDate) {
		this.entryEndDate = entryEndDate;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getEntryTypeName() {
		return entryTypeName;
	}
	public void setEntryTypeName(String entryTypeName) {
		this.entryTypeName = entryTypeName;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getEntryEndTime() {
		return entryEndTime;
	}
	public void setEntryEndTime(String entryEndTime) {
		this.entryEndTime = entryEndTime;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
}
