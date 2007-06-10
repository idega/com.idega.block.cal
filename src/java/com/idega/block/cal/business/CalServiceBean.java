package com.idega.block.cal.business;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.text.ArabicShaping;
import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;

public class CalServiceBean extends IBOServiceBean implements CalService {

	public void setConnectionData(String serverName, String login,
			String password) {
		// TODO Auto-generated method stub
	}
	
	public List getCalendarParametersList(){
		
		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		List <CalendarLedgerBMPBean>allLedgers = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllLedgers();
		
		List <CalendarEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
		
		for (int i = 0; i < allLedgers.size(); i++) {
			CalendarLedgerBMPBean ledger = allLedgers.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}

		return calendarParameters;
	}
	
	public List getCalendarParameters(String id){

		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		List <CalendarLedgerBMPBean>ledgersByGroupId = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getLedgersByGroupId(id);
		
		List <CalendarEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
		
		for (int i = 0; i < ledgersByGroupId.size(); i++) {
			CalendarLedgerBMPBean ledger = ledgersByGroupId.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}
				
		return calendarParameters;
	}
	
	public List setCheckedParameters(List checkedParameters){
		
		IWContext iwc = IWContext.getInstance();
		String parameter = null;
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		List<String> listOfLedgerIds = new ArrayList<String>();
		List<String> listOfEntryTypesIds = new ArrayList<String>();
		
		for (int i = 0; i < checkedParameters.size(); i++) {
//			System.out.println(checkedParameters.get(i));
			parameter = (String)(checkedParameters.get(i));
			if(parameter.substring(0, 1).equals("L")){
				listOfLedgerIds.add(parameter.substring(1));
			}
			if(parameter.substring(0, 1).equals("T")){
				listOfEntryTypesIds.add(parameter.substring(1));
			}			
		}
		
		List<CalendarEntryBMPBean> entriesToDisplay = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getEntriesByLedgersAndEntryTypes(listOfEntryTypesIds, listOfLedgerIds);
		
		List result = new ArrayList<CalEntry>();

		for (int i = 0; i < entriesToDisplay.size(); i++) {
			CalendarEntryBMPBean entry = entriesToDisplay.get(i);
			if(checkIfTypeIsCorrect(entry, listOfEntryTypesIds)){
				result.add(new CalEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), getDate(entry.getStringColumnValue("CAL_ENTRY_DATE")), getDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE")),getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")), getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
			}
		}

		return result;
	}
	
	private boolean checkIfTypeIsCorrect(CalendarEntryBMPBean entry, List<String> entryTypesIds){
		String typeId = entry.getStringColumnValue("CAL_TYPE_ID");
//System.out.println(entry.getColumn("CAL_TYPE_ID"));
//System.out.println(entry.getColumnValue("CAL_TYPE_ID"));
//System.out.println(entry.getStringColumnValue("CAL_TYPE_ID"));
		for (int i = 0; i < entryTypesIds.size(); i++) {
			if(entryTypesIds.get(i).equals(typeId))
				return true;
		}
		return false;
	}
	private String getDate(String entryDate){
		String date = entryDate.substring(0, 10);
		return date.replaceAll("-", "");
	}

	private String getTime(String entryDate){
		return entryDate.substring(11,16);
	}
	
}
