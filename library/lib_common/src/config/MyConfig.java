package config;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class MyConfig {
	private static Context c;
	public final static String CONFIG = "config";
	public final static String USER_ACCOUNT = "user_account";
	public final static String USER_PASSWORD = "user_password";
	public final static String PERSON_IMG = "person_img";
	public final static String USER_ID = "user_id";
	public final static String CLEAR_CACHE = "clearCache";
	public final static String WELCOME_IMG = "WELCOME_IMG";
	public final static String SPOT_IMG = "SPOT_IMG";
	public final static String SUB_SPOT_IMG = "SUB_SPOT_IMG";
	public final static int CLEAR_CACHE_DEFAULT =0;
	public final static int CLEAR_CACHE_SUCCESS = 1;

    public static String getUid(){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        String uid = sharePreferences.getString("uid", "");
        return uid;
    }

	public static void init(Context context){
		c = context;
	}
    public static void setUid(String uid){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putString("uid", uid).commit();
    }
    
    public static String getUserId(){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        String uid = sharePreferences.getString(USER_ID, null);
        if (TextUtils.isEmpty(uid)) {
			return null;
		}
        return uid;
    }
    public static void setUserId(String useId){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putString(USER_ID, useId).commit();
    }
    
    public static void setLoginInfo(String key,String value){
    	SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    	Editor edit = sharePreferences.edit();
    	edit.putString(key, value).commit();
    }
    
    public static String getLoginInfo(String key){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getString(key, null);
    }
    
    public static void setIsAutoLogin(boolean isAutoLogin){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit(); 
        edit.putBoolean("user_auto_login", isAutoLogin);
        edit.commit();
    }
    public static boolean getIsAutoLogin(){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getBoolean("user_auto_login", false);
    }
    public static void setIsLogin(boolean isLogin){
    	if (!isLogin) {
			setUserId(null);
		}
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit(); 
        edit.putBoolean("user_is_login", isLogin);
        edit.commit();
    }
    
    public static boolean getIsLogin(){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Boolean b=false;
        if(sharePreferences.getBoolean("user_is_login", false)){
            b=true;
        }
        return b;
    }
    
    public static void setNormalString(String key,String value){
    	SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    	Editor edit = sharePreferences.edit();
    	edit.putString(key, value).commit();
    }
    
    public static String getNormalString(String key,String defValue){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getString(key, defValue);
    }
    public static void setNormalValue(String key,int value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putInt(key, value).commit();
    }
    
    public static int getNormalValue(String key,int defValue){ 
    	SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    	return sharePreferences.getInt(key, defValue);
    }
    public static void setNormalValue(String key,long value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putLong(key, value).commit();
    }
    
    public static long getNormalValue(String key,long defValue){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getLong(key, defValue);
    }
    public static void setPersonImg(String key,int value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putInt(key, value).commit();
    }
    
    public static int getPersonImg(String key,int defValue){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getInt(key, defValue);
    }
    /**
     * 保存CPU数量
     * @param key
     * @param value
     */
    public static void setCpuNum(String key,int value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putInt(key, value).commit();
    }
    /**
     * 获取cpu数量
     * @param key
     * @param defValue
     * @return
     */
    public static int getCpuNum(String key,int defValue){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getInt(key, defValue);
    }
    /**
     * 设置CPU型号
     * @param key
     * @param value
     */
    public static void setCpuType(String key,String value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putString(key, value).commit();
    }
    /**
     * 获取cpu型号
     * @param key
     * @return
     */
    public static String getCpuType(String key){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getString(key, null);
    }
    /**
     * 设置CPU主频
     * @param key
     * @param value
     */
    public static void setCpuFreq(String key,String value){
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        Editor edit = sharePreferences.edit();
        edit.putString(key, value).commit();
    }
    /**
     * 获取cpu主频
     * @param key
     * @return
     */
    public static String getCpuFreq(String key){ 
        SharedPreferences sharePreferences = c.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sharePreferences.getString(key, null);
    }
}
