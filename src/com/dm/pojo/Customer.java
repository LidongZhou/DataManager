package com.dm.pojo;

public class Customer {

	private int table_id;
	private String ship_name;
	private String ship_length;
	private String ship_width;
	private String ship_heigth;
	private String ship_weight;
	private String haul_height;
	private String ship_owner_name;
	private String ship_owner_phone;
	private String more;
	

	public Customer(int table_id,String ship_owner_name,String ship_owner_phone,String ship_name,
					String ship_length,String ship_width,String ship_heigth,
					String ship_weight,String haul_height,String more) {
		super();
		
		this.table_id = table_id;
		this.ship_name = ship_name;
		this.ship_length = ship_length;
		this.ship_width = ship_width;
		this.ship_heigth = ship_heigth;
		this.ship_weight = ship_weight;
		this.haul_height = haul_height;
		this.ship_owner_name = ship_owner_name;
		this.ship_owner_phone = ship_owner_phone;
		this.more = more;
	}

	public int getTableId() {
		return table_id;
	}
	
	public String getShipName() {
		return ship_name;
	}

	public void setShipName(String ship_name) {
		this.ship_name = ship_name;
	}
		
	public String getShipLength() {
		return ship_length;
	}

	public void setShipLength(String ship_length) {
		this.ship_length = ship_length;
	}
	
	public String getShipWidth() {
		return ship_width;
	}

	public void setShipWidth(String ship_width) {
		this.ship_width = ship_width;
	}
	
	
	public String getShipHeigth() {
		return ship_heigth;
	}

	public void setShipHeigth(String ship_heigth) {
		this.ship_heigth = ship_heigth;
	}
	
	
	public String geShipWeight() {
		return ship_weight;
	}

	public void setShipWeight(String ship_weight) {
		this.ship_weight = ship_weight;
	}
	
	public String getHaulHeight() {
		return haul_height;
	}

	public void setHaulHeight(String haul_height) {
		this.haul_height = haul_height;
	}
	
	public String getShipownerName() {
		return ship_owner_name;
	}

	public void setShipwnerName(String ship_owner_name) {
		this.ship_owner_name = ship_owner_name;
	}
	
	public String getShipOwnerPhone() {
		return ship_owner_phone;
	}

	public void setShipOwnerPhone(String ship_owner_phone) {
		this.ship_owner_phone = ship_owner_phone;
	}
	
	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

}
