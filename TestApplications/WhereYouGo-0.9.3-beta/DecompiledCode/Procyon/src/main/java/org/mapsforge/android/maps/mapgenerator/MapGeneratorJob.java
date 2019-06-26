// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.mapsforge.core.model.Tile;
import java.io.File;
import org.mapsforge.android.maps.DebugSettings;
import java.io.Serializable;

public class MapGeneratorJob implements Comparable<MapGeneratorJob>, Serializable
{
    private static final long serialVersionUID = 1L;
    public final DebugSettings debugSettings;
    private transient int hashCodeValue;
    public final JobParameters jobParameters;
    private final File mapFile;
    private final MapGenerator mapGenerator;
    private final int mapGeneratorId;
    private transient double priority;
    public final Tile tile;
    
    public MapGeneratorJob(final Tile tile, final MapGenerator mapGenerator, final int mapGeneratorId, final File mapFile, final JobParameters jobParameters, final DebugSettings debugSettings) {
        this.tile = tile;
        this.mapGeneratorId = mapGeneratorId;
        this.mapGenerator = mapGenerator;
        this.mapFile = mapFile;
        this.jobParameters = jobParameters;
        this.debugSettings = debugSettings;
        this.calculateTransientValues();
    }
    
    private int calculateHashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.debugSettings == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.debugSettings.hashCode();
        }
        int hashCode3;
        if (this.jobParameters == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = this.jobParameters.hashCode();
        }
        int hashCode4;
        if (this.mapFile == null) {
            hashCode4 = 0;
        }
        else {
            hashCode4 = this.mapFile.hashCode();
        }
        if (this.tile != null) {
            hashCode = this.tile.hashCode();
        }
        return (((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    private void calculateTransientValues() {
        this.hashCodeValue = this.calculateHashCode();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.calculateTransientValues();
    }
    
    @Override
    public int compareTo(final MapGeneratorJob mapGeneratorJob) {
        int n;
        if (this.priority < mapGeneratorJob.priority) {
            n = -1;
        }
        else if (this.priority > mapGeneratorJob.priority) {
            n = 1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof MapGeneratorJob)) {
                b = false;
            }
            else {
                final MapGeneratorJob mapGeneratorJob = (MapGeneratorJob)o;
                if (this.debugSettings == null) {
                    if (mapGeneratorJob.debugSettings != null) {
                        b = false;
                        return b;
                    }
                }
                else if (!this.debugSettings.equals(mapGeneratorJob.debugSettings)) {
                    b = false;
                    return b;
                }
                if (this.jobParameters == null) {
                    if (mapGeneratorJob.jobParameters != null) {
                        b = false;
                        return b;
                    }
                }
                else if (!this.jobParameters.equals(mapGeneratorJob.jobParameters)) {
                    b = false;
                    return b;
                }
                if (this.mapGenerator == null) {
                    if (mapGeneratorJob.mapGenerator != null) {
                        b = false;
                        return b;
                    }
                }
                else {
                    if (mapGeneratorJob.mapGenerator == null) {
                        b = false;
                        return b;
                    }
                    if (this.mapGeneratorId != mapGeneratorJob.mapGeneratorId) {
                        b = false;
                        return b;
                    }
                    if (!this.mapGenerator.requiresInternetConnection()) {
                        if (this.mapFile == null) {
                            if (mapGeneratorJob.mapFile != null) {
                                b = false;
                                return b;
                            }
                        }
                        else if (!this.mapFile.equals(mapGeneratorJob.mapFile)) {
                            b = false;
                            return b;
                        }
                    }
                }
                if (this.tile == null) {
                    if (mapGeneratorJob.tile != null) {
                        b = false;
                    }
                }
                else if (!this.tile.equals(mapGeneratorJob.tile)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return this.hashCodeValue;
    }
    
    void setPriority(final double priority) {
        this.priority = priority;
    }
}
