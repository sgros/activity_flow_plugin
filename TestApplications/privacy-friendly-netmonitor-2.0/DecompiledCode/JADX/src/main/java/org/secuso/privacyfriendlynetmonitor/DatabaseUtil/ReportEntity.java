package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

public class ReportEntity {
    private String appName;
    private String connectionInfo;
    /* renamed from: id */
    private Long f78id;
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

    public ReportEntity(Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13) {
        this.f78id = l;
        this.appName = str;
        this.userID = str2;
        this.timeStamp = str3;
        this.remoteAddress = str4;
        this.remoteHex = str5;
        this.remoteHost = str6;
        this.localAddress = str7;
        this.localHex = str8;
        this.servicePort = str9;
        this.payloadProtocol = str10;
        this.transportProtocol = str11;
        this.localPort = str12;
        this.connectionInfo = str13;
    }

    public Long getId() {
        return this.f78id;
    }

    public void setId(Long l) {
        this.f78id = l;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String str) {
        this.userID = str;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(String str) {
        this.timeStamp = str;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    public void setRemoteAddress(String str) {
        this.remoteAddress = str;
    }

    public String getRemoteHex() {
        return this.remoteHex;
    }

    public void setRemoteHex(String str) {
        this.remoteHex = str;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setRemoteHost(String str) {
        this.remoteHost = str;
    }

    public String getLocalAddress() {
        return this.localAddress;
    }

    public void setLocalAddress(String str) {
        this.localAddress = str;
    }

    public String getLocalHex() {
        return this.localHex;
    }

    public void setLocalHex(String str) {
        this.localHex = str;
    }

    public String getServicePort() {
        return this.servicePort;
    }

    public void setServicePort(String str) {
        this.servicePort = str;
    }

    public String getPayloadProtocol() {
        return this.payloadProtocol;
    }

    public void setPayloadProtocol(String str) {
        this.payloadProtocol = str;
    }

    public String getTransportProtocol() {
        return this.transportProtocol;
    }

    public void setTransportProtocol(String str) {
        this.transportProtocol = str;
    }

    public String getLocalPort() {
        return this.localPort;
    }

    public void setLocalPort(String str) {
        this.localPort = str;
    }

    public String getConnectionInfo() {
        return this.connectionInfo;
    }

    public void setConnectionInfo(String str) {
        this.connectionInfo = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ReportEntity{id=");
        stringBuilder.append(this.f78id);
        stringBuilder.append(", appName='");
        stringBuilder.append(this.appName);
        stringBuilder.append('\'');
        stringBuilder.append(", userID='");
        stringBuilder.append(this.userID);
        stringBuilder.append('\'');
        stringBuilder.append(", timeStamp='");
        stringBuilder.append(this.timeStamp);
        stringBuilder.append('\'');
        stringBuilder.append(", remoteAddress='");
        stringBuilder.append(this.remoteAddress);
        stringBuilder.append('\'');
        stringBuilder.append(", remoteHex='");
        stringBuilder.append(this.remoteHex);
        stringBuilder.append('\'');
        stringBuilder.append(", remoteHost='");
        stringBuilder.append(this.remoteHost);
        stringBuilder.append('\'');
        stringBuilder.append(", localAddress='");
        stringBuilder.append(this.localAddress);
        stringBuilder.append('\'');
        stringBuilder.append(", localHex='");
        stringBuilder.append(this.localHex);
        stringBuilder.append('\'');
        stringBuilder.append(", servicePort='");
        stringBuilder.append(this.servicePort);
        stringBuilder.append('\'');
        stringBuilder.append(", payloadProtocol='");
        stringBuilder.append(this.payloadProtocol);
        stringBuilder.append('\'');
        stringBuilder.append(", transportProtocol='");
        stringBuilder.append(this.transportProtocol);
        stringBuilder.append('\'');
        stringBuilder.append(", localPort='");
        stringBuilder.append(this.localPort);
        stringBuilder.append('\'');
        stringBuilder.append(", connectionInfo='");
        stringBuilder.append(this.connectionInfo);
        stringBuilder.append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public String toStringWithoutTimestamp() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(this.f78id);
        stringBuilder.append(this.appName);
        stringBuilder.append(this.userID);
        stringBuilder.append(this.remoteAddress);
        stringBuilder.append(this.remoteHex);
        stringBuilder.append(this.remoteHost);
        stringBuilder.append(this.localAddress);
        stringBuilder.append(this.localHex);
        stringBuilder.append(this.servicePort);
        stringBuilder.append(this.payloadProtocol);
        stringBuilder.append(this.transportProtocol);
        stringBuilder.append(this.localPort);
        stringBuilder.append(this.connectionInfo);
        return stringBuilder.toString();
    }
}
