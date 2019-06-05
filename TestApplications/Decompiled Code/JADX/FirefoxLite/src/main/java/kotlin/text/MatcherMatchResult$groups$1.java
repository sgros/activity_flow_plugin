package kotlin.text;

import java.util.Iterator;
import java.util.regex.MatchResult;
import kotlin.collections.AbstractCollection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* compiled from: Regex.kt */
public final class MatcherMatchResult$groups$1 extends AbstractCollection<MatchGroup> implements MatchNamedGroupCollection {
    final /* synthetic */ MatcherMatchResult this$0;

    public boolean isEmpty() {
        return false;
    }

    MatcherMatchResult$groups$1(MatcherMatchResult matcherMatchResult) {
        this.this$0 = matcherMatchResult;
    }

    public final /* bridge */ boolean contains(Object obj) {
        return obj != null ? obj instanceof MatchGroup : true ? contains((MatchGroup) obj) : false;
    }

    public /* bridge */ boolean contains(MatchGroup matchGroup) {
        return super.contains(matchGroup);
    }

    public int getSize() {
        return this.this$0.matchResult.groupCount() + 1;
    }

    public Iterator<MatchGroup> iterator() {
        return SequencesKt___SequencesKt.map(CollectionsKt___CollectionsKt.asSequence(CollectionsKt__CollectionsKt.getIndices(this)), new MatcherMatchResult$groups$1$iterator$1(this)).iterator();
    }

    public MatchGroup get(int i) {
        MatchResult access$getMatchResult$p = this.this$0.matchResult;
        Intrinsics.checkExpressionValueIsNotNull(access$getMatchResult$p, "matchResult");
        IntRange access$range = RegexKt.range(access$getMatchResult$p, i);
        if (access$range.getStart().intValue() < 0) {
            return null;
        }
        String group = this.this$0.matchResult.group(i);
        Intrinsics.checkExpressionValueIsNotNull(group, "matchResult.group(index)");
        return new MatchGroup(group, access$range);
    }
}
