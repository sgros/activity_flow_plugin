// 
// Decompiled by Procyon v0.5.34
// 

package com.adjust.sdk;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Locale;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.io.ObjectStreamField;
import java.io.Serializable;

public class ActivityPackage implements Serializable
{
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long serialVersionUID = -35935556512024097L;
    private ActivityKind activityKind;
    private Map<String, String> callbackParameters;
    private String clientSdk;
    private transient int hashCode;
    private Map<String, String> parameters;
    private Map<String, String> partnerParameters;
    private String path;
    private int retries;
    private String suffix;
    
    static {
        serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("path", String.class), new ObjectStreamField("clientSdk", String.class), new ObjectStreamField("parameters", Map.class), new ObjectStreamField("activityKind", ActivityKind.class), new ObjectStreamField("suffix", String.class), new ObjectStreamField("callbackParameters", Map.class), new ObjectStreamField("partnerParameters", Map.class) };
    }
    
    public ActivityPackage(final ActivityKind activityKind) {
        this.activityKind = ActivityKind.UNKNOWN;
        this.activityKind = activityKind;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        final ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.path = Util.readStringField(fields, "path", null);
        this.clientSdk = Util.readStringField(fields, "clientSdk", null);
        this.parameters = Util.readObjectField(fields, "parameters", (Map<String, String>)null);
        this.activityKind = Util.readObjectField(fields, "activityKind", ActivityKind.UNKNOWN);
        this.suffix = Util.readStringField(fields, "suffix", null);
        this.callbackParameters = Util.readObjectField(fields, "callbackParameters", (Map<String, String>)null);
        this.partnerParameters = Util.readObjectField(fields, "partnerParameters", (Map<String, String>)null);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final ActivityPackage activityPackage = (ActivityPackage)o;
        return Util.equalString(this.path, activityPackage.path) && Util.equalString(this.clientSdk, activityPackage.clientSdk) && Util.equalObject(this.parameters, activityPackage.parameters) && Util.equalEnum(this.activityKind, activityPackage.activityKind) && Util.equalString(this.suffix, activityPackage.suffix) && Util.equalObject(this.callbackParameters, activityPackage.callbackParameters) && Util.equalObject(this.partnerParameters, activityPackage.partnerParameters);
    }
    
    public ActivityKind getActivityKind() {
        return this.activityKind;
    }
    
    public Map<String, String> getCallbackParameters() {
        return this.callbackParameters;
    }
    
    public String getClientSdk() {
        return this.clientSdk;
    }
    
    public String getExtendedString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Path:      %s\n", this.path));
        sb.append(String.format(Locale.US, "ClientSdk: %s\n", this.clientSdk));
        if (this.parameters != null) {
            sb.append("Parameters:");
            for (final Map.Entry<Object, Object> entry : new TreeMap<Object, Object>(this.parameters).entrySet()) {
                sb.append(String.format(Locale.US, "\n\t%-16s %s", entry.getKey(), entry.getValue()));
            }
        }
        return sb.toString();
    }
    
    protected String getFailureMessage() {
        return String.format(Locale.US, "Failed to track %s%s", this.activityKind.toString(), this.suffix);
    }
    
    public Map<String, String> getParameters() {
        return this.parameters;
    }
    
    public Map<String, String> getPartnerParameters() {
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
    
    @Override
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
        return ++this.retries;
    }
    
    public void setCallbackParameters(final Map<String, String> callbackParameters) {
        this.callbackParameters = callbackParameters;
    }
    
    public void setClientSdk(final String clientSdk) {
        this.clientSdk = clientSdk;
    }
    
    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public void setPartnerParameters(final Map<String, String> partnerParameters) {
        this.partnerParameters = partnerParameters;
    }
    
    public void setPath(final String path) {
        this.path = path;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "%s%s", this.activityKind.toString(), this.suffix);
    }
}
