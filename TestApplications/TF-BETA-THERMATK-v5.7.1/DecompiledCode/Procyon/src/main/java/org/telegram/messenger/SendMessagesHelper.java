// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Bundle;
import android.location.LocationListener;
import android.location.LocationManager;
import java.io.RandomAccessFile;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.QuickAckDelegate;
import android.os.SystemClock;
import org.telegram.messenger.support.SparseLongArray;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import org.telegram.tgnet.ConnectionsManager;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;
import android.graphics.Point;
import java.util.Locale;
import android.graphics.Bitmap$Config;
import android.graphics.Rect;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import android.graphics.BitmapFactory$Options;
import java.util.Iterator;
import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import org.telegram.tgnet.AbstractSerializedData;
import android.text.TextUtils;
import android.util.Base64;
import org.telegram.tgnet.SerializedData;
import android.graphics.Bitmap;
import android.media.MediaCodecInfo;
import java.io.File;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.tgnet.TLObject;
import java.util.Map;
import android.location.Location;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import android.os.Build$VERSION;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import java.util.concurrent.ThreadPoolExecutor;

public class SendMessagesHelper implements NotificationCenterDelegate
{
    private static volatile SendMessagesHelper[] Instance;
    private static DispatchQueue mediaSendQueue;
    private static ThreadPoolExecutor mediaSendThreadPool;
    private int currentAccount;
    private TLRPC.ChatFull currentChatInfo;
    private HashMap<String, ArrayList<DelayedMessage>> delayedMessages;
    private LocationProvider locationProvider;
    private SparseArray<TLRPC.Message> sendingMessages;
    private SparseArray<MessageObject> unsentMessages;
    private LongSparseArray<Long> voteSendTime;
    private HashMap<String, Boolean> waitingForCallback;
    private HashMap<String, MessageObject> waitingForLocation;
    private HashMap<String, byte[]> waitingForVote;
    
    static {
        SendMessagesHelper.mediaSendQueue = new DispatchQueue("mediaSendQueue");
        int availableProcessors;
        if (Build$VERSION.SDK_INT >= 17) {
            availableProcessors = Runtime.getRuntime().availableProcessors();
        }
        else {
            availableProcessors = 2;
        }
        SendMessagesHelper.mediaSendThreadPool = new ThreadPoolExecutor(availableProcessors, availableProcessors, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        SendMessagesHelper.Instance = new SendMessagesHelper[3];
    }
    
    public SendMessagesHelper(final int currentAccount) {
        this.currentChatInfo = null;
        this.delayedMessages = new HashMap<String, ArrayList<DelayedMessage>>();
        this.unsentMessages = (SparseArray<MessageObject>)new SparseArray();
        this.sendingMessages = (SparseArray<TLRPC.Message>)new SparseArray();
        this.waitingForLocation = new HashMap<String, MessageObject>();
        this.waitingForCallback = new HashMap<String, Boolean>();
        this.waitingForVote = new HashMap<String, byte[]>();
        this.voteSendTime = (LongSparseArray<Long>)new LongSparseArray();
        this.locationProvider = new LocationProvider((LocationProviderDelegate)new LocationProviderDelegate() {
            @Override
            public void onLocationAcquired(final Location location) {
                SendMessagesHelper.this.sendLocation(location);
                SendMessagesHelper.this.waitingForLocation.clear();
            }
            
            @Override
            public void onUnableLocationAcquire() {
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.wasUnableToFindCurrentLocation, new HashMap(SendMessagesHelper.this.waitingForLocation));
                SendMessagesHelper.this.waitingForLocation.clear();
            }
        });
        this.currentAccount = currentAccount;
        AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$F3OpGpLNH47E9LDuBXXNWIdgYDE(this));
    }
    
    private static VideoEditedInfo createCompressionSettings(final String s) {
        final int[] array = new int[9];
        AnimatedFileDrawable.getVideoInfo(s, array);
        if (array[0] == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("video hasn't avc1 atom");
            }
            return null;
        }
        final int bitrate = array[3];
        final int n = array[3];
        final float n2 = (float)array[4];
        final long n3 = array[6];
        final long n4 = array[5];
        final int framerate = array[7];
        final int n5 = 900000;
        int bitrate2;
        if ((bitrate2 = n) > 900000) {
            bitrate2 = 900000;
        }
        Label_0252: {
            if (Build$VERSION.SDK_INT >= 18) {
                break Label_0252;
            }
            try {
                final MediaCodecInfo selectCodec = MediaController.selectCodec("video/avc");
                if (selectCodec == null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("no codec info for video/avc");
                    }
                    return null;
                }
                final String name = selectCodec.getName();
                if (name.equals("OMX.google.h264.encoder") || name.equals("OMX.ST.VFM.H264Enc") || name.equals("OMX.Exynos.avc.enc") || name.equals("OMX.MARVELL.VIDEO.HW.CODA7542ENCODER") || name.equals("OMX.MARVELL.VIDEO.H264ENCODER") || name.equals("OMX.k3.video.encoder.avc") || name.equals("OMX.TI.DUCATI1.VIDEO.H264E")) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unsupported encoder = ");
                        sb.append(name);
                        FileLog.d(sb.toString());
                    }
                    return null;
                }
                if (MediaController.selectColorFormat(selectCodec, "video/avc") == 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("no color format for video/avc");
                    }
                    return null;
                }
                final VideoEditedInfo videoEditedInfo = new VideoEditedInfo();
                videoEditedInfo.startTime = -1L;
                videoEditedInfo.endTime = -1L;
                videoEditedInfo.bitrate = bitrate2;
                videoEditedInfo.originalPath = s;
                videoEditedInfo.framerate = framerate;
                videoEditedInfo.estimatedDuration = (long)Math.ceil(n2);
                final int n6 = array[1];
                videoEditedInfo.originalWidth = n6;
                videoEditedInfo.resultWidth = n6;
                final int n7 = array[2];
                videoEditedInfo.originalHeight = n7;
                videoEditedInfo.resultHeight = n7;
                videoEditedInfo.rotationValue = array[8];
                final int int1 = MessagesController.getGlobalMainSettings().getInt("compress_video2", 1);
                final int originalWidth = videoEditedInfo.originalWidth;
                int n8 = 0;
                Label_0471: {
                    if (originalWidth <= 1280) {
                        final int originalHeight = videoEditedInfo.originalHeight;
                        if (originalHeight <= 1280) {
                            if (originalWidth > 848 || originalHeight > 848) {
                                n8 = 4;
                                break Label_0471;
                            }
                            if (originalWidth > 640 || originalHeight > 640) {
                                n8 = 3;
                                break Label_0471;
                            }
                            if (originalWidth <= 480 && originalHeight <= 480) {
                                n8 = 1;
                                break Label_0471;
                            }
                            n8 = 2;
                            break Label_0471;
                        }
                    }
                    n8 = 5;
                }
                int n9 = int1;
                if (int1 >= n8) {
                    n9 = n8 - 1;
                }
                final int n10 = n8 - 1;
                int min = bitrate2;
                long n11 = n3;
                if (n9 != n10) {
                    int a;
                    float n12;
                    if (n9 != 0) {
                        if (n9 != 1) {
                            if (n9 != 2) {
                                a = 2500000;
                                n12 = 1280.0f;
                            }
                            else {
                                n12 = 848.0f;
                                a = 1100000;
                            }
                        }
                        else {
                            n12 = 640.0f;
                            a = n5;
                        }
                    }
                    else {
                        n12 = 432.0f;
                        a = 400000;
                    }
                    final int originalWidth2 = videoEditedInfo.originalWidth;
                    final int originalHeight2 = videoEditedInfo.originalHeight;
                    float n13;
                    if (originalWidth2 > originalHeight2) {
                        n13 = (float)originalWidth2;
                    }
                    else {
                        n13 = (float)originalHeight2;
                    }
                    final float n14 = n12 / n13;
                    videoEditedInfo.resultWidth = Math.round(videoEditedInfo.originalWidth * n14 / 2.0f) * 2;
                    videoEditedInfo.resultHeight = Math.round(videoEditedInfo.originalHeight * n14 / 2.0f) * 2;
                    min = bitrate2;
                    n11 = n3;
                    if (bitrate2 != 0) {
                        min = Math.min(a, (int)(bitrate / n14));
                        n11 = (long)(min / 8 * n2 / 1000.0f);
                    }
                }
                if (n9 == n10) {
                    videoEditedInfo.resultWidth = videoEditedInfo.originalWidth;
                    videoEditedInfo.resultHeight = videoEditedInfo.originalHeight;
                    videoEditedInfo.bitrate = bitrate;
                    videoEditedInfo.estimatedSize = (int)new File(s).length();
                }
                else {
                    videoEditedInfo.bitrate = min;
                    videoEditedInfo.estimatedSize = (int)(n4 + n11);
                    final long estimatedSize = videoEditedInfo.estimatedSize;
                    videoEditedInfo.estimatedSize = estimatedSize + estimatedSize / 32768L * 16L;
                }
                if (videoEditedInfo.estimatedSize == 0L) {
                    videoEditedInfo.estimatedSize = 1L;
                }
                return videoEditedInfo;
            }
            catch (Exception ex) {
                return null;
            }
        }
    }
    
    private static Bitmap createVideoThumbnail(final String p0, final long p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   android/media/MediaMetadataRetriever.<init>:()V
        //     7: astore_3       
        //     8: aload_3        
        //     9: aload_0        
        //    10: invokevirtual   android/media/MediaMetadataRetriever.setDataSource:(Ljava/lang/String;)V
        //    13: aload_3        
        //    14: lload_1        
        //    15: iconst_1       
        //    16: invokevirtual   android/media/MediaMetadataRetriever.getFrameAtTime:(JI)Landroid/graphics/Bitmap;
        //    19: astore_0       
        //    20: aload_3        
        //    21: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    24: goto            41
        //    27: astore_0       
        //    28: aload_3        
        //    29: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    32: aload_0        
        //    33: athrow         
        //    34: astore_0       
        //    35: aload_3        
        //    36: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //    39: aconst_null    
        //    40: astore_0       
        //    41: aload_0        
        //    42: ifnonnull       47
        //    45: aconst_null    
        //    46: areturn        
        //    47: aload_0        
        //    48: areturn        
        //    49: astore_3       
        //    50: goto            41
        //    53: astore_3       
        //    54: goto            32
        //    57: astore_0       
        //    58: goto            39
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  8      20     34     41     Ljava/lang/Exception;
        //  8      20     27     34     Any
        //  20     24     49     53     Ljava/lang/RuntimeException;
        //  28     32     53     57     Ljava/lang/RuntimeException;
        //  35     39     57     61     Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 37 out-of-bounds for length 37
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void editMessageMedia(final MessageObject messageObject, TLRPC.TL_photo photo, VideoEditedInfo videoEditedInfo, final TLRPC.TL_document tl_document, String attachPath, HashMap<String, String> params, final boolean b, final Object parentObject) {
        TLRPC.TL_document locationParent = tl_document;
        if (messageObject == null) {
            return;
        }
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        messageObject.cancelEditing = false;
        try {
            final long dialogId = messageObject.getDialogId();
            int n;
            HashMap<String, String> params2;
            TLRPC.TL_photo locationParent2;
            if (b) {
                if (messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) {
                    photo = (TLRPC.TL_photo)messageObject.messageOwner.media.photo;
                    n = 2;
                }
                else {
                    locationParent = (TLRPC.TL_document)messageObject.messageOwner.media.document;
                    if (!MessageObject.isVideoDocument(locationParent) && videoEditedInfo == null) {
                        n = 7;
                    }
                    else {
                        n = 3;
                    }
                    videoEditedInfo = messageObject.videoEditedInfo;
                }
                params2 = messageOwner.params;
                messageObject.editingMessage = messageOwner.message;
                messageObject.editingMessageEntities = messageOwner.entities;
                attachPath = messageOwner.attachPath;
                locationParent2 = photo;
            }
            else {
                messageObject.previousMedia = messageOwner.media;
                messageObject.previousCaption = messageOwner.message;
                messageObject.previousCaptionEntities = messageOwner.entities;
                messageObject.previousAttachPath = messageOwner.attachPath;
                final SerializedData serializedData = new SerializedData(true);
                this.writePreviousMessageData(messageOwner, serializedData);
                final SerializedData serializedData2 = new SerializedData(serializedData.length());
                this.writePreviousMessageData(messageOwner, serializedData2);
                if (params == null) {
                    params = new HashMap<String, String>();
                }
                params.put("prevMedia", Base64.encodeToString(serializedData2.toByteArray(), 0));
                serializedData2.cleanup();
                if (photo != null) {
                    messageOwner.media = new TLRPC.TL_messageMediaPhoto();
                    final TLRPC.MessageMedia media = messageOwner.media;
                    media.flags |= 0x3;
                    messageOwner.media.photo = photo;
                    if (attachPath != null && attachPath.length() > 0 && attachPath.startsWith("http")) {
                        messageOwner.attachPath = attachPath;
                    }
                    else {
                        messageOwner.attachPath = FileLoader.getPathToAttach(photo.sizes.get(photo.sizes.size() - 1).location, true).toString();
                    }
                    n = 2;
                }
                else if (locationParent != null) {
                    messageOwner.media = new TLRPC.TL_messageMediaDocument();
                    final TLRPC.MessageMedia media2 = messageOwner.media;
                    media2.flags |= 0x3;
                    messageOwner.media.document = locationParent;
                    if (!MessageObject.isVideoDocument(tl_document) && videoEditedInfo == null) {
                        n = 7;
                    }
                    else {
                        n = 3;
                    }
                    if (videoEditedInfo != null) {
                        params.put("ve", videoEditedInfo.getString());
                    }
                    messageOwner.attachPath = attachPath;
                }
                else {
                    n = -1;
                }
                messageOwner.params = params;
                messageOwner.send_state = 3;
                params2 = params;
                locationParent2 = photo;
            }
            if (messageOwner.attachPath == null) {
                messageOwner.attachPath = "";
            }
            messageOwner.local_id = 0;
            if ((messageObject.type == 3 || videoEditedInfo != null || messageObject.type == 2) && !TextUtils.isEmpty((CharSequence)messageOwner.attachPath)) {
                messageObject.attachPathExists = true;
            }
            VideoEditedInfo videoEditedInfo2 = videoEditedInfo;
            if (messageObject.videoEditedInfo != null && (videoEditedInfo2 = videoEditedInfo) == null) {
                videoEditedInfo2 = messageObject.videoEditedInfo;
            }
            if (!b) {
                if (messageObject.editingMessage != null) {
                    messageOwner.message = messageObject.editingMessage.toString();
                    if (messageObject.editingMessageEntities != null) {
                        messageOwner.entities = messageObject.editingMessageEntities;
                    }
                    else {
                        final ArrayList<TLRPC.MessageEntity> entities = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[] { messageObject.editingMessage });
                        if (entities != null && !entities.isEmpty()) {
                            messageOwner.entities = entities;
                        }
                    }
                    messageObject.caption = null;
                    messageObject.generateCaption();
                }
                final ArrayList<TLRPC.Message> list = new ArrayList<TLRPC.Message>();
                list.add(messageOwner);
                MessagesStorage.getInstance(this.currentAccount).putMessages(list, false, true, false, 0);
                messageObject.type = -1;
                messageObject.setType();
                messageObject.createMessageSendInfo();
                final ArrayList<MessageObject> list2 = new ArrayList<MessageObject>();
                list2.add(messageObject);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, dialogId, list2);
            }
            String originalPath;
            if (params2 != null && params2.containsKey("originalPath")) {
                originalPath = params2.get("originalPath");
            }
            else {
                originalPath = null;
            }
            if ((n >= 1 && n <= 3) || (n >= 5 && n <= 8)) {
                TLRPC.InputMedia media3 = null;
                boolean performMediaUpload = false;
                DelayedMessage delayedMessage2 = null;
                Label_1788: {
                    if (n == 2) {
                        final TLRPC.TL_inputMediaUploadedPhoto inputUploadMedia = new TLRPC.TL_inputMediaUploadedPhoto();
                        if (params2 != null) {
                            final String s = params2.get("masks");
                            if (s != null) {
                                final SerializedData serializedData3 = new SerializedData(Utilities.hexToBytes(s));
                                for (int int32 = serializedData3.readInt32(false), i = 0; i < int32; ++i) {
                                    inputUploadMedia.stickers.add(TLRPC.InputDocument.TLdeserialize(serializedData3, serializedData3.readInt32(false), false));
                                }
                                inputUploadMedia.flags |= 0x1;
                                serializedData3.cleanup();
                            }
                        }
                        if (locationParent2.access_hash == 0L) {
                            media3 = inputUploadMedia;
                            performMediaUpload = true;
                        }
                        else {
                            media3 = new TLRPC.TL_inputMediaPhoto();
                            ((TLRPC.TL_inputMediaPhoto)media3).id = new TLRPC.TL_inputPhoto();
                            ((TLRPC.TL_inputMediaPhoto)media3).id.id = locationParent2.id;
                            ((TLRPC.TL_inputMediaPhoto)media3).id.access_hash = locationParent2.access_hash;
                            ((TLRPC.TL_inputMediaPhoto)media3).id.file_reference = locationParent2.file_reference;
                            if (((TLRPC.TL_inputMediaPhoto)media3).id.file_reference == null) {
                                ((TLRPC.TL_inputMediaPhoto)media3).id.file_reference = new byte[0];
                            }
                            performMediaUpload = false;
                        }
                        final DelayedMessage delayedMessage = new DelayedMessage(dialogId);
                        delayedMessage.type = 0;
                        delayedMessage.obj = messageObject;
                        delayedMessage.originalPath = originalPath;
                        delayedMessage.parentObject = parentObject;
                        delayedMessage.inputUploadMedia = inputUploadMedia;
                        delayedMessage.performMediaUpload = performMediaUpload;
                        if (attachPath != null && attachPath.length() > 0 && attachPath.startsWith("http")) {
                            delayedMessage.httpLocation = attachPath;
                        }
                        else {
                            delayedMessage.photoSize = locationParent2.sizes.get(locationParent2.sizes.size() - 1);
                            delayedMessage.locationParent = locationParent2;
                        }
                        delayedMessage2 = delayedMessage;
                    }
                    else {
                        final int n2 = n;
                        if (n2 == 3) {
                            final TLRPC.TL_inputMediaUploadedDocument inputUploadMedia2 = new TLRPC.TL_inputMediaUploadedDocument();
                            inputUploadMedia2.mime_type = locationParent.mime_type;
                            inputUploadMedia2.attributes = locationParent.attributes;
                            if (!messageObject.isGif() && (videoEditedInfo2 == null || !videoEditedInfo2.muted)) {
                                inputUploadMedia2.nosound_video = true;
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("nosound_video = true");
                                }
                            }
                            if (locationParent.access_hash == 0L) {
                                media3 = inputUploadMedia2;
                                performMediaUpload = true;
                            }
                            else {
                                media3 = new TLRPC.TL_inputMediaDocument();
                                ((TLRPC.TL_inputMediaDocument)media3).id = new TLRPC.TL_inputDocument();
                                ((TLRPC.TL_inputMediaDocument)media3).id.id = locationParent.id;
                                ((TLRPC.TL_inputMediaDocument)media3).id.access_hash = locationParent.access_hash;
                                ((TLRPC.TL_inputMediaDocument)media3).id.file_reference = locationParent.file_reference;
                                if (((TLRPC.TL_inputMediaDocument)media3).id.file_reference == null) {
                                    ((TLRPC.TL_inputMediaDocument)media3).id.file_reference = new byte[0];
                                }
                                performMediaUpload = false;
                            }
                            final DelayedMessage delayedMessage3 = new DelayedMessage(dialogId);
                            delayedMessage3.type = 1;
                            delayedMessage3.obj = messageObject;
                            delayedMessage3.originalPath = originalPath;
                            delayedMessage3.parentObject = parentObject;
                            delayedMessage3.inputUploadMedia = inputUploadMedia2;
                            delayedMessage3.performMediaUpload = performMediaUpload;
                            if (!locationParent.thumbs.isEmpty()) {
                                delayedMessage3.photoSize = locationParent.thumbs.get(0);
                                delayedMessage3.locationParent = locationParent;
                            }
                            delayedMessage3.videoEditedInfo = videoEditedInfo2;
                            delayedMessage2 = delayedMessage3;
                        }
                        else {
                            if (n2 == 7) {
                                TLRPC.InputMedia inputUploadMedia3;
                                boolean b2;
                                if (originalPath != null && originalPath.length() > 0 && originalPath.startsWith("http") && params2 != null) {
                                    inputUploadMedia3 = new TLRPC.TL_inputMediaGifExternal();
                                    final String[] split = params2.get("url").split("\\|");
                                    if (split.length == 2) {
                                        inputUploadMedia3.url = split[0];
                                        inputUploadMedia3.q = split[1];
                                    }
                                    b2 = true;
                                }
                                else {
                                    inputUploadMedia3 = new TLRPC.TL_inputMediaUploadedDocument();
                                    b2 = false;
                                }
                                inputUploadMedia3.mime_type = locationParent.mime_type;
                                inputUploadMedia3.attributes = locationParent.attributes;
                                TLRPC.TL_inputMediaDocument tl_inputMediaDocument;
                                if (locationParent.access_hash == 0L) {
                                    performMediaUpload = (inputUploadMedia3 instanceof TLRPC.TL_inputMediaUploadedDocument);
                                    tl_inputMediaDocument = (TLRPC.TL_inputMediaDocument)inputUploadMedia3;
                                }
                                else {
                                    tl_inputMediaDocument = new TLRPC.TL_inputMediaDocument();
                                    tl_inputMediaDocument.id = new TLRPC.TL_inputDocument();
                                    tl_inputMediaDocument.id.id = locationParent.id;
                                    tl_inputMediaDocument.id.access_hash = locationParent.access_hash;
                                    tl_inputMediaDocument.id.file_reference = locationParent.file_reference;
                                    if (tl_inputMediaDocument.id.file_reference == null) {
                                        tl_inputMediaDocument.id.file_reference = new byte[0];
                                    }
                                    performMediaUpload = false;
                                }
                                if (!b2) {
                                    final DelayedMessage delayedMessage4 = new DelayedMessage(dialogId);
                                    delayedMessage4.originalPath = originalPath;
                                    delayedMessage4.type = 2;
                                    delayedMessage4.obj = messageObject;
                                    if (!locationParent.thumbs.isEmpty()) {
                                        delayedMessage4.photoSize = locationParent.thumbs.get(0);
                                        delayedMessage4.locationParent = locationParent;
                                    }
                                    delayedMessage4.parentObject = parentObject;
                                    delayedMessage4.inputUploadMedia = inputUploadMedia3;
                                    delayedMessage4.performMediaUpload = performMediaUpload;
                                    media3 = tl_inputMediaDocument;
                                    delayedMessage2 = delayedMessage4;
                                    break Label_1788;
                                }
                                media3 = tl_inputMediaDocument;
                            }
                            else {
                                media3 = null;
                                performMediaUpload = false;
                            }
                            delayedMessage2 = null;
                        }
                    }
                }
                final TLRPC.TL_messages_editMessage sendRequest = new TLRPC.TL_messages_editMessage();
                sendRequest.id = messageObject.getId();
                sendRequest.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)dialogId);
                sendRequest.flags |= 0x4000;
                sendRequest.media = media3;
                if (messageObject.editingMessage != null) {
                    sendRequest.message = messageObject.editingMessage.toString();
                    sendRequest.flags |= 0x800;
                    if (messageObject.editingMessageEntities != null) {
                        sendRequest.entities = messageObject.editingMessageEntities;
                        sendRequest.flags |= 0x8;
                    }
                    else {
                        final ArrayList<TLRPC.MessageEntity> entities2 = DataQuery.getInstance(this.currentAccount).getEntities(new CharSequence[] { messageObject.editingMessage });
                        if (entities2 != null && !entities2.isEmpty()) {
                            sendRequest.entities = entities2;
                            sendRequest.flags |= 0x8;
                        }
                    }
                    messageObject.editingMessage = null;
                    messageObject.editingMessageEntities = null;
                }
                if (delayedMessage2 != null) {
                    delayedMessage2.sendRequest = sendRequest;
                }
                if (n == 1) {
                    this.performSendMessageRequest(sendRequest, messageObject, null, delayedMessage2, parentObject);
                }
                else if (n == 2) {
                    if (performMediaUpload) {
                        this.performSendDelayedMessage(delayedMessage2);
                    }
                    else {
                        this.performSendMessageRequest(sendRequest, messageObject, originalPath, null, true, delayedMessage2, parentObject);
                    }
                }
                else if (n == 3) {
                    if (performMediaUpload) {
                        this.performSendDelayedMessage(delayedMessage2);
                    }
                    else {
                        this.performSendMessageRequest(sendRequest, messageObject, originalPath, delayedMessage2, parentObject);
                    }
                }
                else if (n == 6) {
                    this.performSendMessageRequest(sendRequest, messageObject, originalPath, delayedMessage2, parentObject);
                }
                else if (n == 7) {
                    if (performMediaUpload) {
                        this.performSendDelayedMessage(delayedMessage2);
                    }
                    else {
                        this.performSendMessageRequest(sendRequest, messageObject, originalPath, delayedMessage2, parentObject);
                    }
                }
                else if (n == 8) {
                    if (performMediaUpload) {
                        this.performSendDelayedMessage(delayedMessage2);
                    }
                    else {
                        this.performSendMessageRequest(sendRequest, messageObject, originalPath, delayedMessage2, parentObject);
                    }
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.revertEditingMessageObject(messageObject);
        }
    }
    
    public static void ensureMediaThumbExists(int n, final boolean b, final TLObject tlObject, final String s, final Uri uri, final long n2) {
        if (tlObject instanceof TLRPC.TL_photo) {
            final TLRPC.TL_photo tl_photo = (TLRPC.TL_photo)tlObject;
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tl_photo.sizes, 90);
            final boolean b2 = closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize || FileLoader.getPathToAttach(closestPhotoSizeWithSize, true).exists();
            final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tl_photo.sizes, AndroidUtilities.getPhotoSize());
            final boolean exists = FileLoader.getPathToAttach(closestPhotoSizeWithSize2, false).exists();
            if (!b2 || !exists) {
                Bitmap bitmap = ImageLoader.loadBitmap(s, uri, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), true);
                if (bitmap == null) {
                    bitmap = ImageLoader.loadBitmap(s, uri, 800.0f, 800.0f, true);
                }
                if (!exists) {
                    final TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize2, bitmap, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
                    if (scaleAndSaveImage != closestPhotoSizeWithSize2) {
                        tl_photo.sizes.add(0, scaleAndSaveImage);
                    }
                }
                if (!b2) {
                    final TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize, bitmap, 90.0f, 90.0f, 55, true);
                    if (scaleAndSaveImage2 != closestPhotoSizeWithSize) {
                        tl_photo.sizes.add(0, scaleAndSaveImage2);
                    }
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
        else if (tlObject instanceof TLRPC.TL_document) {
            final TLRPC.TL_document tl_document = (TLRPC.TL_document)tlObject;
            if ((MessageObject.isVideoDocument(tl_document) || MessageObject.isNewGifDocument(tl_document)) && MessageObject.isDocumentHasThumb(tl_document)) {
                final ArrayList<TLRPC.PhotoSize> thumbs = tl_document.thumbs;
                n = 320;
                final TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(thumbs, 320);
                if (closestPhotoSizeWithSize3 instanceof TLRPC.TL_photoStrippedSize) {
                    return;
                }
                if (!FileLoader.getPathToAttach(closestPhotoSizeWithSize3, true).exists()) {
                    Bitmap bitmap2 = createVideoThumbnail(s, n2);
                    if (bitmap2 == null) {
                        bitmap2 = ThumbnailUtils.createVideoThumbnail(s, 1);
                    }
                    if (b) {
                        n = 90;
                    }
                    final ArrayList<TLRPC.PhotoSize> thumbs2 = tl_document.thumbs;
                    final float n3 = (float)n;
                    if (n > 90) {
                        n = 80;
                    }
                    else {
                        n = 55;
                    }
                    thumbs2.set(0, ImageLoader.scaleAndSaveImage(closestPhotoSizeWithSize3, bitmap2, n3, n3, n, false));
                }
            }
        }
    }
    
    private static void fillVideoAttribute(final String s, final TLRPC.TL_documentAttributeVideo tl_documentAttributeVideo, VideoEditedInfo ex) {
        final Exception ex2 = null;
        Object s2;
        final Object o = s2 = null;
        int n = 0;
        Label_0262: {
            String s3 = null;
            try {
                try {
                    s2 = o;
                    final Object o2 = new MediaMetadataRetriever();
                    try {
                        ((MediaMetadataRetriever)o2).setDataSource(s);
                        s2 = ((MediaMetadataRetriever)o2).extractMetadata(18);
                        if (s2 != null) {
                            tl_documentAttributeVideo.w = Integer.parseInt((String)s2);
                        }
                        s2 = ((MediaMetadataRetriever)o2).extractMetadata(19);
                        if (s2 != null) {
                            tl_documentAttributeVideo.h = Integer.parseInt((String)s2);
                        }
                        s2 = ((MediaMetadataRetriever)o2).extractMetadata(9);
                        if (s2 != null) {
                            tl_documentAttributeVideo.duration = (int)Math.ceil(Long.parseLong((String)s2) / 1000.0f);
                        }
                        if (Build$VERSION.SDK_INT >= 17) {
                            s2 = ((MediaMetadataRetriever)o2).extractMetadata(24);
                            if (s2 != null) {
                                n = Utilities.parseInt((String)s2);
                                if (ex != null) {
                                    ((VideoEditedInfo)ex).rotationValue = n;
                                }
                                else if (n == 90 || n == 270) {
                                    n = tl_documentAttributeVideo.w;
                                    tl_documentAttributeVideo.w = tl_documentAttributeVideo.h;
                                    tl_documentAttributeVideo.h = n;
                                }
                            }
                        }
                        n = 1;
                        try {
                            ((MediaMetadataRetriever)o2).release();
                            break Label_0262;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                            break Label_0262;
                        }
                    }
                    catch (Exception s2) {
                        ex = (Exception)o2;
                    }
                }
                finally {
                    s3 = (String)s2;
                }
            }
            catch (Exception s3) {
                ex = ex2;
            }
            FileLog.e((Throwable)s3);
            if (ex != null) {
                try {
                    ((MediaMetadataRetriever)ex).release();
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
            }
            n = 0;
        }
        if (n == 0) {
            try {
                final MediaPlayer create = MediaPlayer.create(ApplicationLoader.applicationContext, Uri.fromFile(new File(s)));
                if (create != null) {
                    tl_documentAttributeVideo.duration = (int)Math.ceil(create.getDuration() / 1000.0f);
                    tl_documentAttributeVideo.w = create.getVideoWidth();
                    tl_documentAttributeVideo.h = create.getVideoHeight();
                    create.release();
                }
            }
            catch (Exception ex4) {
                FileLog.e(ex4);
            }
        }
        return;
        String s3 = null;
        if (s3 != null) {
            try {
                ((MediaMetadataRetriever)s3).release();
            }
            catch (Exception ex5) {
                FileLog.e(ex5);
            }
        }
    }
    
    private DelayedMessage findMaxDelayedMessageForMessageId(final int n, final long n2) {
        final Iterator<Map.Entry<String, ArrayList<DelayedMessage>>> iterator = this.delayedMessages.entrySet().iterator();
        DelayedMessage delayedMessage = null;
        int n3 = Integer.MIN_VALUE;
        while (iterator.hasNext()) {
            final ArrayList<DelayedMessage> list = iterator.next().getValue();
            final int size = list.size();
            int i = 0;
            int n4 = n3;
            while (i < size) {
                final DelayedMessage delayedMessage2 = list.get(i);
                final int type = delayedMessage2.type;
                DelayedMessage delayedMessage3 = null;
                int n5 = 0;
                Label_0267: {
                    if (type != 4) {
                        delayedMessage3 = delayedMessage;
                        n5 = n4;
                        if (type != 0) {
                            break Label_0267;
                        }
                    }
                    delayedMessage3 = delayedMessage;
                    n5 = n4;
                    if (delayedMessage2.peer == n2) {
                        final MessageObject obj = delayedMessage2.obj;
                        int n6;
                        if (obj != null) {
                            n6 = obj.getId();
                        }
                        else {
                            final ArrayList<MessageObject> messageObjects = delayedMessage2.messageObjects;
                            if (messageObjects != null && !messageObjects.isEmpty()) {
                                final ArrayList<MessageObject> messageObjects2 = delayedMessage2.messageObjects;
                                n6 = messageObjects2.get(messageObjects2.size() - 1).getId();
                            }
                            else {
                                n6 = 0;
                            }
                        }
                        delayedMessage3 = delayedMessage;
                        n5 = n4;
                        if (n6 != 0) {
                            delayedMessage3 = delayedMessage;
                            n5 = n4;
                            if (n6 > n) {
                                delayedMessage3 = delayedMessage;
                                n5 = n4;
                                if (delayedMessage == null) {
                                    delayedMessage3 = delayedMessage;
                                    if ((n5 = n4) < n6) {
                                        delayedMessage3 = delayedMessage2;
                                        n5 = n6;
                                    }
                                }
                            }
                        }
                    }
                }
                ++i;
                delayedMessage = delayedMessage3;
                n4 = n5;
            }
            n3 = n4;
        }
        return delayedMessage;
    }
    
    public static SendMessagesHelper getInstance(final int n) {
        final SendMessagesHelper sendMessagesHelper;
        if ((sendMessagesHelper = SendMessagesHelper.Instance[n]) == null) {
            synchronized (SendMessagesHelper.class) {
                if (SendMessagesHelper.Instance[n] == null) {
                    SendMessagesHelper.Instance[n] = new SendMessagesHelper(n);
                }
            }
        }
        return sendMessagesHelper;
    }
    
    private static String getKeyForPhotoSize(final TLRPC.PhotoSize photoSize, final Bitmap[] array, final boolean b) {
        if (photoSize == null) {
            return null;
        }
        int n2 = 0;
        Label_0089: {
            int n;
            if (AndroidUtilities.isTablet()) {
                n = AndroidUtilities.getMinTabletSide();
            }
            else {
                if (photoSize.w >= photoSize.h) {
                    final Point displaySize = AndroidUtilities.displaySize;
                    n2 = Math.min(displaySize.x, displaySize.y) - AndroidUtilities.dp(64.0f);
                    break Label_0089;
                }
                final Point displaySize2 = AndroidUtilities.displaySize;
                n = Math.min(displaySize2.x, displaySize2.y);
            }
            n2 = (int)(n * 0.7f);
        }
        final int n3 = AndroidUtilities.dp(100.0f) + n2;
        int photoSize2 = n2;
        if (n2 > AndroidUtilities.getPhotoSize()) {
            photoSize2 = AndroidUtilities.getPhotoSize();
        }
        int photoSize3;
        if ((photoSize3 = n3) > AndroidUtilities.getPhotoSize()) {
            photoSize3 = AndroidUtilities.getPhotoSize();
        }
        final int w = photoSize.w;
        final float n4 = (float)w;
        final float n5 = (float)photoSize2;
        final float n6 = n4 / n5;
        final int n7 = (int)(w / n6);
        final int n8 = (int)(photoSize.h / n6);
        int dp;
        if ((dp = n7) == 0) {
            dp = AndroidUtilities.dp(150.0f);
        }
        int dp2;
        if ((dp2 = n8) == 0) {
            dp2 = AndroidUtilities.dp(150.0f);
        }
        if (dp2 > photoSize3) {
            dp /= (int)(dp2 / (float)photoSize3);
        }
        else if (dp2 < AndroidUtilities.dp(120.0f)) {
            final int dp3 = AndroidUtilities.dp(120.0f);
            final float n9 = photoSize.h / (float)dp3;
            final int w2 = photoSize.w;
            photoSize3 = dp3;
            if (w2 / n9 < n5) {
                dp = (int)(w2 / n9);
                photoSize3 = dp3;
            }
        }
        else {
            photoSize3 = dp2;
        }
        if (array != null) {
            try {
                final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                bitmapFactory$Options.inJustDecodeBounds = true;
                final File pathToAttach = FileLoader.getPathToAttach(photoSize);
                final FileInputStream fileInputStream = new FileInputStream(pathToAttach);
                BitmapFactory.decodeStream((InputStream)fileInputStream, (Rect)null, bitmapFactory$Options);
                fileInputStream.close();
                float max;
                if ((max = Math.max(bitmapFactory$Options.outWidth / (float)dp, bitmapFactory$Options.outHeight / (float)photoSize3)) < 1.0f) {
                    max = 1.0f;
                }
                bitmapFactory$Options.inJustDecodeBounds = false;
                bitmapFactory$Options.inSampleSize = (int)max;
                bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
                if (Build$VERSION.SDK_INT >= 21) {
                    final FileInputStream fileInputStream2 = new FileInputStream(pathToAttach);
                    array[0] = BitmapFactory.decodeStream((InputStream)fileInputStream2, (Rect)null, bitmapFactory$Options);
                    fileInputStream2.close();
                }
            }
            catch (Throwable t) {}
        }
        final Locale us = Locale.US;
        String format;
        if (b) {
            format = "%d_%d@%d_%d_b";
        }
        else {
            format = "%d_%d@%d_%d";
        }
        return String.format(us, format, photoSize.location.volume_id, photoSize.location.local_id, (int)(dp / AndroidUtilities.density), (int)(photoSize3 / AndroidUtilities.density));
    }
    
    private TLRPC.PhotoSize getThumbForSecretChat(final ArrayList<TLRPC.PhotoSize> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final TLRPC.PhotoSize photoSize = list.get(i);
                    if (photoSize != null && !(photoSize instanceof TLRPC.TL_photoStrippedSize) && !(photoSize instanceof TLRPC.TL_photoSizeEmpty) && photoSize.location != null) {
                        final TLRPC.TL_photoSize tl_photoSize = new TLRPC.TL_photoSize();
                        tl_photoSize.type = photoSize.type;
                        tl_photoSize.w = photoSize.w;
                        tl_photoSize.h = photoSize.h;
                        tl_photoSize.size = photoSize.size;
                        tl_photoSize.bytes = photoSize.bytes;
                        if (tl_photoSize.bytes == null) {
                            tl_photoSize.bytes = new byte[0];
                        }
                        tl_photoSize.location = new TLRPC.TL_fileLocation_layer82();
                        final TLRPC.FileLocation location = tl_photoSize.location;
                        final TLRPC.FileLocation location2 = photoSize.location;
                        location.dc_id = location2.dc_id;
                        location.volume_id = location2.volume_id;
                        location.local_id = location2.local_id;
                        location.secret = location2.secret;
                        return tl_photoSize;
                    }
                }
            }
        }
        return null;
    }
    
    private static String getTrimmedString(String substring) {
        final String trim = substring.trim();
        if (trim.length() == 0) {
            return trim;
        }
        String substring2;
        while (true) {
            substring2 = substring;
            if (!substring.startsWith("\n")) {
                break;
            }
            substring = substring.substring(1);
        }
        while (substring2.endsWith("\n")) {
            substring2 = substring2.substring(0, substring2.length() - 1);
        }
        return substring2;
    }
    
    private void performSendDelayedMessage(final DelayedMessage delayedMessage) {
        this.performSendDelayedMessage(delayedMessage, -1);
    }
    
    private void performSendDelayedMessage(final DelayedMessage delayedMessage, int index) {
        final int type = delayedMessage.type;
        final boolean b = false;
        boolean b2 = true;
        if (type == 0) {
            final String httpLocation = delayedMessage.httpLocation;
            if (httpLocation != null) {
                this.putToDelayedMessages(httpLocation, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
            }
            else if (delayedMessage.sendRequest != null) {
                final String string = FileLoader.getPathToAttach(delayedMessage.photoSize).toString();
                this.putToDelayedMessages(string, delayedMessage);
                FileLoader.getInstance(this.currentAccount).uploadFile(string, false, true, 16777216);
            }
            else {
                String string3;
                String string2 = string3 = FileLoader.getPathToAttach(delayedMessage.photoSize).toString();
                if (delayedMessage.sendEncryptedRequest != null) {
                    string3 = string2;
                    if (delayedMessage.photoSize.location.dc_id != 0) {
                        File file;
                        if (!(file = new File(string2)).exists()) {
                            string2 = FileLoader.getPathToAttach(delayedMessage.photoSize, true).toString();
                            file = new File(string2);
                        }
                        string3 = string2;
                        if (!file.exists()) {
                            this.putToDelayedMessages(FileLoader.getAttachFileName(delayedMessage.photoSize), delayedMessage);
                            FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForObject(delayedMessage.photoSize, delayedMessage.locationParent), delayedMessage.parentObject, "jpg", 2, 0);
                            return;
                        }
                    }
                }
                this.putToDelayedMessages(string3, delayedMessage);
                FileLoader.getInstance(this.currentAccount).uploadFile(string3, true, true, 16777216);
            }
        }
        else if (type == 1) {
            final VideoEditedInfo videoEditedInfo = delayedMessage.videoEditedInfo;
            if (videoEditedInfo != null && videoEditedInfo.needConvert()) {
                final MessageObject obj = delayedMessage.obj;
                final String attachPath = obj.messageOwner.attachPath;
                final TLRPC.Document document = obj.getDocument();
                String string4;
                if ((string4 = attachPath) == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(FileLoader.getDirectory(4));
                    sb.append("/");
                    sb.append(document.id);
                    sb.append(".mp4");
                    string4 = sb.toString();
                }
                this.putToDelayedMessages(string4, delayedMessage);
                MediaController.getInstance().scheduleVideoConvert(delayedMessage.obj);
            }
            else {
                final VideoEditedInfo videoEditedInfo2 = delayedMessage.videoEditedInfo;
                if (videoEditedInfo2 != null) {
                    if (videoEditedInfo2.file != null) {
                        final TLObject sendRequest = delayedMessage.sendRequest;
                        TLRPC.InputMedia inputMedia;
                        if (sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                            inputMedia = ((TLRPC.TL_messages_sendMedia)sendRequest).media;
                        }
                        else if (sendRequest instanceof TLRPC.TL_messages_editMessage) {
                            inputMedia = ((TLRPC.TL_messages_editMessage)sendRequest).media;
                        }
                        else {
                            inputMedia = ((TLRPC.TL_messages_sendBroadcast)sendRequest).media;
                        }
                        final VideoEditedInfo videoEditedInfo3 = delayedMessage.videoEditedInfo;
                        inputMedia.file = videoEditedInfo3.file;
                        videoEditedInfo3.file = null;
                    }
                    else if (videoEditedInfo2.encryptedFile != null) {
                        final TLRPC.TL_decryptedMessage tl_decryptedMessage = (TLRPC.TL_decryptedMessage)delayedMessage.sendEncryptedRequest;
                        final TLRPC.DecryptedMessageMedia media = tl_decryptedMessage.media;
                        media.size = (int)videoEditedInfo2.estimatedSize;
                        media.key = videoEditedInfo2.key;
                        media.iv = videoEditedInfo2.iv;
                        final SecretChatHelper instance = SecretChatHelper.getInstance(this.currentAccount);
                        final MessageObject obj2 = delayedMessage.obj;
                        instance.performSendEncryptedRequest(tl_decryptedMessage, obj2.messageOwner, delayedMessage.encryptedChat, delayedMessage.videoEditedInfo.encryptedFile, delayedMessage.originalPath, obj2);
                        delayedMessage.videoEditedInfo.encryptedFile = null;
                        return;
                    }
                }
                final TLObject sendRequest2 = delayedMessage.sendRequest;
                if (sendRequest2 != null) {
                    TLRPC.InputMedia inputMedia2;
                    if (sendRequest2 instanceof TLRPC.TL_messages_sendMedia) {
                        inputMedia2 = ((TLRPC.TL_messages_sendMedia)sendRequest2).media;
                    }
                    else if (sendRequest2 instanceof TLRPC.TL_messages_editMessage) {
                        inputMedia2 = ((TLRPC.TL_messages_editMessage)sendRequest2).media;
                    }
                    else {
                        inputMedia2 = ((TLRPC.TL_messages_sendBroadcast)sendRequest2).media;
                    }
                    if (inputMedia2.file == null) {
                        final MessageObject obj3 = delayedMessage.obj;
                        final String attachPath2 = obj3.messageOwner.attachPath;
                        final TLRPC.Document document2 = obj3.getDocument();
                        String string5;
                        if ((string5 = attachPath2) == null) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(FileLoader.getDirectory(4));
                            sb2.append("/");
                            sb2.append(document2.id);
                            sb2.append(".mp4");
                            string5 = sb2.toString();
                        }
                        this.putToDelayedMessages(string5, delayedMessage);
                        final VideoEditedInfo videoEditedInfo4 = delayedMessage.obj.videoEditedInfo;
                        if (videoEditedInfo4 != null && videoEditedInfo4.needConvert()) {
                            FileLoader.getInstance(this.currentAccount).uploadFile(string5, false, false, document2.size, 33554432);
                        }
                        else {
                            FileLoader.getInstance(this.currentAccount).uploadFile(string5, false, false, 33554432);
                        }
                    }
                    else {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(FileLoader.getDirectory(4));
                        sb3.append("/");
                        sb3.append(delayedMessage.photoSize.location.volume_id);
                        sb3.append("_");
                        sb3.append(delayedMessage.photoSize.location.local_id);
                        sb3.append(".jpg");
                        final String string6 = sb3.toString();
                        this.putToDelayedMessages(string6, delayedMessage);
                        FileLoader.getInstance(this.currentAccount).uploadFile(string6, false, true, 16777216);
                    }
                }
                else {
                    final MessageObject obj4 = delayedMessage.obj;
                    final String attachPath3 = obj4.messageOwner.attachPath;
                    final TLRPC.Document document3 = obj4.getDocument();
                    String string7;
                    if ((string7 = attachPath3) == null) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(FileLoader.getDirectory(4));
                        sb4.append("/");
                        sb4.append(document3.id);
                        sb4.append(".mp4");
                        string7 = sb4.toString();
                    }
                    if (delayedMessage.sendEncryptedRequest != null && document3.dc_id != 0 && !new File(string7).exists()) {
                        this.putToDelayedMessages(FileLoader.getAttachFileName(document3), delayedMessage);
                        FileLoader.getInstance(this.currentAccount).loadFile(document3, delayedMessage.parentObject, 2, 0);
                        return;
                    }
                    this.putToDelayedMessages(string7, delayedMessage);
                    final VideoEditedInfo videoEditedInfo5 = delayedMessage.obj.videoEditedInfo;
                    if (videoEditedInfo5 != null && videoEditedInfo5.needConvert()) {
                        FileLoader.getInstance(this.currentAccount).uploadFile(string7, true, false, document3.size, 33554432);
                    }
                    else {
                        FileLoader.getInstance(this.currentAccount).uploadFile(string7, true, false, 33554432);
                    }
                }
            }
        }
        else if (type == 2) {
            final String httpLocation2 = delayedMessage.httpLocation;
            if (httpLocation2 != null) {
                this.putToDelayedMessages(httpLocation2, delayedMessage);
                ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "gif", this.currentAccount);
            }
            else {
                final TLObject sendRequest3 = delayedMessage.sendRequest;
                if (sendRequest3 != null) {
                    TLRPC.InputMedia inputMedia3;
                    if (sendRequest3 instanceof TLRPC.TL_messages_sendMedia) {
                        inputMedia3 = ((TLRPC.TL_messages_sendMedia)sendRequest3).media;
                    }
                    else if (sendRequest3 instanceof TLRPC.TL_messages_editMessage) {
                        inputMedia3 = ((TLRPC.TL_messages_editMessage)sendRequest3).media;
                    }
                    else {
                        inputMedia3 = ((TLRPC.TL_messages_sendBroadcast)sendRequest3).media;
                    }
                    if (inputMedia3.file == null) {
                        final String attachPath4 = delayedMessage.obj.messageOwner.attachPath;
                        this.putToDelayedMessages(attachPath4, delayedMessage);
                        final FileLoader instance2 = FileLoader.getInstance(this.currentAccount);
                        if (delayedMessage.sendRequest != null) {
                            b2 = false;
                        }
                        instance2.uploadFile(attachPath4, b2, false, 67108864);
                    }
                    else if (inputMedia3.thumb == null && delayedMessage.photoSize != null) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(FileLoader.getDirectory(4));
                        sb5.append("/");
                        sb5.append(delayedMessage.photoSize.location.volume_id);
                        sb5.append("_");
                        sb5.append(delayedMessage.photoSize.location.local_id);
                        sb5.append(".jpg");
                        final String string8 = sb5.toString();
                        this.putToDelayedMessages(string8, delayedMessage);
                        FileLoader.getInstance(this.currentAccount).uploadFile(string8, false, true, 16777216);
                    }
                }
                else {
                    final MessageObject obj5 = delayedMessage.obj;
                    final String attachPath5 = obj5.messageOwner.attachPath;
                    final TLRPC.Document document4 = obj5.getDocument();
                    if (delayedMessage.sendEncryptedRequest != null && document4.dc_id != 0 && !new File(attachPath5).exists()) {
                        this.putToDelayedMessages(FileLoader.getAttachFileName(document4), delayedMessage);
                        FileLoader.getInstance(this.currentAccount).loadFile(document4, delayedMessage.parentObject, 2, 0);
                        return;
                    }
                    this.putToDelayedMessages(attachPath5, delayedMessage);
                    FileLoader.getInstance(this.currentAccount).uploadFile(attachPath5, true, false, 67108864);
                }
            }
        }
        else if (type == 3) {
            final String attachPath6 = delayedMessage.obj.messageOwner.attachPath;
            this.putToDelayedMessages(attachPath6, delayedMessage);
            final FileLoader instance3 = FileLoader.getInstance(this.currentAccount);
            boolean b3 = b;
            if (delayedMessage.sendRequest == null) {
                b3 = true;
            }
            instance3.uploadFile(attachPath6, b3, true, 50331648);
        }
        else if (type == 4) {
            final boolean b4 = index < 0;
            if (delayedMessage.performMediaUpload) {
                if (index < 0) {
                    index = delayedMessage.messageObjects.size() - 1;
                }
                final MessageObject messageObject = delayedMessage.messageObjects.get(index);
                if (messageObject.getDocument() != null) {
                    if (delayedMessage.videoEditedInfo != null) {
                        final String attachPath7 = messageObject.messageOwner.attachPath;
                        final TLRPC.Document document5 = messageObject.getDocument();
                        String string9;
                        if ((string9 = attachPath7) == null) {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append(FileLoader.getDirectory(4));
                            sb6.append("/");
                            sb6.append(document5.id);
                            sb6.append(".mp4");
                            string9 = sb6.toString();
                        }
                        this.putToDelayedMessages(string9, delayedMessage);
                        delayedMessage.extraHashMap.put(messageObject, string9);
                        final HashMap<Object, Object> extraHashMap = delayedMessage.extraHashMap;
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append(string9);
                        sb7.append("_i");
                        extraHashMap.put(sb7.toString(), messageObject);
                        if (delayedMessage.photoSize != null) {
                            final HashMap<Object, Object> extraHashMap2 = delayedMessage.extraHashMap;
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append(string9);
                            sb8.append("_t");
                            extraHashMap2.put(sb8.toString(), delayedMessage.photoSize);
                        }
                        MediaController.getInstance().scheduleVideoConvert(messageObject);
                    }
                    else {
                        final TLRPC.Document document6 = messageObject.getDocument();
                        String str = messageObject.messageOwner.attachPath;
                        if (str == null) {
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append(FileLoader.getDirectory(4));
                            sb9.append("/");
                            sb9.append(document6.id);
                            sb9.append(".mp4");
                            str = sb9.toString();
                        }
                        final TLObject sendRequest4 = delayedMessage.sendRequest;
                        if (sendRequest4 != null) {
                            final TLRPC.InputMedia media2 = ((TLRPC.TL_messages_sendMultiMedia)sendRequest4).multi_media.get(index).media;
                            if (media2.file == null) {
                                this.putToDelayedMessages(str, delayedMessage);
                                delayedMessage.extraHashMap.put(messageObject, str);
                                delayedMessage.extraHashMap.put(str, media2);
                                final HashMap<Object, Object> extraHashMap3 = delayedMessage.extraHashMap;
                                final StringBuilder sb10 = new StringBuilder();
                                sb10.append(str);
                                sb10.append("_i");
                                extraHashMap3.put(sb10.toString(), messageObject);
                                if (delayedMessage.photoSize != null) {
                                    final HashMap<Object, Object> extraHashMap4 = delayedMessage.extraHashMap;
                                    final StringBuilder sb11 = new StringBuilder();
                                    sb11.append(str);
                                    sb11.append("_t");
                                    extraHashMap4.put(sb11.toString(), delayedMessage.photoSize);
                                }
                                final VideoEditedInfo videoEditedInfo6 = messageObject.videoEditedInfo;
                                if (videoEditedInfo6 != null && videoEditedInfo6.needConvert()) {
                                    FileLoader.getInstance(this.currentAccount).uploadFile(str, false, false, document6.size, 33554432);
                                }
                                else {
                                    FileLoader.getInstance(this.currentAccount).uploadFile(str, false, false, 33554432);
                                }
                            }
                            else {
                                final StringBuilder sb12 = new StringBuilder();
                                sb12.append(FileLoader.getDirectory(4));
                                sb12.append("/");
                                sb12.append(delayedMessage.photoSize.location.volume_id);
                                sb12.append("_");
                                sb12.append(delayedMessage.photoSize.location.local_id);
                                sb12.append(".jpg");
                                final String string10 = sb12.toString();
                                this.putToDelayedMessages(string10, delayedMessage);
                                final HashMap<Object, Object> extraHashMap5 = delayedMessage.extraHashMap;
                                final StringBuilder sb13 = new StringBuilder();
                                sb13.append(string10);
                                sb13.append("_o");
                                extraHashMap5.put(sb13.toString(), str);
                                delayedMessage.extraHashMap.put(messageObject, string10);
                                delayedMessage.extraHashMap.put(string10, media2);
                                FileLoader.getInstance(this.currentAccount).uploadFile(string10, false, true, 16777216);
                            }
                        }
                        else {
                            final TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest;
                            this.putToDelayedMessages(str, delayedMessage);
                            delayedMessage.extraHashMap.put(messageObject, str);
                            delayedMessage.extraHashMap.put(str, tl_messages_sendEncryptedMultiMedia.files.get(index));
                            final HashMap<Object, Object> extraHashMap6 = delayedMessage.extraHashMap;
                            final StringBuilder sb14 = new StringBuilder();
                            sb14.append(str);
                            sb14.append("_i");
                            extraHashMap6.put(sb14.toString(), messageObject);
                            if (delayedMessage.photoSize != null) {
                                final HashMap<Object, Object> extraHashMap7 = delayedMessage.extraHashMap;
                                final StringBuilder sb15 = new StringBuilder();
                                sb15.append(str);
                                sb15.append("_t");
                                extraHashMap7.put(sb15.toString(), delayedMessage.photoSize);
                            }
                            final VideoEditedInfo videoEditedInfo7 = messageObject.videoEditedInfo;
                            if (videoEditedInfo7 != null && videoEditedInfo7.needConvert()) {
                                FileLoader.getInstance(this.currentAccount).uploadFile(str, true, false, document6.size, 33554432);
                            }
                            else {
                                FileLoader.getInstance(this.currentAccount).uploadFile(str, true, false, 33554432);
                            }
                        }
                    }
                    delayedMessage.videoEditedInfo = null;
                    delayedMessage.photoSize = null;
                }
                else {
                    final String httpLocation3 = delayedMessage.httpLocation;
                    if (httpLocation3 != null) {
                        this.putToDelayedMessages(httpLocation3, delayedMessage);
                        delayedMessage.extraHashMap.put(messageObject, delayedMessage.httpLocation);
                        delayedMessage.extraHashMap.put(delayedMessage.httpLocation, messageObject);
                        ImageLoader.getInstance().loadHttpFile(delayedMessage.httpLocation, "file", this.currentAccount);
                        delayedMessage.httpLocation = null;
                    }
                    else {
                        final TLObject sendRequest5 = delayedMessage.sendRequest;
                        TLObject media3;
                        if (sendRequest5 != null) {
                            media3 = ((TLRPC.TL_messages_sendMultiMedia)sendRequest5).multi_media.get(index).media;
                        }
                        else {
                            media3 = ((TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest).files.get(index);
                        }
                        final String string11 = FileLoader.getPathToAttach(delayedMessage.photoSize).toString();
                        this.putToDelayedMessages(string11, delayedMessage);
                        delayedMessage.extraHashMap.put(string11, media3);
                        delayedMessage.extraHashMap.put(messageObject, string11);
                        FileLoader.getInstance(this.currentAccount).uploadFile(string11, delayedMessage.sendEncryptedRequest != null, true, 16777216);
                        delayedMessage.photoSize = null;
                    }
                }
                delayedMessage.performMediaUpload = false;
            }
            else if (!delayedMessage.messageObjects.isEmpty()) {
                final ArrayList<MessageObject> messageObjects = delayedMessage.messageObjects;
                this.putToSendingMessages(messageObjects.get(messageObjects.size() - 1).messageOwner);
            }
            this.sendReadyToSendGroup(delayedMessage, b4, true);
        }
    }
    
    private void performSendMessageRequest(final TLObject tlObject, final MessageObject messageObject, final String s, final DelayedMessage delayedMessage, final Object o) {
        this.performSendMessageRequest(tlObject, messageObject, s, null, false, delayedMessage, o);
    }
    
    public static void prepareSendingAudioDocuments(final ArrayList<MessageObject> list, final long n, final MessageObject messageObject, final MessageObject messageObject2) {
        new Thread(new _$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw(list, n, UserConfig.selectedAccount, messageObject2, messageObject)).start();
    }
    
    public static void prepareSendingBotContextResult(final TLRPC.BotInlineResult botInlineResult, final HashMap<String, String> hashMap, final long n, final MessageObject messageObject) {
        if (botInlineResult == null) {
            return;
        }
        final int selectedAccount = UserConfig.selectedAccount;
        final TLRPC.BotInlineMessage send_message = botInlineResult.send_message;
        if (send_message instanceof TLRPC.TL_botInlineMessageMediaAuto) {
            new Thread(new _$$Lambda$SendMessagesHelper$LmDH_h6B9Uggp2w_KTpB6c_fhcQ(n, botInlineResult, selectedAccount, hashMap, messageObject)).run();
        }
        else if (send_message instanceof TLRPC.TL_botInlineMessageText) {
            TLRPC.WebPage webPage2;
            final TLRPC.WebPage webPage = webPage2 = null;
            if ((int)n == 0) {
                int index = 0;
                while (true) {
                    webPage2 = webPage;
                    if (index >= botInlineResult.send_message.entities.size()) {
                        break;
                    }
                    final TLRPC.MessageEntity messageEntity = botInlineResult.send_message.entities.get(index);
                    if (messageEntity instanceof TLRPC.TL_messageEntityUrl) {
                        webPage2 = new TLRPC.TL_webPagePending();
                        final String message = botInlineResult.send_message.message;
                        final int offset = messageEntity.offset;
                        webPage2.url = message.substring(offset, messageEntity.length + offset);
                        break;
                    }
                    ++index;
                }
            }
            final SendMessagesHelper instance = getInstance(selectedAccount);
            final TLRPC.BotInlineMessage send_message2 = botInlineResult.send_message;
            instance.sendMessage(send_message2.message, n, messageObject, webPage2, send_message2.no_webpage ^ true, send_message2.entities, send_message2.reply_markup, hashMap);
        }
        else if (send_message instanceof TLRPC.TL_botInlineMessageMediaVenue) {
            final TLRPC.TL_messageMediaVenue tl_messageMediaVenue = new TLRPC.TL_messageMediaVenue();
            final TLRPC.BotInlineMessage send_message3 = botInlineResult.send_message;
            tl_messageMediaVenue.geo = send_message3.geo;
            tl_messageMediaVenue.address = send_message3.address;
            tl_messageMediaVenue.title = send_message3.title;
            tl_messageMediaVenue.provider = send_message3.provider;
            tl_messageMediaVenue.venue_id = send_message3.venue_id;
            final String venue_type = send_message3.venue_type;
            tl_messageMediaVenue.venue_id = venue_type;
            tl_messageMediaVenue.venue_type = venue_type;
            if (tl_messageMediaVenue.venue_type == null) {
                tl_messageMediaVenue.venue_type = "";
            }
            getInstance(selectedAccount).sendMessage(tl_messageMediaVenue, n, messageObject, botInlineResult.send_message.reply_markup, hashMap);
        }
        else if (send_message instanceof TLRPC.TL_botInlineMessageMediaGeo) {
            if (send_message.period != 0) {
                final TLRPC.TL_messageMediaGeoLive tl_messageMediaGeoLive = new TLRPC.TL_messageMediaGeoLive();
                final TLRPC.BotInlineMessage send_message4 = botInlineResult.send_message;
                tl_messageMediaGeoLive.period = send_message4.period;
                tl_messageMediaGeoLive.geo = send_message4.geo;
                getInstance(selectedAccount).sendMessage(tl_messageMediaGeoLive, n, messageObject, botInlineResult.send_message.reply_markup, hashMap);
            }
            else {
                final TLRPC.TL_messageMediaGeo tl_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
                tl_messageMediaGeo.geo = botInlineResult.send_message.geo;
                getInstance(selectedAccount).sendMessage(tl_messageMediaGeo, n, messageObject, botInlineResult.send_message.reply_markup, hashMap);
            }
        }
        else if (send_message instanceof TLRPC.TL_botInlineMessageMediaContact) {
            final TLRPC.TL_user tl_user = new TLRPC.TL_user();
            final TLRPC.BotInlineMessage send_message5 = botInlineResult.send_message;
            tl_user.phone = send_message5.phone_number;
            tl_user.first_name = send_message5.first_name;
            tl_user.last_name = send_message5.last_name;
            tl_user.restriction_reason = send_message5.vcard;
            getInstance(selectedAccount).sendMessage(tl_user, n, messageObject, botInlineResult.send_message.reply_markup, hashMap);
        }
    }
    
    public static void prepareSendingDocument(final String e, final String e2, final Uri e3, final String s, final String s2, final long n, final MessageObject messageObject, final InputContentInfoCompat inputContentInfoCompat, final MessageObject messageObject2) {
        if ((e == null || e2 == null) && e3 == null) {
            return;
        }
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<Uri> list3 = null;
        if (e3 != null) {
            list3 = new ArrayList<Uri>();
            list3.add(e3);
        }
        if (e != null) {
            list.add(e);
            list2.add(e2);
        }
        prepareSendingDocuments(list, list2, list3, s, s2, n, messageObject, inputContentInfoCompat, messageObject2);
    }
    
    private static boolean prepareSendingDocumentInternal(final int p0, final String p1, final String p2, final Uri p3, final String p4, final long p5, final MessageObject p6, final CharSequence p7, final ArrayList<TLRPC.MessageEntity> p8, final MessageObject p9, final boolean p10) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          12
        //     3: aload_1        
        //     4: ifnull          14
        //     7: aload_1        
        //     8: invokevirtual   java/lang/String.length:()I
        //    11: ifne            20
        //    14: aload_3        
        //    15: ifnonnull       20
        //    18: iconst_0       
        //    19: ireturn        
        //    20: aload_3        
        //    21: ifnull          33
        //    24: aload_3        
        //    25: invokestatic    org/telegram/messenger/AndroidUtilities.isInternalUri:(Landroid/net/Uri;)Z
        //    28: ifeq            33
        //    31: iconst_0       
        //    32: ireturn        
        //    33: aload_1        
        //    34: ifnull          56
        //    37: new             Ljava/io/File;
        //    40: dup            
        //    41: aload_1        
        //    42: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    45: invokestatic    android/net/Uri.fromFile:(Ljava/io/File;)Landroid/net/Uri;
        //    48: invokestatic    org/telegram/messenger/AndroidUtilities.isInternalUri:(Landroid/net/Uri;)Z
        //    51: ifeq            56
        //    54: iconst_0       
        //    55: ireturn        
        //    56: invokestatic    android/webkit/MimeTypeMap.getSingleton:()Landroid/webkit/MimeTypeMap;
        //    59: astore          13
        //    61: aload_3        
        //    62: ifnull          128
        //    65: aload           4
        //    67: ifnull          81
        //    70: aload           13
        //    72: aload           4
        //    74: invokevirtual   android/webkit/MimeTypeMap.getExtensionFromMimeType:(Ljava/lang/String;)Ljava/lang/String;
        //    77: astore_1       
        //    78: goto            83
        //    81: aconst_null    
        //    82: astore_1       
        //    83: aload_1        
        //    84: ifnonnull       97
        //    87: ldc_w           "txt"
        //    90: astore_1       
        //    91: iconst_0       
        //    92: istore          14
        //    94: goto            100
        //    97: iconst_1       
        //    98: istore          14
        //   100: aload_3        
        //   101: aload_1        
        //   102: invokestatic    org/telegram/messenger/MediaController.copyFileToCache:(Landroid/net/Uri;Ljava/lang/String;)Ljava/lang/String;
        //   105: astore          15
        //   107: aload           15
        //   109: ifnonnull       114
        //   112: iconst_0       
        //   113: ireturn        
        //   114: iload           14
        //   116: ifne            125
        //   119: aload           15
        //   121: astore_1       
        //   122: goto            128
        //   125: goto            135
        //   128: aconst_null    
        //   129: astore_2       
        //   130: aload_1        
        //   131: astore          15
        //   133: aload_2        
        //   134: astore_1       
        //   135: new             Ljava/io/File;
        //   138: dup            
        //   139: aload           15
        //   141: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   144: astore          16
        //   146: aload           16
        //   148: invokevirtual   java/io/File.exists:()Z
        //   151: ifeq            1905
        //   154: aload           16
        //   156: invokevirtual   java/io/File.length:()J
        //   159: lconst_0       
        //   160: lcmp           
        //   161: ifne            167
        //   164: goto            1905
        //   167: lload           5
        //   169: l2i            
        //   170: ifne            179
        //   173: iconst_1       
        //   174: istore          17
        //   176: goto            182
        //   179: iconst_0       
        //   180: istore          17
        //   182: aload           16
        //   184: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   187: astore          18
        //   189: aload_1        
        //   190: ifnull          199
        //   193: aload_1        
        //   194: astore          19
        //   196: goto            232
        //   199: aload           15
        //   201: bipush          46
        //   203: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   206: istore          14
        //   208: iload           14
        //   210: iconst_m1      
        //   211: if_icmpeq       227
        //   214: aload           15
        //   216: iload           14
        //   218: iconst_1       
        //   219: iadd           
        //   220: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   223: astore_1       
        //   224: goto            193
        //   227: ldc_w           ""
        //   230: astore          19
        //   232: aload           19
        //   234: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   237: astore          20
        //   239: aload           20
        //   241: ldc_w           "mp3"
        //   244: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   247: ifne            583
        //   250: aload           20
        //   252: ldc_w           "m4a"
        //   255: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   258: ifeq            264
        //   261: goto            583
        //   264: aload           20
        //   266: ldc_w           "opus"
        //   269: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   272: ifne            310
        //   275: aload           20
        //   277: ldc_w           "ogg"
        //   280: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   283: ifne            310
        //   286: aload           20
        //   288: ldc_w           "flac"
        //   291: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   294: ifeq            300
        //   297: goto            310
        //   300: iconst_0       
        //   301: istore          14
        //   303: aconst_null    
        //   304: astore_1       
        //   305: aconst_null    
        //   306: astore_2       
        //   307: goto            622
        //   310: new             Landroid/media/MediaMetadataRetriever;
        //   313: astore_1       
        //   314: aload_1        
        //   315: invokespecial   android/media/MediaMetadataRetriever.<init>:()V
        //   318: aload_1        
        //   319: astore          4
        //   321: aload_1        
        //   322: aload           16
        //   324: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   327: invokevirtual   android/media/MediaMetadataRetriever.setDataSource:(Ljava/lang/String;)V
        //   330: aload_1        
        //   331: astore          4
        //   333: aload_1        
        //   334: bipush          9
        //   336: invokevirtual   android/media/MediaMetadataRetriever.extractMetadata:(I)Ljava/lang/String;
        //   339: astore_2       
        //   340: aload_2        
        //   341: ifnull          416
        //   344: aload_1        
        //   345: astore          4
        //   347: aload_2        
        //   348: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   351: l2f            
        //   352: ldc_w           1000.0
        //   355: fdiv           
        //   356: f2d            
        //   357: invokestatic    java/lang/Math.ceil:(D)D
        //   360: dstore          21
        //   362: dload           21
        //   364: d2i            
        //   365: istore          14
        //   367: aload_1        
        //   368: astore          4
        //   370: aload_1        
        //   371: bipush          7
        //   373: invokevirtual   android/media/MediaMetadataRetriever.extractMetadata:(I)Ljava/lang/String;
        //   376: astore          23
        //   378: aload_1        
        //   379: astore          4
        //   381: aload_1        
        //   382: iconst_2       
        //   383: invokevirtual   android/media/MediaMetadataRetriever.extractMetadata:(I)Ljava/lang/String;
        //   386: astore_3       
        //   387: aload           23
        //   389: astore_2       
        //   390: goto            423
        //   393: astore_2       
        //   394: aload           23
        //   396: astore_3       
        //   397: aload_1        
        //   398: astore          23
        //   400: goto            525
        //   403: astore_2       
        //   404: aconst_null    
        //   405: astore_3       
        //   406: aload_1        
        //   407: astore          23
        //   409: goto            525
        //   412: astore_2       
        //   413: goto            504
        //   416: aconst_null    
        //   417: astore_3       
        //   418: aconst_null    
        //   419: astore_2       
        //   420: iconst_0       
        //   421: istore          14
        //   423: aload           10
        //   425: ifnonnull       483
        //   428: aload_1        
        //   429: astore          4
        //   431: aload           20
        //   433: ldc_w           "ogg"
        //   436: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   439: ifeq            483
        //   442: aload_1        
        //   443: astore          4
        //   445: aload           16
        //   447: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   450: invokestatic    org/telegram/messenger/MediaController.isOpusFile:(Ljava/lang/String;)I
        //   453: istore          24
        //   455: iload           24
        //   457: iconst_1       
        //   458: if_icmpne       483
        //   461: iconst_1       
        //   462: istore          24
        //   464: goto            486
        //   467: astore          25
        //   469: aload_3        
        //   470: astore          4
        //   472: aload_2        
        //   473: astore_3       
        //   474: aload_1        
        //   475: astore          23
        //   477: aload           4
        //   479: astore_1       
        //   480: goto            530
        //   483: iconst_0       
        //   484: istore          24
        //   486: aload_1        
        //   487: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   490: goto            498
        //   493: astore_1       
        //   494: aload_1        
        //   495: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   498: aload_3        
        //   499: astore_1       
        //   500: goto            625
        //   503: astore_2       
        //   504: goto            517
        //   507: astore_1       
        //   508: aconst_null    
        //   509: astore          4
        //   511: goto            563
        //   514: astore_2       
        //   515: aconst_null    
        //   516: astore_1       
        //   517: aconst_null    
        //   518: astore_3       
        //   519: iconst_0       
        //   520: istore          14
        //   522: aload_1        
        //   523: astore          23
        //   525: aconst_null    
        //   526: astore_1       
        //   527: aload_2        
        //   528: astore          25
        //   530: aload           23
        //   532: astore          4
        //   534: aload           25
        //   536: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   539: aload           23
        //   541: ifnull          557
        //   544: aload           23
        //   546: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   549: goto            557
        //   552: astore_2       
        //   553: aload_2        
        //   554: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   557: aload_3        
        //   558: astore_2       
        //   559: goto            622
        //   562: astore_1       
        //   563: aload           4
        //   565: ifnull          581
        //   568: aload           4
        //   570: invokevirtual   android/media/MediaMetadataRetriever.release:()V
        //   573: goto            581
        //   576: astore_2       
        //   577: aload_2        
        //   578: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   581: aload_1        
        //   582: athrow         
        //   583: aload           16
        //   585: invokestatic    org/telegram/messenger/audioinfo/AudioInfo.getAudioInfo:(Ljava/io/File;)Lorg/telegram/messenger/audioinfo/AudioInfo;
        //   588: astore_2       
        //   589: aload_2        
        //   590: ifnull          615
        //   593: aload_2        
        //   594: invokevirtual   org/telegram/messenger/audioinfo/AudioInfo.getDuration:()J
        //   597: lconst_0       
        //   598: lcmp           
        //   599: ifeq            615
        //   602: aload_2        
        //   603: invokevirtual   org/telegram/messenger/audioinfo/AudioInfo.getArtist:()Ljava/lang/String;
        //   606: astore_1       
        //   607: aload_2        
        //   608: invokevirtual   org/telegram/messenger/audioinfo/AudioInfo.getTitle:()Ljava/lang/String;
        //   611: astore_2       
        //   612: goto            619
        //   615: aconst_null    
        //   616: astore_2       
        //   617: aconst_null    
        //   618: astore_1       
        //   619: iconst_0       
        //   620: istore          14
        //   622: iconst_0       
        //   623: istore          24
        //   625: iload           14
        //   627: ifeq            728
        //   630: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeAudio;
        //   633: dup            
        //   634: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeAudio.<init>:()V
        //   637: astore          4
        //   639: aload           4
        //   641: iload           14
        //   643: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.duration:I
        //   646: aload           4
        //   648: aload_2        
        //   649: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.title:Ljava/lang/String;
        //   652: aload           4
        //   654: aload_1        
        //   655: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.performer:Ljava/lang/String;
        //   658: aload           4
        //   660: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.title:Ljava/lang/String;
        //   663: ifnonnull       674
        //   666: aload           4
        //   668: ldc_w           ""
        //   671: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.title:Ljava/lang/String;
        //   674: aload           4
        //   676: aload           4
        //   678: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.flags:I
        //   681: iconst_1       
        //   682: ior            
        //   683: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.flags:I
        //   686: aload           4
        //   688: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.performer:Ljava/lang/String;
        //   691: ifnonnull       702
        //   694: aload           4
        //   696: ldc_w           ""
        //   699: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.performer:Ljava/lang/String;
        //   702: aload           4
        //   704: aload           4
        //   706: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.flags:I
        //   709: iconst_2       
        //   710: ior            
        //   711: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.flags:I
        //   714: iload           24
        //   716: ifeq            725
        //   719: aload           4
        //   721: iconst_1       
        //   722: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.voice:Z
        //   725: goto            731
        //   728: aconst_null    
        //   729: astore          4
        //   731: aload           12
        //   733: astore_1       
        //   734: aload           12
        //   736: ifnull          843
        //   739: aload           12
        //   741: ldc_w           "attheme"
        //   744: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   747: ifeq            759
        //   750: aload           12
        //   752: astore_1       
        //   753: iconst_1       
        //   754: istore          14
        //   756: goto            846
        //   759: aload           4
        //   761: ifnull          805
        //   764: new             Ljava/lang/StringBuilder;
        //   767: dup            
        //   768: invokespecial   java/lang/StringBuilder.<init>:()V
        //   771: astore_1       
        //   772: aload_1        
        //   773: aload           12
        //   775: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   778: pop            
        //   779: aload_1        
        //   780: ldc_w           "audio"
        //   783: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   786: pop            
        //   787: aload_1        
        //   788: aload           16
        //   790: invokevirtual   java/io/File.length:()J
        //   793: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   796: pop            
        //   797: aload_1        
        //   798: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   801: astore_1       
        //   802: goto            843
        //   805: new             Ljava/lang/StringBuilder;
        //   808: dup            
        //   809: invokespecial   java/lang/StringBuilder.<init>:()V
        //   812: astore_1       
        //   813: aload_1        
        //   814: aload           12
        //   816: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   819: pop            
        //   820: aload_1        
        //   821: ldc_w           ""
        //   824: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   827: pop            
        //   828: aload_1        
        //   829: aload           16
        //   831: invokevirtual   java/io/File.length:()J
        //   834: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   837: pop            
        //   838: aload_1        
        //   839: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   842: astore_1       
        //   843: iconst_0       
        //   844: istore          14
        //   846: iload           14
        //   848: ifne            1049
        //   851: iload           17
        //   853: ifne            1049
        //   856: iload_0        
        //   857: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        //   860: astore_2       
        //   861: iload           17
        //   863: ifne            872
        //   866: iconst_1       
        //   867: istore          14
        //   869: goto            875
        //   872: iconst_4       
        //   873: istore          14
        //   875: aload_2        
        //   876: aload_1        
        //   877: iload           14
        //   879: invokevirtual   org/telegram/messenger/MessagesStorage.getSentFile:(Ljava/lang/String;I)[Ljava/lang/Object;
        //   882: astore_3       
        //   883: aload_3        
        //   884: ifnull          904
        //   887: aload_3        
        //   888: iconst_0       
        //   889: aaload         
        //   890: checkcast       Lorg/telegram/tgnet/TLRPC$TL_document;
        //   893: astore_2       
        //   894: aload_3        
        //   895: iconst_1       
        //   896: aaload         
        //   897: checkcast       Ljava/lang/String;
        //   900: astore_3       
        //   901: goto            908
        //   904: aconst_null    
        //   905: astore_3       
        //   906: aconst_null    
        //   907: astore_2       
        //   908: aload_2        
        //   909: ifnonnull       1016
        //   912: aload           15
        //   914: aload_1        
        //   915: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   918: ifne            1016
        //   921: iload           17
        //   923: ifne            1016
        //   926: iload_0        
        //   927: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        //   930: astore          12
        //   932: new             Ljava/lang/StringBuilder;
        //   935: dup            
        //   936: invokespecial   java/lang/StringBuilder.<init>:()V
        //   939: astore          23
        //   941: aload           23
        //   943: aload           15
        //   945: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   948: pop            
        //   949: aload           23
        //   951: aload           16
        //   953: invokevirtual   java/io/File.length:()J
        //   956: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   959: pop            
        //   960: aload           23
        //   962: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   965: astore          23
        //   967: iload           17
        //   969: ifne            978
        //   972: iconst_1       
        //   973: istore          14
        //   975: goto            981
        //   978: iconst_4       
        //   979: istore          14
        //   981: aload           12
        //   983: aload           23
        //   985: iload           14
        //   987: invokevirtual   org/telegram/messenger/MessagesStorage.getSentFile:(Ljava/lang/String;I)[Ljava/lang/Object;
        //   990: astore          12
        //   992: aload           12
        //   994: ifnull          1016
        //   997: aload           12
        //   999: iconst_0       
        //  1000: aaload         
        //  1001: checkcast       Lorg/telegram/tgnet/TLRPC$TL_document;
        //  1004: astore_3       
        //  1005: aload           12
        //  1007: iconst_1       
        //  1008: aaload         
        //  1009: checkcast       Ljava/lang/String;
        //  1012: astore_2       
        //  1013: goto            1024
        //  1016: aload_2        
        //  1017: astore          12
        //  1019: aload_3        
        //  1020: astore_2       
        //  1021: aload           12
        //  1023: astore_3       
        //  1024: ldc_w           ""
        //  1027: astore          23
        //  1029: iload_0        
        //  1030: iload           17
        //  1032: aload_3        
        //  1033: aload           15
        //  1035: aconst_null    
        //  1036: lconst_0       
        //  1037: invokestatic    org/telegram/messenger/SendMessagesHelper.ensureMediaThumbExists:(IZLorg/telegram/tgnet/TLObject;Ljava/lang/String;Landroid/net/Uri;J)V
        //  1040: aload_2        
        //  1041: astore          12
        //  1043: aload           23
        //  1045: astore_2       
        //  1046: goto            1058
        //  1049: ldc_w           ""
        //  1052: astore_2       
        //  1053: aconst_null    
        //  1054: astore_3       
        //  1055: aconst_null    
        //  1056: astore          12
        //  1058: aload_1        
        //  1059: astore          23
        //  1061: aload_3        
        //  1062: astore_1       
        //  1063: aload_3        
        //  1064: ifnonnull       1822
        //  1067: new             Lorg/telegram/tgnet/TLRPC$TL_document;
        //  1070: dup            
        //  1071: invokespecial   org/telegram/tgnet/TLRPC$TL_document.<init>:()V
        //  1074: astore_3       
        //  1075: aload_3        
        //  1076: lconst_0       
        //  1077: putfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  1080: aload_3        
        //  1081: iload_0        
        //  1082: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
        //  1085: invokevirtual   org/telegram/tgnet/ConnectionsManager.getCurrentTime:()I
        //  1088: putfield        org/telegram/tgnet/TLRPC$Document.date:I
        //  1091: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeFilename;
        //  1094: dup            
        //  1095: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeFilename.<init>:()V
        //  1098: astore_1       
        //  1099: aload_1        
        //  1100: aload           18
        //  1102: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.file_name:Ljava/lang/String;
        //  1105: aload_3        
        //  1106: iconst_0       
        //  1107: newarray        B
        //  1109: putfield        org/telegram/tgnet/TLRPC$Document.file_reference:[B
        //  1112: aload_3        
        //  1113: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  1116: aload_1        
        //  1117: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1120: pop            
        //  1121: aload_3        
        //  1122: aload           16
        //  1124: invokevirtual   java/io/File.length:()J
        //  1127: l2i            
        //  1128: putfield        org/telegram/tgnet/TLRPC$Document.size:I
        //  1131: aload_3        
        //  1132: iconst_0       
        //  1133: putfield        org/telegram/tgnet/TLRPC$Document.dc_id:I
        //  1136: aload           4
        //  1138: ifnull          1151
        //  1141: aload_3        
        //  1142: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  1145: aload           4
        //  1147: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1150: pop            
        //  1151: aload           19
        //  1153: invokevirtual   java/lang/String.length:()I
        //  1156: ifeq            1460
        //  1159: aload           20
        //  1161: invokevirtual   java/lang/String.hashCode:()I
        //  1164: lookupswitch {
        //           106458: 1312
        //           108272: 1295
        //           109967: 1278
        //          3145576: 1261
        //          3418175: 1244
        //          3645340: 1227
        //          default: 1224
        //        }
        //  1224: goto            1329
        //  1227: aload           20
        //  1229: ldc_w           "webp"
        //  1232: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1235: ifeq            1329
        //  1238: iconst_0       
        //  1239: istore          14
        //  1241: goto            1332
        //  1244: aload           20
        //  1246: ldc_w           "opus"
        //  1249: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1252: ifeq            1329
        //  1255: iconst_1       
        //  1256: istore          14
        //  1258: goto            1332
        //  1261: aload           20
        //  1263: ldc_w           "flac"
        //  1266: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1269: ifeq            1329
        //  1272: iconst_5       
        //  1273: istore          14
        //  1275: goto            1332
        //  1278: aload           20
        //  1280: ldc_w           "ogg"
        //  1283: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1286: ifeq            1329
        //  1289: iconst_4       
        //  1290: istore          14
        //  1292: goto            1332
        //  1295: aload           20
        //  1297: ldc_w           "mp3"
        //  1300: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1303: ifeq            1329
        //  1306: iconst_2       
        //  1307: istore          14
        //  1309: goto            1332
        //  1312: aload           20
        //  1314: ldc_w           "m4a"
        //  1317: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1320: ifeq            1329
        //  1323: iconst_3       
        //  1324: istore          14
        //  1326: goto            1332
        //  1329: iconst_m1      
        //  1330: istore          14
        //  1332: iload           14
        //  1334: ifeq            1450
        //  1337: iload           14
        //  1339: iconst_1       
        //  1340: if_icmpeq       1440
        //  1343: iload           14
        //  1345: iconst_2       
        //  1346: if_icmpeq       1430
        //  1349: iload           14
        //  1351: iconst_3       
        //  1352: if_icmpeq       1420
        //  1355: iload           14
        //  1357: iconst_4       
        //  1358: if_icmpeq       1410
        //  1361: iload           14
        //  1363: iconst_5       
        //  1364: if_icmpeq       1400
        //  1367: aload           13
        //  1369: aload           20
        //  1371: invokevirtual   android/webkit/MimeTypeMap.getMimeTypeFromExtension:(Ljava/lang/String;)Ljava/lang/String;
        //  1374: astore          4
        //  1376: aload           4
        //  1378: ifnull          1390
        //  1381: aload_3        
        //  1382: aload           4
        //  1384: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1387: goto            1467
        //  1390: aload_3        
        //  1391: ldc_w           "application/octet-stream"
        //  1394: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1397: goto            1467
        //  1400: aload_3        
        //  1401: ldc_w           "audio/flac"
        //  1404: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1407: goto            1467
        //  1410: aload_3        
        //  1411: ldc_w           "audio/ogg"
        //  1414: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1417: goto            1467
        //  1420: aload_3        
        //  1421: ldc_w           "audio/m4a"
        //  1424: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1427: goto            1467
        //  1430: aload_3        
        //  1431: ldc_w           "audio/mpeg"
        //  1434: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1437: goto            1467
        //  1440: aload_3        
        //  1441: ldc_w           "audio/opus"
        //  1444: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1447: goto            1467
        //  1450: aload_3        
        //  1451: ldc_w           "image/webp"
        //  1454: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1457: goto            1467
        //  1460: aload_3        
        //  1461: ldc_w           "application/octet-stream"
        //  1464: putfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1467: aload_3        
        //  1468: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1471: ldc_w           "image/gif"
        //  1474: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1477: ifeq            1599
        //  1480: aload           10
        //  1482: ifnull          1495
        //  1485: aload           10
        //  1487: invokevirtual   org/telegram/messenger/MessageObject.getGroupIdForUse:()J
        //  1490: lconst_0       
        //  1491: lcmp           
        //  1492: ifne            1599
        //  1495: aload           16
        //  1497: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //  1500: aconst_null    
        //  1501: ldc_w           90.0
        //  1504: ldc_w           90.0
        //  1507: iconst_1       
        //  1508: invokestatic    org/telegram/messenger/ImageLoader.loadBitmap:(Ljava/lang/String;Landroid/net/Uri;FFZ)Landroid/graphics/Bitmap;
        //  1511: astore          4
        //  1513: aload           4
        //  1515: ifnull          1599
        //  1518: aload_1        
        //  1519: ldc_w           "animation.gif"
        //  1522: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.file_name:Ljava/lang/String;
        //  1525: aload_3        
        //  1526: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  1529: astore_1       
        //  1530: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeAnimated;
        //  1533: astore          19
        //  1535: aload           19
        //  1537: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeAnimated.<init>:()V
        //  1540: aload_1        
        //  1541: aload           19
        //  1543: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1546: pop            
        //  1547: aload           4
        //  1549: ldc_w           90.0
        //  1552: ldc_w           90.0
        //  1555: bipush          55
        //  1557: iload           17
        //  1559: invokestatic    org/telegram/messenger/ImageLoader.scaleAndSaveImage:(Landroid/graphics/Bitmap;FFIZ)Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  1562: astore_1       
        //  1563: aload_1        
        //  1564: ifnull          1586
        //  1567: aload_3        
        //  1568: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  1571: aload_1        
        //  1572: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1575: pop            
        //  1576: aload_3        
        //  1577: aload_3        
        //  1578: getfield        org/telegram/tgnet/TLRPC$Document.flags:I
        //  1581: iconst_1       
        //  1582: ior            
        //  1583: putfield        org/telegram/tgnet/TLRPC$Document.flags:I
        //  1586: aload           4
        //  1588: invokevirtual   android/graphics/Bitmap.recycle:()V
        //  1591: goto            1599
        //  1594: astore_1       
        //  1595: aload_1        
        //  1596: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1599: aload_3        
        //  1600: astore_1       
        //  1601: aload_3        
        //  1602: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  1605: ldc_w           "image/webp"
        //  1608: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1611: ifeq            1822
        //  1614: aload_3        
        //  1615: astore_1       
        //  1616: iload           17
        //  1618: iconst_1       
        //  1619: ixor           
        //  1620: ifeq            1822
        //  1623: aload_3        
        //  1624: astore_1       
        //  1625: aload           10
        //  1627: ifnonnull       1822
        //  1630: new             Landroid/graphics/BitmapFactory$Options;
        //  1633: dup            
        //  1634: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //  1637: astore          4
        //  1639: aload           4
        //  1641: iconst_1       
        //  1642: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //  1645: new             Ljava/io/RandomAccessFile;
        //  1648: astore_1       
        //  1649: aload_1        
        //  1650: aload           15
        //  1652: ldc_w           "r"
        //  1655: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1658: aload_1        
        //  1659: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
        //  1662: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
        //  1665: lconst_0       
        //  1666: aload           15
        //  1668: invokevirtual   java/lang/String.length:()I
        //  1671: i2l            
        //  1672: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
        //  1675: astore          19
        //  1677: aconst_null    
        //  1678: aload           19
        //  1680: aload           19
        //  1682: invokevirtual   java/nio/ByteBuffer.limit:()I
        //  1685: aload           4
        //  1687: iconst_1       
        //  1688: invokestatic    org/telegram/messenger/Utilities.loadWebpImage:(Landroid/graphics/Bitmap;Ljava/nio/ByteBuffer;ILandroid/graphics/BitmapFactory$Options;Z)Z
        //  1691: pop            
        //  1692: aload_1        
        //  1693: invokevirtual   java/io/RandomAccessFile.close:()V
        //  1696: goto            1704
        //  1699: astore_1       
        //  1700: aload_1        
        //  1701: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1704: aload           4
        //  1706: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //  1709: istore          24
        //  1711: aload_3        
        //  1712: astore_1       
        //  1713: iload           24
        //  1715: ifeq            1822
        //  1718: aload           4
        //  1720: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //  1723: istore          14
        //  1725: aload_3        
        //  1726: astore_1       
        //  1727: iload           14
        //  1729: ifeq            1822
        //  1732: aload_3        
        //  1733: astore_1       
        //  1734: iload           24
        //  1736: sipush          800
        //  1739: if_icmpgt       1822
        //  1742: aload_3        
        //  1743: astore_1       
        //  1744: iload           14
        //  1746: sipush          800
        //  1749: if_icmpgt       1822
        //  1752: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeSticker;
        //  1755: dup            
        //  1756: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeSticker.<init>:()V
        //  1759: astore_1       
        //  1760: aload_1        
        //  1761: aload_2        
        //  1762: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.alt:Ljava/lang/String;
        //  1765: aload_1        
        //  1766: new             Lorg/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty;
        //  1769: dup            
        //  1770: invokespecial   org/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty.<init>:()V
        //  1773: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  1776: aload_3        
        //  1777: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  1780: aload_1        
        //  1781: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1784: pop            
        //  1785: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeImageSize;
        //  1788: dup            
        //  1789: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeImageSize.<init>:()V
        //  1792: astore_1       
        //  1793: aload_1        
        //  1794: aload           4
        //  1796: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //  1799: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.w:I
        //  1802: aload_1        
        //  1803: aload           4
        //  1805: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //  1808: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.h:I
        //  1811: aload_3        
        //  1812: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  1815: aload_1        
        //  1816: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1819: pop            
        //  1820: aload_3        
        //  1821: astore_1       
        //  1822: aload           8
        //  1824: ifnull          1838
        //  1827: aload           8
        //  1829: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //  1834: astore_2       
        //  1835: goto            1838
        //  1838: new             Ljava/util/HashMap;
        //  1841: dup            
        //  1842: invokespecial   java/util/HashMap.<init>:()V
        //  1845: astore_3       
        //  1846: aload           23
        //  1848: ifnull          1861
        //  1851: aload_3        
        //  1852: ldc_w           "originalPath"
        //  1855: aload           23
        //  1857: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1860: pop            
        //  1861: iload           11
        //  1863: ifeq            1877
        //  1866: aload_3        
        //  1867: ldc_w           "forceDocument"
        //  1870: ldc_w           "1"
        //  1873: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1876: pop            
        //  1877: new             Lorg/telegram/messenger/_$$Lambda$SendMessagesHelper$oWtR0AbMdP04znHWUTItf_tR4Wc;
        //  1880: dup            
        //  1881: aload           10
        //  1883: iload_0        
        //  1884: aload_1        
        //  1885: aload           15
        //  1887: aload_3        
        //  1888: aload           12
        //  1890: lload           5
        //  1892: aload           7
        //  1894: aload_2        
        //  1895: aload           9
        //  1897: invokespecial   org/telegram/messenger/_$$Lambda$SendMessagesHelper$oWtR0AbMdP04znHWUTItf_tR4Wc.<init>:(Lorg/telegram/messenger/MessageObject;ILorg/telegram/tgnet/TLRPC$TL_document;Ljava/lang/String;Ljava/util/HashMap;Ljava/lang/String;JLorg/telegram/messenger/MessageObject;Ljava/lang/String;Ljava/util/ArrayList;)V
        //  1900: invokestatic    org/telegram/messenger/AndroidUtilities.runOnUIThread:(Ljava/lang/Runnable;)V
        //  1903: iconst_1       
        //  1904: ireturn        
        //  1905: iconst_0       
        //  1906: ireturn        
        //    Signature:
        //  (ILjava/lang/String;Ljava/lang/String;Landroid/net/Uri;Ljava/lang/String;JLorg/telegram/messenger/MessageObject;Ljava/lang/CharSequence;Ljava/util/ArrayList<Lorg/telegram/tgnet/TLRPC$MessageEntity;>;Lorg/telegram/messenger/MessageObject;Z)Z
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  310    318    514    517    Ljava/lang/Exception;
        //  310    318    507    514    Any
        //  321    330    503    504    Ljava/lang/Exception;
        //  321    330    562    563    Any
        //  333    340    503    504    Ljava/lang/Exception;
        //  333    340    562    563    Any
        //  347    362    412    416    Ljava/lang/Exception;
        //  347    362    562    563    Any
        //  370    378    403    412    Ljava/lang/Exception;
        //  370    378    562    563    Any
        //  381    387    393    403    Ljava/lang/Exception;
        //  381    387    562    563    Any
        //  431    442    467    483    Ljava/lang/Exception;
        //  431    442    562    563    Any
        //  445    455    467    483    Ljava/lang/Exception;
        //  445    455    562    563    Any
        //  486    490    493    498    Ljava/lang/Exception;
        //  534    539    562    563    Any
        //  544    549    552    557    Ljava/lang/Exception;
        //  568    573    576    581    Ljava/lang/Exception;
        //  1495   1513   1594   1599   Ljava/lang/Exception;
        //  1518   1563   1594   1599   Ljava/lang/Exception;
        //  1567   1586   1594   1599   Ljava/lang/Exception;
        //  1586   1591   1594   1599   Ljava/lang/Exception;
        //  1639   1696   1699   1704   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0416:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void prepareSendingDocuments(final ArrayList<String> list, final ArrayList<String> list2, final ArrayList<Uri> list3, final String s, final String s2, final long n, final MessageObject messageObject, final InputContentInfoCompat inputContentInfoCompat, final MessageObject messageObject2) {
        if ((list == null && list2 == null && list3 == null) || (list != null && list2 != null && list.size() != list2.size())) {
            return;
        }
        new Thread(new _$$Lambda$SendMessagesHelper$u_MDqklxXvAmqp9ZSz6JzWZDqR0(list, UserConfig.selectedAccount, list2, s2, n, messageObject, s, messageObject2, list3, inputContentInfoCompat)).start();
    }
    
    public static void prepareSendingLocation(final Location location, final long n) {
        final int selectedAccount = UserConfig.selectedAccount;
        MessagesStorage.getInstance(selectedAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE(location, selectedAccount, n));
    }
    
    public static void prepareSendingMedia(final ArrayList<SendingMediaInfo> list, final long n, final MessageObject messageObject, final InputContentInfoCompat inputContentInfoCompat, final boolean b, final boolean b2, final MessageObject messageObject2) {
        if (list.isEmpty()) {
            return;
        }
        SendMessagesHelper.mediaSendQueue.postRunnable(new _$$Lambda$SendMessagesHelper$x39RupUe_lkQUIgui46eiCkD1zA(list, n, UserConfig.selectedAccount, b, b2, messageObject2, messageObject, inputContentInfoCompat));
    }
    
    public static void prepareSendingPhoto(final String path, final Uri uri, final long n, final MessageObject messageObject, final CharSequence charSequence, final ArrayList<TLRPC.MessageEntity> entities, final ArrayList<TLRPC.InputDocument> c, final InputContentInfoCompat inputContentInfoCompat, final int ttl, final MessageObject messageObject2) {
        final SendingMediaInfo e = new SendingMediaInfo();
        e.path = path;
        e.uri = uri;
        if (charSequence != null) {
            e.caption = charSequence.toString();
        }
        e.entities = entities;
        e.ttl = ttl;
        if (c != null && !c.isEmpty()) {
            e.masks = new ArrayList<TLRPC.InputDocument>(c);
        }
        final ArrayList<SendingMediaInfo> list = new ArrayList<SendingMediaInfo>();
        list.add(e);
        prepareSendingMedia(list, n, messageObject, inputContentInfoCompat, false, false, messageObject2);
    }
    
    public static void prepareSendingText(final String s, final long n) {
        final int selectedAccount = UserConfig.selectedAccount;
        MessagesStorage.getInstance(selectedAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$mEb_8CvzR9IFpSPa5BhRMXY7oRk(s, selectedAccount, n));
    }
    
    public static void prepareSendingVideo(final String s, final long n, final long n2, final int n3, final int n4, final VideoEditedInfo videoEditedInfo, final long n5, final MessageObject messageObject, final CharSequence charSequence, final ArrayList<TLRPC.MessageEntity> list, final int n6, final MessageObject messageObject2) {
        if (s != null) {
            if (s.length() != 0) {
                new Thread(new _$$Lambda$SendMessagesHelper$ZxDs0vXJa1NnG2Mq0H0o0iqTbfM(videoEditedInfo, s, n5, n2, n6, UserConfig.selectedAccount, n4, n3, n, charSequence, messageObject2, messageObject, list)).start();
            }
        }
    }
    
    private void putToDelayedMessages(final String s, final DelayedMessage e) {
        ArrayList<DelayedMessage> value;
        if ((value = this.delayedMessages.get(s)) == null) {
            value = new ArrayList<DelayedMessage>();
            this.delayedMessages.put(s, value);
        }
        value.add(e);
    }
    
    private void revertEditingMessageObject(final MessageObject e) {
        e.cancelEditing = true;
        final TLRPC.Message messageOwner = e.messageOwner;
        messageOwner.media = e.previousMedia;
        messageOwner.message = e.previousCaption;
        messageOwner.entities = e.previousCaptionEntities;
        messageOwner.attachPath = e.previousAttachPath;
        messageOwner.send_state = 0;
        e.previousMedia = null;
        e.previousCaption = null;
        e.previousCaptionEntities = null;
        e.previousAttachPath = null;
        e.videoEditedInfo = null;
        e.type = -1;
        e.setType();
        e.caption = null;
        e.generateCaption();
        final ArrayList<TLRPC.Message> list = new ArrayList<TLRPC.Message>();
        list.add(e.messageOwner);
        MessagesStorage.getInstance(this.currentAccount).putMessages(list, false, true, false, 0);
        final ArrayList<MessageObject> list2 = new ArrayList<MessageObject>();
        list2.add(e);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, e.getDialogId(), list2);
    }
    
    private void sendLocation(final Location location) {
        final TLRPC.TL_messageMediaGeo tl_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
        tl_messageMediaGeo.geo = new TLRPC.TL_geoPoint();
        tl_messageMediaGeo.geo.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
        tl_messageMediaGeo.geo._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
        final Iterator<Map.Entry<String, MessageObject>> iterator = this.waitingForLocation.entrySet().iterator();
        while (iterator.hasNext()) {
            final MessageObject messageObject = iterator.next().getValue();
            this.sendMessage(tl_messageMediaGeo, messageObject.getDialogId(), messageObject, null, null);
        }
    }
    
    private void sendMessage(final String p0, final String p1, final TLRPC.MessageMedia p2, final TLRPC.TL_photo p3, final VideoEditedInfo p4, final TLRPC.User p5, final TLRPC.TL_document p6, final TLRPC.TL_game p7, final TLRPC.TL_messageMediaPoll p8, final long p9, final String p10, final MessageObject p11, final TLRPC.WebPage p12, final boolean p13, final MessageObject p14, final ArrayList<TLRPC.MessageEntity> p15, final TLRPC.ReplyMarkup p16, final HashMap<String, String> p17, final int p18, final Object p19) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          22
        //     3: aload_3        
        //     4: astore          23
        //     6: aload           4
        //     8: astore          24
        //    10: aload           14
        //    12: astore          25
        //    14: aload           19
        //    16: astore          26
        //    18: aload           6
        //    20: ifnull          32
        //    23: aload           6
        //    25: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //    28: ifnonnull       32
        //    31: return         
        //    32: lconst_0       
        //    33: lstore          27
        //    35: lload           10
        //    37: lconst_0       
        //    38: lcmp           
        //    39: ifne            43
        //    42: return         
        //    43: aload           22
        //    45: ifnonnull       60
        //    48: aload_2        
        //    49: ifnonnull       60
        //    52: ldc_w           ""
        //    55: astore          29
        //    57: goto            63
        //    60: aload_2        
        //    61: astore          29
        //    63: aload           26
        //    65: ifnull          95
        //    68: aload           26
        //    70: ldc_w           "originalPath"
        //    73: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //    76: ifeq            95
        //    79: aload           26
        //    81: ldc_w           "originalPath"
        //    84: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    87: checkcast       Ljava/lang/String;
        //    90: astore          19
        //    92: goto            98
        //    95: aconst_null    
        //    96: astore          19
        //    98: iconst_m1      
        //    99: istore          30
        //   101: lload           10
        //   103: l2i            
        //   104: istore          31
        //   106: lload           10
        //   108: bipush          32
        //   110: lshr           
        //   111: l2i            
        //   112: istore          32
        //   114: iload           31
        //   116: ifeq            136
        //   119: aload_0        
        //   120: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //   123: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   126: iload           31
        //   128: invokevirtual   org/telegram/messenger/MessagesController.getInputPeer:(I)Lorg/telegram/tgnet/TLRPC$InputPeer;
        //   131: astore          33
        //   133: goto            139
        //   136: aconst_null    
        //   137: astore          33
        //   139: iload           31
        //   141: ifne            234
        //   144: aload_0        
        //   145: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //   148: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   151: iload           32
        //   153: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   156: invokevirtual   org/telegram/messenger/MessagesController.getEncryptedChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //   159: astore_2       
        //   160: aload_2        
        //   161: ifnonnull       231
        //   164: aload           16
        //   166: ifnull          230
        //   169: aload_0        
        //   170: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //   173: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        //   176: aload           16
        //   178: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   181: invokevirtual   org/telegram/messenger/MessagesStorage.markMessageAsSendError:(Lorg/telegram/tgnet/TLRPC$Message;)V
        //   184: aload           16
        //   186: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   189: iconst_2       
        //   190: putfield        org/telegram/tgnet/TLRPC$Message.send_state:I
        //   193: aload_0        
        //   194: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //   197: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //   200: getstatic       org/telegram/messenger/NotificationCenter.messageSendError:I
        //   203: iconst_1       
        //   204: anewarray       Ljava/lang/Object;
        //   207: dup            
        //   208: iconst_0       
        //   209: aload           16
        //   211: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //   214: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   217: aastore        
        //   218: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //   221: aload_0        
        //   222: aload           16
        //   224: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //   227: invokevirtual   org/telegram/messenger/SendMessagesHelper.processSentMessage:(I)V
        //   230: return         
        //   231: goto            293
        //   234: aload           33
        //   236: instanceof      Lorg/telegram/tgnet/TLRPC$TL_inputPeerChannel;
        //   239: ifeq            291
        //   242: aload_0        
        //   243: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //   246: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   249: aload           33
        //   251: getfield        org/telegram/tgnet/TLRPC$InputPeer.channel_id:I
        //   254: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   257: invokevirtual   org/telegram/messenger/MessagesController.getChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$Chat;
        //   260: astore_2       
        //   261: aload_2        
        //   262: ifnull          278
        //   265: aload_2        
        //   266: getfield        org/telegram/tgnet/TLRPC$Chat.megagroup:Z
        //   269: ifne            278
        //   272: iconst_1       
        //   273: istore          34
        //   275: goto            281
        //   278: iconst_0       
        //   279: istore          34
        //   281: aconst_null    
        //   282: astore          35
        //   284: iload           34
        //   286: istore          36
        //   288: goto            299
        //   291: aconst_null    
        //   292: astore_2       
        //   293: iconst_0       
        //   294: istore          36
        //   296: aload_2        
        //   297: astore          35
        //   299: aload           16
        //   301: ifnull          911
        //   304: aload           16
        //   306: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   309: astore          37
        //   311: aload           37
        //   313: astore_2       
        //   314: aload           16
        //   316: invokevirtual   org/telegram/messenger/MessageObject.isForwarded:()Z
        //   319: ifeq            350
        //   322: aload           9
        //   324: astore          14
        //   326: aload           23
        //   328: astore          9
        //   330: aload           24
        //   332: astore_3       
        //   333: iconst_4       
        //   334: istore          34
        //   336: aload           22
        //   338: astore          8
        //   340: aload           37
        //   342: astore_1       
        //   343: aload           25
        //   345: astore          22
        //   347: goto            2986
        //   350: aload           37
        //   352: astore_2       
        //   353: aload           16
        //   355: getfield        org/telegram/messenger/MessageObject.type:I
        //   358: ifne            400
        //   361: aload           37
        //   363: astore_2       
        //   364: aload           16
        //   366: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   369: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   372: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaGame;
        //   375: ifeq            381
        //   378: goto            391
        //   381: aload           37
        //   383: astore_2       
        //   384: aload           37
        //   386: getfield        org/telegram/tgnet/TLRPC$Message.message:Ljava/lang/String;
        //   389: astore          22
        //   391: aload           6
        //   393: astore_1       
        //   394: iconst_0       
        //   395: istore          34
        //   397: goto            428
        //   400: aload           37
        //   402: astore_2       
        //   403: aload           16
        //   405: getfield        org/telegram/messenger/MessageObject.type:I
        //   408: iconst_4       
        //   409: if_icmpne       440
        //   412: aload           37
        //   414: astore_2       
        //   415: aload           37
        //   417: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   420: astore          23
        //   422: aload           6
        //   424: astore_1       
        //   425: iconst_1       
        //   426: istore          34
        //   428: aload           7
        //   430: astore          4
        //   432: aload_1        
        //   433: astore_3       
        //   434: aload           9
        //   436: astore_1       
        //   437: goto            802
        //   440: aload           37
        //   442: astore_2       
        //   443: aload           16
        //   445: getfield        org/telegram/messenger/MessageObject.type:I
        //   448: iconst_1       
        //   449: if_icmpne       477
        //   452: aload           37
        //   454: astore_2       
        //   455: aload           37
        //   457: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   460: getfield        org/telegram/tgnet/TLRPC$MessageMedia.photo:Lorg/telegram/tgnet/TLRPC$Photo;
        //   463: checkcast       Lorg/telegram/tgnet/TLRPC$TL_photo;
        //   466: astore          24
        //   468: aload           6
        //   470: astore_1       
        //   471: iconst_2       
        //   472: istore          34
        //   474: goto            428
        //   477: aload           37
        //   479: astore_2       
        //   480: aload           16
        //   482: getfield        org/telegram/messenger/MessageObject.type:I
        //   485: iconst_3       
        //   486: if_icmpeq       775
        //   489: aload           37
        //   491: astore_2       
        //   492: aload           16
        //   494: getfield        org/telegram/messenger/MessageObject.type:I
        //   497: iconst_5       
        //   498: if_icmpeq       775
        //   501: aload           5
        //   503: ifnull          509
        //   506: goto            775
        //   509: aload           37
        //   511: astore_2       
        //   512: aload           16
        //   514: getfield        org/telegram/messenger/MessageObject.type:I
        //   517: bipush          12
        //   519: if_icmpne       618
        //   522: aload           37
        //   524: astore_2       
        //   525: new             Lorg/telegram/tgnet/TLRPC$TL_userRequest_old2;
        //   528: astore_1       
        //   529: aload           37
        //   531: astore_2       
        //   532: aload_1        
        //   533: invokespecial   org/telegram/tgnet/TLRPC$TL_userRequest_old2.<init>:()V
        //   536: aload           37
        //   538: astore_2       
        //   539: aload_1        
        //   540: aload           37
        //   542: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   545: getfield        org/telegram/tgnet/TLRPC$MessageMedia.phone_number:Ljava/lang/String;
        //   548: putfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //   551: aload           37
        //   553: astore_2       
        //   554: aload_1        
        //   555: aload           37
        //   557: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   560: getfield        org/telegram/tgnet/TLRPC$MessageMedia.first_name:Ljava/lang/String;
        //   563: putfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //   566: aload           37
        //   568: astore_2       
        //   569: aload_1        
        //   570: aload           37
        //   572: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   575: getfield        org/telegram/tgnet/TLRPC$MessageMedia.last_name:Ljava/lang/String;
        //   578: putfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //   581: aload           37
        //   583: astore_2       
        //   584: aload_1        
        //   585: aload           37
        //   587: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   590: getfield        org/telegram/tgnet/TLRPC$MessageMedia.vcard:Ljava/lang/String;
        //   593: putfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //   596: aload           37
        //   598: astore_2       
        //   599: aload_1        
        //   600: aload           37
        //   602: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   605: getfield        org/telegram/tgnet/TLRPC$MessageMedia.user_id:I
        //   608: putfield        org/telegram/tgnet/TLRPC$User.id:I
        //   611: bipush          6
        //   613: istore          34
        //   615: goto            397
        //   618: aload           37
        //   620: astore_2       
        //   621: aload           16
        //   623: getfield        org/telegram/messenger/MessageObject.type:I
        //   626: bipush          8
        //   628: if_icmpeq       753
        //   631: aload           37
        //   633: astore_2       
        //   634: aload           16
        //   636: getfield        org/telegram/messenger/MessageObject.type:I
        //   639: bipush          9
        //   641: if_icmpeq       753
        //   644: aload           37
        //   646: astore_2       
        //   647: aload           16
        //   649: getfield        org/telegram/messenger/MessageObject.type:I
        //   652: bipush          13
        //   654: if_icmpeq       753
        //   657: aload           37
        //   659: astore_2       
        //   660: aload           16
        //   662: getfield        org/telegram/messenger/MessageObject.type:I
        //   665: bipush          14
        //   667: if_icmpne       673
        //   670: goto            753
        //   673: aload           37
        //   675: astore_2       
        //   676: aload           16
        //   678: getfield        org/telegram/messenger/MessageObject.type:I
        //   681: iconst_2       
        //   682: if_icmpne       707
        //   685: aload           37
        //   687: astore_2       
        //   688: aload           37
        //   690: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   693: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //   696: checkcast       Lorg/telegram/tgnet/TLRPC$TL_document;
        //   699: astore_1       
        //   700: bipush          8
        //   702: istore          34
        //   704: goto            793
        //   707: aload           37
        //   709: astore_2       
        //   710: aload           16
        //   712: getfield        org/telegram/messenger/MessageObject.type:I
        //   715: bipush          17
        //   717: if_icmpne       743
        //   720: aload           37
        //   722: astore_2       
        //   723: aload           37
        //   725: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   728: checkcast       Lorg/telegram/tgnet/TLRPC$TL_messageMediaPoll;
        //   731: astore_1       
        //   732: aload           7
        //   734: astore          4
        //   736: bipush          10
        //   738: istore          34
        //   740: goto            799
        //   743: aload           6
        //   745: astore_1       
        //   746: iload           30
        //   748: istore          34
        //   750: goto            397
        //   753: aload           37
        //   755: astore_2       
        //   756: aload           37
        //   758: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   761: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //   764: checkcast       Lorg/telegram/tgnet/TLRPC$TL_document;
        //   767: astore_1       
        //   768: bipush          7
        //   770: istore          34
        //   772: goto            793
        //   775: aload           37
        //   777: astore_2       
        //   778: aload           37
        //   780: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   783: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //   786: checkcast       Lorg/telegram/tgnet/TLRPC$TL_document;
        //   789: astore_1       
        //   790: iconst_3       
        //   791: istore          34
        //   793: aload_1        
        //   794: astore          4
        //   796: aload           9
        //   798: astore_1       
        //   799: aload           6
        //   801: astore_3       
        //   802: aload           26
        //   804: ifnull          828
        //   807: aload           37
        //   809: astore_2       
        //   810: aload           26
        //   812: ldc_w           "query_id"
        //   815: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //   818: ifeq            828
        //   821: bipush          9
        //   823: istore          34
        //   825: goto            828
        //   828: aload           22
        //   830: astore          7
        //   832: aload           37
        //   834: astore_2       
        //   835: aload           37
        //   837: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   840: getfield        org/telegram/tgnet/TLRPC$MessageMedia.ttl_seconds:I
        //   843: ifle            862
        //   846: aload           37
        //   848: astore_2       
        //   849: aload           37
        //   851: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //   854: getfield        org/telegram/tgnet/TLRPC$MessageMedia.ttl_seconds:I
        //   857: istore          20
        //   859: goto            862
        //   862: aload           14
        //   864: astore          22
        //   866: aload_3        
        //   867: astore          6
        //   869: aload           24
        //   871: astore_3       
        //   872: aload_1        
        //   873: astore          14
        //   875: aload           23
        //   877: astore          9
        //   879: aload           7
        //   881: astore          8
        //   883: aload           37
        //   885: astore_1       
        //   886: aload           4
        //   888: astore          7
        //   890: goto            2986
        //   893: astore_3       
        //   894: aload_2        
        //   895: astore_1       
        //   896: aload_3        
        //   897: astore_2       
        //   898: aload_1        
        //   899: astore_3       
        //   900: goto            906
        //   903: astore_2       
        //   904: aconst_null    
        //   905: astore_3       
        //   906: aconst_null    
        //   907: astore_1       
        //   908: goto            12148
        //   911: aload           22
        //   913: ifnull          1134
        //   916: aload           35
        //   918: ifnull          933
        //   921: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //   924: dup            
        //   925: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //   928: astore          14
        //   930: goto            942
        //   933: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //   936: dup            
        //   937: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //   940: astore          14
        //   942: aload           25
        //   944: astore          8
        //   946: aload           35
        //   948: ifnull          1012
        //   951: aload           14
        //   953: astore_2       
        //   954: aload           25
        //   956: astore          8
        //   958: aload           25
        //   960: instanceof      Lorg/telegram/tgnet/TLRPC$TL_webPagePending;
        //   963: ifeq            1012
        //   966: aload           14
        //   968: astore_2       
        //   969: aload           25
        //   971: getfield        org/telegram/tgnet/TLRPC$WebPage.url:Ljava/lang/String;
        //   974: ifnull          1009
        //   977: aload           14
        //   979: astore_2       
        //   980: new             Lorg/telegram/tgnet/TLRPC$TL_webPageUrlPending;
        //   983: astore          8
        //   985: aload           14
        //   987: astore_2       
        //   988: aload           8
        //   990: invokespecial   org/telegram/tgnet/TLRPC$TL_webPageUrlPending.<init>:()V
        //   993: aload           14
        //   995: astore_2       
        //   996: aload           8
        //   998: aload           25
        //  1000: getfield        org/telegram/tgnet/TLRPC$WebPage.url:Ljava/lang/String;
        //  1003: putfield        org/telegram/tgnet/TLRPC$WebPage.url:Ljava/lang/String;
        //  1006: goto            1012
        //  1009: aconst_null    
        //  1010: astore          8
        //  1012: aload           8
        //  1014: ifnonnull       1046
        //  1017: aload           14
        //  1019: astore_2       
        //  1020: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaEmpty;
        //  1023: astore          25
        //  1025: aload           14
        //  1027: astore_2       
        //  1028: aload           25
        //  1030: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaEmpty.<init>:()V
        //  1033: aload           14
        //  1035: astore_2       
        //  1036: aload           14
        //  1038: aload           25
        //  1040: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1043: goto            1085
        //  1046: aload           14
        //  1048: astore_2       
        //  1049: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaWebPage;
        //  1052: astore          25
        //  1054: aload           14
        //  1056: astore_2       
        //  1057: aload           25
        //  1059: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaWebPage.<init>:()V
        //  1062: aload           14
        //  1064: astore_2       
        //  1065: aload           14
        //  1067: aload           25
        //  1069: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1072: aload           14
        //  1074: astore_2       
        //  1075: aload           14
        //  1077: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1080: aload           8
        //  1082: putfield        org/telegram/tgnet/TLRPC$MessageMedia.webpage:Lorg/telegram/tgnet/TLRPC$WebPage;
        //  1085: aload           26
        //  1087: ifnull          1111
        //  1090: aload           14
        //  1092: astore_2       
        //  1093: aload           26
        //  1095: ldc_w           "query_id"
        //  1098: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  1101: ifeq            1111
        //  1104: bipush          9
        //  1106: istore          34
        //  1108: goto            1114
        //  1111: iconst_0       
        //  1112: istore          34
        //  1114: aload           14
        //  1116: astore_2       
        //  1117: aload           14
        //  1119: aload           22
        //  1121: putfield        org/telegram/tgnet/TLRPC$Message.message:Ljava/lang/String;
        //  1124: aload           14
        //  1126: astore_2       
        //  1127: aload           8
        //  1129: astore          24
        //  1131: goto            1667
        //  1134: aload           9
        //  1136: ifnull          1186
        //  1139: aload           35
        //  1141: ifnull          1156
        //  1144: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //  1147: dup            
        //  1148: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //  1151: astore          8
        //  1153: goto            1165
        //  1156: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  1159: dup            
        //  1160: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  1163: astore          8
        //  1165: aload           8
        //  1167: astore_2       
        //  1168: aload           8
        //  1170: aload           9
        //  1172: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1175: bipush          10
        //  1177: istore          34
        //  1179: aload           25
        //  1181: astore          24
        //  1183: goto            2734
        //  1186: aload           23
        //  1188: ifnull          1271
        //  1191: aload           35
        //  1193: ifnull          1208
        //  1196: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //  1199: dup            
        //  1200: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //  1203: astore          8
        //  1205: goto            1217
        //  1208: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  1211: dup            
        //  1212: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  1215: astore          8
        //  1217: aload           8
        //  1219: astore_2       
        //  1220: aload           8
        //  1222: aload           23
        //  1224: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1227: aload           26
        //  1229: ifnull          1261
        //  1232: aload           8
        //  1234: astore_2       
        //  1235: aload           26
        //  1237: ldc_w           "query_id"
        //  1240: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  1243: istore          38
        //  1245: iload           38
        //  1247: ifeq            1261
        //  1250: bipush          9
        //  1252: istore          34
        //  1254: aload           25
        //  1256: astore          24
        //  1258: goto            2734
        //  1261: iconst_1       
        //  1262: istore          34
        //  1264: aload           25
        //  1266: astore          24
        //  1268: goto            2734
        //  1271: aload           24
        //  1273: ifnull          1562
        //  1276: aload           35
        //  1278: ifnull          1293
        //  1281: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //  1284: dup            
        //  1285: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //  1288: astore          8
        //  1290: goto            1302
        //  1293: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  1296: dup            
        //  1297: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  1300: astore          8
        //  1302: aload           8
        //  1304: astore_2       
        //  1305: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaPhoto;
        //  1308: astore          14
        //  1310: aload           8
        //  1312: astore_2       
        //  1313: aload           14
        //  1315: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaPhoto.<init>:()V
        //  1318: aload           8
        //  1320: astore_2       
        //  1321: aload           8
        //  1323: aload           14
        //  1325: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1328: aload           8
        //  1330: astore_2       
        //  1331: aload           8
        //  1333: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1336: astore          14
        //  1338: aload           8
        //  1340: astore_2       
        //  1341: aload           14
        //  1343: aload           14
        //  1345: getfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  1348: iconst_3       
        //  1349: ior            
        //  1350: putfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  1353: aload           17
        //  1355: ifnull          1368
        //  1358: aload           8
        //  1360: astore_2       
        //  1361: aload           8
        //  1363: aload           17
        //  1365: putfield        org/telegram/tgnet/TLRPC$Message.entities:Ljava/util/ArrayList;
        //  1368: iload           20
        //  1370: ifeq            1421
        //  1373: aload           8
        //  1375: astore_2       
        //  1376: aload           8
        //  1378: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1381: iload           20
        //  1383: putfield        org/telegram/tgnet/TLRPC$MessageMedia.ttl_seconds:I
        //  1386: aload           8
        //  1388: astore_2       
        //  1389: aload           8
        //  1391: iload           20
        //  1393: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  1396: aload           8
        //  1398: astore_2       
        //  1399: aload           8
        //  1401: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1404: astore          14
        //  1406: aload           8
        //  1408: astore_2       
        //  1409: aload           14
        //  1411: aload           14
        //  1413: getfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  1416: iconst_4       
        //  1417: ior            
        //  1418: putfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  1421: aload           8
        //  1423: astore_2       
        //  1424: aload           8
        //  1426: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1429: aload           24
        //  1431: putfield        org/telegram/tgnet/TLRPC$MessageMedia.photo:Lorg/telegram/tgnet/TLRPC$Photo;
        //  1434: aload           26
        //  1436: ifnull          1460
        //  1439: aload           8
        //  1441: astore_2       
        //  1442: aload           26
        //  1444: ldc_w           "query_id"
        //  1447: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  1450: ifeq            1460
        //  1453: bipush          9
        //  1455: istore          34
        //  1457: goto            1463
        //  1460: iconst_2       
        //  1461: istore          34
        //  1463: aload           12
        //  1465: ifnull          1513
        //  1468: aload           8
        //  1470: astore_2       
        //  1471: aload           12
        //  1473: invokevirtual   java/lang/String.length:()I
        //  1476: ifle            1513
        //  1479: aload           8
        //  1481: astore_2       
        //  1482: aload           12
        //  1484: ldc_w           "http"
        //  1487: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1490: ifeq            1513
        //  1493: aload           8
        //  1495: astore_2       
        //  1496: aload           8
        //  1498: aload           12
        //  1500: putfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  1503: aload           8
        //  1505: astore_2       
        //  1506: aload           25
        //  1508: astore          24
        //  1510: goto            1667
        //  1513: aload           8
        //  1515: astore_2       
        //  1516: aload           8
        //  1518: aload           24
        //  1520: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  1523: aload           24
        //  1525: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  1528: invokevirtual   java/util/ArrayList.size:()I
        //  1531: iconst_1       
        //  1532: isub           
        //  1533: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1536: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  1539: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  1542: iconst_1       
        //  1543: invokestatic    org/telegram/messenger/FileLoader.getPathToAttach:(Lorg/telegram/tgnet/TLObject;Z)Ljava/io/File;
        //  1546: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //  1549: putfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  1552: aload           8
        //  1554: astore_2       
        //  1555: aload           25
        //  1557: astore          24
        //  1559: goto            1667
        //  1562: aload           8
        //  1564: ifnull          1673
        //  1567: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  1570: dup            
        //  1571: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  1574: astore          14
        //  1576: aload           14
        //  1578: astore_2       
        //  1579: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaGame;
        //  1582: astore          22
        //  1584: aload           14
        //  1586: astore_2       
        //  1587: aload           22
        //  1589: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaGame.<init>:()V
        //  1592: aload           14
        //  1594: astore_2       
        //  1595: aload           14
        //  1597: aload           22
        //  1599: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1602: aload           14
        //  1604: astore_2       
        //  1605: aload           14
        //  1607: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1610: aload           8
        //  1612: putfield        org/telegram/tgnet/TLRPC$MessageMedia.game:Lorg/telegram/tgnet/TLRPC$TL_game;
        //  1615: aload           14
        //  1617: astore_2       
        //  1618: aload           25
        //  1620: astore          24
        //  1622: iload           30
        //  1624: istore          34
        //  1626: aload           26
        //  1628: ifnull          1667
        //  1631: aload           14
        //  1633: astore_2       
        //  1634: aload           26
        //  1636: ldc_w           "query_id"
        //  1639: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  1642: istore          38
        //  1644: aload           14
        //  1646: astore_2       
        //  1647: aload           25
        //  1649: astore          24
        //  1651: iload           30
        //  1653: istore          34
        //  1655: iload           38
        //  1657: ifeq            1667
        //  1660: aload           14
        //  1662: astore          8
        //  1664: goto            1250
        //  1667: aload_2        
        //  1668: astore          8
        //  1670: goto            2734
        //  1673: aload           6
        //  1675: ifnull          1971
        //  1678: aload           35
        //  1680: ifnull          1695
        //  1683: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //  1686: dup            
        //  1687: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //  1690: astore          8
        //  1692: goto            1704
        //  1695: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  1698: dup            
        //  1699: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  1702: astore          8
        //  1704: aload           8
        //  1706: astore_2       
        //  1707: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaContact;
        //  1710: astore          14
        //  1712: aload           8
        //  1714: astore_2       
        //  1715: aload           14
        //  1717: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaContact.<init>:()V
        //  1720: aload           8
        //  1722: astore_2       
        //  1723: aload           8
        //  1725: aload           14
        //  1727: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1730: aload           8
        //  1732: astore_2       
        //  1733: aload           8
        //  1735: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1738: aload           6
        //  1740: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  1743: putfield        org/telegram/tgnet/TLRPC$MessageMedia.phone_number:Ljava/lang/String;
        //  1746: aload           8
        //  1748: astore_2       
        //  1749: aload           8
        //  1751: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1754: aload           6
        //  1756: getfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //  1759: putfield        org/telegram/tgnet/TLRPC$MessageMedia.first_name:Ljava/lang/String;
        //  1762: aload           8
        //  1764: astore_2       
        //  1765: aload           8
        //  1767: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1770: aload           6
        //  1772: getfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //  1775: putfield        org/telegram/tgnet/TLRPC$MessageMedia.last_name:Ljava/lang/String;
        //  1778: aload           8
        //  1780: astore_2       
        //  1781: aload           8
        //  1783: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1786: aload           6
        //  1788: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //  1791: putfield        org/telegram/tgnet/TLRPC$MessageMedia.user_id:I
        //  1794: aload           8
        //  1796: astore_2       
        //  1797: aload           6
        //  1799: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  1802: ifnull          1841
        //  1805: aload           8
        //  1807: astore_2       
        //  1808: aload           6
        //  1810: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  1813: ldc_w           "BEGIN:VCARD"
        //  1816: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  1819: ifeq            1841
        //  1822: aload           8
        //  1824: astore_2       
        //  1825: aload           8
        //  1827: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1830: aload           6
        //  1832: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  1835: putfield        org/telegram/tgnet/TLRPC$MessageMedia.vcard:Ljava/lang/String;
        //  1838: goto            1855
        //  1841: aload           8
        //  1843: astore_2       
        //  1844: aload           8
        //  1846: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1849: ldc_w           ""
        //  1852: putfield        org/telegram/tgnet/TLRPC$MessageMedia.vcard:Ljava/lang/String;
        //  1855: ldc_w           ""
        //  1858: astore          14
        //  1860: aload           8
        //  1862: astore_2       
        //  1863: aload           8
        //  1865: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1868: getfield        org/telegram/tgnet/TLRPC$MessageMedia.first_name:Ljava/lang/String;
        //  1871: ifnonnull       1897
        //  1874: aload           8
        //  1876: astore_2       
        //  1877: aload           8
        //  1879: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1882: aload           14
        //  1884: putfield        org/telegram/tgnet/TLRPC$MessageMedia.first_name:Ljava/lang/String;
        //  1887: aload           8
        //  1889: astore_2       
        //  1890: aload           6
        //  1892: aload           14
        //  1894: putfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //  1897: aload           8
        //  1899: astore_2       
        //  1900: aload           8
        //  1902: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1905: getfield        org/telegram/tgnet/TLRPC$MessageMedia.last_name:Ljava/lang/String;
        //  1908: ifnonnull       1934
        //  1911: aload           8
        //  1913: astore_2       
        //  1914: aload           8
        //  1916: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  1919: aload           14
        //  1921: putfield        org/telegram/tgnet/TLRPC$MessageMedia.last_name:Ljava/lang/String;
        //  1924: aload           8
        //  1926: astore_2       
        //  1927: aload           6
        //  1929: aload           14
        //  1931: putfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //  1934: aload           26
        //  1936: ifnull          1960
        //  1939: aload           8
        //  1941: astore_2       
        //  1942: aload           26
        //  1944: ldc_w           "query_id"
        //  1947: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  1950: istore          38
        //  1952: iload           38
        //  1954: ifeq            1960
        //  1957: goto            1250
        //  1960: bipush          6
        //  1962: istore          34
        //  1964: aload           25
        //  1966: astore          24
        //  1968: goto            2734
        //  1971: aload           7
        //  1973: astore          8
        //  1975: aload           8
        //  1977: ifnull          2723
        //  1980: aload           35
        //  1982: ifnull          1997
        //  1985: new             Lorg/telegram/tgnet/TLRPC$TL_message_secret;
        //  1988: dup            
        //  1989: invokespecial   org/telegram/tgnet/TLRPC$TL_message_secret.<init>:()V
        //  1992: astore          14
        //  1994: goto            2006
        //  1997: new             Lorg/telegram/tgnet/TLRPC$TL_message;
        //  2000: dup            
        //  2001: invokespecial   org/telegram/tgnet/TLRPC$TL_message.<init>:()V
        //  2004: astore          14
        //  2006: aload           14
        //  2008: astore_2       
        //  2009: new             Lorg/telegram/tgnet/TLRPC$TL_messageMediaDocument;
        //  2012: astore          22
        //  2014: aload           14
        //  2016: astore_2       
        //  2017: aload           22
        //  2019: invokespecial   org/telegram/tgnet/TLRPC$TL_messageMediaDocument.<init>:()V
        //  2022: aload           14
        //  2024: astore_2       
        //  2025: aload           14
        //  2027: aload           22
        //  2029: putfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  2032: aload           14
        //  2034: astore_2       
        //  2035: aload           14
        //  2037: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  2040: astore          22
        //  2042: aload           14
        //  2044: astore_2       
        //  2045: aload           22
        //  2047: aload           22
        //  2049: getfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  2052: iconst_3       
        //  2053: ior            
        //  2054: putfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  2057: iload           20
        //  2059: ifeq            2110
        //  2062: aload           14
        //  2064: astore_2       
        //  2065: aload           14
        //  2067: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  2070: iload           20
        //  2072: putfield        org/telegram/tgnet/TLRPC$MessageMedia.ttl_seconds:I
        //  2075: aload           14
        //  2077: astore_2       
        //  2078: aload           14
        //  2080: iload           20
        //  2082: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  2085: aload           14
        //  2087: astore_2       
        //  2088: aload           14
        //  2090: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  2093: astore          22
        //  2095: aload           14
        //  2097: astore_2       
        //  2098: aload           22
        //  2100: aload           22
        //  2102: getfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  2105: iconst_4       
        //  2106: ior            
        //  2107: putfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  2110: aload           14
        //  2112: astore_2       
        //  2113: aload           14
        //  2115: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  2118: aload           8
        //  2120: putfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  2123: aload           26
        //  2125: ifnull          2149
        //  2128: aload           14
        //  2130: astore_2       
        //  2131: aload           26
        //  2133: ldc_w           "query_id"
        //  2136: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  2139: ifeq            2149
        //  2142: bipush          9
        //  2144: istore          30
        //  2146: goto            2207
        //  2149: aload           14
        //  2151: astore_2       
        //  2152: aload           7
        //  2154: invokestatic    org/telegram/messenger/MessageObject.isVideoDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  2157: ifne            2204
        //  2160: aload           14
        //  2162: astore_2       
        //  2163: aload           7
        //  2165: invokestatic    org/telegram/messenger/MessageObject.isRoundVideoDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  2168: ifne            2204
        //  2171: aload           5
        //  2173: ifnull          2179
        //  2176: goto            2204
        //  2179: aload           14
        //  2181: astore_2       
        //  2182: aload           7
        //  2184: invokestatic    org/telegram/messenger/MessageObject.isVoiceDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  2187: ifeq            2197
        //  2190: bipush          8
        //  2192: istore          30
        //  2194: goto            2207
        //  2197: bipush          7
        //  2199: istore          30
        //  2201: goto            2207
        //  2204: iconst_3       
        //  2205: istore          30
        //  2207: aload           26
        //  2209: astore          22
        //  2211: aload           5
        //  2213: ifnull          2265
        //  2216: aload           14
        //  2218: astore_2       
        //  2219: aload           5
        //  2221: invokevirtual   org/telegram/messenger/VideoEditedInfo.getString:()Ljava/lang/String;
        //  2224: astore          24
        //  2226: aload           26
        //  2228: astore          22
        //  2230: aload           26
        //  2232: ifnonnull       2251
        //  2235: aload           14
        //  2237: astore_2       
        //  2238: new             Ljava/util/HashMap;
        //  2241: astore          22
        //  2243: aload           14
        //  2245: astore_2       
        //  2246: aload           22
        //  2248: invokespecial   java/util/HashMap.<init>:()V
        //  2251: aload           14
        //  2253: astore_2       
        //  2254: aload           22
        //  2256: ldc_w           "ve"
        //  2259: aload           24
        //  2261: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2264: pop            
        //  2265: aload           35
        //  2267: ifnull          2311
        //  2270: aload           14
        //  2272: astore_2       
        //  2273: aload           8
        //  2275: getfield        org/telegram/tgnet/TLRPC$Document.dc_id:I
        //  2278: ifle            2311
        //  2281: aload           14
        //  2283: astore_2       
        //  2284: aload           7
        //  2286: invokestatic    org/telegram/messenger/MessageObject.isStickerDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  2289: ifne            2311
        //  2292: aload           14
        //  2294: astore_2       
        //  2295: aload           14
        //  2297: aload           7
        //  2299: invokestatic    org/telegram/messenger/FileLoader.getPathToAttach:(Lorg/telegram/tgnet/TLObject;)Ljava/io/File;
        //  2302: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //  2305: putfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  2308: goto            2321
        //  2311: aload           14
        //  2313: astore_2       
        //  2314: aload           14
        //  2316: aload           12
        //  2318: putfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  2321: aload           14
        //  2323: astore          8
        //  2325: aload           22
        //  2327: astore          26
        //  2329: aload           25
        //  2331: astore          24
        //  2333: iload           30
        //  2335: istore          34
        //  2337: aload           35
        //  2339: ifnull          2734
        //  2342: aload           14
        //  2344: astore_2       
        //  2345: aload           14
        //  2347: astore          8
        //  2349: aload           22
        //  2351: astore          26
        //  2353: aload           25
        //  2355: astore          24
        //  2357: iload           30
        //  2359: istore          34
        //  2361: aload           7
        //  2363: invokestatic    org/telegram/messenger/MessageObject.isStickerDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  2366: ifeq            2734
        //  2369: iconst_0       
        //  2370: istore          39
        //  2372: aload           7
        //  2374: astore          23
        //  2376: aload           14
        //  2378: astore_2       
        //  2379: aload           14
        //  2381: astore          8
        //  2383: aload           22
        //  2385: astore          26
        //  2387: aload           25
        //  2389: astore          24
        //  2391: iload           30
        //  2393: istore          34
        //  2395: iload           39
        //  2397: aload           23
        //  2399: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  2402: invokevirtual   java/util/ArrayList.size:()I
        //  2405: if_icmpge       2734
        //  2408: aload           14
        //  2410: astore_2       
        //  2411: aload           23
        //  2413: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  2416: iload           39
        //  2418: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  2421: checkcast       Lorg/telegram/tgnet/TLRPC$DocumentAttribute;
        //  2424: astore          8
        //  2426: aload           14
        //  2428: astore_2       
        //  2429: aload           8
        //  2431: instanceof      Lorg/telegram/tgnet/TLRPC$TL_documentAttributeSticker;
        //  2434: ifeq            2717
        //  2437: aload           14
        //  2439: astore_2       
        //  2440: aload           23
        //  2442: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  2445: iload           39
        //  2447: invokevirtual   java/util/ArrayList.remove:(I)Ljava/lang/Object;
        //  2450: pop            
        //  2451: aload           14
        //  2453: astore_2       
        //  2454: new             Lorg/telegram/tgnet/TLRPC$TL_documentAttributeSticker_layer55;
        //  2457: astore          26
        //  2459: aload           14
        //  2461: astore_2       
        //  2462: aload           26
        //  2464: invokespecial   org/telegram/tgnet/TLRPC$TL_documentAttributeSticker_layer55.<init>:()V
        //  2467: aload           14
        //  2469: astore_2       
        //  2470: aload           23
        //  2472: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  2475: aload           26
        //  2477: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  2480: pop            
        //  2481: aload           14
        //  2483: astore_2       
        //  2484: aload           26
        //  2486: aload           8
        //  2488: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.alt:Ljava/lang/String;
        //  2491: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.alt:Ljava/lang/String;
        //  2494: aload           14
        //  2496: astore_2       
        //  2497: aload           8
        //  2499: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2502: ifnull          2672
        //  2505: aload           14
        //  2507: astore_2       
        //  2508: aload           8
        //  2510: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2513: instanceof      Lorg/telegram/tgnet/TLRPC$TL_inputStickerSetShortName;
        //  2516: ifeq            2535
        //  2519: aload           14
        //  2521: astore_2       
        //  2522: aload           8
        //  2524: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2527: getfield        org/telegram/tgnet/TLRPC$InputStickerSet.short_name:Ljava/lang/String;
        //  2530: astore          8
        //  2532: goto            2558
        //  2535: aload           14
        //  2537: astore_2       
        //  2538: aload_0        
        //  2539: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  2542: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        //  2545: aload           8
        //  2547: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2550: getfield        org/telegram/tgnet/TLRPC$InputStickerSet.id:J
        //  2553: invokevirtual   org/telegram/messenger/DataQuery.getStickerSetName:(J)Ljava/lang/String;
        //  2556: astore          8
        //  2558: aload           14
        //  2560: astore_2       
        //  2561: aload           8
        //  2563: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  2566: ifne            2627
        //  2569: aload           14
        //  2571: astore_2       
        //  2572: new             Lorg/telegram/tgnet/TLRPC$TL_inputStickerSetShortName;
        //  2575: astore          24
        //  2577: aload           14
        //  2579: astore_2       
        //  2580: aload           24
        //  2582: invokespecial   org/telegram/tgnet/TLRPC$TL_inputStickerSetShortName.<init>:()V
        //  2585: aload           14
        //  2587: astore_2       
        //  2588: aload           26
        //  2590: aload           24
        //  2592: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2595: aload           14
        //  2597: astore_2       
        //  2598: aload           26
        //  2600: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2603: aload           8
        //  2605: putfield        org/telegram/tgnet/TLRPC$InputStickerSet.short_name:Ljava/lang/String;
        //  2608: aload           14
        //  2610: astore          8
        //  2612: aload           22
        //  2614: astore          26
        //  2616: aload           25
        //  2618: astore          24
        //  2620: iload           30
        //  2622: istore          34
        //  2624: goto            2734
        //  2627: aload           14
        //  2629: astore_2       
        //  2630: new             Lorg/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty;
        //  2633: astore          8
        //  2635: aload           14
        //  2637: astore_2       
        //  2638: aload           8
        //  2640: invokespecial   org/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty.<init>:()V
        //  2643: aload           14
        //  2645: astore_2       
        //  2646: aload           26
        //  2648: aload           8
        //  2650: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2653: aload           14
        //  2655: astore          8
        //  2657: aload           22
        //  2659: astore          26
        //  2661: aload           25
        //  2663: astore          24
        //  2665: iload           30
        //  2667: istore          34
        //  2669: goto            2734
        //  2672: aload           14
        //  2674: astore_2       
        //  2675: new             Lorg/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty;
        //  2678: astore          8
        //  2680: aload           14
        //  2682: astore_2       
        //  2683: aload           8
        //  2685: invokespecial   org/telegram/tgnet/TLRPC$TL_inputStickerSetEmpty.<init>:()V
        //  2688: aload           14
        //  2690: astore_2       
        //  2691: aload           26
        //  2693: aload           8
        //  2695: putfield        org/telegram/tgnet/TLRPC$DocumentAttribute.stickerset:Lorg/telegram/tgnet/TLRPC$InputStickerSet;
        //  2698: aload           14
        //  2700: astore          8
        //  2702: aload           22
        //  2704: astore          26
        //  2706: aload           25
        //  2708: astore          24
        //  2710: iload           30
        //  2712: istore          34
        //  2714: goto            2734
        //  2717: iinc            39, 1
        //  2720: goto            2372
        //  2723: aconst_null    
        //  2724: astore          8
        //  2726: iload           30
        //  2728: istore          34
        //  2730: aload           25
        //  2732: astore          24
        //  2734: ldc_w           ""
        //  2737: astore          14
        //  2739: aload           17
        //  2741: ifnull          2782
        //  2744: aload           8
        //  2746: astore_2       
        //  2747: aload           17
        //  2749: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  2752: ifne            2782
        //  2755: aload           8
        //  2757: astore_2       
        //  2758: aload           8
        //  2760: aload           17
        //  2762: putfield        org/telegram/tgnet/TLRPC$Message.entities:Ljava/util/ArrayList;
        //  2765: aload           8
        //  2767: astore_2       
        //  2768: aload           8
        //  2770: aload           8
        //  2772: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  2775: sipush          128
        //  2778: ior            
        //  2779: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  2782: aload           29
        //  2784: ifnull          2800
        //  2787: aload           8
        //  2789: astore_2       
        //  2790: aload           8
        //  2792: aload           29
        //  2794: putfield        org/telegram/tgnet/TLRPC$Message.message:Ljava/lang/String;
        //  2797: goto            2821
        //  2800: aload           8
        //  2802: astore_2       
        //  2803: aload           8
        //  2805: getfield        org/telegram/tgnet/TLRPC$Message.message:Ljava/lang/String;
        //  2808: ifnonnull       2821
        //  2811: aload           8
        //  2813: astore_2       
        //  2814: aload           8
        //  2816: aload           14
        //  2818: putfield        org/telegram/tgnet/TLRPC$Message.message:Ljava/lang/String;
        //  2821: aload           8
        //  2823: astore_2       
        //  2824: aload           8
        //  2826: getfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  2829: ifnonnull       2842
        //  2832: aload           8
        //  2834: astore_2       
        //  2835: aload           8
        //  2837: aload           14
        //  2839: putfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  2842: aload           8
        //  2844: astore_2       
        //  2845: aload_0        
        //  2846: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  2849: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  2852: invokevirtual   org/telegram/messenger/UserConfig.getNewMessageId:()I
        //  2855: istore          30
        //  2857: aload           8
        //  2859: astore_2       
        //  2860: aload           8
        //  2862: iload           30
        //  2864: putfield        org/telegram/tgnet/TLRPC$Message.id:I
        //  2867: aload           8
        //  2869: astore_2       
        //  2870: aload           8
        //  2872: iload           30
        //  2874: putfield        org/telegram/tgnet/TLRPC$Message.local_id:I
        //  2877: aload           8
        //  2879: astore_2       
        //  2880: aload           8
        //  2882: iconst_1       
        //  2883: putfield        org/telegram/tgnet/TLRPC$Message.out:Z
        //  2886: iload           36
        //  2888: ifeq            2913
        //  2891: aload           33
        //  2893: ifnull          2913
        //  2896: aload           8
        //  2898: astore_2       
        //  2899: aload           8
        //  2901: aload           33
        //  2903: getfield        org/telegram/tgnet/TLRPC$InputPeer.channel_id:I
        //  2906: ineg           
        //  2907: putfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //  2910: goto            2948
        //  2913: aload           8
        //  2915: astore_2       
        //  2916: aload           8
        //  2918: aload_0        
        //  2919: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  2922: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  2925: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //  2928: putfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //  2931: aload           8
        //  2933: astore_2       
        //  2934: aload           8
        //  2936: aload           8
        //  2938: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  2941: sipush          256
        //  2944: ior            
        //  2945: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  2948: aload           8
        //  2950: astore_2       
        //  2951: aload_0        
        //  2952: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  2955: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  2958: iconst_0       
        //  2959: invokevirtual   org/telegram/messenger/UserConfig.saveConfig:(Z)V
        //  2962: aload_1        
        //  2963: astore_2       
        //  2964: aload           24
        //  2966: astore          22
        //  2968: aload_3        
        //  2969: astore_1       
        //  2970: aload           4
        //  2972: astore_3       
        //  2973: aload           9
        //  2975: astore          14
        //  2977: aload_1        
        //  2978: astore          9
        //  2980: aload           8
        //  2982: astore_1       
        //  2983: aload_2        
        //  2984: astore          8
        //  2986: ldc_w           ""
        //  2989: astore          4
        //  2991: aload_1        
        //  2992: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  2995: lstore          40
        //  2997: lload           40
        //  2999: lconst_0       
        //  3000: lcmp           
        //  3001: ifne            3014
        //  3004: aload_1        
        //  3005: astore_2       
        //  3006: aload_1        
        //  3007: aload_0        
        //  3008: invokevirtual   org/telegram/messenger/SendMessagesHelper.getNextRandomId:()J
        //  3011: putfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  3014: aload           26
        //  3016: ifnull          3111
        //  3019: aload_1        
        //  3020: astore_2       
        //  3021: aload           26
        //  3023: ldc_w           "bot"
        //  3026: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  3029: ifeq            3111
        //  3032: aload           35
        //  3034: ifnull          3074
        //  3037: aload_1        
        //  3038: astore_2       
        //  3039: aload_1        
        //  3040: aload           26
        //  3042: ldc_w           "bot_name"
        //  3045: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3048: checkcast       Ljava/lang/String;
        //  3051: putfield        org/telegram/tgnet/TLRPC$Message.via_bot_name:Ljava/lang/String;
        //  3054: aload_1        
        //  3055: astore_2       
        //  3056: aload_1        
        //  3057: getfield        org/telegram/tgnet/TLRPC$Message.via_bot_name:Ljava/lang/String;
        //  3060: ifnonnull       3097
        //  3063: aload_1        
        //  3064: astore_2       
        //  3065: aload_1        
        //  3066: aload           4
        //  3068: putfield        org/telegram/tgnet/TLRPC$Message.via_bot_name:Ljava/lang/String;
        //  3071: goto            3097
        //  3074: aload_1        
        //  3075: astore_2       
        //  3076: aload_1        
        //  3077: aload           26
        //  3079: ldc_w           "bot"
        //  3082: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3085: checkcast       Ljava/lang/String;
        //  3088: invokestatic    org/telegram/messenger/Utilities.parseInt:(Ljava/lang/String;)Ljava/lang/Integer;
        //  3091: invokevirtual   java/lang/Integer.intValue:()I
        //  3094: putfield        org/telegram/tgnet/TLRPC$Message.via_bot_id:I
        //  3097: aload_1        
        //  3098: astore_2       
        //  3099: aload_1        
        //  3100: aload_1        
        //  3101: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3104: sipush          2048
        //  3107: ior            
        //  3108: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3111: aload_1        
        //  3112: aload           26
        //  3114: putfield        org/telegram/tgnet/TLRPC$Message.params:Ljava/util/HashMap;
        //  3117: aload           16
        //  3119: ifnull          3142
        //  3122: aload_1        
        //  3123: astore_2       
        //  3124: aload           16
        //  3126: getfield        org/telegram/messenger/MessageObject.resendAsIs:Z
        //  3129: istore          38
        //  3131: iload           38
        //  3133: ifne            3139
        //  3136: goto            3142
        //  3139: goto            3296
        //  3142: aload_1        
        //  3143: aload_0        
        //  3144: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3147: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
        //  3150: invokevirtual   org/telegram/tgnet/ConnectionsManager.getCurrentTime:()I
        //  3153: putfield        org/telegram/tgnet/TLRPC$Message.date:I
        //  3156: aload           33
        //  3158: instanceof      Lorg/telegram/tgnet/TLRPC$TL_inputPeerChannel;
        //  3161: istore          38
        //  3163: iload           38
        //  3165: ifeq            3291
        //  3168: iload           36
        //  3170: ifeq            3194
        //  3173: aload_1        
        //  3174: astore_2       
        //  3175: aload_1        
        //  3176: iconst_1       
        //  3177: putfield        org/telegram/tgnet/TLRPC$Message.views:I
        //  3180: aload_1        
        //  3181: astore_2       
        //  3182: aload_1        
        //  3183: aload_1        
        //  3184: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3187: sipush          1024
        //  3190: ior            
        //  3191: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3194: aload_1        
        //  3195: astore_2       
        //  3196: aload_0        
        //  3197: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3200: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  3203: aload           33
        //  3205: getfield        org/telegram/tgnet/TLRPC$InputPeer.channel_id:I
        //  3208: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3211: invokevirtual   org/telegram/messenger/MessagesController.getChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$Chat;
        //  3214: astore          25
        //  3216: aload           25
        //  3218: ifnull          3296
        //  3221: aload_1        
        //  3222: astore_2       
        //  3223: aload           25
        //  3225: getfield        org/telegram/tgnet/TLRPC$Chat.megagroup:Z
        //  3228: ifeq            3255
        //  3231: aload_1        
        //  3232: astore_2       
        //  3233: aload_1        
        //  3234: aload_1        
        //  3235: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3238: ldc_w           -2147483648
        //  3241: ior            
        //  3242: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3245: aload_1        
        //  3246: astore_2       
        //  3247: aload_1        
        //  3248: iconst_1       
        //  3249: putfield        org/telegram/tgnet/TLRPC$Message.unread:Z
        //  3252: goto            3296
        //  3255: aload_1        
        //  3256: astore_2       
        //  3257: aload_1        
        //  3258: iconst_1       
        //  3259: putfield        org/telegram/tgnet/TLRPC$Message.post:Z
        //  3262: aload_1        
        //  3263: astore_2       
        //  3264: aload           25
        //  3266: getfield        org/telegram/tgnet/TLRPC$Chat.signatures:Z
        //  3269: ifeq            3296
        //  3272: aload_1        
        //  3273: astore_2       
        //  3274: aload_1        
        //  3275: aload_0        
        //  3276: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3279: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  3282: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //  3285: putfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //  3288: goto            3296
        //  3291: aload_1        
        //  3292: iconst_1       
        //  3293: putfield        org/telegram/tgnet/TLRPC$Message.unread:Z
        //  3296: aload_1        
        //  3297: aload_1        
        //  3298: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3301: sipush          512
        //  3304: ior            
        //  3305: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3308: aload_1        
        //  3309: lload           10
        //  3311: putfield        org/telegram/tgnet/TLRPC$Message.dialog_id:J
        //  3314: aload           13
        //  3316: ifnull          3396
        //  3319: aload           35
        //  3321: ifnull          3369
        //  3324: aload_1        
        //  3325: astore_2       
        //  3326: aload           13
        //  3328: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  3331: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  3334: lconst_0       
        //  3335: lcmp           
        //  3336: ifeq            3369
        //  3339: aload_1        
        //  3340: astore_2       
        //  3341: aload_1        
        //  3342: aload           13
        //  3344: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  3347: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  3350: putfield        org/telegram/tgnet/TLRPC$Message.reply_to_random_id:J
        //  3353: aload_1        
        //  3354: astore_2       
        //  3355: aload_1        
        //  3356: aload_1        
        //  3357: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3360: bipush          8
        //  3362: ior            
        //  3363: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3366: goto            3382
        //  3369: aload_1        
        //  3370: astore_2       
        //  3371: aload_1        
        //  3372: aload_1        
        //  3373: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3376: bipush          8
        //  3378: ior            
        //  3379: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3382: aload_1        
        //  3383: astore_2       
        //  3384: aload_1        
        //  3385: aload           13
        //  3387: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //  3390: putfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        //  3393: goto            3396
        //  3396: aload           18
        //  3398: ifnull          3427
        //  3401: aload           35
        //  3403: ifnonnull       3427
        //  3406: aload_1        
        //  3407: astore_2       
        //  3408: aload_1        
        //  3409: aload_1        
        //  3410: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3413: bipush          64
        //  3415: ior            
        //  3416: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  3419: aload_1        
        //  3420: astore_2       
        //  3421: aload_1        
        //  3422: aload           18
        //  3424: putfield        org/telegram/tgnet/TLRPC$Message.reply_markup:Lorg/telegram/tgnet/TLRPC$ReplyMarkup;
        //  3427: iload           31
        //  3429: ifeq            3734
        //  3432: iload           32
        //  3434: iconst_1       
        //  3435: if_icmpne       3656
        //  3438: aload_1        
        //  3439: astore_2       
        //  3440: aload_0        
        //  3441: getfield        org/telegram/messenger/SendMessagesHelper.currentChatInfo:Lorg/telegram/tgnet/TLRPC$ChatFull;
        //  3444: ifnonnull       3500
        //  3447: aload_1        
        //  3448: astore_2       
        //  3449: aload_0        
        //  3450: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3453: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        //  3456: aload_1        
        //  3457: invokevirtual   org/telegram/messenger/MessagesStorage.markMessageAsSendError:(Lorg/telegram/tgnet/TLRPC$Message;)V
        //  3460: aload_1        
        //  3461: astore_2       
        //  3462: aload_0        
        //  3463: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3466: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  3469: getstatic       org/telegram/messenger/NotificationCenter.messageSendError:I
        //  3472: iconst_1       
        //  3473: anewarray       Ljava/lang/Object;
        //  3476: dup            
        //  3477: iconst_0       
        //  3478: aload_1        
        //  3479: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        //  3482: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3485: aastore        
        //  3486: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  3489: aload_1        
        //  3490: astore_2       
        //  3491: aload_0        
        //  3492: aload_1        
        //  3493: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        //  3496: invokevirtual   org/telegram/messenger/SendMessagesHelper.processSentMessage:(I)V
        //  3499: return         
        //  3500: aload_1        
        //  3501: astore_2       
        //  3502: new             Ljava/util/ArrayList;
        //  3505: astore          25
        //  3507: aload_1        
        //  3508: astore_2       
        //  3509: aload           25
        //  3511: invokespecial   java/util/ArrayList.<init>:()V
        //  3514: aload_1        
        //  3515: astore_2       
        //  3516: aload_0        
        //  3517: getfield        org/telegram/messenger/SendMessagesHelper.currentChatInfo:Lorg/telegram/tgnet/TLRPC$ChatFull;
        //  3520: getfield        org/telegram/tgnet/TLRPC$ChatFull.participants:Lorg/telegram/tgnet/TLRPC$ChatParticipants;
        //  3523: getfield        org/telegram/tgnet/TLRPC$ChatParticipants.participants:Ljava/util/ArrayList;
        //  3526: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //  3529: astore          18
        //  3531: aload_1        
        //  3532: astore_2       
        //  3533: aload           18
        //  3535: invokeinterface java/util/Iterator.hasNext:()Z
        //  3540: ifeq            3613
        //  3543: aload_1        
        //  3544: astore_2       
        //  3545: aload           18
        //  3547: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  3552: checkcast       Lorg/telegram/tgnet/TLRPC$ChatParticipant;
        //  3555: astore          24
        //  3557: aload_1        
        //  3558: astore_2       
        //  3559: aload_0        
        //  3560: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3563: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  3566: aload           24
        //  3568: getfield        org/telegram/tgnet/TLRPC$ChatParticipant.user_id:I
        //  3571: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3574: invokevirtual   org/telegram/messenger/MessagesController.getUser:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$User;
        //  3577: astore          24
        //  3579: aload_1        
        //  3580: astore_2       
        //  3581: aload_0        
        //  3582: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3585: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  3588: aload           24
        //  3590: invokevirtual   org/telegram/messenger/MessagesController.getInputUser:(Lorg/telegram/tgnet/TLRPC$User;)Lorg/telegram/tgnet/TLRPC$InputUser;
        //  3593: astore          24
        //  3595: aload           24
        //  3597: ifnull          3610
        //  3600: aload_1        
        //  3601: astore_2       
        //  3602: aload           25
        //  3604: aload           24
        //  3606: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  3609: pop            
        //  3610: goto            3531
        //  3613: aload_1        
        //  3614: astore_2       
        //  3615: new             Lorg/telegram/tgnet/TLRPC$TL_peerChat;
        //  3618: astore          18
        //  3620: aload_1        
        //  3621: astore_2       
        //  3622: aload           18
        //  3624: invokespecial   org/telegram/tgnet/TLRPC$TL_peerChat.<init>:()V
        //  3627: aload_1        
        //  3628: astore_2       
        //  3629: aload_1        
        //  3630: aload           18
        //  3632: putfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3635: aload_1        
        //  3636: astore_2       
        //  3637: aload_1        
        //  3638: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3641: iload           31
        //  3643: putfield        org/telegram/tgnet/TLRPC$Peer.chat_id:I
        //  3646: iload           20
        //  3648: istore          30
        //  3650: aload_3        
        //  3651: astore          18
        //  3653: goto            4146
        //  3656: aload_1        
        //  3657: astore_2       
        //  3658: aload_1        
        //  3659: aload_0        
        //  3660: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3663: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  3666: iload           31
        //  3668: invokevirtual   org/telegram/messenger/MessagesController.getPeer:(I)Lorg/telegram/tgnet/TLRPC$Peer;
        //  3671: putfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3674: iload           31
        //  3676: ifle            3731
        //  3679: aload_1        
        //  3680: astore_2       
        //  3681: aload_0        
        //  3682: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3685: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  3688: iload           31
        //  3690: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3693: invokevirtual   org/telegram/messenger/MessagesController.getUser:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$User;
        //  3696: astore          18
        //  3698: aload           18
        //  3700: ifnonnull       3714
        //  3703: aload_1        
        //  3704: astore_2       
        //  3705: aload_0        
        //  3706: aload_1        
        //  3707: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        //  3710: invokevirtual   org/telegram/messenger/SendMessagesHelper.processSentMessage:(I)V
        //  3713: return         
        //  3714: aload_1        
        //  3715: astore_2       
        //  3716: aload           18
        //  3718: getfield        org/telegram/tgnet/TLRPC$User.bot:Z
        //  3721: ifeq            3731
        //  3724: aload_1        
        //  3725: astore_2       
        //  3726: aload_1        
        //  3727: iconst_0       
        //  3728: putfield        org/telegram/tgnet/TLRPC$Message.unread:Z
        //  3731: goto            4136
        //  3734: new             Lorg/telegram/tgnet/TLRPC$TL_peerUser;
        //  3737: astore_2       
        //  3738: aload_2        
        //  3739: invokespecial   org/telegram/tgnet/TLRPC$TL_peerUser.<init>:()V
        //  3742: aload_1        
        //  3743: aload_2        
        //  3744: putfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3747: aload           35
        //  3749: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.participant_id:I
        //  3752: istore          36
        //  3754: aload_0        
        //  3755: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  3758: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  3761: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //  3764: istore          30
        //  3766: iload           36
        //  3768: iload           30
        //  3770: if_icmpne       3790
        //  3773: aload_1        
        //  3774: astore_2       
        //  3775: aload_1        
        //  3776: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3779: aload           35
        //  3781: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.admin_id:I
        //  3784: putfield        org/telegram/tgnet/TLRPC$Peer.user_id:I
        //  3787: goto            3802
        //  3790: aload_1        
        //  3791: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  3794: aload           35
        //  3796: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.participant_id:I
        //  3799: putfield        org/telegram/tgnet/TLRPC$Peer.user_id:I
        //  3802: iload           20
        //  3804: ifeq            3818
        //  3807: aload_1        
        //  3808: astore_2       
        //  3809: aload_1        
        //  3810: iload           20
        //  3812: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3815: goto            3882
        //  3818: aload_1        
        //  3819: aload           35
        //  3821: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.ttl:I
        //  3824: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3827: aload_1        
        //  3828: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3831: istore          30
        //  3833: iload           30
        //  3835: ifeq            3882
        //  3838: aload_1        
        //  3839: astore_2       
        //  3840: aload_1        
        //  3841: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3844: ifnull          3882
        //  3847: aload_1        
        //  3848: astore_2       
        //  3849: aload_1        
        //  3850: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3853: aload_1        
        //  3854: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3857: putfield        org/telegram/tgnet/TLRPC$MessageMedia.ttl_seconds:I
        //  3860: aload_1        
        //  3861: astore_2       
        //  3862: aload_1        
        //  3863: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3866: astore          18
        //  3868: aload_1        
        //  3869: astore_2       
        //  3870: aload           18
        //  3872: aload           18
        //  3874: getfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  3877: iconst_4       
        //  3878: ior            
        //  3879: putfield        org/telegram/tgnet/TLRPC$MessageMedia.flags:I
        //  3882: aload_1        
        //  3883: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3886: istore          30
        //  3888: iload           30
        //  3890: ifeq            4136
        //  3893: aload_1        
        //  3894: astore_2       
        //  3895: aload_1        
        //  3896: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3899: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  3902: ifnull          4136
        //  3905: aload_1        
        //  3906: astore_2       
        //  3907: aload_1        
        //  3908: invokestatic    org/telegram/messenger/MessageObject.isVoiceMessage:(Lorg/telegram/tgnet/TLRPC$Message;)Z
        //  3911: ifeq            4010
        //  3914: iconst_0       
        //  3915: istore          30
        //  3917: aload_1        
        //  3918: astore_2       
        //  3919: iload           30
        //  3921: aload_1        
        //  3922: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3925: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  3928: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  3931: invokevirtual   java/util/ArrayList.size:()I
        //  3934: if_icmpge       3987
        //  3937: aload_1        
        //  3938: astore_2       
        //  3939: aload_1        
        //  3940: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  3943: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  3946: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  3949: iload           30
        //  3951: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  3954: checkcast       Lorg/telegram/tgnet/TLRPC$DocumentAttribute;
        //  3957: astore          18
        //  3959: aload_1        
        //  3960: astore_2       
        //  3961: aload           18
        //  3963: instanceof      Lorg/telegram/tgnet/TLRPC$TL_documentAttributeAudio;
        //  3966: ifeq            3981
        //  3969: aload_1        
        //  3970: astore_2       
        //  3971: aload           18
        //  3973: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.duration:I
        //  3976: istore          30
        //  3978: goto            3990
        //  3981: iinc            30, 1
        //  3984: goto            3917
        //  3987: iconst_0       
        //  3988: istore          30
        //  3990: aload_1        
        //  3991: astore_2       
        //  3992: aload_1        
        //  3993: aload_1        
        //  3994: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  3997: iload           30
        //  3999: iconst_1       
        //  4000: iadd           
        //  4001: invokestatic    java/lang/Math.max:(II)I
        //  4004: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  4007: goto            4136
        //  4010: iload           20
        //  4012: istore          30
        //  4014: aload_1        
        //  4015: astore_2       
        //  4016: aload_1        
        //  4017: invokestatic    org/telegram/messenger/MessageObject.isVideoMessage:(Lorg/telegram/tgnet/TLRPC$Message;)Z
        //  4020: ifne            4036
        //  4023: aload_1        
        //  4024: astore_2       
        //  4025: iload           30
        //  4027: istore          20
        //  4029: aload_1        
        //  4030: invokestatic    org/telegram/messenger/MessageObject.isRoundVideoMessage:(Lorg/telegram/tgnet/TLRPC$Message;)Z
        //  4033: ifeq            4136
        //  4036: iconst_0       
        //  4037: istore          20
        //  4039: aload_1        
        //  4040: astore_2       
        //  4041: iload           20
        //  4043: aload_1        
        //  4044: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  4047: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  4050: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  4053: invokevirtual   java/util/ArrayList.size:()I
        //  4056: if_icmpge       4109
        //  4059: aload_1        
        //  4060: astore_2       
        //  4061: aload_1        
        //  4062: getfield        org/telegram/tgnet/TLRPC$Message.media:Lorg/telegram/tgnet/TLRPC$MessageMedia;
        //  4065: getfield        org/telegram/tgnet/TLRPC$MessageMedia.document:Lorg/telegram/tgnet/TLRPC$Document;
        //  4068: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  4071: iload           20
        //  4073: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  4076: checkcast       Lorg/telegram/tgnet/TLRPC$DocumentAttribute;
        //  4079: astore          18
        //  4081: aload_1        
        //  4082: astore_2       
        //  4083: aload           18
        //  4085: instanceof      Lorg/telegram/tgnet/TLRPC$TL_documentAttributeVideo;
        //  4088: ifeq            4103
        //  4091: aload_1        
        //  4092: astore_2       
        //  4093: aload           18
        //  4095: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.duration:I
        //  4098: istore          20
        //  4100: goto            4112
        //  4103: iinc            20, 1
        //  4106: goto            4039
        //  4109: iconst_0       
        //  4110: istore          20
        //  4112: aload_1        
        //  4113: astore_2       
        //  4114: aload_1        
        //  4115: aload_1        
        //  4116: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  4119: iload           20
        //  4121: iconst_1       
        //  4122: iadd           
        //  4123: invokestatic    java/lang/Math.max:(II)I
        //  4126: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  4129: iload           30
        //  4131: istore          20
        //  4133: goto            4136
        //  4136: aconst_null    
        //  4137: astore          25
        //  4139: aload_3        
        //  4140: astore          18
        //  4142: iload           20
        //  4144: istore          30
        //  4146: aload           6
        //  4148: astore          24
        //  4150: iload           32
        //  4152: iconst_1       
        //  4153: if_icmpeq       4184
        //  4156: aload_1        
        //  4157: astore_2       
        //  4158: aload_1        
        //  4159: invokestatic    org/telegram/messenger/MessageObject.isVoiceMessage:(Lorg/telegram/tgnet/TLRPC$Message;)Z
        //  4162: ifne            4174
        //  4165: aload_1        
        //  4166: astore_2       
        //  4167: aload_1        
        //  4168: invokestatic    org/telegram/messenger/MessageObject.isRoundVideoMessage:(Lorg/telegram/tgnet/TLRPC$Message;)Z
        //  4171: ifeq            4184
        //  4174: aload_1        
        //  4175: astore_2       
        //  4176: aload_1        
        //  4177: iconst_1       
        //  4178: putfield        org/telegram/tgnet/TLRPC$Message.media_unread:Z
        //  4181: goto            4184
        //  4184: aload_1        
        //  4185: iconst_1       
        //  4186: putfield        org/telegram/tgnet/TLRPC$Message.send_state:I
        //  4189: new             Lorg/telegram/messenger/MessageObject;
        //  4192: dup            
        //  4193: aload_0        
        //  4194: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  4197: aload_1        
        //  4198: iconst_1       
        //  4199: invokespecial   org/telegram/messenger/MessageObject.<init>:(ILorg/telegram/tgnet/TLRPC$Message;Z)V
        //  4202: astore          6
        //  4204: aload           6
        //  4206: aload           13
        //  4208: putfield        org/telegram/messenger/MessageObject.replyMessageObject:Lorg/telegram/messenger/MessageObject;
        //  4211: aload           6
        //  4213: invokevirtual   org/telegram/messenger/MessageObject.isForwarded:()Z
        //  4216: istore          38
        //  4218: iload           38
        //  4220: ifne            4274
        //  4223: aload           6
        //  4225: getfield        org/telegram/messenger/MessageObject.type:I
        //  4228: iconst_3       
        //  4229: if_icmpeq       4246
        //  4232: aload           5
        //  4234: ifnonnull       4246
        //  4237: aload           6
        //  4239: getfield        org/telegram/messenger/MessageObject.type:I
        //  4242: iconst_2       
        //  4243: if_icmpne       4274
        //  4246: aload_1        
        //  4247: getfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //  4250: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  4253: ifne            4274
        //  4256: aload           6
        //  4258: iconst_1       
        //  4259: putfield        org/telegram/messenger/MessageObject.attachPathExists:Z
        //  4262: goto            4274
        //  4265: astore_2       
        //  4266: aload_1        
        //  4267: astore_3       
        //  4268: aload           6
        //  4270: astore_1       
        //  4271: goto            12148
        //  4274: aload           6
        //  4276: getfield        org/telegram/messenger/MessageObject.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  4279: astore_2       
        //  4280: aload_2        
        //  4281: ifnull          4299
        //  4284: aload           5
        //  4286: ifnonnull       4299
        //  4289: aload           6
        //  4291: getfield        org/telegram/messenger/MessageObject.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  4294: astore          5
        //  4296: goto            4299
        //  4299: aload           26
        //  4301: ifnull          4378
        //  4304: aload           26
        //  4306: ldc_w           "groupId"
        //  4309: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4312: checkcast       Ljava/lang/String;
        //  4315: astore_2       
        //  4316: aload_2        
        //  4317: ifnull          4350
        //  4320: aload_2        
        //  4321: invokestatic    org/telegram/messenger/Utilities.parseLong:(Ljava/lang/String;)Ljava/lang/Long;
        //  4324: invokevirtual   java/lang/Long.longValue:()J
        //  4327: lstore          40
        //  4329: aload_1        
        //  4330: lload           40
        //  4332: putfield        org/telegram/tgnet/TLRPC$Message.grouped_id:J
        //  4335: aload_1        
        //  4336: aload_1        
        //  4337: getfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  4340: ldc_w           131072
        //  4343: ior            
        //  4344: putfield        org/telegram/tgnet/TLRPC$Message.flags:I
        //  4347: goto            4353
        //  4350: lconst_0       
        //  4351: lstore          40
        //  4353: aload           26
        //  4355: ldc_w           "final"
        //  4358: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4361: astore_2       
        //  4362: aload_2        
        //  4363: ifnull          4372
        //  4366: iconst_1       
        //  4367: istore          20
        //  4369: goto            4375
        //  4372: iconst_0       
        //  4373: istore          20
        //  4375: goto            4384
        //  4378: lconst_0       
        //  4379: lstore          40
        //  4381: iconst_0       
        //  4382: istore          20
        //  4384: lload           40
        //  4386: lconst_0       
        //  4387: lcmp           
        //  4388: ifne            4487
        //  4391: new             Ljava/util/ArrayList;
        //  4394: astore_3       
        //  4395: aload_3        
        //  4396: invokespecial   java/util/ArrayList.<init>:()V
        //  4399: aload_3        
        //  4400: aload           6
        //  4402: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  4405: pop            
        //  4406: new             Ljava/util/ArrayList;
        //  4409: astore_2       
        //  4410: aload_2        
        //  4411: invokespecial   java/util/ArrayList.<init>:()V
        //  4414: aload_2        
        //  4415: aload_1        
        //  4416: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  4419: pop            
        //  4420: aload_0        
        //  4421: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  4424: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        //  4427: aload_2        
        //  4428: iconst_0       
        //  4429: iconst_1       
        //  4430: iconst_0       
        //  4431: iconst_0       
        //  4432: invokevirtual   org/telegram/messenger/MessagesStorage.putMessages:(Ljava/util/ArrayList;ZZZI)V
        //  4435: aload_0        
        //  4436: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  4439: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //  4442: lload           10
        //  4444: aload_3        
        //  4445: invokevirtual   org/telegram/messenger/MessagesController.updateInterfaceWithMessages:(JLjava/util/ArrayList;)V
        //  4448: aload_0        
        //  4449: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  4452: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        //  4455: getstatic       org/telegram/messenger/NotificationCenter.dialogsNeedReload:I
        //  4458: iconst_0       
        //  4459: anewarray       Ljava/lang/Object;
        //  4462: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        //  4465: aconst_null    
        //  4466: astore_3       
        //  4467: goto            4611
        //  4470: astore_2       
        //  4471: goto            4475
        //  4474: astore_2       
        //  4475: aload           6
        //  4477: astore          4
        //  4479: aload_1        
        //  4480: astore_3       
        //  4481: aload           4
        //  4483: astore_1       
        //  4484: goto            12148
        //  4487: new             Ljava/lang/StringBuilder;
        //  4490: astore_2       
        //  4491: aload_2        
        //  4492: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4495: aload_2        
        //  4496: ldc_w           "group_"
        //  4499: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4502: pop            
        //  4503: aload_2        
        //  4504: lload           40
        //  4506: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4509: pop            
        //  4510: aload_2        
        //  4511: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4514: astore_2       
        //  4515: aload_0        
        //  4516: getfield        org/telegram/messenger/SendMessagesHelper.delayedMessages:Ljava/util/HashMap;
        //  4519: aload_2        
        //  4520: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4523: checkcast       Ljava/util/ArrayList;
        //  4526: astore_2       
        //  4527: aload_2        
        //  4528: ifnull          4543
        //  4531: aload_2        
        //  4532: iconst_0       
        //  4533: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  4536: checkcast       Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  4539: astore_3       
        //  4540: goto            4545
        //  4543: aconst_null    
        //  4544: astore_3       
        //  4545: aload_3        
        //  4546: astore_2       
        //  4547: aload_3        
        //  4548: ifnonnull       4574
        //  4551: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  4554: astore_2       
        //  4555: aload_2        
        //  4556: aload_0        
        //  4557: lload           10
        //  4559: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  4562: aload_2        
        //  4563: lload           40
        //  4565: invokevirtual   org/telegram/messenger/SendMessagesHelper$DelayedMessage.initForGroup:(J)V
        //  4568: aload_2        
        //  4569: aload           35
        //  4571: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.encryptedChat:Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //  4574: aload_2        
        //  4575: iconst_0       
        //  4576: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  4579: aload_2        
        //  4580: aconst_null    
        //  4581: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  4584: aload_2        
        //  4585: aconst_null    
        //  4586: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  4589: aload_2        
        //  4590: aconst_null    
        //  4591: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocation:Ljava/lang/String;
        //  4594: aload_2        
        //  4595: astore_3       
        //  4596: iload           20
        //  4598: ifeq            4611
        //  4601: aload_2        
        //  4602: aload_1        
        //  4603: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        //  4606: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.finalGroupMessage:I
        //  4609: aload_2        
        //  4610: astore_3       
        //  4611: aload           6
        //  4613: astore_2       
        //  4614: aload           5
        //  4616: astore          13
        //  4618: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  4621: istore          38
        //  4623: iload           38
        //  4625: ifeq            4734
        //  4628: aload           33
        //  4630: ifnull          4734
        //  4633: new             Ljava/lang/StringBuilder;
        //  4636: astore          5
        //  4638: aload           5
        //  4640: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4643: aload           5
        //  4645: ldc_w           "send message user_id = "
        //  4648: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4651: pop            
        //  4652: aload           5
        //  4654: aload           33
        //  4656: getfield        org/telegram/tgnet/TLRPC$InputPeer.user_id:I
        //  4659: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  4662: pop            
        //  4663: aload           5
        //  4665: ldc_w           " chat_id = "
        //  4668: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4671: pop            
        //  4672: aload           5
        //  4674: aload           33
        //  4676: getfield        org/telegram/tgnet/TLRPC$InputPeer.chat_id:I
        //  4679: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  4682: pop            
        //  4683: aload           5
        //  4685: ldc_w           " channel_id = "
        //  4688: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4691: pop            
        //  4692: aload           5
        //  4694: aload           33
        //  4696: getfield        org/telegram/tgnet/TLRPC$InputPeer.channel_id:I
        //  4699: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  4702: pop            
        //  4703: aload           5
        //  4705: ldc_w           " access_hash = "
        //  4708: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4711: pop            
        //  4712: aload           5
        //  4714: aload           33
        //  4716: getfield        org/telegram/tgnet/TLRPC$InputPeer.access_hash:J
        //  4719: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4722: pop            
        //  4723: aload           5
        //  4725: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4728: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  4731: goto            4734
        //  4734: iload           34
        //  4736: ifeq            11339
        //  4739: iload           34
        //  4741: bipush          9
        //  4743: if_icmpne       4759
        //  4746: aload           8
        //  4748: ifnull          4759
        //  4751: aload           35
        //  4753: ifnull          4759
        //  4756: goto            11339
        //  4759: iload           34
        //  4761: iconst_1       
        //  4762: if_icmplt       4771
        //  4765: iload           34
        //  4767: iconst_3       
        //  4768: if_icmple       4803
        //  4771: iload           34
        //  4773: iconst_5       
        //  4774: if_icmplt       4784
        //  4777: iload           34
        //  4779: bipush          8
        //  4781: if_icmple       4803
        //  4784: iload           34
        //  4786: bipush          9
        //  4788: if_icmpne       4796
        //  4791: aload           35
        //  4793: ifnonnull       4803
        //  4796: iload           34
        //  4798: bipush          10
        //  4800: if_icmpne       10669
        //  4803: aload           35
        //  4805: ifnonnull       7919
        //  4808: iload           34
        //  4810: iconst_1       
        //  4811: if_icmpne       5003
        //  4814: aload           9
        //  4816: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaVenue;
        //  4819: ifeq            4882
        //  4822: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaVenue;
        //  4825: astore          5
        //  4827: aload           5
        //  4829: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaVenue.<init>:()V
        //  4832: aload           5
        //  4834: aload           9
        //  4836: getfield        org/telegram/tgnet/TLRPC$MessageMedia.address:Ljava/lang/String;
        //  4839: putfield        org/telegram/tgnet/TLRPC$InputMedia.address:Ljava/lang/String;
        //  4842: aload           5
        //  4844: aload           9
        //  4846: getfield        org/telegram/tgnet/TLRPC$MessageMedia.title:Ljava/lang/String;
        //  4849: putfield        org/telegram/tgnet/TLRPC$InputMedia.title:Ljava/lang/String;
        //  4852: aload           5
        //  4854: aload           9
        //  4856: getfield        org/telegram/tgnet/TLRPC$MessageMedia.provider:Ljava/lang/String;
        //  4859: putfield        org/telegram/tgnet/TLRPC$InputMedia.provider:Ljava/lang/String;
        //  4862: aload           5
        //  4864: aload           9
        //  4866: getfield        org/telegram/tgnet/TLRPC$MessageMedia.venue_id:Ljava/lang/String;
        //  4869: putfield        org/telegram/tgnet/TLRPC$InputMedia.venue_id:Ljava/lang/String;
        //  4872: aload           5
        //  4874: aload           4
        //  4876: putfield        org/telegram/tgnet/TLRPC$InputMedia.venue_type:Ljava/lang/String;
        //  4879: goto            4934
        //  4882: aload           9
        //  4884: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaGeoLive;
        //  4887: ifeq            4925
        //  4890: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaGeoLive;
        //  4893: astore          5
        //  4895: aload           5
        //  4897: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaGeoLive.<init>:()V
        //  4900: aload           5
        //  4902: aload           9
        //  4904: getfield        org/telegram/tgnet/TLRPC$MessageMedia.period:I
        //  4907: putfield        org/telegram/tgnet/TLRPC$InputMedia.period:I
        //  4910: aload           5
        //  4912: aload           5
        //  4914: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  4917: iconst_2       
        //  4918: ior            
        //  4919: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  4922: goto            4934
        //  4925: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaGeoPoint;
        //  4928: dup            
        //  4929: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaGeoPoint.<init>:()V
        //  4932: astore          5
        //  4934: new             Lorg/telegram/tgnet/TLRPC$TL_inputGeoPoint;
        //  4937: astore          7
        //  4939: aload           7
        //  4941: invokespecial   org/telegram/tgnet/TLRPC$TL_inputGeoPoint.<init>:()V
        //  4944: aload           5
        //  4946: aload           7
        //  4948: putfield        org/telegram/tgnet/TLRPC$InputMedia.geo_point:Lorg/telegram/tgnet/TLRPC$InputGeoPoint;
        //  4951: aload           5
        //  4953: getfield        org/telegram/tgnet/TLRPC$InputMedia.geo_point:Lorg/telegram/tgnet/TLRPC$InputGeoPoint;
        //  4956: aload           9
        //  4958: getfield        org/telegram/tgnet/TLRPC$MessageMedia.geo:Lorg/telegram/tgnet/TLRPC$GeoPoint;
        //  4961: getfield        org/telegram/tgnet/TLRPC$GeoPoint.lat:D
        //  4964: putfield        org/telegram/tgnet/TLRPC$InputGeoPoint.lat:D
        //  4967: aload           5
        //  4969: getfield        org/telegram/tgnet/TLRPC$InputMedia.geo_point:Lorg/telegram/tgnet/TLRPC$InputGeoPoint;
        //  4972: aload           9
        //  4974: getfield        org/telegram/tgnet/TLRPC$MessageMedia.geo:Lorg/telegram/tgnet/TLRPC$GeoPoint;
        //  4977: getfield        org/telegram/tgnet/TLRPC$GeoPoint._long:D
        //  4980: putfield        org/telegram/tgnet/TLRPC$InputGeoPoint._long:D
        //  4983: aload           4
        //  4985: astore          7
        //  4987: iconst_0       
        //  4988: istore          15
        //  4990: aload           5
        //  4992: astore          4
        //  4994: aload_3        
        //  4995: astore          6
        //  4997: aload           7
        //  4999: astore_3       
        //  5000: goto            6827
        //  5003: iload           34
        //  5005: iconst_2       
        //  5006: if_icmpeq       6305
        //  5009: iload           34
        //  5011: bipush          9
        //  5013: if_icmpne       5024
        //  5016: aload           18
        //  5018: ifnull          5024
        //  5021: goto            6305
        //  5024: iload           34
        //  5026: iconst_3       
        //  5027: if_icmpne       5355
        //  5030: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument;
        //  5033: astore          8
        //  5035: aload           8
        //  5037: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument.<init>:()V
        //  5040: aload           8
        //  5042: aload           7
        //  5044: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  5047: putfield        org/telegram/tgnet/TLRPC$InputMedia.mime_type:Ljava/lang/String;
        //  5050: aload           8
        //  5052: aload           7
        //  5054: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  5057: putfield        org/telegram/tgnet/TLRPC$InputMedia.attributes:Ljava/util/ArrayList;
        //  5060: aload           7
        //  5062: invokestatic    org/telegram/messenger/MessageObject.isRoundVideoDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  5065: ifne            5117
        //  5068: aload           13
        //  5070: ifnull          5096
        //  5073: aload           13
        //  5075: astore          5
        //  5077: aload           5
        //  5079: getfield        org/telegram/messenger/VideoEditedInfo.muted:Z
        //  5082: ifne            5117
        //  5085: aload           5
        //  5087: getfield        org/telegram/messenger/VideoEditedInfo.roundVideo:Z
        //  5090: ifne            5117
        //  5093: goto            5096
        //  5096: aload           8
        //  5098: iconst_1       
        //  5099: putfield        org/telegram/tgnet/TLRPC$InputMedia.nosound_video:Z
        //  5102: getstatic       org/telegram/messenger/BuildVars.DEBUG_VERSION:Z
        //  5105: ifeq            5117
        //  5108: ldc_w           "nosound_video = true"
        //  5111: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  5114: goto            5117
        //  5117: iload           30
        //  5119: ifeq            5147
        //  5122: aload           8
        //  5124: iload           30
        //  5126: putfield        org/telegram/tgnet/TLRPC$InputMedia.ttl_seconds:I
        //  5129: aload_1        
        //  5130: iload           30
        //  5132: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  5135: aload           8
        //  5137: aload           8
        //  5139: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5142: iconst_2       
        //  5143: ior            
        //  5144: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5147: aload           7
        //  5149: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  5152: lconst_0       
        //  5153: lcmp           
        //  5154: ifne            5167
        //  5157: aload           8
        //  5159: astore          5
        //  5161: iconst_1       
        //  5162: istore          15
        //  5164: goto            5258
        //  5167: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaDocument;
        //  5170: astore          5
        //  5172: aload           5
        //  5174: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaDocument.<init>:()V
        //  5177: new             Lorg/telegram/tgnet/TLRPC$TL_inputDocument;
        //  5180: astore          9
        //  5182: aload           9
        //  5184: invokespecial   org/telegram/tgnet/TLRPC$TL_inputDocument.<init>:()V
        //  5187: aload           5
        //  5189: aload           9
        //  5191: putfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5194: aload           5
        //  5196: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5199: aload           7
        //  5201: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  5204: putfield        org/telegram/tgnet/TLRPC$InputDocument.id:J
        //  5207: aload           5
        //  5209: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5212: aload           7
        //  5214: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  5217: putfield        org/telegram/tgnet/TLRPC$InputDocument.access_hash:J
        //  5220: aload           5
        //  5222: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5225: aload           7
        //  5227: getfield        org/telegram/tgnet/TLRPC$Document.file_reference:[B
        //  5230: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5233: aload           5
        //  5235: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5238: getfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5241: ifnonnull       5255
        //  5244: aload           5
        //  5246: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5249: iconst_0       
        //  5250: newarray        B
        //  5252: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5255: iconst_0       
        //  5256: istore          15
        //  5258: aload_3        
        //  5259: ifnonnull       5298
        //  5262: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  5265: astore_3       
        //  5266: aload_3        
        //  5267: aload_0        
        //  5268: lload           10
        //  5270: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  5273: aload_3        
        //  5274: iconst_1       
        //  5275: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  5278: aload_3        
        //  5279: aload_2        
        //  5280: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  5283: aload_3        
        //  5284: aload           19
        //  5286: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  5289: aload_3        
        //  5290: aload           21
        //  5292: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  5295: goto            5298
        //  5298: aload_3        
        //  5299: aload           8
        //  5301: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputUploadMedia:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  5304: aload_3        
        //  5305: iload           15
        //  5307: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  5310: aload           7
        //  5312: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  5315: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  5318: ifne            5346
        //  5321: aload_3        
        //  5322: aload           7
        //  5324: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  5327: iconst_0       
        //  5328: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  5331: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  5334: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  5337: aload_3        
        //  5338: aload           7
        //  5340: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.locationParent:Lorg/telegram/tgnet/TLObject;
        //  5343: goto            5346
        //  5346: aload_3        
        //  5347: aload           13
        //  5349: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  5352: goto            5458
        //  5355: aload_2        
        //  5356: astore          5
        //  5358: aload           19
        //  5360: astore          13
        //  5362: aload           21
        //  5364: astore          9
        //  5366: iload           34
        //  5368: bipush          6
        //  5370: if_icmpne       5482
        //  5373: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaContact;
        //  5376: astore          5
        //  5378: aload           5
        //  5380: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaContact.<init>:()V
        //  5383: aload           5
        //  5385: aload           24
        //  5387: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  5390: putfield        org/telegram/tgnet/TLRPC$InputMedia.phone_number:Ljava/lang/String;
        //  5393: aload           5
        //  5395: aload           24
        //  5397: getfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //  5400: putfield        org/telegram/tgnet/TLRPC$InputMedia.first_name:Ljava/lang/String;
        //  5403: aload           5
        //  5405: aload           24
        //  5407: getfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //  5410: putfield        org/telegram/tgnet/TLRPC$InputMedia.last_name:Ljava/lang/String;
        //  5413: aload           24
        //  5415: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  5418: ifnull          5448
        //  5421: aload           24
        //  5423: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  5426: ldc_w           "BEGIN:VCARD"
        //  5429: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  5432: ifeq            5448
        //  5435: aload           5
        //  5437: aload           24
        //  5439: getfield        org/telegram/tgnet/TLRPC$User.restriction_reason:Ljava/lang/String;
        //  5442: putfield        org/telegram/tgnet/TLRPC$InputMedia.vcard:Ljava/lang/String;
        //  5445: goto            5455
        //  5448: aload           5
        //  5450: aload           4
        //  5452: putfield        org/telegram/tgnet/TLRPC$InputMedia.vcard:Ljava/lang/String;
        //  5455: iconst_0       
        //  5456: istore          15
        //  5458: aload           4
        //  5460: astore          7
        //  5462: aload           5
        //  5464: astore          4
        //  5466: aload_3        
        //  5467: astore          6
        //  5469: aload           7
        //  5471: astore_3       
        //  5472: goto            6827
        //  5475: aload_2        
        //  5476: astore          4
        //  5478: astore_2       
        //  5479: goto            4479
        //  5482: iload           34
        //  5484: bipush          7
        //  5486: if_icmpeq       5773
        //  5489: iload           34
        //  5491: bipush          9
        //  5493: if_icmpne       5499
        //  5496: goto            5773
        //  5499: iload           34
        //  5501: bipush          8
        //  5503: if_icmpne       5724
        //  5506: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument;
        //  5509: astore          6
        //  5511: aload           6
        //  5513: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument.<init>:()V
        //  5516: aload           6
        //  5518: aload           7
        //  5520: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  5523: putfield        org/telegram/tgnet/TLRPC$InputMedia.mime_type:Ljava/lang/String;
        //  5526: aload           6
        //  5528: aload           7
        //  5530: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  5533: putfield        org/telegram/tgnet/TLRPC$InputMedia.attributes:Ljava/util/ArrayList;
        //  5536: iload           30
        //  5538: ifeq            5566
        //  5541: aload           6
        //  5543: iload           30
        //  5545: putfield        org/telegram/tgnet/TLRPC$InputMedia.ttl_seconds:I
        //  5548: aload_1        
        //  5549: iload           30
        //  5551: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  5554: aload           6
        //  5556: aload           6
        //  5558: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5561: iconst_2       
        //  5562: ior            
        //  5563: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5566: aload           7
        //  5568: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  5571: lconst_0       
        //  5572: lcmp           
        //  5573: ifne            5585
        //  5576: aload           6
        //  5578: astore_3       
        //  5579: iconst_1       
        //  5580: istore          15
        //  5582: goto            5668
        //  5585: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaDocument;
        //  5588: astore_3       
        //  5589: aload_3        
        //  5590: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaDocument.<init>:()V
        //  5593: new             Lorg/telegram/tgnet/TLRPC$TL_inputDocument;
        //  5596: astore          8
        //  5598: aload           8
        //  5600: invokespecial   org/telegram/tgnet/TLRPC$TL_inputDocument.<init>:()V
        //  5603: aload_3        
        //  5604: aload           8
        //  5606: putfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5609: aload_3        
        //  5610: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5613: aload           7
        //  5615: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  5618: putfield        org/telegram/tgnet/TLRPC$InputDocument.id:J
        //  5621: aload_3        
        //  5622: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5625: aload           7
        //  5627: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  5630: putfield        org/telegram/tgnet/TLRPC$InputDocument.access_hash:J
        //  5633: aload_3        
        //  5634: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5637: aload           7
        //  5639: getfield        org/telegram/tgnet/TLRPC$Document.file_reference:[B
        //  5642: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5645: aload_3        
        //  5646: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5649: getfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5652: ifnonnull       5665
        //  5655: aload_3        
        //  5656: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  5659: iconst_0       
        //  5660: newarray        B
        //  5662: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  5665: iconst_0       
        //  5666: istore          15
        //  5668: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  5671: astore          7
        //  5673: aload           7
        //  5675: aload_0        
        //  5676: lload           10
        //  5678: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  5681: aload           7
        //  5683: iconst_3       
        //  5684: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  5687: aload           7
        //  5689: aload           5
        //  5691: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  5694: aload           7
        //  5696: aload           9
        //  5698: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  5701: aload           7
        //  5703: aload           6
        //  5705: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputUploadMedia:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  5708: aload           7
        //  5710: iload           15
        //  5712: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  5715: aload_3        
        //  5716: astore          5
        //  5718: aload           7
        //  5720: astore_3       
        //  5721: goto            6817
        //  5724: aload           4
        //  5726: astore          5
        //  5728: iload           34
        //  5730: bipush          10
        //  5732: if_icmpne       5758
        //  5735: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaPoll;
        //  5738: astore          4
        //  5740: aload           4
        //  5742: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaPoll.<init>:()V
        //  5745: aload           4
        //  5747: aload           14
        //  5749: getfield        org/telegram/tgnet/TLRPC$TL_messageMediaPoll.poll:Lorg/telegram/tgnet/TLRPC$TL_poll;
        //  5752: putfield        org/telegram/tgnet/TLRPC$TL_inputMediaPoll.poll:Lorg/telegram/tgnet/TLRPC$TL_poll;
        //  5755: goto            5761
        //  5758: aconst_null    
        //  5759: astore          4
        //  5761: iconst_0       
        //  5762: istore          15
        //  5764: aload_3        
        //  5765: astore          6
        //  5767: aload           5
        //  5769: astore_3       
        //  5770: goto            6827
        //  5773: aload           4
        //  5775: astore          8
        //  5777: aload           13
        //  5779: ifnonnull       5809
        //  5782: aload           12
        //  5784: ifnonnull       5809
        //  5787: aload           7
        //  5789: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  5792: lconst_0       
        //  5793: lcmp           
        //  5794: ifne            5800
        //  5797: goto            5809
        //  5800: aconst_null    
        //  5801: astore          4
        //  5803: iconst_0       
        //  5804: istore          20
        //  5806: goto            6009
        //  5809: aload           35
        //  5811: ifnonnull       5898
        //  5814: aload           13
        //  5816: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  5819: ifne            5898
        //  5822: aload           13
        //  5824: ldc_w           "http"
        //  5827: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  5830: ifeq            5898
        //  5833: aload           26
        //  5835: ifnull          5898
        //  5838: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaGifExternal;
        //  5841: astore          4
        //  5843: aload           4
        //  5845: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaGifExternal.<init>:()V
        //  5848: aload           26
        //  5850: ldc_w           "url"
        //  5853: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  5856: checkcast       Ljava/lang/String;
        //  5859: ldc_w           "\\|"
        //  5862: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //  5865: astore          6
        //  5867: aload           6
        //  5869: arraylength    
        //  5870: iconst_2       
        //  5871: if_icmpne       5892
        //  5874: aload           4
        //  5876: aload           6
        //  5878: iconst_0       
        //  5879: aaload         
        //  5880: putfield        org/telegram/tgnet/TLRPC$InputMedia.url:Ljava/lang/String;
        //  5883: aload           4
        //  5885: aload           6
        //  5887: iconst_1       
        //  5888: aaload         
        //  5889: putfield        org/telegram/tgnet/TLRPC$InputMedia.q:Ljava/lang/String;
        //  5892: iconst_1       
        //  5893: istore          20
        //  5895: goto            5989
        //  5898: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument;
        //  5901: astore          4
        //  5903: aload           4
        //  5905: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument.<init>:()V
        //  5908: iload           30
        //  5910: ifeq            5938
        //  5913: aload           4
        //  5915: iload           30
        //  5917: putfield        org/telegram/tgnet/TLRPC$InputMedia.ttl_seconds:I
        //  5920: aload_1        
        //  5921: iload           30
        //  5923: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  5926: aload           4
        //  5928: aload           4
        //  5930: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5933: iconst_2       
        //  5934: ior            
        //  5935: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  5938: aload           12
        //  5940: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  5943: istore          15
        //  5945: iload           15
        //  5947: ifne            5986
        //  5950: aload           12
        //  5952: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  5955: ldc_w           "mp4"
        //  5958: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //  5961: ifeq            5986
        //  5964: aload           26
        //  5966: ifnull          5980
        //  5969: aload           26
        //  5971: ldc_w           "forceDocument"
        //  5974: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  5977: ifeq            5986
        //  5980: aload           4
        //  5982: iconst_1       
        //  5983: putfield        org/telegram/tgnet/TLRPC$InputMedia.nosound_video:Z
        //  5986: iconst_0       
        //  5987: istore          20
        //  5989: aload           4
        //  5991: aload           7
        //  5993: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  5996: putfield        org/telegram/tgnet/TLRPC$InputMedia.mime_type:Ljava/lang/String;
        //  5999: aload           4
        //  6001: aload           7
        //  6003: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  6006: putfield        org/telegram/tgnet/TLRPC$InputMedia.attributes:Ljava/util/ArrayList;
        //  6009: aload           7
        //  6011: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  6014: lstore          27
        //  6016: lload           27
        //  6018: lconst_0       
        //  6019: lcmp           
        //  6020: ifne            6037
        //  6023: aload           4
        //  6025: instanceof      Lorg/telegram/tgnet/TLRPC$TL_inputMediaUploadedDocument;
        //  6028: istore          15
        //  6030: aload           4
        //  6032: astore          6
        //  6034: goto            6160
        //  6037: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaDocument;
        //  6040: astore          12
        //  6042: aload           12
        //  6044: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaDocument.<init>:()V
        //  6047: new             Lorg/telegram/tgnet/TLRPC$TL_inputDocument;
        //  6050: astore          6
        //  6052: aload           6
        //  6054: invokespecial   org/telegram/tgnet/TLRPC$TL_inputDocument.<init>:()V
        //  6057: aload           12
        //  6059: aload           6
        //  6061: putfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6064: aload           12
        //  6066: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6069: astore          6
        //  6071: aload           6
        //  6073: aload           7
        //  6075: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  6078: putfield        org/telegram/tgnet/TLRPC$InputDocument.id:J
        //  6081: aload           12
        //  6083: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6086: astore          14
        //  6088: aload           5
        //  6090: astore          6
        //  6092: aload           6
        //  6094: astore          5
        //  6096: aload           14
        //  6098: aload           7
        //  6100: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  6103: putfield        org/telegram/tgnet/TLRPC$InputDocument.access_hash:J
        //  6106: aload           6
        //  6108: astore          5
        //  6110: aload           12
        //  6112: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6115: aload           7
        //  6117: getfield        org/telegram/tgnet/TLRPC$Document.file_reference:[B
        //  6120: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  6123: aload           6
        //  6125: astore          5
        //  6127: aload           12
        //  6129: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6132: getfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  6135: ifnonnull       6153
        //  6138: aload           6
        //  6140: astore          5
        //  6142: aload           12
        //  6144: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaDocument.id:Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6147: iconst_0       
        //  6148: newarray        B
        //  6150: putfield        org/telegram/tgnet/TLRPC$InputDocument.file_reference:[B
        //  6153: iconst_0       
        //  6154: istore          15
        //  6156: aload           12
        //  6158: astore          6
        //  6160: iload           20
        //  6162: ifne            6284
        //  6165: aload           4
        //  6167: ifnull          6284
        //  6170: aload_2        
        //  6171: astore          5
        //  6173: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  6176: astore_3       
        //  6177: aload_2        
        //  6178: astore          5
        //  6180: aload_3        
        //  6181: aload_0        
        //  6182: lload           10
        //  6184: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  6187: aload_2        
        //  6188: astore          5
        //  6190: aload_3        
        //  6191: aload           13
        //  6193: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  6196: aload_2        
        //  6197: astore          5
        //  6199: aload_3        
        //  6200: iconst_2       
        //  6201: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  6204: aload_2        
        //  6205: astore          5
        //  6207: aload_3        
        //  6208: aload_2        
        //  6209: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  6212: aload_2        
        //  6213: astore          5
        //  6215: aload           7
        //  6217: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  6220: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  6223: ifne            6254
        //  6226: aload_2        
        //  6227: astore          5
        //  6229: aload_3        
        //  6230: aload           7
        //  6232: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  6235: iconst_0       
        //  6236: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6239: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  6242: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  6245: aload_2        
        //  6246: astore          5
        //  6248: aload_3        
        //  6249: aload           7
        //  6251: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.locationParent:Lorg/telegram/tgnet/TLObject;
        //  6254: aload_2        
        //  6255: astore          5
        //  6257: aload_3        
        //  6258: aload           9
        //  6260: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  6263: aload_2        
        //  6264: astore          5
        //  6266: aload_3        
        //  6267: aload           4
        //  6269: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputUploadMedia:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  6272: aload_2        
        //  6273: astore          5
        //  6275: aload_3        
        //  6276: iload           15
        //  6278: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  6281: goto            6284
        //  6284: aload           6
        //  6286: astore          4
        //  6288: aload_3        
        //  6289: astore          6
        //  6291: aload           8
        //  6293: astore_3       
        //  6294: goto            6827
        //  6297: astore_2       
        //  6298: goto            6302
        //  6301: astore_2       
        //  6302: goto            7911
        //  6305: aload_2        
        //  6306: astore          7
        //  6308: aload           7
        //  6310: astore          5
        //  6312: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaUploadedPhoto;
        //  6315: astore          8
        //  6317: aload           7
        //  6319: astore          5
        //  6321: aload           8
        //  6323: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaUploadedPhoto.<init>:()V
        //  6326: iload           30
        //  6328: ifeq            6371
        //  6331: aload           7
        //  6333: astore          5
        //  6335: aload           8
        //  6337: iload           30
        //  6339: putfield        org/telegram/tgnet/TLRPC$InputMedia.ttl_seconds:I
        //  6342: aload           7
        //  6344: astore          5
        //  6346: aload_1        
        //  6347: iload           30
        //  6349: putfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  6352: aload           7
        //  6354: astore          5
        //  6356: aload           8
        //  6358: aload           8
        //  6360: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  6363: iconst_2       
        //  6364: ior            
        //  6365: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  6368: goto            6371
        //  6371: aload           26
        //  6373: ifnull          6499
        //  6376: aload           7
        //  6378: astore          5
        //  6380: aload           26
        //  6382: ldc_w           "masks"
        //  6385: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  6388: checkcast       Ljava/lang/String;
        //  6391: astore          6
        //  6393: aload           6
        //  6395: ifnull          6499
        //  6398: aload           7
        //  6400: astore          5
        //  6402: new             Lorg/telegram/tgnet/SerializedData;
        //  6405: astore          9
        //  6407: aload           7
        //  6409: astore          5
        //  6411: aload           9
        //  6413: aload           6
        //  6415: invokestatic    org/telegram/messenger/Utilities.hexToBytes:(Ljava/lang/String;)[B
        //  6418: invokespecial   org/telegram/tgnet/SerializedData.<init>:([B)V
        //  6421: aload           7
        //  6423: astore          5
        //  6425: aload           9
        //  6427: iconst_0       
        //  6428: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
        //  6431: istore          30
        //  6433: iconst_0       
        //  6434: istore          20
        //  6436: iload           20
        //  6438: iload           30
        //  6440: if_icmpge       6474
        //  6443: aload           7
        //  6445: astore          5
        //  6447: aload           8
        //  6449: getfield        org/telegram/tgnet/TLRPC$InputMedia.stickers:Ljava/util/ArrayList;
        //  6452: aload           9
        //  6454: aload           9
        //  6456: iconst_0       
        //  6457: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
        //  6460: iconst_0       
        //  6461: invokestatic    org/telegram/tgnet/TLRPC$InputDocument.TLdeserialize:(Lorg/telegram/tgnet/AbstractSerializedData;IZ)Lorg/telegram/tgnet/TLRPC$InputDocument;
        //  6464: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  6467: pop            
        //  6468: iinc            20, 1
        //  6471: goto            6436
        //  6474: aload           7
        //  6476: astore          5
        //  6478: aload           8
        //  6480: aload           8
        //  6482: getfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  6485: iconst_1       
        //  6486: ior            
        //  6487: putfield        org/telegram/tgnet/TLRPC$InputMedia.flags:I
        //  6490: aload           7
        //  6492: astore          5
        //  6494: aload           9
        //  6496: invokevirtual   org/telegram/tgnet/SerializedData.cleanup:()V
        //  6499: aload           7
        //  6501: astore          5
        //  6503: aload           18
        //  6505: getfield        org/telegram/tgnet/TLRPC$Photo.access_hash:J
        //  6508: lconst_0       
        //  6509: lcmp           
        //  6510: ifne            6523
        //  6513: aload           8
        //  6515: astore          6
        //  6517: iconst_1       
        //  6518: istore          15
        //  6520: goto            6654
        //  6523: aload           7
        //  6525: astore          5
        //  6527: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaPhoto;
        //  6530: astore          6
        //  6532: aload           7
        //  6534: astore          5
        //  6536: aload           6
        //  6538: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.<init>:()V
        //  6541: aload           7
        //  6543: astore          5
        //  6545: new             Lorg/telegram/tgnet/TLRPC$TL_inputPhoto;
        //  6548: astore          9
        //  6550: aload           7
        //  6552: astore          5
        //  6554: aload           9
        //  6556: invokespecial   org/telegram/tgnet/TLRPC$TL_inputPhoto.<init>:()V
        //  6559: aload           7
        //  6561: astore          5
        //  6563: aload           6
        //  6565: aload           9
        //  6567: putfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6570: aload           7
        //  6572: astore          5
        //  6574: aload           6
        //  6576: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6579: aload           18
        //  6581: getfield        org/telegram/tgnet/TLRPC$Photo.id:J
        //  6584: putfield        org/telegram/tgnet/TLRPC$InputPhoto.id:J
        //  6587: aload           7
        //  6589: astore          5
        //  6591: aload           6
        //  6593: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6596: aload           18
        //  6598: getfield        org/telegram/tgnet/TLRPC$Photo.access_hash:J
        //  6601: putfield        org/telegram/tgnet/TLRPC$InputPhoto.access_hash:J
        //  6604: aload           7
        //  6606: astore          5
        //  6608: aload           6
        //  6610: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6613: aload           18
        //  6615: getfield        org/telegram/tgnet/TLRPC$Photo.file_reference:[B
        //  6618: putfield        org/telegram/tgnet/TLRPC$InputPhoto.file_reference:[B
        //  6621: aload           7
        //  6623: astore          5
        //  6625: aload           6
        //  6627: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6630: getfield        org/telegram/tgnet/TLRPC$InputPhoto.file_reference:[B
        //  6633: ifnonnull       6651
        //  6636: aload           7
        //  6638: astore          5
        //  6640: aload           6
        //  6642: getfield        org/telegram/tgnet/TLRPC$TL_inputMediaPhoto.id:Lorg/telegram/tgnet/TLRPC$InputPhoto;
        //  6645: iconst_0       
        //  6646: newarray        B
        //  6648: putfield        org/telegram/tgnet/TLRPC$InputPhoto.file_reference:[B
        //  6651: iconst_0       
        //  6652: istore          15
        //  6654: aload_3        
        //  6655: ifnonnull       6709
        //  6658: aload           7
        //  6660: astore          5
        //  6662: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  6665: astore_3       
        //  6666: aload           7
        //  6668: astore          5
        //  6670: aload_3        
        //  6671: aload_0        
        //  6672: lload           10
        //  6674: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  6677: aload           7
        //  6679: astore          5
        //  6681: aload_3        
        //  6682: iconst_0       
        //  6683: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  6686: aload           7
        //  6688: astore          5
        //  6690: aload_3        
        //  6691: aload           7
        //  6693: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  6696: aload           7
        //  6698: astore          5
        //  6700: aload_3        
        //  6701: aload           19
        //  6703: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  6706: goto            6709
        //  6709: aload           7
        //  6711: astore          5
        //  6713: aload_3        
        //  6714: aload           8
        //  6716: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputUploadMedia:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  6719: aload           7
        //  6721: astore          5
        //  6723: aload_3        
        //  6724: iload           15
        //  6726: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  6729: aload           12
        //  6731: ifnull          6774
        //  6734: aload           7
        //  6736: astore          5
        //  6738: aload           12
        //  6740: invokevirtual   java/lang/String.length:()I
        //  6743: ifle            6774
        //  6746: aload           7
        //  6748: astore          5
        //  6750: aload           12
        //  6752: ldc_w           "http"
        //  6755: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  6758: ifeq            6774
        //  6761: aload           7
        //  6763: astore          5
        //  6765: aload_3        
        //  6766: aload           12
        //  6768: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocation:Ljava/lang/String;
        //  6771: goto            6813
        //  6774: aload           7
        //  6776: astore          5
        //  6778: aload_3        
        //  6779: aload           18
        //  6781: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  6784: aload           18
        //  6786: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  6789: invokevirtual   java/util/ArrayList.size:()I
        //  6792: iconst_1       
        //  6793: isub           
        //  6794: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  6797: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  6800: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  6803: aload           7
        //  6805: astore          5
        //  6807: aload_3        
        //  6808: aload           18
        //  6810: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.locationParent:Lorg/telegram/tgnet/TLObject;
        //  6813: aload           6
        //  6815: astore          5
        //  6817: aload_3        
        //  6818: astore          6
        //  6820: aload           4
        //  6822: astore_3       
        //  6823: aload           5
        //  6825: astore          4
        //  6827: aload_1        
        //  6828: astore          7
        //  6830: aload           25
        //  6832: ifnull          6988
        //  6835: aload_2        
        //  6836: astore          5
        //  6838: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendBroadcast;
        //  6841: astore          7
        //  6843: aload_2        
        //  6844: astore          5
        //  6846: aload           7
        //  6848: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.<init>:()V
        //  6851: aload_2        
        //  6852: astore          5
        //  6854: new             Ljava/util/ArrayList;
        //  6857: astore          8
        //  6859: aload_2        
        //  6860: astore          5
        //  6862: aload           8
        //  6864: invokespecial   java/util/ArrayList.<init>:()V
        //  6867: iconst_0       
        //  6868: istore          20
        //  6870: aload_2        
        //  6871: astore          5
        //  6873: iload           20
        //  6875: aload           25
        //  6877: invokevirtual   java/util/ArrayList.size:()I
        //  6880: if_icmpge       6907
        //  6883: aload_2        
        //  6884: astore          5
        //  6886: aload           8
        //  6888: getstatic       org/telegram/messenger/Utilities.random:Ljava/security/SecureRandom;
        //  6891: invokevirtual   java/security/SecureRandom.nextLong:()J
        //  6894: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  6897: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  6900: pop            
        //  6901: iinc            20, 1
        //  6904: goto            6870
        //  6907: aload_2        
        //  6908: astore          5
        //  6910: aload           7
        //  6912: aload           25
        //  6914: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.contacts:Ljava/util/ArrayList;
        //  6917: aload_2        
        //  6918: astore          5
        //  6920: aload           7
        //  6922: aload           4
        //  6924: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.media:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  6927: aload_2        
        //  6928: astore          5
        //  6930: aload           7
        //  6932: aload           8
        //  6934: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.random_id:Ljava/util/ArrayList;
        //  6937: aload_2        
        //  6938: astore          5
        //  6940: aload           7
        //  6942: aload_3        
        //  6943: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.message:Ljava/lang/String;
        //  6946: aload           6
        //  6948: ifnull          6961
        //  6951: aload_2        
        //  6952: astore          5
        //  6954: aload           6
        //  6956: aload           7
        //  6958: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendRequest:Lorg/telegram/tgnet/TLObject;
        //  6961: aload           16
        //  6963: ifnonnull       6982
        //  6966: aload_2        
        //  6967: astore          5
        //  6969: aload_0        
        //  6970: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  6973: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        //  6976: lload           10
        //  6978: iconst_0       
        //  6979: invokevirtual   org/telegram/messenger/DataQuery.cleanDraft:(JZ)V
        //  6982: aload           7
        //  6984: astore_3       
        //  6985: goto            7645
        //  6988: lload           40
        //  6990: lconst_0       
        //  6991: lcmp           
        //  6992: ifeq            7403
        //  6995: aload_2        
        //  6996: astore          5
        //  6998: aload           6
        //  7000: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendRequest:Lorg/telegram/tgnet/TLObject;
        //  7003: ifnull          7021
        //  7006: aload_2        
        //  7007: astore          5
        //  7009: aload           6
        //  7011: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendRequest:Lorg/telegram/tgnet/TLObject;
        //  7014: checkcast       Lorg/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia;
        //  7017: astore_3       
        //  7018: goto            7174
        //  7021: aload_2        
        //  7022: astore          5
        //  7024: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia;
        //  7027: astore_3       
        //  7028: aload_2        
        //  7029: astore          5
        //  7031: aload_3        
        //  7032: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.<init>:()V
        //  7035: aload_2        
        //  7036: astore          5
        //  7038: aload_3        
        //  7039: aload           33
        //  7041: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        //  7044: aload_2        
        //  7045: astore          5
        //  7047: aload           7
        //  7049: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  7052: instanceof      Lorg/telegram/tgnet/TLRPC$TL_peerChannel;
        //  7055: ifeq            7129
        //  7058: aload_2        
        //  7059: astore          5
        //  7061: aload_0        
        //  7062: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  7065: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        //  7068: astore          8
        //  7070: aload_2        
        //  7071: astore          5
        //  7073: new             Ljava/lang/StringBuilder;
        //  7076: astore          9
        //  7078: aload_2        
        //  7079: astore          5
        //  7081: aload           9
        //  7083: invokespecial   java/lang/StringBuilder.<init>:()V
        //  7086: aload_2        
        //  7087: astore          5
        //  7089: aload           9
        //  7091: ldc_w           "silent_"
        //  7094: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  7097: pop            
        //  7098: aload_2        
        //  7099: astore          5
        //  7101: aload           9
        //  7103: lload           10
        //  7105: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  7108: pop            
        //  7109: aload_2        
        //  7110: astore          5
        //  7112: aload_3        
        //  7113: aload           8
        //  7115: aload           9
        //  7117: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  7120: iconst_0       
        //  7121: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //  7126: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.silent:Z
        //  7129: aload_2        
        //  7130: astore          5
        //  7132: aload           7
        //  7134: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        //  7137: ifeq            7165
        //  7140: aload_2        
        //  7141: astore          5
        //  7143: aload_3        
        //  7144: aload_3        
        //  7145: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.flags:I
        //  7148: iconst_1       
        //  7149: ior            
        //  7150: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.flags:I
        //  7153: aload_2        
        //  7154: astore          5
        //  7156: aload_3        
        //  7157: aload           7
        //  7159: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        //  7162: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.reply_to_msg_id:I
        //  7165: aload_2        
        //  7166: astore          5
        //  7168: aload           6
        //  7170: aload_3        
        //  7171: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendRequest:Lorg/telegram/tgnet/TLObject;
        //  7174: aload_2        
        //  7175: astore          5
        //  7177: aload           6
        //  7179: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.messageObjects:Ljava/util/ArrayList;
        //  7182: aload_2        
        //  7183: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7186: pop            
        //  7187: aload_2        
        //  7188: astore          5
        //  7190: aload           6
        //  7192: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObjects:Ljava/util/ArrayList;
        //  7195: aload           21
        //  7197: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7200: pop            
        //  7201: aload_2        
        //  7202: astore          5
        //  7204: aload           6
        //  7206: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.locations:Ljava/util/ArrayList;
        //  7209: aload           6
        //  7211: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  7214: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7217: pop            
        //  7218: aload_2        
        //  7219: astore          5
        //  7221: aload           6
        //  7223: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.videoEditedInfos:Ljava/util/ArrayList;
        //  7226: aload           6
        //  7228: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  7231: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7234: pop            
        //  7235: aload_2        
        //  7236: astore          5
        //  7238: aload           6
        //  7240: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocations:Ljava/util/ArrayList;
        //  7243: aload           6
        //  7245: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocation:Ljava/lang/String;
        //  7248: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7251: pop            
        //  7252: aload_2        
        //  7253: astore          5
        //  7255: aload           6
        //  7257: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputMedias:Ljava/util/ArrayList;
        //  7260: aload           6
        //  7262: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.inputUploadMedia:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  7265: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7268: pop            
        //  7269: aload_2        
        //  7270: astore          5
        //  7272: aload           6
        //  7274: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.messages:Ljava/util/ArrayList;
        //  7277: aload           7
        //  7279: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7282: pop            
        //  7283: aload_2        
        //  7284: astore          5
        //  7286: aload           6
        //  7288: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPaths:Ljava/util/ArrayList;
        //  7291: aload           19
        //  7293: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7296: pop            
        //  7297: aload_2        
        //  7298: astore          5
        //  7300: new             Lorg/telegram/tgnet/TLRPC$TL_inputSingleMedia;
        //  7303: astore          8
        //  7305: aload_2        
        //  7306: astore          5
        //  7308: aload           8
        //  7310: invokespecial   org/telegram/tgnet/TLRPC$TL_inputSingleMedia.<init>:()V
        //  7313: aload_2        
        //  7314: astore          5
        //  7316: aload           8
        //  7318: aload           7
        //  7320: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  7323: putfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.random_id:J
        //  7326: aload_2        
        //  7327: astore          5
        //  7329: aload           8
        //  7331: aload           4
        //  7333: putfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.media:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  7336: aload_2        
        //  7337: astore          5
        //  7339: aload           8
        //  7341: aload           29
        //  7343: putfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.message:Ljava/lang/String;
        //  7346: aload           17
        //  7348: ifnull          7387
        //  7351: aload_2        
        //  7352: astore          5
        //  7354: aload           17
        //  7356: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  7359: ifne            7387
        //  7362: aload_2        
        //  7363: astore          5
        //  7365: aload           8
        //  7367: aload           17
        //  7369: putfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.entities:Ljava/util/ArrayList;
        //  7372: aload_2        
        //  7373: astore          5
        //  7375: aload           8
        //  7377: aload           8
        //  7379: getfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.flags:I
        //  7382: iconst_1       
        //  7383: ior            
        //  7384: putfield        org/telegram/tgnet/TLRPC$TL_inputSingleMedia.flags:I
        //  7387: aload_2        
        //  7388: astore          5
        //  7390: aload_3        
        //  7391: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMultiMedia.multi_media:Ljava/util/ArrayList;
        //  7394: aload           8
        //  7396: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  7399: pop            
        //  7400: goto            7645
        //  7403: aload_2        
        //  7404: astore          5
        //  7406: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendMedia;
        //  7409: astore          8
        //  7411: aload_2        
        //  7412: astore          5
        //  7414: aload           8
        //  7416: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendMedia.<init>:()V
        //  7419: aload_2        
        //  7420: astore          5
        //  7422: aload           8
        //  7424: aload           33
        //  7426: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        //  7429: aload_2        
        //  7430: astore          5
        //  7432: aload           7
        //  7434: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //  7437: instanceof      Lorg/telegram/tgnet/TLRPC$TL_peerChannel;
        //  7440: ifeq            7510
        //  7443: aload_2        
        //  7444: astore          5
        //  7446: aload_0        
        //  7447: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  7450: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        //  7453: astore          9
        //  7455: aload_2        
        //  7456: astore          5
        //  7458: new             Ljava/lang/StringBuilder;
        //  7461: astore_3       
        //  7462: aload_2        
        //  7463: astore          5
        //  7465: aload_3        
        //  7466: invokespecial   java/lang/StringBuilder.<init>:()V
        //  7469: aload_2        
        //  7470: astore          5
        //  7472: aload_3        
        //  7473: ldc_w           "silent_"
        //  7476: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  7479: pop            
        //  7480: aload_2        
        //  7481: astore          5
        //  7483: aload_3        
        //  7484: lload           10
        //  7486: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  7489: pop            
        //  7490: aload_2        
        //  7491: astore          5
        //  7493: aload           8
        //  7495: aload           9
        //  7497: aload_3        
        //  7498: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  7501: iconst_0       
        //  7502: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //  7507: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.silent:Z
        //  7510: aload_2        
        //  7511: astore          5
        //  7513: aload           7
        //  7515: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        //  7518: ifeq            7549
        //  7521: aload_2        
        //  7522: astore          5
        //  7524: aload           8
        //  7526: aload           8
        //  7528: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.flags:I
        //  7531: iconst_1       
        //  7532: ior            
        //  7533: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.flags:I
        //  7536: aload_2        
        //  7537: astore          5
        //  7539: aload           8
        //  7541: aload           7
        //  7543: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        //  7546: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.reply_to_msg_id:I
        //  7549: aload_2        
        //  7550: astore          5
        //  7552: aload           8
        //  7554: aload           7
        //  7556: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  7559: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.random_id:J
        //  7562: aload_2        
        //  7563: astore          5
        //  7565: aload           8
        //  7567: aload           4
        //  7569: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.media:Lorg/telegram/tgnet/TLRPC$InputMedia;
        //  7572: aload_2        
        //  7573: astore          5
        //  7575: aload           8
        //  7577: aload           29
        //  7579: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.message:Ljava/lang/String;
        //  7582: aload           17
        //  7584: ifnull          7624
        //  7587: aload_2        
        //  7588: astore          5
        //  7590: aload           17
        //  7592: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  7595: ifne            7624
        //  7598: aload_2        
        //  7599: astore          5
        //  7601: aload           8
        //  7603: aload           17
        //  7605: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.entities:Ljava/util/ArrayList;
        //  7608: aload_2        
        //  7609: astore          5
        //  7611: aload           8
        //  7613: aload           8
        //  7615: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.flags:I
        //  7618: bipush          8
        //  7620: ior            
        //  7621: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMedia.flags:I
        //  7624: aload           8
        //  7626: astore_3       
        //  7627: aload           6
        //  7629: ifnull          7645
        //  7632: aload_2        
        //  7633: astore          5
        //  7635: aload           6
        //  7637: aload           8
        //  7639: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendRequest:Lorg/telegram/tgnet/TLObject;
        //  7642: aload           8
        //  7644: astore_3       
        //  7645: lload           40
        //  7647: lconst_0       
        //  7648: lcmp           
        //  7649: ifeq            7664
        //  7652: aload_2        
        //  7653: astore          5
        //  7655: aload_0        
        //  7656: aload           6
        //  7658: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  7661: goto            12210
        //  7664: iload           34
        //  7666: iconst_1       
        //  7667: if_icmpne       7687
        //  7670: aload_2        
        //  7671: astore          5
        //  7673: aload_0        
        //  7674: aload_3        
        //  7675: aload_2        
        //  7676: aconst_null    
        //  7677: aload           6
        //  7679: aload           21
        //  7681: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7684: goto            12210
        //  7687: iload           34
        //  7689: iconst_2       
        //  7690: if_icmpne       7730
        //  7693: iload           15
        //  7695: ifeq            7710
        //  7698: aload_2        
        //  7699: astore          5
        //  7701: aload_0        
        //  7702: aload           6
        //  7704: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  7707: goto            12210
        //  7710: aload_2        
        //  7711: astore          5
        //  7713: aload_0        
        //  7714: aload_3        
        //  7715: aload_2        
        //  7716: aload           19
        //  7718: aconst_null    
        //  7719: iconst_1       
        //  7720: aload           6
        //  7722: aload           21
        //  7724: invokevirtual   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;ZLorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7727: goto            12210
        //  7730: iload           34
        //  7732: iconst_3       
        //  7733: if_icmpne       7771
        //  7736: iload           15
        //  7738: ifeq            7753
        //  7741: aload_2        
        //  7742: astore          5
        //  7744: aload_0        
        //  7745: aload           6
        //  7747: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  7750: goto            12210
        //  7753: aload_2        
        //  7754: astore          5
        //  7756: aload_0        
        //  7757: aload_3        
        //  7758: aload_2        
        //  7759: aload           19
        //  7761: aload           6
        //  7763: aload           21
        //  7765: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7768: goto            12210
        //  7771: iload           34
        //  7773: bipush          6
        //  7775: if_icmpne       7796
        //  7778: aload_2        
        //  7779: astore          5
        //  7781: aload_0        
        //  7782: aload_3        
        //  7783: aload_2        
        //  7784: aload           19
        //  7786: aload           6
        //  7788: aload           21
        //  7790: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7793: goto            12210
        //  7796: iload           34
        //  7798: bipush          7
        //  7800: if_icmpne       7843
        //  7803: iload           15
        //  7805: ifeq            7825
        //  7808: aload           6
        //  7810: ifnull          7825
        //  7813: aload_2        
        //  7814: astore          5
        //  7816: aload_0        
        //  7817: aload           6
        //  7819: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  7822: goto            12210
        //  7825: aload_2        
        //  7826: astore          5
        //  7828: aload_0        
        //  7829: aload_3        
        //  7830: aload_2        
        //  7831: aload           19
        //  7833: aload           6
        //  7835: aload           21
        //  7837: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7840: goto            12210
        //  7843: iload           34
        //  7845: bipush          8
        //  7847: if_icmpne       7885
        //  7850: iload           15
        //  7852: ifeq            7867
        //  7855: aload_2        
        //  7856: astore          5
        //  7858: aload_0        
        //  7859: aload           6
        //  7861: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  7864: goto            12210
        //  7867: aload_2        
        //  7868: astore          5
        //  7870: aload_0        
        //  7871: aload_3        
        //  7872: aload_2        
        //  7873: aload           19
        //  7875: aload           6
        //  7877: aload           21
        //  7879: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7882: goto            12210
        //  7885: iload           34
        //  7887: bipush          10
        //  7889: if_icmpne       12210
        //  7892: aload_2        
        //  7893: astore          5
        //  7895: aload_0        
        //  7896: aload_3        
        //  7897: aload_2        
        //  7898: aload           19
        //  7900: aload           6
        //  7902: aload           21
        //  7904: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        //  7907: goto            12210
        //  7910: astore_2       
        //  7911: aload_1        
        //  7912: astore_3       
        //  7913: aload           5
        //  7915: astore_1       
        //  7916: goto            12148
        //  7919: aload_1        
        //  7920: astore          14
        //  7922: aload_2        
        //  7923: astore          6
        //  7925: aload           35
        //  7927: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.layer:I
        //  7930: invokestatic    org/telegram/messenger/AndroidUtilities.getPeerLayerVersion:(I)I
        //  7933: istore          20
        //  7935: iload           20
        //  7937: bipush          73
        //  7939: if_icmplt       8002
        //  7942: aload           6
        //  7944: astore          5
        //  7946: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessage;
        //  7949: astore          8
        //  7951: aload           6
        //  7953: astore          5
        //  7955: aload           8
        //  7957: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessage.<init>:()V
        //  7960: lload           40
        //  7962: lconst_0       
        //  7963: lcmp           
        //  7964: ifeq            7999
        //  7967: aload           6
        //  7969: astore          5
        //  7971: aload           8
        //  7973: lload           40
        //  7975: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.grouped_id:J
        //  7978: aload           6
        //  7980: astore          5
        //  7982: aload           8
        //  7984: aload           8
        //  7986: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  7989: ldc_w           131072
        //  7992: ior            
        //  7993: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  7996: goto            8011
        //  7999: goto            8011
        //  8002: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessage_layer45;
        //  8005: dup            
        //  8006: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessage_layer45.<init>:()V
        //  8009: astore          8
        //  8011: aload           8
        //  8013: aload           14
        //  8015: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        //  8018: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.ttl:I
        //  8021: aload           17
        //  8023: ifnull          8067
        //  8026: aload           6
        //  8028: astore          5
        //  8030: aload           17
        //  8032: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  8035: ifne            8067
        //  8038: aload           6
        //  8040: astore          5
        //  8042: aload           8
        //  8044: aload           17
        //  8046: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.entities:Ljava/util/ArrayList;
        //  8049: aload           6
        //  8051: astore          5
        //  8053: aload           8
        //  8055: aload           8
        //  8057: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8060: sipush          128
        //  8063: ior            
        //  8064: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8067: aload           14
        //  8069: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_random_id:J
        //  8072: lstore          42
        //  8074: lload           42
        //  8076: lconst_0       
        //  8077: lcmp           
        //  8078: ifeq            8112
        //  8081: aload           6
        //  8083: astore          5
        //  8085: aload           8
        //  8087: aload           14
        //  8089: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_random_id:J
        //  8092: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.reply_to_random_id:J
        //  8095: aload           6
        //  8097: astore          5
        //  8099: aload           8
        //  8101: aload           8
        //  8103: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8106: bipush          8
        //  8108: ior            
        //  8109: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8112: aload           8
        //  8114: aload           8
        //  8116: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8119: sipush          512
        //  8122: ior            
        //  8123: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8126: aload           26
        //  8128: ifnull          8184
        //  8131: aload           6
        //  8133: astore          5
        //  8135: aload           26
        //  8137: ldc_w           "bot_name"
        //  8140: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  8143: ifnull          8184
        //  8146: aload           6
        //  8148: astore          5
        //  8150: aload           8
        //  8152: aload           26
        //  8154: ldc_w           "bot_name"
        //  8157: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  8160: checkcast       Ljava/lang/String;
        //  8163: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.via_bot_name:Ljava/lang/String;
        //  8166: aload           6
        //  8168: astore          5
        //  8170: aload           8
        //  8172: aload           8
        //  8174: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8177: sipush          2048
        //  8180: ior            
        //  8181: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        //  8184: aload           8
        //  8186: aload           14
        //  8188: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        //  8191: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.random_id:J
        //  8194: aload           8
        //  8196: aload           4
        //  8198: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.message:Ljava/lang/String;
        //  8201: iload           34
        //  8203: iconst_1       
        //  8204: if_icmpne       8418
        //  8207: aload           6
        //  8209: astore          5
        //  8211: aload           9
        //  8213: instanceof      Lorg/telegram/tgnet/TLRPC$TL_messageMediaVenue;
        //  8216: ifeq            8319
        //  8219: aload           6
        //  8221: astore          5
        //  8223: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVenue;
        //  8226: astore          4
        //  8228: aload           6
        //  8230: astore          5
        //  8232: aload           4
        //  8234: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVenue.<init>:()V
        //  8237: aload           6
        //  8239: astore          5
        //  8241: aload           8
        //  8243: aload           4
        //  8245: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8248: aload           6
        //  8250: astore          5
        //  8252: aload           8
        //  8254: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8257: aload           9
        //  8259: getfield        org/telegram/tgnet/TLRPC$MessageMedia.address:Ljava/lang/String;
        //  8262: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.address:Ljava/lang/String;
        //  8265: aload           6
        //  8267: astore          5
        //  8269: aload           8
        //  8271: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8274: aload           9
        //  8276: getfield        org/telegram/tgnet/TLRPC$MessageMedia.title:Ljava/lang/String;
        //  8279: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.title:Ljava/lang/String;
        //  8282: aload           6
        //  8284: astore          5
        //  8286: aload           8
        //  8288: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8291: aload           9
        //  8293: getfield        org/telegram/tgnet/TLRPC$MessageMedia.provider:Ljava/lang/String;
        //  8296: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.provider:Ljava/lang/String;
        //  8299: aload           6
        //  8301: astore          5
        //  8303: aload           8
        //  8305: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8308: aload           9
        //  8310: getfield        org/telegram/tgnet/TLRPC$MessageMedia.venue_id:Ljava/lang/String;
        //  8313: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.venue_id:Ljava/lang/String;
        //  8316: goto            8348
        //  8319: aload           6
        //  8321: astore          5
        //  8323: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaGeoPoint;
        //  8326: astore          4
        //  8328: aload           6
        //  8330: astore          5
        //  8332: aload           4
        //  8334: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaGeoPoint.<init>:()V
        //  8337: aload           6
        //  8339: astore          5
        //  8341: aload           8
        //  8343: aload           4
        //  8345: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8348: aload           6
        //  8350: astore          5
        //  8352: aload           8
        //  8354: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8357: aload           9
        //  8359: getfield        org/telegram/tgnet/TLRPC$MessageMedia.geo:Lorg/telegram/tgnet/TLRPC$GeoPoint;
        //  8362: getfield        org/telegram/tgnet/TLRPC$GeoPoint.lat:D
        //  8365: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.lat:D
        //  8368: aload           6
        //  8370: astore          5
        //  8372: aload           8
        //  8374: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8377: aload           9
        //  8379: getfield        org/telegram/tgnet/TLRPC$MessageMedia.geo:Lorg/telegram/tgnet/TLRPC$GeoPoint;
        //  8382: getfield        org/telegram/tgnet/TLRPC$GeoPoint._long:D
        //  8385: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia._long:D
        //  8388: aload           6
        //  8390: astore          5
        //  8392: aload_0        
        //  8393: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  8396: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        //  8399: aload           8
        //  8401: aload           6
        //  8403: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  8406: aload           35
        //  8408: aconst_null    
        //  8409: aconst_null    
        //  8410: aload           6
        //  8412: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        //  8415: goto            9591
        //  8418: iload           34
        //  8420: iconst_2       
        //  8421: if_icmpeq       9975
        //  8424: iload           34
        //  8426: bipush          9
        //  8428: if_icmpne       8439
        //  8431: aload           18
        //  8433: ifnull          8439
        //  8436: goto            9975
        //  8439: iload           34
        //  8441: iconst_3       
        //  8442: if_icmpne       9007
        //  8445: aload_0        
        //  8446: aload           7
        //  8448: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  8451: invokespecial   org/telegram/messenger/SendMessagesHelper.getThumbForSecretChat:(Ljava/util/ArrayList;)Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  8454: astore          4
        //  8456: aload           4
        //  8458: invokestatic    org/telegram/messenger/ImageLoader.fillPhotoSizeWithBytes:(Lorg/telegram/tgnet/TLRPC$PhotoSize;)V
        //  8461: aload           7
        //  8463: invokestatic    org/telegram/messenger/MessageObject.isNewGifDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  8466: ifne            8558
        //  8469: aload           7
        //  8471: invokestatic    org/telegram/messenger/MessageObject.isRoundVideoDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  8474: ifeq            8480
        //  8477: goto            8558
        //  8480: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo;
        //  8483: astore          5
        //  8485: aload           5
        //  8487: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo.<init>:()V
        //  8490: aload           8
        //  8492: aload           5
        //  8494: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8497: aload           4
        //  8499: ifnull          8537
        //  8502: aload           6
        //  8504: astore          5
        //  8506: aload           4
        //  8508: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  8511: ifnull          8537
        //  8514: aload           6
        //  8516: astore          5
        //  8518: aload           8
        //  8520: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8523: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo;
        //  8526: aload           4
        //  8528: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  8531: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo.thumb:[B
        //  8534: goto            8634
        //  8537: aload           8
        //  8539: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8542: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo;
        //  8545: astore          5
        //  8547: aload           5
        //  8549: iconst_0       
        //  8550: newarray        B
        //  8552: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaVideo.thumb:[B
        //  8555: goto            8634
        //  8558: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  8561: astore          5
        //  8563: aload           5
        //  8565: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.<init>:()V
        //  8568: aload           8
        //  8570: aload           5
        //  8572: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8575: aload           8
        //  8577: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8580: aload           7
        //  8582: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  8585: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.attributes:Ljava/util/ArrayList;
        //  8588: aload           4
        //  8590: ifnull          8620
        //  8593: aload           4
        //  8595: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  8598: ifnull          8620
        //  8601: aload           8
        //  8603: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8606: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  8609: aload           4
        //  8611: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  8614: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  8617: goto            8634
        //  8620: aload           8
        //  8622: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8625: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  8628: iconst_0       
        //  8629: newarray        B
        //  8631: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  8634: aload           8
        //  8636: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8639: aload           29
        //  8641: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.caption:Ljava/lang/String;
        //  8644: aload           8
        //  8646: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8649: ldc_w           "video/mp4"
        //  8652: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.mime_type:Ljava/lang/String;
        //  8655: aload           8
        //  8657: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8660: aload           7
        //  8662: getfield        org/telegram/tgnet/TLRPC$Document.size:I
        //  8665: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.size:I
        //  8668: iconst_0       
        //  8669: istore          20
        //  8671: iload           20
        //  8673: aload           7
        //  8675: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  8678: invokevirtual   java/util/ArrayList.size:()I
        //  8681: if_icmpge       8755
        //  8684: aload           7
        //  8686: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  8689: iload           20
        //  8691: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  8694: checkcast       Lorg/telegram/tgnet/TLRPC$DocumentAttribute;
        //  8697: astore          5
        //  8699: aload           5
        //  8701: instanceof      Lorg/telegram/tgnet/TLRPC$TL_documentAttributeVideo;
        //  8704: ifeq            8749
        //  8707: aload           8
        //  8709: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8712: aload           5
        //  8714: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.w:I
        //  8717: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.w:I
        //  8720: aload           8
        //  8722: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8725: aload           5
        //  8727: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.h:I
        //  8730: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.h:I
        //  8733: aload           8
        //  8735: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8738: aload           5
        //  8740: getfield        org/telegram/tgnet/TLRPC$DocumentAttribute.duration:I
        //  8743: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.duration:I
        //  8746: goto            8755
        //  8749: iinc            20, 1
        //  8752: goto            8671
        //  8755: aload           8
        //  8757: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8760: aload           4
        //  8762: getfield        org/telegram/tgnet/TLRPC$PhotoSize.h:I
        //  8765: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        //  8768: aload           8
        //  8770: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8773: aload           4
        //  8775: getfield        org/telegram/tgnet/TLRPC$PhotoSize.w:I
        //  8778: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        //  8781: aload           7
        //  8783: getfield        org/telegram/tgnet/TLRPC$Document.key:[B
        //  8786: ifnull          8885
        //  8789: lload           40
        //  8791: lconst_0       
        //  8792: lcmp           
        //  8793: ifeq            8799
        //  8796: goto            8885
        //  8799: new             Lorg/telegram/tgnet/TLRPC$TL_inputEncryptedFile;
        //  8802: astore          4
        //  8804: aload           4
        //  8806: invokespecial   org/telegram/tgnet/TLRPC$TL_inputEncryptedFile.<init>:()V
        //  8809: aload           4
        //  8811: aload           7
        //  8813: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  8816: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.id:J
        //  8819: aload           4
        //  8821: aload           7
        //  8823: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  8826: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.access_hash:J
        //  8829: aload           8
        //  8831: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8834: aload           7
        //  8836: getfield        org/telegram/tgnet/TLRPC$Document.key:[B
        //  8839: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.key:[B
        //  8842: aload           8
        //  8844: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  8847: aload           7
        //  8849: getfield        org/telegram/tgnet/TLRPC$Document.iv:[B
        //  8852: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.iv:[B
        //  8855: aload_0        
        //  8856: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  8859: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        //  8862: aload           8
        //  8864: aload           6
        //  8866: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  8869: aload           35
        //  8871: aload           4
        //  8873: aconst_null    
        //  8874: aload           6
        //  8876: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        //  8879: aload_3        
        //  8880: astore          4
        //  8882: goto            8995
        //  8885: aload_3        
        //  8886: ifnonnull       8974
        //  8889: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  8892: astore_3       
        //  8893: aload_3        
        //  8894: aload_0        
        //  8895: lload           10
        //  8897: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  8900: aload_3        
        //  8901: aload           35
        //  8903: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.encryptedChat:Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //  8906: aload_3        
        //  8907: iconst_1       
        //  8908: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  8911: aload_3        
        //  8912: aload           8
        //  8914: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        //  8917: aload_3        
        //  8918: aload           19
        //  8920: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  8923: aload_3        
        //  8924: aload           6
        //  8926: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  8929: aload           26
        //  8931: ifnull          8960
        //  8934: aload           26
        //  8936: ldc_w           "parentObject"
        //  8939: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  8942: ifeq            8960
        //  8945: aload_3        
        //  8946: aload           26
        //  8948: ldc_w           "parentObject"
        //  8951: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  8954: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  8957: goto            8966
        //  8960: aload_3        
        //  8961: aload           21
        //  8963: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  8966: aload_3        
        //  8967: iconst_1       
        //  8968: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  8971: goto            8974
        //  8974: aload_3        
        //  8975: aload           13
        //  8977: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //  8980: lload           40
        //  8982: lconst_0       
        //  8983: lcmp           
        //  8984: ifne            8992
        //  8987: aload_0        
        //  8988: aload_3        
        //  8989: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  8992: aload_3        
        //  8993: astore          4
        //  8995: goto            10448
        //  8998: astore_2       
        //  8999: aload_1        
        //  9000: astore_3       
        //  9001: aload           6
        //  9003: astore_1       
        //  9004: goto            12148
        //  9007: lload           10
        //  9009: lstore          42
        //  9011: aload           19
        //  9013: astore          4
        //  9015: iload           34
        //  9017: bipush          6
        //  9019: if_icmpne       9121
        //  9022: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaContact;
        //  9025: astore          4
        //  9027: aload           4
        //  9029: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaContact.<init>:()V
        //  9032: aload           8
        //  9034: aload           4
        //  9036: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9039: aload           8
        //  9041: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9044: aload           24
        //  9046: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  9049: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.phone_number:Ljava/lang/String;
        //  9052: aload           8
        //  9054: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9057: aload           24
        //  9059: getfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //  9062: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.first_name:Ljava/lang/String;
        //  9065: aload           8
        //  9067: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9070: aload           24
        //  9072: getfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //  9075: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.last_name:Ljava/lang/String;
        //  9078: aload           8
        //  9080: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9083: aload           24
        //  9085: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //  9088: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.user_id:I
        //  9091: aload_0        
        //  9092: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  9095: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        //  9098: aload           8
        //  9100: aload           6
        //  9102: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  9105: aload           35
        //  9107: aconst_null    
        //  9108: aconst_null    
        //  9109: aload           6
        //  9111: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        //  9114: goto            9591
        //  9117: astore_2       
        //  9118: goto            8999
        //  9121: iload           34
        //  9123: bipush          7
        //  9125: if_icmpeq       9376
        //  9128: iload           34
        //  9130: bipush          9
        //  9132: if_icmpne       9143
        //  9135: aload           7
        //  9137: ifnull          9143
        //  9140: goto            9376
        //  9143: iload           34
        //  9145: bipush          8
        //  9147: if_icmpne       9591
        //  9150: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  9153: astore_3       
        //  9154: aload_3        
        //  9155: aload_0        
        //  9156: lload           42
        //  9158: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  9161: aload_3        
        //  9162: aload           35
        //  9164: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.encryptedChat:Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //  9167: aload_3        
        //  9168: aload           8
        //  9170: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        //  9173: aload_3        
        //  9174: aload           6
        //  9176: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  9179: aload_3        
        //  9180: iconst_3       
        //  9181: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  9184: aload_3        
        //  9185: aload           21
        //  9187: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  9190: aload_3        
        //  9191: iconst_1       
        //  9192: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  9195: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9198: astore          5
        //  9200: aload           5
        //  9202: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.<init>:()V
        //  9205: aload           8
        //  9207: aload           5
        //  9209: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9212: aload           8
        //  9214: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9217: aload           7
        //  9219: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  9222: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.attributes:Ljava/util/ArrayList;
        //  9225: aload           8
        //  9227: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9230: aload           29
        //  9232: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.caption:Ljava/lang/String;
        //  9235: aload_0        
        //  9236: aload           7
        //  9238: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  9241: invokespecial   org/telegram/messenger/SendMessagesHelper.getThumbForSecretChat:(Ljava/util/ArrayList;)Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9244: astore          5
        //  9246: aload           5
        //  9248: ifnull          9301
        //  9251: aload           5
        //  9253: invokestatic    org/telegram/messenger/ImageLoader.fillPhotoSizeWithBytes:(Lorg/telegram/tgnet/TLRPC$PhotoSize;)V
        //  9256: aload           8
        //  9258: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9261: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9264: aload           5
        //  9266: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  9269: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  9272: aload           8
        //  9274: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9277: aload           5
        //  9279: getfield        org/telegram/tgnet/TLRPC$PhotoSize.h:I
        //  9282: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        //  9285: aload           8
        //  9287: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9290: aload           5
        //  9292: getfield        org/telegram/tgnet/TLRPC$PhotoSize.w:I
        //  9295: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        //  9298: goto            9333
        //  9301: aload           8
        //  9303: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9306: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9309: iconst_0       
        //  9310: newarray        B
        //  9312: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  9315: aload           8
        //  9317: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9320: iconst_0       
        //  9321: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        //  9324: aload           8
        //  9326: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9329: iconst_0       
        //  9330: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        //  9333: aload           8
        //  9335: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9338: aload           7
        //  9340: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  9343: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.mime_type:Ljava/lang/String;
        //  9346: aload           8
        //  9348: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9351: aload           7
        //  9353: getfield        org/telegram/tgnet/TLRPC$Document.size:I
        //  9356: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.size:I
        //  9359: aload_3        
        //  9360: aload           4
        //  9362: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  9365: aload_0        
        //  9366: aload_3        
        //  9367: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  9370: aload_3        
        //  9371: astore          4
        //  9373: goto            9594
        //  9376: aload           7
        //  9378: invokestatic    org/telegram/messenger/MessageObject.isStickerDocument:(Lorg/telegram/tgnet/TLRPC$Document;)Z
        //  9381: ifeq            9597
        //  9384: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument;
        //  9387: astore          4
        //  9389: aload           4
        //  9391: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument.<init>:()V
        //  9394: aload           8
        //  9396: aload           4
        //  9398: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9401: aload           8
        //  9403: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9406: aload           7
        //  9408: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  9411: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.id:J
        //  9414: aload           8
        //  9416: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9419: aload           7
        //  9421: getfield        org/telegram/tgnet/TLRPC$Document.date:I
        //  9424: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.date:I
        //  9427: aload           8
        //  9429: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9432: aload           7
        //  9434: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  9437: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.access_hash:J
        //  9440: aload           8
        //  9442: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9445: aload           7
        //  9447: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  9450: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.mime_type:Ljava/lang/String;
        //  9453: aload           8
        //  9455: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9458: aload           7
        //  9460: getfield        org/telegram/tgnet/TLRPC$Document.size:I
        //  9463: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.size:I
        //  9466: aload           8
        //  9468: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9471: aload           7
        //  9473: getfield        org/telegram/tgnet/TLRPC$Document.dc_id:I
        //  9476: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.dc_id:I
        //  9479: aload           8
        //  9481: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9484: aload           7
        //  9486: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  9489: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.attributes:Ljava/util/ArrayList;
        //  9492: aload_0        
        //  9493: aload           7
        //  9495: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  9498: invokespecial   org/telegram/messenger/SendMessagesHelper.getThumbForSecretChat:(Ljava/util/ArrayList;)Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9501: astore          4
        //  9503: aload           4
        //  9505: ifnull          9524
        //  9508: aload           8
        //  9510: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9513: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument;
        //  9516: aload           4
        //  9518: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument.thumb:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9521: goto            9568
        //  9524: aload           8
        //  9526: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9529: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument;
        //  9532: astore          4
        //  9534: new             Lorg/telegram/tgnet/TLRPC$TL_photoSizeEmpty;
        //  9537: astore          5
        //  9539: aload           5
        //  9541: invokespecial   org/telegram/tgnet/TLRPC$TL_photoSizeEmpty.<init>:()V
        //  9544: aload           4
        //  9546: aload           5
        //  9548: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument.thumb:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9551: aload           8
        //  9553: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9556: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument;
        //  9559: getfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaExternalDocument.thumb:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9562: ldc_w           "s"
        //  9565: putfield        org/telegram/tgnet/TLRPC$PhotoSize.type:Ljava/lang/String;
        //  9568: aload_0        
        //  9569: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  9572: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        //  9575: aload           8
        //  9577: aload           6
        //  9579: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  9582: aload           35
        //  9584: aconst_null    
        //  9585: aconst_null    
        //  9586: aload           6
        //  9588: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        //  9591: aload_3        
        //  9592: astore          4
        //  9594: goto            10448
        //  9597: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9600: astore          5
        //  9602: aload           5
        //  9604: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.<init>:()V
        //  9607: aload           8
        //  9609: aload           5
        //  9611: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9614: aload           8
        //  9616: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9619: aload           7
        //  9621: getfield        org/telegram/tgnet/TLRPC$Document.attributes:Ljava/util/ArrayList;
        //  9624: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.attributes:Ljava/util/ArrayList;
        //  9627: aload           8
        //  9629: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9632: aload           29
        //  9634: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.caption:Ljava/lang/String;
        //  9637: aload_0        
        //  9638: aload           7
        //  9640: getfield        org/telegram/tgnet/TLRPC$Document.thumbs:Ljava/util/ArrayList;
        //  9643: invokespecial   org/telegram/messenger/SendMessagesHelper.getThumbForSecretChat:(Ljava/util/ArrayList;)Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9646: astore          5
        //  9648: aload           5
        //  9650: ifnull          9703
        //  9653: aload           5
        //  9655: invokestatic    org/telegram/messenger/ImageLoader.fillPhotoSizeWithBytes:(Lorg/telegram/tgnet/TLRPC$PhotoSize;)V
        //  9658: aload           8
        //  9660: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9663: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9666: aload           5
        //  9668: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        //  9671: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  9674: aload           8
        //  9676: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9679: aload           5
        //  9681: getfield        org/telegram/tgnet/TLRPC$PhotoSize.h:I
        //  9684: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        //  9687: aload           8
        //  9689: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9692: aload           5
        //  9694: getfield        org/telegram/tgnet/TLRPC$PhotoSize.w:I
        //  9697: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        //  9700: goto            9735
        //  9703: aload           8
        //  9705: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9708: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument;
        //  9711: iconst_0       
        //  9712: newarray        B
        //  9714: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaDocument.thumb:[B
        //  9717: aload           8
        //  9719: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9722: iconst_0       
        //  9723: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        //  9726: aload           8
        //  9728: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9731: iconst_0       
        //  9732: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        //  9735: aload           8
        //  9737: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9740: aload           7
        //  9742: getfield        org/telegram/tgnet/TLRPC$Document.size:I
        //  9745: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.size:I
        //  9748: aload           8
        //  9750: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9753: aload           7
        //  9755: getfield        org/telegram/tgnet/TLRPC$Document.mime_type:Ljava/lang/String;
        //  9758: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.mime_type:Ljava/lang/String;
        //  9761: aload           7
        //  9763: getfield        org/telegram/tgnet/TLRPC$Document.key:[B
        //  9766: ifnonnull       9892
        //  9769: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        //  9772: astore_3       
        //  9773: aload_3        
        //  9774: aload_0        
        //  9775: lload           42
        //  9777: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        //  9780: aload_3        
        //  9781: aload           4
        //  9783: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        //  9786: aload_3        
        //  9787: aload           8
        //  9789: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        //  9792: aload_3        
        //  9793: iconst_2       
        //  9794: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        //  9797: aload_3        
        //  9798: aload           6
        //  9800: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        //  9803: aload           26
        //  9805: ifnull          9834
        //  9808: aload           26
        //  9810: ldc_w           "parentObject"
        //  9813: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //  9816: ifeq            9834
        //  9819: aload_3        
        //  9820: aload           26
        //  9822: ldc_w           "parentObject"
        //  9825: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  9828: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  9831: goto            9840
        //  9834: aload_3        
        //  9835: aload           21
        //  9837: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        //  9840: aload_3        
        //  9841: aload           35
        //  9843: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.encryptedChat:Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        //  9846: aload_3        
        //  9847: iconst_1       
        //  9848: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        //  9851: aload           12
        //  9853: ifnull          9881
        //  9856: aload           12
        //  9858: invokevirtual   java/lang/String.length:()I
        //  9861: ifle            9881
        //  9864: aload           12
        //  9866: ldc_w           "http"
        //  9869: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  9872: ifeq            9881
        //  9875: aload_3        
        //  9876: aload           12
        //  9878: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocation:Ljava/lang/String;
        //  9881: aload_0        
        //  9882: aload_3        
        //  9883: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        //  9886: aload_3        
        //  9887: astore          4
        //  9889: goto            9594
        //  9892: new             Lorg/telegram/tgnet/TLRPC$TL_inputEncryptedFile;
        //  9895: astore          4
        //  9897: aload           4
        //  9899: invokespecial   org/telegram/tgnet/TLRPC$TL_inputEncryptedFile.<init>:()V
        //  9902: aload           4
        //  9904: aload           7
        //  9906: getfield        org/telegram/tgnet/TLRPC$Document.id:J
        //  9909: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.id:J
        //  9912: aload           4
        //  9914: aload           7
        //  9916: getfield        org/telegram/tgnet/TLRPC$Document.access_hash:J
        //  9919: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.access_hash:J
        //  9922: aload           8
        //  9924: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9927: aload           7
        //  9929: getfield        org/telegram/tgnet/TLRPC$Document.key:[B
        //  9932: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.key:[B
        //  9935: aload           8
        //  9937: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        //  9940: aload           7
        //  9942: getfield        org/telegram/tgnet/TLRPC$Document.iv:[B
        //  9945: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.iv:[B
        //  9948: aload_0        
        //  9949: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        //  9952: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        //  9955: aload           8
        //  9957: aload           6
        //  9959: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  9962: aload           35
        //  9964: aload           4
        //  9966: aconst_null    
        //  9967: aload           6
        //  9969: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        //  9972: goto            9591
        //  9975: lload           40
        //  9977: lstore          42
        //  9979: aload           18
        //  9981: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  9984: iconst_0       
        //  9985: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  9988: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        //  9991: astore          5
        //  9993: aload           18
        //  9995: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        //  9998: aload           18
        // 10000: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        // 10003: invokevirtual   java/util/ArrayList.size:()I
        // 10006: iconst_1       
        // 10007: isub           
        // 10008: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        // 10011: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        // 10014: astore          4
        // 10016: aload           5
        // 10018: invokestatic    org/telegram/messenger/ImageLoader.fillPhotoSizeWithBytes:(Lorg/telegram/tgnet/TLRPC$PhotoSize;)V
        // 10021: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto;
        // 10024: astore          7
        // 10026: aload           7
        // 10028: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto.<init>:()V
        // 10031: aload           8
        // 10033: aload           7
        // 10035: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10038: aload           8
        // 10040: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10043: aload           29
        // 10045: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.caption:Ljava/lang/String;
        // 10048: aload           5
        // 10050: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        // 10053: astore          7
        // 10055: aload           7
        // 10057: ifnull          10079
        // 10060: aload           8
        // 10062: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10065: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto;
        // 10068: aload           5
        // 10070: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
        // 10073: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto.thumb:[B
        // 10076: goto            10093
        // 10079: aload           8
        // 10081: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10084: checkcast       Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto;
        // 10087: iconst_0       
        // 10088: newarray        B
        // 10090: putfield        org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaPhoto.thumb:[B
        // 10093: aload           8
        // 10095: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10098: aload           5
        // 10100: getfield        org/telegram/tgnet/TLRPC$PhotoSize.h:I
        // 10103: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_h:I
        // 10106: aload           8
        // 10108: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10111: aload           5
        // 10113: getfield        org/telegram/tgnet/TLRPC$PhotoSize.w:I
        // 10116: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.thumb_w:I
        // 10119: aload           8
        // 10121: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10124: aload           4
        // 10126: getfield        org/telegram/tgnet/TLRPC$PhotoSize.w:I
        // 10129: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.w:I
        // 10132: aload           8
        // 10134: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10137: aload           4
        // 10139: getfield        org/telegram/tgnet/TLRPC$PhotoSize.h:I
        // 10142: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.h:I
        // 10145: aload           8
        // 10147: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10150: aload           4
        // 10152: getfield        org/telegram/tgnet/TLRPC$PhotoSize.size:I
        // 10155: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.size:I
        // 10158: aload           4
        // 10160: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        // 10163: getfield        org/telegram/tgnet/TLRPC$FileLocation.key:[B
        // 10166: astore          5
        // 10168: aload           5
        // 10170: ifnull          10281
        // 10173: lload           42
        // 10175: lconst_0       
        // 10176: lcmp           
        // 10177: ifeq            10183
        // 10180: goto            10281
        // 10183: new             Lorg/telegram/tgnet/TLRPC$TL_inputEncryptedFile;
        // 10186: astore          5
        // 10188: aload           5
        // 10190: invokespecial   org/telegram/tgnet/TLRPC$TL_inputEncryptedFile.<init>:()V
        // 10193: aload           5
        // 10195: aload           4
        // 10197: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        // 10200: getfield        org/telegram/tgnet/TLRPC$FileLocation.volume_id:J
        // 10203: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.id:J
        // 10206: aload           5
        // 10208: aload           4
        // 10210: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        // 10213: getfield        org/telegram/tgnet/TLRPC$FileLocation.secret:J
        // 10216: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.access_hash:J
        // 10219: aload           8
        // 10221: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10224: aload           4
        // 10226: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        // 10229: getfield        org/telegram/tgnet/TLRPC$FileLocation.key:[B
        // 10232: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.key:[B
        // 10235: aload           8
        // 10237: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 10240: aload           4
        // 10242: getfield        org/telegram/tgnet/TLRPC$PhotoSize.location:Lorg/telegram/tgnet/TLRPC$FileLocation;
        // 10245: getfield        org/telegram/tgnet/TLRPC$FileLocation.iv:[B
        // 10248: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.iv:[B
        // 10251: aload_0        
        // 10252: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 10255: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        // 10258: aload           8
        // 10260: aload           6
        // 10262: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10265: aload           35
        // 10267: aload           5
        // 10269: aconst_null    
        // 10270: aload           6
        // 10272: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        // 10275: aload_3        
        // 10276: astore          4
        // 10278: goto            10448
        // 10281: aload_3        
        // 10282: ifnonnull       10370
        // 10285: new             Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;
        // 10288: astore_3       
        // 10289: aload_3        
        // 10290: aload_0        
        // 10291: lload           10
        // 10293: invokespecial   org/telegram/messenger/SendMessagesHelper$DelayedMessage.<init>:(Lorg/telegram/messenger/SendMessagesHelper;J)V
        // 10296: aload_3        
        // 10297: aload           35
        // 10299: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.encryptedChat:Lorg/telegram/tgnet/TLRPC$EncryptedChat;
        // 10302: aload_3        
        // 10303: iconst_0       
        // 10304: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.type:I
        // 10307: aload_3        
        // 10308: aload           19
        // 10310: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPath:Ljava/lang/String;
        // 10313: aload_3        
        // 10314: aload           8
        // 10316: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        // 10319: aload_3        
        // 10320: aload           6
        // 10322: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.obj:Lorg/telegram/messenger/MessageObject;
        // 10325: aload           26
        // 10327: ifnull          10356
        // 10330: aload           26
        // 10332: ldc_w           "parentObject"
        // 10335: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        // 10338: ifeq            10356
        // 10341: aload_3        
        // 10342: aload           26
        // 10344: ldc_w           "parentObject"
        // 10347: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        // 10350: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        // 10353: goto            10362
        // 10356: aload_3        
        // 10357: aload           21
        // 10359: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.parentObject:Ljava/lang/Object;
        // 10362: aload_3        
        // 10363: iconst_1       
        // 10364: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        // 10367: goto            10370
        // 10370: aload           12
        // 10372: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        // 10375: istore          15
        // 10377: iload           15
        // 10379: ifne            10402
        // 10382: aload           12
        // 10384: ldc_w           "http"
        // 10387: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        // 10390: ifeq            10402
        // 10393: aload_3        
        // 10394: aload           12
        // 10396: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.httpLocation:Ljava/lang/String;
        // 10399: goto            10433
        // 10402: aload_3        
        // 10403: aload           18
        // 10405: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        // 10408: aload           18
        // 10410: getfield        org/telegram/tgnet/TLRPC$Photo.sizes:Ljava/util/ArrayList;
        // 10413: invokevirtual   java/util/ArrayList.size:()I
        // 10416: iconst_1       
        // 10417: isub           
        // 10418: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        // 10421: checkcast       Lorg/telegram/tgnet/TLRPC$PhotoSize;
        // 10424: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
        // 10427: aload_3        
        // 10428: aload           18
        // 10430: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.locationParent:Lorg/telegram/tgnet/TLObject;
        // 10433: lload           42
        // 10435: lconst_0       
        // 10436: lcmp           
        // 10437: ifne            10445
        // 10440: aload_0        
        // 10441: aload_3        
        // 10442: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        // 10445: aload_3        
        // 10446: astore          4
        // 10448: aload_1        
        // 10449: astore          5
        // 10451: lload           40
        // 10453: lconst_0       
        // 10454: lcmp           
        // 10455: ifeq            10622
        // 10458: aload           4
        // 10460: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        // 10463: astore_3       
        // 10464: aload_3        
        // 10465: ifnull          10481
        // 10468: aload           4
        // 10470: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        // 10473: checkcast       Lorg/telegram/tgnet/TLRPC$TL_messages_sendEncryptedMultiMedia;
        // 10476: astore          7
        // 10478: goto            10498
        // 10481: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendEncryptedMultiMedia;
        // 10484: astore          7
        // 10486: aload           7
        // 10488: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendEncryptedMultiMedia.<init>:()V
        // 10491: aload           4
        // 10493: aload           7
        // 10495: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.sendEncryptedRequest:Lorg/telegram/tgnet/TLObject;
        // 10498: aload           4
        // 10500: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.messageObjects:Ljava/util/ArrayList;
        // 10503: aload           6
        // 10505: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10508: pop            
        // 10509: aload           4
        // 10511: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.messages:Ljava/util/ArrayList;
        // 10514: astore          9
        // 10516: aload           5
        // 10518: astore_1       
        // 10519: aload_1        
        // 10520: astore_3       
        // 10521: aload           9
        // 10523: aload_1        
        // 10524: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10527: pop            
        // 10528: aload_1        
        // 10529: astore_3       
        // 10530: aload           4
        // 10532: getfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.originalPaths:Ljava/util/ArrayList;
        // 10535: aload           19
        // 10537: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10540: pop            
        // 10541: aload_1        
        // 10542: astore_3       
        // 10543: aload           4
        // 10545: iconst_1       
        // 10546: putfield        org/telegram/messenger/SendMessagesHelper$DelayedMessage.performMediaUpload:Z
        // 10549: aload_1        
        // 10550: astore_3       
        // 10551: aload           7
        // 10553: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendEncryptedMultiMedia.messages:Ljava/util/ArrayList;
        // 10556: aload           8
        // 10558: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10561: pop            
        // 10562: aload_1        
        // 10563: astore_3       
        // 10564: new             Lorg/telegram/tgnet/TLRPC$TL_inputEncryptedFile;
        // 10567: astore          6
        // 10569: aload_1        
        // 10570: astore_3       
        // 10571: aload           6
        // 10573: invokespecial   org/telegram/tgnet/TLRPC$TL_inputEncryptedFile.<init>:()V
        // 10576: lload           27
        // 10578: lstore          40
        // 10580: iload           34
        // 10582: iconst_3       
        // 10583: if_icmpne       10589
        // 10586: lconst_1       
        // 10587: lstore          40
        // 10589: aload_1        
        // 10590: astore_3       
        // 10591: aload           6
        // 10593: lload           40
        // 10595: putfield        org/telegram/tgnet/TLRPC$InputEncryptedFile.id:J
        // 10598: aload_1        
        // 10599: astore_3       
        // 10600: aload           7
        // 10602: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendEncryptedMultiMedia.files:Ljava/util/ArrayList;
        // 10605: aload           6
        // 10607: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10610: pop            
        // 10611: aload_1        
        // 10612: astore_3       
        // 10613: aload_0        
        // 10614: aload           4
        // 10616: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendDelayedMessage:(Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;)V
        // 10619: goto            10622
        // 10622: aload           5
        // 10624: astore_3       
        // 10625: aload           16
        // 10627: ifnonnull       12210
        // 10630: aload_0        
        // 10631: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 10634: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        // 10637: lload           10
        // 10639: iconst_0       
        // 10640: invokevirtual   org/telegram/messenger/DataQuery.cleanDraft:(JZ)V
        // 10643: goto            12210
        // 10646: astore_3       
        // 10647: aload_1        
        // 10648: astore_2       
        // 10649: aload_3        
        // 10650: astore_1       
        // 10651: aload           6
        // 10653: astore          4
        // 10655: goto            12128
        // 10658: astore_1       
        // 10659: aload           14
        // 10661: astore_2       
        // 10662: aload           6
        // 10664: astore          4
        // 10666: goto            12128
        // 10669: aload_2        
        // 10670: astore          4
        // 10672: iload           34
        // 10674: iconst_4       
        // 10675: if_icmpne       11068
        // 10678: aload_1        
        // 10679: astore_3       
        // 10680: new             Lorg/telegram/tgnet/TLRPC$TL_messages_forwardMessages;
        // 10683: astore          5
        // 10685: aload_1        
        // 10686: astore_3       
        // 10687: aload           5
        // 10689: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.<init>:()V
        // 10692: aload_1        
        // 10693: astore_3       
        // 10694: aload           5
        // 10696: aload           33
        // 10698: putfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.to_peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 10701: aload_1        
        // 10702: astore_3       
        // 10703: aload           5
        // 10705: aload           16
        // 10707: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10710: getfield        org/telegram/tgnet/TLRPC$Message.with_my_score:Z
        // 10713: putfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.with_my_score:Z
        // 10716: aload_1        
        // 10717: astore_3       
        // 10718: aload           16
        // 10720: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10723: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        // 10726: ifeq            10820
        // 10729: aload_1        
        // 10730: astore_3       
        // 10731: aload_0        
        // 10732: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 10735: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        // 10738: aload           16
        // 10740: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10743: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        // 10746: ineg           
        // 10747: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        // 10750: invokevirtual   org/telegram/messenger/MessagesController.getChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$Chat;
        // 10753: astore          6
        // 10755: aload_1        
        // 10756: astore_3       
        // 10757: new             Lorg/telegram/tgnet/TLRPC$TL_inputPeerChannel;
        // 10760: astore          7
        // 10762: aload_1        
        // 10763: astore_3       
        // 10764: aload           7
        // 10766: invokespecial   org/telegram/tgnet/TLRPC$TL_inputPeerChannel.<init>:()V
        // 10769: aload_1        
        // 10770: astore_3       
        // 10771: aload           5
        // 10773: aload           7
        // 10775: putfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.from_peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 10778: aload_1        
        // 10779: astore_3       
        // 10780: aload           5
        // 10782: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.from_peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 10785: aload           16
        // 10787: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10790: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        // 10793: ineg           
        // 10794: putfield        org/telegram/tgnet/TLRPC$InputPeer.channel_id:I
        // 10797: aload           6
        // 10799: ifnull          10843
        // 10802: aload_1        
        // 10803: astore_3       
        // 10804: aload           5
        // 10806: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.from_peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 10809: aload           6
        // 10811: getfield        org/telegram/tgnet/TLRPC$Chat.access_hash:J
        // 10814: putfield        org/telegram/tgnet/TLRPC$InputPeer.access_hash:J
        // 10817: goto            10843
        // 10820: aload_1        
        // 10821: astore_3       
        // 10822: new             Lorg/telegram/tgnet/TLRPC$TL_inputPeerEmpty;
        // 10825: astore          6
        // 10827: aload_1        
        // 10828: astore_3       
        // 10829: aload           6
        // 10831: invokespecial   org/telegram/tgnet/TLRPC$TL_inputPeerEmpty.<init>:()V
        // 10834: aload_1        
        // 10835: astore_3       
        // 10836: aload           5
        // 10838: aload           6
        // 10840: putfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.from_peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 10843: aload_1        
        // 10844: astore_3       
        // 10845: aload           16
        // 10847: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10850: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        // 10853: instanceof      Lorg/telegram/tgnet/TLRPC$TL_peerChannel;
        // 10856: ifeq            10925
        // 10859: aload_1        
        // 10860: astore_3       
        // 10861: aload_0        
        // 10862: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 10865: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        // 10868: astore          7
        // 10870: aload_1        
        // 10871: astore_3       
        // 10872: new             Ljava/lang/StringBuilder;
        // 10875: astore          6
        // 10877: aload_1        
        // 10878: astore_3       
        // 10879: aload           6
        // 10881: invokespecial   java/lang/StringBuilder.<init>:()V
        // 10884: aload_1        
        // 10885: astore_3       
        // 10886: aload           6
        // 10888: ldc_w           "silent_"
        // 10891: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 10894: pop            
        // 10895: aload_1        
        // 10896: astore_3       
        // 10897: aload           6
        // 10899: lload           10
        // 10901: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        // 10904: pop            
        // 10905: aload_1        
        // 10906: astore_3       
        // 10907: aload           5
        // 10909: aload           7
        // 10911: aload           6
        // 10913: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        // 10916: iconst_0       
        // 10917: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        // 10922: putfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.silent:Z
        // 10925: aload_1        
        // 10926: astore_3       
        // 10927: aload           5
        // 10929: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.random_id:Ljava/util/ArrayList;
        // 10932: aload_1        
        // 10933: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        // 10936: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        // 10939: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10942: pop            
        // 10943: aload_1        
        // 10944: astore_3       
        // 10945: aload           16
        // 10947: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        // 10950: iflt            10975
        // 10953: aload_1        
        // 10954: astore_3       
        // 10955: aload           5
        // 10957: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.id:Ljava/util/ArrayList;
        // 10960: aload           16
        // 10962: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        // 10965: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        // 10968: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 10971: pop            
        // 10972: goto            11051
        // 10975: aload_1        
        // 10976: astore_3       
        // 10977: aload           16
        // 10979: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 10982: getfield        org/telegram/tgnet/TLRPC$Message.fwd_msg_id:I
        // 10985: ifeq            11013
        // 10988: aload_1        
        // 10989: astore_3       
        // 10990: aload           5
        // 10992: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.id:Ljava/util/ArrayList;
        // 10995: aload           16
        // 10997: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 11000: getfield        org/telegram/tgnet/TLRPC$Message.fwd_msg_id:I
        // 11003: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        // 11006: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 11009: pop            
        // 11010: goto            11051
        // 11013: aload_1        
        // 11014: astore_3       
        // 11015: aload           16
        // 11017: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 11020: getfield        org/telegram/tgnet/TLRPC$Message.fwd_from:Lorg/telegram/tgnet/TLRPC$MessageFwdHeader;
        // 11023: ifnull          11051
        // 11026: aload_1        
        // 11027: astore_3       
        // 11028: aload           5
        // 11030: getfield        org/telegram/tgnet/TLRPC$TL_messages_forwardMessages.id:Ljava/util/ArrayList;
        // 11033: aload           16
        // 11035: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 11038: getfield        org/telegram/tgnet/TLRPC$Message.fwd_from:Lorg/telegram/tgnet/TLRPC$MessageFwdHeader;
        // 11041: getfield        org/telegram/tgnet/TLRPC$MessageFwdHeader.channel_post:I
        // 11044: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        // 11047: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 11050: pop            
        // 11051: aload_1        
        // 11052: astore_3       
        // 11053: aload_0        
        // 11054: aload           5
        // 11056: aload           4
        // 11058: aconst_null    
        // 11059: aconst_null    
        // 11060: aload           21
        // 11062: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        // 11065: goto            12210
        // 11068: iload           34
        // 11070: bipush          9
        // 11072: if_icmpne       12210
        // 11075: aload_1        
        // 11076: astore_3       
        // 11077: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult;
        // 11080: astore          5
        // 11082: aload_1        
        // 11083: astore_3       
        // 11084: aload           5
        // 11086: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.<init>:()V
        // 11089: aload_1        
        // 11090: astore_3       
        // 11091: aload           5
        // 11093: aload           33
        // 11095: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 11098: aload_1        
        // 11099: astore_3       
        // 11100: aload           5
        // 11102: aload_1        
        // 11103: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        // 11106: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.random_id:J
        // 11109: aload_1        
        // 11110: astore_3       
        // 11111: aload           26
        // 11113: ldc_w           "bot"
        // 11116: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        // 11119: ifne            11128
        // 11122: iconst_1       
        // 11123: istore          15
        // 11125: goto            11131
        // 11128: iconst_0       
        // 11129: istore          15
        // 11131: aload_1        
        // 11132: astore_3       
        // 11133: aload           5
        // 11135: iload           15
        // 11137: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.hide_via:Z
        // 11140: aload_1        
        // 11141: astore_3       
        // 11142: aload_1        
        // 11143: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        // 11146: ifeq            11174
        // 11149: aload_1        
        // 11150: astore_3       
        // 11151: aload           5
        // 11153: aload           5
        // 11155: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.flags:I
        // 11158: iconst_1       
        // 11159: ior            
        // 11160: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.flags:I
        // 11163: aload_1        
        // 11164: astore_3       
        // 11165: aload           5
        // 11167: aload_1        
        // 11168: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        // 11171: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.reply_to_msg_id:I
        // 11174: aload_1        
        // 11175: astore_3       
        // 11176: aload_1        
        // 11177: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        // 11180: instanceof      Lorg/telegram/tgnet/TLRPC$TL_peerChannel;
        // 11183: ifeq            11252
        // 11186: aload_1        
        // 11187: astore_3       
        // 11188: aload_0        
        // 11189: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 11192: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        // 11195: astore          6
        // 11197: aload_1        
        // 11198: astore_3       
        // 11199: new             Ljava/lang/StringBuilder;
        // 11202: astore          7
        // 11204: aload_1        
        // 11205: astore_3       
        // 11206: aload           7
        // 11208: invokespecial   java/lang/StringBuilder.<init>:()V
        // 11211: aload_1        
        // 11212: astore_3       
        // 11213: aload           7
        // 11215: ldc_w           "silent_"
        // 11218: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 11221: pop            
        // 11222: aload_1        
        // 11223: astore_3       
        // 11224: aload           7
        // 11226: lload           10
        // 11228: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        // 11231: pop            
        // 11232: aload_1        
        // 11233: astore_3       
        // 11234: aload           5
        // 11236: aload           6
        // 11238: aload           7
        // 11240: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        // 11243: iconst_0       
        // 11244: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        // 11249: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.silent:Z
        // 11252: aload_1        
        // 11253: astore_3       
        // 11254: aload           5
        // 11256: aload           26
        // 11258: ldc_w           "query_id"
        // 11261: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        // 11264: checkcast       Ljava/lang/String;
        // 11267: invokestatic    org/telegram/messenger/Utilities.parseLong:(Ljava/lang/String;)Ljava/lang/Long;
        // 11270: invokevirtual   java/lang/Long.longValue:()J
        // 11273: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.query_id:J
        // 11276: aload_1        
        // 11277: astore_3       
        // 11278: aload           5
        // 11280: aload           26
        // 11282: ldc_w           "id"
        // 11285: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        // 11288: checkcast       Ljava/lang/String;
        // 11291: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.id:Ljava/lang/String;
        // 11294: aload           16
        // 11296: ifnonnull       11322
        // 11299: aload_1        
        // 11300: astore_3       
        // 11301: aload           5
        // 11303: iconst_1       
        // 11304: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendInlineBotResult.clear_draft:Z
        // 11307: aload_1        
        // 11308: astore_3       
        // 11309: aload_0        
        // 11310: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 11313: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        // 11316: lload           10
        // 11318: iconst_0       
        // 11319: invokevirtual   org/telegram/messenger/DataQuery.cleanDraft:(JZ)V
        // 11322: aload_1        
        // 11323: astore_3       
        // 11324: aload_0        
        // 11325: aload           5
        // 11327: aload           4
        // 11329: aconst_null    
        // 11330: aconst_null    
        // 11331: aload           21
        // 11333: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        // 11336: goto            12210
        // 11339: aload_2        
        // 11340: astore          5
        // 11342: aload           35
        // 11344: ifnonnull       11752
        // 11347: aload           25
        // 11349: ifnull          11485
        // 11352: aload_1        
        // 11353: astore_3       
        // 11354: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendBroadcast;
        // 11357: astore          6
        // 11359: aload_1        
        // 11360: astore_3       
        // 11361: aload           6
        // 11363: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.<init>:()V
        // 11366: aload_1        
        // 11367: astore_3       
        // 11368: new             Ljava/util/ArrayList;
        // 11371: astore          4
        // 11373: aload_1        
        // 11374: astore_3       
        // 11375: aload           4
        // 11377: invokespecial   java/util/ArrayList.<init>:()V
        // 11380: iconst_0       
        // 11381: istore          20
        // 11383: aload_1        
        // 11384: astore_3       
        // 11385: iload           20
        // 11387: aload           25
        // 11389: invokevirtual   java/util/ArrayList.size:()I
        // 11392: if_icmpge       11418
        // 11395: aload_1        
        // 11396: astore_3       
        // 11397: aload           4
        // 11399: getstatic       org/telegram/messenger/Utilities.random:Ljava/security/SecureRandom;
        // 11402: invokevirtual   java/security/SecureRandom.nextLong:()J
        // 11405: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        // 11408: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        // 11411: pop            
        // 11412: iinc            20, 1
        // 11415: goto            11383
        // 11418: aload_1        
        // 11419: astore_3       
        // 11420: aload           6
        // 11422: aload           8
        // 11424: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.message:Ljava/lang/String;
        // 11427: aload_1        
        // 11428: astore_3       
        // 11429: aload           6
        // 11431: aload           25
        // 11433: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.contacts:Ljava/util/ArrayList;
        // 11436: aload_1        
        // 11437: astore_3       
        // 11438: new             Lorg/telegram/tgnet/TLRPC$TL_inputMediaEmpty;
        // 11441: astore          7
        // 11443: aload_1        
        // 11444: astore_3       
        // 11445: aload           7
        // 11447: invokespecial   org/telegram/tgnet/TLRPC$TL_inputMediaEmpty.<init>:()V
        // 11450: aload_1        
        // 11451: astore_3       
        // 11452: aload           6
        // 11454: aload           7
        // 11456: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.media:Lorg/telegram/tgnet/TLRPC$InputMedia;
        // 11459: aload_1        
        // 11460: astore_3       
        // 11461: aload           6
        // 11463: aload           4
        // 11465: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendBroadcast.random_id:Ljava/util/ArrayList;
        // 11468: aload_1        
        // 11469: astore_3       
        // 11470: aload_0        
        // 11471: aload           6
        // 11473: aload           5
        // 11475: aconst_null    
        // 11476: aconst_null    
        // 11477: aload           21
        // 11479: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        // 11482: goto            12210
        // 11485: aload_1        
        // 11486: astore_3       
        // 11487: new             Lorg/telegram/tgnet/TLRPC$TL_messages_sendMessage;
        // 11490: astore          4
        // 11492: aload_1        
        // 11493: astore_3       
        // 11494: aload           4
        // 11496: invokespecial   org/telegram/tgnet/TLRPC$TL_messages_sendMessage.<init>:()V
        // 11499: aload_1        
        // 11500: astore_3       
        // 11501: aload           4
        // 11503: aload           8
        // 11505: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.message:Ljava/lang/String;
        // 11508: aload           16
        // 11510: ifnonnull       11519
        // 11513: iconst_1       
        // 11514: istore          38
        // 11516: goto            11522
        // 11519: iconst_0       
        // 11520: istore          38
        // 11522: aload_1        
        // 11523: astore_3       
        // 11524: aload           4
        // 11526: iload           38
        // 11528: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.clear_draft:Z
        // 11531: aload_1        
        // 11532: astore_3       
        // 11533: aload_1        
        // 11534: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        // 11537: instanceof      Lorg/telegram/tgnet/TLRPC$TL_peerChannel;
        // 11540: ifeq            11609
        // 11543: aload_1        
        // 11544: astore_3       
        // 11545: aload_0        
        // 11546: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 11549: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        // 11552: astore          6
        // 11554: aload_1        
        // 11555: astore_3       
        // 11556: new             Ljava/lang/StringBuilder;
        // 11559: astore          7
        // 11561: aload_1        
        // 11562: astore_3       
        // 11563: aload           7
        // 11565: invokespecial   java/lang/StringBuilder.<init>:()V
        // 11568: aload_1        
        // 11569: astore_3       
        // 11570: aload           7
        // 11572: ldc_w           "silent_"
        // 11575: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        // 11578: pop            
        // 11579: aload_1        
        // 11580: astore_3       
        // 11581: aload           7
        // 11583: lload           10
        // 11585: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        // 11588: pop            
        // 11589: aload_1        
        // 11590: astore_3       
        // 11591: aload           4
        // 11593: aload           6
        // 11595: aload           7
        // 11597: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        // 11600: iconst_0       
        // 11601: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        // 11606: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.silent:Z
        // 11609: aload_1        
        // 11610: astore_3       
        // 11611: aload           4
        // 11613: aload           33
        // 11615: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.peer:Lorg/telegram/tgnet/TLRPC$InputPeer;
        // 11618: aload_1        
        // 11619: astore_3       
        // 11620: aload           4
        // 11622: aload_1        
        // 11623: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        // 11626: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.random_id:J
        // 11629: aload_1        
        // 11630: astore_3       
        // 11631: aload_1        
        // 11632: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        // 11635: ifeq            11663
        // 11638: aload_1        
        // 11639: astore_3       
        // 11640: aload           4
        // 11642: aload           4
        // 11644: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.flags:I
        // 11647: iconst_1       
        // 11648: ior            
        // 11649: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.flags:I
        // 11652: aload_1        
        // 11653: astore_3       
        // 11654: aload           4
        // 11656: aload_1        
        // 11657: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_msg_id:I
        // 11660: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.reply_to_msg_id:I
        // 11663: iload           15
        // 11665: ifne            11676
        // 11668: aload_1        
        // 11669: astore_3       
        // 11670: aload           4
        // 11672: iconst_1       
        // 11673: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.no_webpage:Z
        // 11676: aload           17
        // 11678: ifnull          11715
        // 11681: aload_1        
        // 11682: astore_3       
        // 11683: aload           17
        // 11685: invokevirtual   java/util/ArrayList.isEmpty:()Z
        // 11688: ifne            11715
        // 11691: aload_1        
        // 11692: astore_3       
        // 11693: aload           4
        // 11695: aload           17
        // 11697: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.entities:Ljava/util/ArrayList;
        // 11700: aload_1        
        // 11701: astore_3       
        // 11702: aload           4
        // 11704: aload           4
        // 11706: getfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.flags:I
        // 11709: bipush          8
        // 11711: ior            
        // 11712: putfield        org/telegram/tgnet/TLRPC$TL_messages_sendMessage.flags:I
        // 11715: aload_1        
        // 11716: astore_3       
        // 11717: aload_0        
        // 11718: aload           4
        // 11720: aload           5
        // 11722: aconst_null    
        // 11723: aconst_null    
        // 11724: aload           21
        // 11726: invokespecial   org/telegram/messenger/SendMessagesHelper.performSendMessageRequest:(Lorg/telegram/tgnet/TLObject;Lorg/telegram/messenger/MessageObject;Ljava/lang/String;Lorg/telegram/messenger/SendMessagesHelper$DelayedMessage;Ljava/lang/Object;)V
        // 11729: aload           16
        // 11731: ifnonnull       12210
        // 11734: aload_1        
        // 11735: astore_3       
        // 11736: aload_0        
        // 11737: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 11740: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        // 11743: lload           10
        // 11745: iconst_0       
        // 11746: invokevirtual   org/telegram/messenger/DataQuery.cleanDraft:(JZ)V
        // 11749: goto            12210
        // 11752: aload_1        
        // 11753: astore_3       
        // 11754: aload           35
        // 11756: getfield        org/telegram/tgnet/TLRPC$EncryptedChat.layer:I
        // 11759: invokestatic    org/telegram/messenger/AndroidUtilities.getPeerLayerVersion:(I)I
        // 11762: bipush          73
        // 11764: if_icmplt       11784
        // 11767: aload_1        
        // 11768: astore_3       
        // 11769: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessage;
        // 11772: astore          4
        // 11774: aload_1        
        // 11775: astore_3       
        // 11776: aload           4
        // 11778: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessage.<init>:()V
        // 11781: goto            11795
        // 11784: aload_1        
        // 11785: astore_3       
        // 11786: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessage_layer45;
        // 11789: dup            
        // 11790: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessage_layer45.<init>:()V
        // 11793: astore          4
        // 11795: aload_1        
        // 11796: astore_3       
        // 11797: aload           4
        // 11799: aload_1        
        // 11800: getfield        org/telegram/tgnet/TLRPC$Message.ttl:I
        // 11803: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.ttl:I
        // 11806: aload           17
        // 11808: ifnull          11846
        // 11811: aload_1        
        // 11812: astore_3       
        // 11813: aload           17
        // 11815: invokevirtual   java/util/ArrayList.isEmpty:()Z
        // 11818: ifne            11846
        // 11821: aload_1        
        // 11822: astore_3       
        // 11823: aload           4
        // 11825: aload           17
        // 11827: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.entities:Ljava/util/ArrayList;
        // 11830: aload_1        
        // 11831: astore_3       
        // 11832: aload           4
        // 11834: aload           4
        // 11836: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11839: sipush          128
        // 11842: ior            
        // 11843: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11846: aload_1        
        // 11847: astore_3       
        // 11848: aload_1        
        // 11849: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_random_id:J
        // 11852: lconst_0       
        // 11853: lcmp           
        // 11854: ifeq            11883
        // 11857: aload_1        
        // 11858: astore_3       
        // 11859: aload           4
        // 11861: aload_1        
        // 11862: getfield        org/telegram/tgnet/TLRPC$Message.reply_to_random_id:J
        // 11865: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.reply_to_random_id:J
        // 11868: aload_1        
        // 11869: astore_3       
        // 11870: aload           4
        // 11872: aload           4
        // 11874: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11877: bipush          8
        // 11879: ior            
        // 11880: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11883: aload           26
        // 11885: ifnull          11935
        // 11888: aload_1        
        // 11889: astore_3       
        // 11890: aload           26
        // 11892: ldc_w           "bot_name"
        // 11895: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        // 11898: ifnull          11935
        // 11901: aload_1        
        // 11902: astore_3       
        // 11903: aload           4
        // 11905: aload           26
        // 11907: ldc_w           "bot_name"
        // 11910: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        // 11913: checkcast       Ljava/lang/String;
        // 11916: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.via_bot_name:Ljava/lang/String;
        // 11919: aload_1        
        // 11920: astore_3       
        // 11921: aload           4
        // 11923: aload           4
        // 11925: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11928: sipush          2048
        // 11931: ior            
        // 11932: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 11935: aload_1        
        // 11936: astore_3       
        // 11937: aload           4
        // 11939: aload_1        
        // 11940: getfield        org/telegram/tgnet/TLRPC$Message.random_id:J
        // 11943: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.random_id:J
        // 11946: aload_1        
        // 11947: astore_3       
        // 11948: aload           4
        // 11950: aload           8
        // 11952: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.message:Ljava/lang/String;
        // 11955: aload           22
        // 11957: ifnull          12027
        // 11960: aload_1        
        // 11961: astore_3       
        // 11962: aload           22
        // 11964: getfield        org/telegram/tgnet/TLRPC$WebPage.url:Ljava/lang/String;
        // 11967: ifnull          12027
        // 11970: aload_1        
        // 11971: astore_3       
        // 11972: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaWebPage;
        // 11975: astore          6
        // 11977: aload_1        
        // 11978: astore_3       
        // 11979: aload           6
        // 11981: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaWebPage.<init>:()V
        // 11984: aload_1        
        // 11985: astore_3       
        // 11986: aload           4
        // 11988: aload           6
        // 11990: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 11993: aload_1        
        // 11994: astore_3       
        // 11995: aload           4
        // 11997: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 12000: aload           22
        // 12002: getfield        org/telegram/tgnet/TLRPC$WebPage.url:Ljava/lang/String;
        // 12005: putfield        org/telegram/tgnet/TLRPC$DecryptedMessageMedia.url:Ljava/lang/String;
        // 12008: aload_1        
        // 12009: astore_3       
        // 12010: aload           4
        // 12012: aload           4
        // 12014: getfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 12017: sipush          512
        // 12020: ior            
        // 12021: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.flags:I
        // 12024: goto            12050
        // 12027: aload_1        
        // 12028: astore_3       
        // 12029: new             Lorg/telegram/tgnet/TLRPC$TL_decryptedMessageMediaEmpty;
        // 12032: astore          6
        // 12034: aload_1        
        // 12035: astore_3       
        // 12036: aload           6
        // 12038: invokespecial   org/telegram/tgnet/TLRPC$TL_decryptedMessageMediaEmpty.<init>:()V
        // 12041: aload_1        
        // 12042: astore_3       
        // 12043: aload           4
        // 12045: aload           6
        // 12047: putfield        org/telegram/tgnet/TLRPC$DecryptedMessage.media:Lorg/telegram/tgnet/TLRPC$DecryptedMessageMedia;
        // 12050: aload_1        
        // 12051: astore_3       
        // 12052: aload_0        
        // 12053: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 12056: invokestatic    org/telegram/messenger/SecretChatHelper.getInstance:(I)Lorg/telegram/messenger/SecretChatHelper;
        // 12059: aload           4
        // 12061: aload           5
        // 12063: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 12066: aload           35
        // 12068: aconst_null    
        // 12069: aconst_null    
        // 12070: aload           5
        // 12072: invokevirtual   org/telegram/messenger/SecretChatHelper.performSendEncryptedRequest:(Lorg/telegram/tgnet/TLRPC$DecryptedMessage;Lorg/telegram/tgnet/TLRPC$Message;Lorg/telegram/tgnet/TLRPC$EncryptedChat;Lorg/telegram/tgnet/TLRPC$InputEncryptedFile;Ljava/lang/String;Lorg/telegram/messenger/MessageObject;)V
        // 12075: aload           16
        // 12077: ifnonnull       12210
        // 12080: aload_1        
        // 12081: astore_3       
        // 12082: aload_0        
        // 12083: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 12086: invokestatic    org/telegram/messenger/DataQuery.getInstance:(I)Lorg/telegram/messenger/DataQuery;
        // 12089: lload           10
        // 12091: iconst_0       
        // 12092: invokevirtual   org/telegram/messenger/DataQuery.cleanDraft:(JZ)V
        // 12095: goto            12210
        // 12098: astore_1       
        // 12099: aload_2        
        // 12100: astore          4
        // 12102: aload_3        
        // 12103: astore_2       
        // 12104: goto            12128
        // 12107: astore_3       
        // 12108: aload_1        
        // 12109: astore_2       
        // 12110: aload           6
        // 12112: astore          4
        // 12114: aload_3        
        // 12115: astore_1       
        // 12116: goto            12128
        // 12119: astore_3       
        // 12120: aload           6
        // 12122: astore          4
        // 12124: aload_1        
        // 12125: astore_2       
        // 12126: aload_3        
        // 12127: astore_1       
        // 12128: aload_1        
        // 12129: astore_3       
        // 12130: aload           4
        // 12132: astore_1       
        // 12133: aload_2        
        // 12134: astore          4
        // 12136: aload_3        
        // 12137: astore_2       
        // 12138: aload           4
        // 12140: astore_3       
        // 12141: goto            12148
        // 12144: astore_2       
        // 12145: goto            898
        // 12148: aload_2        
        // 12149: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        // 12152: aload_0        
        // 12153: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 12156: invokestatic    org/telegram/messenger/MessagesStorage.getInstance:(I)Lorg/telegram/messenger/MessagesStorage;
        // 12159: aload_3        
        // 12160: invokevirtual   org/telegram/messenger/MessagesStorage.markMessageAsSendError:(Lorg/telegram/tgnet/TLRPC$Message;)V
        // 12163: aload_1        
        // 12164: ifnull          12175
        // 12167: aload_1        
        // 12168: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        // 12171: iconst_2       
        // 12172: putfield        org/telegram/tgnet/TLRPC$Message.send_state:I
        // 12175: aload_0        
        // 12176: getfield        org/telegram/messenger/SendMessagesHelper.currentAccount:I
        // 12179: invokestatic    org/telegram/messenger/NotificationCenter.getInstance:(I)Lorg/telegram/messenger/NotificationCenter;
        // 12182: getstatic       org/telegram/messenger/NotificationCenter.messageSendError:I
        // 12185: iconst_1       
        // 12186: anewarray       Ljava/lang/Object;
        // 12189: dup            
        // 12190: iconst_0       
        // 12191: aload_3        
        // 12192: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        // 12195: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        // 12198: aastore        
        // 12199: invokevirtual   org/telegram/messenger/NotificationCenter.postNotificationName:(I[Ljava/lang/Object;)V
        // 12202: aload_0        
        // 12203: aload_3        
        // 12204: getfield        org/telegram/tgnet/TLRPC$Message.id:I
        // 12207: invokevirtual   org/telegram/messenger/SendMessagesHelper.processSentMessage:(I)V
        // 12210: return         
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/String;Lorg/telegram/tgnet/TLRPC$MessageMedia;Lorg/telegram/tgnet/TLRPC$TL_photo;Lorg/telegram/messenger/VideoEditedInfo;Lorg/telegram/tgnet/TLRPC$User;Lorg/telegram/tgnet/TLRPC$TL_document;Lorg/telegram/tgnet/TLRPC$TL_game;Lorg/telegram/tgnet/TLRPC$TL_messageMediaPoll;JLjava/lang/String;Lorg/telegram/messenger/MessageObject;Lorg/telegram/tgnet/TLRPC$WebPage;ZLorg/telegram/messenger/MessageObject;Ljava/util/ArrayList<Lorg/telegram/tgnet/TLRPC$MessageEntity;>;Lorg/telegram/tgnet/TLRPC$ReplyMarkup;Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;ILjava/lang/Object;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  304    311    903    906    Ljava/lang/Exception;
        //  314    322    893    898    Ljava/lang/Exception;
        //  353    361    893    898    Ljava/lang/Exception;
        //  364    378    893    898    Ljava/lang/Exception;
        //  384    391    893    898    Ljava/lang/Exception;
        //  403    412    893    898    Ljava/lang/Exception;
        //  415    422    893    898    Ljava/lang/Exception;
        //  443    452    893    898    Ljava/lang/Exception;
        //  455    468    893    898    Ljava/lang/Exception;
        //  480    489    893    898    Ljava/lang/Exception;
        //  492    501    893    898    Ljava/lang/Exception;
        //  512    522    893    898    Ljava/lang/Exception;
        //  525    529    893    898    Ljava/lang/Exception;
        //  532    536    893    898    Ljava/lang/Exception;
        //  539    551    893    898    Ljava/lang/Exception;
        //  554    566    893    898    Ljava/lang/Exception;
        //  569    581    893    898    Ljava/lang/Exception;
        //  584    596    893    898    Ljava/lang/Exception;
        //  599    611    893    898    Ljava/lang/Exception;
        //  621    631    893    898    Ljava/lang/Exception;
        //  634    644    893    898    Ljava/lang/Exception;
        //  647    657    893    898    Ljava/lang/Exception;
        //  660    670    893    898    Ljava/lang/Exception;
        //  676    685    893    898    Ljava/lang/Exception;
        //  688    700    893    898    Ljava/lang/Exception;
        //  710    720    893    898    Ljava/lang/Exception;
        //  723    732    893    898    Ljava/lang/Exception;
        //  756    768    893    898    Ljava/lang/Exception;
        //  778    790    893    898    Ljava/lang/Exception;
        //  810    821    893    898    Ljava/lang/Exception;
        //  835    846    893    898    Ljava/lang/Exception;
        //  849    859    893    898    Ljava/lang/Exception;
        //  921    930    903    906    Ljava/lang/Exception;
        //  933    942    903    906    Ljava/lang/Exception;
        //  958    966    893    898    Ljava/lang/Exception;
        //  969    977    893    898    Ljava/lang/Exception;
        //  980    985    893    898    Ljava/lang/Exception;
        //  988    993    893    898    Ljava/lang/Exception;
        //  996    1006   893    898    Ljava/lang/Exception;
        //  1020   1025   893    898    Ljava/lang/Exception;
        //  1028   1033   893    898    Ljava/lang/Exception;
        //  1036   1043   893    898    Ljava/lang/Exception;
        //  1049   1054   893    898    Ljava/lang/Exception;
        //  1057   1062   893    898    Ljava/lang/Exception;
        //  1065   1072   893    898    Ljava/lang/Exception;
        //  1075   1085   893    898    Ljava/lang/Exception;
        //  1093   1104   893    898    Ljava/lang/Exception;
        //  1117   1124   893    898    Ljava/lang/Exception;
        //  1144   1153   903    906    Ljava/lang/Exception;
        //  1156   1165   903    906    Ljava/lang/Exception;
        //  1168   1175   893    898    Ljava/lang/Exception;
        //  1196   1205   903    906    Ljava/lang/Exception;
        //  1208   1217   903    906    Ljava/lang/Exception;
        //  1220   1227   893    898    Ljava/lang/Exception;
        //  1235   1245   893    898    Ljava/lang/Exception;
        //  1281   1290   903    906    Ljava/lang/Exception;
        //  1293   1302   903    906    Ljava/lang/Exception;
        //  1305   1310   893    898    Ljava/lang/Exception;
        //  1313   1318   893    898    Ljava/lang/Exception;
        //  1321   1328   893    898    Ljava/lang/Exception;
        //  1331   1338   893    898    Ljava/lang/Exception;
        //  1341   1353   893    898    Ljava/lang/Exception;
        //  1361   1368   893    898    Ljava/lang/Exception;
        //  1376   1386   893    898    Ljava/lang/Exception;
        //  1389   1396   893    898    Ljava/lang/Exception;
        //  1399   1406   893    898    Ljava/lang/Exception;
        //  1409   1421   893    898    Ljava/lang/Exception;
        //  1424   1434   893    898    Ljava/lang/Exception;
        //  1442   1453   893    898    Ljava/lang/Exception;
        //  1471   1479   893    898    Ljava/lang/Exception;
        //  1482   1493   893    898    Ljava/lang/Exception;
        //  1496   1503   893    898    Ljava/lang/Exception;
        //  1516   1552   893    898    Ljava/lang/Exception;
        //  1567   1576   903    906    Ljava/lang/Exception;
        //  1579   1584   893    898    Ljava/lang/Exception;
        //  1587   1592   893    898    Ljava/lang/Exception;
        //  1595   1602   893    898    Ljava/lang/Exception;
        //  1605   1615   893    898    Ljava/lang/Exception;
        //  1634   1644   893    898    Ljava/lang/Exception;
        //  1683   1692   903    906    Ljava/lang/Exception;
        //  1695   1704   903    906    Ljava/lang/Exception;
        //  1707   1712   893    898    Ljava/lang/Exception;
        //  1715   1720   893    898    Ljava/lang/Exception;
        //  1723   1730   893    898    Ljava/lang/Exception;
        //  1733   1746   893    898    Ljava/lang/Exception;
        //  1749   1762   893    898    Ljava/lang/Exception;
        //  1765   1778   893    898    Ljava/lang/Exception;
        //  1781   1794   893    898    Ljava/lang/Exception;
        //  1797   1805   893    898    Ljava/lang/Exception;
        //  1808   1822   893    898    Ljava/lang/Exception;
        //  1825   1838   893    898    Ljava/lang/Exception;
        //  1844   1855   893    898    Ljava/lang/Exception;
        //  1863   1874   893    898    Ljava/lang/Exception;
        //  1877   1887   893    898    Ljava/lang/Exception;
        //  1890   1897   893    898    Ljava/lang/Exception;
        //  1900   1911   893    898    Ljava/lang/Exception;
        //  1914   1924   893    898    Ljava/lang/Exception;
        //  1927   1934   893    898    Ljava/lang/Exception;
        //  1942   1952   893    898    Ljava/lang/Exception;
        //  1985   1994   903    906    Ljava/lang/Exception;
        //  1997   2006   903    906    Ljava/lang/Exception;
        //  2009   2014   893    898    Ljava/lang/Exception;
        //  2017   2022   893    898    Ljava/lang/Exception;
        //  2025   2032   893    898    Ljava/lang/Exception;
        //  2035   2042   893    898    Ljava/lang/Exception;
        //  2045   2057   893    898    Ljava/lang/Exception;
        //  2065   2075   893    898    Ljava/lang/Exception;
        //  2078   2085   893    898    Ljava/lang/Exception;
        //  2088   2095   893    898    Ljava/lang/Exception;
        //  2098   2110   893    898    Ljava/lang/Exception;
        //  2113   2123   893    898    Ljava/lang/Exception;
        //  2131   2142   893    898    Ljava/lang/Exception;
        //  2152   2160   893    898    Ljava/lang/Exception;
        //  2163   2171   893    898    Ljava/lang/Exception;
        //  2182   2190   893    898    Ljava/lang/Exception;
        //  2219   2226   893    898    Ljava/lang/Exception;
        //  2238   2243   893    898    Ljava/lang/Exception;
        //  2246   2251   893    898    Ljava/lang/Exception;
        //  2254   2265   893    898    Ljava/lang/Exception;
        //  2273   2281   893    898    Ljava/lang/Exception;
        //  2284   2292   893    898    Ljava/lang/Exception;
        //  2295   2308   893    898    Ljava/lang/Exception;
        //  2314   2321   893    898    Ljava/lang/Exception;
        //  2361   2369   893    898    Ljava/lang/Exception;
        //  2395   2408   893    898    Ljava/lang/Exception;
        //  2411   2426   893    898    Ljava/lang/Exception;
        //  2429   2437   893    898    Ljava/lang/Exception;
        //  2440   2451   893    898    Ljava/lang/Exception;
        //  2454   2459   893    898    Ljava/lang/Exception;
        //  2462   2467   893    898    Ljava/lang/Exception;
        //  2470   2481   893    898    Ljava/lang/Exception;
        //  2484   2494   893    898    Ljava/lang/Exception;
        //  2497   2505   893    898    Ljava/lang/Exception;
        //  2508   2519   893    898    Ljava/lang/Exception;
        //  2522   2532   893    898    Ljava/lang/Exception;
        //  2538   2558   893    898    Ljava/lang/Exception;
        //  2561   2569   893    898    Ljava/lang/Exception;
        //  2572   2577   893    898    Ljava/lang/Exception;
        //  2580   2585   893    898    Ljava/lang/Exception;
        //  2588   2595   893    898    Ljava/lang/Exception;
        //  2598   2608   893    898    Ljava/lang/Exception;
        //  2630   2635   893    898    Ljava/lang/Exception;
        //  2638   2643   893    898    Ljava/lang/Exception;
        //  2646   2653   893    898    Ljava/lang/Exception;
        //  2675   2680   893    898    Ljava/lang/Exception;
        //  2683   2688   893    898    Ljava/lang/Exception;
        //  2691   2698   893    898    Ljava/lang/Exception;
        //  2747   2755   893    898    Ljava/lang/Exception;
        //  2758   2765   893    898    Ljava/lang/Exception;
        //  2768   2782   893    898    Ljava/lang/Exception;
        //  2790   2797   893    898    Ljava/lang/Exception;
        //  2803   2811   893    898    Ljava/lang/Exception;
        //  2814   2821   893    898    Ljava/lang/Exception;
        //  2824   2832   893    898    Ljava/lang/Exception;
        //  2835   2842   893    898    Ljava/lang/Exception;
        //  2845   2857   893    898    Ljava/lang/Exception;
        //  2860   2867   893    898    Ljava/lang/Exception;
        //  2870   2877   893    898    Ljava/lang/Exception;
        //  2880   2886   893    898    Ljava/lang/Exception;
        //  2899   2910   893    898    Ljava/lang/Exception;
        //  2916   2931   893    898    Ljava/lang/Exception;
        //  2934   2948   893    898    Ljava/lang/Exception;
        //  2951   2962   893    898    Ljava/lang/Exception;
        //  2991   2997   12144  12148  Ljava/lang/Exception;
        //  3006   3014   893    898    Ljava/lang/Exception;
        //  3021   3032   893    898    Ljava/lang/Exception;
        //  3039   3054   893    898    Ljava/lang/Exception;
        //  3056   3063   893    898    Ljava/lang/Exception;
        //  3065   3071   893    898    Ljava/lang/Exception;
        //  3076   3097   893    898    Ljava/lang/Exception;
        //  3099   3111   893    898    Ljava/lang/Exception;
        //  3111   3117   12144  12148  Ljava/lang/Exception;
        //  3124   3131   893    898    Ljava/lang/Exception;
        //  3142   3163   12144  12148  Ljava/lang/Exception;
        //  3175   3180   893    898    Ljava/lang/Exception;
        //  3182   3194   893    898    Ljava/lang/Exception;
        //  3196   3216   893    898    Ljava/lang/Exception;
        //  3223   3231   893    898    Ljava/lang/Exception;
        //  3233   3245   893    898    Ljava/lang/Exception;
        //  3247   3252   893    898    Ljava/lang/Exception;
        //  3257   3262   893    898    Ljava/lang/Exception;
        //  3264   3272   893    898    Ljava/lang/Exception;
        //  3274   3288   893    898    Ljava/lang/Exception;
        //  3291   3296   12144  12148  Ljava/lang/Exception;
        //  3296   3314   12144  12148  Ljava/lang/Exception;
        //  3326   3339   893    898    Ljava/lang/Exception;
        //  3341   3353   893    898    Ljava/lang/Exception;
        //  3355   3366   893    898    Ljava/lang/Exception;
        //  3371   3382   893    898    Ljava/lang/Exception;
        //  3384   3393   893    898    Ljava/lang/Exception;
        //  3408   3419   893    898    Ljava/lang/Exception;
        //  3421   3427   893    898    Ljava/lang/Exception;
        //  3440   3447   893    898    Ljava/lang/Exception;
        //  3449   3460   893    898    Ljava/lang/Exception;
        //  3462   3489   893    898    Ljava/lang/Exception;
        //  3491   3499   893    898    Ljava/lang/Exception;
        //  3502   3507   893    898    Ljava/lang/Exception;
        //  3509   3514   893    898    Ljava/lang/Exception;
        //  3516   3531   893    898    Ljava/lang/Exception;
        //  3533   3543   893    898    Ljava/lang/Exception;
        //  3545   3557   893    898    Ljava/lang/Exception;
        //  3559   3579   893    898    Ljava/lang/Exception;
        //  3581   3595   893    898    Ljava/lang/Exception;
        //  3602   3610   893    898    Ljava/lang/Exception;
        //  3615   3620   893    898    Ljava/lang/Exception;
        //  3622   3627   893    898    Ljava/lang/Exception;
        //  3629   3635   893    898    Ljava/lang/Exception;
        //  3637   3646   893    898    Ljava/lang/Exception;
        //  3658   3674   893    898    Ljava/lang/Exception;
        //  3681   3698   893    898    Ljava/lang/Exception;
        //  3705   3713   893    898    Ljava/lang/Exception;
        //  3716   3724   893    898    Ljava/lang/Exception;
        //  3726   3731   893    898    Ljava/lang/Exception;
        //  3734   3766   12144  12148  Ljava/lang/Exception;
        //  3775   3787   893    898    Ljava/lang/Exception;
        //  3790   3802   12144  12148  Ljava/lang/Exception;
        //  3809   3815   893    898    Ljava/lang/Exception;
        //  3818   3833   12144  12148  Ljava/lang/Exception;
        //  3840   3847   893    898    Ljava/lang/Exception;
        //  3849   3860   893    898    Ljava/lang/Exception;
        //  3862   3868   893    898    Ljava/lang/Exception;
        //  3870   3882   893    898    Ljava/lang/Exception;
        //  3882   3888   12144  12148  Ljava/lang/Exception;
        //  3895   3905   893    898    Ljava/lang/Exception;
        //  3907   3914   893    898    Ljava/lang/Exception;
        //  3919   3937   893    898    Ljava/lang/Exception;
        //  3939   3959   893    898    Ljava/lang/Exception;
        //  3961   3969   893    898    Ljava/lang/Exception;
        //  3971   3978   893    898    Ljava/lang/Exception;
        //  3992   4007   893    898    Ljava/lang/Exception;
        //  4016   4023   893    898    Ljava/lang/Exception;
        //  4029   4036   893    898    Ljava/lang/Exception;
        //  4041   4059   893    898    Ljava/lang/Exception;
        //  4061   4081   893    898    Ljava/lang/Exception;
        //  4083   4091   893    898    Ljava/lang/Exception;
        //  4093   4100   893    898    Ljava/lang/Exception;
        //  4114   4129   893    898    Ljava/lang/Exception;
        //  4158   4165   893    898    Ljava/lang/Exception;
        //  4167   4174   893    898    Ljava/lang/Exception;
        //  4176   4181   893    898    Ljava/lang/Exception;
        //  4184   4204   12144  12148  Ljava/lang/Exception;
        //  4204   4218   12119  12128  Ljava/lang/Exception;
        //  4223   4232   4265   4274   Ljava/lang/Exception;
        //  4237   4246   4265   4274   Ljava/lang/Exception;
        //  4246   4262   4265   4274   Ljava/lang/Exception;
        //  4274   4280   12119  12128  Ljava/lang/Exception;
        //  4289   4296   4265   4274   Ljava/lang/Exception;
        //  4304   4316   4265   4274   Ljava/lang/Exception;
        //  4320   4335   4265   4274   Ljava/lang/Exception;
        //  4335   4347   4265   4274   Ljava/lang/Exception;
        //  4353   4362   4265   4274   Ljava/lang/Exception;
        //  4391   4420   4474   4475   Ljava/lang/Exception;
        //  4420   4465   4470   4474   Ljava/lang/Exception;
        //  4487   4527   12107  12119  Ljava/lang/Exception;
        //  4531   4540   4470   4474   Ljava/lang/Exception;
        //  4551   4574   4470   4474   Ljava/lang/Exception;
        //  4574   4594   12107  12119  Ljava/lang/Exception;
        //  4601   4609   4470   4474   Ljava/lang/Exception;
        //  4618   4623   12107  12119  Ljava/lang/Exception;
        //  4633   4731   4470   4474   Ljava/lang/Exception;
        //  4814   4879   4470   4474   Ljava/lang/Exception;
        //  4882   4922   4470   4474   Ljava/lang/Exception;
        //  4925   4934   4470   4474   Ljava/lang/Exception;
        //  4934   4983   4470   4474   Ljava/lang/Exception;
        //  5030   5068   4470   4474   Ljava/lang/Exception;
        //  5077   5093   4470   4474   Ljava/lang/Exception;
        //  5096   5114   4470   4474   Ljava/lang/Exception;
        //  5122   5147   4470   4474   Ljava/lang/Exception;
        //  5147   5157   4470   4474   Ljava/lang/Exception;
        //  5167   5255   4470   4474   Ljava/lang/Exception;
        //  5262   5278   4470   4474   Ljava/lang/Exception;
        //  5278   5295   5475   5482   Ljava/lang/Exception;
        //  5298   5343   5475   5482   Ljava/lang/Exception;
        //  5346   5352   5475   5482   Ljava/lang/Exception;
        //  5373   5445   5475   5482   Ljava/lang/Exception;
        //  5448   5455   5475   5482   Ljava/lang/Exception;
        //  5506   5536   5475   5482   Ljava/lang/Exception;
        //  5541   5566   5475   5482   Ljava/lang/Exception;
        //  5566   5576   5475   5482   Ljava/lang/Exception;
        //  5585   5665   5475   5482   Ljava/lang/Exception;
        //  5668   5715   5475   5482   Ljava/lang/Exception;
        //  5735   5755   5475   5482   Ljava/lang/Exception;
        //  5787   5797   5475   5482   Ljava/lang/Exception;
        //  5814   5833   5475   5482   Ljava/lang/Exception;
        //  5838   5892   5475   5482   Ljava/lang/Exception;
        //  5898   5908   6301   6302   Ljava/lang/Exception;
        //  5913   5938   5475   5482   Ljava/lang/Exception;
        //  5938   5945   6301   6302   Ljava/lang/Exception;
        //  5950   5964   5475   5482   Ljava/lang/Exception;
        //  5969   5980   5475   5482   Ljava/lang/Exception;
        //  5980   5986   5475   5482   Ljava/lang/Exception;
        //  5989   6009   6301   6302   Ljava/lang/Exception;
        //  6009   6016   6301   6302   Ljava/lang/Exception;
        //  6023   6030   5475   5482   Ljava/lang/Exception;
        //  6037   6071   6301   6302   Ljava/lang/Exception;
        //  6071   6088   6297   6301   Ljava/lang/Exception;
        //  6096   6106   7910   7911   Ljava/lang/Exception;
        //  6110   6123   7910   7911   Ljava/lang/Exception;
        //  6127   6138   7910   7911   Ljava/lang/Exception;
        //  6142   6153   7910   7911   Ljava/lang/Exception;
        //  6173   6177   7910   7911   Ljava/lang/Exception;
        //  6180   6187   7910   7911   Ljava/lang/Exception;
        //  6190   6196   7910   7911   Ljava/lang/Exception;
        //  6199   6204   7910   7911   Ljava/lang/Exception;
        //  6207   6212   7910   7911   Ljava/lang/Exception;
        //  6215   6226   7910   7911   Ljava/lang/Exception;
        //  6229   6245   7910   7911   Ljava/lang/Exception;
        //  6248   6254   7910   7911   Ljava/lang/Exception;
        //  6257   6263   7910   7911   Ljava/lang/Exception;
        //  6266   6272   7910   7911   Ljava/lang/Exception;
        //  6275   6281   7910   7911   Ljava/lang/Exception;
        //  6312   6317   7910   7911   Ljava/lang/Exception;
        //  6321   6326   7910   7911   Ljava/lang/Exception;
        //  6335   6342   7910   7911   Ljava/lang/Exception;
        //  6346   6352   7910   7911   Ljava/lang/Exception;
        //  6356   6368   7910   7911   Ljava/lang/Exception;
        //  6380   6393   7910   7911   Ljava/lang/Exception;
        //  6402   6407   7910   7911   Ljava/lang/Exception;
        //  6411   6421   7910   7911   Ljava/lang/Exception;
        //  6425   6433   7910   7911   Ljava/lang/Exception;
        //  6447   6468   7910   7911   Ljava/lang/Exception;
        //  6478   6490   7910   7911   Ljava/lang/Exception;
        //  6494   6499   7910   7911   Ljava/lang/Exception;
        //  6503   6513   7910   7911   Ljava/lang/Exception;
        //  6527   6532   7910   7911   Ljava/lang/Exception;
        //  6536   6541   7910   7911   Ljava/lang/Exception;
        //  6545   6550   7910   7911   Ljava/lang/Exception;
        //  6554   6559   7910   7911   Ljava/lang/Exception;
        //  6563   6570   7910   7911   Ljava/lang/Exception;
        //  6574   6587   7910   7911   Ljava/lang/Exception;
        //  6591   6604   7910   7911   Ljava/lang/Exception;
        //  6608   6621   7910   7911   Ljava/lang/Exception;
        //  6625   6636   7910   7911   Ljava/lang/Exception;
        //  6640   6651   7910   7911   Ljava/lang/Exception;
        //  6662   6666   7910   7911   Ljava/lang/Exception;
        //  6670   6677   7910   7911   Ljava/lang/Exception;
        //  6681   6686   7910   7911   Ljava/lang/Exception;
        //  6690   6696   7910   7911   Ljava/lang/Exception;
        //  6700   6706   7910   7911   Ljava/lang/Exception;
        //  6713   6719   7910   7911   Ljava/lang/Exception;
        //  6723   6729   7910   7911   Ljava/lang/Exception;
        //  6738   6746   7910   7911   Ljava/lang/Exception;
        //  6750   6761   7910   7911   Ljava/lang/Exception;
        //  6765   6771   7910   7911   Ljava/lang/Exception;
        //  6778   6803   7910   7911   Ljava/lang/Exception;
        //  6807   6813   7910   7911   Ljava/lang/Exception;
        //  6838   6843   7910   7911   Ljava/lang/Exception;
        //  6846   6851   7910   7911   Ljava/lang/Exception;
        //  6854   6859   7910   7911   Ljava/lang/Exception;
        //  6862   6867   7910   7911   Ljava/lang/Exception;
        //  6873   6883   7910   7911   Ljava/lang/Exception;
        //  6886   6901   7910   7911   Ljava/lang/Exception;
        //  6910   6917   7910   7911   Ljava/lang/Exception;
        //  6920   6927   7910   7911   Ljava/lang/Exception;
        //  6930   6937   7910   7911   Ljava/lang/Exception;
        //  6940   6946   7910   7911   Ljava/lang/Exception;
        //  6954   6961   7910   7911   Ljava/lang/Exception;
        //  6969   6982   7910   7911   Ljava/lang/Exception;
        //  6998   7006   7910   7911   Ljava/lang/Exception;
        //  7009   7018   7910   7911   Ljava/lang/Exception;
        //  7024   7028   7910   7911   Ljava/lang/Exception;
        //  7031   7035   7910   7911   Ljava/lang/Exception;
        //  7038   7044   7910   7911   Ljava/lang/Exception;
        //  7047   7058   7910   7911   Ljava/lang/Exception;
        //  7061   7070   7910   7911   Ljava/lang/Exception;
        //  7073   7078   7910   7911   Ljava/lang/Exception;
        //  7081   7086   7910   7911   Ljava/lang/Exception;
        //  7089   7098   7910   7911   Ljava/lang/Exception;
        //  7101   7109   7910   7911   Ljava/lang/Exception;
        //  7112   7129   7910   7911   Ljava/lang/Exception;
        //  7132   7140   7910   7911   Ljava/lang/Exception;
        //  7143   7153   7910   7911   Ljava/lang/Exception;
        //  7156   7165   7910   7911   Ljava/lang/Exception;
        //  7168   7174   7910   7911   Ljava/lang/Exception;
        //  7177   7187   7910   7911   Ljava/lang/Exception;
        //  7190   7201   7910   7911   Ljava/lang/Exception;
        //  7204   7218   7910   7911   Ljava/lang/Exception;
        //  7221   7235   7910   7911   Ljava/lang/Exception;
        //  7238   7252   7910   7911   Ljava/lang/Exception;
        //  7255   7269   7910   7911   Ljava/lang/Exception;
        //  7272   7283   7910   7911   Ljava/lang/Exception;
        //  7286   7297   7910   7911   Ljava/lang/Exception;
        //  7300   7305   7910   7911   Ljava/lang/Exception;
        //  7308   7313   7910   7911   Ljava/lang/Exception;
        //  7316   7326   7910   7911   Ljava/lang/Exception;
        //  7329   7336   7910   7911   Ljava/lang/Exception;
        //  7339   7346   7910   7911   Ljava/lang/Exception;
        //  7354   7362   7910   7911   Ljava/lang/Exception;
        //  7365   7372   7910   7911   Ljava/lang/Exception;
        //  7375   7387   7910   7911   Ljava/lang/Exception;
        //  7390   7400   7910   7911   Ljava/lang/Exception;
        //  7406   7411   7910   7911   Ljava/lang/Exception;
        //  7414   7419   7910   7911   Ljava/lang/Exception;
        //  7422   7429   7910   7911   Ljava/lang/Exception;
        //  7432   7443   7910   7911   Ljava/lang/Exception;
        //  7446   7455   7910   7911   Ljava/lang/Exception;
        //  7458   7462   7910   7911   Ljava/lang/Exception;
        //  7465   7469   7910   7911   Ljava/lang/Exception;
        //  7472   7480   7910   7911   Ljava/lang/Exception;
        //  7483   7490   7910   7911   Ljava/lang/Exception;
        //  7493   7510   7910   7911   Ljava/lang/Exception;
        //  7513   7521   7910   7911   Ljava/lang/Exception;
        //  7524   7536   7910   7911   Ljava/lang/Exception;
        //  7539   7549   7910   7911   Ljava/lang/Exception;
        //  7552   7562   7910   7911   Ljava/lang/Exception;
        //  7565   7572   7910   7911   Ljava/lang/Exception;
        //  7575   7582   7910   7911   Ljava/lang/Exception;
        //  7590   7598   7910   7911   Ljava/lang/Exception;
        //  7601   7608   7910   7911   Ljava/lang/Exception;
        //  7611   7624   7910   7911   Ljava/lang/Exception;
        //  7635   7642   7910   7911   Ljava/lang/Exception;
        //  7655   7661   7910   7911   Ljava/lang/Exception;
        //  7673   7684   7910   7911   Ljava/lang/Exception;
        //  7701   7707   7910   7911   Ljava/lang/Exception;
        //  7713   7727   7910   7911   Ljava/lang/Exception;
        //  7744   7750   7910   7911   Ljava/lang/Exception;
        //  7756   7768   7910   7911   Ljava/lang/Exception;
        //  7781   7793   7910   7911   Ljava/lang/Exception;
        //  7816   7822   7910   7911   Ljava/lang/Exception;
        //  7828   7840   7910   7911   Ljava/lang/Exception;
        //  7858   7864   7910   7911   Ljava/lang/Exception;
        //  7870   7882   7910   7911   Ljava/lang/Exception;
        //  7895   7907   7910   7911   Ljava/lang/Exception;
        //  7925   7935   10658  10669  Ljava/lang/Exception;
        //  7946   7951   7910   7911   Ljava/lang/Exception;
        //  7955   7960   7910   7911   Ljava/lang/Exception;
        //  7971   7978   7910   7911   Ljava/lang/Exception;
        //  7982   7996   7910   7911   Ljava/lang/Exception;
        //  8002   8011   10658  10669  Ljava/lang/Exception;
        //  8011   8021   10658  10669  Ljava/lang/Exception;
        //  8030   8038   7910   7911   Ljava/lang/Exception;
        //  8042   8049   7910   7911   Ljava/lang/Exception;
        //  8053   8067   7910   7911   Ljava/lang/Exception;
        //  8067   8074   10658  10669  Ljava/lang/Exception;
        //  8085   8095   7910   7911   Ljava/lang/Exception;
        //  8099   8112   7910   7911   Ljava/lang/Exception;
        //  8112   8126   10658  10669  Ljava/lang/Exception;
        //  8135   8146   7910   7911   Ljava/lang/Exception;
        //  8150   8166   7910   7911   Ljava/lang/Exception;
        //  8170   8184   7910   7911   Ljava/lang/Exception;
        //  8184   8201   10658  10669  Ljava/lang/Exception;
        //  8211   8219   7910   7911   Ljava/lang/Exception;
        //  8223   8228   7910   7911   Ljava/lang/Exception;
        //  8232   8237   7910   7911   Ljava/lang/Exception;
        //  8241   8248   7910   7911   Ljava/lang/Exception;
        //  8252   8265   7910   7911   Ljava/lang/Exception;
        //  8269   8282   7910   7911   Ljava/lang/Exception;
        //  8286   8299   7910   7911   Ljava/lang/Exception;
        //  8303   8316   7910   7911   Ljava/lang/Exception;
        //  8323   8328   7910   7911   Ljava/lang/Exception;
        //  8332   8337   7910   7911   Ljava/lang/Exception;
        //  8341   8348   7910   7911   Ljava/lang/Exception;
        //  8352   8368   7910   7911   Ljava/lang/Exception;
        //  8372   8388   7910   7911   Ljava/lang/Exception;
        //  8392   8415   7910   7911   Ljava/lang/Exception;
        //  8445   8477   8998   8999   Ljava/lang/Exception;
        //  8480   8497   8998   8999   Ljava/lang/Exception;
        //  8506   8514   7910   7911   Ljava/lang/Exception;
        //  8518   8534   7910   7911   Ljava/lang/Exception;
        //  8537   8547   8998   8999   Ljava/lang/Exception;
        //  8547   8555   9117   9121   Ljava/lang/Exception;
        //  8558   8588   9117   9121   Ljava/lang/Exception;
        //  8593   8617   9117   9121   Ljava/lang/Exception;
        //  8620   8634   9117   9121   Ljava/lang/Exception;
        //  8634   8668   9117   9121   Ljava/lang/Exception;
        //  8671   8746   9117   9121   Ljava/lang/Exception;
        //  8755   8789   9117   9121   Ljava/lang/Exception;
        //  8799   8879   9117   9121   Ljava/lang/Exception;
        //  8889   8929   9117   9121   Ljava/lang/Exception;
        //  8934   8957   9117   9121   Ljava/lang/Exception;
        //  8960   8966   9117   9121   Ljava/lang/Exception;
        //  8966   8971   9117   9121   Ljava/lang/Exception;
        //  8974   8980   9117   9121   Ljava/lang/Exception;
        //  8987   8992   9117   9121   Ljava/lang/Exception;
        //  9022   9114   9117   9121   Ljava/lang/Exception;
        //  9150   9246   9117   9121   Ljava/lang/Exception;
        //  9251   9298   9117   9121   Ljava/lang/Exception;
        //  9301   9333   9117   9121   Ljava/lang/Exception;
        //  9333   9370   9117   9121   Ljava/lang/Exception;
        //  9376   9503   9117   9121   Ljava/lang/Exception;
        //  9508   9521   9117   9121   Ljava/lang/Exception;
        //  9524   9568   9117   9121   Ljava/lang/Exception;
        //  9568   9591   9117   9121   Ljava/lang/Exception;
        //  9597   9648   9117   9121   Ljava/lang/Exception;
        //  9653   9700   9117   9121   Ljava/lang/Exception;
        //  9703   9735   9117   9121   Ljava/lang/Exception;
        //  9735   9803   9117   9121   Ljava/lang/Exception;
        //  9808   9831   9117   9121   Ljava/lang/Exception;
        //  9834   9840   9117   9121   Ljava/lang/Exception;
        //  9840   9851   9117   9121   Ljava/lang/Exception;
        //  9856   9881   9117   9121   Ljava/lang/Exception;
        //  9881   9886   9117   9121   Ljava/lang/Exception;
        //  9892   9972   9117   9121   Ljava/lang/Exception;
        //  9979   10055  10646  10658  Ljava/lang/Exception;
        //  10060  10076  9117   9121   Ljava/lang/Exception;
        //  10079  10093  10646  10658  Ljava/lang/Exception;
        //  10093  10168  10646  10658  Ljava/lang/Exception;
        //  10183  10275  9117   9121   Ljava/lang/Exception;
        //  10285  10325  9117   9121   Ljava/lang/Exception;
        //  10330  10353  9117   9121   Ljava/lang/Exception;
        //  10356  10362  9117   9121   Ljava/lang/Exception;
        //  10362  10367  9117   9121   Ljava/lang/Exception;
        //  10370  10377  10646  10658  Ljava/lang/Exception;
        //  10382  10399  9117   9121   Ljava/lang/Exception;
        //  10402  10433  10646  10658  Ljava/lang/Exception;
        //  10440  10445  9117   9121   Ljava/lang/Exception;
        //  10458  10464  10646  10658  Ljava/lang/Exception;
        //  10468  10478  9117   9121   Ljava/lang/Exception;
        //  10481  10498  10646  10658  Ljava/lang/Exception;
        //  10498  10516  10646  10658  Ljava/lang/Exception;
        //  10521  10528  12098  12107  Ljava/lang/Exception;
        //  10530  10541  12098  12107  Ljava/lang/Exception;
        //  10543  10549  12098  12107  Ljava/lang/Exception;
        //  10551  10562  12098  12107  Ljava/lang/Exception;
        //  10564  10569  12098  12107  Ljava/lang/Exception;
        //  10571  10576  12098  12107  Ljava/lang/Exception;
        //  10591  10598  12098  12107  Ljava/lang/Exception;
        //  10600  10611  12098  12107  Ljava/lang/Exception;
        //  10613  10619  12098  12107  Ljava/lang/Exception;
        //  10630  10643  12098  12107  Ljava/lang/Exception;
        //  10680  10685  12098  12107  Ljava/lang/Exception;
        //  10687  10692  12098  12107  Ljava/lang/Exception;
        //  10694  10701  12098  12107  Ljava/lang/Exception;
        //  10703  10716  12098  12107  Ljava/lang/Exception;
        //  10718  10729  12098  12107  Ljava/lang/Exception;
        //  10731  10755  12098  12107  Ljava/lang/Exception;
        //  10757  10762  12098  12107  Ljava/lang/Exception;
        //  10764  10769  12098  12107  Ljava/lang/Exception;
        //  10771  10778  12098  12107  Ljava/lang/Exception;
        //  10780  10797  12098  12107  Ljava/lang/Exception;
        //  10804  10817  12098  12107  Ljava/lang/Exception;
        //  10822  10827  12098  12107  Ljava/lang/Exception;
        //  10829  10834  12098  12107  Ljava/lang/Exception;
        //  10836  10843  12098  12107  Ljava/lang/Exception;
        //  10845  10859  12098  12107  Ljava/lang/Exception;
        //  10861  10870  12098  12107  Ljava/lang/Exception;
        //  10872  10877  12098  12107  Ljava/lang/Exception;
        //  10879  10884  12098  12107  Ljava/lang/Exception;
        //  10886  10895  12098  12107  Ljava/lang/Exception;
        //  10897  10905  12098  12107  Ljava/lang/Exception;
        //  10907  10925  12098  12107  Ljava/lang/Exception;
        //  10927  10943  12098  12107  Ljava/lang/Exception;
        //  10945  10953  12098  12107  Ljava/lang/Exception;
        //  10955  10972  12098  12107  Ljava/lang/Exception;
        //  10977  10988  12098  12107  Ljava/lang/Exception;
        //  10990  11010  12098  12107  Ljava/lang/Exception;
        //  11015  11026  12098  12107  Ljava/lang/Exception;
        //  11028  11051  12098  12107  Ljava/lang/Exception;
        //  11053  11065  12098  12107  Ljava/lang/Exception;
        //  11077  11082  12098  12107  Ljava/lang/Exception;
        //  11084  11089  12098  12107  Ljava/lang/Exception;
        //  11091  11098  12098  12107  Ljava/lang/Exception;
        //  11100  11109  12098  12107  Ljava/lang/Exception;
        //  11111  11122  12098  12107  Ljava/lang/Exception;
        //  11133  11140  12098  12107  Ljava/lang/Exception;
        //  11142  11149  12098  12107  Ljava/lang/Exception;
        //  11151  11163  12098  12107  Ljava/lang/Exception;
        //  11165  11174  12098  12107  Ljava/lang/Exception;
        //  11176  11186  12098  12107  Ljava/lang/Exception;
        //  11188  11197  12098  12107  Ljava/lang/Exception;
        //  11199  11204  12098  12107  Ljava/lang/Exception;
        //  11206  11211  12098  12107  Ljava/lang/Exception;
        //  11213  11222  12098  12107  Ljava/lang/Exception;
        //  11224  11232  12098  12107  Ljava/lang/Exception;
        //  11234  11252  12098  12107  Ljava/lang/Exception;
        //  11254  11276  12098  12107  Ljava/lang/Exception;
        //  11278  11294  12098  12107  Ljava/lang/Exception;
        //  11301  11307  12098  12107  Ljava/lang/Exception;
        //  11309  11322  12098  12107  Ljava/lang/Exception;
        //  11324  11336  12098  12107  Ljava/lang/Exception;
        //  11354  11359  12098  12107  Ljava/lang/Exception;
        //  11361  11366  12098  12107  Ljava/lang/Exception;
        //  11368  11373  12098  12107  Ljava/lang/Exception;
        //  11375  11380  12098  12107  Ljava/lang/Exception;
        //  11385  11395  12098  12107  Ljava/lang/Exception;
        //  11397  11412  12098  12107  Ljava/lang/Exception;
        //  11420  11427  12098  12107  Ljava/lang/Exception;
        //  11429  11436  12098  12107  Ljava/lang/Exception;
        //  11438  11443  12098  12107  Ljava/lang/Exception;
        //  11445  11450  12098  12107  Ljava/lang/Exception;
        //  11452  11459  12098  12107  Ljava/lang/Exception;
        //  11461  11468  12098  12107  Ljava/lang/Exception;
        //  11470  11482  12098  12107  Ljava/lang/Exception;
        //  11487  11492  12098  12107  Ljava/lang/Exception;
        //  11494  11499  12098  12107  Ljava/lang/Exception;
        //  11501  11508  12098  12107  Ljava/lang/Exception;
        //  11524  11531  12098  12107  Ljava/lang/Exception;
        //  11533  11543  12098  12107  Ljava/lang/Exception;
        //  11545  11554  12098  12107  Ljava/lang/Exception;
        //  11556  11561  12098  12107  Ljava/lang/Exception;
        //  11563  11568  12098  12107  Ljava/lang/Exception;
        //  11570  11579  12098  12107  Ljava/lang/Exception;
        //  11581  11589  12098  12107  Ljava/lang/Exception;
        //  11591  11609  12098  12107  Ljava/lang/Exception;
        //  11611  11618  12098  12107  Ljava/lang/Exception;
        //  11620  11629  12098  12107  Ljava/lang/Exception;
        //  11631  11638  12098  12107  Ljava/lang/Exception;
        //  11640  11652  12098  12107  Ljava/lang/Exception;
        //  11654  11663  12098  12107  Ljava/lang/Exception;
        //  11670  11676  12098  12107  Ljava/lang/Exception;
        //  11683  11691  12098  12107  Ljava/lang/Exception;
        //  11693  11700  12098  12107  Ljava/lang/Exception;
        //  11702  11715  12098  12107  Ljava/lang/Exception;
        //  11717  11729  12098  12107  Ljava/lang/Exception;
        //  11736  11749  12098  12107  Ljava/lang/Exception;
        //  11754  11767  12098  12107  Ljava/lang/Exception;
        //  11769  11774  12098  12107  Ljava/lang/Exception;
        //  11776  11781  12098  12107  Ljava/lang/Exception;
        //  11786  11795  12098  12107  Ljava/lang/Exception;
        //  11797  11806  12098  12107  Ljava/lang/Exception;
        //  11813  11821  12098  12107  Ljava/lang/Exception;
        //  11823  11830  12098  12107  Ljava/lang/Exception;
        //  11832  11846  12098  12107  Ljava/lang/Exception;
        //  11848  11857  12098  12107  Ljava/lang/Exception;
        //  11859  11868  12098  12107  Ljava/lang/Exception;
        //  11870  11883  12098  12107  Ljava/lang/Exception;
        //  11890  11901  12098  12107  Ljava/lang/Exception;
        //  11903  11919  12098  12107  Ljava/lang/Exception;
        //  11921  11935  12098  12107  Ljava/lang/Exception;
        //  11937  11946  12098  12107  Ljava/lang/Exception;
        //  11948  11955  12098  12107  Ljava/lang/Exception;
        //  11962  11970  12098  12107  Ljava/lang/Exception;
        //  11972  11977  12098  12107  Ljava/lang/Exception;
        //  11979  11984  12098  12107  Ljava/lang/Exception;
        //  11986  11993  12098  12107  Ljava/lang/Exception;
        //  11995  12008  12098  12107  Ljava/lang/Exception;
        //  12010  12024  12098  12107  Ljava/lang/Exception;
        //  12029  12034  12098  12107  Ljava/lang/Exception;
        //  12036  12041  12098  12107  Ljava/lang/Exception;
        //  12043  12050  12098  12107  Ljava/lang/Exception;
        //  12052  12075  12098  12107  Ljava/lang/Exception;
        //  12082  12095  12098  12107  Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_6153:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void sendReadyToSendGroup(final DelayedMessage delayedMessage, final boolean b, final boolean b2) {
        if (delayedMessage.messageObjects.isEmpty()) {
            delayedMessage.markAsError();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("group_");
        sb.append(delayedMessage.groupId);
        final String string = sb.toString();
        final int finalGroupMessage = delayedMessage.finalGroupMessage;
        final ArrayList<MessageObject> messageObjects = delayedMessage.messageObjects;
        if (finalGroupMessage != messageObjects.get(messageObjects.size() - 1).getId()) {
            if (b) {
                this.putToDelayedMessages(string, delayedMessage);
            }
            return;
        }
        final int n = 0;
        int i = 0;
        if (b) {
            this.delayedMessages.remove(string);
            MessagesStorage.getInstance(this.currentAccount).putMessages(delayedMessage.messages, false, true, false, 0);
            MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(delayedMessage.peer, delayedMessage.messageObjects);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
        final TLObject sendRequest = delayedMessage.sendRequest;
        if (sendRequest instanceof TLRPC.TL_messages_sendMultiMedia) {
            for (TLRPC.TL_messages_sendMultiMedia tl_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia)sendRequest; i < tl_messages_sendMultiMedia.multi_media.size(); ++i) {
                final TLRPC.InputMedia media = tl_messages_sendMultiMedia.multi_media.get(i).media;
                if (media instanceof TLRPC.TL_inputMediaUploadedPhoto || media instanceof TLRPC.TL_inputMediaUploadedDocument) {
                    return;
                }
            }
            if (b2) {
                final DelayedMessage maxDelayedMessageForMessageId = this.findMaxDelayedMessageForMessageId(delayedMessage.finalGroupMessage, delayedMessage.peer);
                if (maxDelayedMessageForMessageId != null) {
                    maxDelayedMessageForMessageId.addDelayedRequest(delayedMessage.sendRequest, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage);
                    final ArrayList<DelayedMessageSendAfterRequest> requests = delayedMessage.requests;
                    if (requests != null) {
                        maxDelayedMessageForMessageId.requests.addAll(requests);
                    }
                    return;
                }
            }
        }
        else {
            final TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest;
            for (int j = n; j < tl_messages_sendEncryptedMultiMedia.files.size(); ++j) {
                if (tl_messages_sendEncryptedMultiMedia.files.get(j) instanceof TLRPC.TL_inputEncryptedFile) {
                    return;
                }
            }
        }
        final TLObject sendRequest2 = delayedMessage.sendRequest;
        if (sendRequest2 instanceof TLRPC.TL_messages_sendMultiMedia) {
            this.performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia)sendRequest2, delayedMessage.messageObjects, delayedMessage.originalPaths, delayedMessage.parentObjects, delayedMessage);
        }
        else {
            SecretChatHelper.getInstance(this.currentAccount).performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest, delayedMessage);
        }
        delayedMessage.sendDelayedRequests();
    }
    
    private void updateMediaPaths(final MessageObject messageObject, final TLRPC.Message message, int i, final String s, final boolean b) {
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        final TLRPC.MessageMedia media = messageOwner.media;
        if (media != null) {
            final TLRPC.Photo photo = media.photo;
            TLRPC.PhotoSize photoSize = null;
            TLRPC.PhotoSize photoSize2 = null;
            TLObject tlObject = null;
            Label_0401: {
                if (photo != null) {
                    photoSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 40);
                    Label_0088: {
                        if (message != null) {
                            final TLRPC.MessageMedia media2 = message.media;
                            if (media2 != null) {
                                final TLRPC.Photo photo2 = media2.photo;
                                if (photo2 != null) {
                                    photoSize2 = FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, 40);
                                    break Label_0088;
                                }
                            }
                        }
                        photoSize2 = photoSize;
                    }
                    tlObject = messageOwner.media.photo;
                }
                else {
                    final TLRPC.Document document = media.document;
                    if (document != null) {
                        photoSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 40);
                        Label_0171: {
                            if (message != null) {
                                final TLRPC.MessageMedia media3 = message.media;
                                if (media3 != null) {
                                    final TLRPC.Document document2 = media3.document;
                                    if (document2 != null) {
                                        photoSize2 = FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 40);
                                        break Label_0171;
                                    }
                                }
                            }
                            photoSize2 = photoSize;
                        }
                        tlObject = messageOwner.media.document;
                    }
                    else {
                        final TLRPC.WebPage webpage = media.webpage;
                        if (webpage != null) {
                            final TLRPC.Photo photo3 = webpage.photo;
                            if (photo3 != null) {
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(photo3.sizes, 40);
                                Label_0278: {
                                    if (message != null) {
                                        final TLRPC.MessageMedia media4 = message.media;
                                        if (media4 != null) {
                                            final TLRPC.WebPage webpage2 = media4.webpage;
                                            if (webpage2 != null) {
                                                final TLRPC.Photo photo4 = webpage2.photo;
                                                if (photo4 != null) {
                                                    photoSize2 = FileLoader.getClosestPhotoSizeWithSize(photo4.sizes, 40);
                                                    break Label_0278;
                                                }
                                            }
                                        }
                                    }
                                    photoSize2 = photoSize;
                                }
                                tlObject = messageOwner.media.webpage.photo;
                                break Label_0401;
                            }
                            final TLRPC.Document document3 = webpage.document;
                            if (document3 != null) {
                                photoSize = FileLoader.getClosestPhotoSizeWithSize(document3.thumbs, 40);
                                Label_0376: {
                                    if (message != null) {
                                        final TLRPC.MessageMedia media5 = message.media;
                                        if (media5 != null) {
                                            final TLRPC.WebPage webpage3 = media5.webpage;
                                            if (webpage3 != null) {
                                                final TLRPC.Document document4 = webpage3.document;
                                                if (document4 != null) {
                                                    photoSize2 = FileLoader.getClosestPhotoSizeWithSize(document4.thumbs, 40);
                                                    break Label_0376;
                                                }
                                            }
                                        }
                                    }
                                    photoSize2 = photoSize;
                                }
                                tlObject = messageOwner.media.webpage.document;
                                break Label_0401;
                            }
                        }
                        photoSize = null;
                        photoSize2 = null;
                        tlObject = null;
                    }
                }
            }
            if (photoSize2 instanceof TLRPC.TL_photoStrippedSize && photoSize instanceof TLRPC.TL_photoStrippedSize) {
                final StringBuilder sb = new StringBuilder();
                sb.append("stripped");
                sb.append(FileRefController.getKeyForParentObject(messageObject));
                final String string = sb.toString();
                String s2;
                if (message != null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("stripped");
                    sb2.append(FileRefController.getKeyForParentObject(message));
                    s2 = sb2.toString();
                }
                else {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("strippedmessage");
                    sb3.append(i);
                    sb3.append("_");
                    sb3.append(messageObject.getChannelId());
                    s2 = sb3.toString();
                }
                ImageLoader.getInstance().replaceImageInCache(string, s2, ImageLocation.getForObject(photoSize2, tlObject), b);
            }
        }
        if (message == null) {
            return;
        }
        final TLRPC.MessageMedia media6 = message.media;
        if (media6 instanceof TLRPC.TL_messageMediaPhoto && media6.photo != null) {
            final TLRPC.MessageMedia media7 = messageOwner.media;
            if (media7 instanceof TLRPC.TL_messageMediaPhoto && media7.photo != null) {
                if (media6.ttl_seconds == 0) {
                    final MessagesStorage instance = MessagesStorage.getInstance(this.currentAccount);
                    final TLRPC.Photo photo5 = message.media.photo;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("sent_");
                    sb4.append(message.to_id.channel_id);
                    sb4.append("_");
                    sb4.append(message.id);
                    instance.putSentFile(s, photo5, 0, sb4.toString());
                }
                if (messageOwner.media.photo.sizes.size() == 1 && messageOwner.media.photo.sizes.get(0).location instanceof TLRPC.TL_fileLocationUnavailable) {
                    messageOwner.media.photo.sizes = message.media.photo.sizes;
                }
                else {
                    TLRPC.PhotoSize photoSize3;
                    int j;
                    TLRPC.PhotoSize photoSize4;
                    TLRPC.FileLocation location;
                    String type;
                    StringBuilder sb5;
                    String string2;
                    StringBuilder sb6;
                    String string3;
                    File directory;
                    StringBuilder sb7;
                    File file;
                    TLRPC.MessageMedia media8;
                    File pathToAttach;
                    File directory2;
                    StringBuilder sb8;
                    for (i = 0; i < message.media.photo.sizes.size(); ++i) {
                        photoSize3 = message.media.photo.sizes.get(i);
                        if (photoSize3 != null && photoSize3.location != null && !(photoSize3 instanceof TLRPC.TL_photoSizeEmpty)) {
                            if (photoSize3.type != null) {
                                for (j = 0; j < messageOwner.media.photo.sizes.size(); ++j) {
                                    photoSize4 = messageOwner.media.photo.sizes.get(j);
                                    if (photoSize4 != null) {
                                        location = photoSize4.location;
                                        if (location != null) {
                                            type = photoSize4.type;
                                            if (type != null) {
                                                if ((location.volume_id == -2147483648L && photoSize3.type.equals(type)) || (photoSize3.w == photoSize4.w && photoSize3.h == photoSize4.h)) {
                                                    sb5 = new StringBuilder();
                                                    sb5.append(photoSize4.location.volume_id);
                                                    sb5.append("_");
                                                    sb5.append(photoSize4.location.local_id);
                                                    string2 = sb5.toString();
                                                    sb6 = new StringBuilder();
                                                    sb6.append(photoSize3.location.volume_id);
                                                    sb6.append("_");
                                                    sb6.append(photoSize3.location.local_id);
                                                    string3 = sb6.toString();
                                                    if (string2.equals(string3)) {
                                                        break;
                                                    }
                                                    directory = FileLoader.getDirectory(4);
                                                    sb7 = new StringBuilder();
                                                    sb7.append(string2);
                                                    sb7.append(".jpg");
                                                    file = new File(directory, sb7.toString());
                                                    media8 = message.media;
                                                    if (media8.ttl_seconds == 0 && (media8.photo.sizes.size() == 1 || photoSize3.w > 90 || photoSize3.h > 90)) {
                                                        pathToAttach = FileLoader.getPathToAttach(photoSize3);
                                                    }
                                                    else {
                                                        directory2 = FileLoader.getDirectory(4);
                                                        sb8 = new StringBuilder();
                                                        sb8.append(string3);
                                                        sb8.append(".jpg");
                                                        pathToAttach = new File(directory2, sb8.toString());
                                                    }
                                                    file.renameTo(pathToAttach);
                                                    ImageLoader.getInstance().replaceImageInCache(string2, string3, ImageLocation.getForPhoto(photoSize3, message.media.photo), b);
                                                    photoSize4.location = photoSize3.location;
                                                    photoSize4.size = photoSize3.size;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                message.message = messageOwner.message;
                message.attachPath = messageOwner.attachPath;
                final TLRPC.Photo photo6 = messageOwner.media.photo;
                final TLRPC.Photo photo7 = message.media.photo;
                photo6.id = photo7.id;
                photo6.access_hash = photo7.access_hash;
                return;
            }
        }
        final TLRPC.MessageMedia media9 = message.media;
        if (media9 instanceof TLRPC.TL_messageMediaDocument && media9.document != null) {
            final TLRPC.MessageMedia media10 = messageOwner.media;
            if (media10 instanceof TLRPC.TL_messageMediaDocument && media10.document != null) {
                if (media9.ttl_seconds == 0) {
                    final boolean videoMessage = MessageObject.isVideoMessage(message);
                    if ((videoMessage || MessageObject.isGifMessage(message)) && MessageObject.isGifDocument(message.media.document) == MessageObject.isGifDocument(messageOwner.media.document)) {
                        final MessagesStorage instance2 = MessagesStorage.getInstance(this.currentAccount);
                        final TLRPC.Document document5 = message.media.document;
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append("sent_");
                        sb9.append(message.to_id.channel_id);
                        sb9.append("_");
                        sb9.append(message.id);
                        instance2.putSentFile(s, document5, 2, sb9.toString());
                        if (videoMessage) {
                            message.attachPath = messageOwner.attachPath;
                        }
                    }
                    else if (!MessageObject.isVoiceMessage(message) && !MessageObject.isRoundVideoMessage(message)) {
                        final MessagesStorage instance3 = MessagesStorage.getInstance(this.currentAccount);
                        final TLRPC.Document document6 = message.media.document;
                        final StringBuilder sb10 = new StringBuilder();
                        sb10.append("sent_");
                        sb10.append(message.to_id.channel_id);
                        sb10.append("_");
                        sb10.append(message.id);
                        instance3.putSentFile(s, document6, 1, sb10.toString());
                    }
                }
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageOwner.media.document.thumbs, 320);
                final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(message.media.document.thumbs, 320);
                Label_2072: {
                    if (closestPhotoSizeWithSize != null) {
                        final TLRPC.FileLocation location2 = closestPhotoSizeWithSize.location;
                        if (location2 != null && location2.volume_id == -2147483648L && closestPhotoSizeWithSize2 != null && closestPhotoSizeWithSize2.location != null && !(closestPhotoSizeWithSize2 instanceof TLRPC.TL_photoSizeEmpty) && !(closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeEmpty)) {
                            final StringBuilder sb11 = new StringBuilder();
                            sb11.append(closestPhotoSizeWithSize.location.volume_id);
                            sb11.append("_");
                            sb11.append(closestPhotoSizeWithSize.location.local_id);
                            final String string4 = sb11.toString();
                            final StringBuilder sb12 = new StringBuilder();
                            sb12.append(closestPhotoSizeWithSize2.location.volume_id);
                            sb12.append("_");
                            sb12.append(closestPhotoSizeWithSize2.location.local_id);
                            final String string5 = sb12.toString();
                            if (!string4.equals(string5)) {
                                final File directory3 = FileLoader.getDirectory(4);
                                final StringBuilder sb13 = new StringBuilder();
                                sb13.append(string4);
                                sb13.append(".jpg");
                                final File file2 = new File(directory3, sb13.toString());
                                final File directory4 = FileLoader.getDirectory(4);
                                final StringBuilder sb14 = new StringBuilder();
                                sb14.append(string5);
                                sb14.append(".jpg");
                                file2.renameTo(new File(directory4, sb14.toString()));
                                ImageLoader.getInstance().replaceImageInCache(string4, string5, ImageLocation.getForDocument(closestPhotoSizeWithSize2, message.media.document), b);
                                closestPhotoSizeWithSize.location = closestPhotoSizeWithSize2.location;
                                closestPhotoSizeWithSize.size = closestPhotoSizeWithSize2.size;
                            }
                            break Label_2072;
                        }
                    }
                    if (closestPhotoSizeWithSize != null && MessageObject.isStickerMessage(message)) {
                        final TLRPC.FileLocation location3 = closestPhotoSizeWithSize.location;
                        if (location3 != null) {
                            closestPhotoSizeWithSize2.location = location3;
                            break Label_2072;
                        }
                    }
                    if (closestPhotoSizeWithSize == null || (closestPhotoSizeWithSize != null && closestPhotoSizeWithSize.location instanceof TLRPC.TL_fileLocationUnavailable) || closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeEmpty) {
                        messageOwner.media.document.thumbs = message.media.document.thumbs;
                    }
                }
                final TLRPC.Document document7 = messageOwner.media.document;
                final TLRPC.Document document8 = message.media.document;
                document7.dc_id = document8.dc_id;
                document7.id = document8.id;
                document7.access_hash = document8.access_hash;
                i = 0;
                while (true) {
                    while (i < messageOwner.media.document.attributes.size()) {
                        final TLRPC.DocumentAttribute documentAttribute = messageOwner.media.document.attributes.get(i);
                        if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                            final byte[] waveform = documentAttribute.waveform;
                            messageOwner.media.document.attributes = message.media.document.attributes;
                            if (waveform != null) {
                                TLRPC.DocumentAttribute documentAttribute2;
                                for (i = 0; i < messageOwner.media.document.attributes.size(); ++i) {
                                    documentAttribute2 = messageOwner.media.document.attributes.get(i);
                                    if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                                        documentAttribute2.waveform = waveform;
                                        documentAttribute2.flags |= 0x4;
                                    }
                                }
                            }
                            final TLRPC.Document document9 = messageOwner.media.document;
                            final TLRPC.Document document10 = message.media.document;
                            document9.size = document10.size;
                            document9.mime_type = document10.mime_type;
                            if ((message.flags & 0x4) == 0x0 && MessageObject.isOut(message)) {
                                if (MessageObject.isNewGifDocument(message.media.document)) {
                                    DataQuery.getInstance(this.currentAccount).addRecentGif(message.media.document, message.date);
                                }
                                else if (MessageObject.isStickerDocument(message.media.document)) {
                                    DataQuery.getInstance(this.currentAccount).addRecentSticker(0, message, message.media.document, message.date, false);
                                }
                            }
                            final String attachPath = messageOwner.attachPath;
                            if (attachPath == null || !attachPath.startsWith(FileLoader.getDirectory(4).getAbsolutePath())) {
                                message.attachPath = messageOwner.attachPath;
                                message.message = messageOwner.message;
                                return;
                            }
                            final File file3 = new File(messageOwner.attachPath);
                            final TLRPC.MessageMedia media11 = message.media;
                            final File pathToAttach2 = FileLoader.getPathToAttach(media11.document, media11.ttl_seconds != 0);
                            if (!file3.renameTo(pathToAttach2)) {
                                if (file3.exists()) {
                                    message.attachPath = messageOwner.attachPath;
                                }
                                else {
                                    messageObject.attachPathExists = false;
                                }
                                messageObject.mediaExists = pathToAttach2.exists();
                                message.message = messageOwner.message;
                                return;
                            }
                            if (MessageObject.isVideoMessage(message)) {
                                messageObject.attachPathExists = true;
                                return;
                            }
                            messageObject.mediaExists = messageObject.attachPathExists;
                            messageObject.attachPathExists = false;
                            messageOwner.attachPath = "";
                            if (s != null && s.startsWith("http")) {
                                MessagesStorage.getInstance(this.currentAccount).addRecentLocalFile(s, pathToAttach2.toString(), messageOwner.media.document);
                            }
                            return;
                        }
                        else {
                            ++i;
                        }
                    }
                    final byte[] waveform = null;
                    continue;
                }
            }
        }
        final TLRPC.MessageMedia media12 = message.media;
        if (media12 instanceof TLRPC.TL_messageMediaContact && messageOwner.media instanceof TLRPC.TL_messageMediaContact) {
            messageOwner.media = media12;
        }
        else {
            final TLRPC.MessageMedia media13 = message.media;
            if (media13 instanceof TLRPC.TL_messageMediaWebPage) {
                messageOwner.media = media13;
            }
            else if (media13 instanceof TLRPC.TL_messageMediaGeo) {
                final TLRPC.GeoPoint geo = media13.geo;
                final TLRPC.GeoPoint geo2 = messageOwner.media.geo;
                geo.lat = geo2.lat;
                geo._long = geo2._long;
            }
            else if (media13 instanceof TLRPC.TL_messageMediaGame) {
                messageOwner.media = media13;
                if (messageOwner.media instanceof TLRPC.TL_messageMediaGame && !TextUtils.isEmpty((CharSequence)message.message)) {
                    messageOwner.entities = message.entities;
                    messageOwner.message = message.message;
                }
            }
            else if (media13 instanceof TLRPC.TL_messageMediaPoll) {
                messageOwner.media = media13;
            }
        }
    }
    
    private void uploadMultiMedia(final DelayedMessage delayedMessage, final TLRPC.InputMedia media, final TLRPC.InputEncryptedFile inputEncryptedFile, final String s) {
        final Float value = 1.0f;
        final Boolean value2 = false;
        if (media != null) {
            final TLRPC.TL_messages_sendMultiMedia tl_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia)delayedMessage.sendRequest;
            for (int i = 0; i < tl_messages_sendMultiMedia.multi_media.size(); ++i) {
                if (tl_messages_sendMultiMedia.multi_media.get(i).media == media) {
                    this.putToSendingMessages(delayedMessage.messages.get(i));
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, s, value, value2);
                    break;
                }
            }
            final TLRPC.TL_messages_uploadMedia tl_messages_uploadMedia = new TLRPC.TL_messages_uploadMedia();
            tl_messages_uploadMedia.media = media;
            tl_messages_uploadMedia.peer = ((TLRPC.TL_messages_sendMultiMedia)delayedMessage.sendRequest).peer;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_uploadMedia, new _$$Lambda$SendMessagesHelper$9ikUx4RLVeYwBBYbegYSYKLcFew(this, media, delayedMessage));
        }
        else if (inputEncryptedFile != null) {
            final TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest;
            for (int j = 0; j < tl_messages_sendEncryptedMultiMedia.files.size(); ++j) {
                if (tl_messages_sendEncryptedMultiMedia.files.get(j) == inputEncryptedFile) {
                    this.putToSendingMessages(delayedMessage.messages.get(j));
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileUploadProgressChanged, s, value, value2);
                    break;
                }
            }
            this.sendReadyToSendGroup(delayedMessage, false, true);
        }
    }
    
    private void writePreviousMessageData(final TLRPC.Message message, final SerializedData serializedData) {
        message.media.serializeToStream(serializedData);
        String message2 = message.message;
        if (message2 == null) {
            message2 = "";
        }
        serializedData.writeString(message2);
        String attachPath = message.attachPath;
        if (attachPath == null) {
            attachPath = "";
        }
        serializedData.writeString(attachPath);
        final int size = message.entities.size();
        serializedData.writeInt32(size);
        for (int i = 0; i < size; ++i) {
            message.entities.get(i).serializeToStream(serializedData);
        }
    }
    
    public void cancelSendingMessage(final ArrayList<MessageObject> list) {
        final ArrayList<Object> list2 = new ArrayList<Object>();
        final ArrayList<Integer> list3 = new ArrayList<Integer>();
        int i = 0;
        boolean b = false;
        int n = 0;
        while (i < list.size()) {
            final MessageObject messageObject = list.get(i);
            list3.add(messageObject.getId());
            final int channel_id = messageObject.messageOwner.to_id.channel_id;
            final TLRPC.Message removeFromSendingMessages = this.removeFromSendingMessages(messageObject.getId());
            if (removeFromSendingMessages != null) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(removeFromSendingMessages.reqId, true);
            }
            for (final Map.Entry<String, ArrayList<DelayedMessage>> entry : this.delayedMessages.entrySet()) {
                final ArrayList<DelayedMessage> list4 = entry.getValue();
                int j = 0;
                while (j < list4.size()) {
                    final DelayedMessage delayedMessage = list4.get(j);
                    if (delayedMessage.type == 4) {
                        final int n2 = -1;
                        MessageObject key = null;
                        int index = 0;
                        int n3;
                        while (true) {
                            n3 = n2;
                            if (index >= delayedMessage.messageObjects.size()) {
                                break;
                            }
                            key = delayedMessage.messageObjects.get(index);
                            if (key.getId() == messageObject.getId()) {
                                n3 = index;
                                break;
                            }
                            ++index;
                        }
                        if (n3 < 0) {
                            break;
                        }
                        delayedMessage.messageObjects.remove(n3);
                        delayedMessage.messages.remove(n3);
                        delayedMessage.originalPaths.remove(n3);
                        final TLObject sendRequest = delayedMessage.sendRequest;
                        if (sendRequest != null) {
                            ((TLRPC.TL_messages_sendMultiMedia)sendRequest).multi_media.remove(n3);
                        }
                        else {
                            final TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessage.sendEncryptedRequest;
                            tl_messages_sendEncryptedMultiMedia.messages.remove(n3);
                            tl_messages_sendEncryptedMultiMedia.files.remove(n3);
                        }
                        MediaController.getInstance().cancelVideoConvert(messageObject);
                        final String e = delayedMessage.extraHashMap.get(key);
                        if (e != null) {
                            list2.add(e);
                        }
                        if (delayedMessage.messageObjects.isEmpty()) {
                            delayedMessage.sendDelayedRequests();
                            break;
                        }
                        if (delayedMessage.finalGroupMessage == messageObject.getId()) {
                            final ArrayList<MessageObject> messageObjects = delayedMessage.messageObjects;
                            final MessageObject messageObject2 = messageObjects.get(messageObjects.size() - 1);
                            delayedMessage.finalGroupMessage = messageObject2.getId();
                            messageObject2.messageOwner.params.put("final", "1");
                            final TLRPC.TL_messages_messages tl_messages_messages = new TLRPC.TL_messages_messages();
                            tl_messages_messages.messages.add(messageObject2.messageOwner);
                            MessagesStorage.getInstance(this.currentAccount).putMessages(tl_messages_messages, delayedMessage.peer, -2, 0, false);
                        }
                        this.sendReadyToSendGroup(delayedMessage, false, true);
                        break;
                    }
                    else if (delayedMessage.obj.getId() == messageObject.getId()) {
                        list4.remove(j);
                        delayedMessage.sendDelayedRequests();
                        MediaController.getInstance().cancelVideoConvert(delayedMessage.obj);
                        if (list4.size() != 0) {
                            break;
                        }
                        list2.add(entry.getKey());
                        if (delayedMessage.sendEncryptedRequest != null) {
                            b = true;
                            break;
                        }
                        break;
                    }
                    else {
                        ++j;
                    }
                }
            }
            ++i;
            n = channel_id;
        }
        for (int k = 0; k < list2.size(); ++k) {
            final String key2 = list2.get(k);
            if (key2.startsWith("http")) {
                ImageLoader.getInstance().cancelLoadHttpFile(key2);
            }
            else {
                FileLoader.getInstance(this.currentAccount).cancelUploadFile(key2, b);
            }
            this.stopVideoService(key2);
            this.delayedMessages.remove(key2);
        }
        if (list.size() == 1 && list.get(0).isEditing() && list.get(0).previousMedia != null) {
            this.revertEditingMessageObject(list.get(0));
        }
        else {
            MessagesController.getInstance(this.currentAccount).deleteMessages(list3, null, null, n, false);
        }
    }
    
    public void cancelSendingMessage(final MessageObject e) {
        final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
        list.add(e);
        this.cancelSendingMessage(list);
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
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        final int fileDidUpload = NotificationCenter.FileDidUpload;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        j = 0;
        if (i == fileDidUpload) {
            final String key = (String)array[0];
            final TLRPC.InputFile file = (TLRPC.InputFile)array[1];
            TLRPC.InputEncryptedFile inputEncryptedFile = (TLRPC.InputEncryptedFile)array[2];
            ArrayList<DelayedMessage> list = this.delayedMessages.get(key);
            if (list != null) {
                DelayedMessage delayedMessage;
                TLObject sendRequest;
                TLRPC.InputMedia inputMedia;
                TLRPC.InputFile inputFile;
                DelayedMessage delayedMessage2;
                TLRPC.PhotoSize photoSize;
                TLRPC.PhotoSize photoSize2;
                HashMap<Object, Object> extraHashMap;
                StringBuilder sb;
                HashMap<Object, Object> extraHashMap2;
                StringBuilder sb2;
                HashMap<Object, Object> extraHashMap3;
                StringBuilder sb3;
                ArrayList<DelayedMessage> list2;
                TLRPC.InputEncryptedFile element;
                TLObject sendEncryptedRequest;
                TLRPC.TL_messages_sendEncryptedMultiMedia tl_messages_sendEncryptedMultiMedia;
                TLRPC.InputEncryptedFile o;
                HashMap<Object, Object> extraHashMap4;
                StringBuilder sb4;
                MessageObject messageObject;
                HashMap<Object, Object> extraHashMap5;
                StringBuilder sb5;
                TLRPC.TL_decryptedMessage tl_decryptedMessage;
                TLRPC.DecryptedMessageMedia media;
                TLRPC.DecryptedMessageMedia media2;
                SecretChatHelper instance;
                MessageObject obj;
                for (i = j; i < list.size(); i = j + 1, list = list2) {
                    delayedMessage = list.get(i);
                    sendRequest = delayedMessage.sendRequest;
                    if (sendRequest instanceof TLRPC.TL_messages_sendMedia) {
                        inputMedia = ((TLRPC.TL_messages_sendMedia)sendRequest).media;
                    }
                    else if (sendRequest instanceof TLRPC.TL_messages_editMessage) {
                        inputMedia = ((TLRPC.TL_messages_editMessage)sendRequest).media;
                    }
                    else if (sendRequest instanceof TLRPC.TL_messages_sendBroadcast) {
                        inputMedia = ((TLRPC.TL_messages_sendBroadcast)sendRequest).media;
                    }
                    else if (sendRequest instanceof TLRPC.TL_messages_sendMultiMedia) {
                        inputMedia = delayedMessage.extraHashMap.get(key);
                    }
                    else {
                        inputMedia = null;
                    }
                    if (file != null && inputMedia != null) {
                        j = delayedMessage.type;
                        Label_0244: {
                            if (j == 0) {
                                inputMedia.file = file;
                                this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, delayedMessage, true, null, delayedMessage.parentObject);
                            }
                            else {
                                inputFile = file;
                                if (j == 1) {
                                    if (inputMedia.file == null) {
                                        inputMedia.file = inputFile;
                                        if (inputMedia.thumb == null) {
                                            delayedMessage2 = delayedMessage;
                                            photoSize = delayedMessage2.photoSize;
                                            if (photoSize != null && photoSize.location != null) {
                                                this.performSendDelayedMessage(delayedMessage2);
                                                break Label_0244;
                                            }
                                        }
                                        this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject);
                                    }
                                    else {
                                        inputMedia.thumb = inputFile;
                                        inputMedia.flags |= 0x4;
                                        this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject);
                                    }
                                }
                                else if (j == 2) {
                                    if (inputMedia.file == null) {
                                        inputMedia.file = inputFile;
                                        if (inputMedia.thumb == null) {
                                            photoSize2 = delayedMessage.photoSize;
                                            if (photoSize2 != null && photoSize2.location != null) {
                                                this.performSendDelayedMessage(delayedMessage);
                                                break Label_0244;
                                            }
                                        }
                                        this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject);
                                    }
                                    else {
                                        inputMedia.thumb = inputFile;
                                        inputMedia.flags |= 0x4;
                                        this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject);
                                    }
                                }
                                else if (j == 3) {
                                    inputMedia.file = inputFile;
                                    this.performSendMessageRequest(delayedMessage.sendRequest, delayedMessage.obj, delayedMessage.originalPath, null, delayedMessage.parentObject);
                                }
                                else if (j == 4) {
                                    if (inputMedia instanceof TLRPC.TL_inputMediaUploadedDocument) {
                                        if (inputMedia.file == null) {
                                            inputMedia.file = inputFile;
                                            extraHashMap = delayedMessage.extraHashMap;
                                            sb = new StringBuilder();
                                            sb.append(key);
                                            sb.append("_i");
                                            j = delayedMessage.messageObjects.indexOf(extraHashMap.get(sb.toString()));
                                            extraHashMap2 = delayedMessage.extraHashMap;
                                            sb2 = new StringBuilder();
                                            sb2.append(key);
                                            sb2.append("_t");
                                            delayedMessage.photoSize = extraHashMap2.get(sb2.toString());
                                            this.stopVideoService(delayedMessage.messageObjects.get(j).messageOwner.attachPath);
                                            if (inputMedia.thumb == null && delayedMessage.photoSize != null) {
                                                delayedMessage.performMediaUpload = true;
                                                this.performSendDelayedMessage(delayedMessage, j);
                                            }
                                            else {
                                                this.uploadMultiMedia(delayedMessage, inputMedia, null, key);
                                            }
                                        }
                                        else {
                                            inputMedia.thumb = inputFile;
                                            inputMedia.flags |= 0x4;
                                            extraHashMap3 = delayedMessage.extraHashMap;
                                            sb3 = new StringBuilder();
                                            sb3.append(key);
                                            sb3.append("_o");
                                            this.uploadMultiMedia(delayedMessage, inputMedia, null, extraHashMap3.get(sb3.toString()));
                                        }
                                    }
                                    else {
                                        inputMedia.file = inputFile;
                                        this.uploadMultiMedia(delayedMessage, inputMedia, null, key);
                                    }
                                }
                            }
                        }
                        list2 = list;
                        list2.remove(i);
                        j = i - 1;
                    }
                    else {
                        element = inputEncryptedFile;
                        list2 = list;
                        inputEncryptedFile = element;
                        j = i;
                        if (element != null) {
                            sendEncryptedRequest = delayedMessage.sendEncryptedRequest;
                            list2 = list;
                            inputEncryptedFile = element;
                            j = i;
                            if (sendEncryptedRequest != null) {
                                if (delayedMessage.type == 4) {
                                    tl_messages_sendEncryptedMultiMedia = (TLRPC.TL_messages_sendEncryptedMultiMedia)sendEncryptedRequest;
                                    o = delayedMessage.extraHashMap.get(key);
                                    j = tl_messages_sendEncryptedMultiMedia.files.indexOf(o);
                                    if (j >= 0) {
                                        tl_messages_sendEncryptedMultiMedia.files.set(j, element);
                                        if (o.id == 1L) {
                                            extraHashMap4 = delayedMessage.extraHashMap;
                                            sb4 = new StringBuilder();
                                            sb4.append(key);
                                            sb4.append("_i");
                                            messageObject = extraHashMap4.get(sb4.toString());
                                            extraHashMap5 = delayedMessage.extraHashMap;
                                            sb5 = new StringBuilder();
                                            sb5.append(key);
                                            sb5.append("_t");
                                            delayedMessage.photoSize = extraHashMap5.get(sb5.toString());
                                            this.stopVideoService(delayedMessage.messageObjects.get(j).messageOwner.attachPath);
                                        }
                                        tl_decryptedMessage = tl_messages_sendEncryptedMultiMedia.messages.get(j);
                                    }
                                    else {
                                        tl_decryptedMessage = null;
                                    }
                                }
                                else {
                                    tl_decryptedMessage = (TLRPC.TL_decryptedMessage)sendEncryptedRequest;
                                }
                                if (tl_decryptedMessage != null) {
                                    media = tl_decryptedMessage.media;
                                    if (media instanceof TLRPC.TL_decryptedMessageMediaVideo || media instanceof TLRPC.TL_decryptedMessageMediaPhoto || media instanceof TLRPC.TL_decryptedMessageMediaDocument) {
                                        tl_decryptedMessage.media.size = (int)(long)array[5];
                                    }
                                    media2 = tl_decryptedMessage.media;
                                    media2.key = (byte[])array[3];
                                    media2.iv = (byte[])array[4];
                                    if (delayedMessage.type == 4) {
                                        this.uploadMultiMedia(delayedMessage, null, element, key);
                                    }
                                    else {
                                        instance = SecretChatHelper.getInstance(this.currentAccount);
                                        obj = delayedMessage.obj;
                                        instance.performSendEncryptedRequest(tl_decryptedMessage, obj.messageOwner, delayedMessage.encryptedChat, element, delayedMessage.originalPath, obj);
                                    }
                                }
                                list.remove(i);
                                j = i - 1;
                                inputEncryptedFile = element;
                                list2 = list;
                            }
                        }
                    }
                }
                if (list.isEmpty()) {
                    this.delayedMessages.remove(key);
                }
            }
        }
        else if (i == NotificationCenter.FileDidFailUpload) {
            final String s = (String)array[0];
            final boolean booleanValue = (boolean)array[1];
            final ArrayList<DelayedMessage> list3 = this.delayedMessages.get(s);
            if (list3 != null) {
                DelayedMessage delayedMessage3;
                for (i = n; i < list3.size(); i = j + 1) {
                    delayedMessage3 = list3.get(i);
                    if (!booleanValue || delayedMessage3.sendEncryptedRequest == null) {
                        j = i;
                        if (booleanValue) {
                            continue;
                        }
                        j = i;
                        if (delayedMessage3.sendRequest == null) {
                            continue;
                        }
                    }
                    delayedMessage3.markAsError();
                    list3.remove(i);
                    j = i - 1;
                }
                if (list3.isEmpty()) {
                    this.delayedMessages.remove(s);
                }
            }
        }
        else if (i == NotificationCenter.filePreparingStarted) {
            final MessageObject o2 = (MessageObject)array[0];
            if (o2.getId() == 0) {
                return;
            }
            final String s2 = (String)array[1];
            final ArrayList<DelayedMessage> list4 = this.delayedMessages.get(o2.messageOwner.attachPath);
            if (list4 != null) {
                DelayedMessage delayedMessage4;
                HashMap<Object, Object> extraHashMap6;
                StringBuilder sb6;
                for (i = n2; i < list4.size(); ++i) {
                    delayedMessage4 = list4.get(i);
                    if (delayedMessage4.type == 4) {
                        j = delayedMessage4.messageObjects.indexOf(o2);
                        extraHashMap6 = delayedMessage4.extraHashMap;
                        sb6 = new StringBuilder();
                        sb6.append(o2.messageOwner.attachPath);
                        sb6.append("_t");
                        delayedMessage4.photoSize = extraHashMap6.get(sb6.toString());
                        delayedMessage4.performMediaUpload = true;
                        this.performSendDelayedMessage(delayedMessage4, j);
                        list4.remove(i);
                        break;
                    }
                    if (delayedMessage4.obj == o2) {
                        delayedMessage4.videoEditedInfo = null;
                        this.performSendDelayedMessage(delayedMessage4);
                        list4.remove(i);
                        break;
                    }
                }
                if (list4.isEmpty()) {
                    this.delayedMessages.remove(o2.messageOwner.attachPath);
                }
            }
        }
        else if (i == NotificationCenter.fileNewChunkAvailable) {
            final MessageObject messageObject2 = (MessageObject)array[0];
            if (messageObject2.getId() == 0) {
                return;
            }
            final String s3 = (String)array[1];
            final long longValue = (long)array[2];
            final long longValue2 = (long)array[3];
            FileLoader.getInstance(this.currentAccount).checkUploadNewDataAvailable(s3, (int)messageObject2.getDialogId() == 0, longValue, longValue2);
            if (longValue2 != 0L) {
                this.stopVideoService(messageObject2.messageOwner.attachPath);
                final ArrayList<DelayedMessage> list5 = this.delayedMessages.get(messageObject2.messageOwner.attachPath);
                if (list5 != null) {
                    DelayedMessage delayedMessage5;
                    MessageObject messageObject3;
                    ArrayList<TLRPC.Message> list6;
                    MessageObject obj2;
                    ArrayList<TLRPC.Message> list7;
                    for (i = 0; i < list5.size(); ++i) {
                        delayedMessage5 = list5.get(i);
                        if (delayedMessage5.type == 4) {
                            for (j = 0; j < delayedMessage5.messageObjects.size(); ++j) {
                                messageObject3 = delayedMessage5.messageObjects.get(j);
                                if (messageObject3 == messageObject2) {
                                    messageObject3.videoEditedInfo = null;
                                    messageObject3.messageOwner.params.remove("ve");
                                    messageObject3.messageOwner.media.document.size = (int)longValue2;
                                    list6 = new ArrayList<TLRPC.Message>();
                                    list6.add(messageObject3.messageOwner);
                                    MessagesStorage.getInstance(this.currentAccount).putMessages(list6, false, true, false, 0);
                                    break;
                                }
                            }
                        }
                        else {
                            obj2 = delayedMessage5.obj;
                            if (obj2 == messageObject2) {
                                obj2.videoEditedInfo = null;
                                obj2.messageOwner.params.remove("ve");
                                delayedMessage5.obj.messageOwner.media.document.size = (int)longValue2;
                                list7 = new ArrayList<TLRPC.Message>();
                                list7.add(delayedMessage5.obj.messageOwner);
                                MessagesStorage.getInstance(this.currentAccount).putMessages(list7, false, true, false, 0);
                                break;
                            }
                        }
                    }
                }
            }
        }
        else if (i == NotificationCenter.filePreparingFailed) {
            final MessageObject messageObject4 = (MessageObject)array[0];
            if (messageObject4.getId() == 0) {
                return;
            }
            final String s4 = (String)array[1];
            this.stopVideoService(messageObject4.messageOwner.attachPath);
            final ArrayList<DelayedMessage> list8 = this.delayedMessages.get(s4);
            if (list8 != null) {
            Label_2270:
                for (i = 0; i < list8.size(); i = j + 1) {
                    final DelayedMessage delayedMessage6 = list8.get(i);
                    if (delayedMessage6.type == 4) {
                        int index = 0;
                        while (true) {
                            j = i;
                            if (index >= delayedMessage6.messages.size()) {
                                continue Label_2270;
                            }
                            if (delayedMessage6.messageObjects.get(index) == messageObject4) {
                                delayedMessage6.markAsError();
                                list8.remove(i);
                                break;
                            }
                            ++index;
                        }
                    }
                    else {
                        j = i;
                        if (delayedMessage6.obj != messageObject4) {
                            continue;
                        }
                        delayedMessage6.markAsError();
                        list8.remove(i);
                    }
                    j = i - 1;
                }
                if (list8.isEmpty()) {
                    this.delayedMessages.remove(s4);
                }
            }
        }
        else if (i == NotificationCenter.httpFileDidLoad) {
            final String key2 = (String)array[0];
            final ArrayList<DelayedMessage> list9 = this.delayedMessages.get(key2);
            if (list9 != null) {
                DelayedMessage delayedMessage7;
                MessageObject messageObject5 = null;
                StringBuilder sb7;
                StringBuilder sb8;
                for (j = 0; j < list9.size(); ++j) {
                    delayedMessage7 = list9.get(j);
                    i = delayedMessage7.type;
                    Label_2428: {
                        if (i == 0) {
                            messageObject5 = delayedMessage7.obj;
                            i = 0;
                        }
                        else {
                            if (i == 2) {
                                messageObject5 = delayedMessage7.obj;
                            }
                            else {
                                if (i != 4) {
                                    i = -1;
                                    messageObject5 = null;
                                    break Label_2428;
                                }
                                messageObject5 = delayedMessage7.extraHashMap.get(key2);
                                if (messageObject5.getDocument() == null) {
                                    i = 0;
                                    break Label_2428;
                                }
                            }
                            i = 1;
                        }
                    }
                    if (i == 0) {
                        sb7 = new StringBuilder();
                        sb7.append(Utilities.MD5(key2));
                        sb7.append(".");
                        sb7.append(ImageLoader.getHttpUrlExtension(key2, "file"));
                        Utilities.globalQueue.postRunnable(new _$$Lambda$SendMessagesHelper$1kOX99gMEbip9sYs_E7UQv_97eY(this, new File(FileLoader.getDirectory(4), sb7.toString()), messageObject5, delayedMessage7, key2));
                    }
                    else if (i == 1) {
                        sb8 = new StringBuilder();
                        sb8.append(Utilities.MD5(key2));
                        sb8.append(".gif");
                        Utilities.globalQueue.postRunnable(new _$$Lambda$SendMessagesHelper$pp0U4GJ1r75dDYF4YGnbf9kI6EU(this, delayedMessage7, new File(FileLoader.getDirectory(4), sb8.toString()), messageObject5));
                    }
                }
                this.delayedMessages.remove(key2);
            }
        }
        else if (i == NotificationCenter.fileDidLoad) {
            final String s5 = (String)array[0];
            final ArrayList<DelayedMessage> list10 = this.delayedMessages.get(s5);
            if (list10 != null) {
                for (i = n3; i < list10.size(); ++i) {
                    this.performSendDelayedMessage(list10.get(i));
                }
                this.delayedMessages.remove(s5);
            }
        }
        else if (i == NotificationCenter.httpFileDidFailedLoad || i == NotificationCenter.fileDidFailedLoad) {
            final String s6 = (String)array[0];
            final ArrayList<DelayedMessage> list11 = this.delayedMessages.get(s6);
            if (list11 != null) {
                for (i = n4; i < list11.size(); ++i) {
                    list11.get(i).markAsError();
                }
                this.delayedMessages.remove(s6);
            }
        }
    }
    
    public int editMessage(final MessageObject messageObject, final String message, final boolean b, final BaseFragment baseFragment, final ArrayList<TLRPC.MessageEntity> entities, final Runnable runnable) {
        if (baseFragment != null && baseFragment.getParentActivity() != null && runnable != null) {
            final TLRPC.TL_messages_editMessage tl_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tl_messages_editMessage.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)messageObject.getDialogId());
            tl_messages_editMessage.message = message;
            tl_messages_editMessage.flags |= 0x800;
            tl_messages_editMessage.id = messageObject.getId();
            tl_messages_editMessage.no_webpage = (b ^ true);
            if (entities != null) {
                tl_messages_editMessage.entities = entities;
                tl_messages_editMessage.flags |= 0x8;
            }
            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_editMessage, new _$$Lambda$SendMessagesHelper$0k3RdsQSyxpPqVyuBg3ZosCuce8(this, baseFragment, tl_messages_editMessage, runnable));
        }
        return 0;
    }
    
    public TLRPC.TL_photo generatePhotoSizes(final String s, final Uri uri) {
        return this.generatePhotoSizes(null, s, uri);
    }
    
    public TLRPC.TL_photo generatePhotoSizes(final TLRPC.TL_photo tl_photo, final String s, final Uri uri) {
        Bitmap bitmap;
        if ((bitmap = ImageLoader.loadBitmap(s, uri, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), true)) == null) {
            bitmap = ImageLoader.loadBitmap(s, uri, 800.0f, 800.0f, true);
        }
        final ArrayList<TLRPC.PhotoSize> sizes = new ArrayList<TLRPC.PhotoSize>();
        final TLRPC.PhotoSize scaleAndSaveImage = ImageLoader.scaleAndSaveImage(bitmap, 90.0f, 90.0f, 55, true);
        if (scaleAndSaveImage != null) {
            sizes.add(scaleAndSaveImage);
        }
        final TLRPC.PhotoSize scaleAndSaveImage2 = ImageLoader.scaleAndSaveImage(bitmap, (float)AndroidUtilities.getPhotoSize(), (float)AndroidUtilities.getPhotoSize(), 80, false, 101, 101);
        if (scaleAndSaveImage2 != null) {
            sizes.add(scaleAndSaveImage2);
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (sizes.isEmpty()) {
            return null;
        }
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
        TLRPC.TL_photo tl_photo2;
        if ((tl_photo2 = tl_photo) == null) {
            tl_photo2 = new TLRPC.TL_photo();
        }
        tl_photo2.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        tl_photo2.sizes = sizes;
        tl_photo2.file_reference = new byte[0];
        return tl_photo2;
    }
    
    protected ArrayList<DelayedMessage> getDelayedMessages(final String key) {
        return this.delayedMessages.get(key);
    }
    
    protected long getNextRandomId() {
        long nextLong;
        for (nextLong = 0L; nextLong == 0L; nextLong = Utilities.random.nextLong()) {}
        return nextLong;
    }
    
    protected long getVoteSendTime(final long n) {
        return (long)this.voteSendTime.get(n, (Object)0L);
    }
    
    public boolean isSendingCallback(final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton) {
        int i = 0;
        if (messageObject != null && keyboardButton != null) {
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth) {
                i = 3;
            }
            else if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                i = 1;
            }
            else if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                i = 2;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(messageObject.getDialogId());
            sb.append("_");
            sb.append(messageObject.getId());
            sb.append("_");
            sb.append(Utilities.bytesToHex(keyboardButton.data));
            sb.append("_");
            sb.append(i);
            return this.waitingForCallback.containsKey(sb.toString());
        }
        return false;
    }
    
    public boolean isSendingCurrentLocation(final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton) {
        if (messageObject != null && keyboardButton != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(messageObject.getDialogId());
            sb.append("_");
            sb.append(messageObject.getId());
            sb.append("_");
            sb.append(Utilities.bytesToHex(keyboardButton.data));
            sb.append("_");
            String str;
            if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                str = "1";
            }
            else {
                str = "0";
            }
            sb.append(str);
            return this.waitingForLocation.containsKey(sb.toString());
        }
        return false;
    }
    
    public boolean isSendingMessage(final int n) {
        return this.sendingMessages.indexOfKey(n) >= 0;
    }
    
    public byte[] isSendingVote(final MessageObject messageObject) {
        if (messageObject == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("poll_");
        sb.append(messageObject.getPollId());
        return this.waitingForVote.get(sb.toString());
    }
    
    protected void performSendMessageRequest(final TLObject tlObject, final MessageObject messageObject, final String s, final DelayedMessage delayedMessage, final boolean b, final DelayedMessage delayedMessage2, final Object o) {
        if (!(tlObject instanceof TLRPC.TL_messages_editMessage) && b) {
            final DelayedMessage maxDelayedMessageForMessageId = this.findMaxDelayedMessageForMessageId(messageObject.getId(), messageObject.getDialogId());
            if (maxDelayedMessageForMessageId != null) {
                maxDelayedMessageForMessageId.addDelayedRequest(tlObject, messageObject, s, o, delayedMessage2);
                if (delayedMessage != null) {
                    final ArrayList<DelayedMessageSendAfterRequest> requests = delayedMessage.requests;
                    if (requests != null) {
                        maxDelayedMessageForMessageId.requests.addAll(requests);
                    }
                }
                return;
            }
        }
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        this.putToSendingMessages(messageOwner);
        final ConnectionsManager instance = ConnectionsManager.getInstance(this.currentAccount);
        final _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk $$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk = new _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk(this, tlObject, o, messageObject, s, delayedMessage, b, delayedMessage2, messageOwner);
        final _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg $$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg = new _$$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg(this, messageOwner);
        int n;
        if (tlObject instanceof TLRPC.TL_messages_sendMessage) {
            n = 128;
        }
        else {
            n = 0;
        }
        messageOwner.reqId = instance.sendRequest(tlObject, $$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk, $$Lambda$SendMessagesHelper$_wu1JxbPB7vnkTrFhWBgUdpKleg, n | 0x44);
        if (delayedMessage != null) {
            delayedMessage.sendDelayedRequests();
        }
    }
    
    protected void performSendMessageRequestMulti(final TLRPC.TL_messages_sendMultiMedia tl_messages_sendMultiMedia, final ArrayList<MessageObject> list, final ArrayList<String> list2, final ArrayList<Object> list3, final DelayedMessage delayedMessage) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            this.putToSendingMessages(list.get(i).messageOwner);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_sendMultiMedia, new _$$Lambda$SendMessagesHelper$KTc_sr8270evlRbeKgXtKisOYgM(this, list3, tl_messages_sendMultiMedia, list, list2, delayedMessage), null, 68);
    }
    
    public void processForwardFromMyName(final MessageObject messageObject, final long n) {
        if (messageObject == null) {
            return;
        }
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        final TLRPC.MessageMedia media = messageOwner.media;
        if (media != null && !(media instanceof TLRPC.TL_messageMediaEmpty) && !(media instanceof TLRPC.TL_messageMediaWebPage) && !(media instanceof TLRPC.TL_messageMediaGame) && !(media instanceof TLRPC.TL_messageMediaInvoice)) {
            final int n2 = (int)n;
            HashMap<String, String> hashMap;
            if (n2 == 0 && messageOwner.to_id != null && (media.photo instanceof TLRPC.TL_photo || media.document instanceof TLRPC.TL_document)) {
                hashMap = new HashMap<String, String>();
                final StringBuilder sb = new StringBuilder();
                sb.append("sent_");
                sb.append(messageObject.messageOwner.to_id.channel_id);
                sb.append("_");
                sb.append(messageObject.getId());
                hashMap.put("parentObject", sb.toString());
            }
            else {
                hashMap = null;
            }
            final TLRPC.Message messageOwner2 = messageObject.messageOwner;
            final TLRPC.MessageMedia media2 = messageOwner2.media;
            final TLRPC.Photo photo = media2.photo;
            if (photo instanceof TLRPC.TL_photo) {
                this.sendMessage((TLRPC.TL_photo)photo, null, n, messageObject.replyMessageObject, messageOwner2.message, messageOwner2.entities, null, hashMap, media2.ttl_seconds, messageObject);
            }
            else {
                final TLRPC.Document document = media2.document;
                if (document instanceof TLRPC.TL_document) {
                    this.sendMessage((TLRPC.TL_document)document, null, messageOwner2.attachPath, n, messageObject.replyMessageObject, messageOwner2.message, messageOwner2.entities, null, hashMap, media2.ttl_seconds, messageObject);
                }
                else if (!(media2 instanceof TLRPC.TL_messageMediaVenue) && !(media2 instanceof TLRPC.TL_messageMediaGeo)) {
                    if (media2.phone_number != null) {
                        final TLRPC.TL_userContact_old2 tl_userContact_old2 = new TLRPC.TL_userContact_old2();
                        final TLRPC.MessageMedia media3 = messageObject.messageOwner.media;
                        tl_userContact_old2.phone = media3.phone_number;
                        tl_userContact_old2.first_name = media3.first_name;
                        tl_userContact_old2.last_name = media3.last_name;
                        tl_userContact_old2.id = media3.user_id;
                        this.sendMessage(tl_userContact_old2, n, messageObject.replyMessageObject, null, null);
                    }
                    else if (n2 != 0) {
                        final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                        list.add(messageObject);
                        this.sendMessage(list, n);
                    }
                }
                else {
                    this.sendMessage(messageObject.messageOwner.media, n, messageObject.replyMessageObject, null, null);
                }
            }
        }
        else {
            final TLRPC.Message messageOwner3 = messageObject.messageOwner;
            if (messageOwner3.message != null) {
                final TLRPC.MessageMedia media4 = messageOwner3.media;
                TLRPC.WebPage webpage;
                if (media4 instanceof TLRPC.TL_messageMediaWebPage) {
                    webpage = media4.webpage;
                }
                else {
                    webpage = null;
                }
                final ArrayList<TLRPC.MessageEntity> entities = messageObject.messageOwner.entities;
                ArrayList<TLRPC.MessageEntity> list2;
                if (entities != null && !entities.isEmpty()) {
                    list2 = new ArrayList<TLRPC.MessageEntity>();
                    for (int i = 0; i < messageObject.messageOwner.entities.size(); ++i) {
                        final TLRPC.MessageEntity e = messageObject.messageOwner.entities.get(i);
                        if (e instanceof TLRPC.TL_messageEntityBold || e instanceof TLRPC.TL_messageEntityItalic || e instanceof TLRPC.TL_messageEntityPre || e instanceof TLRPC.TL_messageEntityCode || e instanceof TLRPC.TL_messageEntityTextUrl) {
                            list2.add(e);
                        }
                    }
                }
                else {
                    list2 = null;
                }
                this.sendMessage(messageObject.messageOwner.message, n, messageObject.replyMessageObject, webpage, true, list2, null, null);
            }
            else if ((int)n != 0) {
                final ArrayList<MessageObject> list3 = new ArrayList<MessageObject>();
                list3.add(messageObject);
                this.sendMessage(list3, n);
            }
        }
    }
    
    protected void processSentMessage(final int n) {
        final int size = this.unsentMessages.size();
        this.unsentMessages.remove(n);
        if (size != 0 && this.unsentMessages.size() == 0) {
            this.checkUnsentMessages();
        }
    }
    
    protected void processUnsentMessages(final ArrayList<TLRPC.Message> list, final ArrayList<TLRPC.User> list2, final ArrayList<TLRPC.Chat> list3, final ArrayList<TLRPC.EncryptedChat> list4) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$uLBi7R9ZOYopc_Gzh_Qk8zK9YAo(this, list2, list3, list4, list));
    }
    
    protected void putToSendingMessages(final TLRPC.Message message) {
        this.sendingMessages.put(message.id, (Object)message);
    }
    
    protected TLRPC.Message removeFromSendingMessages(final int n) {
        final TLRPC.Message message = (TLRPC.Message)this.sendingMessages.get(n);
        if (message != null) {
            this.sendingMessages.remove(n);
        }
        return message;
    }
    
    public boolean retrySendMessage(final MessageObject messageObject, final boolean b) {
        if (messageObject.getId() >= 0) {
            if (messageObject.isEditing()) {
                this.editMessageMedia(messageObject, null, null, null, null, null, true, messageObject);
            }
            return false;
        }
        final TLRPC.MessageAction action = messageObject.messageOwner.action;
        if (!(action instanceof TLRPC.TL_messageEncryptedAction)) {
            if (action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser((int)messageObject.getDialogId());
                final TLRPC.Message messageOwner = messageObject.messageOwner;
                this.sendScreenshotMessage(user, messageOwner.reply_to_msg_id, messageOwner);
            }
            if (b) {
                this.unsentMessages.put(messageObject.getId(), (Object)messageObject);
            }
            this.sendMessage(messageObject);
            return true;
        }
        final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat((int)(messageObject.getDialogId() >> 32));
        if (encryptedChat == null) {
            MessagesStorage.getInstance(this.currentAccount).markMessageAsSendError(messageObject.messageOwner);
            messageObject.messageOwner.send_state = 2;
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageSendError, messageObject.getId());
            this.processSentMessage(messageObject.getId());
            return false;
        }
        final TLRPC.Message messageOwner2 = messageObject.messageOwner;
        if (messageOwner2.random_id == 0L) {
            messageOwner2.random_id = this.getNextRandomId();
        }
        final TLRPC.DecryptedMessageAction encryptedAction = messageObject.messageOwner.action.encryptedAction;
        if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
            SecretChatHelper.getInstance(this.currentAccount).sendTTLMessage(encryptedChat, messageObject.messageOwner);
        }
        else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionDeleteMessages) {
            SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(encryptedChat, null, messageObject.messageOwner);
        }
        else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionFlushHistory) {
            SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(encryptedChat, messageObject.messageOwner);
        }
        else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionNotifyLayer) {
            SecretChatHelper.getInstance(this.currentAccount).sendNotifyLayerMessage(encryptedChat, messageObject.messageOwner);
        }
        else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionReadMessages) {
            SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(encryptedChat, null, messageObject.messageOwner);
        }
        else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionScreenshotMessages) {
            SecretChatHelper.getInstance(this.currentAccount).sendScreenshotMessage(encryptedChat, null, messageObject.messageOwner);
        }
        else if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionTyping)) {
            if (!(encryptedAction instanceof TLRPC.TL_decryptedMessageActionResend)) {
                if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionCommitKey) {
                    SecretChatHelper.getInstance(this.currentAccount).sendCommitKeyMessage(encryptedChat, messageObject.messageOwner);
                }
                else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionAbortKey) {
                    SecretChatHelper.getInstance(this.currentAccount).sendAbortKeyMessage(encryptedChat, messageObject.messageOwner, 0L);
                }
                else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionRequestKey) {
                    SecretChatHelper.getInstance(this.currentAccount).sendRequestKeyMessage(encryptedChat, messageObject.messageOwner);
                }
                else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionAcceptKey) {
                    SecretChatHelper.getInstance(this.currentAccount).sendAcceptKeyMessage(encryptedChat, messageObject.messageOwner);
                }
                else if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionNoop) {
                    SecretChatHelper.getInstance(this.currentAccount).sendNoopMessage(encryptedChat, messageObject.messageOwner);
                }
            }
        }
        return true;
    }
    
    public void sendCallback(boolean b, final MessageObject messageObject, final TLRPC.KeyboardButton keyboardButton, final ChatActivity chatActivity) {
        if (messageObject != null && keyboardButton != null) {
            if (chatActivity != null) {
                final boolean b2 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
                int i = 0;
                Label_0064: {
                    if (b2) {
                        i = 3;
                    }
                    else if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                        i = 1;
                    }
                    else {
                        if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                            i = 2;
                            break Label_0064;
                        }
                        i = 0;
                        break Label_0064;
                    }
                    b = false;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(messageObject.getDialogId());
                sb.append("_");
                sb.append(messageObject.getId());
                sb.append("_");
                sb.append(Utilities.bytesToHex(keyboardButton.data));
                sb.append("_");
                sb.append(i);
                final String string = sb.toString();
                this.waitingForCallback.put(string, true);
                final TLObject[] array = { null };
                final _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY = new _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY(this, string, b, messageObject, keyboardButton, chatActivity, array);
                if (b) {
                    MessagesStorage.getInstance(this.currentAccount).getBotCache(string, $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY);
                }
                else if (b2) {
                    final TLRPC.TL_messages_requestUrlAuth tl_messages_requestUrlAuth = new TLRPC.TL_messages_requestUrlAuth();
                    tl_messages_requestUrlAuth.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)messageObject.getDialogId());
                    tl_messages_requestUrlAuth.msg_id = messageObject.getId();
                    tl_messages_requestUrlAuth.button_id = keyboardButton.button_id;
                    array[0] = tl_messages_requestUrlAuth;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_requestUrlAuth, $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY, 2);
                }
                else if (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) {
                    if ((messageObject.messageOwner.media.flags & 0x4) == 0x0) {
                        final TLRPC.TL_payments_getPaymentForm tl_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                        tl_payments_getPaymentForm.msg_id = messageObject.getId();
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_payments_getPaymentForm, $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY, 2);
                    }
                    else {
                        final TLRPC.TL_payments_getPaymentReceipt tl_payments_getPaymentReceipt = new TLRPC.TL_payments_getPaymentReceipt();
                        tl_payments_getPaymentReceipt.msg_id = messageObject.messageOwner.media.receipt_msg_id;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_payments_getPaymentReceipt, $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY, 2);
                    }
                }
                else {
                    final TLRPC.TL_messages_getBotCallbackAnswer tl_messages_getBotCallbackAnswer = new TLRPC.TL_messages_getBotCallbackAnswer();
                    tl_messages_getBotCallbackAnswer.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)messageObject.getDialogId());
                    tl_messages_getBotCallbackAnswer.msg_id = messageObject.getId();
                    tl_messages_getBotCallbackAnswer.game = (keyboardButton instanceof TLRPC.TL_keyboardButtonGame);
                    final byte[] data = keyboardButton.data;
                    if (data != null) {
                        tl_messages_getBotCallbackAnswer.flags |= 0x1;
                        tl_messages_getBotCallbackAnswer.data = data;
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getBotCallbackAnswer, $$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY, 2);
                }
            }
        }
    }
    
    public void sendCurrentLocation(final MessageObject value, final TLRPC.KeyboardButton keyboardButton) {
        if (value != null) {
            if (keyboardButton != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(value.getDialogId());
                sb.append("_");
                sb.append(value.getId());
                sb.append("_");
                sb.append(Utilities.bytesToHex(keyboardButton.data));
                sb.append("_");
                String str;
                if (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) {
                    str = "1";
                }
                else {
                    str = "0";
                }
                sb.append(str);
                this.waitingForLocation.put(sb.toString(), value);
                this.locationProvider.start();
            }
        }
    }
    
    public void sendGame(final TLRPC.InputPeer peer, final TLRPC.TL_inputMediaGame media, final long n, final long n2) {
        if (peer != null) {
            if (media != null) {
                final TLRPC.TL_messages_sendMedia tl_messages_sendMedia = new TLRPC.TL_messages_sendMedia();
                tl_messages_sendMedia.peer = peer;
                if (tl_messages_sendMedia.peer instanceof TLRPC.TL_inputPeerChannel) {
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("silent_");
                    sb.append(peer.channel_id);
                    tl_messages_sendMedia.silent = notificationsSettings.getBoolean(sb.toString(), false);
                }
                long nextRandomId;
                if (n != 0L) {
                    nextRandomId = n;
                }
                else {
                    nextRandomId = this.getNextRandomId();
                }
                tl_messages_sendMedia.random_id = nextRandomId;
                tl_messages_sendMedia.message = "";
                tl_messages_sendMedia.media = media;
                long pendingTask = n2;
                if (n2 == 0L) {
                    NativeByteBuffer nativeByteBuffer2;
                    try {
                        final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(peer.getObjectSize() + media.getObjectSize() + 4 + 8);
                        try {
                            nativeByteBuffer.writeInt32(3);
                            nativeByteBuffer.writeInt64(n);
                            peer.serializeToStream(nativeByteBuffer);
                            media.serializeToStream(nativeByteBuffer);
                            nativeByteBuffer2 = nativeByteBuffer;
                        }
                        catch (Exception ex) {
                            nativeByteBuffer2 = nativeByteBuffer;
                        }
                    }
                    catch (Exception ex) {
                        nativeByteBuffer2 = null;
                    }
                    final Exception ex;
                    FileLog.e(ex);
                    pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer2);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_sendMedia, new _$$Lambda$SendMessagesHelper$ydKQSIv3UJTKVK2ad12P2kFNfXM(this, pendingTask));
            }
        }
    }
    
    public int sendMessage(final ArrayList<MessageObject> list, final long n) {
        if (list != null && !list.isEmpty()) {
            final int i = (int)n;
            int n6;
            if (i != 0) {
                final TLRPC.Peer peer = MessagesController.getInstance(this.currentAccount).getPeer(i);
                TLRPC.Chat chat;
                boolean canSendStickers;
                boolean canSendMedia;
                int n2;
                int n3;
                boolean b;
                int n4;
                if (i > 0) {
                    if (MessagesController.getInstance(this.currentAccount).getUser(i) == null) {
                        return 0;
                    }
                    chat = null;
                    canSendStickers = true;
                    canSendMedia = true;
                    n2 = 1;
                    n3 = 1;
                    b = false;
                    n4 = 0;
                }
                else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                    boolean megagroup;
                    int signatures;
                    if (ChatObject.isChannel(chat)) {
                        megagroup = chat.megagroup;
                        signatures = (chat.signatures ? 1 : 0);
                    }
                    else {
                        megagroup = false;
                        signatures = 0;
                    }
                    canSendStickers = ChatObject.canSendStickers(chat);
                    canSendMedia = ChatObject.canSendMedia(chat);
                    final boolean canSendEmbed = ChatObject.canSendEmbed(chat);
                    final boolean canSendPolls = ChatObject.canSendPolls(chat);
                    n4 = signatures;
                    b = megagroup;
                    n3 = (canSendEmbed ? 1 : 0);
                    n2 = (canSendPolls ? 1 : 0);
                }
                LongSparseArray longSparseArray = new LongSparseArray();
                final ArrayList<MessageObject> list2 = new ArrayList<MessageObject>();
                ArrayList<TLRPC.TL_message> c = new ArrayList<TLRPC.TL_message>();
                ArrayList<Long> random_id = new ArrayList<Long>();
                final ArrayList<Integer> list3 = new ArrayList<Integer>();
                LongSparseArray longSparseArray2 = new LongSparseArray();
                TLRPC.InputPeer inputPeer = MessagesController.getInstance(this.currentAccount).getInputPeer(i);
                final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                TLRPC.Peer to_id = peer;
                long n5 = clientUserId;
                final boolean b2 = n == n5;
                ArrayList<Integer> id = list3;
                n6 = 0;
                int j = 0;
                final TLRPC.Chat chat2 = chat;
                ArrayList<MessageObject> list4 = list2;
                while (j < list.size()) {
                    final MessageObject messageObject = list.get(j);
                    LongSparseArray longSparseArray4 = null;
                    TLRPC.Peer peer3 = null;
                    LongSparseArray longSparseArray5 = null;
                    ArrayList<Integer> list9 = null;
                    Label_3141: {
                        ArrayList<MessageObject> list7 = null;
                        ArrayList<Integer> list8 = null;
                        Label_3129: {
                            if (messageObject.getId() > 0 && !messageObject.needDrawBluredPreview()) {
                                ArrayList<MessageObject> list5 = null;
                                Label_0441: {
                                    Label_0410: {
                                        if (!canSendStickers && (messageObject.isSticker() || messageObject.isGif() || messageObject.isGame())) {
                                            if (n6 != 0) {
                                                list5 = list4;
                                                break Label_0441;
                                            }
                                            if (ChatObject.isActionBannedByDefault(chat2, 8)) {
                                                n6 = 4;
                                            }
                                            else {
                                                n6 = 1;
                                            }
                                        }
                                        else {
                                            while (true) {
                                                Label_0533: {
                                                    if (canSendMedia) {
                                                        break Label_0533;
                                                    }
                                                    final TLRPC.MessageMedia media = messageObject.messageOwner.media;
                                                    if (!(media instanceof TLRPC.TL_messageMediaPhoto) && !(media instanceof TLRPC.TL_messageMediaDocument)) {
                                                        break Label_0533;
                                                    }
                                                    if (n6 != 0) {
                                                        break Label_0526;
                                                    }
                                                    if (ChatObject.isActionBannedByDefault(chat2, 7)) {
                                                        n6 = 5;
                                                    }
                                                    else {
                                                        n6 = 2;
                                                    }
                                                    break Label_0410;
                                                    list5 = list4;
                                                    break Label_0441;
                                                }
                                                final ArrayList<MessageObject> list6 = list4;
                                                if (n2 == 0 && messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPoll) {
                                                    if (n6 != 0) {
                                                        continue;
                                                    }
                                                    if (ChatObject.isActionBannedByDefault(chat2, 10)) {
                                                        n6 = 6;
                                                    }
                                                    else {
                                                        n6 = 3;
                                                    }
                                                }
                                                else {
                                                    final TLRPC.TL_message e = new TLRPC.TL_message();
                                                    final boolean b3 = messageObject.getDialogId() == n5 && messageObject.messageOwner.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                    if (messageObject.isForwarded()) {
                                                        e.fwd_from = new TLRPC.TL_messageFwdHeader();
                                                        final TLRPC.MessageFwdHeader fwd_from = e.fwd_from;
                                                        final TLRPC.MessageFwdHeader fwd_from2 = messageObject.messageOwner.fwd_from;
                                                        fwd_from.flags = fwd_from2.flags;
                                                        fwd_from.from_id = fwd_from2.from_id;
                                                        fwd_from.date = fwd_from2.date;
                                                        fwd_from.channel_id = fwd_from2.channel_id;
                                                        fwd_from.channel_post = fwd_from2.channel_post;
                                                        fwd_from.post_author = fwd_from2.post_author;
                                                        fwd_from.from_name = fwd_from2.from_name;
                                                        e.flags = 4;
                                                    }
                                                    else if (!b3) {
                                                        e.fwd_from = new TLRPC.TL_messageFwdHeader();
                                                        e.fwd_from.channel_post = messageObject.getId();
                                                        final TLRPC.MessageFwdHeader fwd_from3 = e.fwd_from;
                                                        fwd_from3.flags |= 0x4;
                                                        if (messageObject.isFromUser()) {
                                                            final TLRPC.MessageFwdHeader fwd_from4 = e.fwd_from;
                                                            fwd_from4.from_id = messageObject.messageOwner.from_id;
                                                            fwd_from4.flags |= 0x1;
                                                        }
                                                        else {
                                                            final TLRPC.MessageFwdHeader fwd_from5 = e.fwd_from;
                                                            final TLRPC.Message messageOwner = messageObject.messageOwner;
                                                            fwd_from5.channel_id = messageOwner.to_id.channel_id;
                                                            fwd_from5.flags |= 0x2;
                                                            if (messageOwner.post) {
                                                                final int from_id = messageOwner.from_id;
                                                                if (from_id > 0) {
                                                                    fwd_from5.from_id = from_id;
                                                                    fwd_from5.flags |= 0x1;
                                                                }
                                                            }
                                                        }
                                                        final String post_author = messageObject.messageOwner.post_author;
                                                        if (post_author != null) {
                                                            final TLRPC.MessageFwdHeader fwd_from6 = e.fwd_from;
                                                            fwd_from6.post_author = post_author;
                                                            fwd_from6.flags |= 0x8;
                                                        }
                                                        else if (!messageObject.isOutOwner()) {
                                                            final TLRPC.Message messageOwner2 = messageObject.messageOwner;
                                                            if (messageOwner2.from_id > 0 && messageOwner2.post) {
                                                                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.from_id);
                                                                if (user != null) {
                                                                    e.fwd_from.post_author = ContactsController.formatName(user.first_name, user.last_name);
                                                                    final TLRPC.MessageFwdHeader fwd_from7 = e.fwd_from;
                                                                    fwd_from7.flags |= 0x8;
                                                                }
                                                            }
                                                        }
                                                        e.date = messageObject.messageOwner.date;
                                                        e.flags = 4;
                                                    }
                                                    if (n == n5) {
                                                        final TLRPC.MessageFwdHeader fwd_from8 = e.fwd_from;
                                                        if (fwd_from8 != null) {
                                                            fwd_from8.flags |= 0x10;
                                                            fwd_from8.saved_from_msg_id = messageObject.getId();
                                                            e.fwd_from.saved_from_peer = messageObject.messageOwner.to_id;
                                                        }
                                                    }
                                                    if (n3 == 0 && messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage) {
                                                        e.media = new TLRPC.TL_messageMediaEmpty();
                                                    }
                                                    else {
                                                        e.media = messageObject.messageOwner.media;
                                                    }
                                                    if (e.media != null) {
                                                        e.flags |= 0x200;
                                                    }
                                                    if (b) {
                                                        e.flags |= Integer.MIN_VALUE;
                                                    }
                                                    final int via_bot_id = messageObject.messageOwner.via_bot_id;
                                                    if (via_bot_id != 0) {
                                                        e.via_bot_id = via_bot_id;
                                                        e.flags |= 0x800;
                                                    }
                                                    e.message = messageObject.messageOwner.message;
                                                    e.fwd_msg_id = messageObject.getId();
                                                    final TLRPC.Message messageOwner3 = messageObject.messageOwner;
                                                    e.attachPath = messageOwner3.attachPath;
                                                    e.entities = messageOwner3.entities;
                                                    long n7 = n5;
                                                    if (messageOwner3.reply_markup instanceof TLRPC.TL_replyInlineMarkup) {
                                                        e.flags |= 0x40;
                                                        e.reply_markup = new TLRPC.TL_replyInlineMarkup();
                                                        final int size = messageObject.messageOwner.reply_markup.rows.size();
                                                        int index = 0;
                                                        while (true) {
                                                            n7 = n5;
                                                            if (index >= size) {
                                                                break;
                                                            }
                                                            final TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow = messageObject.messageOwner.reply_markup.rows.get(index);
                                                            final int size2 = tl_keyboardButtonRow.buttons.size();
                                                            TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow2 = null;
                                                            TLRPC.TL_keyboardButtonRow e2;
                                                            for (int k = 0; k < size2; ++k, tl_keyboardButtonRow2 = e2) {
                                                                final TLRPC.KeyboardButton keyboardButton = tl_keyboardButtonRow.buttons.get(k);
                                                                final boolean b4 = keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth;
                                                                if (!b4 && !(keyboardButton instanceof TLRPC.TL_keyboardButtonUrl)) {
                                                                    e2 = tl_keyboardButtonRow2;
                                                                    if (!(keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline)) {
                                                                        continue;
                                                                    }
                                                                }
                                                                TLRPC.KeyboardButton e3 = keyboardButton;
                                                                if (b4) {
                                                                    e3 = new TLRPC.TL_keyboardButtonUrlAuth();
                                                                    e3.flags = keyboardButton.flags;
                                                                    final String fwd_text = keyboardButton.fwd_text;
                                                                    if (fwd_text != null) {
                                                                        e3.fwd_text = fwd_text;
                                                                        e3.text = fwd_text;
                                                                    }
                                                                    else {
                                                                        e3.text = keyboardButton.text;
                                                                    }
                                                                    e3.url = keyboardButton.url;
                                                                    e3.button_id = keyboardButton.button_id;
                                                                }
                                                                if ((e2 = tl_keyboardButtonRow2) == null) {
                                                                    e2 = new TLRPC.TL_keyboardButtonRow();
                                                                    e.reply_markup.rows.add(e2);
                                                                }
                                                                e2.buttons.add(e3);
                                                            }
                                                            ++index;
                                                        }
                                                    }
                                                    if (!e.entities.isEmpty()) {
                                                        e.flags |= 0x80;
                                                    }
                                                    if (e.attachPath == null) {
                                                        e.attachPath = "";
                                                    }
                                                    final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                                                    e.id = newMessageId;
                                                    e.local_id = newMessageId;
                                                    e.out = true;
                                                    final long grouped_id = messageObject.messageOwner.grouped_id;
                                                    if (grouped_id != 0L) {
                                                        Long value;
                                                        if ((value = (Long)longSparseArray.get(grouped_id)) == null) {
                                                            value = Utilities.random.nextLong();
                                                            longSparseArray.put(messageObject.messageOwner.grouped_id, (Object)value);
                                                        }
                                                        e.grouped_id = value;
                                                        e.flags |= 0x20000;
                                                    }
                                                    final boolean b5 = j != list.size() - 1 && list.get(j + 1).messageOwner.grouped_id != messageObject.messageOwner.grouped_id;
                                                    final int channel_id = to_id.channel_id;
                                                    if (channel_id != 0 && !b) {
                                                        int clientUserId2;
                                                        if (n4 != 0) {
                                                            clientUserId2 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                        }
                                                        else {
                                                            clientUserId2 = -channel_id;
                                                        }
                                                        e.from_id = clientUserId2;
                                                        e.post = true;
                                                    }
                                                    else {
                                                        e.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                        e.flags |= 0x100;
                                                    }
                                                    if (e.random_id == 0L) {
                                                        e.random_id = this.getNextRandomId();
                                                    }
                                                    random_id.add(e.random_id);
                                                    longSparseArray2.put(e.random_id, (Object)e);
                                                    id.add(e.fwd_msg_id);
                                                    e.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                                                    final boolean b6 = inputPeer instanceof TLRPC.TL_inputPeerChannel;
                                                    if (b6 && !b) {
                                                        e.views = 1;
                                                        e.flags |= 0x400;
                                                    }
                                                    else {
                                                        final TLRPC.Message messageOwner4 = messageObject.messageOwner;
                                                        if ((messageOwner4.flags & 0x400) != 0x0) {
                                                            e.views = messageOwner4.views;
                                                            e.flags |= 0x400;
                                                        }
                                                        e.unread = true;
                                                    }
                                                    e.dialog_id = n;
                                                    e.to_id = to_id;
                                                    if (MessageObject.isVoiceMessage(e) || MessageObject.isRoundVideoMessage(e)) {
                                                        if (b6 && messageObject.getChannelId() != 0) {
                                                            e.media_unread = messageObject.isContentUnread();
                                                        }
                                                        else {
                                                            e.media_unread = true;
                                                        }
                                                    }
                                                    final TLRPC.Peer to_id2 = messageObject.messageOwner.to_id;
                                                    final LongSparseArray longSparseArray3 = longSparseArray;
                                                    if (to_id2 instanceof TLRPC.TL_peerChannel) {
                                                        e.ttl = -to_id2.channel_id;
                                                    }
                                                    final MessageObject e4 = new MessageObject(this.currentAccount, e, true);
                                                    e4.messageOwner.send_state = 1;
                                                    list6.add(e4);
                                                    c.add(e);
                                                    this.putToSendingMessages(e);
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        final StringBuilder sb = new StringBuilder();
                                                        sb.append("forward message user_id = ");
                                                        sb.append(inputPeer.user_id);
                                                        sb.append(" chat_id = ");
                                                        sb.append(inputPeer.chat_id);
                                                        sb.append(" channel_id = ");
                                                        sb.append(inputPeer.channel_id);
                                                        sb.append(" access_hash = ");
                                                        sb.append(inputPeer.access_hash);
                                                        FileLog.d(sb.toString());
                                                    }
                                                    final TLRPC.Peer peer2 = to_id;
                                                    if ((!b5 || c.size() <= 0) && c.size() != 100 && j != list.size() - 1 && (j == list.size() - 1 || list.get(j + 1).getDialogId() == messageObject.getDialogId())) {
                                                        list7 = list6;
                                                        longSparseArray4 = longSparseArray2;
                                                        list8 = id;
                                                        peer3 = peer2;
                                                        longSparseArray5 = longSparseArray3;
                                                        n5 = n7;
                                                        break Label_3129;
                                                    }
                                                    MessagesStorage.getInstance(this.currentAccount).putMessages(new ArrayList<TLRPC.Message>(c), false, true, false, 0);
                                                    MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(n, list6);
                                                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                                                    final TLRPC.TL_messages_forwardMessages tl_messages_forwardMessages = new TLRPC.TL_messages_forwardMessages();
                                                    tl_messages_forwardMessages.to_peer = inputPeer;
                                                    tl_messages_forwardMessages.grouped = (grouped_id != 0L);
                                                    if (tl_messages_forwardMessages.to_peer instanceof TLRPC.TL_inputPeerChannel) {
                                                        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                                                        final StringBuilder sb2 = new StringBuilder();
                                                        sb2.append("silent_");
                                                        sb2.append(n);
                                                        tl_messages_forwardMessages.silent = notificationsSettings.getBoolean(sb2.toString(), false);
                                                    }
                                                    if (messageObject.messageOwner.to_id instanceof TLRPC.TL_peerChannel) {
                                                        final TLRPC.Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(messageObject.messageOwner.to_id.channel_id);
                                                        tl_messages_forwardMessages.from_peer = new TLRPC.TL_inputPeerChannel();
                                                        final TLRPC.InputPeer from_peer = tl_messages_forwardMessages.from_peer;
                                                        from_peer.channel_id = messageObject.messageOwner.to_id.channel_id;
                                                        if (chat3 != null) {
                                                            from_peer.access_hash = chat3.access_hash;
                                                        }
                                                    }
                                                    else {
                                                        tl_messages_forwardMessages.from_peer = new TLRPC.TL_inputPeerEmpty();
                                                    }
                                                    tl_messages_forwardMessages.random_id = random_id;
                                                    tl_messages_forwardMessages.id = id;
                                                    tl_messages_forwardMessages.with_my_score = (list.size() == 1 && list.get(0).messageOwner.with_my_score);
                                                    final ConnectionsManager instance = ConnectionsManager.getInstance(this.currentAccount);
                                                    list7 = list6;
                                                    longSparseArray4 = longSparseArray2;
                                                    list8 = id;
                                                    final TLRPC.InputPeer inputPeer2 = inputPeer;
                                                    instance.sendRequest(tl_messages_forwardMessages, new _$$Lambda$SendMessagesHelper$GtPQ6DFMMI1Gm_S7QANSsM7url8(this, n, b, b2, longSparseArray4, c, list7, peer2, tl_messages_forwardMessages), 68);
                                                    peer3 = peer2;
                                                    longSparseArray5 = longSparseArray3;
                                                    n5 = n7;
                                                    inputPeer = inputPeer2;
                                                    if (j != list.size() - 1) {
                                                        list4 = new ArrayList<MessageObject>();
                                                        c = new ArrayList<TLRPC.TL_message>();
                                                        random_id = new ArrayList<Long>();
                                                        list9 = new ArrayList<Integer>();
                                                        longSparseArray4 = new LongSparseArray();
                                                        peer3 = peer2;
                                                        longSparseArray5 = longSparseArray3;
                                                        n5 = n7;
                                                        inputPeer = inputPeer2;
                                                        break Label_3141;
                                                    }
                                                    break Label_3129;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    final LongSparseArray longSparseArray6 = longSparseArray;
                                    final TLRPC.Peer peer4 = to_id;
                                    list9 = id;
                                    longSparseArray4 = longSparseArray2;
                                    peer3 = peer4;
                                    longSparseArray5 = longSparseArray6;
                                    break Label_3141;
                                }
                                final LongSparseArray longSparseArray7 = longSparseArray2;
                                longSparseArray5 = longSparseArray;
                                list8 = id;
                                list7 = list5;
                                peer3 = to_id;
                                longSparseArray4 = longSparseArray7;
                            }
                            else {
                                final ArrayList<MessageObject> list10 = list4;
                                final LongSparseArray longSparseArray8 = longSparseArray2;
                                final TLRPC.InputPeer inputPeer3 = inputPeer;
                                final LongSparseArray longSparseArray9 = longSparseArray;
                                final long n8 = n5;
                                final TLRPC.Peer peer5 = to_id;
                                final ArrayList<Integer> list11 = list8 = id;
                                list7 = list10;
                                peer3 = peer5;
                                longSparseArray5 = longSparseArray9;
                                n5 = n8;
                                longSparseArray4 = longSparseArray8;
                                inputPeer = inputPeer3;
                                if (messageObject.type == 0) {
                                    list8 = list11;
                                    list7 = list10;
                                    peer3 = peer5;
                                    longSparseArray5 = longSparseArray9;
                                    n5 = n8;
                                    longSparseArray4 = longSparseArray8;
                                    inputPeer = inputPeer3;
                                    if (!TextUtils.isEmpty(messageObject.messageText)) {
                                        final TLRPC.MessageMedia media2 = messageObject.messageOwner.media;
                                        TLRPC.WebPage webpage;
                                        if (media2 != null) {
                                            webpage = media2.webpage;
                                        }
                                        else {
                                            webpage = null;
                                        }
                                        this.sendMessage(messageObject.messageText.toString(), n, null, webpage, webpage != null, messageObject.messageOwner.entities, null, null);
                                        inputPeer = inputPeer3;
                                        longSparseArray4 = longSparseArray8;
                                        n5 = n8;
                                        longSparseArray5 = longSparseArray9;
                                        peer3 = peer5;
                                        list7 = list10;
                                        list8 = list11;
                                    }
                                }
                            }
                        }
                        final ArrayList<Integer> list12 = list8;
                        list4 = list7;
                        list9 = list12;
                    }
                    ++j;
                    final TLRPC.Peer peer6 = peer3;
                    final LongSparseArray longSparseArray10 = longSparseArray5;
                    id = list9;
                    longSparseArray2 = longSparseArray4;
                    longSparseArray = longSparseArray10;
                    to_id = peer6;
                }
            }
            else {
                for (int l = 0; l < list.size(); ++l) {
                    this.processForwardFromMyName(list.get(l), n);
                }
                n6 = 0;
            }
            return n6;
        }
        return 0;
    }
    
    public void sendMessage(final String s, final long n, final MessageObject messageObject, final TLRPC.WebPage webPage, final boolean b, final ArrayList<TLRPC.MessageEntity> list, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap) {
        this.sendMessage(s, null, null, null, null, null, null, null, null, n, null, messageObject, webPage, b, null, list, replyMarkup, hashMap, 0, null);
    }
    
    public void sendMessage(final MessageObject messageObject) {
        final long dialogId = messageObject.getDialogId();
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        this.sendMessage(null, null, null, null, null, null, null, null, null, dialogId, messageOwner.attachPath, null, null, true, messageObject, null, messageOwner.reply_markup, messageOwner.params, 0, null);
    }
    
    public void sendMessage(final TLRPC.MessageMedia messageMedia, final long n, final MessageObject messageObject, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap) {
        this.sendMessage(null, null, messageMedia, null, null, null, null, null, null, n, null, messageObject, null, true, null, null, replyMarkup, hashMap, 0, null);
    }
    
    public void sendMessage(final TLRPC.TL_document tl_document, final VideoEditedInfo videoEditedInfo, final String s, final long n, final MessageObject messageObject, final String s2, final ArrayList<TLRPC.MessageEntity> list, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap, final int n2, final Object o) {
        this.sendMessage(null, s2, null, null, videoEditedInfo, null, tl_document, null, null, n, s, messageObject, null, true, null, list, replyMarkup, hashMap, n2, o);
    }
    
    public void sendMessage(final TLRPC.TL_game tl_game, final long n, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap) {
        this.sendMessage(null, null, null, null, null, null, null, tl_game, null, n, null, null, null, true, null, null, replyMarkup, hashMap, 0, null);
    }
    
    public void sendMessage(final TLRPC.TL_messageMediaPoll tl_messageMediaPoll, final long n, final MessageObject messageObject, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap) {
        this.sendMessage(null, null, null, null, null, null, null, null, tl_messageMediaPoll, n, null, messageObject, null, true, null, null, replyMarkup, hashMap, 0, null);
    }
    
    public void sendMessage(final TLRPC.TL_photo tl_photo, final String s, final long n, final MessageObject messageObject, final String s2, final ArrayList<TLRPC.MessageEntity> list, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap, final int n2, final Object o) {
        this.sendMessage(null, s2, null, tl_photo, null, null, null, null, null, n, s, messageObject, null, true, null, list, replyMarkup, hashMap, n2, o);
    }
    
    public void sendMessage(final TLRPC.User user, final long n, final MessageObject messageObject, final TLRPC.ReplyMarkup replyMarkup, final HashMap<String, String> hashMap) {
        this.sendMessage(null, null, null, null, null, user, null, null, null, n, null, messageObject, null, true, null, null, replyMarkup, hashMap, 0, null);
    }
    
    public void sendNotificationCallback(final long n, final int n2, final byte[] array) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$SendMessagesHelper$HbRNU4Jc_Y0XpAaKW1pVIkZttzI(this, n, n2, array));
    }
    
    public void sendScreenshotMessage(final TLRPC.User user, final int n, TLRPC.Message e) {
        if (user != null && n != 0) {
            if (user.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                final TLRPC.TL_messages_sendScreenshotNotification tl_messages_sendScreenshotNotification = new TLRPC.TL_messages_sendScreenshotNotification();
                tl_messages_sendScreenshotNotification.peer = new TLRPC.TL_inputPeerUser();
                final TLRPC.InputPeer peer = tl_messages_sendScreenshotNotification.peer;
                peer.access_hash = user.access_hash;
                peer.user_id = user.id;
                if (e != null) {
                    tl_messages_sendScreenshotNotification.reply_to_msg_id = n;
                    tl_messages_sendScreenshotNotification.random_id = e.random_id;
                }
                else {
                    e = new TLRPC.TL_messageService();
                    e.random_id = this.getNextRandomId();
                    e.dialog_id = user.id;
                    e.unread = true;
                    e.out = true;
                    final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    e.id = newMessageId;
                    e.local_id = newMessageId;
                    e.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    e.flags |= 0x100;
                    e.flags |= 0x8;
                    e.reply_to_msg_id = n;
                    e.to_id = new TLRPC.TL_peerUser();
                    e.to_id.user_id = user.id;
                    e.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    e.action = new TLRPC.TL_messageActionScreenshotTaken();
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                tl_messages_sendScreenshotNotification.random_id = e.random_id;
                final MessageObject e2 = new MessageObject(this.currentAccount, e, false);
                e2.messageOwner.send_state = 1;
                final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                list.add(e2);
                MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(e.dialog_id, list);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                final ArrayList<TLRPC.Message> list2 = new ArrayList<TLRPC.Message>();
                list2.add(e);
                MessagesStorage.getInstance(this.currentAccount).putMessages(list2, false, true, false, 0);
                this.performSendMessageRequest(tl_messages_sendScreenshotNotification, e2, null, null, null);
            }
        }
    }
    
    public void sendSticker(TLRPC.Document document, final long n, final MessageObject messageObject, final Object o) {
        if (document == null) {
            return;
        }
        if ((int)n == 0) {
            if (MessagesController.getInstance(this.currentAccount).getEncryptedChat((int)(n >> 32)) == null) {
                return;
            }
            final TLRPC.TL_document_layer82 tl_document_layer82 = new TLRPC.TL_document_layer82();
            tl_document_layer82.id = document.id;
            tl_document_layer82.access_hash = document.access_hash;
            tl_document_layer82.date = document.date;
            tl_document_layer82.mime_type = document.mime_type;
            tl_document_layer82.file_reference = document.file_reference;
            if (tl_document_layer82.file_reference == null) {
                tl_document_layer82.file_reference = new byte[0];
            }
            tl_document_layer82.size = document.size;
            tl_document_layer82.dc_id = document.dc_id;
            tl_document_layer82.attributes = new ArrayList<TLRPC.DocumentAttribute>(document.attributes);
            if (tl_document_layer82.mime_type == null) {
                tl_document_layer82.mime_type = "";
            }
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
            if (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) {
                final File pathToAttach = FileLoader.getPathToAttach(closestPhotoSizeWithSize, true);
                if (pathToAttach.exists()) {
                    try {
                        pathToAttach.length();
                        final byte[] array = new byte[(int)pathToAttach.length()];
                        new RandomAccessFile(pathToAttach, "r").readFully(array);
                        final TLRPC.TL_photoCachedSize e = new TLRPC.TL_photoCachedSize();
                        final TLRPC.TL_fileLocation_layer82 location = new TLRPC.TL_fileLocation_layer82();
                        location.dc_id = closestPhotoSizeWithSize.location.dc_id;
                        location.volume_id = closestPhotoSizeWithSize.location.volume_id;
                        location.local_id = closestPhotoSizeWithSize.location.local_id;
                        location.secret = closestPhotoSizeWithSize.location.secret;
                        e.location = location;
                        e.size = closestPhotoSizeWithSize.size;
                        e.w = closestPhotoSizeWithSize.w;
                        e.h = closestPhotoSizeWithSize.h;
                        e.type = closestPhotoSizeWithSize.type;
                        e.bytes = array;
                        tl_document_layer82.thumbs.add(e);
                        tl_document_layer82.flags |= 0x1;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
            }
            if (tl_document_layer82.thumbs.isEmpty()) {
                final TLRPC.TL_photoSizeEmpty e2 = new TLRPC.TL_photoSizeEmpty();
                e2.type = "s";
                tl_document_layer82.thumbs.add(e2);
            }
            document = tl_document_layer82;
        }
        if (document instanceof TLRPC.TL_document) {
            this.sendMessage((TLRPC.TL_document)document, null, null, n, messageObject, null, null, null, null, 0, o);
        }
    }
    
    public int sendVote(final MessageObject messageObject, final TLRPC.TL_pollAnswer tl_pollAnswer, final Runnable runnable) {
        if (messageObject == null) {
            return 0;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("poll_");
        sb.append(messageObject.getPollId());
        final String string = sb.toString();
        if (this.waitingForCallback.containsKey(string)) {
            return 0;
        }
        final HashMap<String, byte[]> waitingForVote = this.waitingForVote;
        byte[] option;
        if (tl_pollAnswer != null) {
            option = tl_pollAnswer.option;
        }
        else {
            option = new byte[0];
        }
        waitingForVote.put(string, option);
        final TLRPC.TL_messages_sendVote tl_messages_sendVote = new TLRPC.TL_messages_sendVote();
        tl_messages_sendVote.msg_id = messageObject.getId();
        tl_messages_sendVote.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)messageObject.getDialogId());
        if (tl_pollAnswer != null) {
            tl_messages_sendVote.options.add(tl_pollAnswer.option);
        }
        return ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_sendVote, new _$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY(this, messageObject, string, runnable));
    }
    
    public void setCurrentChatInfo(final TLRPC.ChatFull currentChatInfo) {
        this.currentChatInfo = currentChatInfo;
    }
    
    protected void stopVideoService(final String s) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SendMessagesHelper$30d877KfsrPIYnw1mIlTicl4KVY(this, s));
    }
    
    protected class DelayedMessage
    {
        public TLRPC.EncryptedChat encryptedChat;
        public HashMap<Object, Object> extraHashMap;
        public int finalGroupMessage;
        public long groupId;
        public String httpLocation;
        public ArrayList<String> httpLocations;
        public ArrayList<TLRPC.InputMedia> inputMedias;
        public TLRPC.InputMedia inputUploadMedia;
        public TLObject locationParent;
        public ArrayList<TLRPC.PhotoSize> locations;
        public ArrayList<MessageObject> messageObjects;
        public ArrayList<TLRPC.Message> messages;
        public MessageObject obj;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public long peer;
        public boolean performMediaUpload;
        public TLRPC.PhotoSize photoSize;
        ArrayList<DelayedMessageSendAfterRequest> requests;
        public TLObject sendEncryptedRequest;
        public TLObject sendRequest;
        public int type;
        public VideoEditedInfo videoEditedInfo;
        public ArrayList<VideoEditedInfo> videoEditedInfos;
        
        public DelayedMessage(final long peer) {
            this.peer = peer;
        }
        
        public void addDelayedRequest(final TLObject request, final ArrayList<MessageObject> msgObjs, final ArrayList<String> originalPaths, final ArrayList<Object> parentObjects, final DelayedMessage delayedMessage) {
            final DelayedMessageSendAfterRequest e = new DelayedMessageSendAfterRequest();
            e.request = request;
            e.msgObjs = msgObjs;
            e.originalPaths = originalPaths;
            e.delayedMessage = delayedMessage;
            e.parentObjects = parentObjects;
            if (this.requests == null) {
                this.requests = new ArrayList<DelayedMessageSendAfterRequest>();
            }
            this.requests.add(e);
        }
        
        public void addDelayedRequest(final TLObject request, final MessageObject msgObj, final String originalPath, final Object parentObject, final DelayedMessage delayedMessage) {
            final DelayedMessageSendAfterRequest e = new DelayedMessageSendAfterRequest();
            e.request = request;
            e.msgObj = msgObj;
            e.originalPath = originalPath;
            e.delayedMessage = delayedMessage;
            e.parentObject = parentObject;
            if (this.requests == null) {
                this.requests = new ArrayList<DelayedMessageSendAfterRequest>();
            }
            this.requests.add(e);
        }
        
        public void initForGroup(final long groupId) {
            this.type = 4;
            this.groupId = groupId;
            this.messageObjects = new ArrayList<MessageObject>();
            this.messages = new ArrayList<TLRPC.Message>();
            this.inputMedias = new ArrayList<TLRPC.InputMedia>();
            this.originalPaths = new ArrayList<String>();
            this.parentObjects = new ArrayList<Object>();
            this.extraHashMap = new HashMap<Object, Object>();
            this.locations = new ArrayList<TLRPC.PhotoSize>();
            this.httpLocations = new ArrayList<String>();
            this.videoEditedInfos = new ArrayList<VideoEditedInfo>();
        }
        
        public void markAsError() {
            if (this.type == 4) {
                for (int i = 0; i < this.messageObjects.size(); ++i) {
                    final MessageObject messageObject = this.messageObjects.get(i);
                    MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(messageObject.messageOwner);
                    messageObject.messageOwner.send_state = 2;
                    NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, messageObject.getId());
                    SendMessagesHelper.this.processSentMessage(messageObject.getId());
                }
                final HashMap access$900 = SendMessagesHelper.this.delayedMessages;
                final StringBuilder sb = new StringBuilder();
                sb.append("group_");
                sb.append(this.groupId);
                access$900.remove(sb.toString());
            }
            else {
                MessagesStorage.getInstance(SendMessagesHelper.this.currentAccount).markMessageAsSendError(this.obj.messageOwner);
                this.obj.messageOwner.send_state = 2;
                NotificationCenter.getInstance(SendMessagesHelper.this.currentAccount).postNotificationName(NotificationCenter.messageSendError, this.obj.getId());
                SendMessagesHelper.this.processSentMessage(this.obj.getId());
            }
            this.sendDelayedRequests();
        }
        
        public void sendDelayedRequests() {
            if (this.requests != null) {
                final int type = this.type;
                if (type == 4 || type == 0) {
                    for (int size = this.requests.size(), i = 0; i < size; ++i) {
                        final DelayedMessageSendAfterRequest delayedMessageSendAfterRequest = this.requests.get(i);
                        final TLObject request = delayedMessageSendAfterRequest.request;
                        if (request instanceof TLRPC.TL_messages_sendEncryptedMultiMedia) {
                            SecretChatHelper.getInstance(SendMessagesHelper.this.currentAccount).performSendEncryptedRequest((TLRPC.TL_messages_sendEncryptedMultiMedia)delayedMessageSendAfterRequest.request, this);
                        }
                        else if (request instanceof TLRPC.TL_messages_sendMultiMedia) {
                            SendMessagesHelper.this.performSendMessageRequestMulti((TLRPC.TL_messages_sendMultiMedia)request, delayedMessageSendAfterRequest.msgObjs, delayedMessageSendAfterRequest.originalPaths, delayedMessageSendAfterRequest.parentObjects, delayedMessageSendAfterRequest.delayedMessage);
                        }
                        else {
                            SendMessagesHelper.this.performSendMessageRequest(request, delayedMessageSendAfterRequest.msgObj, delayedMessageSendAfterRequest.originalPath, delayedMessageSendAfterRequest.delayedMessage, delayedMessageSendAfterRequest.parentObject);
                        }
                    }
                    this.requests = null;
                }
            }
        }
    }
    
    protected class DelayedMessageSendAfterRequest
    {
        public DelayedMessage delayedMessage;
        public MessageObject msgObj;
        public ArrayList<MessageObject> msgObjs;
        public String originalPath;
        public ArrayList<String> originalPaths;
        public Object parentObject;
        public ArrayList<Object> parentObjects;
        public TLObject request;
    }
    
    public static class LocationProvider
    {
        private LocationProviderDelegate delegate;
        private GpsLocationListener gpsLocationListener;
        private Location lastKnownLocation;
        private LocationManager locationManager;
        private Runnable locationQueryCancelRunnable;
        private GpsLocationListener networkLocationListener;
        
        public LocationProvider() {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
        }
        
        public LocationProvider(final LocationProviderDelegate delegate) {
            this.gpsLocationListener = new GpsLocationListener();
            this.networkLocationListener = new GpsLocationListener();
            this.delegate = delegate;
        }
        
        private void cleanup() {
            this.locationManager.removeUpdates((LocationListener)this.gpsLocationListener);
            this.locationManager.removeUpdates((LocationListener)this.networkLocationListener);
            this.lastKnownLocation = null;
            this.locationQueryCancelRunnable = null;
        }
        
        public void setDelegate(final LocationProviderDelegate delegate) {
            this.delegate = delegate;
        }
        
        public void start() {
            if (this.locationManager == null) {
                this.locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
            }
            try {
                this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, (LocationListener)this.gpsLocationListener);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            try {
                this.locationManager.requestLocationUpdates("network", 1L, 0.0f, (LocationListener)this.networkLocationListener);
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
            try {
                this.lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
                if (this.lastKnownLocation == null) {
                    this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                }
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
            final Runnable locationQueryCancelRunnable = this.locationQueryCancelRunnable;
            if (locationQueryCancelRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(locationQueryCancelRunnable);
            }
            AndroidUtilities.runOnUIThread(this.locationQueryCancelRunnable = new Runnable() {
                @Override
                public void run() {
                    if (LocationProvider.this.locationQueryCancelRunnable != this) {
                        return;
                    }
                    if (LocationProvider.this.delegate != null) {
                        if (LocationProvider.this.lastKnownLocation != null) {
                            LocationProvider.this.delegate.onLocationAcquired(LocationProvider.this.lastKnownLocation);
                        }
                        else {
                            LocationProvider.this.delegate.onUnableLocationAcquire();
                        }
                    }
                    LocationProvider.this.cleanup();
                }
            }, 5000L);
        }
        
        public void stop() {
            if (this.locationManager == null) {
                return;
            }
            final Runnable locationQueryCancelRunnable = this.locationQueryCancelRunnable;
            if (locationQueryCancelRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(locationQueryCancelRunnable);
            }
            this.cleanup();
        }
        
        private class GpsLocationListener implements LocationListener
        {
            public void onLocationChanged(final Location obj) {
                if (obj != null) {
                    if (LocationProvider.this.locationQueryCancelRunnable != null) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("found location ");
                            sb.append(obj);
                            FileLog.d(sb.toString());
                        }
                        LocationProvider.this.lastKnownLocation = obj;
                        if (obj.getAccuracy() < 100.0f) {
                            if (LocationProvider.this.delegate != null) {
                                LocationProvider.this.delegate.onLocationAcquired(obj);
                            }
                            if (LocationProvider.this.locationQueryCancelRunnable != null) {
                                AndroidUtilities.cancelRunOnUIThread(LocationProvider.this.locationQueryCancelRunnable);
                            }
                            LocationProvider.this.cleanup();
                        }
                    }
                }
            }
            
            public void onProviderDisabled(final String s) {
            }
            
            public void onProviderEnabled(final String s) {
            }
            
            public void onStatusChanged(final String s, final int n, final Bundle bundle) {
            }
        }
        
        public interface LocationProviderDelegate
        {
            void onLocationAcquired(final Location p0);
            
            void onUnableLocationAcquire();
        }
    }
    
    private static class MediaSendPrepareWorker
    {
        public volatile String parentObject;
        public volatile TLRPC.TL_photo photo;
        public CountDownLatch sync;
    }
    
    public static class SendingMediaInfo
    {
        public boolean canDeleteAfter;
        public String caption;
        public ArrayList<TLRPC.MessageEntity> entities;
        public boolean isVideo;
        public ArrayList<TLRPC.InputDocument> masks;
        public String path;
        public MediaController.SearchImage searchImage;
        public int ttl;
        public Uri uri;
        public VideoEditedInfo videoEditedInfo;
    }
}
