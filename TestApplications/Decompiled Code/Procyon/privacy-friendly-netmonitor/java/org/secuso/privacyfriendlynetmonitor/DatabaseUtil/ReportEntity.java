// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.DatabaseUtil;

public class ReportEntity
{
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
    
    public ReportEntity(final Long id, final String appName, final String userID, final String timeStamp, final String remoteAddress, final String remoteHex, final String remoteHost, final String localAddress, final String localHex, final String servicePort, final String payloadProtocol, final String transportProtocol, final String localPort, final String connectionInfo) {
        this.id = id;
        this.appName = appName;
        this.userID = userID;
        this.timeStamp = timeStamp;
        this.remoteAddress = remoteAddress;
        this.remoteHex = remoteHex;
        this.remoteHost = remoteHost;
        this.localAddress = localAddress;
        this.localHex = localHex;
        this.servicePort = servicePort;
        this.payloadProtocol = payloadProtocol;
        this.transportProtocol = transportProtocol;
        this.localPort = localPort;
        this.connectionInfo = connectionInfo;
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
    
    public void setAppName(final String appName) {
        this.appName = appName;
    }
    
    public void setConnectionInfo(final String connectionInfo) {
        this.connectionInfo = connectionInfo;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setLocalAddress(final String localAddress) {
        this.localAddress = localAddress;
    }
    
    public void setLocalHex(final String localHex) {
        this.localHex = localHex;
    }
    
    public void setLocalPort(final String localPort) {
        this.localPort = localPort;
    }
    
    public void setPayloadProtocol(final String payloadProtocol) {
        this.payloadProtocol = payloadProtocol;
    }
    
    public void setRemoteAddress(final String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
    
    public void setRemoteHex(final String remoteHex) {
        this.remoteHex = remoteHex;
    }
    
    public void setRemoteHost(final String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public void setServicePort(final String servicePort) {
        this.servicePort = servicePort;
    }
    
    public void setTimeStamp(final String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public void setTransportProtocol(final String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }
    
    public void setUserID(final String userID) {
        this.userID = userID;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ReportEntity{id=");
        sb.append(this.id);
        sb.append(", appName='");
        sb.append(this.appName);
        sb.append('\'');
        sb.append(", userID='");
        sb.append(this.userID);
        sb.append('\'');
        sb.append(", timeStamp='");
        sb.append(this.timeStamp);
        sb.append('\'');
        sb.append(", remoteAddress='");
        sb.append(this.remoteAddress);
        sb.append('\'');
        sb.append(", remoteHex='");
        sb.append(this.remoteHex);
        sb.append('\'');
        sb.append(", remoteHost='");
        sb.append(this.remoteHost);
        sb.append('\'');
        sb.append(", localAddress='");
        sb.append(this.localAddress);
        sb.append('\'');
        sb.append(", localHex='");
        sb.append(this.localHex);
        sb.append('\'');
        sb.append(", servicePort='");
        sb.append(this.servicePort);
        sb.append('\'');
        sb.append(", payloadProtocol='");
        sb.append(this.payloadProtocol);
        sb.append('\'');
        sb.append(", transportProtocol='");
        sb.append(this.transportProtocol);
        sb.append('\'');
        sb.append(", localPort='");
        sb.append(this.localPort);
        sb.append('\'');
        sb.append(", connectionInfo='");
        sb.append(this.connectionInfo);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    public String toStringWithoutTimestamp() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.id);
        sb.append(this.appName);
        sb.append(this.userID);
        sb.append(this.remoteAddress);
        sb.append(this.remoteHex);
        sb.append(this.remoteHost);
        sb.append(this.localAddress);
        sb.append(this.localHex);
        sb.append(this.servicePort);
        sb.append(this.payloadProtocol);
        sb.append(this.transportProtocol);
        sb.append(this.localPort);
        sb.append(this.connectionInfo);
        return sb.toString();
    }
}
