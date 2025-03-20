package seguranca.teste.security.dto;

public record RolesDto(Long id, String nome) {
    public Long id() {
        return this.id;
    }
}
