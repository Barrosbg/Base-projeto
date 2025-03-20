package seguranca.teste.security.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import seguranca.teste.security.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional <Usuario> findByUserName(String userName);
	
	Optional <Usuario> findByEmail(String email);
	
	@Query("SELECT u FROM Usuario u " +
	           "WHERE (:userName IS NULL OR u.userName = :userName) " +
	           "AND (:email IS NULL OR u.email = :email) " +
	           "AND (:status IS NULL OR u.status = :status) " +
	           "AND (:cpf IS NULL OR u.cpf = :cpf) " +
	           "AND (:nome IS NULL OR LOWER(u.nomeCompleto) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
	           "AND (:roleName IS NULL OR EXISTS (SELECT r FROM u.roles r WHERE r.nome = :roleName))")
	    List<Usuario> findUsuariosByFilters(@Param("userName") String userName, @Param("email") String email, @Param("status") String status, 
	                                        @Param("cpf") String cpf, @Param("nome") String nome, @Param("roleName") String roleName);
	
	@Query("SELECT u FROM Usuario u WHERE u.dataCriacao >= :dataInicio AND u.dataCriacao <= :dataFim")
    List<Usuario> findUsuariosCriadosEntre(@Param("dataInicio") LocalDateTime dataInicio, 
                                           @Param("dataFim") LocalDateTime dataFim); // Buscar usuários criados em um período

}
