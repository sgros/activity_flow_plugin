// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Assistant;

import java.io.Serializable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import android.util.Log;
import java.io.Closeable;

public class ExecCom extends Thread
{
    public static void closeSilently(final Object... array) {
        for (int i = 0; i < array.length; ++i) {
            final Object o = array[i];
            if (o != null) {
                try {
                    if (!(o instanceof Closeable)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("cannot close: ");
                        sb.append(o);
                        Log.d("NetMonitor", sb.toString());
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("cannot close ");
                        sb2.append(o);
                        throw new RuntimeException(sb2.toString());
                    }
                    ((Closeable)o).close();
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
    
    public static String readFully(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        return byteArrayOutputStream.toString("UTF-8");
    }
    
    static void user(final String command) {
        try {
            final Process exec = Runtime.getRuntime().exec(command);
            try {
                exec.waitFor();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
    
    public static String userForResult(String command) {
        Serializable fully = null;
        try {
            final Process exec = Runtime.getRuntime().exec(command);
            command = (String)new DataOutputStream(exec.getOutputStream());
            try {
                final InputStream inputStream = exec.getInputStream();
                try {
                    ((DataOutputStream)command).writeBytes("exit\n");
                    ((DataOutputStream)command).flush();
                    try {
                        exec.waitFor();
                    }
                    catch (InterruptedException fully) {
                        ((Throwable)fully).printStackTrace();
                    }
                    fully = readFully(inputStream);
                    closeSilently(command, inputStream);
                    command = (String)fully;
                }
                catch (IOException fully) {}
                finally {
                    fully = command;
                    final String s;
                    command = s;
                }
            }
            catch (IOException ex) {}
            finally {
                fully = command;
            }
        }
        catch (IOException ex2) {}
    }
}
