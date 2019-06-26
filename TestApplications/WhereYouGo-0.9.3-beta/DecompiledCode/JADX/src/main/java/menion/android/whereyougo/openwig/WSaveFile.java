package menion.android.whereyougo.openwig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.platform.FileHandle;

public class WSaveFile implements FileHandle {
    private static final String TAG = "WSaveFile";
    private final File file;

    public WSaveFile(File cartridgeFile) {
        this.file = new File(cartridgeFile.getAbsolutePath().substring(0, cartridgeFile.getAbsolutePath().lastIndexOf(".")) + ".ows");
    }

    public void create() throws IOException {
        this.file.createNewFile();
    }

    public void delete() throws IOException {
        this.file.delete();
    }

    public boolean exists() throws IOException {
        return this.file.exists();
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(new BufferedInputStream(new FileInputStream(this.file)));
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
    }

    public void truncate(long len) throws IOException {
        Logger.m20d(TAG, "truncate()");
    }
}
