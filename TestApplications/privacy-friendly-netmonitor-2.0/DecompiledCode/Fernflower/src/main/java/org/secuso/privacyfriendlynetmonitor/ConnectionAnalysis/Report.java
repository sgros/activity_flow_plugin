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

   Report(ByteBuffer var1, TLType var2) {
      this.touch();
      this.type = var2;
      if (var2 != TLType.tcp && var2 != TLType.udp) {
         this.initIP6(var1);
      } else {
         this.initIP4(var1);
      }

      UnknownHostException var10000;
      label39: {
         boolean var10001;
         label48: {
            try {
               if (var2 != TLType.tcp && var2 != TLType.udp) {
                  break label48;
               }
            } catch (UnknownHostException var5) {
               var10000 = var5;
               var10001 = false;
               break label39;
            }

            try {
               this.localAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.localAddHex)));
               this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp4(ToolBox.printHexBinary(this.remoteAddHex)));
               return;
            } catch (UnknownHostException var4) {
               var10000 = var4;
               var10001 = false;
               break label39;
            }
         }

         try {
            this.localAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.localAddHex)));
            this.remoteAdd = InetAddress.getByName(ToolBox.hexToIp6(ToolBox.printHexBinary(this.remoteAddHex)));
            return;
         } catch (UnknownHostException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      UnknownHostException var6 = var10000;
      var6.printStackTrace();
   }

   private void initIP4(ByteBuffer var1) {
      var1.position(0);
      byte[] var2 = new byte[2];
      this.localAddHex = new byte[4];
      var1.get(this.localAddHex);
      var1.get(var2);
      this.localPort = ToolBox.twoBytesToInt(var2);
      this.remoteAddHex = new byte[4];
      var1.get(this.remoteAddHex);
      var1.get(var2);
      this.remotePort = ToolBox.twoBytesToInt(var2);
      this.uid = Math.abs(var1.getInt());
      this.state = new byte[1];
      var1.get(this.state);
   }

   private void initIP6(ByteBuffer var1) {
      var1.position(0);
      byte[] var2 = new byte[2];
      this.localAddHex = new byte[16];
      var1.get(this.localAddHex);
      var1.get(var2);
      this.localPort = ToolBox.twoBytesToInt(var2);
      this.remoteAddHex = new byte[16];
      var1.get(this.remoteAddHex);
      var1.get(var2);
      this.remotePort = ToolBox.twoBytesToInt(var2);
      this.uid = Math.abs(var1.getInt());
      this.state = new byte[1];
      var1.get(this.state);
   }

   public void touch() {
      this.timestamp = new Timestamp(System.currentTimeMillis());
   }
}
