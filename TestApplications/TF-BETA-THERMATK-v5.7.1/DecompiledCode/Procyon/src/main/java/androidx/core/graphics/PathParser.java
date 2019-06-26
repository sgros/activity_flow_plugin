// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.graphics;

import android.util.Log;
import android.graphics.Path;
import java.util.ArrayList;

public class PathParser
{
    private static void addNode(final ArrayList<PathDataNode> list, final char c, final float[] array) {
        list.add(new PathDataNode(c, array));
    }
    
    public static boolean canMorph(final PathDataNode[] array, final PathDataNode[] array2) {
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i].mType != array2[i].mType || array[i].mParams.length != array2[i].mParams.length) {
                return false;
            }
        }
        return true;
    }
    
    static float[] copyOfRange(final float[] array, final int n, int a) {
        if (n > a) {
            throw new IllegalArgumentException();
        }
        final int length = array.length;
        if (n >= 0 && n <= length) {
            a -= n;
            final int min = Math.min(a, length - n);
            final float[] array2 = new float[a];
            System.arraycopy(array, n, array2, 0, min);
            return array2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static PathDataNode[] createNodesFromPathData(final String s) {
        if (s == null) {
            return null;
        }
        final ArrayList<PathDataNode> list = new ArrayList<PathDataNode>();
        int i = 1;
        int n = 0;
        while (i < s.length()) {
            i = nextStart(s, i);
            final String trim = s.substring(n, i).trim();
            if (trim.length() > 0) {
                addNode(list, trim.charAt(0), getFloats(trim));
            }
            n = i;
            ++i;
        }
        if (i - n == 1 && n < s.length()) {
            addNode(list, s.charAt(n), new float[0]);
        }
        return list.toArray(new PathDataNode[list.size()]);
    }
    
    public static Path createPathFromPathData(final String str) {
        final Path path = new Path();
        final PathDataNode[] nodesFromPathData = createNodesFromPathData(str);
        if (nodesFromPathData != null) {
            try {
                PathDataNode.nodesToPath(nodesFromPathData, path);
                return path;
            }
            catch (RuntimeException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Error in parsing ");
                sb.append(str);
                throw new RuntimeException(sb.toString(), cause);
            }
        }
        return null;
    }
    
    public static PathDataNode[] deepCopyNodes(final PathDataNode[] array) {
        if (array == null) {
            return null;
        }
        final PathDataNode[] array2 = new PathDataNode[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = new PathDataNode(array[i]);
        }
        return array2;
    }
    
    private static void extract(final String s, final int n, final ExtractFloatResult extractFloatResult) {
        extractFloatResult.mEndWithNegOrDot = false;
        int i = n;
        int n2 = 0;
        int n3 = 0;
        boolean b = false;
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            Label_0141: {
                Label_0135: {
                    if (char1 != ' ') {
                        if (char1 != 'E' && char1 != 'e') {
                            switch (char1) {
                                case 46: {
                                    if (n3 == 0) {
                                        n2 = 0;
                                        n3 = 1;
                                        break Label_0141;
                                    }
                                    extractFloatResult.mEndWithNegOrDot = true;
                                    break Label_0135;
                                }
                                case 45: {
                                    if (i != n && n2 == 0) {
                                        extractFloatResult.mEndWithNegOrDot = true;
                                        break Label_0135;
                                    }
                                    break;
                                }
                                case 44: {
                                    break Label_0135;
                                }
                            }
                            n2 = 0;
                            break Label_0141;
                        }
                        n2 = 1;
                        break Label_0141;
                    }
                }
                n2 = 0;
                b = true;
            }
            if (b) {
                break;
            }
            ++i;
        }
        extractFloatResult.mEndPosition = i;
    }
    
    private static float[] getFloats(final String str) {
        if (str.charAt(0) != 'z') {
            if (str.charAt(0) != 'Z') {
                try {
                    final float[] array = new float[str.length()];
                    final ExtractFloatResult extractFloatResult = new ExtractFloatResult();
                    final int length = str.length();
                    int i = 1;
                    int n = 0;
                    while (i < length) {
                        extract(str, i, extractFloatResult);
                        final int mEndPosition = extractFloatResult.mEndPosition;
                        int n2 = n;
                        if (i < mEndPosition) {
                            array[n] = Float.parseFloat(str.substring(i, mEndPosition));
                            n2 = n + 1;
                        }
                        if (extractFloatResult.mEndWithNegOrDot) {
                            i = mEndPosition;
                            n = n2;
                        }
                        else {
                            i = mEndPosition + 1;
                            n = n2;
                        }
                    }
                    return copyOfRange(array, 0, n);
                }
                catch (NumberFormatException cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("error in parsing \"");
                    sb.append(str);
                    sb.append("\"");
                    throw new RuntimeException(sb.toString(), cause);
                }
            }
        }
        return new float[0];
    }
    
    private static int nextStart(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (((char1 - 'A') * (char1 - 'Z') <= 0 || (char1 - 'a') * (char1 - 'z') <= 0) && char1 != 'e' && char1 != 'E') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    public static void updateNodes(final PathDataNode[] array, final PathDataNode[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i].mType = array2[i].mType;
            for (int j = 0; j < array2[i].mParams.length; ++j) {
                array[i].mParams[j] = array2[i].mParams[j];
            }
        }
    }
    
    private static class ExtractFloatResult
    {
        int mEndPosition;
        boolean mEndWithNegOrDot;
        
        ExtractFloatResult() {
        }
    }
    
    public static class PathDataNode
    {
        public float[] mParams;
        public char mType;
        
        PathDataNode(final char c, final float[] mParams) {
            this.mType = c;
            this.mParams = mParams;
        }
        
        PathDataNode(final PathDataNode pathDataNode) {
            this.mType = pathDataNode.mType;
            final float[] mParams = pathDataNode.mParams;
            this.mParams = PathParser.copyOfRange(mParams, 0, mParams.length);
        }
        
        private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
            final float n = array[0];
            final float n2 = array[1];
            final float n3 = array[2];
            final float n4 = array[3];
            float n5 = array[4];
            float n6 = array[5];
            float n7 = n;
            float n8 = n2;
            float n9 = n3;
            float n10 = n4;
            int n11 = 0;
            switch (c2) {
                case 'Z':
                case 'z': {
                    path.close();
                    path.moveTo(n5, n6);
                    n7 = (n9 = n5);
                    n8 = (n10 = n6);
                }
                default: {
                    n10 = n4;
                    n9 = n3;
                    n8 = n2;
                    n7 = n;
                }
                case 'L':
                case 'M':
                case 'T':
                case 'l':
                case 'm':
                case 't': {
                    n11 = 2;
                    break;
                }
                case 'Q':
                case 'S':
                case 'q':
                case 's': {
                    n11 = 4;
                    n7 = n;
                    n8 = n2;
                    n9 = n3;
                    n10 = n4;
                    break;
                }
                case 'H':
                case 'V':
                case 'h':
                case 'v': {
                    n11 = 1;
                    n7 = n;
                    n8 = n2;
                    n9 = n3;
                    n10 = n4;
                    break;
                }
                case 'C':
                case 'c': {
                    n11 = 6;
                    n7 = n;
                    n8 = n2;
                    n9 = n3;
                    n10 = n4;
                    break;
                }
                case 'A':
                case 'a': {
                    n11 = 7;
                    n10 = n4;
                    n9 = n3;
                    n8 = n2;
                    n7 = n;
                    break;
                }
            }
            final int n12 = 0;
            char c3 = c;
        Label_0501_Outer:
            for (int i = n12; i < array2.length; i += n11, c3 = c2) {
                if (c2 != 'A') {
                    if (c2 == 'C') {
                        int n13 = i;
                        final float n14 = array2[n13 + 0];
                        final float n15 = array2[n13 + 1];
                        final int n16 = n13 + 2;
                        final float n17 = array2[n16];
                        final int n18 = n13 + 3;
                        final float n19 = array2[n18];
                        final int n20 = n13 + 4;
                        final float n21 = array2[n20];
                        n13 += 5;
                        path.cubicTo(n14, n15, n17, n19, n21, array2[n13]);
                        n7 = array2[n20];
                        n8 = array2[n13];
                        n9 = array2[n16];
                        n10 = array2[n18];
                        continue;
                    }
                    if (c2 == 'H') {
                        final int n22 = i + 0;
                        path.lineTo(array2[n22], n8);
                        n7 = array2[n22];
                        continue;
                    }
                    if (c2 == 'Q') {
                        int n23 = i;
                        final int n24 = n23 + 0;
                        final float n25 = array2[n24];
                        final int n26 = n23 + 1;
                        final float n27 = array2[n26];
                        final int n28 = n23 + 2;
                        final float n29 = array2[n28];
                        n23 += 3;
                        path.quadTo(n25, n27, n29, array2[n23]);
                        n9 = array2[n24];
                        n10 = array2[n26];
                        n7 = array2[n28];
                        n8 = array2[n23];
                        continue;
                    }
                    if (c2 == 'V') {
                        final int n30 = i + 0;
                        path.lineTo(n7, array2[n30]);
                        n8 = array2[n30];
                        continue;
                    }
                    if (c2 != 'a') {
                        while (true) {
                            float n41 = 0.0f;
                            float n44 = 0.0f;
                            float n62 = 0.0f;
                            Label_1568: {
                                float n42 = 0.0f;
                                float n43 = 0.0f;
                                Label_1557: {
                                    if (c2 == 'c') {
                                        final float n31 = array2[i + 0];
                                        final float n32 = array2[i + 1];
                                        final int n33 = i + 2;
                                        final float n34 = array2[n33];
                                        final int n35 = i + 3;
                                        final float n36 = array2[n35];
                                        final int n37 = i + 4;
                                        final float n38 = array2[n37];
                                        final int n39 = i + 5;
                                        path.rCubicTo(n31, n32, n34, n36, n38, array2[n39]);
                                        final float n40 = array2[n33] + n7;
                                        n41 = array2[n35] + n8;
                                        n42 = n7 + array2[n37];
                                        n43 = array2[n39];
                                        n44 = n40;
                                        break Label_1557;
                                    }
                                    if (c2 != 'h') {
                                        if (c2 == 'q') {
                                            final int n45 = i + 0;
                                            final float n46 = array2[n45];
                                            final int n47 = i + 1;
                                            final float n48 = array2[n47];
                                            final int n49 = i + 2;
                                            final float n50 = array2[n49];
                                            final int n51 = i + 3;
                                            path.rQuadTo(n46, n48, n50, array2[n51]);
                                            final float n52 = array2[n45] + n7;
                                            n41 = array2[n47] + n8;
                                            n42 = n7 + array2[n49];
                                            n43 = array2[n51];
                                            n44 = n52;
                                            break Label_1557;
                                        }
                                        float n71 = 0.0f;
                                        Label_0906: {
                                            if (c2 != 'v') {
                                                if (c2 != 'L') {
                                                    if (c2 != 'M') {
                                                        if (c2 == 'S') {
                                                            float n53 = 0.0f;
                                                            float n54 = 0.0f;
                                                            Label_1081: {
                                                                if (c3 != 'c' && c3 != 's' && c3 != 'C') {
                                                                    n53 = n8;
                                                                    n54 = n7;
                                                                    if (c3 != 'S') {
                                                                        break Label_1081;
                                                                    }
                                                                }
                                                                n54 = n7 * 2.0f - n9;
                                                                n53 = n8 * 2.0f - n10;
                                                            }
                                                            final int n55 = i + 0;
                                                            final float n56 = array2[n55];
                                                            final int n57 = i + 1;
                                                            final float n58 = array2[n57];
                                                            final int n59 = i + 2;
                                                            final float n60 = array2[n59];
                                                            final int n61 = i + 3;
                                                            path.cubicTo(n54, n53, n56, n58, n60, array2[n61]);
                                                            n44 = array2[n55];
                                                            n41 = array2[n57];
                                                            n62 = array2[n59];
                                                            n8 = array2[n61];
                                                            break Label_1568;
                                                        }
                                                        if (c2 == 'T') {
                                                            float n63 = 0.0f;
                                                            float n64 = 0.0f;
                                                            Label_0970: {
                                                                if (c3 != 'q' && c3 != 't' && c3 != 'Q') {
                                                                    n63 = n8;
                                                                    n64 = n7;
                                                                    if (c3 != 'T') {
                                                                        break Label_0970;
                                                                    }
                                                                }
                                                                n64 = n7 * 2.0f - n9;
                                                                n63 = n8 * 2.0f - n10;
                                                            }
                                                            final int n65 = i + 0;
                                                            final float n66 = array2[n65];
                                                            final int n67 = i + 1;
                                                            path.quadTo(n64, n63, n66, array2[n67]);
                                                            n7 = array2[n65];
                                                            n8 = array2[n67];
                                                            n10 = n63;
                                                            n9 = n64;
                                                            continue Label_0501_Outer;
                                                        }
                                                        if (c2 == 'l') {
                                                            final int n68 = i + 0;
                                                            final float n69 = array2[n68];
                                                            final int n70 = i + 1;
                                                            path.rLineTo(n69, array2[n70]);
                                                            n7 += array2[n68];
                                                            n71 = array2[n70];
                                                            break Label_0906;
                                                        }
                                                        if (c2 != 'm') {
                                                            if (c2 == 's') {
                                                                float n72;
                                                                float n73;
                                                                if (c3 != 'c' && c3 != 's' && c3 != 'C' && c3 != 'S') {
                                                                    n72 = 0.0f;
                                                                    n73 = 0.0f;
                                                                }
                                                                else {
                                                                    n73 = n8 - n10;
                                                                    n72 = n7 - n9;
                                                                }
                                                                final int n74 = i + 0;
                                                                final float n75 = array2[n74];
                                                                final int n76 = i + 1;
                                                                final float n77 = array2[n76];
                                                                final int n78 = i + 2;
                                                                final float n79 = array2[n78];
                                                                final int n80 = i + 3;
                                                                path.rCubicTo(n72, n73, n75, n77, n79, array2[n80]);
                                                                final float n81 = array2[n74] + n7;
                                                                n41 = array2[n76] + n8;
                                                                n42 = n7 + array2[n78];
                                                                n43 = array2[n80];
                                                                n44 = n81;
                                                                break Label_1557;
                                                            }
                                                            if (c2 != 't') {
                                                                break Label_0501;
                                                            }
                                                            float n82;
                                                            float n83;
                                                            if (c3 != 'q' && c3 != 't' && c3 != 'Q' && c3 != 'T') {
                                                                n82 = 0.0f;
                                                                n83 = 0.0f;
                                                            }
                                                            else {
                                                                n83 = n7 - n9;
                                                                n82 = n8 - n10;
                                                            }
                                                            final int n84 = i + 0;
                                                            final float n85 = array2[n84];
                                                            final int n86 = i + 1;
                                                            path.rQuadTo(n83, n82, n85, array2[n86]);
                                                            final float n87 = n7 + array2[n84];
                                                            final float n88 = n8 + array2[n86];
                                                            n10 = n82 + n8;
                                                            n9 = n83 + n7;
                                                            n8 = n88;
                                                            n7 = n87;
                                                            break Label_0501;
                                                        }
                                                        else {
                                                            final int n89 = i + 0;
                                                            n7 += array2[n89];
                                                            final int n90 = i + 1;
                                                            n8 += array2[n90];
                                                            if (i > 0) {
                                                                path.rLineTo(array2[n89], array2[n90]);
                                                                break Label_0501;
                                                            }
                                                            path.rMoveTo(array2[n89], array2[n90]);
                                                        }
                                                    }
                                                    else {
                                                        final int n91 = i + 0;
                                                        n7 = array2[n91];
                                                        final int n92 = i + 1;
                                                        n8 = array2[n92];
                                                        if (i > 0) {
                                                            path.lineTo(array2[n91], array2[n92]);
                                                            break Label_0501;
                                                        }
                                                        path.moveTo(array2[n91], array2[n92]);
                                                    }
                                                    n6 = n8;
                                                    n5 = n7;
                                                    break Label_0501;
                                                }
                                                final int n93 = i + 0;
                                                final float n94 = array2[n93];
                                                final int n95 = i + 1;
                                                path.lineTo(n94, array2[n95]);
                                                n7 = array2[n93];
                                                n8 = array2[n95];
                                                break Label_0501;
                                            }
                                            else {
                                                final int n96 = i + 0;
                                                path.rLineTo(0.0f, array2[n96]);
                                                n71 = array2[n96];
                                            }
                                        }
                                        n8 += n71;
                                    }
                                    else {
                                        final int n97 = i + 0;
                                        path.rLineTo(array2[n97], 0.0f);
                                        n7 += array2[n97];
                                    }
                                    continue Label_0501_Outer;
                                }
                                n8 += n43;
                                n62 = n42;
                            }
                            n10 = n41;
                            n9 = n44;
                            n7 = n62;
                            continue;
                        }
                    }
                    final int n98 = i + 5;
                    final float n99 = array2[n98];
                    final int n100 = i + 6;
                    drawArc(path, n7, n8, n99 + n7, array2[n100] + n8, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                    n7 += array2[n98];
                    n8 += array2[n100];
                }
                else {
                    final int n101 = i;
                    final int n102 = n101 + 5;
                    final float n103 = array2[n102];
                    final int n104 = n101 + 6;
                    drawArc(path, n7, n8, n103, array2[n104], array2[n101 + 0], array2[n101 + 1], array2[n101 + 2], array2[n101 + 3] != 0.0f, array2[n101 + 4] != 0.0f);
                    n7 = array2[n102];
                    n8 = array2[n104];
                }
                n10 = n8;
                n9 = n7;
            }
            array[0] = n7;
            array[1] = n8;
            array[2] = n9;
            array[3] = n10;
            array[4] = n5;
            array[5] = n6;
        }
        
        private static void arcToBezier(final Path path, final double n, final double n2, final double n3, double v, double n4, double n5, double n6, double n7, double n8) {
            final int n9 = (int)Math.ceil(Math.abs(n8 * 4.0 / 3.141592653589793));
            final double cos = Math.cos(n6);
            final double sin = Math.sin(n6);
            final double cos2 = Math.cos(n7);
            final double sin2 = Math.sin(n7);
            n6 = -n3;
            final double n10 = n6 * cos;
            final double n11 = v * sin;
            final double n12 = n6 * sin;
            final double n13 = v * cos;
            v = n9;
            Double.isNaN(v);
            n8 /= v;
            n6 = n4;
            n4 = sin2 * n12 + cos2 * n13;
            double n14 = n10 * sin2 - n11 * cos2;
            int i = 0;
            double n15 = n7;
            n7 = n5;
            v = n12;
            n5 = sin;
            while (i < n9) {
                final double n16 = n15 + n8;
                final double sin3 = Math.sin(n16);
                final double cos3 = Math.cos(n16);
                final double n17 = n + n3 * cos * cos3 - n11 * sin3;
                final double n18 = n2 + n3 * n5 * cos3 + n13 * sin3;
                final double n19 = n10 * sin3 - n11 * cos3;
                final double n20 = sin3 * v + cos3 * n13;
                final double a = n16 - n15;
                final double tan = Math.tan(a / 2.0);
                final double n21 = Math.sin(a) * (Math.sqrt(tan * 3.0 * tan + 4.0) - 1.0) / 3.0;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float)(n6 + n14 * n21), (float)(n7 + n4 * n21), (float)(n17 - n21 * n19), (float)(n18 - n21 * n20), (float)n17, (float)n18);
                ++i;
                n7 = n18;
                n15 = n16;
                n4 = n20;
                n14 = n19;
                n6 = n17;
            }
        }
        
        private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
            final double radians = Math.toRadians(n7);
            final double cos = Math.cos(radians);
            final double sin = Math.sin(radians);
            final double v = n;
            Double.isNaN(v);
            final double n8 = n2;
            Double.isNaN(n8);
            final double v2 = n5;
            Double.isNaN(v2);
            final double n9 = (v * cos + n8 * sin) / v2;
            final double v3 = -n;
            Double.isNaN(v3);
            Double.isNaN(n8);
            final double v4 = n6;
            Double.isNaN(v4);
            final double n10 = (v3 * sin + n8 * cos) / v4;
            final double v5 = n3;
            Double.isNaN(v5);
            final double n11 = n4;
            Double.isNaN(n11);
            Double.isNaN(v2);
            final double n12 = (v5 * cos + n11 * sin) / v2;
            final double v6 = -n3;
            Double.isNaN(v6);
            Double.isNaN(n11);
            Double.isNaN(v4);
            final double n13 = (v6 * sin + n11 * cos) / v4;
            final double n14 = n9 - n12;
            final double n15 = n10 - n13;
            final double n16 = (n9 + n12) / 2.0;
            final double n17 = (n10 + n13) / 2.0;
            final double n18 = n14 * n14 + n15 * n15;
            if (n18 == 0.0) {
                Log.w("PathParser", " Points are coincident");
                return;
            }
            final double a = 1.0 / n18 - 0.25;
            if (a < 0.0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Points are too far apart ");
                sb.append(n18);
                Log.w("PathParser", sb.toString());
                final float n19 = (float)(Math.sqrt(n18) / 1.99999);
                drawArc(path, n, n2, n3, n4, n5 * n19, n6 * n19, n7, b, b2);
                return;
            }
            final double sqrt = Math.sqrt(a);
            final double n20 = n14 * sqrt;
            final double n21 = sqrt * n15;
            double n22;
            double n23;
            if (b == b2) {
                n22 = n16 - n21;
                n23 = n17 + n20;
            }
            else {
                n22 = n16 + n21;
                n23 = n17 - n20;
            }
            final double atan2 = Math.atan2(n10 - n23, n9 - n22);
            final double n24 = Math.atan2(n13 - n23, n12 - n22) - atan2;
            final boolean b3 = n24 >= 0.0;
            double n25 = n24;
            if (b2 != b3) {
                if (n24 > 0.0) {
                    n25 = n24 - 6.283185307179586;
                }
                else {
                    n25 = n24 + 6.283185307179586;
                }
            }
            Double.isNaN(v2);
            final double n26 = n22 * v2;
            Double.isNaN(v4);
            final double n27 = n23 * v4;
            arcToBezier(path, n26 * cos - n27 * sin, n26 * sin + n27 * cos, v2, v4, v, n8, radians, atan2, n25);
        }
        
        public static void nodesToPath(final PathDataNode[] array, final Path path) {
            final float[] array2 = new float[6];
            final char c = 'm';
            int i = 0;
            char c2 = c;
            while (i < array.length) {
                addCommand(path, array2, c2, array[i].mType, array[i].mParams);
                final char mType = array[i].mType;
                ++i;
                c2 = mType;
            }
        }
        
        public void interpolatePathDataNode(final PathDataNode pathDataNode, final PathDataNode pathDataNode2, final float n) {
            this.mType = pathDataNode.mType;
            int n2 = 0;
            while (true) {
                final float[] mParams = pathDataNode.mParams;
                if (n2 >= mParams.length) {
                    break;
                }
                this.mParams[n2] = mParams[n2] * (1.0f - n) + pathDataNode2.mParams[n2] * n;
                ++n2;
            }
        }
    }
}
