package util;

import java.util.Vector;

public class BackgroundRunner extends Thread {
    private static BackgroundRunner instance;
    private boolean end = false;
    private boolean paused = false;
    private Vector queue = new Vector();
    private Runnable queueProcessedListener = null;

    public BackgroundRunner() {
        start();
    }

    public BackgroundRunner(boolean paused) {
        this.paused = paused;
        start();
    }

    public synchronized void pause() {
        this.paused = true;
    }

    public synchronized void unpause() {
        this.paused = false;
        notify();
    }

    public static BackgroundRunner getInstance() {
        if (instance == null) {
            instance = new BackgroundRunner();
        }
        return instance;
    }

    public void setQueueListener(Runnable r) {
        this.queueProcessedListener = r;
    }

    /* JADX WARNING: Missing block: B:13:0x0013, code skipped:
            r1 = false;
     */
    /* JADX WARNING: Missing block: B:15:0x001a, code skipped:
            if (r5.queue.isEmpty() != false) goto L_0x0032;
     */
    /* JADX WARNING: Missing block: B:16:0x001c, code skipped:
            r1 = true;
            r0 = (java.lang.Runnable) r5.queue.firstElement();
            r5.queue.removeElementAt(0);
     */
    /* JADX WARNING: Missing block: B:18:?, code skipped:
            r0.run();
     */
    /* JADX WARNING: Missing block: B:38:0x004e, code skipped:
            r2 = move-exception;
     */
    /* JADX WARNING: Missing block: B:39:0x004f, code skipped:
            r2.printStackTrace();
     */
    public void run() {
        /*
        r5 = this;
    L_0x0000:
        r3 = r5.end;
        if (r3 != 0) goto L_0x0011;
    L_0x0004:
        monitor-enter(r5);
    L_0x0005:
        r3 = r5.paused;	 Catch:{ all -> 0x004b }
        if (r3 == 0) goto L_0x0012;
    L_0x0009:
        r5.wait();	 Catch:{ InterruptedException -> 0x005e }
    L_0x000c:
        r3 = r5.end;	 Catch:{ all -> 0x004b }
        if (r3 == 0) goto L_0x0005;
    L_0x0010:
        monitor-exit(r5);	 Catch:{ all -> 0x004b }
    L_0x0011:
        return;
    L_0x0012:
        monitor-exit(r5);	 Catch:{ all -> 0x004b }
        r1 = 0;
    L_0x0014:
        r3 = r5.queue;
        r3 = r3.isEmpty();
        if (r3 != 0) goto L_0x0032;
    L_0x001c:
        r1 = 1;
        r3 = r5.queue;
        r0 = r3.firstElement();
        r0 = (java.lang.Runnable) r0;
        r3 = r5.queue;
        r4 = 0;
        r3.removeElementAt(r4);
        r0.run();	 Catch:{ Throwable -> 0x004e }
    L_0x002e:
        r3 = r5.paused;
        if (r3 == 0) goto L_0x0014;
    L_0x0032:
        if (r1 == 0) goto L_0x003d;
    L_0x0034:
        r3 = r5.queueProcessedListener;
        if (r3 == 0) goto L_0x003d;
    L_0x0038:
        r3 = r5.queueProcessedListener;
        r3.run();
    L_0x003d:
        monitor-enter(r5);
        r3 = r5.queue;	 Catch:{ all -> 0x0048 }
        r3 = r3.isEmpty();	 Catch:{ all -> 0x0048 }
        if (r3 != 0) goto L_0x0053;
    L_0x0046:
        monitor-exit(r5);	 Catch:{ all -> 0x0048 }
        goto L_0x0000;
    L_0x0048:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0048 }
        throw r3;
    L_0x004b:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x004b }
        throw r3;
    L_0x004e:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x002e;
    L_0x0053:
        r3 = r5.end;	 Catch:{ all -> 0x0048 }
        if (r3 == 0) goto L_0x0059;
    L_0x0057:
        monitor-exit(r5);	 Catch:{ all -> 0x0048 }
        goto L_0x0011;
    L_0x0059:
        r5.wait();	 Catch:{ InterruptedException -> 0x0060 }
    L_0x005c:
        monitor-exit(r5);	 Catch:{ all -> 0x0048 }
        goto L_0x0000;
    L_0x005e:
        r3 = move-exception;
        goto L_0x000c;
    L_0x0060:
        r3 = move-exception;
        goto L_0x005c;
        */
        throw new UnsupportedOperationException("Method not decompiled: util.BackgroundRunner.run():void");
    }

    public synchronized void perform(Runnable c) {
        this.queue.addElement(c);
        notify();
    }

    public static void performTask(Runnable c) {
        getInstance().perform(c);
    }

    public synchronized void kill() {
        this.end = true;
        notify();
    }
}
