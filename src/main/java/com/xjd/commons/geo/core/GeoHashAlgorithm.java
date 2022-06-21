package com.xjd.commons.geo.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GeoHashAlgorithm {
    public static final String NEAR_ALL = "L,LU,U,RU,R,RD,D,LD";
    static final char[] base32Codes = "0123456789bcdefghjkmnpqrstuvwxyz".toCharArray();

    public static String base32Point(double lng, double lat, int len) {
        byte[] bits = encodePoint(lng, lat, len * 5);
        return base32Bits(bits);
    }

    public static String[] getBase32NearPoint(String point, String nearParam) {
        if (point == null || nearParam == null) return null;
        List<String> list = new ArrayList<>(8);
        byte[] pointBits = bitsBase32(point);
        List<byte[]> oddEventBits = splitOddEvenBits(pointBits);
        Number lngNumber = numberBits(oddEventBits.get(0));
        Number latNumber = numberBits(oddEventBits.get(1));

        nearParam = nearParam.trim().toUpperCase();
        if ("ALL".equals(nearParam)) {
            nearParam = NEAR_ALL;
        }
        String[] nearParams = nearParam.split("\\,");
        for (String param : nearParams) {
            param = param.trim();
            long lng = lngNumber.longValue();
            long lat = latNumber.longValue();
            if (param.contains("L")) {
                lng = lng - 1;
            }
            if (param.contains("R")) {
                lng = lng + 1;
            }
            if (param.contains("D")) {
                lat = lat - 1;
            }
            if (param.contains("U")) {
                lat = lat + 1;
            }
            // FIXME 越界问题
            byte[] bytesLng = bitsNumber(lng);
            byte[] bytesLat = bitsNumber(lat);
            if (bytesLat != null && bytesLng != null) {
                list.add(base32Bits(mergeOddEvenBits(trimBits(bytesLng, oddEventBits.get(0).length), trimBits(bytesLat, oddEventBits.get(1).length))));
            }
        }
        return list.toArray(new String[list.size()]);
    }



    static String base32Bits(byte[] bits) {
        if (bits == null || bits.length == 0) return null;
        if (bits.length % 5 > 0) throw new IllegalArgumentException("can not base32 bits(len=" + bits.length + ")");
        StringBuilder encodeBuf = new StringBuilder(bits.length / 5);
        StringBuilder bitBuf = new StringBuilder(5);
        for (int i = 0; i < bits.length; i++) {
            bitBuf.append(bits[i]);
            if ((i + 1) % 5 == 0) {
                encodeBuf.append(base32Codes[Integer.parseInt(bitBuf.toString(), 2)]);
                bitBuf.delete(0, 5);
            }
        }
        return encodeBuf.toString();
    }

    public static byte[] bitsBase32(String str) {
        if (str == null || str.length() == 0) return  null;
        byte[] bits = new byte[str.length() * 5];
        for (int i = 0, len = str.length(); i < len; i++) {

            byte[] bytes = bitsNumber(indexBase32(str.charAt(i)));
            if (bytes != null) {
                System.arraycopy(trimMulBits(bytes, 5), 0, bits, i * 5, 5);
            }
        }
        return bits;
    }

    static int indexBase32(char c) {
        int i = 0;
        for (char code : base32Codes) {
            if (code == c) return i;
            i ++;
        }
        throw new IllegalArgumentException("can not base32 index char[" + c + "]");
    }

    static Number numberBits(byte[] bits) {
        if (bits == null || bits.length == 0) return null;
        if (bits.length > 64) throw new IllegalArgumentException("can not case bits to a number: bitsLen=" + bits.length);
        StringBuilder buf = new StringBuilder(bits.length);
        for (byte bit : bits) {
            buf.append(bit);
        }
        if (bits.length <= 32) { // int
            return Integer.valueOf(buf.toString(), 2);
        } else { // long
            return Long.valueOf(buf.toString(), 2);
        }
    }

    static byte[] bitsNumber(Number number) {
        if (number == null) return null;
        if (!(number instanceof Integer) && !(number instanceof Long)) throw new IllegalArgumentException("can not case number to bits: number type=" + number.getClass());
        String str = Long.toString(number.longValue(), 2);
        byte[] bits = new byte[str.length()];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = Byte.parseByte(str.charAt(i) + "");
        }
        return bits;
    }

    static byte[] trimMulBits(byte[] bits, int factor) {
        if (bits.length % factor == 0) return bits;
        return trimBits(bits, (bits.length / factor + 1) * factor);
    }

    static byte[] trimBits(byte[] bits, int len) {
        if (len == bits.length) return bits;
        byte[] newBits = new byte[len];
        if (len > bits.length) {
            for (int i = 0; i < (len - bits.length); i++) {
                newBits[i] = (byte) 0;
            }
            System.arraycopy(bits, 0, newBits, (len - bits.length), bits.length);
        } else {
            System.arraycopy(bits, bits.length - len, newBits, 0, len);
        }
        return newBits;
    }

    public static byte[] encodePoint(double lng, double lat, int bitLen) {
        byte[] lngBits = encodeLongitude(lng, bitLen / 2 + (bitLen % 2 == 0 ? 0 : 1));
        byte[] latBits = encodeLatitude(lat, bitLen / 2);
        return mergeOddEvenBits(lngBits, latBits);
    }

    public static byte[] encodeLatitude(double value, int bitLen) {
        return encode(value, bitLen, new double[]{-90D, 90D});
    }

    public static byte[] encodeLongitude(double value, int bitLen) {
        return encode(value, bitLen, new double[]{-180D, 180D});
    }

    static List<byte[]> splitOddEvenBits(byte[] bits) {
        if (bits == null || bits.length == 0) return null;
        byte[] odd = new byte[bits.length / 2 + (bits.length % 2 == 0 ? 0 : 1)];
        byte[] even = new byte[bits.length / 2];
        for (int i = 0; i < even.length; i++) {
            odd[i] = bits[i * 2];
            even[i] = bits[i * 2 + 1];
        }
        if (bits.length % 2 > 0) {
            odd[odd.length - 1] = bits[bits.length - 1];
        }
        return Arrays.asList(odd, even);
    }

    static byte[] mergeOddEvenBits(byte[] odd, byte[] even) {
        byte[] mergeBits = new byte[odd.length + even.length];
        for (int i = 0; i < even.length; i++) {
            mergeBits[i * 2] = odd[i];
            mergeBits[i * 2 + 1] = even[i];
        }
        if (mergeBits.length % 2 > 0) {
            mergeBits[mergeBits.length - 1] = odd[odd.length - 1];
        }
        return mergeBits;
    }

    static byte[] encode(double value, int bitLen, double[] range) {
        byte[] bs = new byte[bitLen];
        for (int i = 0; i < bitLen; i++) {
            bs[i] = encode(value, range);
        }
        return bs;
    }

    static byte encode(double value, double[] range) {
        double middle = (range[0] + range[1]) / 2.0d;
        if (value > middle) {
            range[0] = middle;
            return (byte) 1;
        } else {
            range[1] = middle;
            return (byte) 0;
        }
    }

    public static double[][] getRangeOfHashBlock(String geoHash) {
        if (geoHash == null) return null;
        byte[] pointBits = bitsBase32(geoHash);
        List<byte[]> oddEventBits = splitOddEvenBits(pointBits);
        double[] lngRange = range(oddEventBits.get(0), new double[]{-180D, 180D});
        double[] latRange = range(oddEventBits.get(1), new double[]{-90D, 90D});
        return new double[][]{{lngRange[0], latRange[0]}, {lngRange[1], latRange[1]}};
    }

    static double[] range(byte[] bits, double[] range) {
        for (byte bit : bits) {
            double middle = (range[0] + range[1]) / 2.0d;
            if (bit == 1) {
                range[0] = middle;
            } else {
                range[1] = middle;
            }
        }
        return range;
    }
}
