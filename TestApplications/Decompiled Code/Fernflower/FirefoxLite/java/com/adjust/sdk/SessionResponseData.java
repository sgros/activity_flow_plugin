package com.adjust.sdk;

public class SessionResponseData extends ResponseData {
   public AdjustSessionFailure getFailureResponseData() {
      if (this.success) {
         return null;
      } else {
         AdjustSessionFailure var1 = new AdjustSessionFailure();
         var1.message = this.message;
         var1.timestamp = this.timestamp;
         var1.adid = this.adid;
         var1.willRetry = this.willRetry;
         var1.jsonResponse = this.jsonResponse;
         return var1;
      }
   }

   public AdjustSessionSuccess getSuccessResponseData() {
      if (!this.success) {
         return null;
      } else {
         AdjustSessionSuccess var1 = new AdjustSessionSuccess();
         var1.message = this.message;
         var1.timestamp = this.timestamp;
         var1.adid = this.adid;
         var1.jsonResponse = this.jsonResponse;
         return var1;
      }
   }
}
