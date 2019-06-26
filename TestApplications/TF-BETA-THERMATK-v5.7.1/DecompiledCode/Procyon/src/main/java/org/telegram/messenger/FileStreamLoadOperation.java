// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import android.net.Uri;
import java.io.RandomAccessFile;
import org.telegram.tgnet.TLRPC;
import java.util.concurrent.CountDownLatch;
import com.google.android.exoplayer2.upstream.BaseDataSource;

public class FileStreamLoadOperation extends BaseDataSource implements FileLoadOperationStream
{
    private long bytesRemaining;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    private int currentOffset;
    private TLRPC.Document document;
    private RandomAccessFile file;
    private FileLoadOperation loadOperation;
    private boolean opened;
    private Object parentObject;
    private Uri uri;
    
    public FileStreamLoadOperation() {
        super(false);
    }
    
    @Deprecated
    public FileStreamLoadOperation(final TransferListener transferListener) {
        this();
        if (transferListener != null) {
            this.addTransferListener(transferListener);
        }
    }
    
    @Override
    public void close() {
        final FileLoadOperation loadOperation = this.loadOperation;
        if (loadOperation != null) {
            loadOperation.removeStreamListener(this);
        }
        final CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
        final RandomAccessFile file = this.file;
        if (file != null) {
            try {
                file.close();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.file = null;
        }
        this.uri = null;
        if (this.opened) {
            this.opened = false;
            this.transferEnded();
        }
    }
    
    @Override
    public Uri getUri() {
        return this.uri;
    }
    
    @Override
    public void newDataAvailable() {
        final CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        this.uri = dataSpec.uri;
        this.currentAccount = Utilities.parseInt(this.uri.getQueryParameter("account"));
        this.parentObject = FileLoader.getInstance(this.currentAccount).getParentObject(Utilities.parseInt(this.uri.getQueryParameter("rid")));
        this.document = new TLRPC.TL_document();
        this.document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash"));
        this.document.id = Utilities.parseLong(this.uri.getQueryParameter("id"));
        this.document.size = Utilities.parseInt(this.uri.getQueryParameter("size"));
        this.document.dc_id = Utilities.parseInt(this.uri.getQueryParameter("dc"));
        this.document.mime_type = this.uri.getQueryParameter("mime");
        this.document.file_reference = Utilities.hexToBytes(this.uri.getQueryParameter("reference"));
        final TLRPC.TL_documentAttributeFilename e = new TLRPC.TL_documentAttributeFilename();
        e.file_name = this.uri.getQueryParameter("name");
        this.document.attributes.add(e);
        if (this.document.mime_type.startsWith("video")) {
            this.document.attributes.add(new TLRPC.TL_documentAttributeVideo());
        }
        else if (this.document.mime_type.startsWith("audio")) {
            this.document.attributes.add(new TLRPC.TL_documentAttributeAudio());
        }
        final FileLoader instance = FileLoader.getInstance(this.currentAccount);
        final TLRPC.Document document = this.document;
        final Object parentObject = this.parentObject;
        final int currentOffset = (int)dataSpec.position;
        this.currentOffset = currentOffset;
        this.loadOperation = instance.loadStreamFile(this, document, parentObject, currentOffset);
        long length;
        if ((length = dataSpec.length) == -1L) {
            length = this.document.size - dataSpec.position;
        }
        this.bytesRemaining = length;
        if (this.bytesRemaining >= 0L) {
            this.opened = true;
            this.transferStarted(dataSpec);
            final FileLoadOperation loadOperation = this.loadOperation;
            if (loadOperation != null) {
                (this.file = new RandomAccessFile(loadOperation.getCurrentFile(), "r")).seek(this.currentOffset);
            }
            return this.bytesRemaining;
        }
        throw new EOFException();
    }
    
    @Override
    public int read(final byte[] b, final int n, int n2) throws IOException {
        final int n3 = 0;
        if (n2 == 0) {
            return 0;
        }
        final long bytesRemaining = this.bytesRemaining;
        if (bytesRemaining == 0L) {
            return -1;
        }
        int downloadedLengthFromOffset = n3;
        int n4 = n2;
        if (bytesRemaining < n2) {
            n4 = (int)bytesRemaining;
            downloadedLengthFromOffset = n3;
        }
        while (true) {
            Label_0162: {
                if (downloadedLengthFromOffset != 0) {
                    break Label_0162;
                }
                try {
                    n2 = (downloadedLengthFromOffset = this.loadOperation.getDownloadedLengthFromOffset(this.currentOffset, n4));
                    if (n2 == 0) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("not found bytes ");
                        sb.append(n);
                        FileLog.d(sb.toString());
                        FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.parentObject, this.currentOffset);
                        (this.countDownLatch = new CountDownLatch(1)).await();
                        downloadedLengthFromOffset = n2;
                        continue;
                    }
                    continue;
                    this.file.readFully(b, n, downloadedLengthFromOffset);
                    this.currentOffset += downloadedLengthFromOffset;
                    this.bytesRemaining -= downloadedLengthFromOffset;
                    this.bytesTransferred(downloadedLengthFromOffset);
                    return downloadedLengthFromOffset;
                }
                catch (Exception cause) {
                    throw new IOException(cause);
                }
            }
        }
    }
}
