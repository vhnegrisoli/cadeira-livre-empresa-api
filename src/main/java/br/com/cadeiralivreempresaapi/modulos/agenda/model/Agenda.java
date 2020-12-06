package br.com.cadeiralivreempresaapi.modulos.agenda.model;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.AgendaRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ESituacaoAgenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.enums.ETipoAgenda;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.model.Usuario;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.comum.util.Constantes.PERCENTUAL;
import static org.springframework.util.ObjectUtils.isEmpty;


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
    @JoinColumn(name = "FK_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "FK_EMPRESA")
    private Empresa empresa;

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
    private Integer clienteId;

    @Column(name = "CLIENTE_NOME")
    private String clienteNome;

    @Column(name = "CLIENTE_EMAIL")
    private String clienteEmail;

    @Column(name = "CLIENTE_CPF")
    private String clienteCpf;

    @Column(name = "TOTAL_DESCONTO", nullable = false)
    private Double totalDesconto;

    @Column(name = "TOTAL_PAGAMENTO", nullable = false)
    private Double totalPagamento;

    @Column(name = "DESCONTO")
    private Float desconto;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_AGENDA", nullable = false)
    private ETipoAgenda tipoAgenda;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }

    public static Agenda of(AgendaRequest request) {
        var cliente = request.getCliente();
        return Agenda
            .builder()
            .clienteId(cliente.getId())
            .clienteNome(cliente.getNome())
            .clienteEmail(cliente.getEmail())
            .clienteCpf(cliente.getCpf())
            .servicos(request
                .getServicosIds()
                .stream()
                .map(Servico::new)
                .collect(Collectors.toSet()))
            .horario(new Horario(request.getHorarioId()))
            .situacao(ESituacaoAgenda.RESERVA)
            .tipoAgenda(ETipoAgenda.HORARIO_MARCADO)
            .empresa(new Empresa(request.getEmpresaId()))
            .build();
    }

    public static Agenda of(CadeiraLivreRequest request,
                            UsuarioAutenticado usuario,
                            Horario horario,
                            Set<Servico> servicos) {
        var agenda = Agenda
            .builder()
            .servicos(servicos)
            .horario(horario)
            .horarioAgendamento(horario.getHorario())
            .usuario(new Usuario(usuario.getId()))
            .situacao(ESituacaoAgenda.DISPNIVEL)
            .desconto(request.getDesconto())
            .tipoAgenda(ETipoAgenda.CADEIRA_LIVRE)
            .empresa(new Empresa(request.getEmpresaId()))
            .build();
        agenda.calcularTotal(request.getDesconto());
        return agenda;
    }

    public void calcularTotal(Float desconto) {
        var totalServico = servicos
            .stream()
            .map(Servico::getPreco)
            .mapToDouble(Double::doubleValue)
            .sum();
        totalDesconto = isEmpty(desconto)
            ? totalServico
            : totalServico * (desconto / PERCENTUAL);
        totalPagamento = totalServico - totalDesconto;
    }
}