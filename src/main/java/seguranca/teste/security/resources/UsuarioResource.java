package seguranca.teste.security.resources;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seguranca.teste.security.domain.Usuario;
import seguranca.teste.security.dto.UsuarioDto;
import seguranca.teste.security.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para salvar um novo usuário.ok
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody UsuarioDto.CreateUsuarioDto usuarioDto) {
        Usuario usuario = usuarioService.salvarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    //------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para atualizar um usuário existente.ok
    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDto.UpdateUsuarioDto usuarioDto) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    //------------------------------------------------------------------------------------------------------------------------------
    // Endpoint para alterar a senha de um usuário.ok
    @PutMapping("/pass-update/{id}")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody UsuarioDto.AlterarSenhaDto alterarSenhaDto) {
        usuarioService.alterarSenha(id, alterarSenhaDto);
        return ResponseEntity.ok().build();
    }

    //------------------------------------------------------------------------------------------------------------------------------
//    @GetMapping("/find-by-id/{id}")
//    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
//        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
//        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
    
    // Endpoint para buscar um usuário por ID
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id, Authentication authentication) {
        // Pegando o "sub" do token JWT (que é o ID do usuário logado)
        Long usuarioIdLogado = Long.parseLong(authentication.getName());

        // Se for o próprio usuário ou um ADMIN, permitir a busca
        if (id.equals(usuarioIdLogado) || authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return usuarioService.buscarUsuarioPorId(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }

        // Se não for o próprio usuário nem ADMIN, retorna 403 (Forbidden)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    // Endpoint para listar todos os usuários ---------------------------------------------------------------------------------------
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<Usuario>> buscarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para excluir (marcar como inativo) um usuário
    //------------------------------------------------------------------------------------------------------------------------------
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
