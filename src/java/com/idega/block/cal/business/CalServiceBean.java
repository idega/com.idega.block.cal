package com.idega.block.cal.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.block.category.data.CategoryEntityBMPBean;
import com.idega.business.IBOServiceBean;
import com.idega.data.EntityFinder;
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
//			System.out.println(""+ledger.getID()+" "+ledger.getName()+" "+ "L");
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
//			System.out.println(""+entryType.getID()+" "+entryType.getName()+" "+ "T");
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
			System.out.println(""+ledger.getID()+" "+ledger.getName()+" "+ "L");
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
			System.out.println(""+entryType.getID()+" "+entryType.getName()+" "+ "T");
		}
		
		
		return calendarParameters;
	}
	
	public int setCheckedParameters(List checkedParameters){
		System.out.println("checked parameters");
		for (int i = 0; i < checkedParameters.size(); i++) {
			System.out.println(checkedParameters.get(i));
		}
		return 0;
	}
	
}
