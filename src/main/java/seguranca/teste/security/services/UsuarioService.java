package seguranca.teste.security.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import seguranca.teste.security.domain.Roles;
import seguranca.teste.security.domain.Usuario;
import seguranca.teste.security.dto.UsuarioDto;
import seguranca.teste.security.repositories.RolesRepository;
import seguranca.teste.security.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }
    
    //Salvar
    public Usuario salvarUsuario(UsuarioDto.CreateUsuarioDto usuarioDto) {
        // Busca as roles no banco com base nos IDs informados no DTO
        Set<Roles> roles = usuarioDto.roles().stream()
            .map(roleDto -> {
                Long roleId = roleDto.id(); // Extrai o ID do roleDto
                return rolesRepository.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role não encontrada: " + roleId));
            })
            .collect(Collectors.toSet());

        // Cria um novo usuário com os dados do DTO e já define as roles
        Usuario usuario = new Usuario(
            usuarioDto.userName(),
            usuarioDto.email(),
            passwordEncoder.encode(usuarioDto.senha()),
            usuarioDto.nomeCompleto(),
            usuarioDto.cpf(),
            usuarioDto.telefone(),
            roles  // Passando as roles diretamente no construtor
        );

        return usuarioRepository.save(usuario);
    }
    
    // Método para atualizar um usuário existente
 // Método para atualizar um usuário existente
    public Usuario atualizarUsuario(Long id, UsuarioDto.UpdateUsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setUserName(usuarioDto.userName());
        usuario.setEmail(usuarioDto.email());
        usuario.setNomeCompleto(usuarioDto.nomeCompleto());
        usuario.setCpf(usuarioDto.cpf());
        usuario.setTelefone(usuarioDto.telefone());

        // Se houver roles no DTO, atualiza as roles do usuário
        if (usuarioDto.roles() != null && !usuarioDto.roles().isEmpty()) {
            Set<Roles> roles = usuarioDto.roles().stream()
                .map(roleDto -> rolesRepository.findById(roleDto.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role não encontrada: " + roleDto.id())))
                .collect(Collectors.toSet());

            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }

    // Método para alterar a senha de um usuário
    public void alterarSenha(Long id, UsuarioDto.AlterarSenhaDto alterarSenhaDto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Comparar a senha antiga com a senha armazenada criptografada
        if (!passwordEncoder.matches(alterarSenhaDto.senhaAntiga(), usuario.getPassword())) {
            throw new BadCredentialsException("Senha antiga incorreta!");
        }

        // Criptografar a nova senha antes de salvar
        usuario.setPassword(passwordEncoder.encode(alterarSenhaDto.novaSenha()));
        usuarioRepository.save(usuario);
    }
    
    // Localizar um usuário por ID
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Localizar todos os usuários
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Excluir (marcar como inativo) um usuário
    public void excluirUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            user.setStatus("INATIVO");  // Alterar o status para INATIVO
            usuarioRepository.save(user);
        }
    }
}
