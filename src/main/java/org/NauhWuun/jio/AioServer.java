package java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

public class AioServer implements Runnable 
{
    public static CircularBuffer<byte[]> RingBuffer;

    private static long timeOut;
    private AsynchronousChannelGroup ChannelGroup;
    private AsynchronousServerSocketChannel Server;

    public AioServer() throws IOException {
        RingBuffer = new CircularBuffer<>(1024 * 1024 * 50 * 10);
        ValidParams.Printof("AIO Server Starting...");
    }

    public void Start(int port) throws IOException, ClassCastException {
        ChannelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2,
                        Executors.defaultThreadFactory());

        ValidParams.Printof("Thread Counts：" + Runtime.getRuntime().availableProcessors() * 2 + 2);

        Server = AsynchronousServerSocketChannel.open(ChannelGroup);
        Server.bind(new InetSocketAddress(port < 0 ? 0 : port));

        ValidParams.Printof("Binding Port：" + port);

        Server.setOption(StandardSocketOptions.SO_RCVBUF, 0);
        Server.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        new Thread((Runnable) Server).start();
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

        ValidParams.Printof("AIO Server Closing：");
    }

    public static void setTimeout(long TimeOut) {
        timeOut = ((TimeOut <= 0) || (TimeOut > Integer.MAX_VALUE - 1)) ? 1000 : 5000;
    }

    public static long getTimeOut() {
        return timeOut;
    }
}