package com.liangmayong.preferences;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Preferences
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class Preferences {

    // perferencesMap
    private static final Map<String, Preferences> perferencesMap = new HashMap<String, Preferences>();
    // DEFAULT_PREFERENCES_NAME
    private static final String DEFAULT_PREFERENCES_NAME = "default";
    // application
    private static Application application = null;
    // DEBUG
    private static boolean DEBUG = true;
    // tag
    private static String TAG = "Preferences";

    /**
     * init Preferences
     *
     * @param application application
     * @param debug       debug
     */
    public static void init(Application application, boolean debug) {
        Preferences.application = application;
        Preferences.DEBUG = isDebugable(application, debug);
        if (DEBUG) {
            Log.d(TAG, "Preferences initialized");
        }
    }

    /**
     * isDebugable
     *
     * @param application application
     * @return true or false
     */
    private static boolean isDebugable(Application application, boolean debug) {
        try {
            ApplicationInfo info = application.getApplicationInfo();
            boolean debugable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (debugable) {
                return debug;
            }
            return false;
        } catch (Exception e) {
            return debug;
        }
    }

    /**
     * isInit()
     *
     * @return is inited
     */
    private static boolean isInited() {
        if (getApplication() != null) {
            return true;
        }
        return false;
    }

    /**
     * getApplication
     *
     * @return application
     */
    private static Application getApplication() {
        return application;
    }

    /**
     * getDefaultPreferences
     *
     * @return preferences
     */
    public static Preferences getDefaultPreferences() {
        return getPreferences(DEFAULT_PREFERENCES_NAME);
    }

    /**
     * getPreferences
     *
     * @param name name
     * @return preferences
     */
    @SuppressLint("DefaultLocale")
    public static Preferences getPreferences(String name) {
        if (name == null || "".equals(name)) {
            name = DEFAULT_PREFERENCES_NAME;
        }
        if (perferencesMap.containsKey(name)) {
            return perferencesMap.get(name);
        } else {
            Preferences preferences = new Preferences(name);
            perferencesMap.put(name, preferences);
            return preferences;
        }
    }

    /**
     * PreferencesUtils
     *
     * @param sharedPreferencesName sharedPreferencesName
     */
    private Preferences(String sharedPreferencesName) {
        this.sharedPreferencesName = "preferences_" + sharedPreferencesName;
    }

    private String sharedPreferencesName = "";

    private Map<String, String> preferencesValueMap = new HashMap<String, String>();

    /**
     * getString
     *
     * @param key key
     * @return string
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * getString
     *
     * @param key      key
     * @param defValue defValue
     * @return string
     */
    public String getString(String key, String defValue) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "Preferences not initialized");
            }
            return "";
        }
        if (preferencesValueMap.containsKey(key)) {
            try {
                return (String) preferencesValueMap.get(key);
            } catch (Exception e) {
            }
        }
        String mString = "";
        try {
            SharedPreferences Host = getApplication().getSharedPreferences(sharedPreferencesName, 0);
            mString = new String(Des.decrypt(Host.getString(key, defValue), key));
        } catch (Exception e) {
        }
        return mString;
    }

    /**
     * get int
     *
     * @param key      key
     * @param defValue defValue
     * @return int
     */
    public int getInt(String key, int defValue) {
        int mInt = 0;
        try {
            String string = getString(key, defValue + "");
            mInt = Integer.parseInt(string);
        } catch (Exception e) {
        }
        return mInt;
    }

    /**
     * get boolean
     *
     * @param key      key
     * @param defValue defValue
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defValue) {
        boolean retu = false;
        try {
            retu = "Yes".equals(getString(key, defValue ? "Yes" : "No"));
        } catch (Exception e) {
        }
        return retu;
    }

    /**
     * get float
     *
     * @param key      key
     * @param defValue defValue
     * @return float
     */
    public float getFloat(String key, float defValue) {
        float retu = 0;
        try {
            String string = getString(key, defValue + "");
            retu = Float.parseFloat(string);
        } catch (Exception e) {
        }
        return retu;
    }

    /**
     * get long
     *
     * @param key      key
     * @param defValue defValue
     * @return long
     */
    public long getLong(String key, long defValue) {
        long retu = 0;
        try {
            String string = getString(key, defValue + "");
            retu = Long.parseLong(string);
        } catch (Exception e) {
        }
        return retu;
    }

    /**
     * set string
     *
     * @param key   key
     * @param value value
     */
    public void setString(String key, String value) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "Preferences not initialized");
            }
            return;
        }
        try {
            SharedPreferences.Editor Home = getApplication().getSharedPreferences(sharedPreferencesName, 0).edit();
            Home.putString(key, Des.encrypt(value.getBytes(), key));
            Home.commit();
            preferencesValueMap.put(key, value);
        } catch (Exception e) {
        }
    }

    /**
     * set long
     *
     * @param key   key
     * @param value value
     */
    public void setLong(String key, long value) {
        setString(key, value + "");
    }

    /**
     * set float
     *
     * @param key   key
     * @param value value
     */
    public void setFloat(String key, float value) {
        setString(key, value + "");
    }

    /**
     * set boolean
     *
     * @param key   key
     * @param value value
     */
    public void setBoolean(String key, boolean value) {
        setString(key, value ? "Yes" : "No");
    }

    /**
     * set int
     *
     * @param key   key
     * @param value value
     */
    public void setInt(String key, int value) {
        setString(key, value + "");
    }

    /**
     * remove
     *
     * @param key key
     */
    public void remove(String key) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "Preferences not initialized");
            }
            return;
        }
        try {
            SharedPreferences.Editor sp = getApplication().getSharedPreferences(sharedPreferencesName, 0).edit();
            sp.remove(key);
            sp.commit();
        } catch (Exception e) {
        }
    }

    /**
     * Des
     *
     * @author LiangMaYong
     * @version 1.0
     */
    private static final class Des {

        private String iv = "national";
        private static Des des = null;

        private Des() {
        }

        private static Des getDes() {
            if (des == null) {
                des = new Des();
            }
            return des;
        }

        /**
         * encrypt
         *
         * @param encryptByte encryptByte
         * @param encryptKey  encryptKey
         * @return encrypt string
         */
        public static String encrypt(byte[] encryptByte, String encryptKey) {
            return getDes()._encrypt(encryptByte, getKey(encryptKey));
        }

        @SuppressLint("TrulyRandom")
        private String _encrypt(byte[] encryptByte, String encryptKey) {
            try {
                IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
                SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
                byte[] encryptedData = cipher.doFinal(encryptByte);
                return Base64.encode(encryptedData);
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * decrypt
         *
         * @param encryptString encryptString
         * @param encryptKey    encryptKey
         * @return byte[]
         */
        public static byte[] decrypt(String encryptString, String encryptKey) {
            return getDes()._decrypt(encryptString, getKey(encryptKey));
        }

        private byte[] _decrypt(String encryptString, String encryptKey) {
            try {
                byte[] encryptByte = Base64.decode(encryptString);
                IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes());
                SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
                Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
                return cipher.doFinal(encryptByte);
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * md5 encode
         *
         * @param plain plain
         * @return string
         */
        private final static String md5(String plain) {
            String re_md5 = new String();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plain.getBytes());
                byte b[] = md.digest();

                int i;

                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                re_md5 = buf.toString();
            } catch (NoSuchAlgorithmException e) {
            }
            return re_md5;
        }

        /**
         * The encryptKey to 8 characters
         *
         * @param encryptKey encryptKey
         * @return string
         */
        private static String getKey(String encryptKey) {
            return md5(encryptKey).substring(4, 12);
        }

        private static class Base64 {

            private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                    .toCharArray();

            public static String encode(byte[] data) {
                int start = 0;
                int len = data.length;
                StringBuffer buf = new StringBuffer(data.length * 3 / 2);

                int end = len - 3;
                int i = start;

                while (i <= end) {
                    int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
                            | (((int) data[i + 2]) & 0x0ff);

                    buf.append(legalChars[(d >> 18) & 63]);
                    buf.append(legalChars[(d >> 12) & 63]);
                    buf.append(legalChars[(d >> 6) & 63]);
                    buf.append(legalChars[d & 63]);

                    i += 3;
                }

                if (i == start + len - 2) {
                    int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

                    buf.append(legalChars[(d >> 18) & 63]);
                    buf.append(legalChars[(d >> 12) & 63]);
                    buf.append(legalChars[(d >> 6) & 63]);
                    buf.append("=");
                } else if (i == start + len - 1) {
                    int d = (((int) data[i]) & 0x0ff) << 16;

                    buf.append(legalChars[(d >> 18) & 63]);
                    buf.append(legalChars[(d >> 12) & 63]);
                    buf.append("==");
                }

                return buf.toString();
            }

            public static byte[] decode(String s) {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    decode(s, bos);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                byte[] decodedBytes = bos.toByteArray();
                try {
                    bos.close();
                    bos = null;
                } catch (IOException ex) {
                    System.err.println("Error while decoding BASE64: " + ex.toString());
                }
                return decodedBytes;
            }

            private static void decode(String s, OutputStream os) throws IOException {
                int i = 0;

                int len = s.length();

                while (true) {
                    while (i < len && s.charAt(i) <= ' ')
                        i++;

                    if (i == len)
                        break;

                    int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12)
                            + (decode(s.charAt(i + 2)) << 6) + (decode(s.charAt(i + 3)));

                    os.write((tri >> 16) & 255);
                    if (s.charAt(i + 2) == '=')
                        break;
                    os.write((tri >> 8) & 255);
                    if (s.charAt(i + 3) == '=')
                        break;
                    os.write(tri & 255);

                    i += 4;
                }
            }

            private static int decode(char c) {
                if (c >= 'A' && c <= 'Z')
                    return ((int) c) - 65;
                else if (c >= 'a' && c <= 'z')
                    return ((int) c) - 97 + 26;
                else if (c >= '0' && c <= '9')
                    return ((int) c) - 48 + 26 + 26;
                else
                    switch (c) {
                        case '+':
                            return 62;
                        case '/':
                            return 63;
                        case '=':
                            return 0;
                        default:
                            throw new RuntimeException("unexpected code: " + c);
                    }
            }
        }
    }
}