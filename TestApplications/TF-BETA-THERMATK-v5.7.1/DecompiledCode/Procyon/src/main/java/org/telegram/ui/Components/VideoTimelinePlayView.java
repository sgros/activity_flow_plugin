// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import org.telegram.messenger.FileLog;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.annotation.TargetApi;
import android.view.View;

@TargetApi(10)
public class VideoTimelinePlayView extends View
{
    private static final Object sync;
    private float bufferedProgress;
    private AsyncTask<Integer, Integer, Bitmap> currentTask;
    private VideoTimelineViewDelegate delegate;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private int frameHeight;
    private long frameTimeOffset;
    private int frameWidth;
    private ArrayList<Bitmap> frames;
    private int framesToLoad;
    private boolean isRoundFrames;
    private int lastWidth;
    private float maxProgressDiff;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private float minProgressDiff;
    private Paint paint;
    private Paint paint2;
    private float playProgress;
    private float pressDx;
    private boolean pressedLeft;
    private boolean pressedPlay;
    private boolean pressedRight;
    private float progressLeft;
    private float progressRight;
    private Rect rect1;
    private Rect rect2;
    private RectF rect3;
    private long videoLength;
    
    static {
        sync = new Object();
    }
    
    public VideoTimelinePlayView(final Context context) {
        super(context);
        this.progressRight = 1.0f;
        this.playProgress = 0.5f;
        this.bufferedProgress = 0.5f;
        this.frames = new ArrayList<Bitmap>();
        this.maxProgressDiff = 1.0f;
        this.minProgressDiff = 0.0f;
        this.rect3 = new RectF();
        (this.paint = new Paint(1)).setColor(-1);
        (this.paint2 = new Paint()).setColor(2130706432);
        (this.drawableLeft = context.getResources().getDrawable(2131165902)).setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
        (this.drawableRight = context.getResources().getDrawable(2131165903)).setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
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
                try {
                    final Bitmap frameAtTime = VideoTimelinePlayView.this.mediaMetadataRetriever.getFrameAtTime(VideoTimelinePlayView.this.frameTimeOffset * this.frameNum * 1000L, 2);
                    try {
                        if (this.isCancelled()) {
                            return null;
                        }
                        if ((bitmap = frameAtTime) != null) {
                            bitmap = Bitmap.createBitmap(VideoTimelinePlayView.this.frameWidth, VideoTimelinePlayView.this.frameHeight, frameAtTime.getConfig());
                            final Canvas canvas = new Canvas(bitmap);
                            float n = VideoTimelinePlayView.this.frameWidth / (float)frameAtTime.getWidth();
                            final float n2 = VideoTimelinePlayView.this.frameHeight / (float)frameAtTime.getHeight();
                            if (n <= n2) {
                                n = n2;
                            }
                            final int n3 = (int)(frameAtTime.getWidth() * n);
                            final int n4 = (int)(frameAtTime.getHeight() * n);
                            canvas.drawBitmap(frameAtTime, new Rect(0, 0, frameAtTime.getWidth(), frameAtTime.getHeight()), new Rect((VideoTimelinePlayView.this.frameWidth - n3) / 2, (VideoTimelinePlayView.this.frameHeight - n4) / 2, n3, n4), (Paint)null);
                            frameAtTime.recycle();
                            return bitmap;
                        }
                        return bitmap;
                    }
                    catch (Exception ex) {
                        bitmap = frameAtTime;
                    }
                }
                catch (Exception ex) {
                    bitmap = null;
                }
                final Exception ex;
                FileLog.e(ex);
                return bitmap;
            }
            
            protected void onPostExecute(final Bitmap e) {
                if (!this.isCancelled()) {
                    VideoTimelinePlayView.this.frames.add(e);
                    VideoTimelinePlayView.this.invalidate();
                    if (this.frameNum < VideoTimelinePlayView.this.framesToLoad) {
                        VideoTimelinePlayView.this.reloadFrames(this.frameNum + 1);
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
        final Object sync = VideoTimelinePlayView.sync;
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
                    // iftrue(Label_0078:, index >= this.frames.size())
                    // iftrue(Label_0072:, bitmap == null)
                    // monitorexit(sync)
                    while (true) {
                        int index = 0;
                        final Bitmap bitmap = this.frames.get(index);
                        bitmap.recycle();
                        Label_0072: {
                            break Label_0072;
                            Label_0105: {
                                return;
                            }
                            index = 0;
                            continue;
                            final AsyncTask<Integer, Integer, Bitmap> currentTask;
                            currentTask.cancel(true);
                            this.currentTask = null;
                            return;
                        }
                        ++index;
                        continue;
                    }
                    Label_0078: {
                        this.frames.clear();
                    }
                    final AsyncTask<Integer, Integer, Bitmap> currentTask = this.currentTask;
                }
                // iftrue(Label_0105:, currentTask == null)
            }
        }
        catch (Exception ex) {}
    }
    
    public float getLeftProgress() {
        return this.progressLeft;
    }
    
    public float getProgress() {
        return this.playProgress;
    }
    
    public float getRightProgress() {
        return this.progressRight;
    }
    
    public boolean isDragging() {
        return this.pressedPlay;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = this.getMeasuredWidth() - AndroidUtilities.dp(36.0f);
        final float n2 = (float)n;
        final int n3 = (int)(this.progressLeft * n2) + AndroidUtilities.dp(16.0f);
        final int n4 = (int)(this.progressRight * n2) + AndroidUtilities.dp(16.0f);
        canvas.save();
        canvas.clipRect(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(20.0f) + n, AndroidUtilities.dp(48.0f));
        final boolean empty = this.frames.isEmpty();
        int i = 0;
        if (empty && this.currentTask == null) {
            this.reloadFrames(0);
        }
        else {
            int n5 = 0;
            while (i < this.frames.size()) {
                final Bitmap bitmap = this.frames.get(i);
                if (bitmap != null) {
                    final int dp = AndroidUtilities.dp(16.0f);
                    int frameWidth;
                    if (this.isRoundFrames) {
                        frameWidth = this.frameWidth / 2;
                    }
                    else {
                        frameWidth = this.frameWidth;
                    }
                    final int n6 = dp + frameWidth * n5;
                    final int dp2 = AndroidUtilities.dp(6.0f);
                    if (this.isRoundFrames) {
                        this.rect2.set(n6, dp2, n6 + AndroidUtilities.dp(28.0f), dp2 + AndroidUtilities.dp(28.0f));
                        canvas.drawBitmap(bitmap, this.rect1, this.rect2, (Paint)null);
                    }
                    else {
                        canvas.drawBitmap(bitmap, (float)n6, (float)dp2, (Paint)null);
                    }
                }
                ++n5;
                ++i;
            }
        }
        final int dp3 = AndroidUtilities.dp(6.0f);
        final int dp4 = AndroidUtilities.dp(48.0f);
        final float n7 = (float)AndroidUtilities.dp(16.0f);
        final float n8 = (float)dp3;
        final float n9 = (float)n3;
        canvas.drawRect(n7, n8, n9, (float)AndroidUtilities.dp(46.0f), this.paint2);
        canvas.drawRect((float)(AndroidUtilities.dp(4.0f) + n4), n8, (float)(AndroidUtilities.dp(16.0f) + n + AndroidUtilities.dp(4.0f)), (float)AndroidUtilities.dp(46.0f), this.paint2);
        final float n10 = (float)AndroidUtilities.dp(4.0f);
        final float n11 = (float)(AndroidUtilities.dp(2.0f) + n3);
        final float n12 = (float)dp4;
        canvas.drawRect(n9, n10, n11, n12, this.paint);
        canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + n4), (float)AndroidUtilities.dp(4.0f), (float)(AndroidUtilities.dp(4.0f) + n4), n12, this.paint);
        canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + n3), (float)AndroidUtilities.dp(4.0f), (float)(AndroidUtilities.dp(4.0f) + n4), n8, this.paint);
        canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + n3), (float)(dp4 - AndroidUtilities.dp(2.0f)), (float)(AndroidUtilities.dp(4.0f) + n4), n12, this.paint);
        canvas.restore();
        this.rect3.set((float)(n3 - AndroidUtilities.dp(8.0f)), (float)AndroidUtilities.dp(4.0f), (float)(AndroidUtilities.dp(2.0f) + n3), n12);
        canvas.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint);
        this.drawableLeft.setBounds(n3 - AndroidUtilities.dp(8.0f), AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(44.0f) - AndroidUtilities.dp(18.0f)) / 2, n3 + AndroidUtilities.dp(2.0f), (AndroidUtilities.dp(44.0f) - AndroidUtilities.dp(18.0f)) / 2 + AndroidUtilities.dp(22.0f));
        this.drawableLeft.draw(canvas);
        this.rect3.set((float)(AndroidUtilities.dp(2.0f) + n4), (float)AndroidUtilities.dp(4.0f), (float)(AndroidUtilities.dp(12.0f) + n4), n12);
        canvas.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint);
        this.drawableRight.setBounds(AndroidUtilities.dp(2.0f) + n4, AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(44.0f) - AndroidUtilities.dp(18.0f)) / 2, n4 + AndroidUtilities.dp(12.0f), (AndroidUtilities.dp(44.0f) - AndroidUtilities.dp(18.0f)) / 2 + AndroidUtilities.dp(22.0f));
        this.drawableRight.draw(canvas);
        final float n13 = (float)AndroidUtilities.dp(18.0f);
        final float progressLeft = this.progressLeft;
        final float n14 = n13 + n2 * (progressLeft + (this.progressRight - progressLeft) * this.playProgress);
        this.rect3.set(n14 - AndroidUtilities.dp(1.5f), (float)AndroidUtilities.dp(2.0f), AndroidUtilities.dp(1.5f) + n14, (float)AndroidUtilities.dp(50.0f));
        canvas.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(1.0f), this.paint2);
        canvas.drawCircle(n14, (float)AndroidUtilities.dp(52.0f), (float)AndroidUtilities.dp(3.5f), this.paint2);
        this.rect3.set(n14 - AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(2.0f), AndroidUtilities.dp(1.0f) + n14, (float)AndroidUtilities.dp(50.0f));
        canvas.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(1.0f), (float)AndroidUtilities.dp(1.0f), this.paint);
        canvas.drawCircle(n14, (float)AndroidUtilities.dp(52.0f), (float)AndroidUtilities.dp(3.0f), this.paint);
    }
    
    protected void onMeasure(int size, final int n) {
        super.onMeasure(size, n);
        size = View$MeasureSpec.getSize(size);
        if (this.lastWidth != size) {
            this.clearFrames();
            this.lastWidth = size;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final int n = this.getMeasuredWidth() - AndroidUtilities.dp(32.0f);
        final float n2 = (float)n;
        int n3 = (int)(this.progressLeft * n2) + AndroidUtilities.dp(16.0f);
        final float progressLeft = this.progressLeft;
        final int n4 = (int)((progressLeft + (this.progressRight - progressLeft) * this.playProgress) * n2) + AndroidUtilities.dp(16.0f);
        final int n5 = (int)(this.progressRight * n2) + AndroidUtilities.dp(16.0f);
        if (motionEvent.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            if (this.mediaMetadataRetriever == null) {
                return false;
            }
            final int dp = AndroidUtilities.dp(12.0f);
            final int dp2 = AndroidUtilities.dp(8.0f);
            if (n4 - dp2 <= x && x <= dp2 + n4 && y >= 0.0f && y <= this.getMeasuredHeight()) {
                final VideoTimelineViewDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.didStartDragging();
                }
                this.pressedPlay = true;
                this.pressDx = (float)(int)(x - n4);
                this.invalidate();
                return true;
            }
            if (n3 - dp <= x && x <= n3 + dp && y >= 0.0f && y <= this.getMeasuredHeight()) {
                final VideoTimelineViewDelegate delegate2 = this.delegate;
                if (delegate2 != null) {
                    delegate2.didStartDragging();
                }
                this.pressedLeft = true;
                this.pressDx = (float)(int)(x - n3);
                this.invalidate();
                return true;
            }
            if (n5 - dp <= x && x <= dp + n5 && y >= 0.0f && y <= this.getMeasuredHeight()) {
                final VideoTimelineViewDelegate delegate3 = this.delegate;
                if (delegate3 != null) {
                    delegate3.didStartDragging();
                }
                this.pressedRight = true;
                this.pressDx = (float)(int)(x - n5);
                this.invalidate();
                return true;
            }
        }
        else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
            if (motionEvent.getAction() == 2) {
                if (this.pressedPlay) {
                    this.playProgress = ((int)(x - this.pressDx) - AndroidUtilities.dp(16.0f)) / n2;
                    final float playProgress = this.playProgress;
                    final float progressLeft2 = this.progressLeft;
                    if (playProgress < progressLeft2) {
                        this.playProgress = progressLeft2;
                    }
                    else {
                        final float progressRight = this.progressRight;
                        if (playProgress > progressRight) {
                            this.playProgress = progressRight;
                        }
                    }
                    final float playProgress2 = this.playProgress;
                    final float progressLeft3 = this.progressLeft;
                    final float progressRight2 = this.progressRight;
                    this.playProgress = (playProgress2 - progressLeft3) / (progressRight2 - progressLeft3);
                    final VideoTimelineViewDelegate delegate4 = this.delegate;
                    if (delegate4 != null) {
                        delegate4.onPlayProgressChanged(progressLeft3 + (progressRight2 - progressLeft3) * this.playProgress);
                    }
                    this.invalidate();
                    return true;
                }
                if (this.pressedLeft) {
                    int dp3 = (int)(x - this.pressDx);
                    if (dp3 < AndroidUtilities.dp(16.0f)) {
                        dp3 = AndroidUtilities.dp(16.0f);
                    }
                    else if (dp3 > n5) {
                        dp3 = n5;
                    }
                    this.progressLeft = (dp3 - AndroidUtilities.dp(16.0f)) / n2;
                    final float progressRight3 = this.progressRight;
                    final float progressLeft4 = this.progressLeft;
                    final float maxProgressDiff = this.maxProgressDiff;
                    if (progressRight3 - progressLeft4 > maxProgressDiff) {
                        this.progressRight = progressLeft4 + maxProgressDiff;
                    }
                    else {
                        final float minProgressDiff = this.minProgressDiff;
                        if (minProgressDiff != 0.0f && progressRight3 - progressLeft4 < minProgressDiff) {
                            this.progressLeft = progressRight3 - minProgressDiff;
                            if (this.progressLeft < 0.0f) {
                                this.progressLeft = 0.0f;
                            }
                        }
                    }
                    final VideoTimelineViewDelegate delegate5 = this.delegate;
                    if (delegate5 != null) {
                        delegate5.onLeftProgressChanged(this.progressLeft);
                    }
                    this.invalidate();
                    return true;
                }
                if (this.pressedRight) {
                    final int n6 = (int)(x - this.pressDx);
                    if (n6 >= n3) {
                        if (n6 > AndroidUtilities.dp(16.0f) + n) {
                            n3 = n + AndroidUtilities.dp(16.0f);
                        }
                        else {
                            n3 = n6;
                        }
                    }
                    this.progressRight = (n3 - AndroidUtilities.dp(16.0f)) / n2;
                    final float progressRight4 = this.progressRight;
                    final float progressLeft5 = this.progressLeft;
                    final float maxProgressDiff2 = this.maxProgressDiff;
                    if (progressRight4 - progressLeft5 > maxProgressDiff2) {
                        this.progressLeft = progressRight4 - maxProgressDiff2;
                    }
                    else {
                        final float minProgressDiff2 = this.minProgressDiff;
                        if (minProgressDiff2 != 0.0f && progressRight4 - progressLeft5 < minProgressDiff2) {
                            this.progressRight = progressLeft5 + minProgressDiff2;
                            if (this.progressRight > 1.0f) {
                                this.progressRight = 1.0f;
                            }
                        }
                    }
                    final VideoTimelineViewDelegate delegate6 = this.delegate;
                    if (delegate6 != null) {
                        delegate6.onRightProgressChanged(this.progressRight);
                    }
                    this.invalidate();
                    return true;
                }
            }
        }
        else {
            if (this.pressedLeft) {
                final VideoTimelineViewDelegate delegate7 = this.delegate;
                if (delegate7 != null) {
                    delegate7.didStopDragging();
                }
                this.pressedLeft = false;
                return true;
            }
            if (this.pressedRight) {
                final VideoTimelineViewDelegate delegate8 = this.delegate;
                if (delegate8 != null) {
                    delegate8.didStopDragging();
                }
                this.pressedRight = false;
                return true;
            }
            if (this.pressedPlay) {
                final VideoTimelineViewDelegate delegate9 = this.delegate;
                if (delegate9 != null) {
                    delegate9.didStopDragging();
                }
                this.pressedPlay = false;
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
    
    public void setMaxProgressDiff(float progressLeft) {
        this.maxProgressDiff = progressLeft;
        final float progressRight = this.progressRight;
        progressLeft = this.progressLeft;
        final float maxProgressDiff = this.maxProgressDiff;
        if (progressRight - progressLeft > maxProgressDiff) {
            this.progressRight = progressLeft + maxProgressDiff;
            this.invalidate();
        }
    }
    
    public void setMinProgressDiff(final float minProgressDiff) {
        this.minProgressDiff = minProgressDiff;
    }
    
    public void setProgress(final float playProgress) {
        this.playProgress = playProgress;
        this.invalidate();
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
        
        void onPlayProgressChanged(final float p0);
        
        void onRightProgressChanged(final float p0);
    }
}
