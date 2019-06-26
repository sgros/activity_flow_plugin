// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

import java.io.File;

public class FileLoadOperation
{
    private long address;
    private FileLoadOperationDelegate delegate;
    private boolean isForceRequest;
    private boolean started;
    
    public FileLoadOperation(final int n, final long n2, final long n3, final long n4, final int n5, final byte[] array, final byte[] array2, final String s, final int n6, final int n7, final File file, final File file2, final FileLoadOperationDelegate delegate) {
        this.address = native_createLoadOpetation(n, n2, n3, n4, n5, array, array2, s, n6, n7, file.getAbsolutePath(), file2.getAbsolutePath(), delegate);
        this.delegate = delegate;
    }
    
    public static native void native_cancelLoadOperation(final long p0);
    
    public static native long native_createLoadOpetation(final int p0, final long p1, final long p2, final long p3, final int p4, final byte[] p5, final byte[] p6, final String p7, final int p8, final int p9, final String p10, final String p11, final Object p12);
    
    public static native void native_startLoadOperation(final long p0);
    
    public void cancel() {
        if (this.started) {
            final long address = this.address;
            if (address != 0L) {
                native_cancelLoadOperation(address);
            }
        }
    }
    
    public boolean isForceRequest() {
        return this.isForceRequest;
    }
    
    public void setForceRequest(final boolean isForceRequest) {
        this.isForceRequest = isForceRequest;
    }
    
    public void start() {
        if (this.started) {
            return;
        }
        final long address = this.address;
        if (address == 0L) {
            this.delegate.onFailed(0);
            return;
        }
        this.started = true;
        native_startLoadOperation(address);
    }
    
    public boolean wasStarted() {
        return this.started;
    }
}
