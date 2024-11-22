package seguranca.teste.security.domain;

import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tweets")
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "tweet_id")
	private Long tweetId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Usuario usuario;
	
	@Column(name = "content")
	private String contexto;
	
	@CreationTimestamp
	@Column(name = "creation_timestamp")
	private Instant creationTimestamp;

	public Tweet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public Instant getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Instant creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}	
	
}
