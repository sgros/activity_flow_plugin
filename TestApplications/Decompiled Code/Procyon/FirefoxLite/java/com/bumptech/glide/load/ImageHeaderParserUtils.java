// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load;

import java.io.IOException;
import java.util.Iterator;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.InputStream;
import java.util.List;

public final class ImageHeaderParserUtils
{
    public static int getOrientation(final List<ImageHeaderParser> list, final InputStream inputStream, final ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return -1;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(5242880);
        for (final ImageHeaderParser imageHeaderParser : list) {
            try {
                final int orientation = imageHeaderParser.getOrientation(inputStream2, arrayPool);
                if (orientation != -1) {
                    return orientation;
                }
                continue;
            }
            finally {
                inputStream2.reset();
            }
            break;
        }
        return -1;
    }
    
    public static ImageHeaderParser.ImageType getType(final List<ImageHeaderParser> list, final InputStream inputStream, final ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return ImageHeaderParser.ImageType.UNKNOWN;
        }
        InputStream inputStream2 = inputStream;
        if (!inputStream.markSupported()) {
            inputStream2 = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream2.mark(5242880);
        for (final ImageHeaderParser imageHeaderParser : list) {
            try {
                final ImageHeaderParser.ImageType type = imageHeaderParser.getType(inputStream2);
                if (type != ImageHeaderParser.ImageType.UNKNOWN) {
                    return type;
                }
                continue;
            }
            finally {
                inputStream2.reset();
            }
            break;
        }
        return ImageHeaderParser.ImageType.UNKNOWN;
    }
}
