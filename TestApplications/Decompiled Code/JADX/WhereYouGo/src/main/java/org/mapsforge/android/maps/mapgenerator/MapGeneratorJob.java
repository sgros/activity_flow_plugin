package org.mapsforge.android.maps.mapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.core.model.Tile;

public class MapGeneratorJob implements Comparable<MapGeneratorJob>, Serializable {
    private static final long serialVersionUID = 1;
    public final DebugSettings debugSettings;
    private transient int hashCodeValue;
    public final JobParameters jobParameters;
    private final File mapFile;
    private final MapGenerator mapGenerator;
    private final int mapGeneratorId;
    private transient double priority;
    public final Tile tile;

    public MapGeneratorJob(Tile tile, MapGenerator mapGenerator, int mapGeneratorId, File mapFile, JobParameters jobParameters, DebugSettings debugSettings) {
        this.tile = tile;
        this.mapGeneratorId = mapGeneratorId;
        this.mapGenerator = mapGenerator;
        this.mapFile = mapFile;
        this.jobParameters = jobParameters;
        this.debugSettings = debugSettings;
        calculateTransientValues();
    }

    public int compareTo(MapGeneratorJob otherMapGeneratorJob) {
        if (this.priority < otherMapGeneratorJob.priority) {
            return -1;
        }
        if (this.priority > otherMapGeneratorJob.priority) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapGeneratorJob)) {
            return false;
        }
        MapGeneratorJob other = (MapGeneratorJob) obj;
        if (this.debugSettings == null) {
            if (other.debugSettings != null) {
                return false;
            }
        } else if (!this.debugSettings.equals(other.debugSettings)) {
            return false;
        }
        if (this.jobParameters == null) {
            if (other.jobParameters != null) {
                return false;
            }
        } else if (!this.jobParameters.equals(other.jobParameters)) {
            return false;
        }
        if (this.mapGenerator == null) {
            if (other.mapGenerator != null) {
                return false;
            }
        } else if (other.mapGenerator == null) {
            return false;
        } else {
            if (this.mapGeneratorId != other.mapGeneratorId) {
                return false;
            }
            if (!this.mapGenerator.requiresInternetConnection()) {
                if (this.mapFile == null) {
                    if (other.mapFile != null) {
                        return false;
                    }
                } else if (!this.mapFile.equals(other.mapFile)) {
                    return false;
                }
            }
        }
        if (this.tile == null) {
            if (other.tile != null) {
                return false;
            }
            return true;
        } else if (this.tile.equals(other.tile)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    private int calculateHashCode() {
        int i = 0;
        int hashCode = ((((((this.debugSettings == null ? 0 : this.debugSettings.hashCode()) + 31) * 31) + (this.jobParameters == null ? 0 : this.jobParameters.hashCode())) * 31) + (this.mapFile == null ? 0 : this.mapFile.hashCode())) * 31;
        if (this.tile != null) {
            i = this.tile.hashCode();
        }
        return hashCode + i;
    }

    private void calculateTransientValues() {
        this.hashCodeValue = calculateHashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        calculateTransientValues();
    }

    /* Access modifiers changed, original: 0000 */
    public void setPriority(double priority) {
        this.priority = priority;
    }
}
