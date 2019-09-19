package com.virtubuild.services.clientgui.custom;
/*
 * Author: Shiyam M 
 * Date: 8-April-2019
 * Purpose: Bean class for holding the Syndication_SYN85233 data from excel file
 */
public class SyndicateBean {
	//For BOM
	String orderNumber; //7th column. Filtered based on this attribute
	String instructionManualID; // 12th column
	String mechanicalDrawings; // 15th column
	String weight; // 36th column
	//For Copper parts
	String length; //24th column
	String depth; //30th column
	String width; //42nd column
	//For Copper export
	String bomCode; //6th column - Product Main Type
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getInstructionManualID() {
		return instructionManualID;
	}
	public void setInstructionManualID(String instructionManual) {
		this.instructionManualID = instructionManual;
	}
	public String getMechanicalDrawings() {
		return mechanicalDrawings;
	}
	public void setMechanicalDrawings(String mechanicalDrawings) {
		this.mechanicalDrawings = mechanicalDrawings;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getBomCode() {
		return bomCode;
	}
	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}
	

}
