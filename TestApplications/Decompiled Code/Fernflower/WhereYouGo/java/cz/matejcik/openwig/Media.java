package cz.matejcik.openwig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.krka.kahlua.vm.LuaTable;

public class Media extends EventTable {
   private static int media_no;
   public String altText = null;
   public int id;
   public String type = null;

   public Media() {
      int var1 = media_no++;
      this.id = var1;
   }

   public static void reset() {
      media_no = 1;
   }

   public void deserialize(DataInputStream var1) throws IOException {
      --media_no;
      this.id = var1.readInt();
      if (this.id >= media_no) {
         media_no = this.id + 1;
      }

      super.deserialize(var1);
   }

   public String jarFilename() {
      StringBuilder var1 = (new StringBuilder()).append(String.valueOf(this.id)).append(".");
      String var2;
      if (this.type == null) {
         var2 = "";
      } else {
         var2 = this.type;
      }

      return var1.append(var2).toString();
   }

   public void play() {
      String var1 = null;

      boolean var10001;
      label31: {
         label37: {
            try {
               if ("wav".equals(this.type)) {
                  break label37;
               }
            } catch (IOException var4) {
               var10001 = false;
               return;
            }

            try {
               if (!"mp3".equals(this.type)) {
                  break label31;
               }
            } catch (IOException var3) {
               var10001 = false;
               return;
            }

            var1 = "audio/mpeg";
            break label31;
         }

         var1 = "audio/x-wav";
      }

      try {
         Engine.ui.playSound(Engine.mediaFile(this), var1);
      } catch (IOException var2) {
         var10001 = false;
      }

   }

   public void serialize(DataOutputStream var1) throws IOException {
      var1.writeInt(this.id);
      super.serialize(var1);
   }

   protected void setItem(String var1, Object var2) {
      if ("AltText".equals(var1)) {
         this.altText = (String)var2;
      } else if ("Resources".equals(var1)) {
         LuaTable var5 = (LuaTable)var2;
         int var3 = var5.len();

         for(int var4 = 1; var4 <= var3; ++var4) {
            String var6 = (String)((LuaTable)var5.rawget(new Double((double)var4))).rawget("Type");
            if (!"fdl".equals(var6)) {
               this.type = var6.toLowerCase();
            }
         }
      } else {
         super.setItem(var1, var2);
      }

   }
}
