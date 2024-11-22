package seguranca.teste.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Roles {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;
	
	@Column(name = "name")
	private String nome;

	public Roles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public enum Values{
		BASIC(1L),
		ADMIN(2L);
		
		long roleId;

		Values(long roleId) {
			this.roleId = roleId;
		}
	}
	
	public long getRoleId() {
		return getRoleId();
	}
}
