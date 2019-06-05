// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content.data;

import kotlin.jvm.internal.Intrinsics;

public final class ShoppingLink
{
    private final String image;
    private final String name;
    private final String source;
    private final String url;
    
    public ShoppingLink(final String url, final String name, final String image, final String source) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(image, "image");
        Intrinsics.checkParameterIsNotNull(source, "source");
        this.url = url;
        this.name = name;
        this.image = image;
        this.source = source;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof ShoppingLink) {
                final ShoppingLink shoppingLink = (ShoppingLink)o;
                if (Intrinsics.areEqual(this.url, shoppingLink.url) && Intrinsics.areEqual(this.name, shoppingLink.name) && Intrinsics.areEqual(this.image, shoppingLink.image) && Intrinsics.areEqual(this.source, shoppingLink.source)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public final String getImage() {
        return this.image;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final String getSource() {
        return this.source;
    }
    
    public final String getUrl() {
        return this.url;
    }
    
    @Override
    public int hashCode() {
        final String url = this.url;
        int hashCode = 0;
        int hashCode2;
        if (url != null) {
            hashCode2 = url.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final String name = this.name;
        int hashCode3;
        if (name != null) {
            hashCode3 = name.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final String image = this.image;
        int hashCode4;
        if (image != null) {
            hashCode4 = image.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        final String source = this.source;
        if (source != null) {
            hashCode = source.hashCode();
        }
        return ((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ShoppingLink(url=");
        sb.append(this.url);
        sb.append(", name=");
        sb.append(this.name);
        sb.append(", image=");
        sb.append(this.image);
        sb.append(", source=");
        sb.append(this.source);
        sb.append(")");
        return sb.toString();
    }
}
