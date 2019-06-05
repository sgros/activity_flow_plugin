// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitArray;

final class GeneralAppIdDecoder
{
    private final StringBuilder buffer;
    private final CurrentParsingState current;
    private final BitArray information;
    
    GeneralAppIdDecoder(final BitArray information) {
        this.current = new CurrentParsingState();
        this.buffer = new StringBuilder();
        this.information = information;
    }
    
    private DecodedChar decodeAlphanumeric(final int n) {
        final int numericValueFromBitArray = this.extractNumericValueFromBitArray(n, 5);
        DecodedChar decodedChar;
        if (numericValueFromBitArray == 15) {
            decodedChar = new DecodedChar(n + 5, '$');
        }
        else if (numericValueFromBitArray >= 5 && numericValueFromBitArray < 15) {
            decodedChar = new DecodedChar(n + 5, (char)(numericValueFromBitArray + 48 - 5));
        }
        else {
            final int numericValueFromBitArray2 = this.extractNumericValueFromBitArray(n, 6);
            if (numericValueFromBitArray2 >= 32 && numericValueFromBitArray2 < 58) {
                decodedChar = new DecodedChar(n + 6, (char)(numericValueFromBitArray2 + 33));
            }
            else {
                char c = '\0';
                switch (numericValueFromBitArray2) {
                    default: {
                        throw new IllegalStateException("Decoding invalid alphanumeric value: " + numericValueFromBitArray2);
                    }
                    case 58: {
                        c = '*';
                        break;
                    }
                    case 59: {
                        c = ',';
                        break;
                    }
                    case 60: {
                        c = '-';
                        break;
                    }
                    case 61: {
                        c = '.';
                        break;
                    }
                    case 62: {
                        c = '/';
                        break;
                    }
                }
                decodedChar = new DecodedChar(n + 6, c);
            }
        }
        return decodedChar;
    }
    
    private DecodedChar decodeIsoIec646(final int n) throws FormatException {
        final int numericValueFromBitArray = this.extractNumericValueFromBitArray(n, 5);
        DecodedChar decodedChar;
        if (numericValueFromBitArray == 15) {
            decodedChar = new DecodedChar(n + 5, '$');
        }
        else if (numericValueFromBitArray >= 5 && numericValueFromBitArray < 15) {
            decodedChar = new DecodedChar(n + 5, (char)(numericValueFromBitArray + 48 - 5));
        }
        else {
            final int numericValueFromBitArray2 = this.extractNumericValueFromBitArray(n, 7);
            if (numericValueFromBitArray2 >= 64 && numericValueFromBitArray2 < 90) {
                decodedChar = new DecodedChar(n + 7, (char)(numericValueFromBitArray2 + 1));
            }
            else if (numericValueFromBitArray2 >= 90 && numericValueFromBitArray2 < 116) {
                decodedChar = new DecodedChar(n + 7, (char)(numericValueFromBitArray2 + 7));
            }
            else {
                char c = '\0';
                switch (this.extractNumericValueFromBitArray(n, 8)) {
                    default: {
                        throw FormatException.getFormatInstance();
                    }
                    case 232: {
                        c = '!';
                        break;
                    }
                    case 233: {
                        c = '\"';
                        break;
                    }
                    case 234: {
                        c = '%';
                        break;
                    }
                    case 235: {
                        c = '&';
                        break;
                    }
                    case 236: {
                        c = '\'';
                        break;
                    }
                    case 237: {
                        c = '(';
                        break;
                    }
                    case 238: {
                        c = ')';
                        break;
                    }
                    case 239: {
                        c = '*';
                        break;
                    }
                    case 240: {
                        c = '+';
                        break;
                    }
                    case 241: {
                        c = ',';
                        break;
                    }
                    case 242: {
                        c = '-';
                        break;
                    }
                    case 243: {
                        c = '.';
                        break;
                    }
                    case 244: {
                        c = '/';
                        break;
                    }
                    case 245: {
                        c = ':';
                        break;
                    }
                    case 246: {
                        c = ';';
                        break;
                    }
                    case 247: {
                        c = '<';
                        break;
                    }
                    case 248: {
                        c = '=';
                        break;
                    }
                    case 249: {
                        c = '>';
                        break;
                    }
                    case 250: {
                        c = '?';
                        break;
                    }
                    case 251: {
                        c = '_';
                        break;
                    }
                    case 252: {
                        c = ' ';
                        break;
                    }
                }
                decodedChar = new DecodedChar(n + 8, c);
            }
        }
        return decodedChar;
    }
    
    private DecodedNumeric decodeNumeric(int numericValueFromBitArray) throws FormatException {
        DecodedNumeric decodedNumeric;
        if (numericValueFromBitArray + 7 > this.information.getSize()) {
            numericValueFromBitArray = this.extractNumericValueFromBitArray(numericValueFromBitArray, 4);
            if (numericValueFromBitArray == 0) {
                decodedNumeric = new DecodedNumeric(this.information.getSize(), 10, 10);
            }
            else {
                decodedNumeric = new DecodedNumeric(this.information.getSize(), numericValueFromBitArray - 1, 10);
            }
        }
        else {
            final int numericValueFromBitArray2 = this.extractNumericValueFromBitArray(numericValueFromBitArray, 7);
            decodedNumeric = new DecodedNumeric(numericValueFromBitArray + 7, (numericValueFromBitArray2 - 8) / 11, (numericValueFromBitArray2 - 8) % 11);
        }
        return decodedNumeric;
    }
    
    static int extractNumericValueFromBitArray(final BitArray bitArray, final int n, final int n2) {
        int n3 = 0;
        int n4;
        for (int i = 0; i < n2; ++i, n3 = n4) {
            n4 = n3;
            if (bitArray.get(n + i)) {
                n4 = (n3 | 1 << n2 - i - 1);
            }
        }
        return n3;
    }
    
    private boolean isAlphaOr646ToNumericLatch(final int n) {
        final boolean b = false;
        boolean b2;
        if (n + 3 > this.information.getSize()) {
            b2 = b;
        }
        else {
            for (int i = n; i < n + 3; ++i) {
                b2 = b;
                if (this.information.get(i)) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    private boolean isAlphaTo646ToAlphaLatch(final int n) {
        final boolean b = false;
        boolean b2;
        if (n + 1 > this.information.getSize()) {
            b2 = b;
        }
        else {
            for (int n2 = 0; n2 < 5 && n2 + n < this.information.getSize(); ++n2) {
                if (n2 == 2) {
                    b2 = b;
                    if (!this.information.get(n + 2)) {
                        return b2;
                    }
                }
                else if (this.information.get(n + n2)) {
                    b2 = b;
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    private boolean isNumericToAlphaNumericLatch(final int n) {
        final boolean b = false;
        boolean b2;
        if (n + 1 > this.information.getSize()) {
            b2 = b;
        }
        else {
            for (int n2 = 0; n2 < 4 && n2 + n < this.information.getSize(); ++n2) {
                b2 = b;
                if (this.information.get(n + n2)) {
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    private boolean isStillAlpha(int numericValueFromBitArray) {
        final boolean b = false;
        boolean b2;
        if (numericValueFromBitArray + 5 > this.information.getSize()) {
            b2 = b;
        }
        else {
            final int numericValueFromBitArray2 = this.extractNumericValueFromBitArray(numericValueFromBitArray, 5);
            if (numericValueFromBitArray2 >= 5 && numericValueFromBitArray2 < 16) {
                b2 = true;
            }
            else {
                b2 = b;
                if (numericValueFromBitArray + 6 <= this.information.getSize()) {
                    numericValueFromBitArray = this.extractNumericValueFromBitArray(numericValueFromBitArray, 6);
                    b2 = b;
                    if (numericValueFromBitArray >= 16) {
                        b2 = b;
                        if (numericValueFromBitArray < 63) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private boolean isStillIsoIec646(int numericValueFromBitArray) {
        final boolean b = false;
        boolean b2;
        if (numericValueFromBitArray + 5 > this.information.getSize()) {
            b2 = b;
        }
        else {
            final int numericValueFromBitArray2 = this.extractNumericValueFromBitArray(numericValueFromBitArray, 5);
            if (numericValueFromBitArray2 >= 5 && numericValueFromBitArray2 < 16) {
                b2 = true;
            }
            else {
                b2 = b;
                if (numericValueFromBitArray + 7 <= this.information.getSize()) {
                    final int numericValueFromBitArray3 = this.extractNumericValueFromBitArray(numericValueFromBitArray, 7);
                    if (numericValueFromBitArray3 >= 64 && numericValueFromBitArray3 < 116) {
                        b2 = true;
                    }
                    else {
                        b2 = b;
                        if (numericValueFromBitArray + 8 <= this.information.getSize()) {
                            numericValueFromBitArray = this.extractNumericValueFromBitArray(numericValueFromBitArray, 8);
                            b2 = b;
                            if (numericValueFromBitArray >= 232) {
                                b2 = b;
                                if (numericValueFromBitArray < 253) {
                                    b2 = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private boolean isStillNumeric(final int n) {
        final boolean b = true;
        boolean value;
        if (n + 7 > this.information.getSize()) {
            value = (n + 4 <= this.information.getSize() && b);
        }
        else {
            for (int i = n; i < n + 3; ++i) {
                value = b;
                if (this.information.get(i)) {
                    return value;
                }
            }
            value = this.information.get(n + 3);
        }
        return value;
    }
    
    private BlockParsedResult parseAlphaBlock() {
        while (this.isStillAlpha(this.current.getPosition())) {
            final DecodedChar decodeAlphanumeric = this.decodeAlphanumeric(this.current.getPosition());
            this.current.setPosition(decodeAlphanumeric.getNewPosition());
            if (decodeAlphanumeric.isFNC1()) {
                return new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
            }
            this.buffer.append(decodeAlphanumeric.getValue());
        }
        if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
            this.current.incrementPosition(3);
            this.current.setNumeric();
        }
        else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
            if (this.current.getPosition() + 5 < this.information.getSize()) {
                this.current.incrementPosition(5);
            }
            else {
                this.current.setPosition(this.information.getSize());
            }
            this.current.setIsoIec646();
        }
        return new BlockParsedResult(false);
    }
    
    private DecodedInformation parseBlocks() throws FormatException {
        BlockParsedResult blockParsedResult;
        boolean b;
        boolean b2;
        do {
            final int position = this.current.getPosition();
            if (this.current.isAlpha()) {
                blockParsedResult = this.parseAlphaBlock();
                b = blockParsedResult.isFinished();
            }
            else if (this.current.isIsoIec646()) {
                blockParsedResult = this.parseIsoIec646Block();
                b = blockParsedResult.isFinished();
            }
            else {
                blockParsedResult = this.parseNumericBlock();
                b = blockParsedResult.isFinished();
            }
            if (position != this.current.getPosition()) {
                b2 = true;
            }
            else {
                b2 = false;
            }
        } while ((b2 || b) && !b);
        return blockParsedResult.getDecodedInformation();
    }
    
    private BlockParsedResult parseIsoIec646Block() throws FormatException {
        while (this.isStillIsoIec646(this.current.getPosition())) {
            final DecodedChar decodeIsoIec646 = this.decodeIsoIec646(this.current.getPosition());
            this.current.setPosition(decodeIsoIec646.getNewPosition());
            if (decodeIsoIec646.isFNC1()) {
                return new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
            }
            this.buffer.append(decodeIsoIec646.getValue());
        }
        if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
            this.current.incrementPosition(3);
            this.current.setNumeric();
        }
        else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
            if (this.current.getPosition() + 5 < this.information.getSize()) {
                this.current.incrementPosition(5);
            }
            else {
                this.current.setPosition(this.information.getSize());
            }
            this.current.setAlpha();
        }
        return new BlockParsedResult(false);
    }
    
    private BlockParsedResult parseNumericBlock() throws FormatException {
        while (this.isStillNumeric(this.current.getPosition())) {
            final DecodedNumeric decodeNumeric = this.decodeNumeric(this.current.getPosition());
            this.current.setPosition(decodeNumeric.getNewPosition());
            BlockParsedResult blockParsedResult;
            if (decodeNumeric.isFirstDigitFNC1()) {
                DecodedInformation decodedInformation;
                if (decodeNumeric.isSecondDigitFNC1()) {
                    decodedInformation = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
                }
                else {
                    decodedInformation = new DecodedInformation(this.current.getPosition(), this.buffer.toString(), decodeNumeric.getSecondDigit());
                }
                blockParsedResult = new BlockParsedResult(decodedInformation, true);
            }
            else {
                this.buffer.append(decodeNumeric.getFirstDigit());
                if (!decodeNumeric.isSecondDigitFNC1()) {
                    this.buffer.append(decodeNumeric.getSecondDigit());
                    continue;
                }
                blockParsedResult = new BlockParsedResult(new DecodedInformation(this.current.getPosition(), this.buffer.toString()), true);
            }
            return blockParsedResult;
        }
        if (this.isNumericToAlphaNumericLatch(this.current.getPosition())) {
            this.current.setAlpha();
            this.current.incrementPosition(4);
        }
        return new BlockParsedResult(false);
    }
    
    String decodeAllCodes(final StringBuilder sb, int newPosition) throws NotFoundException, FormatException {
        String value = null;
        while (true) {
            final DecodedInformation decodeGeneralPurposeField = this.decodeGeneralPurposeField(newPosition, value);
            final String fieldsInGeneralPurpose = FieldParser.parseFieldsInGeneralPurpose(decodeGeneralPurposeField.getNewString());
            if (fieldsInGeneralPurpose != null) {
                sb.append(fieldsInGeneralPurpose);
            }
            if (decodeGeneralPurposeField.isRemaining()) {
                value = String.valueOf(decodeGeneralPurposeField.getRemainingValue());
            }
            else {
                value = null;
            }
            if (newPosition == decodeGeneralPurposeField.getNewPosition()) {
                break;
            }
            newPosition = decodeGeneralPurposeField.getNewPosition();
        }
        return sb.toString();
    }
    
    DecodedInformation decodeGeneralPurposeField(final int position, final String str) throws FormatException {
        this.buffer.setLength(0);
        if (str != null) {
            this.buffer.append(str);
        }
        this.current.setPosition(position);
        final DecodedInformation blocks = this.parseBlocks();
        DecodedInformation decodedInformation;
        if (blocks != null && blocks.isRemaining()) {
            decodedInformation = new DecodedInformation(this.current.getPosition(), this.buffer.toString(), blocks.getRemainingValue());
        }
        else {
            decodedInformation = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
        }
        return decodedInformation;
    }
    
    int extractNumericValueFromBitArray(final int n, final int n2) {
        return extractNumericValueFromBitArray(this.information, n, n2);
    }
}
