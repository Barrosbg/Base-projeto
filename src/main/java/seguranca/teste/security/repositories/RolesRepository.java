package seguranca.teste.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import seguranca.teste.security.domain.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long>{

}
