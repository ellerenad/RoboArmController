package roboarmcontroller.domain.gateways;

import roboarmcontroller.domain.dom.Command;

public interface SimulationGateway {
    void send(Command command);
}
