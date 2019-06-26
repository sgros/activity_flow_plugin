package org.telegram.messenger.audioinfo.m4a;

import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public final class MP4Input extends MP4Box {
   public MP4Input(InputStream var1) {
      super(new PositionInputStream(var1), (MP4Box)null, "");
   }

   public MP4Atom nextChildUpTo(String var1) throws IOException {
      MP4Atom var2;
      do {
         var2 = this.nextChild();
      } while(!var2.getType().matches(var1));

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("mp4[pos=");
      var1.append(this.getPosition());
      var1.append("]");
      return var1.toString();
   }
}
