// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import java.util.concurrent.CountDownLatch;

public class AnimatedFileDrawableStream implements FileLoadOperationStream
{
    private volatile boolean canceled;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    private TLRPC.Document document;
    private int lastOffset;
    private FileLoadOperation loadOperation;
    private Object parentObject;
    private final Object sync;
    private boolean waitingForLoad;
    
    public AnimatedFileDrawableStream(final TLRPC.Document document, final Object parentObject, final int currentAccount) {
        this.sync = new Object();
        this.document = document;
        this.parentObject = parentObject;
        this.currentAccount = currentAccount;
        this.loadOperation = FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.parentObject, 0);
    }
    
    public void cancel() {
        this.cancel(true);
    }
    
    public void cancel(final boolean b) {
        synchronized (this.sync) {
            if (this.countDownLatch != null) {
                this.countDownLatch.countDown();
                if (b && !this.canceled) {
                    FileLoader.getInstance(this.currentAccount).removeLoadingVideo(this.document, false, true);
                }
            }
            this.canceled = true;
        }
    }
    
    public int getCurrentAccount() {
        return this.currentAccount;
    }
    
    public TLRPC.Document getDocument() {
        return this.document;
    }
    
    public Object getParentObject() {
        return this.document;
    }
    
    public boolean isWaitingForLoad() {
        return this.waitingForLoad;
    }
    
    @Override
    public void newDataAvailable() {
        final CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
    
    public int read(final int n, final int n2) {
        Object o = this.sync;
        synchronized (o) {
            if (this.canceled) {
                return 0;
            }
            // monitorexit(o)
            if (n2 == 0) {
                return 0;
            }
            int downloadedLengthFromOffset = 0;
        Label_0221_Outer:
            while (true) {
                while (true) {
                    if (downloadedLengthFromOffset == 0) {
                        int n3 = downloadedLengthFromOffset;
                        try {
                            final int n4 = downloadedLengthFromOffset = this.loadOperation.getDownloadedLengthFromOffset(n, n2);
                            if (n4 != 0) {
                                continue Label_0221_Outer;
                            }
                            n3 = n4;
                            Label_0109: {
                                if (!this.loadOperation.isPaused()) {
                                    n3 = n4;
                                    if (this.lastOffset == n) {
                                        break Label_0109;
                                    }
                                }
                                n3 = n4;
                                FileLoader.getInstance(this.currentAccount).loadStreamFile(this, this.document, this.parentObject, n);
                            }
                            n3 = n4;
                            o = this.sync;
                            n3 = n4;
                            // monitorenter(o)
                            try {
                                if (this.canceled) {
                                    return 0;
                                }
                                this.countDownLatch = new CountDownLatch(1);
                                // monitorexit(o)
                                n3 = n4;
                                FileLoader.getInstance(this.currentAccount).setLoadingVideo(this.document, false, true);
                                n3 = n4;
                                this.waitingForLoad = true;
                                n3 = n4;
                                this.countDownLatch.await();
                                n3 = n4;
                                this.waitingForLoad = false;
                                downloadedLengthFromOffset = n4;
                                continue Label_0221_Outer;
                            }
                            finally {
                                // monitorexit(o)
                                n3 = n4;
                            }
                            n3 = downloadedLengthFromOffset;
                            this.lastOffset = n + downloadedLengthFromOffset;
                        }
                        catch (Exception o) {
                            FileLog.e((Throwable)o);
                            downloadedLengthFromOffset = n3;
                        }
                        break;
                    }
                    continue;
                }
            }
            return downloadedLengthFromOffset;
        }
    }
    
    public void reset() {
        synchronized (this.sync) {
            this.canceled = false;
        }
    }
}
