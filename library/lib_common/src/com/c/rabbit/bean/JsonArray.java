package com.c.rabbit.bean;

import java.util.ArrayList;

import com.c.rabbit.utils.ListUtils;

public class JsonArray<T> {
	public String desc;
	public String code;
	public ArrayList<T> data;
	
	public boolean hasData(){
		return ListUtils.hasData(data);
	}
	
}
