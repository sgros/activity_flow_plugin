package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import java.util.Locale;

public class Layer {
    private final LottieComposition composition;
    private final List<Keyframe<Float>> inOutKeyframes;
    private final long layerId;
    private final String layerName;
    private final LayerType layerType;
    private final List<Mask> masks;
    private final MatteType matteType;
    private final long parentId;
    private final int preCompHeight;
    private final int preCompWidth;
    private final String refId;
    private final List<ContentModel> shapes;
    private final int solidColor;
    private final int solidHeight;
    private final int solidWidth;
    private final float startFrame;
    private final AnimatableTextFrame text;
    private final AnimatableTextProperties textProperties;
    private final AnimatableFloatValue timeRemapping;
    private final float timeStretch;
    private final AnimatableTransform transform;

    public enum LayerType {
        PreComp,
        Solid,
        Image,
        Null,
        Shape,
        Text,
        Unknown
    }

    public enum MatteType {
        None,
        Add,
        Invert,
        Unknown
    }

    public Layer(List<ContentModel> list, LottieComposition lottieComposition, String str, long j, LayerType layerType, long j2, String str2, List<Mask> list2, AnimatableTransform animatableTransform, int i, int i2, int i3, float f, float f2, int i4, int i5, AnimatableTextFrame animatableTextFrame, AnimatableTextProperties animatableTextProperties, List<Keyframe<Float>> list3, MatteType matteType, AnimatableFloatValue animatableFloatValue) {
        this.shapes = list;
        this.composition = lottieComposition;
        this.layerName = str;
        this.layerId = j;
        this.layerType = layerType;
        this.parentId = j2;
        this.refId = str2;
        this.masks = list2;
        this.transform = animatableTransform;
        this.solidWidth = i;
        this.solidHeight = i2;
        this.solidColor = i3;
        this.timeStretch = f;
        this.startFrame = f2;
        this.preCompWidth = i4;
        this.preCompHeight = i5;
        this.text = animatableTextFrame;
        this.textProperties = animatableTextProperties;
        this.inOutKeyframes = list3;
        this.matteType = matteType;
        this.timeRemapping = animatableFloatValue;
    }

    /* Access modifiers changed, original: 0000 */
    public LottieComposition getComposition() {
        return this.composition;
    }

    /* Access modifiers changed, original: 0000 */
    public float getTimeStretch() {
        return this.timeStretch;
    }

    /* Access modifiers changed, original: 0000 */
    public float getStartProgress() {
        return this.startFrame / this.composition.getDurationFrames();
    }

    /* Access modifiers changed, original: 0000 */
    public List<Keyframe<Float>> getInOutKeyframes() {
        return this.inOutKeyframes;
    }

    public long getId() {
        return this.layerId;
    }

    /* Access modifiers changed, original: 0000 */
    public String getName() {
        return this.layerName;
    }

    /* Access modifiers changed, original: 0000 */
    public String getRefId() {
        return this.refId;
    }

    /* Access modifiers changed, original: 0000 */
    public int getPreCompWidth() {
        return this.preCompWidth;
    }

    /* Access modifiers changed, original: 0000 */
    public int getPreCompHeight() {
        return this.preCompHeight;
    }

    /* Access modifiers changed, original: 0000 */
    public List<Mask> getMasks() {
        return this.masks;
    }

    public LayerType getLayerType() {
        return this.layerType;
    }

    /* Access modifiers changed, original: 0000 */
    public MatteType getMatteType() {
        return this.matteType;
    }

    /* Access modifiers changed, original: 0000 */
    public long getParentId() {
        return this.parentId;
    }

    /* Access modifiers changed, original: 0000 */
    public List<ContentModel> getShapes() {
        return this.shapes;
    }

    /* Access modifiers changed, original: 0000 */
    public AnimatableTransform getTransform() {
        return this.transform;
    }

    /* Access modifiers changed, original: 0000 */
    public int getSolidColor() {
        return this.solidColor;
    }

    /* Access modifiers changed, original: 0000 */
    public int getSolidHeight() {
        return this.solidHeight;
    }

    /* Access modifiers changed, original: 0000 */
    public int getSolidWidth() {
        return this.solidWidth;
    }

    /* Access modifiers changed, original: 0000 */
    public AnimatableTextFrame getText() {
        return this.text;
    }

    /* Access modifiers changed, original: 0000 */
    public AnimatableTextProperties getTextProperties() {
        return this.textProperties;
    }

    /* Access modifiers changed, original: 0000 */
    public AnimatableFloatValue getTimeRemapping() {
        return this.timeRemapping;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(getName());
        stringBuilder.append("\n");
        Layer layerModelForId = this.composition.layerModelForId(getParentId());
        if (layerModelForId != null) {
            stringBuilder.append("\t\tParents: ");
            stringBuilder.append(layerModelForId.getName());
            layerModelForId = this.composition.layerModelForId(layerModelForId.getParentId());
            while (layerModelForId != null) {
                stringBuilder.append("->");
                stringBuilder.append(layerModelForId.getName());
                layerModelForId = this.composition.layerModelForId(layerModelForId.getParentId());
            }
            stringBuilder.append(str);
            stringBuilder.append("\n");
        }
        if (!getMasks().isEmpty()) {
            stringBuilder.append(str);
            stringBuilder.append("\tMasks: ");
            stringBuilder.append(getMasks().size());
            stringBuilder.append("\n");
        }
        if (!(getSolidWidth() == 0 || getSolidHeight() == 0)) {
            stringBuilder.append(str);
            stringBuilder.append("\tBackground: ");
            stringBuilder.append(String.format(Locale.US, "%dx%d %X\n", new Object[]{Integer.valueOf(getSolidWidth()), Integer.valueOf(getSolidHeight()), Integer.valueOf(getSolidColor())}));
        }
        if (!this.shapes.isEmpty()) {
            stringBuilder.append(str);
            stringBuilder.append("\tShapes:\n");
            for (Object next : this.shapes) {
                stringBuilder.append(str);
                stringBuilder.append("\t\t");
                stringBuilder.append(next);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
