package roboarmcontroller.domain.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ExitService {

    public void terminateProgram(int code) {
        System.exit(code);
    }
}
