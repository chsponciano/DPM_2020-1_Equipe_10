package br.com.furb.dpm.dpmunidade04.controller.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import br.com.furb.dpm.dpmunidade04.dto.CoordinatesDTO;
import br.com.furb.dpm.dpmunidade04.dto.DeviceDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceDescriptionDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceUseDTO;
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

    DeviceRepository deviceRepository;

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
        final String uri = "https://google.com/";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        System.out.println(result.getBody());
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
