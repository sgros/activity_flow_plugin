package org.telegram.ui.Components.Paint;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.telegram.messenger.DispatchQueue;
import org.telegram.ui.Components.Size;

public class Painting {
   private Path activePath;
   private RectF activeStrokeBounds;
   private Slice backupSlice;
   private Texture bitmapTexture;
   private Brush brush;
   private Texture brushTexture;
   private int[] buffers = new int[1];
   private ByteBuffer dataBuffer;
   private Painting.PaintingDelegate delegate;
   private int paintTexture;
   private boolean paused;
   private float[] projection;
   private float[] renderProjection;
   private RenderState renderState = new RenderState();
   private RenderView renderView;
   private int reusableFramebuffer;
   private Map shaders;
   private Size size;
   private int suppressChangesCounter;
   private ByteBuffer textureBuffer;
   private ByteBuffer vertexBuffer;

   public Painting(Size var1) {
      this.size = var1;
      var1 = this.size;
      this.dataBuffer = ByteBuffer.allocateDirect((int)var1.width * (int)var1.height * 4);
      var1 = this.size;
      this.projection = GLMatrix.LoadOrtho(0.0F, var1.width, 0.0F, var1.height, -1.0F, 1.0F);
      if (this.vertexBuffer == null) {
         this.vertexBuffer = ByteBuffer.allocateDirect(32);
         this.vertexBuffer.order(ByteOrder.nativeOrder());
      }

      this.vertexBuffer.putFloat(0.0F);
      this.vertexBuffer.putFloat(0.0F);
      this.vertexBuffer.putFloat(this.size.width);
      this.vertexBuffer.putFloat(0.0F);
      this.vertexBuffer.putFloat(0.0F);
      this.vertexBuffer.putFloat(this.size.height);
      this.vertexBuffer.putFloat(this.size.width);
      this.vertexBuffer.putFloat(this.size.height);
      this.vertexBuffer.rewind();
      if (this.textureBuffer == null) {
         this.textureBuffer = ByteBuffer.allocateDirect(32);
         this.textureBuffer.order(ByteOrder.nativeOrder());
         this.textureBuffer.putFloat(0.0F);
         this.textureBuffer.putFloat(0.0F);
         this.textureBuffer.putFloat(1.0F);
         this.textureBuffer.putFloat(0.0F);
         this.textureBuffer.putFloat(0.0F);
         this.textureBuffer.putFloat(1.0F);
         this.textureBuffer.putFloat(1.0F);
         this.textureBuffer.putFloat(1.0F);
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
         int[] var1 = new int[1];
         GLES20.glGenFramebuffers(1, var1, 0);
         this.reusableFramebuffer = var1[0];
         Utils.HasGLError();
      }

      return this.reusableFramebuffer;
   }

   private int getTexture() {
      Texture var1 = this.bitmapTexture;
      return var1 != null ? var1.texture() : 0;
   }

   private boolean isSuppressingChanges() {
      boolean var1;
      if (this.suppressChangesCounter > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void registerUndo(RectF var1) {
      if (var1 != null) {
         if (var1.setIntersect(var1, this.getBounds())) {
            final Slice var2 = new Slice(this.getPaintingData(var1, true).data, var1, this.delegate.requestDispatchQueue());
            this.delegate.requestUndoStore().registerUndo(UUID.randomUUID(), new Runnable() {
               public void run() {
                  Painting.this.restoreSlice(var2);
               }
            });
         }
      }
   }

   private void render(int var1, int var2) {
      Map var3 = this.shaders;
      String var4;
      if (this.brush.isLightSaber()) {
         var4 = "blitWithMaskLight";
      } else {
         var4 = "blitWithMask";
      }

      Shader var5 = (Shader)var3.get(var4);
      if (var5 != null) {
         GLES20.glUseProgram(var5.program);
         GLES20.glUniformMatrix4fv(var5.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(this.renderProjection));
         GLES20.glUniform1i(var5.getUniform("texture"), 0);
         GLES20.glUniform1i(var5.getUniform("mask"), 1);
         Shader.SetColorUniform(var5.getUniform("color"), var2);
         GLES20.glActiveTexture(33984);
         GLES20.glBindTexture(3553, this.getTexture());
         GLES20.glActiveTexture(33985);
         GLES20.glBindTexture(3553, var1);
         GLES20.glBlendFunc(1, 771);
         GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, this.vertexBuffer);
         GLES20.glEnableVertexAttribArray(0);
         GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, this.textureBuffer);
         GLES20.glEnableVertexAttribArray(1);
         GLES20.glDrawArrays(5, 0, 4);
         Utils.HasGLError();
      }
   }

   private void renderBlit() {
      Shader var1 = (Shader)this.shaders.get("blit");
      if (var1 != null) {
         GLES20.glUseProgram(var1.program);
         GLES20.glUniformMatrix4fv(var1.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(this.renderProjection));
         GLES20.glUniform1i(var1.getUniform("texture"), 0);
         GLES20.glActiveTexture(33984);
         GLES20.glBindTexture(3553, this.getTexture());
         GLES20.glBlendFunc(1, 771);
         GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, this.vertexBuffer);
         GLES20.glEnableVertexAttribArray(0);
         GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, this.textureBuffer);
         GLES20.glEnableVertexAttribArray(1);
         GLES20.glDrawArrays(5, 0, 4);
         Utils.HasGLError();
      }
   }

   private void restoreSlice(final Slice var1) {
      this.renderView.performInContext(new Runnable() {
         public void run() {
            ByteBuffer var1x = var1.getData();
            GLES20.glBindTexture(3553, Painting.this.getTexture());
            GLES20.glTexSubImage2D(3553, 0, var1.getX(), var1.getY(), var1.getWidth(), var1.getHeight(), 6408, 5121, var1x);
            if (!Painting.this.isSuppressingChanges() && Painting.this.delegate != null) {
               Painting.this.delegate.contentChanged(var1.getBounds());
            }

            var1.cleanResources();
         }
      });
   }

   private void update(RectF var1, Runnable var2) {
      GLES20.glBindFramebuffer(36160, this.getReusableFramebuffer());
      GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.getTexture(), 0);
      if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
         Size var3 = this.size;
         GLES20.glViewport(0, 0, (int)var3.width, (int)var3.height);
         var2.run();
      }

      GLES20.glBindFramebuffer(36160, 0);
      if (!this.isSuppressingChanges()) {
         Painting.PaintingDelegate var4 = this.delegate;
         if (var4 != null) {
            var4.contentChanged(var1);
         }
      }

   }

   public void cleanResources(boolean var1) {
      int var2 = this.reusableFramebuffer;
      int[] var3;
      if (var2 != 0) {
         var3 = this.buffers;
         var3[0] = var2;
         GLES20.glDeleteFramebuffers(1, var3, 0);
         this.reusableFramebuffer = 0;
      }

      this.bitmapTexture.cleanResources(var1);
      var2 = this.paintTexture;
      if (var2 != 0) {
         var3 = this.buffers;
         var3[0] = var2;
         GLES20.glDeleteTextures(1, var3, 0);
         this.paintTexture = 0;
      }

      Texture var4 = this.brushTexture;
      if (var4 != null) {
         var4.cleanResources(true);
         this.brushTexture = null;
      }

      Map var5 = this.shaders;
      if (var5 != null) {
         Iterator var6 = var5.values().iterator();

         while(var6.hasNext()) {
            ((Shader)var6.next()).cleanResources();
         }

         this.shaders = null;
      }

   }

   public void commitStroke(final int var1) {
      this.renderView.performInContext(new Runnable() {
         public void run() {
            Painting var1x = Painting.this;
            var1x.registerUndo(var1x.activeStrokeBounds);
            Painting.this.beginSuppressingChanges();
            Painting.this.update((RectF)null, new Runnable() {
               public void run() {
                  if (Painting.this.shaders != null) {
                     Map var1x = Painting.this.shaders;
                     String var2;
                     if (Painting.this.brush.isLightSaber()) {
                        var2 = "compositeWithMaskLight";
                     } else {
                        var2 = "compositeWithMask";
                     }

                     Shader var3 = (Shader)var1x.get(var2);
                     if (var3 != null) {
                        GLES20.glUseProgram(var3.program);
                        GLES20.glUniformMatrix4fv(var3.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(Painting.this.projection));
                        GLES20.glUniform1i(var3.getUniform("mask"), 0);
                        Shader.SetColorUniform(var3.getUniform("color"), var1);
                        GLES20.glActiveTexture(33984);
                        GLES20.glBindTexture(3553, Painting.this.getPaintTexture());
                        GLES20.glBlendFuncSeparate(770, 771, 770, 1);
                        GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, Painting.this.vertexBuffer);
                        GLES20.glEnableVertexAttribArray(0);
                        GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, Painting.this.textureBuffer);
                        GLES20.glEnableVertexAttribArray(1);
                        GLES20.glDrawArrays(5, 0, 4);
                     }
                  }
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
      Size var1 = this.size;
      return new RectF(0.0F, 0.0F, var1.width, var1.height);
   }

   public Painting.PaintingData getPaintingData(RectF var1, boolean var2) {
      int var3 = (int)var1.left;
      int var4 = (int)var1.top;
      int var5 = (int)var1.width();
      int var6 = (int)var1.height();
      GLES20.glGenFramebuffers(1, this.buffers, 0);
      int var7 = this.buffers[0];
      GLES20.glBindFramebuffer(36160, var7);
      GLES20.glGenTextures(1, this.buffers, 0);
      int var8 = this.buffers[0];
      GLES20.glBindTexture(3553, var8);
      GLES20.glTexParameteri(3553, 10241, 9729);
      GLES20.glTexParameteri(3553, 10240, 9729);
      GLES20.glTexParameteri(3553, 10242, 33071);
      GLES20.glTexParameteri(3553, 10243, 33071);
      GLES20.glTexImage2D(3553, 0, 6408, var5, var6, 0, 6408, 5121, (Buffer)null);
      GLES20.glFramebufferTexture2D(36160, 36064, 3553, var8, 0);
      Size var10 = this.size;
      GLES20.glViewport(0, 0, (int)var10.width, (int)var10.height);
      Map var9 = this.shaders;
      if (var9 == null) {
         return null;
      } else {
         String var11;
         if (var2) {
            var11 = "nonPremultipliedBlit";
         } else {
            var11 = "blit";
         }

         Shader var12 = (Shader)var9.get(var11);
         if (var12 == null) {
            return null;
         } else {
            GLES20.glUseProgram(var12.program);
            Matrix var15 = new Matrix();
            var15.preTranslate((float)(-var3), (float)(-var4));
            float[] var16 = GLMatrix.LoadGraphicsMatrix(var15);
            var16 = GLMatrix.MultiplyMat4f(this.projection, var16);
            GLES20.glUniformMatrix4fv(var12.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(var16));
            if (var2) {
               GLES20.glUniform1i(var12.getUniform("texture"), 0);
               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, this.getTexture());
            } else {
               GLES20.glUniform1i(var12.getUniform("texture"), 0);
               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, this.bitmapTexture.texture());
               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, this.getTexture());
            }

            GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            GLES20.glClear(16384);
            GLES20.glBlendFunc(1, 771);
            GLES20.glVertexAttribPointer(0, 2, 5126, false, 8, this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(0);
            GLES20.glVertexAttribPointer(1, 2, 5126, false, 8, this.textureBuffer);
            GLES20.glEnableVertexAttribArray(1);
            GLES20.glDrawArrays(5, 0, 4);
            this.dataBuffer.limit(var5 * var6 * 4);
            GLES20.glReadPixels(0, 0, var5, var6, 6408, 5121, this.dataBuffer);
            Painting.PaintingData var13;
            if (var2) {
               var13 = new Painting.PaintingData((Bitmap)null, this.dataBuffer);
            } else {
               Bitmap var14 = Bitmap.createBitmap(var5, var6, Config.ARGB_8888);
               var14.copyPixelsFromBuffer(this.dataBuffer);
               var13 = new Painting.PaintingData(var14, (ByteBuffer)null);
            }

            int[] var17 = this.buffers;
            var17[0] = var7;
            GLES20.glDeleteFramebuffers(1, var17, 0);
            var17 = this.buffers;
            var17[0] = var8;
            GLES20.glDeleteTextures(1, var17, 0);
            return var13;
         }
      }
   }

   public Size getSize() {
      return this.size;
   }

   public boolean isPaused() {
      return this.paused;
   }

   public void onPause(final Runnable var1) {
      this.renderView.performInContext(new Runnable() {
         public void run() {
            Painting.this.paused = true;
            Painting var1x = Painting.this;
            Painting.PaintingData var2 = var1x.getPaintingData(var1x.getBounds(), true);
            var1x = Painting.this;
            var1x.backupSlice = new Slice(var2.data, var1x.getBounds(), Painting.this.delegate.requestDispatchQueue());
            Painting.this.cleanResources(false);
            Runnable var3 = var1;
            if (var3 != null) {
               var3.run();
            }

         }
      });
   }

   public void onResume() {
      this.restoreSlice(this.backupSlice);
      this.backupSlice = null;
      this.paused = false;
   }

   public void paintStroke(final Path var1, final boolean var2, final Runnable var3) {
      this.renderView.performInContext(new Runnable() {
         public void run() {
            Painting.this.activePath = var1;
            GLES20.glBindFramebuffer(36160, Painting.this.getReusableFramebuffer());
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, Painting.this.getPaintTexture(), 0);
            Utils.HasGLError();
            RectF var5;
            if (GLES20.glCheckFramebufferStatus(36160) == 36053) {
               GLES20.glViewport(0, 0, (int)Painting.this.size.width, (int)Painting.this.size.height);
               if (var2) {
                  GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                  GLES20.glClear(16384);
               }

               if (Painting.this.shaders == null) {
                  return;
               }

               Map var1x = Painting.this.shaders;
               String var2x;
               if (Painting.this.brush.isLightSaber()) {
                  var2x = "brushLight";
               } else {
                  var2x = "brush";
               }

               Shader var3x = (Shader)var1x.get(var2x);
               if (var3x == null) {
                  return;
               }

               GLES20.glUseProgram(var3x.program);
               if (Painting.this.brushTexture == null) {
                  Painting var4 = Painting.this;
                  var4.brushTexture = new Texture(var4.brush.getStamp());
               }

               GLES20.glActiveTexture(33984);
               GLES20.glBindTexture(3553, Painting.this.brushTexture.texture());
               GLES20.glUniformMatrix4fv(var3x.getUniform("mvpMatrix"), 1, false, FloatBuffer.wrap(Painting.this.projection));
               GLES20.glUniform1i(var3x.getUniform("texture"), 0);
               var5 = Render.RenderPath(var1, Painting.this.renderState);
            } else {
               var5 = null;
            }

            GLES20.glBindFramebuffer(36160, 0);
            if (Painting.this.delegate != null) {
               Painting.this.delegate.contentChanged(var5);
            }

            if (Painting.this.activeStrokeBounds != null) {
               Painting.this.activeStrokeBounds.union(var5);
            } else {
               Painting.this.activeStrokeBounds = var5;
            }

            Runnable var6 = var3;
            if (var6 != null) {
               var6.run();
            }

         }
      });
   }

   public void render() {
      if (this.shaders != null) {
         if (this.activePath != null) {
            this.render(this.getPaintTexture(), this.activePath.getColor());
         } else {
            this.renderBlit();
         }

      }
   }

   public void setBitmap(Bitmap var1) {
      if (this.bitmapTexture == null) {
         this.bitmapTexture = new Texture(var1);
      }
   }

   public void setBrush(Brush var1) {
      this.brush = var1;
      Texture var2 = this.brushTexture;
      if (var2 != null) {
         var2.cleanResources(true);
         this.brushTexture = null;
      }

   }

   public void setDelegate(Painting.PaintingDelegate var1) {
      this.delegate = var1;
   }

   public void setRenderProjection(float[] var1) {
      this.renderProjection = var1;
   }

   public void setRenderView(RenderView var1) {
      this.renderView = var1;
   }

   public void setupShaders() {
      this.shaders = ShaderSet.setup();
   }

   public class PaintingData {
      public Bitmap bitmap;
      public ByteBuffer data;

      PaintingData(Bitmap var2, ByteBuffer var3) {
         this.bitmap = var2;
         this.data = var3;
      }
   }

   public interface PaintingDelegate {
      void contentChanged(RectF var1);

      DispatchQueue requestDispatchQueue();

      UndoStore requestUndoStore();

      void strokeCommited();
   }
}
