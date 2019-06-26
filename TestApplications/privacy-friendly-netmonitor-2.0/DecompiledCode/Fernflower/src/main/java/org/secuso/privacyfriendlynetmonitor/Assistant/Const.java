package org.secuso.privacyfriendlynetmonitor.Assistant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Const {
   String FILE_IF_LIST = "iflist";
   Integer[] INCONCLUSIVE_PORT_VALUES = new Integer[]{25, 110, 143};
   Set INCONCUSIVE_PORTS = new HashSet(Arrays.asList(INCONCLUSIVE_PORT_VALUES));
   String IS_CERTVAL = "IS_CERTVAL";
   boolean IS_DEBUG = false;
   String IS_DETAIL_MODE = "IS_DETAIL_MODE";
   String IS_FIRST_START = "IS_FIRST_START";
   String IS_HIGHLIGHTED = "IS_HIGHLIGHTED";
   String IS_LOG = "IS_LOG";
   String LOG_TAG = "NetMonitor";
   String PREF_NAME = "PREF_NAME";
   String REPORT_TTL = "REPORT_TTL";
   long REPORT_TTL_DEFAULT = 10000L;
   String SSLLABS_URL = "https://www.ssllabs.com/ssltest/analyze.html?d=";
   String STATUS_INCONCLUSIVE = "Inconclusive";
   String STATUS_TLS = "Encrypted";
   String STATUS_UNKNOWN = "Unknown";
   String STATUS_UNSECURE = "Unencrypted";
   Set TLS_PORTS = new HashSet(Arrays.asList(TLS_PORT_VALUES));
   Integer[] TLS_PORT_VALUES = new Integer[]{993, 443, 995, 995, 614, 465, 587, 22};
   Set UNSECURE_PORTS = new HashSet(Arrays.asList(UNSECURE_PORT_VALUES));
   Integer[] UNSECURE_PORT_VALUES = new Integer[]{21, 23, 80, 109, 137, 138, 139, 161, 992};
}
