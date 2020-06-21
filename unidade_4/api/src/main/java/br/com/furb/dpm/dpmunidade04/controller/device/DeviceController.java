package br.com.furb.dpm.dpmunidade04.controller.device;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.furb.dpm.dpmunidade04.dto.CoordinatesDTO;
import br.com.furb.dpm.dpmunidade04.dto.DeviceDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceDescriptionDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostDeviceUseDTO;

@RequestMapping("/device")
public interface DeviceController {

    @GetMapping
    ResponseEntity<List<DeviceDTO>> getAll();

    @PostMapping
    ResponseEntity<DeviceDTO> create(@RequestBody PostDeviceDTO postDeviceDTO);

    @PostMapping("/{id}/use")
    ResponseEntity<DeviceDTO> updateUse(@PathVariable String id, @RequestBody PostDeviceUseDTO postDeviceUseDTO);

    @PostMapping("/{id}/description")
    ResponseEntity<DeviceDTO> updateDescription(@PathVariable String id, @RequestBody PostDeviceDescriptionDTO postDeviceDescriptionDTO);

    @PostMapping("/checkLocation")
    void checkLocation(@RequestBody CoordinatesDTO coordinatesDTO);

}
