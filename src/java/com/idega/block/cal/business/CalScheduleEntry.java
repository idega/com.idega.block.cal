package com.idega.block.cal.business;

public class CalScheduleEntry {
	private String id = null;
	private String entryName = null;
	private String entryDate = null;
	private String localizedEntryDate = null;
	private String entryEndDate = null;	
	private String localizedEntryEndDate = null;	
	private String entryTime = null;
	private String entryEndTime = null;
	private String repeat = null;
	private String entryTypeName = null;
	private String entryDescription = null;
	
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
	public String getEntryDescription() {
		return entryDescription;
	}
	public void setEntryDescription(String entryDescription) {
		this.entryDescription = entryDescription;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLocalizedEntryDate() {
		return localizedEntryDate;
	}
	public void setLocalizedEntryDate(String localizedEntryDate) {
		this.localizedEntryDate = localizedEntryDate;
	}
	public String getLocalizedEntryEndDate() {
		return localizedEntryEndDate;
	}
	public void setLocalizedEntryEndDate(String localizedEntryEndDate) {
		this.localizedEntryEndDate = localizedEntryEndDate;
	}
}
