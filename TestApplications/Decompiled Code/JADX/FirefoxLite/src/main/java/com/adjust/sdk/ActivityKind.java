package com.adjust.sdk;

public enum ActivityKind {
    UNKNOWN,
    SESSION,
    EVENT,
    CLICK,
    ATTRIBUTION,
    REVENUE,
    REATTRIBUTION,
    INFO;

    public static ActivityKind fromString(String str) {
        if ("session".equals(str)) {
            return SESSION;
        }
        if ("event".equals(str)) {
            return EVENT;
        }
        if ("click".equals(str)) {
            return CLICK;
        }
        if ("attribution".equals(str)) {
            return ATTRIBUTION;
        }
        if ("info".equals(str)) {
            return INFO;
        }
        return UNKNOWN;
    }

    public String toString() {
        switch (this) {
            case SESSION:
                return "session";
            case EVENT:
                return "event";
            case CLICK:
                return "click";
            case ATTRIBUTION:
                return "attribution";
            case INFO:
                return "info";
            default:
                return "unknown";
        }
    }
}
