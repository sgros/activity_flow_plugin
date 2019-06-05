// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.matrix;

public final class Vector3
{
    public static final Vector3 UNIT_X;
    public static final Vector3 UNIT_Y;
    public static final Vector3 UNIT_Z;
    public static final Vector3 ZERO;
    public float x;
    public float y;
    public float z;
    
    static {
        ZERO = new Vector3(0.0f, 0.0f, 0.0f);
        UNIT_X = new Vector3(1.0f, 0.0f, 0.0f);
        UNIT_Y = new Vector3(0.0f, 1.0f, 0.0f);
        UNIT_Z = new Vector3(0.0f, 0.0f, 1.0f);
    }
    
    public Vector3() {
    }
    
    public Vector3(final float n, final float n2, final float n3) {
        this.set(n, n2, n3);
    }
    
    public Vector3(final Vector3 vector3) {
        this.set(vector3);
    }
    
    public Vector3(final float[] array) {
        this.set(array[0], array[1], array[2]);
    }
    
    public final void add(final float n, final float n2, final float n3) {
        this.x += n;
        this.y += n2;
        this.z += n3;
    }
    
    public final void add(final Vector3 vector3) {
        this.x += vector3.x;
        this.y += vector3.y;
        this.z += vector3.z;
    }
    
    public final Vector3 cross(final Vector3 vector3) {
        return new Vector3(this.y * vector3.z - this.z * vector3.y, this.z * vector3.x - this.x * vector3.z, this.x * vector3.y - this.y * vector3.x);
    }
    
    public final float distance2(final Vector3 vector3) {
        final float n = this.x - vector3.x;
        final float n2 = this.y - vector3.y;
        final float n3 = this.z - vector3.z;
        return n * n + n2 * n2 + n3 * n3;
    }
    
    public final void divide(final float n) {
        if (n != 0.0f) {
            this.x /= n;
            this.y /= n;
            this.z /= n;
        }
    }
    
    public final float dot(final Vector3 vector3) {
        return this.x * vector3.x + this.y * vector3.y + this.z * vector3.z;
    }
    
    public final float length() {
        return (float)Math.sqrt(this.length2());
    }
    
    public final float length2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public final void multiply(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }
    
    public final void multiply(final Vector3 vector3) {
        this.x *= vector3.x;
        this.y *= vector3.y;
        this.z *= vector3.z;
    }
    
    public final float normalize() {
        final float length = this.length();
        if (length != 0.0f) {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }
        return length;
    }
    
    public final boolean pointsInSameDirection(final Vector3 vector3) {
        return this.dot(vector3) > 0.0f;
    }
    
    public final void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public final void set(final Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }
    
    public final void subtract(final Vector3 vector3) {
        this.x -= vector3.x;
        this.y -= vector3.y;
        this.z -= vector3.z;
    }
    
    public final void subtractMultiple(final Vector3 vector3, final float n) {
        this.x -= vector3.x * n;
        this.y -= vector3.y * n;
        this.z -= vector3.z * n;
    }
    
    public final void zero() {
        this.set(0.0f, 0.0f, 0.0f);
    }
}
