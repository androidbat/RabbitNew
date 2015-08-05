package com.c.rabbit.constant;

import com.c.rabbit.utils.PropertiesUtils;

public class UrlFactory {
	public static String platform;
    public static String HTTP_HEAD;
	static {
		HTTP_HEAD = PropertiesUtils.getValueA(PropertiesUtils.PROPERTIES_COMMON_PATH, PropertiesUtils.SERVER_PATH).trim();
        platform = AppConstants.getInstance().getmPlatform();
    }

    public static String getUpload() {
        return "";
    }
}
