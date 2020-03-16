package org.NauhWuun.jio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

public class AioServer implements Runnable 
{
    public static CircularBuffer<Object> RingBuffer;

    private AsynchronousChannelGroup ChannelGroup;
    private AsynchronousServerSocketChannel Server;

    public void AioServer(int port) throws IOException, ClassCastException {
        ChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                (Runtime.getRuntime().availableProcessors() <= 8 || Runtime.getRuntime().availableProcessors() == 16)
                        ?  Runtime.getRuntime().availableProcessors() * 2 + 2 /* availableProcessors * 2 + 2 => multiThreads */
                        :  Runtime.getRuntime().availableProcessors() * 4 + 4
                        ,  Executors.defaultThreadFactory());

        Server = AsynchronousServerSocketChannel.open(ChannelGroup);
        Server.bind(new InetSocketAddress(Math.max(port, 0)));

        ValidParams.Print("Binding Port：" + port);    
    }

    public void setAddrReuse(boolean reuseAddr) throws IOException {
        Server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
    }

    public void setRecvBufferSize(int recvBufSize) throws IOException {
        Server.setOption(StandardSocketOptions.SO_RCVBUF, (recvBufSize <= 0) ? 8192 : recvBufSize); /* 8k page */
    }

    public void start() { 
        RingBuffer = new CircularBuffer<Object>((int) Runtime.getRuntime().freeMemory() / 2);

        new Thread((Runnable) Server).start();
        ValidParams.Print("AIO Server Starting...");
    }

    public void run() {
        Server.accept(Server, new AioAcceptHandler());
    }

    public void Close() throws IOException {
        if (! ChannelGroup.isShutdown())
            ChannelGroup.shutdown();

        if (! ChannelGroup.isTerminated())
            ChannelGroup.shutdownNow();

        if (Server.isOpen())
            Server.close();

        ValidParams.Print("AIO Server Closing：");
    }
}