// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.tls;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;
import java.util.Locale;
import java.util.Iterator;
import java.security.cert.CertificateParsingException;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;

public final class OkHostnameVerifier implements HostnameVerifier
{
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE;
    
    static {
        INSTANCE = new OkHostnameVerifier();
    }
    
    private OkHostnameVerifier() {
    }
    
    public static List<String> allSubjectAltNames(final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        final List<String> subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
        final ArrayList list = new ArrayList<Object>(subjectAltNames.size() + subjectAltNames2.size());
        list.addAll((Collection<?>)subjectAltNames);
        list.addAll((Collection<?>)subjectAltNames2);
        return (List<String>)list;
    }
    
    private static List<String> getSubjectAltNames(final X509Certificate x509Certificate, final int n) {
        final ArrayList<String> list = new ArrayList<String>();
        try {
            final Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            List<String> list2;
            if (subjectAlternativeNames == null) {
                list2 = Collections.emptyList();
            }
            else {
                final Iterator<List<Integer>> iterator = subjectAlternativeNames.iterator();
                while (true) {
                    list2 = list;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final List<Integer> list3 = iterator.next();
                    if (list3 == null || list3.size() < 2) {
                        continue;
                    }
                    final Integer n2 = list3.get(0);
                    if (n2 == null || n2 != n) {
                        continue;
                    }
                    final String s = (String)list3.get(1);
                    if (s == null) {
                        continue;
                    }
                    list.add(s);
                }
            }
            return list2;
        }
        catch (CertificateParsingException ex) {
            return Collections.emptyList();
        }
    }
    
    private boolean verifyHostname(String s, final String str) {
        boolean equals;
        final boolean b = equals = false;
        if (s != null) {
            equals = b;
            if (s.length() != 0) {
                equals = b;
                if (!s.startsWith(".")) {
                    if (s.endsWith("..")) {
                        equals = b;
                    }
                    else {
                        equals = b;
                        if (str != null) {
                            equals = b;
                            if (str.length() != 0) {
                                equals = b;
                                if (!str.startsWith(".")) {
                                    equals = b;
                                    if (!str.endsWith("..")) {
                                        String string = s;
                                        if (!s.endsWith(".")) {
                                            string = s + '.';
                                        }
                                        s = str;
                                        if (!str.endsWith(".")) {
                                            s = str + '.';
                                        }
                                        s = s.toLowerCase(Locale.US);
                                        if (!s.contains("*")) {
                                            equals = string.equals(s);
                                        }
                                        else {
                                            equals = b;
                                            if (s.startsWith("*.")) {
                                                equals = b;
                                                if (s.indexOf(42, 1) == -1) {
                                                    equals = b;
                                                    if (string.length() >= s.length()) {
                                                        equals = b;
                                                        if (!"*.".equals(s)) {
                                                            s = s.substring(1);
                                                            equals = b;
                                                            if (string.endsWith(s)) {
                                                                final int n = string.length() - s.length();
                                                                if (n > 0) {
                                                                    equals = b;
                                                                    if (string.lastIndexOf(46, n - 1) != -1) {
                                                                        return equals;
                                                                    }
                                                                }
                                                                equals = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return equals;
    }
    
    private boolean verifyHostname(String lowerCase, final X509Certificate x509Certificate) {
        lowerCase = lowerCase.toLowerCase(Locale.US);
        boolean b = false;
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 2);
        for (int i = 0; i < subjectAltNames.size(); ++i) {
            b = true;
            if (this.verifyHostname(lowerCase, subjectAltNames.get(i))) {
                return true;
            }
        }
        if (!b) {
            final String mostSpecific = new DistinguishedNameParser(x509Certificate.getSubjectX500Principal()).findMostSpecific("cn");
            if (mostSpecific != null) {
                return this.verifyHostname(lowerCase, mostSpecific);
            }
        }
        return false;
    }
    
    private boolean verifyIpAddress(final String s, final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        for (int i = 0; i < subjectAltNames.size(); ++i) {
            if (s.equalsIgnoreCase(subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public boolean verify(final String s, final X509Certificate x509Certificate) {
        boolean b;
        if (Util.verifyAsIpAddress(s)) {
            b = this.verifyIpAddress(s, x509Certificate);
        }
        else {
            b = this.verifyHostname(s, x509Certificate);
        }
        return b;
    }
    
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        try {
            return this.verify(s, (X509Certificate)sslSession.getPeerCertificates()[0]);
        }
        catch (SSLException ex) {
            return false;
        }
    }
}
