package br.com.furb.dpm.dpmunidade04.controller.security;

public interface SecurityController {

    /**
     * Gera um token para o usuário/senha, caso estes sejam validos
     *
     * @param username nome de usuário
     * @param password senha
     * @return o token gerado
     * @throws TokenGenerationException caso ocorra algum erro na geração do token
     */
    String generateToken(String username, String password) throws TokenGenerationException;

    /**
     * Criptografa a senha passada usando o algoritmo, o segredo e o tempo de expiração do sistema
     *
     * @param password senha que será criptografada
     * @return senha criptografada
     */
    String encodePassword(String password);
}
