// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.LinkedHashMap;
import java.io.RandomAccessFile;
import java.util.List;

public class GEMFFile
{
    private int mCurrentSource;
    private final List<String> mFileNames;
    private final List<Long> mFileSizes;
    private final List<RandomAccessFile> mFiles;
    private final String mLocation;
    private final List<GEMFRange> mRangeData;
    private boolean mSourceLimited;
    private final LinkedHashMap<Integer, String> mSources;
    
    public GEMFFile(final File file) throws FileNotFoundException, IOException {
        this(file.getAbsolutePath());
    }
    
    public GEMFFile(final String mLocation) throws FileNotFoundException, IOException {
        this.mFiles = new ArrayList<RandomAccessFile>();
        this.mFileNames = new ArrayList<String>();
        this.mRangeData = new ArrayList<GEMFRange>();
        this.mFileSizes = new ArrayList<Long>();
        this.mSources = new LinkedHashMap<Integer, String>();
        this.mSourceLimited = false;
        this.mCurrentSource = 0;
        this.mLocation = mLocation;
        this.openFiles();
        this.readHeader();
    }
    
    private void openFiles() throws FileNotFoundException {
        final File file = new File(this.mLocation);
        this.mFiles.add(new RandomAccessFile(file, "r"));
        this.mFileNames.add(file.getPath());
        int i = 0;
        while (true) {
            ++i;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mLocation);
            sb.append("-");
            sb.append(i);
            final File file2 = new File(sb.toString());
            if (!file2.exists()) {
                break;
            }
            this.mFiles.add(new RandomAccessFile(file2, "r"));
            this.mFileNames.add(file2.getPath());
        }
    }
    
    private void readHeader() throws IOException {
        final List<RandomAccessFile> mFiles = this.mFiles;
        final int n = 0;
        final RandomAccessFile randomAccessFile = mFiles.get(0);
        final Iterator<RandomAccessFile> iterator = this.mFiles.iterator();
        while (iterator.hasNext()) {
            this.mFileSizes.add(iterator.next().length());
        }
        final int int1 = randomAccessFile.readInt();
        if (int1 != 4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad file version: ");
            sb.append(int1);
            throw new IOException(sb.toString());
        }
        final int int2 = randomAccessFile.readInt();
        if (int2 == 256) {
            for (int int3 = randomAccessFile.readInt(), i = 0; i < int3; ++i) {
                final int int4 = randomAccessFile.readInt();
                final int int5 = randomAccessFile.readInt();
                final byte[] array = new byte[int5];
                randomAccessFile.read(array, 0, int5);
                this.mSources.put(new Integer(int4), new String(array));
            }
            for (int int6 = randomAccessFile.readInt(), j = n; j < int6; ++j) {
                final GEMFRange gemfRange = new GEMFRange();
                gemfRange.zoom = randomAccessFile.readInt();
                gemfRange.xMin = randomAccessFile.readInt();
                gemfRange.xMax = randomAccessFile.readInt();
                gemfRange.yMin = randomAccessFile.readInt();
                gemfRange.yMax = randomAccessFile.readInt();
                gemfRange.sourceIndex = randomAccessFile.readInt();
                gemfRange.offset = randomAccessFile.readLong();
                this.mRangeData.add(gemfRange);
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad tile size: ");
        sb2.append(int2);
        throw new IOException(sb2.toString());
    }
    
    public void close() throws IOException {
        final Iterator<RandomAccessFile> iterator = this.mFiles.iterator();
        while (iterator.hasNext()) {
            iterator.next().close();
        }
    }
    
    public InputStream getInputStream(int read, int int1, int n) {
        final Iterator<GEMFRange> iterator = this.mRangeData.iterator();
        Object o;
        GEMFRange gemfRange;
        while (true) {
            final boolean hasNext = iterator.hasNext();
            o = null;
            if (!hasNext) {
                gemfRange = null;
                break;
            }
            final GEMFRange gemfRange2 = iterator.next();
            if (n != gemfRange2.zoom || read < gemfRange2.xMin || read > gemfRange2.xMax || int1 < gemfRange2.yMin || int1 > gemfRange2.yMax) {
                continue;
            }
            gemfRange = gemfRange2;
            if (!this.mSourceLimited) {
                break;
            }
            if (gemfRange2.sourceIndex == this.mCurrentSource) {
                gemfRange = gemfRange2;
                break;
            }
        }
        if (gemfRange == null) {
            return null;
        }
        GEMFInputStream gemfInputStream2 = null;
        GEMFInputStream gemfInputStream3 = null;
        Label_0640: {
            try {
                n = gemfRange.yMax;
                final long n2 = (read - gemfRange.xMin) * (n + 1 - gemfRange.yMin) + (int1 - gemfRange.yMin);
                final long longValue = gemfRange.offset;
                final RandomAccessFile randomAccessFile = this.mFiles.get(0);
                randomAccessFile.seek(n2 * 12L + longValue);
                long long1 = randomAccessFile.readLong();
                int1 = randomAccessFile.readInt();
                RandomAccessFile randomAccessFile2 = this.mFiles.get(0);
                if (long1 > this.mFileSizes.get(0)) {
                    for (n = this.mFileSizes.size(), read = 0; read < n - 1 && long1 > this.mFileSizes.get(read); long1 -= this.mFileSizes.get(read), ++read) {}
                    randomAccessFile2 = this.mFiles.get(read);
                }
                else {
                    read = 0;
                }
                randomAccessFile2.seek(long1);
                final GEMFInputStream gemfInputStream = new GEMFInputStream(this.mFileNames.get(read), long1, int1);
                try {
                    Object o2 = new ByteArrayOutputStream();
                    try {
                        o = new byte[1024];
                        while (gemfInputStream.available() > 0) {
                            read = gemfInputStream.read((byte[])o);
                            if (read > 0) {
                                ((ByteArrayOutputStream)o2).write((byte[])o, 0, read);
                            }
                        }
                        o = new ByteArrayInputStream(((ByteArrayOutputStream)o2).toByteArray());
                        try {
                            ((ByteArrayOutputStream)o2).close();
                        }
                        catch (IOException o2) {
                            ((Throwable)o2).printStackTrace();
                        }
                        try {
                            gemfInputStream.close();
                            o2 = o;
                            return gemfInputStream2;
                        }
                        catch (IOException o2) {
                            ((IOException)o2).printStackTrace();
                            o2 = o;
                            return gemfInputStream2;
                        }
                    }
                    catch (IOException o) {}
                    finally {
                        o = o2;
                    }
                }
                catch (IOException o) {}
            }
            catch (IOException o) {
                gemfInputStream3 = (gemfInputStream2 = null);
            }
            finally {
                gemfInputStream3 = null;
                break Label_0640;
            }
            try {
                ((IOException)o).printStackTrace();
                if (gemfInputStream3 != null) {
                    try {
                        ((ByteArrayOutputStream)gemfInputStream3).close();
                    }
                    catch (IOException gemfInputStream3) {
                        ((Throwable)gemfInputStream3).printStackTrace();
                    }
                }
                if (gemfInputStream2 != null) {
                    try {
                        gemfInputStream2.close();
                    }
                    catch (IOException gemfInputStream2) {
                        ((Throwable)gemfInputStream2).printStackTrace();
                    }
                }
                gemfInputStream2 = null;
                return gemfInputStream2;
            }
            finally {
                final GEMFInputStream gemfInputStream4 = gemfInputStream3;
                gemfInputStream3 = gemfInputStream2;
                final ByteArrayOutputStream byteArrayOutputStream;
                gemfInputStream2 = (GEMFInputStream)byteArrayOutputStream;
                o = gemfInputStream4;
            }
        }
        if (o != null) {
            try {
                ((ByteArrayOutputStream)o).close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (gemfInputStream3 != null) {
            try {
                gemfInputStream3.close();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
        throw gemfInputStream2;
    }
    
    public String getName() {
        return this.mLocation;
    }
    
    class GEMFInputStream extends InputStream
    {
        RandomAccessFile raf;
        int remainingBytes;
        
        GEMFInputStream(final String name, final long pos, final int remainingBytes) throws IOException {
            (this.raf = new RandomAccessFile(name, "r")).seek(pos);
            this.remainingBytes = remainingBytes;
        }
        
        @Override
        public int available() {
            return this.remainingBytes;
        }
        
        @Override
        public void close() throws IOException {
            this.raf.close();
        }
        
        @Override
        public boolean markSupported() {
            return false;
        }
        
        @Override
        public int read() throws IOException {
            final int remainingBytes = this.remainingBytes;
            if (remainingBytes > 0) {
                this.remainingBytes = remainingBytes - 1;
                return this.raf.read();
            }
            throw new IOException("End of stream");
        }
        
        @Override
        public int read(final byte[] b, int read, final int n) throws IOException {
            final RandomAccessFile raf = this.raf;
            final int remainingBytes = this.remainingBytes;
            int len = n;
            if (n > remainingBytes) {
                len = remainingBytes;
            }
            read = raf.read(b, read, len);
            this.remainingBytes -= read;
            return read;
        }
        
        @Override
        public long skip(final long n) {
            return 0L;
        }
    }
    
    private class GEMFRange
    {
        Long offset;
        Integer sourceIndex;
        Integer xMax;
        Integer xMin;
        Integer yMax;
        Integer yMin;
        Integer zoom;
        
        @Override
        public String toString() {
            return String.format("GEMF Range: source=%d, zoom=%d, x=%d-%d, y=%d-%d, offset=0x%08X", this.sourceIndex, this.zoom, this.xMin, this.xMax, this.yMin, this.yMax, this.offset);
        }
    }
}
