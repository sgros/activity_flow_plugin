package com.google.android.exoplayer2.video;

import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;

public interface VideoRendererEventListener {

    /* renamed from: com.google.android.exoplayer2.video.VideoRendererEventListener$-CC */
    public final /* synthetic */ class C0246-CC {
        public static void $default$onDroppedFrames(VideoRendererEventListener videoRendererEventListener, int i, long j) {
        }

        public static void $default$onRenderedFirstFrame(VideoRendererEventListener videoRendererEventListener, Surface surface) {
        }

        public static void $default$onVideoDecoderInitialized(VideoRendererEventListener videoRendererEventListener, String str, long j, long j2) {
        }

        public static void $default$onVideoDisabled(VideoRendererEventListener videoRendererEventListener, DecoderCounters decoderCounters) {
        }

        public static void $default$onVideoEnabled(VideoRendererEventListener videoRendererEventListener, DecoderCounters decoderCounters) {
        }

        public static void $default$onVideoInputFormatChanged(VideoRendererEventListener videoRendererEventListener, Format format) {
        }

        public static void $default$onVideoSizeChanged(VideoRendererEventListener videoRendererEventListener, int i, int i2, int i3, float f) {
        }
    }

    public static final class EventDispatcher {
        private final Handler handler;
        private final VideoRendererEventListener listener;

        public EventDispatcher(Handler handler, VideoRendererEventListener videoRendererEventListener) {
            if (videoRendererEventListener != null) {
                Assertions.checkNotNull(handler);
                handler = handler;
            } else {
                handler = null;
            }
            this.handler = handler;
            this.listener = videoRendererEventListener;
        }

        public void enabled(DecoderCounters decoderCounters) {
            if (this.listener != null) {
                this.handler.post(new C0240xeb53b9bf(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$enabled$0$VideoRendererEventListener$EventDispatcher(DecoderCounters decoderCounters) {
            this.listener.onVideoEnabled(decoderCounters);
        }

        public void decoderInitialized(String str, long j, long j2) {
            if (this.listener != null) {
                this.handler.post(new C0239xed443dd9(this, str, j, j2));
            }
        }

        /* renamed from: lambda$decoderInitialized$1$VideoRendererEventListener$EventDispatcher */
        public /* synthetic */ void mo3815x9a08f997(String str, long j, long j2) {
            this.listener.onVideoDecoderInitialized(str, j, j2);
        }

        public void inputFormatChanged(Format format) {
            if (this.listener != null) {
                this.handler.post(new C0236xd380fd15(this, format));
            }
        }

        /* renamed from: lambda$inputFormatChanged$2$VideoRendererEventListener$EventDispatcher */
        public /* synthetic */ void mo3819xe7570b3(Format format) {
            this.listener.onVideoInputFormatChanged(format);
        }

        public void droppedFrames(int i, long j) {
            if (this.listener != null) {
                this.handler.post(new C0242x89d7f10d(this, i, j));
            }
        }

        /* renamed from: lambda$droppedFrames$3$VideoRendererEventListener$EventDispatcher */
        public /* synthetic */ void mo3817xf7e95759(int i, long j) {
            this.listener.onDroppedFrames(i, j);
        }

        public void videoSizeChanged(int i, int i2, int i3, float f) {
            if (this.listener != null) {
                this.handler.post(new C0238x3b67d6b6(this, i, i2, i3, f));
            }
        }

        /* renamed from: lambda$videoSizeChanged$4$VideoRendererEventListener$EventDispatcher */
        public /* synthetic */ void mo3821x6ff94f6c(int i, int i2, int i3, float f) {
            this.listener.onVideoSizeChanged(i, i2, i3, f);
        }

        public void renderedFirstFrame(Surface surface) {
            if (this.listener != null) {
                this.handler.post(new C0237x785b3d18(this, surface));
            }
        }

        /* renamed from: lambda$renderedFirstFrame$5$VideoRendererEventListener$EventDispatcher */
        public /* synthetic */ void mo3820x44bb7f11(Surface surface) {
            this.listener.onRenderedFirstFrame(surface);
        }

        public void disabled(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            if (this.listener != null) {
                this.handler.post(new C0241x62435c5(this, decoderCounters));
            }
        }

        public /* synthetic */ void lambda$disabled$6$VideoRendererEventListener$EventDispatcher(DecoderCounters decoderCounters) {
            decoderCounters.ensureUpdated();
            this.listener.onVideoDisabled(decoderCounters);
        }
    }

    void onDroppedFrames(int i, long j);

    void onRenderedFirstFrame(Surface surface);

    void onVideoDecoderInitialized(String str, long j, long j2);

    void onVideoDisabled(DecoderCounters decoderCounters);

    void onVideoEnabled(DecoderCounters decoderCounters);

    void onVideoInputFormatChanged(Format format);

    void onVideoSizeChanged(int i, int i2, int i3, float f);
}
