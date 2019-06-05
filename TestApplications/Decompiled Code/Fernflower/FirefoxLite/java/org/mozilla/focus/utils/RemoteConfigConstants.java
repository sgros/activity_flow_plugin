package org.mozilla.focus.utils;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.mozilla.focus.Inject;

public final class RemoteConfigConstants {
   private static final int FEATURE_SURVEY_DEFAULT = Inject.getDefaultFeatureSurvey().getValue();
   public static final RemoteConfigConstants INSTANCE = new RemoteConfigConstants();

   private RemoteConfigConstants() {
   }

   public final int getFEATURE_SURVEY_DEFAULT() {
      return FEATURE_SURVEY_DEFAULT;
   }

   public static enum SURVEY {
      public static final RemoteConfigConstants.SURVEY.Companion Companion;
      NONE,
      VPN,
      VPN_RECOMMENDER,
      WIFI_FINDING;

      private final int value;

      static {
         RemoteConfigConstants.SURVEY var0 = new RemoteConfigConstants.SURVEY("NONE", 0, 0);
         NONE = var0;
         RemoteConfigConstants.SURVEY var1 = new RemoteConfigConstants.SURVEY("WIFI_FINDING", 1, 1);
         WIFI_FINDING = var1;
         RemoteConfigConstants.SURVEY var2 = new RemoteConfigConstants.SURVEY("VPN", 2, 2);
         VPN = var2;
         RemoteConfigConstants.SURVEY var3 = new RemoteConfigConstants.SURVEY("VPN_RECOMMENDER", 3, 3);
         VPN_RECOMMENDER = var3;
         Companion = new RemoteConfigConstants.SURVEY.Companion((DefaultConstructorMarker)null);
      }

      protected SURVEY(int var3) {
         this.value = var3;
      }

      public final int getValue() {
         return this.value;
      }

      public static final class Companion {
         private Companion() {
         }

         // $FF: synthetic method
         public Companion(DefaultConstructorMarker var1) {
            this();
         }

         public final RemoteConfigConstants.SURVEY parseLong(long var1) {
            RemoteConfigConstants.SURVEY var3;
            if (var1 == (long)RemoteConfigConstants.SURVEY.WIFI_FINDING.getValue()) {
               var3 = RemoteConfigConstants.SURVEY.WIFI_FINDING;
            } else if (var1 == (long)RemoteConfigConstants.SURVEY.VPN.getValue()) {
               var3 = RemoteConfigConstants.SURVEY.VPN;
            } else if (var1 == (long)RemoteConfigConstants.SURVEY.VPN_RECOMMENDER.getValue()) {
               var3 = RemoteConfigConstants.SURVEY.VPN_RECOMMENDER;
            } else {
               var3 = RemoteConfigConstants.SURVEY.NONE;
            }

            return var3;
         }
      }
   }
}
