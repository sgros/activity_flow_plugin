// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import java.io.IOException;
import android.util.Log;
import com.bumptech.glide.util.ByteBufferUtil;
import com.bumptech.glide.load.Options;
import java.io.File;
import java.nio.ByteBuffer;
import com.bumptech.glide.load.Encoder;

public class ByteBufferEncoder implements Encoder<ByteBuffer>
{
    @Override
    public boolean encode(final ByteBuffer byteBuffer, final File file, final Options options) {
        boolean b;
        try {
            ByteBufferUtil.toFile(byteBuffer, file);
            b = true;
        }
        catch (IOException ex) {
            if (Log.isLoggable("ByteBufferEncoder", 3)) {
                Log.d("ByteBufferEncoder", "Failed to write data", (Throwable)ex);
            }
            b = false;
        }
        return b;
    }
}
