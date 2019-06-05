package roboarmcontroller.domain.services.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class CommandExecutor {
    private final static int SERVER_PORT = 52333;
    private final static String SERVER_HOST = "localhost";

    private final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    private OutputStream output;
    private int[] intData = new int[2];
    private byte[] bytes = new byte[intData.length * 4];

    public void execute(CommandParameters commandParameters) {
        if (output == null) {
            this.initSocket();
        }

        intData[0] = commandParameters.getServoId();
        intData[1] = commandParameters.getDelta();
        byte[] data = convertIntArrayToByteArray(intData);
        try {
            output.write(data);
        } catch (IOException e) {
            log.error("Problem on writing to the socket");
        }
    }

    private void initSocket() {
        Socket socket;
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            output = socket.getOutputStream();
        } catch (IOException e) {
            log.error("Exception opening the socket", e);
            throw new RuntimeException("Exception opening the socket");
        }
    }

    private byte[] convertIntArrayToByteArray(int[] data) {
        if (data == null) return null;
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(intToBytes(data[i]), 0, bytes, i * 4, 4);
        }
        return bytes;
    }

    private byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }
}
