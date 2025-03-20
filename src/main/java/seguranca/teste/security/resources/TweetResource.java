package seguranca.teste.security.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import seguranca.teste.security.domain.Roles;
import seguranca.teste.security.domain.Tweet;
import seguranca.teste.security.domain.Usuario;
import seguranca.teste.security.dto.CreateTweetDto;
import seguranca.teste.security.dto.FeedDto;
import seguranca.teste.security.dto.FeedItemDto;
import seguranca.teste.security.repositories.TweetRepository;
import seguranca.teste.security.repositories.UsuarioRepository;

@RestController
@RequestMapping("/tweets")
public class TweetResource {
	
	@Autowired
	private final TweetRepository tweetRepository;
	
	@Autowired
	private final UsuarioRepository usuarioRepository;

	public TweetResource(TweetRepository tweetRepository, UsuarioRepository usuarioRepository) {
		super();
		this.tweetRepository = tweetRepository;
		this.usuarioRepository = usuarioRepository;
	}
	
	@PostMapping("/save")
	public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken token){
		
		Optional<Usuario> user = Optional.empty();

		try {
		    var userId = Long.parseLong(token.getName());
		    user = usuarioRepository.findById(userId);
		} catch (NumberFormatException e) {
		    // Tratar erro se o valor de token.getName() não for um número válido.
		    System.err.println("Erro ao converter token para Long: " + e.getMessage());
		}

		// Verifique se o usuário foi encontrado antes de usá-lo
		if (user.isPresent()) {
		    var tweet = new Tweet();
		    tweet.setUsuario(user.get());
		    tweet.setContexto(createTweetDto.content());
		    
		    tweetRepository.save(tweet);
		} else {
		    // Trate o caso de usuário não encontrado
		    System.err.println("Usuário não encontrado com o ID fornecido.");
		}

		return ResponseEntity.ok().build();
	}
	
	//usuario admin consegue apagar todos os tweets
	@DeleteMapping("/del/{id}")
	public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token){
		
		Optional<Usuario> user = Optional.empty();
		try {
		    var userId = Long.parseLong(token.getName());
		    user = usuarioRepository.findById(userId);
		} catch (NumberFormatException e) {
		    // Tratar erro se o valor de token.getName() não for um número válido.
		    System.err.println("Erro ao converter token para Long: " + e.getMessage());
		}
		
		var tweet = tweetRepository.findById(tweetId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		var isAdmin = user.get().getRoles()
			.stream().anyMatch(role -> role.getNome().equalsIgnoreCase(Roles.Values.ADMIN.name()));
		
		try {
		    var userId = Long.parseLong(token.getName());  // Converte a String para Long
		    
		    if (isAdmin || tweet.getUsuario().getId().equals(userId)) {
		        tweetRepository.deleteById(tweetId);
		    } else {
		    	return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		    }
		} catch (NumberFormatException e) {
		    // Tratar erro se o valor de token.getName() não for um número válido.
		    System.err.println("Erro ao converter token para Long: " + e.getMessage());
		}
		
		return ResponseEntity.ok().build();
		
	}

	@GetMapping("/feed")
	public ResponseEntity<FeedDto> feed(@RequestParam(value= "page", defaultValue = "0") int page, 
										@RequestParam(value= "pageSize", defaultValue = "10") int pageSize){
		
		var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
				.map(tweet -> 
					new FeedItemDto(
						tweet.getTweetId(), 
						tweet.getContexto(), 
						tweet.getUsuario().getUserName())
					);
		
			return ResponseEntity.ok(new FeedDto(
					tweets.getContent(), 
					page, 
					pageSize, 
					tweets.getTotalPages(), 
					tweets.getTotalElements())
					);
		
		}
		
	
	}


