package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.spherical.CameraMotionRenderer;
import java.util.ArrayList;

public class DefaultRenderersFactory implements RenderersFactory {
    private final long allowedVideoJoiningTimeMs;
    private final Context context;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final int extensionRendererMode;

    /* Access modifiers changed, original: protected */
    public void buildMiscellaneousRenderers(Context context, Handler handler, int i, ArrayList<Renderer> arrayList) {
    }

    public DefaultRenderersFactory(Context context, int i) {
        this(context, i, 5000);
    }

    public DefaultRenderersFactory(Context context, int i, long j) {
        this.context = context;
        this.extensionRendererMode = i;
        this.allowedVideoJoiningTimeMs = j;
        this.drmSessionManager = null;
    }

    public Renderer[] createRenderers(Handler handler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textOutput, MetadataOutput metadataOutput, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        DrmSessionManager drmSessionManager2 = drmSessionManager == null ? this.drmSessionManager : drmSessionManager;
        ArrayList arrayList = new ArrayList();
        DrmSessionManager drmSessionManager3 = drmSessionManager2;
        buildVideoRenderers(this.context, drmSessionManager3, this.allowedVideoJoiningTimeMs, handler, videoRendererEventListener, this.extensionRendererMode, arrayList);
        buildAudioRenderers(this.context, drmSessionManager3, buildAudioProcessors(), handler, audioRendererEventListener, this.extensionRendererMode, arrayList);
        ArrayList arrayList2 = arrayList;
        buildTextRenderers(this.context, textOutput, handler.getLooper(), this.extensionRendererMode, arrayList2);
        buildMetadataRenderers(this.context, metadataOutput, handler.getLooper(), this.extensionRendererMode, arrayList2);
        buildCameraMotionRenderers(this.context, this.extensionRendererMode, arrayList);
        Handler handler2 = handler;
        buildMiscellaneousRenderers(this.context, handler, this.extensionRendererMode, arrayList);
        return (Renderer[]) arrayList.toArray(new Renderer[0]);
    }

    /* Access modifiers changed, original: protected */
    public void buildVideoRenderers(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, long j, Handler handler, VideoRendererEventListener videoRendererEventListener, int i, ArrayList<Renderer> arrayList) {
        int i2 = i;
        ArrayList<Renderer> arrayList2 = arrayList;
        arrayList2.add(new MediaCodecVideoRenderer(context, MediaCodecSelector.DEFAULT, j, drmSessionManager, false, handler, videoRendererEventListener, 50));
        if (i2 != 0) {
            int size = arrayList.size();
            if (i2 == 2) {
                size--;
            }
            try {
                arrayList2.add(size, (Renderer) Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(new Class[]{Boolean.TYPE, Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE}).newInstance(new Object[]{Boolean.valueOf(true), Long.valueOf(j), handler, videoRendererEventListener, Integer.valueOf(50)}));
                Log.m16i("DefaultRenderersFactory", "Loaded LibvpxVideoRenderer.");
            } catch (ClassNotFoundException unused) {
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating VP9 extension", e);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0068 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x00a3 */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x005e A:{ExcHandler: Exception (r0_2 'e' java.lang.Exception), Splitter:B:7:0x002d} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0099 A:{ExcHandler: Exception (r0_3 'e' java.lang.Exception), Splitter:B:17:0x0068} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:12:0x005e, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x0066, code skipped:
            throw new java.lang.RuntimeException("Error instantiating Opus extension", r0);
     */
    /* JADX WARNING: Missing block: B:22:0x0099, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:24:0x00a1, code skipped:
            throw new java.lang.RuntimeException("Error instantiating FLAC extension", r0);
     */
    public void buildAudioRenderers(android.content.Context r14, com.google.android.exoplayer2.drm.DrmSessionManager<com.google.android.exoplayer2.drm.FrameworkMediaCrypto> r15, com.google.android.exoplayer2.audio.AudioProcessor[] r16, android.os.Handler r17, com.google.android.exoplayer2.audio.AudioRendererEventListener r18, int r19, java.util.ArrayList<com.google.android.exoplayer2.Renderer> r20) {
        /*
        r13 = this;
        r0 = r19;
        r10 = r20;
        r11 = "DefaultRenderersFactory";
        r12 = new com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
        r3 = com.google.android.exoplayer2.mediacodec.MediaCodecSelector.DEFAULT;
        r8 = com.google.android.exoplayer2.audio.AudioCapabilities.getCapabilities(r14);
        r5 = 0;
        r1 = r12;
        r2 = r14;
        r4 = r15;
        r6 = r17;
        r7 = r18;
        r9 = r16;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9);
        r10.add(r12);
        if (r0 != 0) goto L_0x0021;
    L_0x0020:
        return;
    L_0x0021:
        r1 = r20.size();
        r2 = 2;
        if (r0 != r2) goto L_0x002a;
    L_0x0028:
        r1 = r1 + -1;
    L_0x002a:
        r0 = 0;
        r3 = 3;
        r4 = 1;
        r5 = "com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer";
        r5 = java.lang.Class.forName(r5);	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6 = new java.lang.Class[r3];	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r7 = android.os.Handler.class;
        r6[r0] = r7;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r7 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;
        r6[r4] = r7;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r7 = com.google.android.exoplayer2.audio.AudioProcessor[].class;
        r6[r2] = r7;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r5 = r5.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6 = new java.lang.Object[r3];	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6[r0] = r17;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6[r4] = r18;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r5 = r5.newInstance(r6);	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r5 = (com.google.android.exoplayer2.Renderer) r5;	 Catch:{ ClassNotFoundException -> 0x0067, Exception -> 0x005e }
        r6 = r1 + 1;
        r10.add(r1, r5);	 Catch:{ ClassNotFoundException -> 0x0068, Exception -> 0x005e }
        r1 = "Loaded LibopusAudioRenderer.";
        com.google.android.exoplayer2.util.Log.m16i(r11, r1);	 Catch:{ ClassNotFoundException -> 0x0068, Exception -> 0x005e }
        goto L_0x0068;
    L_0x005e:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r2 = "Error instantiating Opus extension";
        r1.<init>(r2, r0);
        throw r1;
    L_0x0067:
        r6 = r1;
    L_0x0068:
        r1 = "com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer";
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5 = new java.lang.Class[r3];	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r7 = android.os.Handler.class;
        r5[r0] = r7;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r7 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;
        r5[r4] = r7;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r7 = com.google.android.exoplayer2.audio.AudioProcessor[].class;
        r5[r2] = r7;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r1 = r1.getConstructor(r5);	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5 = new java.lang.Object[r3];	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5[r0] = r17;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5[r4] = r18;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r1 = r1.newInstance(r5);	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r1 = (com.google.android.exoplayer2.Renderer) r1;	 Catch:{ ClassNotFoundException -> 0x00a2, Exception -> 0x0099 }
        r5 = r6 + 1;
        r10.add(r6, r1);	 Catch:{ ClassNotFoundException -> 0x00a3, Exception -> 0x0099 }
        r1 = "Loaded LibflacAudioRenderer.";
        com.google.android.exoplayer2.util.Log.m16i(r11, r1);	 Catch:{ ClassNotFoundException -> 0x00a3, Exception -> 0x0099 }
        goto L_0x00a3;
    L_0x0099:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r2 = "Error instantiating FLAC extension";
        r1.<init>(r2, r0);
        throw r1;
    L_0x00a2:
        r5 = r6;
    L_0x00a3:
        r1 = "com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer";
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r6 = new java.lang.Class[r3];	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r7 = android.os.Handler.class;
        r6[r0] = r7;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r7 = com.google.android.exoplayer2.audio.AudioRendererEventListener.class;
        r6[r4] = r7;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r7 = com.google.android.exoplayer2.audio.AudioProcessor[].class;
        r6[r2] = r7;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r1 = r1.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r3 = new java.lang.Object[r3];	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r3[r0] = r17;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r3[r4] = r18;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r3[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r0 = r1.newInstance(r3);	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r0 = (com.google.android.exoplayer2.Renderer) r0;	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r10.add(r5, r0);	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        r0 = "Loaded FfmpegAudioRenderer.";
        com.google.android.exoplayer2.util.Log.m16i(r11, r0);	 Catch:{ ClassNotFoundException -> 0x00db, Exception -> 0x00d2 }
        goto L_0x00db;
    L_0x00d2:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r2 = "Error instantiating FFmpeg extension";
        r1.<init>(r2, r0);
        throw r1;
    L_0x00db:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.DefaultRenderersFactory.buildAudioRenderers(android.content.Context, com.google.android.exoplayer2.drm.DrmSessionManager, com.google.android.exoplayer2.audio.AudioProcessor[], android.os.Handler, com.google.android.exoplayer2.audio.AudioRendererEventListener, int, java.util.ArrayList):void");
    }

    /* Access modifiers changed, original: protected */
    public void buildTextRenderers(Context context, TextOutput textOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new TextRenderer(textOutput, looper));
    }

    /* Access modifiers changed, original: protected */
    public void buildMetadataRenderers(Context context, MetadataOutput metadataOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new MetadataRenderer(metadataOutput, looper));
    }

    /* Access modifiers changed, original: protected */
    public void buildCameraMotionRenderers(Context context, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new CameraMotionRenderer());
    }

    /* Access modifiers changed, original: protected */
    public AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }
}
