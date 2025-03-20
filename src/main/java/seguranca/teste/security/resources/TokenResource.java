package seguranca.teste.security.resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import seguranca.teste.security.dto.LoginRequest;
import seguranca.teste.security.dto.LoginResponse;
import seguranca.teste.security.services.TokenService;

@RestController
public class TokenResource {
	
	@Autowired
	private final TokenService tokenService;
	
	public TokenResource(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Delegar a lógica de login para o TokenService
        LoginResponse loginResponse = tokenService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
	
}
