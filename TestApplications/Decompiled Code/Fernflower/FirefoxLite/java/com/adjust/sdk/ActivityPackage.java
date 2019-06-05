package com.adjust.sdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ActivityPackage implements Serializable {
   private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("path", String.class), new ObjectStreamField("clientSdk", String.class), new ObjectStreamField("parameters", Map.class), new ObjectStreamField("activityKind", ActivityKind.class), new ObjectStreamField("suffix", String.class), new ObjectStreamField("callbackParameters", Map.class), new ObjectStreamField("partnerParameters", Map.class)};
   private static final long serialVersionUID = -35935556512024097L;
   private ActivityKind activityKind;
   private Map callbackParameters;
   private String clientSdk;
   private transient int hashCode;
   private Map parameters;
   private Map partnerParameters;
   private String path;
   private int retries;
   private String suffix;

   public ActivityPackage(ActivityKind var1) {
      this.activityKind = ActivityKind.UNKNOWN;
      this.activityKind = var1;
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      GetField var2 = var1.readFields();
      this.path = Util.readStringField(var2, "path", (String)null);
      this.clientSdk = Util.readStringField(var2, "clientSdk", (String)null);
      this.parameters = (Map)Util.readObjectField(var2, "parameters", (Object)null);
      this.activityKind = (ActivityKind)Util.readObjectField(var2, "activityKind", ActivityKind.UNKNOWN);
      this.suffix = Util.readStringField(var2, "suffix", (String)null);
      this.callbackParameters = (Map)Util.readObjectField(var2, "callbackParameters", (Object)null);
      this.partnerParameters = (Map)Util.readObjectField(var2, "partnerParameters", (Object)null);
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         ActivityPackage var2 = (ActivityPackage)var1;
         if (!Util.equalString(this.path, var2.path)) {
            return false;
         } else if (!Util.equalString(this.clientSdk, var2.clientSdk)) {
            return false;
         } else if (!Util.equalObject(this.parameters, var2.parameters)) {
            return false;
         } else if (!Util.equalEnum(this.activityKind, var2.activityKind)) {
            return false;
         } else if (!Util.equalString(this.suffix, var2.suffix)) {
            return false;
         } else if (!Util.equalObject(this.callbackParameters, var2.callbackParameters)) {
            return false;
         } else {
            return Util.equalObject(this.partnerParameters, var2.partnerParameters);
         }
      }
   }

   public ActivityKind getActivityKind() {
      return this.activityKind;
   }

   public Map getCallbackParameters() {
      return this.callbackParameters;
   }

   public String getClientSdk() {
      return this.clientSdk;
   }

   public String getExtendedString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(String.format(Locale.US, "Path:      %s\n", this.path));
      var1.append(String.format(Locale.US, "ClientSdk: %s\n", this.clientSdk));
      if (this.parameters != null) {
         var1.append("Parameters:");
         Iterator var2 = (new TreeMap(this.parameters)).entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            var1.append(String.format(Locale.US, "\n\t%-16s %s", var3.getKey(), var3.getValue()));
         }
      }

      return var1.toString();
   }

   protected String getFailureMessage() {
      return String.format(Locale.US, "Failed to track %s%s", this.activityKind.toString(), this.suffix);
   }

   public Map getParameters() {
      return this.parameters;
   }

   public Map getPartnerParameters() {
      return this.partnerParameters;
   }

   public String getPath() {
      return this.path;
   }

   public int getRetries() {
      return this.retries;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = 17;
         this.hashCode = this.hashCode * 37 + Util.hashString(this.path);
         this.hashCode = this.hashCode * 37 + Util.hashString(this.clientSdk);
         this.hashCode = this.hashCode * 37 + Util.hashObject(this.parameters);
         this.hashCode = this.hashCode * 37 + Util.hashEnum(this.activityKind);
         this.hashCode = this.hashCode * 37 + Util.hashString(this.suffix);
         this.hashCode = this.hashCode * 37 + Util.hashObject(this.callbackParameters);
         this.hashCode = this.hashCode * 37 + Util.hashObject(this.partnerParameters);
      }

      return this.hashCode;
   }

   public int increaseRetries() {
      ++this.retries;
      return this.retries;
   }

   public void setCallbackParameters(Map var1) {
      this.callbackParameters = var1;
   }

   public void setClientSdk(String var1) {
      this.clientSdk = var1;
   }

   public void setParameters(Map var1) {
      this.parameters = var1;
   }

   public void setPartnerParameters(Map var1) {
      this.partnerParameters = var1;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public void setSuffix(String var1) {
      this.suffix = var1;
   }

   public String toString() {
      return String.format(Locale.US, "%s%s", this.activityKind.toString(), this.suffix);
   }
}
