package org.osmdroid.views;

import android.graphics.drawable.Drawable;
import java.util.HashSet;
import java.util.Set;
import org.osmdroid.library.R$drawable;
import org.osmdroid.library.R$layout;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class MapViewRepository {
    private Drawable mDefaultMarkerIcon;
    private MarkerInfoWindow mDefaultMarkerInfoWindow;
    private BasicInfoWindow mDefaultPolygonInfoWindow;
    private BasicInfoWindow mDefaultPolylineInfoWindow;
    private final Set<InfoWindow> mInfoWindowList = new HashSet();
    private MapView mMapView;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:15:0x002e in {6, 10, 14} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void onDetach() {
        /*
        r3 = this;
        r0 = r3.mInfoWindowList;
        monitor-enter(r0);
        r1 = r3.mInfoWindowList;	 Catch:{ all -> 0x002b }
        r1 = r1.iterator();	 Catch:{ all -> 0x002b }
        r2 = r1.hasNext();	 Catch:{ all -> 0x002b }
        if (r2 == 0) goto L_0x0019;	 Catch:{ all -> 0x002b }
        r2 = r1.next();	 Catch:{ all -> 0x002b }
        r2 = (org.osmdroid.views.overlay.infowindow.InfoWindow) r2;	 Catch:{ all -> 0x002b }
        r2.onDetach();	 Catch:{ all -> 0x002b }
        goto L_0x0009;	 Catch:{ all -> 0x002b }
        r1 = r3.mInfoWindowList;	 Catch:{ all -> 0x002b }
        r1.clear();	 Catch:{ all -> 0x002b }
        monitor-exit(r0);	 Catch:{ all -> 0x002b }
        r0 = 0;
        r3.mMapView = r0;
        r3.mDefaultMarkerInfoWindow = r0;
        r3.mDefaultPolylineInfoWindow = r0;
        r3.mDefaultPolygonInfoWindow = r0;
        r3.mDefaultMarkerIcon = r0;
        return;
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002b }
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.MapViewRepository.onDetach():void");
    }

    public MapViewRepository(MapView mapView) {
        this.mMapView = mapView;
    }

    public void add(InfoWindow infoWindow) {
        this.mInfoWindowList.add(infoWindow);
    }

    public MarkerInfoWindow getDefaultMarkerInfoWindow() {
        if (this.mDefaultMarkerInfoWindow == null) {
            this.mDefaultMarkerInfoWindow = new MarkerInfoWindow(R$layout.bonuspack_bubble, this.mMapView);
        }
        return this.mDefaultMarkerInfoWindow;
    }

    public Drawable getDefaultMarkerIcon() {
        if (this.mDefaultMarkerIcon == null) {
            this.mDefaultMarkerIcon = this.mMapView.getContext().getResources().getDrawable(R$drawable.marker_default);
        }
        return this.mDefaultMarkerIcon;
    }
}
