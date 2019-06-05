// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import java.util.Date;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;

class LogWriter
{
    private static final DateFormat dateFormat;
    
    static {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
    }
    
    static void log(String string, final String str) {
        if (str != null) {
            try {
                string = FileSystem.getRoot() + File.separator + string;
                final PrintWriter printWriter = new PrintWriter(new FileWriter(string, true));
                printWriter.println("" + LogWriter.dateFormat.format(new Date()) + "\n" + str);
                printWriter.close();
            }
            catch (Exception ex) {}
        }
    }
    
    static void log(String string, final String str, final Throwable t) {
        if (t != null) {
            try {
                string = FileSystem.getRoot() + File.separator + string;
                final PrintWriter s = new PrintWriter(new FileWriter(string, true));
                s.println("" + LogWriter.dateFormat.format(new Date()) + "\n" + str);
                t.printStackTrace(s);
                s.close();
            }
            catch (Exception ex) {}
        }
    }
    
    static void log(String string, final Throwable t) {
        if (t != null) {
            try {
                string = FileSystem.getRoot() + File.separator + string;
                final PrintWriter s = new PrintWriter(new FileWriter(string, true));
                s.println("" + LogWriter.dateFormat.format(new Date()));
                t.printStackTrace(s);
                s.close();
            }
            catch (Exception ex) {}
        }
    }
}
