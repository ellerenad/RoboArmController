package roboarmcontroller.infrastructure.communication.parser;

import org.springframework.stereotype.Component;
import roboarmcontroller.domain.dom.hands.*;
import roboarmcontroller.infrastructure.communication.input.dto.JsonHand;
import roboarmcontroller.infrastructure.communication.input.dto.JsonPointable;
import roboarmcontroller.infrastructure.communication.input.dto.JsonTrackingFrame;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoToDomainMapper {

    private static final FingerType[] FINGER_TYPES = FingerType.values();

    public TrackingFrame map(JsonTrackingFrame jsonTrackingFrame) {
        TrackingFrame trackingFrame = new TrackingFrame();

        trackingFrame.setId(jsonTrackingFrame.getId());
        trackingFrame.setTimestamp(jsonTrackingFrame.getTimestamp());
        List<Hand> hands = this.getHands(jsonTrackingFrame);
        trackingFrame.setHands(hands);

        return trackingFrame;
    }

    private List<Hand> getHands(JsonTrackingFrame jsonTrackingFrame) {
        List<Hand> hands = jsonTrackingFrame.getJsonHands().stream()
                .map(jsonHand -> this.getHand(jsonHand, jsonTrackingFrame.getJsonPointables()))
                .collect(Collectors.toList());

        return hands;
    }

    private Hand getHand(JsonHand jsonHand, List<JsonPointable> allPointables) {
        Hand hand = new Hand();
        hand.setId(jsonHand.getId());
        hand.setConfidence(jsonHand.getConfidence());

        HandType handType = HandType.valueOf(jsonHand.getType().toUpperCase());
        hand.setType(handType);

        List<Finger> fingers = this.getFingers(jsonHand.getId(), allPointables);
        hand.setFingers(fingers);
        return hand;
    }

    private List<Finger> getFingers(int handId, List<JsonPointable> jsonPointables) {
        List<Finger> fingers;

        fingers = jsonPointables.stream()
                .filter(jsonPointable -> jsonPointable.getHandId() == handId)
                .map(this::getFinger)
                .collect(Collectors.toList());

        return fingers;
    }

    private Finger getFinger(JsonPointable jsonPointable) {
        Finger finger = new Finger();

        finger.setId(jsonPointable.getId());
        finger.setHandId(jsonPointable.getHandId());
        finger.setExtended(jsonPointable.isExtended());

        int fingerTypeIndex = jsonPointable.getType();
        finger.setType(FINGER_TYPES[fingerTypeIndex]);

        FingerPosition fingerPosition = new FingerPosition(jsonPointable.getStabilizedTipPosition());
        finger.setPosition(fingerPosition);

        return finger;
    }

}
