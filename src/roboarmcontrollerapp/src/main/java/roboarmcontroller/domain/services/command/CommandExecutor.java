package roboarmcontroller.domain.services.command;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

// Code partially taken from https://stackoverflow.com/a/45376510
// This implementation should be in communication, but as the POC showed several performance problems, this approach is abandoned.
public class CommandExecutor {

    private final Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    // private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    private static final String DIR_PATH = "/home/kike/Documents/development/RoboArmController/src/RoboArmSimulation/plugins/build";

    // remote_ctrl_robo_arm <ServoNumber:1,2,3> <delta:float>
    private final String commandPattern = "/home/kike/Documents/development/RoboArmController/src/RoboArmSimulation/plugins/build/remote_ctrl_robo_arm %d %.2f";


    public void execute(Parameters parameters) {
        try {
            String command = String.format(commandPattern, parameters.getServoId(), parameters.getDelta());
            log.info("Executing command, command {}", command);


            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }

            b.close();

            // assert exitCode == 0;
        } catch (Exception e) {
            log.info("Exception:{}", e.getMessage());
        }
    }


    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

}
