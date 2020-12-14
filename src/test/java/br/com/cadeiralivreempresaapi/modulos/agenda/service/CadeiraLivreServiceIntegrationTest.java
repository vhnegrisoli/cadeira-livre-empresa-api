package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.config.exception.PermissaoException;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.empresa.model.Empresa;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.service.FuncionarioService;
import br.com.cadeiralivreempresaapi.modulos.jwt.service.JwtService;
import br.com.cadeiralivreempresaapi.modulos.notificacao.service.NotificacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.PermissaoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioAcessoService;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static br.com.cadeiralivreempresaapi.modulos.agenda.mocks.AgendaMocks.umaAgendaCadeiraLivre;
import static br.com.cadeiralivreempresaapi.modulos.comum.util.NumeroUtil.converterParaDuasCasasDecimais;
import static br.com.cadeiralivreempresaapi.modulos.jwt.util.JwtTestUtil.gerarTokenTeste;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({
    CadeiraLivreService.class,
    AgendaService.class,
    ServicoService.class,
    EmpresaService.class,
    FuncionarioService.class,
    JwtService.class,
    UsuarioService.class,
    PermissaoService.class
})
@ExtendWith(MockitoExtension.class)
@Sql(scripts = {
    "classpath:/usuarios_tests.sql",
    "classpath:/funcionarios_tests.sql",
    "classpath:/agendas_tests.sql"
})
public class CadeiraLivreServiceIntegrationTest {

    @Autowired
    private CadeiraLivreService service;
    @Autowired
    private AgendaService agendaService;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private JwtService jwtService;
    @MockBean
    private UsuarioAcessoService acessoService;
    @MockBean
    private AutenticacaoService autenticacaoService;
    @MockBean
    private NotificacaoService notificacaoService;
    @MockBean
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Deve buscar cadeiras livres quando existirem sem filtrar por empresas")
    public void buscarCadeirasLivresDisponiveis_deveBuscarCadeirasLivres_quandoExistiremDisponiveis() {
        var cadeiraLivre = umaAgendaCadeiraLivre();
        cadeiraLivre.setId(null);
        cadeiraLivre.setEmpresa(new Empresa(4));
        agendaRepository.deleteAll();
        agendaRepository.save(cadeiraLivre);
        assertThat(service.buscarCadeirasLivresDisponiveis(gerarTokenTeste(), null))
            .extracting("situacao", "minutosDisponiveis", "totalPagamento", "totalDesconto", "totalServico")
            .containsExactlyInAnyOrder(
                tuple("Disponível",
                    20,
                    converterParaDuasCasasDecimais(25.00),
                    converterParaDuasCasasDecimais(12.00),
                    converterParaDuasCasasDecimais(25.00))
            );
    }

    @Test
    @DisplayName("Deve buscar cadeiras livres quando existirem filtrando por empresas")
    public void buscarCadeirasLivresDisponiveis_deveBuscarCadeirasLivres_quandoExistiremDisponiveisComFiltroPorEmpresa() {
        var cadeiraLivre = umaAgendaCadeiraLivre();
        cadeiraLivre.setId(null);
        cadeiraLivre.setEmpresa(new Empresa(4));
        agendaRepository.deleteAll();
        agendaRepository.save(cadeiraLivre);
        assertThat(service.buscarCadeirasLivresDisponiveis(gerarTokenTeste(), 4))
            .extracting("situacao", "minutosDisponiveis", "totalPagamento", "totalDesconto", "totalServico")
            .containsExactlyInAnyOrder(
                tuple("Disponível",
                    20,
                    converterParaDuasCasasDecimais(25.00),
                    converterParaDuasCasasDecimais(12.00),
                    converterParaDuasCasasDecimais(25.00))
            );
        assertThat(service.buscarCadeirasLivresDisponiveis(gerarTokenTeste(), 7)).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar buscar cadeiras livres disponíveis e token JWT não estiver válido.")
    public void buscarCadeirasLivresDisponiveis_deveLancarException_quandoTokenJwtNaoEstiverValido() {
        var cadeiraLivre = umaAgendaCadeiraLivre();
        cadeiraLivre.setId(null);
        cadeiraLivre.setEmpresa(new Empresa(4));
        agendaRepository.deleteAll();
        agendaRepository.save(cadeiraLivre);
        assertThatExceptionOfType(PermissaoException.class)
            .isThrownBy(() -> service.buscarCadeirasLivresDisponiveis(gerarTokenTeste() + "1", null))
            .withMessage("O usuário não está autenticado.");
    }
}