package com.virtubuild.services.clientgui.custom;

import java.util.Collections;
import java.util.List;

public class MyDataNode implements Comparable<MyDataNode> {

    private String productID;
	private String productType;
	private Float quantity;
	private String orderNumber;
	private String uoM;
	private String desc;
	private String instructionManual;
	private Float grossPrice;
	private Float grossPriceSum;
	private String title;
	private Float grossPriceSumAll;
	private Integer columnNumber;
	private String columnName;
	private String position;
	private String link;
	private Float weight;

	private List<MyDataNode> children;

	public MyDataNode() {

	}

	public MyDataNode(String productType, String productID, String orderNumber, String position, Float quantity, String uoM, String desc, String instructionManual, Float weight,
			Float grossPrice, Float grossPriceSum, Integer columnNumber, String columnName, List<MyDataNode> children, String link) {
	    
	    this.productID = productID;
		this.productType = productType;
		this.orderNumber = orderNumber;
		this.position = position;
		this.quantity = quantity;
		this.desc = desc;
		this.instructionManual = instructionManual;
		this.weight = weight;
		this.uoM = uoM;
		this.grossPrice = grossPrice;
		this.grossPriceSum = grossPriceSum;
		this.children = children;
		this.link = link;
		this.columnNumber = columnNumber;
		this.columnName = columnName;

		if (this.children == null) {
			this.children = Collections.emptyList();
		}
	}

	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Float getGrossPriceSumAll() {
		return grossPriceSumAll;
	}

	public void setGrossPriceSumAll(Float grossPriceSumAll) {
		this.grossPriceSumAll = grossPriceSumAll;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUoM() {
		return uoM;
	}

	public void setUoM(String uoM) {
		this.uoM = uoM;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getInstructionManual() {
		return instructionManual;
	}

	public void setInstructionManual(String instructionManual) {
		this.instructionManual = instructionManual;
	}
	
    
    public String getProductID() {
        return productID;
    }

    
    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Float getGrossPriceSum() {
    	/*String regEx = "(\\d+)";
		String columnValue="Column-"+regEx;
		if (null!=productType && (productType.equalsIgnoreCase("Total Gross")|| productType.matches(columnValue))) {
			return grossPriceSum;
		} else {

			if (null == this.quantity && this.grossPrice == null) {
				return null;
			}

			return this.quantity * this.grossPrice;
		}*/
    	
    	/*if ( null!=productType && (productType.equalsIgnoreCase("Total Gross")) ){
    		
    		return this.grossPriceSum;
    		
    	}
    	else{
    		
    		if(null != this.quantity && null != this.grossPrice)
    			return this.quantity * this.grossPrice;
    		else	
    			return null;
    		
    	}*/
    	
    	return this.grossPriceSum;
    	
	}

	public void setGrossPriceSum(Float grossPriceSum) {
		this.grossPriceSum = grossPriceSum;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Float getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(Float grossPrice) {
		this.grossPrice = grossPrice;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public List<MyDataNode> getChildren() {
		return children;
	}

	public void setChildren(List<MyDataNode> children) {
		this.children = children;
	}

	public String toString() {
		return productType;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@Override
	public int compareTo(MyDataNode thatNode) {
		if ((Integer.valueOf(this.productType)) > Integer.valueOf(thatNode.getProductType())) {
			return 0;
		} else
			return 1;
	}
}
