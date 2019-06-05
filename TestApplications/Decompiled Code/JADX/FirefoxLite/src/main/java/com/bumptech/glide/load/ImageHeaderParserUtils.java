package com.bumptech.glide.load;

import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ImageHeaderParserUtils {
    public static ImageType getType(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return ImageType.UNKNOWN;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(5242880);
        for (ImageHeaderParser type : list) {
            try {
                ImageType type2 = type.getType(inputStream);
                if (type2 != ImageType.UNKNOWN) {
                    inputStream.reset();
                    return type2;
                }
                inputStream.reset();
            } catch (Throwable th) {
                inputStream.reset();
                throw th;
            }
        }
        return ImageType.UNKNOWN;
    }

    public static int getOrientation(List<ImageHeaderParser> list, InputStream inputStream, ArrayPool arrayPool) throws IOException {
        if (inputStream == null) {
            return -1;
        }
        if (!inputStream.markSupported()) {
            inputStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        }
        inputStream.mark(5242880);
        for (ImageHeaderParser orientation : list) {
            try {
                int orientation2 = orientation.getOrientation(inputStream, arrayPool);
                if (orientation2 != -1) {
                    inputStream.reset();
                    return orientation2;
                }
                inputStream.reset();
            } catch (Throwable th) {
                inputStream.reset();
                throw th;
            }
        }
        return -1;
    }
}
