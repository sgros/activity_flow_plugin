// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import java.util.Iterator;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Pair;
import java.util.Collection;

class StringsKt__StringsKt extends StringsKt__StringsJVMKt
{
    public static final boolean contains(final CharSequence charSequence, final CharSequence charSequence2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        final boolean b2 = charSequence2 instanceof String;
        boolean b3 = false;
        if (b2) {
            if (indexOf$default(charSequence, (String)charSequence2, 0, b, 2, null) < 0) {
                return b3;
            }
        }
        else if (indexOf$StringsKt__StringsKt$default(charSequence, charSequence2, 0, charSequence.length(), b, false, 16, null) < 0) {
            return b3;
        }
        b3 = true;
        return b3;
    }
    
    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(final CharSequence charSequence, final Collection<String> collection, int i, final boolean b, final boolean b2) {
        final Pair<Integer, String> pair = null;
        if (!b && collection.size() == 1) {
            final String s = CollectionsKt___CollectionsKt.single((Iterable<? extends String>)collection);
            if (!b2) {
                i = indexOf$default(charSequence, s, i, false, 4, null);
            }
            else {
                i = lastIndexOf$default(charSequence, s, i, false, 4, null);
            }
            Pair<Integer, String> to;
            if (i < 0) {
                to = pair;
            }
            else {
                to = TuplesKt.to(i, s);
            }
            return to;
        }
        IntProgression downTo;
        if (!b2) {
            downTo = new IntRange(RangesKt___RangesKt.coerceAtLeast(i, 0), charSequence.length());
        }
        else {
            downTo = RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(i, getLastIndex(charSequence)), 0);
        }
        Label_0407: {
            if (charSequence instanceof String) {
                i = downTo.getFirst();
                final int last = downTo.getLast();
                final int step = downTo.getStep();
                if (step > 0) {
                    if (i > last) {
                        return null;
                    }
                }
                else if (i < last) {
                    return null;
                }
            Label_0169:
                while (true) {
                    while (true) {
                        for (final String next : (Collection<? extends T>)collection) {
                            final String s2 = next;
                            if (StringsKt__StringsJVMKt.regionMatches(s2, 0, (String)charSequence, i, s2.length(), b)) {
                                final String s3 = next;
                                if (s3 != null) {
                                    return TuplesKt.to(i, s3);
                                }
                                if (i != last) {
                                    i += step;
                                    continue Label_0169;
                                }
                                break Label_0407;
                            }
                        }
                        String next = null;
                        continue;
                    }
                }
            }
            else {
                i = downTo.getFirst();
                final int last2 = downTo.getLast();
                final int step2 = downTo.getStep();
                if (step2 > 0) {
                    if (i > last2) {
                        return null;
                    }
                }
                else if (i < last2) {
                    return null;
                }
            Label_0308:
                while (true) {
                    while (true) {
                        for (final String next2 : (Collection<? extends T>)collection) {
                            final String s4 = next2;
                            if (regionMatchesImpl(s4, 0, charSequence, i, s4.length(), b)) {
                                final String s5 = next2;
                                if (s5 != null) {
                                    return TuplesKt.to(i, s5);
                                }
                                if (i != last2) {
                                    i += step2;
                                    continue Label_0308;
                                }
                                break Label_0407;
                            }
                        }
                        String next2 = null;
                        continue;
                    }
                }
            }
        }
        return null;
    }
    
    public static final IntRange getIndices(final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        return new IntRange(0, charSequence.length() - 1);
    }
    
    public static final int getLastIndex(final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        return charSequence.length() - 1;
    }
    
    public static final int indexOf(final CharSequence charSequence, final char ch, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).indexOf(ch, fromIndex);
        }
        else {
            fromIndex = indexOfAny(charSequence, new char[] { ch }, fromIndex, b);
        }
        return fromIndex;
    }
    
    public static final int indexOf(final CharSequence charSequence, final String str, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).indexOf(str, fromIndex);
        }
        else {
            fromIndex = indexOf$StringsKt__StringsKt$default(charSequence, str, fromIndex, charSequence.length(), b, false, 16, null);
        }
        return fromIndex;
    }
    
    private static final int indexOf$StringsKt__StringsKt(final CharSequence charSequence, final CharSequence charSequence2, int n, int n2, final boolean b, final boolean b2) {
        IntProgression downTo;
        if (!b2) {
            downTo = new IntRange(RangesKt___RangesKt.coerceAtLeast(n, 0), RangesKt___RangesKt.coerceAtMost(n2, charSequence.length()));
        }
        else {
            downTo = RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(n, getLastIndex(charSequence)), RangesKt___RangesKt.coerceAtLeast(n2, 0));
        }
        if (charSequence instanceof String && charSequence2 instanceof String) {
            n = downTo.getFirst();
            n2 = downTo.getLast();
            final int step = downTo.getStep();
            if (step > 0) {
                if (n > n2) {
                    return -1;
                }
            }
            else if (n < n2) {
                return -1;
            }
            while (!StringsKt__StringsJVMKt.regionMatches((String)charSequence2, 0, (String)charSequence, n, charSequence2.length(), b)) {
                if (n == n2) {
                    return -1;
                }
                n += step;
            }
            return n;
        }
        n = downTo.getFirst();
        n2 = downTo.getLast();
        final int step2 = downTo.getStep();
        if (step2 > 0) {
            if (n > n2) {
                return -1;
            }
        }
        else if (n < n2) {
            return -1;
        }
        while (!regionMatchesImpl(charSequence2, 0, charSequence, n, charSequence2.length(), b)) {
            if (n == n2) {
                return -1;
            }
            n += step2;
        }
        return n;
    }
    
    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(final CharSequence charSequence, final CharSequence charSequence2, final int n, final int n2, final boolean b, boolean b2, final int n3, final Object o) {
        if ((n3 & 0x10) != 0x0) {
            b2 = false;
        }
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, n, n2, b, b2);
    }
    
    public static /* synthetic */ int indexOf$default(final CharSequence charSequence, final String s, int n, boolean b, final int n2, final Object o) {
        if ((n2 & 0x2) != 0x0) {
            n = 0;
        }
        if ((n2 & 0x4) != 0x0) {
            b = false;
        }
        return indexOf(charSequence, s, n, b);
    }
    
    public static final int indexOfAny(final CharSequence charSequence, final char[] array, int coerceAtLeast, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "chars");
        if (!b && array.length == 1 && charSequence instanceof String) {
            return ((String)charSequence).indexOf(ArraysKt___ArraysKt.single(array), coerceAtLeast);
        }
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(coerceAtLeast, 0);
        final int lastIndex = getLastIndex(charSequence);
        Label_0134: {
            if (coerceAtLeast <= lastIndex) {
            Label_0064:
                while (true) {
                    final char char1 = charSequence.charAt(coerceAtLeast);
                    final int length = array.length;
                    int i = 0;
                    while (true) {
                        while (i < length) {
                            if (CharsKt__CharKt.equals(array[i], char1, b)) {
                                final boolean b2 = true;
                                if (b2) {
                                    return coerceAtLeast;
                                }
                                if (coerceAtLeast != lastIndex) {
                                    ++coerceAtLeast;
                                    continue Label_0064;
                                }
                                break Label_0134;
                            }
                            else {
                                ++i;
                            }
                        }
                        final boolean b2 = false;
                        continue;
                    }
                }
            }
        }
        return -1;
    }
    
    public static final int lastIndexOf(final CharSequence charSequence, final String str, int fromIndex, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!b && charSequence instanceof String) {
            fromIndex = ((String)charSequence).lastIndexOf(str, fromIndex);
        }
        else {
            fromIndex = indexOf$StringsKt__StringsKt(charSequence, str, fromIndex, 0, b, true);
        }
        return fromIndex;
    }
    
    public static /* synthetic */ int lastIndexOf$default(final CharSequence charSequence, final String s, int lastIndex, boolean b, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            lastIndex = getLastIndex(charSequence);
        }
        if ((n & 0x4) != 0x0) {
            b = false;
        }
        return lastIndexOf(charSequence, s, lastIndex, b);
    }
    
    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(final CharSequence charSequence, final String[] array, final int n, final boolean b, final int i) {
        if (i >= 0) {
            return new DelimitedRangesSequence(charSequence, n, i, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new StringsKt__StringsKt$rangesDelimitedBy.StringsKt__StringsKt$rangesDelimitedBy$4((List)ArraysKt___ArraysJvmKt.asList(array), b));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Limit must be non-negative, but was ");
        sb.append(i);
        sb.append('.');
        throw new IllegalArgumentException(sb.toString().toString());
    }
    
    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(final CharSequence charSequence, final String[] array, int n, boolean b, int n2, final int n3, final Object o) {
        if ((n3 & 0x2) != 0x0) {
            n = 0;
        }
        if ((n3 & 0x4) != 0x0) {
            b = false;
        }
        if ((n3 & 0x8) != 0x0) {
            n2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, array, n, b, n2);
    }
    
    public static final boolean regionMatchesImpl(final CharSequence charSequence, final int n, final CharSequence charSequence2, final int n2, final int n3, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        if (n2 >= 0 && n >= 0 && n <= charSequence.length() - n3 && n2 <= charSequence2.length() - n3) {
            for (int i = 0; i < n3; ++i) {
                if (!CharsKt__CharKt.equals(charSequence.charAt(n + i), charSequence2.charAt(n2 + i), b)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static final List<String> split(final CharSequence charSequence, final String[] array, final boolean b, final int n) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(array, "delimiters");
        if (array.length == 1) {
            boolean b2 = false;
            final String s = array[0];
            if (s.length() == 0) {
                b2 = true;
            }
            if (!b2) {
                return split$StringsKt__StringsKt(charSequence, s, b, n);
            }
        }
        final Iterable<? extends T> iterable = SequencesKt___SequencesKt.asIterable((Sequence<? extends T>)rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, array, 0, b, n, 2, null));
        final ArrayList<String> list = new ArrayList<String>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)iterable, 10));
        final Iterator<? extends T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(substring(charSequence, (IntRange)iterator.next()));
        }
        return list;
    }
    
    private static final List<String> split$StringsKt__StringsKt(final CharSequence charSequence, final String s, final boolean b, final int i) {
        int n = 0;
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        final int index = indexOf(charSequence, s, 0, b);
        if (index != -1 && i != 1) {
            final boolean b2 = i > 0;
            int coerceAtMost = 10;
            if (b2) {
                coerceAtMost = RangesKt___RangesKt.coerceAtMost(i, 10);
            }
            final ArrayList list = new ArrayList<String>(coerceAtMost);
            int n2 = index;
            int index2;
            int n3;
            do {
                list.add(charSequence.subSequence(n, n2).toString());
                n3 = s.length() + n2;
                if (b2 && list.size() == i - 1) {
                    break;
                }
                index2 = indexOf(charSequence, s, n3, b);
                n = n3;
            } while ((n2 = index2) != -1);
            list.add(charSequence.subSequence(n3, charSequence.length()).toString());
            return (ArrayList<String>)list;
        }
        return CollectionsKt__CollectionsJVMKt.listOf(charSequence.toString());
    }
    
    public static final String substring(final CharSequence charSequence, final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return charSequence.subSequence(intRange.getStart(), intRange.getEndInclusive() + 1).toString();
    }
}
