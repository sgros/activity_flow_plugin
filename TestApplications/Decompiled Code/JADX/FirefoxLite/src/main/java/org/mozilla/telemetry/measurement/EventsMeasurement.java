package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.event.TelemetryEvent;
import org.mozilla.telemetry.util.IOUtils;

public class EventsMeasurement extends TelemetryMeasurement {
    private static final String LOG_TAG = "EventsMeasurement";
    private TelemetryConfiguration configuration;
    private Logger logger = new Logger("telemetry/events");

    public EventsMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("events");
        this.configuration = telemetryConfiguration;
    }

    public void add(TelemetryEvent telemetryEvent) {
        saveEventToDisk(telemetryEvent);
    }

    public Object flush() {
        return readAndClearEventsFromDisk();
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:38:0x0074 */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0082  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0099  */
    private synchronized org.json.JSONArray readAndClearEventsFromDisk() {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = new org.json.JSONArray;	 Catch:{ all -> 0x00a6 }
        r0.<init>();	 Catch:{ all -> 0x00a6 }
        r1 = r8.getEventFile();	 Catch:{ all -> 0x00a6 }
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0052 }
        r3.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0074, IOException -> 0x0052 }
        r2 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        r4 = new java.io.InputStreamReader;	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        r4.<init>(r3);	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        r2.<init>(r4);	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
    L_0x001a:
        r4 = r2.readLine();	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        if (r4 == 0) goto L_0x0035;
    L_0x0020:
        r5 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x002c }
        r5.<init>(r4);	 Catch:{ JSONException -> 0x002c }
        r0.put(r5);	 Catch:{ JSONException -> 0x002c }
        r8.resetEventCount();	 Catch:{ JSONException -> 0x002c }
        goto L_0x001a;
    L_0x002c:
        r4 = move-exception;
        r5 = r8.logger;	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        r6 = "Could not parse event from disk";
        r5.warn(r6, r4);	 Catch:{ FileNotFoundException -> 0x004d, IOException -> 0x004b }
        goto L_0x001a;
    L_0x0035:
        org.mozilla.telemetry.util.IOUtils.safeClose(r3);	 Catch:{ all -> 0x00a6 }
        r1 = r1.delete();	 Catch:{ all -> 0x00a6 }
        if (r1 != 0) goto L_0x0070;
    L_0x003e:
        r1 = r8.logger;	 Catch:{ all -> 0x00a6 }
        r2 = "Events file could not be deleted";
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00a6 }
        r3.<init>();	 Catch:{ all -> 0x00a6 }
    L_0x0047:
        r1.warn(r2, r3);	 Catch:{ all -> 0x00a6 }
        goto L_0x0070;
    L_0x004b:
        r2 = move-exception;
        goto L_0x0056;
    L_0x004d:
        r2 = r3;
        goto L_0x0074;
    L_0x004f:
        r0 = move-exception;
        r3 = r2;
        goto L_0x0090;
    L_0x0052:
        r3 = move-exception;
        r7 = r3;
        r3 = r2;
        r2 = r7;
    L_0x0056:
        r4 = r8.logger;	 Catch:{ all -> 0x0072 }
        r5 = "IOException while reading events from disk";
        r4.warn(r5, r2);	 Catch:{ all -> 0x0072 }
        org.mozilla.telemetry.util.IOUtils.safeClose(r3);	 Catch:{ all -> 0x00a6 }
        r1 = r1.delete();	 Catch:{ all -> 0x00a6 }
        if (r1 != 0) goto L_0x0070;
    L_0x0066:
        r1 = r8.logger;	 Catch:{ all -> 0x00a6 }
        r2 = "Events file could not be deleted";
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00a6 }
        r3.<init>();	 Catch:{ all -> 0x00a6 }
        goto L_0x0047;
    L_0x0070:
        monitor-exit(r8);
        return r0;
    L_0x0072:
        r0 = move-exception;
        goto L_0x0090;
    L_0x0074:
        r0 = new org.json.JSONArray;	 Catch:{ all -> 0x004f }
        r0.<init>();	 Catch:{ all -> 0x004f }
        org.mozilla.telemetry.util.IOUtils.safeClose(r2);	 Catch:{ all -> 0x00a6 }
        r1 = r1.delete();	 Catch:{ all -> 0x00a6 }
        if (r1 != 0) goto L_0x008e;
    L_0x0082:
        r1 = r8.logger;	 Catch:{ all -> 0x00a6 }
        r2 = "Events file could not be deleted";
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00a6 }
        r3.<init>();	 Catch:{ all -> 0x00a6 }
        r1.warn(r2, r3);	 Catch:{ all -> 0x00a6 }
    L_0x008e:
        monitor-exit(r8);
        return r0;
    L_0x0090:
        org.mozilla.telemetry.util.IOUtils.safeClose(r3);	 Catch:{ all -> 0x00a6 }
        r1 = r1.delete();	 Catch:{ all -> 0x00a6 }
        if (r1 != 0) goto L_0x00a5;
    L_0x0099:
        r1 = r8.logger;	 Catch:{ all -> 0x00a6 }
        r2 = "Events file could not be deleted";
        r3 = new java.io.IOException;	 Catch:{ all -> 0x00a6 }
        r3.<init>();	 Catch:{ all -> 0x00a6 }
        r1.warn(r2, r3);	 Catch:{ all -> 0x00a6 }
    L_0x00a5:
        throw r0;	 Catch:{ all -> 0x00a6 }
    L_0x00a6:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.telemetry.measurement.EventsMeasurement.readAndClearEventsFromDisk():org.json.JSONArray");
    }

    /* Access modifiers changed, original: 0000 */
    public File getEventFile() {
        return new File(this.configuration.getDataDirectory(), "events1");
    }

    private synchronized void saveEventToDisk(TelemetryEvent telemetryEvent) {
        Throwable e;
        Closeable closeable = null;
        try {
            Closeable fileOutputStream = new FileOutputStream(getEventFile(), true);
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                bufferedWriter.write(telemetryEvent.toJSON());
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                countEvent();
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

    private synchronized void countEvent() {
        SharedPreferences sharedPreferences = this.configuration.getSharedPreferences();
        sharedPreferences.edit().putLong("event_count", sharedPreferences.getLong("event_count", 0) + 1).apply();
    }

    private synchronized void resetEventCount() {
        this.configuration.getSharedPreferences().edit().putLong("event_count", 0).apply();
    }

    public long getEventCount() {
        return this.configuration.getSharedPreferences().getLong("event_count", 0);
    }
}
