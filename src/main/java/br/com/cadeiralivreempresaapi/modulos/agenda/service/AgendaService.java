package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.agenda.AgendaRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.AGENDA_NAO_ENCONTRADA;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public SuccessResponseDetails reservarAgenda(AgendaRequest request) {
        // TODO implementar reserva de agenda
        return null;
    }

    public Agenda buscarAgendaPorId(Integer id) {
        return agendaRepository.findById(id)
            .orElseThrow(() -> AGENDA_NAO_ENCONTRADA);
    }
}