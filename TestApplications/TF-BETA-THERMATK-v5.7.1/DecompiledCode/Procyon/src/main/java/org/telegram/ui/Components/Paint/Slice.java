// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.zip.Deflater;
import java.io.FileOutputStream;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DispatchQueue;
import java.nio.ByteBuffer;
import java.io.File;
import android.graphics.RectF;

public class Slice
{
    private RectF bounds;
    private File file;
    
    public Slice(final ByteBuffer byteBuffer, final RectF bounds, final DispatchQueue dispatchQueue) {
        this.bounds = bounds;
        try {
            this.file = File.createTempFile("paint", ".bin", ApplicationLoader.applicationContext.getCacheDir());
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (this.file == null) {
            return;
        }
        this.storeData(byteBuffer);
    }
    
    private void storeData(final ByteBuffer byteBuffer) {
        try {
            final byte[] array = byteBuffer.array();
            final FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            final Deflater deflater = new Deflater(1, true);
            deflater.setInput(array, byteBuffer.arrayOffset(), byteBuffer.remaining());
            deflater.finish();
            final byte[] array2 = new byte[1024];
            while (!deflater.finished()) {
                fileOutputStream.write(array2, 0, deflater.deflate(array2));
            }
            deflater.end();
            fileOutputStream.close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void cleanResources() {
        final File file = this.file;
        if (file != null) {
            file.delete();
            this.file = null;
        }
    }
    
    public RectF getBounds() {
        return new RectF(this.bounds);
    }
    
    public ByteBuffer getData() {
        try {
            final byte[] array = new byte[1024];
            final byte[] array2 = new byte[1024];
            final FileInputStream fileInputStream = new FileInputStream(this.file);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final Inflater inflater = new Inflater(true);
            while (true) {
                final int read = fileInputStream.read(array);
                if (read != -1) {
                    inflater.setInput(array, 0, read);
                }
                while (true) {
                    final int inflate = inflater.inflate(array2, 0, array2.length);
                    if (inflate == 0) {
                        break;
                    }
                    byteArrayOutputStream.write(array2, 0, inflate);
                }
                if (inflater.finished()) {
                    break;
                }
                inflater.needsInput();
            }
            inflater.end();
            final ByteBuffer wrap = ByteBuffer.wrap(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            byteArrayOutputStream.close();
            fileInputStream.close();
            return wrap;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    public int getHeight() {
        return (int)this.bounds.height();
    }
    
    public int getWidth() {
        return (int)this.bounds.width();
    }
    
    public int getX() {
        return (int)this.bounds.left;
    }
    
    public int getY() {
        return (int)this.bounds.top;
    }
}
