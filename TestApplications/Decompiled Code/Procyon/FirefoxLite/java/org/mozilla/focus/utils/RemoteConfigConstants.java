// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.focus.Inject;

public final class RemoteConfigConstants
{
    private static final int FEATURE_SURVEY_DEFAULT;
    public static final RemoteConfigConstants INSTANCE;
    
    static {
        INSTANCE = new RemoteConfigConstants();
        FEATURE_SURVEY_DEFAULT = Inject.getDefaultFeatureSurvey().getValue();
    }
    
    private RemoteConfigConstants() {
    }
    
    public final int getFEATURE_SURVEY_DEFAULT() {
        return RemoteConfigConstants.FEATURE_SURVEY_DEFAULT;
    }
    
    public enum SURVEY
    {
        public static final Companion Companion;
        
        NONE(0), 
        VPN(2), 
        VPN_RECOMMENDER(3), 
        WIFI_FINDING(1);
        
        private final int value;
        
        static {
            Companion = new Companion(null);
        }
        
        protected SURVEY(final int value) {
            this.value = value;
        }
        
        public final int getValue() {
            return this.value;
        }
        
        public static final class Companion
        {
            private Companion() {
            }
            
            public final SURVEY parseLong(final long n) {
                SURVEY survey;
                if (n == SURVEY.WIFI_FINDING.getValue()) {
                    survey = SURVEY.WIFI_FINDING;
                }
                else if (n == SURVEY.VPN.getValue()) {
                    survey = SURVEY.VPN;
                }
                else if (n == SURVEY.VPN_RECOMMENDER.getValue()) {
                    survey = SURVEY.VPN_RECOMMENDER;
                }
                else {
                    survey = SURVEY.NONE;
                }
                return survey;
            }
        }
    }
}
