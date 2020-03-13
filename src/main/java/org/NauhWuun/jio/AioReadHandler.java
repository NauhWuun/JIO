package java;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Callable;

public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> 
{
    private AsynchronousSocketChannel sockets;

    public AioReadHandler(AsynchronousSocketChannel socket) {
        this.sockets = socket;
    }

    public void cancelled(ByteBuffer attachment) {
        ValidParams.Printof("AioReadHandler cancelled");
        attachment.rewind();
    }

    public void completed(Integer i, ByteBuffer buf) {
        if (i > 0) {
            buf.flip();

            ByteBuffer CallBackBuffer = buf.duplicate();
            AioServer.RingBuffer.add(CallBackBuffer);


            try {
                ValidParams.Printof("ip addr:" + sockets.getRemoteAddress().toString() + "buffer:" + CallBackBuffer.toString());
            } catch (IOException e) {}
            
            sockets.read(buf, buf, this);
        } else if (i == -1) {
            try {
                buf.rewind();
                sockets.close();
            } catch (IOException e) {}
        }
    }

    public void failed(Throwable exc, ByteBuffer buf) {
        ValidParams.Printof(exc.getMessage());
        buf.rewind();
    }
}