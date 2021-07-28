package com.soobinpark.example.nfccommunication;

import java.io.UnsupportedEncodingException;

public class ByteUtils {
    static String ReadRecordByCharEnc(byte[] payload) throws UnsupportedEncodingException {
        int languageCodeLength = payload[0] & 0x3F; // 5th bit defines language code length
        String charEncType = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16"; // 7th bit defines utf-8 or utf-16

        // text length starts after control byte language code
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, charEncType);
    }
}
