package org.telegram.messenger;

import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8 */
public final /* synthetic */ class C0904-$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8 implements OnHeaderDecodedListener {
    public static final /* synthetic */ C0904-$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8 INSTANCE = new C0904-$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8();

    private /* synthetic */ C0904-$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8() {
    }

    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
        imageDecoder.setPostProcessor(C0928-$$Lambda$NotificationsController$wGfyzcvvHxFIxbrke7bSnOwTfcM.INSTANCE);
    }
}
