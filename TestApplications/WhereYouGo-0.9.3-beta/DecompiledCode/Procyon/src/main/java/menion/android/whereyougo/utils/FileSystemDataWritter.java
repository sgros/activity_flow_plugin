// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import java.io.FileOutputStream;
import java.io.File;

public class FileSystemDataWritter extends Thread
{
    private static final String TAG = "FileSystemDataWritter";
    private final long bytePos;
    private final byte[] dataToWrite;
    private final String fileToWrite;
    
    public FileSystemDataWritter(final String fileToWrite, final byte[] dataToWrite, final long bytePos) {
        this.fileToWrite = fileToWrite;
        this.dataToWrite = dataToWrite;
        this.bytePos = bytePos;
        this.start();
    }
    
    @Override
    public void run() {
        try {
            FileSystem.checkFolders(this.fileToWrite);
            final File file = new File(this.fileToWrite);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (this.dataToWrite != null) {
                FileOutputStream fileOutputStream;
                if (this.bytePos == -1L) {
                    fileOutputStream = new FileOutputStream(file, false);
                }
                else if (this.bytePos == -2L) {
                    fileOutputStream = new FileOutputStream(file, true);
                }
                else {
                    fileOutputStream = new FileOutputStream(file, true);
                    fileOutputStream.getChannel().position(this.bytePos);
                }
                fileOutputStream.write(this.dataToWrite);
                fileOutputStream.close();
            }
        }
        catch (Exception ex) {
            Logger.e("FileSystemDataWritter", "run(" + this.fileToWrite + ")", ex);
        }
    }
}
