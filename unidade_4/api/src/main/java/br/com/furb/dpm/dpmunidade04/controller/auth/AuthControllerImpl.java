package br.com.furb.dpm.dpmunidade04.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.furb.dpm.dpmunidade04.controller.security.SecurityController;
import br.com.furb.dpm.dpmunidade04.dto.CredentialsDTO;
import br.com.furb.dpm.dpmunidade04.dto.LoginResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuthControllerImpl implements AuthController {

    SecurityController securityController;

    @Override
    public ResponseEntity<LoginResponseDTO> login(@RequestBody CredentialsDTO credentialsDTO) {
        try {
            var token = securityController.generateToken(credentialsDTO.getUsername(), credentialsDTO.getPassword());
            return ResponseEntity.ok(LoginResponseDTO.builder().success(true).token(token).build());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //
                    .body(LoginResponseDTO.builder().success(false).message("Invalid credentials").build());
        }
    }

}
