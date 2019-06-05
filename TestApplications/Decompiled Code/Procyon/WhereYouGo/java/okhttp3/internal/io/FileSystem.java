// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.io;

import okio.Source;
import java.io.IOException;
import java.io.FileNotFoundException;
import okio.Okio;
import okio.Sink;
import java.io.File;

public interface FileSystem
{
    public static final FileSystem SYSTEM = new FileSystem() {
        @Override
        public Sink appendingSink(File o) throws FileNotFoundException {
            try {
                o = Okio.appendingSink((File)o);
                return (Sink)o;
            }
            catch (FileNotFoundException ex) {
                ((File)o).getParentFile().mkdirs();
                o = Okio.appendingSink((File)o);
                return (Sink)o;
            }
        }
        
        @Override
        public void delete(final File obj) throws IOException {
            if (!obj.delete() && obj.exists()) {
                throw new IOException("failed to delete " + obj);
            }
        }
        
        @Override
        public void deleteContents(File file) throws IOException {
            final File[] listFiles = file.listFiles();
            if (listFiles == null) {
                throw new IOException("not a readable directory: " + file);
            }
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                file = listFiles[i];
                if (file.isDirectory()) {
                    this.deleteContents(file);
                }
                if (!file.delete()) {
                    throw new IOException("failed to delete " + file);
                }
            }
        }
        
        @Override
        public boolean exists(final File file) {
            return file.exists();
        }
        
        @Override
        public void rename(final File obj, final File file) throws IOException {
            this.delete(file);
            if (!obj.renameTo(file)) {
                throw new IOException("failed to rename " + obj + " to " + file);
            }
        }
        
        @Override
        public Sink sink(File o) throws FileNotFoundException {
            try {
                o = Okio.sink((File)o);
                return (Sink)o;
            }
            catch (FileNotFoundException ex) {
                ((File)o).getParentFile().mkdirs();
                o = Okio.sink((File)o);
                return (Sink)o;
            }
        }
        
        @Override
        public long size(final File file) {
            return file.length();
        }
        
        @Override
        public Source source(final File file) throws FileNotFoundException {
            return Okio.source(file);
        }
    };
    
    Sink appendingSink(final File p0) throws FileNotFoundException;
    
    void delete(final File p0) throws IOException;
    
    void deleteContents(final File p0) throws IOException;
    
    boolean exists(final File p0);
    
    void rename(final File p0, final File p1) throws IOException;
    
    Sink sink(final File p0) throws FileNotFoundException;
    
    long size(final File p0);
    
    Source source(final File p0) throws FileNotFoundException;
}
