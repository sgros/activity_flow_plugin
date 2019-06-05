package p005cz.matejcik.openwig.platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* renamed from: cz.matejcik.openwig.platform.FileHandle */
public interface FileHandle {
    void create() throws IOException;

    void delete() throws IOException;

    boolean exists() throws IOException;

    DataInputStream openDataInputStream() throws IOException;

    DataOutputStream openDataOutputStream() throws IOException;

    void truncate(long j) throws IOException;
}
