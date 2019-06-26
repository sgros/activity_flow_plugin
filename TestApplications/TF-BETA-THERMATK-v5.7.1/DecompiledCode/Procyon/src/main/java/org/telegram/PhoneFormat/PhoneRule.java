// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.PhoneFormat;

public class PhoneRule
{
    public int byte8;
    public int flag12;
    public int flag13;
    public String format;
    public boolean hasIntlPrefix;
    public boolean hasTrunkPrefix;
    public int maxLen;
    public int maxVal;
    public int minVal;
    public int otherFlag;
    public int prefixLen;
    
    String format(final String s, final String str, final String s2) {
        final StringBuilder sb = new StringBuilder(20);
        int i = 0;
        int n = 0;
        int beginIndex = 0;
        int n2 = 0;
        int n3 = 0;
        while (i < this.format.length()) {
            final char char1 = this.format.charAt(i);
            int n4 = 0;
            int endIndex = 0;
            int n5 = 0;
            int n6 = 0;
            Label_0442: {
                if (char1 != '#') {
                    if (char1 != '(') {
                        if (char1 == 'c') {
                            if (str != null) {
                                sb.append(str);
                            }
                            n4 = 1;
                            endIndex = beginIndex;
                            n5 = n2;
                            n6 = n3;
                            break Label_0442;
                        }
                        if (char1 == 'n') {
                            if (s2 != null) {
                                sb.append(s2);
                            }
                            n5 = 1;
                            n4 = n;
                            endIndex = beginIndex;
                            n6 = n3;
                            break Label_0442;
                        }
                    }
                    else if (beginIndex < s.length()) {
                        n3 = 1;
                    }
                    if (char1 == ' ' && i > 0) {
                        final String format = this.format;
                        final int n7 = i - 1;
                        if (format.charAt(n7) == 'n') {
                            n4 = n;
                            endIndex = beginIndex;
                            n5 = n2;
                            n6 = n3;
                            if (s2 == null) {
                                break Label_0442;
                            }
                        }
                        if (this.format.charAt(n7) == 'c') {
                            n4 = n;
                            endIndex = beginIndex;
                            n5 = n2;
                            n6 = n3;
                            if (str == null) {
                                break Label_0442;
                            }
                        }
                    }
                    if (beginIndex >= s.length()) {
                        n4 = n;
                        endIndex = beginIndex;
                        n5 = n2;
                        if ((n6 = n3) == 0) {
                            break Label_0442;
                        }
                        n4 = n;
                        endIndex = beginIndex;
                        n5 = n2;
                        n6 = n3;
                        if (char1 != ')') {
                            break Label_0442;
                        }
                    }
                    sb.append(this.format.substring(i, i + 1));
                    n4 = n;
                    endIndex = beginIndex;
                    n5 = n2;
                    n6 = n3;
                    if (char1 == ')') {
                        n6 = 0;
                        n4 = n;
                        endIndex = beginIndex;
                        n5 = n2;
                    }
                }
                else if (beginIndex < s.length()) {
                    endIndex = beginIndex + 1;
                    sb.append(s.substring(beginIndex, endIndex));
                    n4 = n;
                    n5 = n2;
                    n6 = n3;
                }
                else {
                    n4 = n;
                    endIndex = beginIndex;
                    n5 = n2;
                    if ((n6 = n3) != 0) {
                        sb.append(" ");
                        n6 = n3;
                        n5 = n2;
                        endIndex = beginIndex;
                        n4 = n;
                    }
                }
            }
            ++i;
            n = n4;
            beginIndex = endIndex;
            n2 = n5;
            n3 = n6;
        }
        if (str != null && n == 0) {
            sb.insert(0, String.format("%s ", str));
        }
        else if (s2 != null && n2 == 0) {
            sb.insert(0, s2);
        }
        return sb.toString();
    }
    
    boolean hasIntlPrefix() {
        return (this.flag12 & 0x2) != 0x0;
    }
    
    boolean hasTrunkPrefix() {
        final int flag12 = this.flag12;
        boolean b = true;
        if ((flag12 & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
}
