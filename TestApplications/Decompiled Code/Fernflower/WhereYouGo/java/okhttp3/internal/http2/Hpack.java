package okhttp3.internal.http2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;

final class Hpack {
   static final Map NAME_TO_FIRST_INDEX;
   private static final int PREFIX_4_BITS = 15;
   private static final int PREFIX_5_BITS = 31;
   private static final int PREFIX_6_BITS = 63;
   private static final int PREFIX_7_BITS = 127;
   static final Header[] STATIC_HEADER_TABLE;

   static {
      STATIC_HEADER_TABLE = new Header[]{new Header(Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "")};
      NAME_TO_FIRST_INDEX = nameToFirstIndex();
   }

   private Hpack() {
   }

   static ByteString checkLowercase(ByteString var0) throws IOException {
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         byte var3 = var0.getByte(var1);
         if (var3 >= 65 && var3 <= 90) {
            throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + var0.utf8());
         }
      }

      return var0;
   }

   private static Map nameToFirstIndex() {
      LinkedHashMap var0 = new LinkedHashMap(STATIC_HEADER_TABLE.length);

      for(int var1 = 0; var1 < STATIC_HEADER_TABLE.length; ++var1) {
         if (!var0.containsKey(STATIC_HEADER_TABLE[var1].name)) {
            var0.put(STATIC_HEADER_TABLE[var1].name, var1);
         }
      }

      return Collections.unmodifiableMap(var0);
   }

   static final class Reader {
      Header[] dynamicTable;
      int dynamicTableByteCount;
      int headerCount;
      private final List headerList;
      private final int headerTableSizeSetting;
      private int maxDynamicTableByteCount;
      int nextHeaderIndex;
      private final BufferedSource source;

      Reader(int var1, int var2, Source var3) {
         this.headerList = new ArrayList();
         this.dynamicTable = new Header[8];
         this.nextHeaderIndex = this.dynamicTable.length - 1;
         this.headerCount = 0;
         this.dynamicTableByteCount = 0;
         this.headerTableSizeSetting = var1;
         this.maxDynamicTableByteCount = var2;
         this.source = Okio.buffer(var3);
      }

      Reader(int var1, Source var2) {
         this(var1, var1, var2);
      }

      private void adjustDynamicTableByteCount() {
         if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
            if (this.maxDynamicTableByteCount == 0) {
               this.clearDynamicTable();
            } else {
               this.evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
         }

      }

      private void clearDynamicTable() {
         Arrays.fill(this.dynamicTable, (Object)null);
         this.nextHeaderIndex = this.dynamicTable.length - 1;
         this.headerCount = 0;
         this.dynamicTableByteCount = 0;
      }

      private int dynamicTableIndex(int var1) {
         return this.nextHeaderIndex + 1 + var1;
      }

      private int evictToRecoverBytes(int var1) {
         int var2 = 0;
         byte var3 = 0;
         if (var1 > 0) {
            var2 = this.dynamicTable.length - 1;
            int var4 = var1;

            for(var1 = var3; var2 >= this.nextHeaderIndex && var4 > 0; --var2) {
               var4 -= this.dynamicTable[var2].hpackSize;
               this.dynamicTableByteCount -= this.dynamicTable[var2].hpackSize;
               --this.headerCount;
               ++var1;
            }

            System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + var1, this.headerCount);
            this.nextHeaderIndex += var1;
            var2 = var1;
         }

         return var2;
      }

      private ByteString getName(int var1) {
         ByteString var2;
         if (this.isStaticHeader(var1)) {
            var2 = Hpack.STATIC_HEADER_TABLE[var1].name;
         } else {
            var2 = this.dynamicTable[this.dynamicTableIndex(var1 - Hpack.STATIC_HEADER_TABLE.length)].name;
         }

         return var2;
      }

      private void insertIntoDynamicTable(int var1, Header var2) {
         this.headerList.add(var2);
         int var3 = var2.hpackSize;
         int var4 = var3;
         if (var1 != -1) {
            var4 = var3 - this.dynamicTable[this.dynamicTableIndex(var1)].hpackSize;
         }

         if (var4 > this.maxDynamicTableByteCount) {
            this.clearDynamicTable();
         } else {
            int var5 = this.evictToRecoverBytes(this.dynamicTableByteCount + var4 - this.maxDynamicTableByteCount);
            if (var1 == -1) {
               if (this.headerCount + 1 > this.dynamicTable.length) {
                  Header[] var6 = new Header[this.dynamicTable.length * 2];
                  System.arraycopy(this.dynamicTable, 0, var6, this.dynamicTable.length, this.dynamicTable.length);
                  this.nextHeaderIndex = this.dynamicTable.length - 1;
                  this.dynamicTable = var6;
               }

               var1 = this.nextHeaderIndex--;
               this.dynamicTable[var1] = var2;
               ++this.headerCount;
            } else {
               var3 = this.dynamicTableIndex(var1);
               this.dynamicTable[var1 + var3 + var5] = var2;
            }

            this.dynamicTableByteCount += var4;
         }

      }

      private boolean isStaticHeader(int var1) {
         boolean var2;
         if (var1 >= 0 && var1 <= Hpack.STATIC_HEADER_TABLE.length - 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      private int readByte() throws IOException {
         return this.source.readByte() & 255;
      }

      private void readIndexedHeader(int var1) throws IOException {
         if (this.isStaticHeader(var1)) {
            Header var2 = Hpack.STATIC_HEADER_TABLE[var1];
            this.headerList.add(var2);
         } else {
            int var3 = this.dynamicTableIndex(var1 - Hpack.STATIC_HEADER_TABLE.length);
            if (var3 < 0 || var3 > this.dynamicTable.length - 1) {
               throw new IOException("Header index too large " + (var1 + 1));
            }

            this.headerList.add(this.dynamicTable[var3]);
         }

      }

      private void readLiteralHeaderWithIncrementalIndexingIndexedName(int var1) throws IOException {
         this.insertIntoDynamicTable(-1, new Header(this.getName(var1), this.readByteString()));
      }

      private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
         this.insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
      }

      private void readLiteralHeaderWithoutIndexingIndexedName(int var1) throws IOException {
         ByteString var2 = this.getName(var1);
         ByteString var3 = this.readByteString();
         this.headerList.add(new Header(var2, var3));
      }

      private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
         ByteString var1 = Hpack.checkLowercase(this.readByteString());
         ByteString var2 = this.readByteString();
         this.headerList.add(new Header(var1, var2));
      }

      public List getAndResetHeaderList() {
         ArrayList var1 = new ArrayList(this.headerList);
         this.headerList.clear();
         return var1;
      }

      int maxDynamicTableByteCount() {
         return this.maxDynamicTableByteCount;
      }

      ByteString readByteString() throws IOException {
         int var1 = this.readByte();
         boolean var2;
         if ((var1 & 128) == 128) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1 = this.readInt(var1, 127);
         ByteString var3;
         if (var2) {
            var3 = ByteString.of(Huffman.get().decode(this.source.readByteArray((long)var1)));
         } else {
            var3 = this.source.readByteString((long)var1);
         }

         return var3;
      }

      void readHeaders() throws IOException {
         while(!this.source.exhausted()) {
            int var1 = this.source.readByte() & 255;
            if (var1 == 128) {
               throw new IOException("index == 0");
            }

            if ((var1 & 128) == 128) {
               this.readIndexedHeader(this.readInt(var1, 127) - 1);
            } else if (var1 == 64) {
               this.readLiteralHeaderWithIncrementalIndexingNewName();
            } else if ((var1 & 64) == 64) {
               this.readLiteralHeaderWithIncrementalIndexingIndexedName(this.readInt(var1, 63) - 1);
            } else if ((var1 & 32) == 32) {
               this.maxDynamicTableByteCount = this.readInt(var1, 31);
               if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting) {
                  throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount);
               }

               this.adjustDynamicTableByteCount();
            } else if (var1 != 16 && var1 != 0) {
               this.readLiteralHeaderWithoutIndexingIndexedName(this.readInt(var1, 15) - 1);
            } else {
               this.readLiteralHeaderWithoutIndexingNewName();
            }
         }

      }

      int readInt(int var1, int var2) throws IOException {
         var1 &= var2;
         if (var1 >= var2) {
            var1 = 0;

            while(true) {
               int var3 = this.readByte();
               if ((var3 & 128) == 0) {
                  var1 = var2 + (var3 << var1);
                  break;
               }

               var2 += (var3 & 127) << var1;
               var1 += 7;
            }
         }

         return var1;
      }
   }

   static final class Writer {
      private static final int SETTINGS_HEADER_TABLE_SIZE = 4096;
      private static final int SETTINGS_HEADER_TABLE_SIZE_LIMIT = 16384;
      Header[] dynamicTable;
      int dynamicTableByteCount;
      private boolean emitDynamicTableSizeUpdate;
      int headerCount;
      int headerTableSizeSetting;
      int maxDynamicTableByteCount;
      int nextHeaderIndex;
      private final Buffer out;
      private int smallestHeaderTableSizeSetting;
      private final boolean useCompression;

      Writer(int var1, boolean var2, Buffer var3) {
         this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
         this.dynamicTable = new Header[8];
         this.nextHeaderIndex = this.dynamicTable.length - 1;
         this.headerCount = 0;
         this.dynamicTableByteCount = 0;
         this.headerTableSizeSetting = var1;
         this.maxDynamicTableByteCount = var1;
         this.useCompression = var2;
         this.out = var3;
      }

      Writer(Buffer var1) {
         this(4096, true, var1);
      }

      private void adjustDynamicTableByteCount() {
         if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
            if (this.maxDynamicTableByteCount == 0) {
               this.clearDynamicTable();
            } else {
               this.evictToRecoverBytes(this.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
         }

      }

      private void clearDynamicTable() {
         Arrays.fill(this.dynamicTable, (Object)null);
         this.nextHeaderIndex = this.dynamicTable.length - 1;
         this.headerCount = 0;
         this.dynamicTableByteCount = 0;
      }

      private int evictToRecoverBytes(int var1) {
         int var2 = 0;
         byte var3 = 0;
         if (var1 > 0) {
            var2 = this.dynamicTable.length - 1;
            int var4 = var1;

            for(var1 = var3; var2 >= this.nextHeaderIndex && var4 > 0; --var2) {
               var4 -= this.dynamicTable[var2].hpackSize;
               this.dynamicTableByteCount -= this.dynamicTable[var2].hpackSize;
               --this.headerCount;
               ++var1;
            }

            System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + var1, this.headerCount);
            Arrays.fill(this.dynamicTable, this.nextHeaderIndex + 1, this.nextHeaderIndex + 1 + var1, (Object)null);
            this.nextHeaderIndex += var1;
            var2 = var1;
         }

         return var2;
      }

      private void insertIntoDynamicTable(Header var1) {
         int var2 = var1.hpackSize;
         if (var2 > this.maxDynamicTableByteCount) {
            this.clearDynamicTable();
         } else {
            this.evictToRecoverBytes(this.dynamicTableByteCount + var2 - this.maxDynamicTableByteCount);
            if (this.headerCount + 1 > this.dynamicTable.length) {
               Header[] var3 = new Header[this.dynamicTable.length * 2];
               System.arraycopy(this.dynamicTable, 0, var3, this.dynamicTable.length, this.dynamicTable.length);
               this.nextHeaderIndex = this.dynamicTable.length - 1;
               this.dynamicTable = var3;
            }

            int var4 = this.nextHeaderIndex--;
            this.dynamicTable[var4] = var1;
            ++this.headerCount;
            this.dynamicTableByteCount += var2;
         }

      }

      void setHeaderTableSizeSetting(int var1) {
         this.headerTableSizeSetting = var1;
         var1 = Math.min(var1, 16384);
         if (this.maxDynamicTableByteCount != var1) {
            if (var1 < this.maxDynamicTableByteCount) {
               this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, var1);
            }

            this.emitDynamicTableSizeUpdate = true;
            this.maxDynamicTableByteCount = var1;
            this.adjustDynamicTableByteCount();
         }

      }

      void writeByteString(ByteString var1) throws IOException {
         if (this.useCompression && Huffman.get().encodedLength(var1) < var1.size()) {
            Buffer var2 = new Buffer();
            Huffman.get().encode(var1, var2);
            var1 = var2.readByteString();
            this.writeInt(var1.size(), 127, 128);
            this.out.write(var1);
         } else {
            this.writeInt(var1.size(), 127, 0);
            this.out.write(var1);
         }

      }

      void writeHeaders(List var1) throws IOException {
         if (this.emitDynamicTableSizeUpdate) {
            if (this.smallestHeaderTableSizeSetting < this.maxDynamicTableByteCount) {
               this.writeInt(this.smallestHeaderTableSizeSetting, 31, 32);
            }

            this.emitDynamicTableSizeUpdate = false;
            this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
            this.writeInt(this.maxDynamicTableByteCount, 31, 32);
         }

         int var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            Header var4 = (Header)var1.get(var2);
            ByteString var5 = var4.name.toAsciiLowercase();
            ByteString var6 = var4.value;
            byte var7 = -1;
            int var8 = -1;
            Integer var9 = (Integer)Hpack.NAME_TO_FIRST_INDEX.get(var5);
            int var10 = var7;
            int var11;
            if (var9 != null) {
               var11 = var9 + 1;
               var10 = var7;
               var8 = var11;
               if (var11 > 1) {
                  var10 = var7;
                  var8 = var11;
                  if (var11 < 8) {
                     if (Util.equal(Hpack.STATIC_HEADER_TABLE[var11 - 1].value, var6)) {
                        var10 = var11;
                        var8 = var11;
                     } else {
                        var10 = var7;
                        var8 = var11;
                        if (Util.equal(Hpack.STATIC_HEADER_TABLE[var11].value, var6)) {
                           var10 = var11 + 1;
                           var8 = var11;
                        }
                     }
                  }
               }
            }

            int var12 = var10;
            int var14 = var8;
            if (var10 == -1) {
               var11 = this.nextHeaderIndex + 1;
               int var13 = this.dynamicTable.length;

               while(true) {
                  var12 = var10;
                  var14 = var8;
                  if (var11 >= var13) {
                     break;
                  }

                  var14 = var8;
                  if (Util.equal(this.dynamicTable[var11].name, var5)) {
                     if (Util.equal(this.dynamicTable[var11].value, var6)) {
                        var12 = var11 - this.nextHeaderIndex + Hpack.STATIC_HEADER_TABLE.length;
                        var14 = var8;
                        break;
                     }

                     var14 = var8;
                     if (var8 == -1) {
                        var14 = var11 - this.nextHeaderIndex + Hpack.STATIC_HEADER_TABLE.length;
                     }
                  }

                  ++var11;
                  var8 = var14;
               }
            }

            if (var12 != -1) {
               this.writeInt(var12, 127, 128);
            } else if (var14 == -1) {
               this.out.writeByte(64);
               this.writeByteString(var5);
               this.writeByteString(var6);
               this.insertIntoDynamicTable(var4);
            } else if (var5.startsWith(Header.PSEUDO_PREFIX) && !Header.TARGET_AUTHORITY.equals(var5)) {
               this.writeInt(var14, 15, 0);
               this.writeByteString(var6);
            } else {
               this.writeInt(var14, 63, 64);
               this.writeByteString(var6);
               this.insertIntoDynamicTable(var4);
            }
         }

      }

      void writeInt(int var1, int var2, int var3) {
         if (var1 < var2) {
            this.out.writeByte(var3 | var1);
         } else {
            this.out.writeByte(var3 | var2);

            for(var1 -= var2; var1 >= 128; var1 >>>= 7) {
               this.out.writeByte(var1 & 127 | 128);
            }

            this.out.writeByte(var1);
         }

      }
   }
}
