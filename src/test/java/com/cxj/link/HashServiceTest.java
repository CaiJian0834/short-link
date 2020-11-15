package com.cxj.link;

import com.cxj.link.util.MathUtils;
import org.apache.commons.codec.digest.MurmurHash3;
import org.junit.Test;

public class HashServiceTest {

    @Test
    public void generateHash() {
        String url = "http://localhost:9080/";
        long hash = MurmurHash3.hash32x86(url.getBytes());
        System.out.println(hash);
        System.out.println(MathUtils._10_to_62(hash));
    }

    @Test
    public void generateHash128() {
        String url = "http://localhost:9080/";
        long[] longs = MurmurHash3.hash128x64(url.getBytes());
        for (long hash : longs) {
            System.out.println(hash);
            System.out.println(MathUtils._10_to_62(hash));
        }
    }


}
