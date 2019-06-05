package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import org.mapsforge.core.model.Tile;

class LabelPlacement {
    private static final int LABEL_DISTANCE_TO_LABEL = 2;
    private static final int LABEL_DISTANCE_TO_SYMBOL = 2;
    private static final int PLACEMENT_MODEL = 1;
    private static final int START_DISTANCE_TO_SYMBOLS = 4;
    private static final int SYMBOL_DISTANCE_TO_SYMBOL = 2;
    final DependencyCache dependencyCache = new DependencyCache();
    PointTextContainer label;
    Rect rect1;
    Rect rect2;
    ReferencePosition referencePosition;
    SymbolContainer symbolContainer;

    static class ReferencePosition {
        final float height;
        final int nodeNumber;
        SymbolContainer symbol;
        final float width;
        /* renamed from: x */
        final double f64x;
        /* renamed from: y */
        final double f65y;

        ReferencePosition(double x, double y, int nodeNumber, float width, float height, SymbolContainer symbol) {
            this.f64x = x;
            this.f65y = y;
            this.nodeNumber = nodeNumber;
            this.width = width;
            this.height = height;
            this.symbol = symbol;
        }
    }

    static final class ReferencePositionHeightComparator implements Comparator<ReferencePosition>, Serializable {
        static final ReferencePositionHeightComparator INSTANCE = new ReferencePositionHeightComparator();
        private static final long serialVersionUID = 1;

        private ReferencePositionHeightComparator() {
        }

        public int compare(ReferencePosition x, ReferencePosition y) {
            if (x.f65y - ((double) x.height) < y.f65y - ((double) y.height)) {
                return -1;
            }
            if (x.f65y - ((double) x.height) > y.f65y - ((double) y.height)) {
                return 1;
            }
            return 0;
        }
    }

    static final class ReferencePositionWidthComparator implements Comparator<ReferencePosition>, Serializable {
        static final ReferencePositionWidthComparator INSTANCE = new ReferencePositionWidthComparator();
        private static final long serialVersionUID = 1;

        private ReferencePositionWidthComparator() {
        }

        public int compare(ReferencePosition x, ReferencePosition y) {
            if (x.f64x + ((double) x.width) < y.f64x + ((double) y.width)) {
                return -1;
            }
            if (x.f64x + ((double) x.width) > y.f64x + ((double) y.width)) {
                return 1;
            }
            return 0;
        }
    }

    static final class ReferencePositionXComparator implements Comparator<ReferencePosition>, Serializable {
        static final ReferencePositionXComparator INSTANCE = new ReferencePositionXComparator();
        private static final long serialVersionUID = 1;

        private ReferencePositionXComparator() {
        }

        public int compare(ReferencePosition x, ReferencePosition y) {
            if (x.f64x < y.f64x) {
                return -1;
            }
            if (x.f64x > y.f64x) {
                return 1;
            }
            return 0;
        }
    }

    static final class ReferencePositionYComparator implements Comparator<ReferencePosition>, Serializable {
        static final ReferencePositionYComparator INSTANCE = new ReferencePositionYComparator();
        private static final long serialVersionUID = 1;

        private ReferencePositionYComparator() {
        }

        public int compare(ReferencePosition x, ReferencePosition y) {
            if (x.f65y < y.f65y) {
                return -1;
            }
            if (x.f65y > y.f65y) {
                return 1;
            }
            return 0;
        }
    }

    LabelPlacement() {
    }

    private void centerLabels(List<PointTextContainer> labels) {
        for (int i = 0; i < labels.size(); i++) {
            this.label = (PointTextContainer) labels.get(i);
            this.label.f66x -= (double) (this.label.boundary.width() / 2);
        }
    }

    private void preprocessAreaLabels(List<PointTextContainer> areaLabels) {
        centerLabels(areaLabels);
        removeOutOfTileAreaLabels(areaLabels);
        removeOverlappingAreaLabels(areaLabels);
        if (!areaLabels.isEmpty()) {
            this.dependencyCache.removeAreaLabelsInAlreadyDrawnAreas(areaLabels);
        }
    }

    private void preprocessLabels(List<PointTextContainer> labels) {
        removeOutOfTileLabels(labels);
    }

    private void preprocessSymbols(List<SymbolContainer> symbols) {
        removeOutOfTileSymbols(symbols);
        removeOverlappingSymbols(symbols);
        this.dependencyCache.removeSymbolsFromDrawnAreas(symbols);
    }

    private List<PointTextContainer> processFourPointGreedy(List<PointTextContainer> labels, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels) {
        int i;
        List<PointTextContainer> resolutionSet = new ArrayList();
        ReferencePosition[] refPos = new ReferencePosition[(labels.size() * 4)];
        PriorityQueue<ReferencePosition> priorityQueue = new PriorityQueue(((labels.size() * 4) * 2) + ((labels.size() / 10) * 2), ReferencePositionYComparator.INSTANCE);
        priorityQueue = new PriorityQueue(((labels.size() * 4) * 2) + ((labels.size() / 10) * 2), ReferencePositionHeightComparator.INSTANCE);
        for (int z = 0; z < labels.size(); z++) {
            if (labels.get(z) != null) {
                if (((PointTextContainer) labels.get(z)).symbol != null) {
                    PointTextContainer tmp = (PointTextContainer) labels.get(z);
                    refPos[z * 4] = new ReferencePosition(tmp.f66x - ((double) (tmp.boundary.width() / 2)), (tmp.f67y - ((double) (tmp.symbol.symbol.getHeight() / 2))) - ((double) 4), z, (float) tmp.boundary.width(), (float) tmp.boundary.height(), tmp.symbol);
                    refPos[(z * 4) + 1] = new ReferencePosition(tmp.f66x - ((double) (tmp.boundary.width() / 2)), ((tmp.f67y + ((double) (tmp.symbol.symbol.getHeight() / 2))) + ((double) tmp.boundary.height())) + ((double) 4), z, (float) tmp.boundary.width(), (float) tmp.boundary.height(), tmp.symbol);
                    refPos[(z * 4) + 2] = new ReferencePosition(((tmp.f66x - ((double) (tmp.symbol.symbol.getWidth() / 2))) - ((double) tmp.boundary.width())) - ((double) 4), tmp.f67y + ((double) (tmp.boundary.height() / 2)), z, (float) tmp.boundary.width(), (float) tmp.boundary.height(), tmp.symbol);
                    refPos[(z * 4) + 3] = new ReferencePosition((tmp.f66x + ((double) (tmp.symbol.symbol.getWidth() / 2))) + ((double) 4), (tmp.f67y + ((double) (tmp.boundary.height() / 2))) - 0.10000000149011612d, z, (float) tmp.boundary.width(), (float) tmp.boundary.height(), tmp.symbol);
                } else {
                    refPos[z * 4] = new ReferencePosition(((PointTextContainer) labels.get(z)).f66x - ((double) (((PointTextContainer) labels.get(z)).boundary.width() / 2)), ((PointTextContainer) labels.get(z)).f67y, z, (float) ((PointTextContainer) labels.get(z)).boundary.width(), (float) ((PointTextContainer) labels.get(z)).boundary.height(), null);
                    refPos[(z * 4) + 1] = null;
                    refPos[(z * 4) + 2] = null;
                    refPos[(z * 4) + 3] = null;
                }
            }
        }
        removeNonValidateReferencePosition(refPos, symbols, areaLabels);
        for (ReferencePosition referencePosition : refPos) {
            this.referencePosition = referencePosition;
            if (this.referencePosition != null) {
                priorityQueue.add(this.referencePosition);
                priorityQueue.add(this.referencePosition);
            }
        }
        while (priorityQueue.size() != 0) {
            this.referencePosition = (ReferencePosition) priorityQueue.remove();
            this.label = (PointTextContainer) labels.get(this.referencePosition.nodeNumber);
            resolutionSet.add(new PointTextContainer(this.label.text, this.referencePosition.f64x, this.referencePosition.f65y, this.label.paintFront, this.label.paintBack, this.label.symbol));
            if (priorityQueue.size() == 0) {
                break;
            }
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 0]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 1]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 2]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 3]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 0]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 1]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 2]);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 4) + 3]);
            LinkedList<ReferencePosition> linkedRef = new LinkedList();
            while (priorityQueue.size() != 0 && ((ReferencePosition) priorityQueue.peek()).f64x < this.referencePosition.f64x + ((double) this.referencePosition.width)) {
                linkedRef.add(priorityQueue.remove());
            }
            i = 0;
            while (i < linkedRef.size()) {
                if (((ReferencePosition) linkedRef.get(i)).f64x <= this.referencePosition.f64x + ((double) this.referencePosition.width) && ((ReferencePosition) linkedRef.get(i)).f65y >= this.referencePosition.f65y - ((double) ((ReferencePosition) linkedRef.get(i)).height) && ((ReferencePosition) linkedRef.get(i)).f65y <= this.referencePosition.f65y + ((double) ((ReferencePosition) linkedRef.get(i)).height)) {
                    priorityQueue.remove(linkedRef.get(i));
                    linkedRef.remove(i);
                    i--;
                }
                i++;
            }
            priorityQueue.addAll(linkedRef);
        }
        return resolutionSet;
    }

    private List<PointTextContainer> processTwoPointGreedy(List<PointTextContainer> labels, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels) {
        int i;
        List<PointTextContainer> resolutionSet = new ArrayList();
        ReferencePosition[] refPos = new ReferencePosition[(labels.size() * 2)];
        PriorityQueue<ReferencePosition> priorityQueue = new PriorityQueue((labels.size() * 2) + ((labels.size() / 10) * 2), ReferencePositionWidthComparator.INSTANCE);
        priorityQueue = new PriorityQueue((labels.size() * 2) + ((labels.size() / 10) * 2), ReferencePositionXComparator.INSTANCE);
        for (int z = 0; z < labels.size(); z++) {
            this.label = (PointTextContainer) labels.get(z);
            if (this.label.symbol != null) {
                refPos[z * 2] = new ReferencePosition((this.label.f66x - ((double) (this.label.boundary.width() / 2))) - 0.10000000149011612d, (this.label.f67y - ((double) this.label.boundary.height())) - 4.0d, z, (float) this.label.boundary.width(), (float) this.label.boundary.height(), this.label.symbol);
                refPos[(z * 2) + 1] = new ReferencePosition(this.label.f66x - ((double) (this.label.boundary.width() / 2)), (this.label.f67y + ((double) this.label.symbol.symbol.getHeight())) + 4.0d, z, (float) this.label.boundary.width(), (float) this.label.boundary.height(), this.label.symbol);
            } else {
                refPos[z * 2] = new ReferencePosition((this.label.f66x - ((double) (this.label.boundary.width() / 2))) - 0.10000000149011612d, this.label.f67y, z, (float) this.label.boundary.width(), (float) this.label.boundary.height(), null);
                refPos[(z * 2) + 1] = null;
            }
        }
        removeNonValidateReferencePosition(refPos, symbols, areaLabels);
        for (ReferencePosition referencePosition : refPos) {
            this.referencePosition = referencePosition;
            if (this.referencePosition != null) {
                priorityQueue.add(this.referencePosition);
                priorityQueue.add(this.referencePosition);
            }
        }
        while (priorityQueue.size() != 0) {
            this.referencePosition = (ReferencePosition) priorityQueue.remove();
            this.label = (PointTextContainer) labels.get(this.referencePosition.nodeNumber);
            resolutionSet.add(new PointTextContainer(this.label.text, this.referencePosition.f64x, this.referencePosition.f65y, this.label.paintFront, this.label.paintBack, this.referencePosition.symbol));
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 2) + 1]);
            if (priorityQueue.size() == 0) {
                break;
            }
            priorityQueue.remove(this.referencePosition);
            priorityQueue.remove(refPos[(this.referencePosition.nodeNumber * 2) + 1]);
            LinkedList<ReferencePosition> linkedRef = new LinkedList();
            while (priorityQueue.size() != 0 && ((ReferencePosition) priorityQueue.peek()).f64x < this.referencePosition.f64x + ((double) this.referencePosition.width)) {
                linkedRef.add(priorityQueue.remove());
            }
            i = 0;
            while (i < linkedRef.size()) {
                if (((ReferencePosition) linkedRef.get(i)).f64x <= this.referencePosition.f64x + ((double) this.referencePosition.width) && ((ReferencePosition) linkedRef.get(i)).f65y >= this.referencePosition.f65y - ((double) ((ReferencePosition) linkedRef.get(i)).height) && ((ReferencePosition) linkedRef.get(i)).f65y <= this.referencePosition.f65y + ((double) ((ReferencePosition) linkedRef.get(i)).height)) {
                    priorityQueue.remove(linkedRef.get(i));
                    linkedRef.remove(i);
                    i--;
                }
                i++;
            }
            priorityQueue.addAll(linkedRef);
        }
        return resolutionSet;
    }

    private void removeEmptySymbolReferences(List<PointTextContainer> nodes, List<SymbolContainer> symbols) {
        for (int i = 0; i < nodes.size(); i++) {
            this.label = (PointTextContainer) nodes.get(i);
            if (!symbols.contains(this.label.symbol)) {
                this.label.symbol = null;
            }
        }
    }

    private void removeNonValidateReferencePosition(ReferencePosition[] refPos, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels) {
        int y;
        for (int i = 0; i < symbols.size(); i++) {
            this.symbolContainer = (SymbolContainer) symbols.get(i);
            this.rect1 = new Rect(((int) this.symbolContainer.point.f68x) - 2, ((int) this.symbolContainer.point.f69y) - 2, (((int) this.symbolContainer.point.f68x) + this.symbolContainer.symbol.getWidth()) + 2, (((int) this.symbolContainer.point.f69y) + this.symbolContainer.symbol.getHeight()) + 2);
            for (y = 0; y < refPos.length; y++) {
                if (refPos[y] != null) {
                    this.rect2 = new Rect((int) refPos[y].f64x, (int) (refPos[y].f65y - ((double) refPos[y].height)), (int) (refPos[y].f64x + ((double) refPos[y].width)), (int) refPos[y].f65y);
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        refPos[y] = null;
                    }
                }
            }
        }
        for (PointTextContainer areaLabel : areaLabels) {
            this.rect1 = new Rect(((int) areaLabel.f66x) - 2, (((int) areaLabel.f67y) - areaLabel.boundary.height()) - 2, (((int) areaLabel.f66x) + areaLabel.boundary.width()) + 2, ((int) areaLabel.f67y) + 2);
            for (y = 0; y < refPos.length; y++) {
                if (refPos[y] != null) {
                    this.rect2 = new Rect((int) refPos[y].f64x, (int) (refPos[y].f65y - ((double) refPos[y].height)), (int) (refPos[y].f64x + ((double) refPos[y].width)), (int) refPos[y].f65y);
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        refPos[y] = null;
                    }
                }
            }
        }
        this.dependencyCache.removeReferencePointsFromDependencyCache(refPos);
    }

    private void removeOutOfTileAreaLabels(List<PointTextContainer> areaLabels) {
        int i = 0;
        while (i < areaLabels.size()) {
            this.label = (PointTextContainer) areaLabels.get(i);
            if (this.label.f66x > 256.0d) {
                areaLabels.remove(i);
                i--;
            } else if (this.label.f67y - ((double) this.label.boundary.height()) > 256.0d) {
                areaLabels.remove(i);
                i--;
            } else if (this.label.f66x + ((double) this.label.boundary.width()) < 0.0d) {
                areaLabels.remove(i);
                i--;
            } else if (this.label.f67y + ((double) this.label.boundary.height()) < 0.0d) {
                areaLabels.remove(i);
                i--;
            }
            i++;
        }
    }

    private void removeOutOfTileLabels(List<PointTextContainer> labels) {
        int i = 0;
        while (i < labels.size()) {
            this.label = (PointTextContainer) labels.get(i);
            if (this.label.f66x - ((double) (this.label.boundary.width() / 2)) > 256.0d) {
                labels.remove(i);
                this.label = null;
            } else if (this.label.f67y - ((double) this.label.boundary.height()) > 256.0d) {
                labels.remove(i);
                this.label = null;
            } else if ((this.label.f66x - ((double) (this.label.boundary.width() / 2))) + ((double) this.label.boundary.width()) < 0.0d) {
                labels.remove(i);
                this.label = null;
            } else if (this.label.f67y < 0.0d) {
                labels.remove(i);
                this.label = null;
            } else {
                i++;
            }
        }
    }

    private void removeOutOfTileSymbols(List<SymbolContainer> symbols) {
        int i = 0;
        while (i < symbols.size()) {
            this.symbolContainer = (SymbolContainer) symbols.get(i);
            if (this.symbolContainer.point.f68x > 256.0d) {
                symbols.remove(i);
            } else if (this.symbolContainer.point.f69y > 256.0d) {
                symbols.remove(i);
            } else if (this.symbolContainer.point.f68x + ((double) this.symbolContainer.symbol.getWidth()) < 0.0d) {
                symbols.remove(i);
            } else if (this.symbolContainer.point.f69y + ((double) this.symbolContainer.symbol.getHeight()) < 0.0d) {
                symbols.remove(i);
            } else {
                i++;
            }
        }
    }

    private void removeOverlappingAreaLabels(List<PointTextContainer> areaLabels) {
        for (int x = 0; x < areaLabels.size(); x++) {
            this.label = (PointTextContainer) areaLabels.get(x);
            this.rect1 = new Rect(((int) this.label.f66x) - 2, ((int) this.label.f67y) - 2, ((int) (this.label.f66x + ((double) this.label.boundary.width()))) + 2, (int) ((this.label.f67y + ((double) this.label.boundary.height())) + ((double) 2)));
            int y = x + 1;
            while (y < areaLabels.size()) {
                if (y != x) {
                    this.label = (PointTextContainer) areaLabels.get(y);
                    this.rect2 = new Rect((int) this.label.f66x, (int) this.label.f67y, (int) (this.label.f66x + ((double) this.label.boundary.width())), (int) (this.label.f67y + ((double) this.label.boundary.height())));
                    if (Rect.intersects(this.rect1, this.rect2)) {
                        areaLabels.remove(y);
                        y--;
                    }
                }
                y++;
            }
        }
    }

    private void removeOverlappingSymbolsWithAreaLabels(List<SymbolContainer> symbols, List<PointTextContainer> pTC) {
        for (int x = 0; x < pTC.size(); x++) {
            this.label = (PointTextContainer) pTC.get(x);
            this.rect1 = new Rect(((int) this.label.f66x) - 2, ((int) (this.label.f67y - ((double) this.label.boundary.height()))) - 2, (int) ((this.label.f66x + ((double) this.label.boundary.width())) + ((double) 2)), (int) (this.label.f67y + ((double) 2)));
            int y = 0;
            while (y < symbols.size()) {
                this.symbolContainer = (SymbolContainer) symbols.get(y);
                this.rect2 = new Rect((int) this.symbolContainer.point.f68x, (int) this.symbolContainer.point.f69y, (int) (this.symbolContainer.point.f68x + ((double) this.symbolContainer.symbol.getWidth())), (int) (this.symbolContainer.point.f69y + ((double) this.symbolContainer.symbol.getHeight())));
                if (Rect.intersects(this.rect1, this.rect2)) {
                    symbols.remove(y);
                    y--;
                }
                y++;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public List<PointTextContainer> placeLabels(List<PointTextContainer> labels, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels, Tile cT) {
        List<PointTextContainer> returnLabels = labels;
        this.dependencyCache.generateTileAndDependencyOnTile(cT);
        preprocessAreaLabels(areaLabels);
        preprocessLabels(returnLabels);
        preprocessSymbols(symbols);
        removeEmptySymbolReferences(returnLabels, symbols);
        removeOverlappingSymbolsWithAreaLabels(symbols, areaLabels);
        this.dependencyCache.removeOverlappingObjectsWithDependencyOnTile(returnLabels, areaLabels, symbols);
        if (!returnLabels.isEmpty()) {
            switch (1) {
                case 0:
                    returnLabels = processTwoPointGreedy(returnLabels, symbols, areaLabels);
                    break;
                case 1:
                    returnLabels = processFourPointGreedy(returnLabels, symbols, areaLabels);
                    break;
            }
        }
        this.dependencyCache.fillDependencyOnTile(returnLabels, symbols, areaLabels);
        return returnLabels;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeOverlappingSymbols(List<SymbolContainer> symbols) {
        for (int x = 0; x < symbols.size(); x++) {
            this.symbolContainer = (SymbolContainer) symbols.get(x);
            this.rect1 = new Rect(((int) this.symbolContainer.point.f68x) - 2, ((int) this.symbolContainer.point.f69y) - 2, (((int) this.symbolContainer.point.f68x) + this.symbolContainer.symbol.getWidth()) + 2, (((int) this.symbolContainer.point.f69y) + this.symbolContainer.symbol.getHeight()) + 2);
            int y = x + 1;
            while (y < symbols.size()) {
                if (y != x) {
                    this.symbolContainer = (SymbolContainer) symbols.get(y);
                    this.rect2 = new Rect((int) this.symbolContainer.point.f68x, (int) this.symbolContainer.point.f69y, ((int) this.symbolContainer.point.f68x) + this.symbolContainer.symbol.getWidth(), ((int) this.symbolContainer.point.f69y) + this.symbolContainer.symbol.getHeight());
                    if (Rect.intersects(this.rect2, this.rect1)) {
                        symbols.remove(y);
                        y--;
                    }
                }
                y++;
            }
        }
    }
}
