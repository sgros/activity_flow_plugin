package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExecCom extends Thread {
    static void user(String str) {
        try {
            try {
                Runtime.getRuntime().exec(str).waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String userForResult(String str) {
        DataOutputStream dataOutputStream;
        Throwable th;
        String str2 = "";
        Object obj = null;
        try {
            InputStream inputStream;
            Process exec = Runtime.getRuntime().exec(str);
            dataOutputStream = new DataOutputStream(exec.getOutputStream());
            try {
                inputStream = exec.getInputStream();
            } catch (IOException unused) {
                closeSilently(dataOutputStream, obj);
                return str2;
            } catch (Throwable th2) {
                th = th2;
                closeSilently(dataOutputStream, obj);
                throw th;
            }
            try {
                dataOutputStream.writeBytes("exit\n");
                dataOutputStream.flush();
                try {
                    exec.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                str = readFully(inputStream);
                closeSilently(dataOutputStream, inputStream);
                return str;
            } catch (IOException unused2) {
                obj = inputStream;
                closeSilently(dataOutputStream, obj);
                return str2;
            } catch (Throwable th3) {
                th = th3;
                obj = inputStream;
                closeSilently(dataOutputStream, obj);
                throw th;
            }
        } catch (IOException unused3) {
            dataOutputStream = null;
            closeSilently(dataOutputStream, obj);
            return str2;
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream = null;
            closeSilently(dataOutputStream, obj);
            throw th;
        }
    }

    public static String readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toString("UTF-8");
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static void closeSilently(Object... objArr) {
        for (Object obj : objArr) {
            if (obj != null) {
                try {
                    if (obj instanceof Closeable) {
                        ((Closeable) obj).close();
                    } else {
                        String str = Const.LOG_TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("cannot close: ");
                        stringBuilder.append(obj);
                        Log.d(str, stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("cannot close ");
                        stringBuilder.append(obj);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }
}
