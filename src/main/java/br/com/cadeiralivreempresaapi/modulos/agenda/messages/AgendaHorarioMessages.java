package br.com.cadeiralivreempresaapi.modulos.agenda.messages;

import br.com.cadeiralivreempresaapi.config.exception.PermissaoException;
import br.com.cadeiralivreempresaapi.config.exception.ValidacaoException;
import br.com.cadeiralivreempresaapi.modulos.comum.response.SuccessResponseDetails;

public interface AgendaHorarioMessages {

    ValidacaoException HORARIO_JA_EXISTENTE = new ValidacaoException("Este horário já está registrado para esta "
        + "empresa neste dia.");
    ValidacaoException HORARIO_NAO_ENCONTRADO = new ValidacaoException("O horário não foi encontrado.");
    ValidacaoException HORARIO_NAO_INFORMADO = new ValidacaoException("O horário deve ser informado.");
    ValidacaoException DIA_SEMANA_NAO_INFORMADO = new ValidacaoException("O dia da semana deve ser informado.");
    ValidacaoException EMPRESA_NAO_INFORMADA = new ValidacaoException("A empresa deve ser informada.");
    ValidacaoException AGENDA_EXISTENTE_HORARIO = new ValidacaoException("Já existe um agendamento para este horário.");
    PermissaoException HORARIO_SEM_PERMISSAO = new PermissaoException("Usuário sem permissão para visualizar"
        + " este horário.");
    SuccessResponseDetails HORARIO_CRIADO_SUCESSO = new SuccessResponseDetails("Horário inserido com sucesso!");
    SuccessResponseDetails HORARIO_ALTERADO_SUCESSO = new SuccessResponseDetails("Horário alterado com sucesso!");
    SuccessResponseDetails HORARIO_REMOVIDO_SUCESSO = new SuccessResponseDetails("Horário removido com sucesso!");
}
