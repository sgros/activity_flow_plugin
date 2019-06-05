package org.mozilla.focus.utils;

import org.mozilla.focus.Inject;

/* compiled from: RemoteConfigConstants.kt */
public final class RemoteConfigConstants {
    private static final int FEATURE_SURVEY_DEFAULT = Inject.getDefaultFeatureSurvey().getValue();
    public static final RemoteConfigConstants INSTANCE = new RemoteConfigConstants();

    /* compiled from: RemoteConfigConstants.kt */
    public enum SURVEY {
        NONE(0),
        WIFI_FINDING(1),
        VPN(2),
        VPN_RECOMMENDER(3);
        
        public static final Companion Companion = null;
        private final int value;

        /* compiled from: RemoteConfigConstants.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final SURVEY parseLong(long j) {
                if (j == ((long) SURVEY.WIFI_FINDING.getValue())) {
                    return SURVEY.WIFI_FINDING;
                }
                if (j == ((long) SURVEY.VPN.getValue())) {
                    return SURVEY.VPN;
                }
                if (j == ((long) SURVEY.VPN_RECOMMENDER.getValue())) {
                    return SURVEY.VPN_RECOMMENDER;
                }
                return SURVEY.NONE;
            }
        }

        protected SURVEY(int i) {
            this.value = i;
        }

        public final int getValue() {
            return this.value;
        }

        static {
            Companion = new Companion();
        }
    }

    private RemoteConfigConstants() {
    }

    public final int getFEATURE_SURVEY_DEFAULT() {
        return FEATURE_SURVEY_DEFAULT;
    }
}
