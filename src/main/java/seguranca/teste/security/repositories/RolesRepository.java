package seguranca.teste.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import seguranca.teste.security.domain.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long>{
	
	Roles findBynome(String name);
	Optional<Roles> findById(Long id);

}
