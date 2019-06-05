package com.bumptech.glide.disklrucache;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

class StrictLineReader implements Closeable {
   private byte[] buf;
   private final Charset charset;
   private int end;
   private final InputStream in;
   private int pos;

   public StrictLineReader(InputStream var1, int var2, Charset var3) {
      if (var1 != null && var3 != null) {
         if (var2 >= 0) {
            if (var3.equals(Util.US_ASCII)) {
               this.in = var1;
               this.charset = var3;
               this.buf = new byte[var2];
            } else {
               throw new IllegalArgumentException("Unsupported encoding");
            }
         } else {
            throw new IllegalArgumentException("capacity <= 0");
         }
      } else {
         throw new NullPointerException();
      }
   }

   public StrictLineReader(InputStream var1, Charset var2) {
      this(var1, 8192, var2);
   }

   private void fillBuf() throws IOException {
      int var1 = this.in.read(this.buf, 0, this.buf.length);
      if (var1 != -1) {
         this.pos = 0;
         this.end = var1;
      } else {
         throw new EOFException();
      }
   }

   public void close() throws IOException {
      InputStream var1 = this.in;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.buf != null) {
               this.buf = null;
               this.in.close();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean hasUnterminatedLine() {
      boolean var1;
      if (this.end == -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String readLine() throws IOException {
      InputStream var1 = this.in;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label1224: {
         label1228: {
            try {
               if (this.buf == null) {
                  break label1228;
               }

               if (this.pos >= this.end) {
                  this.fillBuf();
               }
            } catch (Throwable var114) {
               var10000 = var114;
               var10001 = false;
               break label1224;
            }

            int var2;
            try {
               var2 = this.pos;
            } catch (Throwable var111) {
               var10000 = var111;
               var10001 = false;
               break label1224;
            }

            int var4;
            String var115;
            label1230: {
               while(true) {
                  label1210: {
                     label1209: {
                        byte[] var3;
                        try {
                           if (var2 == this.end) {
                              break;
                           }

                           if (this.buf[var2] != 10) {
                              break label1210;
                           }

                           if (var2 == this.pos) {
                              break label1209;
                           }

                           var3 = this.buf;
                        } catch (Throwable var112) {
                           var10000 = var112;
                           var10001 = false;
                           break label1224;
                        }

                        var4 = var2 - 1;
                        if (var3[var4] == 13) {
                           break label1230;
                        }
                     }

                     var4 = var2;
                     break label1230;
                  }

                  ++var2;
               }

               ByteArrayOutputStream var116;
               try {
                  var116 = new ByteArrayOutputStream(this.end - this.pos + 80) {
                     public String toString() {
                        int var1;
                        if (this.count > 0 && this.buf[this.count - 1] == 13) {
                           var1 = this.count - 1;
                        } else {
                           var1 = this.count;
                        }

                        try {
                           String var2 = new String(this.buf, 0, var1, StrictLineReader.this.charset.name());
                           return var2;
                        } catch (UnsupportedEncodingException var3) {
                           throw new AssertionError(var3);
                        }
                     }
                  };
               } catch (Throwable var109) {
                  var10000 = var109;
                  var10001 = false;
                  break label1224;
               }

               label1191:
               while(true) {
                  try {
                     var116.write(this.buf, this.pos, this.end - this.pos);
                     this.end = -1;
                     this.fillBuf();
                     var2 = this.pos;
                  } catch (Throwable var108) {
                     var10000 = var108;
                     var10001 = false;
                     break label1224;
                  }

                  while(true) {
                     try {
                        if (var2 == this.end) {
                           break;
                        }

                        if (this.buf[var2] == 10) {
                           if (var2 != this.pos) {
                              var116.write(this.buf, this.pos, var2 - this.pos);
                           }
                           break label1191;
                        }
                     } catch (Throwable var110) {
                        var10000 = var110;
                        var10001 = false;
                        break label1224;
                     }

                     ++var2;
                  }
               }

               try {
                  this.pos = var2 + 1;
                  var115 = var116.toString();
                  return var115;
               } catch (Throwable var107) {
                  var10000 = var107;
                  var10001 = false;
                  break label1224;
               }
            }

            try {
               var115 = new String(this.buf, this.pos, var4 - this.pos, this.charset.name());
               this.pos = var2 + 1;
               return var115;
            } catch (Throwable var106) {
               var10000 = var106;
               var10001 = false;
               break label1224;
            }
         }

         label1217:
         try {
            IOException var118 = new IOException("LineReader is closed");
            throw var118;
         } catch (Throwable var113) {
            var10000 = var113;
            var10001 = false;
            break label1217;
         }
      }

      while(true) {
         Throwable var117 = var10000;

         try {
            throw var117;
         } catch (Throwable var105) {
            var10000 = var105;
            var10001 = false;
            continue;
         }
      }
   }
}
