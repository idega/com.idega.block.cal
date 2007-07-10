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
package com.idega.block.cal.business;

import java.io.Serializable;
import java.util.Date;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * An event that is fired when the schedule is clicked, and the submitOnClick
 * property on the schedule is set to true.
 * 
 * @author Jurgen Lust (latest modification by $Author: justinas $)
 * @version $Revision: 1.1 $
 */
public class ScheduleMouseEvent extends FacesEvent implements Serializable
{
    public static final int SCHEDULE_NOTHING_CLICKED = 0;
    public static final int SCHEDULE_BODY_CLICKED = 1;
    public static final int SCHEDULE_HEADER_CLICKED = 2;
    public static final int SCHEDULE_ENTRY_CLICKED = 3;

    private static final long serialVersionUID = -2810582008938303475L;

    private final int eventType;

    public ScheduleMouseEvent(final HtmlSchedule source, final int eventType)
    {
        super(source);
        //the FacesEvent throws an IllegalArgumentException when source == null
        //so don't bother here
        this.eventType = eventType;
    }

    public Date getClickedDate()
    {
        return getSchedule().getLastClickedDateAndTime();
    }

    public Date getClickedTime()
    {
        return getSchedule().getLastClickedDateAndTime();
    }

    public int getEventType()
    {
        return eventType;
    }

    /**
     * @return The schedule component where the mouse event originated
     */
    public HtmlSchedule getSchedule()
    {
        return (HtmlSchedule) getSource();
    }

    /**
     * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
     */
    public boolean isAppropriateListener(FacesListener listener)
    {
        return (listener instanceof ScheduleMouseListener);
    }

    /**
     * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
     */
    public void processListener(FacesListener listener)
    {
        if (listener instanceof ScheduleMouseListener)
        {
            ScheduleMouseListener mouseListener = (ScheduleMouseListener) listener;
            mouseListener.processMouseEvent(this);
        }
    }

}
