// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.result;

public final class WifiParsedResult extends ParsedResult
{
    private final boolean hidden;
    private final String networkEncryption;
    private final String password;
    private final String ssid;
    
    public WifiParsedResult(final String s, final String s2, final String s3) {
        this(s, s2, s3, false);
    }
    
    public WifiParsedResult(final String networkEncryption, final String ssid, final String password, final boolean hidden) {
        super(ParsedResultType.WIFI);
        this.ssid = ssid;
        this.networkEncryption = networkEncryption;
        this.password = password;
        this.hidden = hidden;
    }
    
    @Override
    public String getDisplayResult() {
        final StringBuilder sb = new StringBuilder(80);
        ParsedResult.maybeAppend(this.ssid, sb);
        ParsedResult.maybeAppend(this.networkEncryption, sb);
        ParsedResult.maybeAppend(this.password, sb);
        ParsedResult.maybeAppend(Boolean.toString(this.hidden), sb);
        return sb.toString();
    }
    
    public String getNetworkEncryption() {
        return this.networkEncryption;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getSsid() {
        return this.ssid;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
}
