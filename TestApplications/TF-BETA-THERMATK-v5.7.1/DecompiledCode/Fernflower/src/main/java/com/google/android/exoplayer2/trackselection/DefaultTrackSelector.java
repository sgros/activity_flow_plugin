package com.google.android.exoplayer2.trackselection;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultTrackSelector extends MappingTrackSelector {
   private static final int[] NO_TRACKS = new int[0];
   private final AtomicReference parametersReference;
   private final TrackSelection.Factory trackSelectionFactory;

   public DefaultTrackSelector(TrackSelection.Factory var1) {
      this.trackSelectionFactory = var1;
      this.parametersReference = new AtomicReference(DefaultTrackSelector.Parameters.DEFAULT);
   }

   private static int compareFormatValues(int var0, int var1) {
      byte var2 = -1;
      if (var0 == -1) {
         var0 = var2;
         if (var1 == -1) {
            var0 = 0;
         }
      } else if (var1 == -1) {
         var0 = 1;
      } else {
         var0 -= var1;
      }

      return var0;
   }

   private static int compareInts(int var0, int var1) {
      byte var2;
      if (var0 > var1) {
         var2 = 1;
      } else if (var1 > var0) {
         var2 = -1;
      } else {
         var2 = 0;
      }

      return var2;
   }

   private static void filterAdaptiveVideoTrackCountForMimeType(TrackGroup var0, int[] var1, int var2, String var3, int var4, int var5, int var6, int var7, List var8) {
      for(int var9 = var8.size() - 1; var9 >= 0; --var9) {
         int var10 = (Integer)var8.get(var9);
         if (!isSupportedAdaptiveVideoTrack(var0.getFormat(var10), var3, var1[var10], var2, var4, var5, var6, var7)) {
            var8.remove(var9);
         }
      }

   }

   protected static boolean formatHasLanguage(Format var0, String var1) {
      boolean var2;
      if (var1 != null && TextUtils.equals(var1, Util.normalizeLanguageCode(var0.language))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected static boolean formatHasNoLanguage(Format var0) {
      boolean var1;
      if (!TextUtils.isEmpty(var0.language) && !formatHasLanguage(var0, "und")) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private static int getAdaptiveAudioTrackCount(TrackGroup var0, int[] var1, DefaultTrackSelector.AudioConfigurationTuple var2, boolean var3, boolean var4) {
      int var5 = 0;

      int var6;
      int var7;
      for(var6 = 0; var5 < var0.length; var6 = var7) {
         var7 = var6;
         if (isSupportedAdaptiveAudioTrack(var0.getFormat(var5), var1[var5], var2, var3, var4)) {
            var7 = var6 + 1;
         }

         ++var5;
      }

      return var6;
   }

   private static int[] getAdaptiveAudioTracks(TrackGroup var0, int[] var1, boolean var2, boolean var3) {
      HashSet var4 = new HashSet();
      byte var5 = 0;
      DefaultTrackSelector.AudioConfigurationTuple var6 = null;
      int var7 = 0;

      int var8;
      Format var9;
      int var11;
      DefaultTrackSelector.AudioConfigurationTuple var14;
      for(var8 = 0; var7 < var0.length; var6 = var14) {
         var9 = var0.getFormat(var7);
         DefaultTrackSelector.AudioConfigurationTuple var10 = new DefaultTrackSelector.AudioConfigurationTuple(var9.channelCount, var9.sampleRate, var9.sampleMimeType);
         var11 = var8;
         var14 = var6;
         if (var4.add(var10)) {
            int var12 = getAdaptiveAudioTrackCount(var0, var1, var10, var2, var3);
            var11 = var8;
            var14 = var6;
            if (var12 > var8) {
               var11 = var12;
               var14 = var10;
            }
         }

         ++var7;
         var8 = var11;
      }

      if (var8 > 1) {
         int[] var15 = new int[var8];
         var11 = 0;

         for(var8 = var5; var8 < var0.length; var11 = var7) {
            var9 = var0.getFormat(var8);
            int var13 = var1[var8];
            Assertions.checkNotNull(var6);
            var7 = var11;
            if (isSupportedAdaptiveAudioTrack(var9, var13, (DefaultTrackSelector.AudioConfigurationTuple)var6, var2, var3)) {
               var15[var11] = var8;
               var7 = var11 + 1;
            }

            ++var8;
         }

         return var15;
      } else {
         return NO_TRACKS;
      }
   }

   private static int getAdaptiveVideoTrackCountForMimeType(TrackGroup var0, int[] var1, int var2, String var3, int var4, int var5, int var6, int var7, List var8) {
      int var9 = 0;

      int var10;
      int var12;
      for(var10 = 0; var9 < var8.size(); var10 = var12) {
         int var11 = (Integer)var8.get(var9);
         var12 = var10;
         if (isSupportedAdaptiveVideoTrack(var0.getFormat(var11), var3, var1[var11], var2, var4, var5, var6, var7)) {
            var12 = var10 + 1;
         }

         ++var9;
      }

      return var10;
   }

   private static int[] getAdaptiveVideoTracksForGroup(TrackGroup var0, int[] var1, boolean var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      if (var0.length < 2) {
         return NO_TRACKS;
      } else {
         List var11 = getViewportFilteredTrackIndices(var0, var8, var9, var10);
         if (var11.size() < 2) {
            return NO_TRACKS;
         } else {
            String var13;
            if (!var2) {
               HashSet var12 = new HashSet();
               var13 = null;
               var8 = 0;

               int var16;
               for(var9 = 0; var8 < var11.size(); var9 = var16) {
                  String var14 = var0.getFormat((Integer)var11.get(var8)).sampleMimeType;
                  String var15 = var13;
                  var16 = var9;
                  if (var12.add(var14)) {
                     int var17 = getAdaptiveVideoTrackCountForMimeType(var0, var1, var3, var14, var4, var5, var6, var7, var11);
                     var15 = var13;
                     var16 = var9;
                     if (var17 > var9) {
                        var16 = var17;
                        var15 = var14;
                     }
                  }

                  ++var8;
                  var13 = var15;
               }
            } else {
               var13 = null;
            }

            filterAdaptiveVideoTrackCountForMimeType(var0, var1, var3, var13, var4, var5, var6, var7, var11);
            int[] var18;
            if (var11.size() < 2) {
               var18 = NO_TRACKS;
            } else {
               var18 = Util.toArray(var11);
            }

            return var18;
         }
      }
   }

   private static Point getMaxVideoSizeInViewport(boolean var0, int var1, int var2, int var3, int var4) {
      int var8;
      label26: {
         if (var0) {
            boolean var5 = true;
            boolean var6;
            if (var3 > var4) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var1 <= var2) {
               var5 = false;
            }

            if (var6 != var5) {
               break label26;
            }
         }

         var8 = var2;
         var2 = var1;
         var1 = var8;
      }

      var8 = var3 * var1;
      int var7 = var4 * var2;
      return var8 >= var7 ? new Point(var2, Util.ceilDivide(var7, var3)) : new Point(Util.ceilDivide(var8, var4), var1);
   }

   private static List getViewportFilteredTrackIndices(TrackGroup var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList(var0.length);
      int var5 = 0;

      int var6;
      for(var6 = 0; var6 < var0.length; ++var6) {
         var4.add(var6);
      }

      if (var1 != Integer.MAX_VALUE && var2 != Integer.MAX_VALUE) {
         int var9;
         for(var6 = Integer.MAX_VALUE; var5 < var0.length; var6 = var9) {
            Format var7 = var0.getFormat(var5);
            int var8 = var7.width;
            var9 = var6;
            if (var8 > 0) {
               int var10 = var7.height;
               var9 = var6;
               if (var10 > 0) {
                  Point var11 = getMaxVideoSizeInViewport(var3, var1, var2, var8, var10);
                  var10 = var7.width;
                  int var12 = var7.height;
                  var8 = var10 * var12;
                  var9 = var6;
                  if (var10 >= (int)((float)var11.x * 0.98F)) {
                     var9 = var6;
                     if (var12 >= (int)((float)var11.y * 0.98F)) {
                        var9 = var6;
                        if (var8 < var6) {
                           var9 = var8;
                        }
                     }
                  }
               }
            }

            ++var5;
         }

         if (var6 != Integer.MAX_VALUE) {
            for(var1 = var4.size() - 1; var1 >= 0; --var1) {
               var2 = var0.getFormat((Integer)var4.get(var1)).getPixelCount();
               if (var2 == -1 || var2 > var6) {
                  var4.remove(var1);
               }
            }
         }
      }

      return var4;
   }

   protected static boolean isSupported(int var0, boolean var1) {
      var0 &= 7;
      if (var0 == 4 || var1 && var0 == 3) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isSupportedAdaptiveAudioTrack(Format var0, int var1, DefaultTrackSelector.AudioConfigurationTuple var2, boolean var3, boolean var4) {
      boolean var5 = false;
      boolean var6 = var5;
      if (isSupported(var1, false)) {
         var1 = var0.channelCount;
         var6 = var5;
         if (var1 != -1) {
            var6 = var5;
            if (var1 == var2.channelCount) {
               if (!var3) {
                  String var7 = var0.sampleMimeType;
                  var6 = var5;
                  if (var7 == null) {
                     return var6;
                  }

                  var6 = var5;
                  if (!TextUtils.equals(var7, var2.mimeType)) {
                     return var6;
                  }
               }

               if (!var4) {
                  var1 = var0.sampleRate;
                  var6 = var5;
                  if (var1 == -1) {
                     return var6;
                  }

                  var6 = var5;
                  if (var1 != var2.sampleRate) {
                     return var6;
                  }
               }

               var6 = true;
            }
         }
      }

      return var6;
   }

   private static boolean isSupportedAdaptiveVideoTrack(Format var0, String var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      boolean var8 = false;
      boolean var9 = var8;
      if (isSupported(var2, false)) {
         var9 = var8;
         if ((var2 & var3) != 0) {
            if (var1 != null) {
               var9 = var8;
               if (!Util.areEqual(var0.sampleMimeType, var1)) {
                  return var9;
               }
            }

            var2 = var0.width;
            if (var2 != -1) {
               var9 = var8;
               if (var2 > var4) {
                  return var9;
               }
            }

            var2 = var0.height;
            if (var2 != -1) {
               var9 = var8;
               if (var2 > var5) {
                  return var9;
               }
            }

            float var10 = var0.frameRate;
            if (var10 != -1.0F) {
               var9 = var8;
               if (var10 > (float)var6) {
                  return var9;
               }
            }

            var2 = var0.bitrate;
            if (var2 != -1) {
               var9 = var8;
               if (var2 > var7) {
                  return var9;
               }
            }

            var9 = true;
         }
      }

      return var9;
   }

   private static void maybeConfigureRenderersForTunneling(MappingTrackSelector.MappedTrackInfo var0, int[][][] var1, RendererConfiguration[] var2, TrackSelection[] var3, int var4) {
      if (var4 != 0) {
         boolean var5 = false;
         int var6 = 0;
         int var7 = -1;
         int var8 = -1;

         boolean var14;
         while(true) {
            if (var6 >= var0.getRendererCount()) {
               var14 = true;
               break;
            }

            int var11;
            int var12;
            label56: {
               int var9 = var0.getRendererType(var6);
               TrackSelection var10 = var3[var6];
               if (var9 != 1) {
                  var11 = var7;
                  var12 = var8;
                  if (var9 != 2) {
                     break label56;
                  }
               }

               var11 = var7;
               var12 = var8;
               if (var10 != null) {
                  var11 = var7;
                  var12 = var8;
                  if (rendererSupportsTunneling(var1[var6], var0.getTrackGroups(var6), var10)) {
                     label59: {
                        if (var9 == 1) {
                           if (var8 == -1) {
                              var12 = var6;
                              var11 = var7;
                              break label59;
                           }
                        } else if (var7 == -1) {
                           var11 = var6;
                           var12 = var8;
                           break label59;
                        }

                        var14 = false;
                        break;
                     }
                  }
               }
            }

            ++var6;
            var7 = var11;
            var8 = var12;
         }

         boolean var15 = var5;
         if (var8 != -1) {
            var15 = var5;
            if (var7 != -1) {
               var15 = true;
            }
         }

         if (var14 & var15) {
            RendererConfiguration var13 = new RendererConfiguration(var4);
            var2[var8] = var13;
            var2[var7] = var13;
         }

      }
   }

   private static boolean rendererSupportsTunneling(int[][] var0, TrackGroupArray var1, TrackSelection var2) {
      if (var2 == null) {
         return false;
      } else {
         int var3 = var1.indexOf(var2.getTrackGroup());

         for(int var4 = 0; var4 < var2.length(); ++var4) {
            if ((var0[var3][var2.getIndexInTrackGroup(var4)] & 32) != 32) {
               return false;
            }
         }

         return true;
      }
   }

   private static TrackSelection.Definition selectAdaptiveVideoTrack(TrackGroupArray var0, int[][] var1, int var2, DefaultTrackSelector.Parameters var3) {
      byte var4;
      if (var3.allowVideoNonSeamlessAdaptiveness) {
         var4 = 24;
      } else {
         var4 = 16;
      }

      boolean var5;
      if (var3.allowVideoMixedMimeTypeAdaptiveness && (var2 & var4) != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      for(var2 = 0; var2 < var0.length; ++var2) {
         TrackGroup var7 = var0.get(var2);
         int[] var6 = getAdaptiveVideoTracksForGroup(var7, var1[var2], var5, var4, var3.maxVideoWidth, var3.maxVideoHeight, var3.maxVideoFrameRate, var3.maxVideoBitrate, var3.viewportWidth, var3.viewportHeight, var3.viewportOrientationMayChange);
         if (var6.length > 0) {
            return new TrackSelection.Definition(var7, var6);
         }
      }

      return null;
   }

   private static TrackSelection.Definition selectFixedVideoTrack(TrackGroupArray var0, int[][] var1, DefaultTrackSelector.Parameters var2) {
      int var3 = 0;
      TrackGroup var4 = null;
      int var5 = 0;
      int var6 = 0;
      int var7 = -1;

      int var12;
      for(int var8 = -1; var3 < var0.length; var8 = var12) {
         TrackGroup var9 = var0.get(var3);
         List var10 = getViewportFilteredTrackIndices(var9, var2.viewportWidth, var2.viewportHeight, var2.viewportOrientationMayChange);
         int[] var11 = var1[var3];
         var12 = var6;
         byte var13 = 0;
         var6 = var8;
         var8 = var12;
         var12 = var5;

         for(var5 = var13; var5 < var9.length; ++var5) {
            if (isSupported(var11[var5], var2.exceedRendererCapabilitiesIfNecessary)) {
               Format var14;
               boolean var16;
               int var20;
               label123: {
                  var14 = var9.getFormat(var5);
                  if (var10.contains(var5)) {
                     var20 = var14.width;
                     if (var20 == -1 || var20 <= var2.maxVideoWidth) {
                        var20 = var14.height;
                        if (var20 == -1 || var20 <= var2.maxVideoHeight) {
                           float var15 = var14.frameRate;
                           if (var15 == -1.0F || var15 <= (float)var2.maxVideoFrameRate) {
                              var20 = var14.bitrate;
                              if (var20 == -1 || var20 <= var2.maxVideoBitrate) {
                                 var16 = true;
                                 break label123;
                              }
                           }
                        }
                     }
                  }

                  var16 = false;
               }

               if (var16 || var2.exceedVideoConstraintsIfNecessary) {
                  byte var17;
                  if (var16) {
                     var17 = 2;
                  } else {
                     var17 = 1;
                  }

                  boolean var18 = isSupported(var11[var5], false);
                  var20 = var17;
                  if (var18) {
                     var20 = var17 + 1000;
                  }

                  boolean var21;
                  if (var20 > var8) {
                     var21 = true;
                  } else {
                     var21 = false;
                  }

                  if (var20 == var8) {
                     label82: {
                        label128: {
                           int var22 = compareFormatValues(var14.bitrate, var7);
                           if (var2.forceLowestBitrate && var22 != 0) {
                              if (var22 < 0) {
                                 break label128;
                              }
                           } else {
                              var22 = var14.getPixelCount();
                              if (var22 != var6) {
                                 var22 = compareFormatValues(var22, var6);
                              } else {
                                 var22 = compareFormatValues(var14.bitrate, var7);
                              }

                              if (var18 && var16) {
                                 if (var22 > 0) {
                                    break label128;
                                 }
                              } else if (var22 < 0) {
                                 break label128;
                              }
                           }

                           var21 = false;
                           break label82;
                        }

                        var21 = true;
                     }
                  }

                  if (var21) {
                     var7 = var14.bitrate;
                     var6 = var14.getPixelCount();
                     var12 = var5;
                     var4 = var9;
                     var8 = var20;
                  }
               }
            }
         }

         ++var3;
         var5 = var12;
         var12 = var6;
         var6 = var8;
      }

      TrackSelection.Definition var19;
      if (var4 == null) {
         var19 = null;
      } else {
         var19 = new TrackSelection.Definition(var4, new int[]{var5});
      }

      return var19;
   }

   protected TrackSelection.Definition[] selectAllTracks(MappingTrackSelector.MappedTrackInfo var1, int[][][] var2, int[] var3, DefaultTrackSelector.Parameters var4) throws ExoPlaybackException {
      MappingTrackSelector.MappedTrackInfo var5 = var1;
      int var6 = var1.getRendererCount();
      TrackSelection.Definition[] var7 = new TrackSelection.Definition[var6];
      boolean var8 = false;
      int var9 = 0;
      boolean var10 = false;

      while(true) {
         boolean var11 = true;
         if (var9 >= var6) {
            DefaultTrackSelector.AudioTrackScore var14 = null;
            DefaultTrackSelector.AudioTrackScore var20 = null;
            int var23 = -1;
            int var24 = -1;
            var9 = Integer.MIN_VALUE;

            DefaultTrackSelector.AudioTrackScore var26;
            for(int var21 = 0; var21 < var6; var20 = var26) {
               label91: {
                  int var22 = var1.getRendererType(var21);
                  TrackGroupArray var15;
                  Pair var25;
                  if (var22 != 1) {
                     if (var22 != 2) {
                        if (var22 != 3) {
                           var7[var21] = this.selectOtherTrack(var22, var1.getTrackGroups(var21), var2[var21], var4);
                        } else {
                           var25 = this.selectTextTrack(var1.getTrackGroups(var21), var2[var21], var4);
                           if (var25 != null && (Integer)var25.second > var9) {
                              if (var24 != -1) {
                                 var7[var24] = var14;
                              }

                              var7[var21] = (TrackSelection.Definition)var25.first;
                              var9 = (Integer)var25.second;
                              var24 = var21;
                              var15 = var14;
                              var14 = var20;
                              var5 = var15;
                              break label91;
                           }
                        }
                     }
                  } else {
                     var15 = var1.getTrackGroups(var21);
                     int[][] var16 = var2[var21];
                     int var17 = var3[var21];
                     boolean var18;
                     if (!var10) {
                        var18 = true;
                     } else {
                        var18 = false;
                     }

                     var25 = this.selectAudioTrack(var15, var16, var17, var4, var18);
                     if (var25 != null && (var20 == null || ((DefaultTrackSelector.AudioTrackScore)var25.second).compareTo(var20) > 0)) {
                        if (var23 != -1) {
                           var7[var23] = null;
                        }

                        var5 = null;
                        var7[var21] = (TrackSelection.Definition)var25.first;
                        var14 = (DefaultTrackSelector.AudioTrackScore)var25.second;
                        var23 = var21;
                        break label91;
                     }

                     var14 = null;
                  }

                  var5 = var14;
                  var14 = var20;
               }

               ++var21;
               var26 = var14;
               var14 = var5;
            }

            return var7;
         }

         boolean var12 = var8;
         boolean var13 = var10;
         if (2 == var5.getRendererType(var9)) {
            var12 = var8;
            if (!var8) {
               var7[var9] = this.selectVideoTrack(var5.getTrackGroups(var9), var2[var9], var3[var9], var4, true);
               if (var7[var9] != null) {
                  var12 = true;
               } else {
                  var12 = false;
               }
            }

            if (var5.getTrackGroups(var9).length > 0) {
               var8 = var11;
            } else {
               var8 = false;
            }

            var13 = var10 | var8;
         }

         ++var9;
         var8 = var12;
         var10 = var13;
      }
   }

   protected Pair selectAudioTrack(TrackGroupArray var1, int[][] var2, int var3, DefaultTrackSelector.Parameters var4, boolean var5) throws ExoPlaybackException {
      Object var6 = null;
      DefaultTrackSelector.AudioTrackScore var7 = null;
      var3 = 0;
      int var8 = -1;

      int var9;
      int var12;
      for(var9 = -1; var3 < var1.length; var8 = var12) {
         TrackGroup var10 = var1.get(var3);
         int[] var11 = var2[var3];
         var12 = var8;
         byte var13 = 0;
         var8 = var9;
         var9 = var12;

         DefaultTrackSelector.AudioTrackScore var15;
         for(var12 = var13; var12 < var10.length; var7 = var15) {
            int var14 = var9;
            int var20 = var8;
            var15 = var7;
            if (isSupported(var11[var12], var4.exceedRendererCapabilitiesIfNecessary)) {
               DefaultTrackSelector.AudioTrackScore var16 = new DefaultTrackSelector.AudioTrackScore(var10.getFormat(var12), var4, var11[var12]);
               if (!var16.isWithinConstraints && !var4.exceedAudioConstraintsIfNecessary) {
                  var14 = var9;
                  var20 = var8;
                  var15 = var7;
               } else {
                  label62: {
                     if (var7 != null) {
                        var14 = var9;
                        var20 = var8;
                        var15 = var7;
                        if (var16.compareTo(var7) <= 0) {
                           break label62;
                        }
                     }

                     var14 = var3;
                     var20 = var12;
                     var15 = var16;
                  }
               }
            }

            ++var12;
            var9 = var14;
            var8 = var20;
         }

         ++var3;
         var12 = var9;
         var9 = var8;
      }

      if (var8 == -1) {
         return null;
      } else {
         TrackGroup var21 = var1.get(var8);
         TrackSelection.Definition var17 = (TrackSelection.Definition)var6;
         if (!var4.forceHighestSupportedBitrate) {
            var17 = (TrackSelection.Definition)var6;
            if (!var4.forceLowestBitrate) {
               var17 = (TrackSelection.Definition)var6;
               if (var5) {
                  int[] var18 = getAdaptiveAudioTracks(var21, var2[var8], var4.allowAudioMixedMimeTypeAdaptiveness, var4.allowAudioMixedSampleRateAdaptiveness);
                  var17 = (TrackSelection.Definition)var6;
                  if (var18.length > 0) {
                     var17 = new TrackSelection.Definition(var21, var18);
                  }
               }
            }
         }

         TrackSelection.Definition var19 = var17;
         if (var17 == null) {
            var19 = new TrackSelection.Definition(var21, new int[]{var9});
         }

         Assertions.checkNotNull(var7);
         return Pair.create(var19, var7);
      }
   }

   protected TrackSelection.Definition selectOtherTrack(int var1, TrackGroupArray var2, int[][] var3, DefaultTrackSelector.Parameters var4) throws ExoPlaybackException {
      Object var5 = null;
      TrackGroup var6 = null;
      int var7 = 0;
      var1 = 0;

      int var11;
      for(int var8 = 0; var7 < var2.length; var1 = var11) {
         TrackGroup var9 = var2.get(var7);
         int[] var10 = var3[var7];
         var11 = var1;
         byte var12 = 0;
         var1 = var8;
         var8 = var11;

         int var15;
         for(var11 = var12; var11 < var9.length; var1 = var15) {
            TrackGroup var13 = var6;
            int var14 = var8;
            var15 = var1;
            if (isSupported(var10[var11], var4.exceedRendererCapabilitiesIfNecessary)) {
               boolean var17;
               if ((var9.getFormat(var11).selectionFlags & 1) != 0) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               byte var19;
               if (var17) {
                  var19 = 2;
               } else {
                  var19 = 1;
               }

               int var18 = var19;
               if (isSupported(var10[var11], false)) {
                  var18 = var19 + 1000;
               }

               var13 = var6;
               var14 = var8;
               var15 = var1;
               if (var18 > var1) {
                  var14 = var11;
                  var13 = var9;
                  var15 = var18;
               }
            }

            ++var11;
            var6 = var13;
            var8 = var14;
         }

         ++var7;
         var11 = var8;
         var8 = var1;
      }

      TrackSelection.Definition var16;
      if (var6 == null) {
         var16 = (TrackSelection.Definition)var5;
      } else {
         var16 = new TrackSelection.Definition(var6, new int[]{var1});
      }

      return var16;
   }

   protected Pair selectTextTrack(TrackGroupArray var1, int[][] var2, DefaultTrackSelector.Parameters var3) throws ExoPlaybackException {
      int var4 = 0;
      TrackGroup var5 = null;
      int var6 = 0;

      int var7;
      int var11;
      for(var7 = 0; var4 < var1.length; var6 = var11) {
         TrackGroup var8 = var1.get(var4);
         int[] var9 = var2[var4];
         int var10 = 0;

         for(var11 = var6; var10 < var8.length; var7 = var6) {
            TrackGroup var12 = var5;
            int var13 = var11;
            var6 = var7;
            if (isSupported(var9[var10], var3.exceedRendererCapabilitiesIfNecessary)) {
               label89: {
                  Format var14 = var8.getFormat(var10);
                  int var15 = var14.selectionFlags & ~var3.disabledTextTrackSelectionFlags;
                  boolean var17;
                  if ((var15 & 1) != 0) {
                     var17 = true;
                  } else {
                     var17 = false;
                  }

                  boolean var20;
                  if ((var15 & 2) != 0) {
                     var20 = true;
                  } else {
                     var20 = false;
                  }

                  byte var19 = formatHasLanguage(var14, var3.preferredTextLanguage);
                  if (var19 == 0 && (!var3.selectUndeterminedTextLanguage || !formatHasNoLanguage(var14))) {
                     if (var17) {
                        var6 = 3;
                     } else {
                        var12 = var5;
                        var13 = var11;
                        var6 = var7;
                        if (!var20) {
                           break label89;
                        }

                        if (formatHasLanguage(var14, var3.preferredAudioLanguage)) {
                           var6 = 2;
                        } else {
                           var6 = 1;
                        }
                     }
                  } else {
                     byte var18;
                     if (var17) {
                        var18 = 8;
                     } else if (!var20) {
                        var18 = 6;
                     } else {
                        var18 = 4;
                     }

                     var6 = var18 + var19;
                  }

                  var15 = var6;
                  if (isSupported(var9[var10], false)) {
                     var15 = var6 + 1000;
                  }

                  var12 = var5;
                  var13 = var11;
                  var6 = var7;
                  if (var15 > var7) {
                     var6 = var15;
                     var13 = var10;
                     var12 = var8;
                  }
               }
            }

            ++var10;
            var5 = var12;
            var11 = var13;
         }

         ++var4;
      }

      Pair var16;
      if (var5 == null) {
         var16 = null;
      } else {
         var16 = Pair.create(new TrackSelection.Definition(var5, new int[]{var6}), var7);
      }

      return var16;
   }

   protected final Pair selectTracks(MappingTrackSelector.MappedTrackInfo var1, int[][][] var2, int[] var3) throws ExoPlaybackException {
      DefaultTrackSelector.Parameters var4 = (DefaultTrackSelector.Parameters)this.parametersReference.get();
      int var5 = var1.getRendererCount();
      TrackSelection.Definition[] var6 = this.selectAllTracks(var1, var2, var3, var4);
      int var7 = 0;

      while(true) {
         TrackSelection.Definition var11 = null;
         if (var7 >= var5) {
            TrackSelection[] var14 = this.trackSelectionFactory.createTrackSelections(var6, this.getBandwidthMeter());
            RendererConfiguration[] var13 = new RendererConfiguration[var5];

            for(var7 = 0; var7 < var5; ++var7) {
               boolean var10;
               if (var4.getRendererDisabled(var7) || var1.getRendererType(var7) != 6 && var14[var7] == null) {
                  var10 = false;
               } else {
                  var10 = true;
               }

               RendererConfiguration var12;
               if (var10) {
                  var12 = RendererConfiguration.DEFAULT;
               } else {
                  var12 = null;
               }

               var13[var7] = var12;
            }

            maybeConfigureRenderersForTunneling(var1, var2, var13, var14, var4.tunnelingAudioSessionId);
            return Pair.create(var13, var14);
         }

         if (var4.getRendererDisabled(var7)) {
            var6[var7] = null;
         } else {
            TrackGroupArray var8 = var1.getTrackGroups(var7);
            if (var4.hasSelectionOverride(var7, var8)) {
               DefaultTrackSelector.SelectionOverride var9 = var4.getSelectionOverride(var7, var8);
               if (var9 != null) {
                  var11 = new TrackSelection.Definition(var8.get(var9.groupIndex), var9.tracks);
               }

               var6[var7] = var11;
            }
         }

         ++var7;
      }
   }

   protected TrackSelection.Definition selectVideoTrack(TrackGroupArray var1, int[][] var2, int var3, DefaultTrackSelector.Parameters var4, boolean var5) throws ExoPlaybackException {
      TrackSelection.Definition var6;
      if (!var4.forceHighestSupportedBitrate && !var4.forceLowestBitrate && var5) {
         var6 = selectAdaptiveVideoTrack(var1, var2, var3, var4);
      } else {
         var6 = null;
      }

      TrackSelection.Definition var7 = var6;
      if (var6 == null) {
         var7 = selectFixedVideoTrack(var1, var2, var4);
      }

      return var7;
   }

   private static final class AudioConfigurationTuple {
      public final int channelCount;
      public final String mimeType;
      public final int sampleRate;

      public AudioConfigurationTuple(int var1, int var2, String var3) {
         this.channelCount = var1;
         this.sampleRate = var2;
         this.mimeType = var3;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && DefaultTrackSelector.AudioConfigurationTuple.class == var1.getClass()) {
            DefaultTrackSelector.AudioConfigurationTuple var3 = (DefaultTrackSelector.AudioConfigurationTuple)var1;
            if (this.channelCount != var3.channelCount || this.sampleRate != var3.sampleRate || !TextUtils.equals(this.mimeType, var3.mimeType)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.channelCount;
         int var2 = this.sampleRate;
         String var3 = this.mimeType;
         int var4;
         if (var3 != null) {
            var4 = var3.hashCode();
         } else {
            var4 = 0;
         }

         return (var1 * 31 + var2) * 31 + var4;
      }
   }

   protected static final class AudioTrackScore implements Comparable {
      private final int bitrate;
      private final int channelCount;
      private final int defaultSelectionFlagScore;
      public final boolean isWithinConstraints;
      private final int matchLanguageScore;
      private final DefaultTrackSelector.Parameters parameters;
      private final int sampleRate;
      private final int withinRendererCapabilitiesScore;

      public AudioTrackScore(Format var1, DefaultTrackSelector.Parameters var2, int var3) {
         this.parameters = var2;
         boolean var4 = false;
         this.withinRendererCapabilitiesScore = DefaultTrackSelector.isSupported(var3, false);
         this.matchLanguageScore = DefaultTrackSelector.formatHasLanguage(var1, var2.preferredAudioLanguage);
         byte var6;
         if ((var1.selectionFlags & 1) != 0) {
            var6 = 1;
         } else {
            var6 = 0;
         }

         boolean var5;
         label25: {
            this.defaultSelectionFlagScore = var6;
            this.channelCount = var1.channelCount;
            this.sampleRate = var1.sampleRate;
            var3 = var1.bitrate;
            this.bitrate = var3;
            if (var3 != -1) {
               var5 = var4;
               if (var3 > var2.maxAudioBitrate) {
                  break label25;
               }
            }

            var3 = var1.channelCount;
            if (var3 != -1) {
               var5 = var4;
               if (var3 > var2.maxAudioChannelCount) {
                  break label25;
               }
            }

            var5 = true;
         }

         this.isWithinConstraints = var5;
      }

      public int compareTo(DefaultTrackSelector.AudioTrackScore var1) {
         int var2 = this.withinRendererCapabilitiesScore;
         int var3 = var1.withinRendererCapabilitiesScore;
         if (var2 != var3) {
            return DefaultTrackSelector.compareInts(var2, var3);
         } else {
            var2 = this.matchLanguageScore;
            var3 = var1.matchLanguageScore;
            if (var2 != var3) {
               return DefaultTrackSelector.compareInts(var2, var3);
            } else {
               boolean var4 = this.isWithinConstraints;
               boolean var5 = var1.isWithinConstraints;
               byte var7 = -1;
               if (var4 != var5) {
                  if (var4) {
                     var7 = 1;
                  }

                  return var7;
               } else {
                  if (this.parameters.forceLowestBitrate) {
                     var3 = DefaultTrackSelector.compareFormatValues(this.bitrate, var1.bitrate);
                     if (var3 != 0) {
                        if (var3 <= 0) {
                           var7 = 1;
                        }

                        return var7;
                     }
                  }

                  var3 = this.defaultSelectionFlagScore;
                  int var6 = var1.defaultSelectionFlagScore;
                  if (var3 != var6) {
                     return DefaultTrackSelector.compareInts(var3, var6);
                  } else {
                     byte var8 = var7;
                     if (this.isWithinConstraints) {
                        var8 = var7;
                        if (this.withinRendererCapabilitiesScore == 1) {
                           var8 = 1;
                        }
                     }

                     var2 = this.channelCount;
                     var6 = var1.channelCount;
                     if (var2 != var6) {
                        var2 = DefaultTrackSelector.compareInts(var2, var6);
                     } else {
                        var6 = this.sampleRate;
                        var2 = var1.sampleRate;
                        if (var6 != var2) {
                           var2 = DefaultTrackSelector.compareInts(var6, var2);
                        } else {
                           var2 = DefaultTrackSelector.compareInts(this.bitrate, var1.bitrate);
                        }
                     }

                     return var8 * var2;
                  }
               }
            }
         }
      }
   }

   public static final class Parameters implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public DefaultTrackSelector.Parameters createFromParcel(Parcel var1) {
            return new DefaultTrackSelector.Parameters(var1);
         }

         public DefaultTrackSelector.Parameters[] newArray(int var1) {
            return new DefaultTrackSelector.Parameters[var1];
         }
      };
      public static final DefaultTrackSelector.Parameters DEFAULT = new DefaultTrackSelector.Parameters();
      public final boolean allowAudioMixedMimeTypeAdaptiveness;
      public final boolean allowAudioMixedSampleRateAdaptiveness;
      @Deprecated
      public final boolean allowMixedMimeAdaptiveness;
      @Deprecated
      public final boolean allowNonSeamlessAdaptiveness;
      public final boolean allowVideoMixedMimeTypeAdaptiveness;
      public final boolean allowVideoNonSeamlessAdaptiveness;
      public final int disabledTextTrackSelectionFlags;
      public final boolean exceedAudioConstraintsIfNecessary;
      public final boolean exceedRendererCapabilitiesIfNecessary;
      public final boolean exceedVideoConstraintsIfNecessary;
      public final boolean forceHighestSupportedBitrate;
      public final boolean forceLowestBitrate;
      public final int maxAudioBitrate;
      public final int maxAudioChannelCount;
      public final int maxVideoBitrate;
      public final int maxVideoFrameRate;
      public final int maxVideoHeight;
      public final int maxVideoWidth;
      public final String preferredAudioLanguage;
      public final String preferredTextLanguage;
      private final SparseBooleanArray rendererDisabledFlags;
      public final boolean selectUndeterminedTextLanguage;
      private final SparseArray selectionOverrides;
      public final int tunnelingAudioSessionId;
      public final int viewportHeight;
      public final boolean viewportOrientationMayChange;
      public final int viewportWidth;

      private Parameters() {
         this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true, Integer.MAX_VALUE, Integer.MAX_VALUE, true, (String)null, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, false, (String)null, false, 0, false, false, true, 0, new SparseArray(), new SparseBooleanArray());
      }

      Parameters(int var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7, int var8, int var9, boolean var10, String var11, int var12, int var13, boolean var14, boolean var15, boolean var16, String var17, boolean var18, int var19, boolean var20, boolean var21, boolean var22, int var23, SparseArray var24, SparseBooleanArray var25) {
         this.maxVideoWidth = var1;
         this.maxVideoHeight = var2;
         this.maxVideoFrameRate = var3;
         this.maxVideoBitrate = var4;
         this.exceedVideoConstraintsIfNecessary = var5;
         this.allowVideoMixedMimeTypeAdaptiveness = var6;
         this.allowVideoNonSeamlessAdaptiveness = var7;
         this.viewportWidth = var8;
         this.viewportHeight = var9;
         this.viewportOrientationMayChange = var10;
         this.preferredAudioLanguage = Util.normalizeLanguageCode(var11);
         this.maxAudioChannelCount = var12;
         this.maxAudioBitrate = var13;
         this.exceedAudioConstraintsIfNecessary = var14;
         this.allowAudioMixedMimeTypeAdaptiveness = var15;
         this.allowAudioMixedSampleRateAdaptiveness = var16;
         this.preferredTextLanguage = Util.normalizeLanguageCode(var17);
         this.selectUndeterminedTextLanguage = var18;
         this.disabledTextTrackSelectionFlags = var19;
         this.forceLowestBitrate = var20;
         this.forceHighestSupportedBitrate = var21;
         this.exceedRendererCapabilitiesIfNecessary = var22;
         this.tunnelingAudioSessionId = var23;
         this.selectionOverrides = var24;
         this.rendererDisabledFlags = var25;
         this.allowMixedMimeAdaptiveness = var6;
         this.allowNonSeamlessAdaptiveness = var7;
      }

      Parameters(Parcel var1) {
         this.maxVideoWidth = var1.readInt();
         this.maxVideoHeight = var1.readInt();
         this.maxVideoFrameRate = var1.readInt();
         this.maxVideoBitrate = var1.readInt();
         this.exceedVideoConstraintsIfNecessary = Util.readBoolean(var1);
         this.allowVideoMixedMimeTypeAdaptiveness = Util.readBoolean(var1);
         this.allowVideoNonSeamlessAdaptiveness = Util.readBoolean(var1);
         this.viewportWidth = var1.readInt();
         this.viewportHeight = var1.readInt();
         this.viewportOrientationMayChange = Util.readBoolean(var1);
         this.preferredAudioLanguage = var1.readString();
         this.maxAudioChannelCount = var1.readInt();
         this.maxAudioBitrate = var1.readInt();
         this.exceedAudioConstraintsIfNecessary = Util.readBoolean(var1);
         this.allowAudioMixedMimeTypeAdaptiveness = Util.readBoolean(var1);
         this.allowAudioMixedSampleRateAdaptiveness = Util.readBoolean(var1);
         this.preferredTextLanguage = var1.readString();
         this.selectUndeterminedTextLanguage = Util.readBoolean(var1);
         this.disabledTextTrackSelectionFlags = var1.readInt();
         this.forceLowestBitrate = Util.readBoolean(var1);
         this.forceHighestSupportedBitrate = Util.readBoolean(var1);
         this.exceedRendererCapabilitiesIfNecessary = Util.readBoolean(var1);
         this.tunnelingAudioSessionId = var1.readInt();
         this.selectionOverrides = readSelectionOverrides(var1);
         this.rendererDisabledFlags = var1.readSparseBooleanArray();
         this.allowMixedMimeAdaptiveness = this.allowVideoMixedMimeTypeAdaptiveness;
         this.allowNonSeamlessAdaptiveness = this.allowVideoNonSeamlessAdaptiveness;
      }

      private static boolean areRendererDisabledFlagsEqual(SparseBooleanArray var0, SparseBooleanArray var1) {
         int var2 = var0.size();
         if (var1.size() != var2) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (var1.indexOfKey(var0.keyAt(var3)) < 0) {
                  return false;
               }
            }

            return true;
         }
      }

      private static boolean areSelectionOverridesEqual(SparseArray var0, SparseArray var1) {
         int var2 = var0.size();
         if (var1.size() != var2) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2; ++var3) {
               int var4 = var1.indexOfKey(var0.keyAt(var3));
               if (var4 < 0 || !areSelectionOverridesEqual((Map)var0.valueAt(var3), (Map)var1.valueAt(var4))) {
                  return false;
               }
            }

            return true;
         }
      }

      private static boolean areSelectionOverridesEqual(Map var0, Map var1) {
         int var2 = var0.size();
         if (var1.size() != var2) {
            return false;
         } else {
            Iterator var5 = var0.entrySet().iterator();

            Entry var3;
            TrackGroupArray var4;
            do {
               if (!var5.hasNext()) {
                  return true;
               }

               var3 = (Entry)var5.next();
               var4 = (TrackGroupArray)var3.getKey();
            } while(var1.containsKey(var4) && Util.areEqual(var3.getValue(), var1.get(var4)));

            return false;
         }
      }

      private static SparseArray readSelectionOverrides(Parcel var0) {
         int var1 = var0.readInt();
         SparseArray var2 = new SparseArray(var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            int var4 = var0.readInt();
            int var5 = var0.readInt();
            HashMap var6 = new HashMap(var5);

            for(int var7 = 0; var7 < var5; ++var7) {
               var6.put((TrackGroupArray)var0.readParcelable(TrackGroupArray.class.getClassLoader()), (DefaultTrackSelector.SelectionOverride)var0.readParcelable(DefaultTrackSelector.SelectionOverride.class.getClassLoader()));
            }

            var2.put(var4, var6);
         }

         return var2;
      }

      private static void writeSelectionOverridesToParcel(Parcel var0, SparseArray var1) {
         int var2 = var1.size();
         var0.writeInt(var2);

         for(int var3 = 0; var3 < var2; ++var3) {
            int var4 = var1.keyAt(var3);
            Map var5 = (Map)var1.valueAt(var3);
            int var6 = var5.size();
            var0.writeInt(var4);
            var0.writeInt(var6);
            Iterator var8 = var5.entrySet().iterator();

            while(var8.hasNext()) {
               Entry var7 = (Entry)var8.next();
               var0.writeParcelable((Parcelable)var7.getKey(), 0);
               var0.writeParcelable((Parcelable)var7.getValue(), 0);
            }
         }

      }

      public int describeContents() {
         return 0;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && DefaultTrackSelector.Parameters.class == var1.getClass()) {
            DefaultTrackSelector.Parameters var3 = (DefaultTrackSelector.Parameters)var1;
            if (this.maxVideoWidth != var3.maxVideoWidth || this.maxVideoHeight != var3.maxVideoHeight || this.maxVideoFrameRate != var3.maxVideoFrameRate || this.maxVideoBitrate != var3.maxVideoBitrate || this.exceedVideoConstraintsIfNecessary != var3.exceedVideoConstraintsIfNecessary || this.allowVideoMixedMimeTypeAdaptiveness != var3.allowVideoMixedMimeTypeAdaptiveness || this.allowVideoNonSeamlessAdaptiveness != var3.allowVideoNonSeamlessAdaptiveness || this.viewportOrientationMayChange != var3.viewportOrientationMayChange || this.viewportWidth != var3.viewportWidth || this.viewportHeight != var3.viewportHeight || !TextUtils.equals(this.preferredAudioLanguage, var3.preferredAudioLanguage) || this.maxAudioChannelCount != var3.maxAudioChannelCount || this.maxAudioBitrate != var3.maxAudioBitrate || this.exceedAudioConstraintsIfNecessary != var3.exceedAudioConstraintsIfNecessary || this.allowAudioMixedMimeTypeAdaptiveness != var3.allowAudioMixedMimeTypeAdaptiveness || this.allowAudioMixedSampleRateAdaptiveness != var3.allowAudioMixedSampleRateAdaptiveness || !TextUtils.equals(this.preferredTextLanguage, var3.preferredTextLanguage) || this.selectUndeterminedTextLanguage != var3.selectUndeterminedTextLanguage || this.disabledTextTrackSelectionFlags != var3.disabledTextTrackSelectionFlags || this.forceLowestBitrate != var3.forceLowestBitrate || this.forceHighestSupportedBitrate != var3.forceHighestSupportedBitrate || this.exceedRendererCapabilitiesIfNecessary != var3.exceedRendererCapabilitiesIfNecessary || this.tunnelingAudioSessionId != var3.tunnelingAudioSessionId || !areRendererDisabledFlagsEqual(this.rendererDisabledFlags, var3.rendererDisabledFlags) || !areSelectionOverridesEqual(this.selectionOverrides, var3.selectionOverrides)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public final boolean getRendererDisabled(int var1) {
         return this.rendererDisabledFlags.get(var1);
      }

      public final DefaultTrackSelector.SelectionOverride getSelectionOverride(int var1, TrackGroupArray var2) {
         Map var3 = (Map)this.selectionOverrides.get(var1);
         DefaultTrackSelector.SelectionOverride var4;
         if (var3 != null) {
            var4 = (DefaultTrackSelector.SelectionOverride)var3.get(var2);
         } else {
            var4 = null;
         }

         return var4;
      }

      public final boolean hasSelectionOverride(int var1, TrackGroupArray var2) {
         Map var3 = (Map)this.selectionOverrides.get(var1);
         boolean var4;
         if (var3 != null && var3.containsKey(var2)) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      public int hashCode() {
         int var1 = this.maxVideoWidth;
         int var2 = this.maxVideoHeight;
         int var3 = this.maxVideoFrameRate;
         int var4 = this.maxVideoBitrate;
         byte var5 = this.exceedVideoConstraintsIfNecessary;
         byte var6 = this.allowVideoMixedMimeTypeAdaptiveness;
         byte var7 = this.allowVideoNonSeamlessAdaptiveness;
         byte var8 = this.viewportOrientationMayChange;
         int var9 = this.viewportWidth;
         int var10 = this.viewportHeight;
         String var11 = this.preferredAudioLanguage;
         int var12 = 0;
         int var13;
         if (var11 == null) {
            var13 = 0;
         } else {
            var13 = var11.hashCode();
         }

         int var14 = this.maxAudioChannelCount;
         int var15 = this.maxAudioBitrate;
         byte var16 = this.exceedAudioConstraintsIfNecessary;
         byte var17 = this.allowAudioMixedMimeTypeAdaptiveness;
         byte var18 = this.allowAudioMixedSampleRateAdaptiveness;
         var11 = this.preferredTextLanguage;
         if (var11 != null) {
            var12 = var11.hashCode();
         }

         return ((((((((((((((((((((((var1 + 31) * 31 + var2) * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var10) * 31 + var13) * 31 + var14) * 31 + var15) * 31 + var16) * 31 + var17) * 31 + var18) * 31 + var12) * 31 + this.selectUndeterminedTextLanguage) * 31 + this.disabledTextTrackSelectionFlags) * 31 + this.forceLowestBitrate) * 31 + this.forceHighestSupportedBitrate) * 31 + this.exceedRendererCapabilitiesIfNecessary) * 31 + this.tunnelingAudioSessionId;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.maxVideoWidth);
         var1.writeInt(this.maxVideoHeight);
         var1.writeInt(this.maxVideoFrameRate);
         var1.writeInt(this.maxVideoBitrate);
         Util.writeBoolean(var1, this.exceedVideoConstraintsIfNecessary);
         Util.writeBoolean(var1, this.allowVideoMixedMimeTypeAdaptiveness);
         Util.writeBoolean(var1, this.allowVideoNonSeamlessAdaptiveness);
         var1.writeInt(this.viewportWidth);
         var1.writeInt(this.viewportHeight);
         Util.writeBoolean(var1, this.viewportOrientationMayChange);
         var1.writeString(this.preferredAudioLanguage);
         var1.writeInt(this.maxAudioChannelCount);
         var1.writeInt(this.maxAudioBitrate);
         Util.writeBoolean(var1, this.exceedAudioConstraintsIfNecessary);
         Util.writeBoolean(var1, this.allowAudioMixedMimeTypeAdaptiveness);
         Util.writeBoolean(var1, this.allowAudioMixedSampleRateAdaptiveness);
         var1.writeString(this.preferredTextLanguage);
         Util.writeBoolean(var1, this.selectUndeterminedTextLanguage);
         var1.writeInt(this.disabledTextTrackSelectionFlags);
         Util.writeBoolean(var1, this.forceLowestBitrate);
         Util.writeBoolean(var1, this.forceHighestSupportedBitrate);
         Util.writeBoolean(var1, this.exceedRendererCapabilitiesIfNecessary);
         var1.writeInt(this.tunnelingAudioSessionId);
         writeSelectionOverridesToParcel(var1, this.selectionOverrides);
         var1.writeSparseBooleanArray(this.rendererDisabledFlags);
      }
   }

   public static final class SelectionOverride implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public DefaultTrackSelector.SelectionOverride createFromParcel(Parcel var1) {
            return new DefaultTrackSelector.SelectionOverride(var1);
         }

         public DefaultTrackSelector.SelectionOverride[] newArray(int var1) {
            return new DefaultTrackSelector.SelectionOverride[var1];
         }
      };
      public final int groupIndex;
      public final int length;
      public final int[] tracks;

      SelectionOverride(Parcel var1) {
         this.groupIndex = var1.readInt();
         this.length = var1.readByte();
         this.tracks = new int[this.length];
         var1.readIntArray(this.tracks);
      }

      public int describeContents() {
         return 0;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && DefaultTrackSelector.SelectionOverride.class == var1.getClass()) {
            DefaultTrackSelector.SelectionOverride var3 = (DefaultTrackSelector.SelectionOverride)var1;
            if (this.groupIndex != var3.groupIndex || !Arrays.equals(this.tracks, var3.tracks)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.groupIndex * 31 + Arrays.hashCode(this.tracks);
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.groupIndex);
         var1.writeInt(this.tracks.length);
         var1.writeIntArray(this.tracks);
      }
   }
}
