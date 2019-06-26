// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

public final class AtomicFile
{
    private static final String TAG = "AtomicFile";
    private final File backupName;
    private final File baseName;
    
    public AtomicFile(final File baseName) {
        this.baseName = baseName;
        final StringBuilder sb = new StringBuilder();
        sb.append(baseName.getPath());
        sb.append(".bak");
        this.backupName = new File(sb.toString());
    }
    
    private void restoreBackup() {
        if (this.backupName.exists()) {
            this.baseName.delete();
            this.backupName.renameTo(this.baseName);
        }
    }
    
    public void delete() {
        this.baseName.delete();
        this.backupName.delete();
    }
    
    public void endWrite(final OutputStream outputStream) throws IOException {
        outputStream.close();
        this.backupName.delete();
    }
    
    public InputStream openRead() throws FileNotFoundException {
        this.restoreBackup();
        return new FileInputStream(this.baseName);
    }
    
    public OutputStream startWrite() throws IOException {
        if (this.baseName.exists()) {
            if (!this.backupName.exists()) {
                if (!this.baseName.renameTo(this.backupName)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Couldn't rename file ");
                    sb.append(this.baseName);
                    sb.append(" to backup file ");
                    sb.append(this.backupName);
                    Log.w("AtomicFile", sb.toString());
                }
            }
            else {
                this.baseName.delete();
            }
        }
        try {
            return new AtomicFileOutputStream(this.baseName);
        }
        catch (FileNotFoundException cause2) {
            final File parentFile = this.baseName.getParentFile();
            if (parentFile != null && parentFile.mkdirs()) {
                try {
                    return new AtomicFileOutputStream(this.baseName);
                }
                catch (FileNotFoundException cause) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Couldn't create ");
                    sb2.append(this.baseName);
                    throw new IOException(sb2.toString(), cause);
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Couldn't create directory ");
            sb3.append(this.baseName);
            throw new IOException(sb3.toString(), cause2);
        }
    }
    
    private static final class AtomicFileOutputStream extends OutputStream
    {
        private boolean closed;
        private final FileOutputStream fileOutputStream;
        
        public AtomicFileOutputStream(final File file) throws FileNotFoundException {
            this.closed = false;
            this.fileOutputStream = new FileOutputStream(file);
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.flush();
            try {
                this.fileOutputStream.getFD().sync();
            }
            catch (IOException ex) {
                Log.w("AtomicFile", "Failed to sync file descriptor:", ex);
            }
            this.fileOutputStream.close();
        }
        
        @Override
        public void flush() throws IOException {
            this.fileOutputStream.flush();
        }
        
        @Override
        public void write(final int b) throws IOException {
            this.fileOutputStream.write(b);
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            this.fileOutputStream.write(b);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            this.fileOutputStream.write(b, off, len);
        }
    }
}
