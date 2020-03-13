package org.NauhWuun.jio;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataStream 
{
    public static void writeGzipFile(byte[] data, String fileName) throws IOException {
        FileOutputStream fout = new FileOutputStream(fileName);
        GZIPOutputStream gz = new GZIPOutputStream(fout);

        gz.write(data);
        gz.finish();

        gz.close();
        fout.close();
    }

    public static byte[] readGzipFile(String fileName) throws IOException {
        FileInputStream fin = new FileInputStream(fileName);
        GZIPInputStream gs  = new GZIPInputStream(fin);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = gs.read(buffer)) != -1) {
            byteBuffer.put(buffer, 0, length);
        }

        gs.close();
        fin.close();

		byte[] r = new byte[length];
        for (int i = 0; i < byteBuffer.position(); i++) {
            r[i] = byteBuffer.get(i);
        }

        return byteBuffer.array();
    }

    public static byte[] gZip(byte[] data) throws IOException {
        byte[] b = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(bos);

        gzip.write(data);
        gzip.finish();
        gzip.close();

        b = bos.toByteArray();
        bos.close();

        return b;
    }

    public static byte[] unGZip(byte[] data) throws IOException {
        byte[] b = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream gzip = new GZIPInputStream(bis);

        byte[] buf = new byte[1024];
        int num = -1;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((num = gzip.read(buf, 0, buf.length)) != -1) {
            baos.write(buf, 0, num);
        }

        b = baos.toByteArray();
        baos.flush();

        baos.close();
        gzip.close();
        bis.close();

        return b;
    }
}