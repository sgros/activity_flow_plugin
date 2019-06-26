// 
// Decompiled by Procyon v0.5.34
// 

package com.journeyapps.barcodescanner;

import android.support.annotation.NonNull;

public class Size implements Comparable<Size>
{
    public final int height;
    public final int width;
    
    public Size(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public int compareTo(@NonNull final Size size) {
        final int n = this.height * this.width;
        final int n2 = size.height * size.width;
        int n3;
        if (n2 < n) {
            n3 = 1;
        }
        else if (n2 > n) {
            n3 = -1;
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                b = false;
            }
            else {
                final Size size = (Size)o;
                if (this.width != size.width || this.height != size.height) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    public boolean fitsIn(final Size size) {
        return this.width <= size.width && this.height <= size.height;
    }
    
    @Override
    public int hashCode() {
        return this.width * 31 + this.height;
    }
    
    public Size rotate() {
        return new Size(this.height, this.width);
    }
    
    public Size scale(final int n, final int n2) {
        return new Size(this.width * n / n2, this.height * n / n2);
    }
    
    public Size scaleCrop(Size size) {
        if (this.width * size.height <= size.width * this.height) {
            size = new Size(size.width, this.height * size.width / this.width);
        }
        else {
            size = new Size(this.width * size.height / this.height, size.height);
        }
        return size;
    }
    
    public Size scaleFit(Size size) {
        if (this.width * size.height >= size.width * this.height) {
            size = new Size(size.width, this.height * size.width / this.width);
        }
        else {
            size = new Size(this.width * size.height / this.height, size.height);
        }
        return size;
    }
    
    @Override
    public String toString() {
        return this.width + "x" + this.height;
    }
}
