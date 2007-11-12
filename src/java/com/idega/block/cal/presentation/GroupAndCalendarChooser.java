package com.idega.block.cal.presentation;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.AbstractChooser;
/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class GroupAndCalendarChooser extends AbstractChooser {

	private String chooserHelper = "groups_chooser_helper";
	private List<String> values = null;
	
	public GroupAndCalendarChooser(){
		super();
	}
	
	public GroupAndCalendarChooser(String instanceId, String method) {
		super(false);
		addForm(false);
		setInstanceId(instanceId);
		setMethod(method);
	}
	
	public void encodeBegin(FacesContext fc)throws IOException{
		super.encodeBegin(fc);
		Layer panels = (Layer)this.getFacet("PANELS");
		this.renderChild(fc,panels);
		
	}
	
	@Override
	public String getChooserHelperVarName() {
		return chooserHelper;
	}
	
	@Override
	public Class<GroupAndCalendarChooserBlock> getChooserWindowClass() {
		return GroupAndCalendarChooserBlock.class;
	}
	
	public PresentationObject getChooser(IWContext iwc, IWBundle bundle) {
		GroupAndCalendarChooserBlock chooserBlock = new GroupAndCalendarChooserBlock();
		return chooserBlock;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
}