package com.coremedia.iso;

import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import java.io.IOException;

public class IsoFile extends BasicContainer implements Closeable {
    private static Logger LOG = Logger.getLogger(IsoFile.class);

    public static byte[] fourCCtoBytes(String str) {
        byte[] bArr = new byte[4];
        if (str != null) {
            for (int i = 0; i < Math.min(4, str.length()); i++) {
                bArr[i] = (byte) str.charAt(i);
            }
        }
        return bArr;
    }

    public void close() throws IOException {
        this.dataSource.close();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("model(");
        stringBuilder.append(this.dataSource.toString());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
