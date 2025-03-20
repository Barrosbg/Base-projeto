package seguranca.teste.security.dto;

import java.util.Set;

public class UsuarioDto {

	// DTO para criar um novo usuário
	public record CreateUsuarioDto(
		    String userName,
		    String email,
		    String senha,
		    String nomeCompleto,
		    String cpf,
		    String telefone,
		    Set<RolesDto> roles
		) {
		    public Set<RolesDto> roles() {
		        return this.roles;
		    }
		}

    // DTO para atualizar um usuário existente
    public record UpdateUsuarioDto(
        String userName,
        String email,
        String nomeCompleto,
        String cpf,
	    String telefone,
	    Set<RolesDto> roles
    ) {}

    // DTO para alterar a senha de um usuário
    public record AlterarSenhaDto(
        String senhaAntiga,
        String novaSenha
    ) {}
    
}
