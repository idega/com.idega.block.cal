package com.idega.block.cal.presentation;

import java.io.IOException;

import javax.faces.context.FacesContext;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.user.presentation.group.GroupsChooserBlock;
/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class GroupAndCalendarChooser extends Block {
	
//	private UserBusiness userBiz = null;
	private static IWBundle bundle = null;	
	
	public void main(IWContext iwc) {
		add(new GroupsChooserBlock());
	}
	
//	public void main(IWContext iwc) {
//		Page parentPage = PresentationObjectUtil.getParentPage(this);
//		if (parentPage == null){
//			return;
//		}
//		//TODO: get real resource path
//		IWBundle bundle = getBundle();
//		String path = bundle.getResourcesPath();
//		parentPage.addJavascriptURL(path + "/javascript/groupTree.js");
//		parentPage.addJavascriptURL(path + "/javascript/cal.js");
//		parentPage.addJavascriptURL("/dwr/interface/CalService.js");
//		parentPage.addJavascriptURL("/dwr/engine.js");
////		parentPage.addJavaScriptAfterJavaScriptURLs("tree", "addEvent(window, 'load', loadTree)");
//		parentPage.addJavaScriptAfterJavaScriptURLs("tree", "registerEvent(window, 'load', loadTree)");
//		Layer layer = new Layer();
//		layer.setId(CalendarConstants.groupsDivId);
//		
//		RadioGroup radioGroup = new RadioGroup("server");
//			
//		Text txtLocal = new Text(bundle.getLocalizedString(CalendarConstants.localServerParameterName,"Local server"));
//		RadioButton btnLocal = new RadioButton("server");
//		
//		btnLocal.setId("radioBtnLocal");
//		
//		btnLocal.setOnClick("setLocal('"+CalendarConstants.tableID+"');");
//
//		Text txtRemote = new Text(bundle.getLocalizedString(CalendarConstants.remoteServerParameterName,"Remote server"));
//		RadioButton btnRemote = new RadioButton("server");
//		btnRemote.setOnClick("setRemote('"+CalendarConstants.tableID+"');");
//		
//		btnLocal.setId("radioBtnRemote");
//		
//		radioGroup.addRadioButton(btnLocal, txtLocal);
//		radioGroup.addRadioButton(btnRemote, txtRemote);	
//			
//		Text txtServerName = new Text(bundle.getLocalizedString(CalendarConstants.serverNameParameterName, "Server name"));
//		Text txtLogin = new Text(bundle.getLocalizedString(CalendarConstants.loginParameterName, "Login"));
//		Text txtPass = new Text(bundle.getLocalizedString(CalendarConstants.passwordParameterName, "Password"));
//			
//		TextInput inpServerName = new TextInput();
//		inpServerName.setId(CalendarConstants.serverNameFieldId);
//		TextInput inpLogin = new TextInput();
//		inpLogin.setId(CalendarConstants.loginFieldId);
//		PasswordInput pswInput = new PasswordInput();
//		pswInput.setId(CalendarConstants.passwordFieldid);
//			
//		GenericButton button = new GenericButton(bundle.getLocalizedString(CalendarConstants.refreshParameterName, "Refresh"));
//		button.setOnClick("sendConnectionData('"+CalendarConstants.serverNameFieldId+"','"+CalendarConstants.loginFieldId+"','"+CalendarConstants.passwordFieldid+"')");
//		button.setId("connDataBtn");
////		button.attributes.put("onClick", "sendConnectionData('"+CalendarConstants.serverNameFieldId+"','"+CalendarConstants.loginFieldId+"','"+CalendarConstants.passwordFieldid+"')");
//			
////		Table table = new Table(2, 4);
////		table.setId(CalendarConstants.tableID);
////		table.attributes.put("class", "hidden_table");
////		table.add(txtServerName, 1, 1);
////		table.add(txtLogin, 1, 2);
////		table.add(txtPass, 1, 3);
////		table.add(inpServerName, 2, 1);
////		table.add(inpLogin, 2, 2);
////		table.add(pswInput, 2, 3);
////		table.add(button, 1, 4);
//		
//		Layer connData = new Layer();
//		connData.setId("connectionData");
////		connData.setStyleClass("hidden_table");
//		Layer serverNameLayer = new Layer();
//		serverNameLayer.add(txtServerName);
//		serverNameLayer.add(inpServerName);
//		serverNameLayer.setStyleClass("connection_table_row");
//		
//		Layer loginLayer = new Layer();
//		loginLayer.add(txtLogin);
//		loginLayer.add(inpLogin);
//		loginLayer.setStyleClass("connection_table_row");
//		
//		Layer passLayer = new Layer();	
//		passLayer.add(txtPass);
//		passLayer.add(pswInput);		
//		passLayer.setStyleClass("connection_table_row");
//		
//		Layer buttonLayer = new Layer();
//		buttonLayer.setStyleClass("connection_table_button");
//		buttonLayer.add(button);
//		
//		
//		connData.add(serverNameLayer);
//		connData.add(loginLayer);
//		connData.add(passLayer);
//		connData.add(buttonLayer);
//		
//		connData.setStyleClass("hidden_table");
//		
////		layer.add(connData);
//		layer.add(radioGroup);
////		layer.add(table);
//		layer.add(connData);
//		
//		Script script = new Script();
//		script.addScriptLine("setDivId(\'"+CalendarConstants.groupsDivId+"\');");
//		layer.add(script);
////		this.getChildren().add(script);		
//		
//		parentPage.add(layer);
//	}
	
	public void encodeBegin(FacesContext fc)throws IOException{
		super.encodeBegin(fc);
		
		Layer panels = (Layer)this.getFacet("PANELS");
		this.renderChild(fc,panels);
		
	}
	
//	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
//		if (this.userBiz == null) {
//			try {
//				this.userBiz = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
//			}
//			catch (java.rmi.RemoteException rme) {
//				throw new RuntimeException(rme.getMessage());
//			}
//		}
//		return this.userBiz;
//	}	
	
//	private Collection getGroupTypes(String selectedGroup, IWContext iwc)  {
//		Collection groupTypes = new ArrayList();
//		Group group = null;
//		// get group types
//		GroupBusiness groupBusiness;
//		try {
//			groupBusiness =(GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
//			if (! CreateGroupWindow.NO_GROUP_SELECTED.equals(selectedGroup))  {
//				group = groupBusiness.getGroupByGroupID((new Integer(selectedGroup)).intValue());
//			}
//		}
//		// Remote and FinderException
//		catch (Exception ex)  {
//			throw new RuntimeException(ex.getMessage());
//		}
//		Iterator iterator = null;
//		try {
//			iterator = groupBusiness.getAllAllowedGroupTypesForChildren(group, iwc).iterator();
//		}
//		catch (RemoteException e) {
//			e.printStackTrace();
//		}
//		
//		while (iterator!=null && iterator.hasNext())  {
//			GroupType item = (GroupType) iterator.next();
//			String value = item.getType();
//			groupTypes.add(value);
//		}
//		return groupTypes;
//	}
	
//	private boolean checkGroupType(Group group, Collection allowedGroupTypes) {
//		String groupType = group.getGroupTypeValue();
//		Iterator iterator = allowedGroupTypes.iterator();
//		while (iterator.hasNext())  {
//			String type = (String) iterator.next();
//			if (type.equals(groupType)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	public Collection convertGroupCollectionToGroupNodeCollection(Collection col, IWApplicationContext iwac){
//		List list = new Vector();
//		
//		Iterator iter = col.iterator();
//		while (iter.hasNext()) {
//			Group group = (Group) iter.next();
//			GroupTreeNode node = new GroupTreeNode(group,iwac);
//			list.add(node);
//		}
//
//		return list;
//	}	
	
//	public WFTreeNode getRootNode(Collection groupNodes, WFTreeNode rootNode){
////		WFTreeNode node = null;
//		for (Iterator iter = groupNodes.iterator(); iter.hasNext();) {
//			GroupTreeNode element = (GroupTreeNode) iter.next();
//			WFTreeNode newNode = new WFTreeNode(new IWTreeNode(element.getNodeName()));				
//			if(element.getChildCount() != 0)
//				newNode = getRootNode(element.getChildren(), newNode);
//			newNode.setIdentifier(element.getId());
//			rootNode.addChild(newNode);			
//		}
//		
//		return rootNode;
//	}
	
	public static IWBundle getBundle() {
		if (bundle == null) {
			setupBundle();
		}
		return bundle;
	}

	private static void setupBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		IWContext iwContext = IWContext.getIWContext(context);
		bundle = iwContext.getIWMainApplication().getBundle(CalendarConstants.BUNDLE_IDENTIFIER);
	}
}