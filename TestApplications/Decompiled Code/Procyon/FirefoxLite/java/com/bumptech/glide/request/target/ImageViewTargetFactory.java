// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageViewTargetFactory
{
    public <Z> Target<Z> buildTarget(final ImageView imageView, final Class<Z> clazz) {
        if (Bitmap.class.equals(clazz)) {
            return (Target<Z>)new BitmapImageViewTarget(imageView);
        }
        if (Drawable.class.isAssignableFrom(clazz)) {
            return (Target<Z>)new DrawableImageViewTarget(imageView);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unhandled class: ");
        sb.append(clazz);
        sb.append(", try .as*(Class).transcode(ResourceTranscoder)");
        throw new IllegalArgumentException(sb.toString());
    }
}
