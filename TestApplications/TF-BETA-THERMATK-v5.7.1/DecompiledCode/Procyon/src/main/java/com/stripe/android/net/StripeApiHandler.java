// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.net;

import java.net.URLEncoder;
import org.json.JSONException;
import java.security.Security;
import com.stripe.android.exception.PermissionException;
import com.stripe.android.exception.RateLimitException;
import java.util.Scanner;
import java.io.InputStream;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Token;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import com.stripe.android.exception.InvalidRequestException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class StripeApiHandler
{
    private static final SSLSocketFactory SSL_SOCKET_FACTORY;
    
    static {
        SSL_SOCKET_FACTORY = new StripeSSLSocketFactory();
    }
    
    private static HttpURLConnection createGetConnection(final String s, final String s2, final RequestOptions requestOptions) throws IOException {
        final HttpURLConnection stripeConnection = createStripeConnection(formatURL(s, s2), requestOptions);
        stripeConnection.setRequestMethod("GET");
        return stripeConnection;
    }
    
    private static HttpURLConnection createPostConnection(final String s, final String s2, RequestOptions outputStream) throws IOException {
        final HttpURLConnection stripeConnection = createStripeConnection(s, outputStream);
        stripeConnection.setDoOutput(true);
        stripeConnection.setRequestMethod("POST");
        stripeConnection.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", "UTF-8"));
        OutputStream outputStream2;
        try {
            outputStream = (RequestOptions)stripeConnection.getOutputStream();
            try {
                ((OutputStream)outputStream).write(s2.getBytes("UTF-8"));
                if (outputStream != null) {
                    ((OutputStream)outputStream).close();
                }
                return stripeConnection;
            }
            finally {}
        }
        finally {
            outputStream2 = null;
        }
        if (outputStream2 != null) {
            outputStream2.close();
        }
    }
    
    static String createQuery(final Map<String, Object> map) throws UnsupportedEncodingException, InvalidRequestException {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Parameter> iterator = flattenParams(map).iterator();
        while (iterator.hasNext()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            final Parameter parameter = iterator.next();
            sb.append(urlEncodePair(parameter.key, parameter.value));
        }
        return sb.toString();
    }
    
    private static HttpURLConnection createStripeConnection(final String spec, final RequestOptions requestOptions) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(spec).openConnection();
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(80000);
        httpURLConnection.setUseCaches(false);
        for (final Map.Entry<String, String> entry : getHeaders(requestOptions).entrySet()) {
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (httpURLConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)httpURLConnection).setSSLSocketFactory(StripeApiHandler.SSL_SOCKET_FACTORY);
        }
        return httpURLConnection;
    }
    
    public static Token createToken(final Map<String, Object> map, final RequestOptions requestOptions) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        return requestToken("POST", getApiUrl(), map, requestOptions);
    }
    
    private static List<Parameter> flattenParams(final Map<String, Object> map) throws InvalidRequestException {
        return flattenParamsMap(map, null);
    }
    
    private static List<Parameter> flattenParamsList(final List<Object> list, final String s) throws InvalidRequestException {
        final LinkedList<Object> list2 = new LinkedList<Object>();
        final Iterator<Object> iterator = list.iterator();
        final String format = String.format("%s[]", s);
        if (list.isEmpty()) {
            list2.add(new Parameter(s, ""));
        }
        else {
            while (iterator.hasNext()) {
                list2.addAll(flattenParamsValue(iterator.next(), format));
            }
        }
        return (List<Parameter>)list2;
    }
    
    private static List<Parameter> flattenParamsMap(final Map<String, Object> map, final String s) throws InvalidRequestException {
        final LinkedList<Object> list = new LinkedList<Object>();
        if (map == null) {
            return (List<Parameter>)list;
        }
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final String s2 = entry.getKey();
            final Object value = entry.getValue();
            String format = s2;
            if (s != null) {
                format = String.format("%s[%s]", s, s2);
            }
            list.addAll(flattenParamsValue(value, format));
        }
        return (List<Parameter>)list;
    }
    
    private static List<Parameter> flattenParamsValue(final Object anObject, final String s) throws InvalidRequestException {
        List<Parameter> list;
        if (anObject instanceof Map) {
            list = flattenParamsMap((Map<String, Object>)anObject, s);
        }
        else if (anObject instanceof List) {
            list = flattenParamsList((List<Object>)anObject, s);
        }
        else {
            if ("".equals(anObject)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("You cannot set '");
                sb.append(s);
                sb.append("' to an empty string. We interpret empty strings as null in requests. You may set '");
                sb.append(s);
                sb.append("' to null to delete the property.");
                throw new InvalidRequestException(sb.toString(), s, null, 0, null);
            }
            if (anObject == null) {
                list = new LinkedList<Parameter>();
                list.add(new Parameter(s, ""));
            }
            else {
                final LinkedList<Parameter> list2 = new LinkedList<Parameter>();
                list2.add(new Parameter(s, anObject.toString()));
                list = list2;
            }
        }
        return list;
    }
    
    private static String formatURL(final String s, final String s2) {
        String format = s;
        if (s2 != null) {
            if (s2.isEmpty()) {
                format = s;
            }
            else {
                String s3 = "?";
                if (s.contains("?")) {
                    s3 = "&";
                }
                format = String.format("%s%s%s", s, s3, s2);
            }
        }
        return format;
    }
    
    static String getApiUrl() {
        return String.format("%s/v1/%s", "https://api.stripe.com", "tokens");
    }
    
    static Map<String, String> getHeaders(final RequestOptions requestOptions) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final String apiVersion = requestOptions.getApiVersion();
        hashMap.put("Accept-Charset", "UTF-8");
        hashMap.put("Accept", "application/json");
        int i = 0;
        hashMap.put("User-Agent", String.format("Stripe/v1 JavaBindings/%s", "3.5.0"));
        hashMap.put("Authorization", String.format("Bearer %s", requestOptions.getPublishableApiKey()));
        final String[] array = { "os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor" };
        final HashMap<String, String> hashMap2 = new HashMap<String, String>();
        while (i < array.length) {
            final String key = array[i];
            hashMap2.put(key, System.getProperty(key));
            ++i;
        }
        hashMap2.put("bindings.version", "3.5.0");
        hashMap2.put("lang", "Java");
        hashMap2.put("publisher", "Stripe");
        hashMap.put("X-Stripe-Client-User-Agent", new JSONObject((Map)hashMap2).toString());
        if (apiVersion != null) {
            hashMap.put("Stripe-Version", apiVersion);
        }
        if (requestOptions.getIdempotencyKey() != null) {
            hashMap.put("Idempotency-Key", requestOptions.getIdempotencyKey());
        }
        return hashMap;
    }
    
    private static String getResponseBody(final InputStream source) throws IOException {
        final String next = new Scanner(source, "UTF-8").useDelimiter("\\A").next();
        source.close();
        return next;
    }
    
    private static StripeResponse getStripeResponse(final String s, final String s2, final Map<String, Object> map, final RequestOptions requestOptions) throws InvalidRequestException, APIConnectionException, APIException {
        try {
            return makeURLConnectionRequest(s, s2, createQuery(map), requestOptions);
        }
        catch (UnsupportedEncodingException ex) {
            throw new InvalidRequestException("Unable to encode parameters to UTF-8. Please contact support@stripe.com for assistance.", null, null, 0, ex);
        }
    }
    
    private static void handleAPIError(final String s, final int i, final String s2) throws InvalidRequestException, AuthenticationException, CardException, APIException {
        final ErrorParser.StripeError error = ErrorParser.parseError(s);
        if (i == 429) {
            throw new RateLimitException(error.message, error.param, s2, i, null);
        }
        switch (i) {
            default: {
                throw new APIException(error.message, s2, i, null);
            }
            case 404: {
                throw new InvalidRequestException(error.message, error.param, s2, i, null);
            }
            case 403: {
                throw new PermissionException(error.message, s2, i);
            }
            case 402: {
                throw new CardException(error.message, s2, error.code, error.param, error.decline_code, error.charge, i, null);
            }
            case 401: {
                throw new AuthenticationException(error.message, s2, i);
            }
            case 400: {
                throw new InvalidRequestException(error.message, error.param, s2, i, null);
            }
        }
    }
    
    private static StripeResponse makeURLConnectionRequest(final String s, String s2, final String s3, final RequestOptions requestOptions) throws APIConnectionException {
        int n = -1;
        HttpURLConnection httpURLConnection2;
        final HttpURLConnection httpURLConnection = httpURLConnection2 = null;
        try {
            try {
                final int hashCode = s.hashCode();
                if (hashCode != 70454) {
                    if (hashCode == 2461856) {
                        httpURLConnection2 = httpURLConnection;
                        if (s.equals("POST")) {
                            n = 1;
                        }
                    }
                }
                else {
                    httpURLConnection2 = httpURLConnection;
                    if (s.equals("GET")) {
                        n = 0;
                    }
                }
                HttpURLConnection httpURLConnection3;
                if (n != 0) {
                    if (n != 1) {
                        httpURLConnection2 = httpURLConnection;
                        httpURLConnection2 = httpURLConnection;
                        final APIConnectionException ex = new APIConnectionException(String.format("Unrecognized HTTP method %s. This indicates a bug in the Stripe bindings. Please contact support@stripe.com for assistance.", s));
                        httpURLConnection2 = httpURLConnection;
                        throw ex;
                    }
                    httpURLConnection2 = httpURLConnection;
                    httpURLConnection3 = createPostConnection(s2, s3, requestOptions);
                }
                else {
                    httpURLConnection2 = httpURLConnection;
                    httpURLConnection3 = createGetConnection(s2, s3, requestOptions);
                }
                httpURLConnection2 = httpURLConnection3;
                final int responseCode = httpURLConnection3.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    httpURLConnection2 = httpURLConnection3;
                    s2 = getResponseBody(httpURLConnection3.getInputStream());
                }
                else {
                    httpURLConnection2 = httpURLConnection3;
                    s2 = getResponseBody(httpURLConnection3.getErrorStream());
                }
                httpURLConnection2 = httpURLConnection3;
                final StripeResponse stripeResponse = new StripeResponse(responseCode, s2, httpURLConnection3.getHeaderFields());
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                return stripeResponse;
            }
            finally {
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
        }
        catch (IOException ex2) {}
    }
    
    private static Token requestToken(String ex, String responseBody, final Map<String, Object> map, final RequestOptions requestOptions) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        if (requestOptions == null) {
            return null;
        }
        Boolean value = true;
    Label_0037_Outer:
        while (true) {
            String property = null;
            try {
                property = Security.getProperty("networkaddress.cache.ttl");
                final String s = "networkaddress.cache.ttl";
                final String s2 = "0";
                Security.setProperty(s, s2);
                break Label_0051;
            }
            catch (SecurityException ex2) {
                final String s3 = null;
            }
            while (true) {
                try {
                    final String s = "networkaddress.cache.ttl";
                    final String s2 = "0";
                    Security.setProperty(s, s2);
                    if (!requestOptions.getPublishableApiKey().trim().isEmpty()) {
                        try {
                            final StripeResponse stripeResponse = getStripeResponse((String)ex, responseBody, map, requestOptions);
                            final int responseCode = stripeResponse.getResponseCode();
                            responseBody = stripeResponse.getResponseBody();
                            final Map<String, List<String>> responseHeaders = stripeResponse.getResponseHeaders();
                            List<String> list;
                            if (responseHeaders == null) {
                                list = null;
                            }
                            else {
                                list = responseHeaders.get("Request-Id");
                            }
                            if (list != null && list.size() > 0) {
                                ex = (JSONException)list.get(0);
                            }
                            else {
                                ex = null;
                            }
                            if (responseCode >= 200 && responseCode < 300) {
                                return TokenParser.parseToken(responseBody);
                            }
                            handleAPIError(responseBody, responseCode, (String)ex);
                            throw null;
                        }
                        catch (JSONException ex) {
                            return null;
                        }
                        finally {
                            if (value) {
                                if (property == null) {
                                    Security.setProperty("networkaddress.cache.ttl", "-1");
                                }
                                else {
                                    Security.setProperty("networkaddress.cache.ttl", property);
                                }
                            }
                        }
                    }
                    throw new AuthenticationException("No API key provided. (HINT: set your API key using 'Stripe.apiKey = <API-KEY>'. You can generate API keys from the Stripe web interface. See https://stripe.com/api for details or email support@stripe.com if you have questions.", null, 0);
                    final Boolean value2 = false;
                    final String s3;
                    property = s3;
                    value = value2;
                    continue Label_0037_Outer;
                }
                catch (SecurityException ex3) {
                    final String s3 = property;
                    continue;
                }
                break;
            }
            break;
        }
    }
    
    private static String urlEncode(final String s) throws UnsupportedEncodingException {
        if (s == null) {
            return null;
        }
        return URLEncoder.encode(s, "UTF-8");
    }
    
    private static String urlEncodePair(final String s, final String s2) throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(s), urlEncode(s2));
    }
    
    private static final class Parameter
    {
        public final String key;
        public final String value;
        
        public Parameter(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
    }
}
