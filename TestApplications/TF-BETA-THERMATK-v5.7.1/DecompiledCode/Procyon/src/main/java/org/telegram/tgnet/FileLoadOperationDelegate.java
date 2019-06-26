// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

public interface FileLoadOperationDelegate
{
    void onFailed(final int p0);
    
    void onFinished(final String p0);
    
    void onProgressChanged(final float p0);
}
