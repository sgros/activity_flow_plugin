// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

public enum ActivityKind
{
    ATTRIBUTION, 
    CLICK, 
    EVENT, 
    INFO, 
    REATTRIBUTION, 
    REVENUE, 
    SESSION, 
    UNKNOWN;
    
    public static ActivityKind fromString(final String anObject) {
        if ("session".equals(anObject)) {
            return ActivityKind.SESSION;
        }
        if ("event".equals(anObject)) {
            return ActivityKind.EVENT;
        }
        if ("click".equals(anObject)) {
            return ActivityKind.CLICK;
        }
        if ("attribution".equals(anObject)) {
            return ActivityKind.ATTRIBUTION;
        }
        if ("info".equals(anObject)) {
            return ActivityKind.INFO;
        }
        return ActivityKind.UNKNOWN;
    }
    
    @Override
    public String toString() {
        switch (ActivityKind$1.$SwitchMap$com$adjust$sdk$ActivityKind[this.ordinal()]) {
            default: {
                return "unknown";
            }
            case 5: {
                return "info";
            }
            case 4: {
                return "attribution";
            }
            case 3: {
                return "click";
            }
            case 2: {
                return "event";
            }
            case 1: {
                return "session";
            }
        }
    }
}
