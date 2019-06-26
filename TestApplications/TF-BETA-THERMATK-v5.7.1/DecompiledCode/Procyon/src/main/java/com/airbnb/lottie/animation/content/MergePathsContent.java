// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import java.util.ListIterator;
import android.graphics.Path$Op;
import android.os.Build$VERSION;
import java.util.ArrayList;
import java.util.List;
import com.airbnb.lottie.model.content.MergePaths;
import android.graphics.Path;
import android.annotation.TargetApi;

@TargetApi(19)
public class MergePathsContent implements PathContent, GreedyContent
{
    private final Path firstPath;
    private final MergePaths mergePaths;
    private final String name;
    private final Path path;
    private final List<PathContent> pathContents;
    private final Path remainderPath;
    
    public MergePathsContent(final MergePaths mergePaths) {
        this.firstPath = new Path();
        this.remainderPath = new Path();
        this.path = new Path();
        this.pathContents = new ArrayList<PathContent>();
        if (Build$VERSION.SDK_INT >= 19) {
            this.name = mergePaths.getName();
            this.mergePaths = mergePaths;
            return;
        }
        throw new IllegalStateException("Merge paths are not supported pre-KitKat.");
    }
    
    private void addPaths() {
        for (int i = 0; i < this.pathContents.size(); ++i) {
            this.path.addPath(this.pathContents.get(i).getPath());
        }
    }
    
    @TargetApi(19)
    private void opFirstPathWithRest(final Path$Op path$Op) {
        this.remainderPath.reset();
        this.firstPath.reset();
        for (int i = this.pathContents.size() - 1; i >= 1; --i) {
            final PathContent pathContent = this.pathContents.get(i);
            if (pathContent instanceof ContentGroup) {
                final ContentGroup contentGroup = (ContentGroup)pathContent;
                final List<PathContent> pathList = contentGroup.getPathList();
                for (int j = pathList.size() - 1; j >= 0; --j) {
                    final Path path = pathList.get(j).getPath();
                    path.transform(contentGroup.getTransformationMatrix());
                    this.remainderPath.addPath(path);
                }
            }
            else {
                this.remainderPath.addPath(pathContent.getPath());
            }
        }
        final List<PathContent> pathContents = this.pathContents;
        int k = 0;
        final PathContent pathContent2 = pathContents.get(0);
        if (pathContent2 instanceof ContentGroup) {
            final ContentGroup contentGroup2 = (ContentGroup)pathContent2;
            for (List<PathContent> pathList2 = contentGroup2.getPathList(); k < pathList2.size(); ++k) {
                final Path path2 = pathList2.get(k).getPath();
                path2.transform(contentGroup2.getTransformationMatrix());
                this.firstPath.addPath(path2);
            }
        }
        else {
            this.firstPath.set(pathContent2.getPath());
        }
        this.path.op(this.firstPath, this.remainderPath, path$Op);
    }
    
    @Override
    public void absorbContent(final ListIterator<Content> listIterator) {
        while (listIterator.hasPrevious() && listIterator.previous() != this) {}
        while (listIterator.hasPrevious()) {
            final Content content = listIterator.previous();
            if (content instanceof PathContent) {
                this.pathContents.add((PathContent)content);
                listIterator.remove();
            }
        }
    }
    
    @Override
    public Path getPath() {
        this.path.reset();
        if (this.mergePaths.isHidden()) {
            return this.path;
        }
        final int n = MergePathsContent$1.$SwitchMap$com$airbnb$lottie$model$content$MergePaths$MergePathsMode[this.mergePaths.getMode().ordinal()];
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n == 5) {
                            this.opFirstPathWithRest(Path$Op.XOR);
                        }
                    }
                    else {
                        this.opFirstPathWithRest(Path$Op.INTERSECT);
                    }
                }
                else {
                    this.opFirstPathWithRest(Path$Op.REVERSE_DIFFERENCE);
                }
            }
            else {
                this.opFirstPathWithRest(Path$Op.UNION);
            }
        }
        else {
            this.addPaths();
        }
        return this.path;
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        for (int i = 0; i < this.pathContents.size(); ++i) {
            this.pathContents.get(i).setContents(list, list2);
        }
    }
}
