// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.openwig;

import menion.android.whereyougo.utils.Logger;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import cz.matejcik.openwig.platform.FileHandle;

public class WSaveFile implements FileHandle
{
    private static final String TAG = "WSaveFile";
    private final File file;
    
    public WSaveFile(final File file) {
        this.file = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".ows");
    }
    
    @Override
    public void create() throws IOException {
        this.file.createNewFile();
    }
    
    @Override
    public void delete() throws IOException {
        this.file.delete();
    }
    
    @Override
    public boolean exists() throws IOException {
        return this.file.exists();
    }
    
    @Override
    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(new BufferedInputStream(new FileInputStream(this.file)));
    }
    
    @Override
    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
    }
    
    @Override
    public void truncate(final long n) throws IOException {
        Logger.d("WSaveFile", "truncate()");
    }
}
