package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class FileUploadOperation {
   private static final int initialRequestsCount = 8;
   private static final int initialRequestsSlowNetworkCount = 1;
   private static final int maxUploadingKBytes = 2048;
   private static final int maxUploadingSlowNetworkKBytes = 32;
   private static final int minUploadChunkSize = 128;
   private static final int minUploadChunkSlowNetworkSize = 32;
   private long availableSize;
   private SparseArray cachedResults = new SparseArray();
   private int currentAccount;
   private long currentFileId;
   private int currentPartNum;
   private int currentType;
   private int currentUploadRequetsCount;
   private FileUploadOperation.FileUploadOperationDelegate delegate;
   private int estimatedSize;
   private String fileKey;
   private int fingerprint;
   private ArrayList freeRequestIvs;
   private boolean isBigFile;
   private boolean isEncrypted;
   private boolean isLastPart;
   private byte[] iv;
   private byte[] ivChange;
   private byte[] key;
   private int lastSavedPartNum;
   private int maxRequestsCount;
   private boolean nextPartFirst;
   private int operationGuid;
   private SharedPreferences preferences;
   private byte[] readBuffer;
   private long readBytesCount;
   private int requestNum;
   private SparseIntArray requestTokens = new SparseIntArray();
   private int saveInfoTimes;
   private boolean slowNetwork;
   private boolean started;
   private int state;
   private RandomAccessFile stream;
   private long totalFileSize;
   private int totalPartsCount;
   private int uploadChunkSize = 65536;
   private boolean uploadFirstPartLater;
   private int uploadStartTime;
   private long uploadedBytesCount;
   private String uploadingFilePath;

   public FileUploadOperation(int var1, String var2, boolean var3, int var4, int var5) {
      this.currentAccount = var1;
      this.uploadingFilePath = var2;
      this.isEncrypted = var3;
      this.estimatedSize = var4;
      this.currentType = var5;
      if (var4 != 0 && !this.isEncrypted) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.uploadFirstPartLater = var3;
   }

   private void calcTotalPartsCount() {
      long var1;
      int var3;
      if (this.uploadFirstPartLater) {
         if (this.isBigFile) {
            var1 = this.totalFileSize;
            var3 = this.uploadChunkSize;
            this.totalPartsCount = (int)(var1 - (long)var3 + (long)var3 - 1L) / var3 + 1;
         } else {
            var1 = this.totalFileSize;
            var3 = this.uploadChunkSize;
            this.totalPartsCount = (int)(var1 - 1024L + (long)var3 - 1L) / var3 + 1;
         }
      } else {
         var1 = this.totalFileSize;
         var3 = this.uploadChunkSize;
         this.totalPartsCount = (int)(var1 + (long)var3 - 1L) / var3;
      }

   }

   private void cleanup() {
      if (this.preferences == null) {
         this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
      }

      Editor var1 = this.preferences.edit();
      StringBuilder var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_time");
      var1 = var1.remove(var2.toString());
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_size");
      Editor var5 = var1.remove(var2.toString());
      StringBuilder var4 = new StringBuilder();
      var4.append(this.fileKey);
      var4.append("_uploaded");
      var5 = var5.remove(var4.toString());
      var4 = new StringBuilder();
      var4.append(this.fileKey);
      var4.append("_id");
      var1 = var5.remove(var4.toString());
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_iv");
      var1 = var1.remove(var2.toString());
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_key");
      var1 = var1.remove(var2.toString());
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_ivc");
      var1.remove(var2.toString()).commit();

      try {
         if (this.stream != null) {
            this.stream.close();
            this.stream = null;
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   private void startUploadRequest() {
      if (this.state == 1) {
         Exception var10000;
         Exception var90;
         label653: {
            boolean var10001;
            label657: {
               long var3;
               int var5;
               int var6;
               int var8;
               int var11;
               int var12;
               byte[] var87;
               label690: {
                  label649: {
                     File var1;
                     try {
                        this.started = true;
                        if (this.stream != null) {
                           break label690;
                        }

                        var1 = new File(this.uploadingFilePath);
                        if (AndroidUtilities.isInternalUri(Uri.fromFile(var1))) {
                           break label657;
                        }

                        RandomAccessFile var2 = new RandomAccessFile(var1, "r");
                        this.stream = var2;
                        if (this.estimatedSize != 0) {
                           this.totalFileSize = (long)this.estimatedSize;
                           break label649;
                        }
                     } catch (Exception var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label653;
                     }

                     try {
                        this.totalFileSize = var1.length();
                     } catch (Exception var51) {
                        var10000 = var51;
                        var10001 = false;
                        break label653;
                     }
                  }

                  try {
                     if (this.totalFileSize > 10485760L) {
                        this.isBigFile = true;
                     }
                  } catch (Exception var50) {
                     var10000 = var50;
                     var10001 = false;
                     break label653;
                  }

                  label634: {
                     label633: {
                        try {
                           if (this.slowNetwork) {
                              break label633;
                           }
                        } catch (Exception var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label653;
                        }

                        var3 = 128L;
                        break label634;
                     }

                     var3 = 32L;
                  }

                  label660: {
                     try {
                        this.uploadChunkSize = (int)Math.max(var3, (this.totalFileSize + 3072000L - 1L) / 3072000L);
                        if (1024 % this.uploadChunkSize == 0) {
                           break label660;
                        }
                     } catch (Exception var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label653;
                     }

                     var5 = 64;

                     while(true) {
                        try {
                           if (this.uploadChunkSize <= var5) {
                              break;
                           }
                        } catch (Exception var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label653;
                        }

                        var5 *= 2;
                     }

                     try {
                        this.uploadChunkSize = var5;
                     } catch (Exception var49) {
                        var10000 = var49;
                        var10001 = false;
                        break label653;
                     }
                  }

                  short var85;
                  label615: {
                     label614: {
                        try {
                           if (this.slowNetwork) {
                              break label614;
                           }
                        } catch (Exception var71) {
                           var10000 = var71;
                           var10001 = false;
                           break label653;
                        }

                        var85 = 2048;
                        break label615;
                     }

                     var85 = 32;
                  }

                  label661: {
                     try {
                        this.maxRequestsCount = Math.max(1, var85 / this.uploadChunkSize);
                        if (!this.isEncrypted) {
                           break label661;
                        }

                        ArrayList var78 = new ArrayList(this.maxRequestsCount);
                        this.freeRequestIvs = var78;
                     } catch (Exception var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label653;
                     }

                     var5 = 0;

                     while(true) {
                        try {
                           if (var5 >= this.maxRequestsCount) {
                              break;
                           }

                           this.freeRequestIvs.add(new byte[32]);
                        } catch (Exception var69) {
                           var10000 = var69;
                           var10001 = false;
                           break label653;
                        }

                        ++var5;
                     }
                  }

                  StringBuilder var76;
                  String var79;
                  label596: {
                     label595: {
                        try {
                           this.uploadChunkSize *= 1024;
                           this.calcTotalPartsCount();
                           this.readBuffer = new byte[this.uploadChunkSize];
                           var76 = new StringBuilder();
                           var76.append(this.uploadingFilePath);
                           if (this.isEncrypted) {
                              break label595;
                           }
                        } catch (Exception var68) {
                           var10000 = var68;
                           var10001 = false;
                           break label653;
                        }

                        var79 = "";
                        break label596;
                     }

                     var79 = "enc";
                  }

                  boolean var89;
                  label589: {
                     label662: {
                        SharedPreferences var80;
                        label587: {
                           label586: {
                              label663: {
                                 String var81;
                                 try {
                                    var76.append(var79);
                                    this.fileKey = Utilities.MD5(var76.toString());
                                    var80 = this.preferences;
                                    var76 = new StringBuilder();
                                    var76.append(this.fileKey);
                                    var76.append("_size");
                                    var3 = var80.getLong(var76.toString(), 0L);
                                    this.uploadStartTime = (int)(System.currentTimeMillis() / 1000L);
                                    if (this.uploadFirstPartLater || this.nextPartFirst || this.estimatedSize != 0 || var3 != this.totalFileSize) {
                                       break label662;
                                    }

                                    SharedPreferences var77 = this.preferences;
                                    StringBuilder var82 = new StringBuilder();
                                    var82.append(this.fileKey);
                                    var82.append("_id");
                                    this.currentFileId = var77.getLong(var82.toString(), 0L);
                                    var80 = this.preferences;
                                    var76 = new StringBuilder();
                                    var76.append(this.fileKey);
                                    var76.append("_time");
                                    var6 = var80.getInt(var76.toString(), 0);
                                    var80 = this.preferences;
                                    var76 = new StringBuilder();
                                    var76.append(this.fileKey);
                                    var76.append("_uploaded");
                                    var3 = var80.getLong(var76.toString(), 0L);
                                    if (!this.isEncrypted) {
                                       break label663;
                                    }

                                    var77 = this.preferences;
                                    var82 = new StringBuilder();
                                    var82.append(this.fileKey);
                                    var82.append("_iv");
                                    var79 = var77.getString(var82.toString(), (String)null);
                                    SharedPreferences var7 = this.preferences;
                                    var76 = new StringBuilder();
                                    var76.append(this.fileKey);
                                    var76.append("_key");
                                    var81 = var7.getString(var76.toString(), (String)null);
                                 } catch (Exception var67) {
                                    var10000 = var67;
                                    var10001 = false;
                                    break label653;
                                 }

                                 if (var79 == null || var81 == null) {
                                    break label586;
                                 }

                                 try {
                                    this.key = Utilities.hexToBytes(var81);
                                    this.iv = Utilities.hexToBytes(var79);
                                    if (this.key == null || this.iv == null || this.key.length != 32 || this.iv.length != 32) {
                                       break label586;
                                    }

                                    this.ivChange = new byte[32];
                                    System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                                 } catch (Exception var66) {
                                    var10000 = var66;
                                    var10001 = false;
                                    break label653;
                                 }
                              }

                              var89 = false;
                              break label587;
                           }

                           var89 = true;
                        }

                        if (!var89 && var6 != 0) {
                           label562: {
                              label666: {
                                 try {
                                    if (this.isBigFile && var6 < this.uploadStartTime - 86400) {
                                       break label666;
                                    }
                                 } catch (Exception var65) {
                                    var10000 = var65;
                                    var10001 = false;
                                    break label653;
                                 }

                                 var8 = var6;

                                 try {
                                    if (this.isBigFile) {
                                       break label562;
                                    }
                                 } catch (Exception var64) {
                                    var10000 = var64;
                                    var10001 = false;
                                    break label653;
                                 }

                                 var8 = var6;

                                 try {
                                    if ((float)var6 >= (float)this.uploadStartTime - 5400.0F) {
                                       break label562;
                                    }
                                 } catch (Exception var63) {
                                    var10000 = var63;
                                    var10001 = false;
                                    break label653;
                                 }
                              }

                              var8 = 0;
                           }

                           if (var8 == 0) {
                              break label589;
                           }

                           if (var3 > 0L) {
                              label687: {
                                 boolean var91;
                                 label667: {
                                    try {
                                       this.readBytesCount = var3;
                                       this.currentPartNum = (int)(var3 / (long)this.uploadChunkSize);
                                       if (!this.isBigFile) {
                                          break label667;
                                       }
                                    } catch (Exception var62) {
                                       var10000 = var62;
                                       var10001 = false;
                                       break label653;
                                    }

                                    var91 = var89;

                                    try {
                                       this.stream.seek(var3);
                                    } catch (Exception var42) {
                                       var10000 = var42;
                                       var10001 = false;
                                       break label653;
                                    }

                                    var89 = var89;

                                    try {
                                       if (!this.isEncrypted) {
                                          break label589;
                                       }

                                       var80 = this.preferences;
                                       var76 = new StringBuilder();
                                       var76.append(this.fileKey);
                                       var76.append("_ivc");
                                       var79 = var80.getString(var76.toString(), (String)null);
                                    } catch (Exception var57) {
                                       var10000 = var57;
                                       var10001 = false;
                                       break label653;
                                    }

                                    if (var79 != null) {
                                       label668: {
                                          try {
                                             this.ivChange = Utilities.hexToBytes(var79);
                                             if (this.ivChange == null) {
                                                break label668;
                                             }
                                          } catch (Exception var61) {
                                             var10000 = var61;
                                             var10001 = false;
                                             break label653;
                                          }

                                          var89 = var91;

                                          try {
                                             if (this.ivChange.length == 32) {
                                                break label589;
                                             }
                                          } catch (Exception var56) {
                                             var10000 = var56;
                                             var10001 = false;
                                             break label653;
                                          }
                                       }

                                       try {
                                          this.readBytesCount = 0L;
                                          this.currentPartNum = 0;
                                          break label687;
                                       } catch (Exception var41) {
                                          var10000 = var41;
                                          var10001 = false;
                                          break label653;
                                       }
                                    } else {
                                       try {
                                          this.readBytesCount = 0L;
                                          this.currentPartNum = 0;
                                          break label687;
                                       } catch (Exception var40) {
                                          var10000 = var40;
                                          var10001 = false;
                                          break label653;
                                       }
                                    }
                                 }

                                 var6 = 0;

                                 while(true) {
                                    var3 = (long)var6;

                                    long var9;
                                    try {
                                       var9 = this.readBytesCount;
                                       var11 = this.uploadChunkSize;
                                    } catch (Exception var43) {
                                       var10000 = var43;
                                       var10001 = false;
                                       break label653;
                                    }

                                    var91 = var89;
                                    var89 = var89;

                                    label501: {
                                       label500: {
                                          try {
                                             if (var3 >= var9 / (long)var11) {
                                                break label589;
                                             }

                                             var11 = this.stream.read(this.readBuffer);
                                             if (!this.isEncrypted) {
                                                break label500;
                                             }
                                          } catch (Exception var58) {
                                             var10000 = var58;
                                             var10001 = false;
                                             break label653;
                                          }

                                          if (var11 % 16 != 0) {
                                             var5 = 16 - var11 % 16 + 0;
                                             break label501;
                                          }
                                       }

                                       var5 = 0;
                                    }

                                    NativeByteBuffer var86;
                                    try {
                                       var86 = new NativeByteBuffer;
                                    } catch (Exception var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label653;
                                    }

                                    var12 = var11 + var5;

                                    label510: {
                                       try {
                                          var86.<init>(var12);
                                          if (var11 == this.uploadChunkSize && this.totalPartsCount != this.currentPartNum + 1) {
                                             break label510;
                                          }
                                       } catch (Exception var59) {
                                          var10000 = var59;
                                          var10001 = false;
                                          break label653;
                                       }

                                       try {
                                          this.isLastPart = true;
                                       } catch (Exception var47) {
                                          var10000 = var47;
                                          var10001 = false;
                                          break label653;
                                       }
                                    }

                                    label670: {
                                       try {
                                          var86.writeBytes(this.readBuffer, 0, var11);
                                          if (!this.isEncrypted) {
                                             break label670;
                                          }
                                       } catch (Exception var60) {
                                          var10000 = var60;
                                          var10001 = false;
                                          break label653;
                                       }

                                       var11 = 0;

                                       while(true) {
                                          if (var11 >= var5) {
                                             try {
                                                Utilities.aesIgeEncryption(var86.buffer, this.key, this.ivChange, true, true, 0, var12);
                                                break;
                                             } catch (Exception var45) {
                                                var10000 = var45;
                                                var10001 = false;
                                                break label653;
                                             }
                                          }

                                          try {
                                             var86.writeByte((int)0);
                                          } catch (Exception var46) {
                                             var10000 = var46;
                                             var10001 = false;
                                             break label653;
                                          }

                                          ++var11;
                                       }
                                    }

                                    try {
                                       var86.reuse();
                                    } catch (Exception var44) {
                                       var10000 = var44;
                                       var10001 = false;
                                       break label653;
                                    }

                                    ++var6;
                                    var89 = var91;
                                 }
                              }
                           }
                        }
                     }

                     var89 = true;
                  }

                  if (var89) {
                     try {
                        if (this.isEncrypted) {
                           this.iv = new byte[32];
                           this.key = new byte[32];
                           this.ivChange = new byte[32];
                           Utilities.random.nextBytes(this.iv);
                           Utilities.random.nextBytes(this.key);
                           System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                        }
                     } catch (Exception var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label653;
                     }

                     try {
                        this.currentFileId = Utilities.random.nextLong();
                        if (!this.nextPartFirst && !this.uploadFirstPartLater && this.estimatedSize == 0) {
                           this.storeFileUploadInfo();
                        }
                     } catch (Exception var39) {
                        var10000 = var39;
                        var10001 = false;
                        break label653;
                     }
                  }

                  boolean var13;
                  try {
                     var13 = this.isEncrypted;
                  } catch (Exception var38) {
                     var10000 = var38;
                     var10001 = false;
                     break label653;
                  }

                  if (var13) {
                     label689: {
                        label673: {
                           try {
                              MessageDigest var83 = MessageDigest.getInstance("MD5");
                              var87 = new byte[64];
                              System.arraycopy(this.key, 0, var87, 0, 32);
                              System.arraycopy(this.iv, 0, var87, 32, 32);
                              var87 = var83.digest(var87);
                           } catch (Exception var54) {
                              var10000 = var54;
                              var10001 = false;
                              break label673;
                           }

                           var5 = 0;

                           while(true) {
                              if (var5 >= 4) {
                                 break label689;
                              }

                              try {
                                 this.fingerprint |= ((var87[var5] ^ var87[var5 + 4]) & 255) << var5 * 8;
                              } catch (Exception var53) {
                                 var10000 = var53;
                                 var10001 = false;
                                 break;
                              }

                              ++var5;
                           }
                        }

                        var90 = var10000;

                        try {
                           FileLog.e((Throwable)var90);
                        } catch (Exception var37) {
                           var10000 = var37;
                           var10001 = false;
                           break label653;
                        }
                     }
                  }

                  label692: {
                     try {
                        this.uploadedBytesCount = this.readBytesCount;
                        this.lastSavedPartNum = this.currentPartNum;
                        if (!this.uploadFirstPartLater) {
                           break label690;
                        }

                        if (this.isBigFile) {
                           this.stream.seek((long)this.uploadChunkSize);
                           this.readBytesCount = (long)this.uploadChunkSize;
                           break label692;
                        }
                     } catch (Exception var52) {
                        var10000 = var52;
                        var10001 = false;
                        break label653;
                     }

                     try {
                        this.stream.seek(1024L);
                        this.readBytesCount = 1024L;
                     } catch (Exception var36) {
                        var10000 = var36;
                        var10001 = false;
                        break label653;
                     }
                  }

                  try {
                     this.currentPartNum = 1;
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label653;
                  }
               }

               try {
                  if (this.estimatedSize != 0 && this.readBytesCount + (long)this.uploadChunkSize > this.availableSize) {
                     return;
                  }
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label653;
               }

               label410: {
                  label409: {
                     label693: {
                        try {
                           if (!this.nextPartFirst) {
                              break label409;
                           }

                           this.stream.seek(0L);
                           if (this.isBigFile) {
                              var5 = this.stream.read(this.readBuffer);
                              break label693;
                           }
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label653;
                        }

                        try {
                           var5 = this.stream.read(this.readBuffer, 0, 1024);
                        } catch (Exception var32) {
                           var10000 = var32;
                           var10001 = false;
                           break label653;
                        }
                     }

                     try {
                        this.currentPartNum = 0;
                        break label410;
                     } catch (Exception var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label653;
                     }
                  }

                  try {
                     var5 = this.stream.read(this.readBuffer);
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label653;
                  }
               }

               if (var5 == -1) {
                  return;
               }

               label391: {
                  label390: {
                     try {
                        if (!this.isEncrypted) {
                           break label390;
                        }
                     } catch (Exception var29) {
                        var10000 = var29;
                        var10001 = false;
                        break label653;
                     }

                     if (var5 % 16 != 0) {
                        var8 = 16 - var5 % 16 + 0;
                        break label391;
                     }
                  }

                  var8 = 0;
               }

               NativeByteBuffer var88;
               try {
                  var88 = new NativeByteBuffer;
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label653;
               }

               var11 = var5 + var8;

               label683: {
                  try {
                     var88.<init>(var11);
                     if (!this.nextPartFirst && var5 == this.uploadChunkSize && (this.estimatedSize != 0 || this.totalPartsCount != this.currentPartNum + 1)) {
                        break label683;
                     }
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label653;
                  }

                  try {
                     if (this.uploadFirstPartLater) {
                        this.nextPartFirst = true;
                        this.uploadFirstPartLater = false;
                        break label683;
                     }
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label653;
                  }

                  try {
                     this.isLastPart = true;
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label653;
                  }
               }

               label357: {
                  label678: {
                     try {
                        var88.writeBytes(this.readBuffer, 0, var5);
                        if (!this.isEncrypted) {
                           break label678;
                        }
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label653;
                     }

                     for(var6 = 0; var6 < var8; ++var6) {
                        try {
                           var88.writeByte((int)0);
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label653;
                        }
                     }

                     try {
                        Utilities.aesIgeEncryption(var88.buffer, this.key, this.ivChange, true, true, 0, var11);
                        var87 = (byte[])this.freeRequestIvs.get(0);
                        System.arraycopy(this.ivChange, 0, var87, 0, 32);
                        this.freeRequestIvs.remove(0);
                        break label357;
                     } catch (Exception var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label653;
                     }
                  }

                  var87 = null;
               }

               Object var84;
               label339: {
                  label680: {
                     label694: {
                        try {
                           if (!this.isBigFile) {
                              break label680;
                           }

                           var84 = new TLRPC.TL_upload_saveBigFilePart();
                           var8 = this.currentPartNum;
                           ((TLRPC.TL_upload_saveBigFilePart)var84).file_part = var8;
                           ((TLRPC.TL_upload_saveBigFilePart)var84).file_id = this.currentFileId;
                           if (this.estimatedSize != 0) {
                              ((TLRPC.TL_upload_saveBigFilePart)var84).file_total_parts = -1;
                              break label694;
                           }
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label653;
                        }

                        try {
                           ((TLRPC.TL_upload_saveBigFilePart)var84).file_total_parts = this.totalPartsCount;
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label653;
                        }
                     }

                     try {
                        ((TLRPC.TL_upload_saveBigFilePart)var84).bytes = var88;
                        break label339;
                     } catch (Exception var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label653;
                     }
                  }

                  try {
                     var84 = new TLRPC.TL_upload_saveFilePart();
                     var8 = this.currentPartNum;
                     ((TLRPC.TL_upload_saveFilePart)var84).file_part = var8;
                     ((TLRPC.TL_upload_saveFilePart)var84).file_id = this.currentFileId;
                     ((TLRPC.TL_upload_saveFilePart)var84).bytes = var88;
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label653;
                  }
               }

               try {
                  if (this.isLastPart && this.nextPartFirst) {
                     this.nextPartFirst = false;
                     this.currentPartNum = this.totalPartsCount - 1;
                     this.stream.seek(this.totalFileSize);
                  }
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label653;
               }

               try {
                  this.readBytesCount += (long)var5;
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label653;
               }

               ++this.currentPartNum;
               ++this.currentUploadRequetsCount;
               var11 = this.requestNum++;
               var3 = (long)(var8 + var5);
               var12 = ((TLObject)var84).getObjectSize();
               int var14 = this.operationGuid;
               if (this.slowNetwork) {
                  var6 = 4;
               } else {
                  var6 = var11 % 4 << 16 | 4;
               }

               var5 = ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var84, new _$$Lambda$FileUploadOperation$XMcVvcrqfWd56m49RmvLW8_t4Os(this, var14, var12 + 4, var87, var11, var5, var8, var3, (TLObject)var84), (QuickAckDelegate)null, new _$$Lambda$FileUploadOperation$H_o0ouVev_JFhE9lBzpUHg6WYPI(this), 0, Integer.MAX_VALUE, var6, true);
               this.requestTokens.put(var11, var5);
               return;
            }

            try {
               var90 = new Exception("trying to upload internal file");
               throw var90;
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
            }
         }

         var90 = var10000;
         FileLog.e((Throwable)var90);
         this.state = 4;
         this.delegate.didFailedUploadingFile(this);
         this.cleanup();
      }
   }

   private void storeFileUploadInfo() {
      Editor var1 = this.preferences.edit();
      StringBuilder var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_time");
      var1.putInt(var2.toString(), this.uploadStartTime);
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_size");
      var1.putLong(var2.toString(), this.totalFileSize);
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_id");
      var1.putLong(var2.toString(), this.currentFileId);
      var2 = new StringBuilder();
      var2.append(this.fileKey);
      var2.append("_uploaded");
      var1.remove(var2.toString());
      if (this.isEncrypted) {
         var2 = new StringBuilder();
         var2.append(this.fileKey);
         var2.append("_iv");
         var1.putString(var2.toString(), Utilities.bytesToHex(this.iv));
         var2 = new StringBuilder();
         var2.append(this.fileKey);
         var2.append("_ivc");
         var1.putString(var2.toString(), Utilities.bytesToHex(this.ivChange));
         var2 = new StringBuilder();
         var2.append(this.fileKey);
         var2.append("_key");
         var1.putString(var2.toString(), Utilities.bytesToHex(this.key));
      }

      var1.commit();
   }

   public void cancel() {
      if (this.state != 3) {
         this.state = 2;
         Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$1Av0AtRL3UNZuURFidi06RFneIU(this));
         this.delegate.didFailedUploadingFile(this);
         this.cleanup();
      }
   }

   protected void checkNewDataAvailable(long var1, long var3) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$gwhBTZdvm3NdxGlA_jj4tE_lywA(this, var3, var1));
   }

   public long getTotalFileSize() {
      return this.totalFileSize;
   }

   // $FF: synthetic method
   public void lambda$cancel$2$FileUploadOperation() {
      for(int var1 = 0; var1 < this.requestTokens.size(); ++var1) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(var1), true);
      }

   }

   // $FF: synthetic method
   public void lambda$checkNewDataAvailable$3$FileUploadOperation(long var1, long var3) {
      if (this.estimatedSize != 0 && var1 != 0L) {
         this.estimatedSize = 0;
         this.totalFileSize = var1;
         this.calcTotalPartsCount();
         if (!this.uploadFirstPartLater && this.started) {
            this.storeFileUploadInfo();
         }
      }

      if (var1 <= 0L) {
         var1 = var3;
      }

      this.availableSize = var1;
      if (this.currentUploadRequetsCount < this.maxRequestsCount) {
         this.startUploadRequest();
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$FileUploadOperation() {
      if (this.currentUploadRequetsCount < this.maxRequestsCount) {
         this.startUploadRequest();
      }

   }

   // $FF: synthetic method
   public void lambda$onNetworkChanged$1$FileUploadOperation(boolean var1) {
      if (this.slowNetwork != var1) {
         this.slowNetwork = var1;
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var2 = new StringBuilder();
            var2.append("network changed to slow = ");
            var2.append(this.slowNetwork);
            FileLog.d(var2.toString());
         }

         int var3 = 0;
         int var4 = 0;

         while(true) {
            int var5 = this.requestTokens.size();
            byte var6 = 1;
            if (var4 >= var5) {
               this.requestTokens.clear();
               this.cleanup();
               this.isLastPart = false;
               this.nextPartFirst = false;
               this.requestNum = 0;
               this.currentPartNum = 0;
               this.readBytesCount = 0L;
               this.uploadedBytesCount = 0L;
               this.saveInfoTimes = 0;
               this.key = null;
               this.iv = null;
               this.ivChange = null;
               this.currentUploadRequetsCount = 0;
               this.lastSavedPartNum = 0;
               this.uploadFirstPartLater = false;
               this.cachedResults.clear();
               ++this.operationGuid;
               byte var7;
               if (this.slowNetwork) {
                  var7 = var6;
               } else {
                  var7 = 8;
               }

               while(var3 < var7) {
                  this.startUploadRequest();
                  ++var3;
               }
               break;
            }

            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requestTokens.valueAt(var4), true);
            ++var4;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$start$0$FileUploadOperation() {
      Context var1 = ApplicationLoader.applicationContext;
      int var2 = 0;
      this.preferences = var1.getSharedPreferences("uploadinfo", 0);
      this.slowNetwork = ApplicationLoader.isConnectionSlow();
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var4 = new StringBuilder();
         var4.append("start upload on slow network = ");
         var4.append(this.slowNetwork);
         FileLog.d(var4.toString());
      }

      byte var3;
      if (this.slowNetwork) {
         var3 = 1;
      } else {
         var3 = 8;
      }

      while(var2 < var3) {
         this.startUploadRequest();
         ++var2;
      }

   }

   // $FF: synthetic method
   public void lambda$startUploadRequest$4$FileUploadOperation(int var1, int var2, byte[] var3, int var4, int var5, int var6, long var7, TLObject var9, TLObject var10, TLRPC.TL_error var11) {
      if (var1 == this.operationGuid) {
         if (var10 != null) {
            var1 = var10.networkType;
         } else {
            var1 = ApplicationLoader.getCurrentNetworkType();
         }

         int var12 = this.currentType;
         if (var12 == 50331648) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(var1, 3, (long)var2);
         } else if (var12 == 33554432) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(var1, 2, (long)var2);
         } else if (var12 == 16777216) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(var1, 4, (long)var2);
         } else if (var12 == 67108864) {
            StatsController.getInstance(this.currentAccount).incrementSentBytesCount(var1, 5, (long)var2);
         }

         if (var3 != null) {
            this.freeRequestIvs.add(var3);
         }

         this.requestTokens.delete(var4);
         if (var10 instanceof TLRPC.TL_boolTrue) {
            if (this.state != 1) {
               return;
            }

            this.uploadedBytesCount += (long)var5;
            var1 = this.estimatedSize;
            long var13;
            if (var1 != 0) {
               var13 = Math.max(this.availableSize, (long)var1);
            } else {
               var13 = this.totalFileSize;
            }

            this.delegate.didChangedUploadProgress(this, (float)this.uploadedBytesCount / (float)var13);
            --this.currentUploadRequetsCount;
            if (this.isLastPart && this.currentUploadRequetsCount == 0 && this.state == 1) {
               this.state = 3;
               Object var15;
               if (this.key == null) {
                  if (this.isBigFile) {
                     var15 = new TLRPC.TL_inputFileBig();
                  } else {
                     var15 = new TLRPC.TL_inputFile();
                     ((TLRPC.InputFile)var15).md5_checksum = "";
                  }

                  ((TLRPC.InputFile)var15).parts = this.currentPartNum;
                  ((TLRPC.InputFile)var15).id = this.currentFileId;
                  String var18 = this.uploadingFilePath;
                  ((TLRPC.InputFile)var15).name = var18.substring(var18.lastIndexOf("/") + 1);
                  this.delegate.didFinishUploadingFile(this, (TLRPC.InputFile)var15, (TLRPC.InputEncryptedFile)null, (byte[])null, (byte[])null);
                  this.cleanup();
               } else {
                  if (this.isBigFile) {
                     var15 = new TLRPC.TL_inputEncryptedFileBigUploaded();
                  } else {
                     var15 = new TLRPC.TL_inputEncryptedFileUploaded();
                     ((TLRPC.InputEncryptedFile)var15).md5_checksum = "";
                  }

                  ((TLRPC.InputEncryptedFile)var15).parts = this.currentPartNum;
                  ((TLRPC.InputEncryptedFile)var15).id = this.currentFileId;
                  ((TLRPC.InputEncryptedFile)var15).key_fingerprint = this.fingerprint;
                  this.delegate.didFinishUploadingFile(this, (TLRPC.InputFile)null, (TLRPC.InputEncryptedFile)var15, this.key, this.iv);
                  this.cleanup();
               }

               var1 = this.currentType;
               if (var1 == 50331648) {
                  StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
               } else if (var1 == 33554432) {
                  StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
               } else if (var1 == 16777216) {
                  StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
               } else if (var1 == 67108864) {
                  StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
               }
            } else if (this.currentUploadRequetsCount < this.maxRequestsCount) {
               if (this.estimatedSize == 0 && !this.uploadFirstPartLater && !this.nextPartFirst) {
                  if (this.saveInfoTimes >= 4) {
                     this.saveInfoTimes = 0;
                  }

                  var1 = this.lastSavedPartNum;
                  FileUploadOperation.UploadCachedResult var16;
                  if (var6 == var1) {
                     this.lastSavedPartNum = var1 + 1;

                     while(true) {
                        var16 = (FileUploadOperation.UploadCachedResult)this.cachedResults.get(this.lastSavedPartNum);
                        if (var16 == null) {
                           if (this.isBigFile && var7 % 1048576L == 0L || !this.isBigFile && this.saveInfoTimes == 0) {
                              Editor var17 = this.preferences.edit();
                              StringBuilder var19 = new StringBuilder();
                              var19.append(this.fileKey);
                              var19.append("_uploaded");
                              var17.putLong(var19.toString(), var7);
                              if (this.isEncrypted) {
                                 var19 = new StringBuilder();
                                 var19.append(this.fileKey);
                                 var19.append("_ivc");
                                 var17.putString(var19.toString(), Utilities.bytesToHex(var3));
                              }

                              var17.commit();
                           }
                           break;
                        }

                        var7 = var16.bytesOffset;
                        var3 = var16.iv;
                        this.cachedResults.remove(this.lastSavedPartNum);
                        ++this.lastSavedPartNum;
                     }
                  } else {
                     var16 = new FileUploadOperation.UploadCachedResult();
                     var16.bytesOffset = var7;
                     if (var3 != null) {
                        var16.iv = new byte[32];
                        System.arraycopy(var3, 0, var16.iv, 0, 32);
                     }

                     this.cachedResults.put(var6, var16);
                  }

                  ++this.saveInfoTimes;
               }

               this.startUploadRequest();
            }
         } else {
            if (var9 != null) {
               FileLog.e("23123");
            }

            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            this.cleanup();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$startUploadRequest$6$FileUploadOperation() {
      Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$gfXB7X6eyjMCnkoAcAZ52SVcCAA(this));
   }

   protected void onNetworkChanged(boolean var1) {
      if (this.state == 1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$AsRwZThcGZTT5evyde1j2fU_PqE(this, var1));
      }
   }

   public void setDelegate(FileUploadOperation.FileUploadOperationDelegate var1) {
      this.delegate = var1;
   }

   public void start() {
      if (this.state == 0) {
         this.state = 1;
         Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$UJ53jdSVrDp9jmxTaqaNFaiq47E(this));
      }
   }

   public interface FileUploadOperationDelegate {
      void didChangedUploadProgress(FileUploadOperation var1, float var2);

      void didFailedUploadingFile(FileUploadOperation var1);

      void didFinishUploadingFile(FileUploadOperation var1, TLRPC.InputFile var2, TLRPC.InputEncryptedFile var3, byte[] var4, byte[] var5);
   }

   private class UploadCachedResult {
      private long bytesOffset;
      private byte[] iv;

      private UploadCachedResult() {
      }

      // $FF: synthetic method
      UploadCachedResult(Object var2) {
         this();
      }
   }
}
