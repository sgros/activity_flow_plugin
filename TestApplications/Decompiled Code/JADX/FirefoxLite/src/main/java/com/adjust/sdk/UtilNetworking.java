package com.adjust.sdk;

import android.net.Uri;
import android.net.Uri.Builder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class UtilNetworking {
    private static String userAgent;

    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }

    public static void setUserAgent(String str) {
        userAgent = str;
    }

    public static ResponseData createPOSTHttpsURLConnection(String str, ActivityPackage activityPackage, int i) throws Exception {
        Exception e;
        Throwable th;
        DataOutputStream dataOutputStream = null;
        try {
            HttpsURLConnection httpsURLConnection = AdjustFactory.getHttpsURLConnection(new URL(str));
            HashMap hashMap = new HashMap(activityPackage.getParameters());
            setDefaultHttpsUrlConnectionProperties(httpsURLConnection, activityPackage.getClientSdk());
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            DataOutputStream dataOutputStream2 = new DataOutputStream(httpsURLConnection.getOutputStream());
            try {
                dataOutputStream2.writeBytes(getPostDataString(hashMap, i));
                ResponseData readHttpResponse = readHttpResponse(httpsURLConnection, activityPackage);
                try {
                    dataOutputStream2.flush();
                    dataOutputStream2.close();
                } catch (Exception unused) {
                }
                return readHttpResponse;
            } catch (Exception e2) {
                e = e2;
                dataOutputStream = dataOutputStream2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                dataOutputStream = dataOutputStream2;
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.flush();
                        dataOutputStream.close();
                    } catch (Exception unused2) {
                    }
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            throw e;
        }
    }

    public static ResponseData createGETHttpsURLConnection(ActivityPackage activityPackage) throws Exception {
        try {
            HttpsURLConnection httpsURLConnection = AdjustFactory.getHttpsURLConnection(new URL(buildUri(activityPackage.getPath(), new HashMap(activityPackage.getParameters())).toString()));
            setDefaultHttpsUrlConnectionProperties(httpsURLConnection, activityPackage.getClientSdk());
            httpsURLConnection.setRequestMethod("GET");
            return readHttpResponse(httpsURLConnection, activityPackage);
        } catch (Exception e) {
            throw e;
        }
    }

    private static ResponseData readHttpResponse(HttpsURLConnection httpsURLConnection, ActivityPackage activityPackage) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        ILogger logger = getLogger();
        ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        try {
            InputStream errorStream;
            httpsURLConnection.connect();
            Integer valueOf = Integer.valueOf(httpsURLConnection.getResponseCode());
            if (valueOf.intValue() >= 400) {
                errorStream = httpsURLConnection.getErrorStream();
            } else {
                errorStream = httpsURLConnection.getInputStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
            }
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            String stringBuffer2 = stringBuffer.toString();
            logger.verbose("Response: %s", stringBuffer2);
            if (stringBuffer2 == null || stringBuffer2.length() == 0) {
                return buildResponseData;
            }
            JSONObject jSONObject;
            try {
                jSONObject = new JSONObject(stringBuffer2);
            } catch (JSONException e) {
                stringBuffer2 = String.format("Failed to parse json response. (%s)", new Object[]{e.getMessage()});
                logger.error(stringBuffer2, new Object[0]);
                buildResponseData.message = stringBuffer2;
                jSONObject = null;
            }
            if (jSONObject == null) {
                return buildResponseData;
            }
            buildResponseData.jsonResponse = jSONObject;
            stringBuffer2 = jSONObject.optString("message", null);
            buildResponseData.message = stringBuffer2;
            buildResponseData.timestamp = jSONObject.optString("timestamp", null);
            buildResponseData.adid = jSONObject.optString("adid", null);
            if (stringBuffer2 == null) {
                stringBuffer2 = "No message found";
            }
            if (valueOf == null || valueOf.intValue() != 200) {
                logger.error("%s", stringBuffer2);
            } else {
                logger.info("%s", stringBuffer2);
                buildResponseData.success = true;
            }
            return buildResponseData;
        } catch (Exception e2) {
            logger.error("Failed to read response. (%s)", e2.getMessage());
            throw e2;
        } catch (Throwable th) {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
    }

    private static String getPostDataString(Map<String, String> map, int i) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : map.entrySet()) {
            String encode = URLEncoder.encode((String) entry.getKey(), Constants.ENCODING);
            String str = (String) entry.getValue();
            str = str != null ? URLEncoder.encode(str, Constants.ENCODING) : "";
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(encode);
            stringBuilder.append("=");
            stringBuilder.append(str);
        }
        String format = Util.dateFormatter.format(Long.valueOf(System.currentTimeMillis()));
        stringBuilder.append("&");
        stringBuilder.append(URLEncoder.encode("sent_at", Constants.ENCODING));
        stringBuilder.append("=");
        stringBuilder.append(URLEncoder.encode(format, Constants.ENCODING));
        if (i > 0) {
            stringBuilder.append("&");
            stringBuilder.append(URLEncoder.encode("queue_size", Constants.ENCODING));
            stringBuilder.append("=");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(i);
            stringBuilder.append(URLEncoder.encode(stringBuilder2.toString(), Constants.ENCODING));
        }
        return stringBuilder.toString();
    }

    private static void setDefaultHttpsUrlConnectionProperties(HttpsURLConnection httpsURLConnection, String str) {
        httpsURLConnection.setRequestProperty("Client-SDK", str);
        httpsURLConnection.setConnectTimeout(60000);
        httpsURLConnection.setReadTimeout(60000);
        if (userAgent != null) {
            httpsURLConnection.setRequestProperty("User-Agent", userAgent);
        }
    }

    private static Uri buildUri(String str, Map<String, String> map) {
        Builder builder = new Builder();
        builder.scheme(Constants.SCHEME);
        builder.authority(Constants.AUTHORITY);
        builder.appendPath(str);
        for (Entry entry : map.entrySet()) {
            builder.appendQueryParameter((String) entry.getKey(), (String) entry.getValue());
        }
        String str2 = "sent_at";
        builder.appendQueryParameter(str2, Util.dateFormatter.format(Long.valueOf(System.currentTimeMillis())));
        return builder.build();
    }
}
