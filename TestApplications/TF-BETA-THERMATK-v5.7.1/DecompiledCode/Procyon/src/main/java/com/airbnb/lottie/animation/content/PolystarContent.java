// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.model.content.PolystarShape;
import android.graphics.PointF;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class PolystarContent implements PathContent, AnimationListener, KeyPathElementContent
{
    private final boolean hidden;
    private final BaseKeyframeAnimation<?, Float> innerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final BaseKeyframeAnimation<?, Float> outerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> outerRoundednessAnimation;
    private final Path path;
    private final BaseKeyframeAnimation<?, Float> pointsAnimation;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final BaseKeyframeAnimation<?, Float> rotationAnimation;
    private CompoundTrimPathContent trimPaths;
    private final PolystarShape.Type type;
    
    public PolystarContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final PolystarShape polystarShape) {
        this.path = new Path();
        this.trimPaths = new CompoundTrimPathContent();
        this.lottieDrawable = lottieDrawable;
        this.name = polystarShape.getName();
        this.type = polystarShape.getType();
        this.hidden = polystarShape.isHidden();
        this.pointsAnimation = polystarShape.getPoints().createAnimation();
        this.positionAnimation = polystarShape.getPosition().createAnimation();
        this.rotationAnimation = polystarShape.getRotation().createAnimation();
        this.outerRadiusAnimation = polystarShape.getOuterRadius().createAnimation();
        this.outerRoundednessAnimation = polystarShape.getOuterRoundedness().createAnimation();
        if (this.type == PolystarShape.Type.STAR) {
            this.innerRadiusAnimation = polystarShape.getInnerRadius().createAnimation();
            this.innerRoundednessAnimation = polystarShape.getInnerRoundedness().createAnimation();
        }
        else {
            this.innerRadiusAnimation = null;
            this.innerRoundednessAnimation = null;
        }
        baseLayer.addAnimation(this.pointsAnimation);
        baseLayer.addAnimation(this.positionAnimation);
        baseLayer.addAnimation(this.rotationAnimation);
        baseLayer.addAnimation(this.outerRadiusAnimation);
        baseLayer.addAnimation(this.outerRoundednessAnimation);
        if (this.type == PolystarShape.Type.STAR) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        this.pointsAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.positionAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.rotationAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.outerRadiusAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.outerRoundednessAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        if (this.type == PolystarShape.Type.STAR) {
            this.innerRadiusAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.innerRoundednessAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        }
    }
    
    private void createPolygonPath() {
        final int n = (int)Math.floor(this.pointsAnimation.getValue());
        final BaseKeyframeAnimation<?, Float> rotationAnimation = this.rotationAnimation;
        double n2;
        if (rotationAnimation == null) {
            n2 = 0.0;
        }
        else {
            n2 = rotationAnimation.getValue();
        }
        final double radians = Math.toRadians(n2 - 90.0);
        final double n3 = n;
        Double.isNaN(n3);
        final float n4 = (float)(6.283185307179586 / n3);
        final float n5 = this.outerRoundednessAnimation.getValue() / 100.0f;
        final float floatValue = this.outerRadiusAnimation.getValue();
        final double n6 = floatValue;
        final double cos = Math.cos(radians);
        Double.isNaN(n6);
        float n7 = (float)(cos * n6);
        final double sin = Math.sin(radians);
        Double.isNaN(n6);
        float n8 = (float)(sin * n6);
        this.path.moveTo(n7, n8);
        final double n9 = n4;
        Double.isNaN(n9);
        double n10 = radians + n9;
        final double ceil = Math.ceil(n3);
        float n12;
        float n13;
        for (int n11 = 0; n11 < ceil; ++n11, n8 = n13, n7 = n12) {
            final double cos2 = Math.cos(n10);
            Double.isNaN(n6);
            n12 = (float)(cos2 * n6);
            final double sin2 = Math.sin(n10);
            Double.isNaN(n6);
            n13 = (float)(n6 * sin2);
            if (n5 != 0.0f) {
                final double n14 = (float)(Math.atan2(n8, n7) - 1.5707963267948966);
                final float n15 = (float)Math.cos(n14);
                final float n16 = (float)Math.sin(n14);
                final double n17 = (float)(Math.atan2(n13, n12) - 1.5707963267948966);
                final float n18 = (float)Math.cos(n17);
                final float n19 = (float)Math.sin(n17);
                final float n20 = floatValue * n5 * 0.25f;
                this.path.cubicTo(n7 - n15 * n20, n8 - n16 * n20, n12 + n18 * n20, n13 + n20 * n19, n12, n13);
            }
            else {
                this.path.lineTo(n12, n13);
            }
            Double.isNaN(n9);
            n10 += n9;
        }
        final PointF pointF = this.positionAnimation.getValue();
        this.path.offset(pointF.x, pointF.y);
        this.path.close();
    }
    
    private void createStarPath() {
        final float floatValue = this.pointsAnimation.getValue();
        final BaseKeyframeAnimation<?, Float> rotationAnimation = this.rotationAnimation;
        double n;
        if (rotationAnimation == null) {
            n = 0.0;
        }
        else {
            n = rotationAnimation.getValue();
        }
        final double radians = Math.toRadians(n - 90.0);
        final double n2 = floatValue;
        Double.isNaN(n2);
        final float n3 = (float)(6.283185307179586 / n2);
        final float n4 = n3 / 2.0f;
        final float n5 = floatValue - (int)floatValue;
        double n6 = radians;
        if (n5 != 0.0f) {
            final double v = (1.0f - n5) * n4;
            Double.isNaN(v);
            n6 = radians + v;
        }
        final float floatValue2 = this.outerRadiusAnimation.getValue();
        final float floatValue3 = this.innerRadiusAnimation.getValue();
        final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation = this.innerRoundednessAnimation;
        float n7;
        if (innerRoundednessAnimation != null) {
            n7 = innerRoundednessAnimation.getValue() / 100.0f;
        }
        else {
            n7 = 0.0f;
        }
        final BaseKeyframeAnimation<?, Float> outerRoundednessAnimation = this.outerRoundednessAnimation;
        float n8;
        if (outerRoundednessAnimation != null) {
            n8 = outerRoundednessAnimation.getValue() / 100.0f;
        }
        else {
            n8 = 0.0f;
        }
        float n9;
        float n11;
        float n12;
        double n13;
        if (n5 != 0.0f) {
            n9 = (floatValue2 - floatValue3) * n5 + floatValue3;
            final double n10 = n9;
            final double cos = Math.cos(n6);
            Double.isNaN(n10);
            n11 = (float)(n10 * cos);
            final double sin = Math.sin(n6);
            Double.isNaN(n10);
            n12 = (float)(n10 * sin);
            this.path.moveTo(n11, n12);
            final double v2 = n3 * n5 / 2.0f;
            Double.isNaN(v2);
            n13 = n6 + v2;
        }
        else {
            final double n14 = floatValue2;
            final double cos2 = Math.cos(n6);
            Double.isNaN(n14);
            n11 = (float)(n14 * cos2);
            final double sin2 = Math.sin(n6);
            Double.isNaN(n14);
            n12 = (float)(n14 * sin2);
            this.path.moveTo(n11, n12);
            final double v3 = n4;
            Double.isNaN(v3);
            n13 = n6 + v3;
            n9 = 0.0f;
        }
        final double n15 = Math.ceil(n2) * 2.0;
        int n16 = 0;
        int n17 = 0;
        float n18 = n12;
        float n19 = n11;
        final float n20 = n4;
        final float n21 = floatValue2;
        while (true) {
            final double n22 = n16;
            if (n22 >= n15) {
                break;
            }
            float n23;
            if (n17 != 0) {
                n23 = n21;
            }
            else {
                n23 = floatValue3;
            }
            float n24;
            if (n9 != 0.0f && n22 == n15 - 2.0) {
                n24 = n3 * n5 / 2.0f;
            }
            else {
                n24 = n20;
            }
            if (n9 != 0.0f && n22 == n15 - 1.0) {
                n23 = n9;
            }
            final double n25 = n23;
            final double cos3 = Math.cos(n13);
            Double.isNaN(n25);
            final float n26 = (float)(n25 * cos3);
            final double sin3 = Math.sin(n13);
            Double.isNaN(n25);
            final float n27 = (float)(n25 * sin3);
            if (n7 == 0.0f && n8 == 0.0f) {
                this.path.lineTo(n26, n27);
            }
            else {
                final double y = n18;
                float n28 = floatValue3;
                float n29 = n7;
                final double n30 = (float)(Math.atan2(y, n19) - 1.5707963267948966);
                final float n31 = (float)Math.cos(n30);
                final float n32 = (float)Math.sin(n30);
                final float n33 = n8;
                final double y2 = n27;
                final float n34 = n27;
                final double n35 = (float)(Math.atan2(y2, n26) - 1.5707963267948966);
                final float n36 = (float)Math.cos(n35);
                final float n37 = (float)Math.sin(n35);
                float n38;
                if (n17 != 0) {
                    n38 = n29;
                }
                else {
                    n38 = n33;
                }
                if (n17 != 0) {
                    n29 = n33;
                }
                float n39;
                if (n17 != 0) {
                    n39 = n28;
                }
                else {
                    n39 = n21;
                }
                if (n17 != 0) {
                    n28 = n21;
                }
                final float n40 = n39 * n38 * 0.47829f;
                final float n41 = n31 * n40;
                final float n42 = n40 * n32;
                final float n43 = n28 * n29 * 0.47829f;
                final float n44 = n36 * n43;
                final float n45 = n43 * n37;
                float n46 = n41;
                float n47 = n44;
                float n48 = n42;
                float n49 = n45;
                if (n5 != 0.0f) {
                    if (n16 == 0) {
                        n46 = n41 * n5;
                        n48 = n42 * n5;
                        n47 = n44;
                        n49 = n45;
                    }
                    else {
                        n46 = n41;
                        n47 = n44;
                        n48 = n42;
                        n49 = n45;
                        if (n22 == n15 - 1.0) {
                            n47 = n44 * n5;
                            n49 = n45 * n5;
                            n48 = n42;
                            n46 = n41;
                        }
                    }
                }
                this.path.cubicTo(n19 - n46, n18 - n48, n26 + n47, n34 + n49, n26, n34);
            }
            final double v4 = n24;
            Double.isNaN(v4);
            n13 += v4;
            n17 ^= 0x1;
            ++n16;
            n19 = n26;
            n18 = n27;
        }
        final PointF pointF = this.positionAnimation.getValue();
        this.path.offset(pointF.x, pointF.y);
        this.path.close();
    }
    
    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> valueCallback) {
        if (t == LottieProperty.POLYSTAR_POINTS) {
            this.pointsAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.POLYSTAR_ROTATION) {
            this.rotationAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback((LottieValueCallback<PointF>)valueCallback);
        }
        else {
            if (t == LottieProperty.POLYSTAR_INNER_RADIUS) {
                final BaseKeyframeAnimation<?, Float> innerRadiusAnimation = this.innerRadiusAnimation;
                if (innerRadiusAnimation != null) {
                    innerRadiusAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
                    return;
                }
            }
            if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
                this.outerRadiusAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
            }
            else {
                if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS) {
                    final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation = this.innerRoundednessAnimation;
                    if (innerRoundednessAnimation != null) {
                        innerRoundednessAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
                        return;
                    }
                }
                if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
                    this.outerRoundednessAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        final int n = PolystarContent$1.$SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[this.type.ordinal()];
        if (n != 1) {
            if (n == 2) {
                this.createPolygonPath();
            }
        }
        else {
            this.createStarPath();
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }
    
    @Override
    public void onValueChanged() {
        this.invalidate();
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, n, list, keyPath2, this);
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        for (int i = 0; i < list.size(); ++i) {
            final Content content = list.get(i);
            if (content instanceof TrimPathContent) {
                final TrimPathContent trimPathContent = (TrimPathContent)content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }
}
