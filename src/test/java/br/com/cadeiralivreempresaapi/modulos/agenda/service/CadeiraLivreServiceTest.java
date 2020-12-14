package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.ServicoAgendaResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.jwt.service.JwtService;
import br.com.cadeiralivreempresaapi.modulos.notificacao.dto.NotificacaoCorpoRequest;
import br.com.cadeiralivreempresaapi.modulos.notificacao.service.NotificacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioAcessoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Set;

import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.AgendaMocks.umaAgendaCadeiraLivre;
import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.AgendaMocks.umaCadeiraLivreRequest;
import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.ServicoMocks.umServico;
import static br.com.cadeiralivreempresaapi.modulos.empresa.mocks.EmpresaMocks.umaEmpresa;
import static br.com.cadeiralivreempresaapi.modulos.usuario.mocks.UsuarioMocks.umUsuarioAutenticadoAdmin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadeiraLivreServiceTest {

    @InjectMocks
    private CadeiraLivreService service;
    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private AgendaService agendaService;
    @Mock
    private AutenticacaoService autenticacaoService;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private ServicoService servicoService;
    @Mock
    private EmpresaService empresaService;
    @Mock
    private UsuarioAcessoService acessoService;
    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("Deve disponibilizar cadeira livre quando dados estiverem corretos")
    public void disponibilizarCadeiraLivre_deveDisponibilizarCadeiraLivre_quandoDadosEstiveremCorretos() {
        when(autenticacaoService.getUsuarioAutenticado()).thenReturn(umUsuarioAutenticadoAdmin());
        when(servicoService.buscarServicosPorIds(anyList())).thenReturn(Set.of(umServico()));
        when(empresaService.buscarPorId(anyInt())).thenReturn(umaEmpresa());
        when(agendaRepository.save(any(Agenda.class))).thenReturn(umaAgendaCadeiraLivre());
        when(agendaService.buscarAgendaPorId(anyInt())).thenReturn(umaAgendaCadeiraLivre());
        when(servicoService.tratarNomesServicos(anyList())).thenReturn("Serviço 1, Serviço 2");
        var horarioAtual = LocalTime.now();

        var response = service.disponibilizarCadeiraLivre(umaCadeiraLivreRequest());

        assertThat(response).isNotNull();
        assertThat(response.getServicos().get(0)).isEqualTo(new ServicoAgendaResponse(1, "Corte de cabelo", 25.0));
        assertThat(response.getEmpresaId()).isEqualTo(1);
        assertThat(response.getEmpresaNome()).isEqualTo("Empresa 01");
        assertThat(response.getEmpresaCnpj()).isEqualTo("82.765.926/0001-32");
        assertThat(response.getMinutosDisponiveis()).isEqualTo(20);
        assertThat(response.getHorarioExpiracao().getMinute()).isEqualTo(horarioAtual.plusMinutes(20).getMinute());
        assertThat(response.getCadeiraLivreValida()).isTrue();
        assertThat(response.getSituacao()).isEqualTo("Disponível");
        assertThat(response.getCliente()).isNull();

        verify(autenticacaoService, times(1)).getUsuarioAutenticado();
        verify(servicoService, times(1)).buscarServicosPorIds(anyList());
        verify(acessoService, times(1)).validarPermissoesDoUsuario(anyInt());
        verify(empresaService, times(1)).buscarPorId(anyInt());
        verify(servicoService, times(1)).validarServicosExistentesPorEmpresa(anyList(), anyInt());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
        verify(agendaService, times(1)).buscarAgendaPorId(anyInt());
        verify(notificacaoService, times(1)).gerarDadosNotificacao(any(NotificacaoCorpoRequest.class));
    }

    @Test
    @DisplayName("Deve lançar exception quando não enviar dados no request para disponibilizar")
    public void disponibilizarCadeiraLivre_deveLancarException_quandoNaoEnviarDadosRequest() {
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.disponibilizarCadeiraLivre(null))
            .withMessage("É obrigatório informar os dados para a agenda.");

        verify(autenticacaoService, times(0)).getUsuarioAutenticado();
        verify(servicoService, times(0)).buscarServicosPorIds(anyList());
        verify(acessoService, times(0)).validarPermissoesDoUsuario(anyInt());
        verify(empresaService, times(0)).buscarPorId(anyInt());
        verify(servicoService, times(0)).validarServicosExistentesPorEmpresa(anyList(), anyInt());
        verify(agendaRepository, times(0)).save(any(Agenda.class));
        verify(agendaService, times(0)).buscarAgendaPorId(anyInt());
        verify(notificacaoService, times(0)).gerarDadosNotificacao(any(NotificacaoCorpoRequest.class));
    }

    @Test
    @DisplayName("Deve lançar exception quando não enviar desconto no request para disponibilizar")
    public void disponibilizarCadeiraLivre_deveLancarException_quandoNaoEnviarDescontoNoRequest() {
        var request = umaCadeiraLivreRequest();
        request.setDesconto(null);
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.disponibilizarCadeiraLivre(request))
            .withMessage("É obrigatório informar um desconto para a Cadeira Livre.");

        verify(autenticacaoService, times(0)).getUsuarioAutenticado();
        verify(servicoService, times(0)).buscarServicosPorIds(anyList());
        verify(acessoService, times(0)).validarPermissoesDoUsuario(anyInt());
        verify(empresaService, times(0)).buscarPorId(anyInt());
        verify(servicoService, times(0)).validarServicosExistentesPorEmpresa(anyList(), anyInt());
        verify(agendaRepository, times(0)).save(any(Agenda.class));
        verify(agendaService, times(0)).buscarAgendaPorId(anyInt());
        verify(notificacaoService, times(0)).gerarDadosNotificacao(any(NotificacaoCorpoRequest.class));
    }

    @Test
    @DisplayName("Deve lançar exception quando não enviar serviços no request para disponibilizar")
    public void disponibilizarCadeiraLivre_deveLancarException_quandoNaoEnviarServicosNoRequest() {
        var request = umaCadeiraLivreRequest();
        request.setServicosIds(null);
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.disponibilizarCadeiraLivre(request))
            .withMessage("É obrigatório informar ao menos um serviço para a agenda.");

        verify(autenticacaoService, times(0)).getUsuarioAutenticado();
        verify(servicoService, times(0)).buscarServicosPorIds(anyList());
        verify(acessoService, times(0)).validarPermissoesDoUsuario(anyInt());
        verify(empresaService, times(0)).buscarPorId(anyInt());
        verify(servicoService, times(0)).validarServicosExistentesPorEmpresa(anyList(), anyInt());
        verify(agendaRepository, times(0)).save(any(Agenda.class));
        verify(agendaService, times(0)).buscarAgendaPorId(anyInt());
        verify(notificacaoService, times(0)).gerarDadosNotificacao(any(NotificacaoCorpoRequest.class));
    }

    @Test
    @DisplayName("Deve lançar exception quando não enviar empresa no request para disponibilizar")
    public void disponibilizarCadeiraLivre_deveLancarException_quandoNaoEnviarEmpresaNoRequest() {
        var request = umaCadeiraLivreRequest();
        request.setEmpresaId(null);
        assertThatExceptionOfType(ValidacaoException.class)
            .isThrownBy(() -> service.disponibilizarCadeiraLivre(request))
            .withMessage("É obrigatório informar uma empresa para a agenda.");

        verify(autenticacaoService, times(0)).getUsuarioAutenticado();
        verify(servicoService, times(0)).buscarServicosPorIds(anyList());
        verify(acessoService, times(0)).validarPermissoesDoUsuario(anyInt());
        verify(empresaService, times(0)).buscarPorId(anyInt());
        verify(servicoService, times(0)).validarServicosExistentesPorEmpresa(anyList(), anyInt());
        verify(agendaRepository, times(0)).save(any(Agenda.class));
        verify(agendaService, times(0)).buscarAgendaPorId(anyInt());
        verify(notificacaoService, times(0)).gerarDadosNotificacao(any(NotificacaoCorpoRequest.class));
    }
}
