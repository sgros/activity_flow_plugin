package org.mapsforge.map.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileHeader;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.reader.header.SubFileParameter;

public class MapDatabase {
    private static final long BITMASK_INDEX_OFFSET = 549755813887L;
    private static final long BITMASK_INDEX_WATER = 549755813888L;
    private static final String DEBUG_SIGNATURE_BLOCK = "block signature: ";
    private static final String DEBUG_SIGNATURE_POI = "POI signature: ";
    private static final String DEBUG_SIGNATURE_WAY = "way signature: ";
    private static final int INDEX_CACHE_SIZE = 64;
    private static final String INVALID_FIRST_WAY_OFFSET = "invalid first way offset: ";
    private static final Logger LOGGER = Logger.getLogger(MapDatabase.class.getName());
    private static final int MAXIMUM_WAY_NODES_SEQUENCE_LENGTH = 8192;
    private static final int MAXIMUM_ZOOM_TABLE_OBJECTS = 65536;
    private static final int POI_FEATURE_ELEVATION = 32;
    private static final int POI_FEATURE_HOUSE_NUMBER = 64;
    private static final int POI_FEATURE_NAME = 128;
    private static final int POI_LAYER_BITMASK = 240;
    private static final int POI_LAYER_SHIFT = 4;
    private static final int POI_NUMBER_OF_TAGS_BITMASK = 15;
    private static final String READ_ONLY_MODE = "r";
    private static final byte SIGNATURE_LENGTH_BLOCK = (byte) 32;
    private static final byte SIGNATURE_LENGTH_POI = (byte) 32;
    private static final byte SIGNATURE_LENGTH_WAY = (byte) 32;
    private static final String TAG_KEY_ELE = "ele";
    private static final String TAG_KEY_HOUSE_NUMBER = "addr:housenumber";
    private static final String TAG_KEY_NAME = "name";
    private static final String TAG_KEY_REF = "ref";
    private static final int WAY_FEATURE_DATA_BLOCKS_BYTE = 8;
    private static final int WAY_FEATURE_DOUBLE_DELTA_ENCODING = 4;
    private static final int WAY_FEATURE_HOUSE_NUMBER = 64;
    private static final int WAY_FEATURE_LABEL_POSITION = 16;
    private static final int WAY_FEATURE_NAME = 128;
    private static final int WAY_FEATURE_REF = 32;
    private static final int WAY_LAYER_BITMASK = 240;
    private static final int WAY_LAYER_SHIFT = 4;
    private static final int WAY_NUMBER_OF_TAGS_BITMASK = 15;
    private IndexCache databaseIndexCache;
    private long fileSize;
    private RandomAccessFile inputFile;
    private MapFileHeader mapFileHeader;
    private ReadBuffer readBuffer;
    private String signatureBlock;
    private String signaturePoi;
    private String signatureWay;
    private double tileLatitude;
    private double tileLongitude;

    public void closeFile() {
        try {
            this.mapFileHeader = null;
            if (this.databaseIndexCache != null) {
                this.databaseIndexCache.destroy();
                this.databaseIndexCache = null;
            }
            if (this.inputFile != null) {
                this.inputFile.close();
                this.inputFile = null;
            }
            this.readBuffer = null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public MapFileInfo getMapFileInfo() {
        if (this.mapFileHeader != null) {
            return this.mapFileHeader.getMapFileInfo();
        }
        throw new IllegalStateException("no map file is currently opened");
    }

    public boolean hasOpenFile() {
        return this.inputFile != null;
    }

    public FileOpenResult openFile(File mapFile) {
        if (mapFile == null) {
            try {
                throw new IllegalArgumentException("mapFile must not be null");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, null, e);
                closeFile();
                return new FileOpenResult(e.getMessage());
            }
        }
        closeFile();
        if (!mapFile.exists()) {
            return new FileOpenResult("file does not exist: " + mapFile);
        }
        if (!mapFile.isFile()) {
            return new FileOpenResult("not a file: " + mapFile);
        }
        if (!mapFile.canRead()) {
            return new FileOpenResult("cannot read file: " + mapFile);
        }
        this.inputFile = new RandomAccessFile(mapFile, READ_ONLY_MODE);
        this.fileSize = this.inputFile.length();
        this.readBuffer = new ReadBuffer(this.inputFile);
        this.mapFileHeader = new MapFileHeader();
        FileOpenResult fileOpenResult = this.mapFileHeader.readHeader(this.readBuffer, this.fileSize);
        if (fileOpenResult.isSuccess()) {
            return FileOpenResult.SUCCESS;
        }
        closeFile();
        return fileOpenResult;
    }

    public MapReadResult readMapData(Tile tile) {
        try {
            prepareExecution();
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.queryZoomLevel = this.mapFileHeader.getQueryZoomLevel(tile.zoomLevel);
            SubFileParameter subFileParameter = this.mapFileHeader.getSubFileParameter(queryParameters.queryZoomLevel);
            if (subFileParameter == null) {
                LOGGER.warning("no sub-file for zoom level: " + queryParameters.queryZoomLevel);
                return null;
            }
            QueryCalculations.calculateBaseTiles(queryParameters, tile, subFileParameter);
            QueryCalculations.calculateBlocks(queryParameters, subFileParameter);
            return processBlocks(queryParameters, subFileParameter);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    private void decodeWayNodesDoubleDelta(GeoPoint[] waySegment) {
        double wayNodeLatitude = this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
        double wayNodeLongitude = this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
        waySegment[0] = new GeoPoint(wayNodeLatitude, wayNodeLongitude);
        double previousSingleDeltaLatitude = 0.0d;
        double previousSingleDeltaLongitude = 0.0d;
        for (int wayNodesIndex = 1; wayNodesIndex < waySegment.length; wayNodesIndex++) {
            double singleDeltaLatitude = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()) + previousSingleDeltaLatitude;
            double singleDeltaLongitude = CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()) + previousSingleDeltaLongitude;
            wayNodeLatitude += singleDeltaLatitude;
            wayNodeLongitude += singleDeltaLongitude;
            waySegment[wayNodesIndex] = new GeoPoint(wayNodeLatitude, wayNodeLongitude);
            previousSingleDeltaLatitude = singleDeltaLatitude;
            previousSingleDeltaLongitude = singleDeltaLongitude;
        }
    }

    private void decodeWayNodesSingleDelta(GeoPoint[] waySegment) {
        double wayNodeLatitude = this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
        double wayNodeLongitude = this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
        waySegment[0] = new GeoPoint(wayNodeLatitude, wayNodeLongitude);
        for (int wayNodesIndex = 1; wayNodesIndex < waySegment.length; wayNodesIndex++) {
            wayNodeLatitude += CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
            wayNodeLongitude += CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
            waySegment[wayNodesIndex] = new GeoPoint(wayNodeLatitude, wayNodeLongitude);
        }
    }

    private void logDebugSignatures() {
        if (this.mapFileHeader.getMapFileInfo().debugFile) {
            LOGGER.warning(DEBUG_SIGNATURE_WAY + this.signatureWay);
            LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
        }
    }

    private void prepareExecution() {
        if (this.databaseIndexCache == null) {
            this.databaseIndexCache = new IndexCache(this.inputFile, 64);
        }
    }

    private PoiWayBundle processBlock(QueryParameters queryParameters, SubFileParameter subFileParameter) {
        if (!processBlockSignature()) {
            return null;
        }
        int[][] zoomTable = readZoomTable(subFileParameter);
        if (zoomTable == null) {
            return null;
        }
        int zoomTableRow = queryParameters.queryZoomLevel - subFileParameter.zoomLevelMin;
        int poisOnQueryZoomLevel = zoomTable[zoomTableRow][0];
        int waysOnQueryZoomLevel = zoomTable[zoomTableRow][1];
        int firstWayOffset = this.readBuffer.readUnsignedInt();
        if (firstWayOffset < 0) {
            LOGGER.warning(INVALID_FIRST_WAY_OFFSET + firstWayOffset);
            if (!this.mapFileHeader.getMapFileInfo().debugFile) {
                return null;
            }
            LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
            return null;
        }
        firstWayOffset += this.readBuffer.getBufferPosition();
        if (firstWayOffset > this.readBuffer.getBufferSize()) {
            LOGGER.warning(INVALID_FIRST_WAY_OFFSET + firstWayOffset);
            if (!this.mapFileHeader.getMapFileInfo().debugFile) {
                return null;
            }
            LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
            return null;
        }
        List<PointOfInterest> pois = processPOIs(poisOnQueryZoomLevel);
        if (pois == null) {
            return null;
        }
        if (this.readBuffer.getBufferPosition() > firstWayOffset) {
            LOGGER.warning("invalid buffer position: " + this.readBuffer.getBufferPosition());
            if (!this.mapFileHeader.getMapFileInfo().debugFile) {
                return null;
            }
            LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
            return null;
        }
        this.readBuffer.setBufferPosition(firstWayOffset);
        List<Way> ways = processWays(queryParameters, waysOnQueryZoomLevel);
        if (ways != null) {
            return new PoiWayBundle(pois, ways);
        }
        return null;
    }

    private MapReadResult processBlocks(QueryParameters queryParameters, SubFileParameter subFileParameter) throws IOException {
        boolean queryIsWater = true;
        boolean queryReadWaterInfo = false;
        MapReadResultBuilder mapReadResultBuilder = new MapReadResultBuilder();
        for (long row = queryParameters.fromBlockY; row <= queryParameters.toBlockY; row++) {
            for (long column = queryParameters.fromBlockX; column <= queryParameters.toBlockX; column++) {
                long blockNumber = (subFileParameter.blocksWidth * row) + column;
                long currentBlockIndexEntry = this.databaseIndexCache.getIndexEntry(subFileParameter, blockNumber);
                if (queryIsWater) {
                    queryIsWater &= (BITMASK_INDEX_WATER & currentBlockIndexEntry) != 0 ? 1 : 0;
                    queryReadWaterInfo = true;
                }
                long currentBlockPointer = currentBlockIndexEntry & BITMASK_INDEX_OFFSET;
                if (currentBlockPointer < 1 || currentBlockPointer > subFileParameter.subFileSize) {
                    LOGGER.warning("invalid current block pointer: " + currentBlockPointer);
                    LOGGER.warning("subFileSize: " + subFileParameter.subFileSize);
                    return null;
                }
                long nextBlockPointer;
                if (1 + blockNumber == subFileParameter.numberOfBlocks) {
                    nextBlockPointer = subFileParameter.subFileSize;
                } else {
                    nextBlockPointer = this.databaseIndexCache.getIndexEntry(subFileParameter, 1 + blockNumber) & BITMASK_INDEX_OFFSET;
                    if (nextBlockPointer > subFileParameter.subFileSize) {
                        LOGGER.warning("invalid next block pointer: " + nextBlockPointer);
                        LOGGER.warning("sub-file size: " + subFileParameter.subFileSize);
                        return null;
                    }
                }
                int currentBlockSize = (int) (nextBlockPointer - currentBlockPointer);
                if (currentBlockSize < 0) {
                    LOGGER.warning("current block size must not be negative: " + currentBlockSize);
                    return null;
                }
                if (currentBlockSize != 0) {
                    if (currentBlockSize > 2500000) {
                        LOGGER.warning("current block size too large: " + currentBlockSize);
                    } else if (((long) currentBlockSize) + currentBlockPointer > this.fileSize) {
                        LOGGER.warning("current block largher than file size: " + currentBlockSize);
                        return null;
                    } else {
                        this.inputFile.seek(subFileParameter.startAddress + currentBlockPointer);
                        if (this.readBuffer.readFromFile(currentBlockSize)) {
                            this.tileLatitude = MercatorProjection.tileYToLatitude(subFileParameter.boundaryTileTop + row, subFileParameter.baseZoomLevel);
                            this.tileLongitude = MercatorProjection.tileXToLongitude(subFileParameter.boundaryTileLeft + column, subFileParameter.baseZoomLevel);
                            try {
                                PoiWayBundle poiWayBundle = processBlock(queryParameters, subFileParameter);
                                if (poiWayBundle != null) {
                                    mapReadResultBuilder.add(poiWayBundle);
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                LOGGER.log(Level.SEVERE, null, e);
                            }
                        } else {
                            LOGGER.warning("reading current block has failed: " + currentBlockSize);
                            return null;
                        }
                    }
                }
            }
        }
        if (queryIsWater && queryReadWaterInfo) {
            mapReadResultBuilder.isWater = true;
        }
        return mapReadResultBuilder.build();
    }

    private boolean processBlockSignature() {
        if (this.mapFileHeader.getMapFileInfo().debugFile) {
            this.signatureBlock = this.readBuffer.readUTF8EncodedString(32);
            if (!this.signatureBlock.startsWith("###TileStart")) {
                LOGGER.warning("invalid block signature: " + this.signatureBlock);
                return false;
            }
        }
        return true;
    }

    private List<PointOfInterest> processPOIs(int numberOfPois) {
        List<PointOfInterest> pois = new ArrayList();
        Tag[] poiTags = this.mapFileHeader.getMapFileInfo().poiTags;
        for (int elementCounter = numberOfPois; elementCounter != 0; elementCounter--) {
            if (this.mapFileHeader.getMapFileInfo().debugFile) {
                this.signaturePoi = this.readBuffer.readUTF8EncodedString(32);
                if (!this.signaturePoi.startsWith("***POIStart")) {
                    LOGGER.warning("invalid POI signature: " + this.signaturePoi);
                    LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
                    return null;
                }
            }
            double latitude = this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
            double longitude = this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt());
            byte specialByte = this.readBuffer.readByte();
            byte layer = (byte) ((specialByte & 240) >>> 4);
            byte numberOfTags = (byte) (specialByte & 15);
            List<Tag> tags = new ArrayList();
            for (byte tagIndex = numberOfTags; tagIndex != (byte) 0; tagIndex = (byte) (tagIndex - 1)) {
                int tagId = this.readBuffer.readUnsignedInt();
                if (tagId < 0 || tagId >= poiTags.length) {
                    LOGGER.warning("invalid POI tag ID: " + tagId);
                    if (this.mapFileHeader.getMapFileInfo().debugFile) {
                        LOGGER.warning(DEBUG_SIGNATURE_POI + this.signaturePoi);
                        LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
                    }
                    return null;
                }
                tags.add(poiTags[tagId]);
            }
            byte featureByte = this.readBuffer.readByte();
            boolean featureName = (featureByte & 128) != 0;
            boolean featureHouseNumber = (featureByte & 64) != 0;
            boolean featureElevation = (featureByte & 32) != 0;
            if (featureName) {
                tags.add(new Tag(TAG_KEY_NAME, this.readBuffer.readUTF8EncodedString()));
            }
            if (featureHouseNumber) {
                tags.add(new Tag(TAG_KEY_HOUSE_NUMBER, this.readBuffer.readUTF8EncodedString()));
            }
            if (featureElevation) {
                tags.add(new Tag(TAG_KEY_ELE, Integer.toString(this.readBuffer.readSignedInt())));
            }
            pois.add(new PointOfInterest(layer, tags, new GeoPoint(latitude, longitude)));
        }
        return pois;
    }

    private GeoPoint[][] processWayDataBlock(boolean doubleDeltaEncoding) {
        int numberOfWayCoordinateBlocks = this.readBuffer.readUnsignedInt();
        if (numberOfWayCoordinateBlocks < 1 || numberOfWayCoordinateBlocks > 32767) {
            LOGGER.warning("invalid number of way coordinate blocks: " + numberOfWayCoordinateBlocks);
            logDebugSignatures();
            return (GeoPoint[][]) null;
        }
        GeoPoint[][] wayCoordinates = new GeoPoint[numberOfWayCoordinateBlocks][];
        for (int coordinateBlock = 0; coordinateBlock < numberOfWayCoordinateBlocks; coordinateBlock++) {
            int numberOfWayNodes = this.readBuffer.readUnsignedInt();
            if (numberOfWayNodes < 2 || numberOfWayNodes > 8192) {
                LOGGER.warning("invalid number of way nodes: " + numberOfWayNodes);
                logDebugSignatures();
                return (GeoPoint[][]) null;
            }
            GeoPoint[] waySegment = new GeoPoint[numberOfWayNodes];
            if (doubleDeltaEncoding) {
                decodeWayNodesDoubleDelta(waySegment);
            } else {
                decodeWayNodesSingleDelta(waySegment);
            }
            wayCoordinates[coordinateBlock] = waySegment;
        }
        return wayCoordinates;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0232  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0235  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0198  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0238  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x023b  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0241  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x01ac  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x01af  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x01e3  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0244  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x020f A:{SYNTHETIC} */
    private java.util.List<org.mapsforge.map.reader.Way> processWays(org.mapsforge.map.reader.QueryParameters r29, int r30) {
        /*
        r28 = this;
        r24 = new java.util.ArrayList;
        r24.<init>();
        r0 = r28;
        r0 = r0.mapFileHeader;
        r25 = r0;
        r25 = r25.getMapFileInfo();
        r0 = r25;
        r0 = r0.wayTags;
        r23 = r0;
        r3 = r30;
    L_0x0017:
        if (r3 == 0) goto L_0x0089;
    L_0x0019:
        r0 = r28;
        r0 = r0.mapFileHeader;
        r25 = r0;
        r25 = r25.getMapFileInfo();
        r0 = r25;
        r0 = r0.debugFile;
        r25 = r0;
        if (r25 == 0) goto L_0x008a;
    L_0x002b:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r26 = 32;
        r25 = r25.readUTF8EncodedString(r26);
        r0 = r25;
        r1 = r28;
        r1.signatureWay = r0;
        r0 = r28;
        r0 = r0.signatureWay;
        r25 = r0;
        r26 = "---WayStart";
        r25 = r25.startsWith(r26);
        if (r25 != 0) goto L_0x008a;
    L_0x004b:
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "invalid way signature: ";
        r26 = r26.append(r27);
        r0 = r28;
        r0 = r0.signatureWay;
        r27 = r0;
        r26 = r26.append(r27);
        r26 = r26.toString();
        r25.warning(r26);
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "block signature: ";
        r26 = r26.append(r27);
        r0 = r28;
        r0 = r0.signatureBlock;
        r27 = r0;
        r26 = r26.append(r27);
        r26 = r26.toString();
        r25.warning(r26);
        r24 = 0;
    L_0x0089:
        return r24;
    L_0x008a:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r21 = r25.readUnsignedInt();
        if (r21 >= 0) goto L_0x00e5;
    L_0x0096:
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "invalid way data size: ";
        r26 = r26.append(r27);
        r0 = r26;
        r1 = r21;
        r26 = r0.append(r1);
        r26 = r26.toString();
        r25.warning(r26);
        r0 = r28;
        r0 = r0.mapFileHeader;
        r25 = r0;
        r25 = r25.getMapFileInfo();
        r0 = r25;
        r0 = r0.debugFile;
        r25 = r0;
        if (r25 == 0) goto L_0x00e2;
    L_0x00c4:
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "block signature: ";
        r26 = r26.append(r27);
        r0 = r28;
        r0 = r0.signatureBlock;
        r27 = r0;
        r26 = r26.append(r27);
        r26 = r26.toString();
        r25.warning(r26);
    L_0x00e2:
        r24 = 0;
        goto L_0x0089;
    L_0x00e5:
        r0 = r29;
        r0 = r0.useTileBitmask;
        r25 = r0;
        if (r25 == 0) goto L_0x0110;
    L_0x00ed:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r18 = r25.readShort();
        r0 = r29;
        r0 = r0.queryTileBitmask;
        r25 = r0;
        r25 = r25 & r18;
        if (r25 != 0) goto L_0x011b;
    L_0x0101:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r26 = r21 + -2;
        r25.skipBytes(r26);
    L_0x010c:
        r3 = r3 + -1;
        goto L_0x0017;
    L_0x0110:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r26 = 2;
        r25.skipBytes(r26);
    L_0x011b:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r14 = r25.readByte();
        r0 = r14 & 240;
        r25 = r0;
        r25 = r25 >>> 4;
        r0 = r25;
        r12 = (byte) r0;
        r25 = r14 & 15;
        r0 = r25;
        r13 = (byte) r0;
        r17 = new java.util.ArrayList;
        r17.<init>();
        r16 = r13;
    L_0x013a:
        if (r16 == 0) goto L_0x0183;
    L_0x013c:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r15 = r25.readUnsignedInt();
        if (r15 < 0) goto L_0x0151;
    L_0x0148:
        r0 = r23;
        r0 = r0.length;
        r25 = r0;
        r0 = r25;
        if (r15 < r0) goto L_0x0172;
    L_0x0151:
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "invalid way tag ID: ";
        r26 = r26.append(r27);
        r0 = r26;
        r26 = r0.append(r15);
        r26 = r26.toString();
        r25.warning(r26);
        r28.logDebugSignatures();
        r24 = 0;
        goto L_0x0089;
    L_0x0172:
        r25 = r23[r15];
        r0 = r17;
        r1 = r25;
        r0.add(r1);
        r25 = r16 + -1;
        r0 = r25;
        r0 = (byte) r0;
        r16 = r0;
        goto L_0x013a;
    L_0x0183:
        r0 = r28;
        r0 = r0.readBuffer;
        r25 = r0;
        r4 = r25.readByte();
        r0 = r4 & 128;
        r25 = r0;
        if (r25 == 0) goto L_0x0232;
    L_0x0193:
        r7 = 1;
    L_0x0194:
        r25 = r4 & 64;
        if (r25 == 0) goto L_0x0235;
    L_0x0198:
        r5 = 1;
    L_0x0199:
        r25 = r4 & 32;
        if (r25 == 0) goto L_0x0238;
    L_0x019d:
        r8 = 1;
    L_0x019e:
        r25 = r4 & 16;
        if (r25 == 0) goto L_0x023b;
    L_0x01a2:
        r6 = 1;
    L_0x01a3:
        r25 = r4 & 8;
        if (r25 == 0) goto L_0x023e;
    L_0x01a7:
        r9 = 1;
    L_0x01a8:
        r25 = r4 & 4;
        if (r25 == 0) goto L_0x0241;
    L_0x01ac:
        r10 = 1;
    L_0x01ad:
        if (r7 == 0) goto L_0x01c7;
    L_0x01af:
        r25 = new org.mapsforge.core.model.Tag;
        r26 = "name";
        r0 = r28;
        r0 = r0.readBuffer;
        r27 = r0;
        r27 = r27.readUTF8EncodedString();
        r25.<init>(r26, r27);
        r0 = r17;
        r1 = r25;
        r0.add(r1);
    L_0x01c7:
        if (r5 == 0) goto L_0x01e1;
    L_0x01c9:
        r25 = new org.mapsforge.core.model.Tag;
        r26 = "addr:housenumber";
        r0 = r28;
        r0 = r0.readBuffer;
        r27 = r0;
        r27 = r27.readUTF8EncodedString();
        r25.<init>(r26, r27);
        r0 = r17;
        r1 = r25;
        r0.add(r1);
    L_0x01e1:
        if (r8 == 0) goto L_0x01fb;
    L_0x01e3:
        r25 = new org.mapsforge.core.model.Tag;
        r26 = "ref";
        r0 = r28;
        r0 = r0.readBuffer;
        r27 = r0;
        r27 = r27.readUTF8EncodedString();
        r25.<init>(r26, r27);
        r0 = r17;
        r1 = r25;
        r0.add(r1);
    L_0x01fb:
        r0 = r28;
        r11 = r0.readOptionalLabelPosition(r6);
        r0 = r28;
        r20 = r0.readOptionalWayDataBlocksByte(r9);
        r25 = 1;
        r0 = r20;
        r1 = r25;
        if (r0 >= r1) goto L_0x0244;
    L_0x020f:
        r25 = LOGGER;
        r26 = new java.lang.StringBuilder;
        r26.<init>();
        r27 = "invalid number of way data blocks: ";
        r26 = r26.append(r27);
        r0 = r26;
        r1 = r20;
        r26 = r0.append(r1);
        r26 = r26.toString();
        r25.warning(r26);
        r28.logDebugSignatures();
        r24 = 0;
        goto L_0x0089;
    L_0x0232:
        r7 = 0;
        goto L_0x0194;
    L_0x0235:
        r5 = 0;
        goto L_0x0199;
    L_0x0238:
        r8 = 0;
        goto L_0x019e;
    L_0x023b:
        r6 = 0;
        goto L_0x01a3;
    L_0x023e:
        r9 = 0;
        goto L_0x01a8;
    L_0x0241:
        r10 = 0;
        goto L_0x01ad;
    L_0x0244:
        r19 = 0;
    L_0x0246:
        r0 = r19;
        r1 = r20;
        if (r0 >= r1) goto L_0x010c;
    L_0x024c:
        r0 = r28;
        r22 = r0.processWayDataBlock(r10);
        if (r22 != 0) goto L_0x0258;
    L_0x0254:
        r24 = 0;
        goto L_0x0089;
    L_0x0258:
        r25 = new org.mapsforge.map.reader.Way;
        r0 = r25;
        r1 = r17;
        r2 = r22;
        r0.<init>(r12, r1, r2, r11);
        r24.add(r25);
        r19 = r19 + 1;
        goto L_0x0246;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.reader.MapDatabase.processWays(org.mapsforge.map.reader.QueryParameters, int):java.util.List");
    }

    private GeoPoint readOptionalLabelPosition(boolean featureLabelPosition) {
        if (featureLabelPosition) {
            return new GeoPoint(this.tileLatitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()), this.tileLongitude + CoordinatesUtil.microdegreesToDegrees(this.readBuffer.readSignedInt()));
        }
        return null;
    }

    private int readOptionalWayDataBlocksByte(boolean featureWayDataBlocksByte) {
        if (featureWayDataBlocksByte) {
            return this.readBuffer.readUnsignedInt();
        }
        return 1;
    }

    private int[][] readZoomTable(SubFileParameter subFileParameter) {
        int rows = (subFileParameter.zoomLevelMax - subFileParameter.zoomLevelMin) + 1;
        int[][] zoomTable = (int[][]) Array.newInstance(Integer.TYPE, new int[]{rows, 2});
        int cumulatedNumberOfPois = 0;
        int cumulatedNumberOfWays = 0;
        int row = 0;
        while (row < rows) {
            cumulatedNumberOfPois += this.readBuffer.readUnsignedInt();
            cumulatedNumberOfWays += this.readBuffer.readUnsignedInt();
            if (cumulatedNumberOfPois < 0 || cumulatedNumberOfPois > 65536) {
                LOGGER.warning("invalid cumulated number of POIs in row " + row + ' ' + cumulatedNumberOfPois);
                if (this.mapFileHeader.getMapFileInfo().debugFile) {
                    LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
                }
                return (int[][]) null;
            } else if (cumulatedNumberOfWays < 0 || cumulatedNumberOfWays > 65536) {
                LOGGER.warning("invalid cumulated number of ways in row " + row + ' ' + cumulatedNumberOfWays);
                if (this.mapFileHeader.getMapFileInfo().debugFile) {
                    LOGGER.warning(DEBUG_SIGNATURE_BLOCK + this.signatureBlock);
                }
                return (int[][]) null;
            } else {
                zoomTable[row][0] = cumulatedNumberOfPois;
                zoomTable[row][1] = cumulatedNumberOfWays;
                row++;
            }
        }
        return zoomTable;
    }
}
