ALTER TABLE EMPRESA ADD COLUMN TEMPO_REFRESH_CADEIRA_LIVRE INT;
UPDATE EMPRESA SET TEMPO_REFRESH_CADEIRA_LIVRE = 30;