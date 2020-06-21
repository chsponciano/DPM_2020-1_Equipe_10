package br.com.furb.dpm.dpmunidade04.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.furb.dpm.dpmunidade04.controller.security.SecurityController;
import br.com.furb.dpm.dpmunidade04.document.UserDocument;
import br.com.furb.dpm.dpmunidade04.dto.PostUserDTO;
import br.com.furb.dpm.dpmunidade04.dto.UserDTO;
import br.com.furb.dpm.dpmunidade04.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserControllerImpl implements UserController {

    SecurityController securityController;
    UserRepository userRepository;

    @Override
    public ResponseEntity<UserDocument> getUser(@PathVariable String id) {
        return userRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<UserDTO> postUser(@RequestBody PostUserDTO postUserDTO) {
        var userDocument = UserDocument.builder() //
                .name(postUserDTO.getName()) //
                .email(postUserDTO.getEmail()) //
                .username(postUserDTO.getUsername()) //
                .password(securityController.encodePassword(postUserDTO.getPassword())) //
                .build();
        userDocument = userRepository.save(userDocument);
        var userDTO = UserDTO.builder() //
                .id(userDocument.getId()) //
                .name(userDocument.getName()) //
                .email(userDocument.getEmail()) //
                .username(userDocument.getUsername()) //
                .build();
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/user/{id}").buildAndExpand(userDocument.getId()).toUri()).body(userDTO);
    }

}
