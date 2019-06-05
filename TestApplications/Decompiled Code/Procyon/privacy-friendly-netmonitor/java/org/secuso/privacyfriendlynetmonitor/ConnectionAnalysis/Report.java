// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import java.net.UnknownHostException;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;
import java.nio.ByteBuffer;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import java.sql.Timestamp;
import java.net.InetAddress;
import android.graphics.drawable.Drawable;
import java.io.Serializable;

public class Report implements Serializable
{
    public String appName;
    public Drawable icon;
    public InetAddress localAdd;
    public byte[] localAddHex;
    public int localPort;
    public String packageName;
    public int pid;
    public InetAddress remoteAdd;
    public byte[] remoteAddHex;
    public int remotePort;
    public byte[] state;
    public Timestamp timestamp;
    public TLType type;
    public int uid;
    
    Report(final ByteBuffer byteBuffer, final TLType type) {
        this.touch();
        this.type = type;
        if (type != TLType.tcp && type != TLType.udp) {
            this.initIP6(byteBuffer);
        }
        else {
            this.initIP4(byteBuffer);
        }
        try {
            if (type != TLType.tcp && type != TLType.udp) {
                this.localAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.localAddHex)));
                this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.remoteAddHex)));
            }
            else {
                this.localAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.localAddHex)));
                this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.remoteAddHex)));
            }
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
    
    private void initIP4(final ByteBuffer byteBuffer) {
        byteBuffer.position(0);
        final byte[] array = new byte[2];
        byteBuffer.get(this.localAddHex = new byte[4]);
        byteBuffer.get(array);
        this.localPort = ToolBox.twoBytesToInt(array);
        byteBuffer.get(this.remoteAddHex = new byte[4]);
        byteBuffer.get(array);
        this.remotePort = ToolBox.twoBytesToInt(array);
        this.uid = Math.abs(byteBuffer.getInt());
        byteBuffer.get(this.state = new byte[1]);
    }
    
    private void initIP6(final ByteBuffer byteBuffer) {
        byteBuffer.position(0);
        final byte[] array = new byte[2];
        byteBuffer.get(this.localAddHex = new byte[16]);
        byteBuffer.get(array);
        this.localPort = ToolBox.twoBytesToInt(array);
        byteBuffer.get(this.remoteAddHex = new byte[16]);
        byteBuffer.get(array);
        this.remotePort = ToolBox.twoBytesToInt(array);
        this.uid = Math.abs(byteBuffer.getInt());
        byteBuffer.get(this.state = new byte[1]);
    }
    
    public void touch() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
