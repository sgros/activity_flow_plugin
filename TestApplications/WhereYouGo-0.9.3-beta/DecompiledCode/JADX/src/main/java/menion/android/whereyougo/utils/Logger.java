package menion.android.whereyougo.utils;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;

public class Logger {
    private static final String TAG = "Logger";
    private static final String logFileName = "error.log";

    /* renamed from: d */
    public static void m20d(String tag, String msg) {
        if (Const.STATE_DEBUG_LOGS) {
            if (msg == null) {
                msg = "null";
            }
            Log.d(tag, msg);
        }
    }

    /* renamed from: e */
    public static void m21e(String tag, String msg) {
        Log.e(tag, msg);
        LogWriter.log(logFileName, "[" + tag + "] " + msg);
    }

    /* renamed from: e */
    public static void m22e(String tag, String msg, Exception e) {
        Log.e(tag, msg != null ? msg : "", e);
        LogWriter.log(logFileName, "[" + tag + "] " + msg, e);
    }

    /* renamed from: e */
    public static void m23e(String tag, String msg, Throwable t) {
        Log.e(tag, msg, new Exception(t.toString()));
        LogWriter.log(logFileName, "[" + tag + "] " + msg, t);
    }

    /* renamed from: i */
    public static void m24i(String tag, String msg) {
        if (Const.STATE_DEBUG_LOGS) {
            if (msg == null) {
                msg = "null";
            }
            Log.i(tag, msg);
        }
    }

    public static void printParserState(XmlPullParser parser) {
        try {
            m20d(TAG, "event:" + parser.getEventType() + ", attCount:" + parser.getAttributeCount() + ", columnNum:" + parser.getColumnNumber() + ", depth:" + parser.getDepth() + ", ln:" + parser.getLineNumber() + ", " + parser.getPositionDescription());
        } catch (Exception e) {
            m21e(TAG, "printParserState()");
        }
    }

    /* renamed from: v */
    public static void m25v(String tag, String msg) {
        if (Const.STATE_DEBUG_LOGS) {
            if (msg == null) {
                msg = "null";
            }
            Log.v(tag, msg);
        }
    }

    /* renamed from: w */
    public static void m26w(String tag, String msg) {
        if (Const.STATE_DEBUG_LOGS) {
            if (msg == null) {
                msg = "null";
            }
            Log.w(tag, msg);
        }
    }
}
