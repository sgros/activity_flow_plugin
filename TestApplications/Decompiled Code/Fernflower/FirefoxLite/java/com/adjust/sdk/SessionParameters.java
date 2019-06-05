package com.adjust.sdk;

import java.util.HashMap;
import java.util.Map;

public class SessionParameters {
   Map callbackParameters;
   Map partnerParameters;

   public SessionParameters deepCopy() {
      SessionParameters var1 = new SessionParameters();
      if (this.callbackParameters != null) {
         var1.callbackParameters = new HashMap(this.callbackParameters);
      }

      if (this.partnerParameters != null) {
         var1.partnerParameters = new HashMap(this.partnerParameters);
      }

      return var1;
   }
}
