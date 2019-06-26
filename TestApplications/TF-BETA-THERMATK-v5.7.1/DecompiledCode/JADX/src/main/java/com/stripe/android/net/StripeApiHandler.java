package com.stripe.android.net;

import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.exception.PermissionException;
import com.stripe.android.exception.RateLimitException;
import com.stripe.android.model.Token;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONObject;

public class StripeApiHandler {
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();

    private static final class Parameter {
        public final String key;
        public final String value;

        public Parameter(String str, String str2) {
            this.key = str;
            this.value = str2;
        }
    }

    public static Token createToken(Map<String, Object> map, RequestOptions requestOptions) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        return requestToken("POST", getApiUrl(), map, requestOptions);
    }

    static String createQuery(Map<String, Object> map) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Parameter parameter : flattenParams(map)) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(urlEncodePair(parameter.key, parameter.value));
        }
        return stringBuilder.toString();
    }

    static Map<String, String> getHeaders(RequestOptions requestOptions) {
        HashMap hashMap = new HashMap();
        String apiVersion = requestOptions.getApiVersion();
        hashMap.put("Accept-Charset", "UTF-8");
        hashMap.put("Accept", "application/json");
        Object[] objArr = new Object[1];
        String str = "3.5.0";
        int i = 0;
        objArr[0] = str;
        hashMap.put("User-Agent", String.format("Stripe/v1 JavaBindings/%s", objArr));
        hashMap.put("Authorization", String.format("Bearer %s", new Object[]{requestOptions.getPublishableApiKey()}));
        String[] strArr = new String[]{"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"};
        HashMap hashMap2 = new HashMap();
        int length = strArr.length;
        while (i < length) {
            String str2 = strArr[i];
            hashMap2.put(str2, System.getProperty(str2));
            i++;
        }
        hashMap2.put("bindings.version", str);
        hashMap2.put("lang", "Java");
        hashMap2.put("publisher", "Stripe");
        hashMap.put("X-Stripe-Client-User-Agent", new JSONObject(hashMap2).toString());
        if (apiVersion != null) {
            hashMap.put("Stripe-Version", apiVersion);
        }
        if (requestOptions.getIdempotencyKey() != null) {
            hashMap.put("Idempotency-Key", requestOptions.getIdempotencyKey());
        }
        return hashMap;
    }

    static String getApiUrl() {
        return String.format("%s/v1/%s", new Object[]{"https://api.stripe.com", "tokens"});
    }

    private static String formatURL(String str, String str2) {
        if (str2 == null || str2.isEmpty()) {
            return str;
        }
        String str3 = "?";
        if (str.contains(str3)) {
            str3 = "&";
        }
        return String.format("%s%s%s", new Object[]{str, str3, str2});
    }

    private static HttpURLConnection createGetConnection(String str, String str2, RequestOptions requestOptions) throws IOException {
        HttpURLConnection createStripeConnection = createStripeConnection(formatURL(str, str2), requestOptions);
        createStripeConnection.setRequestMethod("GET");
        return createStripeConnection;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0036  */
    private static java.net.HttpURLConnection createPostConnection(java.lang.String r2, java.lang.String r3, com.stripe.android.net.RequestOptions r4) throws java.io.IOException {
        /*
        r2 = createStripeConnection(r2, r4);
        r4 = 1;
        r2.setDoOutput(r4);
        r0 = "POST";
        r2.setRequestMethod(r0);
        r4 = new java.lang.Object[r4];
        r0 = "UTF-8";
        r1 = 0;
        r4[r1] = r0;
        r1 = "application/x-www-form-urlencoded;charset=%s";
        r4 = java.lang.String.format(r1, r4);
        r1 = "Content-Type";
        r2.setRequestProperty(r1, r4);
        r4 = r2.getOutputStream();	 Catch:{ all -> 0x0032 }
        r3 = r3.getBytes(r0);	 Catch:{ all -> 0x0030 }
        r4.write(r3);	 Catch:{ all -> 0x0030 }
        if (r4 == 0) goto L_0x002f;
    L_0x002c:
        r4.close();
    L_0x002f:
        return r2;
    L_0x0030:
        r2 = move-exception;
        goto L_0x0034;
    L_0x0032:
        r2 = move-exception;
        r4 = 0;
    L_0x0034:
        if (r4 == 0) goto L_0x0039;
    L_0x0036:
        r4.close();
    L_0x0039:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stripe.android.net.StripeApiHandler.createPostConnection(java.lang.String, java.lang.String, com.stripe.android.net.RequestOptions):java.net.HttpURLConnection");
    }

    private static HttpURLConnection createStripeConnection(String str, RequestOptions requestOptions) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(80000);
        httpURLConnection.setUseCaches(false);
        for (Entry entry : getHeaders(requestOptions).entrySet()) {
            httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(SSL_SOCKET_FACTORY);
        }
        return httpURLConnection;
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002b A:{SYNTHETIC, Splitter:B:14:0x002b} */
    private static com.stripe.android.model.Token requestToken(java.lang.String r7, java.lang.String r8, java.util.Map<java.lang.String, java.lang.Object> r9, com.stripe.android.net.RequestOptions r10) throws com.stripe.android.exception.AuthenticationException, com.stripe.android.exception.InvalidRequestException, com.stripe.android.exception.APIConnectionException, com.stripe.android.exception.CardException, com.stripe.android.exception.APIException {
        /*
        r0 = "-1";
        r1 = "networkaddress.cache.ttl";
        r2 = 0;
        if (r10 != 0) goto L_0x0008;
    L_0x0007:
        return r2;
    L_0x0008:
        r3 = 1;
        r3 = java.lang.Boolean.valueOf(r3);
        r4 = 0;
        r5 = java.security.Security.getProperty(r1);	 Catch:{ SecurityException -> 0x0018 }
        r6 = "0";
        java.security.Security.setProperty(r1, r6);	 Catch:{ SecurityException -> 0x0019 }
        goto L_0x001d;
    L_0x0018:
        r5 = r2;
    L_0x0019:
        r3 = java.lang.Boolean.valueOf(r4);
    L_0x001d:
        r6 = r10.getPublishableApiKey();
        r6 = r6.trim();
        r6 = r6.isEmpty();
        if (r6 != 0) goto L_0x0099;
    L_0x002b:
        r7 = getStripeResponse(r7, r8, r9, r10);	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r8 = r7.getResponseCode();	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r9 = r7.getResponseBody();	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r7 = r7.getResponseHeaders();	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        if (r7 != 0) goto L_0x003f;
    L_0x003d:
        r7 = r2;
        goto L_0x0047;
    L_0x003f:
        r10 = "Request-Id";
        r7 = r7.get(r10);	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r7 = (java.util.List) r7;	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
    L_0x0047:
        if (r7 == 0) goto L_0x0056;
    L_0x0049:
        r10 = r7.size();	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        if (r10 <= 0) goto L_0x0056;
    L_0x004f:
        r7 = r7.get(r4);	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r7 = (java.lang.String) r7;	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        goto L_0x0057;
    L_0x0056:
        r7 = r2;
    L_0x0057:
        r10 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r8 < r10) goto L_0x0073;
    L_0x005b:
        r10 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r8 >= r10) goto L_0x0073;
    L_0x005f:
        r7 = com.stripe.android.net.TokenParser.parseToken(r9);	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        r8 = r3.booleanValue();
        if (r8 == 0) goto L_0x0072;
    L_0x0069:
        if (r5 != 0) goto L_0x006f;
    L_0x006b:
        java.security.Security.setProperty(r1, r0);
        goto L_0x0072;
    L_0x006f:
        java.security.Security.setProperty(r1, r5);
    L_0x0072:
        return r7;
    L_0x0073:
        handleAPIError(r9, r8, r7);	 Catch:{ JSONException -> 0x0088, all -> 0x0077 }
        throw r2;
    L_0x0077:
        r7 = move-exception;
        r8 = r3.booleanValue();
        if (r8 == 0) goto L_0x0087;
    L_0x007e:
        if (r5 != 0) goto L_0x0084;
    L_0x0080:
        java.security.Security.setProperty(r1, r0);
        goto L_0x0087;
    L_0x0084:
        java.security.Security.setProperty(r1, r5);
    L_0x0087:
        throw r7;
        r7 = r3.booleanValue();
        if (r7 == 0) goto L_0x0098;
    L_0x008f:
        if (r5 != 0) goto L_0x0095;
    L_0x0091:
        java.security.Security.setProperty(r1, r0);
        goto L_0x0098;
    L_0x0095:
        java.security.Security.setProperty(r1, r5);
    L_0x0098:
        return r2;
    L_0x0099:
        r7 = new com.stripe.android.exception.AuthenticationException;
        r8 = java.lang.Integer.valueOf(r4);
        r9 = "No API key provided. (HINT: set your API key using 'Stripe.apiKey = <API-KEY>'. You can generate API keys from the Stripe web interface. See https://stripe.com/api for details or email support@stripe.com if you have questions.";
        r7.<init>(r9, r2, r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.stripe.android.net.StripeApiHandler.requestToken(java.lang.String, java.lang.String, java.util.Map, com.stripe.android.net.RequestOptions):com.stripe.android.model.Token");
    }

    private static StripeResponse getStripeResponse(String str, String str2, Map<String, Object> map, RequestOptions requestOptions) throws InvalidRequestException, APIConnectionException, APIException {
        try {
            return makeURLConnectionRequest(str, str2, createQuery(map), requestOptions);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", null, null, Integer.valueOf(0), e);
        }
    }

    private static List<Parameter> flattenParams(Map<String, Object> map) throws InvalidRequestException {
        return flattenParamsMap(map, null);
    }

    private static List<Parameter> flattenParamsList(List<Object> list, String str) throws InvalidRequestException {
        LinkedList linkedList = new LinkedList();
        String format = String.format("%s[]", new Object[]{str});
        if (list.isEmpty()) {
            linkedList.add(new Parameter(str, ""));
        } else {
            for (Object flattenParamsValue : list) {
                linkedList.addAll(flattenParamsValue(flattenParamsValue, format));
            }
        }
        return linkedList;
    }

    private static List<Parameter> flattenParamsMap(Map<String, Object> map, String str) throws InvalidRequestException {
        LinkedList linkedList = new LinkedList();
        if (map == null) {
            return linkedList;
        }
        for (Entry entry : map.entrySet()) {
            String str2 = (String) entry.getKey();
            Object value = entry.getValue();
            if (str != null) {
                str2 = String.format("%s[%s]", new Object[]{str, str2});
            }
            linkedList.addAll(flattenParamsValue(value, str2));
        }
        return linkedList;
    }

    private static List<Parameter> flattenParamsValue(Object obj, String str) throws InvalidRequestException {
        if (obj instanceof Map) {
            return flattenParamsMap((Map) obj, str);
        }
        if (obj instanceof List) {
            return flattenParamsList((List) obj, str);
        }
        String str2 = "";
        if (str2.equals(obj)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You cannot set '");
            stringBuilder.append(str);
            stringBuilder.append("' to an empty string. We interpret empty strings as null in requests. You may set '");
            stringBuilder.append(str);
            stringBuilder.append("' to null to delete the property.");
            throw new InvalidRequestException(stringBuilder.toString(), str, null, Integer.valueOf(0), null);
        } else if (obj == null) {
            List<Parameter> linkedList = new LinkedList();
            linkedList.add(new Parameter(str, str2));
            return linkedList;
        } else {
            LinkedList linkedList2 = new LinkedList();
            linkedList2.add(new Parameter(str, obj.toString()));
            return linkedList2;
        }
    }

    private static void handleAPIError(String str, int i, String str2) throws InvalidRequestException, AuthenticationException, CardException, APIException {
        StripeError parseError = ErrorParser.parseError(str);
        if (i != 429) {
            switch (i) {
                case 400:
                    throw new InvalidRequestException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
                case 401:
                    throw new AuthenticationException(parseError.message, str2, Integer.valueOf(i));
                case 402:
                    throw new CardException(parseError.message, str2, parseError.code, parseError.param, parseError.decline_code, parseError.charge, Integer.valueOf(i), null);
                case 403:
                    throw new PermissionException(parseError.message, str2, Integer.valueOf(i));
                case 404:
                    throw new InvalidRequestException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
                default:
                    throw new APIException(parseError.message, str2, Integer.valueOf(i), null);
            }
        }
        throw new RateLimitException(parseError.message, parseError.param, str2, Integer.valueOf(i), null);
    }

    private static String urlEncodePair(String str, String str2) throws UnsupportedEncodingException {
        return String.format("%s=%s", new Object[]{urlEncode(str), urlEncode(str2)});
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        return str == null ? null : URLEncoder.encode(str, "UTF-8");
    }

    private static StripeResponse makeURLConnectionRequest(String str, String str2, String str3, RequestOptions requestOptions) throws APIConnectionException {
        Object obj = -1;
        HttpURLConnection httpURLConnection = null;
        try {
            HttpURLConnection createGetConnection;
            int hashCode = str.hashCode();
            if (hashCode != 70454) {
                if (hashCode == 2461856) {
                    if (str.equals("POST")) {
                        obj = 1;
                    }
                }
            } else if (str.equals("GET")) {
                obj = null;
            }
            if (obj == null) {
                createGetConnection = createGetConnection(str2, str3, requestOptions);
            } else if (obj == 1) {
                createGetConnection = createPostConnection(str2, str3, requestOptions);
            } else {
                throw new APIConnectionException(String.format("Unrecognized HTTP method %s. This indicates a bug in the Stripe bindings. Please contact support@stripe.com for assistance.", new Object[]{str}));
            }
            httpURLConnection = createGetConnection;
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode < Callback.DEFAULT_DRAG_ANIMATION_DURATION || responseCode >= 300) {
                str2 = getResponseBody(httpURLConnection.getErrorStream());
            } else {
                str2 = getResponseBody(httpURLConnection.getInputStream());
            }
            StripeResponse stripeResponse = new StripeResponse(responseCode, str2, httpURLConnection.getHeaderFields());
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            return stripeResponse;
        } catch (IOException e) {
            throw new APIConnectionException(String.format("IOException during API request to Stripe (%s): %s Please check your internet connection and try again. If this problem persists, you should check Stripe's service status at https://twitter.com/stripestatus, or let us know at support@stripe.com.", new Object[]{getApiUrl(), e.getMessage()}), e);
        } catch (Throwable th) {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private static String getResponseBody(InputStream inputStream) throws IOException {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }
}
