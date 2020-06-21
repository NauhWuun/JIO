package org.NauhWuun.zio.kernel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

public class AioServer implements Runnable, AutoCloseable
{
    public static AioServer Instance = SingleTon.GetInstance(AioServer.class);

    public static CircularBuffer<Object> RingBuffer;

    private AsynchronousChannelGroup ChannelGroup;
    private AsynchronousServerSocketChannel Server;

    private AioServer() {}

    public AioServer Builder(int port) throws IOException, ClassCastException {
        ChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2 + 2, Executors.defaultThreadFactory());

        Server = AsynchronousServerSocketChannel.open(ChannelGroup);
        Server.bind(new InetSocketAddress(Math.max(port, 0)));

        ValidParams.Print("Binding Port：" + port);

        return this;
    }

    public AioServer setAddrReuse(boolean reuseAddr) throws IOException {
        Server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        return this;
    }

    public AioServer setRecvBufferSize(int recvBufSize) throws IOException {
        Server.setOption(StandardSocketOptions.SO_RCVBUF, (recvBufSize <= 0) ? 4096 : recvBufSize); /* 4k heap page */
        return this;
    }

    public void start() { 
        RingBuffer = new CircularBuffer<Object>((int) Runtime.getRuntime().freeMemory() / 4);

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

    @Override
    public void close() throws Exception {
        Close();
    }
}