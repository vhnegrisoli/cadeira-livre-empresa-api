package br.com.cadeiralivreempresaapi.config.auth;

import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

public class UserDetailsImpl extends User implements Serializable {

    private static final long serialVersionUID = -6234411737916275471L;

    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private LocalDateTime ultimoAcesso;

    public UserDetailsImpl(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        this(usuario, true, true, true, true, authorities);
    }

    public UserDetailsImpl(Usuario usuario, boolean enabled, boolean accountNonExpired,
                           boolean credentialsNonExpired, boolean accountNonLocked,
                           Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getEmail(), usuario.getSenha(), enabled, accountNonExpired, credentialsNonExpired,
            accountNonLocked, authorities);
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCpf();
        this.ultimoAcesso = usuario.getUltimoAcesso();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}
