package com.idega.block.cal.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading2;

public class EntryInfoBlock extends Block{
	public void main(IWContext iwc) throws Exception {
		
		Layer header = new Layer();
		header.add(new Heading1(iwc.getParameter("entryName")));
		header.setId("entryInfoHeader");
		this.add(header);
		
		// Menu
		Layer menu = new Layer();
		menu.setId("editModuleMenu");
//		Lists navigation = new Lists();
//		navigation.setId("editModuleMenuNavigation");
//		iwc.getp
//		ListItem settings = new ListItem();
//		Link settingsLink = new Link(iwrb.getLocalizedString("settings", "Settings"), "#");
//		settings.add(settingsLink);
//		navigation.add(settings);
		
		/*ListItem settings2 = new ListItem();
		Link settings2Link = new Link(iwrb.getLocalizedString("settings2", "Settings2"), "#");
		settings2.add(settings2Link);
		navigation.add(settings2);*/
//		menu.add(navigation);
		menu.add(new Heading2(iwc.getParameter("entryStartTime")+"-"+iwc.getParameter("entryEndTime")));
		menu.add(new Heading2(iwc.getParameter("entryDescription")));
		this.add(menu);
	}
}
