package lab;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-10-15 10:54
 */
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        char c = '计';
        int value = c;
        System.out.printf("%c\n", value);
        System.out.printf("Unicode for 计: %X\n", value);


        String str = "计算机";
        System.out.printf("Java platform default: ");
        byte[] bytes0 = str.getBytes();
        for (byte b : bytes0) {
            System.out.printf("%2X ", b);
        }
        System.out.println();

        System.out.printf("GBK: ");
        byte[] bytes1 = str.getBytes("GBK");
        for (byte b : bytes1) {
            System.out.printf("%2X ", b);
        }
        System.out.println();
        System.out.printf("UTF_16: ");
        byte[] bytes2 = str.getBytes(StandardCharsets.UTF_16);
        for (byte b : bytes2) {
            System.out.printf("%2X ", b);
        }
        System.out.println();
        System.out.printf("UTF_16BE: ");
        byte[] bytes3 = str.getBytes(StandardCharsets.UTF_16BE);
        for (byte b : bytes3) {
            System.out.printf("%2X ", b);
        }
        System.out.println();
        System.out.printf("UTF_16LE: ");
        byte[] bytes4 = str.getBytes(StandardCharsets.UTF_16LE);
        for (byte b : bytes4) {
            System.out.printf("%2X ", b);
        }
        System.out.println();
    }
}
