package org.mozilla.telemetry.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.ping.TelemetryPing;
import org.mozilla.telemetry.serialize.TelemetryPingSerializer;
import org.mozilla.telemetry.storage.TelemetryStorage.TelemetryStorageCallback;
import org.mozilla.telemetry.util.FileUtils;
import org.mozilla.telemetry.util.FileUtils.FileLastModifiedComparator;
import org.mozilla.telemetry.util.FileUtils.FilenameRegexFilter;
import org.mozilla.telemetry.util.IOUtils;

public class FileTelemetryStorage implements TelemetryStorage {
    private final TelemetryConfiguration configuration;
    private final Logger logger = new Logger("telemetry/storage");
    private final TelemetryPingSerializer serializer;
    private final File storageDirectory;

    public FileTelemetryStorage(TelemetryConfiguration telemetryConfiguration, TelemetryPingSerializer telemetryPingSerializer) {
        this.configuration = telemetryConfiguration;
        this.serializer = telemetryPingSerializer;
        this.storageDirectory = new File(telemetryConfiguration.getDataDirectory(), "storage");
        FileUtils.assertDirectory(this.storageDirectory);
    }

    public synchronized void store(TelemetryPing telemetryPing) {
        storePing(telemetryPing);
        maybePrunePings(telemetryPing.getType());
    }

    public boolean process(String str, TelemetryStorageCallback telemetryStorageCallback) {
        Throwable th;
        File[] listPingFiles = listPingFiles(str);
        int length = listPingFiles.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                return true;
            }
            File file = listPingFiles[i];
            Closeable fileReader;
            try {
                fileReader = new FileReader(file);
                try {
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String readLine = bufferedReader.readLine();
                    String readLine2 = bufferedReader.readLine();
                    if (readLine2 != null) {
                        if (!telemetryStorageCallback.onTelemetryPingLoaded(readLine, readLine2)) {
                            z = false;
                        }
                    }
                    if (z) {
                        if (!file.delete()) {
                            this.logger.warn("Could not delete local ping file after processing", new IOException());
                        }
                        IOUtils.safeClose(fileReader);
                        i++;
                    } else {
                        IOUtils.safeClose(fileReader);
                        return false;
                    }
                } catch (FileNotFoundException unused) {
                    continue;
                } catch (IOException unused2) {
                    IOUtils.safeClose(fileReader);
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    IOUtils.safeClose(fileReader);
                    throw th;
                }
            } catch (FileNotFoundException unused3) {
                fileReader = null;
            } catch (IOException unused4) {
                fileReader = null;
                IOUtils.safeClose(fileReader);
                return false;
            } catch (Throwable th3) {
                th = th3;
                fileReader = null;
                IOUtils.safeClose(fileReader);
                throw th;
            }
        }
    }

    private void storePing(TelemetryPing telemetryPing) {
        Throwable e;
        File file = new File(this.storageDirectory, telemetryPing.getType());
        FileUtils.assertDirectory(file);
        String serialize = this.serializer.serialize(telemetryPing);
        File file2 = new File(file, telemetryPing.getDocumentId());
        Closeable closeable = null;
        try {
            Closeable fileOutputStream = new FileOutputStream(file2, true);
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(telemetryPing.getUploadPath());
                bufferedWriter.newLine();
                bufferedWriter.write(serialize);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                IOUtils.safeClose(fileOutputStream);
            } catch (IOException e2) {
                e = e2;
                closeable = fileOutputStream;
                try {
                    this.logger.warn("IOException while writing event to disk", e);
                    IOUtils.safeClose(closeable);
                } catch (Throwable th) {
                    e = th;
                    fileOutputStream = closeable;
                    IOUtils.safeClose(fileOutputStream);
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                IOUtils.safeClose(fileOutputStream);
                throw e;
            }
        } catch (IOException e3) {
            e = e3;
            this.logger.warn("IOException while writing event to disk", e);
            IOUtils.safeClose(closeable);
        }
    }

    private void maybePrunePings(String str) {
        File[] listPingFiles = listPingFiles(str);
        int length = listPingFiles.length - this.configuration.getMaximumNumberOfPingsPerType();
        if (length > 0) {
            StringBuilder stringBuilder;
            File file;
            ArrayList<File> arrayList = new ArrayList(Arrays.asList(listPingFiles));
            Collections.sort(arrayList, new FileLastModifiedComparator());
            for (File file2 : arrayList) {
                PrintStream printStream = System.out;
                stringBuilder = new StringBuilder();
                stringBuilder.append(file2.lastModified());
                stringBuilder.append(" ");
                stringBuilder.append(file2.getAbsolutePath());
                printStream.println(stringBuilder.toString());
            }
            for (int i = 0; i < length; i++) {
                file2 = (File) arrayList.get(i);
                if (!file2.delete()) {
                    Logger logger = this.logger;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Can't prune ping file: ");
                    stringBuilder.append(file2.getAbsolutePath());
                    logger.warn(stringBuilder.toString(), new IOException());
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public File[] listPingFiles(String str) {
        File[] listFiles = new File(this.storageDirectory, str).listFiles(new FilenameRegexFilter(Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")));
        return listFiles == null ? new File[0] : listFiles;
    }

    public int countStoredPings(String str) {
        return listPingFiles(str).length;
    }
}
