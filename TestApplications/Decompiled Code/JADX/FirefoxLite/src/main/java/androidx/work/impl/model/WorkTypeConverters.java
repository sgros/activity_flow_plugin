package androidx.work.impl.model;

import androidx.work.BackoffPolicy;
import androidx.work.NetworkType;
import androidx.work.WorkInfo.State;

public class WorkTypeConverters {
    public static int stateToInt(State state) {
        switch (state) {
            case ENQUEUED:
                return 0;
            case RUNNING:
                return 1;
            case SUCCEEDED:
                return 2;
            case FAILED:
                return 3;
            case BLOCKED:
                return 4;
            case CANCELLED:
                return 5;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(state);
                stringBuilder.append(" to int");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static State intToState(int i) {
        switch (i) {
            case 0:
                return State.ENQUEUED;
            case 1:
                return State.RUNNING;
            case 2:
                return State.SUCCEEDED;
            case 3:
                return State.FAILED;
            case 4:
                return State.BLOCKED;
            case 5:
                return State.CANCELLED;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(i);
                stringBuilder.append(" to State");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static int backoffPolicyToInt(BackoffPolicy backoffPolicy) {
        switch (backoffPolicy) {
            case EXPONENTIAL:
                return 0;
            case LINEAR:
                return 1;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(backoffPolicy);
                stringBuilder.append(" to int");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static BackoffPolicy intToBackoffPolicy(int i) {
        switch (i) {
            case 0:
                return BackoffPolicy.EXPONENTIAL;
            case 1:
                return BackoffPolicy.LINEAR;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(i);
                stringBuilder.append(" to BackoffPolicy");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static int networkTypeToInt(NetworkType networkType) {
        switch (networkType) {
            case NOT_REQUIRED:
                return 0;
            case CONNECTED:
                return 1;
            case UNMETERED:
                return 2;
            case NOT_ROAMING:
                return 3;
            case METERED:
                return 4;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(networkType);
                stringBuilder.append(" to int");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static NetworkType intToNetworkType(int i) {
        switch (i) {
            case 0:
                return NetworkType.NOT_REQUIRED;
            case 1:
                return NetworkType.CONNECTED;
            case 2:
                return NetworkType.UNMETERED;
            case 3:
                return NetworkType.NOT_ROAMING;
            case 4:
                return NetworkType.METERED;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not convert ");
                stringBuilder.append(i);
                stringBuilder.append(" to NetworkType");
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:31:0x0062=Splitter:B:31:0x0062, B:16:0x0048=Splitter:B:16:0x0048} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0071 A:{SYNTHETIC, Splitter:B:38:0x0071} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005a A:{SYNTHETIC, Splitter:B:27:0x005a} */
    public static byte[] contentUriTriggersToByteArray(androidx.work.ContentUriTriggers r4) {
        /*
        r0 = r4.size();
        r1 = 0;
        if (r0 != 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>();
        r2 = new java.io.ObjectOutputStream;	 Catch:{ IOException -> 0x0054 }
        r2.<init>(r0);	 Catch:{ IOException -> 0x0054 }
        r1 = r4.size();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r2.writeInt(r1);	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r4 = r4.getTriggers();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r4 = r4.iterator();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
    L_0x0021:
        r1 = r4.hasNext();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        if (r1 == 0) goto L_0x0040;
    L_0x0027:
        r1 = r4.next();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r1 = (androidx.work.ContentUriTriggers.Trigger) r1;	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r3 = r1.getUri();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r3 = r3.toString();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r2.writeUTF(r3);	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r1 = r1.shouldTriggerForDescendants();	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        r2.writeBoolean(r1);	 Catch:{ IOException -> 0x004e, all -> 0x004c }
        goto L_0x0021;
    L_0x0040:
        r2.close();	 Catch:{ IOException -> 0x0044 }
        goto L_0x0048;
    L_0x0044:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x0048:
        r0.close();	 Catch:{ IOException -> 0x0066 }
        goto L_0x006a;
    L_0x004c:
        r4 = move-exception;
        goto L_0x006f;
    L_0x004e:
        r4 = move-exception;
        r1 = r2;
        goto L_0x0055;
    L_0x0051:
        r4 = move-exception;
        r2 = r1;
        goto L_0x006f;
    L_0x0054:
        r4 = move-exception;
    L_0x0055:
        r4.printStackTrace();	 Catch:{ all -> 0x0051 }
        if (r1 == 0) goto L_0x0062;
    L_0x005a:
        r1.close();	 Catch:{ IOException -> 0x005e }
        goto L_0x0062;
    L_0x005e:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x0062:
        r0.close();	 Catch:{ IOException -> 0x0066 }
        goto L_0x006a;
    L_0x0066:
        r4 = move-exception;
        r4.printStackTrace();
    L_0x006a:
        r4 = r0.toByteArray();
        return r4;
    L_0x006f:
        if (r2 == 0) goto L_0x0079;
    L_0x0071:
        r2.close();	 Catch:{ IOException -> 0x0075 }
        goto L_0x0079;
    L_0x0075:
        r1 = move-exception;
        r1.printStackTrace();
    L_0x0079:
        r0.close();	 Catch:{ IOException -> 0x007d }
        goto L_0x0081;
    L_0x007d:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0081:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.model.WorkTypeConverters.contentUriTriggersToByteArray(androidx.work.ContentUriTriggers):byte[]");
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:15:0x0033=Splitter:B:15:0x0033, B:29:0x004e=Splitter:B:29:0x004e} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0046 A:{SYNTHETIC, Splitter:B:25:0x0046} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x005a A:{SYNTHETIC, Splitter:B:36:0x005a} */
    public static androidx.work.ContentUriTriggers byteArrayToContentUriTriggers(byte[] r6) {
        /*
        r0 = new androidx.work.ContentUriTriggers;
        r0.<init>();
        if (r6 != 0) goto L_0x0008;
    L_0x0007:
        return r0;
    L_0x0008:
        r1 = new java.io.ByteArrayInputStream;
        r1.<init>(r6);
        r6 = 0;
        r2 = new java.io.ObjectInputStream;	 Catch:{ IOException -> 0x003d, all -> 0x0039 }
        r2.<init>(r1);	 Catch:{ IOException -> 0x003d, all -> 0x0039 }
        r6 = r2.readInt();	 Catch:{ IOException -> 0x0037 }
    L_0x0017:
        if (r6 <= 0) goto L_0x002b;
    L_0x0019:
        r3 = r2.readUTF();	 Catch:{ IOException -> 0x0037 }
        r3 = android.net.Uri.parse(r3);	 Catch:{ IOException -> 0x0037 }
        r4 = r2.readBoolean();	 Catch:{ IOException -> 0x0037 }
        r0.add(r3, r4);	 Catch:{ IOException -> 0x0037 }
        r6 = r6 + -1;
        goto L_0x0017;
    L_0x002b:
        r2.close();	 Catch:{ IOException -> 0x002f }
        goto L_0x0033;
    L_0x002f:
        r6 = move-exception;
        r6.printStackTrace();
    L_0x0033:
        r1.close();	 Catch:{ IOException -> 0x0052 }
        goto L_0x0056;
    L_0x0037:
        r6 = move-exception;
        goto L_0x0041;
    L_0x0039:
        r0 = move-exception;
        r2 = r6;
        r6 = r0;
        goto L_0x0058;
    L_0x003d:
        r2 = move-exception;
        r5 = r2;
        r2 = r6;
        r6 = r5;
    L_0x0041:
        r6.printStackTrace();	 Catch:{ all -> 0x0057 }
        if (r2 == 0) goto L_0x004e;
    L_0x0046:
        r2.close();	 Catch:{ IOException -> 0x004a }
        goto L_0x004e;
    L_0x004a:
        r6 = move-exception;
        r6.printStackTrace();
    L_0x004e:
        r1.close();	 Catch:{ IOException -> 0x0052 }
        goto L_0x0056;
    L_0x0052:
        r6 = move-exception;
        r6.printStackTrace();
    L_0x0056:
        return r0;
    L_0x0057:
        r6 = move-exception;
    L_0x0058:
        if (r2 == 0) goto L_0x0062;
    L_0x005a:
        r2.close();	 Catch:{ IOException -> 0x005e }
        goto L_0x0062;
    L_0x005e:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0062:
        r1.close();	 Catch:{ IOException -> 0x0066 }
        goto L_0x006a;
    L_0x0066:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x006a:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.model.WorkTypeConverters.byteArrayToContentUriTriggers(byte[]):androidx.work.ContentUriTriggers");
    }
}
