package com.airbnb.lottie.animation.content;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Path.Op;
import android.os.Build.VERSION;
import com.airbnb.lottie.model.content.MergePaths;
import com.airbnb.lottie.model.content.MergePaths.MergePathsMode;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@TargetApi(19)
public class MergePathsContent implements PathContent, GreedyContent {
    private final Path firstPath = new Path();
    private final MergePaths mergePaths;
    private final String name;
    private final Path path = new Path();
    private final List<PathContent> pathContents = new ArrayList();
    private final Path remainderPath = new Path();

    /* renamed from: com.airbnb.lottie.animation.content.MergePathsContent$1 */
    static /* synthetic */ class C01201 {
        /* renamed from: $SwitchMap$com$airbnb$lottie$model$content$MergePaths$MergePathsMode */
        static final /* synthetic */ int[] f10x7df623d1 = new int[MergePathsMode.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Missing block: B:13:?, code skipped:
            return;
     */
        static {
            /*
            r0 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.values();
            r0 = r0.length;
            r0 = new int[r0];
            f10x7df623d1 = r0;
            r0 = f10x7df623d1;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.MERGE;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = f10x7df623d1;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.ADD;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = f10x7df623d1;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.SUBTRACT;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r0 = f10x7df623d1;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.INTERSECT;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0035 }
            r2 = 4;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0035 }
        L_0x0035:
            r0 = f10x7df623d1;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = com.airbnb.lottie.model.content.MergePaths.MergePathsMode.EXCLUDE_INTERSECTIONS;	 Catch:{ NoSuchFieldError -> 0x0040 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0040 }
            r2 = 5;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0040 }
        L_0x0040:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.animation.content.MergePathsContent$C01201.<clinit>():void");
        }
    }

    public MergePathsContent(MergePaths mergePaths) {
        if (VERSION.SDK_INT >= 19) {
            this.name = mergePaths.getName();
            this.mergePaths = mergePaths;
            return;
        }
        throw new IllegalStateException("Merge paths are not supported pre-KitKat.");
    }

    public void absorbContent(ListIterator<Content> listIterator) {
        while (listIterator.hasPrevious() && listIterator.previous() != this) {
        }
        while (listIterator.hasPrevious()) {
            Content content = (Content) listIterator.previous();
            if (content instanceof PathContent) {
                this.pathContents.add((PathContent) content);
                listIterator.remove();
            }
        }
    }

    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < this.pathContents.size(); i++) {
            ((PathContent) this.pathContents.get(i)).setContents(list, list2);
        }
    }

    public Path getPath() {
        this.path.reset();
        if (this.mergePaths.isHidden()) {
            return this.path;
        }
        int i = C01201.f10x7df623d1[this.mergePaths.getMode().ordinal()];
        if (i == 1) {
            addPaths();
        } else if (i == 2) {
            opFirstPathWithRest(Op.UNION);
        } else if (i == 3) {
            opFirstPathWithRest(Op.REVERSE_DIFFERENCE);
        } else if (i == 4) {
            opFirstPathWithRest(Op.INTERSECT);
        } else if (i == 5) {
            opFirstPathWithRest(Op.XOR);
        }
        return this.path;
    }

    private void addPaths() {
        for (int i = 0; i < this.pathContents.size(); i++) {
            this.path.addPath(((PathContent) this.pathContents.get(i)).getPath());
        }
    }

    @TargetApi(19)
    private void opFirstPathWithRest(Op op) {
        this.remainderPath.reset();
        this.firstPath.reset();
        for (int size = this.pathContents.size() - 1; size >= 1; size--) {
            PathContent pathContent = (PathContent) this.pathContents.get(size);
            if (pathContent instanceof ContentGroup) {
                ContentGroup contentGroup = (ContentGroup) pathContent;
                List pathList = contentGroup.getPathList();
                for (int size2 = pathList.size() - 1; size2 >= 0; size2--) {
                    Path path = ((PathContent) pathList.get(size2)).getPath();
                    path.transform(contentGroup.getTransformationMatrix());
                    this.remainderPath.addPath(path);
                }
            } else {
                this.remainderPath.addPath(pathContent.getPath());
            }
        }
        int i = 0;
        PathContent pathContent2 = (PathContent) this.pathContents.get(0);
        if (pathContent2 instanceof ContentGroup) {
            ContentGroup contentGroup2 = (ContentGroup) pathContent2;
            List pathList2 = contentGroup2.getPathList();
            while (i < pathList2.size()) {
                Path path2 = ((PathContent) pathList2.get(i)).getPath();
                path2.transform(contentGroup2.getTransformationMatrix());
                this.firstPath.addPath(path2);
                i++;
            }
        } else {
            this.firstPath.set(pathContent2.getPath());
        }
        this.path.op(this.firstPath, this.remainderPath, op);
    }
}
