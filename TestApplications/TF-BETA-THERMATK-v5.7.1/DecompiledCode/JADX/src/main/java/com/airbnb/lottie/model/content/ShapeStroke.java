package com.airbnb.lottie.model.content;

import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.StrokeContent;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.List;

public class ShapeStroke implements ContentModel {
    private final LineCapType capType;
    private final AnimatableColorValue color;
    private final boolean hidden;
    private final LineJoinType joinType;
    private final List<AnimatableFloatValue> lineDashPattern;
    private final float miterLimit;
    private final String name;
    private final AnimatableFloatValue offset;
    private final AnimatableIntegerValue opacity;
    private final AnimatableFloatValue width;

    /* renamed from: com.airbnb.lottie.model.content.ShapeStroke$1 */
    static /* synthetic */ class C01221 {
        /* renamed from: $SwitchMap$com$airbnb$lottie$model$content$ShapeStroke$LineCapType */
        static final /* synthetic */ int[] f11xd9891597 = new int[LineCapType.values().length];
        /* renamed from: $SwitchMap$com$airbnb$lottie$model$content$ShapeStroke$LineJoinType */
        static final /* synthetic */ int[] f12x8c4dd79 = new int[LineJoinType.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Missing block: B:21:?, code skipped:
            return;
     */
        static {
            /*
            r0 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.values();
            r0 = r0.length;
            r0 = new int[r0];
            f12x8c4dd79 = r0;
            r0 = 1;
            r1 = f12x8c4dd79;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.BEVEL;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = r2.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1[r2] = r0;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r1 = 2;
            r2 = f12x8c4dd79;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.MITER;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r2 = 3;
            r3 = f12x8c4dd79;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.ROUND;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r3[r4] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r3 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.values();
            r3 = r3.length;
            r3 = new int[r3];
            f11xd9891597 = r3;
            r3 = f11xd9891597;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.BUTT;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x003d }
            r3[r4] = r0;	 Catch:{ NoSuchFieldError -> 0x003d }
        L_0x003d:
            r0 = f11xd9891597;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.ROUND;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x0047 }
            r0[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x0047 }
        L_0x0047:
            r0 = f11xd9891597;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.UNKNOWN;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0051 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0051 }
        L_0x0051:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.content.ShapeStroke$C01221.<clinit>():void");
        }
    }

    public enum LineCapType {
        BUTT,
        ROUND,
        UNKNOWN;

        public Cap toPaintCap() {
            int i = C01221.f11xd9891597[ordinal()];
            if (i == 1) {
                return Cap.BUTT;
            }
            if (i != 2) {
                return Cap.SQUARE;
            }
            return Cap.ROUND;
        }
    }

    public enum LineJoinType {
        MITER,
        ROUND,
        BEVEL;

        public Join toPaintJoin() {
            int i = C01221.f12x8c4dd79[ordinal()];
            if (i == 1) {
                return Join.BEVEL;
            }
            if (i == 2) {
                return Join.MITER;
            }
            if (i != 3) {
                return null;
            }
            return Join.ROUND;
        }
    }

    public ShapeStroke(String str, AnimatableFloatValue animatableFloatValue, List<AnimatableFloatValue> list, AnimatableColorValue animatableColorValue, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue2, LineCapType lineCapType, LineJoinType lineJoinType, float f, boolean z) {
        this.name = str;
        this.offset = animatableFloatValue;
        this.lineDashPattern = list;
        this.color = animatableColorValue;
        this.opacity = animatableIntegerValue;
        this.width = animatableFloatValue2;
        this.capType = lineCapType;
        this.joinType = lineJoinType;
        this.miterLimit = f;
        this.hidden = z;
    }

    public Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return new StrokeContent(lottieDrawable, baseLayer, this);
    }

    public String getName() {
        return this.name;
    }

    public AnimatableColorValue getColor() {
        return this.color;
    }

    public AnimatableIntegerValue getOpacity() {
        return this.opacity;
    }

    public AnimatableFloatValue getWidth() {
        return this.width;
    }

    public List<AnimatableFloatValue> getLineDashPattern() {
        return this.lineDashPattern;
    }

    public AnimatableFloatValue getDashOffset() {
        return this.offset;
    }

    public LineCapType getCapType() {
        return this.capType;
    }

    public LineJoinType getJoinType() {
        return this.joinType;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
