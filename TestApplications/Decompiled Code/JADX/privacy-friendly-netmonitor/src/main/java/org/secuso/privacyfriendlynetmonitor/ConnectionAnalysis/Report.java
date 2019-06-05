package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.graphics.drawable.Drawable;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import org.secuso.privacyfriendlynetmonitor.Assistant.TLType;
import org.secuso.privacyfriendlynetmonitor.Assistant.ToolBox;

public class Report implements Serializable {
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

    Report(ByteBuffer byteBuffer, TLType tLType) {
        touch();
        this.type = tLType;
        if (tLType == TLType.tcp || tLType == TLType.udp) {
            initIP4(byteBuffer);
        } else {
            initIP6(byteBuffer);
        }
        try {
            if (tLType != TLType.tcp) {
                if (tLType != TLType.udp) {
                    this.localAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.localAddHex)));
                    this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.remoteAddHex)));
                    return;
                }
            }
            this.localAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.localAddHex)));
            this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.remoteAddHex)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void touch() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    private void initIP4(ByteBuffer byteBuffer) {
        byteBuffer.position(0);
        byte[] bArr = new byte[2];
        this.localAddHex = new byte[4];
        byteBuffer.get(this.localAddHex);
        byteBuffer.get(bArr);
        this.localPort = ToolBox.twoBytesToInt(bArr);
        this.remoteAddHex = new byte[4];
        byteBuffer.get(this.remoteAddHex);
        byteBuffer.get(bArr);
        this.remotePort = ToolBox.twoBytesToInt(bArr);
        this.uid = Math.abs(byteBuffer.getInt());
        this.state = new byte[1];
        byteBuffer.get(this.state);
    }

    private void initIP6(ByteBuffer byteBuffer) {
        byteBuffer.position(0);
        byte[] bArr = new byte[2];
        this.localAddHex = new byte[16];
        byteBuffer.get(this.localAddHex);
        byteBuffer.get(bArr);
        this.localPort = ToolBox.twoBytesToInt(bArr);
        this.remoteAddHex = new byte[16];
        byteBuffer.get(this.remoteAddHex);
        byteBuffer.get(bArr);
        this.remotePort = ToolBox.twoBytesToInt(bArr);
        this.uid = Math.abs(byteBuffer.getInt());
        this.state = new byte[1];
        byteBuffer.get(this.state);
    }
}
