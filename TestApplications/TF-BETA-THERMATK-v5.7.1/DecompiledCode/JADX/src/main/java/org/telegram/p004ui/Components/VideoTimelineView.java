package org.telegram.p004ui.Components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.p004ui.ActionBar.Theme;

@TargetApi(10)
/* renamed from: org.telegram.ui.Components.VideoTimelineView */
public class VideoTimelineView extends View {
    private static final Object sync = new Object();
    private AsyncTask<Integer, Integer, Bitmap> currentTask;
    private VideoTimelineViewDelegate delegate;
    private int frameHeight;
    private long frameTimeOffset;
    private int frameWidth;
    private ArrayList<Bitmap> frames = new ArrayList();
    private int framesToLoad;
    private boolean isRoundFrames;
    private float maxProgressDiff = 1.0f;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private float minProgressDiff = 0.0f;
    private Paint paint = new Paint(1);
    private Paint paint2;
    private float pressDx;
    private boolean pressedLeft;
    private boolean pressedRight;
    private float progressLeft;
    private float progressRight = 1.0f;
    private Rect rect1;
    private Rect rect2;
    private long videoLength;

    /* renamed from: org.telegram.ui.Components.VideoTimelineView$1 */
    class C29801 extends AsyncTask<Integer, Integer, Bitmap> {
        private int frameNum = 0;

        C29801() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Bitmap doInBackground(Integer... numArr) {
            Throwable e;
            this.frameNum = numArr[0].intValue();
            if (isCancelled()) {
                return null;
            }
            Bitmap frameAtTime;
            try {
                frameAtTime = VideoTimelineView.this.mediaMetadataRetriever.getFrameAtTime((VideoTimelineView.this.frameTimeOffset * ((long) this.frameNum)) * 1000, 2);
                try {
                    if (isCancelled()) {
                        return null;
                    }
                    if (frameAtTime != null) {
                        Bitmap createBitmap = Bitmap.createBitmap(VideoTimelineView.this.frameWidth, VideoTimelineView.this.frameHeight, frameAtTime.getConfig());
                        Canvas canvas = new Canvas(createBitmap);
                        float access$200 = ((float) VideoTimelineView.this.frameWidth) / ((float) frameAtTime.getWidth());
                        float access$300 = ((float) VideoTimelineView.this.frameHeight) / ((float) frameAtTime.getHeight());
                        if (access$200 <= access$300) {
                            access$200 = access$300;
                        }
                        int width = (int) (((float) frameAtTime.getWidth()) * access$200);
                        int height = (int) (((float) frameAtTime.getHeight()) * access$200);
                        canvas.drawBitmap(frameAtTime, new Rect(0, 0, frameAtTime.getWidth(), frameAtTime.getHeight()), new Rect((VideoTimelineView.this.frameWidth - width) / 2, (VideoTimelineView.this.frameHeight - height) / 2, width, height), null);
                        frameAtTime.recycle();
                        frameAtTime = createBitmap;
                    }
                    return frameAtTime;
                } catch (Exception e2) {
                    e = e2;
                    FileLog.m30e(e);
                    return frameAtTime;
                }
            } catch (Exception e3) {
                e = e3;
                frameAtTime = null;
                FileLog.m30e(e);
                return frameAtTime;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Bitmap bitmap) {
            if (!isCancelled()) {
                VideoTimelineView.this.frames.add(bitmap);
                VideoTimelineView.this.invalidate();
                if (this.frameNum < VideoTimelineView.this.framesToLoad) {
                    VideoTimelineView.this.reloadFrames(this.frameNum + 1);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.VideoTimelineView$VideoTimelineViewDelegate */
    public interface VideoTimelineViewDelegate {
        void didStartDragging();

        void didStopDragging();

        void onLeftProgressChanged(float f);

        void onRightProgressChanged(float f);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:28:0x0042 in {7, 12, 19, 20, 23, 24, 27} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void destroy() {
        /*
        r3 = this;
        r0 = sync;
        monitor-enter(r0);
        r1 = 0;
        r2 = r3.mediaMetadataRetriever;	 Catch:{ Exception -> 0x0012 }
        if (r2 == 0) goto L_0x0016;	 Catch:{ Exception -> 0x0012 }
        r2 = r3.mediaMetadataRetriever;	 Catch:{ Exception -> 0x0012 }
        r2.release();	 Catch:{ Exception -> 0x0012 }
        r3.mediaMetadataRetriever = r1;	 Catch:{ Exception -> 0x0012 }
        goto L_0x0016;
        r1 = move-exception;
        goto L_0x0040;
        r2 = move-exception;
        org.telegram.messenger.FileLog.m30e(r2);	 Catch:{ all -> 0x0010 }
        monitor-exit(r0);	 Catch:{ all -> 0x0010 }
        r0 = 0;
        r2 = r3.frames;
        r2 = r2.size();
        if (r0 >= r2) goto L_0x0030;
        r2 = r3.frames;
        r2 = r2.get(r0);
        r2 = (android.graphics.Bitmap) r2;
        if (r2 == 0) goto L_0x002d;
        r2.recycle();
        r0 = r0 + 1;
        goto L_0x0018;
        r0 = r3.frames;
        r0.clear();
        r0 = r3.currentTask;
        if (r0 == 0) goto L_0x003f;
        r2 = 1;
        r0.cancel(r2);
        r3.currentTask = r1;
        return;
        monitor-exit(r0);	 Catch:{ all -> 0x0010 }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.VideoTimelineView.destroy():void");
    }

    public VideoTimelineView(Context context) {
        super(context);
        this.paint.setColor(-1);
        this.paint2 = new Paint();
        this.paint2.setColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
    }

    public float getLeftProgress() {
        return this.progressLeft;
    }

    public float getRightProgress() {
        return this.progressRight;
    }

    public void setMinProgressDiff(float f) {
        this.minProgressDiff = f;
    }

    public void setMaxProgressDiff(float f) {
        this.maxProgressDiff = f;
        f = this.progressRight;
        float f2 = this.progressLeft;
        f -= f2;
        float f3 = this.maxProgressDiff;
        if (f > f3) {
            this.progressRight = f2 + f3;
            invalidate();
        }
    }

    public void setRoundFrames(boolean z) {
        this.isRoundFrames = z;
        if (this.isRoundFrames) {
            this.rect1 = new Rect(AndroidUtilities.m26dp(14.0f), AndroidUtilities.m26dp(14.0f), AndroidUtilities.m26dp(42.0f), AndroidUtilities.m26dp(42.0f));
            this.rect2 = new Rect();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int measuredWidth = getMeasuredWidth() - AndroidUtilities.m26dp(32.0f);
        float f = (float) measuredWidth;
        int dp = ((int) (this.progressLeft * f)) + AndroidUtilities.m26dp(16.0f);
        int dp2 = ((int) (this.progressRight * f)) + AndroidUtilities.m26dp(16.0f);
        int dp3;
        VideoTimelineViewDelegate videoTimelineViewDelegate;
        if (motionEvent.getAction() == 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (this.mediaMetadataRetriever == null) {
                return false;
            }
            dp3 = AndroidUtilities.m26dp(12.0f);
            if (((float) (dp - dp3)) <= x && x <= ((float) (dp + dp3)) && y >= 0.0f && y <= ((float) getMeasuredHeight())) {
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.didStartDragging();
                }
                this.pressedLeft = true;
                this.pressDx = (float) ((int) (x - ((float) dp)));
                invalidate();
                return true;
            } else if (((float) (dp2 - dp3)) <= x && x <= ((float) (dp3 + dp2)) && y >= 0.0f && y <= ((float) getMeasuredHeight())) {
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.didStartDragging();
                }
                this.pressedRight = true;
                this.pressDx = (float) ((int) (x - ((float) dp2)));
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (this.pressedLeft) {
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.didStopDragging();
                }
                this.pressedLeft = false;
                return true;
            } else if (this.pressedRight) {
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.didStopDragging();
                }
                this.pressedRight = false;
                return true;
            }
        } else if (motionEvent.getAction() == 2) {
            float f2;
            float f3;
            if (this.pressedLeft) {
                dp3 = (int) (x - this.pressDx);
                if (dp3 < AndroidUtilities.m26dp(16.0f)) {
                    dp2 = AndroidUtilities.m26dp(16.0f);
                } else if (dp3 <= dp2) {
                    dp2 = dp3;
                }
                this.progressLeft = ((float) (dp2 - AndroidUtilities.m26dp(16.0f))) / f;
                f2 = this.progressRight;
                f3 = this.progressLeft;
                x = f2 - f3;
                y = this.maxProgressDiff;
                if (x > y) {
                    this.progressRight = f3 + y;
                } else {
                    x = this.minProgressDiff;
                    if (x != 0.0f && f2 - f3 < x) {
                        this.progressLeft = f2 - x;
                        if (this.progressLeft < 0.0f) {
                            this.progressLeft = 0.0f;
                        }
                    }
                }
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.onLeftProgressChanged(this.progressLeft);
                }
                invalidate();
                return true;
            } else if (this.pressedRight) {
                dp3 = (int) (x - this.pressDx);
                if (dp3 >= dp) {
                    dp = dp3 > AndroidUtilities.m26dp(16.0f) + measuredWidth ? measuredWidth + AndroidUtilities.m26dp(16.0f) : dp3;
                }
                this.progressRight = ((float) (dp - AndroidUtilities.m26dp(16.0f))) / f;
                f2 = this.progressRight;
                f3 = this.progressLeft;
                x = f2 - f3;
                y = this.maxProgressDiff;
                if (x > y) {
                    this.progressLeft = f2 - y;
                } else {
                    x = this.minProgressDiff;
                    if (x != 0.0f && f2 - f3 < x) {
                        this.progressRight = f3 + x;
                        if (this.progressRight > 1.0f) {
                            this.progressRight = 1.0f;
                        }
                    }
                }
                videoTimelineViewDelegate = this.delegate;
                if (videoTimelineViewDelegate != null) {
                    videoTimelineViewDelegate.onRightProgressChanged(this.progressRight);
                }
                invalidate();
                return true;
            }
        }
        return false;
    }

    public void setColor(int i) {
        this.paint.setColor(i);
    }

    public void setVideoPath(String str) {
        destroy();
        this.mediaMetadataRetriever = new MediaMetadataRetriever();
        this.progressLeft = 0.0f;
        this.progressRight = 1.0f;
        try {
            this.mediaMetadataRetriever.setDataSource(str);
            this.videoLength = Long.parseLong(this.mediaMetadataRetriever.extractMetadata(9));
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        invalidate();
    }

    public void setDelegate(VideoTimelineViewDelegate videoTimelineViewDelegate) {
        this.delegate = videoTimelineViewDelegate;
    }

    private void reloadFrames(int i) {
        if (this.mediaMetadataRetriever != null) {
            if (i == 0) {
                if (this.isRoundFrames) {
                    int dp = AndroidUtilities.m26dp(56.0f);
                    this.frameWidth = dp;
                    this.frameHeight = dp;
                    this.framesToLoad = (int) Math.ceil((double) (((float) (getMeasuredWidth() - AndroidUtilities.m26dp(16.0f))) / (((float) this.frameHeight) / 2.0f)));
                } else {
                    this.frameHeight = AndroidUtilities.m26dp(40.0f);
                    this.framesToLoad = (getMeasuredWidth() - AndroidUtilities.m26dp(16.0f)) / this.frameHeight;
                    this.frameWidth = (int) Math.ceil((double) (((float) (getMeasuredWidth() - AndroidUtilities.m26dp(16.0f))) / ((float) this.framesToLoad)));
                }
                this.frameTimeOffset = this.videoLength / ((long) this.framesToLoad);
            }
            this.currentTask = new C29801();
            this.currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[]{Integer.valueOf(i), null, null});
        }
    }

    public void clearFrames() {
        for (int i = 0; i < this.frames.size(); i++) {
            Bitmap bitmap = (Bitmap) this.frames.get(i);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        this.frames.clear();
        AsyncTask asyncTask = this.currentTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.currentTask = null;
        }
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        int measuredWidth = getMeasuredWidth() - AndroidUtilities.m26dp(36.0f);
        float f = (float) measuredWidth;
        int dp = ((int) (this.progressLeft * f)) + AndroidUtilities.m26dp(16.0f);
        int dp2 = ((int) (f * this.progressRight)) + AndroidUtilities.m26dp(16.0f);
        canvas.save();
        int i = 0;
        canvas2.clipRect(AndroidUtilities.m26dp(16.0f), 0, AndroidUtilities.m26dp(20.0f) + measuredWidth, getMeasuredHeight());
        if (this.frames.isEmpty() && this.currentTask == null) {
            reloadFrames(0);
        } else {
            int i2 = 0;
            while (i < this.frames.size()) {
                Bitmap bitmap = (Bitmap) this.frames.get(i);
                if (bitmap != null) {
                    int dp3 = AndroidUtilities.m26dp(16.0f) + ((this.isRoundFrames ? this.frameWidth / 2 : this.frameWidth) * i2);
                    int dp4 = AndroidUtilities.m26dp(2.0f);
                    if (this.isRoundFrames) {
                        this.rect2.set(dp3, dp4, AndroidUtilities.m26dp(28.0f) + dp3, AndroidUtilities.m26dp(28.0f) + dp4);
                        canvas2.drawBitmap(bitmap, this.rect1, this.rect2, null);
                    } else {
                        canvas2.drawBitmap(bitmap, (float) dp3, (float) dp4, null);
                    }
                }
                i2++;
                i++;
            }
        }
        int dp5 = AndroidUtilities.m26dp(2.0f);
        float f2 = (float) dp5;
        float f3 = (float) dp;
        canvas.drawRect((float) AndroidUtilities.m26dp(16.0f), f2, f3, (float) (getMeasuredHeight() - dp5), this.paint2);
        canvas.drawRect((float) (AndroidUtilities.m26dp(4.0f) + dp2), f2, (float) ((AndroidUtilities.m26dp(16.0f) + measuredWidth) + AndroidUtilities.m26dp(4.0f)), (float) (getMeasuredHeight() - dp5), this.paint2);
        canvas.drawRect(f3, 0.0f, (float) (AndroidUtilities.m26dp(2.0f) + dp), (float) getMeasuredHeight(), this.paint);
        canvas.drawRect((float) (AndroidUtilities.m26dp(2.0f) + dp2), 0.0f, (float) (AndroidUtilities.m26dp(4.0f) + dp2), (float) getMeasuredHeight(), this.paint);
        canvas.drawRect((float) (AndroidUtilities.m26dp(2.0f) + dp), 0.0f, (float) (AndroidUtilities.m26dp(4.0f) + dp2), f2, this.paint);
        canvas.drawRect((float) (dp + AndroidUtilities.m26dp(2.0f)), (float) (getMeasuredHeight() - dp5), (float) (AndroidUtilities.m26dp(4.0f) + dp2), (float) getMeasuredHeight(), this.paint);
        canvas.restore();
        canvas2.drawCircle(f3, (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.m26dp(7.0f), this.paint);
        canvas2.drawCircle((float) (dp2 + AndroidUtilities.m26dp(4.0f)), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.m26dp(7.0f), this.paint);
    }
}
