package com.kakaovx.posemachine;

import java.util.HashSet;
import java.util.Set;

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

    // Classes to be ignored when examining the stack trace
    private static final Set<String> IGNORED_CLASS_NAMES;

    static {
        IGNORED_CLASS_NAMES = new HashSet<>(3);
        IGNORED_CLASS_NAMES.add("dalvik.system.VMStack");
        IGNORED_CLASS_NAMES.add("java.lang.Thread");
        IGNORED_CLASS_NAMES.add(Log.class.getCanonicalName());
    }

    /**
     * Return caller's simple name.
     *
     * Android getStackTrace() returns an array that looks like this:
     *     stackTrace[0]: dalvik.system.VMStack
     *     stackTrace[1]: java.lang.Thread
     *     stackTrace[2]: com.google.android.apps.unveil.env.UnveilLogger
     *     stackTrace[3]: com.google.android.apps.unveil.BaseApplication
     *
     * This function returns the simple version of the first non-filtered name.
     *
     * @return caller's simple name
     */
    private static String getCallerSimpleName() {
        // Get the current callstack so we can pull the class of the caller off of it.
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (final StackTraceElement elem : stackTrace) {
            final String className = elem.getClassName();
            if (!IGNORED_CLASS_NAMES.contains(className)) {
                // We're only interested in the simple name of the class, not the complete package.
                final String[] classParts = className.split("\\.");
                return classParts[classParts.length - 1];
            }
        }

        return Log.class.getSimpleName();
    }

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
