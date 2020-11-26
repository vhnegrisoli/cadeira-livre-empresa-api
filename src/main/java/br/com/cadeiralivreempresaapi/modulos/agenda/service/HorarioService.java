package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.horario.DiaDaSemanaResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.horario.HorarioRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.horario.HorarioResponse;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.DiaDaSemana;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Horario;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.DiaDaSemanaRepository;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.HorarioRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import br.com.cadeiralivreempresaapi.modulos.funcionario.repository.FuncionarioRepository;
import br.com.cadeiralivreempresaapi.modulos.usuario.dto.UsuarioAutenticado;
import br.com.cadeiralivreempresaapi.modulos.usuario.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private DiaDaSemanaRepository diaDaSemanaRepository;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional
    public HorarioResponse salvarHorario(HorarioRequest request) {
        validarDadosRequest(request);
        var horario = Horario.of(request);
        horario.setEmpresa(empresaService.buscarPorId(request.getEmpresaId()));
        horario.setDiaDaSemana(buscarDiaDaSemanaPorId(horario.getDiaDaSemana().getId()));
        validarHorarioExistenteParaEmpresa(horario);
        return HorarioResponse.of(horarioRepository.save(horario));
    }

    @Transactional
    public HorarioResponse alterarHorario(HorarioRequest request, Integer id) {
        validarDadosRequest(request);
        var horario = Horario.of(request);
        horario.setId(id);
        horario.setEmpresa(empresaService.buscarPorId(request.getEmpresaId()));
        horario.setDiaDaSemana(buscarDiaDaSemanaPorId(horario.getDiaDaSemana().getId()));
        validarHorarioExistenteParaEmpresa(horario);
        return HorarioResponse.of(horarioRepository.save(horario));
    }

    @Transactional
    public SuccessResponseDetails removerHorarioDoDia(Integer id) {
        var horario = buscarPorId(id);
        validarAgendaMarcadaParaHorarioDoDia(horario.getId());
        horarioRepository.delete(horario);
        return HORARIO_REMOVIDO_SUCESSO;
    }

    private void validarAgendaMarcadaParaHorarioDoDia(Integer horarioId) {
        if (agendaRepository.existsByHorarioId(horarioId)) {
            throw AGENDA_EXISTENTE_HORARIO;
        }
    }

    private void validarDadosRequest(HorarioRequest request) {
        if (isEmpty(request.getEmpresaId())) {
            throw EMPRESA_NAO_INFORMADA;
        }
        if (isEmpty(request.getHorario())) {
            throw HORARIO_NAO_INFORMADO;
        }
        if (isEmpty(request.getDiaSemanaId())) {
            throw DIA_SEMANA_NAO_INFORMADO;
        }
    }

    private void validarHorarioExistenteParaEmpresa(Horario horario) {
        if (horarioRepository.existsByHorarioAndEmpresaIdAndDiaDaSemanaId(horario.getHorario(),
            horario.getEmpresa().getId(),
            horario.getDiaDaSemana().getId())) {
            throw HORARIO_JA_EXISTENTE;
        }
    }

    public List<HorarioResponse> buscarHorariosPorEmpresa(Integer empresaId) {
        validarPermissoesUsuario(empresaId);
        return horarioRepository.findByEmpresaIdOrderByHorario(empresaId)
            .stream()
            .map(HorarioResponse::of)
            .collect(Collectors.toList());
    }

    public Horario buscarPorId(Integer id) {
        var horario = horarioRepository.findById(id)
            .orElseThrow(() -> HORARIO_NAO_ENCONTRADO);
        validarPermissoesUsuario(horario.getEmpresa().getId());
        return horario;
    }

    public List<DiaDaSemanaResponse> buscarDiasDaSemana() {
        return diaDaSemanaRepository.findAll()
            .stream()
            .map(DiaDaSemanaResponse::of)
            .collect(Collectors.toList());
    }

    public DiaDaSemana buscarDiaDaSemanaPorId(Integer id) {
        return diaDaSemanaRepository.findById(id)
            .orElseThrow(() -> DIA_DA_SEMANA_NAO_EXISTENTE);
    }

    private void validarPermissoesUsuario(Integer empresaId) {
        var usuarioAutenticado = autenticacaoService.getUsuarioAutenticado();
        if (!usuarioAutenticado.isAdmin()
            && !isSocioProprietarioValido(usuarioAutenticado, empresaId)
            && !isFuncionarioValido(usuarioAutenticado, empresaId)) {
            throw HORARIO_SEM_PERMISSAO;
        }
    }

    private boolean isSocioProprietarioValido(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        return usuarioAutenticado.isSocioOuProprietario()
            && empresaService.existeEmpresaParaUsuario(empresaId, usuarioAutenticado.getId());
    }

    private boolean isFuncionarioValido(UsuarioAutenticado usuarioAutenticado, Integer empresaId) {
        return usuarioAutenticado.isFuncionario()
            && funcionarioRepository.existsByUsuarioIdAndEmpresaId(usuarioAutenticado.getId(), empresaId);
    }
}
