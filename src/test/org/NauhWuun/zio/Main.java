package org.NauhWuun.zio;

import org.NauhWuun.zio.kernel.AioServer;

public class Main
{
    public static void main(String[] arg) throws Exception {
        AioServer.Instance
                .Builder(80)
                .setAddrReuse(true)
                .setRecvBufferSize(4096)
                .start();
    }
}