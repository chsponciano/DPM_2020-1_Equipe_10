package br.com.furb.dpm.dpmunidade04.controller.device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.furb.dpm.dpmunidade04.controller.log.LogControllerImpl;
import br.com.furb.dpm.dpmunidade04.controller.motion.MotionChange;
import br.com.furb.dpm.dpmunidade04.controller.motion.UserMovement;
import br.com.furb.dpm.dpmunidade04.document.LogDocument;
import br.com.furb.dpm.dpmunidade04.dto.*;
import br.com.furb.dpm.dpmunidade04.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.furb.dpm.dpmunidade04.document.DeviceDocument;
import br.com.furb.dpm.dpmunidade04.repository.DeviceRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class DeviceControllerImpl implements DeviceController {

    private final String distanceArduinoHost = "http://%s/arduino/distance/%s/%s";
    private static boolean isInside = false;
    DeviceRepository deviceRepository;
    LogRepository logRepository;

    @Override
    public ResponseEntity<List<DeviceDTO>> getAll() {
        var result = new ArrayList<DeviceDTO>();
        deviceRepository.findAll().forEach(deviceDocument -> {
            result.add(toDeviceDTO(deviceDocument));
        });
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<DeviceDTO> create(PostDeviceDTO postDeviceDTO) {
        var deviceDocument = DeviceDocument.builder() //
                .use(postDeviceDTO.isUse()) //
                .description(postDeviceDTO.getDescription()) //
                .ip(postDeviceDTO.getIp()) //
                .mac(postDeviceDTO.getMac()) //
                .build();
        deviceRepository.save(deviceDocument);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/device/{id}").buildAndExpand(deviceDocument.getId()).toUri()).build();
    }

    @Override
    public ResponseEntity<DeviceDTO> updateUse(String id, PostDeviceUseDTO postDeviceUseDTO) {
        return deviceRepository.findById(id).map(deviceDocument -> {
            deviceDocument.setUse(postDeviceUseDTO.isUse());
            deviceRepository.save(deviceDocument);
            return ResponseEntity.ok(toDeviceDTO(deviceDocument));
        }).orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<DeviceDTO> updateDescription(String id, PostDeviceDescriptionDTO postDeviceDescriptionDTO) {
        return deviceRepository.findById(id).map(deviceDocument -> {
            deviceDocument.setDescription(postDeviceDescriptionDTO.getDescription());
            deviceRepository.save(deviceDocument);
            return ResponseEntity.ok(toDeviceDTO(deviceDocument));
        }).orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public void checkLocation(CoordinatesDTO coordinatesDTO) {
        this.getAll().getBody().forEach(deviceDTO -> {
            if (deviceDTO.isUse()) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<String> result = restTemplate.exchange(
                        String.format(this.distanceArduinoHost, deviceDTO.getIp(), coordinatesDTO.getLatitude(), coordinatesDTO.getLongitude()),
                        HttpMethod.GET, entity, String.class);

                float distance = Float.parseFloat(result.getBody());
                boolean isInside = distance <= 29.0;
                if (MotionChange.onChange(new UserMovement(coordinatesDTO, isInside))) {
                    new LogControllerImpl(logRepository).create(new PostLogDTO(String.format(LogDocument.INTERNAL_LOG,
                            coordinatesDTO.getUsername(), (isInside) ? "DENTRO" : "FORA", LogDocument.getCurrentDatetime())));
                }
            }
        });
    }

    private DeviceDTO toDeviceDTO(DeviceDocument deviceDocument) {
        return DeviceDTO.builder() //
                .use(deviceDocument.isUse()) //
                .description(deviceDocument.getDescription()) //
                .id(deviceDocument.getId()) //
                .ip(deviceDocument.getIp()) //
                .mac(deviceDocument.getMac()) //
                .build();
    }
}
