package com.tonicartos.widget.stickygridheaders;

public class Log {
	private static boolean isShow = false;
	public static void d(String tag,String msg){
		if (isShow) {
			android.util.Log.d(tag, msg);
		}
	}
}
