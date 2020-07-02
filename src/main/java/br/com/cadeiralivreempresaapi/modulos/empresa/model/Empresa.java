package br.com.cadeiralivreempresaapi.modulos.empresa.model;

import br.com.cadeiralivreempresaapi.modulos.empresa.dto.EmpresaRequest;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.enums.ETipoEmpresa;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static br.com.cadeiralivreempresaapi.modulos.empresa.enums.ESituacaoEmpresa.ATIVA;

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

    @ManyToOne
    @JoinColumn(name = "FK_USUARIO", nullable = false)
    private Usuario usuario;

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

    public static Empresa of(EmpresaRequest request, Usuario usuario) {
        var empresa = new Empresa();
        BeanUtils.copyProperties(request, empresa);
        empresa.setUsuario(usuario);
        return empresa;
    }
}
