package menion.android.whereyougo.utils;

public class FileSystemDataWritter extends Thread {
   private static final String TAG = "FileSystemDataWritter";
   private final long bytePos;
   private final byte[] dataToWrite;
   private final String fileToWrite;

   public FileSystemDataWritter(String var1, byte[] var2, long var3) {
      this.fileToWrite = var1;
      this.dataToWrite = var2;
      this.bytePos = var3;
      this.start();
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
