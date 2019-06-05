package org.mozilla.focus.utils;

import android.content.Context;
import com.adjust.sdk.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IOUtils {
    public static JSONObject readAsset(Context context, String str) throws IOException {
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), StandardCharsets.UTF_8));
            stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    stringBuilder.append(readLine);
                } else {
                    JSONObject jSONObject = new JSONObject(stringBuilder.toString());
                    bufferedReader.close();
                    return jSONObject;
                }
            }
        } catch (JSONException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Corrupt JSON asset (");
            stringBuilder.append(str);
            stringBuilder.append(")");
            throw new AssertionError(stringBuilder.toString(), e);
        } catch (Throwable th) {
            r3.addSuppressed(th);
        }
    }

    public static JSONArray readRawJsonArray(Context context, int i) throws IOException {
        try {
            InputStream openRawResource = context.getResources().openRawResource(i);
            byte[] bArr = new byte[openRawResource.available()];
            openRawResource.read(bArr);
            String str = new String(bArr, Constants.ENCODING);
            openRawResource.close();
            return new JSONArray(str);
        } catch (JSONException e) {
            throw new AssertionError("Corrupt JSON in readRawJsonArray", e);
        }
    }
}
