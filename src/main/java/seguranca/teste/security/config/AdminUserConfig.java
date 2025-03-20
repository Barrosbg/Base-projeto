package seguranca.teste.security.config;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
import seguranca.teste.security.domain.Roles;
import seguranca.teste.security.domain.Usuario;
import seguranca.teste.security.repositories.RolesRepository;
import seguranca.teste.security.repositories.UsuarioRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RolesRepository rolesRepository;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminUserConfig(RolesRepository rolesRepository, UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = rolesRepository.findBynome(Roles.Values.ADMIN.name());
        var userAdmin = usuarioRepository.findByUserName("admin");
        if (roleAdmin == null) {
            throw new IllegalStateException("Role 'ADMIN' não encontrada. Verifique se ela foi criada no banco.");
        }

        userAdmin.ifPresentOrElse(
            user -> {
                // System.out.println("Admin já existe!");
            },
            () -> {
                var user = new Usuario();
                user.setUserName("admin");
                user.setPassword(bCryptPasswordEncoder.encode("123"));
                user.setEmail("barrosbg@gmail.com");
                user.setNomeCompleto("Administrador do Sistema");
                user.setCpf("00000000000"); // Ajuste conforme necessário
                user.setTelefone("(00) 00000-0000"); // Ajuste conforme necessário
                user.setStatus("ATIVO");
                user.setDataCriacao(LocalDateTime.now());
                user.setRoles(Set.of(roleAdmin));

                usuarioRepository.save(user);
            }
        );
    }
}