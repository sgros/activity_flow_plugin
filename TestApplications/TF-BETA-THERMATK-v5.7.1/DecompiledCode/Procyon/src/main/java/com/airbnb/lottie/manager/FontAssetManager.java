// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.manager;

import com.airbnb.lottie.utils.Logger;
import android.view.View;
import java.util.HashMap;
import android.graphics.drawable.Drawable$Callback;
import com.airbnb.lottie.model.MutablePair;
import android.graphics.Typeface;
import java.util.Map;
import com.airbnb.lottie.FontAssetDelegate;
import android.content.res.AssetManager;

public class FontAssetManager
{
    private final AssetManager assetManager;
    private String defaultFontFileExtension;
    private FontAssetDelegate delegate;
    private final Map<String, Typeface> fontFamilies;
    private final Map<MutablePair<String>, Typeface> fontMap;
    private final MutablePair<String> tempPair;
    
    public FontAssetManager(final Drawable$Callback drawable$Callback, final FontAssetDelegate delegate) {
        this.tempPair = new MutablePair<String>();
        this.fontMap = new HashMap<MutablePair<String>, Typeface>();
        this.fontFamilies = new HashMap<String, Typeface>();
        this.defaultFontFileExtension = ".ttf";
        this.delegate = delegate;
        if (!(drawable$Callback instanceof View)) {
            Logger.warning("LottieDrawable must be inside of a view for images to work.");
            this.assetManager = null;
            return;
        }
        this.assetManager = ((View)drawable$Callback).getContext().getAssets();
    }
    
    private Typeface getFontFamily(final String str) {
        final Typeface typeface = this.fontFamilies.get(str);
        if (typeface != null) {
            return typeface;
        }
        final FontAssetDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.fetchFont(str);
            throw null;
        }
        if (delegate != null) {
            delegate.getFontPath(str);
            throw null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("fonts/");
        sb.append(str);
        sb.append(this.defaultFontFileExtension);
        final Typeface fromAsset = Typeface.createFromAsset(this.assetManager, sb.toString());
        this.fontFamilies.put(str, fromAsset);
        return fromAsset;
    }
    
    private Typeface typefaceForStyle(final Typeface typeface, final String s) {
        final boolean contains = s.contains("Italic");
        final boolean contains2 = s.contains("Bold");
        int n;
        if (contains && contains2) {
            n = 3;
        }
        else if (contains) {
            n = 2;
        }
        else if (contains2) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (typeface.getStyle() == n) {
            return typeface;
        }
        return Typeface.create(typeface, n);
    }
    
    public Typeface getTypeface(final String s, final String s2) {
        this.tempPair.set(s, s2);
        final Typeface typeface = this.fontMap.get(this.tempPair);
        if (typeface != null) {
            return typeface;
        }
        final Typeface typefaceForStyle = this.typefaceForStyle(this.getFontFamily(s), s2);
        this.fontMap.put(this.tempPair, typefaceForStyle);
        return typefaceForStyle;
    }
    
    public void setDelegate(final FontAssetDelegate delegate) {
        this.delegate = delegate;
    }
}
