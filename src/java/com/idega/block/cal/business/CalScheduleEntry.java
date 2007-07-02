package com.idega.block.cal.business;

public class CalScheduleEntry {
	private String entryName = null;
	private String entryDate = null;
	private String entryEndDate = null;	
	private String entryTime = null;
	private String entryEndTime = null;
	private String repeat = null;
	private String entryTypeName = null;
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
	public String getEntryEndTime() {
		return entryEndTime;
	}
	public void setEntryEndTime(String entryEndTime) {
		this.entryEndTime = entryEndTime;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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
}
