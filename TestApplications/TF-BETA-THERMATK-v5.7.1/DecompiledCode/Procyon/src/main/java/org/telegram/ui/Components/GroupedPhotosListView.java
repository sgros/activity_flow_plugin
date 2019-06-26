// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.view.MotionEvent;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessageObject;
import java.util.Collection;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.Scroller;
import org.telegram.messenger.ImageReceiver;
import android.view.GestureDetector;
import org.telegram.messenger.ImageLocation;
import java.util.ArrayList;
import android.graphics.Paint;
import android.view.GestureDetector$OnGestureListener;
import android.view.View;

public class GroupedPhotosListView extends View implements GestureDetector$OnGestureListener
{
    private boolean animateAllLine;
    private int animateToDX;
    private int animateToDXStart;
    private int animateToItem;
    private Paint backgroundPaint;
    private long currentGroupId;
    private int currentImage;
    private float currentItemProgress;
    private ArrayList<Object> currentObjects;
    public ArrayList<ImageLocation> currentPhotos;
    private GroupedPhotosListViewDelegate delegate;
    private int drawDx;
    private GestureDetector gestureDetector;
    private boolean ignoreChanges;
    private ArrayList<ImageReceiver> imagesToDraw;
    private int itemHeight;
    private int itemSpacing;
    private int itemWidth;
    private int itemY;
    private long lastUpdateTime;
    private float moveLineProgress;
    private boolean moving;
    private int nextImage;
    private float nextItemProgress;
    private int nextPhotoScrolling;
    private Scroller scroll;
    private boolean scrolling;
    private boolean stopedScrolling;
    private ArrayList<ImageReceiver> unusedReceivers;
    
    public GroupedPhotosListView(final Context context) {
        super(context);
        this.backgroundPaint = new Paint();
        this.unusedReceivers = new ArrayList<ImageReceiver>();
        this.imagesToDraw = new ArrayList<ImageReceiver>();
        this.currentPhotos = new ArrayList<ImageLocation>();
        this.currentObjects = new ArrayList<Object>();
        this.currentItemProgress = 1.0f;
        this.nextItemProgress = 0.0f;
        this.animateToItem = -1;
        this.nextPhotoScrolling = -1;
        this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)this);
        this.scroll = new Scroller(context);
        this.itemWidth = AndroidUtilities.dp(42.0f);
        this.itemHeight = AndroidUtilities.dp(56.0f);
        this.itemSpacing = AndroidUtilities.dp(1.0f);
        this.itemY = AndroidUtilities.dp(3.0f);
        this.backgroundPaint.setColor(2130706432);
    }
    
    private void fillImages(final boolean b, final int n) {
        if (!b && !this.imagesToDraw.isEmpty()) {
            this.unusedReceivers.addAll(this.imagesToDraw);
            this.imagesToDraw.clear();
            this.moving = false;
            this.moveLineProgress = 1.0f;
            this.currentItemProgress = 1.0f;
            this.nextItemProgress = 0.0f;
        }
        this.invalidate();
        if (this.getMeasuredWidth() != 0) {
            if (!this.currentPhotos.isEmpty()) {
                final int measuredWidth = this.getMeasuredWidth();
                final int n2 = this.getMeasuredWidth() / 2 - this.itemWidth / 2;
                int i;
                int j;
                if (b) {
                    int size = this.imagesToDraw.size();
                    int n3 = 0;
                    int max = Integer.MIN_VALUE;
                    int min = Integer.MAX_VALUE;
                    while (true) {
                        i = max;
                        j = min;
                        if (n3 >= size) {
                            break;
                        }
                        final ImageReceiver e = this.imagesToDraw.get(n3);
                        final int param = e.getParam();
                        final int currentImage = this.currentImage;
                        final int itemWidth = this.itemWidth;
                        final int n4 = (param - currentImage) * (this.itemSpacing + itemWidth) + n2 + n;
                        int n5 = 0;
                        int n6 = 0;
                        Label_0243: {
                            if (n4 <= measuredWidth) {
                                n5 = n3;
                                n6 = size;
                                if (n4 + itemWidth >= 0) {
                                    break Label_0243;
                                }
                            }
                            this.unusedReceivers.add(e);
                            this.imagesToDraw.remove(n3);
                            n6 = size - 1;
                            n5 = n3 - 1;
                        }
                        min = Math.min(min, param - 1);
                        max = Math.max(max, param + 1);
                        n3 = n5 + 1;
                        size = n6;
                    }
                }
                else {
                    i = this.currentImage;
                    j = i - 1;
                }
                if (i != Integer.MIN_VALUE) {
                    while (i < this.currentPhotos.size()) {
                        final int n7 = (i - this.currentImage) * (this.itemWidth + this.itemSpacing) + n2 + n;
                        if (n7 >= measuredWidth) {
                            break;
                        }
                        final ImageLocation imageLocation = this.currentPhotos.get(i);
                        final ImageReceiver freeReceiver = this.getFreeReceiver();
                        freeReceiver.setImageCoords(n7, this.itemY, this.itemWidth, this.itemHeight);
                        Object o;
                        if (this.currentObjects.get(0) instanceof MessageObject) {
                            o = this.currentObjects.get(i);
                        }
                        else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                            o = this.delegate.getParentObject();
                        }
                        else {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("avatar_");
                            sb.append(this.delegate.getAvatarsDialogId());
                            o = sb.toString();
                        }
                        freeReceiver.setImage(null, null, imageLocation, "80_80", 0, null, o, 1);
                        freeReceiver.setParam(i);
                        ++i;
                    }
                }
                if (j != Integer.MAX_VALUE) {
                    while (j >= 0) {
                        final int currentImage2 = this.currentImage;
                        final int itemWidth2 = this.itemWidth;
                        final int n8 = (j - currentImage2) * (this.itemSpacing + itemWidth2) + n2 + n + itemWidth2;
                        if (n8 <= 0) {
                            break;
                        }
                        final ImageLocation imageLocation2 = this.currentPhotos.get(j);
                        final ImageReceiver freeReceiver2 = this.getFreeReceiver();
                        freeReceiver2.setImageCoords(n8, this.itemY, this.itemWidth, this.itemHeight);
                        Object o2;
                        if (this.currentObjects.get(0) instanceof MessageObject) {
                            o2 = this.currentObjects.get(j);
                        }
                        else if (this.currentObjects.get(0) instanceof TLRPC.PageBlock) {
                            o2 = this.delegate.getParentObject();
                        }
                        else {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("avatar_");
                            sb2.append(this.delegate.getAvatarsDialogId());
                            o2 = sb2.toString();
                        }
                        freeReceiver2.setImage(null, null, imageLocation2, "80_80", 0, null, o2, 1);
                        freeReceiver2.setParam(j);
                        --j;
                    }
                }
            }
        }
    }
    
    private ImageReceiver getFreeReceiver() {
        ImageReceiver e;
        if (this.unusedReceivers.isEmpty()) {
            e = new ImageReceiver(this);
        }
        else {
            e = this.unusedReceivers.get(0);
            this.unusedReceivers.remove(0);
        }
        this.imagesToDraw.add(e);
        e.setCurrentAccount(this.delegate.getCurrentAccount());
        return e;
    }
    
    private int getMaxScrollX() {
        return this.currentImage * (this.itemWidth + this.itemSpacing * 2);
    }
    
    private int getMinScrollX() {
        return -(this.currentPhotos.size() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing * 2);
    }
    
    private void stopScrolling() {
        this.scrolling = false;
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        final int nextPhotoScrolling = this.nextPhotoScrolling;
        if (nextPhotoScrolling >= 0 && nextPhotoScrolling < this.currentObjects.size()) {
            this.stopedScrolling = true;
            final int nextPhotoScrolling2 = this.nextPhotoScrolling;
            this.animateToItem = nextPhotoScrolling2;
            this.nextImage = nextPhotoScrolling2;
            this.animateToDX = (this.currentImage - nextPhotoScrolling2) * (this.itemWidth + this.itemSpacing);
            this.animateToDXStart = this.drawDx;
            this.moveLineProgress = 1.0f;
            this.nextPhotoScrolling = -1;
        }
        this.invalidate();
    }
    
    private void updateAfterScroll() {
        final int drawDx = this.drawDx;
        final int abs = Math.abs(drawDx);
        final int itemWidth = this.itemWidth;
        final int n = itemWidth / 2;
        final int itemSpacing = this.itemSpacing;
        final int n2 = -1;
        int n5;
        if (abs > n + itemSpacing) {
            int n3;
            int n4;
            if (drawDx > 0) {
                n3 = drawDx - (itemWidth / 2 + itemSpacing);
                n4 = 1;
            }
            else {
                n3 = drawDx + (itemWidth / 2 + itemSpacing);
                n4 = -1;
            }
            n5 = n4 + n3 / (this.itemWidth + this.itemSpacing * 2);
        }
        else {
            n5 = 0;
        }
        this.nextPhotoScrolling = this.currentImage - n5;
        final int currentIndex = this.delegate.getCurrentIndex();
        final ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        final ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        final ArrayList<TLRPC.PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        final int nextPhotoScrolling = this.nextPhotoScrolling;
        if (currentIndex != nextPhotoScrolling && nextPhotoScrolling >= 0 && nextPhotoScrolling < this.currentPhotos.size()) {
            final ImageLocation value = this.currentObjects.get(this.nextPhotoScrolling);
            int currentIndex2;
            if (imagesArr != null && !imagesArr.isEmpty()) {
                currentIndex2 = imagesArr.indexOf(value);
            }
            else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                currentIndex2 = pageBlockArr.indexOf(value);
            }
            else {
                currentIndex2 = n2;
                if (imagesArrLocations != null) {
                    currentIndex2 = n2;
                    if (!imagesArrLocations.isEmpty()) {
                        currentIndex2 = imagesArrLocations.indexOf(value);
                    }
                }
            }
            if (currentIndex2 >= 0) {
                this.ignoreChanges = true;
                this.delegate.setCurrentIndex(currentIndex2);
            }
        }
        if (!this.scrolling) {
            this.scrolling = true;
            this.stopedScrolling = false;
        }
        this.fillImages(true, this.drawDx);
    }
    
    public void clear() {
        this.currentPhotos.clear();
        this.currentObjects.clear();
        this.imagesToDraw.clear();
    }
    
    public void fillList() {
        if (this.ignoreChanges) {
            this.ignoreChanges = false;
            return;
        }
        int i = this.delegate.getCurrentIndex();
        final ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        final ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        final ArrayList<TLRPC.PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        final int slideshowMessageId = this.delegate.getSlideshowMessageId();
        this.delegate.getCurrentAccount();
        Object o = null;
        int n3 = 0;
        int n6 = 0;
        Label_0486: {
            int size = 0;
            Label_0105: {
                if (imagesArrLocations == null || imagesArrLocations.isEmpty()) {
                    Label_0483: {
                        if (imagesArr != null && !imagesArr.isEmpty()) {
                            final MessageObject messageObject = imagesArr.get(i);
                            if (messageObject.getGroupIdForUse() != this.currentGroupId) {
                                this.currentGroupId = messageObject.getGroupIdForUse();
                                o = messageObject;
                            }
                            else {
                                final int min = Math.min(i + 10, imagesArr.size());
                                int j = i;
                                int n = 0;
                                while (j < min) {
                                    final MessageObject messageObject2 = imagesArr.get(j);
                                    if (slideshowMessageId == 0 && messageObject2.getGroupIdForUse() != this.currentGroupId) {
                                        break;
                                    }
                                    ++n;
                                    ++j;
                                }
                                final int max = Math.max(i - 10, 0);
                                int index = i - 1;
                                int n2 = n;
                                while (true) {
                                    o = messageObject;
                                    size = n2;
                                    if (index < max) {
                                        break Label_0105;
                                    }
                                    final MessageObject messageObject3 = imagesArr.get(index);
                                    if (slideshowMessageId == 0) {
                                        o = messageObject;
                                        size = n2;
                                        if (messageObject3.getGroupIdForUse() != this.currentGroupId) {
                                            break Label_0105;
                                        }
                                    }
                                    ++n2;
                                    --index;
                                }
                            }
                        }
                        else {
                            if (pageBlockArr == null || pageBlockArr.isEmpty()) {
                                n3 = 0;
                                break Label_0483;
                            }
                            final TLRPC.PageBlock pageBlock = pageBlockArr.get(i);
                            final int groupId = pageBlock.groupId;
                            if (groupId != this.currentGroupId) {
                                this.currentGroupId = groupId;
                                o = pageBlock;
                            }
                            else {
                                final int size2 = pageBlockArr.size();
                                int index2 = i;
                                int n4 = 0;
                                while (index2 < size2 && pageBlockArr.get(index2).groupId == this.currentGroupId) {
                                    ++n4;
                                    ++index2;
                                }
                                int index3 = i - 1;
                                int n5 = n4;
                                while (true) {
                                    o = pageBlock;
                                    size = n5;
                                    if (index3 < 0) {
                                        break Label_0105;
                                    }
                                    o = pageBlock;
                                    size = n5;
                                    if (pageBlockArr.get(index3).groupId != this.currentGroupId) {
                                        break Label_0105;
                                    }
                                    ++n5;
                                    --index3;
                                }
                            }
                        }
                        n3 = 1;
                    }
                    n6 = 0;
                    break Label_0486;
                }
                o = imagesArrLocations.get(i);
                size = imagesArrLocations.size();
            }
            n3 = 0;
            n6 = size;
        }
        if (o == null) {
            return;
        }
        int n7 = n3;
        if (n3 == 0) {
            if (n6 == this.currentPhotos.size() && this.currentObjects.indexOf(o) != -1) {
                final int index4 = this.currentObjects.indexOf(o);
                final int currentImage = this.currentImage;
                n7 = n3;
                if (currentImage != index4) {
                    n7 = n3;
                    if (index4 != -1) {
                        if (this.animateAllLine) {
                            this.animateToItem = index4;
                            this.nextImage = index4;
                            this.animateToDX = (currentImage - index4) * (this.itemWidth + this.itemSpacing);
                            this.moving = true;
                            this.animateAllLine = false;
                            this.lastUpdateTime = System.currentTimeMillis();
                            this.invalidate();
                        }
                        else {
                            this.fillImages(true, (currentImage - index4) * (this.itemWidth + this.itemSpacing));
                            this.currentImage = index4;
                            this.moving = false;
                        }
                        this.drawDx = 0;
                        n7 = n3;
                    }
                }
            }
            else {
                n7 = 1;
            }
        }
        if (n7 != 0) {
            this.animateAllLine = false;
            this.currentPhotos.clear();
            this.currentObjects.clear();
            if (imagesArrLocations != null && !imagesArrLocations.isEmpty()) {
                this.currentObjects.addAll(imagesArrLocations);
                this.currentPhotos.addAll(imagesArrLocations);
                this.currentImage = i;
                this.animateToItem = -1;
            }
            else if (imagesArr != null && !imagesArr.isEmpty()) {
                if (this.currentGroupId != 0L || slideshowMessageId != 0) {
                    for (int min2 = Math.min(i + 10, imagesArr.size()), k = i; k < min2; ++k) {
                        final MessageObject e = imagesArr.get(k);
                        if (slideshowMessageId == 0 && e.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(e);
                        this.currentPhotos.add(ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(e.photoThumbs, 56, true), e.photoThumbsObject));
                    }
                    this.currentImage = 0;
                    this.animateToItem = -1;
                    final int max2 = Math.max(i - 10, 0);
                    --i;
                    while (i >= max2) {
                        final MessageObject element = imagesArr.get(i);
                        if (slideshowMessageId == 0 && element.getGroupIdForUse() != this.currentGroupId) {
                            break;
                        }
                        this.currentObjects.add(0, element);
                        this.currentPhotos.add(0, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(element.photoThumbs, 56, true), element.photoThumbsObject));
                        ++this.currentImage;
                        --i;
                    }
                }
            }
            else if (pageBlockArr != null && !pageBlockArr.isEmpty() && this.currentGroupId != 0L) {
                for (int size3 = pageBlockArr.size(), l = i; l < size3; ++l) {
                    final TLRPC.PageBlock e2 = pageBlockArr.get(l);
                    if (e2.groupId != this.currentGroupId) {
                        break;
                    }
                    this.currentObjects.add(e2);
                    this.currentPhotos.add(ImageLocation.getForObject(e2.thumb, e2.thumbObject));
                }
                this.currentImage = 0;
                this.animateToItem = -1;
                --i;
                while (i >= 0) {
                    final TLRPC.PageBlock element2 = pageBlockArr.get(i);
                    if (element2.groupId != this.currentGroupId) {
                        break;
                    }
                    this.currentObjects.add(0, element2);
                    this.currentPhotos.add(0, ImageLocation.getForObject(element2.thumb, element2.thumbObject));
                    ++this.currentImage;
                    --i;
                }
            }
            if (this.currentPhotos.size() == 1) {
                this.currentPhotos.clear();
                this.currentObjects.clear();
            }
            this.fillImages(false, 0);
        }
    }
    
    public boolean onDown(final MotionEvent motionEvent) {
        if (!this.scroll.isFinished()) {
            this.scroll.abortAnimation();
        }
        this.animateToItem = -1;
        return true;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.imagesToDraw.isEmpty()) {
            return;
        }
        canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.backgroundPaint);
        final int size = this.imagesToDraw.size();
        final int drawDx = this.drawDx;
        final int n = (int)(this.itemWidth * 2.0f);
        final int dp = AndroidUtilities.dp(8.0f);
        final ImageLocation imageLocation = this.currentPhotos.get(this.currentImage);
        int b = 0;
        Label_0133: {
            if (imageLocation != null) {
                final TLRPC.PhotoSize photoSize = imageLocation.photoSize;
                if (photoSize != null) {
                    b = Math.max(this.itemWidth, (int)(photoSize.w * (this.itemHeight / (float)photoSize.h)));
                    break Label_0133;
                }
            }
            b = this.itemHeight;
        }
        final int min = Math.min(n, b);
        final float n2 = (float)(dp * 2);
        final float currentItemProgress = this.currentItemProgress;
        final int n3 = (int)(n2 * currentItemProgress);
        final int itemWidth = this.itemWidth;
        final int n4 = itemWidth + (int)((min - itemWidth) * currentItemProgress) + n3;
        final int nextImage = this.nextImage;
        int b2 = 0;
        Label_0290: {
            if (nextImage >= 0 && nextImage < this.currentPhotos.size()) {
                final ImageLocation imageLocation2 = this.currentPhotos.get(this.nextImage);
                if (imageLocation2 != null) {
                    final TLRPC.PhotoSize photoSize2 = imageLocation2.photoSize;
                    if (photoSize2 != null) {
                        b2 = Math.max(this.itemWidth, (int)(photoSize2.w * (this.itemHeight / (float)photoSize2.h)));
                        break Label_0290;
                    }
                }
                b2 = this.itemHeight;
            }
            else {
                b2 = this.itemWidth;
            }
        }
        final int min2 = Math.min(n, b2);
        final float nextItemProgress = this.nextItemProgress;
        final int n5 = (int)(n2 * nextItemProgress);
        final float n6 = (float)drawDx;
        final float n7 = (float)((min2 + n5 - this.itemWidth) / 2);
        int n8;
        if (this.nextImage > this.currentImage) {
            n8 = -1;
        }
        else {
            n8 = 1;
        }
        final int n9 = (int)(n6 + n7 * nextItemProgress * n8);
        final int itemWidth2 = this.itemWidth;
        final int n10 = itemWidth2 + (int)((min2 - itemWidth2) * this.nextItemProgress) + n5;
        final int n11 = (this.getMeasuredWidth() - n4) / 2;
        for (int i = 0; i < size; ++i) {
            final ImageReceiver imageReceiver = this.imagesToDraw.get(i);
            final int param = imageReceiver.getParam();
            final int currentImage = this.currentImage;
            if (param == currentImage) {
                imageReceiver.setImageX(n11 + n9 + n3 / 2);
                imageReceiver.setImageWidth(n4 - n3);
            }
            else {
                final int nextImage2 = this.nextImage;
                if (nextImage2 < currentImage) {
                    if (param < currentImage) {
                        if (param <= nextImage2) {
                            final int param2 = imageReceiver.getParam();
                            final int currentImage2 = this.currentImage;
                            final int itemWidth3 = this.itemWidth;
                            final int itemSpacing = this.itemSpacing;
                            imageReceiver.setImageX((param2 - currentImage2 + 1) * (itemWidth3 + itemSpacing) + n11 - (itemSpacing + n10) + n9);
                        }
                        else {
                            imageReceiver.setImageX((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing) + n11 + n9);
                        }
                    }
                    else {
                        imageReceiver.setImageX(n11 + n4 + this.itemSpacing + (imageReceiver.getParam() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing) + n9);
                    }
                }
                else if (param < currentImage) {
                    imageReceiver.setImageX((imageReceiver.getParam() - this.currentImage) * (this.itemWidth + this.itemSpacing) + n11 + n9);
                }
                else if (param <= nextImage2) {
                    imageReceiver.setImageX(n11 + n4 + this.itemSpacing + (imageReceiver.getParam() - this.currentImage - 1) * (this.itemWidth + this.itemSpacing) + n9);
                }
                else {
                    final int itemSpacing2 = this.itemSpacing;
                    final int param3 = imageReceiver.getParam();
                    final int currentImage3 = this.currentImage;
                    final int itemWidth4 = this.itemWidth;
                    final int itemSpacing3 = this.itemSpacing;
                    imageReceiver.setImageX(n11 + n4 + itemSpacing2 + (param3 - currentImage3 - 2) * (itemWidth4 + itemSpacing3) + (itemSpacing3 + n10) + n9);
                }
                if (param == this.nextImage) {
                    imageReceiver.setImageWidth(n10 - n5);
                    imageReceiver.setImageX(imageReceiver.getImageX() + n5 / 2);
                }
                else {
                    imageReceiver.setImageWidth(this.itemWidth);
                }
            }
            imageReceiver.draw(canvas);
        }
        final long currentTimeMillis = System.currentTimeMillis();
        long n12;
        if ((n12 = currentTimeMillis - this.lastUpdateTime) > 17L) {
            n12 = 17L;
        }
        this.lastUpdateTime = currentTimeMillis;
        final int animateToItem = this.animateToItem;
        if (animateToItem >= 0) {
            final float moveLineProgress = this.moveLineProgress;
            if (moveLineProgress > 0.0f) {
                final float n13 = n12 / 200.0f;
                this.moveLineProgress = moveLineProgress - n13;
                if (animateToItem == this.currentImage) {
                    final float currentItemProgress2 = this.currentItemProgress;
                    if (currentItemProgress2 < 1.0f) {
                        this.currentItemProgress = currentItemProgress2 + n13;
                        if (this.currentItemProgress > 1.0f) {
                            this.currentItemProgress = 1.0f;
                        }
                    }
                    final int animateToDXStart = this.animateToDXStart;
                    this.drawDx = animateToDXStart + (int)Math.ceil(this.currentItemProgress * (this.animateToDX - animateToDXStart));
                }
                else {
                    this.nextItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - this.moveLineProgress);
                    if (this.stopedScrolling) {
                        final float currentItemProgress3 = this.currentItemProgress;
                        if (currentItemProgress3 > 0.0f) {
                            this.currentItemProgress = currentItemProgress3 - n13;
                            if (this.currentItemProgress < 0.0f) {
                                this.currentItemProgress = 0.0f;
                            }
                        }
                        final int animateToDXStart2 = this.animateToDXStart;
                        this.drawDx = animateToDXStart2 + (int)Math.ceil(this.nextItemProgress * (this.animateToDX - animateToDXStart2));
                    }
                    else {
                        this.currentItemProgress = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.moveLineProgress);
                        this.drawDx = (int)Math.ceil(this.nextItemProgress * this.animateToDX);
                    }
                }
                if (this.moveLineProgress <= 0.0f) {
                    this.currentImage = this.animateToItem;
                    this.moveLineProgress = 1.0f;
                    this.currentItemProgress = 1.0f;
                    this.nextItemProgress = 0.0f;
                    this.moving = false;
                    this.stopedScrolling = false;
                    this.drawDx = 0;
                    this.animateToItem = -1;
                }
            }
            this.fillImages(true, this.drawDx);
            this.invalidate();
        }
        if (this.scrolling) {
            final float currentItemProgress4 = this.currentItemProgress;
            if (currentItemProgress4 > 0.0f) {
                this.currentItemProgress = currentItemProgress4 - n12 / 200.0f;
                if (this.currentItemProgress < 0.0f) {
                    this.currentItemProgress = 0.0f;
                }
                this.invalidate();
            }
        }
        if (!this.scroll.isFinished()) {
            if (this.scroll.computeScrollOffset()) {
                this.drawDx = this.scroll.getCurrX();
                this.updateAfterScroll();
                this.invalidate();
            }
            if (this.scroll.isFinished()) {
                this.stopScrolling();
            }
        }
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float a, final float n) {
        this.scroll.abortAnimation();
        if (this.currentPhotos.size() >= 10) {
            this.scroll.fling(this.drawDx, 0, Math.round(a), 0, this.getMinScrollX(), this.getMaxScrollX(), 0, 0);
        }
        return false;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.fillImages(false, 0);
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        this.drawDx -= (int)n;
        final int minScrollX = this.getMinScrollX();
        final int maxScrollX = this.getMaxScrollX();
        final int drawDx = this.drawDx;
        if (drawDx < minScrollX) {
            this.drawDx = minScrollX;
        }
        else if (drawDx > maxScrollX) {
            this.drawDx = maxScrollX;
        }
        this.updateAfterScroll();
        return false;
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        final int currentIndex = this.delegate.getCurrentIndex();
        final ArrayList<ImageLocation> imagesArrLocations = this.delegate.getImagesArrLocations();
        final ArrayList<MessageObject> imagesArr = this.delegate.getImagesArr();
        final ArrayList<TLRPC.PageBlock> pageBlockArr = this.delegate.getPageBlockArr();
        this.stopScrolling();
        final int size = this.imagesToDraw.size();
        int i = 0;
        while (i < size) {
            final ImageReceiver imageReceiver = this.imagesToDraw.get(i);
            if (imageReceiver.isInsideImage(motionEvent.getX(), motionEvent.getY())) {
                final int param = imageReceiver.getParam();
                if (param < 0 || param >= this.currentObjects.size()) {
                    return true;
                }
                if (imagesArr != null && !imagesArr.isEmpty()) {
                    final int index = imagesArr.indexOf(this.currentObjects.get(param));
                    if (currentIndex == index) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(index);
                    break;
                }
                else if (pageBlockArr != null && !pageBlockArr.isEmpty()) {
                    final int index2 = pageBlockArr.indexOf(this.currentObjects.get(param));
                    if (currentIndex == index2) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(index2);
                    break;
                }
                else {
                    if (imagesArrLocations == null || imagesArrLocations.isEmpty()) {
                        break;
                    }
                    final int index3 = imagesArrLocations.indexOf(this.currentObjects.get(param));
                    if (currentIndex == index3) {
                        return true;
                    }
                    this.moveLineProgress = 1.0f;
                    this.animateAllLine = true;
                    this.delegate.setCurrentIndex(index3);
                    break;
                }
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean empty = this.currentPhotos.isEmpty();
        final boolean b = false;
        boolean b2 = false;
        boolean b3 = b;
        if (!empty) {
            if (this.getAlpha() != 1.0f) {
                b3 = b;
            }
            else {
                if (this.gestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent)) {
                    b2 = true;
                }
                b3 = b2;
                if (this.scrolling) {
                    b3 = b2;
                    if (motionEvent.getAction() == 1) {
                        b3 = b2;
                        if (this.scroll.isFinished()) {
                            this.stopScrolling();
                            b3 = b2;
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    public void setDelegate(final GroupedPhotosListViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMoveProgress(final float a) {
        if (!this.scrolling) {
            if (this.animateToItem < 0) {
                if (a > 0.0f) {
                    this.nextImage = this.currentImage - 1;
                }
                else {
                    this.nextImage = this.currentImage + 1;
                }
                final int nextImage = this.nextImage;
                if (nextImage >= 0 && nextImage < this.currentPhotos.size()) {
                    this.currentItemProgress = 1.0f - Math.abs(a);
                }
                else {
                    this.currentItemProgress = 1.0f;
                }
                this.nextItemProgress = 1.0f - this.currentItemProgress;
                this.moving = (a != 0.0f);
                this.invalidate();
                if (!this.currentPhotos.isEmpty() && (a >= 0.0f || this.currentImage != this.currentPhotos.size() - 1)) {
                    if (a <= 0.0f || this.currentImage != 0) {
                        this.fillImages(true, this.drawDx = (int)(a * (this.itemWidth + this.itemSpacing)));
                    }
                }
            }
        }
    }
    
    public interface GroupedPhotosListViewDelegate
    {
        int getAvatarsDialogId();
        
        int getCurrentAccount();
        
        int getCurrentIndex();
        
        ArrayList<MessageObject> getImagesArr();
        
        ArrayList<ImageLocation> getImagesArrLocations();
        
        ArrayList<TLRPC.PageBlock> getPageBlockArr();
        
        Object getParentObject();
        
        int getSlideshowMessageId();
        
        void setCurrentIndex(final int p0);
    }
}
