/*
 * Licensed to the �pache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.idega.block.cal.renderer;


import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.schedule.model.ScheduleDay;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

import com.idega.block.cal.business.HtmlSchedule;


/**
 * <p>
 * Renderer for the month view of the Schedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: laddi $)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision: 1.7 $
 */
public class ScheduleCompactMonthRenderer
    extends AbstractCompactScheduleRenderer
    implements Serializable
{

    private static final long serialVersionUID = 2926607881214603314L;
    
    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    @Override
		public void encodeBegin(
        FacesContext context,
        UIComponent component
    )
        throws IOException
    {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();

        //container div for the schedule grid
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, "schedule-compact-" + getTheme(schedule), null);

        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule, "month"), null);

        Calendar cal = Calendar.getInstance();
        cal.setTime(schedule.getModel().getSelectedDate());
        int selectedMonth = cal.get(Calendar.MONTH);

        for (
            Iterator dayIterator = schedule.getModel().iterator();
            dayIterator.hasNext();
        ) {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            cal.setTime(day.getDate());

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int currentMonth = cal.get(Calendar.MONTH);
            boolean isWeekend =
                (dayOfWeek == Calendar.SATURDAY) ||
                (dayOfWeek == Calendar.SUNDAY);

            cal.setTime(day.getDate());

            writeDayCell(
                context, writer, schedule, day, dayOfWeek, dayOfMonth, isWeekend,
                currentMonth == selectedMonth, isWeekend ? 1 : 2
            );

        }

        writer.endElement(HTML.DIV_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     * @see AbstractCompactScheduleRenderer#getDefaultRowHeight()
     */
    @Override
		protected int getDefaultRowHeight()
    {
        return 120;
    }

    /**
     * @see AbstractCompactScheduleRenderer#getRowHeightProperty()
     */
    @Override
		protected String getRowHeightProperty()
    {
        return "compactMonthRowHeight";
    }

    /**
     */
    protected void writeDayCell(
        FacesContext context,
        ResponseWriter writer,
        HtmlSchedule schedule,
        ScheduleDay day,
        int dayOfWeek,
        int dayOfMonth,
        boolean isWeekend,
        boolean isCurrentMonth,
        int rowspan
    )
        throws IOException
    {
        if ((dayOfWeek == Calendar.MONDAY)) {
        	writer.startElement(HTML.DIV_ELEM, schedule);
            writer.writeAttribute(HTML.CLASS_ATTR, "monthModeRow", null);
        }

        super.writeDayCell(
            context, writer, schedule, day, 100f / 6, dayOfWeek, dayOfMonth,
            isWeekend, isCurrentMonth, rowspan
        );

        if ((dayOfWeek == Calendar.SUNDAY)) {
        	writer.endElement(HTML.DIV_ELEM);
        }
    }
}
//The End
