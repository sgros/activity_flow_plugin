package org.telegram.messenger.audioinfo.m4a;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.telegram.messenger.audioinfo.util.PositionInputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class MP4Box {
   protected static final String ASCII = "ISO8859_1";
   private MP4Atom child;
   protected final DataInput data;
   private final PositionInputStream input;
   private final MP4Box parent;
   private final String type;

   public MP4Box(PositionInputStream var1, MP4Box var2, String var3) {
      this.input = var1;
      this.parent = var2;
      this.type = var3;
      this.data = new DataInputStream(var1);
   }

   protected MP4Atom getChild() {
      return this.child;
   }

   public PositionInputStream getInput() {
      return this.input;
   }

   public MP4Box getParent() {
      return this.parent;
   }

   public long getPosition() {
      return this.input.getPosition();
   }

   public String getType() {
      return this.type;
   }

   public MP4Atom nextChild() throws IOException {
      MP4Atom var1 = this.child;
      if (var1 != null) {
         var1.skip();
      }

      int var2 = this.data.readInt();
      byte[] var4 = new byte[4];
      this.data.readFully(var4);
      String var3 = new String(var4, "ISO8859_1");
      RangeInputStream var5;
      if (var2 == 1) {
         var5 = new RangeInputStream(this.input, 16L, this.data.readLong() - 16L);
      } else {
         var5 = new RangeInputStream(this.input, 8L, (long)(var2 - 8));
      }

      var1 = new MP4Atom(var5, this, var3);
      this.child = var1;
      return var1;
   }

   public MP4Atom nextChild(String var1) throws IOException {
      MP4Atom var2 = this.nextChild();
      if (var2.getType().matches(var1)) {
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("atom type mismatch, expected ");
         var3.append(var1);
         var3.append(", got ");
         var3.append(var2.getType());
         throw new IOException(var3.toString());
      }
   }
}
