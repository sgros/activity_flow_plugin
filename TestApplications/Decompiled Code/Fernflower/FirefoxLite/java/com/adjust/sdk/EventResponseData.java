package com.adjust.sdk;

public class EventResponseData extends ResponseData {
   public String eventToken;

   public EventResponseData(ActivityPackage var1) {
      this.eventToken = (String)var1.getParameters().get("event_token");
   }

   public AdjustEventFailure getFailureResponseData() {
      if (this.success) {
         return null;
      } else {
         AdjustEventFailure var1 = new AdjustEventFailure();
         var1.message = this.message;
         var1.timestamp = this.timestamp;
         var1.adid = this.adid;
         var1.willRetry = this.willRetry;
         var1.jsonResponse = this.jsonResponse;
         var1.eventToken = this.eventToken;
         return var1;
      }
   }

   public AdjustEventSuccess getSuccessResponseData() {
      if (!this.success) {
         return null;
      } else {
         AdjustEventSuccess var1 = new AdjustEventSuccess();
         var1.message = this.message;
         var1.timestamp = this.timestamp;
         var1.adid = this.adid;
         var1.jsonResponse = this.jsonResponse;
         var1.eventToken = this.eventToken;
         return var1;
      }
   }
}
