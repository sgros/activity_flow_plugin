package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

class DependencyCache {
    private DependencyOnTile currentDependencyOnTile;
    private Tile currentTile;
    Dependency<DependencyText> depLabel;
    final Map<Tile, DependencyOnTile> dependencyTable = new Hashtable(60);
    Rect rect1;
    Rect rect2;
    SymbolContainer smb;
    DependencyOnTile tmp;

    private static class Dependency<Type> {
        final Point point;
        final Type value;

        Dependency(Type value, Point point) {
            this.value = value;
            this.point = point;
        }
    }

    private static class DependencyOnTile {
        boolean drawn = false;
        List<Dependency<DependencyText>> labels = null;
        List<Dependency<DependencySymbol>> symbols = null;

        DependencyOnTile() {
        }

        /* Access modifiers changed, original: 0000 */
        public void addSymbol(Dependency<DependencySymbol> toAdd) {
            if (this.symbols == null) {
                this.symbols = new ArrayList();
            }
            this.symbols.add(toAdd);
        }

        /* Access modifiers changed, original: 0000 */
        public void addText(Dependency<DependencyText> toAdd) {
            if (this.labels == null) {
                this.labels = new ArrayList();
            }
            this.labels.add(toAdd);
        }
    }

    private static class DependencySymbol {
        final Bitmap symbol;
        private final List<Tile> tiles = new LinkedList();

        DependencySymbol(Bitmap symbol, Tile tile) {
            this.symbol = symbol;
            this.tiles.add(tile);
        }

        /* Access modifiers changed, original: 0000 */
        public void addTile(Tile tile) {
            this.tiles.add(tile);
        }
    }

    private static class DependencyText {
        final Rect boundary;
        final Paint paintBack;
        final Paint paintFront;
        final String text;
        final List<Tile> tiles = new LinkedList();

        DependencyText(Paint paintFront, Paint paintBack, String text, Rect boundary, Tile tile) {
            this.paintFront = paintFront;
            this.paintBack = paintBack;
            this.text = text;
            this.tiles.add(tile);
            this.boundary = boundary;
        }

        /* Access modifiers changed, original: 0000 */
        public void addTile(Tile tile) {
            this.tiles.add(tile);
        }
    }

    DependencyCache() {
    }

    private void addLabelsFromDependencyOnTile(List<PointTextContainer> labels) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); i++) {
            this.depLabel = (Dependency) this.currentDependencyOnTile.labels.get(i);
            if (((DependencyText) this.depLabel.value).paintBack != null) {
                labels.add(new PointTextContainer(((DependencyText) this.depLabel.value).text, this.depLabel.point.f68x, this.depLabel.point.f69y, ((DependencyText) this.depLabel.value).paintFront, ((DependencyText) this.depLabel.value).paintBack));
            } else {
                labels.add(new PointTextContainer(((DependencyText) this.depLabel.value).text, this.depLabel.point.f68x, this.depLabel.point.f69y, ((DependencyText) this.depLabel.value).paintFront));
            }
        }
    }

    private void addSymbolsFromDependencyOnTile(List<SymbolContainer> symbols) {
        for (Dependency<DependencySymbol> depSmb : this.currentDependencyOnTile.symbols) {
            symbols.add(new SymbolContainer(((DependencySymbol) depSmb.value).symbol, depSmb.point));
        }
    }

    private void fillDependencyLabels(List<PointTextContainer> pTC) {
        Tile left = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile right = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile up = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile down = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        Tile leftup = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile leftdown = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        Tile rightup = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile rightdown = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        for (int i = 0; i < pTC.size(); i++) {
            DependencyOnTile linkedDep;
            PointTextContainer label = (PointTextContainer) pTC.get(i);
            DependencyText toAdd = null;
            if (label.f67y - ((double) label.boundary.height()) < 0.0d && !((DependencyOnTile) this.dependencyTable.get(up)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(up);
                toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                linkedDep.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y + 256.0d)));
                toAdd.addTile(up);
                if (label.f66x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftup)).drawn) {
                    ((DependencyOnTile) this.dependencyTable.get(leftup)).addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y + 256.0d)));
                    toAdd.addTile(leftup);
                }
                if (label.f66x + ((double) label.boundary.width()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightup)).drawn) {
                    ((DependencyOnTile) this.dependencyTable.get(rightup)).addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y + 256.0d)));
                    toAdd.addTile(rightup);
                }
            }
            if (label.f67y > 256.0d && !((DependencyOnTile) this.dependencyTable.get(down)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(down);
                if (toAdd == null) {
                    toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                    this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                }
                linkedDep.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y - 256.0d)));
                toAdd.addTile(down);
                if (label.f66x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftdown)).drawn) {
                    ((DependencyOnTile) this.dependencyTable.get(leftdown)).addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y - 256.0d)));
                    toAdd.addTile(leftdown);
                }
                if (label.f66x + ((double) label.boundary.width()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightdown)).drawn) {
                    ((DependencyOnTile) this.dependencyTable.get(rightdown)).addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y - 256.0d)));
                    toAdd.addTile(rightdown);
                }
            }
            if (label.f66x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(left)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(left);
                if (toAdd == null) {
                    toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                    this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                }
                linkedDep.addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y)));
                toAdd.addTile(left);
            }
            if (label.f66x + ((double) label.boundary.width()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(right)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(right);
                if (toAdd == null) {
                    toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                    this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                }
                linkedDep.addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y)));
                toAdd.addTile(right);
            }
            if (label.symbol != null && toAdd == null) {
                if (label.symbol.point.f69y <= 0.0d && !((DependencyOnTile) this.dependencyTable.get(up)).drawn) {
                    linkedDep = (DependencyOnTile) this.dependencyTable.get(up);
                    toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                    this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                    linkedDep.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y + 256.0d)));
                    toAdd.addTile(up);
                    if (label.symbol.point.f68x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftup)).drawn) {
                        ((DependencyOnTile) this.dependencyTable.get(leftup)).addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y + 256.0d)));
                        toAdd.addTile(leftup);
                    }
                    if (label.symbol.point.f68x + ((double) label.symbol.symbol.getWidth()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightup)).drawn) {
                        ((DependencyOnTile) this.dependencyTable.get(rightup)).addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y + 256.0d)));
                        toAdd.addTile(rightup);
                    }
                }
                if (label.symbol.point.f69y + ((double) label.symbol.symbol.getHeight()) >= 256.0d && !((DependencyOnTile) this.dependencyTable.get(down)).drawn) {
                    linkedDep = (DependencyOnTile) this.dependencyTable.get(down);
                    if (toAdd == null) {
                        toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                    }
                    linkedDep.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y + 256.0d)));
                    toAdd.addTile(up);
                    if (label.symbol.point.f68x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftdown)).drawn) {
                        ((DependencyOnTile) this.dependencyTable.get(leftdown)).addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y - 256.0d)));
                        toAdd.addTile(leftdown);
                    }
                    if (label.symbol.point.f68x + ((double) label.symbol.symbol.getWidth()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightdown)).drawn) {
                        ((DependencyOnTile) this.dependencyTable.get(rightdown)).addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y - 256.0d)));
                        toAdd.addTile(rightdown);
                    }
                }
                if (label.symbol.point.f68x <= 0.0d && !((DependencyOnTile) this.dependencyTable.get(left)).drawn) {
                    linkedDep = (DependencyOnTile) this.dependencyTable.get(left);
                    if (toAdd == null) {
                        toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                    }
                    linkedDep.addText(new Dependency(toAdd, new Point(label.f66x - 256.0d, label.f67y)));
                    toAdd.addTile(left);
                }
                if (label.symbol.point.f68x + ((double) label.symbol.symbol.getWidth()) >= 256.0d && !((DependencyOnTile) this.dependencyTable.get(right)).drawn) {
                    linkedDep = (DependencyOnTile) this.dependencyTable.get(right);
                    if (toAdd == null) {
                        toAdd = new DependencyText(label.paintFront, label.paintBack, label.text, label.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText(new Dependency(toAdd, new Point(label.f66x, label.f67y)));
                    }
                    linkedDep.addText(new Dependency(toAdd, new Point(label.f66x + 256.0d, label.f67y)));
                    toAdd.addTile(right);
                }
            }
        }
    }

    private void fillDependencyOnTile2(List<PointTextContainer> labels, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels) {
        Tile left = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile right = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile up = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile down = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        Tile leftup = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile leftdown = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        Tile rightup = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile rightdown = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        if (this.dependencyTable.get(up) == null) {
            this.dependencyTable.put(up, new DependencyOnTile());
        }
        if (this.dependencyTable.get(down) == null) {
            this.dependencyTable.put(down, new DependencyOnTile());
        }
        if (this.dependencyTable.get(left) == null) {
            this.dependencyTable.put(left, new DependencyOnTile());
        }
        if (this.dependencyTable.get(right) == null) {
            this.dependencyTable.put(right, new DependencyOnTile());
        }
        if (this.dependencyTable.get(leftdown) == null) {
            this.dependencyTable.put(leftdown, new DependencyOnTile());
        }
        if (this.dependencyTable.get(rightup) == null) {
            this.dependencyTable.put(rightup, new DependencyOnTile());
        }
        if (this.dependencyTable.get(leftup) == null) {
            this.dependencyTable.put(leftup, new DependencyOnTile());
        }
        if (this.dependencyTable.get(rightdown) == null) {
            this.dependencyTable.put(rightdown, new DependencyOnTile());
        }
        fillDependencyLabels(labels);
        fillDependencyLabels(areaLabels);
        for (SymbolContainer symbol : symbols) {
            DependencyOnTile linkedDep;
            DependencyOnTile dependencyOnTile;
            DependencySymbol addSmb = null;
            if (symbol.point.f69y < 0.0d && !((DependencyOnTile) this.dependencyTable.get(up)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(up);
                addSmb = new DependencySymbol(symbol.symbol, this.currentTile);
                this.currentDependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y)));
                dependencyOnTile = linkedDep;
                dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y + 256.0d)));
                addSmb.addTile(up);
                if (symbol.point.f68x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftup)).drawn) {
                    dependencyOnTile = (DependencyOnTile) this.dependencyTable.get(leftup);
                    dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x + 256.0d, symbol.point.f69y + 256.0d)));
                    addSmb.addTile(leftup);
                }
                if (symbol.point.f68x + ((double) symbol.symbol.getWidth()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightup)).drawn) {
                    dependencyOnTile = (DependencyOnTile) this.dependencyTable.get(rightup);
                    dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x - 256.0d, symbol.point.f69y + 256.0d)));
                    addSmb.addTile(rightup);
                }
            }
            if (symbol.point.f69y + ((double) symbol.symbol.getHeight()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(down)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(down);
                if (addSmb == null) {
                    addSmb = new DependencySymbol(symbol.symbol, this.currentTile);
                    this.currentDependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y)));
                }
                dependencyOnTile = linkedDep;
                dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y - 256.0d)));
                addSmb.addTile(down);
                if (symbol.point.f68x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(leftdown)).drawn) {
                    dependencyOnTile = (DependencyOnTile) this.dependencyTable.get(leftdown);
                    dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x + 256.0d, symbol.point.f69y - 256.0d)));
                    addSmb.addTile(leftdown);
                }
                if (symbol.point.f68x + ((double) symbol.symbol.getWidth()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(rightdown)).drawn) {
                    dependencyOnTile = (DependencyOnTile) this.dependencyTable.get(rightdown);
                    dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x - 256.0d, symbol.point.f69y - 256.0d)));
                    addSmb.addTile(rightdown);
                }
            }
            if (symbol.point.f68x < 0.0d && !((DependencyOnTile) this.dependencyTable.get(left)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(left);
                if (addSmb == null) {
                    addSmb = new DependencySymbol(symbol.symbol, this.currentTile);
                    this.currentDependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y)));
                }
                dependencyOnTile = linkedDep;
                dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x + 256.0d, symbol.point.f69y)));
                addSmb.addTile(left);
            }
            if (symbol.point.f68x + ((double) symbol.symbol.getWidth()) > 256.0d && !((DependencyOnTile) this.dependencyTable.get(right)).drawn) {
                linkedDep = (DependencyOnTile) this.dependencyTable.get(right);
                if (addSmb == null) {
                    addSmb = new DependencySymbol(symbol.symbol, this.currentTile);
                    this.currentDependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x, symbol.point.f69y)));
                }
                dependencyOnTile = linkedDep;
                dependencyOnTile.addSymbol(new Dependency(addSmb, new Point(symbol.point.f68x - 256.0d, symbol.point.f69y)));
                addSmb.addTile(right);
            }
        }
    }

    private void removeOverlappingAreaLabelsWithDependencyLabels(List<PointTextContainer> areaLabels) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); i++) {
            this.depLabel = (Dependency) this.currentDependencyOnTile.labels.get(i);
            this.rect1 = new Rect((int) this.depLabel.point.f68x, (int) (this.depLabel.point.f69y - ((double) ((DependencyText) this.depLabel.value).boundary.height())), (int) (this.depLabel.point.f68x + ((double) ((DependencyText) this.depLabel.value).boundary.width())), (int) this.depLabel.point.f69y);
            int x = 0;
            while (x < areaLabels.size()) {
                PointTextContainer pTC = (PointTextContainer) areaLabels.get(x);
                this.rect2 = new Rect((int) pTC.f66x, ((int) pTC.f67y) - pTC.boundary.height(), ((int) pTC.f66x) + pTC.boundary.width(), (int) pTC.f67y);
                if (Rect.intersects(this.rect2, this.rect1)) {
                    areaLabels.remove(x);
                    x--;
                }
                x++;
            }
        }
    }

    private void removeOverlappingAreaLabelsWithDependencySymbols(List<PointTextContainer> areaLabels) {
        for (Dependency<DependencySymbol> depSmb : this.currentDependencyOnTile.symbols) {
            this.rect1 = new Rect((int) depSmb.point.f68x, (int) depSmb.point.f69y, ((int) depSmb.point.f68x) + ((DependencySymbol) depSmb.value).symbol.getWidth(), ((DependencySymbol) depSmb.value).symbol.getHeight() + ((int) depSmb.point.f69y));
            int x = 0;
            while (x < areaLabels.size()) {
                PointTextContainer label = (PointTextContainer) areaLabels.get(x);
                this.rect2 = new Rect((int) label.f66x, (int) (label.f67y - ((double) label.boundary.height())), (int) (label.f66x + ((double) label.boundary.width())), (int) label.f67y);
                if (Rect.intersects(this.rect2, this.rect1)) {
                    areaLabels.remove(x);
                    x--;
                }
                x++;
            }
        }
    }

    private void removeOverlappingLabelsWithDependencyLabels(List<PointTextContainer> labels) {
        int i = 0;
        while (i < this.currentDependencyOnTile.labels.size()) {
            int x = 0;
            while (x < labels.size()) {
                if (((PointTextContainer) labels.get(x)).text.equals(((DependencyText) ((Dependency) this.currentDependencyOnTile.labels.get(i)).value).text) && ((PointTextContainer) labels.get(x)).paintFront.equals(((DependencyText) ((Dependency) this.currentDependencyOnTile.labels.get(i)).value).paintFront) && ((PointTextContainer) labels.get(x)).paintBack.equals(((DependencyText) ((Dependency) this.currentDependencyOnTile.labels.get(i)).value).paintBack)) {
                    labels.remove(x);
                    i--;
                    break;
                }
                x++;
            }
            i++;
        }
    }

    private void removeOverlappingSymbolsWithDepencySymbols(List<SymbolContainer> symbols, int dis) {
        for (int x = 0; x < this.currentDependencyOnTile.symbols.size(); x++) {
            Dependency<DependencySymbol> sym2 = (Dependency) this.currentDependencyOnTile.symbols.get(x);
            this.rect1 = new Rect(((int) sym2.point.f68x) - dis, ((int) sym2.point.f69y) - dis, (((DependencySymbol) sym2.value).symbol.getWidth() + ((int) sym2.point.f68x)) + dis, (((DependencySymbol) sym2.value).symbol.getHeight() + ((int) sym2.point.f69y)) + dis);
            int y = 0;
            while (y < symbols.size()) {
                SymbolContainer symbolContainer = (SymbolContainer) symbols.get(y);
                this.rect2 = new Rect((int) symbolContainer.point.f68x, (int) symbolContainer.point.f69y, ((int) symbolContainer.point.f68x) + symbolContainer.symbol.getWidth(), ((int) symbolContainer.point.f69y) + symbolContainer.symbol.getHeight());
                if (Rect.intersects(this.rect2, this.rect1)) {
                    symbols.remove(y);
                    y--;
                }
                y++;
            }
        }
    }

    private void removeOverlappingSymbolsWithDependencyLabels(List<SymbolContainer> symbols) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); i++) {
            this.depLabel = (Dependency) this.currentDependencyOnTile.labels.get(i);
            this.rect1 = new Rect((int) this.depLabel.point.f68x, (int) (this.depLabel.point.f69y - ((double) ((DependencyText) this.depLabel.value).boundary.height())), (int) (this.depLabel.point.f68x + ((double) ((DependencyText) this.depLabel.value).boundary.width())), (int) this.depLabel.point.f69y);
            int x = 0;
            while (x < symbols.size()) {
                this.smb = (SymbolContainer) symbols.get(x);
                this.rect2 = new Rect((int) this.smb.point.f68x, (int) this.smb.point.f69y, ((int) this.smb.point.f68x) + this.smb.symbol.getWidth(), ((int) this.smb.point.f69y) + this.smb.symbol.getHeight());
                if (Rect.intersects(this.rect2, this.rect1)) {
                    symbols.remove(x);
                    x--;
                }
                x++;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void fillDependencyOnTile(List<PointTextContainer> labels, List<SymbolContainer> symbols, List<PointTextContainer> areaLabels) {
        this.currentDependencyOnTile.drawn = true;
        if (!(labels.isEmpty() && symbols.isEmpty() && areaLabels.isEmpty())) {
            fillDependencyOnTile2(labels, symbols, areaLabels);
        }
        if (this.currentDependencyOnTile.labels != null) {
            addLabelsFromDependencyOnTile(labels);
        }
        if (this.currentDependencyOnTile.symbols != null) {
            addSymbolsFromDependencyOnTile(symbols);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void generateTileAndDependencyOnTile(Tile tile) {
        this.currentTile = new Tile(tile.tileX, tile.tileY, tile.zoomLevel);
        this.currentDependencyOnTile = (DependencyOnTile) this.dependencyTable.get(this.currentTile);
        if (this.currentDependencyOnTile == null) {
            this.dependencyTable.put(this.currentTile, new DependencyOnTile());
            this.currentDependencyOnTile = (DependencyOnTile) this.dependencyTable.get(this.currentTile);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeAreaLabelsInAlreadyDrawnAreas(List<PointTextContainer> areaLabels) {
        boolean down;
        Tile lefttmp = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile righttmp = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile uptmp = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile downtmp = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        this.tmp = (DependencyOnTile) this.dependencyTable.get(lefttmp);
        boolean left = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(righttmp);
        boolean right = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(uptmp);
        boolean up = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(downtmp);
        if (this.tmp == null) {
            down = false;
        } else {
            down = this.tmp.drawn;
        }
        int i = 0;
        while (i < areaLabels.size()) {
            PointTextContainer label = (PointTextContainer) areaLabels.get(i);
            if (up && label.f67y - ((double) label.boundary.height()) < 0.0d) {
                areaLabels.remove(i);
                i--;
            } else if (down && label.f67y > 256.0d) {
                areaLabels.remove(i);
                i--;
            } else if (left && label.f66x < 0.0d) {
                areaLabels.remove(i);
                i--;
            } else if (right && label.f66x + ((double) label.boundary.width()) > 256.0d) {
                areaLabels.remove(i);
                i--;
            }
            i++;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeOverlappingObjectsWithDependencyOnTile(List<PointTextContainer> labels, List<PointTextContainer> areaLabels, List<SymbolContainer> symbols) {
        if (!(this.currentDependencyOnTile.labels == null || this.currentDependencyOnTile.labels.size() == 0)) {
            removeOverlappingLabelsWithDependencyLabels(labels);
            removeOverlappingSymbolsWithDependencyLabels(symbols);
            removeOverlappingAreaLabelsWithDependencyLabels(areaLabels);
        }
        if (this.currentDependencyOnTile.symbols != null && this.currentDependencyOnTile.symbols.size() != 0) {
            removeOverlappingSymbolsWithDepencySymbols(symbols, 2);
            removeOverlappingAreaLabelsWithDependencySymbols(areaLabels);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeReferencePointsFromDependencyCache(ReferencePosition[] refPos) {
        boolean down;
        int i;
        Tile lefttmp = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile righttmp = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile uptmp = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile downtmp = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        this.tmp = (DependencyOnTile) this.dependencyTable.get(lefttmp);
        boolean left = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(righttmp);
        boolean right = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(uptmp);
        boolean up = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(downtmp);
        if (this.tmp == null) {
            down = false;
        } else {
            down = this.tmp.drawn;
        }
        for (i = 0; i < refPos.length; i++) {
            ReferencePosition ref = refPos[i];
            if (ref != null) {
                if (up && ref.f65y - ((double) ref.height) < 0.0d) {
                    refPos[i] = null;
                } else if (down && ref.f65y >= 256.0d) {
                    refPos[i] = null;
                } else if (left && ref.f64x < 0.0d) {
                    refPos[i] = null;
                } else if (right && ref.f64x + ((double) ref.width) > 256.0d) {
                    refPos[i] = null;
                }
            }
        }
        if (this.currentDependencyOnTile != null) {
            int y;
            if (this.currentDependencyOnTile.labels != null) {
                for (i = 0; i < this.currentDependencyOnTile.labels.size(); i++) {
                    this.depLabel = (Dependency) this.currentDependencyOnTile.labels.get(i);
                    this.rect1 = new Rect(((int) this.depLabel.point.f68x) - 2, ((int) (this.depLabel.point.f69y - ((double) ((DependencyText) this.depLabel.value).boundary.height()))) - 2, (int) ((this.depLabel.point.f68x + ((double) ((DependencyText) this.depLabel.value).boundary.width())) + ((double) 2)), (int) (this.depLabel.point.f69y + ((double) 2)));
                    for (y = 0; y < refPos.length; y++) {
                        if (refPos[y] != null) {
                            this.rect2 = new Rect((int) refPos[y].f64x, (int) (refPos[y].f65y - ((double) refPos[y].height)), (int) (refPos[y].f64x + ((double) refPos[y].width)), (int) refPos[y].f65y);
                            if (Rect.intersects(this.rect2, this.rect1)) {
                                refPos[y] = null;
                            }
                        }
                    }
                }
            }
            if (this.currentDependencyOnTile.symbols != null) {
                for (Dependency<DependencySymbol> symbols2 : this.currentDependencyOnTile.symbols) {
                    this.rect1 = new Rect((int) symbols2.point.f68x, (int) symbols2.point.f69y, (int) (symbols2.point.f68x + ((double) ((DependencySymbol) symbols2.value).symbol.getWidth())), (int) (symbols2.point.f69y + ((double) ((DependencySymbol) symbols2.value).symbol.getHeight())));
                    for (y = 0; y < refPos.length; y++) {
                        if (refPos[y] != null) {
                            this.rect2 = new Rect((int) refPos[y].f64x, (int) (refPos[y].f65y - ((double) refPos[y].height)), (int) (refPos[y].f64x + ((double) refPos[y].width)), (int) refPos[y].f65y);
                            if (Rect.intersects(this.rect2, this.rect1)) {
                                refPos[y] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeSymbolsFromDrawnAreas(List<SymbolContainer> symbols) {
        boolean down;
        Tile lefttmp = new Tile(this.currentTile.tileX - 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile righttmp = new Tile(this.currentTile.tileX + 1, this.currentTile.tileY, this.currentTile.zoomLevel);
        Tile uptmp = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1, this.currentTile.zoomLevel);
        Tile downtmp = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1, this.currentTile.zoomLevel);
        this.tmp = (DependencyOnTile) this.dependencyTable.get(lefttmp);
        boolean left = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(righttmp);
        boolean right = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(uptmp);
        boolean up = this.tmp == null ? false : this.tmp.drawn;
        this.tmp = (DependencyOnTile) this.dependencyTable.get(downtmp);
        if (this.tmp == null) {
            down = false;
        } else {
            down = this.tmp.drawn;
        }
        int i = 0;
        while (i < symbols.size()) {
            SymbolContainer ref = (SymbolContainer) symbols.get(i);
            if (up && ref.point.f69y < 0.0d) {
                symbols.remove(i);
                i--;
            } else if (down && ref.point.f69y + ((double) ref.symbol.getHeight()) > 256.0d) {
                symbols.remove(i);
                i--;
            } else if (left && ref.point.f68x < 0.0d) {
                symbols.remove(i);
                i--;
            } else if (right && ref.point.f68x + ((double) ref.symbol.getWidth()) > 256.0d) {
                symbols.remove(i);
                i--;
            }
            i++;
        }
    }
}
