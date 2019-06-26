// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;

public class SecureDocument extends TLObject
{
    public byte[] fileHash;
    public byte[] fileSecret;
    public TLRPC.TL_inputFile inputFile;
    public String path;
    public SecureDocumentKey secureDocumentKey;
    public TLRPC.TL_secureFile secureFile;
    public int type;
    
    public SecureDocument(final SecureDocumentKey secureDocumentKey, final TLRPC.TL_secureFile secureFile, final String path, final byte[] fileHash, final byte[] fileSecret) {
        this.secureDocumentKey = secureDocumentKey;
        this.secureFile = secureFile;
        this.path = path;
        this.fileHash = fileHash;
        this.fileSecret = fileSecret;
    }
}
