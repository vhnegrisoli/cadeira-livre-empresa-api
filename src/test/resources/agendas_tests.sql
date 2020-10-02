INSERT INTO dia_da_semana (id,dia,dia_nome,dia_codigo) VALUES
  (26,0,'Segunda-feira','SEGUNDA_FEIRA'),
  (27,1,'Terça-feira','TERCA_FEIRA'),
  (28,2,'Quarta-feira','QUARTA_FEIRA'),
  (29,3,'Quinta-feira','QUINTA_FEIRA'),
  (30,4,'Sexta-feira','SEXTA_FEIRA'),
  (31,5,'Sábado','SABADO'),
  (25,6,'Domingo','DOMINGO');

INSERT INTO horario (id,fk_empresa,horario,fk_dia_da_semana) VALUES
  (36,4,'17:30:00',26),
  (40,4,'14:30:00',25),
  (41,7,'14:30:00',25),
  (42,7,'14:30:00',26),
  (43,7,'14:30:00',27);

INSERT INTO servico (id, descricao, fk_empresa, preco) VALUES
  (1, 'Corte exclusivo', 4, 15.90),
  (1, 'Corte de cabelo', 7, 15.90),
  (1, 'Corte e lavagem', 4, 15.90),
  (1, 'Lavagem', 4, 15.90),
  (1, 'Corte e barba', 7, 15.90),
  (1, 'Barba', 7, 15.90),
  (1, 'Corte, barba e lavagem', 4, 15.90);
