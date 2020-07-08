package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.funcionario.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AGENDA")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_FUNCIONARIO")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "FK_HORARIO", nullable = false)
    private Horario horario;

    @Column(name = "SITUACAO", nullable = false)
    @Enumerated(EnumType.STRING)
    private ESituacaoAgenda situacao;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "DESCRICAO_SERVICO", nullable = false)
    private String descricaoServico;

    @Column(name = "CLIENTE_ID")
    private String clienteId;

    @Column(name = "CLIENTE_NOME")
    private String clienteNome;

    @Column(name = "CLIENTE_EMAIL")
    private String clienteEmail;

    @Column(name = "CLIENTE_CPF")
    private String clienteCpf;

    @Column(name = "PRECO", nullable = false)
    private Double preco;

    @Column(name = "DESCONTO")
    private Float desconto;
}