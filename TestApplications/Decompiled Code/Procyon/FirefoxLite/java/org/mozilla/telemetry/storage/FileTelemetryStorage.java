// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.storage;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.io.Closeable;
import org.mozilla.telemetry.util.IOUtils;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import org.mozilla.telemetry.ping.TelemetryPing;
import java.io.PrintStream;
import java.util.Iterator;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import org.mozilla.telemetry.util.FileUtils;
import java.io.File;
import org.mozilla.telemetry.serialize.TelemetryPingSerializer;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class FileTelemetryStorage implements TelemetryStorage
{
    private final TelemetryConfiguration configuration;
    private final Logger logger;
    private final TelemetryPingSerializer serializer;
    private final File storageDirectory;
    
    public FileTelemetryStorage(final TelemetryConfiguration configuration, final TelemetryPingSerializer serializer) {
        this.logger = new Logger("telemetry/storage");
        this.configuration = configuration;
        this.serializer = serializer;
        FileUtils.assertDirectory(this.storageDirectory = new File(configuration.getDataDirectory(), "storage"));
    }
    
    private void maybePrunePings(final String s) {
        final File[] listPingFiles = this.listPingFiles(s);
        final int n = listPingFiles.length - this.configuration.getMaximumNumberOfPingsPerType();
        if (n <= 0) {
            return;
        }
        final ArrayList list = new ArrayList<File>(Arrays.asList(listPingFiles));
        Collections.sort((List<E>)list, (Comparator<? super E>)new FileUtils.FileLastModifiedComparator());
        for (final File file : list) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(file.lastModified());
            sb.append(" ");
            sb.append(file.getAbsolutePath());
            out.println(sb.toString());
        }
        for (int i = 0; i < n; ++i) {
            final File file2 = list.get(i);
            if (!file2.delete()) {
                final Logger logger = this.logger;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Can't prune ping file: ");
                sb2.append(file2.getAbsolutePath());
                logger.warn(sb2.toString(), new IOException());
            }
        }
    }
    
    private void storePing(final TelemetryPing telemetryPing) {
        final File parent = new File(this.storageDirectory, telemetryPing.getType());
        FileUtils.assertDirectory(parent);
        final String serialize = this.serializer.serialize(telemetryPing);
        final File file = new File(parent, telemetryPing.getDocumentId());
        Object out = null;
        Object o2;
        final Object o = o2 = null;
        Writer writer = null;
        Writer writer2;
        try {
            try {
                o2 = o;
                final FileOutputStream out2 = new FileOutputStream(file, true);
                try {
                    out = new OutputStreamWriter(out2);
                    o2 = new BufferedWriter((Writer)out);
                    ((Writer)o2).write(telemetryPing.getUploadPath());
                    ((BufferedWriter)o2).newLine();
                    ((Writer)o2).write(serialize);
                    ((BufferedWriter)o2).newLine();
                    ((BufferedWriter)o2).flush();
                    ((BufferedWriter)o2).close();
                    IOUtils.safeClose(out2);
                }
                catch (IOException o2) {}
            }
            finally {
                writer = (Writer)o2;
            }
        }
        catch (IOException writer) {
            writer2 = (Writer)out;
        }
        this.logger.warn("IOException while writing event to disk", (Throwable)writer);
        IOUtils.safeClose(writer2);
        return;
        IOUtils.safeClose(writer);
    }
    
    @Override
    public int countStoredPings(final String s) {
        return this.listPingFiles(s).length;
    }
    
    File[] listPingFiles(final String child) {
        final File[] listFiles = new File(this.storageDirectory, child).listFiles(new FileUtils.FilenameRegexFilter(Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")));
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }
    
    @Override
    public boolean process(String in, final TelemetryStorageCallback telemetryStorageCallback) {
        final File[] listPingFiles = this.listPingFiles(in);
        final int length = listPingFiles.length;
        final int n = 0;
        final boolean b = true;
        if (n >= length) {
            goto Label_0186;
        }
        final File file = listPingFiles[n];
        try {
            in = (String)new FileReader(file);
            try {
                final BufferedReader bufferedReader = new BufferedReader((Reader)in);
                Object o = bufferedReader.readLine();
                final String line = bufferedReader.readLine();
                int n2 = b ? 1 : 0;
                if (line != null) {
                    if (telemetryStorageCallback.onTelemetryPingLoaded((String)o, line)) {
                        n2 = (b ? 1 : 0);
                    }
                    else {
                        n2 = 0;
                    }
                }
                if (n2 == 0) {
                    IOUtils.safeClose((Closeable)in);
                    return false;
                }
                o = in;
                if (!file.delete()) {
                    o = this.logger;
                    ((Logger)o).warn("Could not delete local ping file after processing", new IOException());
                    o = in;
                    goto Label_0175;
                }
                goto Label_0175;
            }
            catch (FileNotFoundException o) {
                o = in;
            }
            catch (IOException ex) {}
        }
        catch (IOException ex2) {}
        catch (FileNotFoundException ex3) {}
    }
    
    @Override
    public void store(final TelemetryPing telemetryPing) {
        synchronized (this) {
            this.storePing(telemetryPing);
            this.maybePrunePings(telemetryPing.getType());
        }
    }
}
