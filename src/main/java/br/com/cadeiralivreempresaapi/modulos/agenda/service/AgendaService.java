package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.AgendaRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.dto.CadeiraLivreRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Agenda;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.AgendaRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Transactional
    public SuccessResponseDetails reservarAgenda(AgendaRequest request) {
        // TODO implementar reserva de agenda
        return null;
    }

    @Transactional
    public SuccessResponseDetails disponibilizarCadeiraLivre(CadeiraLivreRequest request) {
        // TODO implementar disponibilização de cadeira livre
        return null;
    }

    public Agenda buscarAgendaPorId(Integer id) {
        return agendaRepository.findById(id)
            .orElseThrow(() -> new ValidacaoException("Agenda não encontrada."));
    }
}
