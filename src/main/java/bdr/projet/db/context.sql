BEGIN;
INSERT INTO _user VALUES ('t', 'e3b98a4da31a127d4bde6e43033f66ba274cab0eb7eb1c70ec41402bf6273dd8', false), -- mdp = 't'
                         ('ta', 'e3b98a4da31a127d4bde6e43033f66ba274cab0eb7eb1c70ec41402bf6273dd8', true); -- mdp = 't'
INSERT INTO comment (content, fk_mod_name, fk_mod_game_name, fk_user) VALUES('Hello world!', 'Forge', 'Minecraft', 't');
INSERT INTO comment (content, fk_mod_name, fk_mod_game_name, fk_user) VALUES('Just the best mod !', 'Forge', 'Minecraft', null);
INSERT INTO note VALUES('Forge', 'Minecraft', 'LeVraiGG', 5.0),
                       ('Forge', 'Minecraft', 't', 3.0),
                       ('Forge', 'Minecraft', 'ta', 4.5);
INSERT INTO mod_collection VALUES('Test', 't', '\', null, null, 'Minecraft');
INSERT INTO mod_mod_collection VALUES('Forge', 'Minecraft', 'Test', 't'),
                                     ('LibVulpes', 'Minecraft', 'Test', 't'),
                                     ('Advanced Rocketry', 'Minecraft', 'Test', 't'),
                                     ('Animal Bikes', 'Minecraft', 'Test', 't'),
                                     ('Applied Energistics 2', 'Minecraft', 'Test', 't');
DELETE FROM mod_mod_collection WHERE fk_mod_name='Animal Bikes' AND fk_mod_game_name='Minecraft' AND fk_mod_collection_name='Test' AND fk_mod_collection_user_name='t';
COMMIT;