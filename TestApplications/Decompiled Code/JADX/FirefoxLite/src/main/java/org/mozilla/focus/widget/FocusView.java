package org.mozilla.focus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.support.p001v4.content.ContextCompat;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.C0769R;

/* compiled from: FocusView.kt */
public final class FocusView extends View {
    private int centerX;
    private int centerY;
    private final Path path = new Path();
    private int radius;
    private int statusBarOffset;
    private final Paint transparentPaint = new Paint();

    public FocusView(Context context, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context);
        this.centerX = i;
        this.centerY = i2;
        this.statusBarOffset = ViewUtils.getStatusBarHeight((Activity) context);
        this.radius = i3;
        initPaints();
    }

    private final void initPaints() {
        this.transparentPaint.setColor(0);
        this.transparentPaint.setStrokeWidth(10.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        super.onDraw(canvas);
        this.path.reset();
        this.path.addCircle((float) this.centerX, (float) (this.centerY - this.statusBarOffset), (float) this.radius, Direction.CW);
        this.path.setFillType(FillType.INVERSE_EVEN_ODD);
        canvas.drawCircle((float) this.centerX, (float) (this.centerY - this.statusBarOffset), (float) this.radius, this.transparentPaint);
        canvas.clipPath(this.path);
        canvas.drawColor(ContextCompat.getColor(getContext(), C0769R.color.myShotOnBoardingBackground));
    }
}
