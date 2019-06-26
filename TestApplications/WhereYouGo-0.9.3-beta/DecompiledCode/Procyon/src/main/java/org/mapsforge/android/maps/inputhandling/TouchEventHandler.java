// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.inputhandling;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector$OnScaleGestureListener;
import android.view.ViewConfiguration;
import android.content.Context;
import java.util.Timer;
import android.view.ScaleGestureDetector;
import org.mapsforge.core.model.Point;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.MapView;

public class TouchEventHandler
{
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId;
    private final float doubleTapDelta;
    private final int doubleTapTimeout;
    private long doubleTouchStart;
    private final float mapMoveDelta;
    private MapView mapView;
    private final MapViewPosition mapViewPosition;
    private boolean moveThresholdReached;
    private boolean previousEventTap;
    private Point previousPosition;
    private Point previousTapPosition;
    private long previousTapTime;
    private final ScaleGestureDetector scaleGestureDetector;
    protected Timer singleTapActionTimer;
    
    public TouchEventHandler(final Context context, final MapView mapView) {
        this.mapView = mapView;
        this.mapViewPosition = mapView.getMapViewPosition();
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mapMoveDelta = (float)value.getScaledTouchSlop();
        this.doubleTapDelta = (float)value.getScaledDoubleTapSlop();
        this.doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        this.scaleGestureDetector = new ScaleGestureDetector(context, (ScaleGestureDetector$OnScaleGestureListener)new ScaleListener(mapView));
    }
    
    public static int getAction(final MotionEvent motionEvent) {
        return motionEvent.getAction() & 0xFF;
    }
    
    private boolean onActionCancel() {
        this.activePointerId = -1;
        return true;
    }
    
    private boolean onActionDown(final MotionEvent motionEvent) {
        this.activePointerId = motionEvent.getPointerId(0);
        this.previousPosition = new Point(motionEvent.getX(), motionEvent.getY());
        this.moveThresholdReached = false;
        return true;
    }
    
    private boolean onActionMove(final MotionEvent motionEvent) {
        if (!this.scaleGestureDetector.isInProgress()) {
            final int pointerIndex = motionEvent.findPointerIndex(this.activePointerId);
            final float a = (float)(motionEvent.getX(pointerIndex) - this.previousPosition.x);
            final float a2 = (float)(motionEvent.getY(pointerIndex) - this.previousPosition.y);
            if (!this.moveThresholdReached) {
                if (Math.abs(a) > this.mapMoveDelta || Math.abs(a2) > this.mapMoveDelta) {
                    this.moveThresholdReached = true;
                    this.previousPosition = new Point(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
                }
            }
            else {
                this.previousPosition = new Point(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
                this.mapViewPosition.moveCenter(a, a2);
            }
        }
        return true;
    }
    
    private boolean onActionPointerDown(final MotionEvent motionEvent) {
        this.doubleTouchStart = motionEvent.getEventTime();
        return true;
    }
    
    private boolean onActionPointerUp(final MotionEvent motionEvent) {
        final int n = (motionEvent.getAction() & 0xFF00) >> 8;
        if (motionEvent.getPointerId(n) == this.activePointerId) {
            int n2;
            if (n == 0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            this.activePointerId = motionEvent.getPointerId(n2);
            this.previousPosition = new Point(motionEvent.getX(n2), motionEvent.getY(n2));
        }
        if (motionEvent.getEventTime() - this.doubleTouchStart < this.doubleTapTimeout) {
            this.previousEventTap = false;
            this.mapViewPosition.zoomOut();
        }
        return true;
    }
    
    private boolean onActionUp(final MotionEvent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0        
        //     2: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.activePointerId:I
        //     5: invokevirtual   android/view/MotionEvent.findPointerIndex:(I)I
        //     8: istore_2       
        //     9: aload_0        
        //    10: iconst_m1      
        //    11: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.activePointerId:I
        //    14: aload_0        
        //    15: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.moveThresholdReached:Z
        //    18: ifeq            28
        //    21: aload_0        
        //    22: iconst_0       
        //    23: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousEventTap:Z
        //    26: iconst_1       
        //    27: ireturn        
        //    28: aload_0        
        //    29: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousEventTap:Z
        //    32: ifeq            156
        //    35: aload_1        
        //    36: iload_2        
        //    37: invokevirtual   android/view/MotionEvent.getX:(I)F
        //    40: f2d            
        //    41: aload_0        
        //    42: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousTapPosition:Lorg/mapsforge/core/model/Point;
        //    45: getfield        org/mapsforge/core/model/Point.x:D
        //    48: dsub           
        //    49: invokestatic    java/lang/Math.abs:(D)D
        //    52: dstore_3       
        //    53: aload_1        
        //    54: iload_2        
        //    55: invokevirtual   android/view/MotionEvent.getY:(I)F
        //    58: f2d            
        //    59: aload_0        
        //    60: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousTapPosition:Lorg/mapsforge/core/model/Point;
        //    63: getfield        org/mapsforge/core/model/Point.y:D
        //    66: dsub           
        //    67: invokestatic    java/lang/Math.abs:(D)D
        //    70: dstore          5
        //    72: aload_1        
        //    73: invokevirtual   android/view/MotionEvent.getEventTime:()J
        //    76: lstore          7
        //    78: aload_0        
        //    79: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousTapTime:J
        //    82: lstore          9
        //    84: dload_3        
        //    85: aload_0        
        //    86: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.doubleTapDelta:F
        //    89: f2d            
        //    90: dcmpg          
        //    91: ifge            161
        //    94: dload           5
        //    96: aload_0        
        //    97: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.doubleTapDelta:F
        //   100: f2d            
        //   101: dcmpg          
        //   102: ifge            161
        //   105: lload           7
        //   107: lload           9
        //   109: lsub           
        //   110: aload_0        
        //   111: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.doubleTapTimeout:I
        //   114: i2l            
        //   115: lcmp           
        //   116: ifge            161
        //   119: aload_0        
        //   120: iconst_0       
        //   121: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousEventTap:Z
        //   124: aload_0        
        //   125: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapViewPosition:Lorg/mapsforge/android/maps/MapViewPosition;
        //   128: invokevirtual   org/mapsforge/android/maps/MapViewPosition.zoomIn:()V
        //   131: aload_0        
        //   132: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   135: ifnull          145
        //   138: aload_0        
        //   139: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   142: invokevirtual   java/util/Timer.cancel:()V
        //   145: goto            26
        //   148: astore_1       
        //   149: aload_1        
        //   150: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   153: goto            145
        //   156: aload_0        
        //   157: iconst_1       
        //   158: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousEventTap:Z
        //   161: aload_0        
        //   162: new             Lorg/mapsforge/core/model/Point;
        //   165: dup            
        //   166: aload_1        
        //   167: iload_2        
        //   168: invokevirtual   android/view/MotionEvent.getX:(I)F
        //   171: f2d            
        //   172: aload_1        
        //   173: iload_2        
        //   174: invokevirtual   android/view/MotionEvent.getY:(I)F
        //   177: f2d            
        //   178: invokespecial   org/mapsforge/core/model/Point.<init>:(DD)V
        //   181: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousTapPosition:Lorg/mapsforge/core/model/Point;
        //   184: aload_0        
        //   185: aload_1        
        //   186: invokevirtual   android/view/MotionEvent.getEventTime:()J
        //   189: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.previousTapTime:J
        //   192: aconst_null    
        //   193: astore          11
        //   195: aload_1        
        //   196: iload_2        
        //   197: invokevirtual   android/view/MotionEvent.getX:(I)F
        //   200: f2i            
        //   201: istore          12
        //   203: aload_1        
        //   204: iload_2        
        //   205: invokevirtual   android/view/MotionEvent.getY:(I)F
        //   208: f2i            
        //   209: istore_2       
        //   210: aload_0        
        //   211: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapView:Lorg/mapsforge/android/maps/MapView;
        //   214: invokevirtual   org/mapsforge/android/maps/MapView.getProjection:()Lorg/mapsforge/android/maps/Projection;
        //   217: iload           12
        //   219: iload_2        
        //   220: invokeinterface org/mapsforge/android/maps/Projection.fromPixels:(II)Lorg/mapsforge/core/model/GeoPoint;
        //   225: astore_1       
        //   226: aload_1        
        //   227: ifnull          506
        //   230: aload_0        
        //   231: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapView:Lorg/mapsforge/android/maps/MapView;
        //   234: astore          13
        //   236: iconst_0       
        //   237: istore          12
        //   239: aload_0        
        //   240: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapView:Lorg/mapsforge/android/maps/MapView;
        //   243: invokevirtual   org/mapsforge/android/maps/MapView.getOverlays:()Ljava/util/List;
        //   246: astore          11
        //   248: aload           11
        //   250: monitorenter   
        //   251: aload_0        
        //   252: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapView:Lorg/mapsforge/android/maps/MapView;
        //   255: invokevirtual   org/mapsforge/android/maps/MapView.getOverlays:()Ljava/util/List;
        //   258: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   263: astore          14
        //   265: iload           12
        //   267: istore_2       
        //   268: aload           14
        //   270: invokeinterface java/util/Iterator.hasNext:()Z
        //   275: ifeq            461
        //   278: aload           14
        //   280: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   285: checkcast       Lorg/mapsforge/android/maps/overlay/Overlay;
        //   288: astore          15
        //   290: iload           12
        //   292: istore_2       
        //   293: aload           15
        //   295: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   298: ldc             "checkItemHit"
        //   300: iconst_2       
        //   301: anewarray       Ljava/lang/Class;
        //   304: dup            
        //   305: iconst_0       
        //   306: ldc             Lorg/mapsforge/core/model/GeoPoint;.class
        //   308: aastore        
        //   309: dup            
        //   310: iconst_1       
        //   311: ldc             Lorg/mapsforge/android/maps/MapView;.class
        //   313: aastore        
        //   314: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   317: astore          16
        //   319: iload           12
        //   321: istore_2       
        //   322: aload           15
        //   324: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   327: ldc             "onTap"
        //   329: iconst_1       
        //   330: anewarray       Ljava/lang/Class;
        //   333: dup            
        //   334: iconst_0       
        //   335: ldc             Lorg/mapsforge/core/model/GeoPoint;.class
        //   337: aastore        
        //   338: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   341: astore          17
        //   343: iload           12
        //   345: istore_2       
        //   346: aload           16
        //   348: aload           15
        //   350: iconst_2       
        //   351: anewarray       Ljava/lang/Object;
        //   354: dup            
        //   355: iconst_0       
        //   356: aload_1        
        //   357: aastore        
        //   358: dup            
        //   359: iconst_1       
        //   360: aload_0        
        //   361: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.mapView:Lorg/mapsforge/android/maps/MapView;
        //   364: aastore        
        //   365: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   368: checkcast       Ljava/lang/Boolean;
        //   371: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   374: ifeq            265
        //   377: iconst_1       
        //   378: istore          18
        //   380: iconst_1       
        //   381: istore          12
        //   383: iload           18
        //   385: istore_2       
        //   386: new             Ljava/util/Timer;
        //   389: astore          16
        //   391: iload           18
        //   393: istore_2       
        //   394: aload           16
        //   396: invokespecial   java/util/Timer.<init>:()V
        //   399: iload           18
        //   401: istore_2       
        //   402: aload_0        
        //   403: aload           16
        //   405: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   408: iload           18
        //   410: istore_2       
        //   411: aload_0        
        //   412: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   415: astore          19
        //   417: iload           18
        //   419: istore_2       
        //   420: new             Lorg/mapsforge/android/maps/inputhandling/TouchEventHandler$1;
        //   423: astore          16
        //   425: iload           18
        //   427: istore_2       
        //   428: aload           16
        //   430: aload_0        
        //   431: aload           17
        //   433: aload           15
        //   435: aload_1        
        //   436: invokespecial   org/mapsforge/android/maps/inputhandling/TouchEventHandler$1.<init>:(Lorg/mapsforge/android/maps/inputhandling/TouchEventHandler;Ljava/lang/reflect/Method;Lorg/mapsforge/android/maps/overlay/Overlay;Lorg/mapsforge/core/model/GeoPoint;)V
        //   439: iload           18
        //   441: istore_2       
        //   442: aload           19
        //   444: aload           16
        //   446: aload_0        
        //   447: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.doubleTapTimeout:I
        //   450: i2l            
        //   451: ldc2_w          10
        //   454: ladd           
        //   455: invokevirtual   java/util/Timer.schedule:(Ljava/util/TimerTask;J)V
        //   458: iload           12
        //   460: istore_2       
        //   461: aload           11
        //   463: monitorexit    
        //   464: iload_2        
        //   465: ifne            506
        //   468: aload_0        
        //   469: new             Ljava/util/Timer;
        //   472: dup            
        //   473: invokespecial   java/util/Timer.<init>:()V
        //   476: putfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   479: aload_0        
        //   480: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.singleTapActionTimer:Ljava/util/Timer;
        //   483: new             Lorg/mapsforge/android/maps/inputhandling/TouchEventHandler$2;
        //   486: dup            
        //   487: aload_0        
        //   488: aload           13
        //   490: aload_1        
        //   491: invokespecial   org/mapsforge/android/maps/inputhandling/TouchEventHandler$2.<init>:(Lorg/mapsforge/android/maps/inputhandling/TouchEventHandler;Lorg/mapsforge/android/maps/MapView;Lorg/mapsforge/core/model/GeoPoint;)V
        //   494: aload_0        
        //   495: getfield        org/mapsforge/android/maps/inputhandling/TouchEventHandler.doubleTapTimeout:I
        //   498: i2l            
        //   499: ldc2_w          10
        //   502: ladd           
        //   503: invokevirtual   java/util/Timer.schedule:(Ljava/util/TimerTask;J)V
        //   506: goto            26
        //   509: astore_1       
        //   510: aload_1        
        //   511: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   514: aload           11
        //   516: astore_1       
        //   517: goto            226
        //   520: astore          15
        //   522: aload           15
        //   524: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   527: iload_2        
        //   528: istore          12
        //   530: goto            265
        //   533: astore_1       
        //   534: aload           11
        //   536: monitorexit    
        //   537: aload_1        
        //   538: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  131    145    148    156    Ljava/lang/Exception;
        //  195    226    509    520    Ljava/lang/Exception;
        //  251    265    533    539    Any
        //  268    290    533    539    Any
        //  293    319    520    533    Ljava/lang/Exception;
        //  293    319    533    539    Any
        //  322    343    520    533    Ljava/lang/Exception;
        //  322    343    533    539    Any
        //  346    377    520    533    Ljava/lang/Exception;
        //  346    377    533    539    Any
        //  386    391    520    533    Ljava/lang/Exception;
        //  386    391    533    539    Any
        //  394    399    520    533    Ljava/lang/Exception;
        //  394    399    533    539    Any
        //  402    408    520    533    Ljava/lang/Exception;
        //  402    408    533    539    Any
        //  411    417    520    533    Ljava/lang/Exception;
        //  411    417    533    539    Any
        //  420    425    520    533    Ljava/lang/Exception;
        //  420    425    533    539    Any
        //  428    439    520    533    Ljava/lang/Exception;
        //  428    439    533    539    Any
        //  442    458    520    533    Ljava/lang/Exception;
        //  442    458    533    539    Any
        //  461    464    533    539    Any
        //  522    527    533    539    Any
        //  534    537    533    539    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0265:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int action = getAction(motionEvent);
        if (action != 2 || motionEvent.getPointerCount() > 1) {
            this.scaleGestureDetector.onTouchEvent(motionEvent);
        }
        boolean b = false;
        switch (action) {
            default: {
                b = false;
                break;
            }
            case 0: {
                b = this.onActionDown(motionEvent);
                break;
            }
            case 2: {
                b = this.onActionMove(motionEvent);
                break;
            }
            case 1: {
                b = this.onActionUp(motionEvent);
                break;
            }
            case 3: {
                b = this.onActionCancel();
                break;
            }
            case 5: {
                b = this.onActionPointerDown(motionEvent);
                break;
            }
            case 6: {
                b = this.onActionPointerUp(motionEvent);
                break;
            }
        }
        return b;
    }
}
