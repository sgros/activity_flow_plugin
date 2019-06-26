// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import org.telegram.messenger.FileLog;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.annotation.TargetApi;
import android.view.View;

@TargetApi(10)
public class VideoTimelineView extends View
{
    private static final Object sync;
    private AsyncTask<Integer, Integer, Bitmap> currentTask;
    private VideoTimelineViewDelegate delegate;
    private int frameHeight;
    private long frameTimeOffset;
    private int frameWidth;
    private ArrayList<Bitmap> frames;
    private int framesToLoad;
    private boolean isRoundFrames;
    private float maxProgressDiff;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private float minProgressDiff;
    private Paint paint;
    private Paint paint2;
    private float pressDx;
    private boolean pressedLeft;
    private boolean pressedRight;
    private float progressLeft;
    private float progressRight;
    private Rect rect1;
    private Rect rect2;
    private long videoLength;
    
    static {
        sync = new Object();
    }
    
    public VideoTimelineView(final Context context) {
        super(context);
        this.progressRight = 1.0f;
        this.frames = new ArrayList<Bitmap>();
        this.maxProgressDiff = 1.0f;
        this.minProgressDiff = 0.0f;
        (this.paint = new Paint(1)).setColor(-1);
        (this.paint2 = new Paint()).setColor(2130706432);
    }
    
    private void reloadFrames(final int i) {
        if (this.mediaMetadataRetriever == null) {
            return;
        }
        if (i == 0) {
            if (this.isRoundFrames) {
                final int dp = AndroidUtilities.dp(56.0f);
                this.frameWidth = dp;
                this.frameHeight = dp;
                this.framesToLoad = (int)Math.ceil((this.getMeasuredWidth() - AndroidUtilities.dp(16.0f)) / (this.frameHeight / 2.0f));
            }
            else {
                this.frameHeight = AndroidUtilities.dp(40.0f);
                this.framesToLoad = (this.getMeasuredWidth() - AndroidUtilities.dp(16.0f)) / this.frameHeight;
                this.frameWidth = (int)Math.ceil((this.getMeasuredWidth() - AndroidUtilities.dp(16.0f)) / (float)this.framesToLoad);
            }
            this.frameTimeOffset = this.videoLength / this.framesToLoad;
        }
        (this.currentTask = new AsyncTask<Integer, Integer, Bitmap>() {
            private int frameNum = 0;
            
            protected Bitmap doInBackground(final Integer... array) {
                this.frameNum = array[0];
                if (this.isCancelled()) {
                    return null;
                }
                Bitmap bitmap;
                Exception ex = null;
                try {
                    final Bitmap frameAtTime = VideoTimelineView.this.mediaMetadataRetriever.getFrameAtTime(VideoTimelineView.this.frameTimeOffset * this.frameNum * 1000L, 2);
                    try {
                        if (this.isCancelled()) {
                            return null;
                        }
                        if ((bitmap = frameAtTime) != null) {
                            bitmap = Bitmap.createBitmap(VideoTimelineView.this.frameWidth, VideoTimelineView.this.frameHeight, frameAtTime.getConfig());
                            final Canvas canvas = new Canvas(bitmap);
                            float n = VideoTimelineView.this.frameWidth / (float)frameAtTime.getWidth();
                            final float n2 = VideoTimelineView.this.frameHeight / (float)frameAtTime.getHeight();
                            if (n <= n2) {
                                n = n2;
                            }
                            final int n3 = (int)(frameAtTime.getWidth() * n);
                            final int n4 = (int)(frameAtTime.getHeight() * n);
                            canvas.drawBitmap(frameAtTime, new Rect(0, 0, frameAtTime.getWidth(), frameAtTime.getHeight()), new Rect((VideoTimelineView.this.frameWidth - n3) / 2, (VideoTimelineView.this.frameHeight - n4) / 2, n3, n4), (Paint)null);
                            frameAtTime.recycle();
                            return bitmap;
                        }
                        return bitmap;
                    }
                    catch (Exception ex) {
                        bitmap = frameAtTime;
                    }
                }
                catch (Exception ex2) {
                    bitmap = null;
                    ex = ex2;
                }
                FileLog.e(ex);
                return bitmap;
            }
            
            protected void onPostExecute(final Bitmap e) {
                if (!this.isCancelled()) {
                    VideoTimelineView.this.frames.add(e);
                    VideoTimelineView.this.invalidate();
                    if (this.frameNum < VideoTimelineView.this.framesToLoad) {
                        VideoTimelineView.this.reloadFrames(this.frameNum + 1);
                    }
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Integer[] { i, null, null });
    }
    
    public void clearFrames() {
        for (int i = 0; i < this.frames.size(); ++i) {
            final Bitmap bitmap = this.frames.get(i);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        this.frames.clear();
        final AsyncTask<Integer, Integer, Bitmap> currentTask = this.currentTask;
        if (currentTask != null) {
            currentTask.cancel(true);
            this.currentTask = null;
        }
        this.invalidate();
    }
    
    public void destroy() {
        final Object sync = VideoTimelineView.sync;
        // monitorenter(sync)
        try {
            Label_0037: {
                try {
                    if (this.mediaMetadataRetriever != null) {
                        this.mediaMetadataRetriever.release();
                        this.mediaMetadataRetriever = null;
                    }
                    break Label_0037;
                }
                finally {
                    // monitorexit(sync)
                    // monitorexit(sync)
                    int index = 0;
                    // iftrue(Label_0078:, index >= this.frames.size())
                    // iftrue(Label_0072:, bitmap == null)
                    // iftrue(Label_0105:, currentTask == null)
                    Bitmap bitmap = null;
                    Block_8: {
                        while (true) {
                            while (true) {
                                break Label_0041;
                                Label_0105: {
                                    return;
                                }
                                bitmap = this.frames.get(index);
                                break Block_8;
                                final AsyncTask<Integer, Integer, Bitmap> currentTask;
                                currentTask.cancel(true);
                                this.currentTask = null;
                                return;
                                Label_0072:
                                ++index;
                                continue;
                            }
                            Label_0078: {
                                this.frames.clear();
                            }
                            final AsyncTask<Integer, Integer, Bitmap> currentTask = this.currentTask;
                            continue;
                        }
                    }
                    bitmap.recycle();
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public float getLeftProgress() {
        return this.progressLeft;
    }
    
    public float getRightProgress() {
        return this.progressRight;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = this.getMeasuredWidth() - AndroidUtilities.dp(36.0f);
        final float n2 = (float)n;
        final int n3 = (int)(this.progressLeft * n2) + AndroidUtilities.dp(16.0f);
        final int n4 = (int)(n2 * this.progressRight) + AndroidUtilities.dp(16.0f);
        canvas.save();
        final int dp = AndroidUtilities.dp(16.0f);
        final int dp2 = AndroidUtilities.dp(20.0f);
        final int measuredHeight = this.getMeasuredHeight();
        int i = 0;
        canvas.clipRect(dp, 0, dp2 + n, measuredHeight);
        if (this.frames.isEmpty() && this.currentTask == null) {
            this.reloadFrames(0);
        }
        else {
            int n5 = 0;
            while (i < this.frames.size()) {
                final Bitmap bitmap = this.frames.get(i);
                if (bitmap != null) {
                    final int dp3 = AndroidUtilities.dp(16.0f);
                    int frameWidth;
                    if (this.isRoundFrames) {
                        frameWidth = this.frameWidth / 2;
                    }
                    else {
                        frameWidth = this.frameWidth;
                    }
                    final int n6 = dp3 + frameWidth * n5;
                    final int dp4 = AndroidUtilities.dp(2.0f);
                    if (this.isRoundFrames) {
                        this.rect2.set(n6, dp4, AndroidUtilities.dp(28.0f) + n6, AndroidUtilities.dp(28.0f) + dp4);
                        canvas.drawBitmap(bitmap, this.rect1, this.rect2, (Paint)null);
                    }
                    else {
                        canvas.drawBitmap(bitmap, (float)n6, (float)dp4, (Paint)null);
                    }
                }
                ++n5;
                ++i;
            }
        }
        final int dp5 = AndroidUtilities.dp(2.0f);
        final float n7 = (float)AndroidUtilities.dp(16.0f);
        final float n8 = (float)dp5;
        final float n9 = (float)n3;
        canvas.drawRect(n7, n8, n9, (float)(this.getMeasuredHeight() - dp5), this.paint2);
        canvas.drawRect((float)(AndroidUtilities.dp(4.0f) + n4), n8, (float)(AndroidUtilities.dp(16.0f) + n + AndroidUtilities.dp(4.0f)), (float)(this.getMeasuredHeight() - dp5), this.paint2);
        canvas.drawRect(n9, 0.0f, (float)(AndroidUtilities.dp(2.0f) + n3), (float)this.getMeasuredHeight(), this.paint);
        canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + n4), 0.0f, (float)(AndroidUtilities.dp(4.0f) + n4), (float)this.getMeasuredHeight(), this.paint);
        canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + n3), 0.0f, (float)(AndroidUtilities.dp(4.0f) + n4), n8, this.paint);
        canvas.drawRect((float)(n3 + AndroidUtilities.dp(2.0f)), (float)(this.getMeasuredHeight() - dp5), (float)(AndroidUtilities.dp(4.0f) + n4), (float)this.getMeasuredHeight(), this.paint);
        canvas.restore();
        canvas.drawCircle(n9, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(7.0f), this.paint);
        canvas.drawCircle((float)(n4 + AndroidUtilities.dp(4.0f)), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(7.0f), this.paint);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final int n = this.getMeasuredWidth() - AndroidUtilities.dp(32.0f);
        final float n2 = (float)n;
        final int n3 = (int)(this.progressLeft * n2) + AndroidUtilities.dp(16.0f);
        int dp = (int)(this.progressRight * n2) + AndroidUtilities.dp(16.0f);
        if (motionEvent.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            if (this.mediaMetadataRetriever == null) {
                return false;
            }
            final int dp2 = AndroidUtilities.dp(12.0f);
            if (n3 - dp2 <= x && x <= n3 + dp2 && y >= 0.0f && y <= this.getMeasuredHeight()) {
                final VideoTimelineViewDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.didStartDragging();
                }
                this.pressedLeft = true;
                this.pressDx = (float)(int)(x - n3);
                this.invalidate();
                return true;
            }
            if (dp - dp2 <= x && x <= dp2 + dp && y >= 0.0f && y <= this.getMeasuredHeight()) {
                final VideoTimelineViewDelegate delegate2 = this.delegate;
                if (delegate2 != null) {
                    delegate2.didStartDragging();
                }
                this.pressedRight = true;
                this.pressDx = (float)(int)(x - dp);
                this.invalidate();
                return true;
            }
        }
        else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
            if (motionEvent.getAction() == 2) {
                if (this.pressedLeft) {
                    final int n4 = (int)(x - this.pressDx);
                    if (n4 < AndroidUtilities.dp(16.0f)) {
                        dp = AndroidUtilities.dp(16.0f);
                    }
                    else if (n4 <= dp) {
                        dp = n4;
                    }
                    this.progressLeft = (dp - AndroidUtilities.dp(16.0f)) / n2;
                    final float progressRight = this.progressRight;
                    final float progressLeft = this.progressLeft;
                    final float maxProgressDiff = this.maxProgressDiff;
                    if (progressRight - progressLeft > maxProgressDiff) {
                        this.progressRight = progressLeft + maxProgressDiff;
                    }
                    else {
                        final float minProgressDiff = this.minProgressDiff;
                        if (minProgressDiff != 0.0f && progressRight - progressLeft < minProgressDiff) {
                            this.progressLeft = progressRight - minProgressDiff;
                            if (this.progressLeft < 0.0f) {
                                this.progressLeft = 0.0f;
                            }
                        }
                    }
                    final VideoTimelineViewDelegate delegate3 = this.delegate;
                    if (delegate3 != null) {
                        delegate3.onLeftProgressChanged(this.progressLeft);
                    }
                    this.invalidate();
                    return true;
                }
                if (this.pressedRight) {
                    int n5 = (int)(x - this.pressDx);
                    if (n5 < n3) {
                        n5 = n3;
                    }
                    else if (n5 > AndroidUtilities.dp(16.0f) + n) {
                        n5 = n + AndroidUtilities.dp(16.0f);
                    }
                    this.progressRight = (n5 - AndroidUtilities.dp(16.0f)) / n2;
                    final float progressRight2 = this.progressRight;
                    final float progressLeft2 = this.progressLeft;
                    final float maxProgressDiff2 = this.maxProgressDiff;
                    if (progressRight2 - progressLeft2 > maxProgressDiff2) {
                        this.progressLeft = progressRight2 - maxProgressDiff2;
                    }
                    else {
                        final float minProgressDiff2 = this.minProgressDiff;
                        if (minProgressDiff2 != 0.0f && progressRight2 - progressLeft2 < minProgressDiff2) {
                            this.progressRight = progressLeft2 + minProgressDiff2;
                            if (this.progressRight > 1.0f) {
                                this.progressRight = 1.0f;
                            }
                        }
                    }
                    final VideoTimelineViewDelegate delegate4 = this.delegate;
                    if (delegate4 != null) {
                        delegate4.onRightProgressChanged(this.progressRight);
                    }
                    this.invalidate();
                    return true;
                }
            }
        }
        else {
            if (this.pressedLeft) {
                final VideoTimelineViewDelegate delegate5 = this.delegate;
                if (delegate5 != null) {
                    delegate5.didStopDragging();
                }
                this.pressedLeft = false;
                return true;
            }
            if (this.pressedRight) {
                final VideoTimelineViewDelegate delegate6 = this.delegate;
                if (delegate6 != null) {
                    delegate6.didStopDragging();
                }
                this.pressedRight = false;
                return true;
            }
        }
        return false;
    }
    
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    public void setDelegate(final VideoTimelineViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMaxProgressDiff(float progressRight) {
        this.maxProgressDiff = progressRight;
        progressRight = this.progressRight;
        final float progressLeft = this.progressLeft;
        final float maxProgressDiff = this.maxProgressDiff;
        if (progressRight - progressLeft > maxProgressDiff) {
            this.progressRight = progressLeft + maxProgressDiff;
            this.invalidate();
        }
    }
    
    public void setMinProgressDiff(final float minProgressDiff) {
        this.minProgressDiff = minProgressDiff;
    }
    
    public void setRoundFrames(final boolean isRoundFrames) {
        this.isRoundFrames = isRoundFrames;
        if (this.isRoundFrames) {
            this.rect1 = new Rect(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f), AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f));
            this.rect2 = new Rect();
        }
    }
    
    public void setVideoPath(final String dataSource) {
        this.destroy();
        this.mediaMetadataRetriever = new MediaMetadataRetriever();
        this.progressLeft = 0.0f;
        this.progressRight = 1.0f;
        try {
            this.mediaMetadataRetriever.setDataSource(dataSource);
            this.videoLength = Long.parseLong(this.mediaMetadataRetriever.extractMetadata(9));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.invalidate();
    }
    
    public interface VideoTimelineViewDelegate
    {
        void didStartDragging();
        
        void didStopDragging();
        
        void onLeftProgressChanged(final float p0);
        
        void onRightProgressChanged(final float p0);
    }
}
