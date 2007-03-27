package com.idega.block.cal.business;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import javax.servlet.ServletException;

import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.rss.business.RSSAbstractProducer;
import com.idega.block.rss.business.RSSBusiness;
import com.idega.block.rss.business.RSSProducer;
import com.idega.block.rss.data.RSSRequest;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.builder.data.ICPage;
import com.idega.core.search.business.SearchResult;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWContentEvent;
import com.idega.slide.business.IWSlideChangeListener;
import com.idega.slide.business.IWSlideService;
import com.idega.util.IWTimestamp;
import com.idega.util.datastructures.Collectable;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.tools.javac.comp.Todo;

/**
 * 
 * @author justinas
 *
 */

public class CalendarRSSProducer  extends RSSAbstractProducer implements RSSProducer, IWSlideChangeListener{
	private List rssFileURIsCacheList = new ArrayList();
	private static final int DATE_LENGTH = 8;
	
	public void handleRSSRequest(RSSRequest rssRequest) throws IOException {
		String extraURI = rssRequest.getExtraUri();
		String feedParentFolder = null;
		String feedFile = null;
		if(extraURI == null)
			extraURI = "";
		if((!extraURI.endsWith("/")) && (extraURI.length() != 0))
			extraURI = extraURI.concat("/");
		
		IWContext iwc = getIWContext(rssRequest);
		String uri = null;
//		String groupPeriod = getPeriod(extraURI); 
//			group.substring(groupID.length()+1, group.length());
		
		if(extraURI.startsWith("period")){
try {
				feedParentFolder = "/files/cms/calendar/rss/period/";
	//			uri = extraURI.substring("period/".length(), extraURI.length()-1);
				uri = extraURI.substring("period/".length(), extraURI.length());

				feedFile = "period_"+getName(uri)+iwc.getLocale().getLanguage()+".xml";
} catch (RuntimeException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		}
		else if(extraURI.startsWith("group")){
			feedParentFolder = "/files/cms/calendar/rss/group/";
//			feedFile = "group_"+extraURI.substring("group/".length(), extraURI.length()-1)+"_"+iwc.getLocale().toString()+".xml";
			uri = extraURI.substring("group/".length(), extraURI.length());
//System.out.println("getName "+getName(uri));
//System.out.println("getPeriod "+getPeriod(uri));				
			feedFile = "group_"+getName(uri)+getPeriod(uri)+iwc.getLocale().getLanguage()+".xml";
//System.out.println("uri "+uri);
//System.out.println("feedFile "+feedFile);				

		}
		else if(extraURI.startsWith("ledger")){
			feedParentFolder = "/files/cms/calendar/rss/ledger/";
			feedFile = "ledger_"+extraURI.substring("ledger/".length(), extraURI.length()-1)+"_"+iwc.getLocale().getLanguage()+".xml";			
		}
		else{
//			TODO handle wrong URI
		}
		String realURI = "/content"+feedParentFolder+feedFile;	
		
//		if(rssFileURIsCacheList.contains(feedFile)){
		if(false){
			try {
				this.dispatch(realURI, rssRequest);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//generate rss and store and the dispatch to it
			//and add a listener to that directory
			try {
				searchForEntries(rssRequest,feedParentFolder,feedFile, extraURI);
				rssFileURIsCacheList.add(feedFile);
					
				this.dispatch(realURI, rssRequest);
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		}
	}
	
	public void searchForEntries(RSSRequest rssRequest, String feedParentFolder, String feedFileName, String extraURI) {
		IWContext iwc = getIWContext(rssRequest);
		Collection entries = null;
		String title = null;
		if(extraURI.startsWith("period"))
			entries = getEntriesByPeriod(extraURI);
		if(extraURI.startsWith("group"))
			entries = getEntriesByGroup(extraURI);
		if(extraURI.startsWith("ledger"))
			entries = getEntriesByLedger(extraURI);
		if(extraURI.startsWith("events"))
			entries = getEntriesByEvents(extraURI);		
		if (entries != null){
			Date now = new Date();
			RSSBusiness rss = null;
			try {
				rss = (RSSBusiness) IBOLookup.getServiceInstance(iwc,RSSBusiness.class);			
			} catch (IBOLookupException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String serverName = iwc.getServerURL();
			serverName = serverName.substring(0, serverName.length()-1);
			SyndFeed allArticles = rss.createNewFeed("title", serverName , "File feed generated by IdegaWeb ePlatform, Idega Software, http://www.idega.com", "atom_1.0", iwc.getCurrentLocale().toString(), new Timestamp(now.getTime()));
			
//			sEntry.			
//			sEntry.setDescription(arg0)
			List<SyndEntry> syndEntries = new ArrayList<SyndEntry>();
			try {
//				List<SyndEntry> syndEntries = new ArrayList<SyndEntry>();
				List calendarEntries = new ArrayList<SyndEntry>(entries);
				CalendarEntry calEntry = null;
				for (int i = 0; i < entries.size(); i++) {
//					syndEntries.set(i, (SyndEntry)(calendarEntries.get(i)));
					SyndEntry sEntry = new SyndEntryImpl();
					calEntry = (CalendarEntry)calendarEntries.get(i);
//					sEntry.se
					SyndContent scont = new SyndContentImpl();
					String content = "Name: "+calEntry.getName()+" Type: "+calEntry.getEntryTypeName()+" From: "+calEntry.getDate()+" To: "+calEntry.getEndDate();
//System.out.println(content);					
					scont.setValue(content);
					sEntry.setTitle(calEntry.getName());
					sEntry.setDescription(scont);
					syndEntries.add(sEntry);
				}
			} catch (RuntimeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			allArticles.setEntries(new ArrayList<SyndEntry>(entries));
			allArticles.setEntries(syndEntries);
			try {
				String allArticlesContent = rss.convertFeedToAtomXMLString(allArticles);
				IWSlideService service = this.getIWSlideService(rssRequest);
				service.uploadFileAndCreateFoldersFromStringAsRoot(feedParentFolder, feedFileName, allArticlesContent, this.RSS_CONTENT_TYPE, true);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}	
	
	public void onSlideChange(IWContentEvent contentEvent) {
//		On a file change this code checks if an rss file already exists and if so updates it (overwrites) with a new folder list
		String URI = contentEvent.getContentEvent().getUri();
		//only do it for articles (whenever something changes in the articles folder)
		if(URI.indexOf("/cms/calendar/")>-1){
			//TODO dont remove cache on COmments change, just check the URI for commentesrss.
			getrssFileURIsCacheList().clear();
		}
	}
	protected List getrssFileURIsCacheList() {
		return rssFileURIsCacheList;
	}
	private Collection getEntriesByPeriod(String extraURI){
		String period = extraURI.substring("period/".length());
		String fromStr = period.substring(0, DATE_LENGTH);
		String toStr = period.substring(DATE_LENGTH+1, period.length()-1);
		Timestamp fromTmst = getTimeStampFromString(fromStr);
		Timestamp toTmst = getTimeStampFromString(toStr);
//System.out.println("by period");
//System.out.println("from "+ fromTmst.toString());
//System.out.println("to "+ toTmst.toString());
		CalBusiness calendar = new CalBusinessBean(); 
		return calendar.getEntriesBetweenTimestamps(fromTmst, toTmst);
	}
	
	private Collection getEntriesByGroup(String extraURI){
		String group = extraURI.substring("group/".length());
		String groupID = group.substring(0, group.indexOf("/"));
		String groupPeriod = group.substring(groupID.length()+1, group.length());
		Timestamp from = null;
		Timestamp to = null;
		CalBusiness calendar = new CalBusinessBean();
		int entryGroupID;
		try {
			entryGroupID = Integer.parseInt(groupID);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		if (groupPeriod.length() != 0){
//			String period = extraURI.substring(group.indexOf("/"));
			String fromStr = groupPeriod.substring(0, DATE_LENGTH);
			String toStr = groupPeriod.substring(DATE_LENGTH+1, groupPeriod.length()-1);
			from = getTimeStampFromString(fromStr);
			to = getTimeStampFromString(toStr);
			Collection coll = calendar.getEntriesBetweenTimestamps(from, to);
			List<CalendarEntry> entries = new ArrayList<CalendarEntry>();
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				CalendarEntry element = (CalendarEntry) iter.next();
				int id = element.getGroupID();
				if (element.getGroupID() == entryGroupID){
					entries.add(element);
				}				
			}
//System.out.println("by period and group");
//System.out.println("group "+ groupID);
//System.out.println("from "+ from.toString());
//System.out.println("to "+ to.toString());

			return entries;
		}
//System.out.println("by group");
//System.out.println("group "+ groupID);
//		return calendar.getEntryGroup(entryGroupID);
//		calendar.getent
		
//		return calendar.getEntriesByEntryGroupID(entryGroupID);
		return calendar.getEntriesByICGroup(entryGroupID);
	}	
	private Collection getEntriesByLedger(String extraURI){
		String ledger = extraURI.substring("ledger/".length());
		String ledgerID = ledger.substring(0, ledger.indexOf("/"));
		String ledgerPeriod = ledger.substring(ledgerID.length()+1, ledger.length());		
		Timestamp from = null;
		Timestamp to = null;
		CalBusiness calendar = new CalBusinessBean();
		int ledgerIdInt;
		try {
			ledgerIdInt = Integer.parseInt(ledgerID);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		if (ledgerPeriod.length() != 0){
//			String period = extraURI.substring(ledger.indexOf("/"));
			String fromStr = ledgerPeriod.substring(0, DATE_LENGTH);
			String toStr = ledgerPeriod.substring(DATE_LENGTH+1, ledgerPeriod.length()-1);
			from = getTimeStampFromString(fromStr);
			to = getTimeStampFromString(toStr);
			Collection coll = calendar.getEntriesBetweenTimestamps(from, to);
			List<CalendarEntry> entries = new ArrayList<CalendarEntry>();
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				CalendarEntry element = (CalendarEntry) iter.next();
				if (element.getLedgerID() == ledgerIdInt){
					entries.add(element);
				}				
			}
//System.out.println("by period and ledger");
//System.out.println("ledger "+ ledgerID);
//System.out.println("from "+ from.toString());
//System.out.println("to "+ to.toString());

			return entries;
		}
//System.out.println("by ledger");
//System.out.println("ledger "+ ledgerID);

		return calendar.getEntriesByLedgerID(ledgerIdInt);
	}	
	private Collection getEntriesByEvents(String extraURI){
		String events = extraURI.substring("events/".length());
		String eventsPeriod = null;
		List eventsList = new ArrayList();
		int index = -1;
		if (events.indexOf("+") == -1){
			eventsList.add(events.substring(0, events.indexOf("/")));
			events = events.substring(events.indexOf("/")+1, events.length());
		}
		else{
			while(true){
				index = events.indexOf("+");
				if (index == -1){
					index = events.indexOf("/");
					eventsList.add(events.substring(0, index));
					events = events.substring(index+1, events.length());
					break;
				}
				else{
					eventsList.add(events.substring(0, index));
					events = events.substring(index+1, events.length());
				}
			}
		}
		
		eventsPeriod = events;		
		Timestamp from = null;
		Timestamp to = null;
		CalBusiness calendar = new CalBusinessBean();
		
		if (eventsPeriod.length() != 0){
//			String period = extraURI.substring(ledger.indexOf("/"));
			String fromStr = eventsPeriod.substring(0, DATE_LENGTH);
			String toStr = eventsPeriod.substring(DATE_LENGTH+1, eventsPeriod.length()-1);
			from = getTimeStampFromString(fromStr);
			to = getTimeStampFromString(toStr);
			Collection coll = calendar.getEntriesBetweenTimestamps(from, to);
			List<CalendarEntry> entries = new ArrayList<CalendarEntry>();
			
			for (Iterator iter = coll.iterator(); iter.hasNext();) {
				CalendarEntry element = (CalendarEntry) iter.next();
				if(eventsList.contains(element.getEntryTypeName())){					
					entries.add(element);
				}				
			}
//System.out.println("by events and period");
//System.out.println("events: ");
//for (int i = 0; i < eventsList.size(); i++) {
//	System.out.println(eventsList.get(i));
//}
//System.out.println("from "+ from.toString());
//System.out.println("to "+ to.toString());

			return entries;
		}
//System.out.println("by events");
//for (int i = 0; i < eventsList.size(); i++) {
//	System.out.println(eventsList.get(i));
//}
//		calendar.g

		return null;
//		return calendar.getEntriesByLedgerID(ledgerIdInt);
	}		
	private Timestamp getTimeStampFromString(String dateString){
		try {
			SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
			Date date = simpleDate.parse(dateString, new ParsePosition(0));
			return new Timestamp(date.getTime());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	private String getPeriod(String uri){
//		if(uri.startsWith("/"))
//			uri = uri.substring(1);
		if(uri.length() == 0)
			return "";
		String period = uri.substring(uri.indexOf("/")+1);		
		if(period.length() == 0)
			return "";
		return period.substring(1, period.length()-1)+"_";
	}
	private String getName(String extraURI){
//		String period = extraURI.substring(extraURI.indexOf("/"));
		return extraURI.substring(0,extraURI.indexOf("/"))+"_";
	}
	
}
