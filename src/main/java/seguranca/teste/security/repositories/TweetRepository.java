package seguranca.teste.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import seguranca.teste.security.domain.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long>{

}
