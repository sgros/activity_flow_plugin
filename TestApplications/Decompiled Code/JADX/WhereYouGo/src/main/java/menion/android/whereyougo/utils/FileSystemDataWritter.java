package menion.android.whereyougo.utils;

import java.io.File;
import java.io.FileOutputStream;

public class FileSystemDataWritter extends Thread {
    private static final String TAG = "FileSystemDataWritter";
    private final long bytePos;
    private final byte[] dataToWrite;
    private final String fileToWrite;

    public FileSystemDataWritter(String fileToWrite, byte[] dataToWrite, long bytePos) {
        this.fileToWrite = fileToWrite;
        this.dataToWrite = dataToWrite;
        this.bytePos = bytePos;
        start();
    }

    public void run() {
        try {
            FileSystem.checkFolders(this.fileToWrite);
            File file = new File(this.fileToWrite);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (this.dataToWrite != null) {
                FileOutputStream os;
                if (this.bytePos == -1) {
                    os = new FileOutputStream(file, false);
                } else if (this.bytePos == -2) {
                    os = new FileOutputStream(file, true);
                } else {
                    os = new FileOutputStream(file, true);
                    os.getChannel().position(this.bytePos);
                }
                os.write(this.dataToWrite);
                os.close();
            }
        } catch (Exception e) {
            Logger.m22e(TAG, "run(" + this.fileToWrite + ")", e);
        }
    }
}
