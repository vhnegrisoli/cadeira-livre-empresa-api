package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ETipoAgenda;
import br.com.cadeiralivreempresaapi.modulos.funcionario.model.Funcionario;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

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
    @JoinColumn(name = "FK_HORARIO")
    private Horario horario;

    @NotNull
    @JoinTable(name = "AGENDA_SERVICOS", joinColumns = {
        @JoinColumn(name = "FK_AGENDA", foreignKey = @ForeignKey(name = "FK_AGENDA_ID"),
            referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "FK_SERVICO", foreignKey = @ForeignKey(name = "FK_SERVICO_ID"),
            referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Servico> servicos;

    @Column(name = "HORARIO_AGENDAMENTO", nullable = false)
    private LocalTime horarioAgendamento;

    @Column(name = "SITUACAO", nullable = false)
    @Enumerated(EnumType.STRING)
    private ESituacaoAgenda situacao;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "CLIENTE_ID")
    private String clienteId;

    @Column(name = "CLIENTE_NOME")
    private String clienteNome;

    @Column(name = "CLIENTE_EMAIL")
    private String clienteEmail;

    @Column(name = "CLIENTE_CPF")
    private String clienteCpf;

    @Column(name = "TOTAL", nullable = false)
    private Double total;

    @Column(name = "DESCONTO")
    private Float desconto;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_AGENDA", nullable = false)
    private ETipoAgenda tipoAgenda;
}