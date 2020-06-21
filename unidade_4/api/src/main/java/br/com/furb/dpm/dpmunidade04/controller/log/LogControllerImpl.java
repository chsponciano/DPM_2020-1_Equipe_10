package br.com.furb.dpm.dpmunidade04.controller.log;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.furb.dpm.dpmunidade04.document.LogDocument;
import br.com.furb.dpm.dpmunidade04.dto.LogDTO;
import br.com.furb.dpm.dpmunidade04.dto.PostLogDTO;
import br.com.furb.dpm.dpmunidade04.repository.LogRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class LogControllerImpl implements LogController {

    LogRepository logRepository;

    @Override
    public ResponseEntity<List<LogDTO>> getAll() {
        var result = new ArrayList<LogDTO>();
        logRepository.findAll().forEach(deviceDocument -> {
            result.add(toLogDTO(deviceDocument));
        });
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<LogDTO> create(PostLogDTO postLogDTO) {
        var logDocument = LogDocument.builder().log(postLogDTO.getLog()).build();
        logRepository.save(logDocument);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/log/{id}").buildAndExpand(logDocument.getId()).toUri()).build();
    }

    private LogDTO toLogDTO(LogDocument deviceDocument) {
        return LogDTO.builder().id(deviceDocument.getId()).log(deviceDocument.getLog()).build();
    }

}
