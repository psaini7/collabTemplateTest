package com.virtubuild.services.abbeconf.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Part {

	private static final Logger LOGGER = LoggerFactory.getLogger(Part.class);
	String id = "";
	Double quantity;
	String artNo = "";
	String colNo = "";

	public String getColNo() {
		return colNo;
	}

	public void setColNo(String colNo) {
		this.colNo = colNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Part [colNo = " + colNo + ", id=" + id + ", quantity=" + quantity + ", artNo=" + artNo + "]";
	}

	public String getArtNo() {
		return artNo;
	}

	public void setArtNo(String artNo) {
		this.artNo = artNo;
	}

	@Override
	public boolean equals(Object arg) {
		try {
			Part p2 = (Part) arg;
			if (this.colNo.trim().equalsIgnoreCase(p2.colNo.trim()) && this.id.trim().equalsIgnoreCase(p2.id.trim())
					&& this.artNo.trim().equalsIgnoreCase(p2.artNo.trim())
					&& Double.compare(this.quantity, p2.quantity) == 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return colNo.hashCode() ^ id.hashCode() ^ quantity.hashCode() ^ artNo.hashCode();

	}

}
