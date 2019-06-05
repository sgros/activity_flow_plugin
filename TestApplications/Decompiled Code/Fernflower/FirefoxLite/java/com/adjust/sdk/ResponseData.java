package com.adjust.sdk;

import java.util.Locale;
import org.json.JSONObject;

public class ResponseData {
   public ActivityKind activityKind;
   public String adid;
   public AdjustAttribution attribution;
   public JSONObject jsonResponse;
   public String message;
   public boolean success;
   public String timestamp;
   public boolean willRetry;

   protected ResponseData() {
   }

   public static ResponseData buildResponseData(ActivityPackage var0) {
      ActivityKind var1 = var0.getActivityKind();
      Object var2;
      switch(var1) {
      case SESSION:
         var2 = new SessionResponseData();
         break;
      case CLICK:
         var2 = new SdkClickResponseData();
         break;
      case ATTRIBUTION:
         var2 = new AttributionResponseData();
         break;
      case EVENT:
         var2 = new EventResponseData(var0);
         break;
      default:
         var2 = new ResponseData();
      }

      ((ResponseData)var2).activityKind = var1;
      return (ResponseData)var2;
   }

   public String toString() {
      return String.format(Locale.US, "message:%s timestamp:%s json:%s", this.message, this.timestamp, this.jsonResponse);
   }
}
