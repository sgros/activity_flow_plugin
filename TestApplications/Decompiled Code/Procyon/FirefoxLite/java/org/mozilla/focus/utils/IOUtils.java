// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.io.InputStream;
import org.json.JSONArray;
import java.io.IOException;
import org.json.JSONException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import android.content.Context;

public class IOUtils
{
    public static JSONObject readAsset(Context context, final String str) throws IOException {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), StandardCharsets.UTF_8));
            final Context context2 = context = null;
            try {
                try {
                    context = context2;
                    final StringBuilder sb = new StringBuilder();
                    while (true) {
                        context = context2;
                        final String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        context = context2;
                        sb.append(line);
                    }
                    context = context2;
                    final JSONObject jsonObject = new JSONObject(sb.toString());
                    bufferedReader.close();
                    return jsonObject;
                }
                finally {
                    if (context != null) {
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
        catch (JSONException cause) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Corrupt JSON asset (");
            sb2.append(str);
            sb2.append(")");
            throw new AssertionError(sb2.toString(), (Throwable)cause);
        }
    }
    
    public static JSONArray readRawJsonArray(final Context context, final int n) throws IOException {
        try {
            final InputStream openRawResource = context.getResources().openRawResource(n);
            final byte[] array = new byte[openRawResource.available()];
            openRawResource.read(array);
            final String s = new String(array, "UTF-8");
            openRawResource.close();
            return new JSONArray(s);
        }
        catch (JSONException cause) {
            throw new AssertionError("Corrupt JSON in readRawJsonArray", (Throwable)cause);
        }
    }
}
