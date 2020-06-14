package br.com.furb.dpm.dpmunidade04.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.furb.dpm.dpmunidade04.document.UserDocument;

@Repository
public interface UserRepository extends CrudRepository<UserDocument, String> {

    /**
     * Busca um usuário usando seu nome de usuário
     *
     * @param username nome de usuário
     * @return o usuário encontrado
     */
    Optional<UserDocument> findByUsername(String username);

}
