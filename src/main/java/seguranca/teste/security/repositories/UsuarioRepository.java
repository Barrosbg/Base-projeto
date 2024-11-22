package seguranca.teste.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import seguranca.teste.security.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
