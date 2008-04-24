/*
 * Licensed to the Apache Software Foundation (ASF) under one
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
 * Renderer for the week view of the UISchedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: laddi $)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision: 1.6 $
 */
public class ScheduleCompactWeekRenderer
    extends AbstractCompactScheduleRenderer
    implements Serializable
{
    private static final long serialVersionUID = 5504081783797695487L;

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
        writer.writeAttribute(
            HTML.STYLE_ATTR, "border-style: none; overflow: hidden;", null
        );

        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule, "week"), null);
        writer.writeAttribute(
            HTML.STYLE_ATTR, "position: relative; left: 0px; top: 0px; width: 100%;",
            null
        );
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute(HTML.WIDTH_ATTR, "100%", null);
        Calendar cal = Calendar.getInstance();

        for (
            Iterator dayIterator = schedule.getModel().iterator();
                dayIterator.hasNext();
        ) {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            cal.setTime(day.getDate());

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            boolean isWeekend =
                (dayOfWeek == Calendar.SATURDAY) ||
                (dayOfWeek == Calendar.SUNDAY);

            if (
                (dayOfWeek == Calendar.MONDAY) ||
                    (dayOfWeek == Calendar.WEDNESDAY) ||
                    (dayOfWeek == Calendar.FRIDAY)
            ) {
                writer.startElement(HTML.DIV_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, "possibleweek", null);
            }

            writeDayCell(
                context, writer, schedule, day, 50f, dayOfWeek, dayOfMonth,
                isWeekend, true, (dayOfWeek == Calendar.FRIDAY) ? 2 : 1
            );

            if (
                (dayOfWeek == Calendar.TUESDAY) ||
                    (dayOfWeek == Calendar.THURSDAY) ||
                    (dayOfWeek == Calendar.SUNDAY)
            ) {
                writer.endElement(HTML.DIV_ELEM);
            }
        }

        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     * @see org.apache.myfaces.custom.schedule.renderer.AbstractCompactScheduleRenderer#getDefaultRowHeight()
     */
    @Override
		protected int getDefaultRowHeight()
    {
        return 200;
    }

    /**
     * @see org.apache.myfaces.custom.schedule.renderer.AbstractCompactScheduleRenderer#getRowHeightProperty()
     */
    @Override
		protected String getRowHeightProperty()
    {
        return "compactWeekRowHeight";
    }
}
//The End
