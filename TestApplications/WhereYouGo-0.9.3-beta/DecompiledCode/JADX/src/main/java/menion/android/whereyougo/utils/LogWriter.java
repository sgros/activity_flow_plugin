package menion.android.whereyougo.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class LogWriter {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    LogWriter() {
    }

    static void log(String fname, String msg) {
        if (msg != null) {
            try {
                PrintWriter out = new PrintWriter(new FileWriter(FileSystem.getRoot() + File.separator + fname, true));
                out.println("" + dateFormat.format(new Date()) + "\n" + msg);
                out.close();
            } catch (Exception e) {
            }
        }
    }

    static void log(String fname, Throwable ex) {
        if (ex != null) {
            try {
                PrintWriter out = new PrintWriter(new FileWriter(FileSystem.getRoot() + File.separator + fname, true));
                out.println("" + dateFormat.format(new Date()));
                ex.printStackTrace(out);
                out.close();
            } catch (Exception e) {
            }
        }
    }

    static void log(String fname, String msg, Throwable ex) {
        if (ex != null) {
            try {
                PrintWriter out = new PrintWriter(new FileWriter(FileSystem.getRoot() + File.separator + fname, true));
                out.println("" + dateFormat.format(new Date()) + "\n" + msg);
                ex.printStackTrace(out);
                out.close();
            } catch (Exception e) {
            }
        }
    }
}
