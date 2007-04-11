package com.idega.block.cal.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

//import org.springframework.web.servlet.tags.form.RadioButtonTag;

import com.idega.business.IBOLookup;
import com.idega.core.data.IWTreeNode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObjectUtil;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.AbstractChooser;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.GroupTreeNode;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupType;
import com.idega.user.presentation.CreateGroupWindow;
import com.idega.user.presentation.GroupChooserWindow;
import com.idega.user.presentation.GroupTreeView;
import com.idega.webface.IWTree;
import com.idega.webface.WFTreeNode;
import com.idega.webface.WFUtil;
/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class GroupAndCalendarChooser extends Block {
	
	private UserBusiness userBiz = null;
	
	private String tableID = "connectionData";
	private String serverNameFieldId = "serverNameFieldId";
	private String loginFieldId = "loginFieldId";
	private String passwordFieldid = "passwordFieldid";
	private String buttonLabel = "Get groups";
	
	public void main(IWContext iwc) {
		Page parentPage = PresentationObjectUtil.getParentPage(this);
//		IWTree tree = new IWTree();
//		tree.setRendererType("com.idega.webface.IWTree");
		WFTreeNode rootNode = new WFTreeNode(new IWTreeNode("panelName"));
		if (parentPage == null){
			return;
		}
		//TODO: get real resource path
		parentPage.addJavascriptURL("/idegaweb/bundles/com.idega.block.cal/resources/javascript/groupTree.js");
		parentPage.addJavaScriptAfterJavaScriptURLs("tree", "addEvent(window, 'load', initCalendarTree)");
			try {
				GroupTreeView viewer = new GroupTreeView();
//				IWContext iwc = new IWContext();
//				if(iwc.isSuperAdmin()){
				if(false){
					GroupTreeNode node = new GroupTreeNode(iwc.getDomain(),iwc.getApplicationContext());
					viewer.setRootNode(node);
				}
				else{
					UserBusiness biz = getUserBusiness(iwc);
					Collection allGroups = null;
					try {
						allGroups = biz.getUsersTopGroupNodesByViewAndOwnerPermissions(iwc.getCurrentUser(), iwc);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// filter groups
					Collection allowedGroupTypes = null;
					if (iwc.isParameterSet(AbstractChooser.FILTER_PARAMETER))  {
						String filter = iwc.getParameter(AbstractChooser.FILTER_PARAMETER);
						if (filter.length() > 0)  {
							allowedGroupTypes = getGroupTypes(filter, iwc);
						}
					}
					
					Collection groups = new ArrayList();
					if (allowedGroupTypes == null)  {
						groups = allGroups;
					}
					else {
						Iterator iterator = allGroups.iterator();
						while (iterator.hasNext())  {
							Group group = (Group) iterator.next();
							if (checkGroupType(group, allowedGroupTypes))  {
								groups.add(group);
							}
						}
					}		
//				return convertGroupCollectionToGroupNodeCollection(groups,iwc.getApplicationContext());
					Collection groupNodes = convertGroupCollectionToGroupNodeCollection(groups,iwc.getApplicationContext());
					viewer.setFirstLevelNodes(groupNodes.iterator());	
					rootNode = getRootNode(groupNodes, rootNode);
				}
//
//				IWTree tree = new IWTree();
//				tree.setValue(rootNode);
//			    tree.setShowRootNode(false);	
//			    tree.setId("tree");
//			    tree.setShowLines(false);
//			    tree.setVar("node");
//			    tree.setRendererType("com.idega.webface.IWTree");
//			    HtmlOutputLink linki = new HtmlOutputLink();
//			    linki.setValue("#");
//			    linki.getAttributes().put("iconURI", "testValue");
////			    linki.setOnclick("alert('link');");
////			    linki.getAttributes().put("onClick", "alert('link')");
//			    HtmlOutputText texti = new HtmlOutputText();		    
//			    texti.setValueBinding("value",WFUtil.createValueBinding("#{node.description}"));
//			    
//			    linki.getChildren().add(texti);		    
//			    tree.getFacets().put("IWTreeNode",linki);
//			    tree.getAttributes().put("sourceTree", "true");				
////				parentPage.add(viewer);
//				parentPage.add(tree);
//				parentPage.add(groups);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			}
			RadioGroup radioGroup = new RadioGroup("server");
//
			Text txtLocal = new Text("Local server");
			RadioButton btnLocal = new RadioButton("server");
			btnLocal.attributes.put("onClick","setLocal('"+tableID+"');");

			Text txtRemote = new Text("Remote server");
			RadioButton btnRemote = new RadioButton("server");
			btnRemote.attributes.put("onClick","setRemote('"+tableID+"');");
			
			radioGroup.addRadioButton(btnLocal, txtLocal);
			radioGroup.addRadioButton(btnRemote, txtRemote);	
			
			Text txtSeverName = new Text("Server name: ");
			Text txtLogin = new Text("Login: ");
			Text txtPass = new Text("Password: ");
			
			TextInput inpServerName = new TextInput();
			inpServerName.setId(serverNameFieldId);
			TextInput inpLogin = new TextInput();
			inpLogin.setId(loginFieldId);
			PasswordInput pswInput = new PasswordInput();
			pswInput.setId(passwordFieldid);
			
			GenericButton button = new GenericButton(buttonLabel);
			button.attributes.put("onClick", "sendConnectionData('"+serverNameFieldId+"','"+loginFieldId+"','"+passwordFieldid+"')");
			
			Table table = new Table(2, 4);
			table.setId(tableID);
			table.attributes.put("class", "hidden_table");
			table.add(txtSeverName, 1, 1);
			table.add(txtLogin, 1, 2);
			table.add(txtPass, 1, 3);
			table.add(inpServerName, 2, 1);
			table.add(inpLogin, 2, 2);
			table.add(pswInput, 2, 3);
			table.add(button, 1, 4);
			
			parentPage.add(radioGroup);
			parentPage.add(table);					
	}
	
	public void encodeBegin(FacesContext fc)throws IOException{
		super.encodeBegin(fc);
		
		Layer panels = (Layer)this.getFacet("PANELS");
		this.renderChild(fc,panels);
		
	}
	public UserBusiness getUserBusiness(IWApplicationContext iwc) {
		if (this.userBiz == null) {
			try {
				this.userBiz = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.userBiz;
	}	
	private Collection getGroupTypes(String selectedGroup, IWContext iwc)  {
		Collection groupTypes = new ArrayList();
		Group group = null;
		// get group types
		GroupBusiness groupBusiness;
		try {
			groupBusiness =(GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			if (! CreateGroupWindow.NO_GROUP_SELECTED.equals(selectedGroup))  {
				group = groupBusiness.getGroupByGroupID((new Integer(selectedGroup)).intValue());
			}
		}
		// Remote and FinderException
		catch (Exception ex)  {
			throw new RuntimeException(ex.getMessage());
		}
		Iterator iterator = null;
		try {
			iterator = groupBusiness.getAllAllowedGroupTypesForChildren(group, iwc).iterator();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		while (iterator!=null && iterator.hasNext())  {
			GroupType item = (GroupType) iterator.next();
			String value = item.getType();
			groupTypes.add(value);
		}
		return groupTypes;
	}
	
	private boolean checkGroupType(Group group, Collection allowedGroupTypes) {
		String groupType = group.getGroupTypeValue();
		Iterator iterator = allowedGroupTypes.iterator();
		while (iterator.hasNext())  {
			String type = (String) iterator.next();
			if (type.equals(groupType)) {
				return true;
			}
		}
		return false;
	}
	
	public Collection convertGroupCollectionToGroupNodeCollection(Collection col, IWApplicationContext iwac){
		List list = new Vector();
		
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			Group group = (Group) iter.next();
			GroupTreeNode node = new GroupTreeNode(group,iwac);
			list.add(node);
		}

		return list;
	}	
	
	public WFTreeNode getRootNode(Collection groupNodes, WFTreeNode rootNode){
//		WFTreeNode node = null;
		for (Iterator iter = groupNodes.iterator(); iter.hasNext();) {
			GroupTreeNode element = (GroupTreeNode) iter.next();
			WFTreeNode newNode = new WFTreeNode(new IWTreeNode(element.getNodeName()));				
			if(element.getChildCount() != 0)
				newNode = getRootNode(element.getChildren(), newNode);
			newNode.setIdentifier(element.getId());
			rootNode.addChild(newNode);			
		}
		
		return rootNode;
	}
}