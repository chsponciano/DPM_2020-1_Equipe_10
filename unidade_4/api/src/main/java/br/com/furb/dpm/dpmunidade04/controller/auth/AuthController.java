package br.com.furb.dpm.dpmunidade04.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.furb.dpm.dpmunidade04.dto.CredentialsDTO;
import br.com.furb.dpm.dpmunidade04.dto.LoginResponseDTO;

@RequestMapping("/auth")
public interface AuthController {

    /**
     * Usa as credenciais do usuário para gerar um token e realizar o login no sistema
     *
     * @param credentialsDTO dto contendo o nome de usuário e senha
     * @return token de autenticação
     */
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody CredentialsDTO credentialsDTO);
}
