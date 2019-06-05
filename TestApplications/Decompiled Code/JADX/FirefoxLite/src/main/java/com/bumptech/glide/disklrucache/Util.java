package com.bumptech.glide.disklrucache;

import com.adjust.sdk.Constants;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

final class Util {
    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName(Constants.ENCODING);

    static void deleteContents(File file) throws IOException {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            int length = listFiles.length;
            int i = 0;
            while (i < length) {
                File file2 = listFiles[i];
                if (file2.isDirectory()) {
                    deleteContents(file2);
                }
                if (file2.delete()) {
                    i++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("failed to delete file: ");
                    stringBuilder.append(file2);
                    throw new IOException(stringBuilder.toString());
                }
            }
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("not a readable directory: ");
        stringBuilder2.append(file);
        throw new IOException(stringBuilder2.toString());
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }
}
