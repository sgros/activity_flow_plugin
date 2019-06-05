package com.adjust.sdk;

public enum ActivityKind {
   ATTRIBUTION,
   CLICK,
   EVENT,
   INFO,
   REATTRIBUTION,
   REVENUE,
   SESSION,
   UNKNOWN;

   public static ActivityKind fromString(String var0) {
      if ("session".equals(var0)) {
         return SESSION;
      } else if ("event".equals(var0)) {
         return EVENT;
      } else if ("click".equals(var0)) {
         return CLICK;
      } else if ("attribution".equals(var0)) {
         return ATTRIBUTION;
      } else {
         return "info".equals(var0) ? INFO : UNKNOWN;
      }
   }

   public String toString() {
      switch(this) {
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
