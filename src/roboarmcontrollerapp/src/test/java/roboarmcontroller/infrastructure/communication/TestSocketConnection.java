package roboarmcontroller.infrastructure.communication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

// Class to test the communication with the C server.
// TODO Fix:  This does not belong here
public class TestSocketConnection {


    public static void main(String... args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 52333);
        OutputStream output = socket.getOutputStream();

        int[] intdata = new int[2];

        intdata[0] = 2;
        intdata[1] = 10000;
        byte[] data = convertIntArrayToByteArray(intdata);
        output.write(data);

        for (int x = 0; x < 1000000; x++) {
            intdata[0] = 1;
            intdata[1] = 1;
            data = convertIntArrayToByteArray(intdata);
            output.write(data);
            Thread.sleep(1);

        }
        output.flush();
        output.close();
        socket.close();
    }


    private static byte[] convertIntArrayToByteArray(int[] data) {
        if (data == null) return null;
        // ----------
        byte[] bytes = new byte[data.length * 4];
        for (int i = 0; i < data.length; i++)
            System.arraycopy(intToBytes(data[i]), 0, bytes, i * 4, 4);
        return bytes;
    }

    private static byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);

        return bb.array();
    }

}
