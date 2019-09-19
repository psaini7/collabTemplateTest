package com.virtubuild.services.clientgui.copperforecast;

import java.util.Collections;
import java.util.List;

public class ExcelObject implements Comparable<ExcelObject> {
	String orderNumber;
	String productID;
	Integer columnNumber;
	String classBOM;
	String typeCode;
	String position;
	String quantity;
	String uoM;
	String desc;
	String grossPrice;
	String grossPriceSum;
	String link;
	public ExcelObject() {

	}
	public ExcelObject(Integer columnNumber, String classBOM, String productID, String typeCode, String position, String quantity,
			String uoM, String desc, String grossPrice, String grossPriceSum, String link) {
		this.columnNumber=columnNumber;
		this.classBOM=classBOM;
		this.productID=productID;
		this.typeCode=typeCode;
		this.position=position;
		this.quantity=quantity;
		this.uoM=uoM;
		this.desc=desc;
		this.grossPrice=grossPrice;
		this.grossPriceSum=grossPriceSum;
		this.link=link;
	}
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getClassBOM() {
		return classBOM;
	}

	public void setClassBOM(String classBOM) {
		this.classBOM = classBOM;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		if (quantity == "Qty") {
			this.quantity = quantity;
		} else {
			this.quantity = quantity.replace(".0", "");
		}
	}

	public String getUoM() {
		return uoM;
	}

	public void setUoM(String uoM) {
		this.uoM = uoM;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(String grossPrice) {
		this.grossPrice = grossPrice;
	}

	public String getGrossPriceSum() {
		return grossPriceSum;
	}

	public void setGrossPriceSum(String grossPriceSum) {
		this.grossPriceSum = grossPriceSum;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public int compareTo(ExcelObject excelObject) {

		// return POSITIVE , if first object is greater than the second.
		// return 0 , if first object is equal to the second.
		// return NEGATIVE , if first object is less than the second one.

		return this.columnNumber.compareTo(excelObject.getColumnNumber());

	}

	@Override
	public String toString() {
		return productID + "," + columnNumber + "," + classBOM + "," + typeCode + "," + position + "," + quantity + ","
				+ uoM + "," + grossPrice + "," + grossPriceSum + "," + link;
	}

}
