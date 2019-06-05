package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

public class ReportEntity {
   private String appName;
   private String connectionInfo;
   private Long id;
   private String localAddress;
   private String localHex;
   private String localPort;
   private String payloadProtocol;
   private String remoteAddress;
   private String remoteHex;
   private String remoteHost;
   private String servicePort;
   private String timeStamp;
   private String transportProtocol;
   private String userID;

   public ReportEntity() {
   }

   public ReportEntity(Long var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14) {
      this.id = var1;
      this.appName = var2;
      this.userID = var3;
      this.timeStamp = var4;
      this.remoteAddress = var5;
      this.remoteHex = var6;
      this.remoteHost = var7;
      this.localAddress = var8;
      this.localHex = var9;
      this.servicePort = var10;
      this.payloadProtocol = var11;
      this.transportProtocol = var12;
      this.localPort = var13;
      this.connectionInfo = var14;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getConnectionInfo() {
      return this.connectionInfo;
   }

   public Long getId() {
      return this.id;
   }

   public String getLocalAddress() {
      return this.localAddress;
   }

   public String getLocalHex() {
      return this.localHex;
   }

   public String getLocalPort() {
      return this.localPort;
   }

   public String getPayloadProtocol() {
      return this.payloadProtocol;
   }

   public String getRemoteAddress() {
      return this.remoteAddress;
   }

   public String getRemoteHex() {
      return this.remoteHex;
   }

   public String getRemoteHost() {
      return this.remoteHost;
   }

   public String getServicePort() {
      return this.servicePort;
   }

   public String getTimeStamp() {
      return this.timeStamp;
   }

   public String getTransportProtocol() {
      return this.transportProtocol;
   }

   public String getUserID() {
      return this.userID;
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public void setConnectionInfo(String var1) {
      this.connectionInfo = var1;
   }

   public void setId(Long var1) {
      this.id = var1;
   }

   public void setLocalAddress(String var1) {
      this.localAddress = var1;
   }

   public void setLocalHex(String var1) {
      this.localHex = var1;
   }

   public void setLocalPort(String var1) {
      this.localPort = var1;
   }

   public void setPayloadProtocol(String var1) {
      this.payloadProtocol = var1;
   }

   public void setRemoteAddress(String var1) {
      this.remoteAddress = var1;
   }

   public void setRemoteHex(String var1) {
      this.remoteHex = var1;
   }

   public void setRemoteHost(String var1) {
      this.remoteHost = var1;
   }

   public void setServicePort(String var1) {
      this.servicePort = var1;
   }

   public void setTimeStamp(String var1) {
      this.timeStamp = var1;
   }

   public void setTransportProtocol(String var1) {
      this.transportProtocol = var1;
   }

   public void setUserID(String var1) {
      this.userID = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ReportEntity{id=");
      var1.append(this.id);
      var1.append(", appName='");
      var1.append(this.appName);
      var1.append('\'');
      var1.append(", userID='");
      var1.append(this.userID);
      var1.append('\'');
      var1.append(", timeStamp='");
      var1.append(this.timeStamp);
      var1.append('\'');
      var1.append(", remoteAddress='");
      var1.append(this.remoteAddress);
      var1.append('\'');
      var1.append(", remoteHex='");
      var1.append(this.remoteHex);
      var1.append('\'');
      var1.append(", remoteHost='");
      var1.append(this.remoteHost);
      var1.append('\'');
      var1.append(", localAddress='");
      var1.append(this.localAddress);
      var1.append('\'');
      var1.append(", localHex='");
      var1.append(this.localHex);
      var1.append('\'');
      var1.append(", servicePort='");
      var1.append(this.servicePort);
      var1.append('\'');
      var1.append(", payloadProtocol='");
      var1.append(this.payloadProtocol);
      var1.append('\'');
      var1.append(", transportProtocol='");
      var1.append(this.transportProtocol);
      var1.append('\'');
      var1.append(", localPort='");
      var1.append(this.localPort);
      var1.append('\'');
      var1.append(", connectionInfo='");
      var1.append(this.connectionInfo);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }

   public String toStringWithoutTimestamp() {
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(this.id);
      var1.append(this.appName);
      var1.append(this.userID);
      var1.append(this.remoteAddress);
      var1.append(this.remoteHex);
      var1.append(this.remoteHost);
      var1.append(this.localAddress);
      var1.append(this.localHex);
      var1.append(this.servicePort);
      var1.append(this.payloadProtocol);
      var1.append(this.transportProtocol);
      var1.append(this.localPort);
      var1.append(this.connectionInfo);
      return var1.toString();
   }
}
