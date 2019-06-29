package roboarmcontroller.infrastructure.communication.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import roboarmcontroller.domain.dom.Command;
import roboarmcontroller.domain.gateways.SimulationGateway;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

@Component
public class SimulationGatewayTCP implements SimulationGateway {
    private final static int SERVER_PORT = 52333;
    private final static String SERVER_HOST = "localhost";

    private final Logger log = LoggerFactory.getLogger(SimulationGatewayTCP.class);
    private OutputStream output;
    private int[] intData = new int[2];
    private byte[] bytes = new byte[intData.length * 4];

    @Override
    public void send(Command command) {
        if (output == null) {
            this.initSocket();
        }

        intData[0] = command.getServoId();
        intData[1] = command.getDelta();
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
