/**
 * @(#)CalendarManagementService.java    1.0.0 10:52:15 AM
 *
 * Idega Software hf. Source Code Licence Agreement x
 *
 * This agreement, made this 10th of February 2006 by and between 
 * Idega Software hf., a business formed and operating under laws 
 * of Iceland, having its principal place of business in Reykjavik, 
 * Iceland, hereinafter after referred to as "Manufacturer" and Agura 
 * IT hereinafter referred to as "Licensee".
 * 1.  License Grant: Upon completion of this agreement, the source 
 *     code that may be made available according to the documentation for 
 *     a particular software product (Software) from Manufacturer 
 *     (Source Code) shall be provided to Licensee, provided that 
 *     (1) funds have been received for payment of the License for Software and 
 *     (2) the appropriate License has been purchased as stated in the 
 *     documentation for Software. As used in this License Agreement, 
 *     �Licensee� shall also mean the individual using or installing 
 *     the source code together with any individual or entity, including 
 *     but not limited to your employer, on whose behalf you are acting 
 *     in using or installing the Source Code. By completing this agreement, 
 *     Licensee agrees to be bound by the terms and conditions of this Source 
 *     Code License Agreement. This Source Code License Agreement shall 
 *     be an extension of the Software License Agreement for the associated 
 *     product. No additional amendment or modification shall be made 
 *     to this Agreement except in writing signed by Licensee and 
 *     Manufacturer. This Agreement is effective indefinitely and once
 *     completed, cannot be terminated. Manufacturer hereby grants to 
 *     Licensee a non-transferable, worldwide license during the term of 
 *     this Agreement to use the Source Code for the associated product 
 *     purchased. In the event the Software License Agreement to the 
 *     associated product is terminated; (1) Licensee's rights to use 
 *     the Source Code are revoked and (2) Licensee shall destroy all 
 *     copies of the Source Code including any Source Code used in 
 *     Licensee's applications.
 * 2.  License Limitations
 *     2.1 Licensee may not resell, rent, lease or distribute the 
 *         Source Code alone, it shall only be distributed as a 
 *         compiled component of an application.
 *     2.2 Licensee shall protect and keep secure all Source Code 
 *         provided by this this Source Code License Agreement. 
 *         All Source Code provided by this Agreement that is used 
 *         with an application that is distributed or accessible outside
 *         Licensee's organization (including use from the Internet), 
 *         must be protected to the extent that it cannot be easily 
 *         extracted or decompiled.
 *     2.3 The Licensee shall not resell, rent, lease or distribute 
 *         the products created from the Source Code in any way that 
 *         would compete with Idega Software.
 *     2.4 Manufacturer's copyright notices may not be removed from 
 *         the Source Code.
 *     2.5 All modifications on the source code by Licencee must 
 *         be submitted to or provided to Manufacturer.
 * 3.  Copyright: Manufacturer's source code is copyrighted and contains 
 *     proprietary information. Licensee shall not distribute or 
 *     reveal the Source Code to anyone other than the software 
 *     developers of Licensee's organization. Licensee may be held 
 *     legally responsible for any infringement of intellectual property 
 *     rights that is caused or encouraged by Licensee's failure to abide 
 *     by the terms of this Agreement. Licensee may make copies of the 
 *     Source Code provided the copyright and trademark notices are 
 *     reproduced in their entirety on the copy. Manufacturer reserves 
 *     all rights not specifically granted to Licensee.
 *
 * 4.  Warranty & Risks: Although efforts have been made to assure that the 
 *     Source Code is correct, reliable, date compliant, and technically 
 *     accurate, the Source Code is licensed to Licensee as is and without 
 *     warranties as to performance of merchantability, fitness for a 
 *     particular purpose or use, or any other warranties whether 
 *     expressed or implied. Licensee's organization and all users 
 *     of the source code assume all risks when using it. The manufacturers, 
 *     distributors and resellers of the Source Code shall not be liable 
 *     for any consequential, incidental, punitive or special damages 
 *     arising out of the use of or inability to use the source code or 
 *     the provision of or failure to provide support services, even if we 
 *     have been advised of the possibility of such damages. In any case, 
 *     the entire liability under any provision of this agreement shall be 
 *     limited to the greater of the amount actually paid by Licensee for the 
 *     Software or 5.00 USD. No returns will be provided for the associated 
 *     License that was purchased to become eligible to receive the Source 
 *     Code after Licensee receives the source code. 
 */
package com.idega.block.cal.business;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.idega.block.cal.data.CalDAVCalendar;
import com.idega.core.user.data.User;
import com.idega.user.data.Group;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Jun 29, 2012
 * @author martynasstake
 */
public interface CalendarManagementService {
	/**
	 * <p>Searches for home directory of user calendars. If fails to find, creates directory: 
	 *\/user/\{@link com.idega.user.data.User#getPrimaryKey()}.
	 * @param user
	 * @return Directory of user calendars root, where are or should calendars to be placed.
	 * <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public String getHomeCalendarPath(com.idega.user.data.User user);
	
	public List<CalDAVCalendar> getUnSubscribedCalendars(com.idega.user.data.User user, 
			Integer maxResults, Integer firstResult);

	/**
	* Finds calendars that are available for provided user.
	* @param User			User to which calendars should be available.
	* @param maxResults	maximum amount of results
	* @param firstResult	result number from which all results will be returned
	* @return List of calendars that are available for provided user. If maxResults <=  0 than
	* returns all found calendars, else maxResults of calendars. If firstResult < 0 than starts
	*  from 0 result else starts from firstResult result. Returns empty collection if no calendars
	*  found.
	*/
	public List<CalDAVCalendar> getSubscribedCalendars(com.idega.user.data.User user, 
			Integer maxResults, Integer firstResult);
	
	/**
	 * <p>Fetches all {@link CalDAVCalendar}s, which can be seen or edited by 
	 * given {@link com.idega.user.data.User}.</p>
	 * @param user - {@link com.idega.user.data.User}, who's calendars is needed to be 
	 * fetched.
	 * @param maxResults - tells how much results should be fetched, 
	 * in "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @param firstResult - tells which result in row should be fetched first. 
	 * In "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @return {@link List} of fetched {@link CalDAVCalendar}s or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getVisibleSubscriptions(
			com.idega.user.data.User user, Integer maxResults, Integer firstResult);
	
	/**
	 * <p>Fetches all {@link CalDAVCalendar}s, which can be seen or edited by 
	 * given {@link com.idega.user.data.User}.</p>
	 * @param user - {@link com.idega.user.data.User}, who's calendars is needed to be 
	 * fetched.
	 * @return {@link List} of fetched {@link CalDAVCalendar}s or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getVisibleSubscriptions(com.idega.user.data.User user);
	
	/**
	* Sets that user will get data from this calendar.
	* @param user		user that will get data from Calendar
	* @param calendarPath	path to calendar that will send data for user
	*/
	public boolean subscribeCalendar(com.idega.user.data.User user, String calendarPath);
	
	/**
	* Sets that user will get data from these calendars.
	* @param user		user that will get data from Calendars
	* @param calendarPaths	paths to calendar that will send data for user
	*/
	public boolean subscribeCalendars(com.idega.user.data.User user, Collection<String> calendarPaths);
	
	/**
	* Sets that user will not get data from these calendars.
	* @param user		user that will get data from Calendars
	* @param calendarPaths	paths to calendar that will not send data for user
	*/
	public boolean unsubscribeCalendars(com.idega.user.data.User user, Collection<String> calendarPaths);

	/**
	* Sets that user will not get data from this calendar.
	* @param user		user that will not get data from Calendar
	* @param calendarPath	path to calendar that will not send data for user
	*/
	public boolean unSubscribeCalendar(com.idega.user.data.User user, String calendarPath);
	
	/**
	 * <p>Searches Bedework system for calendars, where given {@link User} is creator.</p>
	 * @param userid {@link User#getPrimaryKey()};
	 * @return {@link Collection} of {@link BwCalendar}s or {@link Collections#EMPTY_SET} 
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<CalDAVCalendar> getCalendars(String userID);

	/**
	 * <p>Fetches all calendars, which can be seen or subscribed by given 
	 * {@link Group#getPrimaryKey()}.</p>
	 * @param groupIDs -  {@link List} of {@link Group#getPrimaryKey()}. Not 
	 * <code>null</code>.
	 * @return {@link List} of {@link CalDAVCalendar}s which can be seen or subscribed by 
	 * given groups.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getPrivateCalendars(List<String> groupIDs);
	
	/**
	 * <p>Fetches all calendars, which can be seen or subscribed by given 
	 * {@link Group#getPrimaryKey()}.</p>
	 * @param groupIDs -  {@link List} of {@link Group#getPrimaryKey()}. Not 
	 * <code>null</code>.
	 * 	@param maxResults - tells how much results should be fetched, 
	 * in "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @param firstResult - tells which result in row should be fetched first. 
	 * In "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @return {@link List} of {@link CalDAVCalendar}s which can be seen or subscribed by 
	 * given groups.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getPrivateCalendars(
			List<String> groupIDs, Integer maxResult, Integer firstResult);

	/**
	 * <p>Fetches all calendars, which can be seen or subscribed by given 
	 * {@link Group#getPrimaryKey()}.</p>
	 * @param groupIDs -  {@link List} of {@link Group#getPrimaryKey()}. Not 
	 * <code>null</code>.
	 * @param maxResults - tells how much results should be fetched, 
	 * in "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @param firstResult - tells which result in row should be fetched first. 
	 * In "SQL" query would be: "LIMIT maxResults, firstResult".
	 * @return {@link List} of {@link CalDAVCalendar}s which can be seen or subscribed by 
	 * given groups.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getPrivateCalendars(Set<Long> groupIDs,
			Integer maxResult, Integer firstResult);

	/**
	 * <p>Fetches all private {@link CalDAVCalendar}s from database.</p>
	 * @return {@link CalDAVCalendar}s or <code>null</code> of failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getPrivateCalendars();

	/**
	 * <p>Fetches all public {@link CalDAVCalendar}s from database.</p>
	 * @return {@link CalDAVCalendar}s or <code>null</code> of failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<CalDAVCalendar> getPublicCalendars();
}
