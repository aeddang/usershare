package com.kakaovx.posemachine;

public final class Log {

    // app tag
    public static final String TAG = "VX_CORE_";

    public static boolean debug = true;
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    public static final int LOG_WTF = 100;

    public static void i(String tag, Object...objects) {
        android.util.Log.i(TAG + tag, toString(objects));
    }

    public static void d(String tag, Object...objects) {
        if (debug) {
            android.util.Log.d(TAG + tag, toString(objects));
        }
    }

    public static void w(String tag, Object...objects) {
        android.util.Log.w(TAG + tag, toString(objects));
    }

    public static void e(String tag, Object...objects) {
        android.util.Log.e(TAG + tag, toString(objects));
    }

    public static void v(String tag, Object...objects) {
        android.util.Log.v(TAG + tag, toString(objects));
    }

    private static String toString(Object...objects) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(o);
        }
        return sb.toString();
    }
}
