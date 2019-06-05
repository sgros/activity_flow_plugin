package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.sequences.Sequence;

/* compiled from: Strings.kt */
class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    public static final IntRange getIndices(CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        return new IntRange(0, charSequence.length() - 1);
    }

    public static final int getLastIndex(CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        return charSequence.length() - 1;
    }

    public static final String substring(CharSequence charSequence, IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return charSequence.subSequence(intRange.getStart().intValue(), intRange.getEndInclusive().intValue() + 1).toString();
    }

    public static final boolean regionMatchesImpl(CharSequence charSequence, int i, CharSequence charSequence2, int i2, int i3, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        if (i2 < 0 || i < 0 || i > charSequence.length() - i3 || i2 > charSequence2.length() - i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (!CharsKt__CharKt.equals(charSequence.charAt(i + i4), charSequence2.charAt(i2 + i4), z)) {
                return false;
            }
        }
        return true;
    }

    public static final int indexOfAny(CharSequence charSequence, char[] cArr, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(cArr, "chars");
        if (!z && cArr.length == 1 && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(ArraysKt___ArraysKt.single(cArr), i);
        }
        i = RangesKt___RangesKt.coerceAtLeast(i, 0);
        int lastIndex = getLastIndex(charSequence);
        if (i <= lastIndex) {
            while (true) {
                Object obj;
                char charAt = charSequence.charAt(i);
                for (char equals : cArr) {
                    if (CharsKt__CharKt.equals(equals, charAt, z)) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj == null) {
                    if (i == lastIndex) {
                        break;
                    }
                    i++;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2, int i3, Object obj) {
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, i, i2, z, (i3 & 16) != 0 ? false : z2);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2) {
        IntProgression downTo;
        if (z2) {
            downTo = RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(i, getLastIndex(charSequence)), RangesKt___RangesKt.coerceAtLeast(i2, 0));
        } else {
            downTo = new IntRange(RangesKt___RangesKt.coerceAtLeast(i, 0), RangesKt___RangesKt.coerceAtMost(i2, charSequence.length()));
        }
        int step;
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            i = downTo.getFirst();
            i2 = downTo.getLast();
            step = downTo.getStep();
            if (step <= 0 ? i < i2 : i > i2) {
                while (true) {
                    if (!StringsKt__StringsJVMKt.regionMatches((String) charSequence2, 0, (String) charSequence, i, charSequence2.length(), z)) {
                        if (i == i2) {
                            break;
                        }
                        i += step;
                    } else {
                        return i;
                    }
                }
            }
        } else {
            i = downTo.getFirst();
            i2 = downTo.getLast();
            step = downTo.getStep();
            if (step <= 0 ? i < i2 : i > i2) {
                while (true) {
                    if (!regionMatchesImpl(charSequence2, 0, charSequence, i, charSequence2.length(), z)) {
                        if (i == i2) {
                            break;
                        }
                        i += step;
                    } else {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence charSequence, Collection<String> collection, int i, boolean z, boolean z2) {
        Pair<Integer, String> pair = null;
        if (z || collection.size() != 1) {
            IntProgression intRange = !z2 ? new IntRange(RangesKt___RangesKt.coerceAtLeast(i, 0), charSequence.length()) : RangesKt___RangesKt.downTo(RangesKt___RangesKt.coerceAtMost(i, getLastIndex(charSequence)), 0);
            int last;
            int step;
            String str;
            Object obj;
            String str2;
            if (charSequence instanceof String) {
                i = intRange.getFirst();
                last = intRange.getLast();
                step = intRange.getStep();
                if (step <= 0 ? i < last : i > last) {
                    while (true) {
                        for (Object obj2 : collection) {
                            str = (String) obj2;
                            if (StringsKt__StringsJVMKt.regionMatches(str, 0, (String) charSequence, i, str.length(), z)) {
                                break;
                            }
                        }
                        obj2 = null;
                        str2 = (String) obj2;
                        if (str2 == null) {
                            if (i == last) {
                                break;
                            }
                            i += step;
                        } else {
                            return TuplesKt.m9to(Integer.valueOf(i), str2);
                        }
                    }
                }
            } else {
                i = intRange.getFirst();
                last = intRange.getLast();
                step = intRange.getStep();
                if (step <= 0 ? i < last : i > last) {
                    while (true) {
                        for (Object obj22 : collection) {
                            str = (String) obj22;
                            if (regionMatchesImpl(str, 0, charSequence, i, str.length(), z)) {
                                break;
                            }
                        }
                        obj22 = null;
                        str2 = (String) obj22;
                        if (str2 == null) {
                            if (i == last) {
                                break;
                            }
                            i += step;
                        } else {
                            return TuplesKt.m9to(Integer.valueOf(i), str2);
                        }
                    }
                }
            }
            return null;
        }
        String str3 = (String) CollectionsKt___CollectionsKt.single((Iterable) collection);
        int indexOf$default = !z2 ? indexOf$default(charSequence, str3, i, false, 4, null) : lastIndexOf$default(charSequence, str3, i, false, 4, null);
        if (indexOf$default >= 0) {
            pair = TuplesKt.m9to(Integer.valueOf(indexOf$default), str3);
        }
        return pair;
    }

    public static final int indexOf(CharSequence charSequence, char c, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        if (!z && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(c, i);
        }
        return indexOfAny(charSequence, new char[]{c}, i, z);
    }

    public static final int indexOf(CharSequence charSequence, String str, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!z && (charSequence instanceof String)) {
            return ((String) charSequence).indexOf(str, i);
        }
        return indexOf$StringsKt__StringsKt$default(charSequence, str, i, charSequence.length(), z, false, 16, null);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return lastIndexOf(charSequence, str, i, z);
    }

    public static final int lastIndexOf(CharSequence charSequence, String str, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(str, "string");
        if (!z && (charSequence instanceof String)) {
            return ((String) charSequence).lastIndexOf(str, i);
        }
        return indexOf$StringsKt__StringsKt(charSequence, str, i, 0, z, true);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return contains(charSequence, charSequence2, z);
    }

    public static final boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        if (charSequence2 instanceof String) {
            if (indexOf$default(charSequence, (String) charSequence2, 0, z, 2, null) < 0) {
                return false;
            }
        }
        if (indexOf$StringsKt__StringsKt$default(charSequence, charSequence2, 0, charSequence.length(), z, false, 16, null) < 0) {
            return false;
        }
        return true;
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] strArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, strArr, i, z, i2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence charSequence, String[] strArr, int i, boolean z, int i2) {
        if ((i2 >= 0 ? 1 : null) != null) {
            return new DelimitedRangesSequence(charSequence, i, i2, new StringsKt__StringsKt$rangesDelimitedBy$4(ArraysKt___ArraysJvmKt.asList(strArr), z));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Limit must be non-negative, but was ");
        stringBuilder.append(i2);
        stringBuilder.append('.');
        throw new IllegalArgumentException(stringBuilder.toString().toString());
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return split(charSequence, strArr, z, i);
    }

    public static final List<String> split(CharSequence charSequence, String[] strArr, boolean z, int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "receiver$0");
        Intrinsics.checkParameterIsNotNull(strArr, "delimiters");
        if (strArr.length == 1) {
            int i2 = 0;
            String str = strArr[0];
            if (str.length() == 0) {
                i2 = 1;
            }
            if (i2 == 0) {
                return split$StringsKt__StringsKt(charSequence, str, z, i);
            }
        }
        Iterable<IntRange> asIterable = SequencesKt___SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, strArr, 0, z, i, 2, null));
        Collection arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(asIterable, 10));
        for (IntRange substring : asIterable) {
            arrayList.add(substring(charSequence, substring));
        }
        return (List) arrayList;
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence charSequence, String str, boolean z, int i) {
        int i2 = 0;
        if ((i >= 0 ? 1 : null) != null) {
            int indexOf = indexOf(charSequence, str, 0, z);
            if (indexOf == -1 || i == 1) {
                return CollectionsKt__CollectionsJVMKt.listOf(charSequence.toString());
            }
            Object obj = i > 0 ? 1 : null;
            int i3 = 10;
            if (obj != null) {
                i3 = RangesKt___RangesKt.coerceAtMost(i, 10);
            }
            ArrayList arrayList = new ArrayList(i3);
            do {
                arrayList.add(charSequence.subSequence(i2, indexOf).toString());
                i2 = str.length() + indexOf;
                if (obj != null && arrayList.size() == i - 1) {
                    break;
                }
                indexOf = indexOf(charSequence, str, i2, z);
            } while (indexOf != -1);
            arrayList.add(charSequence.subSequence(i2, charSequence.length()).toString());
            return arrayList;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Limit must be non-negative, but was ");
        stringBuilder.append(i);
        stringBuilder.append('.');
        throw new IllegalArgumentException(stringBuilder.toString().toString());
    }
}
