package menion.android.whereyougo.openwig;

import cz.matejcik.openwig.platform.FileHandle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import menion.android.whereyougo.utils.Logger;

public class WSaveFile implements FileHandle {
   private static final String TAG = "WSaveFile";
   private final File file;

   public WSaveFile(File var1) {
      this.file = new File(var1.getAbsolutePath().substring(0, var1.getAbsolutePath().lastIndexOf(".")) + ".ows");
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

   public void truncate(long var1) throws IOException {
      Logger.d("WSaveFile", "truncate()");
   }
}
