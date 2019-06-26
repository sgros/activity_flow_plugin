// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

public interface TransferListener
{
    void onBytesTransferred(final DataSource p0, final DataSpec p1, final boolean p2, final int p3);
    
    void onTransferEnd(final DataSource p0, final DataSpec p1, final boolean p2);
    
    void onTransferInitializing(final DataSource p0, final DataSpec p1, final boolean p2);
    
    void onTransferStart(final DataSource p0, final DataSpec p1, final boolean p2);
}
