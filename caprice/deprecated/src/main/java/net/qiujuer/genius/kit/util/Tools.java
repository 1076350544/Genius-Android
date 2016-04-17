/*
 * Copyright (C) 2014 Qiujuer <qiujuer@live.cn>
 * WebSite http://www.qiujuer.net
 * Created 08/13/2014
 * Changed 1/07/2016
 * Version 3.0.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.qiujuer.genius.kit.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * This is a util tools kit
 *
 * @see #sleepIgnoreInterrupt(long)
 * @see #copyFile(File, File)
 * @see #getAndroidId(Context)
 * @see #getSerialNumber()
 * @see #modulateAlpha(int, int)
 * @see #modulateColorAlpha(int, int)
 * @see #setColorAlpha(int, int)
 */
public final class Tools {
    /**
     * Sleep time
     * Don't throw an InterruptedException exception
     *
     * @param time millis time
     */
    public static void sleepIgnoreInterrupt(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleep time
     * Don't throw an InterruptedException exception
     *
     * @param millis millis time
     * @param nanos  nanos time
     */
    public static void sleepIgnoreInterrupt(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy file to file
     *
     * @param source Source File
     * @param target Target File
     * @return Return copy isOk
     */
    public static boolean copyFile(File source, File target) {
        boolean bFlag = false;
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (!target.exists()) {
                if (!target.createNewFile()) {
                    // Create Error
                    return false;
                }
            }
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            byte[] buffer = new byte[8 * 1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            bFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
            bFlag = false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bFlag;
    }

    /**
     * Equipment is started for the first time the generated number
     * Are potential "9774d56d682e549c"
     *
     * @param context Context
     * @return Number
     */
    public static String getAndroidId(Context context) {
        return android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    /**
     * This device's SN
     *
     * @return SerialNumber
     */
    public static String getSerialNumber() {
        String serialNumber = android.os.Build.SERIAL;
        if ((serialNumber == null || serialNumber.length() == 0 || serialNumber.contains("unknown"))) {
            String[] keys = new String[]{"ro.boot.serialno", "ro.serialno"};
            for (String key : keys) {
                try {
                    Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
                    serialNumber = (String) systemProperties_get.invoke(null, key);
                    if (serialNumber != null && serialNumber.length() > 0 && !serialNumber.contains("unknown"))
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return serialNumber;
    }


    /**
     * Modulate the colorAlpha to new alpha
     *
     * @param colorAlpha Color's alpha
     * @param alpha      Modulate alpha
     * @return Modulate alpha
     */
    public static int modulateAlpha(int colorAlpha, int alpha) {
        int scale = alpha + (alpha >> 7);
        return colorAlpha * scale >> 8;
    }

    /**
     * Modulate the color to new alpha
     *
     * @param color Color
     * @param alpha Modulate alpha
     * @return Modulate alpha color
     */
    public static int modulateColorAlpha(int color, int alpha) {
        int colorAlpha = color >>> 24;
        int scale = alpha + (alpha >> 7);
        int newAlpha = colorAlpha * scale >> 8;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return newAlpha << 24 | r << 16 | g << 8 | b;
    }


    /**
     * Set the color to new alpha
     *
     * @param color Color
     * @param alpha New alpha
     * @return New alpha color
     */
    public static int setColorAlpha(int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return alpha << 24 | r << 16 | g << 8 | b;
    }
}
