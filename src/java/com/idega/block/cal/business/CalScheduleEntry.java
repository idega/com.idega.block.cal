package com.idega.block.cal.business;

public class CalScheduleEntry {
	
	private String id = null;
	private String entryName = null;
	
	private String entryDate = null;
	private String localizedDate = null;
	
	private String entryEndDate = null;	
	private String localizedEndDate = null;	
	
	private String entryTime = null;
	private String localizedTime = null;
	
	private String entryEndTime = null;
	private String localizedEndTime = null;
	
	private String localizedShortStartDate = null;
	private String localizedShortEndDate = null;
	
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
	public String getLocalizedDate() {
		return localizedDate;
	}
	public void setLocalizedDate(String localizedDate) {
		this.localizedDate = localizedDate;
	}
	public String getLocalizedEndDate() {
		return localizedEndDate;
	}
	public void setLocalizedEndDate(String localizedEndDate) {
		this.localizedEndDate = localizedEndDate;
	}
	public String getLocalizedTime() {
		return localizedTime;
	}
	public void setLocalizedTime(String localizedTime) {
		this.localizedTime = localizedTime;
	}
	public String getLocalizedEndTime() {
		return localizedEndTime;
	}
	public void setLocalizedEndTime(String localizedEndTime) {
		this.localizedEndTime = localizedEndTime;
	}
	public String getLocalizedShortStartDate() {
		return localizedShortStartDate;
	}
	public void setLocalizedShortStartDate(String localizedShortStartDate) {
		this.localizedShortStartDate = localizedShortStartDate;
	}
	public String getLocalizedShortEndDate() {
		return localizedShortEndDate;
	}
	public void setLocalizedShortEndDate(String localizedShortEndDate) {
		this.localizedShortEndDate = localizedShortEndDate;
	}
	
}
