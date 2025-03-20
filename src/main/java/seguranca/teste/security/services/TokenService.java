package seguranca.teste.security.services;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import seguranca.teste.security.domain.Usuario;
import seguranca.teste.security.dto.LoginRequest;
import seguranca.teste.security.dto.LoginResponse;
import seguranca.teste.security.repositories.UsuarioRepository;

@Service
public class TokenService {

    private final UsuarioRepository usuarioRepository;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenService(UsuarioRepository usuarioRepository, JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Verifica se o usuário existe pelo username
        Optional<Usuario> user = usuarioRepository.findByUserName(loginRequest.username());

        // Caso o usuário não exista ou a senha esteja incorreta
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválido!");
        }

        // Gera o JWT
        var now = Instant.now();
        var expiresIn = 1800000L; // Expires in 30 minutes

        // Coleta as roles do usuário para definir as permissões (scopes)
        var scopes = user.get().getRoles()
                .stream()
                .map(role -> "ROLE_" + role.getNome()) // Adiciona o prefixo "ROLE_"
                .collect(Collectors.joining(" "));

        // Cria as claims (informações) do token JWT
        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scopes) // Inclui as roles no token
                .build();

     // Gera o token JWT
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // Exibe o token JWT decodificado no console
//        System.out.println("Token JWT gerado: " + jwtValue);
        System.out.println("Payload do token JWT: " + claims.getClaims());

        return new LoginResponse(jwtValue, expiresIn);
    }
}