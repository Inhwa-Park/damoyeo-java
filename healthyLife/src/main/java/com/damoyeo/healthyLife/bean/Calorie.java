package com.damoyeo.healthyLife.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Calorie {
	long id;
	String type;
	String title;
	long calory;
	String unit;
	long menuId;
	long sportId;
	String myType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getCalory() {
		return calory;
	}
	public void setCalory(long calory) {
		this.calory = calory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public long getMenuId() {
		return menuId;
	}
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	public long getSportId() {
		return sportId;
	}
	public void setSportId(long sportId) {
		this.sportId = sportId;
	}
	public String getMyType() {
		return myType;
	}
	public void setMyType(String myType) {
		this.myType = myType;
	}
	
}
