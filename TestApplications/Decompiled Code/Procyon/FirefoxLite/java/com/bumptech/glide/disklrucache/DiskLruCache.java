// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.disklrucache;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.io.EOFException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.PrintStream;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.LinkedHashMap;
import java.io.Writer;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.File;
import java.util.concurrent.Callable;
import java.io.Closeable;

public final class DiskLruCache implements Closeable
{
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;
    
    private DiskLruCache(final File file, final int appVersion, final int valueCount, final long maxSize) {
        this.size = 0L;
        this.lruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);
        this.nextSequenceNumber = 0L;
        this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DiskLruCacheThreadFactory());
        this.cleanupCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.journalWriter == null) {
                        return null;
                    }
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                    return null;
                }
            }
        };
        this.directory = file;
        this.appVersion = appVersion;
        this.journalFile = new File(file, "journal");
        this.journalFileTmp = new File(file, "journal.tmp");
        this.journalFileBackup = new File(file, "journal.bkp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }
    
    private void checkNotClosed() {
        if (this.journalWriter != null) {
            return;
        }
        throw new IllegalStateException("cache is closed");
    }
    
    private void completeEdit(final Editor editor, final boolean b) throws IOException {
        synchronized (this) {
            final Entry access$1500 = editor.entry;
            if (access$1500.currentEditor == editor) {
                int i;
                final int n = i = 0;
                if (b) {
                    i = n;
                    if (!access$1500.readable) {
                        int j = 0;
                        while (true) {
                            i = n;
                            if (j >= this.valueCount) {
                                break;
                            }
                            if (!editor.written[j]) {
                                editor.abort();
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Newly created entry didn't create value for index ");
                                sb.append(j);
                                throw new IllegalStateException(sb.toString());
                            }
                            if (!access$1500.getDirtyFile(j).exists()) {
                                editor.abort();
                                return;
                            }
                            ++j;
                        }
                    }
                }
                while (i < this.valueCount) {
                    final File dirtyFile = access$1500.getDirtyFile(i);
                    if (b) {
                        if (dirtyFile.exists()) {
                            final File cleanFile = access$1500.getCleanFile(i);
                            dirtyFile.renameTo(cleanFile);
                            final long n2 = access$1500.lengths[i];
                            final long length = cleanFile.length();
                            access$1500.lengths[i] = length;
                            this.size = this.size - n2 + length;
                        }
                    }
                    else {
                        deleteIfExists(dirtyFile);
                    }
                    ++i;
                }
                ++this.redundantOpCount;
                access$1500.currentEditor = null;
                if (access$1500.readable | b) {
                    access$1500.readable = true;
                    this.journalWriter.append((CharSequence)"CLEAN");
                    this.journalWriter.append(' ');
                    this.journalWriter.append((CharSequence)access$1500.key);
                    this.journalWriter.append((CharSequence)access$1500.getLengths());
                    this.journalWriter.append('\n');
                    if (b) {
                        final long nextSequenceNumber = this.nextSequenceNumber;
                        this.nextSequenceNumber = 1L + nextSequenceNumber;
                        access$1500.sequenceNumber = nextSequenceNumber;
                    }
                }
                else {
                    this.lruEntries.remove(access$1500.key);
                    this.journalWriter.append((CharSequence)"REMOVE");
                    this.journalWriter.append(' ');
                    this.journalWriter.append((CharSequence)access$1500.key);
                    this.journalWriter.append('\n');
                }
                this.journalWriter.flush();
                if (this.size > this.maxSize || this.journalRebuildRequired()) {
                    this.executorService.submit(this.cleanupCallable);
                }
                return;
            }
            throw new IllegalStateException();
        }
    }
    
    private static void deleteIfExists(final File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }
    
    private Editor edit(final String csq, final long n) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            Entry value = this.lruEntries.get(csq);
            if (n != -1L && (value == null || value.sequenceNumber != n)) {
                return null;
            }
            if (value == null) {
                value = new Entry(csq);
                this.lruEntries.put(csq, value);
            }
            else if (value.currentEditor != null) {
                return null;
            }
            final Editor editor = new Editor(value);
            value.currentEditor = editor;
            this.journalWriter.append((CharSequence)"DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence)csq);
            this.journalWriter.append('\n');
            this.journalWriter.flush();
            return editor;
        }
    }
    
    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }
    
    public static DiskLruCache open(final File obj, final int n, final int n2, final long n3) throws IOException {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n2 > 0) {
            final File file = new File(obj, "journal.bkp");
            if (file.exists()) {
                final File file2 = new File(obj, "journal");
                if (file2.exists()) {
                    file.delete();
                }
                else {
                    renameTo(file, file2, false);
                }
            }
            final DiskLruCache diskLruCache = new DiskLruCache(obj, n, n2, n3);
            if (diskLruCache.journalFile.exists()) {
                try {
                    diskLruCache.readJournal();
                    diskLruCache.processJournal();
                    return diskLruCache;
                }
                catch (IOException ex) {
                    final PrintStream out = System.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DiskLruCache ");
                    sb.append(obj);
                    sb.append(" is corrupt: ");
                    sb.append(ex.getMessage());
                    sb.append(", removing");
                    out.println(sb.toString());
                    diskLruCache.delete();
                }
            }
            obj.mkdirs();
            final DiskLruCache diskLruCache2 = new DiskLruCache(obj, n, n2, n3);
            diskLruCache2.rebuildJournal();
            return diskLruCache2;
        }
        throw new IllegalArgumentException("valueCount <= 0");
    }
    
    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            final Entry entry = iterator.next();
            final Editor access$800 = entry.currentEditor;
            final int n = 0;
            int i = 0;
            if (access$800 == null) {
                while (i < this.valueCount) {
                    this.size += entry.lengths[i];
                    ++i;
                }
            }
            else {
                entry.currentEditor = null;
                for (int j = n; j < this.valueCount; ++j) {
                    deleteIfExists(entry.getCleanFile(j));
                    deleteIfExists(entry.getDirtyFile(j));
                }
                iterator.remove();
            }
        }
    }
    
    private void readJournal() throws IOException {
        final StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            final String line = strictLineReader.readLine();
            final String line2 = strictLineReader.readLine();
            final String line3 = strictLineReader.readLine();
            final String line4 = strictLineReader.readLine();
            final String line5 = strictLineReader.readLine();
            if ("libcore.io.DiskLruCache".equals(line) && "1".equals(line2) && Integer.toString(this.appVersion).equals(line3) && Integer.toString(this.valueCount).equals(line4) && "".equals(line5)) {
                int n = 0;
                try {
                    while (true) {
                        this.readJournalLine(strictLineReader.readLine());
                        ++n;
                    }
                }
                catch (EOFException ex) {
                    this.redundantOpCount = n - this.lruEntries.size();
                    if (strictLineReader.hasUnterminatedLine()) {
                        this.rebuildJournal();
                    }
                    else {
                        this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
                    }
                    return;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal header: [");
            sb.append(line);
            sb.append(", ");
            sb.append(line2);
            sb.append(", ");
            sb.append(line4);
            sb.append(", ");
            sb.append(line5);
            sb.append("]");
            throw new IOException(sb.toString());
        }
        finally {
            Util.closeQuietly(strictLineReader);
        }
    }
    
    private void readJournalLine(final String s) throws IOException {
        final int index = s.indexOf(32);
        if (index != -1) {
            final int beginIndex = index + 1;
            final int index2 = s.indexOf(32, beginIndex);
            String s2;
            if (index2 == -1) {
                final String key = s2 = s.substring(beginIndex);
                if (index == "REMOVE".length()) {
                    s2 = key;
                    if (s.startsWith("REMOVE")) {
                        this.lruEntries.remove(key);
                        return;
                    }
                }
            }
            else {
                s2 = s.substring(beginIndex, index2);
            }
            Entry value;
            if ((value = this.lruEntries.get(s2)) == null) {
                value = new Entry(s2);
                this.lruEntries.put(s2, value);
            }
            if (index2 != -1 && index == "CLEAN".length() && s.startsWith("CLEAN")) {
                final String[] split = s.substring(index2 + 1).split(" ");
                value.readable = true;
                value.currentEditor = null;
                value.setLengths(split);
            }
            else if (index2 == -1 && index == "DIRTY".length() && s.startsWith("DIRTY")) {
                value.currentEditor = new Editor(value);
            }
            else if (index2 != -1 || index != "READ".length() || !s.startsWith("READ")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unexpected journal line: ");
                sb.append(s);
                throw new IOException(sb.toString());
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unexpected journal line: ");
        sb2.append(s);
        throw new IOException(sb2.toString());
    }
    
    private void rebuildJournal() throws IOException {
        synchronized (this) {
            if (this.journalWriter != null) {
                this.journalWriter.close();
            }
            Closeable out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
            try {
                ((Writer)out).write("libcore.io.DiskLruCache");
                ((Writer)out).write("\n");
                ((Writer)out).write("1");
                ((Writer)out).write("\n");
                ((Writer)out).write(Integer.toString(this.appVersion));
                ((Writer)out).write("\n");
                ((Writer)out).write(Integer.toString(this.valueCount));
                ((Writer)out).write("\n");
                ((Writer)out).write("\n");
                for (final Entry entry : this.lruEntries.values()) {
                    if (entry.currentEditor != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("DIRTY ");
                        sb.append(entry.key);
                        sb.append('\n');
                        ((Writer)out).write(sb.toString());
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("CLEAN ");
                        sb2.append(entry.key);
                        sb2.append(entry.getLengths());
                        sb2.append('\n');
                        ((Writer)out).write(sb2.toString());
                    }
                }
                ((Writer)out).close();
                if (this.journalFile.exists()) {
                    renameTo(this.journalFile, this.journalFileBackup, true);
                }
                renameTo(this.journalFileTmp, this.journalFile, false);
                this.journalFileBackup.delete();
                out = new FileOutputStream(this.journalFile, true);
                this.journalWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)out, Util.US_ASCII));
            }
            finally {
                ((Writer)out).close();
            }
        }
    }
    
    private static void renameTo(final File file, final File dest, final boolean b) throws IOException {
        if (b) {
            deleteIfExists(dest);
        }
        if (file.renameTo(dest)) {
            return;
        }
        throw new IOException();
    }
    
    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (this.journalWriter == null) {
                return;
            }
            for (final Entry entry : new ArrayList<Entry>(this.lruEntries.values())) {
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            this.trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }
    
    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }
    
    public Editor edit(final String s) throws IOException {
        return this.edit(s, -1L);
    }
    
    public Value get(final String s) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            final Entry entry = this.lruEntries.get(s);
            if (entry == null) {
                return null;
            }
            if (!entry.readable) {
                return null;
            }
            final File[] cleanFiles = entry.cleanFiles;
            for (int length = cleanFiles.length, i = 0; i < length; ++i) {
                if (!cleanFiles[i].exists()) {
                    return null;
                }
            }
            ++this.redundantOpCount;
            this.journalWriter.append((CharSequence)"READ");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence)s);
            this.journalWriter.append('\n');
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return new Value(s, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
        }
    }
    
    public boolean remove(final String key) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            final Entry entry = this.lruEntries.get(key);
            int i = 0;
            if (entry != null && entry.currentEditor == null) {
                while (i < this.valueCount) {
                    final File cleanFile = entry.getCleanFile(i);
                    if (cleanFile.exists() && !cleanFile.delete()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("failed to delete ");
                        sb.append(cleanFile);
                        throw new IOException(sb.toString());
                    }
                    this.size -= entry.lengths[i];
                    entry.lengths[i] = 0L;
                    ++i;
                }
                ++this.redundantOpCount;
                this.journalWriter.append((CharSequence)"REMOVE");
                this.journalWriter.append(' ');
                this.journalWriter.append((CharSequence)key);
                this.journalWriter.append('\n');
                this.lruEntries.remove(key);
                if (this.journalRebuildRequired()) {
                    this.executorService.submit(this.cleanupCallable);
                }
                return true;
            }
            return false;
        }
    }
    
    private static final class DiskLruCacheThreadFactory implements ThreadFactory
    {
        @Override
        public Thread newThread(final Runnable target) {
            synchronized (this) {
                final Thread thread = new Thread(target, "glide-disk-lru-cache-thread");
                thread.setPriority(1);
                return thread;
            }
        }
    }
    
    public final class Editor
    {
        private boolean committed;
        private final Entry entry;
        private final boolean[] written;
        
        private Editor(final Entry entry) {
            this.entry = entry;
            boolean[] written;
            if (entry.readable) {
                written = null;
            }
            else {
                written = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = written;
        }
        
        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }
        
        public void abortUnlessCommitted() {
            if (this.committed) {
                return;
            }
            try {
                this.abort();
            }
            catch (IOException ex) {}
        }
        
        public void commit() throws IOException {
            DiskLruCache.this.completeEdit(this, true);
            this.committed = true;
        }
        
        public File getFile(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor == this) {
                    if (!this.entry.readable) {
                        this.written[n] = true;
                    }
                    final File dirtyFile = this.entry.getDirtyFile(n);
                    if (!DiskLruCache.this.directory.exists()) {
                        DiskLruCache.this.directory.mkdirs();
                    }
                    return dirtyFile;
                }
                throw new IllegalStateException();
            }
        }
    }
    
    private final class Entry
    {
        File[] cleanFiles;
        private Editor currentEditor;
        File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        
        private Entry(final String s) {
            this.key = s;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            final StringBuilder sb = new StringBuilder(s);
            sb.append('.');
            final int length = sb.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; ++i) {
                sb.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, sb.toString());
                sb.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, sb.toString());
                sb.setLength(length);
            }
        }
        
        private IOException invalidLengths(final String[] a) throws IOException {
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal line: ");
            sb.append(Arrays.toString(a));
            throw new IOException(sb.toString());
        }
        
        private void setLengths(final String[] array) throws IOException {
            if (array.length == DiskLruCache.this.valueCount) {
                int i = 0;
                try {
                    while (i < array.length) {
                        this.lengths[i] = Long.parseLong(array[i]);
                        ++i;
                    }
                    return;
                }
                catch (NumberFormatException ex) {
                    throw this.invalidLengths(array);
                }
            }
            throw this.invalidLengths(array);
        }
        
        public File getCleanFile(final int n) {
            return this.cleanFiles[n];
        }
        
        public File getDirtyFile(final int n) {
            return this.dirtyFiles[n];
        }
        
        public String getLengths() throws IOException {
            final StringBuilder sb = new StringBuilder();
            for (final long lng : this.lengths) {
                sb.append(' ');
                sb.append(lng);
            }
            return sb.toString();
        }
    }
    
    public final class Value
    {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        
        private Value(final String key, final long sequenceNumber, final File[] files, final long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.files = files;
            this.lengths = lengths;
        }
        
        public File getFile(final int n) {
            return this.files[n];
        }
    }
}
