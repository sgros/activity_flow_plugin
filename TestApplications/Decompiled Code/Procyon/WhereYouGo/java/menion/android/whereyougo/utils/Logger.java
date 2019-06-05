// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import org.xmlpull.v1.XmlPullParser;
import android.util.Log;

public class Logger
{
    private static final String TAG = "Logger";
    private static final String logFileName = "error.log";
    
    public static void d(final String s, String s2) {
        if (Const.STATE_DEBUG_LOGS) {
            if (s2 == null) {
                s2 = "null";
            }
            Log.d(s, s2);
        }
    }
    
    public static void e(final String str, final String str2) {
        Log.e(str, str2);
        LogWriter.log("error.log", "[" + str + "] " + str2);
    }
    
    public static void e(final String str, final String str2, final Exception ex) {
        String s;
        if (str2 != null) {
            s = str2;
        }
        else {
            s = "";
        }
        Log.e(str, s, (Throwable)ex);
        LogWriter.log("error.log", "[" + str + "] " + str2, ex);
    }
    
    public static void e(final String str, final String str2, final Throwable t) {
        Log.e(str, str2, (Throwable)new Exception(t.toString()));
        LogWriter.log("error.log", "[" + str + "] " + str2, t);
    }
    
    public static void i(final String s, String s2) {
        if (Const.STATE_DEBUG_LOGS) {
            if (s2 == null) {
                s2 = "null";
            }
            Log.i(s, s2);
        }
    }
    
    public static void printParserState(final XmlPullParser xmlPullParser) {
        try {
            d("Logger", "event:" + xmlPullParser.getEventType() + ", attCount:" + xmlPullParser.getAttributeCount() + ", columnNum:" + xmlPullParser.getColumnNumber() + ", depth:" + xmlPullParser.getDepth() + ", ln:" + xmlPullParser.getLineNumber() + ", " + xmlPullParser.getPositionDescription());
        }
        catch (Exception ex) {
            e("Logger", "printParserState()");
        }
    }
    
    public static void v(final String s, String s2) {
        if (Const.STATE_DEBUG_LOGS) {
            if (s2 == null) {
                s2 = "null";
            }
            Log.v(s, s2);
        }
    }
    
    public static void w(final String s, String s2) {
        if (Const.STATE_DEBUG_LOGS) {
            if (s2 == null) {
                s2 = "null";
            }
            Log.w(s, s2);
        }
    }
}
