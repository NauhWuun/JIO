package org.NauhWuun.jio.kernel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> 
{
    ByteBuffer clientBuffer = ByteBuffer.allocateDirect(8192);

    public void cancelled(AsynchronousServerSocketChannel attachment) throws IOException {
        ValidParams.Print("Remote Disconnect...");
        attachment.close();
    }

    public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel attachment) {
        attachment.accept(attachment, this);
        StartRead(socket);
    }

    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        if (attachment.isOpen()) {
            try {
                attachment.close();
            } catch (IOException ignored) {}
        }

        ValidParams.Print("Remote Connecting Failed...");
    }

    private void StartRead(AsynchronousSocketChannel socket) {
        socket.read(clientBuffer, 30, TimeUnit.SECONDS, clientBuffer, new AioReadHandler(socket));
    }
}