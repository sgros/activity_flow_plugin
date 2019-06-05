package org.mozilla.focus.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DrawableWrapper extends Drawable {
   private final Drawable mWrapped;

   public DrawableWrapper(Drawable var1) {
      this.mWrapped = var1;
   }

   public void draw(Canvas var1) {
      this.mWrapped.draw(var1);
   }

   public int getChangingConfigurations() {
      return this.mWrapped.getChangingConfigurations();
   }

   public ConstantState getConstantState() {
      return this.mWrapped.getConstantState();
   }

   public Drawable getCurrent() {
      return this.mWrapped.getCurrent();
   }

   public int getIntrinsicHeight() {
      return this.mWrapped.getIntrinsicHeight();
   }

   public int getIntrinsicWidth() {
      return this.mWrapped.getIntrinsicWidth();
   }

   public int getMinimumHeight() {
      return this.mWrapped.getMinimumHeight();
   }

   public int getMinimumWidth() {
      return this.mWrapped.getMinimumWidth();
   }

   public int getOpacity() {
      return this.mWrapped.getOpacity();
   }

   public boolean getPadding(Rect var1) {
      return this.mWrapped.getPadding(var1);
   }

   public int[] getState() {
      return this.mWrapped.getState();
   }

   public Region getTransparentRegion() {
      return this.mWrapped.getTransparentRegion();
   }

   public Drawable getWrappedDrawable() {
      return this.mWrapped;
   }

   public void inflate(Resources var1, XmlPullParser var2, AttributeSet var3) throws XmlPullParserException, IOException {
      this.mWrapped.inflate(var1, var2, var3);
   }

   public boolean isStateful() {
      return this.mWrapped.isStateful();
   }

   public void jumpToCurrentState() {
      this.mWrapped.jumpToCurrentState();
   }

   public Drawable mutate() {
      return this.mWrapped.mutate();
   }

   protected void onBoundsChange(Rect var1) {
      this.mWrapped.setBounds(var1);
   }

   protected boolean onLevelChange(int var1) {
      return this.mWrapped.setLevel(var1);
   }

   protected boolean onStateChange(int[] var1) {
      return this.mWrapped.setState(var1);
   }

   public void scheduleSelf(Runnable var1, long var2) {
      this.mWrapped.scheduleSelf(var1, var2);
   }

   public void setAlpha(int var1) {
      this.mWrapped.setAlpha(var1);
   }

   public void setChangingConfigurations(int var1) {
      this.mWrapped.setChangingConfigurations(var1);
   }

   public void setColorFilter(int var1, Mode var2) {
      this.mWrapped.setColorFilter(var1, var2);
   }

   public void setColorFilter(ColorFilter var1) {
      this.mWrapped.setColorFilter(var1);
   }

   public void setFilterBitmap(boolean var1) {
      this.mWrapped.setFilterBitmap(var1);
   }

   public boolean setVisible(boolean var1, boolean var2) {
      return this.mWrapped.setVisible(var1, var2);
   }

   public void unscheduleSelf(Runnable var1) {
      this.mWrapped.unscheduleSelf(var1);
   }
}
