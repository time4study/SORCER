package sorcer.provider.exchange.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sorcer.provider.exchange.IpcArray;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;

/**
 * Created by sobolemw on 9/21/15.
 */
public class IpcArrayBean implements IpcArray, Serializable {

    private static Logger logger = LoggerFactory.getLogger(IpcArrayBean.class.getName());

    private int LENGTH = 256;

    private int BB_SIZE = 1024;

    private String hostName;

    private int port;

    public int getPort() throws RemoteException {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostName() throws RemoteException {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public int[] ipcIntegerArray(int[] in) throws RemoteException, IOException {
        ByteBuffer bb = ByteBuffer.allocate(BB_SIZE);
        IntBuffer ib = bb.asIntBuffer();
        ib.put(in);

        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("127.0.0.1", 9010));
        bb.clear();
        while (bb.hasRemaining()) {
            channel.write(bb);
        }
//		logger.info("written array: " + Arrays.toString(bb.array()));
        bb.clear();
        int readBytes = 0;
        while (readBytes != -1 && readBytes < BB_SIZE) {
            readBytes = channel.read(bb);
        }
//		logger.info("read bytes: " + readBytes);
//		logger.info("read array: " + Arrays.toString(bb.array()));

        int[] out = new int[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            out[i] = ib.get(i);
        }
        channel.close();
        return out;
    }

    @Override
    public int[] ipcDoubleArray(int[] in) throws RemoteException, IOException {
        return new int[0];
    }

}
