package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Shader;
import android.util.Log;

public final class ComplexColorCompat {
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:27:0x0078 in {4, 10, 13, 16, 20, 22, 24, 26} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private static androidx.core.content.res.ComplexColorCompat createFromXml(android.content.res.Resources r6, int r7, android.content.res.Resources.Theme r8) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r7 = r6.getXml(r7);
        r0 = android.util.Xml.asAttributeSet(r7);
        r1 = r7.next();
        r2 = 2;
        r3 = 1;
        if (r1 == r2) goto L_0x0013;
        if (r1 == r3) goto L_0x0013;
        goto L_0x0008;
        if (r1 != r2) goto L_0x0070;
        r1 = r7.getName();
        r2 = -1;
        r4 = r1.hashCode();
        r5 = 89650992; // 0x557f730 float:1.01546526E-35 double:4.42934753E-316;
        if (r4 == r5) goto L_0x0033;
        r5 = 1191572447; // 0x4705f3df float:34291.87 double:5.887150106E-315;
        if (r4 == r5) goto L_0x0029;
        goto L_0x003c;
        r4 = "selector";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x003c;
        r2 = 0;
        goto L_0x003c;
        r4 = "gradient";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x003c;
        r2 = 1;
        if (r2 == 0) goto L_0x0067;
        if (r2 != r3) goto L_0x0049;
        r6 = androidx.core.content.res.GradientColorInflaterCompat.createFromXmlInner(r6, r7, r0, r8);
        r6 = from(r6);
        return r6;
        r6 = new org.xmlpull.v1.XmlPullParserException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r7 = r7.getPositionDescription();
        r8.append(r7);
        r7 = ": unsupported complex color tag ";
        r8.append(r7);
        r8.append(r1);
        r7 = r8.toString();
        r6.<init>(r7);
        throw r6;
        r6 = androidx.core.content.res.ColorStateListInflaterCompat.createFromXmlInner(r6, r7, r0, r8);
        r6 = from(r6);
        return r6;
        r6 = new org.xmlpull.v1.XmlPullParserException;
        r7 = "No start tag found";
        r6.<init>(r7);
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.ComplexColorCompat.createFromXml(android.content.res.Resources, int, android.content.res.Resources$Theme):androidx.core.content.res.ComplexColorCompat");
    }

    private ComplexColorCompat(Shader shader, ColorStateList colorStateList, int i) {
        this.mShader = shader;
        this.mColorStateList = colorStateList;
        this.mColor = i;
    }

    static ComplexColorCompat from(Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }

    static ComplexColorCompat from(ColorStateList colorStateList) {
        return new ComplexColorCompat(null, colorStateList, colorStateList.getDefaultColor());
    }

    static ComplexColorCompat from(int i) {
        return new ComplexColorCompat(null, null, i);
    }

    public Shader getShader() {
        return this.mShader;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public boolean isGradient() {
        return this.mShader != null;
    }

    public boolean isStateful() {
        if (this.mShader == null) {
            ColorStateList colorStateList = this.mColorStateList;
            if (colorStateList != null && colorStateList.isStateful()) {
                return true;
            }
        }
        return false;
    }

    public boolean onStateChanged(int[] iArr) {
        if (isStateful()) {
            ColorStateList colorStateList = this.mColorStateList;
            int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
            if (colorForState != this.mColor) {
                this.mColor = colorForState;
                return true;
            }
        }
        return false;
    }

    public boolean willDraw() {
        return isGradient() || this.mColor != 0;
    }

    public static ComplexColorCompat inflate(Resources resources, int i, Theme theme) {
        try {
            return createFromXml(resources, i, theme);
        } catch (Exception e) {
            Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", e);
            return null;
        }
    }
}
