package com.idega.block.cal.business;

import java.util.Collection;

public class GroupNode {
	String name = null;
	String uniqueId = null;
	boolean hasChildren = false;
	Collection<GroupNode> children = null;
	
	public GroupNode() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public Collection<GroupNode> getChildren() {
		return children;
	}
	public void setChildren(Collection<GroupNode> children) {
		this.children = children;
	}
	
}
