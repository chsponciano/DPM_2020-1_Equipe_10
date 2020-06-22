package br.com.furb.dpm.dpmunidade04.controller.motion;

import br.com.furb.dpm.dpmunidade04.dto.CoordinatesDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMovement {
    CoordinatesDTO coordinatesDTO;
    boolean isInside;
}
