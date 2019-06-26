// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import java.util.Iterator;
import java.util.Locale;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import com.airbnb.lottie.LottieComposition;

public class Layer
{
    private final LottieComposition composition;
    private final boolean hidden;
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
    
    public Layer(final List<ContentModel> shapes, final LottieComposition composition, final String layerName, final long layerId, final LayerType layerType, final long parentId, final String refId, final List<Mask> masks, final AnimatableTransform transform, final int solidWidth, final int solidHeight, final int solidColor, final float timeStretch, final float startFrame, final int preCompWidth, final int preCompHeight, final AnimatableTextFrame text, final AnimatableTextProperties textProperties, final List<Keyframe<Float>> inOutKeyframes, final MatteType matteType, final AnimatableFloatValue timeRemapping, final boolean hidden) {
        this.shapes = shapes;
        this.composition = composition;
        this.layerName = layerName;
        this.layerId = layerId;
        this.layerType = layerType;
        this.parentId = parentId;
        this.refId = refId;
        this.masks = masks;
        this.transform = transform;
        this.solidWidth = solidWidth;
        this.solidHeight = solidHeight;
        this.solidColor = solidColor;
        this.timeStretch = timeStretch;
        this.startFrame = startFrame;
        this.preCompWidth = preCompWidth;
        this.preCompHeight = preCompHeight;
        this.text = text;
        this.textProperties = textProperties;
        this.inOutKeyframes = inOutKeyframes;
        this.matteType = matteType;
        this.timeRemapping = timeRemapping;
        this.hidden = hidden;
    }
    
    LottieComposition getComposition() {
        return this.composition;
    }
    
    public long getId() {
        return this.layerId;
    }
    
    List<Keyframe<Float>> getInOutKeyframes() {
        return this.inOutKeyframes;
    }
    
    public LayerType getLayerType() {
        return this.layerType;
    }
    
    List<Mask> getMasks() {
        return this.masks;
    }
    
    MatteType getMatteType() {
        return this.matteType;
    }
    
    String getName() {
        return this.layerName;
    }
    
    long getParentId() {
        return this.parentId;
    }
    
    int getPreCompHeight() {
        return this.preCompHeight;
    }
    
    int getPreCompWidth() {
        return this.preCompWidth;
    }
    
    String getRefId() {
        return this.refId;
    }
    
    List<ContentModel> getShapes() {
        return this.shapes;
    }
    
    int getSolidColor() {
        return this.solidColor;
    }
    
    int getSolidHeight() {
        return this.solidHeight;
    }
    
    int getSolidWidth() {
        return this.solidWidth;
    }
    
    float getStartProgress() {
        return this.startFrame / this.composition.getDurationFrames();
    }
    
    AnimatableTextFrame getText() {
        return this.text;
    }
    
    AnimatableTextProperties getTextProperties() {
        return this.textProperties;
    }
    
    AnimatableFloatValue getTimeRemapping() {
        return this.timeRemapping;
    }
    
    float getTimeStretch() {
        return this.timeStretch;
    }
    
    AnimatableTransform getTransform() {
        return this.transform;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    public String toString(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(this.getName());
        sb.append("\n");
        final Layer layerModelForId = this.composition.layerModelForId(this.getParentId());
        if (layerModelForId != null) {
            sb.append("\t\tParents: ");
            sb.append(layerModelForId.getName());
            for (Layer layer = this.composition.layerModelForId(layerModelForId.getParentId()); layer != null; layer = this.composition.layerModelForId(layer.getParentId())) {
                sb.append("->");
                sb.append(layer.getName());
            }
            sb.append(s);
            sb.append("\n");
        }
        if (!this.getMasks().isEmpty()) {
            sb.append(s);
            sb.append("\tMasks: ");
            sb.append(this.getMasks().size());
            sb.append("\n");
        }
        if (this.getSolidWidth() != 0 && this.getSolidHeight() != 0) {
            sb.append(s);
            sb.append("\tBackground: ");
            sb.append(String.format(Locale.US, "%dx%d %X\n", this.getSolidWidth(), this.getSolidHeight(), this.getSolidColor()));
        }
        if (!this.shapes.isEmpty()) {
            sb.append(s);
            sb.append("\tShapes:\n");
            for (final ContentModel next : this.shapes) {
                sb.append(s);
                sb.append("\t\t");
                sb.append(next);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    public enum LayerType
    {
        IMAGE, 
        NULL, 
        PRE_COMP, 
        SHAPE, 
        SOLID, 
        TEXT, 
        UNKNOWN;
    }
    
    public enum MatteType
    {
        ADD, 
        INVERT, 
        NONE, 
        UNKNOWN;
    }
}
