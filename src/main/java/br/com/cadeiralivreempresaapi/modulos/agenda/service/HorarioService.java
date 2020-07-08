package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.HorarioRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Horario;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.HorarioRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import br.com.cadeiralivreempresaapi.modulos.empresa.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public SuccessResponseDetails salvarHorario(HorarioRequest request) {
        validarDadosRequest(request);
        var horario = Horario.of(request);
        var empresa = empresaService.buscarPorId(request.getEmpresaId());
        validarHorarioExistenteParaEmpresa(horario.getHorario(), empresa.getId());
        horarioRepository.save(horario);
        return HORARIO_CRIADO_SUCESSO;
    }

    @Transactional
    public SuccessResponseDetails alterarHorario(HorarioRequest request, Integer id) {
        validarDadosRequest(request);
        var horario = Horario.of(request);
        horario.setId(id);
        var empresa = empresaService.buscarPorId(request.getEmpresaId());
        validarHorarioExistenteParaEmpresa(horario.getHorario(), empresa.getId());
        horarioRepository.save(horario);
        return HORARIO_ALTERADO_SUCESSO;
    }

    @Transactional
    public SuccessResponseDetails removerHorario(HorarioRequest request, Integer id) {
        validarDadosRequest(request);
        var horario = Horario.of(request);
        horario.setId(id);
        var empresa = empresaService.buscarPorId(request.getEmpresaId());
        validarHorarioExistenteParaEmpresa(horario.getHorario(), empresa.getId());
        validarAgendaMarcadaParaHorario(id);
        horarioRepository.delete(horario);
        return HORARIO_REMOVIDO_SUCESSO;
    }

    private void validarAgendaMarcadaParaHorario(Integer horarioId) {
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
    }

    private void validarHorarioExistenteParaEmpresa(LocalTime horario, Integer empresaId) {
        if (horarioRepository.existsByEmpresaIdAndHorario(empresaId, horario)) {
            throw HORARIO_JA_EXISTENTE;
        }
    }
}
