package com.google.android.exoplayer2.text.ttml;

import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

final class TtmlNode {
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

    public static TtmlNode buildTextNode(String str) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(str), -9223372036854775807L, -9223372036854775807L, null, null, "", null);
    }

    public static TtmlNode buildNode(String str, long j, long j2, TtmlStyle ttmlStyle, String[] strArr, String str2, String str3) {
        return new TtmlNode(str, null, j, j2, ttmlStyle, strArr, str2, str3);
    }

    private TtmlNode(String str, String str2, long j, long j2, TtmlStyle ttmlStyle, String[] strArr, String str3, String str4) {
        this.tag = str;
        this.text = str2;
        this.imageId = str4;
        this.style = ttmlStyle;
        this.styleIds = strArr;
        this.isTextNode = str2 != null;
        this.startTimeUs = j;
        this.endTimeUs = j2;
        Assertions.checkNotNull(str3);
        this.regionId = str3;
        this.nodeStartsByRegion = new HashMap();
        this.nodeEndsByRegion = new HashMap();
    }

    public boolean isActive(long j) {
        return (this.startTimeUs == -9223372036854775807L && this.endTimeUs == -9223372036854775807L) || ((this.startTimeUs <= j && this.endTimeUs == -9223372036854775807L) || ((this.startTimeUs == -9223372036854775807L && j < this.endTimeUs) || (this.startTimeUs <= j && j < this.endTimeUs)));
    }

    public void addChild(TtmlNode ttmlNode) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(ttmlNode);
    }

    public TtmlNode getChild(int i) {
        List list = this.children;
        if (list != null) {
            return (TtmlNode) list.get(i);
        }
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount() {
        List list = this.children;
        return list == null ? 0 : list.size();
    }

    public long[] getEventTimesUs() {
        TreeSet treeSet = new TreeSet();
        int i = 0;
        getEventTimes(treeSet, false);
        long[] jArr = new long[treeSet.size()];
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            int i2 = i + 1;
            jArr[i] = ((Long) it.next()).longValue();
            i = i2;
        }
        return jArr;
    }

    private void getEventTimes(TreeSet<Long> treeSet, boolean z) {
        boolean equals = "p".equals(this.tag);
        boolean equals2 = "div".equals(this.tag);
        if (z || equals || (equals2 && this.imageId != null)) {
            long j = this.startTimeUs;
            if (j != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j));
            }
            j = this.endTimeUs;
            if (j != -9223372036854775807L) {
                treeSet.add(Long.valueOf(j));
            }
        }
        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                TtmlNode ttmlNode = (TtmlNode) this.children.get(i);
                boolean z2 = z || equals;
                ttmlNode.getEventTimes(treeSet, z2);
            }
        }
    }

    public List<Cue> getCues(long j, Map<String, TtmlStyle> map, Map<String, TtmlRegion> map2, Map<String, String> map3) {
        long j2 = j;
        Map<String, TtmlRegion> map4 = map2;
        ArrayList<Pair> arrayList = new ArrayList();
        traverseForImage(j2, this.regionId, arrayList);
        TreeMap treeMap = new TreeMap();
        traverseForText(j, false, this.regionId, treeMap);
        traverseForStyle(j2, map, treeMap);
        ArrayList arrayList2 = new ArrayList();
        for (Pair pair : arrayList) {
            String str = (String) map3.get(pair.second);
            if (str != null) {
                byte[] decode = Base64.decode(str, 0);
                TtmlRegion ttmlRegion = (TtmlRegion) map4.get(pair.first);
                arrayList2.add(new Cue(BitmapFactory.decodeByteArray(decode, 0, decode.length), ttmlRegion.position, 1, ttmlRegion.line, ttmlRegion.lineAnchor, ttmlRegion.width, Float.MIN_VALUE));
            }
        }
        for (Entry entry : treeMap.entrySet()) {
            TtmlRegion ttmlRegion2 = (TtmlRegion) map4.get(entry.getKey());
            CharSequence charSequence = (SpannableStringBuilder) entry.getValue();
            cleanUpText(charSequence);
            arrayList2.add(new Cue(charSequence, null, ttmlRegion2.line, ttmlRegion2.lineType, ttmlRegion2.lineAnchor, ttmlRegion2.position, Integer.MIN_VALUE, ttmlRegion2.width, ttmlRegion2.textSizeType, ttmlRegion2.textSize));
        }
        return arrayList2;
    }

    private void traverseForImage(long j, String str, List<Pair<String, String>> list) {
        Object str2;
        if (!"".equals(this.regionId)) {
            str2 = this.regionId;
        }
        if (isActive(j)) {
            if ("div".equals(this.tag)) {
                String str3 = this.imageId;
                if (str3 != null) {
                    list.add(new Pair(str2, str3));
                    return;
                }
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChild(i).traverseForImage(j, str2, list);
        }
    }

    private void traverseForText(long j, boolean z, String str, Map<String, SpannableStringBuilder> map) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        if (!"metadata".equals(this.tag)) {
            if (!"".equals(this.regionId)) {
                str = this.regionId;
            }
            if (this.isTextNode && z) {
                getRegionOutput(str, map).append(this.text);
            } else {
                if ("br".equals(this.tag) && z) {
                    getRegionOutput(str, map).append(10);
                } else if (isActive(j)) {
                    for (Entry entry : map.entrySet()) {
                        this.nodeStartsByRegion.put(entry.getKey(), Integer.valueOf(((SpannableStringBuilder) entry.getValue()).length()));
                    }
                    boolean equals = "p".equals(this.tag);
                    for (int i = 0; i < getChildCount(); i++) {
                        TtmlNode child = getChild(i);
                        boolean z2 = z || equals;
                        child.traverseForText(j, z2, str, map);
                    }
                    if (equals) {
                        TtmlRenderUtil.endParagraph(getRegionOutput(str, map));
                    }
                    for (Entry entry2 : map.entrySet()) {
                        this.nodeEndsByRegion.put(entry2.getKey(), Integer.valueOf(((SpannableStringBuilder) entry2.getValue()).length()));
                    }
                }
            }
        }
    }

    private static SpannableStringBuilder getRegionOutput(String str, Map<String, SpannableStringBuilder> map) {
        if (!map.containsKey(str)) {
            map.put(str, new SpannableStringBuilder());
        }
        return (SpannableStringBuilder) map.get(str);
    }

    private void traverseForStyle(long j, Map<String, TtmlStyle> map, Map<String, SpannableStringBuilder> map2) {
        if (isActive(j)) {
            int i;
            Iterator it = this.nodeEndsByRegion.entrySet().iterator();
            while (true) {
                i = 0;
                if (!it.hasNext()) {
                    break;
                }
                Entry entry = (Entry) it.next();
                String str = (String) entry.getKey();
                if (this.nodeStartsByRegion.containsKey(str)) {
                    i = ((Integer) this.nodeStartsByRegion.get(str)).intValue();
                }
                int intValue = ((Integer) entry.getValue()).intValue();
                if (i != intValue) {
                    applyStyleToOutput(map, (SpannableStringBuilder) map2.get(str), i, intValue);
                }
            }
            while (i < getChildCount()) {
                getChild(i).traverseForStyle(j, map, map2);
                i++;
            }
        }
    }

    private void applyStyleToOutput(Map<String, TtmlStyle> map, SpannableStringBuilder spannableStringBuilder, int i, int i2) {
        TtmlStyle resolveStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, map);
        if (resolveStyle != null) {
            TtmlRenderUtil.applyStylesToSpan(spannableStringBuilder, i, i2, resolveStyle);
        }
    }

    private SpannableStringBuilder cleanUpText(SpannableStringBuilder spannableStringBuilder) {
        int i;
        int i2;
        int i3 = 0;
        int length = spannableStringBuilder.length();
        for (i = 0; i < length; i++) {
            if (spannableStringBuilder.charAt(i) == ' ') {
                i2 = i + 1;
                int i4 = i2;
                while (i4 < spannableStringBuilder.length() && spannableStringBuilder.charAt(i4) == ' ') {
                    i4++;
                }
                i4 -= i2;
                if (i4 > 0) {
                    spannableStringBuilder.delete(i, i + i4);
                    length -= i4;
                }
            }
        }
        if (length > 0 && spannableStringBuilder.charAt(0) == ' ') {
            spannableStringBuilder.delete(0, 1);
            length--;
        }
        i = 0;
        while (true) {
            i2 = length - 1;
            if (i >= i2) {
                break;
            }
            if (spannableStringBuilder.charAt(i) == 10) {
                i2 = i + 1;
                if (spannableStringBuilder.charAt(i2) == ' ') {
                    spannableStringBuilder.delete(i2, i + 2);
                    length--;
                }
            }
            i++;
        }
        if (length > 0 && spannableStringBuilder.charAt(i2) == ' ') {
            spannableStringBuilder.delete(i2, length);
            length--;
        }
        while (true) {
            i = length - 1;
            if (i3 >= i) {
                break;
            }
            if (spannableStringBuilder.charAt(i3) == ' ') {
                i = i3 + 1;
                if (spannableStringBuilder.charAt(i) == 10) {
                    spannableStringBuilder.delete(i3, i);
                    length--;
                }
            }
            i3++;
        }
        if (length > 0 && spannableStringBuilder.charAt(i) == 10) {
            spannableStringBuilder.delete(i, length);
        }
        return spannableStringBuilder;
    }
}
