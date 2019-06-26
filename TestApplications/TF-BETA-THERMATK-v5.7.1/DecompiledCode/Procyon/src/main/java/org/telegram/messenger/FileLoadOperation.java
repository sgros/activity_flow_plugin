// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.io.Serializable;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.WriteToSocketDelegate;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import java.util.zip.ZipException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import org.telegram.tgnet.NativeByteBuffer;
import java.util.Collection;
import org.telegram.tgnet.ConnectionsManager;
import android.util.SparseIntArray;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import java.io.File;

public class FileLoadOperation
{
    private static final int bigFileSizeFrom = 1048576;
    private static final int cdnChunkCheckSize = 131072;
    private static final int downloadChunkSize = 32768;
    private static final int downloadChunkSizeBig = 131072;
    private static final int maxCdnParts = 12288;
    private static final int maxDownloadRequests = 4;
    private static final int maxDownloadRequestsBig = 4;
    private static final int preloadMaxBytes = 2097152;
    private static final int stateDownloading = 1;
    private static final int stateFailed = 2;
    private static final int stateFinished = 3;
    private static final int stateIdle = 0;
    private boolean allowDisordererFileSave;
    private int bytesCountPadding;
    private File cacheFileFinal;
    private File cacheFileParts;
    private File cacheFilePreload;
    private File cacheFileTemp;
    private File cacheIvTemp;
    private byte[] cdnCheckBytes;
    private int cdnDatacenterId;
    private SparseArray<TLRPC.TL_fileHash> cdnHashes;
    private byte[] cdnIv;
    private byte[] cdnKey;
    private byte[] cdnToken;
    private int currentAccount;
    private int currentDownloadChunkSize;
    private int currentMaxDownloadRequests;
    private int currentType;
    private int datacenterId;
    private ArrayList<RequestInfo> delayedRequestInfos;
    private FileLoadOperationDelegate delegate;
    private int downloadedBytes;
    private boolean encryptFile;
    private byte[] encryptIv;
    private byte[] encryptKey;
    private String ext;
    private RandomAccessFile fileOutputStream;
    private RandomAccessFile filePartsStream;
    private RandomAccessFile fileReadStream;
    private RandomAccessFile fiv;
    private int foundMoovSize;
    private int initialDatacenterId;
    private boolean isCdn;
    private boolean isForceRequest;
    private boolean isPreloadVideoOperation;
    private byte[] iv;
    private byte[] key;
    protected TLRPC.InputFileLocation location;
    private int moovFound;
    private int nextAtomOffset;
    private boolean nextPartWasPreloaded;
    private int nextPreloadDownloadOffset;
    private ArrayList<Range> notCheckedCdnRanges;
    private ArrayList<Range> notLoadedBytesRanges;
    private volatile ArrayList<Range> notLoadedBytesRangesCopy;
    private ArrayList<Range> notRequestedBytesRanges;
    private Object parentObject;
    private volatile boolean paused;
    private boolean preloadFinished;
    private int preloadNotRequestedBytesCount;
    private RandomAccessFile preloadStream;
    private int preloadStreamFileOffset;
    private byte[] preloadTempBuffer;
    private int preloadTempBufferCount;
    private SparseArray<PreloadRange> preloadedBytesRanges;
    private int priority;
    private int renameRetryCount;
    private ArrayList<RequestInfo> requestInfos;
    private int requestedBytesCount;
    private SparseIntArray requestedPreloadedBytesRanges;
    private boolean requestingCdnOffsets;
    protected boolean requestingReference;
    private int requestsCount;
    private boolean reuploadingCdn;
    private boolean started;
    private volatile int state;
    private File storePath;
    private ArrayList<FileLoadOperationStream> streamListeners;
    private int streamStartOffset;
    private boolean supportsPreloading;
    private File tempPath;
    private int totalBytesCount;
    private int totalPreloadedBytes;
    private boolean ungzip;
    private WebFile webFile;
    private TLRPC.InputWebFileLocation webLocation;
    
    public FileLoadOperation(int webFileDatacenterId, final WebFile webFile) {
        this.preloadTempBuffer = new byte[16];
        this.state = 0;
        this.currentAccount = webFileDatacenterId;
        this.webFile = webFile;
        this.webLocation = webFile.location;
        this.totalBytesCount = webFile.size;
        webFileDatacenterId = MessagesController.getInstance(this.currentAccount).webFileDatacenterId;
        this.datacenterId = webFileDatacenterId;
        this.initialDatacenterId = webFileDatacenterId;
        final String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
        if (webFile.mime_type.startsWith("image/")) {
            this.currentType = 16777216;
        }
        else if (webFile.mime_type.equals("audio/ogg")) {
            this.currentType = 50331648;
        }
        else if (webFile.mime_type.startsWith("video/")) {
            this.currentType = 33554432;
        }
        else {
            this.currentType = 67108864;
        }
        this.allowDisordererFileSave = true;
        this.ext = ImageLoader.getHttpUrlExtension(webFile.url, mimeTypePart);
    }
    
    public FileLoadOperation(final ImageLocation imageLocation, final Object parentObject, String ext, final int totalBytesCount) {
        this.preloadTempBuffer = new byte[16];
        this.state = 0;
        this.parentObject = parentObject;
        if (imageLocation.isEncrypted()) {
            this.location = new TLRPC.TL_inputEncryptedFileLocation();
            final TLRPC.InputFileLocation location = this.location;
            final TLRPC.TL_fileLocationToBeDeprecated location2 = imageLocation.location;
            final long volume_id = location2.volume_id;
            location.id = volume_id;
            location.volume_id = volume_id;
            location.local_id = location2.local_id;
            location.access_hash = imageLocation.access_hash;
            this.iv = new byte[32];
            final byte[] iv = imageLocation.iv;
            final byte[] iv2 = this.iv;
            System.arraycopy(iv, 0, iv2, 0, iv2.length);
            this.key = imageLocation.key;
        }
        else if (imageLocation.photoPeer != null) {
            this.location = new TLRPC.TL_inputPeerPhotoFileLocation();
            final TLRPC.InputFileLocation location3 = this.location;
            final TLRPC.TL_fileLocationToBeDeprecated location4 = imageLocation.location;
            final long volume_id2 = location4.volume_id;
            location3.id = volume_id2;
            location3.volume_id = volume_id2;
            location3.local_id = location4.local_id;
            location3.big = imageLocation.photoPeerBig;
            location3.peer = imageLocation.photoPeer;
        }
        else if (imageLocation.stickerSet != null) {
            this.location = new TLRPC.TL_inputStickerSetThumb();
            final TLRPC.InputFileLocation location5 = this.location;
            final TLRPC.TL_fileLocationToBeDeprecated location6 = imageLocation.location;
            final long volume_id3 = location6.volume_id;
            location5.id = volume_id3;
            location5.volume_id = volume_id3;
            location5.local_id = location6.local_id;
            location5.stickerset = imageLocation.stickerSet;
        }
        else if (imageLocation.thumbSize != null) {
            if (imageLocation.photoId != 0L) {
                this.location = new TLRPC.TL_inputPhotoFileLocation();
                final TLRPC.InputFileLocation location7 = this.location;
                location7.id = imageLocation.photoId;
                final TLRPC.TL_fileLocationToBeDeprecated location8 = imageLocation.location;
                location7.volume_id = location8.volume_id;
                location7.local_id = location8.local_id;
                location7.access_hash = imageLocation.access_hash;
                location7.file_reference = imageLocation.file_reference;
                location7.thumb_size = imageLocation.thumbSize;
            }
            else {
                this.location = new TLRPC.TL_inputDocumentFileLocation();
                final TLRPC.InputFileLocation location9 = this.location;
                location9.id = imageLocation.documentId;
                final TLRPC.TL_fileLocationToBeDeprecated location10 = imageLocation.location;
                location9.volume_id = location10.volume_id;
                location9.local_id = location10.local_id;
                location9.access_hash = imageLocation.access_hash;
                location9.file_reference = imageLocation.file_reference;
                location9.thumb_size = imageLocation.thumbSize;
            }
            final TLRPC.InputFileLocation location11 = this.location;
            if (location11.file_reference == null) {
                location11.file_reference = new byte[0];
            }
        }
        else {
            this.location = new TLRPC.TL_inputFileLocation();
            final TLRPC.InputFileLocation location12 = this.location;
            final TLRPC.TL_fileLocationToBeDeprecated location13 = imageLocation.location;
            location12.volume_id = location13.volume_id;
            location12.local_id = location13.local_id;
            location12.secret = imageLocation.access_hash;
            location12.file_reference = imageLocation.file_reference;
            if (location12.file_reference == null) {
                location12.file_reference = new byte[0];
            }
            this.allowDisordererFileSave = true;
        }
        final int dc_id = imageLocation.dc_id;
        this.datacenterId = dc_id;
        this.initialDatacenterId = dc_id;
        this.currentType = 16777216;
        this.totalBytesCount = totalBytesCount;
        if (ext == null) {
            ext = "jpg";
        }
        this.ext = ext;
    }
    
    public FileLoadOperation(final SecureDocument secureDocument) {
        this.preloadTempBuffer = new byte[16];
        this.state = 0;
        this.location = new TLRPC.TL_inputSecureFileLocation();
        final TLRPC.InputFileLocation location = this.location;
        final TLRPC.TL_secureFile secureFile = secureDocument.secureFile;
        location.id = secureFile.id;
        location.access_hash = secureFile.access_hash;
        this.datacenterId = secureFile.dc_id;
        this.totalBytesCount = secureFile.size;
        this.allowDisordererFileSave = true;
        this.currentType = 67108864;
        this.ext = ".jpg";
    }
    
    public FileLoadOperation(final TLRPC.Document document, Object location) {
        this.preloadTempBuffer = new byte[16];
        this.state = 0;
        try {
            this.parentObject = location;
            if (document instanceof TLRPC.TL_documentEncrypted) {
                location = new TLRPC.TL_inputEncryptedFileLocation();
                this.location = (TLRPC.InputFileLocation)location;
                this.location.id = document.id;
                this.location.access_hash = document.access_hash;
                final int dc_id = document.dc_id;
                this.datacenterId = dc_id;
                this.initialDatacenterId = dc_id;
                this.iv = new byte[32];
                System.arraycopy(document.iv, 0, this.iv, 0, this.iv.length);
                this.key = document.key;
            }
            else if (document instanceof TLRPC.TL_document) {
                location = new TLRPC.TL_inputDocumentFileLocation();
                this.location = (TLRPC.InputFileLocation)location;
                this.location.id = document.id;
                this.location.access_hash = document.access_hash;
                this.location.file_reference = document.file_reference;
                this.location.thumb_size = "";
                if (this.location.file_reference == null) {
                    this.location.file_reference = new byte[0];
                }
                final int dc_id2 = document.dc_id;
                this.datacenterId = dc_id2;
                this.initialDatacenterId = dc_id2;
                this.allowDisordererFileSave = true;
                for (int size = document.attributes.size(), i = 0; i < size; ++i) {
                    if (document.attributes.get(i) instanceof TLRPC.TL_documentAttributeVideo) {
                        this.supportsPreloading = true;
                        break;
                    }
                }
            }
            this.ungzip = "application/x-tgsticker".equals(document.mime_type);
            this.totalBytesCount = document.size;
            if (this.key != null && this.totalBytesCount % 16 != 0) {
                this.bytesCountPadding = 16 - this.totalBytesCount % 16;
                this.totalBytesCount += this.bytesCountPadding;
            }
            this.ext = FileLoader.getDocumentFileName(document);
            Label_0399: {
                if (this.ext != null) {
                    final int lastIndex = this.ext.lastIndexOf(46);
                    if (lastIndex != -1) {
                        this.ext = this.ext.substring(lastIndex);
                        break Label_0399;
                    }
                }
                this.ext = "";
            }
            if ("audio/ogg".equals(document.mime_type)) {
                this.currentType = 50331648;
            }
            else if (FileLoader.isVideoMimeType(document.mime_type)) {
                this.currentType = 33554432;
            }
            else {
                this.currentType = 67108864;
            }
            if (this.ext.length() <= 1) {
                this.ext = FileLoader.getExtensionByMimeType(document.mime_type);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.onFail(true, 0);
        }
    }
    
    private void addPart(final ArrayList<Range> list, int i, int n, final boolean b) {
        if (list != null) {
            if (n >= i) {
                final int size = list.size();
                final int n2 = 0;
                int n3 = 0;
                int n4;
                while (true) {
                    final boolean b2 = true;
                    if (n3 >= size) {
                        n4 = 0;
                        break;
                    }
                    final Range range = list.get(n3);
                    if (i <= range.start) {
                        if (n >= range.end) {
                            list.remove(n3);
                            n4 = (b2 ? 1 : 0);
                            break;
                        }
                        if (n > range.start) {
                            range.start = n;
                            n4 = (b2 ? 1 : 0);
                            break;
                        }
                    }
                    else {
                        if (n < range.end) {
                            list.add(0, new Range(range.start, i));
                            range.start = n;
                            n4 = (b2 ? 1 : 0);
                            break;
                        }
                        if (i < range.end) {
                            range.end = i;
                            n4 = (b2 ? 1 : 0);
                            break;
                        }
                    }
                    ++n3;
                }
                if (b) {
                    if (n4 != 0) {
                        try {
                            this.filePartsStream.seek(0L);
                            n = list.size();
                            this.filePartsStream.writeInt(n);
                            Range range2;
                            for (i = 0; i < n; ++i) {
                                range2 = list.get(i);
                                this.filePartsStream.writeInt(range2.start);
                                this.filePartsStream.writeInt(range2.end);
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        final ArrayList<FileLoadOperationStream> streamListeners = this.streamListeners;
                        if (streamListeners != null) {
                            for (n = streamListeners.size(), i = n2; i < n; ++i) {
                                this.streamListeners.get(i).newDataAvailable();
                            }
                        }
                    }
                    else if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(this.cacheFileFinal);
                        sb.append(" downloaded duplicate file part ");
                        sb.append(i);
                        sb.append(" - ");
                        sb.append(n);
                        FileLog.e(sb.toString());
                    }
                }
            }
        }
    }
    
    private void cleanup() {
        try {
            if (this.fileOutputStream != null) {
                try {
                    this.fileOutputStream.getChannel().close();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                this.fileOutputStream.close();
                this.fileOutputStream = null;
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        try {
            if (this.preloadStream != null) {
                try {
                    this.preloadStream.getChannel().close();
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
                this.preloadStream.close();
                this.preloadStream = null;
            }
        }
        catch (Exception ex4) {
            FileLog.e(ex4);
        }
        try {
            if (this.fileReadStream != null) {
                try {
                    this.fileReadStream.getChannel().close();
                }
                catch (Exception ex5) {
                    FileLog.e(ex5);
                }
                this.fileReadStream.close();
                this.fileReadStream = null;
            }
        }
        catch (Exception ex6) {
            FileLog.e(ex6);
        }
        try {
            if (this.filePartsStream != null) {
                try {
                    this.filePartsStream.getChannel().close();
                }
                catch (Exception ex7) {
                    FileLog.e(ex7);
                }
                this.filePartsStream.close();
                this.filePartsStream = null;
            }
        }
        catch (Exception ex8) {
            FileLog.e(ex8);
        }
        try {
            if (this.fiv != null) {
                this.fiv.close();
                this.fiv = null;
            }
        }
        catch (Exception ex9) {
            FileLog.e(ex9);
        }
        if (this.delayedRequestInfos != null) {
            for (int i = 0; i < this.delayedRequestInfos.size(); ++i) {
                final RequestInfo requestInfo = this.delayedRequestInfos.get(i);
                if (requestInfo.response != null) {
                    requestInfo.response.disableFree = false;
                    requestInfo.response.freeResources();
                }
                else if (requestInfo.responseWeb != null) {
                    requestInfo.responseWeb.disableFree = false;
                    requestInfo.responseWeb.freeResources();
                }
                else if (requestInfo.responseCdn != null) {
                    requestInfo.responseCdn.disableFree = false;
                    requestInfo.responseCdn.freeResources();
                }
            }
            this.delayedRequestInfos.clear();
        }
    }
    
    private void clearOperaion(RequestInfo requestInfo, final boolean b) {
        int i = 0;
        int n = Integer.MAX_VALUE;
        while (i < this.requestInfos.size()) {
            final RequestInfo requestInfo2 = this.requestInfos.get(i);
            n = Math.min(requestInfo2.offset, n);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.delete(requestInfo2.offset);
            }
            else {
                this.removePart(this.notRequestedBytesRanges, requestInfo2.offset, requestInfo2.offset + this.currentDownloadChunkSize);
            }
            if (requestInfo != requestInfo2) {
                if (requestInfo2.requestToken != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(requestInfo2.requestToken, true);
                }
            }
            ++i;
        }
        this.requestInfos.clear();
        for (int j = 0; j < this.delayedRequestInfos.size(); ++j) {
            requestInfo = this.delayedRequestInfos.get(j);
            if (this.isPreloadVideoOperation) {
                this.requestedPreloadedBytesRanges.delete(requestInfo.offset);
            }
            else {
                this.removePart(this.notRequestedBytesRanges, requestInfo.offset, requestInfo.offset + this.currentDownloadChunkSize);
            }
            if (requestInfo.response != null) {
                requestInfo.response.disableFree = false;
                requestInfo.response.freeResources();
            }
            else if (requestInfo.responseWeb != null) {
                requestInfo.responseWeb.disableFree = false;
                requestInfo.responseWeb.freeResources();
            }
            else if (requestInfo.responseCdn != null) {
                requestInfo.responseCdn.disableFree = false;
                requestInfo.responseCdn.freeResources();
            }
            n = Math.min(requestInfo.offset, n);
        }
        this.delayedRequestInfos.clear();
        this.requestsCount = 0;
        if (!b && this.isPreloadVideoOperation) {
            this.requestedBytesCount = this.totalPreloadedBytes;
        }
        else if (this.notLoadedBytesRanges == null) {
            this.downloadedBytes = n;
            this.requestedBytesCount = n;
        }
    }
    
    private void copyNotLoadedRanges() {
        final ArrayList<Range> notLoadedBytesRanges = this.notLoadedBytesRanges;
        if (notLoadedBytesRanges == null) {
            return;
        }
        this.notLoadedBytesRangesCopy = new ArrayList<Range>(notLoadedBytesRanges);
    }
    
    private void delayRequestInfo(final RequestInfo e) {
        this.delayedRequestInfos.add(e);
        if (e.response != null) {
            e.response.disableFree = true;
        }
        else if (e.responseWeb != null) {
            e.responseWeb.disableFree = true;
        }
        else if (e.responseCdn != null) {
            e.responseCdn.disableFree = true;
        }
    }
    
    private int findNextPreloadDownloadOffset(int n, final int n2, final NativeByteBuffer nativeByteBuffer) {
        final int limit = nativeByteBuffer.limit();
        int n3;
        int n4;
        do {
            int n5;
            if (this.preloadTempBuffer != null) {
                n5 = 16;
            }
            else {
                n5 = 0;
            }
            if (n >= n2 - n5) {
                n4 = n2 + limit;
                if (n < n4) {
                    if (n >= n4 - 16) {
                        this.preloadTempBufferCount = n4 - n;
                        nativeByteBuffer.position(nativeByteBuffer.limit() - this.preloadTempBufferCount);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, this.preloadTempBufferCount, false);
                        return n4;
                    }
                    if (this.preloadTempBufferCount != 0) {
                        nativeByteBuffer.position(0);
                        final byte[] preloadTempBuffer = this.preloadTempBuffer;
                        final int preloadTempBufferCount = this.preloadTempBufferCount;
                        nativeByteBuffer.readBytes(preloadTempBuffer, preloadTempBufferCount, 16 - preloadTempBufferCount, false);
                        this.preloadTempBufferCount = 0;
                    }
                    else {
                        nativeByteBuffer.position(n - n2);
                        nativeByteBuffer.readBytes(this.preloadTempBuffer, 0, 16, false);
                    }
                    final byte[] preloadTempBuffer2 = this.preloadTempBuffer;
                    final int n6 = ((preloadTempBuffer2[0] & 0xFF) << 24) + ((preloadTempBuffer2[1] & 0xFF) << 16) + ((preloadTempBuffer2[2] & 0xFF) << 8) + (preloadTempBuffer2[3] & 0xFF);
                    if (n6 == 0) {
                        return 0;
                    }
                    int n7;
                    if ((n7 = n6) == 1) {
                        n7 = ((preloadTempBuffer2[12] & 0xFF) << 24) + ((preloadTempBuffer2[13] & 0xFF) << 16) + ((preloadTempBuffer2[14] & 0xFF) << 8) + (preloadTempBuffer2[15] & 0xFF);
                    }
                    final byte[] preloadTempBuffer3 = this.preloadTempBuffer;
                    if (preloadTempBuffer3[4] == 109 && preloadTempBuffer3[5] == 111 && preloadTempBuffer3[6] == 111 && preloadTempBuffer3[7] == 118) {
                        return -n7;
                    }
                    n3 = n + n7;
                    continue;
                }
            }
            return 0;
        } while ((n = n3) < n4);
        return n3;
    }
    
    private int getDownloadedLengthFromOffsetInternal(final ArrayList<Range> list, final int n, final int a) {
        if (list != null && this.state != 3 && !list.isEmpty()) {
            final int size = list.size();
            int n2 = a;
            Range range = null;
            Range range3;
            int n3;
            for (int i = 0; i < size; ++i, n2 = n3, range = range3) {
                final Range range2 = list.get(i);
                range3 = range;
                Label_0094: {
                    if (n <= range2.start) {
                        if (range != null) {
                            range3 = range;
                            if (range2.start >= range.start) {
                                break Label_0094;
                            }
                        }
                        range3 = range2;
                    }
                }
                n3 = n2;
                if (range2.start <= n) {
                    n3 = n2;
                    if (range2.end > n) {
                        n3 = 0;
                    }
                }
            }
            if (n2 == 0) {
                return 0;
            }
            if (range != null) {
                return Math.min(a, range.start - n);
            }
            return Math.min(a, Math.max(this.totalBytesCount - n, 0));
        }
        else {
            final int downloadedBytes = this.downloadedBytes;
            if (downloadedBytes == 0) {
                return a;
            }
            return Math.min(a, Math.max(downloadedBytes - n, 0));
        }
    }
    
    private void onFinishLoadingFile(final boolean b) {
        if (this.state != 1) {
            return;
        }
        this.state = 3;
        this.cleanup();
        if (this.isPreloadVideoOperation) {
            this.preloadFinished = true;
            if (BuildVars.DEBUG_VERSION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("finished preloading file to ");
                sb.append(this.cacheFileTemp);
                sb.append(" loaded ");
                sb.append(this.totalPreloadedBytes);
                sb.append(" of ");
                sb.append(this.totalBytesCount);
                FileLog.d(sb.toString());
            }
        }
        else {
            final File cacheIvTemp = this.cacheIvTemp;
            if (cacheIvTemp != null) {
                cacheIvTemp.delete();
                this.cacheIvTemp = null;
            }
            final File cacheFileParts = this.cacheFileParts;
            if (cacheFileParts != null) {
                cacheFileParts.delete();
                this.cacheFileParts = null;
            }
            final File cacheFilePreload = this.cacheFilePreload;
            if (cacheFilePreload != null) {
                cacheFilePreload.delete();
                this.cacheFilePreload = null;
            }
            final File cacheFileTemp = this.cacheFileTemp;
            if (cacheFileTemp != null) {
                if (this.ungzip) {
                    try {
                        final GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(cacheFileTemp));
                        FileLoader.copyFile(gzipInputStream, this.cacheFileFinal);
                        gzipInputStream.close();
                        this.cacheFileTemp.delete();
                    }
                    catch (Throwable t) {
                        FileLog.e(t);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("unable to ungzip temp = ");
                        sb2.append(this.cacheFileTemp);
                        sb2.append(" to final = ");
                        sb2.append(this.cacheFileFinal);
                        FileLog.e(sb2.toString());
                    }
                    catch (ZipException ex) {
                        this.ungzip = false;
                    }
                }
                if (!this.ungzip && !this.cacheFileTemp.renameTo(this.cacheFileFinal)) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("unable to rename temp = ");
                        sb3.append(this.cacheFileTemp);
                        sb3.append(" to final = ");
                        sb3.append(this.cacheFileFinal);
                        sb3.append(" retry = ");
                        sb3.append(this.renameRetryCount);
                        FileLog.e(sb3.toString());
                    }
                    ++this.renameRetryCount;
                    if (this.renameRetryCount < 3) {
                        this.state = 1;
                        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$Pg1188DV6hLAQ13wzfrMjXc2Ie0(this, b), 200L);
                        return;
                    }
                    this.cacheFileFinal = this.cacheFileTemp;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("finished downloading file to ");
                sb4.append(this.cacheFileFinal);
                FileLog.d(sb4.toString());
            }
            if (b) {
                final int currentType = this.currentType;
                if (currentType == 50331648) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 3, 1);
                }
                else if (currentType == 33554432) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 2, 1);
                }
                else if (currentType == 16777216) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 4, 1);
                }
                else if (currentType == 67108864) {
                    StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 5, 1);
                }
            }
        }
        this.delegate.didFinishLoadingFile(this, this.cacheFileFinal);
    }
    
    private void removePart(final ArrayList<Range> list, final int n, final int n2) {
        if (list != null) {
            if (n2 >= n) {
                final int size = list.size();
                int index = 0;
                int n3;
                while (true) {
                    final boolean b = true;
                    if (index >= size) {
                        n3 = 0;
                        break;
                    }
                    final Range range = list.get(index);
                    if (n == range.end) {
                        range.end = n2;
                        n3 = (b ? 1 : 0);
                        break;
                    }
                    if (n2 == range.start) {
                        range.start = n;
                        n3 = (b ? 1 : 0);
                        break;
                    }
                    ++index;
                }
                if (n3 == 0) {
                    list.add(new Range(n, n2));
                }
            }
        }
    }
    
    private void requestFileOffsets(final int offset) {
        if (this.requestingCdnOffsets) {
            return;
        }
        this.requestingCdnOffsets = true;
        final TLRPC.TL_upload_getCdnFileHashes tl_upload_getCdnFileHashes = new TLRPC.TL_upload_getCdnFileHashes();
        tl_upload_getCdnFileHashes.file_token = this.cdnToken;
        tl_upload_getCdnFileHashes.offset = offset;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_upload_getCdnFileHashes, new _$$Lambda$FileLoadOperation$AloCOvGHlndklEjA6lccwgvlez8(this), null, null, 0, this.datacenterId, 1, true);
    }
    
    private void requestReference(final RequestInfo requestInfo) {
        if (this.requestingReference) {
            return;
        }
        this.clearOperaion(requestInfo, false);
        this.requestingReference = true;
        final Object parentObject = this.parentObject;
        if (parentObject instanceof MessageObject) {
            final MessageObject messageObject = (MessageObject)parentObject;
            if (messageObject.getId() < 0) {
                final TLRPC.WebPage webpage = messageObject.messageOwner.media.webpage;
                if (webpage != null) {
                    this.parentObject = webpage;
                }
            }
        }
        FileRefController.getInstance(this.currentAccount).requestReference(this.parentObject, this.location, this, requestInfo);
    }
    
    public void cancel() {
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$J6yoj3V6lUrD2GOh_IqPARM2LhA(this));
    }
    
    protected File getCurrentFile() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final File[] array = { null };
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$sszlpx_7B35gY8yLDFGHhHn8Nio(this, array, countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return array[0];
    }
    
    public int getCurrentType() {
        return this.currentType;
    }
    
    public int getDatacenterId() {
        return this.initialDatacenterId;
    }
    
    protected float getDownloadedLengthFromOffset(final float n) {
        final ArrayList<Range> notLoadedBytesRangesCopy = this.notLoadedBytesRangesCopy;
        final int totalBytesCount = this.totalBytesCount;
        if (totalBytesCount != 0 && notLoadedBytesRangesCopy != null) {
            return n + this.getDownloadedLengthFromOffsetInternal(notLoadedBytesRangesCopy, (int)(totalBytesCount * n), totalBytesCount) / (float)this.totalBytesCount;
        }
        return 0.0f;
    }
    
    protected int getDownloadedLengthFromOffset(final int n, final int n2) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final int[] array = { 0 };
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$7ur7HYEmrvSDh4lwc7_mdrw1G5A(this, array, n, n2, countDownLatch));
        try {
            countDownLatch.await();
            return array[0];
        }
        catch (Exception ex) {
            return array[0];
        }
    }
    
    public String getFileName() {
        if (this.location != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.location.volume_id);
            sb.append("_");
            sb.append(this.location.local_id);
            sb.append(".");
            sb.append(this.ext);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(Utilities.MD5(this.webFile.url));
        sb2.append(".");
        sb2.append(this.ext);
        return sb2.toString();
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public boolean isForceRequest() {
        return this.isForceRequest;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }
    
    public boolean isPreloadVideoOperation() {
        return this.isPreloadVideoOperation;
    }
    
    protected void onFail(final boolean b, final int n) {
        this.cleanup();
        this.state = 2;
        if (b) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$9spswKeKfAcOLWXDEy_zgCWBTtA(this, n));
        }
        else {
            this.delegate.didFailedLoadingFile(this, n);
        }
    }
    
    public void pause() {
        if (this.state != 1) {
            return;
        }
        this.paused = true;
    }
    
    protected boolean processRequestResult(RequestInfo o, final TLRPC.TL_error tl_error) {
        if (this.state != 1) {
            if (BuildVars.DEBUG_VERSION) {
                final StringBuilder sb = new StringBuilder();
                sb.append("trying to write to finished file ");
                sb.append(this.cacheFileFinal);
                sb.append(" offset ");
                sb.append(o.offset);
                FileLog.d(sb.toString());
            }
            return false;
        }
        this.requestInfos.remove(o);
        Object preloadedBytesRanges = null;
        if (tl_error == null) {
            try {
                if (this.notLoadedBytesRanges == null && this.downloadedBytes != o.offset) {
                    this.delayRequestInfo(o);
                    return false;
                }
                NativeByteBuffer nativeByteBuffer;
                if (o.response != null) {
                    nativeByteBuffer = o.response.bytes;
                }
                else if (o.responseWeb != null) {
                    nativeByteBuffer = o.responseWeb.bytes;
                }
                else if (o.responseCdn != null) {
                    nativeByteBuffer = o.responseCdn.bytes;
                }
                else {
                    nativeByteBuffer = null;
                }
                if (nativeByteBuffer == null || nativeByteBuffer.limit() == 0) {
                    this.onFinishLoadingFile(true);
                    return false;
                }
                final int limit = nativeByteBuffer.limit();
                if (this.isCdn) {
                    final int n = o.offset / 131072 * 131072;
                    if (this.cdnHashes != null) {
                        preloadedBytesRanges = this.cdnHashes.get(n);
                    }
                    else {
                        preloadedBytesRanges = null;
                    }
                    if (preloadedBytesRanges == null) {
                        this.delayRequestInfo(o);
                        this.requestFileOffsets(n);
                        return true;
                    }
                }
                if (o.responseCdn != null) {
                    final int n2 = o.offset / 16;
                    this.cdnIv[15] = (byte)(n2 & 0xFF);
                    this.cdnIv[14] = (byte)(n2 >> 8 & 0xFF);
                    this.cdnIv[13] = (byte)(n2 >> 16 & 0xFF);
                    this.cdnIv[12] = (byte)(n2 >> 24 & 0xFF);
                    Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.cdnKey, this.cdnIv, 0, nativeByteBuffer.limit());
                }
                int n4;
                if (this.isPreloadVideoOperation) {
                    this.preloadStream.writeInt(o.offset);
                    this.preloadStream.writeInt(limit);
                    this.preloadStreamFileOffset += 8;
                    this.preloadStream.getChannel().write(nativeByteBuffer.buffer);
                    if (BuildVars.DEBUG_VERSION) {
                        preloadedBytesRanges = new StringBuilder();
                        ((StringBuilder)preloadedBytesRanges).append("save preload file part ");
                        ((StringBuilder)preloadedBytesRanges).append(this.cacheFilePreload);
                        ((StringBuilder)preloadedBytesRanges).append(" offset ");
                        ((StringBuilder)preloadedBytesRanges).append(o.offset);
                        ((StringBuilder)preloadedBytesRanges).append(" size ");
                        ((StringBuilder)preloadedBytesRanges).append(limit);
                        FileLog.d(((StringBuilder)preloadedBytesRanges).toString());
                    }
                    if (this.preloadedBytesRanges == null) {
                        preloadedBytesRanges = new SparseArray();
                        this.preloadedBytesRanges = (SparseArray<PreloadRange>)preloadedBytesRanges;
                    }
                    preloadedBytesRanges = this.preloadedBytesRanges;
                    ((SparseArray)preloadedBytesRanges).put(o.offset, (Object)new PreloadRange(this.preloadStreamFileOffset, o.offset, limit));
                    this.totalPreloadedBytes += limit;
                    this.preloadStreamFileOffset += limit;
                    if (this.moovFound == 0) {
                        int nextPreloadDownloadOffset = this.findNextPreloadDownloadOffset(this.nextAtomOffset, o.offset, nativeByteBuffer);
                        if (nextPreloadDownloadOffset < 0) {
                            nextPreloadDownloadOffset *= -1;
                            this.nextPreloadDownloadOffset += this.currentDownloadChunkSize;
                            if (this.nextPreloadDownloadOffset < this.totalBytesCount / 2) {
                                final int n3 = 1048576 + nextPreloadDownloadOffset;
                                this.foundMoovSize = n3;
                                this.preloadNotRequestedBytesCount = n3;
                                this.moovFound = 1;
                            }
                            else {
                                this.foundMoovSize = 2097152;
                                this.preloadNotRequestedBytesCount = 2097152;
                                this.moovFound = 2;
                            }
                            this.nextPreloadDownloadOffset = -1;
                        }
                        else {
                            this.nextPreloadDownloadOffset = nextPreloadDownloadOffset / this.currentDownloadChunkSize * this.currentDownloadChunkSize;
                        }
                        this.nextAtomOffset = nextPreloadDownloadOffset;
                    }
                    this.preloadStream.writeInt(this.foundMoovSize);
                    this.preloadStream.writeInt(this.nextPreloadDownloadOffset);
                    this.preloadStream.writeInt(this.nextAtomOffset);
                    this.preloadStreamFileOffset += 12;
                    final boolean b = this.nextPreloadDownloadOffset == 0 || (this.moovFound != 0 && this.foundMoovSize < 0) || this.totalPreloadedBytes > 2097152 || this.nextPreloadDownloadOffset >= this.totalBytesCount;
                    if (b) {
                        this.preloadStream.seek(0L);
                        this.preloadStream.write(1);
                        n4 = (b ? 1 : 0);
                    }
                    else {
                        n4 = (b ? 1 : 0);
                        if (this.moovFound != 0) {
                            this.foundMoovSize -= this.currentDownloadChunkSize;
                            n4 = (b ? 1 : 0);
                        }
                    }
                }
                else {
                    this.downloadedBytes += limit;
                    int n5 = 0;
                    Label_0935: {
                        Label_0876: {
                            if (this.totalBytesCount > 0) {
                                if (this.downloadedBytes < this.totalBytesCount) {
                                    break Label_0876;
                                }
                            }
                            else if (limit == this.currentDownloadChunkSize) {
                                if (this.totalBytesCount != this.downloadedBytes && this.downloadedBytes % this.currentDownloadChunkSize == 0) {
                                    break Label_0876;
                                }
                                if (this.totalBytesCount > 0) {
                                    if (this.totalBytesCount > this.downloadedBytes) {
                                        break Label_0876;
                                    }
                                }
                            }
                            n5 = 1;
                            break Label_0935;
                        }
                        n5 = 0;
                    }
                    if (this.key != null) {
                        Utilities.aesIgeEncryption(nativeByteBuffer.buffer, this.key, this.iv, false, true, 0, nativeByteBuffer.limit());
                        if (n5 && this.bytesCountPadding != 0) {
                            nativeByteBuffer.limit(nativeByteBuffer.limit() - this.bytesCountPadding);
                        }
                    }
                    if (this.encryptFile) {
                        final int n6 = o.offset / 16;
                        this.encryptIv[15] = (byte)(n6 & 0xFF);
                        this.encryptIv[14] = (byte)(n6 >> 8 & 0xFF);
                        this.encryptIv[13] = (byte)(n6 >> 16 & 0xFF);
                        this.encryptIv[12] = (byte)(n6 >> 24 & 0xFF);
                        Utilities.aesCtrDecryption(nativeByteBuffer.buffer, this.encryptKey, this.encryptIv, 0, nativeByteBuffer.limit());
                    }
                    if (this.notLoadedBytesRanges != null) {
                        this.fileOutputStream.seek(o.offset);
                        if (BuildVars.DEBUG_VERSION) {
                            preloadedBytesRanges = new StringBuilder();
                            ((StringBuilder)preloadedBytesRanges).append("save file part ");
                            ((StringBuilder)preloadedBytesRanges).append(this.cacheFileFinal);
                            ((StringBuilder)preloadedBytesRanges).append(" offset ");
                            ((StringBuilder)preloadedBytesRanges).append(o.offset);
                            FileLog.d(((StringBuilder)preloadedBytesRanges).toString());
                        }
                    }
                    this.fileOutputStream.getChannel().write(nativeByteBuffer.buffer);
                    this.addPart(this.notLoadedBytesRanges, o.offset, o.offset + limit, true);
                    Label_1689: {
                        if (this.isCdn) {
                            final int n7 = o.offset / 131072;
                            final int size = this.notCheckedCdnRanges.size();
                            int i = 0;
                            while (true) {
                                while (i < size) {
                                    final Range range = this.notCheckedCdnRanges.get(i);
                                    if (range.start <= n7 && n7 <= range.end) {
                                        final boolean b2 = false;
                                        if (b2) {
                                            break Label_1689;
                                        }
                                        final int n8 = n7 * 131072;
                                        final int downloadedLengthFromOffsetInternal = this.getDownloadedLengthFromOffsetInternal(this.notLoadedBytesRanges, n8, 131072);
                                        if (downloadedLengthFromOffsetInternal == 0 || (downloadedLengthFromOffsetInternal != 131072 && (this.totalBytesCount <= 0 || downloadedLengthFromOffsetInternal != this.totalBytesCount - n8) && (this.totalBytesCount > 0 || n5 == 0))) {
                                            break Label_1689;
                                        }
                                        final TLRPC.TL_fileHash tl_fileHash = (TLRPC.TL_fileHash)this.cdnHashes.get(n8);
                                        if (this.fileReadStream == null) {
                                            this.cdnCheckBytes = new byte[131072];
                                            this.fileReadStream = new RandomAccessFile(this.cacheFileTemp, "r");
                                        }
                                        this.fileReadStream.seek(n8);
                                        this.fileReadStream.readFully(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal);
                                        if (!Arrays.equals(Utilities.computeSHA256(this.cdnCheckBytes, 0, downloadedLengthFromOffsetInternal), tl_fileHash.hash)) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                if (this.location != null) {
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("invalid cdn hash ");
                                                    sb2.append(this.location);
                                                    sb2.append(" id = ");
                                                    sb2.append(this.location.id);
                                                    sb2.append(" local_id = ");
                                                    sb2.append(this.location.local_id);
                                                    sb2.append(" access_hash = ");
                                                    sb2.append(this.location.access_hash);
                                                    sb2.append(" volume_id = ");
                                                    sb2.append(this.location.volume_id);
                                                    sb2.append(" secret = ");
                                                    sb2.append(this.location.secret);
                                                    FileLog.e(sb2.toString());
                                                }
                                                else if (this.webLocation != null) {
                                                    final StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("invalid cdn hash  ");
                                                    sb3.append(this.webLocation);
                                                    sb3.append(" id = ");
                                                    sb3.append(this.getFileName());
                                                    FileLog.e(sb3.toString());
                                                }
                                            }
                                            this.onFail(false, 0);
                                            this.cacheFileTemp.delete();
                                            return false;
                                        }
                                        this.cdnHashes.remove(n8);
                                        this.addPart(this.notCheckedCdnRanges, n7, n7 + 1, false);
                                        break Label_1689;
                                    }
                                    else {
                                        ++i;
                                    }
                                }
                                final boolean b2 = true;
                                continue;
                            }
                        }
                    }
                    if (this.fiv != null) {
                        this.fiv.seek(0L);
                        this.fiv.write(this.iv);
                    }
                    if (this.totalBytesCount > 0 && this.state == 1) {
                        this.copyNotLoadedRanges();
                        this.delegate.didChangedLoadProgress(this, Math.min(1.0f, this.downloadedBytes / (float)this.totalBytesCount));
                    }
                    n4 = n5;
                }
                int j = 0;
                while (j < this.delayedRequestInfos.size()) {
                    o = this.delayedRequestInfos.get(j);
                    if (this.notLoadedBytesRanges == null && this.downloadedBytes != o.offset) {
                        ++j;
                    }
                    else {
                        this.delayedRequestInfos.remove(j);
                        if (this.processRequestResult(o, null)) {
                            break;
                        }
                        if (o.response != null) {
                            o.response.disableFree = false;
                            o.response.freeResources();
                            break;
                        }
                        if (o.responseWeb != null) {
                            o.responseWeb.disableFree = false;
                            o.responseWeb.freeResources();
                            break;
                        }
                        if (o.responseCdn != null) {
                            o.responseCdn.disableFree = false;
                            o.responseCdn.freeResources();
                            break;
                        }
                        break;
                    }
                }
                if (n4 != 0) {
                    this.onFinishLoadingFile(true);
                    return false;
                }
                this.startDownloadRequest();
                return false;
            }
            catch (Exception ex) {
                this.onFail(false, 0);
                FileLog.e(ex);
                return false;
            }
        }
        Label_2038: {
            if (!tl_error.text.contains("FILE_MIGRATE_")) {
                break Label_2038;
            }
            final Scanner scanner = new Scanner(tl_error.text.replace("FILE_MIGRATE_", ""));
            scanner.useDelimiter("");
        Block_74_Outer:
            while (true) {
                try {
                    final Serializable value = scanner.nextInt();
                    if (value == null) {
                        this.onFail(false, 0);
                    }
                    else {
                        this.datacenterId = (int)value;
                        this.downloadedBytes = 0;
                        this.requestedBytesCount = 0;
                        this.startDownloadRequest();
                    }
                    return false;
                    // iftrue(Label_2116:, !tl_error.text.contains((CharSequence)"RETRY_LIMIT"))
                Block_73_Outer:
                    while (true) {
                    Block_70_Outer:
                        while (true) {
                            Block_72: {
                                while (true) {
                                    Label_2338: {
                                        while (true) {
                                            this.onFail(false, 2);
                                            return false;
                                            final StringBuilder sb4 = new StringBuilder();
                                            sb4.append(tl_error.text);
                                            sb4.append(" ");
                                            sb4.append(this.webLocation);
                                            sb4.append(" id = ");
                                            sb4.append(this.getFileName());
                                            FileLog.e(sb4.toString());
                                            break Label_2338;
                                            Label_2094: {
                                                continue Block_74_Outer;
                                            }
                                        }
                                        final StringBuilder sb5 = new StringBuilder();
                                        sb5.append(tl_error.text);
                                        sb5.append(" ");
                                        sb5.append(this.location);
                                        sb5.append(" id = ");
                                        sb5.append(this.location.id);
                                        sb5.append(" local_id = ");
                                        sb5.append(this.location.local_id);
                                        sb5.append(" access_hash = ");
                                        sb5.append(this.location.access_hash);
                                        sb5.append(" volume_id = ");
                                        sb5.append(this.location.volume_id);
                                        sb5.append(" secret = ");
                                        sb5.append(this.location.secret);
                                        FileLog.e(sb5.toString());
                                        break Label_2338;
                                        try {
                                            this.onFinishLoadingFile(true);
                                        }
                                        catch (Exception ex2) {
                                            FileLog.e(ex2);
                                            this.onFail(false, 0);
                                        }
                                        return false;
                                        Label_2116: {
                                            break Block_72;
                                        }
                                    }
                                    this.onFail(false, 0);
                                    return false;
                                    continue;
                                }
                                Label_2085: {
                                    this.onFail(false, 0);
                                }
                                return false;
                            }
                            continue Block_70_Outer;
                        }
                        Label_2273: {
                            continue Block_73_Outer;
                        }
                    }
                }
                // iftrue(Label_2338:, !BuildVars.LOGS_ENABLED)
                // iftrue(Label_2094:, !tl_error.text.contains((CharSequence)"OFFSET_INVALID"))
                // iftrue(Label_2085:, this.downloadedBytes % this.currentDownloadChunkSize != 0)
                // iftrue(Label_2273:, this.location == null)
                // iftrue(Label_2338:, this.webLocation == null)
                catch (Exception ex3) {
                    final Serializable value = (Serializable)preloadedBytesRanges;
                    continue;
                }
                break;
            }
        }
    }
    
    protected void removeStreamListener(final FileStreamLoadOperation fileStreamLoadOperation) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$pRXavC_xR0CSUF8u1ykitHqvWcY(this, fileStreamLoadOperation));
    }
    
    public void setDelegate(final FileLoadOperationDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setEncryptFile(final boolean encryptFile) {
        this.encryptFile = encryptFile;
        if (this.encryptFile) {
            this.allowDisordererFileSave = false;
        }
    }
    
    public void setForceRequest(final boolean isForceRequest) {
        this.isForceRequest = isForceRequest;
    }
    
    public void setIsPreloadVideoOperation(final boolean isPreloadVideoOperation) {
        if (this.isPreloadVideoOperation != isPreloadVideoOperation) {
            if (!isPreloadVideoOperation || this.totalBytesCount > 2097152) {
                if (!isPreloadVideoOperation && this.isPreloadVideoOperation) {
                    if (this.state == 3) {
                        this.isPreloadVideoOperation = isPreloadVideoOperation;
                        this.state = 0;
                        this.preloadFinished = false;
                        this.start();
                    }
                    else if (this.state == 1) {
                        Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$dCUC8Z2YsvYYD1fE6icyc8raIws(this, isPreloadVideoOperation));
                    }
                    else {
                        this.isPreloadVideoOperation = isPreloadVideoOperation;
                    }
                }
                else {
                    this.isPreloadVideoOperation = isPreloadVideoOperation;
                }
            }
        }
    }
    
    public void setPaths(final int currentAccount, final File storePath, final File tempPath) {
        this.storePath = storePath;
        this.tempPath = tempPath;
        this.currentAccount = currentAccount;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public boolean start() {
        return this.start(null, 0);
    }
    
    public boolean start(FileLoadOperationStream ex, int n) {
        if (this.currentDownloadChunkSize == 0) {
            int currentDownloadChunkSize;
            if (this.totalBytesCount >= 1048576) {
                currentDownloadChunkSize = 131072;
            }
            else {
                currentDownloadChunkSize = 32768;
            }
            this.currentDownloadChunkSize = currentDownloadChunkSize;
            final int totalBytesCount = this.totalBytesCount;
            this.currentMaxDownloadRequests = 4;
        }
        final boolean b = this.state != 0;
        final boolean paused = this.paused;
        this.paused = false;
        if (ex != null) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$LZcjmIEbWfyJMJubpJYFWtYZ3uM(this, n, (FileLoadOperationStream)ex, b));
        }
        else if (paused && b) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$HfxdqGkDOxPIykHiS0VM1dK9UjM(this));
        }
        if (b) {
            return paused;
        }
        if (this.location == null && this.webLocation == null) {
            this.onFail(true, 0);
            return false;
        }
        final int currentDownloadChunkSize2 = this.currentDownloadChunkSize;
        this.streamStartOffset = n / currentDownloadChunkSize2 * currentDownloadChunkSize2;
        if (this.allowDisordererFileSave) {
            n = this.totalBytesCount;
            if (n > 0 && n > currentDownloadChunkSize2) {
                this.notLoadedBytesRanges = new ArrayList<Range>();
                this.notRequestedBytesRanges = new ArrayList<Range>();
            }
        }
        String string2 = null;
        String child = null;
        String child2 = null;
        String child3 = null;
        String child4 = null;
        Label_1619: {
            if (this.webLocation != null) {
                final String md5 = Utilities.MD5(this.webFile.url);
                String string = null;
                Label_0472: {
                    if (this.encryptFile) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(md5);
                        sb.append(".temp.enc");
                        string = sb.toString();
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(md5);
                        sb2.append(".");
                        sb2.append(this.ext);
                        sb2.append(".enc");
                        string2 = sb2.toString();
                        if (this.key != null) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(md5);
                            sb3.append(".iv.enc");
                            child = sb3.toString();
                            break Label_0472;
                        }
                    }
                    else {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(md5);
                        sb4.append(".temp");
                        final String string3 = sb4.toString();
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(md5);
                        sb5.append(".");
                        sb5.append(this.ext);
                        final String string4 = sb5.toString();
                        string = string3;
                        string2 = string4;
                        if (this.key != null) {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append(md5);
                            sb6.append(".iv");
                            child = sb6.toString();
                            string = string3;
                            string2 = string4;
                            break Label_0472;
                        }
                    }
                    child = null;
                }
                final String s = null;
                child2 = null;
                child3 = string;
                child4 = s;
            }
            else {
                final TLRPC.InputFileLocation location = this.location;
                final long volume_id = location.volume_id;
                String s2 = null;
                String s3 = null;
                Label_1609: {
                    String s4 = null;
                    Label_1300: {
                        Label_1297: {
                            if (volume_id != 0L && location.local_id != 0) {
                                n = this.datacenterId;
                                if (n == Integer.MIN_VALUE || volume_id == -2147483648L || n == 0) {
                                    this.onFail(true, 0);
                                    return false;
                                }
                                if (!this.encryptFile) {
                                    final StringBuilder sb7 = new StringBuilder();
                                    sb7.append(this.location.volume_id);
                                    sb7.append("_");
                                    sb7.append(this.location.local_id);
                                    sb7.append(".temp");
                                    child3 = sb7.toString();
                                    final StringBuilder sb8 = new StringBuilder();
                                    sb8.append(this.location.volume_id);
                                    sb8.append("_");
                                    sb8.append(this.location.local_id);
                                    sb8.append(".");
                                    sb8.append(this.ext);
                                    s2 = sb8.toString();
                                    if (this.key != null) {
                                        final StringBuilder sb9 = new StringBuilder();
                                        sb9.append(this.location.volume_id);
                                        sb9.append("_");
                                        sb9.append(this.location.local_id);
                                        sb9.append(".iv");
                                        s3 = sb9.toString();
                                    }
                                    else {
                                        s3 = null;
                                    }
                                    if (this.notLoadedBytesRanges != null) {
                                        final StringBuilder sb10 = new StringBuilder();
                                        sb10.append(this.location.volume_id);
                                        sb10.append("_");
                                        sb10.append(this.location.local_id);
                                        sb10.append(".pt");
                                        child2 = sb10.toString();
                                    }
                                    else {
                                        child2 = null;
                                    }
                                    final StringBuilder sb11 = new StringBuilder();
                                    sb11.append(this.location.volume_id);
                                    sb11.append("_");
                                    sb11.append(this.location.local_id);
                                    sb11.append(".preload");
                                    child4 = sb11.toString();
                                    break Label_1609;
                                }
                                final StringBuilder sb12 = new StringBuilder();
                                sb12.append(this.location.volume_id);
                                sb12.append("_");
                                sb12.append(this.location.local_id);
                                sb12.append(".temp.enc");
                                final String string5 = sb12.toString();
                                final StringBuilder sb13 = new StringBuilder();
                                sb13.append(this.location.volume_id);
                                sb13.append("_");
                                sb13.append(this.location.local_id);
                                sb13.append(".");
                                sb13.append(this.ext);
                                sb13.append(".enc");
                                final String string6 = sb13.toString();
                                s4 = string5;
                                string2 = string6;
                                if (this.key == null) {
                                    break Label_1297;
                                }
                                final StringBuilder sb14 = new StringBuilder();
                                sb14.append(this.location.volume_id);
                                sb14.append("_");
                                sb14.append(this.location.local_id);
                                sb14.append(".iv.enc");
                                final String string7 = sb14.toString();
                                s4 = string5;
                                string2 = string6;
                                child = string7;
                            }
                            else {
                                if (this.datacenterId == 0 || this.location.id == 0L) {
                                    this.onFail(true, 0);
                                    return false;
                                }
                                if (!this.encryptFile) {
                                    final StringBuilder sb15 = new StringBuilder();
                                    sb15.append(this.datacenterId);
                                    sb15.append("_");
                                    sb15.append(this.location.id);
                                    sb15.append(".temp");
                                    child3 = sb15.toString();
                                    final StringBuilder sb16 = new StringBuilder();
                                    sb16.append(this.datacenterId);
                                    sb16.append("_");
                                    sb16.append(this.location.id);
                                    sb16.append(this.ext);
                                    s2 = sb16.toString();
                                    if (this.key != null) {
                                        final StringBuilder sb17 = new StringBuilder();
                                        sb17.append(this.datacenterId);
                                        sb17.append("_");
                                        sb17.append(this.location.id);
                                        sb17.append(".iv");
                                        s3 = sb17.toString();
                                    }
                                    else {
                                        s3 = null;
                                    }
                                    if (this.notLoadedBytesRanges != null) {
                                        final StringBuilder sb18 = new StringBuilder();
                                        sb18.append(this.datacenterId);
                                        sb18.append("_");
                                        sb18.append(this.location.id);
                                        sb18.append(".pt");
                                        child2 = sb18.toString();
                                    }
                                    else {
                                        child2 = null;
                                    }
                                    final StringBuilder sb19 = new StringBuilder();
                                    sb19.append(this.datacenterId);
                                    sb19.append("_");
                                    sb19.append(this.location.id);
                                    sb19.append(".preload");
                                    child4 = sb19.toString();
                                    break Label_1609;
                                }
                                final StringBuilder sb20 = new StringBuilder();
                                sb20.append(this.datacenterId);
                                sb20.append("_");
                                sb20.append(this.location.id);
                                sb20.append(".temp.enc");
                                final String string8 = sb20.toString();
                                final StringBuilder sb21 = new StringBuilder();
                                sb21.append(this.datacenterId);
                                sb21.append("_");
                                sb21.append(this.location.id);
                                sb21.append(this.ext);
                                sb21.append(".enc");
                                final String string9 = sb21.toString();
                                s4 = string8;
                                string2 = string9;
                                if (this.key == null) {
                                    break Label_1297;
                                }
                                final StringBuilder sb22 = new StringBuilder();
                                sb22.append(this.datacenterId);
                                sb22.append("_");
                                sb22.append(this.location.id);
                                sb22.append(".iv.enc");
                                child = sb22.toString();
                                string2 = string9;
                                s4 = string8;
                            }
                            break Label_1300;
                        }
                        child = null;
                    }
                    final String s5 = null;
                    child2 = null;
                    child3 = s4;
                    child4 = s5;
                    break Label_1619;
                }
                final String s6 = s3;
                string2 = s2;
                child = s6;
            }
        }
        this.requestInfos = new ArrayList<RequestInfo>(this.currentMaxDownloadRequests);
        this.delayedRequestInfos = new ArrayList<RequestInfo>(this.currentMaxDownloadRequests - 1);
        this.state = 1;
        this.cacheFileFinal = new File(this.storePath, string2);
        boolean exists;
        final boolean b2 = exists = this.cacheFileFinal.exists();
        if (b2) {
            n = this.totalBytesCount;
            exists = b2;
            if (n != 0) {
                exists = b2;
                if (n != this.cacheFileFinal.length()) {
                    this.cacheFileFinal.delete();
                    exists = false;
                }
            }
        }
        if (!exists) {
            this.cacheFileTemp = new File(this.tempPath, child3);
            if (this.encryptFile) {
                final File internalCacheDir = FileLoader.getInternalCacheDir();
                final StringBuilder sb23 = new StringBuilder();
                sb23.append(string2);
                sb23.append(".key");
                final File file = new File(internalCacheDir, sb23.toString());
                try {
                    ex = (Exception)new RandomAccessFile(file, "rws");
                    final long length = file.length();
                    this.encryptKey = new byte[32];
                    this.encryptIv = new byte[16];
                    if (length > 0L && length % 48L == 0L) {
                        ((RandomAccessFile)ex).read(this.encryptKey, 0, 32);
                        ((RandomAccessFile)ex).read(this.encryptIv, 0, 16);
                        n = 0;
                    }
                    else {
                        Utilities.random.nextBytes(this.encryptKey);
                        Utilities.random.nextBytes(this.encryptIv);
                        ((RandomAccessFile)ex).write(this.encryptKey);
                        ((RandomAccessFile)ex).write(this.encryptIv);
                        n = 1;
                    }
                    Label_1944: {
                        try {
                            ((RandomAccessFile)ex).getChannel().close();
                            break Label_1944;
                        }
                        catch (Exception ex3) {
                            final Exception ex2 = ex3;
                            FileLog.e(ex2);
                        }
                        try {
                            final Exception ex3;
                            final Exception ex2 = ex3;
                            FileLog.e(ex2);
                            ((RandomAccessFile)ex).close();
                        }
                        catch (Exception ex4) {}
                    }
                }
                catch (Exception ex4) {
                    n = 0;
                }
                final Exception ex4;
                FileLog.e(ex4);
            }
            else {
                n = 0;
            }
            ex = (Exception)(Object)new boolean[] { false };
            if (this.supportsPreloading && child4 != null) {
                this.cacheFilePreload = new File(this.tempPath, child4);
                Object o = ex;
                try {
                    Object preloadStream = new(java.io.RandomAccessFile.class);
                    o = ex;
                    new RandomAccessFile(this.cacheFilePreload, "rws");
                    o = ex;
                    this.preloadStream = (RandomAccessFile)preloadStream;
                    o = ex;
                    final long length2 = this.preloadStream.length();
                    o = ex;
                    this.preloadStreamFileOffset = 1;
                    o = ex;
                    Label_2536: {
                        if (length2 - 0 <= 1L) {
                            break Label_2536;
                        }
                        o = ex;
                        ex[0] = (this.preloadStream.readByte() != 0);
                        int n2 = 1;
                        while (true) {
                            final long n3 = n2;
                            o = ex;
                            if (n3 >= length2) {
                                break Label_2536;
                            }
                            if (length2 - n3 < 4L) {
                                o = ex;
                                break Label_2536;
                            }
                            o = ex;
                            final int int1 = this.preloadStream.readInt();
                            n2 += 4;
                            o = ex;
                            if (length2 - n2 < 4L) {
                                break Label_2536;
                            }
                            o = ex;
                            if (int1 < 0) {
                                break Label_2536;
                            }
                            o = ex;
                            if (int1 > this.totalBytesCount) {
                                o = ex;
                                break Label_2536;
                            }
                            o = ex;
                            final int int2 = this.preloadStream.readInt();
                            n2 += 4;
                            o = ex;
                            if (length2 - n2 < int2) {
                                break Label_2536;
                            }
                            preloadStream = ex;
                            try {
                                if (int2 > this.currentDownloadChunkSize) {
                                    o = ex;
                                }
                                else {
                                    preloadStream = ex;
                                    o = new(org.telegram.messenger.FileLoadOperation.PreloadRange.class);
                                    preloadStream = ex;
                                    new PreloadRange(n2, int1, int2);
                                    final int n4 = n2 + int2;
                                    preloadStream = ex;
                                    final RandomAccessFile preloadStream2 = this.preloadStream;
                                    final long pos = n4;
                                    preloadStream = ex;
                                    preloadStream2.seek(pos);
                                    if (length2 - pos >= 12L) {
                                        preloadStream = ex;
                                        this.foundMoovSize = this.preloadStream.readInt();
                                        preloadStream = ex;
                                        if (this.foundMoovSize != 0) {
                                            preloadStream = ex;
                                            int moovFound;
                                            if (this.nextPreloadDownloadOffset > this.totalBytesCount / 2) {
                                                moovFound = 2;
                                            }
                                            else {
                                                moovFound = 1;
                                            }
                                            preloadStream = ex;
                                            this.moovFound = moovFound;
                                            preloadStream = ex;
                                            this.preloadNotRequestedBytesCount = this.foundMoovSize;
                                        }
                                        preloadStream = ex;
                                        this.nextPreloadDownloadOffset = this.preloadStream.readInt();
                                        preloadStream = ex;
                                        this.nextAtomOffset = this.preloadStream.readInt();
                                        n2 = n4 + 12;
                                        preloadStream = ex;
                                        if (this.preloadedBytesRanges == null) {
                                            preloadStream = ex;
                                            preloadStream = ex;
                                            final SparseArray preloadedBytesRanges = new SparseArray();
                                            preloadStream = ex;
                                            this.preloadedBytesRanges = (SparseArray<PreloadRange>)preloadedBytesRanges;
                                        }
                                        preloadStream = ex;
                                        if (this.requestedPreloadedBytesRanges == null) {
                                            preloadStream = ex;
                                            preloadStream = ex;
                                            final SparseIntArray requestedPreloadedBytesRanges = new SparseIntArray();
                                            preloadStream = ex;
                                            this.requestedPreloadedBytesRanges = requestedPreloadedBytesRanges;
                                        }
                                        preloadStream = ex;
                                        this.preloadedBytesRanges.put(int1, o);
                                        preloadStream = ex;
                                        this.requestedPreloadedBytesRanges.put(int1, 1);
                                        preloadStream = ex;
                                        this.totalPreloadedBytes += int2;
                                        preloadStream = ex;
                                        this.preloadStreamFileOffset += int2 + 20;
                                        continue;
                                    }
                                    o = ex;
                                }
                                preloadStream = o;
                                this.preloadStream.seek(this.preloadStreamFileOffset);
                            }
                            catch (Exception ex) {
                                o = preloadStream;
                            }
                            break;
                        }
                    }
                }
                catch (Exception ex10) {}
                FileLog.e(ex);
                ex = (Exception)o;
                if (!this.isPreloadVideoOperation) {
                    ex = (Exception)o;
                    if (this.preloadedBytesRanges == null) {
                        this.cacheFilePreload = null;
                        try {
                            final RandomAccessFile preloadStream3 = this.preloadStream;
                            ex = (Exception)o;
                            if (preloadStream3 != null) {
                                try {
                                    this.preloadStream.getChannel().close();
                                }
                                catch (Exception ex5) {
                                    FileLog.e(ex5);
                                }
                                this.preloadStream.close();
                                this.preloadStream = null;
                                ex = (Exception)o;
                            }
                        }
                        catch (Exception ex6) {
                            FileLog.e(ex6);
                            ex = (Exception)o;
                        }
                    }
                }
            }
            if (child2 != null) {
                this.cacheFileParts = new File(this.tempPath, child2);
                try {
                    this.filePartsStream = new RandomAccessFile(this.cacheFileParts, "rws");
                    final long length3 = this.filePartsStream.length();
                    if (length3 % 8L == 4L) {
                        final int int3 = this.filePartsStream.readInt();
                        if (int3 <= (length3 - 4L) / 2L) {
                            for (int i = 0; i < int3; ++i) {
                                final int int4 = this.filePartsStream.readInt();
                                final int int5 = this.filePartsStream.readInt();
                                this.notLoadedBytesRanges.add(new Range(int4, int5));
                                this.notRequestedBytesRanges.add(new Range(int4, int5));
                            }
                        }
                    }
                }
                catch (Exception ex7) {
                    FileLog.e(ex7);
                }
            }
            if (this.cacheFileTemp.exists()) {
                if (n != 0) {
                    this.cacheFileTemp.delete();
                }
                else {
                    final long length4 = this.cacheFileTemp.length();
                    if (child != null && length4 % this.currentDownloadChunkSize != 0L) {
                        this.downloadedBytes = 0;
                        this.requestedBytesCount = 0;
                    }
                    else {
                        final int n5 = (int)this.cacheFileTemp.length();
                        final int currentDownloadChunkSize3 = this.currentDownloadChunkSize;
                        final int n6 = n5 / currentDownloadChunkSize3 * currentDownloadChunkSize3;
                        this.downloadedBytes = n6;
                        this.requestedBytesCount = n6;
                    }
                    final ArrayList<Range> notLoadedBytesRanges = this.notLoadedBytesRanges;
                    if (notLoadedBytesRanges != null && notLoadedBytesRanges.isEmpty()) {
                        this.notLoadedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                        this.notRequestedBytesRanges.add(new Range(this.downloadedBytes, this.totalBytesCount));
                    }
                }
            }
            else {
                final ArrayList<Range> notLoadedBytesRanges2 = this.notLoadedBytesRanges;
                if (notLoadedBytesRanges2 != null && notLoadedBytesRanges2.isEmpty()) {
                    this.notLoadedBytesRanges.add(new Range(0, this.totalBytesCount));
                    this.notRequestedBytesRanges.add(new Range(0, this.totalBytesCount));
                }
            }
            final ArrayList<Range> notLoadedBytesRanges3 = this.notLoadedBytesRanges;
            if (notLoadedBytesRanges3 != null) {
                this.downloadedBytes = this.totalBytesCount;
                for (int size = notLoadedBytesRanges3.size(), j = 0; j < size; ++j) {
                    final Range range = this.notLoadedBytesRanges.get(j);
                    this.downloadedBytes -= range.end - range.start;
                }
                this.requestedBytesCount = this.downloadedBytes;
            }
            if (BuildVars.LOGS_ENABLED) {
                if (this.isPreloadVideoOperation) {
                    final StringBuilder sb24 = new StringBuilder();
                    sb24.append("start preloading file to temp = ");
                    sb24.append(this.cacheFileTemp);
                    FileLog.d(sb24.toString());
                }
                else {
                    final StringBuilder sb25 = new StringBuilder();
                    sb25.append("start loading file to temp = ");
                    sb25.append(this.cacheFileTemp);
                    sb25.append(" final = ");
                    sb25.append(this.cacheFileFinal);
                    FileLog.d(sb25.toString());
                }
            }
            if (child != null) {
                this.cacheIvTemp = new File(this.tempPath, child);
                try {
                    this.fiv = new RandomAccessFile(this.cacheIvTemp, "rws");
                    if (this.downloadedBytes != 0 && n == 0) {
                        final long length5 = this.cacheIvTemp.length();
                        if (length5 > 0L && length5 % 32L == 0L) {
                            this.fiv.read(this.iv, 0, 32);
                        }
                        else {
                            this.downloadedBytes = 0;
                            this.requestedBytesCount = 0;
                        }
                    }
                }
                catch (Exception ex8) {
                    FileLog.e(ex8);
                    this.downloadedBytes = 0;
                    this.requestedBytesCount = 0;
                }
            }
            if (!this.isPreloadVideoOperation && this.downloadedBytes != 0 && this.totalBytesCount > 0) {
                this.copyNotLoadedRanges();
                this.delegate.didChangedLoadProgress(this, Math.min(1.0f, this.downloadedBytes / (float)this.totalBytesCount));
            }
            try {
                this.fileOutputStream = new RandomAccessFile(this.cacheFileTemp, "rws");
                if (this.downloadedBytes != 0) {
                    this.fileOutputStream.seek(this.downloadedBytes);
                }
            }
            catch (Exception ex9) {
                FileLog.e(ex9);
            }
            if (this.fileOutputStream == null) {
                this.onFail(true, 0);
                return false;
            }
            this.started = true;
            Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$zWcnXiI1arbfnF__ZxDNUNllncs(this, (boolean[])(Object)ex));
        }
        else {
            this.started = true;
            try {
                this.onFinishLoadingFile(false);
            }
            catch (Exception ex11) {
                this.onFail(true, 0);
            }
        }
        return true;
    }
    
    protected void startDownloadRequest() {
        if (!this.paused && this.state == 1 && (this.nextPartWasPreloaded || this.requestInfos.size() + this.delayedRequestInfos.size() < this.currentMaxDownloadRequests)) {
            if (this.isPreloadVideoOperation) {
                if (this.requestedBytesCount > 2097152) {
                    return;
                }
                if (this.moovFound != 0 && this.requestInfos.size() > 0) {
                    return;
                }
            }
            int max;
            if (!this.nextPartWasPreloaded && (!this.isPreloadVideoOperation || this.moovFound != 0) && this.totalBytesCount > 0) {
                max = Math.max(0, this.currentMaxDownloadRequests - this.requestInfos.size());
            }
            else {
                max = 1;
            }
            for (int i = 0; i < max; ++i) {
                int n;
                if (this.isPreloadVideoOperation) {
                    if (this.moovFound != 0 && this.preloadNotRequestedBytesCount <= 0) {
                        return;
                    }
                    if ((n = this.nextPreloadDownloadOffset) == -1) {
                        int n2 = 2097152 / this.currentDownloadChunkSize + 2;
                        n = 0;
                        int n4 = 0;
                        Label_0290: {
                            int n3;
                            while (true) {
                                n3 = n;
                                if (n2 == 0) {
                                    break;
                                }
                                if (this.requestedPreloadedBytesRanges.get(n, 0) == 0) {
                                    n4 = 1;
                                    break Label_0290;
                                }
                                final int currentDownloadChunkSize = this.currentDownloadChunkSize;
                                n3 = n + currentDownloadChunkSize;
                                final int totalBytesCount = this.totalBytesCount;
                                if (n3 > totalBytesCount) {
                                    break;
                                }
                                n = n3;
                                if (this.moovFound == 2 && (n = n3) == currentDownloadChunkSize * 8) {
                                    n = (totalBytesCount - 1048576) / currentDownloadChunkSize * currentDownloadChunkSize;
                                }
                                --n2;
                            }
                            final int n5 = 0;
                            n = n3;
                            n4 = n5;
                        }
                        if (n4 == 0 && this.requestInfos.isEmpty()) {
                            this.onFinishLoadingFile(false);
                        }
                    }
                    if (this.requestedPreloadedBytesRanges == null) {
                        this.requestedPreloadedBytesRanges = new SparseIntArray();
                    }
                    this.requestedPreloadedBytesRanges.put(n, 1);
                    if (BuildVars.DEBUG_VERSION) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("start next preload from ");
                        sb.append(n);
                        sb.append(" size ");
                        sb.append(this.totalBytesCount);
                        sb.append(" for ");
                        sb.append(this.cacheFilePreload);
                        FileLog.d(sb.toString());
                    }
                    this.preloadNotRequestedBytesCount -= this.currentDownloadChunkSize;
                }
                else {
                    final ArrayList<Range> notRequestedBytesRanges = this.notRequestedBytesRanges;
                    if (notRequestedBytesRanges != null) {
                        final int size = notRequestedBytesRanges.size();
                        int index = 0;
                        int n6 = Integer.MAX_VALUE;
                        int min = Integer.MAX_VALUE;
                        int streamStartOffset;
                        int n7;
                        while (true) {
                            streamStartOffset = n6;
                            n7 = min;
                            if (index >= size) {
                                break;
                            }
                            final Range range = this.notRequestedBytesRanges.get(index);
                            int access$100 = n6;
                            if (this.streamStartOffset != 0) {
                                if (range.start <= this.streamStartOffset) {
                                    final int access$101 = range.end;
                                    streamStartOffset = this.streamStartOffset;
                                    if (access$101 > streamStartOffset) {
                                        n7 = Integer.MAX_VALUE;
                                        break;
                                    }
                                }
                                access$100 = n6;
                                if (this.streamStartOffset < range.start && range.start < (access$100 = n6)) {
                                    access$100 = range.start;
                                }
                            }
                            min = Math.min(min, range.start);
                            ++index;
                            n6 = access$100;
                        }
                        if (streamStartOffset != Integer.MAX_VALUE) {
                            n = streamStartOffset;
                        }
                        else {
                            if (n7 == Integer.MAX_VALUE) {
                                break;
                            }
                            n = n7;
                        }
                    }
                    else {
                        n = this.requestedBytesCount;
                    }
                }
                if (!this.isPreloadVideoOperation) {
                    final ArrayList<Range> notRequestedBytesRanges2 = this.notRequestedBytesRanges;
                    if (notRequestedBytesRanges2 != null) {
                        this.addPart(notRequestedBytesRanges2, n, this.currentDownloadChunkSize + n, false);
                    }
                }
                final int totalBytesCount2 = this.totalBytesCount;
                if (totalBytesCount2 > 0 && n >= totalBytesCount2) {
                    break;
                }
                final int totalBytesCount3 = this.totalBytesCount;
                final boolean b = totalBytesCount3 <= 0 || i == max - 1 || (totalBytesCount3 > 0 && this.currentDownloadChunkSize + n >= totalBytesCount3);
                int n8;
                if (this.requestsCount % 2 == 0) {
                    n8 = 2;
                }
                else {
                    n8 = 65538;
                }
                int n9;
                if (this.isForceRequest) {
                    n9 = 32;
                }
                else {
                    n9 = 0;
                }
                int n10 = n9;
                if (!(this.webLocation instanceof TLRPC.TL_inputWebFileGeoPointLocation)) {
                    n10 = (n9 | 0x2);
                }
                TLObject tlObject;
                if (this.isCdn) {
                    tlObject = new TLRPC.TL_upload_getCdnFile();
                    ((TLRPC.TL_upload_getCdnFile)tlObject).file_token = this.cdnToken;
                    ((TLRPC.TL_upload_getCdnFile)tlObject).offset = n;
                    ((TLRPC.TL_upload_getCdnFile)tlObject).limit = this.currentDownloadChunkSize;
                    n10 |= 0x1;
                }
                else if (this.webLocation != null) {
                    tlObject = new TLRPC.TL_upload_getWebFile();
                    ((TLRPC.TL_upload_getWebFile)tlObject).location = this.webLocation;
                    ((TLRPC.TL_upload_getWebFile)tlObject).offset = n;
                    ((TLRPC.TL_upload_getWebFile)tlObject).limit = this.currentDownloadChunkSize;
                }
                else {
                    tlObject = new TLRPC.TL_upload_getFile();
                    ((TLRPC.TL_upload_getFile)tlObject).location = this.location;
                    ((TLRPC.TL_upload_getFile)tlObject).offset = n;
                    ((TLRPC.TL_upload_getFile)tlObject).limit = this.currentDownloadChunkSize;
                }
                this.requestedBytesCount += this.currentDownloadChunkSize;
                final RequestInfo e = new RequestInfo();
                this.requestInfos.add(e);
                e.offset = n;
                if (!this.isPreloadVideoOperation && this.supportsPreloading && this.preloadStream != null) {
                    final SparseArray<PreloadRange> preloadedBytesRanges = this.preloadedBytesRanges;
                    if (preloadedBytesRanges != null) {
                        final PreloadRange preloadRange = (PreloadRange)preloadedBytesRanges.get(e.offset);
                        if (preloadRange != null) {
                            e.response = new TLRPC.TL_upload_file();
                            try {
                                final NativeByteBuffer bytes = new NativeByteBuffer(preloadRange.length);
                                this.preloadStream.seek(preloadRange.fileOffset);
                                this.preloadStream.getChannel().read(bytes.buffer);
                                bytes.buffer.position(0);
                                e.response.bytes = bytes;
                                Utilities.stageQueue.postRunnable(new _$$Lambda$FileLoadOperation$CkXyQ6ScRYNY6TMd6qRPWNLnxAA(this, e));
                                continue;
                            }
                            catch (Exception ex) {}
                        }
                    }
                }
                final ConnectionsManager instance = ConnectionsManager.getInstance(this.currentAccount);
                final _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs $$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs = new _$$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs(this, e, tlObject);
                int n11;
                if (this.isCdn) {
                    n11 = this.cdnDatacenterId;
                }
                else {
                    n11 = this.datacenterId;
                }
                e.requestToken = instance.sendRequest(tlObject, $$Lambda$FileLoadOperation$pLwgbN71WnWYsTs7i_Ie2IK_Sjs, null, null, n10, n11, n8, b);
                ++this.requestsCount;
            }
        }
    }
    
    public boolean wasStarted() {
        return this.started && !this.paused;
    }
    
    public interface FileLoadOperationDelegate
    {
        void didChangedLoadProgress(final FileLoadOperation p0, final float p1);
        
        void didFailedLoadingFile(final FileLoadOperation p0, final int p1);
        
        void didFinishLoadingFile(final FileLoadOperation p0, final File p1);
    }
    
    private static class PreloadRange
    {
        private int fileOffset;
        private int length;
        private int start;
        
        private PreloadRange(final int fileOffset, final int start, final int length) {
            this.fileOffset = fileOffset;
            this.start = start;
            this.length = length;
        }
    }
    
    public static class Range
    {
        private int end;
        private int start;
        
        private Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
    }
    
    protected static class RequestInfo
    {
        private int offset;
        private int requestToken;
        private TLRPC.TL_upload_file response;
        private TLRPC.TL_upload_cdnFile responseCdn;
        private TLRPC.TL_upload_webFile responseWeb;
    }
}
