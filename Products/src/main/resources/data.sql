DELETE PRODUCT WHERE Designation='madeira';
INSERT INTO PRODUCT (designation, description, sku) VALUES ('madeira','A madeira é um material produzido a partir do tecido formado pelas plantas lenhosas com funções de sustentação mecânica.', 'SKU0000001');
DELETE PRODUCT WHERE Designation='ferro';
INSERT INTO PRODUCT (designation, description, sku) VALUES ('ferro','O ferro (do latim ferrum) é um elemento químico, símbolo Fe, de número atômico 26 (26 prótons e 26 elétrons) e massa atômica 56 u.', 'SKU0000002');
DELETE PRODUCT WHERE Designation='bolacha';
INSERT INTO PRODUCT (designation, description, sku) VALUES ('bolacha','A bolacha é um bolo chato e seco de farinha de diversas formas e tamanhos. Pode ser consumida de diversas maneiras, doce, com recheios, salgada ou acompanhada de especiarias e/ou patês', 'SKU0000003');

INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (4,'SKU0000001',5,'Madeira de muita qualidade, fantástica para fabrico de móveis. Recomendo', '1999-06-22', 'June 22nd is the day in 1942 that Erwin Rommel is promoted to Field Marshal after the capture of Tobruk.', 0);
INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (5,'SKU0000001',5,'Madeira muito boa para usar como lenha.', '1998-08-09', 'August 9th is the day in 1965 that Singapore is expelled from Malaysia and becomes the first and only country to date to gain independence unwillingly.', 0);
INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (6,'SKU0000001',4,'Muito boa madeira para mesas, fica lindíssima, apenas necessita do verniz adequado.','2001-10-22', 'October 22nd is the day in 1927 that Nikola Tesla exposed his six (6) new inventions including motor with onephase electricity.', 0);
INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (11,'SKU0000001',1,'Madeira péssima para fabricar cadeiras, apodrece muito rapidamente!', '2004-04-29', 'April 29th is the day in 1953 that the first U.S. experimental 3D-TV broadcast showed an episode of Space Patrol on Los Angeles ABC affiliate KECA-TV.', 0);
INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (9,'SKU0000002',4,'Ótimo ferro para fabricar quadros de bicicletas.', '2000-02-13', 'February 13th is the day in 1542 that Catherine Howard, the fifth wife of Henry VIII of England, is executed for adultery.', 0);
INSERT INTO REVIEW (user_Id, sku, rating, text, publishing_Date, fun_Fact, status) VALUES (4,'SKU0000003',1,'Sabor péssimo, sem palavras!', '1999-05-01', 'May 1st is the day in 1894 that Coxey´s Army, the first significant American protest march, arrives in Washington, D.C.', 0);

INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Concordo plenamente, sou demasiado alto para o carro, mas vou dá-lo à minha filha.', 7, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('É verdade, também a uso e nunca tive problemas!', 1, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('É uma questão de hábito!', 12, false);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Aconteceu-me exatamente o mesmo...', 9, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Não devemos estar a falar da mesma madeira! Nem sequer faz chama...', 2, false);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Por acaso não concordo consigo, uso esta madeira há anos precisamente para cadeiras e mesas e nunca tive problemas.', 4, false);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Faço das minhas palavras as suas... muito mau.', 6, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Quando me apercebi que não era carro para mim já tinha desvalorizado uns milhares...', 12, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Verdade, bastou usar durante cerca de 1 mês para ter de deitar fora.', 11, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Eu até gostei! Acho um exagero este comentário.', 9, false);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('Segui a sua review e não me arrependi. Vou começar a usar este ferro frequentemente.', 5, true);
INSERT INTO VOTE (reason, review_Id, vote) VALUES ('O padrão é demasiado irregular, eu particularmente não gosto muito... depende mesmo dos gostos', 3, false);
