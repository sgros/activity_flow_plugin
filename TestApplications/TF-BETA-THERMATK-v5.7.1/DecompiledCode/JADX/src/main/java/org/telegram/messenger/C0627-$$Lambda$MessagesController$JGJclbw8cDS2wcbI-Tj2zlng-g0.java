package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$JGJclbw8cDS2wcbI-Tj2zlng-g0 */
public final /* synthetic */ class C0627-$$Lambda$MessagesController$JGJclbw8cDS2wcbI-Tj2zlng-g0 implements Comparator {
    public static final /* synthetic */ C0627-$$Lambda$MessagesController$JGJclbw8cDS2wcbI-Tj2zlng-g0 INSTANCE = new C0627-$$Lambda$MessagesController$JGJclbw8cDS2wcbI-Tj2zlng-g0();

    private /* synthetic */ C0627-$$Lambda$MessagesController$JGJclbw8cDS2wcbI-Tj2zlng-g0() {
    }

    public final int compare(Object obj, Object obj2) {
        return AndroidUtilities.compare(((Updates) obj).pts, ((Updates) obj2).pts);
    }
}
