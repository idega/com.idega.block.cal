/*
 * Created on Jan 15, 2004
 */
package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarWindow extends StyledIWAdminWindow{
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";
	
	
	private Table table = null;
	private String borderWhiteTableStyle = "borderAllWhite";
	private String styledLink = "styledLink";
	
	private CalendarView calendar;
	private CalBusiness calBiz;
	
	
	public CalendarWindow() {
		setHeight(750);
		setWidth(900);
		setResizable(true);
		setScrollbar(true);
	}
	
	public void initializeWindow(IWContext iwc) {
		
//		SmallCalendar smallCalendar = new SmallCalendar();
		calendar = new CalendarView();
		

		table = new Table();
		table.setWidth("100%");
		table.setHeight(1,1,"100%");
		table.setAlignment("center");
//		table.setWidth(1,"15%");
		table.setCellspacing(5);
//		table.setStyleClass(1,3,borderWhiteTableStyle);
//		table.setVerticalAlignment(1,1,"top");
//		table.setVerticalAlignment(3,1,"top");
//		table.setVerticalAlignment(3,2,"top");
//		table.setVerticalAlignment(3,3,"top");
//		table.setStyleClass(3,1,"main");
//		
//		table.mergeCells(1,1,1,3);
//		table.mergeCells(2,1,2,3);
//		
		table.add(calendar,1,1);
//		table.add(smallCalendar,3,3);
	
		add(table,iwc);
		
	}
	
	public void main(IWContext iwc) throws Exception {
		
//		IWResourceBundle iwrb = getResourceBundle(iwc);	
		
		initializeWindow(iwc);
				
//		CalBusiness biz = getCalBusiness(iwc);
//		Iterator ledgerIter = biz.getAllLedgers().iterator();
//		while(ledgerIter.hasNext()) {
//			CalendarLedger ledger = (CalendarLedger) ledgerIter.next();
//			Link ledgerLink =new Link(ledger.getName());
//			ledgerLink.setStyleClass(styledLink);
//			ledgerLink.addParameter("ledger",ledger.getPrimaryKey().toString());
//			ledgerLink.setWindowToOpen(LedgerWindow.class);
//			table.add(ledgerLink,3,1);
//			table.add("<br>",3,1);
//		}
//		CalendarEntryCreator creator = new CalendarEntryCreator();
//		table.add(creator,3,2);
		
		
//		Text linkText = new Text(iwrb.getLocalizedString("calendarwindow.new_ledger","New Ledger"));
//		Link newLedgerLink = new Link(linkText);
//		newLedgerLink.setStyleClass(styledLink);
//		newLedgerLink.setWindowToOpen(CreateLedgerWindow.class);
//		table.add(newLedgerLink,3,1);
//		
		

	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		if (calBiz == null) {
			try {
				calBiz = (CalBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CalBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return calBiz;
	}


}

