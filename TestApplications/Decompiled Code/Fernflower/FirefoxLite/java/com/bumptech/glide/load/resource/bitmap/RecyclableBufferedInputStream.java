package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
   private volatile byte[] buf;
   private final ArrayPool byteArrayPool;
   private int count;
   private int marklimit;
   private int markpos;
   private int pos;

   public RecyclableBufferedInputStream(InputStream var1, ArrayPool var2) {
      this(var1, var2, 65536);
   }

   RecyclableBufferedInputStream(InputStream var1, ArrayPool var2, int var3) {
      super(var1);
      this.markpos = -1;
      this.byteArrayPool = var2;
      this.buf = (byte[])var2.get(var3, byte[].class);
   }

   private int fillbuf(InputStream var1, byte[] var2) throws IOException {
      int var4;
      if (this.markpos != -1 && this.pos - this.markpos < this.marklimit) {
         int var3;
         byte[] var5;
         if (this.markpos == 0 && this.marklimit > var2.length && this.count == var2.length) {
            var3 = var2.length * 2;
            var4 = var3;
            if (var3 > this.marklimit) {
               var4 = this.marklimit;
            }

            var5 = (byte[])this.byteArrayPool.get(var4, byte[].class);
            System.arraycopy(var2, 0, var5, 0, var2.length);
            this.buf = var5;
            this.byteArrayPool.put(var2, byte[].class);
         } else {
            var5 = var2;
            if (this.markpos > 0) {
               System.arraycopy(var2, this.markpos, var2, 0, var2.length - this.markpos);
               var5 = var2;
            }
         }

         this.pos -= this.markpos;
         this.markpos = 0;
         this.count = 0;
         var3 = var1.read(var5, this.pos, var5.length - this.pos);
         if (var3 <= 0) {
            var4 = this.pos;
         } else {
            var4 = this.pos + var3;
         }

         this.count = var4;
         return var3;
      } else {
         var4 = var1.read(var2);
         if (var4 > 0) {
            this.markpos = -1;
            this.pos = 0;
            this.count = var4;
         }

         return var4;
      }
   }

   private static IOException streamClosed() throws IOException {
      throw new IOException("BufferedInputStream is closed");
   }

   public int available() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label117: {
         boolean var10001;
         label116: {
            InputStream var1;
            try {
               var1 = this.in;
               if (this.buf == null) {
                  break label116;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label117;
            }

            if (var1 != null) {
               int var2;
               int var3;
               int var4;
               try {
                  var2 = this.count;
                  var3 = this.pos;
                  var4 = var1.available();
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label117;
               }

               return var2 - var3 + var4;
            }
         }

         label110:
         try {
            throw streamClosed();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label110;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }

   public void close() throws IOException {
      if (this.buf != null) {
         this.byteArrayPool.put(this.buf, byte[].class);
         this.buf = null;
      }

      InputStream var1 = this.in;
      this.in = null;
      if (var1 != null) {
         var1.close();
      }

   }

   public void fixMarkLimit() {
      synchronized(this){}

      try {
         this.marklimit = this.buf.length;
      } finally {
         ;
      }

   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         this.marklimit = Math.max(this.marklimit, var1);
         this.markpos = this.pos;
      } finally {
         ;
      }

   }

   public boolean markSupported() {
      return true;
   }

   public int read() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label497: {
         byte[] var1;
         InputStream var2;
         boolean var10001;
         try {
            var1 = this.buf;
            var2 = this.in;
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label497;
         }

         if (var1 != null && var2 != null) {
            label498: {
               int var3;
               label483: {
                  try {
                     if (this.pos < this.count) {
                        break label483;
                     }

                     var3 = this.fillbuf(var2, var1);
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label498;
                  }

                  if (var3 == -1) {
                     return -1;
                  }
               }

               byte[] var46 = var1;

               label475: {
                  try {
                     if (var1 == this.buf) {
                        break label475;
                     }

                     var46 = this.buf;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label498;
                  }

                  if (var46 == null) {
                     try {
                        throw streamClosed();
                     } catch (Throwable var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label498;
                     }
                  }
               }

               label467: {
                  try {
                     if (this.count - this.pos > 0) {
                        var3 = this.pos++;
                        break label467;
                     }
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label498;
                  }

                  return -1;
               }

               byte var48 = var46[var3];
               return var48 & 255;
            }
         } else {
            label486:
            try {
               throw streamClosed();
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label486;
            }
         }
      }

      Throwable var47 = var10000;
      throw var47;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label3087: {
         byte[] var4;
         boolean var10001;
         try {
            var4 = this.buf;
         } catch (Throwable var312) {
            var10000 = var312;
            var10001 = false;
            break label3087;
         }

         if (var4 != null) {
            label3088: {
               if (var3 == 0) {
                  return 0;
               }

               InputStream var5;
               try {
                  var5 = this.in;
               } catch (Throwable var311) {
                  var10000 = var311;
                  var10001 = false;
                  break label3088;
               }

               if (var5 == null) {
                  label3004:
                  try {
                     throw streamClosed();
                  } catch (Throwable var301) {
                     var10000 = var301;
                     var10001 = false;
                     break label3004;
                  }
               } else {
                  label3092: {
                     int var6;
                     int var7;
                     label3074: {
                        label3089: {
                           label3072: {
                              label3071: {
                                 try {
                                    if (this.pos >= this.count) {
                                       break label3089;
                                    }

                                    if (this.count - this.pos < var3) {
                                       break label3071;
                                    }
                                 } catch (Throwable var316) {
                                    var10000 = var316;
                                    var10001 = false;
                                    break label3092;
                                 }

                                 var6 = var3;
                                 break label3072;
                              }

                              try {
                                 var6 = this.count - this.pos;
                              } catch (Throwable var309) {
                                 var10000 = var309;
                                 var10001 = false;
                                 break label3092;
                              }
                           }

                           try {
                              System.arraycopy(var4, this.pos, var1, var2, var6);
                              this.pos += var6;
                           } catch (Throwable var308) {
                              var10000 = var308;
                              var10001 = false;
                              break label3092;
                           }

                           if (var6 == var3) {
                              return var6;
                           }

                           try {
                              var7 = var5.available();
                           } catch (Throwable var307) {
                              var10000 = var307;
                              var10001 = false;
                              break label3092;
                           }

                           if (var7 == 0) {
                              return var6;
                           }

                           var7 = var2 + var6;
                           var2 = var3 - var6;
                           var6 = var7;
                           break label3074;
                        }

                        var6 = var2;
                        var2 = var3;
                     }

                     while(true) {
                        int var8;
                        try {
                           var8 = this.markpos;
                        } catch (Throwable var306) {
                           var10000 = var306;
                           var10001 = false;
                           break;
                        }

                        label3059: {
                           int var9;
                           label3091: {
                              var7 = -1;
                              if (var8 == -1) {
                                 try {
                                    if (var2 >= var4.length) {
                                       var9 = var5.read(var1, var6, var2);
                                       break label3091;
                                    }
                                 } catch (Throwable var315) {
                                    var10000 = var315;
                                    var10001 = false;
                                    break;
                                 }
                              }

                              try {
                                 var8 = this.fillbuf(var5, var4);
                              } catch (Throwable var305) {
                                 var10000 = var305;
                                 var10001 = false;
                                 break;
                              }

                              if (var8 == -1) {
                                 if (var2 != var3) {
                                    var7 = var3 - var2;
                                 }

                                 return var7;
                              }

                              byte[] var10 = var4;

                              label3051: {
                                 try {
                                    if (var4 == this.buf) {
                                       break label3051;
                                    }

                                    var10 = this.buf;
                                 } catch (Throwable var314) {
                                    var10000 = var314;
                                    var10001 = false;
                                    break;
                                 }

                                 if (var10 == null) {
                                    try {
                                       throw streamClosed();
                                    } catch (Throwable var300) {
                                       var10000 = var300;
                                       var10001 = false;
                                       break;
                                    }
                                 }
                              }

                              label3045: {
                                 label3044: {
                                    try {
                                       if (this.count - this.pos >= var2) {
                                          break label3044;
                                       }
                                    } catch (Throwable var313) {
                                       var10000 = var313;
                                       var10001 = false;
                                       break;
                                    }

                                    try {
                                       var7 = this.count - this.pos;
                                       break label3045;
                                    } catch (Throwable var304) {
                                       var10000 = var304;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 var7 = var2;
                              }

                              try {
                                 System.arraycopy(var10, this.pos, var1, var6, var7);
                                 this.pos += var7;
                              } catch (Throwable var303) {
                                 var10000 = var303;
                                 var10001 = false;
                                 break;
                              }

                              var8 = var7;
                              var4 = var10;
                              break label3059;
                           }

                           var8 = var9;
                           if (var9 == -1) {
                              if (var2 != var3) {
                                 var7 = var3 - var2;
                              }

                              return var7;
                           }
                        }

                        var2 -= var8;
                        if (var2 == 0) {
                           return var3;
                        }

                        try {
                           var7 = var5.available();
                        } catch (Throwable var310) {
                           var10000 = var310;
                           var10001 = false;
                           break;
                        }

                        if (var7 == 0) {
                           return var3 - var2;
                        }

                        var6 += var8;
                     }
                  }
               }
            }
         } else {
            label3006:
            try {
               throw streamClosed();
            } catch (Throwable var302) {
               var10000 = var302;
               var10001 = false;
               break label3006;
            }
         }
      }

      Throwable var317 = var10000;
      throw var317;
   }

   public void release() {
      synchronized(this){}

      try {
         if (this.buf != null) {
            this.byteArrayPool.put(this.buf, byte[].class);
            this.buf = null;
         }
      } finally {
         ;
      }

   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         if (this.buf == null) {
            IOException var5 = new IOException("Stream is closed");
            throw var5;
         }

         if (-1 == this.markpos) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Mark has been invalidated, pos: ");
            var2.append(this.pos);
            var2.append(" markLimit: ");
            var2.append(this.marklimit);
            RecyclableBufferedInputStream.InvalidMarkException var1 = new RecyclableBufferedInputStream.InvalidMarkException(var2.toString());
            throw var1;
         }

         this.pos = this.markpos;
      } finally {
         ;
      }

   }

   public long skip(long var1) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label895: {
         byte[] var3;
         InputStream var4;
         boolean var10001;
         try {
            var3 = this.buf;
            var4 = this.in;
         } catch (Throwable var100) {
            var10000 = var100;
            var10001 = false;
            break label895;
         }

         if (var3 != null) {
            if (var1 < 1L) {
               return 0L;
            }

            if (var4 == null) {
               label874:
               try {
                  throw streamClosed();
               } catch (Throwable var98) {
                  var10000 = var98;
                  var10001 = false;
                  break label874;
               }
            } else {
               label898: {
                  label886: {
                     try {
                        if ((long)(this.count - this.pos) < var1) {
                           break label886;
                        }

                        this.pos = (int)((long)this.pos + var1);
                     } catch (Throwable var101) {
                        var10000 = var101;
                        var10001 = false;
                        break label898;
                     }

                     return var1;
                  }

                  long var5;
                  int var7;
                  label897: {
                     try {
                        var5 = (long)(this.count - this.pos);
                        this.pos = this.count;
                        if (this.markpos != -1 && var1 <= (long)this.marklimit) {
                           var7 = this.fillbuf(var4, var3);
                           break label897;
                        }
                     } catch (Throwable var97) {
                        var10000 = var97;
                        var10001 = false;
                        break label898;
                     }

                     try {
                        var1 = var4.skip(var1 - var5);
                     } catch (Throwable var96) {
                        var10000 = var96;
                        var10001 = false;
                        break label898;
                     }

                     return var5 + var1;
                  }

                  if (var7 == -1) {
                     return var5;
                  }

                  long var8;
                  try {
                     var8 = (long)(this.count - this.pos);
                  } catch (Throwable var95) {
                     var10000 = var95;
                     var10001 = false;
                     break label898;
                  }

                  long var10 = var1 - var5;
                  if (var8 >= var10) {
                     label851: {
                        try {
                           this.pos = (int)((long)this.pos + var10);
                        } catch (Throwable var93) {
                           var10000 = var93;
                           var10001 = false;
                           break label851;
                        }

                        return var1;
                     }
                  } else {
                     label854: {
                        try {
                           var1 = (long)this.count;
                           var8 = (long)this.pos;
                           this.pos = this.count;
                        } catch (Throwable var94) {
                           var10000 = var94;
                           var10001 = false;
                           break label854;
                        }

                        return var5 + var1 - var8;
                     }
                  }
               }
            }
         } else {
            label876:
            try {
               throw streamClosed();
            } catch (Throwable var99) {
               var10000 = var99;
               var10001 = false;
               break label876;
            }
         }
      }

      Throwable var102 = var10000;
      throw var102;
   }

   public static class InvalidMarkException extends IOException {
      public InvalidMarkException(String var1) {
         super(var1);
      }
   }
}
