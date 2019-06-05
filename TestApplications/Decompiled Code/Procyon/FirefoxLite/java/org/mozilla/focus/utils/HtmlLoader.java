// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.io.InputStream;
import android.graphics.Bitmap;
import java.io.IOException;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import android.content.Context;

public class HtmlLoader
{
    private static final byte[] pngHeader;
    
    static {
        pngHeader = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
    }
    
    public static String loadDrawableAsDataURI(final Context context, int i, int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        final Bitmap bitmap = DrawableUtils.getBitmap(DrawableUtils.loadAndTintDrawable(context, i, n));
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap$CompressFormat.PNG, 0, (OutputStream)byteArrayOutputStream);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            final byte[] b = new byte[300];
            n = 0;
            while (true) {
                final int read = byteArrayInputStream.read(b);
                if (read <= 0) {
                    return sb.toString();
                }
                if ((i = n) == 0) {
                    if (read < 8) {
                        throw new IllegalStateException("Loaded drawable is improbably small");
                    }
                    for (i = 0; i < HtmlLoader.pngHeader.length; ++i) {
                        if (b[i] != HtmlLoader.pngHeader[i]) {
                            throw new IllegalStateException("Invalid png detected");
                        }
                    }
                    i = 1;
                }
                sb.append(Base64.encodeToString(b, 0, read, 0));
                n = i;
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException("Unable to load drawable data");
        }
    }
    
    public static String loadPngAsDataURI(Context context, int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        try {
            final InputStream openRawResource = context.getResources().openRawResource(n);
            final Context context2 = context = null;
            try {
                try {
                    final byte[] b = new byte[300];
                    int n2 = 0;
                    while (true) {
                        context = context2;
                        final int read = openRawResource.read(b);
                        if (read <= 0) {
                            if (openRawResource != null) {
                                openRawResource.close();
                            }
                            return sb.toString();
                        }
                        if ((n = n2) == 0) {
                            if (read < 8) {
                                context = context2;
                                context = context2;
                                final IllegalStateException ex = new IllegalStateException("Loaded drawable is improbably small");
                                context = context2;
                                throw ex;
                            }
                            n = 0;
                            while (true) {
                                context = context2;
                                if (n >= HtmlLoader.pngHeader.length) {
                                    n = 1;
                                    break;
                                }
                                context = context2;
                                if (b[n] != HtmlLoader.pngHeader[n]) {
                                    context = context2;
                                    context = context2;
                                    final IllegalStateException ex2 = new IllegalStateException("Invalid png detected");
                                    context = context2;
                                    throw ex2;
                                }
                                ++n;
                            }
                        }
                        context = context2;
                        sb.append(Base64.encodeToString(b, 0, read, 0));
                        n2 = n;
                    }
                }
                finally {
                    if (openRawResource != null) {
                        if (context != null) {
                            final InputStream inputStream = openRawResource;
                            inputStream.close();
                        }
                        else {
                            openRawResource.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final InputStream inputStream = openRawResource;
                inputStream.close();
            }
            catch (Throwable t2) {}
        }
        catch (IOException ex3) {
            throw new IllegalStateException("Unable to load png data");
        }
    }
    
    public static String loadResourceFile(final Context context, final int n, final Map<String, String> map) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(n), StandardCharsets.UTF_8));
            Object o2;
            final Object o = o2 = null;
            try {
                try {
                    o2 = o;
                    final StringBuilder sb = new StringBuilder();
                    while (true) {
                        o2 = o;
                        String s = bufferedReader.readLine();
                        if (s == null) {
                            break;
                        }
                        String str = s;
                        if (map != null) {
                            o2 = o;
                            final Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                            while (true) {
                                str = s;
                                o2 = o;
                                if (!iterator.hasNext()) {
                                    break;
                                }
                                o2 = o;
                                final Map.Entry<String, String> entry = iterator.next();
                                o2 = o;
                                s = s.replace(entry.getKey(), entry.getValue());
                            }
                        }
                        o2 = o;
                        sb.append(str);
                    }
                    o2 = o;
                    final String string = sb.toString();
                    bufferedReader.close();
                    return string;
                }
                finally {
                    if (o2 != null) {
                        final BufferedReader bufferedReader2 = bufferedReader;
                        bufferedReader2.close();
                    }
                    else {
                        bufferedReader.close();
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final BufferedReader bufferedReader2 = bufferedReader;
                bufferedReader2.close();
            }
            catch (Throwable t2) {}
        }
        catch (IOException cause) {
            throw new IllegalStateException("Unable to load error page data", cause);
        }
    }
}
