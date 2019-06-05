// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.map.graphics.Paint;
import java.util.LinkedList;
import org.mapsforge.map.graphics.Bitmap;
import java.util.ArrayList;
import org.mapsforge.core.model.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import android.graphics.Rect;
import java.util.Map;
import org.mapsforge.core.model.Tile;

class DependencyCache
{
    private DependencyOnTile currentDependencyOnTile;
    private Tile currentTile;
    Dependency<DependencyText> depLabel;
    final Map<Tile, DependencyOnTile> dependencyTable;
    Rect rect1;
    Rect rect2;
    SymbolContainer smb;
    DependencyOnTile tmp;
    
    DependencyCache() {
        this.dependencyTable = new Hashtable<Tile, DependencyOnTile>(60);
    }
    
    private void addLabelsFromDependencyOnTile(final List<PointTextContainer> list) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); ++i) {
            this.depLabel = (Dependency<DependencyText>)(Dependency)this.currentDependencyOnTile.labels.get(i);
            if (this.depLabel.value.paintBack != null) {
                list.add(new PointTextContainer(this.depLabel.value.text, this.depLabel.point.x, this.depLabel.point.y, this.depLabel.value.paintFront, this.depLabel.value.paintBack));
            }
            else {
                list.add(new PointTextContainer(this.depLabel.value.text, this.depLabel.point.x, this.depLabel.point.y, this.depLabel.value.paintFront));
            }
        }
    }
    
    private void addSymbolsFromDependencyOnTile(final List<SymbolContainer> list) {
        for (final Dependency dependency : this.currentDependencyOnTile.symbols) {
            list.add(new SymbolContainer(((DependencySymbol)dependency.value).symbol, dependency.point));
        }
    }
    
    private void fillDependencyLabels(final List<PointTextContainer> list) {
        final Tile tile = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile2 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile3 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile4 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        final Tile tile5 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile6 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        final Tile tile7 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile8 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        for (int i = 0; i < list.size(); ++i) {
            final PointTextContainer pointTextContainer = list.get(i);
            DependencyText dependencyText2;
            final DependencyText dependencyText = dependencyText2 = null;
            if (pointTextContainer.y - pointTextContainer.boundary.height() < 0.0) {
                dependencyText2 = dependencyText;
                if (!this.dependencyTable.get(tile3).drawn) {
                    final DependencyOnTile dependencyOnTile = this.dependencyTable.get(tile3);
                    final DependencyText dependencyText3 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                    this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText3, new Point(pointTextContainer.x, pointTextContainer.y)));
                    dependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText3, new Point(pointTextContainer.x, pointTextContainer.y + 256.0)));
                    dependencyText3.addTile(tile3);
                    if (pointTextContainer.x < 0.0 && !this.dependencyTable.get(tile5).drawn) {
                        this.dependencyTable.get(tile5).addText((Dependency<DependencyText>)new Dependency(dependencyText3, new Point(pointTextContainer.x + 256.0, pointTextContainer.y + 256.0)));
                        dependencyText3.addTile(tile5);
                    }
                    dependencyText2 = dependencyText3;
                    if (pointTextContainer.x + pointTextContainer.boundary.width() > 256.0) {
                        dependencyText2 = dependencyText3;
                        if (!this.dependencyTable.get(tile7).drawn) {
                            this.dependencyTable.get(tile7).addText((Dependency<DependencyText>)new Dependency(dependencyText3, new Point(pointTextContainer.x - 256.0, pointTextContainer.y + 256.0)));
                            dependencyText3.addTile(tile7);
                            dependencyText2 = dependencyText3;
                        }
                    }
                }
            }
            DependencyText dependencyText4 = dependencyText2;
            if (pointTextContainer.y > 256.0) {
                dependencyText4 = dependencyText2;
                if (!this.dependencyTable.get(tile4).drawn) {
                    final DependencyOnTile dependencyOnTile2 = this.dependencyTable.get(tile4);
                    DependencyText dependencyText5;
                    if ((dependencyText5 = dependencyText2) == null) {
                        dependencyText5 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText5, new Point(pointTextContainer.x, pointTextContainer.y)));
                    }
                    dependencyOnTile2.addText((Dependency<DependencyText>)new Dependency(dependencyText5, new Point(pointTextContainer.x, pointTextContainer.y - 256.0)));
                    dependencyText5.addTile(tile4);
                    if (pointTextContainer.x < 0.0 && !this.dependencyTable.get(tile6).drawn) {
                        this.dependencyTable.get(tile6).addText((Dependency<DependencyText>)new Dependency(dependencyText5, new Point(pointTextContainer.x + 256.0, pointTextContainer.y - 256.0)));
                        dependencyText5.addTile(tile6);
                    }
                    dependencyText4 = dependencyText5;
                    if (pointTextContainer.x + pointTextContainer.boundary.width() > 256.0) {
                        dependencyText4 = dependencyText5;
                        if (!this.dependencyTable.get(tile8).drawn) {
                            this.dependencyTable.get(tile8).addText((Dependency<DependencyText>)new Dependency(dependencyText5, new Point(pointTextContainer.x - 256.0, pointTextContainer.y - 256.0)));
                            dependencyText5.addTile(tile8);
                            dependencyText4 = dependencyText5;
                        }
                    }
                }
            }
            DependencyText dependencyText6 = dependencyText4;
            if (pointTextContainer.x < 0.0) {
                dependencyText6 = dependencyText4;
                if (!this.dependencyTable.get(tile).drawn) {
                    final DependencyOnTile dependencyOnTile3 = this.dependencyTable.get(tile);
                    if ((dependencyText6 = dependencyText4) == null) {
                        dependencyText6 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText6, new Point(pointTextContainer.x, pointTextContainer.y)));
                    }
                    dependencyOnTile3.addText((Dependency<DependencyText>)new Dependency(dependencyText6, new Point(pointTextContainer.x + 256.0, pointTextContainer.y)));
                    dependencyText6.addTile(tile);
                }
            }
            DependencyText dependencyText7 = dependencyText6;
            if (pointTextContainer.x + pointTextContainer.boundary.width() > 256.0) {
                dependencyText7 = dependencyText6;
                if (!this.dependencyTable.get(tile2).drawn) {
                    final DependencyOnTile dependencyOnTile4 = this.dependencyTable.get(tile2);
                    if ((dependencyText7 = dependencyText6) == null) {
                        dependencyText7 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText7, new Point(pointTextContainer.x, pointTextContainer.y)));
                    }
                    dependencyOnTile4.addText((Dependency<DependencyText>)new Dependency(dependencyText7, new Point(pointTextContainer.x - 256.0, pointTextContainer.y)));
                    dependencyText7.addTile(tile2);
                }
            }
            if (pointTextContainer.symbol != null && dependencyText7 == null) {
                DependencyText dependencyText8 = dependencyText7;
                if (pointTextContainer.symbol.point.y <= 0.0) {
                    dependencyText8 = dependencyText7;
                    if (!this.dependencyTable.get(tile3).drawn) {
                        final DependencyOnTile dependencyOnTile5 = this.dependencyTable.get(tile3);
                        final DependencyText dependencyText9 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText9, new Point(pointTextContainer.x, pointTextContainer.y)));
                        dependencyOnTile5.addText((Dependency<DependencyText>)new Dependency(dependencyText9, new Point(pointTextContainer.x, pointTextContainer.y + 256.0)));
                        dependencyText9.addTile(tile3);
                        if (pointTextContainer.symbol.point.x < 0.0 && !this.dependencyTable.get(tile5).drawn) {
                            this.dependencyTable.get(tile5).addText((Dependency<DependencyText>)new Dependency(dependencyText9, new Point(pointTextContainer.x + 256.0, pointTextContainer.y + 256.0)));
                            dependencyText9.addTile(tile5);
                        }
                        dependencyText8 = dependencyText9;
                        if (pointTextContainer.symbol.point.x + pointTextContainer.symbol.symbol.getWidth() > 256.0) {
                            dependencyText8 = dependencyText9;
                            if (!this.dependencyTable.get(tile7).drawn) {
                                this.dependencyTable.get(tile7).addText((Dependency<DependencyText>)new Dependency(dependencyText9, new Point(pointTextContainer.x - 256.0, pointTextContainer.y + 256.0)));
                                dependencyText9.addTile(tile7);
                                dependencyText8 = dependencyText9;
                            }
                        }
                    }
                }
                DependencyText dependencyText10 = dependencyText8;
                if (pointTextContainer.symbol.point.y + pointTextContainer.symbol.symbol.getHeight() >= 256.0) {
                    dependencyText10 = dependencyText8;
                    if (!this.dependencyTable.get(tile4).drawn) {
                        final DependencyOnTile dependencyOnTile6 = this.dependencyTable.get(tile4);
                        DependencyText dependencyText11;
                        if ((dependencyText11 = dependencyText8) == null) {
                            dependencyText11 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                            this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText11, new Point(pointTextContainer.x, pointTextContainer.y)));
                        }
                        dependencyOnTile6.addText((Dependency<DependencyText>)new Dependency(dependencyText11, new Point(pointTextContainer.x, pointTextContainer.y + 256.0)));
                        dependencyText11.addTile(tile3);
                        if (pointTextContainer.symbol.point.x < 0.0 && !this.dependencyTable.get(tile6).drawn) {
                            this.dependencyTable.get(tile6).addText((Dependency<DependencyText>)new Dependency(dependencyText11, new Point(pointTextContainer.x + 256.0, pointTextContainer.y - 256.0)));
                            dependencyText11.addTile(tile6);
                        }
                        dependencyText10 = dependencyText11;
                        if (pointTextContainer.symbol.point.x + pointTextContainer.symbol.symbol.getWidth() > 256.0) {
                            dependencyText10 = dependencyText11;
                            if (!this.dependencyTable.get(tile8).drawn) {
                                this.dependencyTable.get(tile8).addText((Dependency<DependencyText>)new Dependency(dependencyText11, new Point(pointTextContainer.x - 256.0, pointTextContainer.y - 256.0)));
                                dependencyText11.addTile(tile8);
                                dependencyText10 = dependencyText11;
                            }
                        }
                    }
                }
                DependencyText dependencyText12 = dependencyText10;
                if (pointTextContainer.symbol.point.x <= 0.0) {
                    dependencyText12 = dependencyText10;
                    if (!this.dependencyTable.get(tile).drawn) {
                        final DependencyOnTile dependencyOnTile7 = this.dependencyTable.get(tile);
                        if ((dependencyText12 = dependencyText10) == null) {
                            dependencyText12 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                            this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText12, new Point(pointTextContainer.x, pointTextContainer.y)));
                        }
                        dependencyOnTile7.addText((Dependency<DependencyText>)new Dependency(dependencyText12, new Point(pointTextContainer.x - 256.0, pointTextContainer.y)));
                        dependencyText12.addTile(tile);
                    }
                }
                if (pointTextContainer.symbol.point.x + pointTextContainer.symbol.symbol.getWidth() >= 256.0 && !this.dependencyTable.get(tile2).drawn) {
                    final DependencyOnTile dependencyOnTile8 = this.dependencyTable.get(tile2);
                    DependencyText dependencyText13;
                    if ((dependencyText13 = dependencyText12) == null) {
                        dependencyText13 = new DependencyText(pointTextContainer.paintFront, pointTextContainer.paintBack, pointTextContainer.text, pointTextContainer.boundary, this.currentTile);
                        this.currentDependencyOnTile.addText((Dependency<DependencyText>)new Dependency(dependencyText13, new Point(pointTextContainer.x, pointTextContainer.y)));
                    }
                    dependencyOnTile8.addText((Dependency<DependencyText>)new Dependency(dependencyText13, new Point(pointTextContainer.x + 256.0, pointTextContainer.y)));
                    dependencyText13.addTile(tile2);
                }
            }
        }
    }
    
    private void fillDependencyOnTile2(final List<PointTextContainer> list, final List<SymbolContainer> list2, final List<PointTextContainer> list3) {
        final Tile tile = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile2 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile3 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile4 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        final Tile tile5 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile6 = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        final Tile tile7 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile8 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        if (this.dependencyTable.get(tile3) == null) {
            this.dependencyTable.put(tile3, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile4) == null) {
            this.dependencyTable.put(tile4, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile) == null) {
            this.dependencyTable.put(tile, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile2) == null) {
            this.dependencyTable.put(tile2, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile6) == null) {
            this.dependencyTable.put(tile6, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile7) == null) {
            this.dependencyTable.put(tile7, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile5) == null) {
            this.dependencyTable.put(tile5, new DependencyOnTile());
        }
        if (this.dependencyTable.get(tile8) == null) {
            this.dependencyTable.put(tile8, new DependencyOnTile());
        }
        this.fillDependencyLabels(list);
        this.fillDependencyLabels(list3);
        for (final SymbolContainer symbolContainer : list2) {
            DependencySymbol dependencySymbol2;
            final DependencySymbol dependencySymbol = dependencySymbol2 = null;
            if (symbolContainer.point.y < 0.0) {
                dependencySymbol2 = dependencySymbol;
                if (!this.dependencyTable.get(tile3).drawn) {
                    final DependencyOnTile dependencyOnTile = this.dependencyTable.get(tile3);
                    final DependencySymbol dependencySymbol3 = new DependencySymbol(symbolContainer.symbol, this.currentTile);
                    this.currentDependencyOnTile.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol3, new Point(symbolContainer.point.x, symbolContainer.point.y)));
                    dependencyOnTile.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol3, new Point(symbolContainer.point.x, symbolContainer.point.y + 256.0)));
                    dependencySymbol3.addTile(tile3);
                    if (symbolContainer.point.x < 0.0 && !this.dependencyTable.get(tile5).drawn) {
                        this.dependencyTable.get(tile5).addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol3, new Point(symbolContainer.point.x + 256.0, symbolContainer.point.y + 256.0)));
                        dependencySymbol3.addTile(tile5);
                    }
                    dependencySymbol2 = dependencySymbol3;
                    if (symbolContainer.point.x + symbolContainer.symbol.getWidth() > 256.0) {
                        dependencySymbol2 = dependencySymbol3;
                        if (!this.dependencyTable.get(tile7).drawn) {
                            this.dependencyTable.get(tile7).addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol3, new Point(symbolContainer.point.x - 256.0, symbolContainer.point.y + 256.0)));
                            dependencySymbol3.addTile(tile7);
                            dependencySymbol2 = dependencySymbol3;
                        }
                    }
                }
            }
            DependencySymbol dependencySymbol4 = dependencySymbol2;
            if (symbolContainer.point.y + symbolContainer.symbol.getHeight() > 256.0) {
                dependencySymbol4 = dependencySymbol2;
                if (!this.dependencyTable.get(tile4).drawn) {
                    final DependencyOnTile dependencyOnTile2 = this.dependencyTable.get(tile4);
                    DependencySymbol dependencySymbol5;
                    if ((dependencySymbol5 = dependencySymbol2) == null) {
                        dependencySymbol5 = new DependencySymbol(symbolContainer.symbol, this.currentTile);
                        this.currentDependencyOnTile.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol5, new Point(symbolContainer.point.x, symbolContainer.point.y)));
                    }
                    dependencyOnTile2.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol5, new Point(symbolContainer.point.x, symbolContainer.point.y - 256.0)));
                    dependencySymbol5.addTile(tile4);
                    if (symbolContainer.point.x < 0.0 && !this.dependencyTable.get(tile6).drawn) {
                        this.dependencyTable.get(tile6).addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol5, new Point(symbolContainer.point.x + 256.0, symbolContainer.point.y - 256.0)));
                        dependencySymbol5.addTile(tile6);
                    }
                    dependencySymbol4 = dependencySymbol5;
                    if (symbolContainer.point.x + symbolContainer.symbol.getWidth() > 256.0) {
                        dependencySymbol4 = dependencySymbol5;
                        if (!this.dependencyTable.get(tile8).drawn) {
                            this.dependencyTable.get(tile8).addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol5, new Point(symbolContainer.point.x - 256.0, symbolContainer.point.y - 256.0)));
                            dependencySymbol5.addTile(tile8);
                            dependencySymbol4 = dependencySymbol5;
                        }
                    }
                }
            }
            DependencySymbol dependencySymbol6 = dependencySymbol4;
            if (symbolContainer.point.x < 0.0) {
                dependencySymbol6 = dependencySymbol4;
                if (!this.dependencyTable.get(tile).drawn) {
                    final DependencyOnTile dependencyOnTile3 = this.dependencyTable.get(tile);
                    if ((dependencySymbol6 = dependencySymbol4) == null) {
                        dependencySymbol6 = new DependencySymbol(symbolContainer.symbol, this.currentTile);
                        this.currentDependencyOnTile.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol6, new Point(symbolContainer.point.x, symbolContainer.point.y)));
                    }
                    dependencyOnTile3.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol6, new Point(symbolContainer.point.x + 256.0, symbolContainer.point.y)));
                    dependencySymbol6.addTile(tile);
                }
            }
            if (symbolContainer.point.x + symbolContainer.symbol.getWidth() > 256.0 && !this.dependencyTable.get(tile2).drawn) {
                final DependencyOnTile dependencyOnTile4 = this.dependencyTable.get(tile2);
                DependencySymbol dependencySymbol7;
                if ((dependencySymbol7 = dependencySymbol6) == null) {
                    dependencySymbol7 = new DependencySymbol(symbolContainer.symbol, this.currentTile);
                    this.currentDependencyOnTile.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol7, new Point(symbolContainer.point.x, symbolContainer.point.y)));
                }
                dependencyOnTile4.addSymbol((Dependency<DependencySymbol>)new Dependency(dependencySymbol7, new Point(symbolContainer.point.x - 256.0, symbolContainer.point.y)));
                dependencySymbol7.addTile(tile2);
            }
        }
    }
    
    private void removeOverlappingAreaLabelsWithDependencyLabels(final List<PointTextContainer> list) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); ++i) {
            this.depLabel = (Dependency<DependencyText>)(Dependency)this.currentDependencyOnTile.labels.get(i);
            this.rect1 = new Rect((int)this.depLabel.point.x, (int)(this.depLabel.point.y - this.depLabel.value.boundary.height()), (int)(this.depLabel.point.x + this.depLabel.value.boundary.width()), (int)this.depLabel.point.y);
            int n;
            for (int j = 0; j < list.size(); j = n + 1) {
                final PointTextContainer pointTextContainer = list.get(j);
                this.rect2 = new Rect((int)pointTextContainer.x, (int)pointTextContainer.y - pointTextContainer.boundary.height(), (int)pointTextContainer.x + pointTextContainer.boundary.width(), (int)pointTextContainer.y);
                n = j;
                if (Rect.intersects(this.rect2, this.rect1)) {
                    list.remove(j);
                    n = j - 1;
                }
            }
        }
    }
    
    private void removeOverlappingAreaLabelsWithDependencySymbols(final List<PointTextContainer> list) {
        for (final Dependency dependency : this.currentDependencyOnTile.symbols) {
            this.rect1 = new Rect((int)dependency.point.x, (int)dependency.point.y, (int)dependency.point.x + ((DependencySymbol)dependency.value).symbol.getWidth(), ((DependencySymbol)dependency.value).symbol.getHeight() + (int)dependency.point.y);
            int n;
            for (int i = 0; i < list.size(); i = n + 1) {
                final PointTextContainer pointTextContainer = list.get(i);
                this.rect2 = new Rect((int)pointTextContainer.x, (int)(pointTextContainer.y - pointTextContainer.boundary.height()), (int)(pointTextContainer.x + pointTextContainer.boundary.width()), (int)pointTextContainer.y);
                n = i;
                if (Rect.intersects(this.rect2, this.rect1)) {
                    list.remove(i);
                    n = i - 1;
                }
            }
        }
    }
    
    private void removeOverlappingLabelsWithDependencyLabels(final List<PointTextContainer> list) {
        int n2;
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); i = n2 + 1) {
            int n = 0;
            while (true) {
                n2 = i;
                if (n >= list.size()) {
                    break;
                }
                if (list.get(n).text.equals(((DependencyText)((Dependency)this.currentDependencyOnTile.labels.get(i)).value).text) && list.get(n).paintFront.equals(((DependencyText)((Dependency)this.currentDependencyOnTile.labels.get(i)).value).paintFront) && list.get(n).paintBack.equals(((DependencyText)((Dependency)this.currentDependencyOnTile.labels.get(i)).value).paintBack)) {
                    list.remove(n);
                    n2 = i - 1;
                    break;
                }
                ++n;
            }
        }
    }
    
    private void removeOverlappingSymbolsWithDepencySymbols(final List<SymbolContainer> list, final int n) {
        for (int i = 0; i < this.currentDependencyOnTile.symbols.size(); ++i) {
            final Dependency dependency = (Dependency)this.currentDependencyOnTile.symbols.get(i);
            this.rect1 = new Rect((int)dependency.point.x - n, (int)dependency.point.y - n, ((DependencySymbol)dependency.value).symbol.getWidth() + (int)dependency.point.x + n, ((DependencySymbol)dependency.value).symbol.getHeight() + (int)dependency.point.y + n);
            int n2;
            for (int j = 0; j < list.size(); j = n2 + 1) {
                final SymbolContainer symbolContainer = list.get(j);
                this.rect2 = new Rect((int)symbolContainer.point.x, (int)symbolContainer.point.y, (int)symbolContainer.point.x + symbolContainer.symbol.getWidth(), (int)symbolContainer.point.y + symbolContainer.symbol.getHeight());
                n2 = j;
                if (Rect.intersects(this.rect2, this.rect1)) {
                    list.remove(j);
                    n2 = j - 1;
                }
            }
        }
    }
    
    private void removeOverlappingSymbolsWithDependencyLabels(final List<SymbolContainer> list) {
        for (int i = 0; i < this.currentDependencyOnTile.labels.size(); ++i) {
            this.depLabel = (Dependency<DependencyText>)(Dependency)this.currentDependencyOnTile.labels.get(i);
            this.rect1 = new Rect((int)this.depLabel.point.x, (int)(this.depLabel.point.y - this.depLabel.value.boundary.height()), (int)(this.depLabel.point.x + this.depLabel.value.boundary.width()), (int)this.depLabel.point.y);
            int n;
            for (int j = 0; j < list.size(); j = n + 1) {
                this.smb = list.get(j);
                this.rect2 = new Rect((int)this.smb.point.x, (int)this.smb.point.y, (int)this.smb.point.x + this.smb.symbol.getWidth(), (int)this.smb.point.y + this.smb.symbol.getHeight());
                n = j;
                if (Rect.intersects(this.rect2, this.rect1)) {
                    list.remove(j);
                    n = j - 1;
                }
            }
        }
    }
    
    void fillDependencyOnTile(final List<PointTextContainer> list, final List<SymbolContainer> list2, final List<PointTextContainer> list3) {
        this.currentDependencyOnTile.drawn = true;
        if (!list.isEmpty() || !list2.isEmpty() || !list3.isEmpty()) {
            this.fillDependencyOnTile2(list, list2, list3);
        }
        if (this.currentDependencyOnTile.labels != null) {
            this.addLabelsFromDependencyOnTile(list);
        }
        if (this.currentDependencyOnTile.symbols != null) {
            this.addSymbolsFromDependencyOnTile(list2);
        }
    }
    
    void generateTileAndDependencyOnTile(final Tile tile) {
        this.currentTile = new Tile(tile.tileX, tile.tileY, tile.zoomLevel);
        this.currentDependencyOnTile = this.dependencyTable.get(this.currentTile);
        if (this.currentDependencyOnTile == null) {
            this.dependencyTable.put(this.currentTile, new DependencyOnTile());
            this.currentDependencyOnTile = this.dependencyTable.get(this.currentTile);
        }
    }
    
    void removeAreaLabelsInAlreadyDrawnAreas(final List<PointTextContainer> list) {
        final Tile tile = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile2 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile3 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile4 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        this.tmp = this.dependencyTable.get(tile);
        final boolean b = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile2);
        final boolean b2 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile3);
        final boolean b3 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile4);
        final boolean b4 = this.tmp != null && this.tmp.drawn;
        int n;
        for (int i = 0; i < list.size(); i = n + 1) {
            final PointTextContainer pointTextContainer = list.get(i);
            if (b3 && pointTextContainer.y - pointTextContainer.boundary.height() < 0.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (b4 && pointTextContainer.y > 256.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (b && pointTextContainer.x < 0.0) {
                list.remove(i);
                n = i - 1;
            }
            else {
                n = i;
                if (b2) {
                    n = i;
                    if (pointTextContainer.x + pointTextContainer.boundary.width() > 256.0) {
                        list.remove(i);
                        n = i - 1;
                    }
                }
            }
        }
    }
    
    void removeOverlappingObjectsWithDependencyOnTile(final List<PointTextContainer> list, final List<PointTextContainer> list2, final List<SymbolContainer> list3) {
        if (this.currentDependencyOnTile.labels != null && this.currentDependencyOnTile.labels.size() != 0) {
            this.removeOverlappingLabelsWithDependencyLabels(list);
            this.removeOverlappingSymbolsWithDependencyLabels(list3);
            this.removeOverlappingAreaLabelsWithDependencyLabels(list2);
        }
        if (this.currentDependencyOnTile.symbols != null && this.currentDependencyOnTile.symbols.size() != 0) {
            this.removeOverlappingSymbolsWithDepencySymbols(list3, 2);
            this.removeOverlappingAreaLabelsWithDependencySymbols(list2);
        }
    }
    
    void removeReferencePointsFromDependencyCache(final LabelPlacement.ReferencePosition[] array) {
        final Tile tile = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile2 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile3 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile4 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        this.tmp = this.dependencyTable.get(tile);
        final boolean b = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile2);
        final boolean b2 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile3);
        final boolean b3 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile4);
        final boolean b4 = this.tmp != null && this.tmp.drawn;
        for (int i = 0; i < array.length; ++i) {
            final LabelPlacement.ReferencePosition referencePosition = array[i];
            if (referencePosition != null) {
                if (b3 && referencePosition.y - referencePosition.height < 0.0) {
                    array[i] = null;
                }
                else if (b4 && referencePosition.y >= 256.0) {
                    array[i] = null;
                }
                else if (b && referencePosition.x < 0.0) {
                    array[i] = null;
                }
                else if (b2 && referencePosition.x + referencePosition.width > 256.0) {
                    array[i] = null;
                }
            }
        }
        if (this.currentDependencyOnTile != null) {
            if (this.currentDependencyOnTile.labels != null) {
                for (int j = 0; j < this.currentDependencyOnTile.labels.size(); ++j) {
                    this.depLabel = (Dependency<DependencyText>)(Dependency)this.currentDependencyOnTile.labels.get(j);
                    this.rect1 = new Rect((int)this.depLabel.point.x - 2, (int)(this.depLabel.point.y - this.depLabel.value.boundary.height()) - 2, (int)(this.depLabel.point.x + this.depLabel.value.boundary.width() + 2), (int)(this.depLabel.point.y + 2));
                    for (int k = 0; k < array.length; ++k) {
                        if (array[k] != null) {
                            this.rect2 = new Rect((int)array[k].x, (int)(array[k].y - array[k].height), (int)(array[k].x + array[k].width), (int)array[k].y);
                            if (Rect.intersects(this.rect2, this.rect1)) {
                                array[k] = null;
                            }
                        }
                    }
                }
            }
            if (this.currentDependencyOnTile.symbols != null) {
                for (final Dependency dependency : this.currentDependencyOnTile.symbols) {
                    this.rect1 = new Rect((int)dependency.point.x, (int)dependency.point.y, (int)(dependency.point.x + ((DependencySymbol)dependency.value).symbol.getWidth()), (int)(dependency.point.y + ((DependencySymbol)dependency.value).symbol.getHeight()));
                    for (int l = 0; l < array.length; ++l) {
                        if (array[l] != null) {
                            this.rect2 = new Rect((int)array[l].x, (int)(array[l].y - array[l].height), (int)(array[l].x + array[l].width), (int)array[l].y);
                            if (Rect.intersects(this.rect2, this.rect1)) {
                                array[l] = null;
                            }
                        }
                    }
                }
            }
        }
    }
    
    void removeSymbolsFromDrawnAreas(final List<SymbolContainer> list) {
        final Tile tile = new Tile(this.currentTile.tileX - 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile2 = new Tile(this.currentTile.tileX + 1L, this.currentTile.tileY, this.currentTile.zoomLevel);
        final Tile tile3 = new Tile(this.currentTile.tileX, this.currentTile.tileY - 1L, this.currentTile.zoomLevel);
        final Tile tile4 = new Tile(this.currentTile.tileX, this.currentTile.tileY + 1L, this.currentTile.zoomLevel);
        this.tmp = this.dependencyTable.get(tile);
        final boolean b = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile2);
        final boolean b2 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile3);
        final boolean b3 = this.tmp != null && this.tmp.drawn;
        this.tmp = this.dependencyTable.get(tile4);
        final boolean b4 = this.tmp != null && this.tmp.drawn;
        int n;
        for (int i = 0; i < list.size(); i = n + 1) {
            final SymbolContainer symbolContainer = list.get(i);
            if (b3 && symbolContainer.point.y < 0.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (b4 && symbolContainer.point.y + symbolContainer.symbol.getHeight() > 256.0) {
                list.remove(i);
                n = i - 1;
            }
            else if (b && symbolContainer.point.x < 0.0) {
                list.remove(i);
                n = i - 1;
            }
            else {
                n = i;
                if (b2) {
                    n = i;
                    if (symbolContainer.point.x + symbolContainer.symbol.getWidth() > 256.0) {
                        list.remove(i);
                        n = i - 1;
                    }
                }
            }
        }
    }
    
    private static class Dependency<Type>
    {
        final Point point;
        final Type value;
        
        Dependency(final Type value, final Point point) {
            this.value = value;
            this.point = point;
        }
    }
    
    private static class DependencyOnTile
    {
        boolean drawn;
        List<Dependency<DependencyText>> labels;
        List<Dependency<DependencySymbol>> symbols;
        
        DependencyOnTile() {
            this.labels = null;
            this.symbols = null;
            this.drawn = false;
        }
        
        void addSymbol(final Dependency<DependencySymbol> dependency) {
            if (this.symbols == null) {
                this.symbols = new ArrayList<Dependency<DependencySymbol>>();
            }
            this.symbols.add(dependency);
        }
        
        void addText(final Dependency<DependencyText> dependency) {
            if (this.labels == null) {
                this.labels = new ArrayList<Dependency<DependencyText>>();
            }
            this.labels.add(dependency);
        }
    }
    
    private static class DependencySymbol
    {
        final Bitmap symbol;
        private final List<Tile> tiles;
        
        DependencySymbol(final Bitmap symbol, final Tile tile) {
            this.symbol = symbol;
            (this.tiles = new LinkedList<Tile>()).add(tile);
        }
        
        void addTile(final Tile tile) {
            this.tiles.add(tile);
        }
    }
    
    private static class DependencyText
    {
        final Rect boundary;
        final Paint paintBack;
        final Paint paintFront;
        final String text;
        final List<Tile> tiles;
        
        DependencyText(final Paint paintFront, final Paint paintBack, final String text, final Rect boundary, final Tile tile) {
            this.paintFront = paintFront;
            this.paintBack = paintBack;
            this.text = text;
            (this.tiles = new LinkedList<Tile>()).add(tile);
            this.boundary = boundary;
        }
        
        void addTile(final Tile tile) {
            this.tiles.add(tile);
        }
    }
}
