package com.google.android.exoplayer2.text;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;

public final class TextRenderer extends BaseRenderer implements Callback {
   private SubtitleDecoder decoder;
   private final SubtitleDecoderFactory decoderFactory;
   private int decoderReplacementState;
   private final FormatHolder formatHolder;
   private boolean inputStreamEnded;
   private SubtitleInputBuffer nextInputBuffer;
   private SubtitleOutputBuffer nextSubtitle;
   private int nextSubtitleEventIndex;
   private final TextOutput output;
   private final Handler outputHandler;
   private boolean outputStreamEnded;
   private Format streamFormat;
   private SubtitleOutputBuffer subtitle;

   public TextRenderer(TextOutput var1, Looper var2) {
      this(var1, var2, SubtitleDecoderFactory.DEFAULT);
   }

   public TextRenderer(TextOutput var1, Looper var2, SubtitleDecoderFactory var3) {
      super(3);
      Assertions.checkNotNull(var1);
      this.output = (TextOutput)var1;
      Handler var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = Util.createHandler(var2, this);
      }

      this.outputHandler = var4;
      this.decoderFactory = var3;
      this.formatHolder = new FormatHolder();
   }

   private void clearOutput() {
      this.updateOutput(Collections.emptyList());
   }

   private long getNextEventTime() {
      int var1 = this.nextSubtitleEventIndex;
      long var2;
      if (var1 != -1 && var1 < this.subtitle.getEventTimeCount()) {
         var2 = this.subtitle.getEventTime(this.nextSubtitleEventIndex);
      } else {
         var2 = Long.MAX_VALUE;
      }

      return var2;
   }

   private void invokeUpdateOutputInternal(List var1) {
      this.output.onCues(var1);
   }

   private void releaseBuffers() {
      this.nextInputBuffer = null;
      this.nextSubtitleEventIndex = -1;
      SubtitleOutputBuffer var1 = this.subtitle;
      if (var1 != null) {
         var1.release();
         this.subtitle = null;
      }

      var1 = this.nextSubtitle;
      if (var1 != null) {
         var1.release();
         this.nextSubtitle = null;
      }

   }

   private void releaseDecoder() {
      this.releaseBuffers();
      this.decoder.release();
      this.decoder = null;
      this.decoderReplacementState = 0;
   }

   private void replaceDecoder() {
      this.releaseDecoder();
      this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
   }

   private void updateOutput(List var1) {
      Handler var2 = this.outputHandler;
      if (var2 != null) {
         var2.obtainMessage(0, var1).sendToTarget();
      } else {
         this.invokeUpdateOutputInternal(var1);
      }

   }

   public boolean handleMessage(Message var1) {
      if (var1.what == 0) {
         this.invokeUpdateOutputInternal((List)var1.obj);
         return true;
      } else {
         throw new IllegalStateException();
      }
   }

   public boolean isEnded() {
      return this.outputStreamEnded;
   }

   public boolean isReady() {
      return true;
   }

   protected void onDisabled() {
      this.streamFormat = null;
      this.clearOutput();
      this.releaseDecoder();
   }

   protected void onPositionReset(long var1, boolean var3) {
      this.clearOutput();
      this.inputStreamEnded = false;
      this.outputStreamEnded = false;
      if (this.decoderReplacementState != 0) {
         this.replaceDecoder();
      } else {
         this.releaseBuffers();
         this.decoder.flush();
      }

   }

   protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
      this.streamFormat = var1[0];
      if (this.decoder != null) {
         this.decoderReplacementState = 1;
      } else {
         this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
      }

   }

   public void render(long var1, long var3) throws ExoPlaybackException {
      if (!this.outputStreamEnded) {
         if (this.nextSubtitle == null) {
            this.decoder.setPositionUs(var1);

            try {
               this.nextSubtitle = (SubtitleOutputBuffer)this.decoder.dequeueOutputBuffer();
            } catch (SubtitleDecoderException var8) {
               throw ExoPlaybackException.createForRenderer(var8, this.getIndex());
            }
         }

         if (this.getState() == 2) {
            boolean var6;
            if (this.subtitle != null) {
               var3 = this.getNextEventTime();

               for(var6 = false; var3 <= var1; var6 = true) {
                  ++this.nextSubtitleEventIndex;
                  var3 = this.getNextEventTime();
               }
            } else {
               var6 = false;
            }

            SubtitleOutputBuffer var5 = this.nextSubtitle;
            boolean var7 = var6;
            if (var5 != null) {
               if (var5.isEndOfStream()) {
                  var7 = var6;
                  if (!var6) {
                     var7 = var6;
                     if (this.getNextEventTime() == Long.MAX_VALUE) {
                        if (this.decoderReplacementState == 2) {
                           this.replaceDecoder();
                           var7 = var6;
                        } else {
                           this.releaseBuffers();
                           this.outputStreamEnded = true;
                           var7 = var6;
                        }
                     }
                  }
               } else {
                  var7 = var6;
                  if (this.nextSubtitle.timeUs <= var1) {
                     var5 = this.subtitle;
                     if (var5 != null) {
                        var5.release();
                     }

                     this.subtitle = this.nextSubtitle;
                     this.nextSubtitle = null;
                     this.nextSubtitleEventIndex = this.subtitle.getNextEventTimeIndex(var1);
                     var7 = true;
                  }
               }
            }

            if (var7) {
               this.updateOutput(this.subtitle.getCues(var1));
            }

            if (this.decoderReplacementState != 2) {
               SubtitleDecoderException var10000;
               label116: {
                  while(true) {
                     boolean var10001;
                     try {
                        if (this.inputStreamEnded) {
                           break;
                        }

                        if (this.nextInputBuffer == null) {
                           this.nextInputBuffer = (SubtitleInputBuffer)this.decoder.dequeueInputBuffer();
                           if (this.nextInputBuffer == null) {
                              return;
                           }
                        }
                     } catch (SubtitleDecoderException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label116;
                     }

                     try {
                        if (this.decoderReplacementState == 1) {
                           this.nextInputBuffer.setFlags(4);
                           this.decoder.queueInputBuffer(this.nextInputBuffer);
                           this.nextInputBuffer = null;
                           this.decoderReplacementState = 2;
                           return;
                        }
                     } catch (SubtitleDecoderException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label116;
                     }

                     int var16;
                     try {
                        var16 = this.readSource(this.formatHolder, this.nextInputBuffer, false);
                     } catch (SubtitleDecoderException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label116;
                     }

                     if (var16 == -4) {
                        label96: {
                           try {
                              if (this.nextInputBuffer.isEndOfStream()) {
                                 this.inputStreamEnded = true;
                                 break label96;
                              }
                           } catch (SubtitleDecoderException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label116;
                           }

                           try {
                              this.nextInputBuffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                              this.nextInputBuffer.flip();
                           } catch (SubtitleDecoderException var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label116;
                           }
                        }

                        try {
                           this.decoder.queueInputBuffer(this.nextInputBuffer);
                           this.nextInputBuffer = null;
                        } catch (SubtitleDecoderException var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label116;
                        }
                     } else {
                        if (var16 != -3) {
                           continue;
                        }
                        break;
                     }
                  }

                  return;
               }

               SubtitleDecoderException var15 = var10000;
               throw ExoPlaybackException.createForRenderer(var15, this.getIndex());
            }
         }
      }
   }

   public int supportsFormat(Format var1) {
      if (this.decoderFactory.supportsFormat(var1)) {
         byte var2;
         if (BaseRenderer.supportsFormatDrm((DrmSessionManager)null, var1.drmInitData)) {
            var2 = 4;
         } else {
            var2 = 2;
         }

         return var2;
      } else {
         return MimeTypes.isText(var1.sampleMimeType) ? 1 : 0;
      }
   }
}
