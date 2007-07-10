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

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.custom.schedule.util.ScheduleUtil;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 *
 * @author Bruno Aranda (latest modification by $Author: justinas $)
 * @author Jurgen Lust
 * @version $Revision: 1.1 $
 */
public class HtmlSchedule extends UISchedule implements UserRoleAware,
        Serializable
{

    public static final String COMPONENT_FAMILY = "javax.faces.Panel";

    public static final String COMPONENT_TYPE = "com.idega.block.cal.Schedule";
    private static final String DEFAULT_RENDERER_TYPE = "com.idega.block.cal.Schedule";
    private static final long serialVersionUID = 5859593107442371656L;
    //  ------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    private String _enabledOnUserRole = null;
    private Date _lastClickedDateAndTime = null;
    private MethodBinding _mouseListener = null;
    private Boolean _submitOnClick = null;
    private String _visibleOnUserRole = null;

    public HtmlSchedule()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * @see javax.faces.component.UIComponent#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        // First invoke the mouse listener, before any other listener
        if (event instanceof ScheduleMouseEvent)
        {
            FacesContext context = getFacesContext();
            ScheduleMouseEvent mouseEvent = (ScheduleMouseEvent) event;
            MethodBinding mouseListenerBinding = getMouseListener();

            if (mouseListenerBinding != null)
            {
                mouseListenerBinding.invoke(context,
                        new Object[] { mouseEvent });
            }
        }
        //now invoke the other listeners
        super.broadcast(event);
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#getEnabledOnUserRole()
     */
    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null)
            return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(),
                vb) : null;
    }

    /**
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    /**
     * <p>
     * The last date and time of day that was clicked. This is set when
     * submitOnClick is true, and the schedule is clicked by the user.
     * </p>
     * 
     * @return the last clicked date and time
     */
    public Date getLastClickedDateAndTime()
    {
        return _lastClickedDateAndTime;
    }

    /**
     * @return the method binding to the mouse listener method
     */
    public MethodBinding getMouseListener()
    {
        return _mouseListener;
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#getVisibleOnUserRole()
     */
    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null)
            return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(),
                vb) : null;
    }

    /**
     * <p>
     * Should the parent form of this schedule be submitted when the user
     * clicks on a day? Note that this will only work when the readonly
     * property is set to false.
     * </p>
     *
     * @return submit the form on mouse click
     */
    public boolean isSubmitOnClick()
    {
        return ScheduleUtil.getBooleanProperty(this, _submitOnClick,
                "submitOnClick", false);
    }

    /**
     * @see javax.faces.component.UIComponent#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event)
    {
        if (event instanceof ScheduleMouseEvent)
        {
            if (isImmediate())
            {
                event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            }
            else
            {
                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
        }
        super.queueEvent(event);
    }

    /**
     * This method is invoked at the beginning of the restore view phase,
     * resetting all mouse event variables that were left from the previous
     * request
     */
    public void resetMouseEvents()
    {
        this._lastClickedDateAndTime = null;
    }

    /**
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _enabledOnUserRole = (String) values[1];
        _visibleOnUserRole = (String) values[2];
        _submitOnClick = (Boolean) values[3];
        _lastClickedDateAndTime = (Date) values[4];
        _mouseListener = (MethodBinding) restoreAttachedState(context,
                values[5]);
    }

    //  ------------------ GENERATED CODE END ---------------------------------------

    /**
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _enabledOnUserRole;
        values[2] = _visibleOnUserRole;
        values[3] = _submitOnClick;
        values[4] = _lastClickedDateAndTime;
        values[5] = saveAttachedState(context, _mouseListener);
        return ((Object) (values));
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#setEnabledOnUserRole(java.lang.String)
     */
    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * <p>
     * The last date and time of day that was clicked. This is set when
     * submitOnClick is true, and the schedule is clicked by the user.
     * </p>
     * 
     * @return the last clicked date and time
     */
    public void setLastClickedDateAndTime(Date lastClickedDateAndTime)
    {
        this._lastClickedDateAndTime = lastClickedDateAndTime;
    }

    /**
     * @param listener the method binding to the mouse listener method
     */
    public void setMouseListener(MethodBinding listener)
    {
        _mouseListener = listener;
    }

    /**
     * <p>
     * Should the parent form of this schedule be submitted when the user
     * clicks on a day? Note that this will only work when the readonly
     * property is set to false.
     * </p>
     * 
     *
     * @param submitOnClick submit the form on mouse click
     */
    public void setSubmitOnClick(boolean submitOnClick)
    {
        this._submitOnClick = Boolean.valueOf(submitOnClick);
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

}
//The End
