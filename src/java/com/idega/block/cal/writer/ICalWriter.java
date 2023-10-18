/**
 * @(#)ICalWriter.java    1.0.0 14:53:43
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
 *     Licensee shall also mean the individual using or installing
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
package com.idega.block.cal.writer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryHome;
import com.idega.block.cal.util.ICalendarUtil;
import com.idega.core.business.DefaultSpringBean;
import com.idega.core.dao.ICFileDAO;
import com.idega.core.file.data.bean.ICFile;
import com.idega.core.file.data.bean.dao.ICMimeTypeDAO;
import com.idega.core.file.util.MimeType;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.bean.User;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.IOUtil;
import com.idega.util.expression.ELUtil;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;

/**
 * <p>Class for writing calendar event to iCalendar file</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 2015 gruod. 28
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ICalWriter extends DefaultSpringBean {

	private CalendarEntryHome calendarEntryHome;

	@Autowired
	private ICFileDAO fileDAO;

	@Autowired
	private ICMimeTypeDAO fileTypeDAO;

	private ICMimeTypeDAO getFileTypeDAO() {
		if (this.fileTypeDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.fileTypeDAO;
	}

	public ICFileDAO getFileDAO() {
		if (this.fileDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.fileDAO;
	}

	private CalendarEntryHome getCalendarEntryHome() {
		if (this.calendarEntryHome == null) {
			try {
				this.calendarEntryHome = (CalendarEntryHome) IDOLookup.getHome(CalendarEntry.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, "Failed to get " + CalendarEntryHome.class + " cause of: ", e);
			}
		}

		return this.calendarEntryHome;
	}

	/**
	 *
	 * <p>Writes all events to iCalendar format to given {@link OutputStream}</p>
	 * @param calendarEntries to write, not <code>null</code>;
	 * @param stream as the destination, not <code>null</code>;
	 */
	public void write(Collection<CalendarEntry> calendarEntries, OutputStream stream) {
		Calendar calendar = ICalendarUtil.getCalendar(calendarEntries);
		if (calendar != null && stream != null) {
			try {
				new CalendarOutputter().output(calendar, stream);
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Failed to write data to output stream, cause of: ", e);
			}
		}
	}

	/**
	 *
	 * <p>Writes all {@link User} events to iCalendar format
	 * to given {@link OutputStream}</p>
	 * @param user to get events for, not <code>null</code>;
	 * @param stream as the destination, not <code>null</code>;
	 */
	public void write(User user, OutputStream stream) {
		write(getCalendarEntryHome().findAll(user, null, null, null), stream);
	}

	/**
	 *
	 * @param calendarEntries to convert and write to file;
	 * @param filename is name of file to write, not <code>null</code>;
	 * @return {@link URL} to created file;
	 */
	public String write(Collection<CalendarEntry> calendarEntries, String filename) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(calendarEntries, out);

		String path = CoreConstants.WEBDAV_SERVLET_URI.concat(CoreConstants.PUBLIC_PATH).concat("/calendar/").concat(UUID.randomUUID().toString()).concat(CoreConstants.SLASH);
		ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(out.toByteArray());
			if (getRepositoryService().uploadFile(path, filename, MimeType.pdf.toString(), in)) {
				IWContext iwc = CoreUtil.getIWContext();
				String serverURL = iwc == null ? null : CoreUtil.getServerURL(iwc.getRequest());
				serverURL = serverURL == null ? getApplication().getIWApplicationContext().getDomain().getURL() : serverURL;
				if (serverURL.endsWith(CoreConstants.SLASH)) {
					path = path.substring(1);
				}

				return serverURL.concat(path).concat(filename);
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Failed to save calendar file, cause of: ", e);
		} finally {
			IOUtil.close(in);
			IOUtil.close(out);
		}

		return null;
	}

	/**
	 *
	 * @param calendarEntries to write, not <code>null</code>
	 * @param filename is name of the file to write, not <code>null</code>
	 * @return entity or <code>null</code> on failure;
	 * @throws SQLException when writing to database failed
	 * @deprecated writing files to database is considered to be an anti-pattern. This method is workaround and should
	 * be avoided when possible
	 */
	@Deprecated
	public ICFile writeToDatabase(Collection<CalendarEntry> calendarEntries, String filename) throws SQLException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(calendarEntries, out);

		byte[] bytes = out.toByteArray();
		ICFile file = getFileDAO().update(null, filename, null, bytes, new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()), Long.valueOf(bytes.length).intValue(),
				getFileTypeDAO().update("text/calendar", "Internet Calendaring and Scheduling Core Object Specification (iCalendar)"), true);

		IOUtil.close(out);

		return file;
	}
}