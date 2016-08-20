package com.liangmayong.eventsink;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * EventSink
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class EventSink {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////// init ///////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////

    private static boolean DEBUG = true;
    private static Application application = null;
    private static String TAG = "EventSink";
    private static Handler handler = new Handler();

    /**
     * init Preferences
     *
     * @param application application
     * @param debug       debug
     */
    public static void init(Application application, boolean debug) {
        EventSink.application = application;
        EventSink.DEBUG = isDebugable(application, debug);
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
    public static boolean isInited() {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////// static //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////

    private static final String ACTION_WHAT = "enent_sink_extra_what";
    private static final String DEFAULT = "event_sink";
    private static Map<String, EventSink> eventSinkMap = new HashMap<String, EventSink>();

    public static EventSink getDefault() {
        return getEvent(DEFAULT, false);
    }

    public static EventSink getEvent(String eventSinkName) {
        return getEvent(eventSinkName, false);
    }

    public static EventSink getEvent(String eventSinkName, boolean isAbsolutely) {
        if (eventSinkMap.containsKey(eventSinkName)) {
            return eventSinkMap.get(eventSinkName);
        } else {
            EventSink eventSink = new EventSink();
            eventSink.setActionPrefix(eventSinkName, isAbsolutely);
            eventSinkMap.put(eventSinkName, eventSink);
            return eventSink;
        }
    }

    /**
     * unregisterAll
     *
     * @param object object
     */
    public static void unregisterAll(Object object) {
        for (Map.Entry<String, EventSink> entry : eventSinkMap.entrySet()) {
            entry.getValue().unregister(object);
        }
    }

    /**
     * Sender
     */
    public static class Sender {

        private String action = "";
        private EventSink sink;

        private Sender(EventSink sink, String action) {
            this.action = action;
            this.sink = sink;
        }

        /**
         * send
         *
         * @param what what
         * @return Sender
         */
        public Sender send(int what) {
            Bundle bundle = new Bundle();
            bundle.putInt(ACTION_WHAT, what);
            sink.doSend(action, bundle);
            return this;
        }

        /**
         * sendDelayed
         *
         * @param what        what
         * @param delayMillis delayMillis
         * @return Sender
         */
        public Sender sendDelayed(int what, long delayMillis) {
            Bundle bundle = new Bundle();
            bundle.putInt(ACTION_WHAT, what);
            sink.doSendDelayed(action, bundle, delayMillis);
            return this;
        }

        /**
         * send
         *
         * @param what   what
         * @param bundle bundle
         * @return Sender
         */
        public Sender send(int what, Bundle bundle) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putInt(ACTION_WHAT, what);
            sink.doSend(action, bundle);
            return this;
        }

        /**
         * sendDelayed
         *
         * @param what        what
         * @param bundle      bundle
         * @param delayMillis delayMillis
         * @return Sender
         */
        public Sender sendDelayed(int what, Bundle bundle, long delayMillis) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putInt(ACTION_WHAT, what);
            sink.doSendDelayed(action, bundle, delayMillis);
            return this;
        }
    }

    /**
     * EventContent
     */
    public static class EventContent {
        private String action = "";
        private int what = 0;
        private String prefix;
        private Bundle extras = null;

        private EventContent(String prefix, String action, int what, Bundle extras) {
            this.action = action;
            this.what = what;
            this.extras = extras;
            this.prefix = prefix;
        }

        /**
         * getPrefix
         *
         * @return prefix
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * getAction
         *
         * @return action
         */
        public String getAction() {
            return action;
        }

        /**
         * getWhat
         *
         * @return what
         */
        public int getWhat() {
            return what;
        }

        /**
         * getExtras
         *
         * @return extras
         */
        public Bundle getExtras() {
            if (extras == null) {
                return new Bundle();
            }
            return new Bundle(extras);
        }

        /**
         * getStringExtra
         *
         * @param key          key
         * @param defualtValue defualtValue
         * @return extra
         */
        public String getStringExtra(String key, String defualtValue) {
            if (extras == null) {
                return defualtValue;
            }
            if (!extras.containsKey(key)) {
                return defualtValue;
            }
            return extras.getString(key);
        }

        /**
         * getStringExtra
         *
         * @param key key
         * @return extra
         */
        public String getStringExtra(String key) {
            return getStringExtra(key, "");
        }

        /**
         * getBooleanArrayExtra
         *
         * @param name name
         * @return extra
         */
        public boolean[] getBooleanArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getBooleanArray(name);
        }

        /**
         * getBooleanExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public boolean getBooleanExtra(String name, boolean defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getBoolean(name, defaultValue);
        }

        /**
         * getBundleExtra
         *
         * @param name name
         * @return extra
         */
        public Bundle getBundleExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getBundle(name);
        }

        /**
         * getByteArrayExtra
         *
         * @param name name
         * @return extra
         */
        public byte[] getByteArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getByteArray(name);
        }

        /**
         * getByteExtra
         *
         * @param name         name
         * @param defualtValue defualtValue
         * @return extra
         */
        public byte getByteExtra(String name, byte defualtValue) {
            if (extras == null) {
                return defualtValue;
            }
            return extras.getByte(name, defualtValue);
        }

        /**
         * getCharArrayExtra
         *
         * @param name name
         * @return extra
         */
        public char[] getCharArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getCharArray(name);
        }

        /**
         * getCharExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public char getCharExtra(String name, char defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getChar(name, defaultValue);
        }

        /**
         * getCharSequenceArrayExtra
         *
         * @param name name
         * @return extra
         */
        @TargetApi(Build.VERSION_CODES.FROYO)
        public CharSequence[] getCharSequenceArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getCharSequenceArray(name);
        }

        /**
         * getCharSequenceArrayListExtra
         *
         * @param name name
         * @return extra
         */
        @TargetApi(Build.VERSION_CODES.FROYO)
        public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getCharSequenceArrayList(name);
        }

        /**
         * getCharSequenceExtra
         *
         * @param name name
         * @return extra
         */
        public CharSequence getCharSequenceExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getCharSequence(name);
        }

        /**
         * getFloatExtra
         *
         * @param name name
         * @return extra
         */
        public double[] getDoubleArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getDoubleArray(name);
        }

        /**
         * getDoubleExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public double getDoubleExtra(String name, double defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getDouble(name, defaultValue);
        }

        /**
         * getFloatArrayExtra
         *
         * @param name name
         * @return extra
         */
        public float[] getFloatArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getFloatArray(name);
        }

        /**
         * getFloatExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public float getFloatExtra(String name, float defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getFloat(name, defaultValue);
        }

        /**
         * getIntArrayExtra
         *
         * @param name name
         * @return extra
         */
        public int[] getIntArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getIntArray(name);
        }

        /**
         * getIntExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public int getIntExtra(String name, int defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getInt(name, defaultValue);
        }

        /**
         * getIntegerArrayListExtra
         *
         * @param name name
         * @return extra
         */
        public ArrayList<Integer> getIntegerArrayListExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getIntegerArrayList(name);
        }

        /**
         * getLongArrayExtra
         *
         * @param name name
         * @return extra
         */
        public long[] getLongArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getLongArray(name);
        }

        /**
         * getLongExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public long getLongExtra(String name, long defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getLong(name, defaultValue);
        }

        /**
         * getParcelableArrayExtra
         *
         * @param name name
         * @return extra
         */
        public Parcelable[] getParcelableArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getParcelableArray(name);
        }

        /**
         * getParcelableArrayListExtra
         *
         * @param name name
         * @param <T>  type
         * @return extra
         */
        public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getParcelableArrayList(name);
        }

        /**
         * getParcelableExtra
         *
         * @param name name
         * @param <T>  type
         * @return extra
         */
        public <T extends Parcelable> T getParcelableExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getParcelable(name);
        }

        /**
         * getSerializableExtra
         *
         * @param name name
         * @return extra
         */
        public Serializable getSerializableExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getSerializable(name);
        }

        /**
         * getShortArrayExtra
         *
         * @param name name
         * @return extra
         */
        public short[] getShortArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getShortArray(name);
        }

        /**
         * getShortExtra
         *
         * @param name         name
         * @param defaultValue defaultValue
         * @return extra
         */
        public short getShortExtra(String name, short defaultValue) {
            if (extras == null) {
                return defaultValue;
            }
            return extras.getShort(name, defaultValue);
        }

        /**
         * getStringArrayExtra
         *
         * @param name name
         * @return extra
         */
        public String[] getStringArrayExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getStringArray(name);
        }

        /**
         * getStringArrayListExtra
         *
         * @param name name
         * @return extra
         */
        public ArrayList<String> getStringArrayListExtra(String name) {
            if (extras == null) {
                return null;
            }
            return extras.getStringArrayList(name);
        }

    }

    /**
     * OnEventListener
     *
     * @author LiangMaYong
     * @version 1.0
     */
    public static interface OnEventListener {
        void onEvent(Context context, EventContent content);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////// private /////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////

    private boolean isAbsolutely = true;
    private String ACTION_PREFIX = "event_sink";

    /**
     * cache event receiver
     */
    private Map<Object, Map<String, BroadcastReceiver>> receiverMap = new HashMap<Object, Map<String, BroadcastReceiver>>();

    /**
     * setActionPrefix
     *
     * @param actionPrefix actionPrefix
     */
    private void setActionPrefix(String actionPrefix, boolean absolutely) {
        this.ACTION_PREFIX = actionPrefix;
        this.isAbsolutely = absolutely;
    }

    /**
     * getActionPrefix
     *
     * @return action prefix
     */
    private String getActionPrefix() {
        if (!isAbsolutely) {
            return getApplication().getPackageName() + "." + ACTION_PREFIX + ".";
        }
        return ACTION_PREFIX + ".";
    }

    /**
     * doSendDelayed
     *
     * @param action      action
     * @param bundle      bundle
     * @param delayMillis delayMillis
     */
    private void doSendDelayed(final String action, final Bundle bundle, long delayMillis) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doSend(action, bundle);
            }
        }, delayMillis);
    }

    /**
     * next
     *
     * @param action action
     * @param bundle bundle
     */
    private void doSend(String action, Bundle bundle) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "EventSink not initialized");
            }
            return;
        }
        Intent intent = new Intent();
        String newAction = getActionPrefix() + action;
        if (DEBUG) {
            Log.d(TAG, "Send EventSink:" + newAction + " extras:" + bundle);
        }
        intent.setAction(newAction);
        if (bundle != null && !bundle.isEmpty()) {
            intent.putExtras(bundle);
        }
        getApplication().sendBroadcast(intent);
    }

    /**
     * newEvent
     *
     * @param action action
     * @return event
     */
    public Sender getSender(String action) {
        return new Sender(this, action);
    }

    /**
     * register
     *
     * @param object        object
     * @param actions       actions
     * @param eventListener eventListener
     */
    public void register(Object object, final String[] actions, final OnEventListener eventListener) {
        if (actions != null) {
            for (int i = 0; i < actions.length; i++) {
                register(object, actions[i], eventListener);
            }
        }
    }

    /**
     * register
     *
     * @param object        context
     * @param action        action
     * @param eventListener eventListener
     */
    public void register(Object object, final String action, final OnEventListener eventListener) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "register error:EventSink not initialized");
            }
            return;
        }
        Map<String, BroadcastReceiver> map = null;
        if (receiverMap.containsKey(object)) {
            map = receiverMap.get(object);
        } else {
            map = new HashMap<String, BroadcastReceiver>();
        }
        if (map.containsKey(action)) {
            unregister(object, action);
        }
        BroadcastReceiver broadcastReceiver = new EventReceiver(action, eventListener);
        IntentFilter filter = new IntentFilter();
        filter.addAction(getActionPrefix() + action);
        getApplication().registerReceiver(broadcastReceiver, filter);
        map.put(action, broadcastReceiver);
        receiverMap.put(object, map);
    }

    /**
     * EventReceiver
     */
    private class EventReceiver extends BroadcastReceiver {
        private OnEventListener eventListener;
        private String action;

        public EventReceiver(String action, OnEventListener eventListener) {
            this.action = action;
            this.eventListener = eventListener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getActionPrefix() + action)) {
                if (eventListener != null) {
                    Bundle bundle = intent.getExtras();
                    int what = 0;
                    if (bundle != null) {
                        what = bundle.getInt(ACTION_WHAT, 0);
                        bundle.remove(ACTION_WHAT);
                    }
                    eventListener.onEvent(context, new EventContent(getActionPrefix(),
                            intent.getAction().substring(getActionPrefix().length()), what, bundle));
                }
            }
        }
    }

    /**
     * unregister
     *
     * @param object object
     * @param action action
     */
    public void unregister(Object object, final String action) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "unregister error:EventSink not initialized");
            }
            return;
        }
        if (receiverMap.containsKey(object)) {
            Map<String, BroadcastReceiver> map = receiverMap.get(object);
            if (map.containsKey(action)) {
                try {
                    BroadcastReceiver broadcastReceiver = map.get(action);
                    getApplication().unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                }
                map.remove(action);
            }
            if (map.isEmpty()) {
                receiverMap.remove(object);
            } else {
                receiverMap.put(object, map);
            }
        }
    }

    /**
     * unregister
     *
     * @param object object
     */
    public void unregister(Object object) {
        if (!isInited()) {
            if (DEBUG) {
                Log.d(TAG, "unregister error:EventSink not initialized");
            }
            return;
        }
        if (receiverMap.containsKey(object)) {
            Map<String, BroadcastReceiver> map = receiverMap.get(object);
            for (BroadcastReceiver receiver : map.values()) {
                try {
                    getApplication().unregisterReceiver(receiver);
                } catch (Exception e) {
                }
            }
            receiverMap.remove(object);
        }
    }
}
