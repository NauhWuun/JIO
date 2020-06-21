package org.NauhWuun.zio.kernel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class AioSendHandler implements CompletionHandler<Integer, ByteBuffer> 
{
    public final int WRITE_TIME_OUT = 0;
    private AsynchronousSocketChannel socket;

    public AioSendHandler(AsynchronousSocketChannel socket) {
        this.socket = socket;
    }

    public void cancelled(ByteBuffer attachment) {
        ValidParams.Print("AioSend Cancelled ");
    }

    public void completed(Integer i, ByteBuffer buf) {
        if (i == -1) {
            try {
                buf.rewind();
                socket.close();
            } catch (IOException ignored) {
                ValidParams.Print(ignored.getMessage());
            }
        }

        socket.write(buf, buf, this);
    }

    public void failed(Throwable exc, ByteBuffer buf) {
        buf.clear();
    }

    public void sendBuffer(ByteBuffer SendBuffer) {
        socket.write(ByteBuffer.wrap(SendBuffer.array()), WRITE_TIME_OUT, TimeUnit.MILLISECONDS, SendBuffer, this);
    }
}