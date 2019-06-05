package cz.matejcik.openwig.formats;

import cz.matejcik.openwig.platform.FileHandle;
import cz.matejcik.openwig.platform.SeekableFile;
import java.io.IOException;

public class CartridgeFile {
   private static final int CACHE_LIMIT = 128000;
   private static final byte[] CART_ID = new byte[]{2, 10, 67, 65, 82, 84, 0};
   public String author;
   public String code;
   public String description;
   public String device;
   public String filename;
   private int files;
   public int iconId;
   private int[] ids;
   private byte[] lastFile = null;
   private int lastId = -1;
   public double latitude;
   public double longitude;
   public String member;
   public String name;
   private int[] offsets;
   protected Savegame savegame;
   private SeekableFile source;
   public int splashId;
   public String startdesc;
   public String type;
   public String url;
   public String version;

   protected CartridgeFile() {
   }

   private boolean fileOk() throws IOException {
      byte[] var1 = new byte[CART_ID.length];
      this.source.seek(0L);
      this.source.readFully(var1);
      int var2 = 0;

      boolean var3;
      while(true) {
         if (var2 >= var1.length) {
            var3 = true;
            break;
         }

         if (var1[var2] != CART_ID[var2]) {
            var3 = false;
            break;
         }

         ++var2;
      }

      return var3;
   }

   public static CartridgeFile read(SeekableFile var0, FileHandle var1) throws IOException {
      CartridgeFile var2 = new CartridgeFile();
      var2.source = var0;
      if (!var2.fileOk()) {
         throw new IOException("invalid cartridge file");
      } else {
         var2.scanOffsets();
         var2.scanHeader();
         var2.savegame = new Savegame(var1);
         return var2;
      }
   }

   private void scanHeader() throws IOException {
      this.source.readInt();
      this.latitude = this.source.readDouble();
      this.longitude = this.source.readDouble();
      this.source.skip(8L);
      this.source.skip(8L);
      this.splashId = this.source.readShort();
      this.iconId = this.source.readShort();
      this.type = this.source.readString();
      this.member = this.source.readString();
      this.source.skip(8L);
      this.name = this.source.readString();
      this.source.readString();
      this.description = this.source.readString();
      this.startdesc = this.source.readString();
      this.version = this.source.readString();
      this.author = this.source.readString();
      this.url = this.source.readString();
      this.device = this.source.readString();
      this.source.skip(4L);
      this.code = this.source.readString();
   }

   private void scanOffsets() throws IOException {
      this.files = this.source.readShort();
      this.offsets = new int[this.files];
      this.ids = new int[this.files];

      for(int var1 = 0; var1 < this.files; ++var1) {
         this.ids[var1] = this.source.readShort();
         this.offsets[var1] = this.source.readInt();
      }

   }

   public byte[] getBytecode() throws IOException {
      this.source.seek((long)this.offsets[0]);
      byte[] var1 = new byte[this.source.readInt()];
      this.source.readFully(var1);
      return var1;
   }

   public byte[] getFile(int var1) throws IOException {
      byte[] var2;
      if (var1 == this.lastId) {
         var2 = this.lastFile;
      } else if (var1 < 1) {
         var2 = null;
      } else {
         byte var3 = -1;
         int var4 = 0;

         int var5;
         while(true) {
            var5 = var3;
            if (var4 >= this.ids.length) {
               break;
            }

            if (this.ids[var4] == var1) {
               var5 = var4;
               break;
            }

            ++var4;
         }

         if (var5 == -1) {
            var2 = null;
         } else {
            this.source.seek((long)this.offsets[var5]);
            if (this.source.read() < 1) {
               var2 = null;
            } else {
               this.source.readInt();
               var4 = this.source.readInt();
               this.lastFile = null;
               this.lastId = -1;

               byte[] var6;
               try {
                  var6 = new byte[var4];
                  this.source.readFully(var6);
               } catch (OutOfMemoryError var7) {
                  var2 = null;
                  return var2;
               }

               var2 = var6;
               if (var4 < 128000) {
                  this.lastId = var1;
                  this.lastFile = var6;
                  var2 = var6;
               }
            }
         }
      }

      return var2;
   }

   public Savegame getSavegame() throws IOException {
      return this.savegame;
   }
}
