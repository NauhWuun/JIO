package org.NauhWuun.jio.kernel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer>
{
    private AsynchronousSocketChannel sockets;

    public AioReadHandler(AsynchronousSocketChannel socket) {
        this.sockets = socket;
    }

    public void cancelled(ByteBuffer attachment) {
        ValidParams.Print("AioReadHandler cancelled");
        attachment.rewind();
    }

    public void completed(Integer i, ByteBuffer buf) {
        if (i > 0) {
            buf.flip();
            AioServer.RingBuffer.add(buf);
            sockets.read(buf, buf, this);
        } else if (i == -1) {
            buf.rewind();
        }
    }

    public void failed(Throwable exc, ByteBuffer buf) {
        ValidParams.Print(exc.getMessage());
        buf.rewind();
    }

    public void close() throws IOException {
        sockets.close();
    }
}