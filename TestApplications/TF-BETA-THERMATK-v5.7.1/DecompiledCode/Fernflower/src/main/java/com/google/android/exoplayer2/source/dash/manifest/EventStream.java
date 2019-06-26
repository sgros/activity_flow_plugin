package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.metadata.emsg.EventMessage;

public final class EventStream {
   public final EventMessage[] events;
   public final long[] presentationTimesUs;
   public final String schemeIdUri;
   public final long timescale;
   public final String value;

   public EventStream(String var1, String var2, long var3, long[] var5, EventMessage[] var6) {
      this.schemeIdUri = var1;
      this.value = var2;
      this.timescale = var3;
      this.presentationTimesUs = var5;
      this.events = var6;
   }

   public String id() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.schemeIdUri);
      var1.append("/");
      var1.append(this.value);
      return var1.toString();
   }
}
