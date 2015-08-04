package com.c.rabbit;

public class DataListenerAdapter<T> extends DataListener<T> {
	@Override
	public void onDataReady(T result) {}
	@Override
	public void onNoData(int state) {}

}
