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
    ValidacaoException DIA_DA_SEMANA_NAO_EXISTENTE = new ValidacaoException("Este dia da semana não existe.");

    ValidacaoException SERVICO_VAZIO = new ValidacaoException("É obrigatório enviar os dados do serviço.");
    ValidacaoException SERVICO_DESCRICAO_VAZIA = new ValidacaoException("É obrigatório informar a descrição.");
    ValidacaoException SERVICO_EMPRESA_VAZIA = new ValidacaoException("É obrigatório informar a empresa.");
    ValidacaoException SERVICO_PRECO_VAZIO = new ValidacaoException("É obrigatório informar o preço.");
    ValidacaoException SERVICO_PRECO_MENOR_IGUAL_ZERO = new ValidacaoException("O preço não pode ser menor ou igual a R$0,00.");
    ValidacaoException SERVICO_JA_EXISTENTE = new ValidacaoException("Este serviço já existe para esta empresa.");
    ValidacaoException SERVICO_NAO_ENCONTRADO = new ValidacaoException("O serviço não foi encontrado.");
    ValidacaoException SERVICO_EXISTENTE_AGENDA = new ValidacaoException("O serviço não pode ser removido pois "
        + "já está cadastrado para uma agenda.");

    PermissaoException HORARIO_SEM_PERMISSAO = new PermissaoException("Usuário sem permissão para visualizar"
        + " este horário.");
    PermissaoException SERVICO_SEM_PERMISSAO = new PermissaoException("Usuário sem permissão para visualizar"
        + " este serviço.");

    SuccessResponseDetails HORARIO_REMOVIDO_SUCESSO = new SuccessResponseDetails("Horário removido com sucesso!");

    SuccessResponseDetails SERVICO_CRIADO_SUCESSO = new SuccessResponseDetails("Serviço inserido com sucesso!");
    SuccessResponseDetails SERVICO_ALTERADO_SUCESSO = new SuccessResponseDetails("Serviço alterado com sucesso!");
    SuccessResponseDetails SERVICO_REMOVIDO_SUCESSO = new SuccessResponseDetails("Serviço removido com sucesso!");
}
