// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

import android.graphics.Bitmap;
import android.text.Layout$Alignment;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.util.TreeMap;
import com.google.android.exoplayer2.text.Cue;
import java.util.ArrayList;
import java.util.Iterator;
import android.util.Pair;
import java.util.TreeSet;
import android.text.SpannableStringBuilder;
import java.util.Map;
import com.google.android.exoplayer2.util.Assertions;
import java.util.HashMap;
import java.util.List;

final class TtmlNode
{
    private List<TtmlNode> children;
    public final long endTimeUs;
    public final String imageId;
    public final boolean isTextNode;
    private final HashMap<String, Integer> nodeEndsByRegion;
    private final HashMap<String, Integer> nodeStartsByRegion;
    public final String regionId;
    public final long startTimeUs;
    public final TtmlStyle style;
    private final String[] styleIds;
    public final String tag;
    public final String text;
    
    private TtmlNode(final String tag, final String text, final long startTimeUs, final long endTimeUs, final TtmlStyle style, final String[] styleIds, final String s, final String imageId) {
        this.tag = tag;
        this.text = text;
        this.imageId = imageId;
        this.style = style;
        this.styleIds = styleIds;
        this.isTextNode = (text != null);
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
        Assertions.checkNotNull(s);
        this.regionId = s;
        this.nodeStartsByRegion = new HashMap<String, Integer>();
        this.nodeEndsByRegion = new HashMap<String, Integer>();
    }
    
    private void applyStyleToOutput(final Map<String, TtmlStyle> map, final SpannableStringBuilder spannableStringBuilder, final int n, final int n2) {
        final TtmlStyle resolveStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, map);
        if (resolveStyle != null) {
            TtmlRenderUtil.applyStylesToSpan(spannableStringBuilder, n, n2, resolveStyle);
        }
    }
    
    public static TtmlNode buildNode(final String s, final long n, final long n2, final TtmlStyle ttmlStyle, final String[] array, final String s2, final String s3) {
        return new TtmlNode(s, null, n, n2, ttmlStyle, array, s2, s3);
    }
    
    public static TtmlNode buildTextNode(final String s) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(s), -9223372036854775807L, -9223372036854775807L, null, null, "", null);
    }
    
    private SpannableStringBuilder cleanUpText(final SpannableStringBuilder spannableStringBuilder) {
        int length = spannableStringBuilder.length();
        final int n = 0;
        int n2;
        for (int i = 0; i < length; ++i, length = n2) {
            n2 = length;
            if (spannableStringBuilder.charAt(i) == ' ') {
                int n4;
                int n3;
                for (n3 = (n4 = i + 1); n4 < spannableStringBuilder.length() && spannableStringBuilder.charAt(n4) == ' '; ++n4) {}
                final int n5 = n4 - n3;
                n2 = length;
                if (n5 > 0) {
                    spannableStringBuilder.delete(i, i + n5);
                    n2 = length - n5;
                }
            }
        }
        int n6;
        if ((n6 = length) > 0) {
            n6 = length;
            if (spannableStringBuilder.charAt(0) == ' ') {
                spannableStringBuilder.delete(0, 1);
                n6 = length - 1;
            }
        }
        final int n7 = 0;
        int n8 = n6;
        int n9 = n7;
        int n10;
        while (true) {
            n10 = n8 - 1;
            if (n9 >= n10) {
                break;
            }
            int n11 = n8;
            if (spannableStringBuilder.charAt(n9) == '\n') {
                final int n12 = n9 + 1;
                n11 = n8;
                if (spannableStringBuilder.charAt(n12) == ' ') {
                    spannableStringBuilder.delete(n12, n9 + 2);
                    n11 = n8 - 1;
                }
            }
            ++n9;
            n8 = n11;
        }
        int n13 = n;
        int n14;
        if ((n14 = n8) > 0) {
            n13 = n;
            n14 = n8;
            if (spannableStringBuilder.charAt(n10) == ' ') {
                spannableStringBuilder.delete(n10, n8);
                n14 = n8 - 1;
                n13 = n;
            }
        }
        int n15;
        while (true) {
            n15 = n14 - 1;
            if (n13 >= n15) {
                break;
            }
            int n16 = n14;
            if (spannableStringBuilder.charAt(n13) == ' ') {
                final int n17 = n13 + 1;
                n16 = n14;
                if (spannableStringBuilder.charAt(n17) == '\n') {
                    spannableStringBuilder.delete(n13, n17);
                    n16 = n14 - 1;
                }
            }
            ++n13;
            n14 = n16;
        }
        if (n14 > 0 && spannableStringBuilder.charAt(n15) == '\n') {
            spannableStringBuilder.delete(n15, n14);
        }
        return spannableStringBuilder;
    }
    
    private void getEventTimes(final TreeSet<Long> set, final boolean b) {
        final boolean equals = "p".equals(this.tag);
        final boolean equals2 = "div".equals(this.tag);
        if (b || equals || (equals2 && this.imageId != null)) {
            final long startTimeUs = this.startTimeUs;
            if (startTimeUs != -9223372036854775807L) {
                set.add(startTimeUs);
            }
            final long endTimeUs = this.endTimeUs;
            if (endTimeUs != -9223372036854775807L) {
                set.add(endTimeUs);
            }
        }
        if (this.children == null) {
            return;
        }
        for (int i = 0; i < this.children.size(); ++i) {
            this.children.get(i).getEventTimes(set, b || equals);
        }
    }
    
    private static SpannableStringBuilder getRegionOutput(final String s, final Map<String, SpannableStringBuilder> map) {
        if (!map.containsKey(s)) {
            map.put(s, new SpannableStringBuilder());
        }
        return map.get(s);
    }
    
    private void traverseForImage(final long n, String regionId, final List<Pair<String, String>> list) {
        if (!"".equals(this.regionId)) {
            regionId = this.regionId;
        }
        if (this.isActive(n) && "div".equals(this.tag)) {
            final String imageId = this.imageId;
            if (imageId != null) {
                list.add((Pair<String, String>)new Pair((Object)regionId, (Object)imageId));
                return;
            }
        }
        for (int i = 0; i < this.getChildCount(); ++i) {
            this.getChild(i).traverseForImage(n, regionId, list);
        }
    }
    
    private void traverseForStyle(final long n, final Map<String, TtmlStyle> map, final Map<String, SpannableStringBuilder> map2) {
        if (!this.isActive(n)) {
            return;
        }
        final Iterator<Map.Entry<String, Integer>> iterator = this.nodeEndsByRegion.entrySet().iterator();
        int i;
        while (true) {
            final boolean hasNext = iterator.hasNext();
            i = 0;
            final int n2 = 0;
            if (!hasNext) {
                break;
            }
            final Map.Entry<String, Integer> entry = iterator.next();
            final String s = entry.getKey();
            int intValue = n2;
            if (this.nodeStartsByRegion.containsKey(s)) {
                intValue = this.nodeStartsByRegion.get(s);
            }
            final int intValue2 = entry.getValue();
            if (intValue == intValue2) {
                continue;
            }
            this.applyStyleToOutput(map, map2.get(s), intValue, intValue2);
        }
        while (i < this.getChildCount()) {
            this.getChild(i).traverseForStyle(n, map, map2);
            ++i;
        }
    }
    
    private void traverseForText(final long n, final boolean b, String regionId, final Map<String, SpannableStringBuilder> map) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        if ("metadata".equals(this.tag)) {
            return;
        }
        if (!"".equals(this.regionId)) {
            regionId = this.regionId;
        }
        if (this.isTextNode && b) {
            getRegionOutput(regionId, map).append((CharSequence)this.text);
        }
        else if ("br".equals(this.tag) && b) {
            getRegionOutput(regionId, map).append('\n');
        }
        else if (this.isActive(n)) {
            for (final Map.Entry<String, SpannableStringBuilder> entry : map.entrySet()) {
                this.nodeStartsByRegion.put(entry.getKey(), entry.getValue().length());
            }
            final boolean equals = "p".equals(this.tag);
            for (int i = 0; i < this.getChildCount(); ++i) {
                this.getChild(i).traverseForText(n, b || equals, regionId, map);
            }
            if (equals) {
                TtmlRenderUtil.endParagraph(getRegionOutput(regionId, map));
            }
            for (final Map.Entry<String, SpannableStringBuilder> entry2 : map.entrySet()) {
                this.nodeEndsByRegion.put(entry2.getKey(), entry2.getValue().length());
            }
        }
    }
    
    public void addChild(final TtmlNode ttmlNode) {
        if (this.children == null) {
            this.children = new ArrayList<TtmlNode>();
        }
        this.children.add(ttmlNode);
    }
    
    public TtmlNode getChild(final int n) {
        final List<TtmlNode> children = this.children;
        if (children != null) {
            return children.get(n);
        }
        throw new IndexOutOfBoundsException();
    }
    
    public int getChildCount() {
        final List<TtmlNode> children = this.children;
        int size;
        if (children == null) {
            size = 0;
        }
        else {
            size = children.size();
        }
        return size;
    }
    
    public List<Cue> getCues(final long n, final Map<String, TtmlStyle> map, final Map<String, TtmlRegion> map2, final Map<String, String> map3) {
        final ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        this.traverseForImage(n, this.regionId, list);
        final TreeMap<String, SpannableStringBuilder> treeMap = new TreeMap<String, SpannableStringBuilder>();
        this.traverseForText(n, false, this.regionId, treeMap);
        this.traverseForStyle(n, map, treeMap);
        final ArrayList<Cue> list2 = new ArrayList<Cue>();
        for (final Pair pair : list) {
            final String s = map3.get(pair.second);
            if (s == null) {
                continue;
            }
            final byte[] decode = Base64.decode(s, 0);
            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            final TtmlRegion ttmlRegion = map2.get(pair.first);
            list2.add(new Cue(decodeByteArray, ttmlRegion.position, 1, ttmlRegion.line, ttmlRegion.lineAnchor, ttmlRegion.width, Float.MIN_VALUE));
        }
        for (final Map.Entry<String, SpannableStringBuilder> entry : treeMap.entrySet()) {
            final TtmlRegion ttmlRegion2 = map2.get(entry.getKey());
            final SpannableStringBuilder spannableStringBuilder = entry.getValue();
            this.cleanUpText(spannableStringBuilder);
            list2.add(new Cue((CharSequence)spannableStringBuilder, null, ttmlRegion2.line, ttmlRegion2.lineType, ttmlRegion2.lineAnchor, ttmlRegion2.position, Integer.MIN_VALUE, ttmlRegion2.width, ttmlRegion2.textSizeType, ttmlRegion2.textSize));
        }
        return list2;
    }
    
    public long[] getEventTimesUs() {
        final TreeSet<Long> set = new TreeSet<Long>();
        int n = 0;
        this.getEventTimes(set, false);
        final long[] array = new long[set.size()];
        final Iterator<Long> iterator = set.iterator();
        while (iterator.hasNext()) {
            array[n] = iterator.next();
            ++n;
        }
        return array;
    }
    
    public boolean isActive(final long n) {
        return (this.startTimeUs == -9223372036854775807L && this.endTimeUs == -9223372036854775807L) || (this.startTimeUs <= n && this.endTimeUs == -9223372036854775807L) || (this.startTimeUs == -9223372036854775807L && n < this.endTimeUs) || (this.startTimeUs <= n && n < this.endTimeUs);
    }
}
