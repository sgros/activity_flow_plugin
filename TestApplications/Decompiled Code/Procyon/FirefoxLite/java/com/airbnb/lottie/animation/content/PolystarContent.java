// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.utils.MiscUtils;
import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.model.content.PolystarShape;
import android.graphics.PointF;
import android.graphics.Path;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class PolystarContent implements KeyPathElementContent, PathContent, AnimationListener
{
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
    private TrimPathContent trimPath;
    private final PolystarShape.Type type;
    
    public PolystarContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final PolystarShape polystarShape) {
        this.path = new Path();
        this.lottieDrawable = lottieDrawable;
        this.name = polystarShape.getName();
        this.type = polystarShape.getType();
        this.pointsAnimation = polystarShape.getPoints().createAnimation();
        this.positionAnimation = polystarShape.getPosition().createAnimation();
        this.rotationAnimation = polystarShape.getRotation().createAnimation();
        this.outerRadiusAnimation = polystarShape.getOuterRadius().createAnimation();
        this.outerRoundednessAnimation = polystarShape.getOuterRoundedness().createAnimation();
        if (this.type == PolystarShape.Type.Star) {
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
        if (this.type == PolystarShape.Type.Star) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        this.pointsAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.positionAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.rotationAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.outerRadiusAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.outerRoundednessAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        if (this.type == PolystarShape.Type.Star) {
            this.innerRadiusAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.innerRoundednessAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        }
    }
    
    private void createPolygonPath() {
        final int n = (int)Math.floor(this.pointsAnimation.getValue());
        double n2;
        if (this.rotationAnimation == null) {
            n2 = 0.0;
        }
        else {
            n2 = this.rotationAnimation.getValue();
        }
        final double radians = Math.toRadians(n2 - 90.0);
        final double a = n;
        final float n3 = (float)(6.283185307179586 / a);
        final float n4 = this.outerRoundednessAnimation.getValue() / 100.0f;
        final float floatValue = this.outerRadiusAnimation.getValue();
        final double n5 = floatValue;
        float n6 = (float)(Math.cos(radians) * n5);
        float n7 = (float)(Math.sin(radians) * n5);
        this.path.moveTo(n6, n7);
        final double n8 = n3;
        double n9 = radians + n8;
        final double ceil = Math.ceil(a);
        float n11;
        float n12;
        for (int n10 = 0; n10 < ceil; ++n10, n7 = n12, n6 = n11) {
            n11 = (float)(Math.cos(n9) * n5);
            n12 = (float)(n5 * Math.sin(n9));
            if (n4 != 0.0f) {
                final double n13 = (float)(Math.atan2(n7, n6) - 1.5707963267948966);
                final float n14 = (float)Math.cos(n13);
                final float n15 = (float)Math.sin(n13);
                final double n16 = (float)(Math.atan2(n12, n11) - 1.5707963267948966);
                final float n17 = (float)Math.cos(n16);
                final float n18 = (float)Math.sin(n16);
                final float n19 = floatValue * n4 * 0.25f;
                this.path.cubicTo(n6 - n14 * n19, n7 - n15 * n19, n11 + n17 * n19, n12 + n19 * n18, n11, n12);
            }
            else {
                this.path.lineTo(n11, n12);
            }
            n9 += n8;
        }
        final PointF pointF = this.positionAnimation.getValue();
        this.path.offset(pointF.x, pointF.y);
        this.path.close();
    }
    
    private void createStarPath() {
        final float floatValue = this.pointsAnimation.getValue();
        double n;
        if (this.rotationAnimation == null) {
            n = 0.0;
        }
        else {
            n = this.rotationAnimation.getValue();
        }
        final double radians = Math.toRadians(n - 90.0);
        final double a = floatValue;
        final float n2 = (float)(6.283185307179586 / a);
        final float n3 = n2 / 2.0f;
        final float n4 = floatValue - (int)floatValue;
        final float n5 = fcmpl(n4, 0.0f);
        double n6 = radians;
        if (n5 != 0) {
            n6 = radians + (1.0f - n4) * n3;
        }
        final float floatValue2 = this.outerRadiusAnimation.getValue();
        final float floatValue3 = this.innerRadiusAnimation.getValue();
        float n7;
        if (this.innerRoundednessAnimation != null) {
            n7 = this.innerRoundednessAnimation.getValue() / 100.0f;
        }
        else {
            n7 = 0.0f;
        }
        float n8;
        if (this.outerRoundednessAnimation != null) {
            n8 = this.outerRoundednessAnimation.getValue() / 100.0f;
        }
        else {
            n8 = 0.0f;
        }
        float n9;
        float n11;
        float n12;
        double n13;
        if (n5 != 0) {
            n9 = (floatValue2 - floatValue3) * n4 + floatValue3;
            final double n10 = n9;
            n11 = (float)(n10 * Math.cos(n6));
            n12 = (float)(n10 * Math.sin(n6));
            this.path.moveTo(n11, n12);
            n13 = n6 + n2 * n4 / 2.0f;
        }
        else {
            final double n14 = floatValue2;
            n11 = (float)(Math.cos(n6) * n14);
            n12 = (float)(n14 * Math.sin(n6));
            this.path.moveTo(n11, n12);
            n13 = n6 + n3;
            n9 = 0.0f;
        }
        final double n15 = Math.ceil(a) * 2.0;
        int n16 = 0;
        int n17 = 0;
        float n18 = n12;
        float n19 = n11;
        while (true) {
            final double n20 = n16;
            if (n20 >= n15) {
                break;
            }
            float n21;
            if (n17 != 0) {
                n21 = floatValue2;
            }
            else {
                n21 = floatValue3;
            }
            final float n22 = fcmpl(n9, 0.0f);
            float n23;
            if (n22 != 0 && n20 == n15 - 2.0) {
                n23 = n2 * n4 / 2.0f;
            }
            else {
                n23 = n3;
            }
            if (n22 != 0 && n20 == n15 - 1.0) {
                n21 = n9;
            }
            final double n24 = n21;
            final float n25 = (float)(n24 * Math.cos(n13));
            final float n26 = (float)(n24 * Math.sin(n13));
            if (n7 == 0.0f && n8 == 0.0f) {
                this.path.lineTo(n25, n26);
            }
            else {
                float n27 = n7;
                final double y = n18;
                final float n28 = n8;
                final double n29 = (float)(Math.atan2(y, n19) - 1.5707963267948966);
                final float n30 = (float)Math.cos(n29);
                final float n31 = (float)Math.sin(n29);
                final double n32 = (float)(Math.atan2(n26, n25) - 1.5707963267948966);
                final float n33 = (float)Math.cos(n32);
                final float n34 = (float)Math.sin(n32);
                float n35;
                if (n17 != 0) {
                    n35 = n27;
                }
                else {
                    n35 = n28;
                }
                if (n17 != 0) {
                    n27 = n28;
                }
                float n36;
                if (n17 != 0) {
                    n36 = floatValue3;
                }
                else {
                    n36 = floatValue2;
                }
                float n37;
                if (n17 != 0) {
                    n37 = floatValue2;
                }
                else {
                    n37 = floatValue3;
                }
                final float n38 = n36 * n35 * 0.47829f;
                final float n39 = n30 * n38;
                final float n40 = n38 * n31;
                final float n41 = n37 * n27 * 0.47829f;
                final float n42 = n33 * n41;
                final float n43 = n41 * n34;
                float n44 = n39;
                float n45 = n42;
                float n46 = n40;
                float n47 = n43;
                if (n5 != 0) {
                    if (n16 == 0) {
                        n44 = n39 * n4;
                        n46 = n40 * n4;
                        n45 = n42;
                        n47 = n43;
                    }
                    else {
                        n44 = n39;
                        n45 = n42;
                        n46 = n40;
                        n47 = n43;
                        if (n20 == n15 - 1.0) {
                            n45 = n42 * n4;
                            n47 = n43 * n4;
                            n46 = n40;
                            n44 = n39;
                        }
                    }
                }
                this.path.cubicTo(n19 - n44, n18 - n46, n25 + n45, n26 + n47, n25, n26);
            }
            n13 += n23;
            n17 ^= 0x1;
            ++n16;
            n18 = n26;
            n19 = n25;
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
        else if (t == LottieProperty.POLYSTAR_INNER_RADIUS && this.innerRadiusAnimation != null) {
            this.innerRadiusAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
            this.outerRadiusAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS && this.innerRoundednessAnimation != null) {
            this.innerRoundednessAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
            this.outerRoundednessAnimation.setValueCallback((LottieValueCallback<Float>)valueCallback);
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
        switch (PolystarContent$1.$SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[this.type.ordinal()]) {
            case 2: {
                this.createPolygonPath();
                break;
            }
            case 1: {
                this.createStarPath();
                break;
            }
        }
        this.path.close();
        Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
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
                final TrimPathContent trimPath = (TrimPathContent)content;
                if (trimPath.getType() == ShapeTrimPath.Type.Simultaneously) {
                    (this.trimPath = trimPath).addListener(this);
                }
            }
        }
    }
}
