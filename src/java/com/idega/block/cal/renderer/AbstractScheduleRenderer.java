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

import com.idega.block.cal.business.HtmlSchedule;

import com.idega.block.cal.business.ScheduleMouseEvent;
import org.apache.myfaces.custom.schedule.model.ScheduleEntry;
import org.apache.myfaces.custom.schedule.util.ScheduleEntryComparator;
import org.apache.myfaces.custom.schedule.util.ScheduleUtil;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * Abstract superclass for all renderer of the UISchedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: justinas $)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision: 1.2 $
 */
public abstract class AbstractScheduleRenderer extends Renderer implements
        Serializable
{
    //~ Static fields/initializers ---------------------------------------------
    protected static final ScheduleEntryComparator comparator = new ScheduleEntryComparator();
    protected static final String LAST_CLICKED_DATE = "_last_clicked_date";
    protected static final String LAST_CLICKED_Y = "_last_clicked_y";
    private static final String CSS_RESOURCE = "css/schedule.css";
    public static final String DEFAULT_THEME = "default";
    public static final String OUTLOOK_THEME = "outlookxp";
    public static final String EVOLUTION_THEME = "evolution";

    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void decode(FacesContext context, UIComponent component)
    {
        HtmlSchedule schedule = (HtmlSchedule) component;
        boolean queueAction = false;
        if (ScheduleUtil.canModifyValue(component))
        {
            Map parameters = context.getExternalContext()
                    .getRequestParameterMap();
            String selectedEntryId = (String) parameters.get((String) schedule
                    .getClientId(context));
            String lastClickedDateId = (String) parameters
                    .get((String) schedule.getClientId(context)
                            + LAST_CLICKED_DATE);
            String lastClickedY = (String) parameters.get((String) schedule
                    .getClientId(context)
                    + LAST_CLICKED_Y);

            ScheduleMouseEvent mouseEvent = null;

            if ((selectedEntryId != null) && (selectedEntryId.length() > 0))
            {
                ScheduleEntry entry = schedule.findEntry(selectedEntryId);
                schedule.setSubmittedEntry(entry);
                mouseEvent = new ScheduleMouseEvent(schedule,
                        ScheduleMouseEvent.SCHEDULE_ENTRY_CLICKED);
                queueAction = true;
            }

            if (schedule.isSubmitOnClick())
            {
                schedule.resetMouseEvents();
                if ((lastClickedY != null) && (lastClickedY.length() > 0))
                {
                    //the body of the schedule was clicked
                    schedule
                            .setLastClickedDateAndTime(determineLastClickedDate(
                                    schedule, lastClickedDateId, lastClickedY));
                    mouseEvent = new ScheduleMouseEvent(schedule,
                            ScheduleMouseEvent.SCHEDULE_BODY_CLICKED);
                    queueAction = true;
                }
                else if ((lastClickedDateId != null)
                        && (lastClickedDateId.length() > 0))
                {
                    //the header of the schedule was clicked
                    schedule
                            .setLastClickedDateAndTime(determineLastClickedDate(
                                    schedule, lastClickedDateId, "0"));
                    mouseEvent = new ScheduleMouseEvent(schedule,
                            ScheduleMouseEvent.SCHEDULE_HEADER_CLICKED);
                    queueAction = true;
                }
                else if (mouseEvent == null)
                {
                    //the form was posted without mouse events on the schedule
                    mouseEvent = new ScheduleMouseEvent(schedule,
                            ScheduleMouseEvent.SCHEDULE_NOTHING_CLICKED);
                }
            }

            if (mouseEvent != null)
                schedule.queueEvent(mouseEvent);
        }
        if (queueAction)
        {
            schedule.queueEvent(new ActionEvent(schedule));
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();

        //add needed CSS and Javascript files to the header 

//        AddResource addResource = AddResourceFactory.getInstance(context);
//        String theme = getTheme(schedule);
        //The default css file is only loaded if the theme is one of the provided
        //themes.
        
//        if (DEFAULT_THEME.equals(theme) || OUTLOOK_THEME.equals(theme)
//                || EVOLUTION_THEME.equals(theme))
//        {
//            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
//                    HtmlSchedule.class, CSS_RESOURCE);
//        }
//        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
//                HtmlSchedule.class, "javascript/schedule.js");
//        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
//                HtmlSchedule.class, "javascript/alphaAPI.js");
//        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
//                HtmlSchedule.class, "javascript/domLib.js");
//        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
//                HtmlSchedule.class, "javascript/domTT.js");
//        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
//                HtmlSchedule.class, "javascript/fadomatic.js");

        //hidden input field containing the id of the selected entry
        writer.startElement(HTML.INPUT_ELEM, schedule);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, schedule.getClientId(context),
                "clientId");
        writer.endElement(HTML.INPUT_ELEM);
        //hidden input field containing the id of the last clicked date
        writer.startElement(HTML.INPUT_ELEM, schedule);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, schedule.getClientId(context)
                + "_last_clicked_date", "clicked_date");
        writer.endElement(HTML.INPUT_ELEM);
        //hidden input field containing the y coordinate of the mouse when
        //the schedule was clicked. This will be used to determine the hour
        //of day.
        writer.startElement(HTML.INPUT_ELEM, schedule);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, schedule.getClientId(context)
                + "_last_clicked_y", "clicked_y");
        writer.endElement(HTML.INPUT_ELEM);
    }

    /**
     * <p>
     * Get the String representation of a date, taking into account the
     * specified date format or the current Locale.
     * </p>
     *
     * @param context the FacesContext
     * @param component the component
     * @param date the date
     *
     * @return the date string
     */
    protected String getDateString(FacesContext context, UIComponent component,
            Date date)
    {
        DateFormat format;
        String pattern = getHeaderDateFormat(component);

        if ((pattern != null) && (pattern.length() > 0))
        {
            format = new SimpleDateFormat(pattern);
        }
        else
        {
            if (context.getApplication().getDefaultLocale() != null)
            {
                format = DateFormat.getDateInstance(DateFormat.MEDIUM, context
                        .getApplication().getDefaultLocale());
            }
            else
            {
                format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            }
        }

        return format.format(date);
    }

    /**
     * <p>
     * The theme used when rendering the schedule
     * </p>
     * 
     * @param component the component
     * 
     * @return the theme
     */
    protected String getTheme(UIComponent component)
    {
        //first check if the theme property is a value binding expression
        ValueBinding binding = component.getValueBinding("theme");
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        String theme = (String) attributes.get("theme");
        if (theme == null || theme.length() < 1)
//            theme = "evolution";
            theme = "default";
        return theme;
    }

    /**
     * <p>
     * Allow the developer to specify custom CSS classnames for the schedule
     * component.
     * </p>
     * @param component the component
     * @param className the default CSS classname
     * @return the custom classname
     */
    protected String getStyleClass(UIComponent component, String className)
    {
        //first check if the styleClass property is a value binding expression
        ValueBinding binding = component.getValueBinding(className);
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        String returnValue = (String) attributes.get(className);
        return returnValue == null ? className : returnValue;
    }

    /**
     * <p>
     * The date format that is used in the schedule header
     * </p>
     *
     * @param component the component
     *
     * @return Returns the headerDateFormat.
     */
    protected String getHeaderDateFormat(UIComponent component)
    {
        //first check if the headerDateFormat property is a value binding expression
        ValueBinding binding = component.getValueBinding("headerDateFormat");
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return (String) attributes.get("headerDateFormat");
    }


    /**
     * <p>
     * Should the tooltip be made visible?
     * </p>
     *
     * @param component the component
     *
     * @return whether or not tooltips should be rendered
     */
    protected boolean showTooltip(UIComponent component)
    {
        //first check if the tooltip property is a value binding expression
        ValueBinding binding = component.getValueBinding("tooltip");
        if (binding != null)
        {
            Boolean value = (Boolean) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value.booleanValue();
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return Boolean.valueOf((String) attributes.get("tooltip"))
                .booleanValue();
    }

    /**
     * The user of the Schedule component can customize the look and feel
     * by specifying a custom implementation of the ScheduleEntryRenderer.
     * This method gets an instance of the specified class, or if none
     * was specified, takes the default.
     * 
     * @param component the Schedule component
     * @return a ScheduleEntryRenderer instance
     */
    protected ScheduleEntryRenderer getEntryRenderer(UIComponent component)
    {
        Object entryRendererObject = ScheduleUtil.getObjectProperty(component,
                null, "entryRenderer", null);
        if (entryRendererObject instanceof ScheduleEntryRenderer)
        {
            return (ScheduleEntryRenderer) entryRendererObject;
        }
        else
        {
            return new DefaultScheduleEntryRenderer();
        }
    }

    /**
     * @return The default height, in pixels, of one row in the schedule grid
     */
    protected abstract int getDefaultRowHeight();

    /**
     * @return The name of the property that determines the row height
     */
    protected abstract String getRowHeightProperty();

    /**
     * @param attributes
     *            The attributes
     * 
     * @return The row height, in pixels
     */
    protected int getRowHeight(Map attributes)
    {
        int rowHeight = 0;

        try
        {
            Integer rowHeightObject = (Integer) attributes
                    .get(getRowHeightProperty());
            rowHeight = rowHeightObject.intValue();
        }
        catch (Exception e)
        {
            rowHeight = 0;
        }

        if (rowHeight <= 0)
        {
            rowHeight = getDefaultRowHeight();
        }

        return rowHeight;
    }

    /**
     * Determine the last clicked date
     * @param schedule the schedule component
     * @param dateId the string identifying the date
     * @param yPos the y coordinate of the mouse
     * @return the clicked date
     */
    protected abstract Date determineLastClickedDate(HtmlSchedule schedule,
            String dateId, String yPos);
}
//The End
