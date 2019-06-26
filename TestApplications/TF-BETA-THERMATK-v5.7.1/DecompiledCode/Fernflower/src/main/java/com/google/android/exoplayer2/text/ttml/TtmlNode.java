package com.google.android.exoplayer2.text.ttml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.Layout.Alignment;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

final class TtmlNode {
   private List children;
   public final long endTimeUs;
   public final String imageId;
   public final boolean isTextNode;
   private final HashMap nodeEndsByRegion;
   private final HashMap nodeStartsByRegion;
   public final String regionId;
   public final long startTimeUs;
   public final TtmlStyle style;
   private final String[] styleIds;
   public final String tag;
   public final String text;

   private TtmlNode(String var1, String var2, long var3, long var5, TtmlStyle var7, String[] var8, String var9, String var10) {
      this.tag = var1;
      this.text = var2;
      this.imageId = var10;
      this.style = var7;
      this.styleIds = var8;
      boolean var11;
      if (var2 != null) {
         var11 = true;
      } else {
         var11 = false;
      }

      this.isTextNode = var11;
      this.startTimeUs = var3;
      this.endTimeUs = var5;
      Assertions.checkNotNull(var9);
      this.regionId = (String)var9;
      this.nodeStartsByRegion = new HashMap();
      this.nodeEndsByRegion = new HashMap();
   }

   private void applyStyleToOutput(Map var1, SpannableStringBuilder var2, int var3, int var4) {
      TtmlStyle var5 = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, var1);
      if (var5 != null) {
         TtmlRenderUtil.applyStylesToSpan(var2, var3, var4, var5);
      }

   }

   public static TtmlNode buildNode(String var0, long var1, long var3, TtmlStyle var5, String[] var6, String var7, String var8) {
      return new TtmlNode(var0, (String)null, var1, var3, var5, var6, var7, var8);
   }

   public static TtmlNode buildTextNode(String var0) {
      return new TtmlNode((String)null, TtmlRenderUtil.applyTextElementSpacePolicy(var0), -9223372036854775807L, -9223372036854775807L, (TtmlStyle)null, (String[])null, "", (String)null);
   }

   private SpannableStringBuilder cleanUpText(SpannableStringBuilder var1) {
      int var2 = var1.length();
      byte var3 = 0;

      int var4;
      int var5;
      int var6;
      for(var4 = 0; var4 < var2; var2 = var5) {
         var5 = var2;
         if (var1.charAt(var4) == ' ') {
            var6 = var4 + 1;

            for(var5 = var6; var5 < var1.length() && var1.charAt(var5) == ' '; ++var5) {
            }

            var6 = var5 - var6;
            var5 = var2;
            if (var6 > 0) {
               var1.delete(var4, var4 + var6);
               var5 = var2 - var6;
            }
         }

         ++var4;
      }

      var4 = var2;
      if (var2 > 0) {
         var4 = var2;
         if (var1.charAt(0) == ' ') {
            var1.delete(0, 1);
            var4 = var2 - 1;
         }
      }

      byte var8 = 0;
      var2 = var4;
      var4 = var8;

      while(true) {
         var6 = var2 - 1;
         if (var4 >= var6) {
            var5 = var3;
            var4 = var2;
            if (var2 > 0) {
               var5 = var3;
               var4 = var2;
               if (var1.charAt(var6) == ' ') {
                  var1.delete(var6, var2);
                  var4 = var2 - 1;
                  var5 = var3;
               }
            }

            while(true) {
               var2 = var4 - 1;
               if (var5 >= var2) {
                  if (var4 > 0 && var1.charAt(var2) == '\n') {
                     var1.delete(var2, var4);
                  }

                  return var1;
               }

               var2 = var4;
               if (var1.charAt(var5) == ' ') {
                  int var7 = var5 + 1;
                  var2 = var4;
                  if (var1.charAt(var7) == '\n') {
                     var1.delete(var5, var7);
                     var2 = var4 - 1;
                  }
               }

               ++var5;
               var4 = var2;
            }
         }

         var5 = var2;
         if (var1.charAt(var4) == '\n') {
            var6 = var4 + 1;
            var5 = var2;
            if (var1.charAt(var6) == ' ') {
               var1.delete(var6, var4 + 2);
               var5 = var2 - 1;
            }
         }

         ++var4;
         var2 = var5;
      }
   }

   private void getEventTimes(TreeSet var1, boolean var2) {
      boolean var3 = "p".equals(this.tag);
      boolean var4 = "div".equals(this.tag);
      if (var2 || var3 || var4 && this.imageId != null) {
         long var5 = this.startTimeUs;
         if (var5 != -9223372036854775807L) {
            var1.add(var5);
         }

         var5 = this.endTimeUs;
         if (var5 != -9223372036854775807L) {
            var1.add(var5);
         }
      }

      if (this.children != null) {
         for(int var7 = 0; var7 < this.children.size(); ++var7) {
            TtmlNode var8 = (TtmlNode)this.children.get(var7);
            if (!var2 && !var3) {
               var4 = false;
            } else {
               var4 = true;
            }

            var8.getEventTimes(var1, var4);
         }

      }
   }

   private static SpannableStringBuilder getRegionOutput(String var0, Map var1) {
      if (!var1.containsKey(var0)) {
         var1.put(var0, new SpannableStringBuilder());
      }

      return (SpannableStringBuilder)var1.get(var0);
   }

   private void traverseForImage(long var1, String var3, List var4) {
      if (!"".equals(this.regionId)) {
         var3 = this.regionId;
      }

      if (this.isActive(var1) && "div".equals(this.tag)) {
         String var5 = this.imageId;
         if (var5 != null) {
            var4.add(new Pair(var3, var5));
            return;
         }
      }

      for(int var6 = 0; var6 < this.getChildCount(); ++var6) {
         this.getChild(var6).traverseForImage(var1, var3, var4);
      }

   }

   private void traverseForStyle(long var1, Map var3, Map var4) {
      if (this.isActive(var1)) {
         Iterator var5 = this.nodeEndsByRegion.entrySet().iterator();

         while(true) {
            boolean var6 = var5.hasNext();
            int var7 = 0;
            byte var8 = 0;
            if (!var6) {
               while(var7 < this.getChildCount()) {
                  this.getChild(var7).traverseForStyle(var1, var3, var4);
                  ++var7;
               }

               return;
            }

            Entry var9 = (Entry)var5.next();
            String var10 = (String)var9.getKey();
            var7 = var8;
            if (this.nodeStartsByRegion.containsKey(var10)) {
               var7 = (Integer)this.nodeStartsByRegion.get(var10);
            }

            int var11 = (Integer)var9.getValue();
            if (var7 != var11) {
               this.applyStyleToOutput(var3, (SpannableStringBuilder)var4.get(var10), var7, var11);
            }
         }
      }
   }

   private void traverseForText(long var1, boolean var3, String var4, Map var5) {
      this.nodeStartsByRegion.clear();
      this.nodeEndsByRegion.clear();
      if (!"metadata".equals(this.tag)) {
         if (!"".equals(this.regionId)) {
            var4 = this.regionId;
         }

         if (this.isTextNode && var3) {
            getRegionOutput(var4, var5).append(this.text);
         } else if ("br".equals(this.tag) && var3) {
            getRegionOutput(var4, var5).append('\n');
         } else if (this.isActive(var1)) {
            Iterator var6 = var5.entrySet().iterator();

            while(var6.hasNext()) {
               Entry var7 = (Entry)var6.next();
               this.nodeStartsByRegion.put(var7.getKey(), ((SpannableStringBuilder)var7.getValue()).length());
            }

            boolean var8 = "p".equals(this.tag);

            for(int var9 = 0; var9 < this.getChildCount(); ++var9) {
               TtmlNode var13 = this.getChild(var9);
               boolean var10;
               if (!var3 && !var8) {
                  var10 = false;
               } else {
                  var10 = true;
               }

               var13.traverseForText(var1, var10, var4, var5);
            }

            if (var8) {
               TtmlRenderUtil.endParagraph(getRegionOutput(var4, var5));
            }

            Iterator var11 = var5.entrySet().iterator();

            while(var11.hasNext()) {
               Entry var12 = (Entry)var11.next();
               this.nodeEndsByRegion.put(var12.getKey(), ((SpannableStringBuilder)var12.getValue()).length());
            }
         }

      }
   }

   public void addChild(TtmlNode var1) {
      if (this.children == null) {
         this.children = new ArrayList();
      }

      this.children.add(var1);
   }

   public TtmlNode getChild(int var1) {
      List var2 = this.children;
      if (var2 != null) {
         return (TtmlNode)var2.get(var1);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int getChildCount() {
      List var1 = this.children;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else {
         var2 = var1.size();
      }

      return var2;
   }

   public List getCues(long var1, Map var3, Map var4, Map var5) {
      ArrayList var6 = new ArrayList();
      this.traverseForImage(var1, this.regionId, var6);
      TreeMap var7 = new TreeMap();
      this.traverseForText(var1, false, this.regionId, var7);
      this.traverseForStyle(var1, var3, var7);
      ArrayList var10 = new ArrayList();
      Iterator var12 = var6.iterator();

      while(var12.hasNext()) {
         Pair var8 = (Pair)var12.next();
         String var9 = (String)var5.get(var8.second);
         if (var9 != null) {
            byte[] var17 = Base64.decode(var9, 0);
            Bitmap var18 = BitmapFactory.decodeByteArray(var17, 0, var17.length);
            TtmlRegion var16 = (TtmlRegion)var4.get(var8.first);
            var10.add(new Cue(var18, var16.position, 1, var16.line, var16.lineAnchor, var16.width, Float.MIN_VALUE));
         }
      }

      Iterator var15 = var7.entrySet().iterator();

      while(var15.hasNext()) {
         Entry var13 = (Entry)var15.next();
         TtmlRegion var11 = (TtmlRegion)var4.get(var13.getKey());
         SpannableStringBuilder var14 = (SpannableStringBuilder)var13.getValue();
         this.cleanUpText(var14);
         var10.add(new Cue(var14, (Alignment)null, var11.line, var11.lineType, var11.lineAnchor, var11.position, Integer.MIN_VALUE, var11.width, var11.textSizeType, var11.textSize));
      }

      return var10;
   }

   public long[] getEventTimesUs() {
      TreeSet var1 = new TreeSet();
      int var2 = 0;
      this.getEventTimes(var1, false);
      long[] var3 = new long[var1.size()];

      for(Iterator var4 = var1.iterator(); var4.hasNext(); ++var2) {
         var3[var2] = (Long)var4.next();
      }

      return var3;
   }

   public boolean isActive(long var1) {
      boolean var3;
      if ((this.startTimeUs != -9223372036854775807L || this.endTimeUs != -9223372036854775807L) && (this.startTimeUs > var1 || this.endTimeUs != -9223372036854775807L) && (this.startTimeUs != -9223372036854775807L || var1 >= this.endTimeUs) && (this.startTimeUs > var1 || var1 >= this.endTimeUs)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }
}
