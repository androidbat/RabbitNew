package com.c.rabbit.utils;

import android.annotation.SuppressLint;
import android.os.StrictMode;

/**
 * <h2>Used to turn on strict mode for Dev builds</h2>
 *
 * <h3>Common uses:</h3>
 * <code>StrictModeUtils.{@link #enableStrictModeForDevRelease enableStrictModeForDevRelease}();</code><br />
 *
 * After calling this method, the screen will flash if any of the following occur:
 * <ul>
 *     <li>Disk reads on the UI thread</li>
 *     <li>Disk writes on the UI thread</li>
 *     <li>Disk network calls on the UI thread</li>
 * </ul>
 */
public class StrictModeUtils {

	/**
	 * Turn on Strict Mode options.  Good idea for dev builds.
     *
     * See: http://developer.android.com/reference/android/os/StrictMode.html
     *
	 */
	@SuppressLint("NewApi")//9
	public static void enableStrictModeForDevRelease(){
		StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyFlashScreen()
                        .build()
        );

	}
}
