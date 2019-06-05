// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.AnimatorSet;
import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Picture;
import android.graphics.Bitmap;
import android.view.View$MeasureSpec;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build$VERSION;

class TransitionUtils
{
    private static final boolean HAS_IS_ATTACHED_TO_WINDOW;
    private static final boolean HAS_OVERLAY;
    private static final boolean HAS_PICTURE_BITMAP;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        HAS_IS_ATTACHED_TO_WINDOW = (sdk_INT >= 19);
        HAS_OVERLAY = (Build$VERSION.SDK_INT >= 18);
        boolean has_PICTURE_BITMAP = b;
        if (Build$VERSION.SDK_INT >= 28) {
            has_PICTURE_BITMAP = true;
        }
        HAS_PICTURE_BITMAP = has_PICTURE_BITMAP;
    }
    
    static View copyViewImage(final ViewGroup viewGroup, final View view, final View view2) {
        final Matrix matrix = new Matrix();
        matrix.setTranslate((float)(-view2.getScrollX()), (float)(-view2.getScrollY()));
        ViewUtils.transformMatrixToGlobal(view, matrix);
        ViewUtils.transformMatrixToLocal((View)viewGroup, matrix);
        final RectF rectF = new RectF(0.0f, 0.0f, (float)view.getWidth(), (float)view.getHeight());
        matrix.mapRect(rectF);
        final int round = Math.round(rectF.left);
        final int round2 = Math.round(rectF.top);
        final int round3 = Math.round(rectF.right);
        final int round4 = Math.round(rectF.bottom);
        final ImageView imageView = new ImageView(view.getContext());
        imageView.setScaleType(ImageView$ScaleType.CENTER_CROP);
        final Bitmap viewBitmap = createViewBitmap(view, matrix, rectF, viewGroup);
        if (viewBitmap != null) {
            imageView.setImageBitmap(viewBitmap);
        }
        imageView.measure(View$MeasureSpec.makeMeasureSpec(round3 - round, 1073741824), View$MeasureSpec.makeMeasureSpec(round4 - round2, 1073741824));
        imageView.layout(round, round2, round3, round4);
        return (View)imageView;
    }
    
    private static Bitmap createViewBitmap(final View view, final Matrix matrix, final RectF rectF, final ViewGroup viewGroup) {
        boolean b = false;
        boolean attachedToWindow = false;
        Label_0036: {
            if (TransitionUtils.HAS_IS_ATTACHED_TO_WINDOW) {
                b = (view.isAttachedToWindow() ^ true);
                if (viewGroup != null) {
                    attachedToWindow = viewGroup.isAttachedToWindow();
                    break Label_0036;
                }
            }
            else {
                b = false;
            }
            attachedToWindow = false;
        }
        final boolean has_OVERLAY = TransitionUtils.HAS_OVERLAY;
        final Bitmap bitmap = null;
        ViewGroup viewGroup2;
        int indexOfChild;
        if (has_OVERLAY && b) {
            if (!attachedToWindow) {
                return null;
            }
            viewGroup2 = (ViewGroup)view.getParent();
            indexOfChild = viewGroup2.indexOfChild(view);
            viewGroup.getOverlay().add(view);
        }
        else {
            viewGroup2 = null;
            indexOfChild = 0;
        }
        final int round = Math.round(rectF.width());
        final int round2 = Math.round(rectF.height());
        Bitmap bitmap2 = bitmap;
        if (round > 0) {
            bitmap2 = bitmap;
            if (round2 > 0) {
                final float min = Math.min(1.0f, 1048576.0f / (round * round2));
                final int round3 = Math.round(round * min);
                final int round4 = Math.round(round2 * min);
                matrix.postTranslate(-rectF.left, -rectF.top);
                matrix.postScale(min, min);
                if (TransitionUtils.HAS_PICTURE_BITMAP) {
                    final Picture picture = new Picture();
                    final Canvas beginRecording = picture.beginRecording(round3, round4);
                    beginRecording.concat(matrix);
                    view.draw(beginRecording);
                    picture.endRecording();
                    bitmap2 = Bitmap.createBitmap(picture);
                }
                else {
                    bitmap2 = Bitmap.createBitmap(round3, round4, Bitmap$Config.ARGB_8888);
                    final Canvas canvas = new Canvas(bitmap2);
                    canvas.concat(matrix);
                    view.draw(canvas);
                }
            }
        }
        if (TransitionUtils.HAS_OVERLAY && b) {
            viewGroup.getOverlay().remove(view);
            viewGroup2.addView(view, indexOfChild);
        }
        return bitmap2;
    }
    
    static Animator mergeAnimators(final Animator animator, final Animator animator2) {
        if (animator == null) {
            return animator2;
        }
        if (animator2 == null) {
            return animator;
        }
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] { animator, animator2 });
        return (Animator)set;
    }
}
