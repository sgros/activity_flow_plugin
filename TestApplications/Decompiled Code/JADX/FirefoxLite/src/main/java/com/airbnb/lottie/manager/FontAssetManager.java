package com.airbnb.lottie.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable.Callback;
import android.util.Log;
import android.view.View;
import com.airbnb.lottie.FontAssetDelegate;
import com.airbnb.lottie.model.MutablePair;
import java.util.HashMap;
import java.util.Map;

public class FontAssetManager {
    private final AssetManager assetManager;
    private String defaultFontFileExtension = ".ttf";
    private FontAssetDelegate delegate;
    private final Map<String, Typeface> fontFamilies = new HashMap();
    private final Map<MutablePair<String>, Typeface> fontMap = new HashMap();
    private final MutablePair<String> tempPair = new MutablePair();

    public FontAssetManager(Callback callback, FontAssetDelegate fontAssetDelegate) {
        this.delegate = fontAssetDelegate;
        if (callback instanceof View) {
            this.assetManager = ((View) callback).getContext().getAssets();
            return;
        }
        Log.w("LOTTIE", "LottieDrawable must be inside of a view for images to work.");
        this.assetManager = null;
    }

    public void setDelegate(FontAssetDelegate fontAssetDelegate) {
        this.delegate = fontAssetDelegate;
    }

    public Typeface getTypeface(String str, String str2) {
        this.tempPair.set(str, str2);
        Typeface typeface = (Typeface) this.fontMap.get(this.tempPair);
        if (typeface != null) {
            return typeface;
        }
        Typeface typefaceForStyle = typefaceForStyle(getFontFamily(str), str2);
        this.fontMap.put(this.tempPair, typefaceForStyle);
        return typefaceForStyle;
    }

    private Typeface getFontFamily(String str) {
        Typeface typeface = (Typeface) this.fontFamilies.get(str);
        if (typeface != null) {
            return typeface;
        }
        Object obj = null;
        if (this.delegate != null) {
            obj = this.delegate.fetchFont(str);
        }
        if (this.delegate != null && r0 == null) {
            String fontPath = this.delegate.getFontPath(str);
            if (fontPath != null) {
                obj = Typeface.createFromAsset(this.assetManager, fontPath);
            }
        }
        if (obj == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fonts/");
            stringBuilder.append(str);
            stringBuilder.append(this.defaultFontFileExtension);
            obj = Typeface.createFromAsset(this.assetManager, stringBuilder.toString());
        }
        this.fontFamilies.put(str, obj);
        return obj;
    }

    private Typeface typefaceForStyle(Typeface typeface, String str) {
        boolean contains = str.contains("Italic");
        boolean contains2 = str.contains("Bold");
        int i = (contains && contains2) ? 3 : contains ? 2 : contains2 ? 1 : 0;
        if (typeface.getStyle() == i) {
            return typeface;
        }
        return Typeface.create(typeface, i);
    }
}
