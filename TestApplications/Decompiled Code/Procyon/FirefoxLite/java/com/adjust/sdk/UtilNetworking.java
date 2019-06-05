// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import android.net.Uri$Builder;
import android.net.Uri;
import java.util.Map;

public class UtilNetworking
{
    private static String userAgent;
    
    private static Uri buildUri(final String s, final Map<String, String> map) {
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("https");
        uri$Builder.authority("app.adjust.com");
        uri$Builder.appendPath(s);
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            uri$Builder.appendQueryParameter((String)entry.getKey(), (String)entry.getValue());
        }
        uri$Builder.appendQueryParameter("sent_at", Util.dateFormatter.format(System.currentTimeMillis()));
        return uri$Builder.build();
    }
    
    public static ResponseData createGETHttpsURLConnection(final ActivityPackage activityPackage) throws Exception {
        try {
            final HttpsURLConnection httpsURLConnection = AdjustFactory.getHttpsURLConnection(new URL(buildUri(activityPackage.getPath(), new HashMap<String, String>(activityPackage.getParameters())).toString()));
            setDefaultHttpsUrlConnectionProperties(httpsURLConnection, activityPackage.getClientSdk());
            httpsURLConnection.setRequestMethod("GET");
            return readHttpResponse(httpsURLConnection, activityPackage);
        }
        catch (Exception ex) {
            throw ex;
        }
    }
    
    public static ResponseData createPOSTHttpsURLConnection(final String p0, final ActivityPackage p1, final int p2) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3       
        //     2: aconst_null    
        //     3: astore          4
        //     5: aload           4
        //     7: astore          5
        //     9: new             Ljava/net/URL;
        //    12: astore          6
        //    14: aload           4
        //    16: astore          5
        //    18: aload           6
        //    20: aload_0        
        //    21: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    24: aload           4
        //    26: astore          5
        //    28: aload           6
        //    30: invokestatic    com/adjust/sdk/AdjustFactory.getHttpsURLConnection:(Ljava/net/URL;)Ljavax/net/ssl/HttpsURLConnection;
        //    33: astore          7
        //    35: aload           4
        //    37: astore          5
        //    39: new             Ljava/util/HashMap;
        //    42: astore          6
        //    44: aload           4
        //    46: astore          5
        //    48: aload           6
        //    50: aload_1        
        //    51: invokevirtual   com/adjust/sdk/ActivityPackage.getParameters:()Ljava/util/Map;
        //    54: invokespecial   java/util/HashMap.<init>:(Ljava/util/Map;)V
        //    57: aload           4
        //    59: astore          5
        //    61: aload           7
        //    63: aload_1        
        //    64: invokevirtual   com/adjust/sdk/ActivityPackage.getClientSdk:()Ljava/lang/String;
        //    67: invokestatic    com/adjust/sdk/UtilNetworking.setDefaultHttpsUrlConnectionProperties:(Ljavax/net/ssl/HttpsURLConnection;Ljava/lang/String;)V
        //    70: aload           4
        //    72: astore          5
        //    74: aload           7
        //    76: ldc             "POST"
        //    78: invokevirtual   javax/net/ssl/HttpsURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //    81: aload           4
        //    83: astore          5
        //    85: aload           7
        //    87: iconst_0       
        //    88: invokevirtual   javax/net/ssl/HttpsURLConnection.setUseCaches:(Z)V
        //    91: aload           4
        //    93: astore          5
        //    95: aload           7
        //    97: iconst_1       
        //    98: invokevirtual   javax/net/ssl/HttpsURLConnection.setDoInput:(Z)V
        //   101: aload           4
        //   103: astore          5
        //   105: aload           7
        //   107: iconst_1       
        //   108: invokevirtual   javax/net/ssl/HttpsURLConnection.setDoOutput:(Z)V
        //   111: aload           4
        //   113: astore          5
        //   115: new             Ljava/io/DataOutputStream;
        //   118: astore_0       
        //   119: aload           4
        //   121: astore          5
        //   123: aload_0        
        //   124: aload           7
        //   126: invokevirtual   javax/net/ssl/HttpsURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   129: invokespecial   java/io/DataOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   132: aload_0        
        //   133: aload           6
        //   135: iload_2        
        //   136: invokestatic    com/adjust/sdk/UtilNetworking.getPostDataString:(Ljava/util/Map;I)Ljava/lang/String;
        //   139: invokevirtual   java/io/DataOutputStream.writeBytes:(Ljava/lang/String;)V
        //   142: aload           7
        //   144: aload_1        
        //   145: invokestatic    com/adjust/sdk/UtilNetworking.readHttpResponse:(Ljavax/net/ssl/HttpsURLConnection;Lcom/adjust/sdk/ActivityPackage;)Lcom/adjust/sdk/ResponseData;
        //   148: astore_1       
        //   149: aload_0        
        //   150: invokevirtual   java/io/DataOutputStream.flush:()V
        //   153: aload_0        
        //   154: invokevirtual   java/io/DataOutputStream.close:()V
        //   157: aload_1        
        //   158: areturn        
        //   159: astore_1       
        //   160: aload_0        
        //   161: astore          5
        //   163: aload_1        
        //   164: astore_0       
        //   165: goto            185
        //   168: astore_1       
        //   169: aload_0        
        //   170: astore          5
        //   172: goto            183
        //   175: astore_0       
        //   176: goto            185
        //   179: astore_1       
        //   180: aload_3        
        //   181: astore          5
        //   183: aload_1        
        //   184: athrow         
        //   185: aload           5
        //   187: ifnull          200
        //   190: aload           5
        //   192: invokevirtual   java/io/DataOutputStream.flush:()V
        //   195: aload           5
        //   197: invokevirtual   java/io/DataOutputStream.close:()V
        //   200: aload_0        
        //   201: athrow         
        //   202: astore_0       
        //   203: goto            157
        //   206: astore_1       
        //   207: goto            200
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  9      14     179    183    Ljava/lang/Exception;
        //  9      14     175    179    Any
        //  18     24     179    183    Ljava/lang/Exception;
        //  18     24     175    179    Any
        //  28     35     179    183    Ljava/lang/Exception;
        //  28     35     175    179    Any
        //  39     44     179    183    Ljava/lang/Exception;
        //  39     44     175    179    Any
        //  48     57     179    183    Ljava/lang/Exception;
        //  48     57     175    179    Any
        //  61     70     179    183    Ljava/lang/Exception;
        //  61     70     175    179    Any
        //  74     81     179    183    Ljava/lang/Exception;
        //  74     81     175    179    Any
        //  85     91     179    183    Ljava/lang/Exception;
        //  85     91     175    179    Any
        //  95     101    179    183    Ljava/lang/Exception;
        //  95     101    175    179    Any
        //  105    111    179    183    Ljava/lang/Exception;
        //  105    111    175    179    Any
        //  115    119    179    183    Ljava/lang/Exception;
        //  115    119    175    179    Any
        //  123    132    179    183    Ljava/lang/Exception;
        //  123    132    175    179    Any
        //  132    149    168    175    Ljava/lang/Exception;
        //  132    149    159    168    Any
        //  149    157    202    206    Ljava/lang/Exception;
        //  183    185    175    179    Any
        //  190    200    206    210    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0157:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static ILogger getLogger() {
        return AdjustFactory.getLogger();
    }
    
    private static String getPostDataString(final Map<String, String> map, final int i) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final String encode = URLEncoder.encode(entry.getKey(), "UTF-8");
            final String s = entry.getValue();
            String encode2;
            if (s != null) {
                encode2 = URLEncoder.encode(s, "UTF-8");
            }
            else {
                encode2 = "";
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(encode);
            sb.append("=");
            sb.append(encode2);
        }
        final String format = Util.dateFormatter.format(System.currentTimeMillis());
        sb.append("&");
        sb.append(URLEncoder.encode("sent_at", "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(format, "UTF-8"));
        if (i > 0) {
            sb.append("&");
            sb.append(URLEncoder.encode("queue_size", "UTF-8"));
            sb.append("=");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(i);
            sb.append(URLEncoder.encode(sb2.toString(), "UTF-8"));
        }
        return sb.toString();
    }
    
    private static ResponseData readHttpResponse(HttpsURLConnection format, final ActivityPackage activityPackage) throws Exception {
        final StringBuffer sb = new StringBuffer();
        final ILogger logger = getLogger();
        final ResponseData buildResponseData = ResponseData.buildResponseData(activityPackage);
        try {
            try {
                ((URLConnection)format).connect();
                final Integer value = ((HttpURLConnection)format).getResponseCode();
                InputStream in;
                if (value >= 400) {
                    in = ((HttpURLConnection)format).getErrorStream();
                }
                else {
                    in = ((URLConnection)format).getInputStream();
                }
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                if (format != null) {
                    ((HttpURLConnection)format).disconnect();
                }
                final String string = sb.toString();
                logger.verbose("Response: %s", string);
                if (string == null || string.length() == 0) {
                    return buildResponseData;
                }
                try {
                    format = (JSONException)new JSONObject(string);
                }
                catch (JSONException format) {
                    format = (JSONException)String.format("Failed to parse json response. (%s)", format.getMessage());
                    logger.error((String)format, new Object[0]);
                    buildResponseData.message = (String)format;
                    format = null;
                }
                if (format == null) {
                    return buildResponseData;
                }
                buildResponseData.jsonResponse = (JSONObject)format;
                final Object optString = ((JSONObject)format).optString("message", (String)null);
                buildResponseData.message = (String)optString;
                buildResponseData.timestamp = ((JSONObject)format).optString("timestamp", (String)null);
                buildResponseData.adid = ((JSONObject)format).optString("adid", (String)null);
                if ((format = (JSONException)optString) == null) {
                    format = (JSONException)"No message found";
                }
                if (value != null && value == 200) {
                    logger.info("%s", format);
                    buildResponseData.success = true;
                }
                else {
                    logger.error("%s", format);
                }
                return buildResponseData;
            }
            finally {
                if (format != null) {
                    ((HttpURLConnection)format).disconnect();
                }
            }
        }
        catch (Exception ex) {}
    }
    
    private static void setDefaultHttpsUrlConnectionProperties(final HttpsURLConnection httpsURLConnection, final String value) {
        httpsURLConnection.setRequestProperty("Client-SDK", value);
        httpsURLConnection.setConnectTimeout(60000);
        httpsURLConnection.setReadTimeout(60000);
        if (UtilNetworking.userAgent != null) {
            httpsURLConnection.setRequestProperty("User-Agent", UtilNetworking.userAgent);
        }
    }
    
    public static void setUserAgent(final String userAgent) {
        UtilNetworking.userAgent = userAgent;
    }
}
