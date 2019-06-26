// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import org.telegram.messenger.DispatchQueue;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import java.util.Iterator;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.UUID;
import android.opengl.GLES20;
import java.nio.ByteOrder;
import org.telegram.ui.Components.Size;
import java.util.Map;
import java.nio.ByteBuffer;
import android.graphics.RectF;

public class Painting
{
    private Path activePath;
    private RectF activeStrokeBounds;
    private Slice backupSlice;
    private Texture bitmapTexture;
    private Brush brush;
    private Texture brushTexture;
    private int[] buffers;
    private ByteBuffer dataBuffer;
    private PaintingDelegate delegate;
    private int paintTexture;
    private boolean paused;
    private float[] projection;
    private float[] renderProjection;
    private RenderState renderState;
    private RenderView renderView;
    private int reusableFramebuffer;
    private Map<String, Shader> shaders;
    private Size size;
    private int suppressChangesCounter;
    private ByteBuffer textureBuffer;
    private ByteBuffer vertexBuffer;
    
    public Painting(Size size) {
        this.buffers = new int[1];
        this.renderState = new RenderState();
        this.size = size;
        size = this.size;
        this.dataBuffer = ByteBuffer.allocateDirect((int)size.width * (int)size.height * 4);
        size = this.size;
        this.projection = GLMatrix.LoadOrtho(0.0f, size.width, 0.0f, size.height, -1.0f, 1.0f);
        if (this.vertexBuffer == null) {
            (this.vertexBuffer = ByteBuffer.allocateDirect(32)).order(ByteOrder.nativeOrder());
        }
        this.vertexBuffer.putFloat(0.0f);
        this.vertexBuffer.putFloat(0.0f);
        this.vertexBuffer.putFloat(this.size.width);
        this.vertexBuffer.putFloat(0.0f);
        this.vertexBuffer.putFloat(0.0f);
        this.vertexBuffer.putFloat(this.size.height);
        this.vertexBuffer.putFloat(this.size.width);
        this.vertexBuffer.putFloat(this.size.height);
        this.vertexBuffer.rewind();
        if (this.textureBuffer == null) {
            (this.textureBuffer = ByteBuffer.allocateDirect(32)).order(ByteOrder.nativeOrder());
            this.textureBuffer.putFloat(0.0f);
            this.textureBuffer.putFloat(0.0f);
            this.textureBuffer.putFloat(1.0f);
            this.textureBuffer.putFloat(0.0f);
            this.textureBuffer.putFloat(0.0f);
            this.textureBuffer.putFloat(1.0f);
            this.textureBuffer.putFloat(1.0f);
            this.textureBuffer.putFloat(1.0f);
            this.textureBuffer.rewind();
        }
    }
    
    private void beginSuppressingChanges() {
        ++this.suppressChangesCounter;
    }
    
    private void endSuppressingChanges() {
        --this.suppressChangesCounter;
    }
    
    private int getPaintTexture() {
        if (this.paintTexture == 0) {
            this.paintTexture = Texture.generateTexture(this.size);
        }
        return this.paintTexture;
    }
    
    private int getReusableFramebuffer() {
        if (this.reusableFramebuffer == 0) {
            final int[] array = { 0 };
            GLES20.glGenFramebuffers(1, array, 0);
            this.reusableFramebuffer = array[0];
            Utils.HasGLError();
        }
        return this.reusableFramebuffer;
    }
    
    private int getTexture() {
        final Texture bitmapTexture = this.bitmapTexture;
        if (bitmapTexture != null) {
            return bitmapTexture.texture();
        }
        return 0;
    }
    
    private boolean isSuppressingChanges() {
        return this.suppressChangesCounter > 0;
    }
    
    private void registerUndo(final RectF rectF) {
        if (rectF == null) {
            return;
        }
        if (!rectF.setIntersect(rectF, this.getBounds())) {
            return;
        }
        this.delegate.requestUndoStore().registerUndo(UUID.randomUUID(), new Runnable() {
            final /* synthetic */ Slice val$slice = new Slice(Painting.this.getPaintingData(rectF, true).data, rectF, Painting.this.delegate.requestDispatchQueue());
            
            @Override
            public void run() {
                Painting.this.restoreSlice(this.val$slice);
            }
        });
    }
    
    private void render(final int n, final int n2) {
        final Map<String, Shader> shaders = this.shaders;
        String s;
        if (this.brush.isLightSaber()) {
            s = "blitWithMaskLight";
        }
        else {
            s = "blitWithMask";
        }
        final Shader shader = shaders.get(s);
        if (shader == null) {
            return;
        }
        GLES20.glUseProgram(shader.program);
        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(this.renderProjection));
        GLES20.glUniform1i(shader.getUniform("texture"), 0);
        GLES20.glUniform1i(shader.getUniform("mask"), 1);
        Shader.SetColorUniform(shader.getUniform("color"), n2);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.getTexture());
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(3553, n);
        GLES20.glBlendFunc(1, 771);
        GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, (Buffer)this.vertexBuffer);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, (Buffer)this.textureBuffer);
        GLES20.glEnableVertexAttribArray(1);
        GLES20.glDrawArrays(5, 0, 4);
        Utils.HasGLError();
    }
    
    private void renderBlit() {
        final Shader shader = this.shaders.get("blit");
        if (shader == null) {
            return;
        }
        GLES20.glUseProgram(shader.program);
        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(this.renderProjection));
        GLES20.glUniform1i(shader.getUniform("texture"), 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.getTexture());
        GLES20.glBlendFunc(1, 771);
        GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, (Buffer)this.vertexBuffer);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, (Buffer)this.textureBuffer);
        GLES20.glEnableVertexAttribArray(1);
        GLES20.glDrawArrays(5, 0, 4);
        Utils.HasGLError();
    }
    
    private void restoreSlice(final Slice slice) {
        this.renderView.performInContext(new Runnable() {
            @Override
            public void run() {
                final ByteBuffer data = slice.getData();
                GLES20.glBindTexture(3553, Painting.this.getTexture());
                GLES20.glTexSubImage2D(3553, 0, slice.getX(), slice.getY(), slice.getWidth(), slice.getHeight(), 6408, 5121, (Buffer)data);
                if (!Painting.this.isSuppressingChanges() && Painting.this.delegate != null) {
                    Painting.this.delegate.contentChanged(slice.getBounds());
                }
                slice.cleanResources();
            }
        });
    }
    
    private void update(final RectF rectF, final Runnable runnable) {
        GLES20.glBindFramebuffer(36160, this.getReusableFramebuffer());
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.getTexture(), 0);
        if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
            final Size size = this.size;
            GLES20.glViewport(0, 0, (int)size.width, (int)size.height);
            runnable.run();
        }
        GLES20.glBindFramebuffer(36160, 0);
        if (!this.isSuppressingChanges()) {
            final PaintingDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.contentChanged(rectF);
            }
        }
    }
    
    public void cleanResources(final boolean b) {
        final int reusableFramebuffer = this.reusableFramebuffer;
        if (reusableFramebuffer != 0) {
            final int[] buffers = this.buffers;
            buffers[0] = reusableFramebuffer;
            GLES20.glDeleteFramebuffers(1, buffers, 0);
            this.reusableFramebuffer = 0;
        }
        this.bitmapTexture.cleanResources(b);
        final int paintTexture = this.paintTexture;
        if (paintTexture != 0) {
            final int[] buffers2 = this.buffers;
            buffers2[0] = paintTexture;
            GLES20.glDeleteTextures(1, buffers2, 0);
            this.paintTexture = 0;
        }
        final Texture brushTexture = this.brushTexture;
        if (brushTexture != null) {
            brushTexture.cleanResources(true);
            this.brushTexture = null;
        }
        final Map<String, Shader> shaders = this.shaders;
        if (shaders != null) {
            final Iterator<Shader> iterator = shaders.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().cleanResources();
            }
            this.shaders = null;
        }
    }
    
    public void commitStroke(final int n) {
        this.renderView.performInContext(new Runnable() {
            @Override
            public void run() {
                final Painting this$0 = Painting.this;
                this$0.registerUndo(this$0.activeStrokeBounds);
                Painting.this.beginSuppressingChanges();
                Painting.this.update(null, new Runnable() {
                    @Override
                    public void run() {
                        if (Painting.this.shaders == null) {
                            return;
                        }
                        final Map access$400 = Painting.this.shaders;
                        String s;
                        if (Painting.this.brush.isLightSaber()) {
                            s = "compositeWithMaskLight";
                        }
                        else {
                            s = "compositeWithMask";
                        }
                        final Shader shader = access$400.get(s);
                        if (shader == null) {
                            return;
                        }
                        GLES20.glUseProgram(shader.program);
                        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(Painting.this.projection));
                        GLES20.glUniform1i(shader.getUniform("mask"), 0);
                        Shader.SetColorUniform(shader.getUniform("color"), n);
                        GLES20.glActiveTexture(33984);
                        GLES20.glBindTexture(3553, Painting.this.getPaintTexture());
                        GLES20.glBlendFuncSeparate(770, 771, 770, 1);
                        GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, (Buffer)Painting.this.vertexBuffer);
                        GLES20.glEnableVertexAttribArray(0);
                        GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, (Buffer)Painting.this.textureBuffer);
                        GLES20.glEnableVertexAttribArray(1);
                        GLES20.glDrawArrays(5, 0, 4);
                    }
                });
                Painting.this.endSuppressingChanges();
                Painting.this.renderState.reset();
                Painting.this.activeStrokeBounds = null;
                Painting.this.activePath = null;
            }
        });
    }
    
    public RectF getBounds() {
        final Size size = this.size;
        return new RectF(0.0f, 0.0f, size.width, size.height);
    }
    
    public PaintingData getPaintingData(final RectF rectF, final boolean b) {
        final int n = (int)rectF.left;
        final int n2 = (int)rectF.top;
        final int n3 = (int)rectF.width();
        final int n4 = (int)rectF.height();
        GLES20.glGenFramebuffers(1, this.buffers, 0);
        final int n5 = this.buffers[0];
        GLES20.glBindFramebuffer(36160, n5);
        GLES20.glGenTextures(1, this.buffers, 0);
        final int n6 = this.buffers[0];
        GLES20.glBindTexture(3553, n6);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6408, n3, n4, 0, 6408, 5121, (Buffer)null);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, n6, 0);
        final Size size = this.size;
        GLES20.glViewport(0, 0, (int)size.width, (int)size.height);
        final Map<String, Shader> shaders = this.shaders;
        if (shaders == null) {
            return null;
        }
        String s;
        if (b) {
            s = "nonPremultipliedBlit";
        }
        else {
            s = "blit";
        }
        final Shader shader = shaders.get(s);
        if (shader == null) {
            return null;
        }
        GLES20.glUseProgram(shader.program);
        final Matrix matrix = new Matrix();
        matrix.preTranslate((float)(-n), (float)(-n2));
        GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(GLMatrix.MultiplyMat4f(this.projection, GLMatrix.LoadGraphicsMatrix(matrix))));
        if (b) {
            GLES20.glUniform1i(shader.getUniform("texture"), 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.getTexture());
        }
        else {
            GLES20.glUniform1i(shader.getUniform("texture"), 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.bitmapTexture.texture());
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.getTexture());
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(16384);
        GLES20.glBlendFunc(1, 771);
        GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, (Buffer)this.vertexBuffer);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, (Buffer)this.textureBuffer);
        GLES20.glEnableVertexAttribArray(1);
        GLES20.glDrawArrays(5, 0, 4);
        this.dataBuffer.limit(n3 * n4 * 4);
        GLES20.glReadPixels(0, 0, n3, n4, 6408, 5121, (Buffer)this.dataBuffer);
        PaintingData paintingData;
        if (b) {
            paintingData = new PaintingData(null, this.dataBuffer);
        }
        else {
            final Bitmap bitmap = Bitmap.createBitmap(n3, n4, Bitmap$Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer((Buffer)this.dataBuffer);
            paintingData = new PaintingData(bitmap, null);
        }
        final int[] buffers = this.buffers;
        buffers[0] = n5;
        GLES20.glDeleteFramebuffers(1, buffers, 0);
        final int[] buffers2 = this.buffers;
        buffers2[0] = n6;
        GLES20.glDeleteTextures(1, buffers2, 0);
        return paintingData;
    }
    
    public Size getSize() {
        return this.size;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public void onPause(final Runnable runnable) {
        this.renderView.performInContext(new Runnable() {
            @Override
            public void run() {
                Painting.this.paused = true;
                final Painting this$0 = Painting.this;
                final PaintingData paintingData = this$0.getPaintingData(this$0.getBounds(), true);
                final Painting this$2 = Painting.this;
                this$2.backupSlice = new Slice(paintingData.data, this$2.getBounds(), Painting.this.delegate.requestDispatchQueue());
                Painting.this.cleanResources(false);
                final Runnable val$completionRunnable = runnable;
                if (val$completionRunnable != null) {
                    val$completionRunnable.run();
                }
            }
        });
    }
    
    public void onResume() {
        this.restoreSlice(this.backupSlice);
        this.backupSlice = null;
        this.paused = false;
    }
    
    public void paintStroke(final Path path, final boolean b, final Runnable runnable) {
        this.renderView.performInContext(new Runnable() {
            @Override
            public void run() {
                Painting.this.activePath = path;
                GLES20.glBindFramebuffer(36160, Painting.this.getReusableFramebuffer());
                GLES20.glFramebufferTexture2D(36160, 36064, 3553, Painting.this.getPaintTexture(), 0);
                Utils.HasGLError();
                RectF renderPath;
                if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
                    GLES20.glViewport(0, 0, (int)Painting.this.size.width, (int)Painting.this.size.height);
                    if (b) {
                        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                        GLES20.glClear(16384);
                    }
                    if (Painting.this.shaders == null) {
                        return;
                    }
                    final Map access$400 = Painting.this.shaders;
                    String s;
                    if (Painting.this.brush.isLightSaber()) {
                        s = "brushLight";
                    }
                    else {
                        s = "brush";
                    }
                    final Shader shader = access$400.get(s);
                    if (shader == null) {
                        return;
                    }
                    GLES20.glUseProgram(shader.program);
                    if (Painting.this.brushTexture == null) {
                        final Painting this$0 = Painting.this;
                        this$0.brushTexture = new Texture(this$0.brush.getStamp());
                    }
                    GLES20.glActiveTexture(33984);
                    GLES20.glBindTexture(3553, Painting.this.brushTexture.texture());
                    GLES20.glUniformMatrix4fv(shader.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(Painting.this.projection));
                    GLES20.glUniform1i(shader.getUniform("texture"), 0);
                    renderPath = Render.RenderPath(path, Painting.this.renderState);
                }
                else {
                    renderPath = null;
                }
                GLES20.glBindFramebuffer(36160, 0);
                if (Painting.this.delegate != null) {
                    Painting.this.delegate.contentChanged(renderPath);
                }
                if (Painting.this.activeStrokeBounds != null) {
                    Painting.this.activeStrokeBounds.union(renderPath);
                }
                else {
                    Painting.this.activeStrokeBounds = renderPath;
                }
                final Runnable val$action = runnable;
                if (val$action != null) {
                    val$action.run();
                }
            }
        });
    }
    
    public void render() {
        if (this.shaders == null) {
            return;
        }
        if (this.activePath != null) {
            this.render(this.getPaintTexture(), this.activePath.getColor());
        }
        else {
            this.renderBlit();
        }
    }
    
    public void setBitmap(final Bitmap bitmap) {
        if (this.bitmapTexture != null) {
            return;
        }
        this.bitmapTexture = new Texture(bitmap);
    }
    
    public void setBrush(final Brush brush) {
        this.brush = brush;
        final Texture brushTexture = this.brushTexture;
        if (brushTexture != null) {
            brushTexture.cleanResources(true);
            this.brushTexture = null;
        }
    }
    
    public void setDelegate(final PaintingDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setRenderProjection(final float[] renderProjection) {
        this.renderProjection = renderProjection;
    }
    
    public void setRenderView(final RenderView renderView) {
        this.renderView = renderView;
    }
    
    public void setupShaders() {
        this.shaders = ShaderSet.setup();
    }
    
    public class PaintingData
    {
        public Bitmap bitmap;
        public ByteBuffer data;
        
        PaintingData(final Bitmap bitmap, final ByteBuffer data) {
            this.bitmap = bitmap;
            this.data = data;
        }
    }
    
    public interface PaintingDelegate
    {
        void contentChanged(final RectF p0);
        
        DispatchQueue requestDispatchQueue();
        
        UndoStore requestUndoStore();
        
        void strokeCommited();
    }
}
