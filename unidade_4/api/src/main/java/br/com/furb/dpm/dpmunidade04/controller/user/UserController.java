package br.com.furb.dpm.dpmunidade04.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.furb.dpm.dpmunidade04.document.UserDocument;
import br.com.furb.dpm.dpmunidade04.dto.PostUserDTO;
import br.com.furb.dpm.dpmunidade04.dto.UserDTO;

@RequestMapping("/user")
public interface UserController {

    /**
     * Path usado para recuperar um usuário usando seu id
     *
     * @param id id do usuário
     * @return o documento referente ao usuário
     */
    @GetMapping("/{id}")
    ResponseEntity<UserDocument> getUser(@PathVariable String id);

    /**
     * Path usado para cadastrar um usuário na base
     *
     * @param postUserDTO dto contendo os dados que serão cadastrados
     * @return o documento referente ao usuário cadastrado
     */
    @PostMapping
    ResponseEntity<UserDTO> postUser(@RequestBody PostUserDTO postUserDTO);
}
