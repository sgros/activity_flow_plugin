package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.model.layer.BaseLayer;

public class MergePaths implements ContentModel {
    private final MergePathsMode mode;
    private final String name;

    public enum MergePathsMode {
        Merge,
        Add,
        Subtract,
        Intersect,
        ExcludeIntersections;

        public static MergePathsMode forId(int i) {
            switch (i) {
                case 1:
                    return Merge;
                case 2:
                    return Add;
                case 3:
                    return Subtract;
                case 4:
                    return Intersect;
                case 5:
                    return ExcludeIntersections;
                default:
                    return Merge;
            }
        }
    }

    public MergePaths(String str, MergePathsMode mergePathsMode) {
        this.name = str;
        this.mode = mergePathsMode;
    }

    public String getName() {
        return this.name;
    }

    public MergePathsMode getMode() {
        return this.mode;
    }

    public Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        if (lottieDrawable.enableMergePathsForKitKatAndAbove()) {
            return new MergePathsContent(this);
        }
        C0352L.warn("Animation contains merge paths but they are disabled.");
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MergePaths{mode=");
        stringBuilder.append(this.mode);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
