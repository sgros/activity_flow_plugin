package com.google.android.exoplayer2.upstream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.EventDispatcher;
import com.google.android.exoplayer2.util.SlidingPercentile;
import com.google.android.exoplayer2.util.Util;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class DefaultBandwidthMeter implements BandwidthMeter, TransferListener {
   public static final Map DEFAULT_INITIAL_BITRATE_COUNTRY_GROUPS = createInitialBitrateCountryGroupAssignment();
   public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_2G = new long[]{169000L, 129000L, 114000L, 102000L, 87000L};
   public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_3G = new long[]{2100000L, 1300000L, 950000L, 700000L, 400000L};
   public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_4G = new long[]{6900000L, 4300000L, 2700000L, 1600000L, 450000L};
   public static final long[] DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI = new long[]{5700000L, 3400000L, 1900000L, 1000000L, 400000L};
   private long bitrateEstimate;
   private final Clock clock;
   private final Context context;
   private final EventDispatcher eventDispatcher;
   private final SparseArray initialBitrateEstimates;
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

   @Deprecated
   public DefaultBandwidthMeter() {
      this((Context)null, new SparseArray(), 2000, Clock.DEFAULT, false);
   }

   private DefaultBandwidthMeter(Context var1, SparseArray var2, int var3, Clock var4, boolean var5) {
      Context var6;
      if (var1 == null) {
         var6 = null;
      } else {
         var6 = var1.getApplicationContext();
      }

      this.context = var6;
      this.initialBitrateEstimates = var2;
      this.eventDispatcher = new EventDispatcher();
      this.slidingPercentile = new SlidingPercentile(var3);
      this.clock = var4;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = Util.getNetworkType(var1);
      }

      this.networkType = var3;
      this.bitrateEstimate = this.getInitialBitrateEstimateForNetworkType(this.networkType);
      if (var1 != null && var5) {
         DefaultBandwidthMeter.ConnectivityActionReceiver.getInstance(var1).register(this);
      }

   }

   // $FF: synthetic method
   DefaultBandwidthMeter(Context var1, SparseArray var2, int var3, Clock var4, boolean var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private static Map createInitialBitrateCountryGroupAssignment() {
      HashMap var0 = new HashMap();
      var0.put("AD", new int[]{1, 0, 0, 0});
      var0.put("AE", new int[]{1, 3, 4, 4});
      var0.put("AF", new int[]{4, 4, 3, 2});
      var0.put("AG", new int[]{3, 2, 1, 2});
      var0.put("AI", new int[]{1, 0, 0, 2});
      var0.put("AL", new int[]{1, 1, 1, 1});
      var0.put("AM", new int[]{2, 2, 4, 3});
      var0.put("AO", new int[]{2, 4, 2, 0});
      var0.put("AR", new int[]{2, 3, 2, 3});
      var0.put("AS", new int[]{3, 4, 4, 1});
      var0.put("AT", new int[]{0, 1, 0, 0});
      var0.put("AU", new int[]{0, 3, 0, 0});
      var0.put("AW", new int[]{1, 1, 0, 4});
      var0.put("AX", new int[]{0, 1, 0, 0});
      var0.put("AZ", new int[]{3, 3, 2, 2});
      var0.put("BA", new int[]{1, 1, 1, 2});
      var0.put("BB", new int[]{0, 1, 0, 0});
      var0.put("BD", new int[]{2, 1, 3, 2});
      var0.put("BE", new int[]{0, 0, 0, 0});
      var0.put("BF", new int[]{4, 4, 4, 1});
      var0.put("BG", new int[]{0, 0, 0, 1});
      var0.put("BH", new int[]{2, 1, 3, 4});
      var0.put("BI", new int[]{4, 3, 4, 4});
      var0.put("BJ", new int[]{4, 3, 4, 3});
      var0.put("BL", new int[]{1, 0, 1, 2});
      var0.put("BM", new int[]{1, 0, 0, 0});
      var0.put("BN", new int[]{4, 3, 3, 3});
      var0.put("BO", new int[]{2, 2, 1, 2});
      var0.put("BQ", new int[]{1, 1, 2, 4});
      var0.put("BR", new int[]{2, 3, 2, 2});
      var0.put("BS", new int[]{1, 1, 0, 2});
      var0.put("BT", new int[]{3, 0, 2, 1});
      var0.put("BW", new int[]{4, 4, 2, 3});
      var0.put("BY", new int[]{1, 1, 1, 1});
      var0.put("BZ", new int[]{2, 3, 3, 1});
      var0.put("CA", new int[]{0, 2, 2, 3});
      var0.put("CD", new int[]{4, 4, 2, 1});
      var0.put("CF", new int[]{4, 4, 3, 3});
      var0.put("CG", new int[]{4, 4, 4, 4});
      var0.put("CH", new int[]{0, 0, 0, 0});
      var0.put("CI", new int[]{4, 4, 4, 4});
      var0.put("CK", new int[]{2, 4, 2, 0});
      var0.put("CL", new int[]{2, 2, 2, 3});
      var0.put("CM", new int[]{3, 4, 3, 1});
      var0.put("CN", new int[]{2, 0, 1, 2});
      var0.put("CO", new int[]{2, 3, 2, 1});
      var0.put("CR", new int[]{2, 2, 4, 4});
      var0.put("CU", new int[]{4, 4, 4, 1});
      var0.put("CV", new int[]{2, 2, 2, 4});
      var0.put("CW", new int[]{1, 1, 0, 0});
      var0.put("CX", new int[]{1, 2, 2, 2});
      var0.put("CY", new int[]{1, 1, 0, 0});
      var0.put("CZ", new int[]{0, 1, 0, 0});
      var0.put("DE", new int[]{0, 2, 2, 2});
      var0.put("DJ", new int[]{3, 4, 4, 0});
      var0.put("DK", new int[]{0, 0, 0, 0});
      var0.put("DM", new int[]{2, 0, 3, 4});
      var0.put("DO", new int[]{3, 3, 4, 4});
      var0.put("DZ", new int[]{3, 3, 4, 4});
      var0.put("EC", new int[]{2, 3, 3, 1});
      var0.put("EE", new int[]{0, 0, 0, 0});
      var0.put("EG", new int[]{3, 3, 1, 1});
      var0.put("EH", new int[]{2, 0, 2, 3});
      var0.put("ER", new int[]{4, 2, 2, 2});
      var0.put("ES", new int[]{0, 0, 1, 1});
      var0.put("ET", new int[]{4, 4, 4, 0});
      var0.put("FI", new int[]{0, 0, 1, 0});
      var0.put("FJ", new int[]{3, 2, 3, 3});
      var0.put("FK", new int[]{3, 4, 2, 1});
      var0.put("FM", new int[]{4, 2, 4, 0});
      var0.put("FO", new int[]{0, 0, 0, 1});
      var0.put("FR", new int[]{1, 0, 2, 1});
      var0.put("GA", new int[]{3, 3, 2, 1});
      var0.put("GB", new int[]{0, 1, 3, 2});
      var0.put("GD", new int[]{2, 0, 3, 0});
      var0.put("GE", new int[]{1, 1, 0, 3});
      var0.put("GF", new int[]{1, 2, 4, 4});
      var0.put("GG", new int[]{0, 1, 0, 0});
      var0.put("GH", new int[]{3, 2, 2, 2});
      var0.put("GI", new int[]{0, 0, 0, 1});
      var0.put("GL", new int[]{2, 4, 1, 4});
      var0.put("GM", new int[]{4, 3, 3, 0});
      var0.put("GN", new int[]{4, 4, 3, 4});
      var0.put("GP", new int[]{2, 2, 1, 3});
      var0.put("GQ", new int[]{4, 4, 3, 1});
      var0.put("GR", new int[]{1, 1, 0, 1});
      var0.put("GT", new int[]{3, 2, 3, 4});
      var0.put("GU", new int[]{1, 0, 4, 4});
      var0.put("GW", new int[]{4, 4, 4, 0});
      var0.put("GY", new int[]{3, 4, 1, 0});
      var0.put("HK", new int[]{0, 2, 3, 4});
      var0.put("HN", new int[]{3, 3, 2, 2});
      var0.put("HR", new int[]{1, 0, 0, 2});
      var0.put("HT", new int[]{3, 3, 3, 3});
      var0.put("HU", new int[]{0, 0, 1, 0});
      var0.put("ID", new int[]{2, 3, 3, 4});
      var0.put("IE", new int[]{0, 0, 1, 1});
      var0.put("IL", new int[]{0, 1, 1, 3});
      var0.put("IM", new int[]{0, 1, 0, 1});
      var0.put("IN", new int[]{2, 3, 3, 4});
      var0.put("IO", new int[]{4, 2, 2, 2});
      var0.put("IQ", new int[]{3, 3, 4, 3});
      var0.put("IR", new int[]{3, 2, 4, 4});
      var0.put("IS", new int[]{0, 0, 0, 0});
      var0.put("IT", new int[]{1, 0, 1, 3});
      var0.put("JE", new int[]{0, 0, 0, 1});
      var0.put("JM", new int[]{3, 3, 3, 2});
      var0.put("JO", new int[]{1, 1, 1, 2});
      var0.put("JP", new int[]{0, 1, 1, 2});
      var0.put("KE", new int[]{3, 3, 3, 3});
      var0.put("KG", new int[]{2, 2, 3, 3});
      var0.put("KH", new int[]{1, 0, 4, 4});
      var0.put("KI", new int[]{4, 4, 4, 4});
      var0.put("KM", new int[]{4, 4, 2, 2});
      var0.put("KN", new int[]{1, 0, 1, 3});
      var0.put("KP", new int[]{1, 2, 2, 2});
      var0.put("KR", new int[]{0, 4, 0, 2});
      var0.put("KW", new int[]{1, 2, 1, 2});
      var0.put("KY", new int[]{1, 1, 0, 2});
      var0.put("KZ", new int[]{1, 2, 2, 3});
      var0.put("LA", new int[]{3, 2, 2, 2});
      var0.put("LB", new int[]{3, 2, 0, 0});
      var0.put("LC", new int[]{2, 2, 1, 0});
      var0.put("LI", new int[]{0, 0, 1, 2});
      var0.put("LK", new int[]{1, 1, 2, 2});
      var0.put("LR", new int[]{3, 4, 3, 1});
      var0.put("LS", new int[]{3, 3, 2, 0});
      var0.put("LT", new int[]{0, 0, 0, 1});
      var0.put("LU", new int[]{0, 0, 1, 0});
      var0.put("LV", new int[]{0, 0, 0, 0});
      var0.put("LY", new int[]{4, 4, 4, 4});
      var0.put("MA", new int[]{2, 1, 2, 2});
      var0.put("MC", new int[]{1, 0, 1, 0});
      var0.put("MD", new int[]{1, 1, 0, 0});
      var0.put("ME", new int[]{1, 2, 2, 3});
      var0.put("MF", new int[]{1, 4, 3, 3});
      var0.put("MG", new int[]{3, 4, 1, 2});
      var0.put("MH", new int[]{4, 0, 2, 3});
      var0.put("MK", new int[]{1, 0, 0, 1});
      var0.put("ML", new int[]{4, 4, 4, 4});
      var0.put("MM", new int[]{2, 3, 1, 2});
      var0.put("MN", new int[]{2, 2, 2, 4});
      var0.put("MO", new int[]{0, 1, 4, 4});
      var0.put("MP", new int[]{0, 0, 4, 4});
      var0.put("MQ", new int[]{1, 1, 1, 3});
      var0.put("MR", new int[]{4, 2, 4, 2});
      var0.put("MS", new int[]{1, 2, 1, 2});
      var0.put("MT", new int[]{0, 0, 0, 0});
      var0.put("MU", new int[]{2, 2, 4, 4});
      var0.put("MV", new int[]{4, 2, 0, 1});
      var0.put("MW", new int[]{3, 2, 1, 1});
      var0.put("MX", new int[]{2, 4, 3, 1});
      var0.put("MY", new int[]{2, 3, 3, 3});
      var0.put("MZ", new int[]{3, 3, 2, 4});
      var0.put("NA", new int[]{4, 2, 1, 1});
      var0.put("NC", new int[]{2, 1, 3, 3});
      var0.put("NE", new int[]{4, 4, 4, 4});
      var0.put("NF", new int[]{0, 2, 2, 2});
      var0.put("NG", new int[]{3, 4, 2, 2});
      var0.put("NI", new int[]{3, 4, 3, 3});
      var0.put("NL", new int[]{0, 1, 3, 2});
      var0.put("NO", new int[]{0, 0, 1, 0});
      var0.put("NP", new int[]{2, 3, 2, 2});
      var0.put("NR", new int[]{4, 3, 4, 1});
      var0.put("NU", new int[]{4, 2, 2, 2});
      var0.put("NZ", new int[]{0, 0, 0, 1});
      var0.put("OM", new int[]{2, 2, 1, 3});
      var0.put("PA", new int[]{1, 3, 2, 3});
      var0.put("PE", new int[]{2, 2, 4, 4});
      var0.put("PF", new int[]{2, 2, 0, 1});
      var0.put("PG", new int[]{4, 4, 4, 4});
      var0.put("PH", new int[]{3, 0, 4, 4});
      var0.put("PK", new int[]{3, 3, 3, 3});
      var0.put("PL", new int[]{1, 0, 1, 3});
      var0.put("PM", new int[]{0, 2, 2, 3});
      var0.put("PR", new int[]{2, 3, 4, 3});
      var0.put("PS", new int[]{2, 3, 0, 4});
      var0.put("PT", new int[]{1, 1, 1, 1});
      var0.put("PW", new int[]{3, 2, 3, 0});
      var0.put("PY", new int[]{2, 1, 3, 3});
      var0.put("QA", new int[]{2, 3, 1, 2});
      var0.put("RE", new int[]{1, 1, 2, 2});
      var0.put("RO", new int[]{0, 1, 1, 3});
      var0.put("RS", new int[]{1, 1, 0, 0});
      var0.put("RU", new int[]{0, 1, 1, 1});
      var0.put("RW", new int[]{3, 4, 3, 1});
      var0.put("SA", new int[]{3, 2, 2, 3});
      var0.put("SB", new int[]{4, 4, 3, 0});
      var0.put("SC", new int[]{4, 2, 0, 1});
      var0.put("SD", new int[]{3, 4, 4, 4});
      var0.put("SE", new int[]{0, 0, 0, 0});
      var0.put("SG", new int[]{1, 2, 3, 3});
      var0.put("SH", new int[]{4, 2, 2, 2});
      var0.put("SI", new int[]{0, 1, 0, 0});
      var0.put("SJ", new int[]{3, 2, 0, 2});
      var0.put("SK", new int[]{0, 1, 0, 1});
      var0.put("SL", new int[]{4, 3, 2, 4});
      var0.put("SM", new int[]{1, 0, 1, 1});
      var0.put("SN", new int[]{4, 4, 4, 2});
      var0.put("SO", new int[]{4, 4, 4, 3});
      var0.put("SR", new int[]{3, 2, 2, 3});
      var0.put("SS", new int[]{4, 3, 4, 2});
      var0.put("ST", new int[]{3, 2, 2, 2});
      var0.put("SV", new int[]{2, 3, 2, 3});
      var0.put("SX", new int[]{2, 4, 2, 0});
      var0.put("SY", new int[]{4, 4, 2, 0});
      var0.put("SZ", new int[]{3, 4, 1, 1});
      var0.put("TC", new int[]{2, 1, 2, 1});
      var0.put("TD", new int[]{4, 4, 4, 3});
      var0.put("TG", new int[]{3, 2, 2, 0});
      var0.put("TH", new int[]{1, 3, 4, 4});
      var0.put("TJ", new int[]{4, 4, 4, 4});
      var0.put("TL", new int[]{4, 2, 4, 4});
      var0.put("TM", new int[]{4, 1, 3, 3});
      var0.put("TN", new int[]{2, 2, 1, 2});
      var0.put("TO", new int[]{2, 3, 3, 1});
      var0.put("TR", new int[]{1, 2, 0, 2});
      var0.put("TT", new int[]{2, 1, 1, 0});
      var0.put("TV", new int[]{4, 2, 2, 4});
      var0.put("TW", new int[]{0, 0, 0, 1});
      var0.put("TZ", new int[]{3, 3, 3, 2});
      var0.put("UA", new int[]{0, 2, 1, 3});
      var0.put("UG", new int[]{4, 3, 2, 2});
      var0.put("US", new int[]{0, 1, 3, 3});
      var0.put("UY", new int[]{2, 1, 2, 2});
      var0.put("UZ", new int[]{4, 3, 2, 4});
      var0.put("VA", new int[]{1, 2, 2, 2});
      var0.put("VC", new int[]{2, 0, 3, 2});
      var0.put("VE", new int[]{3, 4, 4, 3});
      var0.put("VG", new int[]{3, 1, 3, 4});
      var0.put("VI", new int[]{1, 0, 2, 4});
      var0.put("VN", new int[]{0, 2, 4, 4});
      var0.put("VU", new int[]{4, 1, 3, 2});
      var0.put("WS", new int[]{3, 2, 3, 0});
      var0.put("XK", new int[]{1, 2, 1, 0});
      var0.put("YE", new int[]{4, 4, 4, 2});
      var0.put("YT", new int[]{3, 1, 1, 2});
      var0.put("ZA", new int[]{2, 3, 1, 2});
      var0.put("ZM", new int[]{3, 3, 3, 1});
      var0.put("ZW", new int[]{3, 3, 2, 1});
      return Collections.unmodifiableMap(var0);
   }

   private long getInitialBitrateEstimateForNetworkType(int var1) {
      Long var2 = (Long)this.initialBitrateEstimates.get(var1);
      Long var3 = var2;
      if (var2 == null) {
         var3 = (Long)this.initialBitrateEstimates.get(0);
      }

      var2 = var3;
      if (var3 == null) {
         var2 = 1000000L;
      }

      return var2;
   }

   // $FF: synthetic method
   static void lambda$maybeNotifyBandwidthSample$0(int var0, long var1, long var3, BandwidthMeter.EventListener var5) {
      var5.onBandwidthSample(var0, var1, var3);
   }

   private void maybeNotifyBandwidthSample(int var1, long var2, long var4) {
      if (var1 != 0 || var2 != 0L || var4 != this.lastReportedBitrateEstimate) {
         this.lastReportedBitrateEstimate = var4;
         this.eventDispatcher.dispatch(new _$$Lambda$DefaultBandwidthMeter$5fiwNBHdIyEsfLPs7kZdGg5uwiw(var1, var2, var4));
      }
   }

   private void onConnectivityAction() {
      synchronized(this){}

      Throwable var10000;
      label679: {
         int var1;
         boolean var10001;
         label678: {
            try {
               if (this.networkTypeOverrideSet) {
                  var1 = this.networkTypeOverride;
                  break label678;
               }
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label679;
            }

            label661: {
               try {
                  if (this.context != null) {
                     break label661;
                  }
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label679;
               }

               var1 = 0;
               break label678;
            }

            try {
               var1 = Util.getNetworkType(this.context);
            } catch (Throwable var59) {
               var10000 = var59;
               var10001 = false;
               break label679;
            }
         }

         int var2;
         try {
            var2 = this.networkType;
         } catch (Throwable var58) {
            var10000 = var58;
            var10001 = false;
            break label679;
         }

         if (var2 == var1) {
            return;
         }

         try {
            this.networkType = var1;
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label679;
         }

         if (var1 == 1 || var1 == 0 || var1 == 8) {
            return;
         }

         long var3;
         label638: {
            try {
               this.bitrateEstimate = this.getInitialBitrateEstimateForNetworkType(var1);
               var3 = this.clock.elapsedRealtime();
               if (this.streamCount > 0) {
                  var1 = (int)(var3 - this.sampleStartTimeMs);
                  break label638;
               }
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label679;
            }

            var1 = 0;
         }

         try {
            this.maybeNotifyBandwidthSample(var1, this.sampleBytesTransferred, this.bitrateEstimate);
            this.sampleStartTimeMs = var3;
            this.sampleBytesTransferred = 0L;
            this.totalBytesTransferred = 0L;
            this.totalElapsedTimeMs = 0L;
            this.slidingPercentile.reset();
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            break label679;
         }

         return;
      }

      Throwable var5 = var10000;
      throw var5;
   }

   public void addEventListener(Handler var1, BandwidthMeter.EventListener var2) {
      this.eventDispatcher.addListener(var1, var2);
   }

   public long getBitrateEstimate() {
      synchronized(this){}

      long var1;
      try {
         var1 = this.bitrateEstimate;
      } finally {
         ;
      }

      return var1;
   }

   public TransferListener getTransferListener() {
      return this;
   }

   public void onBytesTransferred(DataSource var1, DataSpec var2, boolean var3, int var4) {
      synchronized(this){}
      if (var3) {
         try {
            this.sampleBytesTransferred += (long)var4;
         } finally {
            ;
         }

      }
   }

   public void onTransferEnd(DataSource var1, DataSpec var2, boolean var3) {
      synchronized(this){}
      if (var3) {
         Throwable var10000;
         label512: {
            boolean var10001;
            label506: {
               label505: {
                  try {
                     if (this.streamCount > 0) {
                        break label505;
                     }
                  } catch (Throwable var67) {
                     var10000 = var67;
                     var10001 = false;
                     break label512;
                  }

                  var3 = false;
                  break label506;
               }

               var3 = true;
            }

            long var4;
            int var6;
            long var7;
            try {
               Assertions.checkState(var3);
               var4 = this.clock.elapsedRealtime();
               var6 = (int)(var4 - this.sampleStartTimeMs);
               var7 = this.totalElapsedTimeMs;
            } catch (Throwable var66) {
               var10000 = var66;
               var10001 = false;
               break label512;
            }

            long var9 = (long)var6;

            try {
               this.totalElapsedTimeMs = var7 + var9;
               this.totalBytesTransferred += this.sampleBytesTransferred;
            } catch (Throwable var65) {
               var10000 = var65;
               var10001 = false;
               break label512;
            }

            if (var6 > 0) {
               label489: {
                  try {
                     float var11 = (float)(this.sampleBytesTransferred * 8000L / var9);
                     this.slidingPercentile.addSample((int)Math.sqrt((double)this.sampleBytesTransferred), var11);
                     if (this.totalElapsedTimeMs < 2000L && this.totalBytesTransferred < 524288L) {
                        break label489;
                     }
                  } catch (Throwable var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label512;
                  }

                  try {
                     this.bitrateEstimate = (long)this.slidingPercentile.getPercentile(0.5F);
                  } catch (Throwable var63) {
                     var10000 = var63;
                     var10001 = false;
                     break label512;
                  }
               }

               try {
                  this.maybeNotifyBandwidthSample(var6, this.sampleBytesTransferred, this.bitrateEstimate);
                  this.sampleStartTimeMs = var4;
                  this.sampleBytesTransferred = 0L;
               } catch (Throwable var62) {
                  var10000 = var62;
                  var10001 = false;
                  break label512;
               }
            }

            label479:
            try {
               --this.streamCount;
               return;
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label479;
            }
         }

         Throwable var68 = var10000;
         throw var68;
      }
   }

   public void onTransferInitializing(DataSource var1, DataSpec var2, boolean var3) {
   }

   public void onTransferStart(DataSource var1, DataSpec var2, boolean var3) {
      synchronized(this){}
      if (var3) {
         try {
            if (this.streamCount == 0) {
               this.sampleStartTimeMs = this.clock.elapsedRealtime();
            }

            ++this.streamCount;
         } finally {
            ;
         }

      }
   }

   public void removeEventListener(BandwidthMeter.EventListener var1) {
      this.eventDispatcher.removeListener(var1);
   }

   public static final class Builder {
      private Clock clock;
      private final Context context;
      private SparseArray initialBitrateEstimates;
      private boolean resetOnNetworkTypeChange;
      private int slidingWindowMaxWeight;

      public Builder(Context var1) {
         Context var2;
         if (var1 == null) {
            var2 = null;
         } else {
            var2 = var1.getApplicationContext();
         }

         this.context = var2;
         this.initialBitrateEstimates = getInitialBitrateEstimatesForCountry(Util.getCountryCode(var1));
         this.slidingWindowMaxWeight = 2000;
         this.clock = Clock.DEFAULT;
      }

      private static int[] getCountryGroupIndices(String var0) {
         int[] var1 = (int[])DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_COUNTRY_GROUPS.get(var0);
         int[] var2 = var1;
         if (var1 == null) {
            var2 = new int[]{2, 2, 2, 2};
         }

         return var2;
      }

      private static SparseArray getInitialBitrateEstimatesForCountry(String var0) {
         int[] var1 = getCountryGroupIndices(var0);
         SparseArray var2 = new SparseArray(6);
         var2.append(0, 1000000L);
         var2.append(2, DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI[var1[0]]);
         var2.append(3, DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_2G[var1[1]]);
         var2.append(4, DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_3G[var1[2]]);
         var2.append(5, DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_4G[var1[3]]);
         var2.append(7, DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI[var1[0]]);
         return var2;
      }

      public DefaultBandwidthMeter build() {
         return new DefaultBandwidthMeter(this.context, this.initialBitrateEstimates, this.slidingWindowMaxWeight, this.clock, this.resetOnNetworkTypeChange);
      }
   }

   private static class ConnectivityActionReceiver extends BroadcastReceiver {
      private static DefaultBandwidthMeter.ConnectivityActionReceiver staticInstance;
      private final ArrayList bandwidthMeters = new ArrayList();
      private final Handler mainHandler = new Handler(Looper.getMainLooper());

      public static DefaultBandwidthMeter.ConnectivityActionReceiver getInstance(Context var0) {
         synchronized(DefaultBandwidthMeter.ConnectivityActionReceiver.class){}

         DefaultBandwidthMeter.ConnectivityActionReceiver var4;
         try {
            if (staticInstance == null) {
               DefaultBandwidthMeter.ConnectivityActionReceiver var1 = new DefaultBandwidthMeter.ConnectivityActionReceiver();
               staticInstance = var1;
               IntentFilter var5 = new IntentFilter();
               var5.addAction("android.net.conn.CONNECTIVITY_CHANGE");
               var0.registerReceiver(staticInstance, var5);
            }

            var4 = staticInstance;
         } finally {
            ;
         }

         return var4;
      }

      private void removeClearedReferences() {
         for(int var1 = this.bandwidthMeters.size() - 1; var1 >= 0; --var1) {
            if ((DefaultBandwidthMeter)((WeakReference)this.bandwidthMeters.get(var1)).get() == null) {
               this.bandwidthMeters.remove(var1);
            }
         }

      }

      private void updateBandwidthMeter(DefaultBandwidthMeter var1) {
         var1.onConnectivityAction();
      }

      // $FF: synthetic method
      public void lambda$register$0$DefaultBandwidthMeter$ConnectivityActionReceiver(DefaultBandwidthMeter var1) {
         this.updateBandwidthMeter(var1);
      }

      public void onReceive(Context var1, Intent var2) {
         synchronized(this){}

         Throwable var10000;
         label237: {
            boolean var10001;
            boolean var3;
            try {
               var3 = this.isInitialStickyBroadcast();
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label237;
            }

            if (var3) {
               return;
            }

            try {
               this.removeClearedReferences();
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label237;
            }

            int var4 = 0;

            while(true) {
               DefaultBandwidthMeter var25;
               label222: {
                  try {
                     if (var4 < this.bandwidthMeters.size()) {
                        var25 = (DefaultBandwidthMeter)((WeakReference)this.bandwidthMeters.get(var4)).get();
                        break label222;
                     }
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break;
                  }

                  return;
               }

               if (var25 != null) {
                  try {
                     this.updateBandwidthMeter(var25);
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break;
                  }
               }

               ++var4;
            }
         }

         Throwable var26 = var10000;
         throw var26;
      }

      public void register(DefaultBandwidthMeter var1) {
         synchronized(this){}

         try {
            this.removeClearedReferences();
            ArrayList var2 = this.bandwidthMeters;
            WeakReference var3 = new WeakReference(var1);
            var2.add(var3);
            Handler var6 = this.mainHandler;
            _$$Lambda$DefaultBandwidthMeter$ConnectivityActionReceiver$5orKCfoWtCCTqIpHqEoV_8DMTWQ var7 = new _$$Lambda$DefaultBandwidthMeter$ConnectivityActionReceiver$5orKCfoWtCCTqIpHqEoV_8DMTWQ(this, var1);
            var6.post(var7);
         } finally {
            ;
         }

      }
   }
}
