package br.com.furb.dpm.dpmunidade04.controller.motion;

import java.util.HashMap;
import java.util.Map;

public abstract class MotionChange {
    private static Map<String, UserMovement> motions = new HashMap<>();

    public static boolean onChange(UserMovement motion) {
        UserMovement userMovement = motions.get(motion.getCoordinatesDTO().getUsername());
        boolean equals = false;
        if (userMovement != null) {
            if (userMovement.isInside() == motion.isInside()) {
                equals = true;
            } else {
                motions.replace(motion.getCoordinatesDTO().getUsername(), motion);
            }
        } else {
            motions.put(motion.getCoordinatesDTO().getUsername(), motion);
        }

        return equals;
    }
}
