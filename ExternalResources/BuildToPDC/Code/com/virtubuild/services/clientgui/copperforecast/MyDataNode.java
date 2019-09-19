package com.virtubuild.services.clientgui.copperforecast;

import java.util.Collections;
import java.util.List;

public class MyDataNode implements Comparable<MyDataNode> {

	private Integer columnNumber;
	private String columnName;
	private String columnTitle;
	private Float quantity;
	private String articleNumber;
	
	private Float width;
	private Float depth;
	private Float length;
	private Double weight;
	private String unit;
	
	private String columnNumberDesc;
		

	private List<MyDataNode> children;

	public MyDataNode() {

	}

	public MyDataNode(String columnNumberDesc, Integer columnNumber, String columnName, String columnTitle, Float quantity, String articleNumber, Float width, Float depth, Float length, Double weight, String unit, List<MyDataNode> children) {
		
		this.columnNumberDesc = columnNumberDesc;
		this.columnNumber = columnNumber;
		this.columnName = columnName;
		this.columnTitle = columnTitle;
		this.quantity = quantity;
		this.articleNumber = articleNumber;
		this.width = width;
		this.depth = depth;
		this.length = length;
		this.weight = weight;
		this.unit = unit;
		this.children = children;

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

	public void setWidth(Float width) {
		this.width = width;
	}

	public void setDepth(Float depth) {
		this.depth = depth;
	}

	public void setLength(Float length) {
		this.length = length;
	}

	public String getColumnNumberDesc() {
		return columnNumberDesc;
	}

	public void setColumnNumberDesc(String columnNumberDesc) {
		this.columnNumberDesc = columnNumberDesc;
	}

	public List<MyDataNode> getChildren() {
		return children;
	}

	public void setChildren(List<MyDataNode> children) {
		this.children = children;
	}
	

	public String getColumnTitle() {
		return columnTitle;
	}
	

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public Float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public Float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		return columnNumberDesc;
	}


	@Override
	public int compareTo(MyDataNode thatNode) {
		if ((Integer.valueOf(this.columnNumber)) > Integer.valueOf(thatNode.getColumnNumber())) {
			return 0;
		} else
			return 1;
	}


	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	
	
	}
