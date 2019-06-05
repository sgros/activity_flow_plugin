package org.secuso.privacyfriendlynetmonitor.Assistant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Const {
    public static final String FILE_IF_LIST = "iflist";
    public static final Integer[] INCONCLUSIVE_PORT_VALUES = new Integer[]{Integer.valueOf(25), Integer.valueOf(110), Integer.valueOf(143)};
    public static final Set<Integer> INCONCUSIVE_PORTS = new HashSet(Arrays.asList(INCONCLUSIVE_PORT_VALUES));
    public static final String IS_CERTVAL = "IS_CERTVAL";
    public static final boolean IS_DEBUG = false;
    public static final String IS_DETAIL_MODE = "IS_DETAIL_MODE";
    public static final String IS_FIRST_START = "IS_FIRST_START";
    public static final String IS_HIGHLIGHTED = "IS_HIGHLIGHTED";
    public static final String IS_LOG = "IS_LOG";
    public static final String LOG_TAG = "NetMonitor";
    public static final String PREF_NAME = "PREF_NAME";
    public static final String REPORT_TTL = "REPORT_TTL";
    public static final long REPORT_TTL_DEFAULT = 10000;
    public static final String SSLLABS_URL = "https://www.ssllabs.com/ssltest/analyze.html?d=";
    public static final String STATUS_INCONCLUSIVE = "Inconclusive";
    public static final String STATUS_TLS = "Encrypted";
    public static final String STATUS_UNKNOWN = "Unknown";
    public static final String STATUS_UNSECURE = "Unencrypted";
    public static final Set<Integer> TLS_PORTS = new HashSet(Arrays.asList(TLS_PORT_VALUES));
    public static final Integer[] TLS_PORT_VALUES = new Integer[]{Integer.valueOf(993), Integer.valueOf(443), Integer.valueOf(995), Integer.valueOf(995), Integer.valueOf(614), Integer.valueOf(465), Integer.valueOf(587), Integer.valueOf(22)};
    public static final Set<Integer> UNSECURE_PORTS = new HashSet(Arrays.asList(UNSECURE_PORT_VALUES));
    public static final Integer[] UNSECURE_PORT_VALUES = new Integer[]{Integer.valueOf(21), Integer.valueOf(23), Integer.valueOf(80), Integer.valueOf(109), Integer.valueOf(137), Integer.valueOf(138), Integer.valueOf(139), Integer.valueOf(161), Integer.valueOf(992)};
}
