// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Descriptor(objectTypeIndication = 64, tags = { 5 })
public class AudioSpecificConfig extends BaseDescriptor
{
    public static Map<Integer, String> audioObjectTypeMap;
    public static Map<Integer, Integer> samplingFrequencyIndexMap;
    public boolean aacScalefactorDataResilienceFlag;
    public boolean aacSectionDataResilienceFlag;
    public boolean aacSpectralDataResilienceFlag;
    public int audioObjectType;
    public int channelConfiguration;
    byte[] configBytes;
    public int coreCoderDelay;
    public int dependsOnCoreCoder;
    public int directMapping;
    public ELDSpecificConfig eldSpecificConfig;
    public int epConfig;
    public int erHvxcExtensionFlag;
    public int extensionAudioObjectType;
    public int extensionChannelConfiguration;
    public int extensionFlag;
    public int extensionFlag3;
    public int extensionSamplingFrequency;
    public int extensionSamplingFrequencyIndex;
    public int fillBits;
    public int frameLengthFlag;
    public boolean gaSpecificConfig;
    public int hilnContMode;
    public int hilnEnhaLayer;
    public int hilnEnhaQuantMode;
    public int hilnFrameLength;
    public int hilnMaxNumLine;
    public int hilnQuantMode;
    public int hilnSampleRateCode;
    public int hvxcRateMode;
    public int hvxcVarMode;
    public int isBaseLayer;
    public int layerNr;
    public int layer_length;
    public int numOfSubFrame;
    public int paraExtensionFlag;
    public int paraMode;
    public boolean parametricSpecificConfig;
    public boolean psPresentFlag;
    public int sacPayloadEmbedding;
    public int samplingFrequency;
    public int samplingFrequencyIndex;
    public boolean sbrPresentFlag;
    public int syncExtensionType;
    public int var_ScalableFlag;
    
    static {
        AudioSpecificConfig.samplingFrequencyIndexMap = new HashMap<Integer, Integer>();
        AudioSpecificConfig.audioObjectTypeMap = new HashMap<Integer, String>();
        AudioSpecificConfig.samplingFrequencyIndexMap.put(0, 96000);
        final Map<Integer, Integer> samplingFrequencyIndexMap = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value = 1;
        samplingFrequencyIndexMap.put(value, 88200);
        final Map<Integer, Integer> samplingFrequencyIndexMap2 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value2 = 2;
        samplingFrequencyIndexMap2.put(value2, 64000);
        final Map<Integer, Integer> samplingFrequencyIndexMap3 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value3 = 3;
        samplingFrequencyIndexMap3.put(value3, 48000);
        final Map<Integer, Integer> samplingFrequencyIndexMap4 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value4 = 4;
        samplingFrequencyIndexMap4.put(value4, 44100);
        final Map<Integer, Integer> samplingFrequencyIndexMap5 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value5 = 5;
        samplingFrequencyIndexMap5.put(value5, 32000);
        final Map<Integer, Integer> samplingFrequencyIndexMap6 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value6 = 6;
        samplingFrequencyIndexMap6.put(value6, 24000);
        final Map<Integer, Integer> samplingFrequencyIndexMap7 = AudioSpecificConfig.samplingFrequencyIndexMap;
        final Integer value7 = 7;
        samplingFrequencyIndexMap7.put(value7, 22050);
        AudioSpecificConfig.samplingFrequencyIndexMap.put(8, 16000);
        AudioSpecificConfig.samplingFrequencyIndexMap.put(9, 12000);
        AudioSpecificConfig.samplingFrequencyIndexMap.put(10, 11025);
        AudioSpecificConfig.samplingFrequencyIndexMap.put(11, 8000);
        AudioSpecificConfig.audioObjectTypeMap.put(value, "AAC main");
        AudioSpecificConfig.audioObjectTypeMap.put(value2, "AAC LC");
        AudioSpecificConfig.audioObjectTypeMap.put(value3, "AAC SSR");
        AudioSpecificConfig.audioObjectTypeMap.put(value4, "AAC LTP");
        AudioSpecificConfig.audioObjectTypeMap.put(value5, "SBR");
        AudioSpecificConfig.audioObjectTypeMap.put(value6, "AAC Scalable");
        AudioSpecificConfig.audioObjectTypeMap.put(value7, "TwinVQ");
        AudioSpecificConfig.audioObjectTypeMap.put(8, "CELP");
        AudioSpecificConfig.audioObjectTypeMap.put(9, "HVXC");
        AudioSpecificConfig.audioObjectTypeMap.put(10, "(reserved)");
        AudioSpecificConfig.audioObjectTypeMap.put(11, "(reserved)");
        AudioSpecificConfig.audioObjectTypeMap.put(12, "TTSI");
        AudioSpecificConfig.audioObjectTypeMap.put(13, "Main synthetic");
        AudioSpecificConfig.audioObjectTypeMap.put(14, "Wavetable synthesis");
        AudioSpecificConfig.audioObjectTypeMap.put(15, "General MIDI");
        AudioSpecificConfig.audioObjectTypeMap.put(16, "Algorithmic Synthesis and Audio FX");
        AudioSpecificConfig.audioObjectTypeMap.put(17, "ER AAC LC");
        AudioSpecificConfig.audioObjectTypeMap.put(18, "(reserved)");
        AudioSpecificConfig.audioObjectTypeMap.put(19, "ER AAC LTP");
        AudioSpecificConfig.audioObjectTypeMap.put(20, "ER AAC Scalable");
        AudioSpecificConfig.audioObjectTypeMap.put(21, "ER TwinVQ");
        AudioSpecificConfig.audioObjectTypeMap.put(22, "ER BSAC");
        AudioSpecificConfig.audioObjectTypeMap.put(23, "ER AAC LD");
        AudioSpecificConfig.audioObjectTypeMap.put(24, "ER CELP");
        AudioSpecificConfig.audioObjectTypeMap.put(25, "ER HVXC");
        AudioSpecificConfig.audioObjectTypeMap.put(26, "ER HILN");
        AudioSpecificConfig.audioObjectTypeMap.put(27, "ER Parametric");
        AudioSpecificConfig.audioObjectTypeMap.put(28, "SSC");
        AudioSpecificConfig.audioObjectTypeMap.put(29, "PS");
        AudioSpecificConfig.audioObjectTypeMap.put(30, "MPEG Surround");
        AudioSpecificConfig.audioObjectTypeMap.put(31, "(escape)");
        AudioSpecificConfig.audioObjectTypeMap.put(32, "Layer-1");
        AudioSpecificConfig.audioObjectTypeMap.put(33, "Layer-2");
        AudioSpecificConfig.audioObjectTypeMap.put(34, "Layer-3");
        AudioSpecificConfig.audioObjectTypeMap.put(35, "DST");
        AudioSpecificConfig.audioObjectTypeMap.put(36, "ALS");
        AudioSpecificConfig.audioObjectTypeMap.put(37, "SLS");
        AudioSpecificConfig.audioObjectTypeMap.put(38, "SLS non-core");
        AudioSpecificConfig.audioObjectTypeMap.put(39, "ER AAC ELD");
        AudioSpecificConfig.audioObjectTypeMap.put(40, "SMR Simple");
        AudioSpecificConfig.audioObjectTypeMap.put(41, "SMR Main");
    }
    
    private int gaSpecificConfigSize() {
        return 0;
    }
    
    private int getAudioObjectType(final BitReaderBuffer bitReaderBuffer) throws IOException {
        int bits;
        if ((bits = bitReaderBuffer.readBits(5)) == 31) {
            bits = bitReaderBuffer.readBits(6) + 32;
        }
        return bits;
    }
    
    private void parseErHvxcConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.hvxcVarMode = bitReaderBuffer.readBits(1);
        this.hvxcRateMode = bitReaderBuffer.readBits(2);
        this.erHvxcExtensionFlag = bitReaderBuffer.readBits(1);
        if (this.erHvxcExtensionFlag == 1) {
            this.var_ScalableFlag = bitReaderBuffer.readBits(1);
        }
    }
    
    private void parseGaSpecificConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.frameLengthFlag = bitReaderBuffer.readBits(1);
        this.dependsOnCoreCoder = bitReaderBuffer.readBits(1);
        if (this.dependsOnCoreCoder == 1) {
            this.coreCoderDelay = bitReaderBuffer.readBits(14);
        }
        this.extensionFlag = bitReaderBuffer.readBits(1);
        if (n2 != 0) {
            if (n3 == 6 || n3 == 20) {
                this.layerNr = bitReaderBuffer.readBits(3);
            }
            if (this.extensionFlag == 1) {
                if (n3 == 22) {
                    this.numOfSubFrame = bitReaderBuffer.readBits(5);
                    this.layer_length = bitReaderBuffer.readBits(11);
                }
                if (n3 == 17 || n3 == 19 || n3 == 20 || n3 == 23) {
                    this.aacSectionDataResilienceFlag = bitReaderBuffer.readBool();
                    this.aacScalefactorDataResilienceFlag = bitReaderBuffer.readBool();
                    this.aacSpectralDataResilienceFlag = bitReaderBuffer.readBool();
                }
                this.extensionFlag3 = bitReaderBuffer.readBits(1);
            }
            this.gaSpecificConfig = true;
            return;
        }
        throw new UnsupportedOperationException("can't parse program_config_element yet");
    }
    
    private void parseHilnConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.hilnQuantMode = bitReaderBuffer.readBits(1);
        this.hilnMaxNumLine = bitReaderBuffer.readBits(8);
        this.hilnSampleRateCode = bitReaderBuffer.readBits(4);
        this.hilnFrameLength = bitReaderBuffer.readBits(12);
        this.hilnContMode = bitReaderBuffer.readBits(2);
    }
    
    private void parseHilnEnexConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.hilnEnhaLayer = bitReaderBuffer.readBits(1);
        if (this.hilnEnhaLayer == 1) {
            this.hilnEnhaQuantMode = bitReaderBuffer.readBits(2);
        }
    }
    
    private void parseParaConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.paraMode = bitReaderBuffer.readBits(2);
        if (this.paraMode != 1) {
            this.parseErHvxcConfig(n, n2, n3, bitReaderBuffer);
        }
        if (this.paraMode != 0) {
            this.parseHilnConfig(n, n2, n3, bitReaderBuffer);
        }
        this.paraExtensionFlag = bitReaderBuffer.readBits(1);
        this.parametricSpecificConfig = true;
    }
    
    private void parseParametricSpecificConfig(final int n, final int n2, final int n3, final BitReaderBuffer bitReaderBuffer) throws IOException {
        this.isBaseLayer = bitReaderBuffer.readBits(1);
        if (this.isBaseLayer == 1) {
            this.parseParaConfig(n, n2, n3, bitReaderBuffer);
        }
        else {
            this.parseHilnEnexConfig(n, n2, n3, bitReaderBuffer);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && AudioSpecificConfig.class == o.getClass()) {
            final AudioSpecificConfig audioSpecificConfig = (AudioSpecificConfig)o;
            return this.aacScalefactorDataResilienceFlag == audioSpecificConfig.aacScalefactorDataResilienceFlag && this.aacSectionDataResilienceFlag == audioSpecificConfig.aacSectionDataResilienceFlag && this.aacSpectralDataResilienceFlag == audioSpecificConfig.aacSpectralDataResilienceFlag && this.audioObjectType == audioSpecificConfig.audioObjectType && this.channelConfiguration == audioSpecificConfig.channelConfiguration && this.coreCoderDelay == audioSpecificConfig.coreCoderDelay && this.dependsOnCoreCoder == audioSpecificConfig.dependsOnCoreCoder && this.directMapping == audioSpecificConfig.directMapping && this.epConfig == audioSpecificConfig.epConfig && this.erHvxcExtensionFlag == audioSpecificConfig.erHvxcExtensionFlag && this.extensionAudioObjectType == audioSpecificConfig.extensionAudioObjectType && this.extensionChannelConfiguration == audioSpecificConfig.extensionChannelConfiguration && this.extensionFlag == audioSpecificConfig.extensionFlag && this.extensionFlag3 == audioSpecificConfig.extensionFlag3 && this.extensionSamplingFrequency == audioSpecificConfig.extensionSamplingFrequency && this.extensionSamplingFrequencyIndex == audioSpecificConfig.extensionSamplingFrequencyIndex && this.fillBits == audioSpecificConfig.fillBits && this.frameLengthFlag == audioSpecificConfig.frameLengthFlag && this.gaSpecificConfig == audioSpecificConfig.gaSpecificConfig && this.hilnContMode == audioSpecificConfig.hilnContMode && this.hilnEnhaLayer == audioSpecificConfig.hilnEnhaLayer && this.hilnEnhaQuantMode == audioSpecificConfig.hilnEnhaQuantMode && this.hilnFrameLength == audioSpecificConfig.hilnFrameLength && this.hilnMaxNumLine == audioSpecificConfig.hilnMaxNumLine && this.hilnQuantMode == audioSpecificConfig.hilnQuantMode && this.hilnSampleRateCode == audioSpecificConfig.hilnSampleRateCode && this.hvxcRateMode == audioSpecificConfig.hvxcRateMode && this.hvxcVarMode == audioSpecificConfig.hvxcVarMode && this.isBaseLayer == audioSpecificConfig.isBaseLayer && this.layerNr == audioSpecificConfig.layerNr && this.layer_length == audioSpecificConfig.layer_length && this.numOfSubFrame == audioSpecificConfig.numOfSubFrame && this.paraExtensionFlag == audioSpecificConfig.paraExtensionFlag && this.paraMode == audioSpecificConfig.paraMode && this.parametricSpecificConfig == audioSpecificConfig.parametricSpecificConfig && this.psPresentFlag == audioSpecificConfig.psPresentFlag && this.sacPayloadEmbedding == audioSpecificConfig.sacPayloadEmbedding && this.samplingFrequency == audioSpecificConfig.samplingFrequency && this.samplingFrequencyIndex == audioSpecificConfig.samplingFrequencyIndex && this.sbrPresentFlag == audioSpecificConfig.sbrPresentFlag && this.syncExtensionType == audioSpecificConfig.syncExtensionType && this.var_ScalableFlag == audioSpecificConfig.var_ScalableFlag && Arrays.equals(this.configBytes, audioSpecificConfig.configBytes);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final byte[] configBytes = this.configBytes;
        int hashCode;
        if (configBytes != null) {
            hashCode = Arrays.hashCode(configBytes);
        }
        else {
            hashCode = 0;
        }
        return (((((((((((((((((((((((((((((((((((((((((hashCode * 31 + this.audioObjectType) * 31 + this.samplingFrequencyIndex) * 31 + this.samplingFrequency) * 31 + this.channelConfiguration) * 31 + this.extensionAudioObjectType) * 31 + (this.sbrPresentFlag ? 1 : 0)) * 31 + (this.psPresentFlag ? 1 : 0)) * 31 + this.extensionSamplingFrequencyIndex) * 31 + this.extensionSamplingFrequency) * 31 + this.extensionChannelConfiguration) * 31 + this.sacPayloadEmbedding) * 31 + this.fillBits) * 31 + this.epConfig) * 31 + this.directMapping) * 31 + this.syncExtensionType) * 31 + this.frameLengthFlag) * 31 + this.dependsOnCoreCoder) * 31 + this.coreCoderDelay) * 31 + this.extensionFlag) * 31 + this.layerNr) * 31 + this.numOfSubFrame) * 31 + this.layer_length) * 31 + (this.aacSectionDataResilienceFlag ? 1 : 0)) * 31 + (this.aacScalefactorDataResilienceFlag ? 1 : 0)) * 31 + (this.aacSpectralDataResilienceFlag ? 1 : 0)) * 31 + this.extensionFlag3) * 31 + (this.gaSpecificConfig ? 1 : 0)) * 31 + this.isBaseLayer) * 31 + this.paraMode) * 31 + this.paraExtensionFlag) * 31 + this.hvxcVarMode) * 31 + this.hvxcRateMode) * 31 + this.erHvxcExtensionFlag) * 31 + this.var_ScalableFlag) * 31 + this.hilnQuantMode) * 31 + this.hilnMaxNumLine) * 31 + this.hilnSampleRateCode) * 31 + this.hilnFrameLength) * 31 + this.hilnContMode) * 31 + this.hilnEnhaLayer) * 31 + this.hilnEnhaQuantMode) * 31 + (this.parametricSpecificConfig ? 1 : 0);
    }
    
    @Override
    public void parseDetail(final ByteBuffer byteBuffer) throws IOException {
        final ByteBuffer slice = byteBuffer.slice();
        slice.limit(super.sizeOfInstance);
        byteBuffer.position(byteBuffer.position() + super.sizeOfInstance);
        slice.get(this.configBytes = new byte[super.sizeOfInstance]);
        slice.rewind();
        final BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(slice);
        this.audioObjectType = this.getAudioObjectType(bitReaderBuffer);
        this.samplingFrequencyIndex = bitReaderBuffer.readBits(4);
        if (this.samplingFrequencyIndex == 15) {
            this.samplingFrequency = bitReaderBuffer.readBits(24);
        }
        this.channelConfiguration = bitReaderBuffer.readBits(4);
        final int audioObjectType = this.audioObjectType;
        if (audioObjectType != 5 && audioObjectType != 29) {
            this.extensionAudioObjectType = 0;
        }
        else {
            this.extensionAudioObjectType = 5;
            this.sbrPresentFlag = true;
            if (this.audioObjectType == 29) {
                this.psPresentFlag = true;
            }
            this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
            if (this.extensionSamplingFrequencyIndex == 15) {
                this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
            }
            this.audioObjectType = this.getAudioObjectType(bitReaderBuffer);
            if (this.audioObjectType == 22) {
                this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
            }
        }
        final int audioObjectType2 = this.audioObjectType;
        switch (audioObjectType2) {
            case 40:
            case 41: {
                throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
            }
            case 39: {
                this.eldSpecificConfig = new ELDSpecificConfig(this.channelConfiguration, bitReaderBuffer);
                break;
            }
            case 37:
            case 38: {
                throw new UnsupportedOperationException("can't parse SLSSpecificConfig yet");
            }
            case 36: {
                this.fillBits = bitReaderBuffer.readBits(5);
                throw new UnsupportedOperationException("can't parse ALSSpecificConfig yet");
            }
            case 35: {
                throw new UnsupportedOperationException("can't parse DSTSpecificConfig yet");
            }
            case 32:
            case 33:
            case 34: {
                throw new UnsupportedOperationException("can't parse MPEG_1_2_SpecificConfig yet");
            }
            case 30: {
                this.sacPayloadEmbedding = bitReaderBuffer.readBits(1);
                throw new UnsupportedOperationException("can't parse SpatialSpecificConfig yet");
            }
            case 28: {
                throw new UnsupportedOperationException("can't parse SSCSpecificConfig yet");
            }
            case 26:
            case 27: {
                this.parseParametricSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, audioObjectType2, bitReaderBuffer);
                break;
            }
            case 25: {
                throw new UnsupportedOperationException("can't parse ErrorResilientHvxcSpecificConfig yet");
            }
            case 24: {
                throw new UnsupportedOperationException("can't parse ErrorResilientCelpSpecificConfig yet");
            }
            case 13:
            case 14:
            case 15:
            case 16: {
                throw new UnsupportedOperationException("can't parse StructuredAudioSpecificConfig yet");
            }
            case 12: {
                throw new UnsupportedOperationException("can't parse TTSSpecificConfig yet");
            }
            case 9: {
                throw new UnsupportedOperationException("can't parse HvxcSpecificConfig yet");
            }
            case 8: {
                throw new UnsupportedOperationException("can't parse CelpSpecificConfig yet");
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23: {
                this.parseGaSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, audioObjectType2, bitReaderBuffer);
                break;
            }
        }
        final int audioObjectType3 = this.audioObjectType;
        Label_0742: {
            if (audioObjectType3 != 17 && audioObjectType3 != 39) {
                switch (audioObjectType3) {
                    default: {
                        break Label_0742;
                    }
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27: {
                        break;
                    }
                }
            }
            this.epConfig = bitReaderBuffer.readBits(2);
            final int epConfig = this.epConfig;
            if (epConfig == 2 || epConfig == 3) {
                throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
            }
            if (epConfig == 3) {
                this.directMapping = bitReaderBuffer.readBits(1);
                if (this.directMapping == 0) {
                    throw new RuntimeException("not implemented");
                }
            }
        }
        if (this.extensionAudioObjectType != 5 && bitReaderBuffer.remainingBits() >= 16) {
            this.syncExtensionType = bitReaderBuffer.readBits(11);
            if (this.syncExtensionType == 695) {
                this.extensionAudioObjectType = this.getAudioObjectType(bitReaderBuffer);
                if (this.extensionAudioObjectType == 5) {
                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                    if (this.sbrPresentFlag) {
                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                        }
                        if (bitReaderBuffer.remainingBits() >= 12) {
                            this.syncExtensionType = bitReaderBuffer.readBits(11);
                            if (this.syncExtensionType == 1352) {
                                this.psPresentFlag = bitReaderBuffer.readBool();
                            }
                        }
                    }
                }
                if (this.extensionAudioObjectType == 22) {
                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                    if (this.sbrPresentFlag) {
                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                        }
                    }
                    this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
                }
            }
        }
    }
    
    public ByteBuffer serialize() {
        final ByteBuffer allocate = ByteBuffer.allocate(this.serializedSize());
        IsoTypeWriter.writeUInt8(allocate, 5);
        IsoTypeWriter.writeUInt8(allocate, this.serializedSize() - 2);
        final BitWriterBuffer bitWriterBuffer = new BitWriterBuffer(allocate);
        bitWriterBuffer.writeBits(this.audioObjectType, 5);
        bitWriterBuffer.writeBits(this.samplingFrequencyIndex, 4);
        if (this.samplingFrequencyIndex != 15) {
            bitWriterBuffer.writeBits(this.channelConfiguration, 4);
            return allocate;
        }
        throw new UnsupportedOperationException("can't serialize that yet");
    }
    
    public int serializedSize() {
        if (this.audioObjectType == 2) {
            return this.gaSpecificConfigSize() + 4;
        }
        throw new UnsupportedOperationException("can't serialize that yet");
    }
    
    public void setAudioObjectType(final int audioObjectType) {
        this.audioObjectType = audioObjectType;
    }
    
    public void setChannelConfiguration(final int channelConfiguration) {
        this.channelConfiguration = channelConfiguration;
    }
    
    public void setSamplingFrequencyIndex(final int samplingFrequencyIndex) {
        this.samplingFrequencyIndex = samplingFrequencyIndex;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AudioSpecificConfig");
        sb.append("{configBytes=");
        sb.append(Hex.encodeHex(this.configBytes));
        sb.append(", audioObjectType=");
        sb.append(this.audioObjectType);
        sb.append(" (");
        sb.append(AudioSpecificConfig.audioObjectTypeMap.get(this.audioObjectType));
        sb.append(")");
        sb.append(", samplingFrequencyIndex=");
        sb.append(this.samplingFrequencyIndex);
        sb.append(" (");
        sb.append(AudioSpecificConfig.samplingFrequencyIndexMap.get(this.samplingFrequencyIndex));
        sb.append(")");
        sb.append(", samplingFrequency=");
        sb.append(this.samplingFrequency);
        sb.append(", channelConfiguration=");
        sb.append(this.channelConfiguration);
        if (this.extensionAudioObjectType > 0) {
            sb.append(", extensionAudioObjectType=");
            sb.append(this.extensionAudioObjectType);
            sb.append(" (");
            sb.append(AudioSpecificConfig.audioObjectTypeMap.get(this.extensionAudioObjectType));
            sb.append(")");
            sb.append(", sbrPresentFlag=");
            sb.append(this.sbrPresentFlag);
            sb.append(", psPresentFlag=");
            sb.append(this.psPresentFlag);
            sb.append(", extensionSamplingFrequencyIndex=");
            sb.append(this.extensionSamplingFrequencyIndex);
            sb.append(" (");
            sb.append(AudioSpecificConfig.samplingFrequencyIndexMap.get(this.extensionSamplingFrequencyIndex));
            sb.append(")");
            sb.append(", extensionSamplingFrequency=");
            sb.append(this.extensionSamplingFrequency);
            sb.append(", extensionChannelConfiguration=");
            sb.append(this.extensionChannelConfiguration);
        }
        sb.append(", syncExtensionType=");
        sb.append(this.syncExtensionType);
        if (this.gaSpecificConfig) {
            sb.append(", frameLengthFlag=");
            sb.append(this.frameLengthFlag);
            sb.append(", dependsOnCoreCoder=");
            sb.append(this.dependsOnCoreCoder);
            sb.append(", coreCoderDelay=");
            sb.append(this.coreCoderDelay);
            sb.append(", extensionFlag=");
            sb.append(this.extensionFlag);
            sb.append(", layerNr=");
            sb.append(this.layerNr);
            sb.append(", numOfSubFrame=");
            sb.append(this.numOfSubFrame);
            sb.append(", layer_length=");
            sb.append(this.layer_length);
            sb.append(", aacSectionDataResilienceFlag=");
            sb.append(this.aacSectionDataResilienceFlag);
            sb.append(", aacScalefactorDataResilienceFlag=");
            sb.append(this.aacScalefactorDataResilienceFlag);
            sb.append(", aacSpectralDataResilienceFlag=");
            sb.append(this.aacSpectralDataResilienceFlag);
            sb.append(", extensionFlag3=");
            sb.append(this.extensionFlag3);
        }
        if (this.parametricSpecificConfig) {
            sb.append(", isBaseLayer=");
            sb.append(this.isBaseLayer);
            sb.append(", paraMode=");
            sb.append(this.paraMode);
            sb.append(", paraExtensionFlag=");
            sb.append(this.paraExtensionFlag);
            sb.append(", hvxcVarMode=");
            sb.append(this.hvxcVarMode);
            sb.append(", hvxcRateMode=");
            sb.append(this.hvxcRateMode);
            sb.append(", erHvxcExtensionFlag=");
            sb.append(this.erHvxcExtensionFlag);
            sb.append(", var_ScalableFlag=");
            sb.append(this.var_ScalableFlag);
            sb.append(", hilnQuantMode=");
            sb.append(this.hilnQuantMode);
            sb.append(", hilnMaxNumLine=");
            sb.append(this.hilnMaxNumLine);
            sb.append(", hilnSampleRateCode=");
            sb.append(this.hilnSampleRateCode);
            sb.append(", hilnFrameLength=");
            sb.append(this.hilnFrameLength);
            sb.append(", hilnContMode=");
            sb.append(this.hilnContMode);
            sb.append(", hilnEnhaLayer=");
            sb.append(this.hilnEnhaLayer);
            sb.append(", hilnEnhaQuantMode=");
            sb.append(this.hilnEnhaQuantMode);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public class ELDSpecificConfig
    {
        public boolean aacScalefactorDataResilienceFlag;
        public boolean aacSectionDataResilienceFlag;
        public boolean aacSpectralDataResilienceFlag;
        public boolean frameLengthFlag;
        public boolean ldSbrCrcFlag;
        public boolean ldSbrPresentFlag;
        public boolean ldSbrSamplingRate;
        
        public ELDSpecificConfig(int bits, final BitReaderBuffer bitReaderBuffer) {
            this.frameLengthFlag = bitReaderBuffer.readBool();
            this.aacSectionDataResilienceFlag = bitReaderBuffer.readBool();
            this.aacScalefactorDataResilienceFlag = bitReaderBuffer.readBool();
            this.aacSpectralDataResilienceFlag = bitReaderBuffer.readBool();
            this.ldSbrPresentFlag = bitReaderBuffer.readBool();
            if (this.ldSbrPresentFlag) {
                this.ldSbrSamplingRate = bitReaderBuffer.readBool();
                this.ldSbrCrcFlag = bitReaderBuffer.readBool();
                this.ld_sbr_header(bits, bitReaderBuffer);
            }
            while (bitReaderBuffer.readBits(4) != 0) {
                bits = bitReaderBuffer.readBits(4);
                final int n = 0;
                int bits2;
                if (bits == 15) {
                    bits2 = bitReaderBuffer.readBits(8);
                    bits += bits2;
                }
                else {
                    bits2 = 0;
                }
                int i = n;
                int n2 = bits;
                if (bits2 == 255) {
                    n2 = bits + bitReaderBuffer.readBits(16);
                    i = n;
                }
                while (i < n2) {
                    bitReaderBuffer.readBits(8);
                    ++i;
                }
            }
        }
        
        public void ld_sbr_header(int n, final BitReaderBuffer bitReaderBuffer) {
            int i = 0;
            switch (n) {
                default: {
                    n = 0;
                    break;
                }
                case 7: {
                    n = 4;
                    break;
                }
                case 4:
                case 5:
                case 6: {
                    n = 3;
                    break;
                }
                case 3: {
                    n = 2;
                    break;
                }
                case 1:
                case 2: {
                    n = 1;
                    break;
                }
            }
            while (i < n) {
                new sbr_header(bitReaderBuffer);
                ++i;
            }
        }
    }
    
    public class sbr_header
    {
        public boolean bs_alter_scale;
        public boolean bs_amp_res;
        public int bs_freq_scale;
        public boolean bs_header_extra_1;
        public boolean bs_header_extra_2;
        public boolean bs_interpol_freq;
        public int bs_limiter_bands;
        public int bs_limiter_gains;
        public int bs_noise_bands;
        public int bs_reserved;
        public boolean bs_smoothing_mode;
        public int bs_start_freq;
        public int bs_stop_freq;
        public int bs_xover_band;
        
        public sbr_header(final BitReaderBuffer bitReaderBuffer) {
            this.bs_amp_res = bitReaderBuffer.readBool();
            this.bs_start_freq = bitReaderBuffer.readBits(4);
            this.bs_stop_freq = bitReaderBuffer.readBits(4);
            this.bs_xover_band = bitReaderBuffer.readBits(3);
            this.bs_reserved = bitReaderBuffer.readBits(2);
            this.bs_header_extra_1 = bitReaderBuffer.readBool();
            this.bs_header_extra_2 = bitReaderBuffer.readBool();
            if (this.bs_header_extra_1) {
                this.bs_freq_scale = bitReaderBuffer.readBits(2);
                this.bs_alter_scale = bitReaderBuffer.readBool();
                this.bs_noise_bands = bitReaderBuffer.readBits(2);
            }
            if (this.bs_header_extra_2) {
                this.bs_limiter_bands = bitReaderBuffer.readBits(2);
                this.bs_limiter_gains = bitReaderBuffer.readBits(2);
                this.bs_interpol_freq = bitReaderBuffer.readBool();
            }
            this.bs_smoothing_mode = bitReaderBuffer.readBool();
        }
    }
}
