// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Context;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.WriteToSocketDelegate;
import org.telegram.tgnet.QuickAckDelegate;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import java.security.MessageDigest;
import org.telegram.tgnet.NativeByteBuffer;
import android.net.Uri;
import java.io.File;
import android.content.SharedPreferences$Editor;
import java.io.RandomAccessFile;
import android.util.SparseIntArray;
import android.content.SharedPreferences;
import java.util.ArrayList;
import android.util.SparseArray;

public class FileUploadOperation
{
    private static final int initialRequestsCount = 8;
    private static final int initialRequestsSlowNetworkCount = 1;
    private static final int maxUploadingKBytes = 2048;
    private static final int maxUploadingSlowNetworkKBytes = 32;
    private static final int minUploadChunkSize = 128;
    private static final int minUploadChunkSlowNetworkSize = 32;
    private long availableSize;
    private SparseArray<UploadCachedResult> cachedResults;
    private int currentAccount;
    private long currentFileId;
    private int currentPartNum;
    private int currentType;
    private int currentUploadRequetsCount;
    private FileUploadOperationDelegate delegate;
    private int estimatedSize;
    private String fileKey;
    private int fingerprint;
    private ArrayList<byte[]> freeRequestIvs;
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
    private SparseIntArray requestTokens;
    private int saveInfoTimes;
    private boolean slowNetwork;
    private boolean started;
    private int state;
    private RandomAccessFile stream;
    private long totalFileSize;
    private int totalPartsCount;
    private int uploadChunkSize;
    private boolean uploadFirstPartLater;
    private int uploadStartTime;
    private long uploadedBytesCount;
    private String uploadingFilePath;
    
    public FileUploadOperation(final int currentAccount, final String uploadingFilePath, final boolean isEncrypted, final int estimatedSize, final int currentType) {
        this.uploadChunkSize = 65536;
        this.requestTokens = new SparseIntArray();
        this.cachedResults = (SparseArray<UploadCachedResult>)new SparseArray();
        this.currentAccount = currentAccount;
        this.uploadingFilePath = uploadingFilePath;
        this.isEncrypted = isEncrypted;
        this.estimatedSize = estimatedSize;
        this.currentType = currentType;
        this.uploadFirstPartLater = (estimatedSize != 0 && !this.isEncrypted);
    }
    
    private void calcTotalPartsCount() {
        if (this.uploadFirstPartLater) {
            if (this.isBigFile) {
                final long totalFileSize = this.totalFileSize;
                final int uploadChunkSize = this.uploadChunkSize;
                this.totalPartsCount = (int)(totalFileSize - uploadChunkSize + uploadChunkSize - 1L) / uploadChunkSize + 1;
            }
            else {
                final long totalFileSize2 = this.totalFileSize;
                final int uploadChunkSize2 = this.uploadChunkSize;
                this.totalPartsCount = (int)(totalFileSize2 - 1024L + uploadChunkSize2 - 1L) / uploadChunkSize2 + 1;
            }
        }
        else {
            final long totalFileSize3 = this.totalFileSize;
            final int uploadChunkSize3 = this.uploadChunkSize;
            this.totalPartsCount = (int)(totalFileSize3 + uploadChunkSize3 - 1L) / uploadChunkSize3;
        }
    }
    
    private void cleanup() {
        if (this.preferences == null) {
            this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("uploadinfo", 0);
        }
        final SharedPreferences$Editor edit = this.preferences.edit();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.fileKey);
        sb.append("_time");
        final SharedPreferences$Editor remove = edit.remove(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.fileKey);
        sb2.append("_size");
        final SharedPreferences$Editor remove2 = remove.remove(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.fileKey);
        sb3.append("_uploaded");
        final SharedPreferences$Editor remove3 = remove2.remove(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.fileKey);
        sb4.append("_id");
        final SharedPreferences$Editor remove4 = remove3.remove(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(this.fileKey);
        sb5.append("_iv");
        final SharedPreferences$Editor remove5 = remove4.remove(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(this.fileKey);
        sb6.append("_key");
        final SharedPreferences$Editor remove6 = remove5.remove(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(this.fileKey);
        sb7.append("_ivc");
        remove6.remove(sb7.toString()).commit();
        try {
            if (this.stream != null) {
                this.stream.close();
                this.stream = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void startUploadRequest() {
        if (this.state != 1) {
            return;
        }
        try {
            this.started = true;
            if (this.stream == null) {
                final File file = new File(this.uploadingFilePath);
                if (AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                    throw new Exception("trying to upload internal file");
                }
                this.stream = new RandomAccessFile(file, "r");
                if (this.estimatedSize != 0) {
                    this.totalFileSize = this.estimatedSize;
                }
                else {
                    this.totalFileSize = file.length();
                }
                if (this.totalFileSize > 10485760L) {
                    this.isBigFile = true;
                }
                long a;
                if (this.slowNetwork) {
                    a = 32L;
                }
                else {
                    a = 128L;
                }
                this.uploadChunkSize = (int)Math.max(a, (this.totalFileSize + 3072000L - 1L) / 3072000L);
                if (1024 % this.uploadChunkSize != 0) {
                    int uploadChunkSize;
                    for (uploadChunkSize = 64; this.uploadChunkSize > uploadChunkSize; uploadChunkSize *= 2) {}
                    this.uploadChunkSize = uploadChunkSize;
                }
                int n;
                if (this.slowNetwork) {
                    n = 32;
                }
                else {
                    n = 2048;
                }
                this.maxRequestsCount = Math.max(1, n / this.uploadChunkSize);
                if (this.isEncrypted) {
                    this.freeRequestIvs = new ArrayList<byte[]>(this.maxRequestsCount);
                    for (int i = 0; i < this.maxRequestsCount; ++i) {
                        this.freeRequestIvs.add(new byte[32]);
                    }
                }
                this.uploadChunkSize *= 1024;
                this.calcTotalPartsCount();
                this.readBuffer = new byte[this.uploadChunkSize];
                final StringBuilder sb = new StringBuilder();
                sb.append(this.uploadingFilePath);
                String str;
                if (this.isEncrypted) {
                    str = "enc";
                }
                else {
                    str = "";
                }
                sb.append(str);
                this.fileKey = Utilities.MD5(sb.toString());
                final SharedPreferences preferences = this.preferences;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.fileKey);
                sb2.append("_size");
                final long long1 = preferences.getLong(sb2.toString(), 0L);
                this.uploadStartTime = (int)(System.currentTimeMillis() / 1000L);
                int n2 = 0;
                Label_1177: {
                    if (!this.uploadFirstPartLater && !this.nextPartFirst && this.estimatedSize == 0 && long1 == this.totalFileSize) {
                        final SharedPreferences preferences2 = this.preferences;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.fileKey);
                        sb3.append("_id");
                        this.currentFileId = preferences2.getLong(sb3.toString(), 0L);
                        final SharedPreferences preferences3 = this.preferences;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(this.fileKey);
                        sb4.append("_time");
                        final int int1 = preferences3.getInt(sb4.toString(), 0);
                        final SharedPreferences preferences4 = this.preferences;
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append(this.fileKey);
                        sb5.append("_uploaded");
                        final long long2 = preferences4.getLong(sb5.toString(), 0L);
                        Label_0742: {
                            Label_0739: {
                                if (this.isEncrypted) {
                                    final SharedPreferences preferences5 = this.preferences;
                                    final StringBuilder sb6 = new StringBuilder();
                                    sb6.append(this.fileKey);
                                    sb6.append("_iv");
                                    final String string = preferences5.getString(sb6.toString(), (String)null);
                                    final SharedPreferences preferences6 = this.preferences;
                                    final StringBuilder sb7 = new StringBuilder();
                                    sb7.append(this.fileKey);
                                    sb7.append("_key");
                                    final String string2 = preferences6.getString(sb7.toString(), (String)null);
                                    if (string != null && string2 != null) {
                                        this.key = Utilities.hexToBytes(string2);
                                        this.iv = Utilities.hexToBytes(string);
                                        if (this.key != null && this.iv != null && this.key.length == 32 && this.iv.length == 32) {
                                            this.ivChange = new byte[32];
                                            System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                                            break Label_0739;
                                        }
                                    }
                                    n2 = 1;
                                    break Label_0742;
                                }
                            }
                            n2 = 0;
                        }
                        if (n2 == 0 && int1 != 0) {
                            int n3 = 0;
                            Label_0812: {
                                if (!this.isBigFile || int1 >= this.uploadStartTime - 86400) {
                                    n3 = int1;
                                    if (this.isBigFile) {
                                        break Label_0812;
                                    }
                                    n3 = int1;
                                    if (int1 >= this.uploadStartTime - 5400.0f) {
                                        break Label_0812;
                                    }
                                }
                                n3 = 0;
                            }
                            if (n3 == 0) {
                                break Label_1177;
                            }
                            if (long2 > 0L) {
                                this.readBytesCount = long2;
                                this.currentPartNum = (int)(long2 / this.uploadChunkSize);
                                if (!this.isBigFile) {
                                    int n4 = 0;
                                    while (true) {
                                        final long n5 = n4;
                                        final long readBytesCount = this.readBytesCount;
                                        final int uploadChunkSize2 = this.uploadChunkSize;
                                        final int n6 = n2 = n2;
                                        if (n5 >= readBytesCount / uploadChunkSize2) {
                                            break Label_1177;
                                        }
                                        final int read = this.stream.read(this.readBuffer);
                                        int n7;
                                        if (this.isEncrypted && read % 16 != 0) {
                                            n7 = 16 - read % 16 + 0;
                                        }
                                        else {
                                            n7 = 0;
                                        }
                                        final int n8 = read + n7;
                                        final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(n8);
                                        if (read != this.uploadChunkSize || this.totalPartsCount == this.currentPartNum + 1) {
                                            this.isLastPart = true;
                                        }
                                        nativeByteBuffer.writeBytes(this.readBuffer, 0, read);
                                        if (this.isEncrypted) {
                                            for (int j = 0; j < n7; ++j) {
                                                nativeByteBuffer.writeByte(0);
                                            }
                                            Utilities.aesIgeEncryption(nativeByteBuffer.buffer, this.key, this.ivChange, true, true, 0, n8);
                                        }
                                        nativeByteBuffer.reuse();
                                        ++n4;
                                        n2 = n6;
                                    }
                                }
                                else {
                                    final int n9 = n2;
                                    this.stream.seek(long2);
                                    n2 = n9;
                                    if (!this.isEncrypted) {
                                        break Label_1177;
                                    }
                                    final SharedPreferences preferences7 = this.preferences;
                                    final StringBuilder sb8 = new StringBuilder();
                                    sb8.append(this.fileKey);
                                    sb8.append("_ivc");
                                    final String string3 = preferences7.getString(sb8.toString(), (String)null);
                                    if (string3 != null) {
                                        this.ivChange = Utilities.hexToBytes(string3);
                                        if (this.ivChange != null) {
                                            n2 = n9;
                                            if (this.ivChange.length == 32) {
                                                break Label_1177;
                                            }
                                        }
                                        this.readBytesCount = 0L;
                                        this.currentPartNum = 0;
                                    }
                                    else {
                                        this.readBytesCount = 0L;
                                        this.currentPartNum = 0;
                                    }
                                }
                            }
                        }
                    }
                    n2 = 1;
                }
                if (n2 != 0) {
                    if (this.isEncrypted) {
                        this.iv = new byte[32];
                        this.key = new byte[32];
                        this.ivChange = new byte[32];
                        Utilities.random.nextBytes(this.iv);
                        Utilities.random.nextBytes(this.key);
                        System.arraycopy(this.iv, 0, this.ivChange, 0, 32);
                    }
                    this.currentFileId = Utilities.random.nextLong();
                    if (!this.nextPartFirst && !this.uploadFirstPartLater && this.estimatedSize == 0) {
                        this.storeFileUploadInfo();
                    }
                }
                if (this.isEncrypted) {
                    try {
                        final MessageDigest instance = MessageDigest.getInstance("MD5");
                        final byte[] input = new byte[64];
                        System.arraycopy(this.key, 0, input, 0, 32);
                        System.arraycopy(this.iv, 0, input, 32, 32);
                        final byte[] digest = instance.digest(input);
                        for (int k = 0; k < 4; ++k) {
                            this.fingerprint |= ((digest[k] ^ digest[k + 4]) & 0xFF) << k * 8;
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                this.uploadedBytesCount = this.readBytesCount;
                this.lastSavedPartNum = this.currentPartNum;
                if (this.uploadFirstPartLater) {
                    if (this.isBigFile) {
                        this.stream.seek(this.uploadChunkSize);
                        this.readBytesCount = this.uploadChunkSize;
                    }
                    else {
                        this.stream.seek(1024L);
                        this.readBytesCount = 1024L;
                    }
                    this.currentPartNum = 1;
                }
            }
            if (this.estimatedSize != 0 && this.readBytesCount + this.uploadChunkSize > this.availableSize) {
                return;
            }
            int n10;
            if (this.nextPartFirst) {
                this.stream.seek(0L);
                if (this.isBigFile) {
                    n10 = this.stream.read(this.readBuffer);
                }
                else {
                    n10 = this.stream.read(this.readBuffer, 0, 1024);
                }
                this.currentPartNum = 0;
            }
            else {
                n10 = this.stream.read(this.readBuffer);
            }
            if (n10 == -1) {
                return;
            }
            int n11;
            if (this.isEncrypted && n10 % 16 != 0) {
                n11 = 16 - n10 % 16 + 0;
            }
            else {
                n11 = 0;
            }
            final int n12 = n10 + n11;
            final NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer(n12);
            if (this.nextPartFirst || n10 != this.uploadChunkSize || (this.estimatedSize == 0 && this.totalPartsCount == this.currentPartNum + 1)) {
                if (this.uploadFirstPartLater) {
                    this.nextPartFirst = true;
                    this.uploadFirstPartLater = false;
                }
                else {
                    this.isLastPart = true;
                }
            }
            nativeByteBuffer2.writeBytes(this.readBuffer, 0, n10);
            byte[] array;
            if (this.isEncrypted) {
                for (int l = 0; l < n11; ++l) {
                    nativeByteBuffer2.writeByte(0);
                }
                Utilities.aesIgeEncryption(nativeByteBuffer2.buffer, this.key, this.ivChange, true, true, 0, n12);
                array = this.freeRequestIvs.get(0);
                System.arraycopy(this.ivChange, 0, array, 0, 32);
                this.freeRequestIvs.remove(0);
            }
            else {
                array = null;
            }
            TLObject tlObject;
            int n13;
            if (this.isBigFile) {
                tlObject = new TLRPC.TL_upload_saveBigFilePart();
                n13 = this.currentPartNum;
                ((TLRPC.TL_upload_saveBigFilePart)tlObject).file_part = n13;
                ((TLRPC.TL_upload_saveBigFilePart)tlObject).file_id = this.currentFileId;
                if (this.estimatedSize != 0) {
                    ((TLRPC.TL_upload_saveBigFilePart)tlObject).file_total_parts = -1;
                }
                else {
                    ((TLRPC.TL_upload_saveBigFilePart)tlObject).file_total_parts = this.totalPartsCount;
                }
                ((TLRPC.TL_upload_saveBigFilePart)tlObject).bytes = nativeByteBuffer2;
            }
            else {
                tlObject = new TLRPC.TL_upload_saveFilePart();
                n13 = this.currentPartNum;
                ((TLRPC.TL_upload_saveFilePart)tlObject).file_part = n13;
                ((TLRPC.TL_upload_saveFilePart)tlObject).file_id = this.currentFileId;
                ((TLRPC.TL_upload_saveFilePart)tlObject).bytes = nativeByteBuffer2;
            }
            if (this.isLastPart && this.nextPartFirst) {
                this.nextPartFirst = false;
                this.currentPartNum = this.totalPartsCount - 1;
                this.stream.seek(this.totalFileSize);
            }
            this.readBytesCount += n10;
            ++this.currentPartNum;
            ++this.currentUploadRequetsCount;
            final int n14 = this.requestNum++;
            final long n15 = n13 + n10;
            final int objectSize = tlObject.getObjectSize();
            final int operationGuid = this.operationGuid;
            int n16;
            if (this.slowNetwork) {
                n16 = 4;
            }
            else {
                n16 = (n14 % 4 << 16 | 0x4);
            }
            this.requestTokens.put(n14, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$FileUploadOperation$XMcVvcrqfWd56m49RmvLW8_t4Os(this, operationGuid, objectSize + 4, array, n14, n10, n13, n15, tlObject), null, new _$$Lambda$FileUploadOperation$H_o0ouVev_JFhE9lBzpUHg6WYPI(this), 0, Integer.MAX_VALUE, n16, true));
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            this.state = 4;
            this.delegate.didFailedUploadingFile(this);
            this.cleanup();
        }
    }
    
    private void storeFileUploadInfo() {
        final SharedPreferences$Editor edit = this.preferences.edit();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.fileKey);
        sb.append("_time");
        edit.putInt(sb.toString(), this.uploadStartTime);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.fileKey);
        sb2.append("_size");
        edit.putLong(sb2.toString(), this.totalFileSize);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.fileKey);
        sb3.append("_id");
        edit.putLong(sb3.toString(), this.currentFileId);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.fileKey);
        sb4.append("_uploaded");
        edit.remove(sb4.toString());
        if (this.isEncrypted) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(this.fileKey);
            sb5.append("_iv");
            edit.putString(sb5.toString(), Utilities.bytesToHex(this.iv));
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(this.fileKey);
            sb6.append("_ivc");
            edit.putString(sb6.toString(), Utilities.bytesToHex(this.ivChange));
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(this.fileKey);
            sb7.append("_key");
            edit.putString(sb7.toString(), Utilities.bytesToHex(this.key));
        }
        edit.commit();
    }
    
    public void cancel() {
        if (this.state == 3) {
            return;
        }
        this.state = 2;
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$1Av0AtRL3UNZuURFidi06RFneIU(this));
        this.delegate.didFailedUploadingFile(this);
        this.cleanup();
    }
    
    protected void checkNewDataAvailable(final long n, final long n2) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$gwhBTZdvm3NdxGlA_jj4tE_lywA(this, n2, n));
    }
    
    public long getTotalFileSize() {
        return this.totalFileSize;
    }
    
    protected void onNetworkChanged(final boolean b) {
        if (this.state != 1) {
            return;
        }
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$AsRwZThcGZTT5evyde1j2fU_PqE(this, b));
    }
    
    public void setDelegate(final FileUploadOperationDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void start() {
        if (this.state != 0) {
            return;
        }
        this.state = 1;
        Utilities.stageQueue.postRunnable(new _$$Lambda$FileUploadOperation$UJ53jdSVrDp9jmxTaqaNFaiq47E(this));
    }
    
    public interface FileUploadOperationDelegate
    {
        void didChangedUploadProgress(final FileUploadOperation p0, final float p1);
        
        void didFailedUploadingFile(final FileUploadOperation p0);
        
        void didFinishUploadingFile(final FileUploadOperation p0, final TLRPC.InputFile p1, final TLRPC.InputEncryptedFile p2, final byte[] p3, final byte[] p4);
    }
    
    private class UploadCachedResult
    {
        private long bytesOffset;
        private byte[] iv;
    }
}
