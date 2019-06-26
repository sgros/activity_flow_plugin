// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.pdf417.decoder.ec;

final class ModulusPoly
{
    private final int[] coefficients;
    private final ModulusGF field;
    
    ModulusPoly(final ModulusGF field, final int[] coefficients) {
        if (coefficients.length == 0) {
            throw new IllegalArgumentException();
        }
        this.field = field;
        final int length = coefficients.length;
        if (length > 1 && coefficients[0] == 0) {
            int n;
            for (n = 1; n < length && coefficients[n] == 0; ++n) {}
            if (n == length) {
                this.coefficients = new int[] { 0 };
            }
            else {
                System.arraycopy(coefficients, n, this.coefficients = new int[length - n], 0, this.coefficients.length);
            }
        }
        else {
            this.coefficients = coefficients;
        }
    }
    
    ModulusPoly add(ModulusPoly o) {
        if (!this.field.equals(((ModulusPoly)o).field)) {
            throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
        }
        if (!this.isZero()) {
            if (((ModulusPoly)o).isZero()) {
                o = this;
            }
            else {
                final int[] coefficients = this.coefficients;
                int[] coefficients2;
                final int[] array = coefficients2 = ((ModulusPoly)o).coefficients;
                int[] array2 = coefficients;
                if (coefficients.length > array.length) {
                    array2 = array;
                    coefficients2 = coefficients;
                }
                final int[] array3 = new int[coefficients2.length];
                final int n = coefficients2.length - array2.length;
                System.arraycopy(coefficients2, 0, array3, 0, n);
                for (int i = n; i < coefficients2.length; ++i) {
                    array3[i] = this.field.add(array2[i - n], coefficients2[i]);
                }
                o = new ModulusPoly(this.field, array3);
            }
        }
        return (ModulusPoly)o;
    }
    
    int evaluateAt(int add) {
        int n = 0;
        int coefficient;
        if (add == 0) {
            coefficient = this.getCoefficient(0);
        }
        else if (add == 1) {
            add = 0;
            final int[] coefficients = this.coefficients;
            final int length = coefficients.length;
            while (true) {
                coefficient = add;
                if (n >= length) {
                    break;
                }
                add = this.field.add(add, coefficients[n]);
                ++n;
            }
        }
        else {
            int add2 = this.coefficients[0];
            final int length2 = this.coefficients.length;
            int n2 = 1;
            while (true) {
                coefficient = add2;
                if (n2 >= length2) {
                    break;
                }
                add2 = this.field.add(this.field.multiply(add, add2), this.coefficients[n2]);
                ++n2;
            }
        }
        return coefficient;
    }
    
    int getCoefficient(final int n) {
        return this.coefficients[this.coefficients.length - 1 - n];
    }
    
    int[] getCoefficients() {
        return this.coefficients;
    }
    
    int getDegree() {
        return this.coefficients.length - 1;
    }
    
    boolean isZero() {
        boolean b = false;
        if (this.coefficients[0] == 0) {
            b = true;
        }
        return b;
    }
    
    ModulusPoly multiply(final int n) {
        ModulusPoly zero;
        if (n == 0) {
            zero = this.field.getZero();
        }
        else {
            zero = this;
            if (n != 1) {
                final int length = this.coefficients.length;
                final int[] array = new int[length];
                for (int i = 0; i < length; ++i) {
                    array[i] = this.field.multiply(this.coefficients[i], n);
                }
                zero = new ModulusPoly(this.field, array);
            }
        }
        return zero;
    }
    
    ModulusPoly multiply(ModulusPoly zero) {
        if (!this.field.equals(((ModulusPoly)zero).field)) {
            throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
        }
        if (this.isZero() || ((ModulusPoly)zero).isZero()) {
            zero = this.field.getZero();
        }
        else {
            final int[] coefficients = this.coefficients;
            final int length = coefficients.length;
            final int[] coefficients2 = ((ModulusPoly)zero).coefficients;
            final int length2 = coefficients2.length;
            final int[] array = new int[length + length2 - 1];
            for (int i = 0; i < length; ++i) {
                final int n = coefficients[i];
                for (int j = 0; j < length2; ++j) {
                    array[i + j] = this.field.add(array[i + j], this.field.multiply(n, coefficients2[j]));
                }
            }
            zero = new ModulusPoly(this.field, array);
        }
        return (ModulusPoly)zero;
    }
    
    ModulusPoly multiplyByMonomial(int i, final int n) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        ModulusPoly zero;
        if (n == 0) {
            zero = this.field.getZero();
        }
        else {
            final int length = this.coefficients.length;
            final int[] array = new int[length + i];
            for (i = 0; i < length; ++i) {
                array[i] = this.field.multiply(this.coefficients[i], n);
            }
            zero = new ModulusPoly(this.field, array);
        }
        return zero;
    }
    
    ModulusPoly negative() {
        final int length = this.coefficients.length;
        final int[] array = new int[length];
        for (int i = 0; i < length; ++i) {
            array[i] = this.field.subtract(0, this.coefficients[i]);
        }
        return new ModulusPoly(this.field, array);
    }
    
    ModulusPoly subtract(ModulusPoly add) {
        if (!this.field.equals(add.field)) {
            throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
        }
        if (add.isZero()) {
            add = this;
        }
        else {
            add = this.add(add.negative());
        }
        return add;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getDegree() * 8);
        for (int i = this.getDegree(); i >= 0; --i) {
            final int coefficient = this.getCoefficient(i);
            if (coefficient != 0) {
                int j;
                if (coefficient < 0) {
                    sb.append(" - ");
                    j = -coefficient;
                }
                else {
                    j = coefficient;
                    if (sb.length() > 0) {
                        sb.append(" + ");
                        j = coefficient;
                    }
                }
                if (i == 0 || j != 1) {
                    sb.append(j);
                }
                if (i != 0) {
                    if (i == 1) {
                        sb.append('x');
                    }
                    else {
                        sb.append("x^");
                        sb.append(i);
                    }
                }
            }
        }
        return sb.toString();
    }
}
