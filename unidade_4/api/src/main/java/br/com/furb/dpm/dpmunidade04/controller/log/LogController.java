package br.com.furb.dpm.dpmunidade04.controller.log;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.furb.dpm.dpmunidade04.dto.LogDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostLogDTO;

@RequestMapping("/log")
public interface LogController {

    @GetMapping
    ResponseEntity<List<LogDTO>> getAll();

    @PostMapping
    ResponseEntity<LogDTO> create(@RequestBody PostLogDTO postLogDTO);

}
