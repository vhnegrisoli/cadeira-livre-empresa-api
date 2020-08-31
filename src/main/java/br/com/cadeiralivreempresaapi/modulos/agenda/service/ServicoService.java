package br.com.cadeiralivreempresaapi.modulos.agenda.service;

import br.com.cadeiralivreempresaapi.modulos.agenda.dto.ServicoRequest;
import br.com.cadeiralivreempresaapi.modulos.agenda.model.Servico;
import br.com.cadeiralivreempresaapi.modulos.agenda.repository.ServicoRepository;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.SERVICO_ALTERADO_SUCESSO;
import static br.com.cadeiralivreempresaapi.modulos.agenda.messages.AgendaHorarioMessages.SERVICO_CRIADO_SUCESSO;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public SuccessResponseDetails salvarNovoServico(ServicoRequest request) {
        var servico = Servico.of(request);
        servicoRepository.save(servico);
        return SERVICO_CRIADO_SUCESSO;
    }

    public SuccessResponseDetails atualizarServico(ServicoRequest request,
                                                   Integer id) {
        var servico = Servico.of(request);
        servico.setId(id);
        servicoRepository.save(servico);
        return SERVICO_ALTERADO_SUCESSO;
    }
}
