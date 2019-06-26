// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Handler;
import java.util.Collections;
import java.util.HashMap;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.SlidingPercentile;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.EventDispatcher;
import android.content.Context;
import com.google.android.exoplayer2.util.Clock;
import java.util.Map;

public final class DefaultBandwidthMeter implements BandwidthMeter, TransferListener
{
    public static final Map<String, int[]> DEFAULT_INITIAL_BITRATE_COUNTRY_GROUPS;
    public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_2G;
    public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_3G;
    public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_4G;
    public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI;
    private long bitrateEstimate;
    private final Clock clock;
    private final Context context;
    private final EventDispatcher<EventListener> eventDispatcher;
    private final SparseArray<Long> initialBitrateEstimates;
    private long lastReportedBitrateEstimate;
    private int networkType;
    private int networkTypeOverride;
    private boolean networkTypeOverrideSet;
    private long sampleBytesTransferred;
    private long sampleStartTimeMs;
    private final SlidingPercentile slidingPercentile;
    private int streamCount;
    private long totalBytesTransferred;
    private long totalElapsedTimeMs;
    
    static {
        DEFAULT_INITIAL_BITRATE_COUNTRY_GROUPS = createInitialBitrateCountryGroupAssignment();
        DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI = new long[] { 5700000L, 3400000L, 1900000L, 1000000L, 400000L };
        DEFAULT_INITIAL_BITRATE_ESTIMATES_2G = new long[] { 169000L, 129000L, 114000L, 102000L, 87000L };
        DEFAULT_INITIAL_BITRATE_ESTIMATES_3G = new long[] { 2100000L, 1300000L, 950000L, 700000L, 400000L };
        DEFAULT_INITIAL_BITRATE_ESTIMATES_4G = new long[] { 6900000L, 4300000L, 2700000L, 1600000L, 450000L };
    }
    
    @Deprecated
    public DefaultBandwidthMeter() {
        this(null, (SparseArray<Long>)new SparseArray(), 2000, Clock.DEFAULT, false);
    }
    
    private DefaultBandwidthMeter(final Context context, final SparseArray<Long> initialBitrateEstimates, int networkType, final Clock clock, final boolean b) {
        Context applicationContext;
        if (context == null) {
            applicationContext = null;
        }
        else {
            applicationContext = context.getApplicationContext();
        }
        this.context = applicationContext;
        this.initialBitrateEstimates = initialBitrateEstimates;
        this.eventDispatcher = new EventDispatcher<EventListener>();
        this.slidingPercentile = new SlidingPercentile(networkType);
        this.clock = clock;
        if (context == null) {
            networkType = 0;
        }
        else {
            networkType = Util.getNetworkType(context);
        }
        this.networkType = networkType;
        this.bitrateEstimate = this.getInitialBitrateEstimateForNetworkType(this.networkType);
        if (context != null && b) {
            ConnectivityActionReceiver.getInstance(context).register(this);
        }
    }
    
    private static Map<String, int[]> createInitialBitrateCountryGroupAssignment() {
        final HashMap<String, int[]> m = new HashMap<String, int[]>();
        m.put("AD", new int[] { 1, 0, 0, 0 });
        m.put("AE", new int[] { 1, 3, 4, 4 });
        m.put("AF", new int[] { 4, 4, 3, 2 });
        m.put("AG", new int[] { 3, 2, 1, 2 });
        m.put("AI", new int[] { 1, 0, 0, 2 });
        m.put("AL", new int[] { 1, 1, 1, 1 });
        m.put("AM", new int[] { 2, 2, 4, 3 });
        m.put("AO", new int[] { 2, 4, 2, 0 });
        m.put("AR", new int[] { 2, 3, 2, 3 });
        m.put("AS", new int[] { 3, 4, 4, 1 });
        m.put("AT", new int[] { 0, 1, 0, 0 });
        m.put("AU", new int[] { 0, 3, 0, 0 });
        m.put("AW", new int[] { 1, 1, 0, 4 });
        m.put("AX", new int[] { 0, 1, 0, 0 });
        m.put("AZ", new int[] { 3, 3, 2, 2 });
        m.put("BA", new int[] { 1, 1, 1, 2 });
        m.put("BB", new int[] { 0, 1, 0, 0 });
        m.put("BD", new int[] { 2, 1, 3, 2 });
        m.put("BE", new int[] { 0, 0, 0, 0 });
        m.put("BF", new int[] { 4, 4, 4, 1 });
        m.put("BG", new int[] { 0, 0, 0, 1 });
        m.put("BH", new int[] { 2, 1, 3, 4 });
        m.put("BI", new int[] { 4, 3, 4, 4 });
        m.put("BJ", new int[] { 4, 3, 4, 3 });
        m.put("BL", new int[] { 1, 0, 1, 2 });
        m.put("BM", new int[] { 1, 0, 0, 0 });
        m.put("BN", new int[] { 4, 3, 3, 3 });
        m.put("BO", new int[] { 2, 2, 1, 2 });
        m.put("BQ", new int[] { 1, 1, 2, 4 });
        m.put("BR", new int[] { 2, 3, 2, 2 });
        m.put("BS", new int[] { 1, 1, 0, 2 });
        m.put("BT", new int[] { 3, 0, 2, 1 });
        m.put("BW", new int[] { 4, 4, 2, 3 });
        m.put("BY", new int[] { 1, 1, 1, 1 });
        m.put("BZ", new int[] { 2, 3, 3, 1 });
        m.put("CA", new int[] { 0, 2, 2, 3 });
        m.put("CD", new int[] { 4, 4, 2, 1 });
        m.put("CF", new int[] { 4, 4, 3, 3 });
        m.put("CG", new int[] { 4, 4, 4, 4 });
        m.put("CH", new int[] { 0, 0, 0, 0 });
        m.put("CI", new int[] { 4, 4, 4, 4 });
        m.put("CK", new int[] { 2, 4, 2, 0 });
        m.put("CL", new int[] { 2, 2, 2, 3 });
        m.put("CM", new int[] { 3, 4, 3, 1 });
        m.put("CN", new int[] { 2, 0, 1, 2 });
        m.put("CO", new int[] { 2, 3, 2, 1 });
        m.put("CR", new int[] { 2, 2, 4, 4 });
        m.put("CU", new int[] { 4, 4, 4, 1 });
        m.put("CV", new int[] { 2, 2, 2, 4 });
        m.put("CW", new int[] { 1, 1, 0, 0 });
        m.put("CX", new int[] { 1, 2, 2, 2 });
        m.put("CY", new int[] { 1, 1, 0, 0 });
        m.put("CZ", new int[] { 0, 1, 0, 0 });
        m.put("DE", new int[] { 0, 2, 2, 2 });
        m.put("DJ", new int[] { 3, 4, 4, 0 });
        m.put("DK", new int[] { 0, 0, 0, 0 });
        m.put("DM", new int[] { 2, 0, 3, 4 });
        m.put("DO", new int[] { 3, 3, 4, 4 });
        m.put("DZ", new int[] { 3, 3, 4, 4 });
        m.put("EC", new int[] { 2, 3, 3, 1 });
        m.put("EE", new int[] { 0, 0, 0, 0 });
        m.put("EG", new int[] { 3, 3, 1, 1 });
        m.put("EH", new int[] { 2, 0, 2, 3 });
        m.put("ER", new int[] { 4, 2, 2, 2 });
        m.put("ES", new int[] { 0, 0, 1, 1 });
        m.put("ET", new int[] { 4, 4, 4, 0 });
        m.put("FI", new int[] { 0, 0, 1, 0 });
        m.put("FJ", new int[] { 3, 2, 3, 3 });
        m.put("FK", new int[] { 3, 4, 2, 1 });
        m.put("FM", new int[] { 4, 2, 4, 0 });
        m.put("FO", new int[] { 0, 0, 0, 1 });
        m.put("FR", new int[] { 1, 0, 2, 1 });
        m.put("GA", new int[] { 3, 3, 2, 1 });
        m.put("GB", new int[] { 0, 1, 3, 2 });
        m.put("GD", new int[] { 2, 0, 3, 0 });
        m.put("GE", new int[] { 1, 1, 0, 3 });
        m.put("GF", new int[] { 1, 2, 4, 4 });
        m.put("GG", new int[] { 0, 1, 0, 0 });
        m.put("GH", new int[] { 3, 2, 2, 2 });
        m.put("GI", new int[] { 0, 0, 0, 1 });
        m.put("GL", new int[] { 2, 4, 1, 4 });
        m.put("GM", new int[] { 4, 3, 3, 0 });
        m.put("GN", new int[] { 4, 4, 3, 4 });
        m.put("GP", new int[] { 2, 2, 1, 3 });
        m.put("GQ", new int[] { 4, 4, 3, 1 });
        m.put("GR", new int[] { 1, 1, 0, 1 });
        m.put("GT", new int[] { 3, 2, 3, 4 });
        m.put("GU", new int[] { 1, 0, 4, 4 });
        m.put("GW", new int[] { 4, 4, 4, 0 });
        m.put("GY", new int[] { 3, 4, 1, 0 });
        m.put("HK", new int[] { 0, 2, 3, 4 });
        m.put("HN", new int[] { 3, 3, 2, 2 });
        m.put("HR", new int[] { 1, 0, 0, 2 });
        m.put("HT", new int[] { 3, 3, 3, 3 });
        m.put("HU", new int[] { 0, 0, 1, 0 });
        m.put("ID", new int[] { 2, 3, 3, 4 });
        m.put("IE", new int[] { 0, 0, 1, 1 });
        m.put("IL", new int[] { 0, 1, 1, 3 });
        m.put("IM", new int[] { 0, 1, 0, 1 });
        m.put("IN", new int[] { 2, 3, 3, 4 });
        m.put("IO", new int[] { 4, 2, 2, 2 });
        m.put("IQ", new int[] { 3, 3, 4, 3 });
        m.put("IR", new int[] { 3, 2, 4, 4 });
        m.put("IS", new int[] { 0, 0, 0, 0 });
        m.put("IT", new int[] { 1, 0, 1, 3 });
        m.put("JE", new int[] { 0, 0, 0, 1 });
        m.put("JM", new int[] { 3, 3, 3, 2 });
        m.put("JO", new int[] { 1, 1, 1, 2 });
        m.put("JP", new int[] { 0, 1, 1, 2 });
        m.put("KE", new int[] { 3, 3, 3, 3 });
        m.put("KG", new int[] { 2, 2, 3, 3 });
        m.put("KH", new int[] { 1, 0, 4, 4 });
        m.put("KI", new int[] { 4, 4, 4, 4 });
        m.put("KM", new int[] { 4, 4, 2, 2 });
        m.put("KN", new int[] { 1, 0, 1, 3 });
        m.put("KP", new int[] { 1, 2, 2, 2 });
        m.put("KR", new int[] { 0, 4, 0, 2 });
        m.put("KW", new int[] { 1, 2, 1, 2 });
        m.put("KY", new int[] { 1, 1, 0, 2 });
        m.put("KZ", new int[] { 1, 2, 2, 3 });
        m.put("LA", new int[] { 3, 2, 2, 2 });
        m.put("LB", new int[] { 3, 2, 0, 0 });
        m.put("LC", new int[] { 2, 2, 1, 0 });
        m.put("LI", new int[] { 0, 0, 1, 2 });
        m.put("LK", new int[] { 1, 1, 2, 2 });
        m.put("LR", new int[] { 3, 4, 3, 1 });
        m.put("LS", new int[] { 3, 3, 2, 0 });
        m.put("LT", new int[] { 0, 0, 0, 1 });
        m.put("LU", new int[] { 0, 0, 1, 0 });
        m.put("LV", new int[] { 0, 0, 0, 0 });
        m.put("LY", new int[] { 4, 4, 4, 4 });
        m.put("MA", new int[] { 2, 1, 2, 2 });
        m.put("MC", new int[] { 1, 0, 1, 0 });
        m.put("MD", new int[] { 1, 1, 0, 0 });
        m.put("ME", new int[] { 1, 2, 2, 3 });
        m.put("MF", new int[] { 1, 4, 3, 3 });
        m.put("MG", new int[] { 3, 4, 1, 2 });
        m.put("MH", new int[] { 4, 0, 2, 3 });
        m.put("MK", new int[] { 1, 0, 0, 1 });
        m.put("ML", new int[] { 4, 4, 4, 4 });
        m.put("MM", new int[] { 2, 3, 1, 2 });
        m.put("MN", new int[] { 2, 2, 2, 4 });
        m.put("MO", new int[] { 0, 1, 4, 4 });
        m.put("MP", new int[] { 0, 0, 4, 4 });
        m.put("MQ", new int[] { 1, 1, 1, 3 });
        m.put("MR", new int[] { 4, 2, 4, 2 });
        m.put("MS", new int[] { 1, 2, 1, 2 });
        m.put("MT", new int[] { 0, 0, 0, 0 });
        m.put("MU", new int[] { 2, 2, 4, 4 });
        m.put("MV", new int[] { 4, 2, 0, 1 });
        m.put("MW", new int[] { 3, 2, 1, 1 });
        m.put("MX", new int[] { 2, 4, 3, 1 });
        m.put("MY", new int[] { 2, 3, 3, 3 });
        m.put("MZ", new int[] { 3, 3, 2, 4 });
        m.put("NA", new int[] { 4, 2, 1, 1 });
        m.put("NC", new int[] { 2, 1, 3, 3 });
        m.put("NE", new int[] { 4, 4, 4, 4 });
        m.put("NF", new int[] { 0, 2, 2, 2 });
        m.put("NG", new int[] { 3, 4, 2, 2 });
        m.put("NI", new int[] { 3, 4, 3, 3 });
        m.put("NL", new int[] { 0, 1, 3, 2 });
        m.put("NO", new int[] { 0, 0, 1, 0 });
        m.put("NP", new int[] { 2, 3, 2, 2 });
        m.put("NR", new int[] { 4, 3, 4, 1 });
        m.put("NU", new int[] { 4, 2, 2, 2 });
        m.put("NZ", new int[] { 0, 0, 0, 1 });
        m.put("OM", new int[] { 2, 2, 1, 3 });
        m.put("PA", new int[] { 1, 3, 2, 3 });
        m.put("PE", new int[] { 2, 2, 4, 4 });
        m.put("PF", new int[] { 2, 2, 0, 1 });
        m.put("PG", new int[] { 4, 4, 4, 4 });
        m.put("PH", new int[] { 3, 0, 4, 4 });
        m.put("PK", new int[] { 3, 3, 3, 3 });
        m.put("PL", new int[] { 1, 0, 1, 3 });
        m.put("PM", new int[] { 0, 2, 2, 3 });
        m.put("PR", new int[] { 2, 3, 4, 3 });
        m.put("PS", new int[] { 2, 3, 0, 4 });
        m.put("PT", new int[] { 1, 1, 1, 1 });
        m.put("PW", new int[] { 3, 2, 3, 0 });
        m.put("PY", new int[] { 2, 1, 3, 3 });
        m.put("QA", new int[] { 2, 3, 1, 2 });
        m.put("RE", new int[] { 1, 1, 2, 2 });
        m.put("RO", new int[] { 0, 1, 1, 3 });
        m.put("RS", new int[] { 1, 1, 0, 0 });
        m.put("RU", new int[] { 0, 1, 1, 1 });
        m.put("RW", new int[] { 3, 4, 3, 1 });
        m.put("SA", new int[] { 3, 2, 2, 3 });
        m.put("SB", new int[] { 4, 4, 3, 0 });
        m.put("SC", new int[] { 4, 2, 0, 1 });
        m.put("SD", new int[] { 3, 4, 4, 4 });
        m.put("SE", new int[] { 0, 0, 0, 0 });
        m.put("SG", new int[] { 1, 2, 3, 3 });
        m.put("SH", new int[] { 4, 2, 2, 2 });
        m.put("SI", new int[] { 0, 1, 0, 0 });
        m.put("SJ", new int[] { 3, 2, 0, 2 });
        m.put("SK", new int[] { 0, 1, 0, 1 });
        m.put("SL", new int[] { 4, 3, 2, 4 });
        m.put("SM", new int[] { 1, 0, 1, 1 });
        m.put("SN", new int[] { 4, 4, 4, 2 });
        m.put("SO", new int[] { 4, 4, 4, 3 });
        m.put("SR", new int[] { 3, 2, 2, 3 });
        m.put("SS", new int[] { 4, 3, 4, 2 });
        m.put("ST", new int[] { 3, 2, 2, 2 });
        m.put("SV", new int[] { 2, 3, 2, 3 });
        m.put("SX", new int[] { 2, 4, 2, 0 });
        m.put("SY", new int[] { 4, 4, 2, 0 });
        m.put("SZ", new int[] { 3, 4, 1, 1 });
        m.put("TC", new int[] { 2, 1, 2, 1 });
        m.put("TD", new int[] { 4, 4, 4, 3 });
        m.put("TG", new int[] { 3, 2, 2, 0 });
        m.put("TH", new int[] { 1, 3, 4, 4 });
        m.put("TJ", new int[] { 4, 4, 4, 4 });
        m.put("TL", new int[] { 4, 2, 4, 4 });
        m.put("TM", new int[] { 4, 1, 3, 3 });
        m.put("TN", new int[] { 2, 2, 1, 2 });
        m.put("TO", new int[] { 2, 3, 3, 1 });
        m.put("TR", new int[] { 1, 2, 0, 2 });
        m.put("TT", new int[] { 2, 1, 1, 0 });
        m.put("TV", new int[] { 4, 2, 2, 4 });
        m.put("TW", new int[] { 0, 0, 0, 1 });
        m.put("TZ", new int[] { 3, 3, 3, 2 });
        m.put("UA", new int[] { 0, 2, 1, 3 });
        m.put("UG", new int[] { 4, 3, 2, 2 });
        m.put("US", new int[] { 0, 1, 3, 3 });
        m.put("UY", new int[] { 2, 1, 2, 2 });
        m.put("UZ", new int[] { 4, 3, 2, 4 });
        m.put("VA", new int[] { 1, 2, 2, 2 });
        m.put("VC", new int[] { 2, 0, 3, 2 });
        m.put("VE", new int[] { 3, 4, 4, 3 });
        m.put("VG", new int[] { 3, 1, 3, 4 });
        m.put("VI", new int[] { 1, 0, 2, 4 });
        m.put("VN", new int[] { 0, 2, 4, 4 });
        m.put("VU", new int[] { 4, 1, 3, 2 });
        m.put("WS", new int[] { 3, 2, 3, 0 });
        m.put("XK", new int[] { 1, 2, 1, 0 });
        m.put("YE", new int[] { 4, 4, 4, 2 });
        m.put("YT", new int[] { 3, 1, 1, 2 });
        m.put("ZA", new int[] { 2, 3, 1, 2 });
        m.put("ZM", new int[] { 3, 3, 3, 1 });
        m.put("ZW", new int[] { 3, 3, 2, 1 });
        return (Map<String, int[]>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    private long getInitialBitrateEstimateForNetworkType(final int n) {
        Long n2;
        if ((n2 = (Long)this.initialBitrateEstimates.get(n)) == null) {
            n2 = (Long)this.initialBitrateEstimates.get(0);
        }
        Long value;
        if ((value = n2) == null) {
            value = 1000000L;
        }
        return value;
    }
    
    private void maybeNotifyBandwidthSample(final int n, final long n2, final long lastReportedBitrateEstimate) {
        if (n == 0 && n2 == 0L && lastReportedBitrateEstimate == this.lastReportedBitrateEstimate) {
            return;
        }
        this.lastReportedBitrateEstimate = lastReportedBitrateEstimate;
        this.eventDispatcher.dispatch(new _$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw(n, n2, lastReportedBitrateEstimate));
    }
    
    private void onConnectivityAction() {
        synchronized (this) {
            int networkType;
            if (this.networkTypeOverrideSet) {
                networkType = this.networkTypeOverride;
            }
            else if (this.context == null) {
                networkType = 0;
            }
            else {
                networkType = Util.getNetworkType(this.context);
            }
            if (this.networkType == networkType) {
                return;
            }
            this.networkType = networkType;
            if (networkType != 1 && networkType != 0 && networkType != 8) {
                this.bitrateEstimate = this.getInitialBitrateEstimateForNetworkType(networkType);
                final long elapsedRealtime = this.clock.elapsedRealtime();
                int n;
                if (this.streamCount > 0) {
                    n = (int)(elapsedRealtime - this.sampleStartTimeMs);
                }
                else {
                    n = 0;
                }
                this.maybeNotifyBandwidthSample(n, this.sampleBytesTransferred, this.bitrateEstimate);
                this.sampleStartTimeMs = elapsedRealtime;
                this.sampleBytesTransferred = 0L;
                this.totalBytesTransferred = 0L;
                this.totalElapsedTimeMs = 0L;
                this.slidingPercentile.reset();
            }
        }
    }
    
    @Override
    public void addEventListener(final Handler handler, final EventListener eventListener) {
        this.eventDispatcher.addListener(handler, eventListener);
    }
    
    @Override
    public long getBitrateEstimate() {
        synchronized (this) {
            return this.bitrateEstimate;
        }
    }
    
    @Override
    public TransferListener getTransferListener() {
        return this;
    }
    
    @Override
    public void onBytesTransferred(final DataSource dataSource, final DataSpec dataSpec, final boolean b, final int n) {
        // monitorenter(this)
        if (!b) {
            // monitorexit(this)
            return;
        }
        try {
            this.sampleBytesTransferred += n;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    @Override
    public void onTransferEnd(final DataSource dataSource, final DataSpec dataSpec, final boolean b) {
        // monitorenter(this)
        if (!b) {
            // monitorexit(this)
            return;
        }
        try {
            Assertions.checkState(this.streamCount > 0);
            final long elapsedRealtime = this.clock.elapsedRealtime();
            final int n = (int)(elapsedRealtime - this.sampleStartTimeMs);
            final long totalElapsedTimeMs = this.totalElapsedTimeMs;
            final long n2 = n;
            this.totalElapsedTimeMs = totalElapsedTimeMs + n2;
            this.totalBytesTransferred += this.sampleBytesTransferred;
            if (n > 0) {
                this.slidingPercentile.addSample((int)Math.sqrt((double)this.sampleBytesTransferred), (float)(this.sampleBytesTransferred * 8000L / n2));
                if (this.totalElapsedTimeMs >= 2000L || this.totalBytesTransferred >= 524288L) {
                    this.bitrateEstimate = (long)this.slidingPercentile.getPercentile(0.5f);
                }
                this.maybeNotifyBandwidthSample(n, this.sampleBytesTransferred, this.bitrateEstimate);
                this.sampleStartTimeMs = elapsedRealtime;
                this.sampleBytesTransferred = 0L;
            }
            --this.streamCount;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    @Override
    public void onTransferInitializing(final DataSource dataSource, final DataSpec dataSpec, final boolean b) {
    }
    
    @Override
    public void onTransferStart(final DataSource dataSource, final DataSpec dataSpec, final boolean b) {
        // monitorenter(this)
        if (!b) {
            // monitorexit(this)
            return;
        }
        try {
            if (this.streamCount == 0) {
                this.sampleStartTimeMs = this.clock.elapsedRealtime();
            }
            ++this.streamCount;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    @Override
    public void removeEventListener(final EventListener eventListener) {
        this.eventDispatcher.removeListener(eventListener);
    }
    
    public static final class Builder
    {
        private Clock clock;
        private final Context context;
        private SparseArray<Long> initialBitrateEstimates;
        private boolean resetOnNetworkTypeChange;
        private int slidingWindowMaxWeight;
        
        public Builder(final Context context) {
            Context applicationContext;
            if (context == null) {
                applicationContext = null;
            }
            else {
                applicationContext = context.getApplicationContext();
            }
            this.context = applicationContext;
            this.initialBitrateEstimates = getInitialBitrateEstimatesForCountry(Util.getCountryCode(context));
            this.slidingWindowMaxWeight = 2000;
            this.clock = Clock.DEFAULT;
        }
        
        private static int[] getCountryGroupIndices(final String s) {
            int[] array;
            if ((array = DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_COUNTRY_GROUPS.get(s)) == null) {
                final int[] array2;
                array = (array2 = new int[4]);
                array2[1] = (array2[0] = 2);
                array2[3] = (array2[2] = 2);
            }
            return array;
        }
        
        private static SparseArray<Long> getInitialBitrateEstimatesForCountry(final String s) {
            final int[] countryGroupIndices = getCountryGroupIndices(s);
            final SparseArray sparseArray = new SparseArray(6);
            sparseArray.append(0, (Object)1000000L);
            sparseArray.append(2, (Object)DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI[countryGroupIndices[0]]);
            sparseArray.append(3, (Object)DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_2G[countryGroupIndices[1]]);
            sparseArray.append(4, (Object)DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_3G[countryGroupIndices[2]]);
            sparseArray.append(5, (Object)DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_4G[countryGroupIndices[3]]);
            sparseArray.append(7, (Object)DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI[countryGroupIndices[0]]);
            return (SparseArray<Long>)sparseArray;
        }
        
        public DefaultBandwidthMeter build() {
            return new DefaultBandwidthMeter(this.context, this.initialBitrateEstimates, this.slidingWindowMaxWeight, this.clock, this.resetOnNetworkTypeChange, null);
        }
    }
    
    private static class ConnectivityActionReceiver extends BroadcastReceiver
    {
        private static ConnectivityActionReceiver staticInstance;
        private final ArrayList<WeakReference<DefaultBandwidthMeter>> bandwidthMeters;
        private final Handler mainHandler;
        
        private ConnectivityActionReceiver() {
            this.mainHandler = new Handler(Looper.getMainLooper());
            this.bandwidthMeters = new ArrayList<WeakReference<DefaultBandwidthMeter>>();
        }
        
        public static ConnectivityActionReceiver getInstance(final Context context) {
            synchronized (ConnectivityActionReceiver.class) {
                if (ConnectivityActionReceiver.staticInstance == null) {
                    ConnectivityActionReceiver.staticInstance = new ConnectivityActionReceiver();
                    final IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    context.registerReceiver((BroadcastReceiver)ConnectivityActionReceiver.staticInstance, intentFilter);
                }
                return ConnectivityActionReceiver.staticInstance;
            }
        }
        
        private void removeClearedReferences() {
            for (int i = this.bandwidthMeters.size() - 1; i >= 0; --i) {
                if (this.bandwidthMeters.get(i).get() == null) {
                    this.bandwidthMeters.remove(i);
                }
            }
        }
        
        private void updateBandwidthMeter(final DefaultBandwidthMeter defaultBandwidthMeter) {
            defaultBandwidthMeter.onConnectivityAction();
        }
        
        public void onReceive(final Context context, final Intent intent) {
            synchronized (this) {
                if (this.isInitialStickyBroadcast()) {
                    return;
                }
                this.removeClearedReferences();
                for (int i = 0; i < this.bandwidthMeters.size(); ++i) {
                    final DefaultBandwidthMeter defaultBandwidthMeter = this.bandwidthMeters.get(i).get();
                    if (defaultBandwidthMeter != null) {
                        this.updateBandwidthMeter(defaultBandwidthMeter);
                    }
                }
            }
        }
        
        public void register(final DefaultBandwidthMeter referent) {
            synchronized (this) {
                this.removeClearedReferences();
                this.bandwidthMeters.add(new WeakReference<DefaultBandwidthMeter>(referent));
                this.mainHandler.post((Runnable)new _$$Lambda$DefaultBandwidthMeter$ConnectivityActionReceiver$5orKCfoWtCCTqIpHqEoV_8DMTWQ(this, referent));
            }
        }
    }
}
