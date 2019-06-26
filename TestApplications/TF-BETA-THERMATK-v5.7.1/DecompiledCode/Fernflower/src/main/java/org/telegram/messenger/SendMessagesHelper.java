package org.telegram.messenger;

import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaCodecInfo;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.widget.Toast;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFileDrawable;

public class SendMessagesHelper implements NotificationCenter.NotificationCenterDelegate {
   private static volatile SendMessagesHelper[] Instance;
   private static DispatchQueue mediaSendQueue = new DispatchQueue("mediaSendQueue");
   private static ThreadPoolExecutor mediaSendThreadPool;
   private int currentAccount;
   private TLRPC.ChatFull currentChatInfo = null;
   private HashMap delayedMessages = new HashMap();
   private SendMessagesHelper.LocationProvider locationProvider = new SendMessagesHelper.LocationProvider(new SendMessagesHelper.LocationProvider.LocationProviderDelegate() {
      public void onLocationAcquired(Location var1) {
         SendMessagesHelper.this.sendLocation(var1);
         SendMessagesHelper.this.waitingForLocation.clear();
      }

      public void onUnableLocationAcquire() {
         HashMap var1 = new HashMap(SendMessagesHelper.this.waitingForLocation);
         NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.wasUnableToFindCurrentLocation, var1);
         SendMessagesHelper.this.waitingForLocation.clear();
      }
   });
   private SparseArray sendingMessages = new SparseArray();
   private SparseArray unsentMessages = new SparseArray();
   private LongSparseArray voteSendTime = new LongSparseArray();
   private HashMap waitingForCallback = new HashMap();
   private HashMap waitingForLocation = new HashMap();
   private HashMap waitingForVote = new HashMap();

   static {
      int var0;
      if (VERSION.SDK_INT >= 17) {
         var0 = Runtime.getRuntime().availableProcessors();
      } else {
         var0 = 2;
      }

      mediaSendThreadPool = new ThreadPoolExecutor(var0, var0, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
      Instance = new SendMessagesHelper[3];
   }

   public SendMessagesHelper(int var1) {
      this.currentAccount = var1;
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$F3OpGpLNH47E9LDuBXXNWIdgYDE(this));
   }

   // $FF: synthetic method
   static void access$1100(SendMessagesHelper var0, SendMessagesHelper.DelayedMessage var1) {
      var0.performSendDelayedMessage(var1);
   }

   // $FF: synthetic method
   static void access$1200(SendMessagesHelper var0, SendMessagesHelper.DelayedMessage var1, int var2) {
      var0.performSendDelayedMessage(var1, var2);
   }

   // $FF: synthetic method
   static HashMap access$1300(SendMessagesHelper var0) {
      return var0.waitingForVote;
   }

   private static VideoEditedInfo createCompressionSettings(String var0) {
      int[] var1 = new int[9];
      AnimatedFileDrawable.getVideoInfo(var0, var1);
      if (var1[0] == 0) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("video hasn't avc1 atom");
         }

         return null;
      } else {
         int var2 = var1[3];
         int var3 = var1[3];
         float var4 = (float)var1[4];
         long var5 = (long)var1[6];
         long var7 = (long)var1[5];
         int var9 = var1[7];
         int var10 = 900000;
         int var11 = var3;
         if (var3 > 900000) {
            var11 = 900000;
         }

         if (VERSION.SDK_INT < 18) {
            label187: {
               MediaCodecInfo var12;
               boolean var10001;
               try {
                  var12 = MediaController.selectCodec("video/avc");
               } catch (Exception var24) {
                  var10001 = false;
                  return null;
               }

               if (var12 == null) {
                  try {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("no codec info for video/avc");
                     }

                     return null;
                  } catch (Exception var20) {
                     var10001 = false;
                  }
               } else {
                  label188: {
                     String var13;
                     try {
                        var13 = var12.getName();
                        if (!var13.equals("OMX.google.h264.encoder") && !var13.equals("OMX.ST.VFM.H264Enc") && !var13.equals("OMX.Exynos.avc.enc") && !var13.equals("OMX.MARVELL.VIDEO.HW.CODA7542ENCODER") && !var13.equals("OMX.MARVELL.VIDEO.H264ENCODER") && !var13.equals("OMX.k3.video.encoder.avc") && !var13.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                           break label188;
                        }
                     } catch (Exception var23) {
                        var10001 = false;
                        return null;
                     }

                     try {
                        if (BuildVars.LOGS_ENABLED) {
                           StringBuilder var25 = new StringBuilder();
                           var25.append("unsupported encoder = ");
                           var25.append(var13);
                           FileLog.d(var25.toString());
                           return null;
                        }
                     } catch (Exception var22) {
                        var10001 = false;
                     }

                     return null;
                  }

                  try {
                     if (MediaController.selectColorFormat(var12, "video/avc") == 0) {
                        if (BuildVars.LOGS_ENABLED) {
                           FileLog.d("no color format for video/avc");
                        }

                        return null;
                     }
                     break label187;
                  } catch (Exception var21) {
                     var10001 = false;
                  }
               }

               return null;
            }
         }

         int var14;
         byte var26;
         VideoEditedInfo var27;
         label120: {
            var27 = new VideoEditedInfo();
            var27.startTime = -1L;
            var27.endTime = -1L;
            var27.bitrate = var11;
            var27.originalPath = var0;
            var27.framerate = var9;
            var27.estimatedDuration = (long)Math.ceil((double)var4);
            var3 = var1[1];
            var27.originalWidth = var3;
            var27.resultWidth = var3;
            var3 = var1[2];
            var27.originalHeight = var3;
            var27.resultHeight = var3;
            var27.rotationValue = var1[8];
            var14 = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
            var3 = var27.originalWidth;
            if (var3 <= 1280) {
               var9 = var27.originalHeight;
               if (var9 <= 1280) {
                  if (var3 <= 848 && var9 <= 848) {
                     if (var3 <= 640 && var9 <= 640) {
                        if (var3 <= 480 && var9 <= 480) {
                           var26 = 1;
                        } else {
                           var26 = 2;
                        }
                     } else {
                        var26 = 3;
                     }
                  } else {
                     var26 = 4;
                  }
                  break label120;
               }
            }

            var26 = 5;
         }

         var9 = var14;
         if (var14 >= var26) {
            var9 = var26 - 1;
         }

         int var15 = var26 - 1;
         var14 = var11;
         long var16 = var5;
         if (var9 != var15) {
            float var18;
            if (var9 != 0) {
               if (var9 != 1) {
                  if (var9 != 2) {
                     var3 = 2500000;
                     var18 = 1280.0F;
                  } else {
                     var18 = 848.0F;
                     var3 = 1100000;
                  }
               } else {
                  var18 = 640.0F;
                  var3 = var10;
               }
            } else {
               var18 = 432.0F;
               var3 = 400000;
            }

            var14 = var27.originalWidth;
            var10 = var27.originalHeight;
            float var19;
            if (var14 > var10) {
               var19 = (float)var14;
            } else {
               var19 = (float)var10;
            }

            var18 /= var19;
            var27.resultWidth = Math.round((float)var27.originalWidth * var18 / 2.0F) * 2;
            var27.resultHeight = Math.round((float)var27.originalHeight * var18 / 2.0F) * 2;
            var14 = var11;
            var16 = var5;
            if (var11 != 0) {
               var14 = Math.min(var3, (int)((float)var2 / var18));
               var16 = (long)((float)(var14 / 8) * var4 / 1000.0F);
            }
         }

         if (var9 == var15) {
            var27.resultWidth = var27.originalWidth;
            var27.resultHeight = var27.originalHeight;
            var27.bitrate = var2;
            var27.estimatedSize = (long)((int)(new File(var0)).length());
         } else {
            var27.bitrate = var14;
            var27.estimatedSize = (long)((int)(var7 + var16));
            var16 = var27.estimatedSize;
            var27.estimatedSize = var16 + var16 / 32768L * 16L;
         }

         if (var27.estimatedSize == 0L) {
            var27.estimatedSize = 1L;
         }

         return var27;
      }
   }

   private static Bitmap createVideoThumbnail(String var0, long var1) {
      MediaMetadataRetriever var3 = new MediaMetadataRetriever();

      Bitmap var12;
      try {
         var3.setDataSource(var0);
         var12 = var3.getFrameAtTime(var1, 1);
         return var12 == null ? null : var12;
      } catch (Exception var10) {
      } finally {
         try {
            var3.release();
         } catch (RuntimeException var9) {
         }

      }

      var12 = null;
      return var12 == null ? null : var12;
   }

   private void editMessageMedia(MessageObject var1, TLRPC.TL_photo var2, VideoEditedInfo var3, TLRPC.TL_document var4, String var5, HashMap var6, boolean var7, Object var8) {
      TLRPC.TL_document var9 = var4;
      if (var1 != null) {
         TLRPC.Message var10 = var1.messageOwner;
         var1.cancelEditing = false;

         Exception var10000;
         label663: {
            long var11;
            boolean var10001;
            try {
               var11 = var1.getDialogId();
            } catch (Exception var83) {
               var10000 = var83;
               var10001 = false;
               break label663;
            }

            byte var13;
            HashMap var14;
            TLRPC.TL_photo var97;
            if (var7) {
               label594: {
                  label635: {
                     try {
                        if (var1.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                           var2 = (TLRPC.TL_photo)var1.messageOwner.media.photo;
                           break label635;
                        }
                     } catch (Exception var80) {
                        var10000 = var80;
                        var10001 = false;
                        break label663;
                     }

                     label586: {
                        label585: {
                           try {
                              var9 = (TLRPC.TL_document)var1.messageOwner.media.document;
                              if (MessageObject.isVideoDocument(var9)) {
                                 break label585;
                              }
                           } catch (Exception var79) {
                              var10000 = var79;
                              var10001 = false;
                              break label663;
                           }

                           if (var3 == null) {
                              var13 = 7;
                              break label586;
                           }
                        }

                        var13 = 3;
                     }

                     try {
                        var3 = var1.videoEditedInfo;
                        break label594;
                     } catch (Exception var78) {
                        var10000 = var78;
                        var10001 = false;
                        break label663;
                     }
                  }

                  var13 = 2;
               }

               try {
                  var14 = var10.params;
                  var1.editingMessage = var10.message;
                  var1.editingMessageEntities = var10.entities;
                  var5 = var10.attachPath;
               } catch (Exception var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label663;
               }

               var97 = var2;
            } else {
               SerializedData var107;
               try {
                  var1.previousMedia = var10.media;
                  var1.previousCaption = var10.message;
                  var1.previousCaptionEntities = var10.entities;
                  var1.previousAttachPath = var10.attachPath;
                  SerializedData var15 = new SerializedData(true);
                  this.writePreviousMessageData(var10, var15);
                  var107 = new SerializedData(var15.length());
                  this.writePreviousMessageData(var10, var107);
               } catch (Exception var76) {
                  var10000 = var76;
                  var10001 = false;
                  break label663;
               }

               if (var6 == null) {
                  try {
                     var6 = new HashMap();
                  } catch (Exception var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label663;
                  }
               }

               try {
                  var6.put("prevMedia", Base64.encodeToString(var107.toByteArray(), 0));
                  var107.cleanup();
               } catch (Exception var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label663;
               }

               if (var2 != null) {
                  try {
                     TLRPC.TL_messageMediaPhoto var86 = new TLRPC.TL_messageMediaPhoto();
                     var10.media = var86;
                     TLRPC.MessageMedia var89 = var10.media;
                     var89.flags |= 3;
                     var10.media.photo = var2;
                  } catch (Exception var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label663;
                  }

                  label603: {
                     if (var5 != null) {
                        try {
                           if (var5.length() > 0 && var5.startsWith("http")) {
                              var10.attachPath = var5;
                              break label603;
                           }
                        } catch (Exception var81) {
                           var10000 = var81;
                           var10001 = false;
                           break label663;
                        }
                     }

                     try {
                        var10.attachPath = FileLoader.getPathToAttach(((TLRPC.PhotoSize)var2.sizes.get(var2.sizes.size() - 1)).location, true).toString();
                     } catch (Exception var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label663;
                     }
                  }

                  var13 = 2;
               } else if (var4 == null) {
                  var13 = -1;
               } else {
                  label612: {
                     label611: {
                        try {
                           TLRPC.TL_messageMediaDocument var108 = new TLRPC.TL_messageMediaDocument();
                           var10.media = var108;
                           TLRPC.MessageMedia var109 = var10.media;
                           var109.flags |= 3;
                           var10.media.document = var9;
                           if (MessageObject.isVideoDocument(var4)) {
                              break label611;
                           }
                        } catch (Exception var82) {
                           var10000 = var82;
                           var10001 = false;
                           break label663;
                        }

                        if (var3 == null) {
                           var13 = 7;
                           break label612;
                        }
                     }

                     var13 = 3;
                  }

                  if (var3 != null) {
                     try {
                        var6.put("ve", var3.getString());
                     } catch (Exception var71) {
                        var10000 = var71;
                        var10001 = false;
                        break label663;
                     }
                  }

                  try {
                     var10.attachPath = var5;
                  } catch (Exception var70) {
                     var10000 = var70;
                     var10001 = false;
                     break label663;
                  }
               }

               try {
                  var10.params = var6;
                  var10.send_state = 3;
               } catch (Exception var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label663;
               }

               var14 = var6;
               var97 = var2;
            }

            try {
               if (var10.attachPath == null) {
                  var10.attachPath = "";
               }
            } catch (Exception var68) {
               var10000 = var68;
               var10001 = false;
               break label663;
            }

            label557: {
               label556: {
                  try {
                     var10.local_id = 0;
                     if (var1.type == 3) {
                        break label556;
                     }
                  } catch (Exception var67) {
                     var10000 = var67;
                     var10001 = false;
                     break label663;
                  }

                  if (var3 == null) {
                     try {
                        if (var1.type != 2) {
                           break label557;
                        }
                     } catch (Exception var66) {
                        var10000 = var66;
                        var10001 = false;
                        break label663;
                     }
                  }
               }

               try {
                  if (!TextUtils.isEmpty(var10.attachPath)) {
                     var1.attachPathExists = true;
                  }
               } catch (Exception var65) {
                  var10000 = var65;
                  var10001 = false;
                  break label663;
               }
            }

            VideoEditedInfo var84 = var3;

            label544: {
               try {
                  if (var1.videoEditedInfo == null) {
                     break label544;
                  }
               } catch (Exception var64) {
                  var10000 = var64;
                  var10001 = false;
                  break label663;
               }

               var84 = var3;
               if (var3 == null) {
                  try {
                     var84 = var1.videoEditedInfo;
                  } catch (Exception var63) {
                     var10000 = var63;
                     var10001 = false;
                     break label663;
                  }
               }
            }

            CharSequence var87;
            ArrayList var90;
            if (!var7) {
               label534: {
                  label639: {
                     try {
                        if (var1.editingMessage == null) {
                           break label534;
                        }

                        var10.message = var1.editingMessage.toString();
                        if (var1.editingMessageEntities != null) {
                           var10.entities = var1.editingMessageEntities;
                           break label639;
                        }
                     } catch (Exception var62) {
                        var10000 = var62;
                        var10001 = false;
                        break label663;
                     }

                     try {
                        var87 = var1.editingMessage;
                        var90 = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[]{var87});
                     } catch (Exception var61) {
                        var10000 = var61;
                        var10001 = false;
                        break label663;
                     }

                     if (var90 != null) {
                        try {
                           if (!var90.isEmpty()) {
                              var10.entities = var90;
                           }
                        } catch (Exception var60) {
                           var10000 = var60;
                           var10001 = false;
                           break label663;
                        }
                     }
                  }

                  try {
                     var1.caption = null;
                     var1.generateCaption();
                  } catch (Exception var59) {
                     var10000 = var59;
                     var10001 = false;
                     break label663;
                  }
               }

               try {
                  var90 = new ArrayList();
                  var90.add(var10);
                  MessagesStorage.getInstance(this.currentAccount).putMessages(var90, false, true, false, 0);
                  var1.type = -1;
                  var1.setType();
                  var1.createMessageSendInfo();
                  var90 = new ArrayList();
                  var90.add(var1);
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var11, var90);
               } catch (Exception var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label663;
               }
            }

            String var91;
            label516: {
               if (var14 != null) {
                  try {
                     if (var14.containsKey("originalPath")) {
                        var91 = (String)var14.get("originalPath");
                        break label516;
                     }
                  } catch (Exception var57) {
                     var10000 = var57;
                     var10001 = false;
                     break label663;
                  }
               }

               var91 = null;
            }

            if ((var13 < 1 || var13 > 3) && (var13 < 5 || var13 > 8)) {
               return;
            }

            SendMessagesHelper.DelayedMessage var88;
            Object var95;
            if (var13 == 2) {
               TLRPC.TL_inputMediaUploadedPhoto var85;
               try {
                  var85 = new TLRPC.TL_inputMediaUploadedPhoto();
               } catch (Exception var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label663;
               }

               if (var14 != null) {
                  String var104;
                  try {
                     var104 = (String)var14.get("masks");
                  } catch (Exception var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label663;
                  }

                  if (var104 != null) {
                     int var16;
                     SerializedData var92;
                     try {
                        var92 = new SerializedData(Utilities.hexToBytes(var104));
                        var16 = var92.readInt32(false);
                     } catch (Exception var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label663;
                     }

                     for(int var17 = 0; var17 < var16; ++var17) {
                        try {
                           var85.stickers.add(TLRPC.InputDocument.TLdeserialize(var92, var92.readInt32(false), false));
                        } catch (Exception var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label663;
                        }
                     }

                     try {
                        var85.flags |= 1;
                        var92.cleanup();
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break label663;
                     }
                  }
               }

               label645: {
                  label491: {
                     try {
                        if (var97.access_hash != 0L) {
                           break label491;
                        }
                     } catch (Exception var56) {
                        var10000 = var56;
                        var10001 = false;
                        break label663;
                     }

                     var95 = var85;
                     var7 = true;
                     break label645;
                  }

                  try {
                     var95 = new TLRPC.TL_inputMediaPhoto();
                     TLRPC.TL_inputPhoto var105 = new TLRPC.TL_inputPhoto();
                     ((TLRPC.TL_inputMediaPhoto)var95).id = var105;
                     ((TLRPC.TL_inputMediaPhoto)var95).id.id = var97.id;
                     ((TLRPC.TL_inputMediaPhoto)var95).id.access_hash = var97.access_hash;
                     ((TLRPC.TL_inputMediaPhoto)var95).id.file_reference = var97.file_reference;
                     if (((TLRPC.TL_inputMediaPhoto)var95).id.file_reference == null) {
                        ((TLRPC.TL_inputMediaPhoto)var95).id.file_reference = new byte[0];
                     }
                  } catch (Exception var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label663;
                  }

                  var7 = false;
               }

               SendMessagesHelper.DelayedMessage var106;
               try {
                  var106 = new SendMessagesHelper.DelayedMessage(var11);
                  var106.type = 0;
                  var106.obj = var1;
                  var106.originalPath = var91;
                  var106.parentObject = var8;
                  var106.inputUploadMedia = var85;
                  var106.performMediaUpload = var7;
               } catch (Exception var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label663;
               }

               label479: {
                  if (var5 != null) {
                     try {
                        if (var5.length() > 0 && var5.startsWith("http")) {
                           var106.httpLocation = var5;
                           break label479;
                        }
                     } catch (Exception var54) {
                        var10000 = var54;
                        var10001 = false;
                        break label663;
                     }
                  }

                  try {
                     var106.photoSize = (TLRPC.PhotoSize)var97.sizes.get(var97.sizes.size() - 1);
                     var106.locationParent = var97;
                  } catch (Exception var36) {
                     var10000 = var36;
                     var10001 = false;
                     break label663;
                  }
               }

               var88 = var106;
            } else if (var13 == 3) {
               TLRPC.TL_inputMediaUploadedDocument var94;
               label647: {
                  try {
                     var94 = new TLRPC.TL_inputMediaUploadedDocument();
                     var94.mime_type = var9.mime_type;
                     var94.attributes = var9.attributes;
                     if (var1.isGif()) {
                        break label647;
                     }
                  } catch (Exception var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label663;
                  }

                  if (var84 != null) {
                     try {
                        if (var84.muted) {
                           break label647;
                        }
                     } catch (Exception var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label663;
                     }
                  }

                  try {
                     var94.nosound_video = true;
                     if (BuildVars.DEBUG_VERSION) {
                        FileLog.d("nosound_video = true");
                     }
                  } catch (Exception var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label663;
                  }
               }

               label648: {
                  label416: {
                     try {
                        if (var9.access_hash != 0L) {
                           break label416;
                        }
                     } catch (Exception var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label663;
                     }

                     var95 = var94;
                     var7 = true;
                     break label648;
                  }

                  try {
                     var95 = new TLRPC.TL_inputMediaDocument();
                     TLRPC.TL_inputDocument var101 = new TLRPC.TL_inputDocument();
                     ((TLRPC.TL_inputMediaDocument)var95).id = var101;
                     ((TLRPC.TL_inputMediaDocument)var95).id.id = var9.id;
                     ((TLRPC.TL_inputMediaDocument)var95).id.access_hash = var9.access_hash;
                     ((TLRPC.TL_inputMediaDocument)var95).id.file_reference = var9.file_reference;
                     if (((TLRPC.TL_inputMediaDocument)var95).id.file_reference == null) {
                        ((TLRPC.TL_inputMediaDocument)var95).id.file_reference = new byte[0];
                     }
                  } catch (Exception var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label663;
                  }

                  var7 = false;
               }

               SendMessagesHelper.DelayedMessage var103;
               try {
                  var103 = new SendMessagesHelper.DelayedMessage(var11);
                  var103.type = 1;
                  var103.obj = var1;
                  var103.originalPath = var91;
                  var103.parentObject = var8;
                  var103.inputUploadMedia = var94;
                  var103.performMediaUpload = var7;
                  if (!var9.thumbs.isEmpty()) {
                     var103.photoSize = (TLRPC.PhotoSize)var9.thumbs.get(0);
                     var103.locationParent = var9;
                  }
               } catch (Exception var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label663;
               }

               try {
                  var103.videoEditedInfo = var84;
               } catch (Exception var35) {
                  var10000 = var35;
                  var10001 = false;
                  break label663;
               }

               var88 = var103;
            } else {
               label472: {
                  if (var13 == 7) {
                     boolean var110;
                     label650: {
                        if (var91 != null) {
                           label467: {
                              try {
                                 if (var91.length() <= 0 || !var91.startsWith("http")) {
                                    break label467;
                                 }
                              } catch (Exception var53) {
                                 var10000 = var53;
                                 var10001 = false;
                                 break label663;
                              }

                              if (var14 != null) {
                                 try {
                                    var95 = new TLRPC.TL_inputMediaGifExternal();
                                    String[] var93 = ((String)var14.get("url")).split("\\|");
                                    if (var93.length == 2) {
                                       ((TLRPC.InputMedia)var95).url = var93[0];
                                       ((TLRPC.InputMedia)var95).q = var93[1];
                                    }
                                 } catch (Exception var52) {
                                    var10000 = var52;
                                    var10001 = false;
                                    break label663;
                                 }

                                 var110 = true;
                                 break label650;
                              }
                           }
                        }

                        try {
                           var95 = new TLRPC.TL_inputMediaUploadedDocument();
                        } catch (Exception var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label663;
                        }

                        var110 = false;
                     }

                     Object var96;
                     label453: {
                        label651: {
                           try {
                              ((TLRPC.InputMedia)var95).mime_type = var9.mime_type;
                              ((TLRPC.InputMedia)var95).attributes = var9.attributes;
                              if (var9.access_hash == 0L) {
                                 var7 = var95 instanceof TLRPC.TL_inputMediaUploadedDocument;
                                 break label651;
                              }
                           } catch (Exception var51) {
                              var10000 = var51;
                              var10001 = false;
                              break label663;
                           }

                           try {
                              var96 = new TLRPC.TL_inputMediaDocument();
                              TLRPC.TL_inputDocument var98 = new TLRPC.TL_inputDocument();
                              ((TLRPC.TL_inputMediaDocument)var96).id = var98;
                              ((TLRPC.TL_inputMediaDocument)var96).id.id = var9.id;
                              ((TLRPC.TL_inputMediaDocument)var96).id.access_hash = var9.access_hash;
                              ((TLRPC.TL_inputMediaDocument)var96).id.file_reference = var9.file_reference;
                              if (((TLRPC.TL_inputMediaDocument)var96).id.file_reference == null) {
                                 ((TLRPC.TL_inputMediaDocument)var96).id.file_reference = new byte[0];
                              }
                           } catch (Exception var50) {
                              var10000 = var50;
                              var10001 = false;
                              break label663;
                           }

                           var7 = false;
                           break label453;
                        }

                        var96 = var95;
                     }

                     if (!var110) {
                        SendMessagesHelper.DelayedMessage var99;
                        try {
                           var99 = new SendMessagesHelper.DelayedMessage(var11);
                           var99.originalPath = var91;
                           var99.type = 2;
                           var99.obj = var1;
                           if (!var9.thumbs.isEmpty()) {
                              var99.photoSize = (TLRPC.PhotoSize)var9.thumbs.get(0);
                              var99.locationParent = var9;
                           }
                        } catch (Exception var49) {
                           var10000 = var49;
                           var10001 = false;
                           break label663;
                        }

                        try {
                           var99.parentObject = var8;
                           var99.inputUploadMedia = (TLRPC.InputMedia)var95;
                           var99.performMediaUpload = var7;
                        } catch (Exception var33) {
                           var10000 = var33;
                           var10001 = false;
                           break label663;
                        }

                        var95 = var96;
                        var88 = var99;
                        break label472;
                     }

                     var95 = var96;
                  } else {
                     var95 = null;
                     var7 = false;
                  }

                  var88 = null;
               }
            }

            TLRPC.TL_messages_editMessage var100;
            label384: {
               label653: {
                  try {
                     var100 = new TLRPC.TL_messages_editMessage();
                     var100.id = var1.getId();
                     var100.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var11);
                     var100.flags |= 16384;
                     var100.media = (TLRPC.InputMedia)var95;
                     if (var1.editingMessage == null) {
                        break label384;
                     }

                     var100.message = var1.editingMessage.toString();
                     var100.flags |= 2048;
                     if (var1.editingMessageEntities != null) {
                        var100.entities = var1.editingMessageEntities;
                        var100.flags |= 8;
                        break label653;
                     }
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label663;
                  }

                  try {
                     var87 = var1.editingMessage;
                     var90 = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[]{var87});
                  } catch (Exception var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label663;
                  }

                  if (var90 != null) {
                     try {
                        if (!var90.isEmpty()) {
                           var100.entities = var90;
                           var100.flags |= 8;
                        }
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label663;
                     }
                  }
               }

               try {
                  var1.editingMessage = null;
                  var1.editingMessageEntities = null;
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label663;
               }
            }

            if (var88 != null) {
               try {
                  var88.sendRequest = var100;
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label663;
               }
            }

            if (var13 == 1) {
               try {
                  this.performSendMessageRequest(var100, var1, (String)null, var88, var8);
                  return;
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
               }
            } else if (var13 == 2) {
               if (var7) {
                  try {
                     this.performSendDelayedMessage(var88);
                     return;
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                  }
               } else {
                  try {
                     this.performSendMessageRequest(var100, var1, var91, (SendMessagesHelper.DelayedMessage)null, true, var88, var8);
                     return;
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                  }
               }
            } else if (var13 == 3) {
               if (var7) {
                  try {
                     this.performSendDelayedMessage(var88);
                     return;
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                  }
               } else {
                  try {
                     this.performSendMessageRequest(var100, var1, var91, var88, var8);
                     return;
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                  }
               }
            } else if (var13 == 6) {
               try {
                  this.performSendMessageRequest(var100, var1, var91, var88, var8);
                  return;
               } catch (Exception var23) {
                  var10000 = var23;
                  var10001 = false;
               }
            } else if (var13 == 7) {
               if (var7) {
                  try {
                     this.performSendDelayedMessage(var88);
                     return;
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                  }
               } else {
                  try {
                     this.performSendMessageRequest(var100, var1, var91, var88, var8);
                     return;
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                  }
               }
            } else {
               if (var13 != 8) {
                  return;
               }

               if (var7) {
                  try {
                     this.performSendDelayedMessage(var88);
                     return;
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                  }
               } else {
                  try {
                     this.performSendMessageRequest(var100, var1, var91, var88, var8);
                     return;
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                  }
               }
            }
         }

         Exception var102 = var10000;
         FileLog.e((Throwable)var102);
         this.revertEditingMessageObject(var1);
      }
   }

   public static void ensureMediaThumbExists(int var0, boolean var1, TLObject var2, String var3, Uri var4, long var5) {
      Bitmap var14;
      if (var2 instanceof TLRPC.TL_photo) {
         TLRPC.TL_photo var7 = (TLRPC.TL_photo)var2;
         TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(var7.sizes, 90);
         if (var8 instanceof TLRPC.TL_photoStrippedSize) {
            var1 = true;
         } else {
            var1 = FileLoader.getPathToAttach(var8, true).exists();
         }

         TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var7.sizes, AndroidUtilities.getPhotoSize());
         boolean var10 = FileLoader.getPathToAttach(var9, false).exists();
         if (!var1 || !var10) {
            var14 = ImageLoader.loadBitmap(var3, var4, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), true);
            if (var14 == null) {
               var14 = ImageLoader.loadBitmap(var3, var4, 800.0F, 800.0F, true);
            }

            TLRPC.PhotoSize var15;
            if (!var10) {
               var15 = ImageLoader.scaleAndSaveImage(var9, var14, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
               if (var15 != var9) {
                  var7.sizes.add(0, var15);
               }
            }

            if (!var1) {
               var15 = ImageLoader.scaleAndSaveImage(var8, var14, 90.0F, 90.0F, 55, true);
               if (var15 != var8) {
                  var7.sizes.add(0, var15);
               }
            }

            if (var14 != null) {
               var14.recycle();
            }
         }
      } else if (var2 instanceof TLRPC.TL_document) {
         TLRPC.TL_document var18 = (TLRPC.TL_document)var2;
         if ((MessageObject.isVideoDocument(var18) || MessageObject.isNewGifDocument((TLRPC.Document)var18)) && MessageObject.isDocumentHasThumb(var18)) {
            ArrayList var16 = var18.thumbs;
            short var12 = 320;
            TLRPC.PhotoSize var19 = FileLoader.getClosestPhotoSizeWithSize(var16, 320);
            if (var19 instanceof TLRPC.TL_photoStrippedSize) {
               return;
            }

            if (!FileLoader.getPathToAttach(var19, true).exists()) {
               var14 = createVideoThumbnail(var3, var5);
               if (var14 == null) {
                  var14 = ThumbnailUtils.createVideoThumbnail(var3, 1);
               }

               if (var1) {
                  var12 = 90;
               }

               ArrayList var17 = var18.thumbs;
               float var11 = (float)var12;
               byte var13;
               if (var12 > 90) {
                  var13 = 80;
               } else {
                  var13 = 55;
               }

               var17.set(0, ImageLoader.scaleAndSaveImage(var19, var14, var11, var11, var13, false));
            }
         }
      }

   }

   private static void fillVideoAttribute(String param0, TLRPC.TL_documentAttributeVideo param1, VideoEditedInfo param2) {
      // $FF: Couldn't be decompiled
   }

   private SendMessagesHelper.DelayedMessage findMaxDelayedMessageForMessageId(int var1, long var2) {
      Iterator var4 = this.delayedMessages.entrySet().iterator();
      SendMessagesHelper.DelayedMessage var5 = null;

      int var10;
      for(int var6 = Integer.MIN_VALUE; var4.hasNext(); var6 = var10) {
         ArrayList var7 = (ArrayList)((Entry)var4.next()).getValue();
         int var8 = var7.size();
         int var9 = 0;

         int var13;
         for(var10 = var6; var9 < var8; var10 = var13) {
            SendMessagesHelper.DelayedMessage var12;
            label54: {
               SendMessagesHelper.DelayedMessage var11 = (SendMessagesHelper.DelayedMessage)var7.get(var9);
               var6 = var11.type;
               if (var6 != 4) {
                  var12 = var5;
                  var13 = var10;
                  if (var6 != 0) {
                     break label54;
                  }
               }

               var12 = var5;
               var13 = var10;
               if (var11.peer == var2) {
                  MessageObject var14 = var11.obj;
                  if (var14 != null) {
                     var6 = var14.getId();
                  } else {
                     ArrayList var15 = var11.messageObjects;
                     if (var15 != null && !var15.isEmpty()) {
                        var15 = var11.messageObjects;
                        var6 = ((MessageObject)var15.get(var15.size() - 1)).getId();
                     } else {
                        var6 = 0;
                     }
                  }

                  var12 = var5;
                  var13 = var10;
                  if (var6 != 0) {
                     var12 = var5;
                     var13 = var10;
                     if (var6 > var1) {
                        var12 = var5;
                        var13 = var10;
                        if (var5 == null) {
                           var12 = var5;
                           var13 = var10;
                           if (var10 < var6) {
                              var12 = var11;
                              var13 = var6;
                           }
                        }
                     }
                  }
               }
            }

            ++var9;
            var5 = var12;
         }
      }

      return var5;
   }

   public static SendMessagesHelper getInstance(int var0) {
      SendMessagesHelper var1 = Instance[var0];
      SendMessagesHelper var2 = var1;
      if (var1 == null) {
         synchronized(SendMessagesHelper.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               SendMessagesHelper[] var23;
               try {
                  var23 = Instance;
                  var2 = new SendMessagesHelper(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   private static String getKeyForPhotoSize(TLRPC.PhotoSize var0, Bitmap[] var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3;
         label76: {
            if (AndroidUtilities.isTablet()) {
               var3 = AndroidUtilities.getMinTabletSide();
            } else {
               Point var4;
               if (var0.w >= var0.h) {
                  var4 = AndroidUtilities.displaySize;
                  var3 = Math.min(var4.x, var4.y) - AndroidUtilities.dp(64.0F);
                  break label76;
               }

               var4 = AndroidUtilities.displaySize;
               var3 = Math.min(var4.x, var4.y);
            }

            var3 = (int)((float)var3 * 0.7F);
         }

         int var5 = AndroidUtilities.dp(100.0F) + var3;
         int var6 = var3;
         if (var3 > AndroidUtilities.getPhotoSize()) {
            var6 = AndroidUtilities.getPhotoSize();
         }

         var3 = var5;
         if (var5 > AndroidUtilities.getPhotoSize()) {
            var3 = AndroidUtilities.getPhotoSize();
         }

         var5 = var0.w;
         float var7 = (float)var5;
         float var8 = (float)var6;
         var7 /= var8;
         var5 = (int)((float)var5 / var7);
         int var9 = (int)((float)var0.h / var7);
         var6 = var5;
         if (var5 == 0) {
            var6 = AndroidUtilities.dp(150.0F);
         }

         var5 = var9;
         if (var9 == 0) {
            var5 = AndroidUtilities.dp(150.0F);
         }

         if (var5 > var3) {
            var8 = (float)var5 / (float)var3;
            var6 = (int)((float)var6 / var8);
         } else if (var5 < AndroidUtilities.dp(120.0F)) {
            var5 = AndroidUtilities.dp(120.0F);
            var7 = (float)var0.h / (float)var5;
            var9 = var0.w;
            var3 = var5;
            if ((float)var9 / var7 < var8) {
               var6 = (int)((float)var9 / var7);
               var3 = var5;
            }
         } else {
            var3 = var5;
         }

         if (var1 != null) {
            label82: {
               File var10;
               FileInputStream var11;
               Options var15;
               boolean var10001;
               try {
                  var15 = new Options();
                  var15.inJustDecodeBounds = true;
                  var10 = FileLoader.getPathToAttach(var0);
                  var11 = new FileInputStream(var10);
                  BitmapFactory.decodeStream(var11, (Rect)null, var15);
                  var11.close();
                  var7 = (float)var15.outWidth;
                  var8 = (float)var15.outHeight;
                  var7 = Math.max(var7 / (float)var6, var8 / (float)var3);
               } catch (Throwable var13) {
                  var10001 = false;
                  break label82;
               }

               var8 = var7;
               if (var7 < 1.0F) {
                  var8 = 1.0F;
               }

               try {
                  var15.inJustDecodeBounds = false;
                  var15.inSampleSize = (int)var8;
                  var15.inPreferredConfig = Config.RGB_565;
                  if (VERSION.SDK_INT >= 21) {
                     var11 = new FileInputStream(var10);
                     var1[0] = BitmapFactory.decodeStream(var11, (Rect)null, var15);
                     var11.close();
                  }
               } catch (Throwable var12) {
                  var10001 = false;
               }
            }
         }

         Locale var16 = Locale.US;
         String var14;
         if (var2) {
            var14 = "%d_%d@%d_%d_b";
         } else {
            var14 = "%d_%d@%d_%d";
         }

         return String.format(var16, var14, var0.location.volume_id, var0.location.local_id, (int)((float)var6 / AndroidUtilities.density), (int)((float)var3 / AndroidUtilities.density));
      }
   }

   private TLRPC.PhotoSize getThumbForSecretChat(ArrayList var1) {
      if (var1 != null && !var1.isEmpty()) {
         int var2 = var1.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            TLRPC.PhotoSize var4 = (TLRPC.PhotoSize)var1.get(var3);
            if (var4 != null && !(var4 instanceof TLRPC.TL_photoStrippedSize) && !(var4 instanceof TLRPC.TL_photoSizeEmpty) && var4.location != null) {
               TLRPC.TL_photoSize var6 = new TLRPC.TL_photoSize();
               var6.type = var4.type;
               var6.w = var4.w;
               var6.h = var4.h;
               var6.size = var4.size;
               var6.bytes = var4.bytes;
               if (var6.bytes == null) {
                  var6.bytes = new byte[0];
               }

               var6.location = new TLRPC.TL_fileLocation_layer82();
               TLRPC.FileLocation var5 = var6.location;
               TLRPC.FileLocation var7 = var4.location;
               var5.dc_id = var7.dc_id;
               var5.volume_id = var7.volume_id;
               var5.local_id = var7.local_id;
               var5.secret = var7.secret;
               return var6;
            }
         }
      }

      return null;
   }

   private static String getTrimmedString(String var0) {
      String var1 = var0.trim();
      if (var1.length() == 0) {
         return var1;
      } else {
         while(true) {
            var1 = var0;
            if (!var0.startsWith("\n")) {
               while(var1.endsWith("\n")) {
                  var1 = var1.substring(0, var1.length() - 1);
               }

               return var1;
            }

            var0 = var0.substring(1);
         }
      }
   }

   // $FF: synthetic method
   static void lambda$null$45(MessageObject var0, int var1, TLRPC.TL_document var2, MessageObject var3, HashMap var4, String var5, long var6, MessageObject var8) {
      if (var0 != null) {
         getInstance(var1).editMessageMedia(var0, (TLRPC.TL_photo)null, (VideoEditedInfo)null, var2, var3.messageOwner.attachPath, var4, false, var5);
      } else {
         getInstance(var1).sendMessage(var2, (VideoEditedInfo)null, var3.messageOwner.attachPath, var6, var8, (String)null, (ArrayList)null, (TLRPC.ReplyMarkup)null, var4, 0, var5);
      }

   }

   // $FF: synthetic method
   static void lambda$null$47() {
      try {
         Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("UnsupportedAttachment", 2131560946), 0).show();
      } catch (Exception var1) {
         FileLog.e((Throwable)var1);
      }

   }

   // $FF: synthetic method
   static void lambda$null$49(Location var0, int var1, long var2) {
      CharSequence var4 = var0.getExtras().getCharSequence("venueTitle");
      CharSequence var5 = var0.getExtras().getCharSequence("venueAddress");
      Object var8;
      if (var4 == null && var5 == null) {
         var8 = new TLRPC.TL_messageMediaGeo();
      } else {
         TLRPC.TL_messageMediaVenue var6 = new TLRPC.TL_messageMediaVenue();
         String var7;
         if (var5 == null) {
            var7 = "";
         } else {
            var7 = var5.toString();
         }

         var6.address = var7;
         if (var4 == null) {
            var7 = "";
         } else {
            var7 = var4.toString();
         }

         var6.title = var7;
         var6.provider = "";
         var6.venue_id = "";
         var8 = var6;
      }

      ((TLRPC.MessageMedia)var8).geo = new TLRPC.TL_geoPoint();
      ((TLRPC.MessageMedia)var8).geo.lat = var0.getLatitude();
      ((TLRPC.MessageMedia)var8).geo._long = var0.getLongitude();
      getInstance(var1).sendMessage((TLRPC.MessageMedia)var8, var2, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
   }

   // $FF: synthetic method
   static void lambda$null$50(Location var0, int var1, long var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$XrAgoSDrI045Iz_lMLNSunJUqGU(var0, var1, var2));
   }

   // $FF: synthetic method
   static void lambda$null$52(TLRPC.TL_document var0, int var1, String var2, long var3, MessageObject var5, TLRPC.BotInlineResult var6, HashMap var7, TLRPC.TL_photo var8, TLRPC.TL_game var9) {
      if (var0 != null) {
         SendMessagesHelper var14 = getInstance(var1);
         TLRPC.BotInlineMessage var13 = var6.send_message;
         var14.sendMessage(var0, (VideoEditedInfo)null, var2, var3, var5, var13.message, var13.entities, var13.reply_markup, var7, 0, var6);
      } else if (var8 != null) {
         SendMessagesHelper var12 = getInstance(var1);
         TLRPC.WebDocument var10 = var6.content;
         String var11;
         if (var10 != null) {
            var11 = var10.url;
         } else {
            var11 = null;
         }

         TLRPC.BotInlineMessage var15 = var6.send_message;
         var12.sendMessage(var8, var11, var3, var5, var15.message, var15.entities, var15.reply_markup, var7, 0, var6);
      } else if (var9 != null) {
         getInstance(var1).sendMessage(var9, var3, var6.send_message.reply_markup, var7);
      }

   }

   // $FF: synthetic method
   static void lambda$null$54(String var0, int var1, long var2) {
      String var4 = getTrimmedString(var0);
      if (var4.length() != 0) {
         int var5 = (int)Math.ceil((double)((float)var4.length() / 4096.0F));
         int var6 = 0;

         while(true) {
            int var7 = var6;
            if (var6 >= var5) {
               break;
            }

            ++var6;
            var0 = var4.substring(var7 * 4096, Math.min(var6 * 4096, var4.length()));
            getInstance(var1).sendMessage(var0, var2, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
         }
      }

   }

   // $FF: synthetic method
   static void lambda$null$55(String var0, int var1, long var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$oKRDK_mjDEAeKeMOR2bAQ2LKSjU(var0, var1, var2));
   }

   // $FF: synthetic method
   static void lambda$null$57(SendMessagesHelper.MediaSendPrepareWorker var0, int var1, SendMessagesHelper.SendingMediaInfo var2, boolean var3) {
      var0.photo = getInstance(var1).generatePhotoSizes(var2.path, var2.uri);
      if (var3 && var2.canDeleteAfter) {
         (new File(var2.path)).delete();
      }

      var0.sync.countDown();
   }

   // $FF: synthetic method
   static void lambda$null$58(MessageObject var0, int var1, TLRPC.TL_document var2, String var3, HashMap var4, String var5, long var6, MessageObject var8, SendMessagesHelper.SendingMediaInfo var9) {
      if (var0 != null) {
         getInstance(var1).editMessageMedia(var0, (TLRPC.TL_photo)null, (VideoEditedInfo)null, var2, var3, var4, false, var5);
      } else {
         getInstance(var1).sendMessage(var2, (VideoEditedInfo)null, var3, var6, var8, var9.caption, var9.entities, (TLRPC.ReplyMarkup)null, var4, 0, var5);
      }

   }

   // $FF: synthetic method
   static void lambda$null$59(MessageObject var0, int var1, TLRPC.TL_photo var2, boolean var3, SendMessagesHelper.SendingMediaInfo var4, HashMap var5, String var6, long var7, MessageObject var9) {
      SendMessagesHelper var10 = null;
      SendMessagesHelper var11 = null;
      if (var0 != null) {
         var10 = getInstance(var1);
         String var13 = var11;
         if (var3) {
            var13 = var4.searchImage.imageUrl;
         }

         var10.editMessageMedia(var0, var2, (VideoEditedInfo)null, (TLRPC.TL_document)null, var13, var5, false, var6);
      } else {
         var11 = getInstance(var1);
         String var12 = var10;
         if (var3) {
            var12 = var4.searchImage.imageUrl;
         }

         var11.sendMessage(var2, var12, var7, var9, var4.caption, var4.entities, (TLRPC.ReplyMarkup)null, var5, var4.ttl, var6);
      }

   }

   // $FF: synthetic method
   static void lambda$null$60(Bitmap var0, String var1, MessageObject var2, int var3, VideoEditedInfo var4, TLRPC.TL_document var5, String var6, HashMap var7, String var8, long var9, MessageObject var11, SendMessagesHelper.SendingMediaInfo var12) {
      if (var0 != null && var1 != null) {
         ImageLoader.getInstance().putImageToCache(new BitmapDrawable(var0), var1);
      }

      if (var2 != null) {
         getInstance(var3).editMessageMedia(var2, (TLRPC.TL_photo)null, var4, var5, var6, var7, false, var8);
      } else {
         getInstance(var3).sendMessage(var5, var4, var6, var9, var11, var12.caption, var12.entities, (TLRPC.ReplyMarkup)null, var7, var12.ttl, var8);
      }

   }

   // $FF: synthetic method
   static void lambda$null$61(Bitmap[] var0, String[] var1, MessageObject var2, int var3, TLRPC.TL_photo var4, HashMap var5, String var6, long var7, MessageObject var9, SendMessagesHelper.SendingMediaInfo var10) {
      if (var0[0] != null && var1[0] != null) {
         ImageLoader.getInstance().putImageToCache(new BitmapDrawable(var0[0]), var1[0]);
      }

      if (var2 != null) {
         getInstance(var3).editMessageMedia(var2, var4, (VideoEditedInfo)null, (TLRPC.TL_document)null, (String)null, var5, false, var6);
      } else {
         getInstance(var3).sendMessage(var4, (String)null, var7, var9, var10.caption, var10.entities, (TLRPC.ReplyMarkup)null, var5, var10.ttl, var6);
      }

   }

   // $FF: synthetic method
   static void lambda$null$62(int var0, long var1) {
      SendMessagesHelper var3 = getInstance(var0);
      HashMap var4 = var3.delayedMessages;
      StringBuilder var5 = new StringBuilder();
      var5.append("group_");
      var5.append(var1);
      ArrayList var9 = (ArrayList)var4.get(var5.toString());
      if (var9 != null && !var9.isEmpty()) {
         SendMessagesHelper.DelayedMessage var10 = (SendMessagesHelper.DelayedMessage)var9.get(0);
         ArrayList var7 = var10.messageObjects;
         MessageObject var6 = (MessageObject)var7.get(var7.size() - 1);
         var10.finalGroupMessage = var6.getId();
         var6.messageOwner.params.put("final", "1");
         TLRPC.TL_messages_messages var8 = new TLRPC.TL_messages_messages();
         var8.messages.add(var6.messageOwner);
         MessagesStorage.getInstance(var0).putMessages(var8, var10.peer, -2, 0, false);
         var3.sendReadyToSendGroup(var10, true, true);
      }

   }

   // $FF: synthetic method
   static void lambda$null$64(Bitmap var0, String var1, MessageObject var2, int var3, VideoEditedInfo var4, TLRPC.TL_document var5, String var6, HashMap var7, String var8, long var9, MessageObject var11, String var12, ArrayList var13, int var14) {
      if (var0 != null && var1 != null) {
         ImageLoader.getInstance().putImageToCache(new BitmapDrawable(var0), var1);
      }

      if (var2 != null) {
         getInstance(var3).editMessageMedia(var2, (TLRPC.TL_photo)null, var4, var5, var6, var7, false, var8);
      } else {
         getInstance(var3).sendMessage(var5, var4, var6, var9, var11, var12, var13, (TLRPC.ReplyMarkup)null, var7, var14, var8);
      }

   }

   // $FF: synthetic method
   static void lambda$prepareSendingAudioDocuments$46(ArrayList var0, long var1, int var3, MessageObject var4, MessageObject var5) {
      int var6 = var0.size();

      for(int var7 = 0; var7 < var6; ++var7) {
         MessageObject var8 = (MessageObject)var0.get(var7);
         String var9 = var8.messageOwner.attachPath;
         File var10 = new File(var9);
         boolean var11;
         if ((int)var1 == 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         String var12 = var9;
         if (var9 != null) {
            StringBuilder var20 = new StringBuilder();
            var20.append(var9);
            var20.append("audio");
            var20.append(var10.length());
            var12 = var20.toString();
         }

         TLRPC.TL_document var15;
         String var19;
         label44: {
            var15 = null;
            if (!var11) {
               MessagesStorage var16 = MessagesStorage.getInstance(var3);
               byte var13;
               if (!var11) {
                  var13 = 1;
               } else {
                  var13 = 4;
               }

               Object[] var18 = var16.getSentFile(var12, var13);
               if (var18 != null) {
                  var15 = (TLRPC.TL_document)var18[0];
                  var19 = (String)var18[1];
                  ensureMediaThumbExists(var3, var11, var15, var12, (Uri)null, 0L);
                  break label44;
               }
            }

            var19 = null;
         }

         TLRPC.TL_document var14 = var15;
         if (var15 == null) {
            var14 = (TLRPC.TL_document)var8.messageOwner.media.document;
         }

         if (var11) {
            int var21 = (int)(var1 >> 32);
            if (MessagesController.getInstance(var3).getEncryptedChat(var21) == null) {
               return;
            }
         }

         HashMap var17 = new HashMap();
         if (var12 != null) {
            var17.put("originalPath", var12);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$ScXQOgmc_sCdZ0TlvzYeQcRtFgQ(var4, var3, var14, var8, var17, var19, var1, var5));
      }

   }

   // $FF: synthetic method
   static void lambda$prepareSendingBotContextResult$53(long var0, TLRPC.BotInlineResult var2, int var3, HashMap var4, MessageObject var5) {
      int var6 = (int)var0;
      boolean var7;
      if (var6 == 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      Object var8;
      TLRPC.Document var9;
      Object var10;
      Object var11;
      TLRPC.TL_document var32;
      if ("game".equals(var2.type)) {
         if (var6 == 0) {
            return;
         }

         var8 = new TLRPC.TL_game();
         ((TLRPC.TL_game)var8).title = var2.title;
         ((TLRPC.TL_game)var8).description = var2.description;
         ((TLRPC.TL_game)var8).short_name = var2.id;
         ((TLRPC.TL_game)var8).photo = var2.photo;
         if (((TLRPC.TL_game)var8).photo == null) {
            ((TLRPC.TL_game)var8).photo = new TLRPC.TL_photoEmpty();
         }

         var9 = var2.document;
         if (var9 instanceof TLRPC.TL_document) {
            ((TLRPC.TL_game)var8).document = var9;
            ((TLRPC.TL_game)var8).flags |= 1;
         }

         var32 = null;
         var10 = var32;
         var11 = var32;
      } else {
         label309: {
            label308: {
               label307: {
                  if (var2 instanceof TLRPC.TL_botInlineMediaResult) {
                     var9 = var2.document;
                     if (var9 != null) {
                        if (var9 instanceof TLRPC.TL_document) {
                           var32 = (TLRPC.TL_document)var9;
                           break label307;
                        }
                     } else {
                        TLRPC.Photo var34 = var2.photo;
                        if (var34 != null && var34 instanceof TLRPC.TL_photo) {
                           var11 = (TLRPC.TL_photo)var34;
                           var32 = null;
                           var10 = var32;
                           var8 = var32;
                           break label309;
                        }
                     }
                  } else if (var2.content != null) {
                     File var36 = FileLoader.getDirectory(4);
                     StringBuilder var35 = new StringBuilder();
                     var35.append(Utilities.MD5(var2.content.url));
                     var35.append(".");
                     var35.append(ImageLoader.getHttpUrlExtension(var2.content.url, "file"));
                     File var37 = new File(var36, var35.toString());
                     String var38;
                     if (var37.exists()) {
                        var38 = var37.getAbsolutePath();
                     } else {
                        var38 = var2.content.url;
                     }

                     byte var29;
                     label298: {
                        String var40 = var2.type;
                        switch(var40.hashCode()) {
                        case -1890252483:
                           if (var40.equals("sticker")) {
                              var29 = 4;
                              break label298;
                           }
                           break;
                        case 102340:
                           if (var40.equals("gif")) {
                              var29 = 5;
                              break label298;
                           }
                           break;
                        case 3143036:
                           if (var40.equals("file")) {
                              var29 = 2;
                              break label298;
                           }
                           break;
                        case 93166550:
                           if (var40.equals("audio")) {
                              var29 = 0;
                              break label298;
                           }
                           break;
                        case 106642994:
                           if (var40.equals("photo")) {
                              var29 = 6;
                              break label298;
                           }
                           break;
                        case 112202875:
                           if (var40.equals("video")) {
                              var29 = 3;
                              break label298;
                           }
                           break;
                        case 112386354:
                           if (var40.equals("voice")) {
                              var29 = 1;
                              break label298;
                           }
                        }

                        var29 = -1;
                     }

                     TLRPC.PhotoSize var12;
                     int[] var31;
                     String var43;
                     switch(var29) {
                     case 0:
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                     case 5:
                        TLRPC.TL_documentAttributeFilename var33;
                        TLRPC.TL_document var52;
                        label284: {
                           var52 = new TLRPC.TL_document();
                           var52.id = 0L;
                           var52.size = 0;
                           var52.dc_id = 0;
                           var52.mime_type = var2.content.mime_type;
                           var52.file_reference = new byte[0];
                           var52.date = ConnectionsManager.getInstance(var3).getCurrentTime();
                           var33 = new TLRPC.TL_documentAttributeFilename();
                           var52.attributes.add(var33);
                           String var44 = var2.type;
                           switch(var44.hashCode()) {
                           case -1890252483:
                              if (var44.equals("sticker")) {
                                 var29 = 5;
                                 break label284;
                              }
                              break;
                           case 102340:
                              if (var44.equals("gif")) {
                                 var29 = 0;
                                 break label284;
                              }
                              break;
                           case 3143036:
                              if (var44.equals("file")) {
                                 var29 = 3;
                                 break label284;
                              }
                              break;
                           case 93166550:
                              if (var44.equals("audio")) {
                                 var29 = 2;
                                 break label284;
                              }
                              break;
                           case 112202875:
                              if (var44.equals("video")) {
                                 var29 = 4;
                                 break label284;
                              }
                              break;
                           case 112386354:
                              if (var44.equals("voice")) {
                                 var29 = 1;
                                 break label284;
                              }
                           }

                           var29 = -1;
                        }

                        byte var14 = 55;
                        Throwable var10000;
                        boolean var10001;
                        Throwable var54;
                        if (var29 != 0) {
                           TLRPC.TL_documentAttributeAudio var56;
                           if (var29 == 1) {
                              var56 = new TLRPC.TL_documentAttributeAudio();
                              var56.duration = MessageObject.getInlineResultDuration(var2);
                              var56.voice = true;
                              var33.file_name = "audio.ogg";
                              var52.attributes.add(var56);
                           } else if (var29 != 2) {
                              if (var29 == 3) {
                                 var6 = var2.content.mime_type.lastIndexOf(47);
                                 if (var6 != -1) {
                                    var35 = new StringBuilder();
                                    var35.append("file.");
                                    var35.append(var2.content.mime_type.substring(var6 + 1));
                                    var33.file_name = var35.toString();
                                 } else {
                                    var33.file_name = "file";
                                 }
                              } else {
                                 Bitmap var50;
                                 TLRPC.PhotoSize var59;
                                 if (var29 != 4) {
                                    if (var29 == 5) {
                                       label330: {
                                          TLRPC.TL_documentAttributeSticker var60 = new TLRPC.TL_documentAttributeSticker();
                                          var60.alt = "";
                                          var60.stickerset = new TLRPC.TL_inputStickerSetEmpty();
                                          var52.attributes.add(var60);
                                          TLRPC.TL_documentAttributeImageSize var61 = new TLRPC.TL_documentAttributeImageSize();
                                          int[] var53 = MessageObject.getInlineResultWidthAndHeight(var2);
                                          var61.w = var53[0];
                                          var61.h = var53[1];
                                          var52.attributes.add(var61);
                                          var33.file_name = "sticker.webp";

                                          label321: {
                                             try {
                                                if (var2.thumb == null) {
                                                   break label330;
                                                }

                                                File var51 = FileLoader.getDirectory(4);
                                                StringBuilder var55 = new StringBuilder();
                                                var55.append(Utilities.MD5(var2.thumb.url));
                                                var55.append(".");
                                                var55.append(ImageLoader.getHttpUrlExtension(var2.thumb.url, "webp"));
                                                var37 = new File(var51, var55.toString());
                                                var50 = ImageLoader.loadBitmap(var37.getAbsolutePath(), (Uri)null, 90.0F, 90.0F, true);
                                             } catch (Throwable var19) {
                                                var10000 = var19;
                                                var10001 = false;
                                                break label321;
                                             }

                                             if (var50 == null) {
                                                break label330;
                                             }

                                             try {
                                                var59 = ImageLoader.scaleAndSaveImage(var50, 90.0F, 90.0F, 55, false);
                                             } catch (Throwable var18) {
                                                var10000 = var18;
                                                var10001 = false;
                                                break label321;
                                             }

                                             if (var59 != null) {
                                                try {
                                                   var52.thumbs.add(var59);
                                                   var52.flags |= 1;
                                                } catch (Throwable var17) {
                                                   var10000 = var17;
                                                   var10001 = false;
                                                   break label321;
                                                }
                                             }

                                             try {
                                                var50.recycle();
                                                break label330;
                                             } catch (Throwable var16) {
                                                var10000 = var16;
                                                var10001 = false;
                                             }
                                          }

                                          var54 = var10000;
                                          FileLog.e(var54);
                                       }
                                    }
                                 } else {
                                    label329: {
                                       var33.file_name = "video.mp4";
                                       TLRPC.TL_documentAttributeVideo var45 = new TLRPC.TL_documentAttributeVideo();
                                       int[] var57 = MessageObject.getInlineResultWidthAndHeight(var2);
                                       var45.w = var57[0];
                                       var45.h = var57[1];
                                       var45.duration = MessageObject.getInlineResultDuration(var2);
                                       var45.supports_streaming = true;
                                       var52.attributes.add(var45);

                                       label322: {
                                          try {
                                             if (var2.thumb == null) {
                                                break label329;
                                             }

                                             var37 = FileLoader.getDirectory(4);
                                             StringBuilder var48 = new StringBuilder();
                                             var48.append(Utilities.MD5(var2.thumb.url));
                                             var48.append(".");
                                             var48.append(ImageLoader.getHttpUrlExtension(var2.thumb.url, "jpg"));
                                             File var49 = new File(var37, var48.toString());
                                             var50 = ImageLoader.loadBitmap(var49.getAbsolutePath(), (Uri)null, 90.0F, 90.0F, true);
                                          } catch (Throwable var23) {
                                             var10000 = var23;
                                             var10001 = false;
                                             break label322;
                                          }

                                          if (var50 == null) {
                                             break label329;
                                          }

                                          try {
                                             var59 = ImageLoader.scaleAndSaveImage(var50, 90.0F, 90.0F, 55, false);
                                          } catch (Throwable var22) {
                                             var10000 = var22;
                                             var10001 = false;
                                             break label322;
                                          }

                                          if (var59 != null) {
                                             try {
                                                var52.thumbs.add(var59);
                                                var52.flags |= 1;
                                             } catch (Throwable var21) {
                                                var10000 = var21;
                                                var10001 = false;
                                                break label322;
                                             }
                                          }

                                          try {
                                             var50.recycle();
                                             break label329;
                                          } catch (Throwable var20) {
                                             var10000 = var20;
                                             var10001 = false;
                                          }
                                       }

                                       var54 = var10000;
                                       FileLog.e(var54);
                                    }
                                 }
                              }
                           } else {
                              var56 = new TLRPC.TL_documentAttributeAudio();
                              var56.duration = MessageObject.getInlineResultDuration(var2);
                              var56.title = var2.title;
                              var56.flags |= 1;
                              var43 = var2.description;
                              if (var43 != null) {
                                 var56.performer = var43;
                                 var56.flags |= 2;
                              }

                              var33.file_name = "audio.mp3";
                              var52.attributes.add(var56);
                           }
                        } else {
                           label325: {
                              var33.file_name = "animation.gif";
                              if (var38.endsWith("mp4")) {
                                 var52.mime_type = "video/mp4";
                                 var52.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                              } else {
                                 var52.mime_type = "image/gif";
                              }

                              short var41;
                              if (var7) {
                                 var41 = 90;
                              } else {
                                 var41 = 320;
                              }

                              label332: {
                                 float var15;
                                 Bitmap var47;
                                 label324: {
                                    try {
                                       if (var38.endsWith("mp4")) {
                                          var47 = ThumbnailUtils.createVideoThumbnail(var38, 1);
                                          break label324;
                                       }
                                    } catch (Throwable var28) {
                                       var10000 = var28;
                                       var10001 = false;
                                       break label332;
                                    }

                                    var15 = (float)var41;

                                    try {
                                       var47 = ImageLoader.loadBitmap(var38, (Uri)null, var15, var15, true);
                                    } catch (Throwable var27) {
                                       var10000 = var27;
                                       var10001 = false;
                                       break label332;
                                    }
                                 }

                                 if (var47 == null) {
                                    break label325;
                                 }

                                 var15 = (float)var41;
                                 byte var30 = var14;
                                 if (var41 > 90) {
                                    var30 = 80;
                                 }

                                 try {
                                    var12 = ImageLoader.scaleAndSaveImage(var47, var15, var15, var30, false);
                                 } catch (Throwable var26) {
                                    var10000 = var26;
                                    var10001 = false;
                                    break label332;
                                 }

                                 if (var12 != null) {
                                    try {
                                       var52.thumbs.add(var12);
                                       var52.flags |= 1;
                                    } catch (Throwable var25) {
                                       var10000 = var25;
                                       var10001 = false;
                                       break label332;
                                    }
                                 }

                                 try {
                                    var47.recycle();
                                    break label325;
                                 } catch (Throwable var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                 }
                              }

                              var54 = var10000;
                              FileLog.e(var54);
                           }
                        }

                        if (var33.file_name == null) {
                           var33.file_name = "file";
                        }

                        if (var52.mime_type == null) {
                           var52.mime_type = "application/octet-stream";
                        }

                        if (var52.thumbs.isEmpty()) {
                           TLRPC.TL_photoSize var62 = new TLRPC.TL_photoSize();
                           var31 = MessageObject.getInlineResultWidthAndHeight(var2);
                           var62.w = var31[0];
                           var62.h = var31[1];
                           var62.size = 0;
                           var62.location = new TLRPC.TL_fileLocationUnavailable();
                           var62.type = "x";
                           var52.thumbs.add(var62);
                           var52.flags |= 1;
                        }

                        var10 = var38;
                        var32 = var52;
                        var11 = null;
                        break label308;
                     case 6:
                        TLRPC.TL_photo var42;
                        if (var37.exists()) {
                           var42 = getInstance(var3).generatePhotoSizes(var38, (Uri)null);
                        } else {
                           var42 = null;
                        }

                        TLRPC.TL_photo var39 = var42;
                        if (var42 == null) {
                           var39 = new TLRPC.TL_photo();
                           var39.date = ConnectionsManager.getInstance(var3).getCurrentTime();
                           var39.file_reference = new byte[0];
                           TLRPC.TL_photoSize var46 = new TLRPC.TL_photoSize();
                           var31 = MessageObject.getInlineResultWidthAndHeight(var2);
                           var46.w = var31[0];
                           var46.h = var31[1];
                           var46.size = 1;
                           var46.location = new TLRPC.TL_fileLocationUnavailable();
                           var46.type = "x";
                           var39.sizes.add(var46);
                        }

                        var12 = null;
                        var8 = var12;
                        String var13 = var38;
                        var11 = var39;
                        var32 = var12;
                        var10 = var13;
                        break label309;
                     default:
                        var10 = null;
                        var8 = var10;
                        var43 = var38;
                        var11 = var10;
                        var32 = (TLRPC.TL_document)var10;
                        var10 = var43;
                        break label309;
                     }
                  }

                  var32 = null;
               }

               var10 = null;
               var11 = null;
            }

            var8 = var11;
         }
      }

      if (var4 != null) {
         TLRPC.WebDocument var58 = var2.content;
         if (var58 != null) {
            var4.put("originalPath", var58.url);
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$NAGNwZznFWBfaOyQvFbS7VcD9Ac(var32, var3, (String)var10, var0, var5, var2, var4, (TLRPC.TL_photo)var11, (TLRPC.TL_game)var8));
   }

   // $FF: synthetic method
   static void lambda$prepareSendingDocumentInternal$44(MessageObject var0, int var1, TLRPC.TL_document var2, String var3, HashMap var4, String var5, long var6, MessageObject var8, String var9, ArrayList var10) {
      if (var0 != null) {
         getInstance(var1).editMessageMedia(var0, (TLRPC.TL_photo)null, (VideoEditedInfo)null, var2, var3, var4, false, var5);
      } else {
         getInstance(var1).sendMessage(var2, (VideoEditedInfo)null, var3, var6, var8, var9, var10, (TLRPC.ReplyMarkup)null, var4, 0, var5);
      }

   }

   // $FF: synthetic method
   static void lambda$prepareSendingDocuments$48(ArrayList var0, int var1, ArrayList var2, String var3, long var4, MessageObject var6, String var7, MessageObject var8, ArrayList var9, InputContentInfoCompat var10) {
      byte var11 = 0;
      int var12;
      boolean var13;
      boolean var14;
      if (var0 != null) {
         var12 = 0;
         var13 = false;

         while(true) {
            var14 = var13;
            if (var12 >= var0.size()) {
               break;
            }

            if (!prepareSendingDocumentInternal(var1, (String)var0.get(var12), (String)var2.get(var12), (Uri)null, var3, var4, var6, var7, (ArrayList)null, var8, false)) {
               var13 = true;
            }

            ++var12;
         }
      } else {
         var14 = false;
      }

      var13 = var14;
      if (var9 != null) {
         var12 = var11;

         while(true) {
            var13 = var14;
            if (var12 >= var9.size()) {
               break;
            }

            if (!prepareSendingDocumentInternal(var1, (String)null, (String)null, (Uri)var9.get(var12), var3, var4, var6, var7, (ArrayList)null, var8, false)) {
               var14 = true;
            }

            ++var12;
         }
      }

      if (var10 != null) {
         var10.releasePermission();
      }

      if (var13) {
         AndroidUtilities.runOnUIThread(_$$Lambda$SendMessagesHelper$AzNNw46d9NqQLWk4ri_KgnUrWCU.INSTANCE);
      }

   }

   // $FF: synthetic method
   static void lambda$prepareSendingLocation$51(Location var0, int var1, long var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$CCL59p4Z_8FFpdg0kalcFKLSOEA(var0, var1, var2));
   }

   // $FF: synthetic method
   static void lambda$prepareSendingMedia$63(ArrayList var0, long var1, int var3, boolean var4, boolean var5, MessageObject var6, MessageObject var7, InputContentInfoCompat var8) {
      long var9 = System.currentTimeMillis();
      int var11 = var0.size();
      boolean var12;
      if ((int)var1 == 0) {
         var12 = true;
      } else {
         var12 = false;
      }

      int var13;
      label752: {
         if (var12) {
            var13 = (int)(var1 >> 32);
            TLRPC.EncryptedChat var14 = MessagesController.getInstance(var3).getEncryptedChat(var13);
            if (var14 != null) {
               var13 = AndroidUtilities.getPeerLayerVersion(var14.layer);
               break label752;
            }
         }

         var13 = 0;
      }

      String var15 = ".gif";
      String var16 = "_";
      int var18;
      String var20;
      String var23;
      String var24;
      HashMap var58;
      if ((!var12 || var13 >= 73) && !var4 && var5) {
         HashMap var17 = new HashMap();

         for(var18 = 0; var18 < var11; ++var18) {
            SendMessagesHelper.SendingMediaInfo var19 = (SendMessagesHelper.SendingMediaInfo)var0.get(var18);
            if (var19.searchImage == null && !var19.isVideo) {
               String var60;
               label733: {
                  var20 = var19.path;
                  if (var20 == null) {
                     Uri var59 = var19.uri;
                     if (var59 != null) {
                        var20 = AndroidUtilities.getPath(var59);
                        var60 = var19.uri.toString();
                        break label733;
                     }
                  }

                  var60 = var20;
               }

               if ((var20 == null || !var20.endsWith(var15) && !var20.endsWith(".webp")) && !ImageLoader.shouldSendImageAsDocument(var19.path, var19.uri)) {
                  if (var20 == null) {
                     Uri var21 = var19.uri;
                     if (var21 != null && (MediaController.isGif(var21) || MediaController.isWebp(var19.uri))) {
                        continue;
                     }
                  }

                  if (var20 != null) {
                     File var68 = new File(var20);
                     StringBuilder var69 = new StringBuilder();
                     var69.append(var60);
                     var69.append(var68.length());
                     var69.append(var16);
                     var69.append(var68.lastModified());
                     var60 = var69.toString();
                  } else {
                     var60 = null;
                  }

                  TLRPC.TL_photo var72;
                  if (!var12 && var19.ttl == 0) {
                     MessagesStorage var74 = MessagesStorage.getInstance(var3);
                     byte var22;
                     if (!var12) {
                        var22 = 0;
                     } else {
                        var22 = 3;
                     }

                     Object[] var75 = var74.getSentFile(var60, var22);
                     TLRPC.TL_photo var64;
                     if (var75 != null) {
                        var64 = (TLRPC.TL_photo)var75[0];
                        var20 = (String)var75[1];
                     } else {
                        var20 = null;
                        var64 = null;
                     }

                     TLRPC.TL_photo var73;
                     label716: {
                        var23 = var20;
                        var73 = var64;
                        if (var64 == null) {
                           var23 = var20;
                           var73 = var64;
                           if (var19.uri != null) {
                              MessagesStorage var76 = MessagesStorage.getInstance(var3);
                              var23 = AndroidUtilities.getPath(var19.uri);
                              if (!var12) {
                                 var22 = 0;
                              } else {
                                 var22 = 3;
                              }

                              Object[] var80 = var76.getSentFile(var23, var22);
                              var73 = var64;
                              var60 = var20;
                              if (var80 == null) {
                                 break label716;
                              }

                              var73 = (TLRPC.TL_photo)var80[0];
                              var23 = (String)var80[1];
                           }
                        }

                        var60 = var23;
                     }

                     var72 = var73;
                     ensureMediaThumbExists(var3, var12, var73, var19.path, var19.uri, 0L);
                  } else {
                     var60 = null;
                     var72 = null;
                  }

                  SendMessagesHelper.MediaSendPrepareWorker var77 = new SendMessagesHelper.MediaSendPrepareWorker();
                  var17.put(var19, var77);
                  if (var72 != null) {
                     var77.parentObject = var60;
                     var77.photo = var72;
                  } else {
                     var77.sync = new CountDownLatch(1);
                     mediaSendThreadPool.execute(new _$$Lambda$SendMessagesHelper$TY9aOb7aeyFhqWsPKbd7o8zCP_o(var77, var3, var19, var12));
                  }
               }
            }
         }

         var20 = var16;
         var58 = var17;
         var24 = var15;
      } else {
         var20 = "_";
         var24 = ".gif";
         var58 = null;
      }

      ArrayList var65 = null;
      ArrayList var61 = var65;
      Object var63 = var65;
      ArrayList var78 = var65;
      ArrayList var62 = var65;
      byte var25 = 0;
      long var26 = 0L;
      var18 = 0;
      long var28 = 0L;
      int var30 = var13;
      int var79 = var11;

      long var32;
      for(var13 = var25; var18 < var79; var28 = var32) {
         SendMessagesHelper.SendingMediaInfo var31 = (SendMessagesHelper.SendingMediaInfo)var0.get(var18);
         if (var5 && (!var12 || var30 >= 73) && var79 > 1 && var13 % 10 == 0) {
            var26 = Utilities.random.nextLong();
            var28 = var26;
            var13 = 0;
         } else {
            var32 = var28;
            var28 = var26;
            var26 = var32;
         }

         MediaController.SearchImage var81 = var31.searchImage;
         StringBuilder var35;
         StringBuilder var37;
         float var38;
         boolean var39;
         ArrayList var82;
         String var98;
         String var103;
         Bitmap var106;
         File var116;
         if (var81 != null) {
            if (var81.type == 1) {
               HashMap var101 = new HashMap();
               TLRPC.Document var91 = var31.searchImage.document;
               File var92;
               TLRPC.TL_document var129;
               if (var91 instanceof TLRPC.TL_document) {
                  var129 = (TLRPC.TL_document)var91;
                  var92 = FileLoader.getPathToAttach(var129, true);
               } else {
                  StringBuilder var93 = new StringBuilder();
                  var93.append(Utilities.MD5(var31.searchImage.imageUrl));
                  var93.append(".");
                  var93.append(ImageLoader.getHttpUrlExtension(var31.searchImage.imageUrl, "jpg"));
                  var23 = var93.toString();
                  var92 = new File(FileLoader.getDirectory(4), var23);
                  var129 = null;
               }

               if (var129 == null) {
                  var98 = var31.searchImage.localUrl;
                  if (var98 != null) {
                     var101.put("url", var98);
                  }

                  TLRPC.TL_document var115 = new TLRPC.TL_document();
                  var115.id = 0L;
                  var115.file_reference = new byte[0];
                  var115.date = ConnectionsManager.getInstance(var3).getCurrentTime();
                  TLRPC.TL_documentAttributeFilename var132 = new TLRPC.TL_documentAttributeFilename();
                  var132.file_name = "animation.gif";
                  var115.attributes.add(var132);
                  var115.size = var31.searchImage.size;
                  var115.dc_id = 0;
                  if (var92.toString().endsWith("mp4")) {
                     var115.mime_type = "video/mp4";
                     var115.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                  } else {
                     var115.mime_type = "image/gif";
                  }

                  if (var92.exists()) {
                     var116 = var92;
                     var92 = var92;
                  } else {
                     var116 = null;
                     var92 = null;
                  }

                  File var124 = var116;
                  if (var116 == null) {
                     var35 = new StringBuilder();
                     var35.append(Utilities.MD5(var31.searchImage.thumbUrl));
                     var35.append(".");
                     var35.append(ImageLoader.getHttpUrlExtension(var31.searchImage.thumbUrl, "jpg"));
                     var98 = var35.toString();
                     var116 = new File(FileLoader.getDirectory(4), var98);
                     var124 = var116;
                     if (!var116.exists()) {
                        var124 = null;
                     }
                  }

                  if (var124 != null) {
                     label503: {
                        Exception var133;
                        label502: {
                           Exception var10000;
                           label764: {
                              boolean var10001;
                              short var56;
                              label500: {
                                 label765: {
                                    label498: {
                                       label497: {
                                          if (!var12) {
                                             try {
                                                if (var31.ttl == 0) {
                                                   break label497;
                                                }
                                             } catch (Exception var51) {
                                                var10000 = var51;
                                                var10001 = false;
                                                break label765;
                                             }
                                          }

                                          var56 = 90;
                                          break label498;
                                       }

                                       var56 = 320;
                                    }

                                    try {
                                       if (var124.getAbsolutePath().endsWith("mp4")) {
                                          var106 = ThumbnailUtils.createVideoThumbnail(var124.getAbsolutePath(), 1);
                                          break label500;
                                       }
                                    } catch (Exception var50) {
                                       var10000 = var50;
                                       var10001 = false;
                                       break label765;
                                    }

                                    try {
                                       var98 = var124.getAbsolutePath();
                                    } catch (Exception var49) {
                                       var10000 = var49;
                                       var10001 = false;
                                       break label765;
                                    }

                                    var38 = (float)var56;

                                    try {
                                       var106 = ImageLoader.loadBitmap(var98, (Uri)null, var38, var38, true);
                                       break label500;
                                    } catch (Exception var48) {
                                       var10000 = var48;
                                       var10001 = false;
                                       break label764;
                                    }
                                 }

                                 var133 = var10000;
                                 break label502;
                              }

                              if (var106 == null) {
                                 break label503;
                              }

                              var38 = (float)var56;
                              byte var57;
                              if (var56 > 90) {
                                 var57 = 80;
                              } else {
                                 var57 = 55;
                              }

                              TLRPC.PhotoSize var128;
                              try {
                                 var128 = ImageLoader.scaleAndSaveImage(var106, var38, var38, var57, var12);
                              } catch (Exception var47) {
                                 var10000 = var47;
                                 var10001 = false;
                                 break label764;
                              }

                              if (var128 != null) {
                                 try {
                                    var115.thumbs.add(var128);
                                    var115.flags |= 1;
                                 } catch (Exception var46) {
                                    var10000 = var46;
                                    var10001 = false;
                                    break label764;
                                 }
                              }

                              try {
                                 var106.recycle();
                                 break label503;
                              } catch (Exception var45) {
                                 var10000 = var45;
                                 var10001 = false;
                              }
                           }

                           var133 = var10000;
                        }

                        FileLog.e((Throwable)var133);
                     }
                  }

                  if (var115.thumbs.isEmpty()) {
                     TLRPC.TL_photoSize var130 = new TLRPC.TL_photoSize();
                     MediaController.SearchImage var134 = var31.searchImage;
                     var130.w = var134.width;
                     var130.h = var134.height;
                     var130.size = 0;
                     var130.location = new TLRPC.TL_fileLocationUnavailable();
                     var130.type = "x";
                     var115.thumbs.add(var130);
                     var115.flags |= 1;
                  }

                  var129 = var115;
               }

               var103 = var31.searchImage.imageUrl;
               if (var92 == null) {
                  var23 = var103;
               } else {
                  var23 = var92.toString();
               }

               var103 = var31.searchImage.imageUrl;
               if (var103 != null) {
                  var101.put("originalPath", var103);
               }

               AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$FEI9NpqFFi9VBzbZvdN8Ccx7XJA(var6, var3, var129, var23, var101, (String)null, var1, var7, var31));
            } else {
               TLRPC.Photo var95 = var81.photo;
               TLRPC.TL_photo var96;
               if (var95 instanceof TLRPC.TL_photo) {
                  var96 = (TLRPC.TL_photo)var95;
               } else {
                  if (!var12) {
                     var11 = var31.ttl;
                  }

                  var96 = null;
               }

               if (var96 != null) {
                  var39 = true;
               } else {
                  TLRPC.TL_photo var135;
                  label527: {
                     var35 = new StringBuilder();
                     var35.append(Utilities.MD5(var31.searchImage.imageUrl));
                     var35.append(".");
                     var35.append(ImageLoader.getHttpUrlExtension(var31.searchImage.imageUrl, "jpg"));
                     var98 = var35.toString();
                     var116 = new File(FileLoader.getDirectory(4), var98);
                     if (var116.exists() && var116.length() != 0L) {
                        var135 = getInstance(var3).generatePhotoSizes(var116.toString(), (Uri)null);
                        var96 = var135;
                        if (var135 != null) {
                           var39 = false;
                           var96 = var135;
                           break label527;
                        }
                     }

                     var39 = true;
                  }

                  var135 = var96;
                  if (var96 == null) {
                     var35 = new StringBuilder();
                     var35.append(Utilities.MD5(var31.searchImage.thumbUrl));
                     var35.append(".");
                     var35.append(ImageLoader.getHttpUrlExtension(var31.searchImage.thumbUrl, "jpg"));
                     var98 = var35.toString();
                     var116 = new File(FileLoader.getDirectory(4), var98);
                     if (var116.exists()) {
                        var96 = getInstance(var3).generatePhotoSizes(var116.toString(), (Uri)null);
                     }

                     var135 = var96;
                     if (var96 == null) {
                        var135 = new TLRPC.TL_photo();
                        var135.date = ConnectionsManager.getInstance(var3).getCurrentTime();
                        var135.file_reference = new byte[0];
                        TLRPC.TL_photoSize var97 = new TLRPC.TL_photoSize();
                        MediaController.SearchImage var131 = var31.searchImage;
                        var97.w = var131.width;
                        var97.h = var131.height;
                        var97.size = 0;
                        var97.location = new TLRPC.TL_fileLocationUnavailable();
                        var97.type = "x";
                        var135.sizes.add(var97);
                     }
                  }

                  var96 = var135;
               }

               if (var96 != null) {
                  HashMap var136 = new HashMap();
                  var103 = var31.searchImage.imageUrl;
                  if (var103 != null) {
                     var136.put("originalPath", var103);
                  }

                  if (var5) {
                     ++var13;
                     var37 = new StringBuilder();
                     var37.append("");
                     var37.append(var26);
                     var136.put("groupId", var37.toString());
                     if (var13 == 10 || var18 == var79 - 1) {
                        var136.put("final", "1");
                        var28 = 0L;
                     }
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg(var6, var3, var96, var39, var31, var136, (String)null, var1, var7));
               }
            }

            var82 = var65;
         } else {
            label790: {
               var82 = var65;
               String var34;
               String var70;
               int var83;
               Object var99;
               HashMap var109;
               String var119;
               if (var31.isVideo) {
                  VideoEditedInfo var66;
                  if (var4) {
                     var66 = null;
                  } else {
                     var66 = var31.videoEditedInfo;
                     if (var66 == null) {
                        var66 = createCompressionSettings(var31.path);
                     }
                  }

                  if (var4 || var66 == null && !var31.path.endsWith("mp4")) {
                     var70 = var31.path;
                     prepareSendingDocumentInternal(var3, var70, var70, (Uri)null, (String)null, var1, var7, var31.caption, var31.entities, var6, var4);
                  } else {
                     String var40 = var31.path;
                     File var41 = new File(var40);
                     var35 = new StringBuilder();
                     var35.append(var40);
                     var35.append(var41.length());
                     var35.append(var20);
                     var35.append(var41.lastModified());
                     var98 = var35.toString();
                     if (var66 != null) {
                        var39 = var66.muted;
                        var37 = new StringBuilder();
                        var37.append(var98);
                        var37.append(var66.estimatedDuration);
                        var37.append(var20);
                        var37.append(var66.startTime);
                        var37.append(var20);
                        var37.append(var66.endTime);
                        if (var66.muted) {
                           var98 = "_m";
                        } else {
                           var98 = "";
                        }

                        var37.append(var98);
                        var103 = var37.toString();
                        var98 = var103;
                        if (var66.resultWidth != var66.originalWidth) {
                           var35 = new StringBuilder();
                           var35.append(var103);
                           var35.append(var20);
                           var35.append(var66.resultWidth);
                           var98 = var35.toString();
                        }

                        var32 = var66.startTime;
                        if (var32 < 0L) {
                           var32 = 0L;
                        }
                     } else {
                        var32 = 0L;
                        var39 = false;
                     }

                     TLRPC.TL_document var105;
                     label593: {
                        if (!var12 && var31.ttl == 0) {
                           MessagesStorage var104 = MessagesStorage.getInstance(var3);
                           if (!var12) {
                              var79 = 2;
                           } else {
                              var79 = 5;
                           }

                           Object[] var36 = var104.getSentFile(var98, var79);
                           if (var36 != null) {
                              var105 = (TLRPC.TL_document)var36[0];
                              var34 = (String)var36[1];
                              ensureMediaThumbExists(var3, var12, var105, var31.path, (Uri)null, var32);
                              break label593;
                           }
                        }

                        var105 = null;
                        var34 = null;
                     }

                     if (var105 == null) {
                        Bitmap var107 = createVideoThumbnail(var31.path, var32);
                        var106 = var107;
                        if (var107 == null) {
                           var106 = ThumbnailUtils.createVideoThumbnail(var31.path, 1);
                        }

                        TLRPC.PhotoSize var102;
                        if (var106 != null) {
                           if (!var12 && var31.ttl == 0) {
                              var79 = Math.max(var106.getWidth(), var106.getHeight());
                           } else {
                              var79 = 90;
                           }

                           var38 = (float)var79;
                           if (var79 > 90) {
                              var79 = 80;
                           } else {
                              var79 = 55;
                           }

                           var102 = ImageLoader.scaleAndSaveImage(var106, var38, var38, var79, var12);
                           var103 = getKeyForPhotoSize(var102, (Bitmap[])null, true);
                        } else {
                           var102 = null;
                           var103 = null;
                        }

                        TLRPC.TL_document var43 = new TLRPC.TL_document();
                        var43.file_reference = new byte[0];
                        if (var102 != null) {
                           var43.thumbs.add(var102);
                           var43.flags |= 1;
                        }

                        var43.mime_type = "video/mp4";
                        UserConfig.getInstance(var3).saveConfig(false);
                        if (var12) {
                           if (var30 >= 66) {
                              var99 = new TLRPC.TL_documentAttributeVideo();
                           } else {
                              var99 = new TLRPC.TL_documentAttributeVideo_layer65();
                           }
                        } else {
                           var99 = new TLRPC.TL_documentAttributeVideo();
                           ((TLRPC.DocumentAttribute)var99).supports_streaming = true;
                        }

                        var43.attributes.add(var99);
                        if (var66 != null && var66.needConvert()) {
                           if (var66.muted) {
                              fillVideoAttribute(var31.path, (TLRPC.TL_documentAttributeVideo)var99, var66);
                              var66.originalWidth = ((TLRPC.DocumentAttribute)var99).w;
                              var66.originalHeight = ((TLRPC.DocumentAttribute)var99).h;
                           } else {
                              ((TLRPC.DocumentAttribute)var99).duration = (int)(var66.estimatedDuration / 1000L);
                           }

                           var79 = var66.rotationValue;
                           if (var79 != 90 && var79 != 270) {
                              ((TLRPC.DocumentAttribute)var99).w = var66.resultWidth;
                              ((TLRPC.DocumentAttribute)var99).h = var66.resultHeight;
                           } else {
                              ((TLRPC.DocumentAttribute)var99).w = var66.resultHeight;
                              ((TLRPC.DocumentAttribute)var99).h = var66.resultWidth;
                           }

                           var43.size = (int)var66.estimatedSize;
                        } else {
                           if (var41.exists()) {
                              var43.size = (int)var41.length();
                           }

                           fillVideoAttribute(var31.path, (TLRPC.TL_documentAttributeVideo)var99, (VideoEditedInfo)null);
                        }

                        var99 = var103;
                        var105 = var43;
                     } else {
                        var106 = null;
                        var99 = var106;
                     }

                     if (var66 != null && var66.muted) {
                        var83 = var105.attributes.size();
                        var79 = 0;

                        while(true) {
                           if (var79 >= var83) {
                              var79 = 0;
                              break;
                           }

                           if (var105.attributes.get(var79) instanceof TLRPC.TL_documentAttributeAnimated) {
                              var79 = 1;
                              break;
                           }

                           ++var79;
                        }

                        if (var79 == 0) {
                           var105.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                        }
                     }

                     var119 = var40;
                     if (var66 != null) {
                        var119 = var40;
                        if (var66.needConvert()) {
                           StringBuilder var122 = new StringBuilder();
                           var122.append("-2147483648_");
                           var122.append(SharedConfig.getLastLocalId());
                           var122.append(".mp4");
                           var119 = var122.toString();
                           File var123 = new File(FileLoader.getDirectory(4), var119);
                           SharedConfig.saveConfig();
                           var119 = var123.getAbsolutePath();
                        }
                     }

                     var109 = new HashMap();
                     if (var98 != null) {
                        var109.put("originalPath", var98);
                     }

                     if (!var39 && var5) {
                        ++var13;
                        StringBuilder var114 = new StringBuilder();
                        var114.append("");
                        var114.append(var26);
                        var109.put("groupId", var114.toString());
                        if (var13 == 10 || var18 == var79 - 1) {
                           var109.put("final", "1");
                           var28 = 0L;
                        }
                     }

                     AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$Os_hFoakwia_R62_A2sPxAENsvo(var106, (String)var99, var6, var3, var66, var105, var119, var109, var34, var1, var7, var31));
                  }
               } else {
                  label687: {
                     var98 = var31.path;
                     if (var98 == null) {
                        Uri var71 = var31.uri;
                        if (var71 != null) {
                           var98 = AndroidUtilities.getPath(var71);
                           var70 = var31.uri.toString();
                           break label687;
                        }
                     }

                     var70 = var98;
                  }

                  boolean var84;
                  label682: {
                     label808: {
                        if (!var4 && !ImageLoader.shouldSendImageAsDocument(var31.path, var31.uri)) {
                           if (var98 == null || !var98.endsWith(var24) && !var98.endsWith(".webp")) {
                              if (var98 != null) {
                                 break label808;
                              }

                              Uri var111 = var31.uri;
                              if (var111 == null) {
                                 break label808;
                              }

                              if (MediaController.isGif(var111)) {
                                 var70 = var31.uri.toString();
                                 var98 = MediaController.copyFileToCache(var31.uri, "gif");
                                 var63 = "gif";
                              } else {
                                 if (!MediaController.isWebp(var31.uri)) {
                                    break label808;
                                 }

                                 var70 = var31.uri.toString();
                                 var98 = MediaController.copyFileToCache(var31.uri, "webp");
                                 var63 = "webp";
                              }
                           } else if (var98.endsWith(var24)) {
                              var63 = "gif";
                           } else {
                              var63 = "webp";
                           }
                        } else if (var98 != null) {
                           var63 = FileLoader.getFileExtension(new File(var98));
                        } else {
                           var63 = "";
                        }

                        var84 = true;
                        var34 = var98;
                        break label682;
                     }

                     var84 = false;
                     var34 = var98;
                  }

                  if (!var84) {
                     if (var34 != null) {
                        var116 = new File(var34);
                        var37 = new StringBuilder();
                        var37.append(var70);
                        var37.append(var116.length());
                        var37.append(var20);
                        var37.append(var116.lastModified());
                        var70 = var37.toString();
                     } else {
                        var70 = null;
                     }

                     TLRPC.TL_photo var108;
                     Object var118;
                     Object var126;
                     if (var58 != null) {
                        SendMessagesHelper.MediaSendPrepareWorker var125 = (SendMessagesHelper.MediaSendPrepareWorker)var58.get(var31);
                        var108 = var125.photo;
                        var103 = var125.parentObject;
                        var118 = var108;
                        if (var108 == null) {
                           try {
                              var125.sync.await();
                           } catch (Exception var44) {
                              FileLog.e((Throwable)var44);
                           }

                           var118 = var125.photo;
                           var103 = var125.parentObject;
                        }

                        var126 = var103;
                     } else {
                        Object var117;
                        if (!var12 && var31.ttl == 0) {
                           MessagesStorage var121 = MessagesStorage.getInstance(var3);
                           if (!var12) {
                              var25 = 0;
                           } else {
                              var25 = 3;
                           }

                           Object[] var120 = var121.getSentFile(var70, var25);
                           if (var120 != null) {
                              var118 = (TLRPC.TL_photo)var120[0];
                              var117 = (String)var120[1];
                           } else {
                              var117 = null;
                              var118 = var117;
                           }

                           label648: {
                              if (var118 == null && var31.uri != null) {
                                 MessagesStorage var110 = MessagesStorage.getInstance(var3);
                                 var119 = AndroidUtilities.getPath(var31.uri);
                                 if (!var12) {
                                    var25 = 0;
                                 } else {
                                    var25 = 3;
                                 }

                                 Object[] var127 = var110.getSentFile(var119, var25);
                                 if (var127 != null) {
                                    var99 = (TLRPC.TL_photo)var127[0];
                                    var117 = (String)var127[1];
                                    break label648;
                                 }
                              }

                              var99 = var118;
                           }

                           ensureMediaThumbExists(var3, var12, (TLObject)var99, var31.path, var31.uri, 0L);
                        } else {
                           var99 = null;
                           var117 = var99;
                        }

                        var118 = var99;
                        var126 = var117;
                        if (var99 == null) {
                           var108 = getInstance(var3).generatePhotoSizes(var31.path, var31.uri);
                           var118 = var108;
                           var126 = var117;
                           if (var12) {
                              var118 = var108;
                              var126 = var117;
                              if (var31.canDeleteAfter) {
                                 (new File(var31.path)).delete();
                                 var126 = var117;
                                 var118 = var108;
                              }
                           }
                        }
                     }

                     ArrayList var86;
                     if (var118 == null) {
                        var18 = var18;
                        var26 = var26;
                        ArrayList var67;
                        if (var61 == null) {
                           var61 = new ArrayList();
                           var62 = new ArrayList();
                           var86 = new ArrayList();
                           var67 = new ArrayList();
                        } else {
                           var86 = var78;
                           var67 = var62;
                           var62 = var65;
                        }

                        var61.add(var34);
                        var62.add(var70);
                        var86.add(var31.caption);
                        var67.add(var31.entities);
                        var82 = var62;
                        var78 = var86;
                        var62 = var67;
                        var58 = var58;
                        var20 = var20;
                        var79 = var79;
                        break label790;
                     }

                     var109 = new HashMap();
                     Bitmap[] var100 = new Bitmap[1];
                     String[] var85 = new String[1];
                     var86 = var31.masks;
                     if (var86 != null && !var86.isEmpty()) {
                        var39 = true;
                     } else {
                        var39 = false;
                     }

                     ((TLRPC.Photo)var118).has_stickers = var39;
                     if (var39) {
                        SerializedData var88 = new SerializedData(var31.masks.size() * 20 + 4);
                        var88.writeInt32(var31.masks.size());

                        for(var83 = 0; var83 < var31.masks.size(); ++var83) {
                           ((TLRPC.InputDocument)var31.masks.get(var83)).serializeToStream(var88);
                        }

                        var109.put("masks", Utilities.bytesToHex(var88.toByteArray()));
                        var88.cleanup();
                     }

                     if (var70 != null) {
                        var109.put("originalPath", var70);
                     }

                     label625: {
                        Exception var89;
                        label783: {
                           if (var5) {
                              try {
                                 var83 = var0.size();
                              } catch (Exception var54) {
                                 var89 = var54;
                                 break label783;
                              }

                              if (var83 != 1) {
                                 break label625;
                              }
                           }

                           TLRPC.PhotoSize var90;
                           try {
                              var90 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)var118).sizes, AndroidUtilities.getPhotoSize());
                           } catch (Exception var53) {
                              var89 = var53;
                              break label783;
                           }

                           if (var90 == null) {
                              break label625;
                           }

                           try {
                              var85[0] = getKeyForPhotoSize(var90, var100, false);
                              break label625;
                           } catch (Exception var52) {
                              var89 = var52;
                           }
                        }

                        FileLog.e((Throwable)var89);
                     }

                     if (var5) {
                        label608: {
                           var83 = var13 + 1;
                           StringBuilder var94 = new StringBuilder();
                           var94.append("");
                           var94.append(var26);
                           var109.put("groupId", var94.toString());
                           if (var83 != 10) {
                              var13 = var83;
                              if (var18 != var79 - 1) {
                                 break label608;
                              }
                           }

                           var109.put("final", "1");
                           var28 = 0L;
                           var13 = var83;
                        }
                     }

                     AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$M7CzdagvPTvoV__4PfcXK58t2Pc(var100, var85, var6, var3, (TLRPC.TL_photo)var118, var109, (String)var126, var1, var7, var31));
                     var79 = var79;
                     var20 = var20;
                     break label790;
                  }

                  ArrayList var112;
                  if (var61 == null) {
                     var61 = new ArrayList();
                     var62 = new ArrayList();
                     var82 = new ArrayList();
                     var78 = new ArrayList();
                  } else {
                     var112 = var78;
                     var78 = var62;
                     var62 = var65;
                     var82 = var112;
                  }

                  var61.add(var34);
                  var62.add(var70);
                  var82.add(var31.caption);
                  var78.add(var31.entities);
                  var65 = var82;
                  var112 = var78;
                  var82 = var62;
                  var78 = var65;
                  var62 = var112;
                  var18 = var18;
                  var26 = var26;
               }

               var79 = var79;
            }
         }

         ++var18;
         var32 = var26;
         var26 = var28;
         var65 = var82;
      }

      if (var26 != 0L) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$qXSAXwSZ5y7OeDOMylgcpcvrVQE(var3, var26));
      }

      if (var8 != null) {
         var8.releasePermission();
      }

      if (var61 != null && !var61.isEmpty()) {
         for(var13 = 0; var13 < var61.size(); ++var13) {
            prepareSendingDocumentInternal(var3, (String)var61.get(var13), (String)var65.get(var13), (Uri)null, (String)var63, var1, var7, (CharSequence)var78.get(var13), (ArrayList)var62.get(var13), var6, var4);
         }
      }

      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var55 = new StringBuilder();
         var55.append("total send time = ");
         var55.append(System.currentTimeMillis() - var9);
         FileLog.d(var55.toString());
      }

   }

   // $FF: synthetic method
   static void lambda$prepareSendingText$56(String var0, int var1, long var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$uXReb7FV6VvRZ_atPwFOd7p6_9A(var0, var1, var2));
   }

   // $FF: synthetic method
   static void lambda$prepareSendingVideo$65(VideoEditedInfo var0, String var1, long var2, long var4, int var6, int var7, int var8, int var9, long var10, CharSequence var12, MessageObject var13, MessageObject var14, ArrayList var15) {
      String var16 = var1;
      VideoEditedInfo var17;
      if (var0 != null) {
         var17 = var0;
      } else {
         var17 = createCompressionSettings(var1);
      }

      boolean var18;
      if ((int)var2 == 0) {
         var18 = true;
      } else {
         var18 = false;
      }

      boolean var19;
      if (var17 != null && var17.roundVideo) {
         var19 = true;
      } else {
         var19 = false;
      }

      if (var17 == null && !var1.endsWith("mp4") && !var19) {
         prepareSendingDocumentInternal(var7, var1, var1, (Uri)null, (String)null, var2, var14, var12, var15, var13, false);
      } else {
         File var20 = new File(var1);
         StringBuilder var34 = new StringBuilder();
         var34.append(var1);
         var34.append(var20.length());
         var34.append("_");
         var34.append(var20.lastModified());
         String var21 = var34.toString();
         long var25;
         String var35;
         if (var17 != null) {
            var35 = var21;
            if (!var19) {
               StringBuilder var22 = new StringBuilder();
               var22.append(var21);
               var22.append(var4);
               var22.append("_");
               var22.append(var17.startTime);
               var22.append("_");
               var22.append(var17.endTime);
               if (var17.muted) {
                  var35 = "_m";
               } else {
                  var35 = "";
               }

               var22.append(var35);
               var21 = var22.toString();
               var35 = var21;
               if (var17.resultWidth != var17.originalWidth) {
                  var34 = new StringBuilder();
                  var34.append(var21);
                  var34.append("_");
                  var34.append(var17.resultWidth);
                  var35 = var34.toString();
               }
            }

            long var23 = var17.startTime;
            var25 = 0L;
            var21 = var35;
            if (var23 >= 0L) {
               var25 = var23;
               var21 = var35;
            }
         } else {
            var25 = 0L;
         }

         byte var27;
         TLRPC.TL_document var39;
         String var47;
         label169: {
            if (!var18 && var6 == 0) {
               MessagesStorage var38 = MessagesStorage.getInstance(var7);
               if (!var18) {
                  var27 = 2;
               } else {
                  var27 = 5;
               }

               Object[] var46 = var38.getSentFile(var21, var27);
               if (var46 != null) {
                  var39 = (TLRPC.TL_document)var46[0];
                  var47 = (String)var46[1];
                  ensureMediaThumbExists(var7, var18, var39, var1, (Uri)null, var25);
                  break label169;
               }
            }

            var39 = null;
            var47 = null;
         }

         String var28 = var21;
         Object var36;
         Bitmap var48;
         if (var39 == null) {
            Bitmap var37 = createVideoThumbnail(var1, var25);
            Bitmap var40 = var37;
            if (var37 == null) {
               var40 = ThumbnailUtils.createVideoThumbnail(var16, 1);
            }

            short var52;
            if (!var18 && var6 == 0) {
               var52 = 320;
            } else {
               var52 = 90;
            }

            float var29 = (float)var52;
            if (var52 > 90) {
               var27 = 80;
            } else {
               var27 = 55;
            }

            TLRPC.PhotoSize var49;
            label194: {
               var49 = ImageLoader.scaleAndSaveImage(var40, var29, var29, var27, var18);
               if (var40 != null && var49 != null) {
                  if (var19) {
                     StringBuilder var33;
                     String var54;
                     if (var18) {
                        if (VERSION.SDK_INT < 21) {
                           var27 = 0;
                        } else {
                           var27 = 1;
                        }

                        int var30 = var40.getWidth();
                        int var31 = var40.getHeight();
                        int var32 = var40.getRowBytes();
                        Utilities.blurBitmap(var40, 7, var27, var30, var31, var32);
                        var33 = new StringBuilder();
                        var33.append(var49.location.volume_id);
                        var33.append("_");
                        var33.append(var49.location.local_id);
                        var33.append("@%d_%d_b2");
                        var54 = String.format(var33.toString(), (int)((float)AndroidUtilities.roundMessageSize / AndroidUtilities.density), (int)((float)AndroidUtilities.roundMessageSize / AndroidUtilities.density));
                        var37 = var40;
                        var35 = var54;
                     } else {
                        if (VERSION.SDK_INT < 21) {
                           var27 = 0;
                        } else {
                           var27 = 1;
                        }

                        Utilities.blurBitmap(var40, 3, var27, var40.getWidth(), var40.getHeight(), var40.getRowBytes());
                        var33 = new StringBuilder();
                        var33.append(var49.location.volume_id);
                        var33.append("_");
                        var33.append(var49.location.local_id);
                        var33.append("@%d_%d_b");
                        var54 = String.format(var33.toString(), (int)((float)AndroidUtilities.roundMessageSize / AndroidUtilities.density), (int)((float)AndroidUtilities.roundMessageSize / AndroidUtilities.density));
                        var37 = var40;
                        var35 = var54;
                     }
                     break label194;
                  }

                  var37 = null;
               } else {
                  var37 = var40;
               }

               var35 = null;
            }

            TLRPC.TL_document var55 = new TLRPC.TL_document();
            if (var49 != null) {
               var55.thumbs.add(var49);
               var55.flags |= 1;
            }

            var55.file_reference = new byte[0];
            var55.mime_type = "video/mp4";
            UserConfig.getInstance(var7).saveConfig(false);
            Object var51;
            int var53;
            if (var18) {
               var53 = (int)(var2 >> 32);
               TLRPC.EncryptedChat var50 = MessagesController.getInstance(var7).getEncryptedChat(var53);
               if (var50 == null) {
                  return;
               }

               if (AndroidUtilities.getPeerLayerVersion(var50.layer) >= 66) {
                  var51 = new TLRPC.TL_documentAttributeVideo();
               } else {
                  var51 = new TLRPC.TL_documentAttributeVideo_layer65();
               }
            } else {
               var51 = new TLRPC.TL_documentAttributeVideo();
               ((TLRPC.DocumentAttribute)var51).supports_streaming = true;
            }

            ((TLRPC.DocumentAttribute)var51).round_message = var19;
            var55.attributes.add(var51);
            if (var17 != null && var17.needConvert()) {
               if (var17.muted) {
                  var55.attributes.add(new TLRPC.TL_documentAttributeAnimated());
                  fillVideoAttribute(var16, (TLRPC.TL_documentAttributeVideo)var51, var17);
                  var17.originalWidth = ((TLRPC.DocumentAttribute)var51).w;
                  var17.originalHeight = ((TLRPC.DocumentAttribute)var51).h;
               } else {
                  ((TLRPC.DocumentAttribute)var51).duration = (int)(var4 / 1000L);
               }

               var53 = var17.rotationValue;
               if (var53 != 90 && var53 != 270) {
                  ((TLRPC.DocumentAttribute)var51).w = var9;
                  ((TLRPC.DocumentAttribute)var51).h = var8;
               } else {
                  ((TLRPC.DocumentAttribute)var51).w = var8;
                  ((TLRPC.DocumentAttribute)var51).h = var9;
               }

               var55.size = (int)var10;
            } else {
               if (var20.exists()) {
                  var55.size = (int)var20.length();
               }

               fillVideoAttribute(var16, (TLRPC.TL_documentAttributeVideo)var51, (VideoEditedInfo)null);
            }

            var48 = var37;
            var36 = var35;
            var39 = var55;
         } else {
            var48 = null;
            var36 = var48;
         }

         String var45 = var16;
         if (var17 != null) {
            var45 = var16;
            if (var17.needConvert()) {
               StringBuilder var42 = new StringBuilder();
               var42.append("-2147483648_");
               var42.append(SharedConfig.getLastLocalId());
               var42.append(".mp4");
               var16 = var42.toString();
               File var43 = new File(FileLoader.getDirectory(4), var16);
               SharedConfig.saveConfig();
               var45 = var43.getAbsolutePath();
            }
         }

         HashMap var44 = new HashMap();
         String var41;
         if (var12 != null) {
            var41 = var12.toString();
         } else {
            var41 = "";
         }

         if (var28 != null) {
            var44.put("originalPath", var28);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$bp8HVomUFdAeb6cvfbrX2jU2GRM(var48, (String)var36, var13, var7, var17, var39, var45, var44, var47, var2, var14, var41, var15, var6));
      }

   }

   private void performSendDelayedMessage(SendMessagesHelper.DelayedMessage var1) {
      this.performSendDelayedMessage(var1, -1);
   }

   private void performSendDelayedMessage(SendMessagesHelper.DelayedMessage var1, int var2) {
      int var3 = var1.type;
      boolean var4 = false;
      boolean var5 = true;
      String var6;
      String var7;
      if (var3 == 0) {
         var6 = var1.httpLocation;
         if (var6 != null) {
            this.putToDelayedMessages(var6, var1);
            ImageLoader.getInstance().loadHttpFile(var1.httpLocation, "file", this.currentAccount);
         } else if (var1.sendRequest != null) {
            var6 = FileLoader.getPathToAttach(var1.photoSize).toString();
            this.putToDelayedMessages(var6, var1);
            FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, true, 16777216);
         } else {
            var6 = FileLoader.getPathToAttach(var1.photoSize).toString();
            var7 = var6;
            if (var1.sendEncryptedRequest != null) {
               var7 = var6;
               if (var1.photoSize.location.dc_id != 0) {
                  File var14 = new File(var6);
                  File var8 = var14;
                  if (!var14.exists()) {
                     var6 = FileLoader.getPathToAttach(var1.photoSize, true).toString();
                     var8 = new File(var6);
                  }

                  var7 = var6;
                  if (!var8.exists()) {
                     this.putToDelayedMessages(FileLoader.getAttachFileName(var1.photoSize), var1);
                     FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForObject(var1.photoSize, var1.locationParent), var1.parentObject, "jpg", 2, 0);
                     return;
                  }
               }
            }

            this.putToDelayedMessages(var7, var1);
            FileLoader.getInstance(this.currentAccount).uploadFile(var7, true, true, 16777216);
         }
      } else {
         TLObject var15;
         TLRPC.InputMedia var16;
         TLRPC.Document var17;
         VideoEditedInfo var18;
         String var22;
         StringBuilder var32;
         if (var3 == 1) {
            VideoEditedInfo var13 = var1.videoEditedInfo;
            MessageObject var28;
            if (var13 != null && var13.needConvert()) {
               var28 = var1.obj;
               var22 = var28.messageOwner.attachPath;
               var17 = var28.getDocument();
               var6 = var22;
               if (var22 == null) {
                  var32 = new StringBuilder();
                  var32.append(FileLoader.getDirectory(4));
                  var32.append("/");
                  var32.append(var17.id);
                  var32.append(".mp4");
                  var6 = var32.toString();
               }

               this.putToDelayedMessages(var6, var1);
               MediaController.getInstance().scheduleVideoConvert(var1.obj);
            } else {
               var18 = var1.videoEditedInfo;
               if (var18 != null) {
                  if (var18.file != null) {
                     var15 = var1.sendRequest;
                     if (var15 instanceof TLRPC.TL_messages_sendMedia) {
                        var16 = ((TLRPC.TL_messages_sendMedia)var15).media;
                     } else if (var15 instanceof TLRPC.TL_messages_editMessage) {
                        var16 = ((TLRPC.TL_messages_editMessage)var15).media;
                     } else {
                        var16 = ((TLRPC.TL_messages_sendBroadcast)var15).media;
                     }

                     var18 = var1.videoEditedInfo;
                     var16.file = var18.file;
                     var18.file = null;
                  } else if (var18.encryptedFile != null) {
                     TLRPC.TL_decryptedMessage var33 = (TLRPC.TL_decryptedMessage)var1.sendEncryptedRequest;
                     TLRPC.DecryptedMessageMedia var19 = var33.media;
                     var19.size = (int)var18.estimatedSize;
                     var19.key = var18.key;
                     var19.iv = var18.iv;
                     SecretChatHelper var24 = SecretChatHelper.getInstance(this.currentAccount);
                     MessageObject var21 = var1.obj;
                     var24.performSendEncryptedRequest(var33, var21.messageOwner, var1.encryptedChat, var1.videoEditedInfo.encryptedFile, var1.originalPath, var21);
                     var1.videoEditedInfo.encryptedFile = null;
                     return;
                  }
               }

               var15 = var1.sendRequest;
               VideoEditedInfo var12;
               if (var15 != null) {
                  if (var15 instanceof TLRPC.TL_messages_sendMedia) {
                     var16 = ((TLRPC.TL_messages_sendMedia)var15).media;
                  } else if (var15 instanceof TLRPC.TL_messages_editMessage) {
                     var16 = ((TLRPC.TL_messages_editMessage)var15).media;
                  } else {
                     var16 = ((TLRPC.TL_messages_sendBroadcast)var15).media;
                  }

                  if (var16.file == null) {
                     var28 = var1.obj;
                     var22 = var28.messageOwner.attachPath;
                     var17 = var28.getDocument();
                     var6 = var22;
                     if (var22 == null) {
                        var32 = new StringBuilder();
                        var32.append(FileLoader.getDirectory(4));
                        var32.append("/");
                        var32.append(var17.id);
                        var32.append(".mp4");
                        var6 = var32.toString();
                     }

                     this.putToDelayedMessages(var6, var1);
                     var12 = var1.obj.videoEditedInfo;
                     if (var12 != null && var12.needConvert()) {
                        FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, false, var17.size, 33554432);
                     } else {
                        FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, false, 33554432);
                     }
                  } else {
                     var32 = new StringBuilder();
                     var32.append(FileLoader.getDirectory(4));
                     var32.append("/");
                     var32.append(var1.photoSize.location.volume_id);
                     var32.append("_");
                     var32.append(var1.photoSize.location.local_id);
                     var32.append(".jpg");
                     var6 = var32.toString();
                     this.putToDelayedMessages(var6, var1);
                     FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, true, 16777216);
                  }
               } else {
                  var28 = var1.obj;
                  var22 = var28.messageOwner.attachPath;
                  var17 = var28.getDocument();
                  var6 = var22;
                  if (var22 == null) {
                     var32 = new StringBuilder();
                     var32.append(FileLoader.getDirectory(4));
                     var32.append("/");
                     var32.append(var17.id);
                     var32.append(".mp4");
                     var6 = var32.toString();
                  }

                  if (var1.sendEncryptedRequest != null && var17.dc_id != 0 && !(new File(var6)).exists()) {
                     this.putToDelayedMessages(FileLoader.getAttachFileName(var17), var1);
                     FileLoader.getInstance(this.currentAccount).loadFile(var17, var1.parentObject, 2, 0);
                     return;
                  }

                  this.putToDelayedMessages(var6, var1);
                  var12 = var1.obj.videoEditedInfo;
                  if (var12 != null && var12.needConvert()) {
                     FileLoader.getInstance(this.currentAccount).uploadFile(var6, true, false, var17.size, 33554432);
                  } else {
                     FileLoader.getInstance(this.currentAccount).uploadFile(var6, true, false, 33554432);
                  }
               }
            }
         } else {
            MessageObject var27;
            FileLoader var34;
            if (var3 == 2) {
               var6 = var1.httpLocation;
               if (var6 != null) {
                  this.putToDelayedMessages(var6, var1);
                  ImageLoader.getInstance().loadHttpFile(var1.httpLocation, "gif", this.currentAccount);
               } else {
                  var15 = var1.sendRequest;
                  if (var15 != null) {
                     if (var15 instanceof TLRPC.TL_messages_sendMedia) {
                        var16 = ((TLRPC.TL_messages_sendMedia)var15).media;
                     } else if (var15 instanceof TLRPC.TL_messages_editMessage) {
                        var16 = ((TLRPC.TL_messages_editMessage)var15).media;
                     } else {
                        var16 = ((TLRPC.TL_messages_sendBroadcast)var15).media;
                     }

                     if (var16.file == null) {
                        var22 = var1.obj.messageOwner.attachPath;
                        this.putToDelayedMessages(var22, var1);
                        var34 = FileLoader.getInstance(this.currentAccount);
                        if (var1.sendRequest != null) {
                           var5 = false;
                        }

                        var34.uploadFile(var22, var5, false, 67108864);
                     } else if (var16.thumb == null && var1.photoSize != null) {
                        var32 = new StringBuilder();
                        var32.append(FileLoader.getDirectory(4));
                        var32.append("/");
                        var32.append(var1.photoSize.location.volume_id);
                        var32.append("_");
                        var32.append(var1.photoSize.location.local_id);
                        var32.append(".jpg");
                        var6 = var32.toString();
                        this.putToDelayedMessages(var6, var1);
                        FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, true, 16777216);
                     }
                  } else {
                     var27 = var1.obj;
                     var6 = var27.messageOwner.attachPath;
                     TLRPC.Document var30 = var27.getDocument();
                     if (var1.sendEncryptedRequest != null && var30.dc_id != 0 && !(new File(var6)).exists()) {
                        this.putToDelayedMessages(FileLoader.getAttachFileName(var30), var1);
                        FileLoader.getInstance(this.currentAccount).loadFile(var30, var1.parentObject, 2, 0);
                        return;
                     }

                     this.putToDelayedMessages(var6, var1);
                     FileLoader.getInstance(this.currentAccount).uploadFile(var6, true, false, 67108864);
                  }
               }
            } else if (var3 == 3) {
               var22 = var1.obj.messageOwner.attachPath;
               this.putToDelayedMessages(var22, var1);
               var34 = FileLoader.getInstance(this.currentAccount);
               var5 = var4;
               if (var1.sendRequest == null) {
                  var5 = true;
               }

               var34.uploadFile(var22, var5, true, 50331648);
            } else if (var3 == 4) {
               if (var2 < 0) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               if (var1.performMediaUpload) {
                  if (var2 < 0) {
                     var2 = var1.messageObjects.size() - 1;
                  }

                  var27 = (MessageObject)var1.messageObjects.get(var2);
                  if (var27.getDocument() != null) {
                     StringBuilder var23;
                     if (var1.videoEditedInfo != null) {
                        var7 = var27.messageOwner.attachPath;
                        TLRPC.Document var29 = var27.getDocument();
                        var6 = var7;
                        if (var7 == null) {
                           var32 = new StringBuilder();
                           var32.append(FileLoader.getDirectory(4));
                           var32.append("/");
                           var32.append(var29.id);
                           var32.append(".mp4");
                           var6 = var32.toString();
                        }

                        this.putToDelayedMessages(var6, var1);
                        var1.extraHashMap.put(var27, var6);
                        HashMap var31 = var1.extraHashMap;
                        var23 = new StringBuilder();
                        var23.append(var6);
                        var23.append("_i");
                        var31.put(var23.toString(), var27);
                        if (var1.photoSize != null) {
                           var31 = var1.extraHashMap;
                           var23 = new StringBuilder();
                           var23.append(var6);
                           var23.append("_t");
                           var31.put(var23.toString(), var1.photoSize);
                        }

                        MediaController.getInstance().scheduleVideoConvert(var27);
                     } else {
                        var17 = var27.getDocument();
                        var6 = var27.messageOwner.attachPath;
                        if (var6 == null) {
                           var32 = new StringBuilder();
                           var32.append(FileLoader.getDirectory(4));
                           var32.append("/");
                           var32.append(var17.id);
                           var32.append(".mp4");
                           var6 = var32.toString();
                        }

                        TLObject var9 = var1.sendRequest;
                        HashMap var10;
                        if (var9 != null) {
                           TLRPC.InputMedia var25 = ((TLRPC.TL_inputSingleMedia)((TLRPC.TL_messages_sendMultiMedia)var9).multi_media.get(var2)).media;
                           if (var25.file == null) {
                              this.putToDelayedMessages(var6, var1);
                              var1.extraHashMap.put(var27, var6);
                              var1.extraHashMap.put(var6, var25);
                              var10 = var1.extraHashMap;
                              var23 = new StringBuilder();
                              var23.append(var6);
                              var23.append("_i");
                              var10.put(var23.toString(), var27);
                              if (var1.photoSize != null) {
                                 var10 = var1.extraHashMap;
                                 var23 = new StringBuilder();
                                 var23.append(var6);
                                 var23.append("_t");
                                 var10.put(var23.toString(), var1.photoSize);
                              }

                              var18 = var27.videoEditedInfo;
                              if (var18 != null && var18.needConvert()) {
                                 FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, false, var17.size, 33554432);
                              } else {
                                 FileLoader.getInstance(this.currentAccount).uploadFile(var6, false, false, 33554432);
                              }
                           } else {
                              StringBuilder var26 = new StringBuilder();
                              var26.append(FileLoader.getDirectory(4));
                              var26.append("/");
                              var26.append(var1.photoSize.location.volume_id);
                              var26.append("_");
                              var26.append(var1.photoSize.location.local_id);
                              var26.append(".jpg");
                              var7 = var26.toString();
                              this.putToDelayedMessages(var7, var1);
                              var10 = var1.extraHashMap;
                              StringBuilder var11 = new StringBuilder();
                              var11.append(var7);
                              var11.append("_o");
                              var10.put(var11.toString(), var6);
                              var1.extraHashMap.put(var27, var7);
                              var1.extraHashMap.put(var7, var25);
                              FileLoader.getInstance(this.currentAccount).uploadFile(var7, false, true, 16777216);
                           }
                        } else {
                           TLRPC.TL_messages_sendEncryptedMultiMedia var20 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var1.sendEncryptedRequest;
                           this.putToDelayedMessages(var6, var1);
                           var1.extraHashMap.put(var27, var6);
                           var1.extraHashMap.put(var6, var20.files.get(var2));
                           var10 = var1.extraHashMap;
                           var23 = new StringBuilder();
                           var23.append(var6);
                           var23.append("_i");
                           var10.put(var23.toString(), var27);
                           if (var1.photoSize != null) {
                              var10 = var1.extraHashMap;
                              var23 = new StringBuilder();
                              var23.append(var6);
                              var23.append("_t");
                              var10.put(var23.toString(), var1.photoSize);
                           }

                           var18 = var27.videoEditedInfo;
                           if (var18 != null && var18.needConvert()) {
                              FileLoader.getInstance(this.currentAccount).uploadFile(var6, true, false, var17.size, 33554432);
                           } else {
                              FileLoader.getInstance(this.currentAccount).uploadFile(var6, true, false, 33554432);
                           }
                        }
                     }

                     var1.videoEditedInfo = null;
                     var1.photoSize = null;
                  } else {
                     var6 = var1.httpLocation;
                     if (var6 != null) {
                        this.putToDelayedMessages(var6, var1);
                        var1.extraHashMap.put(var27, var1.httpLocation);
                        var1.extraHashMap.put(var1.httpLocation, var27);
                        ImageLoader.getInstance().loadHttpFile(var1.httpLocation, "file", this.currentAccount);
                        var1.httpLocation = null;
                     } else {
                        var15 = var1.sendRequest;
                        Object var36;
                        if (var15 != null) {
                           var36 = ((TLRPC.TL_inputSingleMedia)((TLRPC.TL_messages_sendMultiMedia)var15).multi_media.get(var2)).media;
                        } else {
                           var36 = (TLObject)((TLRPC.TL_messages_sendEncryptedMultiMedia)var1.sendEncryptedRequest).files.get(var2);
                        }

                        var7 = FileLoader.getPathToAttach(var1.photoSize).toString();
                        this.putToDelayedMessages(var7, var1);
                        var1.extraHashMap.put(var7, var36);
                        var1.extraHashMap.put(var27, var7);
                        var34 = FileLoader.getInstance(this.currentAccount);
                        if (var1.sendEncryptedRequest != null) {
                           var4 = true;
                        } else {
                           var4 = false;
                        }

                        var34.uploadFile(var7, var4, true, 16777216);
                        var1.photoSize = null;
                     }
                  }

                  var1.performMediaUpload = false;
               } else if (!var1.messageObjects.isEmpty()) {
                  ArrayList var35 = var1.messageObjects;
                  this.putToSendingMessages(((MessageObject)var35.get(var35.size() - 1)).messageOwner);
               }

               this.sendReadyToSendGroup(var1, var5, true);
            }
         }
      }

   }

   private void performSendMessageRequest(TLObject var1, MessageObject var2, String var3, SendMessagesHelper.DelayedMessage var4, Object var5) {
      this.performSendMessageRequest(var1, var2, var3, (SendMessagesHelper.DelayedMessage)null, false, var4, var5);
   }

   public static void prepareSendingAudioDocuments(ArrayList var0, long var1, MessageObject var3, MessageObject var4) {
      (new Thread(new _$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw(var0, var1, UserConfig.selectedAccount, var4, var3))).start();
   }

   public static void prepareSendingBotContextResult(TLRPC.BotInlineResult var0, HashMap var1, long var2, MessageObject var4) {
      if (var0 != null) {
         int var5 = UserConfig.selectedAccount;
         TLRPC.BotInlineMessage var6 = var0.send_message;
         if (var6 instanceof TLRPC.TL_botInlineMessageMediaAuto) {
            (new Thread(new _$$Lambda$SendMessagesHelper$LmDH_h6B9Uggp2w_KTpB6c_fhcQ(var2, var0, var5, var1, var4))).run();
         } else {
            String var7;
            if (var6 instanceof TLRPC.TL_botInlineMessageText) {
               var7 = null;
               TLRPC.TL_webPagePending var11 = var7;
               if ((int)var2 == 0) {
                  int var8 = 0;

                  while(true) {
                     var11 = var7;
                     if (var8 >= var0.send_message.entities.size()) {
                        break;
                     }

                     TLRPC.MessageEntity var9 = (TLRPC.MessageEntity)var0.send_message.entities.get(var8);
                     if (var9 instanceof TLRPC.TL_messageEntityUrl) {
                        var11 = new TLRPC.TL_webPagePending();
                        var7 = var0.send_message.message;
                        var8 = var9.offset;
                        var11.url = var7.substring(var8, var9.length + var8);
                        break;
                     }

                     ++var8;
                  }
               }

               SendMessagesHelper var13 = getInstance(var5);
               TLRPC.BotInlineMessage var10 = var0.send_message;
               var13.sendMessage(var10.message, var2, var4, var11, var10.no_webpage ^ true, var10.entities, var10.reply_markup, var1);
            } else {
               TLRPC.BotInlineMessage var14;
               if (var6 instanceof TLRPC.TL_botInlineMessageMediaVenue) {
                  TLRPC.TL_messageMediaVenue var12 = new TLRPC.TL_messageMediaVenue();
                  var14 = var0.send_message;
                  var12.geo = var14.geo;
                  var12.address = var14.address;
                  var12.title = var14.title;
                  var12.provider = var14.provider;
                  var12.venue_id = var14.venue_id;
                  var7 = var14.venue_type;
                  var12.venue_id = var7;
                  var12.venue_type = var7;
                  if (var12.venue_type == null) {
                     var12.venue_type = "";
                  }

                  getInstance(var5).sendMessage((TLRPC.MessageMedia)var12, var2, var4, var0.send_message.reply_markup, var1);
               } else if (var6 instanceof TLRPC.TL_botInlineMessageMediaGeo) {
                  if (var6.period != 0) {
                     TLRPC.TL_messageMediaGeoLive var17 = new TLRPC.TL_messageMediaGeoLive();
                     var6 = var0.send_message;
                     var17.period = var6.period;
                     var17.geo = var6.geo;
                     getInstance(var5).sendMessage((TLRPC.MessageMedia)var17, var2, var4, var0.send_message.reply_markup, var1);
                  } else {
                     TLRPC.TL_messageMediaGeo var15 = new TLRPC.TL_messageMediaGeo();
                     var15.geo = var0.send_message.geo;
                     getInstance(var5).sendMessage((TLRPC.MessageMedia)var15, var2, var4, var0.send_message.reply_markup, var1);
                  }
               } else if (var6 instanceof TLRPC.TL_botInlineMessageMediaContact) {
                  TLRPC.TL_user var16 = new TLRPC.TL_user();
                  var14 = var0.send_message;
                  var16.phone = var14.phone_number;
                  var16.first_name = var14.first_name;
                  var16.last_name = var14.last_name;
                  var16.restriction_reason = var14.vcard;
                  getInstance(var5).sendMessage((TLRPC.User)var16, var2, var4, var0.send_message.reply_markup, var1);
               }
            }
         }

      }
   }

   public static void prepareSendingDocument(String var0, String var1, Uri var2, String var3, String var4, long var5, MessageObject var7, InputContentInfoCompat var8, MessageObject var9) {
      if (var0 != null && var1 != null || var2 != null) {
         ArrayList var10 = new ArrayList();
         ArrayList var11 = new ArrayList();
         ArrayList var12 = null;
         if (var2 != null) {
            var12 = new ArrayList();
            var12.add(var2);
         }

         if (var0 != null) {
            var10.add(var0);
            var11.add(var1);
         }

         prepareSendingDocuments(var10, var11, var12, var3, var4, var5, var7, var8, var9);
      }
   }

   private static boolean prepareSendingDocumentInternal(int param0, String param1, String param2, Uri param3, String param4, long param5, MessageObject param7, CharSequence param8, ArrayList param9, MessageObject param10, boolean param11) {
      // $FF: Couldn't be decompiled
   }

   public static void prepareSendingDocuments(ArrayList var0, ArrayList var1, ArrayList var2, String var3, String var4, long var5, MessageObject var7, InputContentInfoCompat var8, MessageObject var9) {
      if ((var0 != null || var1 != null || var2 != null) && (var0 == null || var1 == null || var0.size() == var1.size())) {
         (new Thread(new _$$Lambda$SendMessagesHelper$u_MDqklxXvAmqp9ZSz6JzWZDqR0(var0, UserConfig.selectedAccount, var1, var4, var5, var7, var3, var9, var2, var8))).start();
      }
   }

   public static void prepareSendingLocation(Location var0, long var1) {
      int var3 = UserConfig.selectedAccount;
      MessagesStorage.getInstance(var3).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE(var0, var3, var1));
   }

   public static void prepareSendingMedia(ArrayList var0, long var1, MessageObject var3, InputContentInfoCompat var4, boolean var5, boolean var6, MessageObject var7) {
      if (!var0.isEmpty()) {
         int var8 = UserConfig.selectedAccount;
         mediaSendQueue.postRunnable(new _$$Lambda$SendMessagesHelper$x39RupUe_lkQUIgui46eiCkD1zA(var0, var1, var8, var5, var6, var7, var3, var4));
      }
   }

   public static void prepareSendingPhoto(String var0, Uri var1, long var2, MessageObject var4, CharSequence var5, ArrayList var6, ArrayList var7, InputContentInfoCompat var8, int var9, MessageObject var10) {
      SendMessagesHelper.SendingMediaInfo var11 = new SendMessagesHelper.SendingMediaInfo();
      var11.path = var0;
      var11.uri = var1;
      if (var5 != null) {
         var11.caption = var5.toString();
      }

      var11.entities = var6;
      var11.ttl = var9;
      if (var7 != null && !var7.isEmpty()) {
         var11.masks = new ArrayList(var7);
      }

      ArrayList var12 = new ArrayList();
      var12.add(var11);
      prepareSendingMedia(var12, var2, var4, var8, false, false, var10);
   }

   public static void prepareSendingText(String var0, long var1) {
      int var3 = UserConfig.selectedAccount;
      MessagesStorage.getInstance(var3).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$mEb_8CvzR9IFpSPa5BhRMXY7oRk(var0, var3, var1));
   }

   public static void prepareSendingVideo(String var0, long var1, long var3, int var5, int var6, VideoEditedInfo var7, long var8, MessageObject var10, CharSequence var11, ArrayList var12, int var13, MessageObject var14) {
      if (var0 != null && var0.length() != 0) {
         (new Thread(new _$$Lambda$SendMessagesHelper$ZxDs0vXJa1NnG2Mq0H0o0iqTbfM(var7, var0, var8, var3, var13, UserConfig.selectedAccount, var6, var5, var1, var11, var14, var10, var12))).start();
      }

   }

   private void putToDelayedMessages(String var1, SendMessagesHelper.DelayedMessage var2) {
      ArrayList var3 = (ArrayList)this.delayedMessages.get(var1);
      ArrayList var4 = var3;
      if (var3 == null) {
         var4 = new ArrayList();
         this.delayedMessages.put(var1, var4);
      }

      var4.add(var2);
   }

   private void revertEditingMessageObject(MessageObject var1) {
      var1.cancelEditing = true;
      TLRPC.Message var2 = var1.messageOwner;
      var2.media = var1.previousMedia;
      var2.message = var1.previousCaption;
      var2.entities = var1.previousCaptionEntities;
      var2.attachPath = var1.previousAttachPath;
      var2.send_state = 0;
      var1.previousMedia = null;
      var1.previousCaption = null;
      var1.previousCaptionEntities = null;
      var1.previousAttachPath = null;
      var1.videoEditedInfo = null;
      var1.type = -1;
      var1.setType();
      var1.caption = null;
      var1.generateCaption();
      ArrayList var3 = new ArrayList();
      var3.add(var1.messageOwner);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var3, false, true, false, 0);
      var3 = new ArrayList();
      var3.add(var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var1.getDialogId(), var3);
   }

   private void sendLocation(Location var1) {
      TLRPC.TL_messageMediaGeo var2 = new TLRPC.TL_messageMediaGeo();
      var2.geo = new TLRPC.TL_geoPoint();
      var2.geo.lat = AndroidUtilities.fixLocationCoord(var1.getLatitude());
      var2.geo._long = AndroidUtilities.fixLocationCoord(var1.getLongitude());
      Iterator var3 = this.waitingForLocation.entrySet().iterator();

      while(var3.hasNext()) {
         MessageObject var4 = (MessageObject)((Entry)var3.next()).getValue();
         this.sendMessage((TLRPC.MessageMedia)var2, var4.getDialogId(), var4, (TLRPC.ReplyMarkup)null, (HashMap)null);
      }

   }

   private void sendMessage(String var1, String var2, TLRPC.MessageMedia var3, TLRPC.TL_photo var4, VideoEditedInfo var5, TLRPC.User var6, TLRPC.TL_document var7, TLRPC.TL_game var8, TLRPC.TL_messageMediaPoll var9, long var10, String var12, MessageObject var13, TLRPC.WebPage var14, boolean var15, MessageObject var16, ArrayList var17, TLRPC.ReplyMarkup var18, HashMap var19, int var20, Object var21) {
      String var22 = var1;
      TLRPC.MessageMedia var23 = var3;
      TLRPC.TL_photo var24 = var4;
      Object var25 = var14;
      HashMap var26 = var19;
      if (var6 == null || ((TLRPC.User)var6).phone != null) {
         long var27 = 0L;
         if (var10 != 0L) {
            String var29;
            if (var1 == null && var2 == null) {
               var29 = "";
            } else {
               var29 = var2;
            }

            String var746;
            if (var19 != null && var19.containsKey("originalPath")) {
               var746 = (String)var19.get("originalPath");
            } else {
               var746 = null;
            }

            byte var30 = -1;
            int var31 = (int)var10;
            int var32 = (int)(var10 >> 32);
            TLRPC.InputPeer var33;
            if (var31 != 0) {
               var33 = MessagesController.getInstance(this.currentAccount).getInputPeer(var31);
            } else {
               var33 = null;
            }

            TLRPC.EncryptedChat var35;
            boolean var36;
            label5479: {
               TLRPC.EncryptedChat var679;
               if (var31 == 0) {
                  var679 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var32);
                  if (var679 == null) {
                     if (var16 != null) {
                        MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(var16.messageOwner);
                        var16.messageOwner.send_state = 2;
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var16.getId());
                        this.processSentMessage(var16.getId());
                     }

                     return;
                  }
               } else {
                  if (var33 instanceof TLRPC.TL_inputPeerChannel) {
                     TLRPC.Chat var680 = MessagesController.getInstance(this.currentAccount).getChat(var33.channel_id);
                     boolean var34;
                     if (var680 != null && !var680.megagroup) {
                        var34 = true;
                     } else {
                        var34 = false;
                     }

                     var35 = null;
                     var36 = var34;
                     break label5479;
                  }

                  var679 = null;
               }

               var36 = false;
               var35 = var679;
            }

            Object var678;
            Object var681;
            Exception var786;
            label5467: {
               label5466: {
                  label5465: {
                     Exception var10000;
                     label5464: {
                        Object var682;
                        Exception var687;
                        label5769: {
                           boolean var38;
                           TLRPC.TL_photo var683;
                           String var696;
                           Object var699;
                           TLRPC.MessageMedia var701;
                           TLRPC.TL_messageMediaPoll var731;
                           Object var756;
                           Object var767;
                           int var782;
                           byte var784;
                           boolean var10001;
                           if (var16 != null) {
                              label5498: {
                                 TLRPC.Message var37;
                                 try {
                                    var37 = var16.messageOwner;
                                 } catch (Exception var453) {
                                    var10000 = var453;
                                    var10001 = false;
                                    break label5464;
                                 }

                                 var682 = var37;

                                 label5722: {
                                    try {
                                       if (var16.isForwarded()) {
                                          break label5722;
                                       }
                                    } catch (Exception var675) {
                                       var10000 = var675;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    var682 = var37;

                                    TLRPC.TL_messageMediaPoll var677;
                                    TLRPC.TL_document var684;
                                    label5452: {
                                       label5500: {
                                          label5501: {
                                             try {
                                                if (var16.type == 0) {
                                                   break label5501;
                                                }
                                             } catch (Exception var674) {
                                                var10000 = var674;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var37;

                                             label5502: {
                                                try {
                                                   if (var16.type == 4) {
                                                      break label5502;
                                                   }
                                                } catch (Exception var673) {
                                                   var10000 = var673;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var37;

                                                label5503: {
                                                   try {
                                                      if (var16.type == 1) {
                                                         break label5503;
                                                      }
                                                   } catch (Exception var672) {
                                                      var10000 = var672;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var37;

                                                   label5430: {
                                                      label5504: {
                                                         label5505: {
                                                            label5427: {
                                                               TLRPC.TL_document var676;
                                                               label5506: {
                                                                  label5507: {
                                                                     try {
                                                                        if (var16.type == 3) {
                                                                           break label5507;
                                                                        }
                                                                     } catch (Exception var671) {
                                                                        var10000 = var671;
                                                                        var10001 = false;
                                                                        break label5769;
                                                                     }

                                                                     var682 = var37;

                                                                     try {
                                                                        if (var16.type == 5) {
                                                                           break label5507;
                                                                        }
                                                                     } catch (Exception var670) {
                                                                        var10000 = var670;
                                                                        var10001 = false;
                                                                        break label5769;
                                                                     }

                                                                     if (var5 == null) {
                                                                        var682 = var37;

                                                                        try {
                                                                           if (var16.type == 12) {
                                                                              break label5505;
                                                                           }
                                                                        } catch (Exception var664) {
                                                                           var10000 = var664;
                                                                           var10001 = false;
                                                                           break label5769;
                                                                        }

                                                                        var682 = var37;

                                                                        label5509: {
                                                                           try {
                                                                              if (var16.type == 8) {
                                                                                 break label5509;
                                                                              }
                                                                           } catch (Exception var669) {
                                                                              var10000 = var669;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var682 = var37;

                                                                           try {
                                                                              if (var16.type == 9) {
                                                                                 break label5509;
                                                                              }
                                                                           } catch (Exception var668) {
                                                                              var10000 = var668;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var682 = var37;

                                                                           try {
                                                                              if (var16.type == 13) {
                                                                                 break label5509;
                                                                              }
                                                                           } catch (Exception var667) {
                                                                              var10000 = var667;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var682 = var37;

                                                                           try {
                                                                              if (var16.type == 14) {
                                                                                 break label5509;
                                                                              }
                                                                           } catch (Exception var666) {
                                                                              var10000 = var666;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var682 = var37;

                                                                           try {
                                                                              if (var16.type != 2) {
                                                                                 break label5427;
                                                                              }
                                                                           } catch (Exception var665) {
                                                                              var10000 = var665;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var682 = var37;

                                                                           try {
                                                                              var676 = (TLRPC.TL_document)var37.media.document;
                                                                           } catch (Exception var595) {
                                                                              var10000 = var595;
                                                                              var10001 = false;
                                                                              break label5769;
                                                                           }

                                                                           var784 = 8;
                                                                           break label5506;
                                                                        }

                                                                        var682 = var37;

                                                                        try {
                                                                           var676 = (TLRPC.TL_document)var37.media.document;
                                                                        } catch (Exception var594) {
                                                                           var10000 = var594;
                                                                           var10001 = false;
                                                                           break label5769;
                                                                        }

                                                                        var784 = 7;
                                                                        break label5506;
                                                                     }
                                                                  }

                                                                  var682 = var37;

                                                                  try {
                                                                     var676 = (TLRPC.TL_document)var37.media.document;
                                                                  } catch (Exception var593) {
                                                                     var10000 = var593;
                                                                     var10001 = false;
                                                                     break label5769;
                                                                  }

                                                                  var784 = 3;
                                                               }

                                                               var684 = var676;
                                                               var677 = var9;
                                                               break label5504;
                                                            }

                                                            var682 = var37;

                                                            try {
                                                               if (var16.type != 17) {
                                                                  break label5430;
                                                               }
                                                            } catch (Exception var663) {
                                                               var10000 = var663;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var37;

                                                            try {
                                                               var677 = (TLRPC.TL_messageMediaPoll)var37.media;
                                                            } catch (Exception var596) {
                                                               var10000 = var596;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var684 = var7;
                                                            var784 = 10;
                                                            break label5504;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            var678 = new TLRPC.TL_userRequest_old2;
                                                         } catch (Exception var603) {
                                                            var10000 = var603;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            var678.<init>();
                                                         } catch (Exception var602) {
                                                            var10000 = var602;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            ((TLRPC.User)var678).phone = var37.media.phone_number;
                                                         } catch (Exception var601) {
                                                            var10000 = var601;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            ((TLRPC.User)var678).first_name = var37.media.first_name;
                                                         } catch (Exception var600) {
                                                            var10000 = var600;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            ((TLRPC.User)var678).last_name = var37.media.last_name;
                                                         } catch (Exception var599) {
                                                            var10000 = var599;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            ((TLRPC.User)var678).restriction_reason = var37.media.vcard;
                                                         } catch (Exception var598) {
                                                            var10000 = var598;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var37;

                                                         try {
                                                            ((TLRPC.User)var678).id = var37.media.user_id;
                                                         } catch (Exception var597) {
                                                            var10000 = var597;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var784 = 6;
                                                         break label5500;
                                                      }

                                                      var681 = var6;
                                                      break label5452;
                                                   }

                                                   var678 = var6;
                                                   var784 = var30;
                                                   break label5500;
                                                }

                                                var682 = var37;

                                                try {
                                                   var24 = (TLRPC.TL_photo)var37.media.photo;
                                                } catch (Exception var604) {
                                                   var10000 = var604;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var678 = var6;
                                                var784 = 2;
                                                break label5500;
                                             }

                                             var682 = var37;

                                             try {
                                                var23 = var37.media;
                                             } catch (Exception var605) {
                                                var10000 = var605;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var678 = var6;
                                             var784 = 1;
                                             break label5500;
                                          }

                                          var682 = var37;

                                          label5510: {
                                             try {
                                                if (var16.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                   break label5510;
                                                }
                                             } catch (Exception var662) {
                                                var10000 = var662;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var37;

                                             try {
                                                var22 = var37.message;
                                             } catch (Exception var606) {
                                                var10000 = var606;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }

                                          var678 = var6;
                                          var784 = 0;
                                       }

                                       var684 = var7;
                                       var681 = var678;
                                       var677 = var9;
                                    }

                                    if (var19 != null) {
                                       label5691: {
                                          var682 = var37;

                                          try {
                                             if (!var26.containsKey("query_id")) {
                                                break label5691;
                                             }
                                          } catch (Exception var661) {
                                             var10000 = var661;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var784 = 9;
                                       }
                                    }

                                    var682 = var37;

                                    label5512: {
                                       try {
                                          if (var37.media.ttl_seconds <= 0) {
                                             break label5512;
                                          }
                                       } catch (Exception var660) {
                                          var10000 = var660;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var37;

                                       try {
                                          var20 = var37.media.ttl_seconds;
                                       } catch (Exception var592) {
                                          var10000 = var592;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    }

                                    var756 = var14;
                                    var6 = var681;
                                    var683 = var24;
                                    var731 = var677;
                                    var701 = var23;
                                    var696 = var22;
                                    var678 = var37;
                                    var7 = var684;
                                    break label5498;
                                 }

                                 var731 = var9;
                                 var701 = var3;
                                 var683 = var4;
                                 var784 = 4;
                                 var696 = var1;
                                 var678 = var37;
                                 var756 = var14;
                              }
                           } else {
                              label5354: {
                                 label5724: {
                                    if (var1 != null) {
                                       if (var35 != null) {
                                          try {
                                             var14 = new TLRPC.TL_message_secret();
                                          } catch (Exception var445) {
                                             var10000 = var445;
                                             var10001 = false;
                                             break label5464;
                                          }
                                       } else {
                                          try {
                                             var14 = new TLRPC.TL_message();
                                          } catch (Exception var444) {
                                             var10000 = var444;
                                             var10001 = false;
                                             break label5464;
                                          }
                                       }

                                       var699 = var25;
                                       if (var35 != null) {
                                          label5692: {
                                             var682 = var14;
                                             var699 = var25;

                                             try {
                                                if (!(var25 instanceof TLRPC.TL_webPagePending)) {
                                                   break label5692;
                                                }
                                             } catch (Exception var641) {
                                                var10000 = var641;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var14;

                                             label5187: {
                                                try {
                                                   if (((TLRPC.WebPage)var25).url != null) {
                                                      break label5187;
                                                   }
                                                } catch (Exception var642) {
                                                   var10000 = var642;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var699 = null;
                                                break label5692;
                                             }

                                             var682 = var14;

                                             try {
                                                var699 = new TLRPC.TL_webPageUrlPending;
                                             } catch (Exception var569) {
                                                var10000 = var569;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var14;

                                             try {
                                                var699.<init>();
                                             } catch (Exception var568) {
                                                var10000 = var568;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var14;

                                             try {
                                                ((TLRPC.WebPage)var699).url = ((TLRPC.WebPage)var25).url;
                                             } catch (Exception var567) {
                                                var10000 = var567;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }
                                       }

                                       if (var699 == null) {
                                          var682 = var14;

                                          TLRPC.TL_messageMediaEmpty var770;
                                          try {
                                             var770 = new TLRPC.TL_messageMediaEmpty;
                                          } catch (Exception var566) {
                                             var10000 = var566;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var14;

                                          try {
                                             var770.<init>();
                                          } catch (Exception var565) {
                                             var10000 = var565;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var14;

                                          try {
                                             ((TLRPC.Message)var14).media = var770;
                                          } catch (Exception var564) {
                                             var10000 = var564;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       } else {
                                          var682 = var14;

                                          TLRPC.TL_messageMediaWebPage var772;
                                          try {
                                             var772 = new TLRPC.TL_messageMediaWebPage;
                                          } catch (Exception var563) {
                                             var10000 = var563;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var14;

                                          try {
                                             var772.<init>();
                                          } catch (Exception var562) {
                                             var10000 = var562;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var14;

                                          try {
                                             ((TLRPC.Message)var14).media = var772;
                                          } catch (Exception var561) {
                                             var10000 = var561;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var14;

                                          try {
                                             ((TLRPC.Message)var14).media.webpage = (TLRPC.WebPage)var699;
                                          } catch (Exception var560) {
                                             var10000 = var560;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }

                                       label5173: {
                                          label5172: {
                                             if (var19 != null) {
                                                var682 = var14;

                                                try {
                                                   if (var26.containsKey("query_id")) {
                                                      break label5172;
                                                   }
                                                } catch (Exception var640) {
                                                   var10000 = var640;
                                                   var10001 = false;
                                                   break label5769;
                                                }
                                             }

                                             var784 = 0;
                                             break label5173;
                                          }

                                          var784 = 9;
                                       }

                                       var682 = var14;

                                       try {
                                          ((TLRPC.Message)var14).message = var22;
                                       } catch (Exception var559) {
                                          var10000 = var559;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var14;
                                       var767 = var699;
                                    } else {
                                       label5726: {
                                          if (var9 != null) {
                                             if (var35 != null) {
                                                try {
                                                   var699 = new TLRPC.TL_message_secret();
                                                } catch (Exception var452) {
                                                   var10000 = var452;
                                                   var10001 = false;
                                                   break label5464;
                                                }
                                             } else {
                                                try {
                                                   var699 = new TLRPC.TL_message();
                                                } catch (Exception var451) {
                                                   var10000 = var451;
                                                   var10001 = false;
                                                   break label5464;
                                                }
                                             }

                                             var682 = var699;

                                             try {
                                                ((TLRPC.Message)var699).media = var9;
                                             } catch (Exception var591) {
                                                var10000 = var591;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var784 = 10;
                                             var767 = var14;
                                             break label5354;
                                          }

                                          if (var3 != null) {
                                             label5522: {
                                                if (var35 != null) {
                                                   try {
                                                      var699 = new TLRPC.TL_message_secret();
                                                   } catch (Exception var450) {
                                                      var10000 = var450;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                } else {
                                                   try {
                                                      var699 = new TLRPC.TL_message();
                                                   } catch (Exception var449) {
                                                      var10000 = var449;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media = var23;
                                                } catch (Exception var590) {
                                                   var10000 = var590;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                if (var19 != null) {
                                                   var682 = var699;

                                                   try {
                                                      var38 = var26.containsKey("query_id");
                                                   } catch (Exception var589) {
                                                      var10000 = var589;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   if (var38) {
                                                      break label5522;
                                                   }
                                                }

                                                var784 = 1;
                                                var767 = var14;
                                                break label5354;
                                             }
                                          } else {
                                             if (var4 != null) {
                                                if (var35 != null) {
                                                   try {
                                                      var699 = new TLRPC.TL_message_secret();
                                                   } catch (Exception var443) {
                                                      var10000 = var443;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                } else {
                                                   try {
                                                      var699 = new TLRPC.TL_message();
                                                   } catch (Exception var442) {
                                                      var10000 = var442;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                }

                                                var682 = var699;

                                                TLRPC.TL_messageMediaPhoto var741;
                                                try {
                                                   var741 = new TLRPC.TL_messageMediaPhoto;
                                                } catch (Exception var558) {
                                                   var10000 = var558;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   var741.<init>();
                                                } catch (Exception var557) {
                                                   var10000 = var557;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media = var741;
                                                } catch (Exception var556) {
                                                   var10000 = var556;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                TLRPC.MessageMedia var742;
                                                try {
                                                   var742 = ((TLRPC.Message)var699).media;
                                                } catch (Exception var555) {
                                                   var10000 = var555;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   var742.flags |= 3;
                                                } catch (Exception var554) {
                                                   var10000 = var554;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                if (var17 != null) {
                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).entities = var17;
                                                   } catch (Exception var553) {
                                                      var10000 = var553;
                                                      var10001 = false;
                                                      break label5769;
                                                   }
                                                }

                                                if (var20 != 0) {
                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).media.ttl_seconds = var20;
                                                   } catch (Exception var552) {
                                                      var10000 = var552;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).ttl = var20;
                                                   } catch (Exception var551) {
                                                      var10000 = var551;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      var742 = ((TLRPC.Message)var699).media;
                                                   } catch (Exception var550) {
                                                      var10000 = var550;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      var742.flags |= 4;
                                                   } catch (Exception var549) {
                                                      var10000 = var549;
                                                      var10001 = false;
                                                      break label5769;
                                                   }
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media.photo = var24;
                                                } catch (Exception var548) {
                                                   var10000 = var548;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                label5212: {
                                                   label5211: {
                                                      if (var19 != null) {
                                                         var682 = var699;

                                                         try {
                                                            if (var26.containsKey("query_id")) {
                                                               break label5211;
                                                            }
                                                         } catch (Exception var645) {
                                                            var10000 = var645;
                                                            var10001 = false;
                                                            break label5769;
                                                         }
                                                      }

                                                      var784 = 2;
                                                      break label5212;
                                                   }

                                                   var784 = 9;
                                                }

                                                if (var12 != null) {
                                                   label5695: {
                                                      var682 = var699;

                                                      try {
                                                         if (var12.length() <= 0) {
                                                            break label5695;
                                                         }
                                                      } catch (Exception var644) {
                                                         var10000 = var644;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var699;

                                                      try {
                                                         if (!var12.startsWith("http")) {
                                                            break label5695;
                                                         }
                                                      } catch (Exception var643) {
                                                         var10000 = var643;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var699;

                                                      try {
                                                         ((TLRPC.Message)var699).attachPath = var12;
                                                      } catch (Exception var547) {
                                                         var10000 = var547;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var699;
                                                      var767 = var14;
                                                      break label5726;
                                                   }
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).attachPath = FileLoader.getPathToAttach(((TLRPC.PhotoSize)var24.sizes.get(var24.sizes.size() - 1)).location, true).toString();
                                                } catch (Exception var546) {
                                                   var10000 = var546;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;
                                                var767 = var14;
                                                break label5726;
                                             }

                                             if (var8 != null) {
                                                try {
                                                   var14 = new TLRPC.TL_message();
                                                } catch (Exception var448) {
                                                   var10000 = var448;
                                                   var10001 = false;
                                                   break label5464;
                                                }

                                                var682 = var14;

                                                TLRPC.TL_messageMediaGame var758;
                                                try {
                                                   var758 = new TLRPC.TL_messageMediaGame;
                                                } catch (Exception var588) {
                                                   var10000 = var588;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var14;

                                                try {
                                                   var758.<init>();
                                                } catch (Exception var587) {
                                                   var10000 = var587;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var14;

                                                try {
                                                   ((TLRPC.Message)var14).media = var758;
                                                } catch (Exception var586) {
                                                   var10000 = var586;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var14;

                                                try {
                                                   ((TLRPC.Message)var14).media.game = var8;
                                                } catch (Exception var585) {
                                                   var10000 = var585;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var14;
                                                var767 = var25;
                                                var784 = var30;
                                                if (var19 == null) {
                                                   break label5726;
                                                }

                                                var682 = var14;

                                                try {
                                                   var38 = var26.containsKey("query_id");
                                                } catch (Exception var584) {
                                                   var10000 = var584;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var14;
                                                var767 = var25;
                                                var784 = var30;
                                                if (!var38) {
                                                   break label5726;
                                                }

                                                var699 = var14;
                                             } else {
                                                if (var6 == null) {
                                                   TLRPC.TL_document var707 = var7;
                                                   if (var7 != null) {
                                                      if (var35 != null) {
                                                         try {
                                                            var14 = new TLRPC.TL_message_secret();
                                                         } catch (Exception var441) {
                                                            var10000 = var441;
                                                            var10001 = false;
                                                            break label5464;
                                                         }
                                                      } else {
                                                         try {
                                                            var14 = new TLRPC.TL_message();
                                                         } catch (Exception var440) {
                                                            var10000 = var440;
                                                            var10001 = false;
                                                            break label5464;
                                                         }
                                                      }

                                                      var682 = var14;

                                                      TLRPC.TL_messageMediaDocument var760;
                                                      try {
                                                         var760 = new TLRPC.TL_messageMediaDocument;
                                                      } catch (Exception var545) {
                                                         var10000 = var545;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var14;

                                                      try {
                                                         var760.<init>();
                                                      } catch (Exception var544) {
                                                         var10000 = var544;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var14;

                                                      try {
                                                         ((TLRPC.Message)var14).media = var760;
                                                      } catch (Exception var543) {
                                                         var10000 = var543;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var14;

                                                      TLRPC.MessageMedia var764;
                                                      try {
                                                         var764 = ((TLRPC.Message)var14).media;
                                                      } catch (Exception var542) {
                                                         var10000 = var542;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var14;

                                                      try {
                                                         var764.flags |= 3;
                                                      } catch (Exception var541) {
                                                         var10000 = var541;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      if (var20 != 0) {
                                                         var682 = var14;

                                                         try {
                                                            ((TLRPC.Message)var14).media.ttl_seconds = var20;
                                                         } catch (Exception var540) {
                                                            var10000 = var540;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            ((TLRPC.Message)var14).ttl = var20;
                                                         } catch (Exception var539) {
                                                            var10000 = var539;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var764 = ((TLRPC.Message)var14).media;
                                                         } catch (Exception var538) {
                                                            var10000 = var538;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var764.flags |= 4;
                                                         } catch (Exception var537) {
                                                            var10000 = var537;
                                                            var10001 = false;
                                                            break label5769;
                                                         }
                                                      }

                                                      var682 = var14;

                                                      try {
                                                         ((TLRPC.Message)var14).media.document = var707;
                                                      } catch (Exception var536) {
                                                         var10000 = var536;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      byte var779;
                                                      label5290: {
                                                         label5535: {
                                                            if (var19 != null) {
                                                               var682 = var14;

                                                               try {
                                                                  if (var26.containsKey("query_id")) {
                                                                     break label5535;
                                                                  }
                                                               } catch (Exception var655) {
                                                                  var10000 = var655;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }
                                                            }

                                                            var682 = var14;

                                                            label5536: {
                                                               try {
                                                                  if (MessageObject.isVideoDocument(var7)) {
                                                                     break label5536;
                                                                  }
                                                               } catch (Exception var654) {
                                                                  var10000 = var654;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }

                                                               var682 = var14;

                                                               try {
                                                                  if (MessageObject.isRoundVideoDocument(var7)) {
                                                                     break label5536;
                                                                  }
                                                               } catch (Exception var653) {
                                                                  var10000 = var653;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }

                                                               if (var5 == null) {
                                                                  var682 = var14;

                                                                  label5267: {
                                                                     try {
                                                                        if (MessageObject.isVoiceDocument(var7)) {
                                                                           break label5267;
                                                                        }
                                                                     } catch (Exception var652) {
                                                                        var10000 = var652;
                                                                        var10001 = false;
                                                                        break label5769;
                                                                     }

                                                                     var779 = 7;
                                                                     break label5290;
                                                                  }

                                                                  var779 = 8;
                                                                  break label5290;
                                                               }
                                                            }

                                                            var779 = 3;
                                                            break label5290;
                                                         }

                                                         var779 = 9;
                                                      }

                                                      HashMap var768 = var19;
                                                      if (var5 != null) {
                                                         var682 = var14;

                                                         String var775;
                                                         try {
                                                            var775 = ((VideoEditedInfo)var5).getString();
                                                         } catch (Exception var535) {
                                                            var10000 = var535;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var768 = var19;
                                                         if (var19 == null) {
                                                            var682 = var14;

                                                            try {
                                                               var768 = new HashMap;
                                                            } catch (Exception var534) {
                                                               var10000 = var534;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var768.<init>();
                                                            } catch (Exception var533) {
                                                               var10000 = var533;
                                                               var10001 = false;
                                                               break label5769;
                                                            }
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var768.put("ve", var775);
                                                         } catch (Exception var532) {
                                                            var10000 = var532;
                                                            var10001 = false;
                                                            break label5769;
                                                         }
                                                      }

                                                      label5728: {
                                                         if (var35 != null) {
                                                            label5696: {
                                                               var682 = var14;

                                                               try {
                                                                  if (var707.dc_id <= 0) {
                                                                     break label5696;
                                                                  }
                                                               } catch (Exception var651) {
                                                                  var10000 = var651;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }

                                                               var682 = var14;

                                                               try {
                                                                  if (MessageObject.isStickerDocument(var7)) {
                                                                     break label5696;
                                                                  }
                                                               } catch (Exception var650) {
                                                                  var10000 = var650;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }

                                                               var682 = var14;

                                                               try {
                                                                  ((TLRPC.Message)var14).attachPath = FileLoader.getPathToAttach(var7).toString();
                                                                  break label5728;
                                                               } catch (Exception var531) {
                                                                  var10000 = var531;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }
                                                            }
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            ((TLRPC.Message)var14).attachPath = var12;
                                                         } catch (Exception var530) {
                                                            var10000 = var530;
                                                            var10001 = false;
                                                            break label5769;
                                                         }
                                                      }

                                                      var699 = var14;
                                                      var26 = var768;
                                                      var767 = var25;
                                                      var784 = var779;
                                                      if (var35 != null) {
                                                         var682 = var14;
                                                         var699 = var14;
                                                         var26 = var768;
                                                         var767 = var25;
                                                         var784 = var779;

                                                         try {
                                                            if (!MessageObject.isStickerDocument(var7)) {
                                                               break label5354;
                                                            }
                                                         } catch (Exception var639) {
                                                            var10000 = var639;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         int var39 = 0;

                                                         TLRPC.DocumentAttribute var716;
                                                         TLRPC.TL_document var762;
                                                         while(true) {
                                                            var762 = var7;
                                                            var682 = var14;
                                                            var699 = var14;
                                                            var26 = var768;
                                                            var767 = var25;
                                                            var784 = var779;

                                                            try {
                                                               if (var39 >= var762.attributes.size()) {
                                                                  break label5354;
                                                               }
                                                            } catch (Exception var638) {
                                                               var10000 = var638;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var716 = (TLRPC.DocumentAttribute)var762.attributes.get(var39);
                                                            } catch (Exception var529) {
                                                               var10000 = var529;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               if (var716 instanceof TLRPC.TL_documentAttributeSticker) {
                                                                  break;
                                                               }
                                                            } catch (Exception var649) {
                                                               var10000 = var649;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            ++var39;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var762.attributes.remove(var39);
                                                         } catch (Exception var528) {
                                                            var10000 = var528;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         TLRPC.TL_documentAttributeSticker_layer55 var776;
                                                         try {
                                                            var776 = new TLRPC.TL_documentAttributeSticker_layer55;
                                                         } catch (Exception var527) {
                                                            var10000 = var527;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var776.<init>();
                                                         } catch (Exception var526) {
                                                            var10000 = var526;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var762.attributes.add(var776);
                                                         } catch (Exception var525) {
                                                            var10000 = var525;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var776.alt = var716.alt;
                                                         } catch (Exception var524) {
                                                            var10000 = var524;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         TLRPC.TL_inputStickerSetEmpty var721;
                                                         label5543: {
                                                            try {
                                                               if (var716.stickerset != null) {
                                                                  break label5543;
                                                               }
                                                            } catch (Exception var648) {
                                                               var10000 = var648;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var721 = new TLRPC.TL_inputStickerSetEmpty;
                                                            } catch (Exception var514) {
                                                               var10000 = var514;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var721.<init>();
                                                            } catch (Exception var513) {
                                                               var10000 = var513;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var776.stickerset = var721;
                                                            } catch (Exception var512) {
                                                               var10000 = var512;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var699 = var14;
                                                            var26 = var768;
                                                            var767 = var25;
                                                            var784 = var779;
                                                            break label5354;
                                                         }

                                                         var682 = var14;

                                                         label5544: {
                                                            label5545: {
                                                               try {
                                                                  if (var716.stickerset instanceof TLRPC.TL_inputStickerSetShortName) {
                                                                     break label5545;
                                                                  }
                                                               } catch (Exception var647) {
                                                                  var10000 = var647;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }

                                                               var682 = var14;

                                                               try {
                                                                  var696 = DataQuery.getInstance(this.currentAccount).getStickerSetName(var716.stickerset.id);
                                                                  break label5544;
                                                               } catch (Exception var522) {
                                                                  var10000 = var522;
                                                                  var10001 = false;
                                                                  break label5769;
                                                               }
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var696 = var716.stickerset.short_name;
                                                            } catch (Exception var523) {
                                                               var10000 = var523;
                                                               var10001 = false;
                                                               break label5769;
                                                            }
                                                         }

                                                         var682 = var14;

                                                         label5546: {
                                                            try {
                                                               if (TextUtils.isEmpty(var696)) {
                                                                  break label5546;
                                                               }
                                                            } catch (Exception var646) {
                                                               var10000 = var646;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            TLRPC.TL_inputStickerSetShortName var777;
                                                            try {
                                                               var777 = new TLRPC.TL_inputStickerSetShortName;
                                                            } catch (Exception var521) {
                                                               var10000 = var521;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var777.<init>();
                                                            } catch (Exception var520) {
                                                               var10000 = var520;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var776.stickerset = var777;
                                                            } catch (Exception var519) {
                                                               var10000 = var519;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var682 = var14;

                                                            try {
                                                               var776.stickerset.short_name = var696;
                                                            } catch (Exception var518) {
                                                               var10000 = var518;
                                                               var10001 = false;
                                                               break label5769;
                                                            }

                                                            var699 = var14;
                                                            var26 = var768;
                                                            var767 = var25;
                                                            var784 = var779;
                                                            break label5354;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var721 = new TLRPC.TL_inputStickerSetEmpty;
                                                         } catch (Exception var517) {
                                                            var10000 = var517;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var721.<init>();
                                                         } catch (Exception var516) {
                                                            var10000 = var516;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var682 = var14;

                                                         try {
                                                            var776.stickerset = var721;
                                                         } catch (Exception var515) {
                                                            var10000 = var515;
                                                            var10001 = false;
                                                            break label5769;
                                                         }

                                                         var699 = var14;
                                                         var26 = var768;
                                                         var767 = var25;
                                                         var784 = var779;
                                                      }
                                                   } else {
                                                      var699 = null;
                                                      var784 = var30;
                                                      var767 = var14;
                                                   }
                                                   break label5354;
                                                }

                                                if (var35 != null) {
                                                   try {
                                                      var699 = new TLRPC.TL_message_secret();
                                                   } catch (Exception var447) {
                                                      var10000 = var447;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                } else {
                                                   try {
                                                      var699 = new TLRPC.TL_message();
                                                   } catch (Exception var446) {
                                                      var10000 = var446;
                                                      var10001 = false;
                                                      break label5464;
                                                   }
                                                }

                                                var682 = var699;

                                                TLRPC.TL_messageMediaContact var738;
                                                try {
                                                   var738 = new TLRPC.TL_messageMediaContact;
                                                } catch (Exception var583) {
                                                   var10000 = var583;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   var738.<init>();
                                                } catch (Exception var582) {
                                                   var10000 = var582;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media = var738;
                                                } catch (Exception var581) {
                                                   var10000 = var581;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media.phone_number = ((TLRPC.User)var6).phone;
                                                } catch (Exception var580) {
                                                   var10000 = var580;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media.first_name = ((TLRPC.User)var6).first_name;
                                                } catch (Exception var579) {
                                                   var10000 = var579;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media.last_name = ((TLRPC.User)var6).last_name;
                                                } catch (Exception var578) {
                                                   var10000 = var578;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                try {
                                                   ((TLRPC.Message)var699).media.user_id = ((TLRPC.User)var6).id;
                                                } catch (Exception var577) {
                                                   var10000 = var577;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var699;

                                                label5529: {
                                                   label5530: {
                                                      try {
                                                         if (((TLRPC.User)var6).restriction_reason == null) {
                                                            break label5530;
                                                         }
                                                      } catch (Exception var659) {
                                                         var10000 = var659;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var699;

                                                      try {
                                                         if (!((TLRPC.User)var6).restriction_reason.startsWith("BEGIN:VCARD")) {
                                                            break label5530;
                                                         }
                                                      } catch (Exception var658) {
                                                         var10000 = var658;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var699;

                                                      try {
                                                         ((TLRPC.Message)var699).media.vcard = ((TLRPC.User)var6).restriction_reason;
                                                         break label5529;
                                                      } catch (Exception var576) {
                                                         var10000 = var576;
                                                         var10001 = false;
                                                         break label5769;
                                                      }
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).media.vcard = "";
                                                   } catch (Exception var575) {
                                                      var10000 = var575;
                                                      var10001 = false;
                                                      break label5769;
                                                   }
                                                }

                                                var14 = "";
                                                var682 = var699;

                                                label5531: {
                                                   try {
                                                      if (((TLRPC.Message)var699).media.first_name != null) {
                                                         break label5531;
                                                      }
                                                   } catch (Exception var657) {
                                                      var10000 = var657;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).media.first_name = (String)var14;
                                                   } catch (Exception var574) {
                                                      var10000 = var574;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.User)var6).first_name = (String)var14;
                                                   } catch (Exception var573) {
                                                      var10000 = var573;
                                                      var10001 = false;
                                                      break label5769;
                                                   }
                                                }

                                                var682 = var699;

                                                label5532: {
                                                   try {
                                                      if (((TLRPC.Message)var699).media.last_name != null) {
                                                         break label5532;
                                                      }
                                                   } catch (Exception var656) {
                                                      var10000 = var656;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.Message)var699).media.last_name = (String)var14;
                                                   } catch (Exception var572) {
                                                      var10000 = var572;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var699;

                                                   try {
                                                      ((TLRPC.User)var6).last_name = (String)var14;
                                                   } catch (Exception var571) {
                                                      var10000 = var571;
                                                      var10001 = false;
                                                      break label5769;
                                                   }
                                                }

                                                if (var19 == null) {
                                                   break label5724;
                                                }

                                                var682 = var699;

                                                try {
                                                   var38 = var26.containsKey("query_id");
                                                } catch (Exception var570) {
                                                   var10000 = var570;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                if (!var38) {
                                                   break label5724;
                                                }
                                             }
                                          }

                                          var784 = 9;
                                          var767 = var14;
                                          break label5354;
                                       }
                                    }

                                    var699 = var682;
                                    break label5354;
                                 }

                                 var784 = 6;
                                 var767 = var25;
                              }

                              String var753 = "";
                              if (var17 != null) {
                                 label5698: {
                                    var682 = var699;

                                    try {
                                       if (var17.isEmpty()) {
                                          break label5698;
                                       }
                                    } catch (Exception var637) {
                                       var10000 = var637;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    var682 = var699;

                                    try {
                                       ((TLRPC.Message)var699).entities = var17;
                                    } catch (Exception var511) {
                                       var10000 = var511;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    var682 = var699;

                                    try {
                                       ((TLRPC.Message)var699).flags |= 128;
                                    } catch (Exception var510) {
                                       var10000 = var510;
                                       var10001 = false;
                                       break label5769;
                                    }
                                 }
                              }

                              if (var29 != null) {
                                 var682 = var699;

                                 try {
                                    ((TLRPC.Message)var699).message = var29;
                                 } catch (Exception var509) {
                                    var10000 = var509;
                                    var10001 = false;
                                    break label5769;
                                 }
                              } else {
                                 label5699: {
                                    var682 = var699;

                                    try {
                                       if (((TLRPC.Message)var699).message != null) {
                                          break label5699;
                                       }
                                    } catch (Exception var636) {
                                       var10000 = var636;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    var682 = var699;

                                    try {
                                       ((TLRPC.Message)var699).message = var753;
                                    } catch (Exception var508) {
                                       var10000 = var508;
                                       var10001 = false;
                                       break label5769;
                                    }
                                 }
                              }

                              var682 = var699;

                              label5549: {
                                 try {
                                    if (((TLRPC.Message)var699).attachPath != null) {
                                       break label5549;
                                    }
                                 } catch (Exception var635) {
                                    var10000 = var635;
                                    var10001 = false;
                                    break label5769;
                                 }

                                 var682 = var699;

                                 try {
                                    ((TLRPC.Message)var699).attachPath = var753;
                                 } catch (Exception var507) {
                                    var10000 = var507;
                                    var10001 = false;
                                    break label5769;
                                 }
                              }

                              var682 = var699;

                              try {
                                 var782 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                              } catch (Exception var506) {
                                 var10000 = var506;
                                 var10001 = false;
                                 break label5769;
                              }

                              var682 = var699;

                              try {
                                 ((TLRPC.Message)var699).id = var782;
                              } catch (Exception var505) {
                                 var10000 = var505;
                                 var10001 = false;
                                 break label5769;
                              }

                              var682 = var699;

                              try {
                                 ((TLRPC.Message)var699).local_id = var782;
                              } catch (Exception var504) {
                                 var10000 = var504;
                                 var10001 = false;
                                 break label5769;
                              }

                              var682 = var699;

                              try {
                                 ((TLRPC.Message)var699).out = true;
                              } catch (Exception var503) {
                                 var10000 = var503;
                                 var10001 = false;
                                 break label5769;
                              }

                              if (var36 && var33 != null) {
                                 var682 = var699;

                                 try {
                                    ((TLRPC.Message)var699).from_id = -var33.channel_id;
                                 } catch (Exception var502) {
                                    var10000 = var502;
                                    var10001 = false;
                                    break label5769;
                                 }
                              } else {
                                 var682 = var699;

                                 try {
                                    ((TLRPC.Message)var699).from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                 } catch (Exception var501) {
                                    var10000 = var501;
                                    var10001 = false;
                                    break label5769;
                                 }

                                 var682 = var699;

                                 try {
                                    ((TLRPC.Message)var699).flags |= 256;
                                 } catch (Exception var500) {
                                    var10000 = var500;
                                    var10001 = false;
                                    break label5769;
                                 }
                              }

                              var682 = var699;

                              try {
                                 UserConfig.getInstance(this.currentAccount).saveConfig(false);
                              } catch (Exception var499) {
                                 var10000 = var499;
                                 var10001 = false;
                                 break label5769;
                              }

                              var756 = var767;
                              var683 = var4;
                              var731 = var9;
                              var701 = var3;
                              var678 = var699;
                              var696 = var1;
                           }

                           String var685 = "";

                           label5729: {
                              long var40;
                              try {
                                 var40 = ((TLRPC.Message)var678).random_id;
                              } catch (Exception var618) {
                                 var10000 = var618;
                                 var10001 = false;
                                 break label5729;
                              }

                              if (var40 == 0L) {
                                 var682 = var678;

                                 try {
                                    ((TLRPC.Message)var678).random_id = this.getNextRandomId();
                                 } catch (Exception var498) {
                                    var10000 = var498;
                                    var10001 = false;
                                    break label5769;
                                 }
                              }

                              if (var26 != null) {
                                 label5552: {
                                    var682 = var678;

                                    try {
                                       if (!var26.containsKey("bot")) {
                                          break label5552;
                                       }
                                    } catch (Exception var634) {
                                       var10000 = var634;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    if (var35 != null) {
                                       label5553: {
                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).via_bot_name = (String)var26.get("bot_name");
                                          } catch (Exception var497) {
                                             var10000 = var497;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             if (((TLRPC.Message)var678).via_bot_name != null) {
                                                break label5553;
                                             }
                                          } catch (Exception var607) {
                                             var10000 = var607;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).via_bot_name = var685;
                                          } catch (Exception var496) {
                                             var10000 = var496;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }
                                    } else {
                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).via_bot_id = Utilities.parseInt((String)var26.get("bot"));
                                       } catch (Exception var495) {
                                          var10000 = var495;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    }

                                    var682 = var678;

                                    try {
                                       ((TLRPC.Message)var678).flags |= 2048;
                                    } catch (Exception var494) {
                                       var10000 = var494;
                                       var10001 = false;
                                       break label5769;
                                    }
                                 }
                              }

                              try {
                                 ((TLRPC.Message)var678).params = var26;
                              } catch (Exception var617) {
                                 var10000 = var617;
                                 var10001 = false;
                                 break label5729;
                              }

                              label5554: {
                                 if (var16 != null) {
                                    var682 = var678;

                                    try {
                                       var38 = var16.resendAsIs;
                                    } catch (Exception var493) {
                                       var10000 = var493;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    if (var38) {
                                       break label5554;
                                    }
                                 }

                                 try {
                                    ((TLRPC.Message)var678).date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                                    var38 = var33 instanceof TLRPC.TL_inputPeerChannel;
                                 } catch (Exception var616) {
                                    var10000 = var616;
                                    var10001 = false;
                                    break label5729;
                                 }

                                 if (var38) {
                                    if (var36) {
                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).views = 1;
                                       } catch (Exception var492) {
                                          var10000 = var492;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).flags |= 1024;
                                       } catch (Exception var491) {
                                          var10000 = var491;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    }

                                    var682 = var678;

                                    TLRPC.Chat var773;
                                    try {
                                       var773 = MessagesController.getInstance(this.currentAccount).getChat(var33.channel_id);
                                    } catch (Exception var490) {
                                       var10000 = var490;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    if (var773 != null) {
                                       label5558: {
                                          var682 = var678;

                                          label5559: {
                                             try {
                                                if (!var773.megagroup) {
                                                   break label5559;
                                                }
                                             } catch (Exception var633) {
                                                var10000 = var633;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;

                                             try {
                                                ((TLRPC.Message)var678).flags |= Integer.MIN_VALUE;
                                             } catch (Exception var489) {
                                                var10000 = var489;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;

                                             try {
                                                ((TLRPC.Message)var678).unread = true;
                                                break label5558;
                                             } catch (Exception var488) {
                                                var10000 = var488;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).post = true;
                                          } catch (Exception var487) {
                                             var10000 = var487;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             if (!var773.signatures) {
                                                break label5558;
                                             }
                                          } catch (Exception var632) {
                                             var10000 = var632;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                          } catch (Exception var486) {
                                             var10000 = var486;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }
                                    }
                                 } else {
                                    try {
                                       ((TLRPC.Message)var678).unread = true;
                                    } catch (Exception var615) {
                                       var10000 = var615;
                                       var10001 = false;
                                       break label5729;
                                    }
                                 }
                              }

                              try {
                                 ((TLRPC.Message)var678).flags |= 512;
                                 ((TLRPC.Message)var678).dialog_id = var10;
                              } catch (Exception var614) {
                                 var10000 = var614;
                                 var10001 = false;
                                 break label5729;
                              }

                              if (var13 != null) {
                                 label5730: {
                                    if (var35 != null) {
                                       label5700: {
                                          var682 = var678;

                                          try {
                                             if (var13.messageOwner.random_id == 0L) {
                                                break label5700;
                                             }
                                          } catch (Exception var631) {
                                             var10000 = var631;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).reply_to_random_id = var13.messageOwner.random_id;
                                          } catch (Exception var485) {
                                             var10000 = var485;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).flags |= 8;
                                             break label5730;
                                          } catch (Exception var484) {
                                             var10000 = var484;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }
                                    }

                                    var682 = var678;

                                    try {
                                       ((TLRPC.Message)var678).flags |= 8;
                                    } catch (Exception var483) {
                                       var10000 = var483;
                                       var10001 = false;
                                       break label5769;
                                    }
                                 }

                                 var682 = var678;

                                 try {
                                    ((TLRPC.Message)var678).reply_to_msg_id = var13.getId();
                                 } catch (Exception var482) {
                                    var10000 = var482;
                                    var10001 = false;
                                    break label5769;
                                 }
                              }

                              if (var18 != null && var35 == null) {
                                 var682 = var678;

                                 try {
                                    ((TLRPC.Message)var678).flags |= 64;
                                 } catch (Exception var481) {
                                    var10000 = var481;
                                    var10001 = false;
                                    break label5769;
                                 }

                                 var682 = var678;

                                 try {
                                    ((TLRPC.Message)var678).reply_markup = var18;
                                 } catch (Exception var480) {
                                    var10000 = var480;
                                    var10001 = false;
                                    break label5769;
                                 }
                              }

                              TLRPC.TL_photo var747;
                              ArrayList var774;
                              label5074: {
                                 if (var31 != 0) {
                                    if (var32 == 1) {
                                       var682 = var678;

                                       label5566: {
                                          try {
                                             if (this.currentChatInfo != null) {
                                                break label5566;
                                             }
                                          } catch (Exception var621) {
                                             var10000 = var621;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError((TLRPC.Message)var678);
                                          } catch (Exception var439) {
                                             var10000 = var439;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, ((TLRPC.Message)var678).id);
                                          } catch (Exception var438) {
                                             var10000 = var438;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             this.processSentMessage(((TLRPC.Message)var678).id);
                                             return;
                                          } catch (Exception var44) {
                                             var10000 = var44;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }

                                       var682 = var678;

                                       try {
                                          var774 = new ArrayList;
                                       } catch (Exception var474) {
                                          var10000 = var474;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          var774.<init>();
                                       } catch (Exception var473) {
                                          var10000 = var473;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       Iterator var744;
                                       try {
                                          var744 = this.currentChatInfo.participants.participants.iterator();
                                       } catch (Exception var472) {
                                          var10000 = var472;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       while(true) {
                                          var682 = var678;

                                          try {
                                             if (!var744.hasNext()) {
                                                break;
                                             }
                                          } catch (Exception var620) {
                                             var10000 = var620;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          TLRPC.ChatParticipant var780;
                                          try {
                                             var780 = (TLRPC.ChatParticipant)var744.next();
                                          } catch (Exception var471) {
                                             var10000 = var471;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          TLRPC.User var781;
                                          try {
                                             var781 = MessagesController.getInstance(this.currentAccount).getUser(var780.user_id);
                                          } catch (Exception var470) {
                                             var10000 = var470;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          TLRPC.InputUser var783;
                                          try {
                                             var783 = MessagesController.getInstance(this.currentAccount).getInputUser(var781);
                                          } catch (Exception var469) {
                                             var10000 = var469;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          if (var783 != null) {
                                             var682 = var678;

                                             try {
                                                var774.add(var783);
                                             } catch (Exception var468) {
                                                var10000 = var468;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }
                                       }

                                       var682 = var678;

                                       TLRPC.TL_peerChat var745;
                                       try {
                                          var745 = new TLRPC.TL_peerChat;
                                       } catch (Exception var467) {
                                          var10000 = var467;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          var745.<init>();
                                       } catch (Exception var466) {
                                          var10000 = var466;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).to_id = var745;
                                       } catch (Exception var465) {
                                          var10000 = var465;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).to_id.chat_id = var31;
                                       } catch (Exception var464) {
                                          var10000 = var464;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var782 = var20;
                                       var747 = var683;
                                       break label5074;
                                    }

                                    var682 = var678;

                                    try {
                                       ((TLRPC.Message)var678).to_id = MessagesController.getInstance(this.currentAccount).getPeer(var31);
                                    } catch (Exception var463) {
                                       var10000 = var463;
                                       var10001 = false;
                                       break label5769;
                                    }

                                    if (var31 > 0) {
                                       label5568: {
                                          var682 = var678;

                                          TLRPC.User var743;
                                          try {
                                             var743 = MessagesController.getInstance(this.currentAccount).getUser(var31);
                                          } catch (Exception var462) {
                                             var10000 = var462;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          if (var743 == null) {
                                             var682 = var678;

                                             try {
                                                this.processSentMessage(((TLRPC.Message)var678).id);
                                                return;
                                             } catch (Exception var45) {
                                                var10000 = var45;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }

                                          var682 = var678;

                                          try {
                                             if (!var743.bot) {
                                                break label5568;
                                             }
                                          } catch (Exception var630) {
                                             var10000 = var630;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).unread = false;
                                          } catch (Exception var461) {
                                             var10000 = var461;
                                             var10001 = false;
                                             break label5769;
                                          }
                                       }
                                    }
                                 } else {
                                    int var785;
                                    try {
                                       TLRPC.TL_peerUser var789 = new TLRPC.TL_peerUser();
                                       ((TLRPC.Message)var678).to_id = var789;
                                       var785 = var35.participant_id;
                                       var782 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                    } catch (Exception var613) {
                                       var10000 = var613;
                                       var10001 = false;
                                       break label5729;
                                    }

                                    if (var785 == var782) {
                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).to_id.user_id = var35.admin_id;
                                       } catch (Exception var479) {
                                          var10000 = var479;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    } else {
                                       try {
                                          ((TLRPC.Message)var678).to_id.user_id = var35.participant_id;
                                       } catch (Exception var612) {
                                          var10000 = var612;
                                          var10001 = false;
                                          break label5729;
                                       }
                                    }

                                    if (var20 != 0) {
                                       var682 = var678;

                                       try {
                                          ((TLRPC.Message)var678).ttl = var20;
                                       } catch (Exception var478) {
                                          var10000 = var478;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    } else {
                                       try {
                                          ((TLRPC.Message)var678).ttl = var35.ttl;
                                          var782 = ((TLRPC.Message)var678).ttl;
                                       } catch (Exception var611) {
                                          var10000 = var611;
                                          var10001 = false;
                                          break label5729;
                                       }

                                       if (var782 != 0) {
                                          label5702: {
                                             var682 = var678;

                                             try {
                                                if (((TLRPC.Message)var678).media == null) {
                                                   break label5702;
                                                }
                                             } catch (Exception var619) {
                                                var10000 = var619;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;

                                             try {
                                                ((TLRPC.Message)var678).media.ttl_seconds = ((TLRPC.Message)var678).ttl;
                                             } catch (Exception var477) {
                                                var10000 = var477;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;

                                             TLRPC.MessageMedia var748;
                                             try {
                                                var748 = ((TLRPC.Message)var678).media;
                                             } catch (Exception var476) {
                                                var10000 = var476;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;

                                             try {
                                                var748.flags |= 4;
                                             } catch (Exception var475) {
                                                var10000 = var475;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }
                                       }
                                    }

                                    try {
                                       var782 = ((TLRPC.Message)var678).ttl;
                                    } catch (Exception var610) {
                                       var10000 = var610;
                                       var10001 = false;
                                       break label5729;
                                    }

                                    if (var782 != 0) {
                                       label5571: {
                                          var682 = var678;

                                          try {
                                             if (((TLRPC.Message)var678).media.document == null) {
                                                break label5571;
                                             }
                                          } catch (Exception var629) {
                                             var10000 = var629;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var682 = var678;

                                          TLRPC.DocumentAttribute var750;
                                          label5572: {
                                             try {
                                                if (!MessageObject.isVoiceMessage((TLRPC.Message)var678)) {
                                                   break label5572;
                                                }
                                             } catch (Exception var628) {
                                                var10000 = var628;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var782 = 0;

                                             while(true) {
                                                var682 = var678;

                                                label5574: {
                                                   try {
                                                      if (var782 >= ((TLRPC.Message)var678).media.document.attributes.size()) {
                                                         break label5574;
                                                      }
                                                   } catch (Exception var623) {
                                                      var10000 = var623;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var678;

                                                   try {
                                                      var750 = (TLRPC.DocumentAttribute)((TLRPC.Message)var678).media.document.attributes.get(var782);
                                                   } catch (Exception var460) {
                                                      var10000 = var460;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   var682 = var678;

                                                   label5575: {
                                                      try {
                                                         if (!(var750 instanceof TLRPC.TL_documentAttributeAudio)) {
                                                            break label5575;
                                                         }
                                                      } catch (Exception var622) {
                                                         var10000 = var622;
                                                         var10001 = false;
                                                         break label5769;
                                                      }

                                                      var682 = var678;

                                                      try {
                                                         var782 = var750.duration;
                                                         break;
                                                      } catch (Exception var459) {
                                                         var10000 = var459;
                                                         var10001 = false;
                                                         break label5769;
                                                      }
                                                   }

                                                   ++var782;
                                                   continue;
                                                }

                                                var782 = 0;
                                                break;
                                             }

                                             var682 = var678;

                                             try {
                                                ((TLRPC.Message)var678).ttl = Math.max(((TLRPC.Message)var678).ttl, var782 + 1);
                                                break label5571;
                                             } catch (Exception var458) {
                                                var10000 = var458;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }

                                          var782 = var20;
                                          var682 = var678;

                                          label5576: {
                                             try {
                                                if (MessageObject.isVideoMessage((TLRPC.Message)var678)) {
                                                   break label5576;
                                                }
                                             } catch (Exception var627) {
                                                var10000 = var627;
                                                var10001 = false;
                                                break label5769;
                                             }

                                             var682 = var678;
                                             var20 = var20;

                                             try {
                                                if (!MessageObject.isRoundVideoMessage((TLRPC.Message)var678)) {
                                                   break label5571;
                                                }
                                             } catch (Exception var626) {
                                                var10000 = var626;
                                                var10001 = false;
                                                break label5769;
                                             }
                                          }

                                          var20 = 0;

                                          while(true) {
                                             var682 = var678;

                                             label5578: {
                                                try {
                                                   if (var20 >= ((TLRPC.Message)var678).media.document.attributes.size()) {
                                                      break label5578;
                                                   }
                                                } catch (Exception var625) {
                                                   var10000 = var625;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var678;

                                                try {
                                                   var750 = (TLRPC.DocumentAttribute)((TLRPC.Message)var678).media.document.attributes.get(var20);
                                                } catch (Exception var457) {
                                                   var10000 = var457;
                                                   var10001 = false;
                                                   break label5769;
                                                }

                                                var682 = var678;

                                                label5027: {
                                                   try {
                                                      if (var750 instanceof TLRPC.TL_documentAttributeVideo) {
                                                         break label5027;
                                                      }
                                                   } catch (Exception var624) {
                                                      var10000 = var624;
                                                      var10001 = false;
                                                      break label5769;
                                                   }

                                                   ++var20;
                                                   continue;
                                                }

                                                var682 = var678;

                                                try {
                                                   var20 = var750.duration;
                                                   break;
                                                } catch (Exception var456) {
                                                   var10000 = var456;
                                                   var10001 = false;
                                                   break label5769;
                                                }
                                             }

                                             var20 = 0;
                                             break;
                                          }

                                          var682 = var678;

                                          try {
                                             ((TLRPC.Message)var678).ttl = Math.max(((TLRPC.Message)var678).ttl, var20 + 1);
                                          } catch (Exception var455) {
                                             var10000 = var455;
                                             var10001 = false;
                                             break label5769;
                                          }

                                          var20 = var782;
                                       }
                                    }
                                 }

                                 var774 = null;
                                 var747 = var683;
                                 var782 = var20;
                              }

                              var767 = var6;
                              if (var32 != 1) {
                                 label5703: {
                                    var682 = var678;

                                    label5580: {
                                       try {
                                          if (MessageObject.isVoiceMessage((TLRPC.Message)var678)) {
                                             break label5580;
                                          }
                                       } catch (Exception var609) {
                                          var10000 = var609;
                                          var10001 = false;
                                          break label5769;
                                       }

                                       var682 = var678;

                                       try {
                                          if (!MessageObject.isRoundVideoMessage((TLRPC.Message)var678)) {
                                             break label5703;
                                          }
                                       } catch (Exception var608) {
                                          var10000 = var608;
                                          var10001 = false;
                                          break label5769;
                                       }
                                    }

                                    var682 = var678;

                                    try {
                                       ((TLRPC.Message)var678).media_unread = true;
                                    } catch (Exception var454) {
                                       var10000 = var454;
                                       var10001 = false;
                                       break label5769;
                                    }
                                 }
                              }

                              try {
                                 ((TLRPC.Message)var678).send_state = 1;
                                 var6 = new MessageObject(this.currentAccount, (TLRPC.Message)var678, true);
                              } catch (Exception var437) {
                                 var10000 = var437;
                                 var10001 = false;
                                 break label5729;
                              }

                              label4480: {
                                 Object var686;
                                 label4479: {
                                    label4478: {
                                       Exception var698;
                                       label5581: {
                                          boolean var761;
                                          label4476: {
                                             label5582: {
                                                try {
                                                   ((MessageObject)var6).replyMessageObject = var13;
                                                   var38 = ((MessageObject)var6).isForwarded();
                                                } catch (Exception var436) {
                                                   var10000 = var436;
                                                   var10001 = false;
                                                   break label5582;
                                                }

                                                label5732: {
                                                   if (!var38) {
                                                      label5704: {
                                                         label4468: {
                                                            try {
                                                               if (((MessageObject)var6).type == 3) {
                                                                  break label4468;
                                                               }
                                                            } catch (Exception var435) {
                                                               var10000 = var435;
                                                               var10001 = false;
                                                               break label5732;
                                                            }

                                                            if (var5 == null) {
                                                               try {
                                                                  if (((MessageObject)var6).type != 2) {
                                                                     break label5704;
                                                                  }
                                                               } catch (Exception var434) {
                                                                  var10000 = var434;
                                                                  var10001 = false;
                                                                  break label5732;
                                                               }
                                                            }
                                                         }

                                                         try {
                                                            if (!TextUtils.isEmpty(((TLRPC.Message)var678).attachPath)) {
                                                               ((MessageObject)var6).attachPathExists = true;
                                                            }
                                                         } catch (Exception var433) {
                                                            var10000 = var433;
                                                            var10001 = false;
                                                            break label5732;
                                                         }
                                                      }
                                                   }

                                                   VideoEditedInfo var790;
                                                   try {
                                                      var790 = ((MessageObject)var6).videoEditedInfo;
                                                   } catch (Exception var432) {
                                                      var10000 = var432;
                                                      var10001 = false;
                                                      break label5582;
                                                   }

                                                   if (var790 != null && var5 == null) {
                                                      try {
                                                         var5 = ((MessageObject)var6).videoEditedInfo;
                                                      } catch (Exception var431) {
                                                         var10000 = var431;
                                                         var10001 = false;
                                                         break label5732;
                                                      }
                                                   }

                                                   if (var26 == null) {
                                                      var40 = 0L;
                                                      var761 = false;
                                                      break label4476;
                                                   }

                                                   try {
                                                      var2 = (String)var26.get("groupId");
                                                   } catch (Exception var430) {
                                                      var10000 = var430;
                                                      var10001 = false;
                                                      break label5732;
                                                   }

                                                   if (var2 != null) {
                                                      try {
                                                         var40 = Utilities.parseLong(var2);
                                                         ((TLRPC.Message)var678).grouped_id = var40;
                                                      } catch (Exception var429) {
                                                         var10000 = var429;
                                                         var10001 = false;
                                                         break label5732;
                                                      }

                                                      try {
                                                         ((TLRPC.Message)var678).flags |= 131072;
                                                      } catch (Exception var428) {
                                                         var10000 = var428;
                                                         var10001 = false;
                                                         break label5732;
                                                      }
                                                   } else {
                                                      var40 = 0L;
                                                   }

                                                   try {
                                                      var682 = var26.get("final");
                                                   } catch (Exception var427) {
                                                      var10000 = var427;
                                                      var10001 = false;
                                                      break label5732;
                                                   }

                                                   if (var682 != null) {
                                                      var761 = true;
                                                   } else {
                                                      var761 = false;
                                                   }
                                                   break label4476;
                                                }

                                                var786 = var10000;
                                                var681 = var678;
                                                var678 = var6;
                                                break label5467;
                                             }

                                             var687 = var10000;
                                             var686 = var6;
                                             var682 = var678;
                                             var698 = var687;
                                             break label5581;
                                          }

                                          label4431: {
                                             label5586: {
                                                SendMessagesHelper.DelayedMessage var693;
                                                ArrayList var791;
                                                if (var40 == 0L) {
                                                   ArrayList var691;
                                                   try {
                                                      var691 = new ArrayList();
                                                      var691.add(var6);
                                                      var791 = new ArrayList();
                                                      var791.add(var678);
                                                   } catch (Exception var65) {
                                                      var786 = var65;
                                                      break label4478;
                                                   }

                                                   try {
                                                      MessagesStorage.getInstance(this.currentAccount).putMessages(var791, false, true, false, 0);
                                                      MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(var10, var691);
                                                      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
                                                   } catch (Exception var426) {
                                                      var10000 = var426;
                                                      var10001 = false;
                                                      break label5586;
                                                   }

                                                   var693 = null;
                                                } else {
                                                   try {
                                                      StringBuilder var792 = new StringBuilder();
                                                      var792.append("group_");
                                                      var792.append(var40);
                                                      var2 = var792.toString();
                                                      var791 = (ArrayList)this.delayedMessages.get(var2);
                                                   } catch (Exception var422) {
                                                      var10000 = var422;
                                                      var10001 = false;
                                                      break label4431;
                                                   }

                                                   if (var791 != null) {
                                                      try {
                                                         var693 = (SendMessagesHelper.DelayedMessage)var791.get(0);
                                                      } catch (Exception var425) {
                                                         var10000 = var425;
                                                         var10001 = false;
                                                         break label5586;
                                                      }
                                                   } else {
                                                      var693 = null;
                                                   }

                                                   SendMessagesHelper.DelayedMessage var793 = var693;
                                                   if (var693 == null) {
                                                      try {
                                                         var793 = new SendMessagesHelper.DelayedMessage(var10);
                                                         var793.initForGroup(var40);
                                                         var793.encryptedChat = var35;
                                                      } catch (Exception var424) {
                                                         var10000 = var424;
                                                         var10001 = false;
                                                         break label5586;
                                                      }
                                                   }

                                                   try {
                                                      var793.performMediaUpload = false;
                                                      var793.photoSize = null;
                                                      var793.videoEditedInfo = null;
                                                      var793.httpLocation = null;
                                                   } catch (Exception var421) {
                                                      var10000 = var421;
                                                      var10001 = false;
                                                      break label4431;
                                                   }

                                                   var693 = var793;
                                                   if (var761) {
                                                      try {
                                                         var793.finalGroupMessage = ((TLRPC.Message)var678).id;
                                                      } catch (Exception var423) {
                                                         var10000 = var423;
                                                         var10001 = false;
                                                         break label5586;
                                                      }

                                                      var693 = var793;
                                                   }
                                                }

                                                var682 = var6;
                                                Object var727 = var5;

                                                try {
                                                   var38 = BuildVars.LOGS_ENABLED;
                                                } catch (Exception var420) {
                                                   var10000 = var420;
                                                   var10001 = false;
                                                   break label4431;
                                                }

                                                if (var38 && var33 != null) {
                                                   try {
                                                      var5 = new StringBuilder();
                                                      ((StringBuilder)var5).append("send message user_id = ");
                                                      ((StringBuilder)var5).append(var33.user_id);
                                                      ((StringBuilder)var5).append(" chat_id = ");
                                                      ((StringBuilder)var5).append(var33.chat_id);
                                                      ((StringBuilder)var5).append(" channel_id = ");
                                                      ((StringBuilder)var5).append(var33.channel_id);
                                                      ((StringBuilder)var5).append(" access_hash = ");
                                                      ((StringBuilder)var5).append(var33.access_hash);
                                                      FileLog.d(((StringBuilder)var5).toString());
                                                   } catch (Exception var419) {
                                                      var10000 = var419;
                                                      var10001 = false;
                                                      break label5586;
                                                   }
                                                }

                                                label4401: {
                                                   label5762: {
                                                      StringBuilder var739;
                                                      if (var784 == 0 || var784 == 9 && var696 != null && var35 != null) {
                                                         var5 = var6;
                                                         if (var35 == null) {
                                                            if (var774 != null) {
                                                               label5591: {
                                                                  var681 = var678;

                                                                  try {
                                                                     var6 = new TLRPC.TL_messages_sendBroadcast;
                                                                  } catch (Exception var173) {
                                                                     var10000 = var173;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6.<init>();
                                                                  } catch (Exception var172) {
                                                                     var10000 = var172;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  ArrayList var751;
                                                                  try {
                                                                     var751 = new ArrayList;
                                                                  } catch (Exception var171) {
                                                                     var10000 = var171;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var751.<init>();
                                                                  } catch (Exception var170) {
                                                                     var10000 = var170;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var20 = 0;

                                                                  while(true) {
                                                                     var681 = var678;

                                                                     try {
                                                                        if (var20 >= var774.size()) {
                                                                           break;
                                                                        }
                                                                     } catch (Exception var160) {
                                                                        var10000 = var160;
                                                                        var10001 = false;
                                                                        break label5591;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var751.add(Utilities.random.nextLong());
                                                                     } catch (Exception var159) {
                                                                        var10000 = var159;
                                                                        var10001 = false;
                                                                        break label5591;
                                                                     }

                                                                     ++var20;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.TL_messages_sendBroadcast)var6).message = var696;
                                                                  } catch (Exception var158) {
                                                                     var10000 = var158;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.TL_messages_sendBroadcast)var6).contacts = var774;
                                                                  } catch (Exception var157) {
                                                                     var10000 = var157;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  TLRPC.TL_inputMediaEmpty var740;
                                                                  try {
                                                                     var740 = new TLRPC.TL_inputMediaEmpty;
                                                                  } catch (Exception var156) {
                                                                     var10000 = var156;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var740.<init>();
                                                                  } catch (Exception var133) {
                                                                     var10000 = var133;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.TL_messages_sendBroadcast)var6).media = var740;
                                                                  } catch (Exception var132) {
                                                                     var10000 = var132;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.TL_messages_sendBroadcast)var6).random_id = var751;
                                                                  } catch (Exception var131) {
                                                                     var10000 = var131;
                                                                     var10001 = false;
                                                                     break label5591;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     this.performSendMessageRequest((TLObject)var6, (MessageObject)var5, (String)null, (SendMessagesHelper.DelayedMessage)null, var21);
                                                                     return;
                                                                  } catch (Exception var61) {
                                                                     var10000 = var61;
                                                                     var10001 = false;
                                                                  }
                                                               }
                                                            } else {
                                                               label5593: {
                                                                  var681 = var678;

                                                                  TLRPC.TL_messages_sendMessage var752;
                                                                  try {
                                                                     var752 = new TLRPC.TL_messages_sendMessage;
                                                                  } catch (Exception var184) {
                                                                     var10000 = var184;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var752.<init>();
                                                                  } catch (Exception var183) {
                                                                     var10000 = var183;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var752.message = var696;
                                                                  } catch (Exception var182) {
                                                                     var10000 = var182;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  if (var16 == null) {
                                                                     var38 = true;
                                                                  } else {
                                                                     var38 = false;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var752.clear_draft = var38;
                                                                  } catch (Exception var181) {
                                                                     var10000 = var181;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  var681 = var678;

                                                                  label5594: {
                                                                     try {
                                                                        if (!(((TLRPC.Message)var678).to_id instanceof TLRPC.TL_peerChannel)) {
                                                                           break label5594;
                                                                        }
                                                                     } catch (Exception var180) {
                                                                        var10000 = var180;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var6 = MessagesController.getNotificationsSettings(this.currentAccount);
                                                                     } catch (Exception var179) {
                                                                        var10000 = var179;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var739 = new StringBuilder;
                                                                     } catch (Exception var178) {
                                                                        var10000 = var178;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var739.<init>();
                                                                     } catch (Exception var177) {
                                                                        var10000 = var177;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var739.append("silent_");
                                                                     } catch (Exception var176) {
                                                                        var10000 = var176;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var739.append(var10);
                                                                     } catch (Exception var175) {
                                                                        var10000 = var175;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var752.silent = ((SharedPreferences)var6).getBoolean(var739.toString(), false);
                                                                     } catch (Exception var174) {
                                                                        var10000 = var174;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var752.peer = var33;
                                                                  } catch (Exception var169) {
                                                                     var10000 = var169;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var752.random_id = ((TLRPC.Message)var678).random_id;
                                                                  } catch (Exception var168) {
                                                                     var10000 = var168;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  var681 = var678;

                                                                  label5595: {
                                                                     try {
                                                                        if (((TLRPC.Message)var678).reply_to_msg_id == 0) {
                                                                           break label5595;
                                                                        }
                                                                     } catch (Exception var167) {
                                                                        var10000 = var167;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var752.flags |= 1;
                                                                     } catch (Exception var166) {
                                                                        var10000 = var166;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var752.reply_to_msg_id = ((TLRPC.Message)var678).reply_to_msg_id;
                                                                     } catch (Exception var165) {
                                                                        var10000 = var165;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }
                                                                  }

                                                                  if (!var15) {
                                                                     var681 = var678;

                                                                     try {
                                                                        var752.no_webpage = true;
                                                                     } catch (Exception var164) {
                                                                        var10000 = var164;
                                                                        var10001 = false;
                                                                        break label5593;
                                                                     }
                                                                  }

                                                                  if (var17 != null) {
                                                                     label5596: {
                                                                        var681 = var678;

                                                                        try {
                                                                           if (var17.isEmpty()) {
                                                                              break label5596;
                                                                           }
                                                                        } catch (Exception var163) {
                                                                           var10000 = var163;
                                                                           var10001 = false;
                                                                           break label5593;
                                                                        }

                                                                        var681 = var678;

                                                                        try {
                                                                           var752.entities = var17;
                                                                        } catch (Exception var162) {
                                                                           var10000 = var162;
                                                                           var10001 = false;
                                                                           break label5593;
                                                                        }

                                                                        var681 = var678;

                                                                        try {
                                                                           var752.flags |= 8;
                                                                        } catch (Exception var161) {
                                                                           var10000 = var161;
                                                                           var10001 = false;
                                                                           break label5593;
                                                                        }
                                                                     }
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     this.performSendMessageRequest(var752, (MessageObject)var5, (String)null, (SendMessagesHelper.DelayedMessage)null, var21);
                                                                  } catch (Exception var134) {
                                                                     var10000 = var134;
                                                                     var10001 = false;
                                                                     break label5593;
                                                                  }

                                                                  if (var16 != null) {
                                                                     return;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     DataQuery.getInstance(this.currentAccount).cleanDraft(var10, false);
                                                                     return;
                                                                  } catch (Exception var62) {
                                                                     var10000 = var62;
                                                                     var10001 = false;
                                                                  }
                                                               }
                                                            }
                                                         } else {
                                                            label5597: {
                                                               var681 = var678;

                                                               label5598: {
                                                                  label5599: {
                                                                     try {
                                                                        if (AndroidUtilities.getPeerLayerVersion(var35.layer) < 73) {
                                                                           break label5599;
                                                                        }
                                                                     } catch (Exception var237) {
                                                                        var10000 = var237;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var686 = new TLRPC.TL_decryptedMessage;
                                                                     } catch (Exception var236) {
                                                                        var10000 = var236;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var686.<init>();
                                                                        break label5598;
                                                                     } catch (Exception var235) {
                                                                        var10000 = var235;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var686 = new TLRPC.TL_decryptedMessage_layer45();
                                                                  } catch (Exception var234) {
                                                                     var10000 = var234;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  ((TLRPC.DecryptedMessage)var686).ttl = ((TLRPC.Message)var678).ttl;
                                                               } catch (Exception var233) {
                                                                  var10000 = var233;
                                                                  var10001 = false;
                                                                  break label5597;
                                                               }

                                                               if (var17 != null) {
                                                                  label5600: {
                                                                     var681 = var678;

                                                                     try {
                                                                        if (var17.isEmpty()) {
                                                                           break label5600;
                                                                        }
                                                                     } catch (Exception var232) {
                                                                        var10000 = var232;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var686).entities = var17;
                                                                     } catch (Exception var231) {
                                                                        var10000 = var231;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var686).flags |= 128;
                                                                     } catch (Exception var230) {
                                                                        var10000 = var230;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               label5601: {
                                                                  try {
                                                                     if (((TLRPC.Message)var678).reply_to_random_id == 0L) {
                                                                        break label5601;
                                                                     }
                                                                  } catch (Exception var229) {
                                                                     var10000 = var229;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.DecryptedMessage)var686).reply_to_random_id = ((TLRPC.Message)var678).reply_to_random_id;
                                                                  } catch (Exception var228) {
                                                                     var10000 = var228;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.DecryptedMessage)var686).flags |= 8;
                                                                  } catch (Exception var227) {
                                                                     var10000 = var227;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }
                                                               }

                                                               if (var26 != null) {
                                                                  label5602: {
                                                                     var681 = var678;

                                                                     try {
                                                                        if (var26.get("bot_name") == null) {
                                                                           break label5602;
                                                                        }
                                                                     } catch (Exception var226) {
                                                                        var10000 = var226;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var686).via_bot_name = (String)var26.get("bot_name");
                                                                     } catch (Exception var225) {
                                                                        var10000 = var225;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var686).flags |= 2048;
                                                                     } catch (Exception var224) {
                                                                        var10000 = var224;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  ((TLRPC.DecryptedMessage)var686).random_id = ((TLRPC.Message)var678).random_id;
                                                               } catch (Exception var223) {
                                                                  var10000 = var223;
                                                                  var10001 = false;
                                                                  break label5597;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  ((TLRPC.DecryptedMessage)var686).message = var696;
                                                               } catch (Exception var222) {
                                                                  var10000 = var222;
                                                                  var10001 = false;
                                                                  break label5597;
                                                               }

                                                               label5603: {
                                                                  label5604: {
                                                                     if (var756 != null) {
                                                                        var681 = var678;

                                                                        try {
                                                                           if (((TLRPC.WebPage)var756).url != null) {
                                                                              break label5604;
                                                                           }
                                                                        } catch (Exception var221) {
                                                                           var10000 = var221;
                                                                           var10001 = false;
                                                                           break label5597;
                                                                        }
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var6 = new TLRPC.TL_decryptedMessageMediaEmpty;
                                                                     } catch (Exception var215) {
                                                                        var10000 = var215;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var6.<init>();
                                                                     } catch (Exception var214) {
                                                                        var10000 = var214;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var686).media = (TLRPC.DecryptedMessageMedia)var6;
                                                                        break label5603;
                                                                     } catch (Exception var213) {
                                                                        var10000 = var213;
                                                                        var10001 = false;
                                                                        break label5597;
                                                                     }
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6 = new TLRPC.TL_decryptedMessageMediaWebPage;
                                                                  } catch (Exception var220) {
                                                                     var10000 = var220;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6.<init>();
                                                                  } catch (Exception var219) {
                                                                     var10000 = var219;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.DecryptedMessage)var686).media = (TLRPC.DecryptedMessageMedia)var6;
                                                                  } catch (Exception var218) {
                                                                     var10000 = var218;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.DecryptedMessage)var686).media.url = ((TLRPC.WebPage)var756).url;
                                                                  } catch (Exception var217) {
                                                                     var10000 = var217;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((TLRPC.DecryptedMessage)var686).flags |= 512;
                                                                  } catch (Exception var216) {
                                                                     var10000 = var216;
                                                                     var10001 = false;
                                                                     break label5597;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var686, ((MessageObject)var5).messageOwner, var35, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)var5);
                                                               } catch (Exception var155) {
                                                                  var10000 = var155;
                                                                  var10001 = false;
                                                                  break label5597;
                                                               }

                                                               if (var16 != null) {
                                                                  return;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  DataQuery.getInstance(this.currentAccount).cleanDraft(var10, false);
                                                                  return;
                                                               } catch (Exception var63) {
                                                                  var10000 = var63;
                                                                  var10001 = false;
                                                               }
                                                            }
                                                         }
                                                      } else if ((var784 < 1 || var784 > 3) && (var784 < 5 || var784 > 8) && (var784 != 9 || var35 == null) && var784 != 10) {
                                                         var686 = var6;
                                                         if (var784 == 4) {
                                                            label5606: {
                                                               var681 = var678;

                                                               TLRPC.TL_messages_forwardMessages var787;
                                                               try {
                                                                  var787 = new TLRPC.TL_messages_forwardMessages;
                                                               } catch (Exception var212) {
                                                                  var10000 = var212;
                                                                  var10001 = false;
                                                                  break label5606;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var787.<init>();
                                                               } catch (Exception var211) {
                                                                  var10000 = var211;
                                                                  var10001 = false;
                                                                  break label5606;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var787.to_peer = var33;
                                                               } catch (Exception var210) {
                                                                  var10000 = var210;
                                                                  var10001 = false;
                                                                  break label5606;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var787.with_my_score = var16.messageOwner.with_my_score;
                                                               } catch (Exception var209) {
                                                                  var10000 = var209;
                                                                  var10001 = false;
                                                                  break label5606;
                                                               }

                                                               var681 = var678;

                                                               label5607: {
                                                                  label5608: {
                                                                     try {
                                                                        if (var16.messageOwner.ttl == 0) {
                                                                           break label5608;
                                                                        }
                                                                     } catch (Exception var208) {
                                                                        var10000 = var208;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var6 = MessagesController.getInstance(this.currentAccount).getChat(-var16.messageOwner.ttl);
                                                                     } catch (Exception var207) {
                                                                        var10000 = var207;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     TLRPC.TL_inputPeerChannel var734;
                                                                     try {
                                                                        var734 = new TLRPC.TL_inputPeerChannel;
                                                                     } catch (Exception var206) {
                                                                        var10000 = var206;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var734.<init>();
                                                                     } catch (Exception var205) {
                                                                        var10000 = var205;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var787.from_peer = var734;
                                                                     } catch (Exception var204) {
                                                                        var10000 = var204;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var787.from_peer.channel_id = -var16.messageOwner.ttl;
                                                                     } catch (Exception var203) {
                                                                        var10000 = var203;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     if (var6 != null) {
                                                                        var681 = var678;

                                                                        try {
                                                                           var787.from_peer.access_hash = ((TLRPC.Chat)var6).access_hash;
                                                                        } catch (Exception var202) {
                                                                           var10000 = var202;
                                                                           var10001 = false;
                                                                           break label5606;
                                                                        }
                                                                     }
                                                                     break label5607;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6 = new TLRPC.TL_inputPeerEmpty;
                                                                  } catch (Exception var201) {
                                                                     var10000 = var201;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6.<init>();
                                                                  } catch (Exception var200) {
                                                                     var10000 = var200;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var787.from_peer = (TLRPC.InputPeer)var6;
                                                                  } catch (Exception var199) {
                                                                     var10000 = var199;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               label5609: {
                                                                  try {
                                                                     if (!(var16.messageOwner.to_id instanceof TLRPC.TL_peerChannel)) {
                                                                        break label5609;
                                                                     }
                                                                  } catch (Exception var198) {
                                                                     var10000 = var198;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  SharedPreferences var736;
                                                                  try {
                                                                     var736 = MessagesController.getNotificationsSettings(this.currentAccount);
                                                                  } catch (Exception var197) {
                                                                     var10000 = var197;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6 = new StringBuilder;
                                                                  } catch (Exception var196) {
                                                                     var10000 = var196;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6.<init>();
                                                                  } catch (Exception var195) {
                                                                     var10000 = var195;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((StringBuilder)var6).append("silent_");
                                                                  } catch (Exception var194) {
                                                                     var10000 = var194;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     ((StringBuilder)var6).append(var10);
                                                                  } catch (Exception var193) {
                                                                     var10000 = var193;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var787.silent = var736.getBoolean(((StringBuilder)var6).toString(), false);
                                                                  } catch (Exception var192) {
                                                                     var10000 = var192;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var787.random_id.add(((TLRPC.Message)var678).random_id);
                                                               } catch (Exception var191) {
                                                                  var10000 = var191;
                                                                  var10001 = false;
                                                                  break label5606;
                                                               }

                                                               var681 = var678;

                                                               label5610: {
                                                                  label5611: {
                                                                     try {
                                                                        if (var16.getId() >= 0) {
                                                                           break label5611;
                                                                        }
                                                                     } catch (Exception var190) {
                                                                        var10000 = var190;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     label5612: {
                                                                        try {
                                                                           if (var16.messageOwner.fwd_msg_id == 0) {
                                                                              break label5612;
                                                                           }
                                                                        } catch (Exception var189) {
                                                                           var10000 = var189;
                                                                           var10001 = false;
                                                                           break label5606;
                                                                        }

                                                                        var681 = var678;

                                                                        try {
                                                                           var787.id.add(var16.messageOwner.fwd_msg_id);
                                                                           break label5610;
                                                                        } catch (Exception var186) {
                                                                           var10000 = var186;
                                                                           var10001 = false;
                                                                           break label5606;
                                                                        }
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        if (var16.messageOwner.fwd_from == null) {
                                                                           break label5610;
                                                                        }
                                                                     } catch (Exception var188) {
                                                                        var10000 = var188;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }

                                                                     var681 = var678;

                                                                     try {
                                                                        var787.id.add(var16.messageOwner.fwd_from.channel_post);
                                                                        break label5610;
                                                                     } catch (Exception var185) {
                                                                        var10000 = var185;
                                                                        var10001 = false;
                                                                        break label5606;
                                                                     }
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var787.id.add(var16.getId());
                                                                  } catch (Exception var187) {
                                                                     var10000 = var187;
                                                                     var10001 = false;
                                                                     break label5606;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  this.performSendMessageRequest(var787, (MessageObject)var686, (String)null, (SendMessagesHelper.DelayedMessage)null, var21);
                                                                  return;
                                                               } catch (Exception var59) {
                                                                  var10000 = var59;
                                                                  var10001 = false;
                                                               }
                                                            }
                                                         } else {
                                                            label5613: {
                                                               if (var784 != 9) {
                                                                  return;
                                                               }

                                                               var681 = var678;

                                                               TLRPC.TL_messages_sendInlineBotResult var788;
                                                               try {
                                                                  var788 = new TLRPC.TL_messages_sendInlineBotResult;
                                                               } catch (Exception var154) {
                                                                  var10000 = var154;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.<init>();
                                                               } catch (Exception var153) {
                                                                  var10000 = var153;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.peer = var33;
                                                               } catch (Exception var152) {
                                                                  var10000 = var152;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.random_id = ((TLRPC.Message)var678).random_id;
                                                               } catch (Exception var151) {
                                                                  var10000 = var151;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               label3292: {
                                                                  label3291: {
                                                                     try {
                                                                        if (!var26.containsKey("bot")) {
                                                                           break label3291;
                                                                        }
                                                                     } catch (Exception var150) {
                                                                        var10000 = var150;
                                                                        var10001 = false;
                                                                        break label5613;
                                                                     }

                                                                     var15 = false;
                                                                     break label3292;
                                                                  }

                                                                  var15 = true;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.hide_via = var15;
                                                               } catch (Exception var149) {
                                                                  var10000 = var149;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               label5614: {
                                                                  try {
                                                                     if (((TLRPC.Message)var678).reply_to_msg_id == 0) {
                                                                        break label5614;
                                                                     }
                                                                  } catch (Exception var148) {
                                                                     var10000 = var148;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var788.flags |= 1;
                                                                  } catch (Exception var147) {
                                                                     var10000 = var147;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var788.reply_to_msg_id = ((TLRPC.Message)var678).reply_to_msg_id;
                                                                  } catch (Exception var146) {
                                                                     var10000 = var146;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               label5615: {
                                                                  try {
                                                                     if (!(((TLRPC.Message)var678).to_id instanceof TLRPC.TL_peerChannel)) {
                                                                        break label5615;
                                                                     }
                                                                  } catch (Exception var145) {
                                                                     var10000 = var145;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var6 = MessagesController.getNotificationsSettings(this.currentAccount);
                                                                  } catch (Exception var144) {
                                                                     var10000 = var144;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var739 = new StringBuilder;
                                                                  } catch (Exception var143) {
                                                                     var10000 = var143;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var739.<init>();
                                                                  } catch (Exception var142) {
                                                                     var10000 = var142;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var739.append("silent_");
                                                                  } catch (Exception var141) {
                                                                     var10000 = var141;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var739.append(var10);
                                                                  } catch (Exception var140) {
                                                                     var10000 = var140;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     var788.silent = ((SharedPreferences)var6).getBoolean(var739.toString(), false);
                                                                  } catch (Exception var139) {
                                                                     var10000 = var139;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.query_id = Utilities.parseLong((String)var26.get("query_id"));
                                                               } catch (Exception var138) {
                                                                  var10000 = var138;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  var788.id = (String)var26.get("id");
                                                               } catch (Exception var137) {
                                                                  var10000 = var137;
                                                                  var10001 = false;
                                                                  break label5613;
                                                               }

                                                               if (var16 == null) {
                                                                  var681 = var678;

                                                                  try {
                                                                     var788.clear_draft = true;
                                                                  } catch (Exception var136) {
                                                                     var10000 = var136;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }

                                                                  var681 = var678;

                                                                  try {
                                                                     DataQuery.getInstance(this.currentAccount).cleanDraft(var10, false);
                                                                  } catch (Exception var135) {
                                                                     var10000 = var135;
                                                                     var10001 = false;
                                                                     break label5613;
                                                                  }
                                                               }

                                                               var681 = var678;

                                                               try {
                                                                  this.performSendMessageRequest(var788, (MessageObject)var686, (String)null, (SendMessagesHelper.DelayedMessage)null, var21);
                                                                  return;
                                                               } catch (Exception var60) {
                                                                  var10000 = var60;
                                                                  var10001 = false;
                                                               }
                                                            }
                                                         }
                                                      } else {
                                                         label5756: {
                                                            label5733: {
                                                               if (var35 == null) {
                                                                  label5618: {
                                                                     SendMessagesHelper.DelayedMessage var694;
                                                                     Object var728;
                                                                     String var749;
                                                                     if (var784 == 1) {
                                                                        label5764: {
                                                                           try {
                                                                              if (var701 instanceof TLRPC.TL_messageMediaVenue) {
                                                                                 var5 = new TLRPC.TL_inputMediaVenue();
                                                                                 ((TLRPC.InputMedia)var5).address = var701.address;
                                                                                 ((TLRPC.InputMedia)var5).title = var701.title;
                                                                                 ((TLRPC.InputMedia)var5).provider = var701.provider;
                                                                                 ((TLRPC.InputMedia)var5).venue_id = var701.venue_id;
                                                                                 ((TLRPC.InputMedia)var5).venue_type = var685;
                                                                                 break label5764;
                                                                              }
                                                                           } catch (Exception var313) {
                                                                              var10000 = var313;
                                                                              var10001 = false;
                                                                              break label5586;
                                                                           }

                                                                           try {
                                                                              if (var701 instanceof TLRPC.TL_messageMediaGeoLive) {
                                                                                 var5 = new TLRPC.TL_inputMediaGeoLive();
                                                                                 ((TLRPC.InputMedia)var5).period = var701.period;
                                                                                 ((TLRPC.InputMedia)var5).flags |= 2;
                                                                                 break label5764;
                                                                              }
                                                                           } catch (Exception var312) {
                                                                              var10000 = var312;
                                                                              var10001 = false;
                                                                              break label5586;
                                                                           }

                                                                           try {
                                                                              var5 = new TLRPC.TL_inputMediaGeoPoint();
                                                                           } catch (Exception var242) {
                                                                              var10000 = var242;
                                                                              var10001 = false;
                                                                              break label5586;
                                                                           }
                                                                        }

                                                                        try {
                                                                           TLRPC.TL_inputGeoPoint var709 = new TLRPC.TL_inputGeoPoint();
                                                                           ((TLRPC.InputMedia)var5).geo_point = var709;
                                                                           ((TLRPC.InputMedia)var5).geo_point.lat = var701.geo.lat;
                                                                           ((TLRPC.InputMedia)var5).geo_point._long = var701.geo._long;
                                                                        } catch (Exception var241) {
                                                                           var10000 = var241;
                                                                           var10001 = false;
                                                                           break label5586;
                                                                        }

                                                                        var15 = false;
                                                                        var686 = var5;
                                                                        var694 = var693;
                                                                        var749 = var685;
                                                                     } else {
                                                                        label5771: {
                                                                           label5763: {
                                                                              if (var784 == 2 || var784 == 9 && var747 != null) {
                                                                                 var728 = var6;
                                                                                 var5 = var6;

                                                                                 TLRPC.TL_inputMediaUploadedPhoto var763;
                                                                                 try {
                                                                                    var763 = new TLRPC.TL_inputMediaUploadedPhoto;
                                                                                 } catch (Exception var298) {
                                                                                    var10000 = var298;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    var763.<init>();
                                                                                 } catch (Exception var297) {
                                                                                    var10000 = var297;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 if (var782 != 0) {
                                                                                    var5 = var6;

                                                                                    try {
                                                                                       var763.ttl_seconds = var782;
                                                                                    } catch (Exception var296) {
                                                                                       var10000 = var296;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var6;

                                                                                    try {
                                                                                       ((TLRPC.Message)var678).ttl = var782;
                                                                                    } catch (Exception var295) {
                                                                                       var10000 = var295;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var6;

                                                                                    try {
                                                                                       var763.flags |= 2;
                                                                                    } catch (Exception var294) {
                                                                                       var10000 = var294;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }
                                                                                 }

                                                                                 if (var26 != null) {
                                                                                    var5 = var6;

                                                                                    String var720;
                                                                                    try {
                                                                                       var720 = (String)var26.get("masks");
                                                                                    } catch (Exception var293) {
                                                                                       var10000 = var293;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    if (var720 != null) {
                                                                                       var5 = var6;

                                                                                       SerializedData var717;
                                                                                       try {
                                                                                          var717 = new SerializedData;
                                                                                       } catch (Exception var292) {
                                                                                          var10000 = var292;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var6;

                                                                                       try {
                                                                                          var717.<init>(Utilities.hexToBytes(var720));
                                                                                       } catch (Exception var291) {
                                                                                          var10000 = var291;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var6;

                                                                                       try {
                                                                                          var782 = var717.readInt32(false);
                                                                                       } catch (Exception var290) {
                                                                                          var10000 = var290;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       for(var20 = 0; var20 < var782; ++var20) {
                                                                                          var5 = var728;

                                                                                          try {
                                                                                             var763.stickers.add(TLRPC.InputDocument.TLdeserialize(var717, var717.readInt32(false), false));
                                                                                          } catch (Exception var289) {
                                                                                             var10000 = var289;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }
                                                                                       }

                                                                                       var5 = var728;

                                                                                       try {
                                                                                          var763.flags |= 1;
                                                                                       } catch (Exception var288) {
                                                                                          var10000 = var288;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var728;

                                                                                       try {
                                                                                          var717.cleanup();
                                                                                       } catch (Exception var287) {
                                                                                          var10000 = var287;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }
                                                                                    }
                                                                                 }

                                                                                 var5 = var728;

                                                                                 label5627: {
                                                                                    label3995: {
                                                                                       try {
                                                                                          if (var747.access_hash != 0L) {
                                                                                             break label3995;
                                                                                          }
                                                                                       } catch (Exception var331) {
                                                                                          var10000 = var331;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var6 = var763;
                                                                                       var15 = true;
                                                                                       break label5627;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var6 = new TLRPC.TL_inputMediaPhoto;
                                                                                    } catch (Exception var286) {
                                                                                       var10000 = var286;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var6.<init>();
                                                                                    } catch (Exception var285) {
                                                                                       var10000 = var285;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    TLRPC.TL_inputPhoto var723;
                                                                                    try {
                                                                                       var723 = new TLRPC.TL_inputPhoto;
                                                                                    } catch (Exception var284) {
                                                                                       var10000 = var284;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var723.<init>();
                                                                                    } catch (Exception var283) {
                                                                                       var10000 = var283;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       ((TLRPC.TL_inputMediaPhoto)var6).id = var723;
                                                                                    } catch (Exception var282) {
                                                                                       var10000 = var282;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       ((TLRPC.TL_inputMediaPhoto)var6).id.id = var747.id;
                                                                                    } catch (Exception var281) {
                                                                                       var10000 = var281;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       ((TLRPC.TL_inputMediaPhoto)var6).id.access_hash = var747.access_hash;
                                                                                    } catch (Exception var280) {
                                                                                       var10000 = var280;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       ((TLRPC.TL_inputMediaPhoto)var6).id.file_reference = var747.file_reference;
                                                                                    } catch (Exception var279) {
                                                                                       var10000 = var279;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    label5628: {
                                                                                       try {
                                                                                          if (((TLRPC.TL_inputMediaPhoto)var6).id.file_reference != null) {
                                                                                             break label5628;
                                                                                          }
                                                                                       } catch (Exception var330) {
                                                                                          var10000 = var330;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var728;

                                                                                       try {
                                                                                          ((TLRPC.TL_inputMediaPhoto)var6).id.file_reference = new byte[0];
                                                                                       } catch (Exception var278) {
                                                                                          var10000 = var278;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }
                                                                                    }

                                                                                    var15 = false;
                                                                                 }

                                                                                 if (var693 == null) {
                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693 = new SendMessagesHelper.DelayedMessage;
                                                                                    } catch (Exception var277) {
                                                                                       var10000 = var277;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.<init>(var10);
                                                                                    } catch (Exception var276) {
                                                                                       var10000 = var276;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.type = 0;
                                                                                    } catch (Exception var275) {
                                                                                       var10000 = var275;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.obj = (MessageObject)var728;
                                                                                    } catch (Exception var274) {
                                                                                       var10000 = var274;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.originalPath = var746;
                                                                                    } catch (Exception var273) {
                                                                                       var10000 = var273;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }
                                                                                 }

                                                                                 var5 = var728;

                                                                                 try {
                                                                                    var693.inputUploadMedia = var763;
                                                                                 } catch (Exception var272) {
                                                                                    var10000 = var272;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var728;

                                                                                 try {
                                                                                    var693.performMediaUpload = var15;
                                                                                 } catch (Exception var271) {
                                                                                    var10000 = var271;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 label5736: {
                                                                                    if (var12 != null) {
                                                                                       label5707: {
                                                                                          var5 = var728;

                                                                                          try {
                                                                                             if (var12.length() <= 0) {
                                                                                                break label5707;
                                                                                             }
                                                                                          } catch (Exception var329) {
                                                                                             var10000 = var329;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }

                                                                                          var5 = var728;

                                                                                          try {
                                                                                             if (!var12.startsWith("http")) {
                                                                                                break label5707;
                                                                                             }
                                                                                          } catch (Exception var328) {
                                                                                             var10000 = var328;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }

                                                                                          var5 = var728;

                                                                                          try {
                                                                                             var693.httpLocation = var12;
                                                                                             break label5736;
                                                                                          } catch (Exception var270) {
                                                                                             var10000 = var270;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }
                                                                                       }
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.photoSize = (TLRPC.PhotoSize)var747.sizes.get(var747.sizes.size() - 1);
                                                                                    } catch (Exception var269) {
                                                                                       var10000 = var269;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }

                                                                                    var5 = var728;

                                                                                    try {
                                                                                       var693.locationParent = var747;
                                                                                    } catch (Exception var268) {
                                                                                       var10000 = var268;
                                                                                       var10001 = false;
                                                                                       break label5618;
                                                                                    }
                                                                                 }

                                                                                 var5 = var6;
                                                                              } else {
                                                                                 if (var784 == 3) {
                                                                                    TLRPC.TL_inputMediaUploadedDocument var759;
                                                                                    label5633: {
                                                                                       try {
                                                                                          var759 = new TLRPC.TL_inputMediaUploadedDocument();
                                                                                          var759.mime_type = var7.mime_type;
                                                                                          var759.attributes = var7.attributes;
                                                                                          if (MessageObject.isRoundVideoDocument(var7)) {
                                                                                             break label5633;
                                                                                          }
                                                                                       } catch (Exception var325) {
                                                                                          var10000 = var325;
                                                                                          var10001 = false;
                                                                                          break label5586;
                                                                                       }

                                                                                       if (var5 != null) {
                                                                                          var5 = var5;

                                                                                          try {
                                                                                             if (((VideoEditedInfo)var5).muted || ((VideoEditedInfo)var5).roundVideo) {
                                                                                                break label5633;
                                                                                             }
                                                                                          } catch (Exception var324) {
                                                                                             var10000 = var324;
                                                                                             var10001 = false;
                                                                                             break label5586;
                                                                                          }
                                                                                       }

                                                                                       try {
                                                                                          var759.nosound_video = true;
                                                                                          if (BuildVars.DEBUG_VERSION) {
                                                                                             FileLog.d("nosound_video = true");
                                                                                          }
                                                                                       } catch (Exception var240) {
                                                                                          var10000 = var240;
                                                                                          var10001 = false;
                                                                                          break label5586;
                                                                                       }
                                                                                    }

                                                                                    if (var782 != 0) {
                                                                                       try {
                                                                                          var759.ttl_seconds = var782;
                                                                                          ((TLRPC.Message)var678).ttl = var782;
                                                                                          var759.flags |= 2;
                                                                                       } catch (Exception var239) {
                                                                                          var10000 = var239;
                                                                                          var10001 = false;
                                                                                          break label5586;
                                                                                       }
                                                                                    }

                                                                                    label5635: {
                                                                                       label3933: {
                                                                                          try {
                                                                                             if (var7.access_hash != 0L) {
                                                                                                break label3933;
                                                                                             }
                                                                                          } catch (Exception var323) {
                                                                                             var10000 = var323;
                                                                                             var10001 = false;
                                                                                             break label5586;
                                                                                          }

                                                                                          var5 = var759;
                                                                                          var15 = true;
                                                                                          break label5635;
                                                                                       }

                                                                                       try {
                                                                                          var5 = new TLRPC.TL_inputMediaDocument();
                                                                                          TLRPC.TL_inputDocument var714 = new TLRPC.TL_inputDocument();
                                                                                          ((TLRPC.TL_inputMediaDocument)var5).id = var714;
                                                                                          ((TLRPC.TL_inputMediaDocument)var5).id.id = var7.id;
                                                                                          ((TLRPC.TL_inputMediaDocument)var5).id.access_hash = var7.access_hash;
                                                                                          ((TLRPC.TL_inputMediaDocument)var5).id.file_reference = var7.file_reference;
                                                                                          if (((TLRPC.TL_inputMediaDocument)var5).id.file_reference == null) {
                                                                                             ((TLRPC.TL_inputMediaDocument)var5).id.file_reference = new byte[0];
                                                                                          }
                                                                                       } catch (Exception var322) {
                                                                                          var10000 = var322;
                                                                                          var10001 = false;
                                                                                          break label5586;
                                                                                       }

                                                                                       var15 = false;
                                                                                    }

                                                                                    if (var693 == null) {
                                                                                       try {
                                                                                          var693 = new SendMessagesHelper.DelayedMessage(var10);
                                                                                          var693.type = 1;
                                                                                       } catch (Exception var238) {
                                                                                          var10000 = var238;
                                                                                          var10001 = false;
                                                                                          break label5586;
                                                                                       }

                                                                                       try {
                                                                                          var693.obj = (MessageObject)var682;
                                                                                          var693.originalPath = var746;
                                                                                          var693.parentObject = var21;
                                                                                       } catch (Exception var249) {
                                                                                          var10000 = var249;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }
                                                                                    }

                                                                                    try {
                                                                                       var693.inputUploadMedia = var759;
                                                                                       var693.performMediaUpload = var15;
                                                                                       if (!var7.thumbs.isEmpty()) {
                                                                                          var693.photoSize = (TLRPC.PhotoSize)var7.thumbs.get(0);
                                                                                          var693.locationParent = var7;
                                                                                       }
                                                                                    } catch (Exception var321) {
                                                                                       var10000 = var321;
                                                                                       var10001 = false;
                                                                                       break label4401;
                                                                                    }

                                                                                    try {
                                                                                       var693.videoEditedInfo = (VideoEditedInfo)var727;
                                                                                       break label5763;
                                                                                    } catch (Exception var248) {
                                                                                       var10000 = var248;
                                                                                       var10001 = false;
                                                                                       break label4401;
                                                                                    }
                                                                                 }

                                                                                 var5 = var6;
                                                                                 String var729 = var746;
                                                                                 Object var713 = var21;
                                                                                 if (var784 == 6) {
                                                                                    label3956: {
                                                                                       try {
                                                                                          var5 = new TLRPC.TL_inputMediaContact();
                                                                                          ((TLRPC.InputMedia)var5).phone_number = ((TLRPC.User)var767).phone;
                                                                                          ((TLRPC.InputMedia)var5).first_name = ((TLRPC.User)var767).first_name;
                                                                                          ((TLRPC.InputMedia)var5).last_name = ((TLRPC.User)var767).last_name;
                                                                                          if (((TLRPC.User)var767).restriction_reason != null && ((TLRPC.User)var767).restriction_reason.startsWith("BEGIN:VCARD")) {
                                                                                             ((TLRPC.InputMedia)var5).vcard = ((TLRPC.User)var767).restriction_reason;
                                                                                             break label3956;
                                                                                          }
                                                                                       } catch (Exception var326) {
                                                                                          var10000 = var326;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }

                                                                                       try {
                                                                                          ((TLRPC.InputMedia)var5).vcard = var685;
                                                                                       } catch (Exception var247) {
                                                                                          var10000 = var247;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }
                                                                                    }

                                                                                    var15 = false;
                                                                                    break label5763;
                                                                                 }

                                                                                 if (var784 == 7 || var784 == 9) {
                                                                                    label5737: {
                                                                                       if (var746 == null && var12 == null) {
                                                                                          label5709: {
                                                                                             try {
                                                                                                if (var7.access_hash == 0L) {
                                                                                                   break label5709;
                                                                                                }
                                                                                             } catch (Exception var320) {
                                                                                                var10000 = var320;
                                                                                                var10001 = false;
                                                                                                break label4401;
                                                                                             }

                                                                                             var686 = null;
                                                                                             var761 = false;
                                                                                             break label5737;
                                                                                          }
                                                                                       }

                                                                                       label5738: {
                                                                                          if (var35 == null) {
                                                                                             label3902: {
                                                                                                try {
                                                                                                   if (TextUtils.isEmpty(var729) || !var729.startsWith("http")) {
                                                                                                      break label3902;
                                                                                                   }
                                                                                                } catch (Exception var319) {
                                                                                                   var10000 = var319;
                                                                                                   var10001 = false;
                                                                                                   break label4401;
                                                                                                }

                                                                                                if (var26 != null) {
                                                                                                   try {
                                                                                                      var686 = new TLRPC.TL_inputMediaGifExternal();
                                                                                                      var6 = ((String)var26.get("url")).split("\\|");
                                                                                                      if (((Object[])var6).length == 2) {
                                                                                                         ((TLRPC.InputMedia)var686).url = (String)((Object[])var6)[0];
                                                                                                         ((TLRPC.InputMedia)var686).q = (String)((Object[])var6)[1];
                                                                                                      }
                                                                                                   } catch (Exception var318) {
                                                                                                      var10000 = var318;
                                                                                                      var10001 = false;
                                                                                                      break label4401;
                                                                                                   }

                                                                                                   var761 = true;
                                                                                                   break label5738;
                                                                                                }
                                                                                             }
                                                                                          }

                                                                                          try {
                                                                                             var686 = new TLRPC.TL_inputMediaUploadedDocument();
                                                                                          } catch (Exception var258) {
                                                                                             var10000 = var258;
                                                                                             var10001 = false;
                                                                                             break label5762;
                                                                                          }

                                                                                          if (var782 != 0) {
                                                                                             try {
                                                                                                ((TLRPC.InputMedia)var686).ttl_seconds = var782;
                                                                                                ((TLRPC.Message)var678).ttl = var782;
                                                                                                ((TLRPC.InputMedia)var686).flags |= 2;
                                                                                             } catch (Exception var252) {
                                                                                                var10000 = var252;
                                                                                                var10001 = false;
                                                                                                break label4401;
                                                                                             }
                                                                                          }

                                                                                          try {
                                                                                             var15 = TextUtils.isEmpty(var12);
                                                                                          } catch (Exception var257) {
                                                                                             var10000 = var257;
                                                                                             var10001 = false;
                                                                                             break label5762;
                                                                                          }

                                                                                          if (!var15) {
                                                                                             label5710: {
                                                                                                try {
                                                                                                   if (!var12.toLowerCase().endsWith("mp4")) {
                                                                                                      break label5710;
                                                                                                   }
                                                                                                } catch (Exception var317) {
                                                                                                   var10000 = var317;
                                                                                                   var10001 = false;
                                                                                                   break label4401;
                                                                                                }

                                                                                                if (var26 != null) {
                                                                                                   try {
                                                                                                      if (!var26.containsKey("forceDocument")) {
                                                                                                         break label5710;
                                                                                                      }
                                                                                                   } catch (Exception var316) {
                                                                                                      var10000 = var316;
                                                                                                      var10001 = false;
                                                                                                      break label4401;
                                                                                                   }
                                                                                                }

                                                                                                try {
                                                                                                   ((TLRPC.InputMedia)var686).nosound_video = true;
                                                                                                } catch (Exception var251) {
                                                                                                   var10000 = var251;
                                                                                                   var10001 = false;
                                                                                                   break label4401;
                                                                                                }
                                                                                             }
                                                                                          }

                                                                                          var761 = false;
                                                                                       }

                                                                                       try {
                                                                                          ((TLRPC.InputMedia)var686).mime_type = var7.mime_type;
                                                                                          ((TLRPC.InputMedia)var686).attributes = var7.attributes;
                                                                                       } catch (Exception var256) {
                                                                                          var10000 = var256;
                                                                                          var10001 = false;
                                                                                          break label5762;
                                                                                       }
                                                                                    }

                                                                                    try {
                                                                                       var27 = var7.access_hash;
                                                                                    } catch (Exception var255) {
                                                                                       var10000 = var255;
                                                                                       var10001 = false;
                                                                                       break label5762;
                                                                                    }

                                                                                    if (var27 == 0L) {
                                                                                       try {
                                                                                          var15 = var686 instanceof TLRPC.TL_inputMediaUploadedDocument;
                                                                                       } catch (Exception var250) {
                                                                                          var10000 = var250;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }

                                                                                       var6 = var686;
                                                                                    } else {
                                                                                       TLRPC.InputDocument var705;
                                                                                       TLRPC.TL_inputMediaDocument var722;
                                                                                       try {
                                                                                          var722 = new TLRPC.TL_inputMediaDocument();
                                                                                          TLRPC.TL_inputDocument var702 = new TLRPC.TL_inputDocument();
                                                                                          var722.id = var702;
                                                                                          var705 = var722.id;
                                                                                       } catch (Exception var254) {
                                                                                          var10000 = var254;
                                                                                          var10001 = false;
                                                                                          break label5762;
                                                                                       }

                                                                                       TLRPC.InputDocument var755;
                                                                                       try {
                                                                                          var705.id = var7.id;
                                                                                          var755 = var722.id;
                                                                                       } catch (Exception var64) {
                                                                                          var786 = var64;
                                                                                          break label4480;
                                                                                       }

                                                                                       var5 = var6;

                                                                                       try {
                                                                                          var755.access_hash = var7.access_hash;
                                                                                       } catch (Exception var311) {
                                                                                          var10000 = var311;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var6;

                                                                                       try {
                                                                                          var722.id.file_reference = var7.file_reference;
                                                                                       } catch (Exception var310) {
                                                                                          var10000 = var310;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var6;

                                                                                       label5643: {
                                                                                          try {
                                                                                             if (var722.id.file_reference != null) {
                                                                                                break label5643;
                                                                                             }
                                                                                          } catch (Exception var315) {
                                                                                             var10000 = var315;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }

                                                                                          var5 = var6;

                                                                                          try {
                                                                                             var722.id.file_reference = new byte[0];
                                                                                          } catch (Exception var309) {
                                                                                             var10000 = var309;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }
                                                                                       }

                                                                                       var15 = false;
                                                                                       var6 = var722;
                                                                                    }

                                                                                    if (!var761 && var686 != null) {
                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693 = new SendMessagesHelper.DelayedMessage;
                                                                                       } catch (Exception var308) {
                                                                                          var10000 = var308;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.<init>(var10);
                                                                                       } catch (Exception var307) {
                                                                                          var10000 = var307;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.originalPath = var729;
                                                                                       } catch (Exception var306) {
                                                                                          var10000 = var306;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.type = 2;
                                                                                       } catch (Exception var305) {
                                                                                          var10000 = var305;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.obj = (MessageObject)var682;
                                                                                       } catch (Exception var304) {
                                                                                          var10000 = var304;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       label5645: {
                                                                                          try {
                                                                                             if (var7.thumbs.isEmpty()) {
                                                                                                break label5645;
                                                                                             }
                                                                                          } catch (Exception var314) {
                                                                                             var10000 = var314;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }

                                                                                          var5 = var682;

                                                                                          try {
                                                                                             var693.photoSize = (TLRPC.PhotoSize)var7.thumbs.get(0);
                                                                                          } catch (Exception var303) {
                                                                                             var10000 = var303;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }

                                                                                          var5 = var682;

                                                                                          try {
                                                                                             var693.locationParent = var7;
                                                                                          } catch (Exception var302) {
                                                                                             var10000 = var302;
                                                                                             var10001 = false;
                                                                                             break label5618;
                                                                                          }
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.parentObject = var713;
                                                                                       } catch (Exception var301) {
                                                                                          var10000 = var301;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.inputUploadMedia = (TLRPC.InputMedia)var686;
                                                                                       } catch (Exception var300) {
                                                                                          var10000 = var300;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }

                                                                                       var5 = var682;

                                                                                       try {
                                                                                          var693.performMediaUpload = var15;
                                                                                       } catch (Exception var299) {
                                                                                          var10000 = var299;
                                                                                          var10001 = false;
                                                                                          break label5618;
                                                                                       }
                                                                                    }

                                                                                    var686 = var6;
                                                                                    var694 = var693;
                                                                                    var749 = var685;
                                                                                    break label5771;
                                                                                 }

                                                                                 if (var784 != 8) {
                                                                                    if (var784 == 10) {
                                                                                       try {
                                                                                          var686 = new TLRPC.TL_inputMediaPoll();
                                                                                          ((TLRPC.TL_inputMediaPoll)var686).poll = var731.poll;
                                                                                       } catch (Exception var246) {
                                                                                          var10000 = var246;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }
                                                                                    } else {
                                                                                       var686 = null;
                                                                                    }

                                                                                    var15 = false;
                                                                                    var694 = var693;
                                                                                    var749 = var685;
                                                                                    break label5771;
                                                                                 }

                                                                                 try {
                                                                                    var6 = new TLRPC.TL_inputMediaUploadedDocument();
                                                                                    ((TLRPC.InputMedia)var6).mime_type = var7.mime_type;
                                                                                    ((TLRPC.InputMedia)var6).attributes = var7.attributes;
                                                                                 } catch (Exception var245) {
                                                                                    var10000 = var245;
                                                                                    var10001 = false;
                                                                                    break label4401;
                                                                                 }

                                                                                 if (var782 != 0) {
                                                                                    try {
                                                                                       ((TLRPC.InputMedia)var6).ttl_seconds = var782;
                                                                                       ((TLRPC.Message)var678).ttl = var782;
                                                                                       ((TLRPC.InputMedia)var6).flags |= 2;
                                                                                    } catch (Exception var244) {
                                                                                       var10000 = var244;
                                                                                       var10001 = false;
                                                                                       break label4401;
                                                                                    }
                                                                                 }

                                                                                 label5637: {
                                                                                    label3962: {
                                                                                       try {
                                                                                          if (var7.access_hash != 0L) {
                                                                                             break label3962;
                                                                                          }
                                                                                       } catch (Exception var327) {
                                                                                          var10000 = var327;
                                                                                          var10001 = false;
                                                                                          break label4401;
                                                                                       }

                                                                                       var681 = var6;
                                                                                       var15 = true;
                                                                                       break label5637;
                                                                                    }

                                                                                    try {
                                                                                       var681 = new TLRPC.TL_inputMediaDocument();
                                                                                       TLRPC.TL_inputDocument var757 = new TLRPC.TL_inputDocument();
                                                                                       ((TLRPC.TL_inputMediaDocument)var681).id = var757;
                                                                                       ((TLRPC.TL_inputMediaDocument)var681).id.id = var7.id;
                                                                                       ((TLRPC.TL_inputMediaDocument)var681).id.access_hash = var7.access_hash;
                                                                                       ((TLRPC.TL_inputMediaDocument)var681).id.file_reference = var7.file_reference;
                                                                                       if (((TLRPC.TL_inputMediaDocument)var681).id.file_reference == null) {
                                                                                          ((TLRPC.TL_inputMediaDocument)var681).id.file_reference = new byte[0];
                                                                                       }
                                                                                    } catch (Exception var253) {
                                                                                       var10000 = var253;
                                                                                       var10001 = false;
                                                                                       break label4401;
                                                                                    }

                                                                                    var15 = false;
                                                                                 }

                                                                                 SendMessagesHelper.DelayedMessage var715;
                                                                                 try {
                                                                                    var715 = new SendMessagesHelper.DelayedMessage(var10);
                                                                                    var715.type = 3;
                                                                                    var715.obj = (MessageObject)var5;
                                                                                    var715.parentObject = var713;
                                                                                    var715.inputUploadMedia = (TLRPC.InputMedia)var6;
                                                                                    var715.performMediaUpload = var15;
                                                                                 } catch (Exception var243) {
                                                                                    var10000 = var243;
                                                                                    var10001 = false;
                                                                                    break label4401;
                                                                                 }

                                                                                 var5 = var681;
                                                                                 var693 = var715;
                                                                              }

                                                                              var694 = var693;
                                                                              var749 = var685;
                                                                              var686 = var5;
                                                                              break label5771;
                                                                           }

                                                                           var686 = var5;
                                                                           var694 = var693;
                                                                           var749 = var685;
                                                                        }
                                                                     }

                                                                     var728 = var678;
                                                                     if (var774 != null) {
                                                                        var5 = var6;

                                                                        TLRPC.TL_messages_sendBroadcast var732;
                                                                        try {
                                                                           var732 = new TLRPC.TL_messages_sendBroadcast;
                                                                        } catch (Exception var122) {
                                                                           var10000 = var122;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var732.<init>();
                                                                        } catch (Exception var121) {
                                                                           var10000 = var121;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        ArrayList var765;
                                                                        try {
                                                                           var765 = new ArrayList;
                                                                        } catch (Exception var120) {
                                                                           var10000 = var120;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var765.<init>();
                                                                        } catch (Exception var119) {
                                                                           var10000 = var119;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var20 = 0;

                                                                        while(true) {
                                                                           var5 = var682;

                                                                           try {
                                                                              if (var20 >= var774.size()) {
                                                                                 break;
                                                                              }
                                                                           } catch (Exception var123) {
                                                                              var10000 = var123;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var682;

                                                                           try {
                                                                              var765.add(Utilities.random.nextLong());
                                                                           } catch (Exception var118) {
                                                                              var10000 = var118;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           ++var20;
                                                                        }

                                                                        var5 = var682;

                                                                        try {
                                                                           var732.contacts = var774;
                                                                        } catch (Exception var117) {
                                                                           var10000 = var117;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var682;

                                                                        try {
                                                                           var732.media = (TLRPC.InputMedia)var686;
                                                                        } catch (Exception var116) {
                                                                           var10000 = var116;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var682;

                                                                        try {
                                                                           var732.random_id = var765;
                                                                        } catch (Exception var115) {
                                                                           var10000 = var115;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var682;

                                                                        try {
                                                                           var732.message = var749;
                                                                        } catch (Exception var114) {
                                                                           var10000 = var114;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        if (var694 != null) {
                                                                           var5 = var682;

                                                                           try {
                                                                              var694.sendRequest = var732;
                                                                           } catch (Exception var113) {
                                                                              var10000 = var113;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }
                                                                        }

                                                                        if (var16 == null) {
                                                                           var5 = var682;

                                                                           try {
                                                                              DataQuery.getInstance(this.currentAccount).cleanDraft(var10, false);
                                                                           } catch (Exception var112) {
                                                                              var10000 = var112;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }
                                                                        }

                                                                        var681 = var732;
                                                                     } else if (var40 != 0L) {
                                                                        var5 = var6;

                                                                        label5649: {
                                                                           label5650: {
                                                                              try {
                                                                                 if (var694.sendRequest != null) {
                                                                                    break label5650;
                                                                                 }
                                                                              } catch (Exception var127) {
                                                                                 var10000 = var127;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var681 = new TLRPC.TL_messages_sendMultiMedia;
                                                                              } catch (Exception var110) {
                                                                                 var10000 = var110;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var681.<init>();
                                                                              } catch (Exception var109) {
                                                                                 var10000 = var109;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 ((TLRPC.TL_messages_sendMultiMedia)var681).peer = var33;
                                                                              } catch (Exception var108) {
                                                                                 var10000 = var108;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              label5651: {
                                                                                 try {
                                                                                    if (!(((TLRPC.Message)var728).to_id instanceof TLRPC.TL_peerChannel)) {
                                                                                       break label5651;
                                                                                    }
                                                                                 } catch (Exception var126) {
                                                                                    var10000 = var126;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 SharedPreferences var766;
                                                                                 try {
                                                                                    var766 = MessagesController.getNotificationsSettings(this.currentAccount);
                                                                                 } catch (Exception var107) {
                                                                                    var10000 = var107;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 StringBuilder var725;
                                                                                 try {
                                                                                    var725 = new StringBuilder;
                                                                                 } catch (Exception var106) {
                                                                                    var10000 = var106;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    var725.<init>();
                                                                                 } catch (Exception var105) {
                                                                                    var10000 = var105;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    var725.append("silent_");
                                                                                 } catch (Exception var104) {
                                                                                    var10000 = var104;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    var725.append(var10);
                                                                                 } catch (Exception var103) {
                                                                                    var10000 = var103;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    ((TLRPC.TL_messages_sendMultiMedia)var681).silent = var766.getBoolean(var725.toString(), false);
                                                                                 } catch (Exception var102) {
                                                                                    var10000 = var102;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }
                                                                              }

                                                                              var5 = var6;

                                                                              label5652: {
                                                                                 try {
                                                                                    if (((TLRPC.Message)var728).reply_to_msg_id == 0) {
                                                                                       break label5652;
                                                                                    }
                                                                                 } catch (Exception var125) {
                                                                                    var10000 = var125;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    ((TLRPC.TL_messages_sendMultiMedia)var681).flags |= 1;
                                                                                 } catch (Exception var101) {
                                                                                    var10000 = var101;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }

                                                                                 var5 = var6;

                                                                                 try {
                                                                                    ((TLRPC.TL_messages_sendMultiMedia)var681).reply_to_msg_id = ((TLRPC.Message)var728).reply_to_msg_id;
                                                                                 } catch (Exception var100) {
                                                                                    var10000 = var100;
                                                                                    var10001 = false;
                                                                                    break label5618;
                                                                                 }
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var694.sendRequest = (TLObject)var681;
                                                                                 break label5649;
                                                                              } catch (Exception var99) {
                                                                                 var10000 = var99;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var681 = (TLRPC.TL_messages_sendMultiMedia)var694.sendRequest;
                                                                           } catch (Exception var111) {
                                                                              var10000 = var111;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.messageObjects.add(var682);
                                                                        } catch (Exception var98) {
                                                                           var10000 = var98;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.parentObjects.add(var21);
                                                                        } catch (Exception var97) {
                                                                           var10000 = var97;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.locations.add(var694.photoSize);
                                                                        } catch (Exception var96) {
                                                                           var10000 = var96;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.videoEditedInfos.add(var694.videoEditedInfo);
                                                                        } catch (Exception var95) {
                                                                           var10000 = var95;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.httpLocations.add(var694.httpLocation);
                                                                        } catch (Exception var94) {
                                                                           var10000 = var94;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.inputMedias.add(var694.inputUploadMedia);
                                                                        } catch (Exception var93) {
                                                                           var10000 = var93;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.messages.add(var728);
                                                                        } catch (Exception var92) {
                                                                           var10000 = var92;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var694.originalPaths.add(var746);
                                                                        } catch (Exception var91) {
                                                                           var10000 = var91;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        TLRPC.TL_inputSingleMedia var769;
                                                                        try {
                                                                           var769 = new TLRPC.TL_inputSingleMedia;
                                                                        } catch (Exception var90) {
                                                                           var10000 = var90;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var769.<init>();
                                                                        } catch (Exception var89) {
                                                                           var10000 = var89;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var769.random_id = ((TLRPC.Message)var728).random_id;
                                                                        } catch (Exception var88) {
                                                                           var10000 = var88;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var769.media = (TLRPC.InputMedia)var686;
                                                                        } catch (Exception var87) {
                                                                           var10000 = var87;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var769.message = var29;
                                                                        } catch (Exception var86) {
                                                                           var10000 = var86;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        if (var17 != null) {
                                                                           label5714: {
                                                                              var5 = var6;

                                                                              try {
                                                                                 if (var17.isEmpty()) {
                                                                                    break label5714;
                                                                                 }
                                                                              } catch (Exception var124) {
                                                                                 var10000 = var124;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var769.entities = var17;
                                                                              } catch (Exception var85) {
                                                                                 var10000 = var85;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var769.flags |= 1;
                                                                              } catch (Exception var84) {
                                                                                 var10000 = var84;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }
                                                                           }
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           ((TLRPC.TL_messages_sendMultiMedia)var681).multi_media.add(var769);
                                                                        } catch (Exception var83) {
                                                                           var10000 = var83;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }
                                                                     } else {
                                                                        var5 = var6;

                                                                        TLRPC.TL_messages_sendMedia var771;
                                                                        try {
                                                                           var771 = new TLRPC.TL_messages_sendMedia;
                                                                        } catch (Exception var82) {
                                                                           var10000 = var82;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var771.<init>();
                                                                        } catch (Exception var81) {
                                                                           var10000 = var81;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var771.peer = var33;
                                                                        } catch (Exception var80) {
                                                                           var10000 = var80;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        label5655: {
                                                                           try {
                                                                              if (!(((TLRPC.Message)var728).to_id instanceof TLRPC.TL_peerChannel)) {
                                                                                 break label5655;
                                                                              }
                                                                           } catch (Exception var130) {
                                                                              var10000 = var130;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           SharedPreferences var726;
                                                                           try {
                                                                              var726 = MessagesController.getNotificationsSettings(this.currentAccount);
                                                                           } catch (Exception var79) {
                                                                              var10000 = var79;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           StringBuilder var778;
                                                                           try {
                                                                              var778 = new StringBuilder;
                                                                           } catch (Exception var78) {
                                                                              var10000 = var78;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var778.<init>();
                                                                           } catch (Exception var77) {
                                                                              var10000 = var77;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var778.append("silent_");
                                                                           } catch (Exception var76) {
                                                                              var10000 = var76;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var778.append(var10);
                                                                           } catch (Exception var75) {
                                                                              var10000 = var75;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var771.silent = var726.getBoolean(var778.toString(), false);
                                                                           } catch (Exception var74) {
                                                                              var10000 = var74;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }
                                                                        }

                                                                        var5 = var6;

                                                                        label5656: {
                                                                           try {
                                                                              if (((TLRPC.Message)var728).reply_to_msg_id == 0) {
                                                                                 break label5656;
                                                                              }
                                                                           } catch (Exception var129) {
                                                                              var10000 = var129;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var771.flags |= 1;
                                                                           } catch (Exception var73) {
                                                                              var10000 = var73;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              var771.reply_to_msg_id = ((TLRPC.Message)var728).reply_to_msg_id;
                                                                           } catch (Exception var72) {
                                                                              var10000 = var72;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var771.random_id = ((TLRPC.Message)var728).random_id;
                                                                        } catch (Exception var71) {
                                                                           var10000 = var71;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var771.media = (TLRPC.InputMedia)var686;
                                                                        } catch (Exception var70) {
                                                                           var10000 = var70;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var771.message = var29;
                                                                        } catch (Exception var69) {
                                                                           var10000 = var69;
                                                                           var10001 = false;
                                                                           break label5618;
                                                                        }

                                                                        if (var17 != null) {
                                                                           label5715: {
                                                                              var5 = var6;

                                                                              try {
                                                                                 if (var17.isEmpty()) {
                                                                                    break label5715;
                                                                                 }
                                                                              } catch (Exception var128) {
                                                                                 var10000 = var128;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var771.entities = var17;
                                                                              } catch (Exception var68) {
                                                                                 var10000 = var68;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }

                                                                              var5 = var6;

                                                                              try {
                                                                                 var771.flags |= 8;
                                                                              } catch (Exception var67) {
                                                                                 var10000 = var67;
                                                                                 var10001 = false;
                                                                                 break label5618;
                                                                              }
                                                                           }
                                                                        }

                                                                        var681 = var771;
                                                                        if (var694 != null) {
                                                                           var5 = var6;

                                                                           try {
                                                                              var694.sendRequest = var771;
                                                                           } catch (Exception var66) {
                                                                              var10000 = var66;
                                                                              var10001 = false;
                                                                              break label5618;
                                                                           }

                                                                           var681 = var771;
                                                                        }
                                                                     }

                                                                     if (var40 != 0L) {
                                                                        var5 = var682;

                                                                        try {
                                                                           this.performSendDelayedMessage(var694);
                                                                           return;
                                                                        } catch (Exception var46) {
                                                                           var10000 = var46;
                                                                           var10001 = false;
                                                                        }
                                                                     } else if (var784 == 1) {
                                                                        var5 = var682;

                                                                        try {
                                                                           this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, (String)null, var694, var21);
                                                                           return;
                                                                        } catch (Exception var47) {
                                                                           var10000 = var47;
                                                                           var10001 = false;
                                                                        }
                                                                     } else if (var784 == 2) {
                                                                        if (var15) {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendDelayedMessage(var694);
                                                                              return;
                                                                           } catch (Exception var48) {
                                                                              var10000 = var48;
                                                                              var10001 = false;
                                                                           }
                                                                        } else {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, (SendMessagesHelper.DelayedMessage)null, true, var694, var21);
                                                                              return;
                                                                           } catch (Exception var49) {
                                                                              var10000 = var49;
                                                                              var10001 = false;
                                                                           }
                                                                        }
                                                                     } else if (var784 == 3) {
                                                                        if (var15) {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendDelayedMessage(var694);
                                                                              return;
                                                                           } catch (Exception var50) {
                                                                              var10000 = var50;
                                                                              var10001 = false;
                                                                           }
                                                                        } else {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, var694, var21);
                                                                              return;
                                                                           } catch (Exception var51) {
                                                                              var10000 = var51;
                                                                              var10001 = false;
                                                                           }
                                                                        }
                                                                     } else if (var784 == 6) {
                                                                        var5 = var682;

                                                                        try {
                                                                           this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, var694, var21);
                                                                           return;
                                                                        } catch (Exception var52) {
                                                                           var10000 = var52;
                                                                           var10001 = false;
                                                                        }
                                                                     } else if (var784 == 7) {
                                                                        if (var15 && var694 != null) {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendDelayedMessage(var694);
                                                                              return;
                                                                           } catch (Exception var53) {
                                                                              var10000 = var53;
                                                                              var10001 = false;
                                                                           }
                                                                        } else {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, var694, var21);
                                                                              return;
                                                                           } catch (Exception var54) {
                                                                              var10000 = var54;
                                                                              var10001 = false;
                                                                           }
                                                                        }
                                                                     } else if (var784 == 8) {
                                                                        if (var15) {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendDelayedMessage(var694);
                                                                              return;
                                                                           } catch (Exception var55) {
                                                                              var10000 = var55;
                                                                              var10001 = false;
                                                                           }
                                                                        } else {
                                                                           var5 = var682;

                                                                           try {
                                                                              this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, var694, var21);
                                                                              return;
                                                                           } catch (Exception var56) {
                                                                              var10000 = var56;
                                                                              var10001 = false;
                                                                           }
                                                                        }
                                                                     } else {
                                                                        if (var784 != 10) {
                                                                           return;
                                                                        }

                                                                        var5 = var682;

                                                                        try {
                                                                           this.performSendMessageRequest((TLObject)var681, (MessageObject)var682, var746, var694, var21);
                                                                           return;
                                                                        } catch (Exception var57) {
                                                                           var10000 = var57;
                                                                           var10001 = false;
                                                                        }
                                                                     }
                                                                  }
                                                               } else {
                                                                  label5716: {
                                                                     var14 = var678;
                                                                     var6 = var6;

                                                                     try {
                                                                        var20 = AndroidUtilities.getPeerLayerVersion(var35.layer);
                                                                     } catch (Exception var406) {
                                                                        var10000 = var406;
                                                                        var10001 = false;
                                                                        break label5733;
                                                                     }

                                                                     if (var20 >= 73) {
                                                                        var5 = var6;

                                                                        try {
                                                                           var699 = new TLRPC.TL_decryptedMessage;
                                                                        } catch (Exception var416) {
                                                                           var10000 = var416;
                                                                           var10001 = false;
                                                                           break label5716;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           var699.<init>();
                                                                        } catch (Exception var415) {
                                                                           var10000 = var415;
                                                                           var10001 = false;
                                                                           break label5716;
                                                                        }

                                                                        if (var40 != 0L) {
                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).grouped_id = var40;
                                                                           } catch (Exception var414) {
                                                                              var10000 = var414;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).flags |= 131072;
                                                                           } catch (Exception var413) {
                                                                              var10000 = var413;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }
                                                                        }
                                                                     } else {
                                                                        try {
                                                                           var699 = new TLRPC.TL_decryptedMessage_layer45();
                                                                        } catch (Exception var405) {
                                                                           var10000 = var405;
                                                                           var10001 = false;
                                                                           break label5733;
                                                                        }
                                                                     }

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var699).ttl = ((TLRPC.Message)var14).ttl;
                                                                     } catch (Exception var404) {
                                                                        var10000 = var404;
                                                                        var10001 = false;
                                                                        break label5733;
                                                                     }

                                                                     if (var17 != null) {
                                                                        label5663: {
                                                                           var5 = var6;

                                                                           try {
                                                                              if (var17.isEmpty()) {
                                                                                 break label5663;
                                                                              }
                                                                           } catch (Exception var418) {
                                                                              var10000 = var418;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).entities = var17;
                                                                           } catch (Exception var412) {
                                                                              var10000 = var412;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).flags |= 128;
                                                                           } catch (Exception var411) {
                                                                              var10000 = var411;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }
                                                                        }
                                                                     }

                                                                     long var42;
                                                                     try {
                                                                        var42 = ((TLRPC.Message)var14).reply_to_random_id;
                                                                     } catch (Exception var403) {
                                                                        var10000 = var403;
                                                                        var10001 = false;
                                                                        break label5733;
                                                                     }

                                                                     if (var42 != 0L) {
                                                                        var5 = var6;

                                                                        try {
                                                                           ((TLRPC.DecryptedMessage)var699).reply_to_random_id = ((TLRPC.Message)var14).reply_to_random_id;
                                                                        } catch (Exception var410) {
                                                                           var10000 = var410;
                                                                           var10001 = false;
                                                                           break label5716;
                                                                        }

                                                                        var5 = var6;

                                                                        try {
                                                                           ((TLRPC.DecryptedMessage)var699).flags |= 8;
                                                                        } catch (Exception var409) {
                                                                           var10000 = var409;
                                                                           var10001 = false;
                                                                           break label5716;
                                                                        }
                                                                     }

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var699).flags |= 512;
                                                                     } catch (Exception var402) {
                                                                        var10000 = var402;
                                                                        var10001 = false;
                                                                        break label5733;
                                                                     }

                                                                     if (var26 != null) {
                                                                        label5665: {
                                                                           var5 = var6;

                                                                           try {
                                                                              if (var26.get("bot_name") == null) {
                                                                                 break label5665;
                                                                              }
                                                                           } catch (Exception var417) {
                                                                              var10000 = var417;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).via_bot_name = (String)var26.get("bot_name");
                                                                           } catch (Exception var408) {
                                                                              var10000 = var408;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }

                                                                           var5 = var6;

                                                                           try {
                                                                              ((TLRPC.DecryptedMessage)var699).flags |= 2048;
                                                                           } catch (Exception var407) {
                                                                              var10000 = var407;
                                                                              var10001 = false;
                                                                              break label5716;
                                                                           }
                                                                        }
                                                                     }

                                                                     try {
                                                                        ((TLRPC.DecryptedMessage)var699).random_id = ((TLRPC.Message)var14).random_id;
                                                                        ((TLRPC.DecryptedMessage)var699).message = var685;
                                                                     } catch (Exception var401) {
                                                                        var10000 = var401;
                                                                        var10001 = false;
                                                                        break label5733;
                                                                     }

                                                                     label4313: {
                                                                        label4312: {
                                                                           label5666: {
                                                                              SendMessagesHelper.DelayedMessage var692;
                                                                              TLRPC.TL_messages_sendEncryptedMultiMedia var706;
                                                                              label4310: {
                                                                                 label4309: {
                                                                                    label5772: {
                                                                                       label5742: {
                                                                                          if (var784 == 1) {
                                                                                             var5 = var6;

                                                                                             label5671: {
                                                                                                label5672: {
                                                                                                   try {
                                                                                                      if (!(var701 instanceof TLRPC.TL_messageMediaVenue)) {
                                                                                                         break label5672;
                                                                                                      }
                                                                                                   } catch (Exception var398) {
                                                                                                      var10000 = var398;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   TLRPC.TL_decryptedMessageMediaVenue var718;
                                                                                                   try {
                                                                                                      var718 = new TLRPC.TL_decryptedMessageMediaVenue;
                                                                                                   } catch (Exception var344) {
                                                                                                      var10000 = var344;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      var718.<init>();
                                                                                                   } catch (Exception var343) {
                                                                                                      var10000 = var343;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media = var718;
                                                                                                   } catch (Exception var342) {
                                                                                                      var10000 = var342;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.address = var701.address;
                                                                                                   } catch (Exception var341) {
                                                                                                      var10000 = var341;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.title = var701.title;
                                                                                                   } catch (Exception var340) {
                                                                                                      var10000 = var340;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.provider = var701.provider;
                                                                                                   } catch (Exception var339) {
                                                                                                      var10000 = var339;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }

                                                                                                   var5 = var6;

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.venue_id = var701.venue_id;
                                                                                                      break label5671;
                                                                                                   } catch (Exception var338) {
                                                                                                      var10000 = var338;
                                                                                                      var10001 = false;
                                                                                                      break label5716;
                                                                                                   }
                                                                                                }

                                                                                                var5 = var6;

                                                                                                TLRPC.TL_decryptedMessageMediaGeoPoint var724;
                                                                                                try {
                                                                                                   var724 = new TLRPC.TL_decryptedMessageMediaGeoPoint;
                                                                                                } catch (Exception var337) {
                                                                                                   var10000 = var337;
                                                                                                   var10001 = false;
                                                                                                   break label5716;
                                                                                                }

                                                                                                var5 = var6;

                                                                                                try {
                                                                                                   var724.<init>();
                                                                                                } catch (Exception var336) {
                                                                                                   var10000 = var336;
                                                                                                   var10001 = false;
                                                                                                   break label5716;
                                                                                                }

                                                                                                var5 = var6;

                                                                                                try {
                                                                                                   ((TLRPC.DecryptedMessage)var699).media = var724;
                                                                                                } catch (Exception var335) {
                                                                                                   var10000 = var335;
                                                                                                   var10001 = false;
                                                                                                   break label5716;
                                                                                                }
                                                                                             }

                                                                                             var5 = var6;

                                                                                             try {
                                                                                                ((TLRPC.DecryptedMessage)var699).media.lat = var701.geo.lat;
                                                                                             } catch (Exception var334) {
                                                                                                var10000 = var334;
                                                                                                var10001 = false;
                                                                                                break label5716;
                                                                                             }

                                                                                             var5 = var6;

                                                                                             try {
                                                                                                ((TLRPC.DecryptedMessage)var699).media._long = var701.geo._long;
                                                                                             } catch (Exception var333) {
                                                                                                var10000 = var333;
                                                                                                var10001 = false;
                                                                                                break label5716;
                                                                                             }

                                                                                             var5 = var6;

                                                                                             try {
                                                                                                SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)var6);
                                                                                             } catch (Exception var332) {
                                                                                                var10000 = var332;
                                                                                                var10001 = false;
                                                                                                break label5716;
                                                                                             }
                                                                                          } else {
                                                                                             TLRPC.PhotoSize var697;
                                                                                             TLRPC.PhotoSize var710;
                                                                                             if (var784 == 2 || var784 == 9 && var747 != null) {
                                                                                                byte[] var703;
                                                                                                try {
                                                                                                   var710 = (TLRPC.PhotoSize)var747.sizes.get(0);
                                                                                                   var697 = (TLRPC.PhotoSize)var747.sizes.get(var747.sizes.size() - 1);
                                                                                                   ImageLoader.fillPhotoSizeWithBytes(var710);
                                                                                                   TLRPC.TL_decryptedMessageMediaPhoto var700 = new TLRPC.TL_decryptedMessageMediaPhoto();
                                                                                                   ((TLRPC.DecryptedMessage)var699).media = var700;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.caption = var29;
                                                                                                   var703 = var710.bytes;
                                                                                                } catch (Exception var353) {
                                                                                                   var10000 = var353;
                                                                                                   var10001 = false;
                                                                                                   break label5666;
                                                                                                }

                                                                                                if (var703 != null) {
                                                                                                   try {
                                                                                                      ((TLRPC.TL_decryptedMessageMediaPhoto)((TLRPC.DecryptedMessage)var699).media).thumb = var710.bytes;
                                                                                                   } catch (Exception var362) {
                                                                                                      var10000 = var362;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }
                                                                                                } else {
                                                                                                   try {
                                                                                                      ((TLRPC.TL_decryptedMessageMediaPhoto)((TLRPC.DecryptedMessage)var699).media).thumb = new byte[0];
                                                                                                   } catch (Exception var352) {
                                                                                                      var10000 = var352;
                                                                                                      var10001 = false;
                                                                                                      break label5666;
                                                                                                   }
                                                                                                }

                                                                                                byte[] var733;
                                                                                                try {
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.thumb_h = var710.h;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.thumb_w = var710.w;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.w = var697.w;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.h = var697.h;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.size = var697.size;
                                                                                                   var733 = var697.location.key;
                                                                                                } catch (Exception var351) {
                                                                                                   var10000 = var351;
                                                                                                   var10001 = false;
                                                                                                   break label5666;
                                                                                                }

                                                                                                if (var733 != null && var40 == 0L) {
                                                                                                   try {
                                                                                                      TLRPC.TL_inputEncryptedFile var735 = new TLRPC.TL_inputEncryptedFile();
                                                                                                      var735.id = var697.location.volume_id;
                                                                                                      var735.access_hash = var697.location.secret;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.key = var697.location.key;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.iv = var697.location.iv;
                                                                                                      SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, var735, (String)null, (MessageObject)var6);
                                                                                                   } catch (Exception var361) {
                                                                                                      var10000 = var361;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   var692 = var693;
                                                                                                } else {
                                                                                                   if (var693 == null) {
                                                                                                      try {
                                                                                                         var693 = new SendMessagesHelper.DelayedMessage(var10);
                                                                                                         var693.encryptedChat = var35;
                                                                                                         var693.type = 0;
                                                                                                         var693.originalPath = var746;
                                                                                                         var693.sendEncryptedRequest = (TLObject)var699;
                                                                                                         var693.obj = (MessageObject)var6;
                                                                                                      } catch (Exception var360) {
                                                                                                         var10000 = var360;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      label4248: {
                                                                                                         if (var26 != null) {
                                                                                                            try {
                                                                                                               if (var26.containsKey("parentObject")) {
                                                                                                                  var693.parentObject = var26.get("parentObject");
                                                                                                                  break label4248;
                                                                                                               }
                                                                                                            } catch (Exception var397) {
                                                                                                               var10000 = var397;
                                                                                                               var10001 = false;
                                                                                                               break label5772;
                                                                                                            }
                                                                                                         }

                                                                                                         try {
                                                                                                            var693.parentObject = var21;
                                                                                                         } catch (Exception var359) {
                                                                                                            var10000 = var359;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         var693.performMediaUpload = true;
                                                                                                      } catch (Exception var358) {
                                                                                                         var10000 = var358;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      var15 = TextUtils.isEmpty(var12);
                                                                                                   } catch (Exception var350) {
                                                                                                      var10000 = var350;
                                                                                                      var10001 = false;
                                                                                                      break label5666;
                                                                                                   }

                                                                                                   label4237: {
                                                                                                      if (!var15) {
                                                                                                         try {
                                                                                                            if (var12.startsWith("http")) {
                                                                                                               var693.httpLocation = var12;
                                                                                                               break label4237;
                                                                                                            }
                                                                                                         } catch (Exception var396) {
                                                                                                            var10000 = var396;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         var693.photoSize = (TLRPC.PhotoSize)var747.sizes.get(var747.sizes.size() - 1);
                                                                                                         var693.locationParent = var747;
                                                                                                      } catch (Exception var349) {
                                                                                                         var10000 = var349;
                                                                                                         var10001 = false;
                                                                                                         break label5666;
                                                                                                      }
                                                                                                   }

                                                                                                   if (var40 == 0L) {
                                                                                                      try {
                                                                                                         this.performSendDelayedMessage(var693);
                                                                                                      } catch (Exception var357) {
                                                                                                         var10000 = var357;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   var692 = var693;
                                                                                                }
                                                                                                break label5742;
                                                                                             }

                                                                                             TLRPC.TL_inputEncryptedFile var690;
                                                                                             TLRPC.TL_decryptedMessageMediaDocument var708;
                                                                                             if (var784 == 3) {
                                                                                                label5688: {
                                                                                                   label5743: {
                                                                                                      try {
                                                                                                         var697 = this.getThumbForSecretChat(var7.thumbs);
                                                                                                         ImageLoader.fillPhotoSizeWithBytes(var697);
                                                                                                         if (MessageObject.isNewGifDocument((TLRPC.Document)var7) || MessageObject.isRoundVideoDocument(var7)) {
                                                                                                            break label5743;
                                                                                                         }
                                                                                                      } catch (Exception var394) {
                                                                                                         var10000 = var394;
                                                                                                         var10001 = false;
                                                                                                         break label4309;
                                                                                                      }

                                                                                                      TLRPC.TL_decryptedMessageMediaVideo var719;
                                                                                                      try {
                                                                                                         var719 = new TLRPC.TL_decryptedMessageMediaVideo();
                                                                                                         ((TLRPC.DecryptedMessage)var699).media = var719;
                                                                                                      } catch (Exception var356) {
                                                                                                         var10000 = var356;
                                                                                                         var10001 = false;
                                                                                                         break label4309;
                                                                                                      }

                                                                                                      if (var697 != null) {
                                                                                                         label5718: {
                                                                                                            var5 = var6;

                                                                                                            try {
                                                                                                               if (var697.bytes == null) {
                                                                                                                  break label5718;
                                                                                                               }
                                                                                                            } catch (Exception var392) {
                                                                                                               var10000 = var392;
                                                                                                               var10001 = false;
                                                                                                               break label5716;
                                                                                                            }

                                                                                                            var5 = var6;

                                                                                                            try {
                                                                                                               ((TLRPC.TL_decryptedMessageMediaVideo)((TLRPC.DecryptedMessage)var699).media).thumb = var697.bytes;
                                                                                                               break label5688;
                                                                                                            } catch (Exception var345) {
                                                                                                               var10000 = var345;
                                                                                                               var10001 = false;
                                                                                                               break label5716;
                                                                                                            }
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         var719 = (TLRPC.TL_decryptedMessageMediaVideo)((TLRPC.DecryptedMessage)var699).media;
                                                                                                      } catch (Exception var355) {
                                                                                                         var10000 = var355;
                                                                                                         var10001 = false;
                                                                                                         break label4309;
                                                                                                      }

                                                                                                      try {
                                                                                                         var719.thumb = new byte[0];
                                                                                                         break label5688;
                                                                                                      } catch (Exception var388) {
                                                                                                         var10000 = var388;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      var708 = new TLRPC.TL_decryptedMessageMediaDocument();
                                                                                                      ((TLRPC.DecryptedMessage)var699).media = var708;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.attributes = var7.attributes;
                                                                                                   } catch (Exception var387) {
                                                                                                      var10000 = var387;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   if (var697 != null) {
                                                                                                      try {
                                                                                                         if (var697.bytes != null) {
                                                                                                            ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = var697.bytes;
                                                                                                            break label5688;
                                                                                                         }
                                                                                                      } catch (Exception var393) {
                                                                                                         var10000 = var393;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = new byte[0];
                                                                                                   } catch (Exception var386) {
                                                                                                      var10000 = var386;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }
                                                                                                }

                                                                                                try {
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.caption = var29;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.mime_type = "video/mp4";
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.size = var7.size;
                                                                                                } catch (Exception var385) {
                                                                                                   var10000 = var385;
                                                                                                   var10001 = false;
                                                                                                   break label5772;
                                                                                                }

                                                                                                var20 = 0;

                                                                                                while(true) {
                                                                                                   try {
                                                                                                      if (var20 >= var7.attributes.size()) {
                                                                                                         break;
                                                                                                      }

                                                                                                      TLRPC.DocumentAttribute var730 = (TLRPC.DocumentAttribute)var7.attributes.get(var20);
                                                                                                      if (var730 instanceof TLRPC.TL_documentAttributeVideo) {
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.w = var730.w;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.h = var730.h;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.duration = var730.duration;
                                                                                                         break;
                                                                                                      }
                                                                                                   } catch (Exception var391) {
                                                                                                      var10000 = var391;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   ++var20;
                                                                                                }

                                                                                                label4181: {
                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.thumb_h = var697.h;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.thumb_w = var697.w;
                                                                                                      if (var7.key == null) {
                                                                                                         break label4181;
                                                                                                      }
                                                                                                   } catch (Exception var390) {
                                                                                                      var10000 = var390;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   if (var40 == 0L) {
                                                                                                      try {
                                                                                                         var690 = new TLRPC.TL_inputEncryptedFile();
                                                                                                         var690.id = var7.id;
                                                                                                         var690.access_hash = var7.access_hash;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.key = var7.key;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.iv = var7.iv;
                                                                                                         SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, var690, (String)null, (MessageObject)var6);
                                                                                                      } catch (Exception var384) {
                                                                                                         var10000 = var384;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      var692 = var693;
                                                                                                      break label5742;
                                                                                                   }
                                                                                                }

                                                                                                if (var693 == null) {
                                                                                                   try {
                                                                                                      var693 = new SendMessagesHelper.DelayedMessage(var10);
                                                                                                      var693.encryptedChat = var35;
                                                                                                      var693.type = 1;
                                                                                                      var693.sendEncryptedRequest = (TLObject)var699;
                                                                                                      var693.originalPath = var746;
                                                                                                      var693.obj = (MessageObject)var6;
                                                                                                   } catch (Exception var383) {
                                                                                                      var10000 = var383;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   label5744: {
                                                                                                      if (var26 != null) {
                                                                                                         try {
                                                                                                            if (var26.containsKey("parentObject")) {
                                                                                                               var693.parentObject = var26.get("parentObject");
                                                                                                               break label5744;
                                                                                                            }
                                                                                                         } catch (Exception var389) {
                                                                                                            var10000 = var389;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         var693.parentObject = var21;
                                                                                                      } catch (Exception var382) {
                                                                                                         var10000 = var382;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      var693.performMediaUpload = true;
                                                                                                   } catch (Exception var381) {
                                                                                                      var10000 = var381;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }
                                                                                                }

                                                                                                try {
                                                                                                   var693.videoEditedInfo = (VideoEditedInfo)var727;
                                                                                                } catch (Exception var380) {
                                                                                                   var10000 = var380;
                                                                                                   var10001 = false;
                                                                                                   break label5772;
                                                                                                }

                                                                                                if (var40 == 0L) {
                                                                                                   try {
                                                                                                      this.performSendDelayedMessage(var693);
                                                                                                   } catch (Exception var379) {
                                                                                                      var10000 = var379;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }
                                                                                                }

                                                                                                var692 = var693;
                                                                                                break label5742;
                                                                                             }

                                                                                             var42 = var10;
                                                                                             var685 = var746;
                                                                                             if (var784 == 6) {
                                                                                                try {
                                                                                                   TLRPC.TL_decryptedMessageMediaContact var704 = new TLRPC.TL_decryptedMessageMediaContact();
                                                                                                   ((TLRPC.DecryptedMessage)var699).media = var704;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.phone_number = ((TLRPC.User)var767).phone;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.first_name = ((TLRPC.User)var767).first_name;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.last_name = ((TLRPC.User)var767).last_name;
                                                                                                   ((TLRPC.DecryptedMessage)var699).media.user_id = ((TLRPC.User)var767).id;
                                                                                                   SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)var6);
                                                                                                } catch (Exception var374) {
                                                                                                   var10000 = var374;
                                                                                                   var10001 = false;
                                                                                                   break label5772;
                                                                                                }
                                                                                             } else if (var784 != 7 && (var784 != 9 || var7 == null)) {
                                                                                                if (var784 == 8) {
                                                                                                   try {
                                                                                                      var693 = new SendMessagesHelper.DelayedMessage(var42);
                                                                                                      var693.encryptedChat = var35;
                                                                                                      var693.sendEncryptedRequest = (TLObject)var699;
                                                                                                      var693.obj = (MessageObject)var6;
                                                                                                      var693.type = 3;
                                                                                                      var693.parentObject = var21;
                                                                                                      var693.performMediaUpload = true;
                                                                                                      var708 = new TLRPC.TL_decryptedMessageMediaDocument();
                                                                                                      ((TLRPC.DecryptedMessage)var699).media = var708;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.attributes = var7.attributes;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.caption = var29;
                                                                                                      var710 = this.getThumbForSecretChat(var7.thumbs);
                                                                                                   } catch (Exception var378) {
                                                                                                      var10000 = var378;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   if (var710 != null) {
                                                                                                      try {
                                                                                                         ImageLoader.fillPhotoSizeWithBytes(var710);
                                                                                                         ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = var710.bytes;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.thumb_h = var710.h;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.thumb_w = var710.w;
                                                                                                      } catch (Exception var377) {
                                                                                                         var10000 = var377;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   } else {
                                                                                                      try {
                                                                                                         ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = new byte[0];
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.thumb_h = 0;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.thumb_w = 0;
                                                                                                      } catch (Exception var376) {
                                                                                                         var10000 = var376;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.mime_type = var7.mime_type;
                                                                                                      ((TLRPC.DecryptedMessage)var699).media.size = var7.size;
                                                                                                      var693.originalPath = var685;
                                                                                                      this.performSendDelayedMessage(var693);
                                                                                                   } catch (Exception var375) {
                                                                                                      var10000 = var375;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }

                                                                                                   var692 = var693;
                                                                                                   break label5742;
                                                                                                }
                                                                                             } else {
                                                                                                label5758: {
                                                                                                   TLRPC.TL_decryptedMessageMediaExternalDocument var695;
                                                                                                   label5682: {
                                                                                                      try {
                                                                                                         if (MessageObject.isStickerDocument(var7)) {
                                                                                                            var695 = new TLRPC.TL_decryptedMessageMediaExternalDocument();
                                                                                                            ((TLRPC.DecryptedMessage)var699).media = var695;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.id = var7.id;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.date = var7.date;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.access_hash = var7.access_hash;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.mime_type = var7.mime_type;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.size = var7.size;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.dc_id = var7.dc_id;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.attributes = var7.attributes;
                                                                                                            var697 = this.getThumbForSecretChat(var7.thumbs);
                                                                                                            break label5682;
                                                                                                         }
                                                                                                      } catch (Exception var400) {
                                                                                                         var10000 = var400;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      try {
                                                                                                         var708 = new TLRPC.TL_decryptedMessageMediaDocument();
                                                                                                         ((TLRPC.DecryptedMessage)var699).media = var708;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.attributes = var7.attributes;
                                                                                                         ((TLRPC.DecryptedMessage)var699).media.caption = var29;
                                                                                                         var710 = this.getThumbForSecretChat(var7.thumbs);
                                                                                                      } catch (Exception var370) {
                                                                                                         var10000 = var370;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      if (var710 != null) {
                                                                                                         try {
                                                                                                            ImageLoader.fillPhotoSizeWithBytes(var710);
                                                                                                            ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = var710.bytes;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.thumb_h = var710.h;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.thumb_w = var710.w;
                                                                                                         } catch (Exception var369) {
                                                                                                            var10000 = var369;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      } else {
                                                                                                         try {
                                                                                                            ((TLRPC.TL_decryptedMessageMediaDocument)((TLRPC.DecryptedMessage)var699).media).thumb = new byte[0];
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.thumb_h = 0;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.thumb_w = 0;
                                                                                                         } catch (Exception var368) {
                                                                                                            var10000 = var368;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      label4281: {
                                                                                                         try {
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.size = var7.size;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.mime_type = var7.mime_type;
                                                                                                            if (var7.key == null) {
                                                                                                               var693 = new SendMessagesHelper.DelayedMessage(var42);
                                                                                                               var693.originalPath = var685;
                                                                                                               var693.sendEncryptedRequest = (TLObject)var699;
                                                                                                               var693.type = 2;
                                                                                                               var693.obj = (MessageObject)var6;
                                                                                                               break label4281;
                                                                                                            }
                                                                                                         } catch (Exception var399) {
                                                                                                            var10000 = var399;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }

                                                                                                         try {
                                                                                                            var690 = new TLRPC.TL_inputEncryptedFile();
                                                                                                            var690.id = var7.id;
                                                                                                            var690.access_hash = var7.access_hash;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.key = var7.key;
                                                                                                            ((TLRPC.DecryptedMessage)var699).media.iv = var7.iv;
                                                                                                            SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, var690, (String)null, (MessageObject)var6);
                                                                                                            break label5758;
                                                                                                         } catch (Exception var367) {
                                                                                                            var10000 = var367;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      label5746: {
                                                                                                         if (var26 != null) {
                                                                                                            try {
                                                                                                               if (var26.containsKey("parentObject")) {
                                                                                                                  var693.parentObject = var26.get("parentObject");
                                                                                                                  break label5746;
                                                                                                               }
                                                                                                            } catch (Exception var395) {
                                                                                                               var10000 = var395;
                                                                                                               var10001 = false;
                                                                                                               break label5772;
                                                                                                            }
                                                                                                         }

                                                                                                         try {
                                                                                                            var693.parentObject = var21;
                                                                                                         } catch (Exception var366) {
                                                                                                            var10000 = var366;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         var693.encryptedChat = var35;
                                                                                                         var693.performMediaUpload = true;
                                                                                                      } catch (Exception var365) {
                                                                                                         var10000 = var365;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      if (var12 != null) {
                                                                                                         try {
                                                                                                            if (var12.length() > 0 && var12.startsWith("http")) {
                                                                                                               var693.httpLocation = var12;
                                                                                                            }
                                                                                                         } catch (Exception var364) {
                                                                                                            var10000 = var364;
                                                                                                            var10001 = false;
                                                                                                            break label5772;
                                                                                                         }
                                                                                                      }

                                                                                                      try {
                                                                                                         this.performSendDelayedMessage(var693);
                                                                                                      } catch (Exception var363) {
                                                                                                         var10000 = var363;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }

                                                                                                      var692 = var693;
                                                                                                      break label5742;
                                                                                                   }

                                                                                                   if (var697 != null) {
                                                                                                      try {
                                                                                                         ((TLRPC.TL_decryptedMessageMediaExternalDocument)((TLRPC.DecryptedMessage)var699).media).thumb = var697;
                                                                                                      } catch (Exception var373) {
                                                                                                         var10000 = var373;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   } else {
                                                                                                      try {
                                                                                                         var695 = (TLRPC.TL_decryptedMessageMediaExternalDocument)((TLRPC.DecryptedMessage)var699).media;
                                                                                                         TLRPC.TL_photoSizeEmpty var712 = new TLRPC.TL_photoSizeEmpty();
                                                                                                         var695.thumb = var712;
                                                                                                         ((TLRPC.TL_decryptedMessageMediaExternalDocument)((TLRPC.DecryptedMessage)var699).media).thumb.type = "s";
                                                                                                      } catch (Exception var372) {
                                                                                                         var10000 = var372;
                                                                                                         var10001 = false;
                                                                                                         break label5772;
                                                                                                      }
                                                                                                   }

                                                                                                   try {
                                                                                                      SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.DecryptedMessage)var699, ((MessageObject)var6).messageOwner, var35, (TLRPC.InputEncryptedFile)null, (String)null, (MessageObject)var6);
                                                                                                   } catch (Exception var371) {
                                                                                                      var10000 = var371;
                                                                                                      var10001 = false;
                                                                                                      break label5772;
                                                                                                   }
                                                                                                }
                                                                                             }
                                                                                          }

                                                                                          var692 = var693;
                                                                                       }

                                                                                       if (var40 == 0L) {
                                                                                          break label4313;
                                                                                       }

                                                                                       TLObject var737;
                                                                                       try {
                                                                                          var737 = var692.sendEncryptedRequest;
                                                                                       } catch (Exception var348) {
                                                                                          var10000 = var348;
                                                                                          var10001 = false;
                                                                                          break label5666;
                                                                                       }

                                                                                       if (var737 == null) {
                                                                                          try {
                                                                                             var706 = new TLRPC.TL_messages_sendEncryptedMultiMedia();
                                                                                             var692.sendEncryptedRequest = var706;
                                                                                             break label4310;
                                                                                          } catch (Exception var347) {
                                                                                             var10000 = var347;
                                                                                             var10001 = false;
                                                                                             break label5666;
                                                                                          }
                                                                                       }

                                                                                       try {
                                                                                          var706 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var692.sendEncryptedRequest;
                                                                                          break label4310;
                                                                                       } catch (Exception var354) {
                                                                                          var10000 = var354;
                                                                                          var10001 = false;
                                                                                       }
                                                                                    }

                                                                                    var786 = var10000;
                                                                                    break label4312;
                                                                                 }

                                                                                 var786 = var10000;
                                                                                 break label4312;
                                                                              }

                                                                              ArrayList var711;
                                                                              try {
                                                                                 var692.messageObjects.add(var6);
                                                                                 var711 = var692.messages;
                                                                              } catch (Exception var346) {
                                                                                 var10000 = var346;
                                                                                 var10001 = false;
                                                                                 break label5666;
                                                                              }

                                                                              var678 = var678;
                                                                              var681 = var678;

                                                                              try {
                                                                                 var711.add(var678);
                                                                              } catch (Exception var267) {
                                                                                 var10000 = var267;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var692.originalPaths.add(var746);
                                                                              } catch (Exception var266) {
                                                                                 var10000 = var266;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var692.performMediaUpload = true;
                                                                              } catch (Exception var265) {
                                                                                 var10000 = var265;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var706.messages.add(var699);
                                                                              } catch (Exception var264) {
                                                                                 var10000 = var264;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var6 = new TLRPC.TL_inputEncryptedFile;
                                                                              } catch (Exception var263) {
                                                                                 var10000 = var263;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var6.<init>();
                                                                              } catch (Exception var262) {
                                                                                 var10000 = var262;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var40 = var27;
                                                                              if (var784 == 3) {
                                                                                 var40 = 1L;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 ((TLRPC.InputEncryptedFile)var6).id = var40;
                                                                              } catch (Exception var261) {
                                                                                 var10000 = var261;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 var706.files.add(var6);
                                                                              } catch (Exception var260) {
                                                                                 var10000 = var260;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }

                                                                              var681 = var678;

                                                                              try {
                                                                                 this.performSendDelayedMessage(var692);
                                                                                 break label4313;
                                                                              } catch (Exception var259) {
                                                                                 var10000 = var259;
                                                                                 var10001 = false;
                                                                                 break label5756;
                                                                              }
                                                                           }

                                                                           var687 = var10000;
                                                                           var682 = var678;
                                                                           var698 = var687;
                                                                           var686 = var6;
                                                                           break label5581;
                                                                        }

                                                                        var681 = var678;
                                                                        var678 = var6;
                                                                        break label5467;
                                                                     }

                                                                     var681 = var678;
                                                                     if (var16 != null) {
                                                                        return;
                                                                     }

                                                                     try {
                                                                        DataQuery.getInstance(this.currentAccount).cleanDraft(var10, false);
                                                                        return;
                                                                     } catch (Exception var58) {
                                                                        var10000 = var58;
                                                                        var10001 = false;
                                                                        break label5756;
                                                                     }
                                                                  }
                                                               }

                                                               var786 = var10000;
                                                               break label4480;
                                                            }

                                                            var698 = var10000;
                                                            var682 = var678;
                                                            var686 = var6;
                                                            break label5581;
                                                         }
                                                      }

                                                      var698 = var10000;
                                                      var686 = var6;
                                                      var682 = var681;
                                                      break label5581;
                                                   }

                                                   var786 = var10000;
                                                   break label4480;
                                                }

                                                var686 = var6;
                                                var786 = var10000;
                                                break label4479;
                                             }

                                             var786 = var10000;
                                             break label4478;
                                          }

                                          var687 = var10000;
                                          var682 = var678;
                                          var686 = var6;
                                          var698 = var687;
                                       }

                                       var678 = var686;
                                       var786 = var698;
                                       var681 = var682;
                                       break label5467;
                                    }

                                    var686 = var6;
                                 }

                                 var681 = var678;
                                 var678 = var686;
                                 break label5467;
                              }

                              var681 = var678;
                              var678 = var5;
                              break label5467;
                           }

                           var786 = var10000;
                           break label5465;
                        }

                        var687 = var10000;
                        var678 = var682;
                        var786 = var687;
                        break label5465;
                     }

                     var786 = var10000;
                     var681 = null;
                     break label5466;
                  }

                  var681 = var678;
               }

               var678 = null;
            }

            FileLog.e((Throwable)var786);
            MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError((TLRPC.Message)var681);
            if (var678 != null) {
               ((MessageObject)var678).messageOwner.send_state = 2;
            }

            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, ((TLRPC.Message)var681).id);
            this.processSentMessage(((TLRPC.Message)var681).id);
         }
      }
   }

   private void sendReadyToSendGroup(SendMessagesHelper.DelayedMessage var1, boolean var2, boolean var3) {
      if (var1.messageObjects.isEmpty()) {
         var1.markAsError();
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("group_");
         var4.append(var1.groupId);
         String var9 = var4.toString();
         int var5 = var1.finalGroupMessage;
         ArrayList var6 = var1.messageObjects;
         if (var5 != ((MessageObject)var6.get(var6.size() - 1)).getId()) {
            if (var2) {
               this.putToDelayedMessages(var9, var1);
            }

         } else {
            byte var7 = 0;
            var5 = 0;
            if (var2) {
               this.delayedMessages.remove(var9);
               MessagesStorage.getInstance(this.currentAccount).putMessages(var1.messages, false, true, false, 0);
               MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(var1.peer, var1.messageObjects);
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
            }

            TLObject var10 = var1.sendRequest;
            if (!(var10 instanceof TLRPC.TL_messages_sendMultiMedia)) {
               TLRPC.TL_messages_sendEncryptedMultiMedia var13 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var1.sendEncryptedRequest;

               for(var5 = var7; var5 < var13.files.size(); ++var5) {
                  if ((TLRPC.InputEncryptedFile)var13.files.get(var5) instanceof TLRPC.TL_inputEncryptedFile) {
                     return;
                  }
               }
            } else {
               TLRPC.TL_messages_sendMultiMedia var14 = (TLRPC.TL_messages_sendMultiMedia)var10;

               while(true) {
                  if (var5 >= var14.multi_media.size()) {
                     if (var3) {
                        SendMessagesHelper.DelayedMessage var12 = this.findMaxDelayedMessageForMessageId(var1.finalGroupMessage, var1.peer);
                        if (var12 != null) {
                           var12.addDelayedRequest(var1.sendRequest, var1.messageObjects, var1.originalPaths, var1.parentObjects, var1);
                           ArrayList var8 = var1.requests;
                           if (var8 != null) {
                              var12.requests.addAll(var8);
                           }

                           return;
                        }
                     }
                     break;
                  }

                  TLRPC.InputMedia var11 = ((TLRPC.TL_inputSingleMedia)var14.multi_media.get(var5)).media;
                  if (var11 instanceof TLRPC.TL_inputMediaUploadedPhoto || var11 instanceof TLRPC.TL_inputMediaUploadedDocument) {
                     return;
                  }

                  ++var5;
               }
            }

            var10 = var1.sendRequest;
            if (var10 instanceof TLRPC.TL_messages_sendMultiMedia) {
               this.performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia)var10, var1.messageObjects, var1.originalPaths, var1.parentObjects, var1);
            } else {
               SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia)var1.sendEncryptedRequest, var1);
            }

            var1.sendDelayedRequests();
         }
      }
   }

   private void updateMediaPaths(MessageObject var1, TLRPC.Message var2, int var3, String var4, boolean var5) {
      TLRPC.Message var6 = var1.messageOwner;
      TLRPC.MessageMedia var7 = var6.media;
      String var10;
      TLRPC.Document var26;
      TLRPC.PhotoSize var27;
      TLRPC.Document var28;
      StringBuilder var34;
      String var36;
      if (var7 != null) {
         TLRPC.Photo var8 = var7.photo;
         TLRPC.PhotoSize var9;
         TLRPC.Photo var25;
         Object var31;
         if (var8 != null) {
            label315: {
               var9 = FileLoader.getClosestPhotoSizeWithSize(var8.sizes, 40);
               if (var2 != null) {
                  var7 = var2.media;
                  if (var7 != null) {
                     var25 = var7.photo;
                     if (var25 != null) {
                        var27 = FileLoader.getClosestPhotoSizeWithSize(var25.sizes, 40);
                        break label315;
                     }
                  }
               }

               var27 = var9;
            }

            var31 = var6.media.photo;
         } else {
            var28 = var7.document;
            if (var28 != null) {
               label322: {
                  var9 = FileLoader.getClosestPhotoSizeWithSize(var28.thumbs, 40);
                  if (var2 != null) {
                     var7 = var2.media;
                     if (var7 != null) {
                        var26 = var7.document;
                        if (var26 != null) {
                           var27 = FileLoader.getClosestPhotoSizeWithSize(var26.thumbs, 40);
                           break label322;
                        }
                     }
                  }

                  var27 = var9;
               }

               var31 = var6.media.document;
            } else {
               label364: {
                  TLRPC.WebPage var29 = var7.webpage;
                  if (var29 != null) {
                     var25 = var29.photo;
                     TLRPC.WebPage var30;
                     if (var25 != null) {
                        label331: {
                           var9 = FileLoader.getClosestPhotoSizeWithSize(var25.sizes, 40);
                           if (var2 != null) {
                              var7 = var2.media;
                              if (var7 != null) {
                                 var30 = var7.webpage;
                                 if (var30 != null) {
                                    var25 = var30.photo;
                                    if (var25 != null) {
                                       var27 = FileLoader.getClosestPhotoSizeWithSize(var25.sizes, 40);
                                       break label331;
                                    }
                                 }
                              }
                           }

                           var27 = var9;
                        }

                        var31 = var6.media.webpage.photo;
                        break label364;
                     }

                     var26 = var29.document;
                     if (var26 != null) {
                        label339: {
                           var9 = FileLoader.getClosestPhotoSizeWithSize(var26.thumbs, 40);
                           if (var2 != null) {
                              var7 = var2.media;
                              if (var7 != null) {
                                 var30 = var7.webpage;
                                 if (var30 != null) {
                                    var26 = var30.document;
                                    if (var26 != null) {
                                       var27 = FileLoader.getClosestPhotoSizeWithSize(var26.thumbs, 40);
                                       break label339;
                                    }
                                 }
                              }
                           }

                           var27 = var9;
                        }

                        var31 = var6.media.webpage.document;
                        break label364;
                     }
                  }

                  var9 = null;
                  var27 = null;
                  var31 = null;
               }
            }
         }

         if (var27 instanceof TLRPC.TL_photoStrippedSize && var9 instanceof TLRPC.TL_photoStrippedSize) {
            var34 = new StringBuilder();
            var34.append("stripped");
            var34.append(FileRefController.getKeyForParentObject(var1));
            var10 = var34.toString();
            if (var2 != null) {
               var34 = new StringBuilder();
               var34.append("stripped");
               var34.append(FileRefController.getKeyForParentObject(var2));
               var36 = var34.toString();
            } else {
               var34 = new StringBuilder();
               var34.append("strippedmessage");
               var34.append(var3);
               var34.append("_");
               var34.append(var1.getChannelId());
               var36 = var34.toString();
            }

            ImageLoader.getInstance().replaceImageInCache(var10, var36, ImageLocation.getForObject(var27, (TLObject)var31), var5);
         }
      }

      if (var2 != null) {
         TLRPC.MessageMedia var33 = var2.media;
         File var12;
         TLRPC.MessageMedia var16;
         StringBuilder var32;
         MessagesStorage var37;
         if (var33 instanceof TLRPC.TL_messageMediaPhoto && var33.photo != null) {
            var7 = var6.media;
            if (var7 instanceof TLRPC.TL_messageMediaPhoto && var7.photo != null) {
               TLRPC.Photo var18;
               if (var33.ttl_seconds == 0) {
                  var37 = MessagesStorage.getInstance(this.currentAccount);
                  var18 = var2.media.photo;
                  StringBuilder var49 = new StringBuilder();
                  var49.append("sent_");
                  var49.append(var2.to_id.channel_id);
                  var49.append("_");
                  var49.append(var2.id);
                  var37.putSentFile(var4, var18, 0, var49.toString());
               }

               if (var6.media.photo.sizes.size() == 1 && ((TLRPC.PhotoSize)var6.media.photo.sizes.get(0)).location instanceof TLRPC.TL_fileLocationUnavailable) {
                  var6.media.photo.sizes = var2.media.photo.sizes;
               } else {
                  for(var3 = 0; var3 < var2.media.photo.sizes.size(); ++var3) {
                     var27 = (TLRPC.PhotoSize)var2.media.photo.sizes.get(var3);
                     if (var27 != null && var27.location != null && !(var27 instanceof TLRPC.TL_photoSizeEmpty) && var27.type != null) {
                        for(int var11 = 0; var11 < var6.media.photo.sizes.size(); ++var11) {
                           TLRPC.PhotoSize var24 = (TLRPC.PhotoSize)var6.media.photo.sizes.get(var11);
                           if (var24 != null) {
                              TLRPC.FileLocation var21 = var24.location;
                              if (var21 != null) {
                                 String var42 = var24.type;
                                 if (var42 != null && (var21.volume_id == -2147483648L && var27.type.equals(var42) || var27.w == var24.w && var27.h == var24.h)) {
                                    StringBuilder var22 = new StringBuilder();
                                    var22.append(var24.location.volume_id);
                                    var22.append("_");
                                    var22.append(var24.location.local_id);
                                    var42 = var22.toString();
                                    var22 = new StringBuilder();
                                    var22.append(var27.location.volume_id);
                                    var22.append("_");
                                    var22.append(var27.location.local_id);
                                    var36 = var22.toString();
                                    if (!var42.equals(var36)) {
                                       File var23 = FileLoader.getDirectory(4);
                                       var32 = new StringBuilder();
                                       var32.append(var42);
                                       var32.append(".jpg");
                                       File var35 = new File(var23, var32.toString());
                                       var16 = var2.media;
                                       if (var16.ttl_seconds != 0 || var16.photo.sizes.size() != 1 && var27.w <= 90 && var27.h <= 90) {
                                          var12 = FileLoader.getDirectory(4);
                                          var22 = new StringBuilder();
                                          var22.append(var36);
                                          var22.append(".jpg");
                                          var23 = new File(var12, var22.toString());
                                       } else {
                                          var23 = FileLoader.getPathToAttach(var27);
                                       }

                                       var35.renameTo(var23);
                                       ImageLoader.getInstance().replaceImageInCache(var42, var36, ImageLocation.getForPhoto(var27, var2.media.photo), var5);
                                       var24.location = var27.location;
                                       var24.size = var27.size;
                                    }
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               var2.message = var6.message;
               var2.attachPath = var6.attachPath;
               var18 = var6.media.photo;
               TLRPC.Photo var20 = var2.media.photo;
               var18.id = var20.id;
               var18.access_hash = var20.access_hash;
               return;
            }
         }

         var33 = var2.media;
         if (var33 instanceof TLRPC.TL_messageMediaDocument && var33.document != null) {
            var7 = var6.media;
            if (var7 instanceof TLRPC.TL_messageMediaDocument && var7.document != null) {
               if (var33.ttl_seconds == 0) {
                  boolean var13 = MessageObject.isVideoMessage(var2);
                  if ((var13 || MessageObject.isGifMessage(var2)) && MessageObject.isGifDocument(var2.media.document) == MessageObject.isGifDocument(var6.media.document)) {
                     var37 = MessagesStorage.getInstance(this.currentAccount);
                     var26 = var2.media.document;
                     var34 = new StringBuilder();
                     var34.append("sent_");
                     var34.append(var2.to_id.channel_id);
                     var34.append("_");
                     var34.append(var2.id);
                     var37.putSentFile(var4, var26, 2, var34.toString());
                     if (var13) {
                        var2.attachPath = var6.attachPath;
                     }
                  } else if (!MessageObject.isVoiceMessage(var2) && !MessageObject.isRoundVideoMessage(var2)) {
                     MessagesStorage var44 = MessagesStorage.getInstance(this.currentAccount);
                     var28 = var2.media.document;
                     var34 = new StringBuilder();
                     var34.append("sent_");
                     var34.append(var2.to_id.channel_id);
                     var34.append("_");
                     var34.append(var2.id);
                     var44.putSentFile(var4, var28, 1, var34.toString());
                  }
               }

               label359: {
                  var27 = FileLoader.getClosestPhotoSizeWithSize(var6.media.document.thumbs, 320);
                  TLRPC.PhotoSize var38 = FileLoader.getClosestPhotoSizeWithSize(var2.media.document.thumbs, 320);
                  TLRPC.FileLocation var40;
                  if (var27 != null) {
                     var40 = var27.location;
                     if (var40 != null && var40.volume_id == -2147483648L && var38 != null && var38.location != null && !(var38 instanceof TLRPC.TL_photoSizeEmpty) && !(var27 instanceof TLRPC.TL_photoSizeEmpty)) {
                        var34 = new StringBuilder();
                        var34.append(var27.location.volume_id);
                        var34.append("_");
                        var34.append(var27.location.local_id);
                        var36 = var34.toString();
                        var32 = new StringBuilder();
                        var32.append(var38.location.volume_id);
                        var32.append("_");
                        var32.append(var38.location.local_id);
                        var10 = var32.toString();
                        if (!var36.equals(var10)) {
                           var12 = FileLoader.getDirectory(4);
                           StringBuilder var14 = new StringBuilder();
                           var14.append(var36);
                           var14.append(".jpg");
                           var12 = new File(var12, var14.toString());
                           File var15 = FileLoader.getDirectory(4);
                           var14 = new StringBuilder();
                           var14.append(var10);
                           var14.append(".jpg");
                           var12.renameTo(new File(var15, var14.toString()));
                           ImageLoader.getInstance().replaceImageInCache(var36, var10, ImageLocation.getForDocument(var38, var2.media.document), var5);
                           var27.location = var38.location;
                           var27.size = var38.size;
                        }
                        break label359;
                     }
                  }

                  if (var27 != null && MessageObject.isStickerMessage(var2)) {
                     var40 = var27.location;
                     if (var40 != null) {
                        var38.location = var40;
                        break label359;
                     }
                  }

                  if (var27 == null || var27 != null && var27.location instanceof TLRPC.TL_fileLocationUnavailable || var27 instanceof TLRPC.TL_photoSizeEmpty) {
                     var6.media.document.thumbs = var2.media.document.thumbs;
                  }
               }

               var26 = var6.media.document;
               var28 = var2.media.document;
               var26.dc_id = var28.dc_id;
               var26.id = var28.id;
               var26.access_hash = var28.access_hash;
               var3 = 0;

               byte[] var46;
               while(true) {
                  if (var3 >= var6.media.document.attributes.size()) {
                     var46 = null;
                     break;
                  }

                  TLRPC.DocumentAttribute var45 = (TLRPC.DocumentAttribute)var6.media.document.attributes.get(var3);
                  if (var45 instanceof TLRPC.TL_documentAttributeAudio) {
                     var46 = var45.waveform;
                     break;
                  }

                  ++var3;
               }

               var6.media.document.attributes = var2.media.document.attributes;
               if (var46 != null) {
                  for(var3 = 0; var3 < var6.media.document.attributes.size(); ++var3) {
                     TLRPC.DocumentAttribute var39 = (TLRPC.DocumentAttribute)var6.media.document.attributes.get(var3);
                     if (var39 instanceof TLRPC.TL_documentAttributeAudio) {
                        var39.waveform = var46;
                        var39.flags |= 4;
                     }
                  }
               }

               var28 = var6.media.document;
               var26 = var2.media.document;
               var28.size = var26.size;
               var28.mime_type = var26.mime_type;
               if ((var2.flags & 4) == 0 && MessageObject.isOut(var2)) {
                  if (MessageObject.isNewGifDocument(var2.media.document)) {
                     DataQuery.getInstance(this.currentAccount).addRecentGif(var2.media.document, var2.date);
                  } else if (MessageObject.isStickerDocument(var2.media.document)) {
                     DataQuery.getInstance(this.currentAccount).addRecentSticker(0, var2, var2.media.document, var2.date, false);
                  }
               }

               String var47 = var6.attachPath;
               if (var47 != null && var47.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                  File var48 = new File(var6.attachPath);
                  TLRPC.MessageMedia var43 = var2.media;
                  var28 = var43.document;
                  if (var43.ttl_seconds != 0) {
                     var5 = true;
                  } else {
                     var5 = false;
                  }

                  File var41 = FileLoader.getPathToAttach(var28, var5);
                  if (!var48.renameTo(var41)) {
                     if (var48.exists()) {
                        var2.attachPath = var6.attachPath;
                     } else {
                        var1.attachPathExists = false;
                     }

                     var1.mediaExists = var41.exists();
                     var2.message = var6.message;
                     return;
                  } else {
                     if (MessageObject.isVideoMessage(var2)) {
                        var1.attachPathExists = true;
                     } else {
                        var1.mediaExists = var1.attachPathExists;
                        var1.attachPathExists = false;
                        var6.attachPath = "";
                        if (var4 != null && var4.startsWith("http")) {
                           MessagesStorage.getInstance(this.currentAccount).addRecentLocalFile(var4, var41.toString(), var6.media.document);
                           return;
                        }
                     }

                     return;
                  }
               } else {
                  var2.attachPath = var6.attachPath;
                  var2.message = var6.message;
                  return;
               }
            }
         }

         var16 = var2.media;
         if (var16 instanceof TLRPC.TL_messageMediaContact && var6.media instanceof TLRPC.TL_messageMediaContact) {
            var6.media = var16;
         } else {
            var16 = var2.media;
            if (var16 instanceof TLRPC.TL_messageMediaWebPage) {
               var6.media = var16;
            } else if (var16 instanceof TLRPC.TL_messageMediaGeo) {
               TLRPC.GeoPoint var17 = var16.geo;
               TLRPC.GeoPoint var19 = var6.media.geo;
               var17.lat = var19.lat;
               var17._long = var19._long;
            } else if (var16 instanceof TLRPC.TL_messageMediaGame) {
               var6.media = var16;
               if (var6.media instanceof TLRPC.TL_messageMediaGame && !TextUtils.isEmpty(var2.message)) {
                  var6.entities = var2.entities;
                  var6.message = var2.message;
               }
            } else if (var16 instanceof TLRPC.TL_messageMediaPoll) {
               var6.media = var16;
            }
         }

      }
   }

   private void uploadMultiMedia(SendMessagesHelper.DelayedMessage var1, TLRPC.InputMedia var2, TLRPC.InputEncryptedFile var3, String var4) {
      Float var5 = 1.0F;
      Boolean var6 = false;
      int var7;
      if (var2 != null) {
         TLRPC.TL_messages_sendMultiMedia var9 = (TLRPC.TL_messages_sendMultiMedia)var1.sendRequest;

         for(var7 = 0; var7 < var9.multi_media.size(); ++var7) {
            if (((TLRPC.TL_inputSingleMedia)var9.multi_media.get(var7)).media == var2) {
               this.putToSendingMessages((TLRPC.Message)var1.messages.get(var7));
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, var4, var5, var6);
               break;
            }
         }

         TLRPC.TL_messages_uploadMedia var10 = new TLRPC.TL_messages_uploadMedia();
         var10.media = var2;
         var10.peer = ((TLRPC.TL_messages_sendMultiMedia)var1.sendRequest).peer;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$SendMessagesHelper$9ikUx4RLVeYwBBYbegYSYKLcFew(this, var2, var1));
      } else if (var3 != null) {
         TLRPC.TL_messages_sendEncryptedMultiMedia var8 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var1.sendEncryptedRequest;

         for(var7 = 0; var7 < var8.files.size(); ++var7) {
            if (var8.files.get(var7) == var3) {
               this.putToSendingMessages((TLRPC.Message)var1.messages.get(var7));
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, var4, var5, var6);
               break;
            }
         }

         this.sendReadyToSendGroup(var1, false, true);
      }

   }

   private void writePreviousMessageData(TLRPC.Message var1, SerializedData var2) {
      var1.media.serializeToStream(var2);
      String var3 = var1.message;
      if (var3 == null) {
         var3 = "";
      }

      var2.writeString(var3);
      var3 = var1.attachPath;
      if (var3 == null) {
         var3 = "";
      }

      var2.writeString(var3);
      int var4 = var1.entities.size();
      var2.writeInt32(var4);

      for(int var5 = 0; var5 < var4; ++var5) {
         ((TLRPC.MessageEntity)var1.entities.get(var5)).serializeToStream(var2);
      }

   }

   public void cancelSendingMessage(ArrayList var1) {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      int var4 = 0;
      boolean var5 = false;

      int var6;
      int var8;
      String var17;
      for(var6 = 0; var4 < var1.size(); var6 = var8) {
         MessageObject var7 = (MessageObject)var1.get(var4);
         var3.add(var7.getId());
         var8 = var7.messageOwner.to_id.channel_id;
         TLRPC.Message var9 = this.removeFromSendingMessages(var7.getId());
         if (var9 != null) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var9.reqId, true);
         }

         Iterator var10 = this.delayedMessages.entrySet().iterator();

         while(true) {
            while(var10.hasNext()) {
               Entry var15 = (Entry)var10.next();
               ArrayList var11 = (ArrayList)var15.getValue();

               for(var6 = 0; var6 < var11.size(); ++var6) {
                  SendMessagesHelper.DelayedMessage var12 = (SendMessagesHelper.DelayedMessage)var11.get(var6);
                  if (var12.type == 4) {
                     byte var13 = -1;
                     MessageObject var16 = null;
                     var6 = 0;

                     int var14;
                     while(true) {
                        var14 = var13;
                        if (var6 >= var12.messageObjects.size()) {
                           break;
                        }

                        var16 = (MessageObject)var12.messageObjects.get(var6);
                        if (var16.getId() == var7.getId()) {
                           var14 = var6;
                           break;
                        }

                        ++var6;
                     }

                     if (var14 >= 0) {
                        var12.messageObjects.remove(var14);
                        var12.messages.remove(var14);
                        var12.originalPaths.remove(var14);
                        TLObject var19 = var12.sendRequest;
                        if (var19 != null) {
                           ((TLRPC.TL_messages_sendMultiMedia)var19).multi_media.remove(var14);
                        } else {
                           TLRPC.TL_messages_sendEncryptedMultiMedia var21 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var12.sendEncryptedRequest;
                           var21.messages.remove(var14);
                           var21.files.remove(var14);
                        }

                        MediaController.getInstance().cancelVideoConvert(var7);
                        var17 = (String)var12.extraHashMap.get(var16);
                        if (var17 != null) {
                           var2.add(var17);
                        }

                        if (var12.messageObjects.isEmpty()) {
                           var12.sendDelayedRequests();
                        } else {
                           if (var12.finalGroupMessage == var7.getId()) {
                              ArrayList var18 = var12.messageObjects;
                              MessageObject var22 = (MessageObject)var18.get(var18.size() - 1);
                              var12.finalGroupMessage = var22.getId();
                              var22.messageOwner.params.put("final", "1");
                              TLRPC.TL_messages_messages var20 = new TLRPC.TL_messages_messages();
                              var20.messages.add(var22.messageOwner);
                              MessagesStorage.getInstance(this.currentAccount).putMessages(var20, var12.peer, -2, 0, false);
                           }

                           this.sendReadyToSendGroup(var12, false, true);
                        }
                     }
                     break;
                  }

                  if (var12.obj.getId() == var7.getId()) {
                     var11.remove(var6);
                     var12.sendDelayedRequests();
                     MediaController.getInstance().cancelVideoConvert(var12.obj);
                     if (var11.size() == 0) {
                        var2.add(var15.getKey());
                        if (var12.sendEncryptedRequest != null) {
                           var5 = true;
                        }
                     }
                     break;
                  }
               }
            }

            ++var4;
            break;
         }
      }

      for(var4 = 0; var4 < var2.size(); ++var4) {
         var17 = (String)var2.get(var4);
         if (var17.startsWith("http")) {
            ImageLoader.getInstance().cancelLoadHttpFile(var17);
         } else {
            FileLoader.getInstance(this.currentAccount).cancelUploadFile(var17, var5);
         }

         this.stopVideoService(var17);
         this.delayedMessages.remove(var17);
      }

      if (var1.size() == 1 && ((MessageObject)var1.get(0)).isEditing() && ((MessageObject)var1.get(0)).previousMedia != null) {
         this.revertEditingMessageObject((MessageObject)var1.get(0));
      } else {
         MessagesController.getInstance(this.currentAccount).deleteMessages(var3, (ArrayList)null, (TLRPC.EncryptedChat)null, var6, false);
      }

   }

   public void cancelSendingMessage(MessageObject var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      this.cancelSendingMessage(var2);
   }

   public void checkUnsentMessages() {
      MessagesStorage.getInstance(this.currentAccount).getUnsentMessages(1000);
   }

   public void cleanup() {
      this.delayedMessages.clear();
      this.unsentMessages.clear();
      this.sendingMessages.clear();
      this.waitingForLocation.clear();
      this.waitingForCallback.clear();
      this.waitingForVote.clear();
      this.currentChatInfo = null;
      this.locationProvider.stop();
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.FileDidUpload;
      byte var5 = 0;
      byte var6 = 0;
      byte var7 = 0;
      byte var8 = 0;
      byte var22 = 0;
      ArrayList var12;
      long var17;
      ArrayList var48;
      if (var1 == var4) {
         String var9 = (String)var3[0];
         TLRPC.InputFile var10 = (TLRPC.InputFile)var3[1];
         TLRPC.InputEncryptedFile var11 = (TLRPC.InputEncryptedFile)var3[2];
         var12 = (ArrayList)this.delayedMessages.get(var9);
         if (var12 != null) {
            for(var1 = var22; var1 < var12.size(); var12 = var48) {
               SendMessagesHelper.DelayedMessage var13 = (SendMessagesHelper.DelayedMessage)var12.get(var1);
               TLObject var14 = var13.sendRequest;
               TLRPC.InputMedia var40;
               if (var14 instanceof TLRPC.TL_messages_sendMedia) {
                  var40 = ((TLRPC.TL_messages_sendMedia)var14).media;
               } else if (var14 instanceof TLRPC.TL_messages_editMessage) {
                  var40 = ((TLRPC.TL_messages_editMessage)var14).media;
               } else if (var14 instanceof TLRPC.TL_messages_sendBroadcast) {
                  var40 = ((TLRPC.TL_messages_sendBroadcast)var14).media;
               } else if (var14 instanceof TLRPC.TL_messages_sendMultiMedia) {
                  var40 = (TLRPC.InputMedia)var13.extraHashMap.get(var9);
               } else {
                  var40 = null;
               }

               HashMap var49;
               if (var10 != null && var40 != null) {
                  var2 = var13.type;
                  if (var2 == 0) {
                     var40.file = var10;
                     this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, var13, true, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                  } else if (var2 == 1) {
                     if (var40.file == null) {
                        label213: {
                           var40.file = var10;
                           if (var40.thumb == null) {
                              TLRPC.PhotoSize var44 = var13.photoSize;
                              if (var44 != null && var44.location != null) {
                                 this.performSendDelayedMessage(var13);
                                 break label213;
                              }
                           }

                           this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                        }
                     } else {
                        var40.thumb = var10;
                        var40.flags |= 4;
                        this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                     }
                  } else if (var2 == 2) {
                     if (var40.file == null) {
                        label206: {
                           var40.file = var10;
                           if (var40.thumb == null) {
                              TLRPC.PhotoSize var55 = var13.photoSize;
                              if (var55 != null && var55.location != null) {
                                 this.performSendDelayedMessage(var13);
                                 break label206;
                              }
                           }

                           this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                        }
                     } else {
                        var40.thumb = var10;
                        var40.flags |= 4;
                        this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                     }
                  } else if (var2 == 3) {
                     var40.file = var10;
                     this.performSendMessageRequest(var13.sendRequest, var13.obj, var13.originalPath, (SendMessagesHelper.DelayedMessage)null, var13.parentObject);
                  } else if (var2 == 4) {
                     if (var40 instanceof TLRPC.TL_inputMediaUploadedDocument) {
                        StringBuilder var46;
                        if (var40.file == null) {
                           var40.file = var10;
                           var49 = var13.extraHashMap;
                           var46 = new StringBuilder();
                           var46.append(var9);
                           var46.append("_i");
                           MessageObject var47 = (MessageObject)var49.get(var46.toString());
                           var2 = var13.messageObjects.indexOf(var47);
                           HashMap var50 = var13.extraHashMap;
                           StringBuilder var54 = new StringBuilder();
                           var54.append(var9);
                           var54.append("_t");
                           var13.photoSize = (TLRPC.PhotoSize)var50.get(var54.toString());
                           this.stopVideoService(((MessageObject)var13.messageObjects.get(var2)).messageOwner.attachPath);
                           if (var40.thumb == null && var13.photoSize != null) {
                              var13.performMediaUpload = true;
                              this.performSendDelayedMessage(var13, var2);
                           } else {
                              this.uploadMultiMedia(var13, var40, (TLRPC.InputEncryptedFile)null, var9);
                           }
                        } else {
                           var40.thumb = var10;
                           var40.flags |= 4;
                           var49 = var13.extraHashMap;
                           var46 = new StringBuilder();
                           var46.append(var9);
                           var46.append("_o");
                           this.uploadMultiMedia(var13, var40, (TLRPC.InputEncryptedFile)null, (String)var49.get(var46.toString()));
                        }
                     } else {
                        var40.file = var10;
                        this.uploadMultiMedia(var13, var40, (TLRPC.InputEncryptedFile)null, var9);
                     }
                  }

                  var48 = var12;
                  var12.remove(var1);
                  var2 = var1 - 1;
               } else {
                  TLRPC.InputEncryptedFile var15 = var11;
                  var48 = var12;
                  var11 = var11;
                  var2 = var1;
                  if (var15 != null) {
                     TLObject var16 = var13.sendEncryptedRequest;
                     var48 = var12;
                     var11 = var15;
                     var2 = var1;
                     if (var16 != null) {
                        TLRPC.TL_decryptedMessage var53;
                        if (var13.type == 4) {
                           TLRPC.TL_messages_sendEncryptedMultiMedia var51 = (TLRPC.TL_messages_sendEncryptedMultiMedia)var16;
                           var11 = (TLRPC.InputEncryptedFile)var13.extraHashMap.get(var9);
                           var2 = var51.files.indexOf(var11);
                           if (var2 >= 0) {
                              var51.files.set(var2, var15);
                              if (var11.id == 1L) {
                                 var49 = var13.extraHashMap;
                                 StringBuilder var34 = new StringBuilder();
                                 var34.append(var9);
                                 var34.append("_i");
                                 MessageObject var36 = (MessageObject)var49.get(var34.toString());
                                 var49 = var13.extraHashMap;
                                 var34 = new StringBuilder();
                                 var34.append(var9);
                                 var34.append("_t");
                                 var13.photoSize = (TLRPC.PhotoSize)var49.get(var34.toString());
                                 this.stopVideoService(((MessageObject)var13.messageObjects.get(var2)).messageOwner.attachPath);
                              }

                              var53 = (TLRPC.TL_decryptedMessage)var51.messages.get(var2);
                           } else {
                              var53 = null;
                           }
                        } else {
                           var53 = (TLRPC.TL_decryptedMessage)var16;
                        }

                        if (var53 != null) {
                           TLRPC.DecryptedMessageMedia var38 = var53.media;
                           if (var38 instanceof TLRPC.TL_decryptedMessageMediaVideo || var38 instanceof TLRPC.TL_decryptedMessageMediaPhoto || var38 instanceof TLRPC.TL_decryptedMessageMediaDocument) {
                              var17 = (Long)var3[5];
                              var53.media.size = (int)var17;
                           }

                           var38 = var53.media;
                           var38.key = (byte[])var3[3];
                           var38.iv = (byte[])var3[4];
                           if (var13.type == 4) {
                              this.uploadMultiMedia(var13, (TLRPC.InputMedia)null, var15, var9);
                           } else {
                              SecretChatHelper var39 = SecretChatHelper.getInstance(this.currentAccount);
                              MessageObject var52 = var13.obj;
                              var39.performSendEncryptedRequest(var53, var52.messageOwner, var13.encryptedChat, var15, var13.originalPath, var52);
                           }
                        }

                        var12.remove(var1);
                        var2 = var1 - 1;
                        var11 = var15;
                        var48 = var12;
                     }
                  }
               }

               var1 = var2 + 1;
            }

            if (var12.isEmpty()) {
               this.delayedMessages.remove(var9);
            }
         }
      } else {
         boolean var19;
         ArrayList var23;
         SendMessagesHelper.DelayedMessage var41;
         String var56;
         if (var1 == NotificationCenter.FileDidFailUpload) {
            var56 = (String)var3[0];
            var19 = (Boolean)var3[1];
            var23 = (ArrayList)this.delayedMessages.get(var56);
            if (var23 != null) {
               for(var1 = var5; var1 < var23.size(); var1 = var2 + 1) {
                  var41 = (SendMessagesHelper.DelayedMessage)var23.get(var1);
                  if (!var19 || var41.sendEncryptedRequest == null) {
                     var2 = var1;
                     if (var19) {
                        continue;
                     }

                     var2 = var1;
                     if (var41.sendRequest == null) {
                        continue;
                     }
                  }

                  var41.markAsError();
                  var23.remove(var1);
                  var2 = var1 - 1;
               }

               if (var23.isEmpty()) {
                  this.delayedMessages.remove(var56);
               }
            }
         } else {
            String var24;
            SendMessagesHelper.DelayedMessage var35;
            MessageObject var57;
            if (var1 == NotificationCenter.filePreparingStarted) {
               var57 = (MessageObject)var3[0];
               if (var57.getId() == 0) {
                  return;
               }

               var24 = (String)var3[1];
               ArrayList var30 = (ArrayList)this.delayedMessages.get(var57.messageOwner.attachPath);
               if (var30 != null) {
                  for(var1 = var6; var1 < var30.size(); ++var1) {
                     var35 = (SendMessagesHelper.DelayedMessage)var30.get(var1);
                     if (var35.type == 4) {
                        var2 = var35.messageObjects.indexOf(var57);
                        HashMap var42 = var35.extraHashMap;
                        StringBuilder var25 = new StringBuilder();
                        var25.append(var57.messageOwner.attachPath);
                        var25.append("_t");
                        var35.photoSize = (TLRPC.PhotoSize)var42.get(var25.toString());
                        var35.performMediaUpload = true;
                        this.performSendDelayedMessage(var35, var2);
                        var30.remove(var1);
                        break;
                     }

                     if (var35.obj == var57) {
                        var35.videoEditedInfo = null;
                        this.performSendDelayedMessage(var35);
                        var30.remove(var1);
                        break;
                     }
                  }

                  if (var30.isEmpty()) {
                     this.delayedMessages.remove(var57.messageOwner.attachPath);
                  }
               }
            } else {
               String var43;
               if (var1 == NotificationCenter.fileNewChunkAvailable) {
                  var57 = (MessageObject)var3[0];
                  if (var57.getId() == 0) {
                     return;
                  }

                  var43 = (String)var3[1];
                  var17 = (Long)var3[2];
                  long var20 = (Long)var3[3];
                  if ((int)var57.getDialogId() == 0) {
                     var19 = true;
                  } else {
                     var19 = false;
                  }

                  FileLoader.getInstance(this.currentAccount).checkUploadNewDataAvailable(var43, var19, var17, var20);
                  if (var20 != 0L) {
                     this.stopVideoService(var57.messageOwner.attachPath);
                     ArrayList var45 = (ArrayList)this.delayedMessages.get(var57.messageOwner.attachPath);
                     if (var45 != null) {
                        for(var1 = 0; var1 < var45.size(); ++var1) {
                           SendMessagesHelper.DelayedMessage var26 = (SendMessagesHelper.DelayedMessage)var45.get(var1);
                           MessageObject var37;
                           if (var26.type == 4) {
                              for(var2 = 0; var2 < var26.messageObjects.size(); ++var2) {
                                 var37 = (MessageObject)var26.messageObjects.get(var2);
                                 if (var37 == var57) {
                                    var37.videoEditedInfo = null;
                                    var37.messageOwner.params.remove("ve");
                                    var37.messageOwner.media.document.size = (int)var20;
                                    var23 = new ArrayList();
                                    var23.add(var37.messageOwner);
                                    MessagesStorage.getInstance(this.currentAccount).putMessages(var23, false, true, false, 0);
                                    break;
                                 }
                              }
                           } else {
                              var37 = var26.obj;
                              if (var37 == var57) {
                                 var37.videoEditedInfo = null;
                                 var37.messageOwner.params.remove("ve");
                                 var26.obj.messageOwner.media.document.size = (int)var20;
                                 var48 = new ArrayList();
                                 var48.add(var26.obj.messageOwner);
                                 MessagesStorage.getInstance(this.currentAccount).putMessages(var48, false, true, false, 0);
                                 break;
                              }
                           }
                        }
                     }
                  }
               } else if (var1 == NotificationCenter.filePreparingFailed) {
                  var57 = (MessageObject)var3[0];
                  if (var57.getId() == 0) {
                     return;
                  }

                  var24 = (String)var3[1];
                  this.stopVideoService(var57.messageOwner.attachPath);
                  var12 = (ArrayList)this.delayedMessages.get(var24);
                  if (var12 != null) {
                     label282:
                     for(var1 = 0; var1 < var12.size(); var1 = var2 + 1) {
                        var41 = (SendMessagesHelper.DelayedMessage)var12.get(var1);
                        if (var41.type == 4) {
                           int var29 = 0;

                           while(true) {
                              var2 = var1;
                              if (var29 >= var41.messages.size()) {
                                 continue label282;
                              }

                              if (var41.messageObjects.get(var29) == var57) {
                                 var41.markAsError();
                                 var12.remove(var1);
                                 break;
                              }

                              ++var29;
                           }
                        } else {
                           var2 = var1;
                           if (var41.obj != var57) {
                              continue;
                           }

                           var41.markAsError();
                           var12.remove(var1);
                        }

                        var2 = var1 - 1;
                     }

                     if (var12.isEmpty()) {
                        this.delayedMessages.remove(var24);
                     }
                  }
               } else if (var1 == NotificationCenter.httpFileDidLoad) {
                  var43 = (String)var3[0];
                  var48 = (ArrayList)this.delayedMessages.get(var43);
                  if (var48 != null) {
                     for(var2 = 0; var2 < var48.size(); ++var2) {
                        var35 = (SendMessagesHelper.DelayedMessage)var48.get(var2);
                        var1 = var35.type;
                        byte var27;
                        MessageObject var28;
                        if (var1 == 0) {
                           var28 = var35.obj;
                           var27 = 0;
                        } else {
                           label372: {
                              if (var1 == 2) {
                                 var28 = var35.obj;
                              } else {
                                 if (var1 != 4) {
                                    var27 = -1;
                                    var28 = null;
                                    break label372;
                                 }

                                 var28 = (MessageObject)var35.extraHashMap.get(var43);
                                 if (var28.getDocument() == null) {
                                    var27 = 0;
                                    break label372;
                                 }
                              }

                              var27 = 1;
                           }
                        }

                        StringBuilder var31;
                        String var32;
                        File var33;
                        if (var27 == 0) {
                           var31 = new StringBuilder();
                           var31.append(Utilities.MD5(var43));
                           var31.append(".");
                           var31.append(ImageLoader.getHttpUrlExtension(var43, "file"));
                           var32 = var31.toString();
                           var33 = new File(FileLoader.getDirectory(4), var32);
                           Utilities.globalQueue.postRunnable(new _$$Lambda$SendMessagesHelper$1kOX99gMEbip9sYs_E7UQv_97eY(this, var33, var28, var35, var43));
                        } else if (var27 == 1) {
                           var31 = new StringBuilder();
                           var31.append(Utilities.MD5(var43));
                           var31.append(".gif");
                           var32 = var31.toString();
                           var33 = new File(FileLoader.getDirectory(4), var32);
                           Utilities.globalQueue.postRunnable(new _$$Lambda$SendMessagesHelper$pp0U4GJ1r75dDYF4YGnbf9kI6EU(this, var35, var33, var28));
                        }
                     }

                     this.delayedMessages.remove(var43);
                  }
               } else if (var1 == NotificationCenter.fileDidLoad) {
                  var56 = (String)var3[0];
                  var23 = (ArrayList)this.delayedMessages.get(var56);
                  if (var23 != null) {
                     for(var1 = var7; var1 < var23.size(); ++var1) {
                        this.performSendDelayedMessage((SendMessagesHelper.DelayedMessage)var23.get(var1));
                     }

                     this.delayedMessages.remove(var56);
                  }
               } else if (var1 == NotificationCenter.httpFileDidFailedLoad || var1 == NotificationCenter.fileDidFailedLoad) {
                  var24 = (String)var3[0];
                  var48 = (ArrayList)this.delayedMessages.get(var24);
                  if (var48 != null) {
                     for(var1 = var8; var1 < var48.size(); ++var1) {
                        ((SendMessagesHelper.DelayedMessage)var48.get(var1)).markAsError();
                     }

                     this.delayedMessages.remove(var24);
                  }
               }
            }
         }
      }

   }

   public int editMessage(MessageObject var1, String var2, boolean var3, BaseFragment var4, ArrayList var5, Runnable var6) {
      if (var4 != null && var4.getParentActivity() != null && var6 != null) {
         TLRPC.TL_messages_editMessage var7 = new TLRPC.TL_messages_editMessage();
         var7.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var1.getDialogId());
         var7.message = var2;
         var7.flags |= 2048;
         var7.id = var1.getId();
         var7.no_webpage = var3 ^ true;
         if (var5 != null) {
            var7.entities = var5;
            var7.flags |= 8;
         }

         return ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8(this, var4, var7, var6));
      } else {
         return 0;
      }
   }

   public TLRPC.TL_photo generatePhotoSizes(String var1, Uri var2) {
      return this.generatePhotoSizes((TLRPC.TL_photo)null, var1, var2);
   }

   public TLRPC.TL_photo generatePhotoSizes(TLRPC.TL_photo var1, String var2, Uri var3) {
      Bitmap var4 = ImageLoader.loadBitmap(var2, var3, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), true);
      Bitmap var5 = var4;
      if (var4 == null) {
         var5 = ImageLoader.loadBitmap(var2, var3, 800.0F, 800.0F, true);
      }

      ArrayList var8 = new ArrayList();
      TLRPC.PhotoSize var6 = ImageLoader.scaleAndSaveImage(var5, 90.0F, 90.0F, 55, true);
      if (var6 != null) {
         var8.add(var6);
      }

      var6 = ImageLoader.scaleAndSaveImage(var5, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
      if (var6 != null) {
         var8.add(var6);
      }

      if (var5 != null) {
         var5.recycle();
      }

      if (var8.isEmpty()) {
         return null;
      } else {
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         TLRPC.TL_photo var7 = var1;
         if (var1 == null) {
            var7 = new TLRPC.TL_photo();
         }

         var7.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         var7.sizes = var8;
         var7.file_reference = new byte[0];
         return var7;
      }
   }

   protected ArrayList getDelayedMessages(String var1) {
      return (ArrayList)this.delayedMessages.get(var1);
   }

   protected long getNextRandomId() {
      long var1;
      for(var1 = 0L; var1 == 0L; var1 = Utilities.random.nextLong()) {
      }

      return var1;
   }

   protected long getVoteSendTime(long var1) {
      return (Long)this.voteSendTime.get(var1, 0L);
   }

   public boolean isSendingCallback(MessageObject var1, TLRPC.KeyboardButton var2) {
      byte var3 = 0;
      if (var1 != null && var2 != null) {
         if (var2 instanceof TLRPC.TL_keyboardButtonUrlAuth) {
            var3 = 3;
         } else if (var2 instanceof TLRPC.TL_keyboardButtonGame) {
            var3 = 1;
         } else if (var2 instanceof TLRPC.TL_keyboardButtonBuy) {
            var3 = 2;
         }

         StringBuilder var4 = new StringBuilder();
         var4.append(var1.getDialogId());
         var4.append("_");
         var4.append(var1.getId());
         var4.append("_");
         var4.append(Utilities.bytesToHex(var2.data));
         var4.append("_");
         var4.append(var3);
         String var5 = var4.toString();
         return this.waitingForCallback.containsKey(var5);
      } else {
         return false;
      }
   }

   public boolean isSendingCurrentLocation(MessageObject var1, TLRPC.KeyboardButton var2) {
      if (var1 != null && var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getDialogId());
         var3.append("_");
         var3.append(var1.getId());
         var3.append("_");
         var3.append(Utilities.bytesToHex(var2.data));
         var3.append("_");
         String var4;
         if (var2 instanceof TLRPC.TL_keyboardButtonGame) {
            var4 = "1";
         } else {
            var4 = "0";
         }

         var3.append(var4);
         var4 = var3.toString();
         return this.waitingForLocation.containsKey(var4);
      } else {
         return false;
      }
   }

   public boolean isSendingMessage(int var1) {
      boolean var2;
      if (this.sendingMessages.indexOfKey(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public byte[] isSendingVote(MessageObject var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("poll_");
         var2.append(var1.getPollId());
         String var3 = var2.toString();
         return (byte[])this.waitingForVote.get(var3);
      }
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$2$SendMessagesHelper(File var1, MessageObject var2, SendMessagesHelper.DelayedMessage var3, String var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$a2su0LhJhPbUXm5fG2WcDQ_Npn4(this, this.generatePhotoSizes(var1.toString(), (Uri)null), var2, var1, var3, var4));
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$4$SendMessagesHelper(SendMessagesHelper.DelayedMessage var1, File var2, MessageObject var3) {
      TLRPC.Document var4 = var1.obj.getDocument();
      boolean var5 = var4.thumbs.isEmpty();
      boolean var6 = false;
      if (var5 || ((TLRPC.PhotoSize)var4.thumbs.get(0)).location instanceof TLRPC.TL_fileLocationUnavailable) {
         label49: {
            Exception var10000;
            label48: {
               Bitmap var7;
               boolean var10001;
               try {
                  var7 = ImageLoader.loadBitmap(var2.getAbsolutePath(), (Uri)null, 90.0F, 90.0F, true);
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label48;
               }

               if (var7 == null) {
                  break label49;
               }

               ArrayList var8;
               try {
                  var4.thumbs.clear();
                  var8 = var4.thumbs;
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label48;
               }

               label32: {
                  try {
                     if (var1.sendEncryptedRequest == null) {
                        break label32;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label48;
                  }

                  var6 = true;
               }

               try {
                  var8.add(ImageLoader.scaleAndSaveImage(var7, 90.0F, 90.0F, 55, var6));
                  var7.recycle();
                  break label49;
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
               }
            }

            Exception var13 = var10000;
            var4.thumbs.clear();
            FileLog.e((Throwable)var13);
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk(this, var1, var2, var4, var3));
   }

   // $FF: synthetic method
   public void lambda$editMessage$11$SendMessagesHelper(BaseFragment var1, TLRPC.TL_messages_editMessage var2, Runnable var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var4, false);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$ag0T9ipOWU0deF9XfslwNyap3gA(this, var5, var1, var2));
      }

      AndroidUtilities.runOnUIThread(var3);
   }

   // $FF: synthetic method
   public void lambda$new$0$SendMessagesHelper() {
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidFailUpload);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingStarted);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileNewChunkAvailable);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.filePreparingFailed);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidFailedLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
   }

   // $FF: synthetic method
   public void lambda$null$1$SendMessagesHelper(TLRPC.TL_photo var1, MessageObject var2, File var3, SendMessagesHelper.DelayedMessage var4, String var5) {
      if (var1 != null) {
         TLRPC.Message var8 = var2.messageOwner;
         var8.media.photo = var1;
         var8.attachPath = var3.toString();
         ArrayList var7 = new ArrayList();
         var7.add(var2.messageOwner);
         MessagesStorage.getInstance(this.currentAccount).putMessages(var7, false, true, false, 0);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, var2.messageOwner);
         var7 = var1.sizes;
         var4.photoSize = (TLRPC.PhotoSize)var7.get(var7.size() - 1);
         var4.locationParent = var1;
         var4.httpLocation = null;
         if (var4.type == 4) {
            var4.performMediaUpload = true;
            this.performSendDelayedMessage(var4, var4.messageObjects.indexOf(var2));
         } else {
            this.performSendDelayedMessage(var4);
         }
      } else {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var6 = new StringBuilder();
            var6.append("can't load image ");
            var6.append(var5);
            var6.append(" to file ");
            var6.append(var3.toString());
            FileLog.e(var6.toString());
         }

         var4.markAsError();
      }

   }

   // $FF: synthetic method
   public void lambda$null$10$SendMessagesHelper(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_messages_editMessage var3) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$12$SendMessagesHelper(String var1) {
      Boolean var2 = (Boolean)this.waitingForCallback.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$null$13$SendMessagesHelper(String var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$Vs48x_dR1zvnzDoILUm1BxyX1bc(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$16$SendMessagesHelper(String var1, boolean var2, TLObject var3, MessageObject var4, TLRPC.KeyboardButton var5, ChatActivity var6, TLObject[] var7) {
      this.waitingForCallback.remove(var1);
      if (var2 && var3 == null) {
         this.sendCallback(false, var4, var5, var6);
      } else if (var3 != null) {
         boolean var8 = var5 instanceof TLRPC.TL_keyboardButtonUrlAuth;
         boolean var9 = true;
         if (var8) {
            if (var3 instanceof TLRPC.TL_urlAuthResultRequest) {
               var6.showRequestUrlAlert((TLRPC.TL_urlAuthResultRequest)var3, (TLRPC.TL_messages_requestUrlAuth)var7[0], var5.url);
            } else if (var3 instanceof TLRPC.TL_urlAuthResultAccepted) {
               var6.showOpenUrlAlert(((TLRPC.TL_urlAuthResultAccepted)var3).url, false);
            } else if (var3 instanceof TLRPC.TL_urlAuthResultDefault) {
               TLRPC.TL_urlAuthResultDefault var12 = (TLRPC.TL_urlAuthResultDefault)var3;
               var6.showOpenUrlAlert(var5.url, true);
            }
         } else if (var5 instanceof TLRPC.TL_keyboardButtonBuy) {
            if (var3 instanceof TLRPC.TL_payments_paymentForm) {
               TLRPC.TL_payments_paymentForm var13 = (TLRPC.TL_payments_paymentForm)var3;
               MessagesController.getInstance(this.currentAccount).putUsers(var13.users, false);
               var6.presentFragment(new PaymentFormActivity(var13, var4));
            } else if (var3 instanceof TLRPC.TL_payments_paymentReceipt) {
               var6.presentFragment(new PaymentFormActivity(var4, (TLRPC.TL_payments_paymentReceipt)var3));
            }
         } else {
            TLRPC.TL_messages_botCallbackAnswer var22 = (TLRPC.TL_messages_botCallbackAnswer)var3;
            if (!var2 && var22.cache_time != 0) {
               MessagesStorage.getInstance(this.currentAccount).saveBotCache(var1, var22);
            }

            int var10;
            int var11;
            TLRPC.Message var14;
            TLRPC.User var15;
            String var17;
            if (var22.message != null) {
               var14 = var4.messageOwner;
               var10 = var14.from_id;
               var11 = var14.via_bot_id;
               if (var11 != 0) {
                  var10 = var11;
               }

               label82: {
                  if (var10 > 0) {
                     var15 = MessagesController.getInstance(this.currentAccount).getUser(var10);
                     if (var15 != null) {
                        var1 = ContactsController.formatName(var15.first_name, var15.last_name);
                        break label82;
                     }
                  } else {
                     TLRPC.Chat var16 = MessagesController.getInstance(this.currentAccount).getChat(-var10);
                     if (var16 != null) {
                        var1 = var16.title;
                        break label82;
                     }
                  }

                  var1 = null;
               }

               var17 = var1;
               if (var1 == null) {
                  var17 = "bot";
               }

               if (var22.alert) {
                  if (var6.getParentActivity() == null) {
                     return;
                  }

                  AlertDialog.Builder var18 = new AlertDialog.Builder(var6.getParentActivity());
                  var18.setTitle(var17);
                  var18.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var18.setMessage(var22.message);
                  var6.showDialog(var18.create());
               } else {
                  var6.showAlert(var17, var22.message);
               }
            } else if (var22.url != null) {
               if (var6.getParentActivity() == null) {
                  return;
               }

               var14 = var4.messageOwner;
               var10 = var14.from_id;
               var11 = var14.via_bot_id;
               if (var11 != 0) {
                  var10 = var11;
               }

               var15 = MessagesController.getInstance(this.currentAccount).getUser(var10);
               boolean var24;
               if (var15 != null && var15.verified) {
                  var24 = true;
               } else {
                  var24 = false;
               }

               if (var5 instanceof TLRPC.TL_keyboardButtonGame) {
                  TLRPC.MessageMedia var19 = var4.messageOwner.media;
                  TLRPC.TL_game var21;
                  if (var19 instanceof TLRPC.TL_messageMediaGame) {
                     var21 = var19.game;
                  } else {
                     var21 = null;
                  }

                  if (var21 == null) {
                     return;
                  }

                  label88: {
                     var17 = var22.url;
                     if (!var24) {
                        SharedPreferences var23 = MessagesController.getNotificationsSettings(this.currentAccount);
                        StringBuilder var20 = new StringBuilder();
                        var20.append("askgame_");
                        var20.append(var10);
                        if (var23.getBoolean(var20.toString(), true)) {
                           var2 = var9;
                           break label88;
                        }
                     }

                     var2 = false;
                  }

                  var6.showOpenGameAlert(var21, var4, var17, var2, var10);
               } else {
                  var6.showOpenUrlAlert(var22.url, false);
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$19$SendMessagesHelper(TLObject var1, TLRPC.InputMedia var2, SendMessagesHelper.DelayedMessage var3) {
      Object var7;
      label42: {
         if (var1 != null) {
            TLRPC.MessageMedia var4 = (TLRPC.MessageMedia)var1;
            if (var2 instanceof TLRPC.TL_inputMediaUploadedPhoto && var4 instanceof TLRPC.TL_messageMediaPhoto) {
               var7 = new TLRPC.TL_inputMediaPhoto();
               ((TLRPC.TL_inputMediaPhoto)var7).id = new TLRPC.TL_inputPhoto();
               TLRPC.InputPhoto var11 = ((TLRPC.TL_inputMediaPhoto)var7).id;
               TLRPC.Photo var9 = var4.photo;
               var11.id = var9.id;
               var11.access_hash = var9.access_hash;
               var11.file_reference = var9.file_reference;
               break label42;
            }

            if (var2 instanceof TLRPC.TL_inputMediaUploadedDocument && var4 instanceof TLRPC.TL_messageMediaDocument) {
               var7 = new TLRPC.TL_inputMediaDocument();
               ((TLRPC.TL_inputMediaDocument)var7).id = new TLRPC.TL_inputDocument();
               TLRPC.InputDocument var5 = ((TLRPC.TL_inputMediaDocument)var7).id;
               TLRPC.Document var8 = var4.document;
               var5.id = var8.id;
               var5.access_hash = var8.access_hash;
               var5.file_reference = var8.file_reference;
               break label42;
            }
         }

         var7 = null;
      }

      if (var7 != null) {
         int var6 = var2.ttl_seconds;
         if (var6 != 0) {
            ((TLRPC.InputMedia)var7).ttl_seconds = var6;
            ((TLRPC.InputMedia)var7).flags |= 1;
         }

         TLRPC.TL_messages_sendMultiMedia var10 = (TLRPC.TL_messages_sendMultiMedia)var3.sendRequest;

         for(var6 = 0; var6 < var10.multi_media.size(); ++var6) {
            if (((TLRPC.TL_inputSingleMedia)var10.multi_media.get(var6)).media == var2) {
               ((TLRPC.TL_inputSingleMedia)var10.multi_media.get(var6)).media = (TLRPC.InputMedia)var7;
               break;
            }
         }

         this.sendReadyToSendGroup(var3, false, true);
      } else {
         var3.markAsError();
      }

   }

   // $FF: synthetic method
   public void lambda$null$21$SendMessagesHelper(String var1) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopEncodingService, var1, this.currentAccount);
   }

   // $FF: synthetic method
   public void lambda$null$23$SendMessagesHelper(TLRPC.TL_updateNewMessage var1) {
      MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, var1.pts, -1, var1.pts_count);
   }

   // $FF: synthetic method
   public void lambda$null$24$SendMessagesHelper(TLRPC.TL_updateNewChannelMessage var1) {
      MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(var1.pts, var1.pts_count, var1.message.to_id.channel_id);
   }

   // $FF: synthetic method
   public void lambda$null$25$SendMessagesHelper(TLRPC.Message var1, int var2, long var3, int var5) {
      DataQuery.getInstance(this.currentAccount).increasePeerRaiting(var1.dialog_id);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var2, var1.id, var1, var1.dialog_id, var3, var5);
      this.processSentMessage(var2);
      this.removeFromSendingMessages(var2);
   }

   // $FF: synthetic method
   public void lambda$null$26$SendMessagesHelper(TLRPC.Message var1, int var2, ArrayList var3, long var4, int var6) {
      MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(var1.random_id, var2, var1.id, 0, false, var1.to_id.channel_id);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var3, true, false, false, 0);
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$jV9Gq3Q6H66Q6pHxnGZUWi6v2TA(this, var1, var2, var4, var6));
   }

   // $FF: synthetic method
   public void lambda$null$27$SendMessagesHelper(TLRPC.Updates var1) {
      MessagesController.getInstance(this.currentAccount).processUpdates(var1, false);
   }

   // $FF: synthetic method
   public void lambda$null$28$SendMessagesHelper(TLRPC.TL_error var1, ArrayList var2, TLRPC.TL_messages_sendMultiMedia var3, ArrayList var4, ArrayList var5, SendMessagesHelper.DelayedMessage var6, TLObject var7) {
      SendMessagesHelper var8 = this;
      if (var1 != null && FileRefController.isFileRefError(var1.text)) {
         if (var2 != null) {
            ArrayList var20 = new ArrayList(var2);
            FileRefController.getInstance(this.currentAccount).requestReference(var20, var3, var4, var5, var20, var6);
            return;
         }

         if (var6 != null) {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$3(this, var3, var6, var4));
            return;
         }
      }

      int var9;
      boolean var32;
      if (var1 == null) {
         SparseArray var18 = new SparseArray();
         LongSparseArray var21 = new LongSparseArray();
         TLRPC.Updates var22 = (TLRPC.Updates)var7;
         ArrayList var23 = var22.updates;

         int var10;
         TLRPC.Message var33;
         for(var9 = 0; var9 < var23.size(); var9 = var10 + 1) {
            TLRPC.Update var24 = (TLRPC.Update)var23.get(var9);
            if (var24 instanceof TLRPC.TL_updateMessageID) {
               TLRPC.TL_updateMessageID var25 = (TLRPC.TL_updateMessageID)var24;
               var21.put(var25.random_id, var25.id);
               var23.remove(var9);
            } else if (var24 instanceof TLRPC.TL_updateNewMessage) {
               TLRPC.TL_updateNewMessage var11 = (TLRPC.TL_updateNewMessage)var24;
               TLRPC.Message var26 = var11.message;
               var18.put(var26.id, var26);
               Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$LtIzRBxRfP0h_IpuxXXH0r91OLg(var8, var11));
               var23.remove(var9);
            } else {
               var10 = var9;
               if (!(var24 instanceof TLRPC.TL_updateNewChannelMessage)) {
                  continue;
               }

               TLRPC.TL_updateNewChannelMessage var28 = (TLRPC.TL_updateNewChannelMessage)var24;
               var33 = var28.message;
               var18.put(var33.id, var33);
               Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$QOjj_wrwvuHdKpPrDByKhoWSuVs(var8, var28));
               var23.remove(var9);
            }

            var10 = var9 - 1;
         }

         var9 = 0;

         label69: {
            while(true) {
               if (var9 >= var4.size()) {
                  var32 = false;
                  break label69;
               }

               MessageObject var29 = (MessageObject)var4.get(var9);
               String var27 = (String)var5.get(var9);
               var33 = var29.messageOwner;
               int var12 = var33.id;
               ArrayList var13 = new ArrayList();
               String var14 = var33.attachPath;
               Integer var34 = (Integer)var21.get(var33.random_id);
               if (var34 == null) {
                  break;
               }

               TLRPC.Message var35 = (TLRPC.Message)var18.get(var34);
               if (var35 == null) {
                  break;
               }

               var13.add(var35);
               this.updateMediaPaths(var29, var35, var35.id, var27, false);
               var10 = var29.getMediaExistanceFlags();
               var33.id = var35.id;
               if ((var33.flags & Integer.MIN_VALUE) != 0) {
                  var35.flags |= Integer.MIN_VALUE;
               }

               long var15 = var35.grouped_id;
               Integer var30 = (Integer)MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(var35.dialog_id);
               Integer var31 = var30;
               if (var30 == null) {
                  var31 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var35.out, var35.dialog_id);
                  MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(var35.dialog_id, var31);
               }

               boolean var17;
               if (var31 < var35.id) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               var35.unread = var17;
               StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
               var33.send_state = 0;
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var12, var33.id, var33, var33.dialog_id, var15, var10);
               MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$s_p_jfZFDyfDIdh58D5Hp9jBe_A(this, var33, var12, var13, var15, var10));
               ++var9;
            }

            var32 = true;
         }

         Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$4Kecy4z15gyx9k2PnYvYSIzID4o(this, var22));
         var8 = this;
      } else {
         AlertsCreator.processError(this.currentAccount, var1, (BaseFragment)null, var3);
         var32 = true;
      }

      if (var32) {
         for(var9 = 0; var9 < var4.size(); ++var9) {
            TLRPC.Message var19 = ((MessageObject)var4.get(var9)).messageOwner;
            MessagesStorage.getInstance(var8.currentAccount).markMessageAsSendError(var19);
            var19.send_state = 2;
            NotificationCenter.getInstance(var8.currentAccount).postNotificationName(NotificationCenter.messageSendError, var19.id);
            var8.processSentMessage(var19.id);
            var8.removeFromSendingMessages(var19.id);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$SendMessagesHelper(SendMessagesHelper.DelayedMessage var1, File var2, TLRPC.Document var3, MessageObject var4) {
      var1.httpLocation = null;
      var1.obj.messageOwner.attachPath = var2.toString();
      if (!var3.thumbs.isEmpty()) {
         var1.photoSize = (TLRPC.PhotoSize)var3.thumbs.get(0);
         var1.locationParent = var3;
      }

      ArrayList var5 = new ArrayList();
      var5.add(var4.messageOwner);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var5, false, true, false, 0);
      var1.performMediaUpload = true;
      this.performSendDelayedMessage(var1);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMessageMedia, var1.obj.messageOwner);
   }

   // $FF: synthetic method
   public void lambda$null$30$SendMessagesHelper(TLRPC.Message var1) {
      this.processSentMessage(var1.id);
      this.removeFromSendingMessages(var1.id);
   }

   // $FF: synthetic method
   public void lambda$null$31$SendMessagesHelper(TLRPC.Updates var1, TLRPC.Message var2) {
      MessagesController.getInstance(this.currentAccount).processUpdates(var1, false);
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$8gQkjP_exgY3T8Zm64H9fCFaXkg(this, var2));
   }

   // $FF: synthetic method
   public void lambda$null$32$SendMessagesHelper(TLRPC.TL_error var1, TLRPC.Message var2, TLObject var3, MessageObject var4, String var5, TLObject var6) {
      int var7 = 0;
      Object var8 = null;
      if (var1 == null) {
         String var13 = var2.attachPath;
         TLRPC.Updates var12 = (TLRPC.Updates)var3;
         ArrayList var9 = var12.updates;

         TLRPC.Message var10;
         while(true) {
            var10 = (TLRPC.Message)var8;
            if (var7 >= var9.size()) {
               break;
            }

            TLRPC.Update var11 = (TLRPC.Update)var9.get(var7);
            if (var11 instanceof TLRPC.TL_updateEditMessage) {
               var10 = ((TLRPC.TL_updateEditMessage)var11).message;
               break;
            }

            if (var11 instanceof TLRPC.TL_updateEditChannelMessage) {
               var10 = ((TLRPC.TL_updateEditChannelMessage)var11).message;
               break;
            }

            ++var7;
         }

         if (var10 != null) {
            ImageLoader.saveMessageThumbs(var10);
            this.updateMediaPaths(var4, var10, var10.id, var5, false);
         }

         Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$5FORFfWffqEc1IY97T80wKl0r1Y(this, var12, var2));
         if (MessageObject.isVideoMessage(var2) || MessageObject.isRoundVideoMessage(var2) || MessageObject.isNewGifMessage(var2)) {
            this.stopVideoService(var13);
         }
      } else {
         AlertsCreator.processError(this.currentAccount, var1, (BaseFragment)null, var6);
         if (MessageObject.isVideoMessage(var2) || MessageObject.isRoundVideoMessage(var2) || MessageObject.isNewGifMessage(var2)) {
            this.stopVideoService(var2.attachPath);
         }

         this.removeFromSendingMessages(var2.id);
         this.revertEditingMessageObject(var4);
      }

   }

   // $FF: synthetic method
   public void lambda$null$33$SendMessagesHelper(TLRPC.TL_updateShortSentMessage var1) {
      MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, var1.pts, var1.date, var1.pts_count);
   }

   // $FF: synthetic method
   public void lambda$null$34$SendMessagesHelper(TLRPC.TL_updateNewMessage var1) {
      MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, var1.pts, -1, var1.pts_count);
   }

   // $FF: synthetic method
   public void lambda$null$35$SendMessagesHelper(TLRPC.TL_updateNewChannelMessage var1) {
      MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(var1.pts, var1.pts_count, var1.message.to_id.channel_id);
   }

   // $FF: synthetic method
   public void lambda$null$36$SendMessagesHelper(TLRPC.Updates var1) {
      MessagesController.getInstance(this.currentAccount).processUpdates(var1, false);
   }

   // $FF: synthetic method
   public void lambda$null$37$SendMessagesHelper(boolean var1, ArrayList var2, TLRPC.Message var3, int var4, int var5) {
      int var6;
      if (var1) {
         for(var6 = 0; var6 < var2.size(); ++var6) {
            TLRPC.Message var7 = (TLRPC.Message)var2.get(var6);
            ArrayList var8 = new ArrayList();
            MessageObject var11 = new MessageObject(this.currentAccount, var7, false);
            var8.add(var11);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(var11.getDialogId(), var8, true);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

      DataQuery.getInstance(this.currentAccount).increasePeerRaiting(var3.dialog_id);
      NotificationCenter var10 = NotificationCenter.getInstance(this.currentAccount);
      int var9 = NotificationCenter.messageReceivedByServer;
      if (var1) {
         var6 = var4;
      } else {
         var6 = var3.id;
      }

      var10.postNotificationName(var9, var4, var6, var3, var3.dialog_id, 0L, var5);
      this.processSentMessage(var4);
      this.removeFromSendingMessages(var4);
   }

   // $FF: synthetic method
   public void lambda$null$38$SendMessagesHelper(TLRPC.Message var1, int var2, boolean var3, ArrayList var4, int var5, String var6) {
      MessagesStorage var7 = MessagesStorage.getInstance(this.currentAccount);
      long var8 = var1.random_id;
      int var10;
      if (var3) {
         var10 = var2;
      } else {
         var10 = var1.id;
      }

      var7.updateMessageStateAndId(var8, var2, var10, 0, false, var1.to_id.channel_id);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var4, true, false, var3, 0);
      if (var3) {
         ArrayList var11 = new ArrayList();
         var11.add(var1);
         MessagesStorage.getInstance(this.currentAccount).putMessages(var11, true, false, false, 0);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$Lyh7x_fVJCtY78eBEW_nfqaECxE(this, var3, var4, var1, var2, var5));
      if (MessageObject.isVideoMessage(var1) || MessageObject.isRoundVideoMessage(var1) || MessageObject.isNewGifMessage(var1)) {
         this.stopVideoService(var6);
      }

   }

   // $FF: synthetic method
   public void lambda$null$39$SendMessagesHelper(TLRPC.TL_error var1, TLRPC.Message var2, TLObject var3, TLObject var4, MessageObject var5, String var6) {
      TLRPC.Message var7 = null;
      boolean var16;
      if (var1 == null) {
         int var8 = var2.id;
         boolean var9 = var3 instanceof TLRPC.TL_messages_sendBroadcast;
         ArrayList var10 = new ArrayList();
         String var11 = var2.attachPath;
         int var13;
         int var28;
         boolean var29;
         if (var4 instanceof TLRPC.TL_updateShortSentMessage) {
            TLRPC.TL_updateShortSentMessage var20 = (TLRPC.TL_updateShortSentMessage)var4;
            this.updateMediaPaths(var5, (TLRPC.Message)null, var20.id, (String)null, false);
            var28 = var5.getMediaExistanceFlags();
            var13 = var20.id;
            var2.id = var13;
            var2.local_id = var13;
            var2.date = var20.date;
            var2.entities = var20.entities;
            var2.out = var20.out;
            TLRPC.MessageMedia var25 = var20.media;
            if (var25 != null) {
               var2.media = var25;
               var2.flags |= 512;
               ImageLoader.saveMessageThumbs(var2);
            }

            if (var20.media instanceof TLRPC.TL_messageMediaGame && !TextUtils.isEmpty(var20.message)) {
               var2.message = var20.message;
            }

            if (!var2.entities.isEmpty()) {
               var2.flags |= 128;
            }

            Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$oGx4Uga4Fsoa8DwCGDnCb2Zs6E8(this, var20));
            var10.add(var2);
            var29 = false;
         } else if (!(var4 instanceof TLRPC.Updates)) {
            var29 = false;
            var28 = 0;
         } else {
            TLRPC.Updates var14 = (TLRPC.Updates)var4;
            ArrayList var21 = var14.updates;
            var13 = 0;

            TLRPC.Message var18;
            while(true) {
               var18 = var7;
               if (var13 >= var21.size()) {
                  break;
               }

               TLRPC.Update var19 = (TLRPC.Update)var21.get(var13);
               if (var19 instanceof TLRPC.TL_updateNewMessage) {
                  TLRPC.TL_updateNewMessage var26 = (TLRPC.TL_updateNewMessage)var19;
                  var18 = var26.message;
                  var10.add(var18);
                  Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$L5hF2IhINUG6nIA5dSvAtaxsz8k(this, var26));
                  var21.remove(var13);
                  break;
               }

               if (var19 instanceof TLRPC.TL_updateNewChannelMessage) {
                  TLRPC.TL_updateNewChannelMessage var24 = (TLRPC.TL_updateNewChannelMessage)var19;
                  var18 = var24.message;
                  var10.add(var18);
                  if ((var2.flags & Integer.MIN_VALUE) != 0) {
                     var7 = var24.message;
                     var7.flags |= Integer.MIN_VALUE;
                  }

                  Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$XuUcVoATmHzvdWgq3344GUvreTA(this, var24));
                  var21.remove(var13);
                  break;
               }

               ++var13;
            }

            boolean var12;
            if (var18 != null) {
               ImageLoader.saveMessageThumbs(var18);
               Integer var27 = (Integer)MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(var18.dialog_id);
               Integer var23 = var27;
               if (var27 == null) {
                  var23 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var18.out, var18.dialog_id);
                  MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(var18.dialog_id, var23);
               }

               boolean var15;
               if (var23 < var18.id) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               var18.unread = var15;
               this.updateMediaPaths(var5, var18, var18.id, var6, false);
               var13 = var5.getMediaExistanceFlags();
               var2.id = var18.id;
               var12 = false;
            } else {
               var12 = true;
               var13 = 0;
            }

            Utilities.stageQueue.postRunnable(new _$$Lambda$SendMessagesHelper$WQ_rgU9i_c841KG3_Zo7CgxwtnA(this, var14));
            var28 = var13;
            var29 = var12;
         }

         if (MessageObject.isLiveLocationMessage(var2)) {
            LocationController.getInstance(this.currentAccount).addSharingLocation(var2.dialog_id, var2.id, var2.media.period, var2);
         }

         var16 = var29;
         if (!var29) {
            StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, 1);
            var2.send_state = 0;
            NotificationCenter var22 = NotificationCenter.getInstance(this.currentAccount);
            int var17 = NotificationCenter.messageReceivedByServer;
            int var30;
            if (var9) {
               var30 = var8;
            } else {
               var30 = var2.id;
            }

            var22.postNotificationName(var17, var8, var30, var2, var2.dialog_id, 0L, var28);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q(this, var2, var8, var9, var10, var28, var11));
            var16 = var29;
         }
      } else {
         AlertsCreator.processError(this.currentAccount, var1, (BaseFragment)null, var3);
         var16 = true;
      }

      if (var16) {
         MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(var2);
         var2.send_state = 2;
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var2.id);
         this.processSentMessage(var2.id);
         if (MessageObject.isVideoMessage(var2) || MessageObject.isRoundVideoMessage(var2) || MessageObject.isNewGifMessage(var2)) {
            this.stopVideoService(var2.attachPath);
         }

         this.removeFromSendingMessages(var2.id);
      }

   }

   // $FF: synthetic method
   public void lambda$null$41$SendMessagesHelper(TLRPC.Message var1, int var2) {
      var1.send_state = 0;
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByAck, var2);
   }

   // $FF: synthetic method
   public void lambda$null$5$SendMessagesHelper(TLRPC.Message var1, long var2, int var4, TLRPC.Message var5, int var6) {
      var1.send_state = 0;
      DataQuery.getInstance(this.currentAccount).increasePeerRaiting(var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var4, var5.id, var5, var2, 0L, var6);
      this.processSentMessage(var4);
      this.removeFromSendingMessages(var4);
   }

   // $FF: synthetic method
   public void lambda$null$6$SendMessagesHelper(TLRPC.Message var1, int var2, TLRPC.Peer var3, ArrayList var4, long var5, TLRPC.Message var7, int var8) {
      MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(var1.random_id, var2, var1.id, 0, false, var3.channel_id);
      MessagesStorage.getInstance(this.currentAccount).putMessages(var4, true, false, false, 0);
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$imJ61LkDqCKC2zsI4rTSXfDp6Z0(this, var1, var5, var2, var7, var8));
   }

   // $FF: synthetic method
   public void lambda$null$7$SendMessagesHelper(TLRPC.TL_error var1, TLRPC.TL_messages_forwardMessages var2) {
      AlertsCreator.processError(this.currentAccount, var1, (BaseFragment)null, var2);
   }

   // $FF: synthetic method
   public void lambda$null$8$SendMessagesHelper(TLRPC.Message var1) {
      var1.send_state = 2;
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var1.id);
      this.processSentMessage(var1.id);
      this.removeFromSendingMessages(var1.id);
   }

   // $FF: synthetic method
   public void lambda$performSendMessageRequest$40$SendMessagesHelper(TLObject var1, Object var2, MessageObject var3, String var4, SendMessagesHelper.DelayedMessage var5, boolean var6, SendMessagesHelper.DelayedMessage var7, TLRPC.Message var8, TLObject var9, TLRPC.TL_error var10) {
      if (var10 != null && (var1 instanceof TLRPC.TL_messages_sendMedia || var1 instanceof TLRPC.TL_messages_editMessage) && FileRefController.isFileRefError(var10.text)) {
         if (var2 != null) {
            FileRefController.getInstance(this.currentAccount).requestReference(var2, var1, var3, var4, var5, var6, var7);
            return;
         }

         if (var7 != null) {
            AndroidUtilities.runOnUIThread(new SendMessagesHelper$4(this, var8, var1, var7));
            return;
         }
      }

      if (var1 instanceof TLRPC.TL_messages_editMessage) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$C5CrRoHs0pjHmck8QByYuCo3o_s(this, var10, var8, var9, var3, var4, var1));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$ep4b2HrbYOUHrh_RuM2XFeYU5D8(this, var10, var8, var1, var9, var3, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$performSendMessageRequest$42$SendMessagesHelper(TLRPC.Message var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$s7kB2gRrnjLRX4z7Rxo0J3e9TgU(this, var1, var1.id));
   }

   // $FF: synthetic method
   public void lambda$performSendMessageRequestMulti$29$SendMessagesHelper(ArrayList var1, TLRPC.TL_messages_sendMultiMedia var2, ArrayList var3, ArrayList var4, SendMessagesHelper.DelayedMessage var5, TLObject var6, TLRPC.TL_error var7) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$GqwnlURs2UJWXkcpzjv2EBiLrD0(this, var7, var1, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$processUnsentMessages$43$SendMessagesHelper(ArrayList var1, ArrayList var2, ArrayList var3, ArrayList var4) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(this.currentAccount).putChats(var2, true);
      MessagesController.getInstance(this.currentAccount).putEncryptedChats(var3, true);

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         TLRPC.Message var6 = (TLRPC.Message)var4.get(var5);
         this.retrySendMessage(new MessageObject(this.currentAccount, var6, false), true);
      }

   }

   // $FF: synthetic method
   public void lambda$sendCallback$17$SendMessagesHelper(String var1, boolean var2, MessageObject var3, TLRPC.KeyboardButton var4, ChatActivity var5, TLObject[] var6, TLObject var7, TLRPC.TL_error var8) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$4H8dDsDRj446uqFYh4DKm4UlDEU(this, var1, var2, var7, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$sendGame$18$SendMessagesHelper(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var3, false);
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$sendMessage$9$SendMessagesHelper(long var1, boolean var3, boolean var4, LongSparseArray var5, ArrayList var6, ArrayList var7, TLRPC.Peer var8, TLRPC.TL_messages_forwardMessages var9, TLObject var10, TLRPC.TL_error var11) {
      int var13;
      if (var11 == null) {
         SparseLongArray var28 = new SparseLongArray();
         TLRPC.Updates var12 = (TLRPC.Updates)var10;

         int var14;
         for(var13 = 0; var13 < var12.updates.size(); var13 = var14 + 1) {
            TLRPC.Update var24 = (TLRPC.Update)var12.updates.get(var13);
            var14 = var13;
            if (var24 instanceof TLRPC.TL_updateMessageID) {
               TLRPC.TL_updateMessageID var25 = (TLRPC.TL_updateMessageID)var24;
               var28.put(var25.id, var25.random_id);
               var12.updates.remove(var13);
               var14 = var13 - 1;
            }
         }

         Integer var26 = (Integer)MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.get(var1);
         if (var26 == null) {
            var26 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var1);
            MessagesController.getInstance(this.currentAccount).dialogs_read_outbox_max.put(var1, var26);
         }

         var13 = 0;
         var14 = 0;

         for(SparseLongArray var27 = var28; var13 < var12.updates.size(); ++var13) {
            TLRPC.Update var29 = (TLRPC.Update)var12.updates.get(var13);
            boolean var16 = var29 instanceof TLRPC.TL_updateNewMessage;
            if (var16 || var29 instanceof TLRPC.TL_updateNewChannelMessage) {
               var12.updates.remove(var13);
               int var17 = var13 - 1;
               TLRPC.Message var30;
               if (var16) {
                  TLRPC.TL_updateNewMessage var18 = (TLRPC.TL_updateNewMessage)var29;
                  var30 = var18.message;
                  MessagesController.getInstance(this.currentAccount).processNewDifferenceParams(-1, var18.pts, -1, var18.pts_count);
               } else {
                  TLRPC.TL_updateNewChannelMessage var31 = (TLRPC.TL_updateNewChannelMessage)var29;
                  TLRPC.Message var32 = var31.message;
                  MessagesController.getInstance(this.currentAccount).processNewChannelDifferenceParams(var31.pts, var31.pts_count, var32.to_id.channel_id);
                  var30 = var32;
                  if (var3) {
                     var32.flags |= Integer.MIN_VALUE;
                     var30 = var32;
                  }
               }

               ImageLoader.saveMessageThumbs(var30);
               if (var26 < var30.id) {
                  var16 = true;
               } else {
                  var16 = false;
               }

               var30.unread = var16;
               if (var4) {
                  var30.out = true;
                  var30.unread = false;
                  var30.media_unread = false;
               }

               long var19 = var27.get(var30.id);
               var13 = var17;
               if (var19 != 0L) {
                  TLRPC.Message var21 = (TLRPC.Message)var5.get(var19);
                  if (var21 == null) {
                     var13 = var17;
                  } else {
                     var13 = var6.indexOf(var21);
                     if (var13 == -1) {
                        var13 = var17;
                     } else {
                        MessageObject var33 = (MessageObject)var7.get(var13);
                        var6.remove(var13);
                        var7.remove(var13);
                        var13 = var21.id;
                        ArrayList var15 = new ArrayList();
                        var15.add(var30);
                        this.updateMediaPaths(var33, var30, var30.id, (String)null, true);
                        int var22 = var33.getMediaExistanceFlags();
                        var21.id = var30.id;
                        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$AguYDqZ0V89DG_eBOiVamirE7iE(this, var21, var13, var8, var15, var1, var30, var22));
                        ++var14;
                        var13 = var17;
                     }
                  }
               }
            }
         }

         if (!var12.updates.isEmpty()) {
            MessagesController.getInstance(this.currentAccount).processUpdates(var12, false);
         }

         var13 = 0;
         StatsController.getInstance(this.currentAccount).incrementSentItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, var14);
      } else {
         var13 = 0;
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$l40Sz3n3XBnqm5WNgM6XcCJnSAw(this, var11, var9));
      }

      while(var13 < var6.size()) {
         TLRPC.Message var23 = (TLRPC.Message)var6.get(var13);
         MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(var23);
         AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$OpZpi03weztqM5vuYHFqnb91tB4(this, var23));
         ++var13;
      }

   }

   // $FF: synthetic method
   public void lambda$sendNotificationCallback$14$SendMessagesHelper(long var1, int var3, byte[] var4) {
      int var5 = (int)var1;
      StringBuilder var6 = new StringBuilder();
      var6.append(var1);
      var6.append("_");
      var6.append(var3);
      var6.append("_");
      var6.append(Utilities.bytesToHex(var4));
      var6.append("_");
      var6.append(0);
      String var9 = var6.toString();
      this.waitingForCallback.put(var9, true);
      if (var5 > 0) {
         if (MessagesController.getInstance(this.currentAccount).getUser(var5) == null) {
            TLRPC.User var7 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var5);
            if (var7 != null) {
               MessagesController.getInstance(this.currentAccount).putUser(var7, true);
            }
         }
      } else {
         MessagesController var10 = MessagesController.getInstance(this.currentAccount);
         int var8 = -var5;
         if (var10.getChat(var8) == null) {
            TLRPC.Chat var11 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var8);
            if (var11 != null) {
               MessagesController.getInstance(this.currentAccount).putChat(var11, true);
            }
         }
      }

      TLRPC.TL_messages_getBotCallbackAnswer var12 = new TLRPC.TL_messages_getBotCallbackAnswer();
      var12.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var5);
      var12.msg_id = var3;
      var12.game = false;
      if (var4 != null) {
         var12.flags |= 1;
         var12.data = var4;
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$SendMessagesHelper$t77TIdvR5oVS88nA5fB_WrU7owc(this, var9), 2);
      MessagesController.getInstance(this.currentAccount).markDialogAsRead(var1, var3, var3, 0, false, 0, true);
   }

   // $FF: synthetic method
   public void lambda$sendVote$15$SendMessagesHelper(MessageObject var1, String var2, Runnable var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         this.voteSendTime.put(var1.getPollId(), 0L);
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var4, false);
         this.voteSendTime.put(var1.getPollId(), SystemClock.uptimeMillis());
      }

      AndroidUtilities.runOnUIThread(new SendMessagesHelper$2(this, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$stopVideoService$22$SendMessagesHelper(String var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$p5AvMlaNmWbZ3rWK9wlPEhiSpDU(this, var1));
   }

   // $FF: synthetic method
   public void lambda$uploadMultiMedia$20$SendMessagesHelper(TLRPC.InputMedia var1, SendMessagesHelper.DelayedMessage var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg(this, var3, var1, var2));
   }

   protected void performSendMessageRequest(TLObject var1, MessageObject var2, String var3, SendMessagesHelper.DelayedMessage var4, boolean var5, SendMessagesHelper.DelayedMessage var6, Object var7) {
      if (!(var1 instanceof TLRPC.TL_messages_editMessage) && var5) {
         SendMessagesHelper.DelayedMessage var8 = this.findMaxDelayedMessageForMessageId(var2.getId(), var2.getDialogId());
         if (var8 != null) {
            var8.addDelayedRequest(var1, var2, var3, var7, var6);
            if (var4 != null) {
               ArrayList var11 = var4.requests;
               if (var11 != null) {
                  var8.requests.addAll(var11);
               }
            }

            return;
         }
      }

      TLRPC.Message var14 = var2.messageOwner;
      this.putToSendingMessages(var14);
      ConnectionsManager var9 = ConnectionsManager.getInstance(this.currentAccount);
      _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk var12 = new _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk(this, var1, var7, var2, var3, var4, var5, var6, var14);
      _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg var13 = new _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg(this, var14);
      short var10;
      if (var1 instanceof TLRPC.TL_messages_sendMessage) {
         var10 = 128;
      } else {
         var10 = 0;
      }

      var14.reqId = var9.sendRequest(var1, var12, var13, var10 | 68);
      if (var4 != null) {
         var4.sendDelayedRequests();
      }

   }

   protected void performSendMessageRequestMulti(TLRPC.TL_messages_sendMultiMedia var1, ArrayList var2, ArrayList var3, ArrayList var4, SendMessagesHelper.DelayedMessage var5) {
      int var6 = var2.size();

      for(int var7 = 0; var7 < var6; ++var7) {
         this.putToSendingMessages(((MessageObject)var2.get(var7)).messageOwner);
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$SendMessagesHelper$KTc_sr8270evlRbeKgXtKisOYgM(this, var4, var1, var2, var3, var5), (QuickAckDelegate)null, 68);
   }

   public void processForwardFromMyName(MessageObject var1, long var2) {
      if (var1 != null) {
         TLRPC.Message var4 = var1.messageOwner;
         TLRPC.MessageMedia var5 = var4.media;
         int var6;
         TLRPC.MessageMedia var9;
         ArrayList var12;
         if (var5 != null && !(var5 instanceof TLRPC.TL_messageMediaEmpty) && !(var5 instanceof TLRPC.TL_messageMediaWebPage) && !(var5 instanceof TLRPC.TL_messageMediaGame) && !(var5 instanceof TLRPC.TL_messageMediaInvoice)) {
            var6 = (int)var2;
            HashMap var13;
            if (var6 != 0 || var4.to_id == null || !(var5.photo instanceof TLRPC.TL_photo) && !(var5.document instanceof TLRPC.TL_document)) {
               var13 = null;
            } else {
               var13 = new HashMap();
               StringBuilder var14 = new StringBuilder();
               var14.append("sent_");
               var14.append(var1.messageOwner.to_id.channel_id);
               var14.append("_");
               var14.append(var1.getId());
               var13.put("parentObject", var14.toString());
            }

            TLRPC.Message var15 = var1.messageOwner;
            var5 = var15.media;
            TLRPC.Photo var8 = var5.photo;
            if (var8 instanceof TLRPC.TL_photo) {
               this.sendMessage((TLRPC.TL_photo)var8, (String)null, var2, var1.replyMessageObject, var15.message, var15.entities, (TLRPC.ReplyMarkup)null, var13, var5.ttl_seconds, var1);
            } else {
               TLRPC.Document var17 = var5.document;
               if (var17 instanceof TLRPC.TL_document) {
                  this.sendMessage((TLRPC.TL_document)var17, (VideoEditedInfo)null, var15.attachPath, var2, var1.replyMessageObject, var15.message, var15.entities, (TLRPC.ReplyMarkup)null, var13, var5.ttl_seconds, var1);
               } else if (!(var5 instanceof TLRPC.TL_messageMediaVenue) && !(var5 instanceof TLRPC.TL_messageMediaGeo)) {
                  if (var5.phone_number != null) {
                     TLRPC.TL_userContact_old2 var16 = new TLRPC.TL_userContact_old2();
                     var9 = var1.messageOwner.media;
                     var16.phone = var9.phone_number;
                     var16.first_name = var9.first_name;
                     var16.last_name = var9.last_name;
                     var16.id = var9.user_id;
                     this.sendMessage((TLRPC.User)var16, var2, var1.replyMessageObject, (TLRPC.ReplyMarkup)null, (HashMap)null);
                  } else if (var6 != 0) {
                     var12 = new ArrayList();
                     var12.add(var1);
                     this.sendMessage(var12, var2);
                  }
               } else {
                  this.sendMessage((TLRPC.MessageMedia)var1.messageOwner.media, var2, var1.replyMessageObject, (TLRPC.ReplyMarkup)null, (HashMap)null);
               }
            }
         } else {
            var4 = var1.messageOwner;
            if (var4.message != null) {
               var9 = var4.media;
               TLRPC.WebPage var10;
               if (var9 instanceof TLRPC.TL_messageMediaWebPage) {
                  var10 = var9.webpage;
               } else {
                  var10 = null;
               }

               ArrayList var11 = var1.messageOwner.entities;
               if (var11 != null && !var11.isEmpty()) {
                  var11 = new ArrayList();

                  for(var6 = 0; var6 < var1.messageOwner.entities.size(); ++var6) {
                     TLRPC.MessageEntity var7 = (TLRPC.MessageEntity)var1.messageOwner.entities.get(var6);
                     if (var7 instanceof TLRPC.TL_messageEntityBold || var7 instanceof TLRPC.TL_messageEntityItalic || var7 instanceof TLRPC.TL_messageEntityPre || var7 instanceof TLRPC.TL_messageEntityCode || var7 instanceof TLRPC.TL_messageEntityTextUrl) {
                        var11.add(var7);
                     }
                  }
               } else {
                  var11 = null;
               }

               this.sendMessage(var1.messageOwner.message, var2, var1.replyMessageObject, var10, true, var11, (TLRPC.ReplyMarkup)null, (HashMap)null);
            } else if ((int)var2 != 0) {
               var12 = new ArrayList();
               var12.add(var1);
               this.sendMessage(var12, var2);
            }
         }

      }
   }

   protected void processSentMessage(int var1) {
      int var2 = this.unsentMessages.size();
      this.unsentMessages.remove(var1);
      if (var2 != 0 && this.unsentMessages.size() == 0) {
         this.checkUnsentMessages();
      }

   }

   protected void processUnsentMessages(ArrayList var1, ArrayList var2, ArrayList var3, ArrayList var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$uLBi7R9ZOYopc_Gzh_Qk8zK9YAo(this, var2, var3, var4, var1));
   }

   protected void putToSendingMessages(TLRPC.Message var1) {
      this.sendingMessages.put(var1.id, var1);
   }

   protected TLRPC.Message removeFromSendingMessages(int var1) {
      TLRPC.Message var2 = (TLRPC.Message)this.sendingMessages.get(var1);
      if (var2 != null) {
         this.sendingMessages.remove(var1);
      }

      return var2;
   }

   public boolean retrySendMessage(MessageObject var1, boolean var2) {
      if (var1.getId() >= 0) {
         if (var1.isEditing()) {
            this.editMessageMedia(var1, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.TL_document)null, (String)null, (HashMap)null, true, var1);
         }

         return false;
      } else {
         TLRPC.MessageAction var3 = var1.messageOwner.action;
         TLRPC.Message var5;
         if (var3 instanceof TLRPC.TL_messageEncryptedAction) {
            int var4 = (int)(var1.getDialogId() >> 32);
            TLRPC.EncryptedChat var7 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var4);
            if (var7 == null) {
               MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(var1.messageOwner);
               var1.messageOwner.send_state = 2;
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var1.getId());
               this.processSentMessage(var1.getId());
               return false;
            } else {
               var5 = var1.messageOwner;
               if (var5.random_id == 0L) {
                  var5.random_id = this.getNextRandomId();
               }

               TLRPC.DecryptedMessageAction var8 = var1.messageOwner.action.encryptedAction;
               if (var8 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                  SecretChatHelper.getInstance(this.currentAccount).sendTTLMessage(var7, var1.messageOwner);
               } else if (var8 instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
                  SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(var7, (ArrayList)null, var1.messageOwner);
               } else if (var8 instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
                  SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(var7, var1.messageOwner);
               } else if (var8 instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
                  SecretChatHelper.getInstance(this.currentAccount).sendNotifyLayerMessage(var7, var1.messageOwner);
               } else if (var8 instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
                  SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(var7, (ArrayList)null, var1.messageOwner);
               } else if (var8 instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
                  SecretChatHelper.getInstance(this.currentAccount).sendScreenshotMessage(var7, (ArrayList)null, var1.messageOwner);
               } else if (!(var8 instanceof TLRPC.TL_decryptedMessageActionTyping) && !(var8 instanceof TLRPC.TL_decryptedMessageActionResend)) {
                  if (var8 instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                     SecretChatHelper.getInstance(this.currentAccount).sendCommitKeyMessage(var7, var1.messageOwner);
                  } else if (var8 instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                     SecretChatHelper.getInstance(this.currentAccount).sendAbortKeyMessage(var7, var1.messageOwner, 0L);
                  } else if (var8 instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                     SecretChatHelper.getInstance(this.currentAccount).sendRequestKeyMessage(var7, var1.messageOwner);
                  } else if (var8 instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                     SecretChatHelper.getInstance(this.currentAccount).sendAcceptKeyMessage(var7, var1.messageOwner);
                  } else if (var8 instanceof TLRPC.TL_decryptedMessageActionNoop) {
                     SecretChatHelper.getInstance(this.currentAccount).sendNoopMessage(var7, var1.messageOwner);
                  }
               }

               return true;
            }
         } else {
            if (var3 instanceof TLRPC.TL_messageActionScreenshotTaken) {
               TLRPC.User var6 = MessagesController.getInstance(this.currentAccount).getUser((int)var1.getDialogId());
               var5 = var1.messageOwner;
               this.sendScreenshotMessage(var6, var5.reply_to_msg_id, var5);
            }

            if (var2) {
               this.unsentMessages.put(var1.getId(), var1);
            }

            this.sendMessage(var1);
            return true;
         }
      }
   }

   public void sendCallback(boolean var1, MessageObject var2, TLRPC.KeyboardButton var3, ChatActivity var4) {
      if (var2 != null && var3 != null && var4 != null) {
         boolean var5;
         byte var6;
         label39: {
            var5 = var3 instanceof TLRPC.TL_keyboardButtonUrlAuth;
            if (var5) {
               var6 = 3;
            } else {
               if (!(var3 instanceof TLRPC.TL_keyboardButtonGame)) {
                  if (var3 instanceof TLRPC.TL_keyboardButtonBuy) {
                     var6 = 2;
                  } else {
                     var6 = 0;
                  }
                  break label39;
               }

               var6 = 1;
            }

            var1 = false;
         }

         StringBuilder var7 = new StringBuilder();
         var7.append(var2.getDialogId());
         var7.append("_");
         var7.append(var2.getId());
         var7.append("_");
         var7.append(Utilities.bytesToHex(var3.data));
         var7.append("_");
         var7.append(var6);
         String var8 = var7.toString();
         this.waitingForCallback.put(var8, true);
         TLObject[] var13 = new TLObject[1];
         _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY var12 = new _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY(this, var8, var1, var2, var3, var4, var13);
         if (var1) {
            MessagesStorage.getInstance(this.currentAccount).getBotCache(var8, var12);
         } else if (var5) {
            TLRPC.TL_messages_requestUrlAuth var15 = new TLRPC.TL_messages_requestUrlAuth();
            var15.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var2.getDialogId());
            var15.msg_id = var2.getId();
            var15.button_id = var3.button_id;
            var13[0] = var15;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var15, var12, 2);
         } else if (var3 instanceof TLRPC.TL_keyboardButtonBuy) {
            if ((var2.messageOwner.media.flags & 4) == 0) {
               TLRPC.TL_payments_getPaymentForm var10 = new TLRPC.TL_payments_getPaymentForm();
               var10.msg_id = var2.getId();
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, var12, 2);
            } else {
               TLRPC.TL_payments_getPaymentReceipt var11 = new TLRPC.TL_payments_getPaymentReceipt();
               var11.msg_id = var2.messageOwner.media.receipt_msg_id;
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, var12, 2);
            }
         } else {
            TLRPC.TL_messages_getBotCallbackAnswer var14 = new TLRPC.TL_messages_getBotCallbackAnswer();
            var14.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var2.getDialogId());
            var14.msg_id = var2.getId();
            var14.game = var3 instanceof TLRPC.TL_keyboardButtonGame;
            byte[] var9 = var3.data;
            if (var9 != null) {
               var14.flags |= 1;
               var14.data = var9;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var14, var12, 2);
         }
      }

   }

   public void sendCurrentLocation(MessageObject var1, TLRPC.KeyboardButton var2) {
      if (var1 != null && var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.getDialogId());
         var3.append("_");
         var3.append(var1.getId());
         var3.append("_");
         var3.append(Utilities.bytesToHex(var2.data));
         var3.append("_");
         String var4;
         if (var2 instanceof TLRPC.TL_keyboardButtonGame) {
            var4 = "1";
         } else {
            var4 = "0";
         }

         var3.append(var4);
         var4 = var3.toString();
         this.waitingForLocation.put(var4, var1);
         this.locationProvider.start();
      }

   }

   public void sendGame(TLRPC.InputPeer var1, TLRPC.TL_inputMediaGame var2, long var3, long var5) {
      if (var1 != null && var2 != null) {
         TLRPC.TL_messages_sendMedia var7 = new TLRPC.TL_messages_sendMedia();
         var7.peer = var1;
         if (var7.peer instanceof TLRPC.TL_inputPeerChannel) {
            SharedPreferences var8 = MessagesController.getNotificationsSettings(this.currentAccount);
            StringBuilder var9 = new StringBuilder();
            var9.append("silent_");
            var9.append(var1.channel_id);
            var7.silent = var8.getBoolean(var9.toString(), false);
         }

         long var10;
         if (var3 != 0L) {
            var10 = var3;
         } else {
            var10 = this.getNextRandomId();
         }

         var7.random_id = var10;
         var7.message = "";
         var7.media = var2;
         var10 = var5;
         if (var5 == 0L) {
            NativeByteBuffer var14;
            label36: {
               NativeByteBuffer var16;
               label35: {
                  Exception var15;
                  label34: {
                     try {
                        var16 = new NativeByteBuffer(var1.getObjectSize() + var2.getObjectSize() + 4 + 8);
                     } catch (Exception var13) {
                        var15 = var13;
                        var14 = null;
                        break label34;
                     }

                     try {
                        var16.writeInt32(3);
                        var16.writeInt64(var3);
                        var1.serializeToStream(var16);
                        var2.serializeToStream(var16);
                        break label35;
                     } catch (Exception var12) {
                        var15 = var12;
                        var14 = var16;
                     }
                  }

                  FileLog.e((Throwable)var15);
                  break label36;
               }

               var14 = var16;
            }

            var10 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var14);
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$SendMessagesHelper$ydKQSIv3UJTKVK2ad12P2kFNfXM(this, var10));
      }

   }

   public int sendMessage(ArrayList var1, long var2) {
      if (var1 != null && !var1.isEmpty()) {
         int var7 = (int)var2;
         byte var40;
         if (var7 != 0) {
            TLRPC.Peer var8 = MessagesController.getInstance(this.currentAccount).getPeer(var7);
            TLRPC.Chat var9;
            boolean var10;
            boolean var11;
            boolean var12;
            boolean var13;
            boolean var14;
            boolean var15;
            boolean var16;
            boolean var17;
            if (var7 > 0) {
               if (MessagesController.getInstance(this.currentAccount).getUser(var7) == null) {
                  return 0;
               }

               var9 = null;
               var10 = true;
               var11 = true;
               var12 = true;
               var13 = true;
               var14 = false;
               var15 = false;
            } else {
               var9 = MessagesController.getInstance(this.currentAccount).getChat(-var7);
               if (ChatObject.isChannel(var9)) {
                  var12 = var9.megagroup;
                  var13 = var9.signatures;
               } else {
                  var12 = false;
                  var13 = false;
               }

               var10 = ChatObject.canSendStickers(var9);
               var11 = ChatObject.canSendMedia(var9);
               var16 = ChatObject.canSendEmbed(var9);
               var17 = ChatObject.canSendPolls(var9);
               var15 = var13;
               var14 = var12;
               var13 = var16;
               var12 = var17;
            }

            LongSparseArray var18 = new LongSparseArray();
            ArrayList var19 = new ArrayList();
            ArrayList var20 = new ArrayList();
            ArrayList var21 = new ArrayList();
            ArrayList var22 = new ArrayList();
            LongSparseArray var23 = new LongSparseArray();
            TLRPC.InputPeer var24 = MessagesController.getInstance(this.currentAccount).getInputPeer(var7);
            var7 = UserConfig.getInstance(this.currentAccount).getClientUserId();
            TLRPC.Peer var4 = var8;
            long var25 = (long)var7;
            if (var2 == var25) {
               var16 = true;
            } else {
               var16 = false;
            }

            ArrayList var41 = var22;
            var40 = 0;
            int var27 = 0;
            TLRPC.Chat var28 = var9;

            for(ArrayList var42 = var19; var27 < var1.size(); var4 = var8) {
               LongSparseArray var39;
               ArrayList var51;
               LongSparseArray var60;
               label406: {
                  MessageObject var30 = (MessageObject)var1.get(var27);
                  long var5;
                  if (var30.getId() > 0 && !var30.needDrawBluredPreview()) {
                     label414: {
                        label410: {
                           if (!var10 && (var30.isSticker() || var30.isGif() || var30.isGame())) {
                              if (var40 == 0) {
                                 if (ChatObject.isActionBannedByDefault(var28, 8)) {
                                    var40 = 4;
                                 } else {
                                    var40 = 1;
                                 }
                                 break label410;
                              }

                              var19 = var42;
                           } else {
                              label402: {
                                 if (!var11) {
                                    TLRPC.MessageMedia var55 = var30.messageOwner.media;
                                    if (var55 instanceof TLRPC.TL_messageMediaPhoto || var55 instanceof TLRPC.TL_messageMediaDocument) {
                                       if (var40 == 0) {
                                          if (ChatObject.isActionBannedByDefault(var28, 7)) {
                                             var40 = 5;
                                          } else {
                                             var40 = 2;
                                          }
                                          break label410;
                                       }
                                       break label402;
                                    }
                                 }

                                 if (var12 || !(var30.messageOwner.media instanceof TLRPC.TL_messageMediaPoll)) {
                                    TLRPC.TL_message var31 = new TLRPC.TL_message();
                                    boolean var32;
                                    if (var30.getDialogId() == var25 && var30.messageOwner.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                       var32 = true;
                                    } else {
                                       var32 = false;
                                    }

                                    TLRPC.MessageFwdHeader var45;
                                    TLRPC.Message var46;
                                    String var64;
                                    int var69;
                                    if (var30.isForwarded()) {
                                       var31.fwd_from = new TLRPC.TL_messageFwdHeader();
                                       var45 = var31.fwd_from;
                                       TLRPC.MessageFwdHeader var62 = var30.messageOwner.fwd_from;
                                       var45.flags = var62.flags;
                                       var45.from_id = var62.from_id;
                                       var45.date = var62.date;
                                       var45.channel_id = var62.channel_id;
                                       var45.channel_post = var62.channel_post;
                                       var45.post_author = var62.post_author;
                                       var45.from_name = var62.from_name;
                                       var31.flags = 4;
                                    } else if (!var32) {
                                       var31.fwd_from = new TLRPC.TL_messageFwdHeader();
                                       var31.fwd_from.channel_post = var30.getId();
                                       var45 = var31.fwd_from;
                                       var45.flags |= 4;
                                       if (var30.isFromUser()) {
                                          var45 = var31.fwd_from;
                                          var45.from_id = var30.messageOwner.from_id;
                                          var45.flags |= 1;
                                       } else {
                                          var45 = var31.fwd_from;
                                          TLRPC.Message var63 = var30.messageOwner;
                                          var45.channel_id = var63.to_id.channel_id;
                                          var45.flags |= 2;
                                          if (var63.post) {
                                             var69 = var63.from_id;
                                             if (var69 > 0) {
                                                var45.from_id = var69;
                                                var45.flags |= 1;
                                             }
                                          }
                                       }

                                       var64 = var30.messageOwner.post_author;
                                       if (var64 != null) {
                                          var45 = var31.fwd_from;
                                          var45.post_author = var64;
                                          var45.flags |= 8;
                                       } else if (!var30.isOutOwner()) {
                                          var46 = var30.messageOwner;
                                          if (var46.from_id > 0 && var46.post) {
                                             TLRPC.User var47 = MessagesController.getInstance(this.currentAccount).getUser(var30.messageOwner.from_id);
                                             if (var47 != null) {
                                                var31.fwd_from.post_author = ContactsController.formatName(var47.first_name, var47.last_name);
                                                var45 = var31.fwd_from;
                                                var45.flags |= 8;
                                             }
                                          }
                                       }

                                       var31.date = var30.messageOwner.date;
                                       var31.flags = 4;
                                    }

                                    if (var2 == var25) {
                                       var45 = var31.fwd_from;
                                       if (var45 != null) {
                                          var45.flags |= 16;
                                          var45.saved_from_msg_id = var30.getId();
                                          var31.fwd_from.saved_from_peer = var30.messageOwner.to_id;
                                       }
                                    }

                                    if (!var13 && var30.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                                       var31.media = new TLRPC.TL_messageMediaEmpty();
                                    } else {
                                       var31.media = var30.messageOwner.media;
                                    }

                                    if (var31.media != null) {
                                       var31.flags |= 512;
                                    }

                                    if (var14) {
                                       var31.flags |= Integer.MIN_VALUE;
                                    }

                                    var69 = var30.messageOwner.via_bot_id;
                                    if (var69 != 0) {
                                       var31.via_bot_id = var69;
                                       var31.flags |= 2048;
                                    }

                                    var31.message = var30.messageOwner.message;
                                    var31.fwd_msg_id = var30.getId();
                                    var46 = var30.messageOwner;
                                    var31.attachPath = var46.attachPath;
                                    var31.entities = var46.entities;
                                    var5 = var25;
                                    int var33;
                                    if (var46.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
                                       var31.flags |= 64;
                                       var31.reply_markup = new TLRPC.TL_replyInlineMarkup();
                                       var33 = var30.messageOwner.reply_markup.rows.size();
                                       var69 = 0;

                                       while(true) {
                                          var5 = var25;
                                          if (var69 >= var33) {
                                             break;
                                          }

                                          TLRPC.TL_keyboardButtonRow var70 = (TLRPC.TL_keyboardButtonRow)var30.messageOwner.reply_markup.rows.get(var69);
                                          int var35 = var70.buttons.size();
                                          TLRPC.TL_keyboardButtonRow var48 = null;

                                          TLRPC.TL_keyboardButtonRow var65;
                                          for(int var36 = 0; var36 < var35; var48 = var65) {
                                             label389: {
                                                TLRPC.KeyboardButton var72 = (TLRPC.KeyboardButton)var70.buttons.get(var36);
                                                var17 = var72 instanceof TLRPC.TL_keyboardButtonUrlAuth;
                                                if (!var17 && !(var72 instanceof TLRPC.TL_keyboardButtonUrl)) {
                                                   var65 = var48;
                                                   if (!(var72 instanceof TLRPC.TL_keyboardButtonSwitchInline)) {
                                                      break label389;
                                                   }
                                                }

                                                Object var73 = var72;
                                                if (var17) {
                                                   var73 = new TLRPC.TL_keyboardButtonUrlAuth();
                                                   ((TLRPC.KeyboardButton)var73).flags = var72.flags;
                                                   var64 = var72.fwd_text;
                                                   if (var64 != null) {
                                                      ((TLRPC.KeyboardButton)var73).fwd_text = var64;
                                                      ((TLRPC.KeyboardButton)var73).text = var64;
                                                   } else {
                                                      ((TLRPC.KeyboardButton)var73).text = var72.text;
                                                   }

                                                   ((TLRPC.KeyboardButton)var73).url = var72.url;
                                                   ((TLRPC.KeyboardButton)var73).button_id = var72.button_id;
                                                }

                                                var65 = var48;
                                                if (var48 == null) {
                                                   var65 = new TLRPC.TL_keyboardButtonRow();
                                                   var31.reply_markup.rows.add(var65);
                                                }

                                                var65.buttons.add(var73);
                                             }

                                             ++var36;
                                          }

                                          ++var69;
                                       }
                                    }

                                    if (!var31.entities.isEmpty()) {
                                       var31.flags |= 128;
                                    }

                                    if (var31.attachPath == null) {
                                       var31.attachPath = "";
                                    }

                                    var69 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                                    var31.id = var69;
                                    var31.local_id = var69;
                                    var31.out = true;
                                    var25 = var30.messageOwner.grouped_id;
                                    if (var25 != 0L) {
                                       Long var66 = (Long)var18.get(var25);
                                       Long var49 = var66;
                                       if (var66 == null) {
                                          var49 = Utilities.random.nextLong();
                                          var18.put(var30.messageOwner.grouped_id, var49);
                                       }

                                       var31.grouped_id = var49;
                                       var31.flags |= 131072;
                                    }

                                    if (var27 != var1.size() - 1 && ((MessageObject)var1.get(var27 + 1)).messageOwner.grouped_id != var30.messageOwner.grouped_id) {
                                       var32 = true;
                                    } else {
                                       var32 = false;
                                    }

                                    var33 = var4.channel_id;
                                    if (var33 != 0 && !var14) {
                                       if (var15) {
                                          var33 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                       } else {
                                          var33 = -var33;
                                       }

                                       var31.from_id = var33;
                                       var31.post = true;
                                    } else {
                                       var31.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                       var31.flags |= 256;
                                    }

                                    if (var31.random_id == 0L) {
                                       var31.random_id = this.getNextRandomId();
                                    }

                                    var21.add(var31.random_id);
                                    var23.put(var31.random_id, var31);
                                    var41.add(var31.fwd_msg_id);
                                    var31.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                                    var17 = var24 instanceof TLRPC.TL_inputPeerChannel;
                                    if (var17 && !var14) {
                                       var31.views = 1;
                                       var31.flags |= 1024;
                                    } else {
                                       var46 = var30.messageOwner;
                                       if ((var46.flags & 1024) != 0) {
                                          var31.views = var46.views;
                                          var31.flags |= 1024;
                                       }

                                       var31.unread = true;
                                    }

                                    var31.dialog_id = var2;
                                    var31.to_id = var4;
                                    if (MessageObject.isVoiceMessage(var31) || MessageObject.isRoundVideoMessage(var31)) {
                                       if (var17 && var30.getChannelId() != 0) {
                                          var31.media_unread = var30.isContentUnread();
                                       } else {
                                          var31.media_unread = true;
                                       }
                                    }

                                    TLRPC.Peer var50 = var30.messageOwner.to_id;
                                    if (var50 instanceof TLRPC.TL_peerChannel) {
                                       var31.ttl = -var50.channel_id;
                                    }

                                    MessageObject var52 = new MessageObject(this.currentAccount, var31, true);
                                    var52.messageOwner.send_state = 1;
                                    var42.add(var52);
                                    var20.add(var31);
                                    this.putToSendingMessages(var31);
                                    StringBuilder var53;
                                    if (BuildVars.LOGS_ENABLED) {
                                       var53 = new StringBuilder();
                                       var53.append("forward message user_id = ");
                                       var53.append(var24.user_id);
                                       var53.append(" chat_id = ");
                                       var53.append(var24.chat_id);
                                       var53.append(" channel_id = ");
                                       var53.append(var24.channel_id);
                                       var53.append(" access_hash = ");
                                       var53.append(var24.access_hash);
                                       FileLog.d(var53.toString());
                                    }

                                    if ((!var32 || var20.size() <= 0) && var20.size() != 100 && var27 != var1.size() - 1 && (var27 == var1.size() - 1 || ((MessageObject)var1.get(var27 + 1)).getDialogId() == var30.getDialogId())) {
                                       var51 = var42;
                                       var39 = var23;
                                       var42 = var41;
                                       var8 = var4;
                                       var23 = var18;
                                       var25 = var5;
                                    } else {
                                       MessagesStorage.getInstance(this.currentAccount).putMessages(new ArrayList(var20), false, true, false, 0);
                                       MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(var2, var42);
                                       NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
                                       UserConfig.getInstance(this.currentAccount).saveConfig(false);
                                       TLRPC.TL_messages_forwardMessages var74 = new TLRPC.TL_messages_forwardMessages();
                                       var74.to_peer = var24;
                                       if (var25 != 0L) {
                                          var17 = true;
                                       } else {
                                          var17 = false;
                                       }

                                       var74.grouped = var17;
                                       if (var74.to_peer instanceof TLRPC.TL_inputPeerChannel) {
                                          SharedPreferences var56 = MessagesController.getNotificationsSettings(this.currentAccount);
                                          var53 = new StringBuilder();
                                          var53.append("silent_");
                                          var53.append(var2);
                                          var74.silent = var56.getBoolean(var53.toString(), false);
                                       }

                                       if (var30.messageOwner.to_id instanceof TLRPC.TL_peerChannel) {
                                          var9 = MessagesController.getInstance(this.currentAccount).getChat(var30.messageOwner.to_id.channel_id);
                                          var74.from_peer = new TLRPC.TL_inputPeerChannel();
                                          TLRPC.InputPeer var57 = var74.from_peer;
                                          var57.channel_id = var30.messageOwner.to_id.channel_id;
                                          if (var9 != null) {
                                             var57.access_hash = var9.access_hash;
                                          }
                                       } else {
                                          var74.from_peer = new TLRPC.TL_inputPeerEmpty();
                                       }

                                       var74.random_id = var21;
                                       var74.id = var41;
                                       if (var1.size() == 1 && ((MessageObject)var1.get(0)).messageOwner.with_my_score) {
                                          var17 = true;
                                       } else {
                                          var17 = false;
                                       }

                                       var74.with_my_score = var17;
                                       ConnectionsManager var68 = ConnectionsManager.getInstance(this.currentAccount);
                                       var51 = var42;
                                       var39 = var23;
                                       var42 = var41;
                                       TLRPC.InputPeer var58 = var24;
                                       var68.sendRequest(var74, new _$$Lambda$SendMessagesHelper$GtPQ6DFMMI1Gm_S7QANSsM7url8(this, var2, var14, var16, var23, var20, var51, var4, var74), 68);
                                       var8 = var4;
                                       var23 = var18;
                                       var25 = var5;
                                       var24 = var24;
                                       if (var27 != var1.size() - 1) {
                                          var42 = new ArrayList();
                                          var20 = new ArrayList();
                                          var21 = new ArrayList();
                                          var51 = new ArrayList();
                                          var39 = new LongSparseArray();
                                          var8 = var4;
                                          var23 = var18;
                                          var25 = var5;
                                          var24 = var58;
                                          break label406;
                                       }
                                    }
                                    break label414;
                                 }

                                 if (var40 == 0) {
                                    if (ChatObject.isActionBannedByDefault(var28, 10)) {
                                       var40 = 6;
                                    } else {
                                       var40 = 3;
                                    }
                                    break label410;
                                 }
                              }

                              var19 = var42;
                           }

                           var60 = var23;
                           var23 = var18;
                           var42 = var41;
                           var51 = var19;
                           var8 = var4;
                           var39 = var60;
                           break label414;
                        }

                        var51 = var41;
                        var39 = var23;
                        var8 = var4;
                        var23 = var18;
                        break label406;
                     }
                  } else {
                     var19 = var42;
                     var60 = var23;
                     TLRPC.InputPeer var34 = var24;
                     var5 = var25;
                     var42 = var41;
                     var51 = var19;
                     var8 = var4;
                     var23 = var18;
                     var25 = var25;
                     var39 = var60;
                     var24 = var24;
                     if (var30.type == 0) {
                        var42 = var41;
                        var51 = var19;
                        var8 = var4;
                        var23 = var18;
                        var25 = var5;
                        var39 = var60;
                        var24 = var34;
                        if (!TextUtils.isEmpty(var30.messageText)) {
                           TLRPC.MessageMedia var43 = var30.messageOwner.media;
                           TLRPC.WebPage var44;
                           if (var43 != null) {
                              var44 = var43.webpage;
                           } else {
                              var44 = null;
                           }

                           String var54 = var30.messageText.toString();
                           if (var44 != null) {
                              var17 = true;
                           } else {
                              var17 = false;
                           }

                           this.sendMessage(var54, var2, (MessageObject)null, var44, var17, var30.messageOwner.entities, (TLRPC.ReplyMarkup)null, (HashMap)null);
                           var24 = var34;
                           var39 = var60;
                           var25 = var5;
                           var23 = var18;
                           var8 = var4;
                           var51 = var19;
                           var42 = var41;
                        }
                     }
                  }

                  var19 = var42;
                  var42 = var51;
                  var51 = var19;
               }

               ++var27;
               var60 = var23;
               var41 = var51;
               var23 = var39;
               var18 = var60;
            }
         } else {
            for(var7 = 0; var7 < var1.size(); ++var7) {
               this.processForwardFromMyName((MessageObject)var1.get(var7), var2);
            }

            var40 = 0;
         }

         return var40;
      } else {
         return 0;
      }
   }

   public void sendMessage(String var1, long var2, MessageObject var4, TLRPC.WebPage var5, boolean var6, ArrayList var7, TLRPC.ReplyMarkup var8, HashMap var9) {
      this.sendMessage(var1, (String)null, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var2, (String)null, var4, var5, var6, (MessageObject)null, var7, var8, var9, 0, (Object)null);
   }

   public void sendMessage(MessageObject var1) {
      long var2 = var1.getDialogId();
      TLRPC.Message var4 = var1.messageOwner;
      this.sendMessage((String)null, (String)null, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var2, var4.attachPath, (MessageObject)null, (TLRPC.WebPage)null, true, var1, (ArrayList)null, var4.reply_markup, var4.params, 0, (Object)null);
   }

   public void sendMessage(TLRPC.MessageMedia var1, long var2, MessageObject var4, TLRPC.ReplyMarkup var5, HashMap var6) {
      this.sendMessage((String)null, (String)null, var1, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var2, (String)null, var4, (TLRPC.WebPage)null, true, (MessageObject)null, (ArrayList)null, var5, var6, 0, (Object)null);
   }

   public void sendMessage(TLRPC.TL_document var1, VideoEditedInfo var2, String var3, long var4, MessageObject var6, String var7, ArrayList var8, TLRPC.ReplyMarkup var9, HashMap var10, int var11, Object var12) {
      this.sendMessage((String)null, var7, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, var2, (TLRPC.User)null, var1, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var4, var3, var6, (TLRPC.WebPage)null, true, (MessageObject)null, var8, var9, var10, var11, var12);
   }

   public void sendMessage(TLRPC.TL_game var1, long var2, TLRPC.ReplyMarkup var4, HashMap var5) {
      this.sendMessage((String)null, (String)null, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, var1, (TLRPC.TL_messageMediaPoll)null, var2, (String)null, (MessageObject)null, (TLRPC.WebPage)null, true, (MessageObject)null, (ArrayList)null, var4, var5, 0, (Object)null);
   }

   public void sendMessage(TLRPC.TL_messageMediaPoll var1, long var2, MessageObject var4, TLRPC.ReplyMarkup var5, HashMap var6) {
      this.sendMessage((String)null, (String)null, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, (TLRPC.TL_game)null, var1, var2, (String)null, var4, (TLRPC.WebPage)null, true, (MessageObject)null, (ArrayList)null, var5, var6, 0, (Object)null);
   }

   public void sendMessage(TLRPC.TL_photo var1, String var2, long var3, MessageObject var5, String var6, ArrayList var7, TLRPC.ReplyMarkup var8, HashMap var9, int var10, Object var11) {
      this.sendMessage((String)null, var6, (TLRPC.MessageMedia)null, var1, (VideoEditedInfo)null, (TLRPC.User)null, (TLRPC.TL_document)null, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var3, var2, var5, (TLRPC.WebPage)null, true, (MessageObject)null, var7, var8, var9, var10, var11);
   }

   public void sendMessage(TLRPC.User var1, long var2, MessageObject var4, TLRPC.ReplyMarkup var5, HashMap var6) {
      this.sendMessage((String)null, (String)null, (TLRPC.MessageMedia)null, (TLRPC.TL_photo)null, (VideoEditedInfo)null, var1, (TLRPC.TL_document)null, (TLRPC.TL_game)null, (TLRPC.TL_messageMediaPoll)null, var2, (String)null, var4, (TLRPC.WebPage)null, true, (MessageObject)null, (ArrayList)null, var5, var6, 0, (Object)null);
   }

   public void sendNotificationCallback(long var1, int var3, byte[] var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$HbRNU4Jc_Y0XpAaKW1pVIkZttzI(this, var1, var3, var4));
   }

   public void sendScreenshotMessage(TLRPC.User var1, int var2, TLRPC.Message var3) {
      if (var1 != null && var2 != 0 && var1.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
         TLRPC.TL_messages_sendScreenshotNotification var4 = new TLRPC.TL_messages_sendScreenshotNotification();
         var4.peer = new TLRPC.TL_inputPeerUser();
         TLRPC.InputPeer var5 = var4.peer;
         var5.access_hash = var1.access_hash;
         var5.user_id = var1.id;
         if (var3 != null) {
            var4.reply_to_msg_id = var2;
            var4.random_id = ((TLRPC.Message)var3).random_id;
         } else {
            var3 = new TLRPC.TL_messageService();
            ((TLRPC.Message)var3).random_id = this.getNextRandomId();
            ((TLRPC.Message)var3).dialog_id = (long)var1.id;
            ((TLRPC.Message)var3).unread = true;
            ((TLRPC.Message)var3).out = true;
            int var6 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            ((TLRPC.Message)var3).id = var6;
            ((TLRPC.Message)var3).local_id = var6;
            ((TLRPC.Message)var3).from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            ((TLRPC.Message)var3).flags |= 256;
            ((TLRPC.Message)var3).flags |= 8;
            ((TLRPC.Message)var3).reply_to_msg_id = var2;
            ((TLRPC.Message)var3).to_id = new TLRPC.TL_peerUser();
            ((TLRPC.Message)var3).to_id.user_id = var1.id;
            ((TLRPC.Message)var3).date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            ((TLRPC.Message)var3).action = new TLRPC.TL_messageActionScreenshotTaken();
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
         }

         var4.random_id = ((TLRPC.Message)var3).random_id;
         MessageObject var7 = new MessageObject(this.currentAccount, (TLRPC.Message)var3, false);
         var7.messageOwner.send_state = 1;
         ArrayList var8 = new ArrayList();
         var8.add(var7);
         MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(((TLRPC.Message)var3).dialog_id, var8);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         var8 = new ArrayList();
         var8.add(var3);
         MessagesStorage.getInstance(this.currentAccount).putMessages(var8, false, true, false, 0);
         this.performSendMessageRequest(var4, var7, (String)null, (SendMessagesHelper.DelayedMessage)null, (Object)null);
      }

   }

   public void sendSticker(TLRPC.Document var1, long var2, MessageObject var4, Object var5) {
      if (var1 != null) {
         if ((int)var2 == 0) {
            int var6 = (int)(var2 >> 32);
            if (MessagesController.getInstance(this.currentAccount).getEncryptedChat(var6) == null) {
               return;
            }

            TLRPC.TL_document_layer82 var7 = new TLRPC.TL_document_layer82();
            var7.id = ((TLRPC.Document)var1).id;
            var7.access_hash = ((TLRPC.Document)var1).access_hash;
            var7.date = ((TLRPC.Document)var1).date;
            var7.mime_type = ((TLRPC.Document)var1).mime_type;
            var7.file_reference = ((TLRPC.Document)var1).file_reference;
            if (var7.file_reference == null) {
               var7.file_reference = new byte[0];
            }

            var7.size = ((TLRPC.Document)var1).size;
            var7.dc_id = ((TLRPC.Document)var1).dc_id;
            var7.attributes = new ArrayList(((TLRPC.Document)var1).attributes);
            if (var7.mime_type == null) {
               var7.mime_type = "";
            }

            TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Document)var1).thumbs, 90);
            if (var8 instanceof TLRPC.TL_photoSize) {
               File var9 = FileLoader.getPathToAttach(var8, true);
               if (var9.exists()) {
                  try {
                     var9.length();
                     byte[] var12 = new byte[(int)var9.length()];
                     RandomAccessFile var10 = new RandomAccessFile(var9, "r");
                     var10.readFully(var12);
                     TLRPC.TL_photoCachedSize var15 = new TLRPC.TL_photoCachedSize();
                     TLRPC.TL_fileLocation_layer82 var14 = new TLRPC.TL_fileLocation_layer82();
                     var14.dc_id = var8.location.dc_id;
                     var14.volume_id = var8.location.volume_id;
                     var14.local_id = var8.location.local_id;
                     var14.secret = var8.location.secret;
                     var15.location = var14;
                     var15.size = var8.size;
                     var15.w = var8.w;
                     var15.h = var8.h;
                     var15.type = var8.type;
                     var15.bytes = var12;
                     var7.thumbs.add(var15);
                     var7.flags |= 1;
                  } catch (Exception var11) {
                     FileLog.e((Throwable)var11);
                  }
               }
            }

            if (var7.thumbs.isEmpty()) {
               TLRPC.TL_photoSizeEmpty var13 = new TLRPC.TL_photoSizeEmpty();
               var13.type = "s";
               var7.thumbs.add(var13);
            }

            var1 = var7;
         }

         if (var1 instanceof TLRPC.TL_document) {
            this.sendMessage((TLRPC.TL_document)var1, (VideoEditedInfo)null, (String)null, var2, var4, (String)null, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null, 0, var5);
         }

      }
   }

   public int sendVote(MessageObject var1, TLRPC.TL_pollAnswer var2, Runnable var3) {
      if (var1 == null) {
         return 0;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("poll_");
         var4.append(var1.getPollId());
         String var5 = var4.toString();
         if (this.waitingForCallback.containsKey(var5)) {
            return 0;
         } else {
            HashMap var6 = this.waitingForVote;
            byte[] var7;
            if (var2 != null) {
               var7 = var2.option;
            } else {
               var7 = new byte[0];
            }

            var6.put(var5, var7);
            TLRPC.TL_messages_sendVote var8 = new TLRPC.TL_messages_sendVote();
            var8.msg_id = var1.getId();
            var8.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var1.getDialogId());
            if (var2 != null) {
               var8.options.add(var2.option);
            }

            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY(this, var1, var5, var3));
         }
      }
   }

   public void setCurrentChatInfo(TLRPC.ChatFull var1) {
      this.currentChatInfo = var1;
   }

   protected void stopVideoService(String var1) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$30d877KfsrPIYnw1mIlTicl4KVY(this, var1));
   }

   protected class DelayedMessage {
      public TLRPC.EncryptedChat encryptedChat;
      public HashMap extraHashMap;
      public int finalGroupMessage;
      public long groupId;
      public String httpLocation;
      public ArrayList httpLocations;
      public ArrayList inputMedias;
      public TLRPC.InputMedia inputUploadMedia;
      public TLObject locationParent;
      public ArrayList locations;
      public ArrayList messageObjects;
      public ArrayList messages;
      public MessageObject obj;
      public String originalPath;
      public ArrayList originalPaths;
      public Object parentObject;
      public ArrayList parentObjects;
      public long peer;
      public boolean performMediaUpload;
      public TLRPC.PhotoSize photoSize;
      ArrayList requests;
      public TLObject sendEncryptedRequest;
      public TLObject sendRequest;
      public int type;
      public VideoEditedInfo videoEditedInfo;
      public ArrayList videoEditedInfos;

      public DelayedMessage(long var2) {
         this.peer = var2;
      }

      public void addDelayedRequest(TLObject var1, ArrayList var2, ArrayList var3, ArrayList var4, SendMessagesHelper.DelayedMessage var5) {
         SendMessagesHelper.DelayedMessageSendAfterRequest var6 = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
         var6.request = var1;
         var6.msgObjs = var2;
         var6.originalPaths = var3;
         var6.delayedMessage = var5;
         var6.parentObjects = var4;
         if (this.requests == null) {
            this.requests = new ArrayList();
         }

         this.requests.add(var6);
      }

      public void addDelayedRequest(TLObject var1, MessageObject var2, String var3, Object var4, SendMessagesHelper.DelayedMessage var5) {
         SendMessagesHelper.DelayedMessageSendAfterRequest var6 = SendMessagesHelper.this.new DelayedMessageSendAfterRequest();
         var6.request = var1;
         var6.msgObj = var2;
         var6.originalPath = var3;
         var6.delayedMessage = var5;
         var6.parentObject = var4;
         if (this.requests == null) {
            this.requests = new ArrayList();
         }

         this.requests.add(var6);
      }

      public void initForGroup(long var1) {
         this.type = 4;
         this.groupId = var1;
         this.messageObjects = new ArrayList();
         this.messages = new ArrayList();
         this.inputMedias = new ArrayList();
         this.originalPaths = new ArrayList();
         this.parentObjects = new ArrayList();
         this.extraHashMap = new HashMap();
         this.locations = new ArrayList();
         this.httpLocations = new ArrayList();
         this.videoEditedInfos = new ArrayList();
      }

      public void markAsError() {
         if (this.type == 4) {
            for(int var1 = 0; var1 < this.messageObjects.size(); ++var1) {
               MessageObject var2 = (MessageObject)this.messageObjects.get(var1);
               MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(var2.messageOwner);
               var2.messageOwner.send_state = 2;
               NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, var2.getId());
               SendMessagesHelper.this.processSentMessage(var2.getId());
            }

            HashMap var4 = SendMessagesHelper.this.delayedMessages;
            StringBuilder var3 = new StringBuilder();
            var3.append("group_");
            var3.append(this.groupId);
            var4.remove(var3.toString());
         } else {
            MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(this.obj.messageOwner);
            this.obj.messageOwner.send_state = 2;
            NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, this.obj.getId());
            SendMessagesHelper.this.processSentMessage(this.obj.getId());
         }

         this.sendDelayedRequests();
      }

      public void sendDelayedRequests() {
         if (this.requests != null) {
            int var1 = this.type;
            if (var1 == 4 || var1 == 0) {
               int var2 = this.requests.size();

               for(var1 = 0; var1 < var2; ++var1) {
                  SendMessagesHelper.DelayedMessageSendAfterRequest var3 = (SendMessagesHelper.DelayedMessageSendAfterRequest)this.requests.get(var1);
                  TLObject var4 = var3.request;
                  if (var4 instanceof TLRPC.TL_messages_sendEncryptedMultiMedia) {
                     SecretChatHelper.getInstance(SendMessagesHelper.this.currentAccount).performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia)var3.request, this);
                  } else if (var4 instanceof TLRPC.TL_messages_sendMultiMedia) {
                     SendMessagesHelper.this.performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia)var4, var3.msgObjs, var3.originalPaths, var3.parentObjects, var3.delayedMessage);
                  } else {
                     SendMessagesHelper.this.performSendMessageRequest(var4, var3.msgObj, var3.originalPath, var3.delayedMessage, var3.parentObject);
                  }
               }

               this.requests = null;
            }
         }

      }
   }

   protected class DelayedMessageSendAfterRequest {
      public SendMessagesHelper.DelayedMessage delayedMessage;
      public MessageObject msgObj;
      public ArrayList msgObjs;
      public String originalPath;
      public ArrayList originalPaths;
      public Object parentObject;
      public ArrayList parentObjects;
      public TLObject request;
   }

   public static class LocationProvider {
      private SendMessagesHelper.LocationProvider.LocationProviderDelegate delegate;
      private SendMessagesHelper.LocationProvider.GpsLocationListener gpsLocationListener = new SendMessagesHelper.LocationProvider.GpsLocationListener();
      private Location lastKnownLocation;
      private LocationManager locationManager;
      private Runnable locationQueryCancelRunnable;
      private SendMessagesHelper.LocationProvider.GpsLocationListener networkLocationListener = new SendMessagesHelper.LocationProvider.GpsLocationListener();

      public LocationProvider() {
      }

      public LocationProvider(SendMessagesHelper.LocationProvider.LocationProviderDelegate var1) {
         this.delegate = var1;
      }

      private void cleanup() {
         this.locationManager.removeUpdates(this.gpsLocationListener);
         this.locationManager.removeUpdates(this.networkLocationListener);
         this.lastKnownLocation = null;
         this.locationQueryCancelRunnable = null;
      }

      public void setDelegate(SendMessagesHelper.LocationProvider.LocationProviderDelegate var1) {
         this.delegate = var1;
      }

      public void start() {
         if (this.locationManager == null) {
            this.locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
         }

         try {
            this.locationManager.requestLocationUpdates("gps", 1L, 0.0F, this.gpsLocationListener);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         try {
            this.locationManager.requestLocationUpdates("network", 1L, 0.0F, this.networkLocationListener);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         try {
            this.lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
            if (this.lastKnownLocation == null) {
               this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
            }
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         Runnable var1 = this.locationQueryCancelRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
         }

         this.locationQueryCancelRunnable = new Runnable() {
            public void run() {
               if (LocationProvider.this.locationQueryCancelRunnable == this) {
                  if (LocationProvider.this.delegate != null) {
                     if (LocationProvider.this.lastKnownLocation != null) {
                        LocationProvider.this.delegate.onLocationAcquired(LocationProvider.this.lastKnownLocation);
                     } else {
                        LocationProvider.this.delegate.onUnableLocationAcquire();
                     }
                  }

                  LocationProvider.this.cleanup();
               }
            }
         };
         AndroidUtilities.runOnUIThread(this.locationQueryCancelRunnable, 5000L);
      }

      public void stop() {
         if (this.locationManager != null) {
            Runnable var1 = this.locationQueryCancelRunnable;
            if (var1 != null) {
               AndroidUtilities.cancelRunOnUIThread(var1);
            }

            this.cleanup();
         }
      }

      private class GpsLocationListener implements LocationListener {
         private GpsLocationListener() {
         }

         // $FF: synthetic method
         GpsLocationListener(Object var2) {
            this();
         }

         public void onLocationChanged(Location var1) {
            if (var1 != null && LocationProvider.this.locationQueryCancelRunnable != null) {
               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var2 = new StringBuilder();
                  var2.append("found location ");
                  var2.append(var1);
                  FileLog.d(var2.toString());
               }

               LocationProvider.this.lastKnownLocation = var1;
               if (var1.getAccuracy() < 100.0F) {
                  if (LocationProvider.this.delegate != null) {
                     LocationProvider.this.delegate.onLocationAcquired(var1);
                  }

                  if (LocationProvider.this.locationQueryCancelRunnable != null) {
                     AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                  }

                  LocationProvider.this.cleanup();
               }
            }

         }

         public void onProviderDisabled(String var1) {
         }

         public void onProviderEnabled(String var1) {
         }

         public void onStatusChanged(String var1, int var2, Bundle var3) {
         }
      }

      public interface LocationProviderDelegate {
         void onLocationAcquired(Location var1);

         void onUnableLocationAcquire();
      }
   }

   private static class MediaSendPrepareWorker {
      public volatile String parentObject;
      public volatile TLRPC.TL_photo photo;
      public CountDownLatch sync;

      private MediaSendPrepareWorker() {
      }

      // $FF: synthetic method
      MediaSendPrepareWorker(Object var1) {
         this();
      }
   }

   public static class SendingMediaInfo {
      public boolean canDeleteAfter;
      public String caption;
      public ArrayList entities;
      public boolean isVideo;
      public ArrayList masks;
      public String path;
      public MediaController.SearchImage searchImage;
      public int ttl;
      public Uri uri;
      public VideoEditedInfo videoEditedInfo;
   }
}
