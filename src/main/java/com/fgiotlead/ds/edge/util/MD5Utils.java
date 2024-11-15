package com.fgiotlead.ds.edge.util;

import org.springframework.util.DigestUtils;

public class MD5Utils {

    public static String calculateMD5(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }
}
