package br.com.cadeiralivreempresaapi.modulos.empresa.model;

import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa.ATIVA;
import static br.com.cadeiralivreempresaapi.modulos.empresa.messages.EmpresaMessages.USUARIO_NAO_PROPRIETARIO;
import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPRESA")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @NotNull
    @JoinTable(name = "EMPRESA_SOCIOS", joinColumns = {
        @JoinColumn(name = "FK_EMPRESA", foreignKey = @ForeignKey(name = "FK_EMPRESA_PK"),
            referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "FK_USUARIO", foreignKey = @ForeignKey(name = "FK_USUARIO_PK"),
            referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Usuario> socios;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "RAZAO_SOCIAL", nullable = false)
    private String razaoSocial;

    @Column(name = "CNPJ", nullable = false)
    private String cnpj;

    @Column(name = "TIPO_EMPRESA", nullable = false)
    @Enumerated(EnumType.STRING)
    private ETipoEmpresa tipoEmpresa;

    @Column(name = "SITUACAO", nullable = false)
    @Enumerated(EnumType.STRING)
    private ESituacaoEmpresa situacao;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
        situacao = ATIVA;
    }

    public Empresa(Integer id) {
        this.id = id;
    }

    public static Empresa of(EmpresaRequest request) {
        var empresa = new Empresa();
        BeanUtils.copyProperties(request, empresa);
        return empresa;
    }

    public void adicionarProprietario(Usuario usuario) {
        validarPermissaoUsuarioProprietario(usuario);
        if (isEmpty(socios)) {
            socios = List.of(usuario);
        } else {
            socios.add(usuario);
        }
    }

    private void validarPermissaoUsuarioProprietario(Usuario usuario) {
        if (!usuario.isProprietario()) {
            throw USUARIO_NAO_PROPRIETARIO;
        }
    }
}
