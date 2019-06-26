// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

class FileLoader$1 implements FileUploadOperationDelegate
{
    final /* synthetic */ FileLoader this$0;
    final /* synthetic */ boolean val$encrypted;
    final /* synthetic */ String val$location;
    final /* synthetic */ boolean val$small;
    
    FileLoader$1(final FileLoader this$0, final boolean val$encrypted, final String val$location, final boolean val$small) {
        this.this$0 = this$0;
        this.val$encrypted = val$encrypted;
        this.val$location = val$location;
        this.val$small = val$small;
    }
    
    @Override
    public void didChangedUploadProgress(final FileUploadOperation fileUploadOperation, final float n) {
        if (this.this$0.delegate != null) {
            this.this$0.delegate.fileUploadProgressChanged(this.val$location, n, this.val$encrypted);
        }
    }
    
    @Override
    public void didFailedUploadingFile(final FileUploadOperation fileUploadOperation) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$1$4L6YnCrX_lUiw2AHHpo_e3FY75I(this, this.val$encrypted, this.val$location, this.val$small));
    }
    
    @Override
    public void didFinishUploadingFile(final FileUploadOperation fileUploadOperation, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] array, final byte[] array2) {
        FileLoader.fileLoaderQueue.postRunnable(new _$$Lambda$FileLoader$1$8qI8Hd84Wsy2NQjG71Z0jivjIew(this, this.val$encrypted, this.val$location, this.val$small, inputFile, inputEncryptedFile, array, array2, fileUploadOperation));
    }
}
