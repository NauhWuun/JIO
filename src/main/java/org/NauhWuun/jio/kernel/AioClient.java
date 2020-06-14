package org.NauhWuun.jio.kernel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AioClient extends Thread 
{
    private final AsynchronousSocketChannel client;

    private ByteBuffer RecvBuffer = ByteBuffer.allocate(8192);

    public AioClient() throws IOException {
        client = AsynchronousSocketChannel.open();
    }

    public boolean Start(final String ip, final int port) throws IOException {
        ValidParams.IsEmpty(ip, "please check dest ip value");
        ValidParams.IsLessEqual(port, "please check dest port value");

        Future<Void> connetional = client.connect(new InetSocketAddress(ip, port));
        if (connetional.isCancelled()) {
            client.close();
            return false;
        }

        return true;
    }

    public void Send(ByteBuffer sendBuffer) throws InterruptedException, ExecutionException, IOException {
        Integer finish = client.write(sendBuffer).get();

        if (finish == -1)
            client.close();
    }

    public void run() {
        client.read(RecvBuffer, RecvBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (result > 0) {
                    buffer.flip();

                    client.read(buffer, buffer, this);
                } else if (result == -1) {
                    try {
                        ValidParams.Print("the dest object is closed");
                        client.close();
                    } catch (IOException e) {
                        ValidParams.Print(e.getMessage());
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                RecvBuffer.clear();
            }
        });
    }

    public void ShutDown() throws IOException {
        if (client.isOpen())
            client.close();

        RecvBuffer.clear();
    }
}