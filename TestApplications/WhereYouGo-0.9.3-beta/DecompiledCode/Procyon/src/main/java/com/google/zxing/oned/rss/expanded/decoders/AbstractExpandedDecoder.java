// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

public abstract class AbstractExpandedDecoder
{
    private final GeneralAppIdDecoder generalDecoder;
    private final BitArray information;
    
    AbstractExpandedDecoder(final BitArray information) {
        this.information = information;
        this.generalDecoder = new GeneralAppIdDecoder(information);
    }
    
    public static AbstractExpandedDecoder createDecoder(final BitArray obj) {
        AbstractExpandedDecoder abstractExpandedDecoder = null;
        Label_0017: {
            if (obj.get(1)) {
                abstractExpandedDecoder = new AI01AndOtherAIs(obj);
            }
            else if (!obj.get(2)) {
                abstractExpandedDecoder = new AnyAIDecoder(obj);
            }
            else {
                switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(obj, 1, 4)) {
                    default: {
                        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(obj, 1, 5)) {
                            default: {
                                switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(obj, 1, 7)) {
                                    default: {
                                        throw new IllegalStateException("unknown decoder: " + obj);
                                    }
                                    case 56: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "310", "11");
                                        break Label_0017;
                                    }
                                    case 57: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "320", "11");
                                        break Label_0017;
                                    }
                                    case 58: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "310", "13");
                                        break Label_0017;
                                    }
                                    case 59: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "320", "13");
                                        break Label_0017;
                                    }
                                    case 60: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "310", "15");
                                        break Label_0017;
                                    }
                                    case 61: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "320", "15");
                                        break Label_0017;
                                    }
                                    case 62: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "310", "17");
                                        break Label_0017;
                                    }
                                    case 63: {
                                        abstractExpandedDecoder = new AI013x0x1xDecoder(obj, "320", "17");
                                        break Label_0017;
                                    }
                                }
                                break;
                            }
                            case 12: {
                                abstractExpandedDecoder = new AI01392xDecoder(obj);
                                break Label_0017;
                            }
                            case 13: {
                                abstractExpandedDecoder = new AI01393xDecoder(obj);
                                break Label_0017;
                            }
                        }
                        break;
                    }
                    case 4: {
                        abstractExpandedDecoder = new AI013103decoder(obj);
                        break;
                    }
                    case 5: {
                        abstractExpandedDecoder = new AI01320xDecoder(obj);
                        break;
                    }
                }
            }
        }
        return abstractExpandedDecoder;
    }
    
    protected final GeneralAppIdDecoder getGeneralDecoder() {
        return this.generalDecoder;
    }
    
    protected final BitArray getInformation() {
        return this.information;
    }
    
    public abstract String parseInformation() throws NotFoundException, FormatException;
}
