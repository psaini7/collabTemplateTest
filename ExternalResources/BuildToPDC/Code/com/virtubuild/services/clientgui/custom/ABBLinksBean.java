package com.virtubuild.services.clientgui.custom;
/*
 * Author: Shiyam M 
 * Date: 8-April-2019
 * Purpose: Bean class for holding the SPEP ABB Library links from the excel 
 */
public class ABBLinksBean {
	
	String productID; // 9th column. Filtered based on this attribute
	String hyperlink; // 13th column
	String instructionManualLink; // 31 column
	
	
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getHyperlink() {
		return hyperlink;
	}
	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}
	public String getInstructionManualLink() {
		return instructionManualLink;
	}
	public void setInstructionManualLink(String instructionManualLink) {
		this.instructionManualLink = instructionManualLink;
	}
	
	
}
