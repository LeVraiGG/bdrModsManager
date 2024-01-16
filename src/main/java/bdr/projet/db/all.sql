-- CRÉATION DE LA DB, DESCRIPTION DE CELLE-CI ---------------------------------------------------------

--
-- Base de données : `mods`
--
\c db_app;


-- DESCRIPTION DES TABLES -----------------------------------------------------------------------------

--
-- Structure de la table `game`
--
DROP TABLE IF EXISTS game;
CREATE TABLE IF NOT EXISTS game
(
    name        varchar(255),
    logo        varchar(255),
    description text,
    modFolder   text,
    PRIMARY KEY (name)
    );

--
-- Structure de la table `mod`
--
DROP TABLE IF EXISTS mod;
CREATE TABLE IF NOT EXISTS mod
(
    name         varchar(255),
    fk_game      varchar(255),
    logo         varchar(255),
    description  text,
    downloadLink varchar(255) NOT NULL UNIQUE,
    nbDownload   bigint       NOT NULL DEFAULT 0,
    PRIMARY KEY (fk_game, name)
    );

--
-- Structure de la table `screenshot`
--
DROP TABLE IF EXISTS screenshot;
CREATE TABLE IF NOT EXISTS screenshot
(
    img_path         varchar(255),
    fk_mod_name      varchar(255) NOT NULL,
    fk_mod_game_name varchar(255) NOT NULL,
    PRIMARY KEY (img_path)
    );

--
-- Structure de la table `_user`
--
DROP TABLE IF EXISTS _user;
CREATE TABLE IF NOT EXISTS _user
(
    name     varchar(255),
    password text    NOT NULL,
    isAdmin  boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (name)
    );

--
-- Structure de la table `genre`
--
DROP TABLE IF EXISTS genre;
CREATE TABLE IF NOT EXISTS genre
(
    name varchar(255),
    PRIMARY KEY (name)
    );

--
-- Structure de la table `version`
--
DROP TABLE IF EXISTS version;
CREATE TABLE IF NOT EXISTS version
(
    name varchar(255),
    PRIMARY KEY (name)
    );

--
-- Structure de la table `impact`
--
DROP TABLE IF EXISTS impact;
CREATE TABLE IF NOT EXISTS impact
(
    name varchar(255),
    PRIMARY KEY (name)
    );

--
-- Structure de la table `note`
--
DROP TABLE IF EXISTS note;
CREATE TABLE IF NOT EXISTS note
(
    fk_mod_name      varchar(255),
    fk_mod_game_name varchar(255),
    fk_user          varchar(255),
    value            decimal(2, 1) NOT NULL CHECK (mod(value, 0.5) = 0 AND 0 <= value AND value <= 5),
    PRIMARY KEY (fk_mod_name, fk_mod_game_name, fk_user)
    );

--
-- Structure de la table `comment`
--
DROP TABLE IF EXISTS comment;
CREATE TABLE IF NOT EXISTS comment
(
    id               SERIAL,
    content          text         NOT NULL CHECK (content != ''),
    nbLike           bigint       NOT NULL DEFAULT 0,
    fk_mod_name      varchar(255) NOT NULL,
    fk_mod_game_name varchar(255) NOT NULL,
    fk_user          varchar(255),
    PRIMARY KEY (id)
    );

--
-- Structure de la table `mod_collection`
--
DROP TABLE IF EXISTS mod_collection;
CREATE TABLE IF NOT EXISTS mod_collection
(
    name                    varchar(255),
    fk_user                 varchar(255),
    relative_path_to_folder text NOT NULL,
    logo                    varchar(255),
    description             text,
    fk_game                 varchar(255),
    PRIMARY KEY (name, fk_user)
    );

--
-- Structure de la table `mod_pack`
--
DROP TABLE IF EXISTS mod_pack;
CREATE TABLE IF NOT EXISTS mod_pack
(
    name         varchar(255),
    fk_game      varchar(255),
    logo         varchar(255),
    description  text,
    downloadLink varchar(255) NOT NULL UNIQUE,
    nbDownload   bigint       NOT NULL DEFAULT 0,
    PRIMARY KEY (fk_game, name)
    );

--
-- Structure de la table `moder`
--
DROP TABLE IF EXISTS moder;
CREATE TABLE IF NOT EXISTS moder
(
    pseudo  varchar(255),
    fk_user varchar(255) UNIQUE DEFAULT NULL,
    PRIMARY KEY (pseudo)
    );

--
-- Structure de la table `mod_collection_logger`
--

DROP TABLE IF EXISTS mod_collection_logger;
CREATE TABLE IF NOT EXISTS mod_collection_logger
(
    version                     timestamp NOT NULL,
    fk_mod_collection_name      varchar(255), -- noFk because collection could be deleted now
    fk_mod_collection_user_name varchar(255), -- noFk because user could be deleted now
    relative_path_to_folder     text,
    logo                        varchar(255),
    description                 text,
    fk_game                     varchar(255),
    fkModList                   text, -- format "x;y;...;z" where fk mod name value
    id                          SERIAL,
    PRIMARY KEY (id)
    );


-- DESCRIPTION DES TR ----------------------------------------------------------------------------------

--
-- Structure de la table `genre_game`
--
DROP TABLE IF EXISTS genre_game;
CREATE TABLE IF NOT EXISTS genre_game
(
    fk_game  varchar(255),
    fk_genre varchar(255),
    PRIMARY KEY (fk_game, fk_genre)
    );

--
-- Structure de la table `game_version`
--
DROP TABLE IF EXISTS game_version;
CREATE TABLE IF NOT EXISTS game_version
(
    fk_game    varchar(255),
    fk_version varchar(255),
    PRIMARY KEY (fk_game, fk_version)
    );

--
-- Structure de la table `mod_dependence`
--
DROP TABLE IF EXISTS mod_dependence;
CREATE TABLE IF NOT EXISTS mod_dependence
(
    fk_mod_name             varchar(255),
    fk_mod_game_name        varchar(255),
    fk_dependence_name      varchar(255),
    fk_dependence_game_name varchar(255),
    PRIMARY KEY (fk_mod_name, fk_mod_game_name, fk_dependence_name, fk_dependence_game_name)
    );

--
-- Structure de la table `mod_version`
--
DROP TABLE IF EXISTS mod_version;
CREATE TABLE IF NOT EXISTS mod_version
(
    fk_mod_name      varchar(255),
    fk_mod_game_name varchar(255),
    fk_version       varchar(255),
    PRIMARY KEY (fk_mod_name, fk_mod_game_name, fk_version)
    );

--
-- Structure de la table `mod_impact`
--
DROP TABLE IF EXISTS mod_impact;
CREATE TABLE IF NOT EXISTS mod_impact
(
    fk_mod_name      varchar(255),
    fk_mod_game_name varchar(255),
    fk_impact        varchar(255),
    PRIMARY KEY (fk_mod_name, fk_mod_game_name, fk_impact)
    );

--
-- Structure de la table `mod_version`
--
DROP TABLE IF EXISTS mod_mod_collection;
CREATE TABLE IF NOT EXISTS mod_mod_collection
(
    fk_mod_name                 varchar(255),
    fk_mod_game_name            varchar(255),
    fk_mod_collection_name      varchar(255),
    fk_mod_collection_user_name varchar(255),
    PRIMARY KEY (fk_mod_name, fk_mod_game_name, fk_mod_collection_name, fk_mod_collection_user_name)
    );

--
-- Structure de la table `mod_mod_pack`
--
DROP TABLE IF EXISTS mod_mod_pack;
CREATE TABLE IF NOT EXISTS mod_mod_pack
(
    fk_mod_name           varchar(255),
    fk_mod_game_name      varchar(255),
    fk_mod_pack_game_name varchar(255),
    fk_mod_pack_name      varchar(255),
    PRIMARY KEY (fk_mod_game_name, fk_mod_name, fk_mod_pack_game_name, fk_mod_pack_name)
    );
--
-- Structure de la table `mod_moder`
--
DROP TABLE IF EXISTS mod_moder;
CREATE TABLE IF NOT EXISTS mod_moder
(
    fk_moder         varchar(255),
    fk_mod_game_name varchar(255),
    fk_mod_name      varchar(255),
    PRIMARY KEY (fk_moder, fk_mod_game_name, fk_mod_name)
    );

--
-- Structure de la table `mod_pack_moder`
--
DROP TABLE IF EXISTS mod_pack_moder;
CREATE TABLE IF NOT EXISTS mod_pack_moder
(
    fk_moder              varchar(255),
    fk_mod_pack_game_name varchar(255),
    fk_mod_pack_name      varchar(255),
    PRIMARY KEY (fk_moder, fk_mod_pack_game_name, fk_mod_pack_name)
    );

--
-- Structure de la table `mod_pack_version`
--
DROP TABLE IF EXISTS mod_pack_version;
CREATE TABLE IF NOT EXISTS mod_pack_version
(
    fk_version            varchar(255),
    fk_mod_pack_game_name varchar(255),
    fk_mod_pack_name      varchar(255),
    PRIMARY KEY (fk_version, fk_mod_pack_game_name, fk_mod_pack_name)
    );

--
-- Structure de la table `mod_pack_impact`
--
DROP TABLE IF EXISTS mod_pack_impact;
CREATE TABLE IF NOT EXISTS mod_pack_impact
(
    fk_impact             varchar(255),
    fk_mod_pack_game_name varchar(255),
    fk_mod_pack_name      varchar(255),
    PRIMARY KEY (fk_impact, fk_mod_pack_game_name, fk_mod_pack_name)
    );

-- AJOUT DES CONTRAINTES ---------------------------------------------------------------------------

--
-- Contraintes pour la table `genre_game`
--
ALTER TABLE genre_game
    ADD CONSTRAINT genre_game_fk_game FOREIGN KEY (fk_game) REFERENCES game (name)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT genre_game_fk_genre FOREIGN KEY (fk_genre) REFERENCES genre (name)
            ON UPDATE CASCADE;

--
-- Contraintes pour la table `game_version`
--
ALTER TABLE game_version
    ADD CONSTRAINT game_version_fk_game FOREIGN KEY (fk_game) REFERENCES game (name)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT game_version_fk_version FOREIGN KEY (fk_version) REFERENCES version (name);

--
-- Contraintes pour la table `mod`
--
ALTER TABLE mod
    ADD CONSTRAINT mod_fk_game FOREIGN KEY (fk_game) REFERENCES game (name)
        ON UPDATE CASCADE
        ON DELETE CASCADE;

--
-- Contraintes pour la table `screenshot`
--
ALTER TABLE screenshot
    ADD CONSTRAINT screenshot_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE;

--
-- Contraintes pour la table `mod_dependence`
--
ALTER TABLE mod_dependence
    ADD CONSTRAINT mod_dependence_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_dependence_fk_dependence FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
            ON UPDATE CASCADE;

--
-- Contraintes pour la table `mod_version`
--
ALTER TABLE mod_version
    ADD CONSTRAINT mod_version_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_version_fk_version FOREIGN KEY (fk_version) REFERENCES version (name);

--
-- Contraintes pour la table `mod_impact`
--
ALTER TABLE mod_impact
    ADD CONSTRAINT mod_impact_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_impact_fk_impact FOREIGN KEY (fk_impact) REFERENCES impact (name)
            ON UPDATE CASCADE;

--
-- Contraintes pour la table `note`
--
ALTER TABLE note
    ADD CONSTRAINT note_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT note_fk_user FOREIGN KEY (fk_user) REFERENCES _user (name)
            ON UPDATE CASCADE
               ON DELETE CASCADE;

    --
    -- Contraintes pour la table `comment`
    --
ALTER TABLE comment
    ADD CONSTRAINT comment_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT comment_fk_user FOREIGN KEY (fk_user) REFERENCES _user (name)
            ON UPDATE CASCADE
               ON DELETE SET NULL;

    --
    -- Contraintes pour la table `mod_collection`
    --
ALTER TABLE mod_collection
    ADD CONSTRAINT mod_collection_fk_user FOREIGN KEY (fk_user) REFERENCES _user (name)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_collection_fk_game FOREIGN KEY (fk_game) REFERENCES game (name)
            ON UPDATE CASCADE
               ON DELETE SET NULL;

    --
    -- Contraintes pour la table `mod_mod_collection`
    --
ALTER TABLE mod_mod_collection
    ADD CONSTRAINT mod_mod_collection_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
        ADD CONSTRAINT mod_mod_collection_fk_mod_collection FOREIGN KEY (fk_mod_collection_name, fk_mod_collection_user_name) REFERENCES mod_collection (name, fk_user)
            ON UPDATE CASCADE
               ON DELETE CASCADE;

    --
    -- Contraintes pour la table `mod_pack`
    --
ALTER TABLE mod_pack
    ADD CONSTRAINT mod_pack_fk_game FOREIGN KEY (fk_game) REFERENCES game (name)
        ON UPDATE CASCADE
        ON DELETE CASCADE;

--
-- Contraintes pour la table `mod_mod_pack`
--
ALTER TABLE mod_mod_pack
    ADD CONSTRAINT mod_mod_pack_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
        ADD CONSTRAINT mod_mod_pack_fk_mod_pack FOREIGN KEY (fk_mod_pack_name, fk_mod_pack_game_name) REFERENCES mod_pack (name, fk_game)
            ON UPDATE CASCADE
               ON DELETE CASCADE;

    --
    -- Contraintes pour la table `moder`
    --
ALTER TABLE moder
    ADD CONSTRAINT moder_fk_user FOREIGN KEY (fk_user) REFERENCES _user (name)
        ON UPDATE CASCADE
        ON DELETE SET NULL;

--
-- Contraintes pour la table `mod_moder`
--
ALTER TABLE mod_moder
    ADD CONSTRAINT mod_moder_fk_mod FOREIGN KEY (fk_mod_name, fk_mod_game_name) REFERENCES mod (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_moder_fk_moder FOREIGN KEY (fk_moder) REFERENCES moder (pseudo)
            ON UPDATE CASCADE
               ON DELETE CASCADE;

    --
    -- Contraintes pour la table `mod_pack_moder`
    --
ALTER TABLE mod_pack_moder
    ADD CONSTRAINT mod_pack_moder_fk_mod_pack FOREIGN KEY (fk_mod_pack_name, fk_mod_pack_game_name) REFERENCES mod_pack (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_moder_fk_moder FOREIGN KEY (fk_moder) REFERENCES moder (pseudo)
            ON UPDATE CASCADE
               ON DELETE CASCADE;

    --
    -- Contraintes pour la table `mod_pack_version`
    --
ALTER TABLE mod_pack_version
    ADD CONSTRAINT mod_pack_version_fk_mod_pack FOREIGN KEY (fk_mod_pack_name, fk_mod_pack_game_name) REFERENCES mod_pack (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_version_fk_version FOREIGN KEY (fk_version) REFERENCES version (name);

--
-- Contraintes pour la table `mod_pack_impact`
--
ALTER TABLE mod_pack_impact
    ADD CONSTRAINT mod_pack_impact_fk_mod_pack FOREIGN KEY (fk_mod_pack_name, fk_mod_pack_game_name) REFERENCES mod_pack (name, fk_game)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
        ADD CONSTRAINT mod_impact_fk_impact FOREIGN KEY (fk_impact) REFERENCES impact (name);

-- TRIGGER/VIEW SCRIPT

-- FUNCTION -------------------------------------------------------------------------------------------------------------------------------------------------------------


-- Function that create a view for the username given.
-- The view will be called [username]_view and contain all versions of all mod collections of this user
CREATE OR REPLACE FUNCTION create_view_user()
    RETURNS TRIGGER AS
$$
BEGIN
    EXECUTE 'CREATE OR REPLACE VIEW ' || NEW.name || '_view AS
             SELECT *
             FROM mod_collection_logger
             WHERE fk_mod_collection_user_name = ''' || NEW.name || '''
             ORDER BY fk_mod_collection_name, version DESC;';
    RETURN NULL;
END;
$$
    LANGUAGE plpgsql;

-- Function that drop the view user called [username]_view and the content
CREATE OR REPLACE FUNCTION drop_view_user()
    RETURNS TRIGGER AS
$$
DECLARE
    username varchar(255);
BEGIN
    username := OLD.name;
    EXECUTE 'DELETE
             FROM ' || username || '_view;';
    EXECUTE 'DROP VIEW IF EXISTS ' || username || '_view;';
    RETURN NULL;
END;
$$
    LANGUAGE plpgsql;

-- Function that get all mods from a mod collection given (format is : fkMod1;fkMod2;...;fkModN) as text
CREATE OR REPLACE FUNCTION get_mods_from_mod_collection(mod_collection_name varchar(255), username varchar(255))
    RETURNS text AS
$$
DECLARE
    fk_mod_list text;
BEGIN
    -- Using STRING_AGG allow us to concat all fk_mod_name result as a string. About as a sum for string.
    -- The second parameter is the separator
    SELECT string_agg(fk_mod_name, ';')
    INTO fk_mod_list
    FROM mod_mod_collection
    WHERE fk_mod_collection_name = mod_collection_name
      AND fk_mod_collection_user_name = username;

    RETURN fk_mod_list;
END;
$$
    LANGUAGE plpgsql;

-- Function that get the mod collection from the pk of it
CREATE OR REPLACE FUNCTION get_mod_collection(mod_collection_name varchar(255), username varchar(255))
    RETURNS mod_collection AS
$$
DECLARE
    mod_collection_res mod_collection;
BEGIN
    -- Will return only one line, we stock it on mod_collection_res to return it
    SELECT *
    INTO mod_collection_res
    FROM mod_collection
    WHERE name = mod_collection_name
      AND fk_user = username;

    RETURN mod_collection_res;
END;
$$
    LANGUAGE plpgsql;

-- Function that log the version _row of mod collection on mod_collection_logger
CREATE OR REPLACE FUNCTION log_mod_collection()
    RETURNS TRIGGER AS
$$
DECLARE
    _row mod_collection;
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF TG_TABLE_NAME = 'mod_collection' THEN
            _row := NEW;
        ELSIF TG_TABLE_NAME = 'mod_mod_collection' THEN
            _row := get_mod_collection(NEW.fk_mod_collection_name, NEW.fk_mod_collection_user_name);
        END IF;
    ELSIF TG_OP = 'UPDATE' OR TG_OP = 'DELETE' THEN
        IF TG_TABLE_NAME = 'mod_collection' THEN
            _row := OLD;
        ELSIF TG_TABLE_NAME = 'mod_mod_collection' THEN
            _row := get_mod_collection(OLD.fk_mod_collection_name, OLD.fk_mod_collection_user_name);
        END IF;
    END IF;

    INSERT INTO mod_collection_logger
    VALUES (now(),
            _row.name,
            _row.fk_user,
            _row.relative_path_to_folder,
            _row.logo,
            _row.description,
            _row.fk_game,
            get_mods_from_mod_collection(_row.name, _row.fk_user)); -- this function make sense now, don't it?
    IF TG_WHEN = 'BEFORE' THEN
        RETURN NEW;
    ELSE
        RETURN null;
    END IF;
END;
$$
    LANGUAGE plpgsql;

-- Function that cancel an update
CREATE OR REPLACE FUNCTION cancel_update()
    RETURNS TRIGGER AS
$$
BEGIN
    RETURN OLD; -- Could be done with users and rights but I think it's a lot much funny to do it like this
END;
$$
    LANGUAGE plpgsql;


-- TRIGGERS -------------------------------------------------------------------------------------------------------------------------------------------------------------

-- On user insert we create a view associated
CREATE OR REPLACE TRIGGER after_user_insert
    AFTER INSERT
    ON _user
    FOR EACH ROW
EXECUTE FUNCTION create_view_user();


-- On user delete we delete the view associated (and the content meaning the mod_collection of this user)
CREATE OR REPLACE TRIGGER after_user_delete
    AFTER DELETE
    ON _user
    FOR EACH ROW
EXECUTE FUNCTION drop_view_user();

-- On mod_collection update we save the before update value of it on mod_collection_logger
CREATE OR REPLACE TRIGGER before_mod_collection_update
    BEFORE UPDATE
    ON mod_collection
    FOR EACH ROW
EXECUTE FUNCTION log_mod_collection();

-- On mods added to the mod_collection we save the before insert value of it on mod_collection_logger
CREATE OR REPLACE TRIGGER before_mod_mod_collection_insert
    BEFORE INSERT
    ON mod_mod_collection
    FOR EACH ROW
EXECUTE FUNCTION log_mod_collection();

-- On mods updated on the mod_collection we save the before update value of it on mod_collection_logger
-- Note that we probably never update as it's would make no sense
CREATE OR REPLACE TRIGGER before_mod_mod_collection_update
    BEFORE UPDATE
    ON mod_mod_collection
    FOR EACH ROW
EXECUTE FUNCTION log_mod_collection();

-- On mods deleted of the mod_collection we save the before delete value of it on mod_collection_logger
CREATE OR REPLACE TRIGGER before_mod_mod_collection_delete
    BEFORE DELETE
    ON mod_mod_collection
    FOR EACH ROW
EXECUTE FUNCTION log_mod_collection();

-- On mod collection logger update we cancel it because it's a logger so it's not supposed to be update (or even deleted but let's allow it for specials cases)
CREATE OR REPLACE TRIGGER before_mod_mod_collection_delete
    BEFORE UPDATE
    ON mod_collection_logger
    FOR EACH ROW
EXECUTE FUNCTION cancel_update();

-- INSERT SCRIPT

-- Sources (not exhaustive) : https://en.wikipedia.org/wiki/List_of_video_game_genres, https://www.9minecraft.net/
BEGIN;
--INSERT INTO ENTITY
-- Table genre
INSERT INTO genre VALUES
                      ('Action'),
                      ('Platform (or Platformer)'),
                      ('Shooter'),
                      ('Fighting'),
                      ('Beat ''em ups (or brawlers)'),
                      ('Stealth'),
                      ('Survival'),
                      ('Rhythm'),
                      ('Battle Royale'),
                      ('Action-Adventure'),
                      ('Survival Horror'),
                      ('Metroidvania'),
                      ('Adventure'),
                      ('Text Adventure'),
                      ('Graphic Adventure'),
                      ('Visual Novels'),
                      ('Interactive Movie'),
                      ('Real-Time 3D Adventure'),
                      ('Puzzle'),
                      ('Breakout Clone (Or block-breaking or ball-and-paddle)'),
                      ('Logical'),
                      ('Physics'),
                      ('Programming'),
                      ('Trial-and-error / Exploration'),
                      ('Hidden object (or hidden picture or hidden object puzzle adventure(HOPA))'),
                      ('Reveal The Picture'),
                      ('Tile-matching'),
                      ('Traditional puzzle'),
                      ('Puzzle-platform'),
                      ('Role-playing'),
                      ('Action Role-playing (or Action RPG)'),
                      ('Massively Multiplayer Online Role-playing (or MMORPG)'),
                      ('Roguelike'),
                      ('Tactical Role-playing (Tactical RPG)'),
                      ('Sandbox RPG (or Open World RPG)'),
                      ('First-person party-based RPG (or blobber or Dungeon RPG (DRPG)'),
                      ('Monster Tamer'),
                      ('Simulation'),
                      ('Construction and Management Simulation (CMS)'),
                      ('City-building'),
                      ('Business Simulation'),
                      ('Government Simulation'),
                      ('Life Simulation (or Artificial Life)'),
                      ('Pet-raising Simulation (or Digital Pet)'),
                      ('Social Simulation'),
                      ('Vehicle Simulation'),
                      ('Flight Simulation'),
                      ('Racing'),
                      ('Space Flight Simulator'),
                      ('Train Simulator'),
                      ('Vehicular Combat (or Car Combat)'),
                      ('Strategy'),
                      ('4X'),
                      ('Artillery'),
                      ('Auto battler (or Auto Chess)'),
                      ('Multiplayer Online Battle Arena (MOBA)'),
                      ('Real-Time Strategy (RTS)'),
                      ('Real-Time Tactics (RTT)'),
                      ('Tower Defense'),
                      ('Turn-Based Strategy (TBS)'),
                      ('Turn-Based Tactics (TBT)'),
                      ('Wargame'),
                      ('Grand Strategy Wargame'),
                      ('Sports'),
                      ('Sport Racing'),
                      ('Competitive (or not-traditional/fictional sport)'),
                      ('Sports-based fighting'),
                      ('Massively Multiplayer Online (MMO or MMOG)'),
                      ('Board'),
                      ('Card'),
                      ('Casino'),
                      ('Digital Collectible Card (DCCG)'),
                      ('Digital Therapeutic'),
                      ('Gacha'),
                      ('Horror'),
                      ('Idle'),
                      ('Party'),
                      ('Photography'),
                      ('Social Deduction'),
                      ('Trivial'),
                      ('Typing'),
                      ('Advergame'),
                      ('Art'),
                      ('Casual'),
                      ('Christian'),
                      ('Educational'),
                      ('ESports'),
                      ('Exergame (Fitness)'),
                      ('Personalized'),
                      ('Serious'),
                      ('Sandbox'),
                      ('Creative'),
                      ('Open World');

-- Table impact
INSERT INTO impact VALUES
                       ('Adventure'),
                       ('RPG'),
                       ('Animal'),
                       ('Pet'),
                       ('Anime'),
                       ('Manga'),
                       ('Armor'),
                       ('Weapon'),
                       ('Backpack'),
                       ('Boat'),
                       ('Boss'),
                       ('Bow'),
                       ('Arrow'),
                       ('Building'),
                       ('Structure'),
                       ('Cape'),
                       ('Elytra'),
                       ('Car'),
                       ('Vehicle'),
                       ('Christmas'),
                       ('Cut Down Trees'),
                       ('Decoration'),
                       ('Furniture'),
                       ('Dimension'),
                       ('Biome'),
                       ('Dragon'),
                       ('Dungeon'),
                       ('Cave'),
                       ('Item Transport'),
                       ('Fly'),
                       ('Fluid'),
                       ('Food'),
                       ('FPS, CPU, RAM'),
                       ('Gun'),
                       ('Information'),
                       ('Guide'),
                       ('Library (API)'),
                       ('Lucky Block'),
                       ('Magic'),
                       ('Minecraft Pixelmon'),
                       ('Minigame'),
                       ('Shaders, Graphics, Sound'),
                       ('Minimap'),
                       ('Mob'),
                       ('Creature'),
                       ('Monster'),
                       ('Morph'),
                       ('Photo'),
                       ('Video'),
                       ('Redstone'),
                       ('Server Utility'),
                       ('Ore'),
                       ('Flower'),
                       ('Agriculture'),
                       ('Technology'),
                       ('Storage'),
                       ('Sword'),
                       ('Blade'),
                       ('Automation'),
                       ('Auto-Crafting'),
                       ('The End'),
                       ('Nether'),
                       ('Tool'),
                       ('Utility'),
                       ('Energy'),
                       ('Machine'),
                       ('Engine'),
                       ('Explosive'),
                       ('Cheats');

-- Table version
INSERT INTO version VALUES
                        ('1.0.0'),
                        ('1.0.1'),
                        ('1.1'),
                        ('1.2.1'),
                        ('1.2.2'),
                        ('1.2.3'),
                        ('1.2.4'),
                        ('1.2.5'),
                        ('1.3.1'),
                        ('1.3.2'),
                        ('1.4.0'),
                        ('1.4.1'),
                        ('1.4.2'),
                        ('1.4.3'),
                        ('1.4.4'),
                        ('1.4.5'),
                        ('1.4.6'),
                        ('1.4.7'),
                        ('1.5'),
                        ('1.5.1'),
                        ('1.5.2'),
                        ('1.6.1'),
                        ('1.6.2'),
                        ('1.6.3'),
                        ('1.6.4'),
                        ('1.7.2'),
                        ('1.7.4'),
                        ('1.7.5'),
                        ('1.7.6'),
                        ('1.7.7'),
                        ('1.7.8'),
                        ('1.7.9'),
                        ('1.7.10'),
                        ('1.8'),
                        ('1.8.1'),
                        ('1.8.2'),
                        ('1.8.3'),
                        ('1.8.4'),
                        ('1.8.5'),
                        ('1.8.6'),
                        ('1.8.7'),
                        ('1.8.8'),
                        ('1.8.9'),
                        ('1.9'),
                        ('1.9.1'),
                        ('1.9.2'),
                        ('1.9.3'),
                        ('1.9.4'),
                        ('1.10'),
                        ('1.10.1'),
                        ('1.10.2'),
                        ('1.11'),
                        ('1.11.1'),
                        ('1.11.2'),
                        ('1.12'),
                        ('1.12.1'),
                        ('1.12.2'),
                        ('1.13'),
                        ('1.13.1'),
                        ('1.13.2'),
                        ('1.14'),
                        ('1.14.1'),
                        ('1.14.2'),
                        ('1.14.3'),
                        ('1.14.4'),
                        ('1.15'),
                        ('1.15.1'),
                        ('1.15.2'),
                        ('1.16'),
                        ('1.16.1'),
                        ('1.16.2'),
                        ('1.16.3'),
                        ('1.16.4'),
                        ('1.16.5'),
                        ('1.17'),
                        ('1.17.1'),
                        ('1.18'),
                        ('1.18.1'),
                        ('1.18.2'),
                        ('1.19'),
                        ('1.19.1'),
                        ('1.19.2'),
                        ('1.19.3'),
                        ('1.19.4'),
                        ('1.20'),
                        ('1.20.1'),
                        ('1.20.2'),
                        ('1.20.3'),
                        ('1.21');

-- Table game
INSERT INTO game VALUES
                     ('Minecraft',
                      'https://st5.depositphotos.com/69187398/66863/v/450/depositphotos_668639188-stock-illustration-vector-logo-video-game-minecraft.jpg',
                      'Explorez votre propre monde unique, survivez à la nuit et créez tout ce que vous pouvez imaginer !',
                      './mods'),
                     ('GameProposition1 (Adventure, Sandbox)',
                      NULL,
                      'To test the request proposition (should appear for each _user playing Minecraft)',
                      NULL),
                     ('GameProposition2 (Adventure)',
                      NULL,
                      'To test the request proposition (should appear for each _user playing Minecraft)',
                      NULL),
                     ('GameProposition3 (Sandbox)',
                      NULL,
                      'To test the request proposition (should appear for each _user playing Minecraft)',
                      NULL),
                     ('GameProposition4 (Serious, Christian)',
                      NULL,
                      'To test the request proposition (should not appear for each _user playing Minecraft)',
                      NULL);

-- Table mod
INSERT INTO mod VALUES
    ('Forge', 'Minecraft',
     'http://files.minecraftforge.net/',
     'Forge is a free, open-source modding API all of your favourite mods use! Modifications to the Minecraft base files to assist in compatibility between mods.',
     'https://pbs.twimg.com/profile_images/778706890914095109/fhMDH9o6_400x400.jpg');
INSERT INTO mod VALUES
    ('LibVulpes', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2017/03/LibVulpes.jpg',
     'LibVulpes est une petite bibliothèque contenant des fonctions et des classes communes aux mods de zmaster587. Actuellement requis pour exécuter le mod suivant : Advanced Rocketry.',
     'http://www.9minecraft.net/libvulpes/');
INSERT INTO mod VALUES
    ('Advanced Rocketry', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2017/10/Advanced-Rocketry-Mod-Logo.jpg',
     'Advanced Rocketry Mod est un mod Minecraft conçu pour ajouter au jeu des planètes aléatoires ou spécifiées par le joueur. Ces planètes ont chacune des propriétés uniques telles que la densité de l''atmosphère, la distance au soleil, la composition de l''atmosphère, la température moyenne et la taille. Les joueurs peuvent construire des fusées à partir de blocs pour voyager vers ces autres mondes.',
     'http://www.9minecraft.net/advanced-rocketry-mod/');
INSERT INTO mod VALUES
    ('Advent of Ascension', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Logo.jpg',
     'Advent of Ascension Mod ajoute de nombreuses dimensions uniques, des tas de nouveaux monstres, de nouveaux boss. Toutes sortes de nouvelles armes, armures, outils et enchantements qui peuvent être placés dessus, ainsi que des matériaux qui servent tous à des fins uniques. Le mod ajoute également plus de 1000 nouveaux effets sonores et musiques.',
     'http://www.9minecraft.net/advent-of-ascension-mod');
INSERT INTO mod VALUES
    ('AdventureCraft', 'Minecraft',
     'https://media.forgecdn.net/attachments/thumbnails/10/610/310/172/AdventureCraft_Title.png',
     'AdventureCraft est un mod Work-In-Progress, renommé à partir du mod précédent, MythicalCraft, il ajoute de nouveaux monstres, blocs, objets et plus encore à votre monde Minecraft.',
     'https://www.curseforge.com/minecraft/mc-mods/adventurecraft/files');
INSERT INTO mod VALUES
    ('Gilded Games Util', 'Minecraft',
     'https://media.forgecdn.net/attachments/19/319/10698602_289797107895427_7006135484967270906_n.png',
     'Mod qui facilite la programmation des mods Minecraft. \n Aide actuellement à se connecter aux joueurs et aux mondes, simplifie la création d''interface graphique, optimise l''éclairage de Minecraft et gère les E/S. \n Dans les versions ultérieures, cela inclura davantage d''utilitaires GUI, un système Party abstrait et des astuces d''IA utiles.',
     'https://www.curseforge.com/minecraft/mc-mods/gilded-games-util');
INSERT INTO mod VALUES
    ('Orbis API', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2018/04/Orbis-API.png',
     'L''API Orbis est destinée à être utilisée avec les mods qui utilisent le mod Orbis de Gilded Games pour les fonctionnalités de structure.',
     'https://www.9minecraft.net/orbis-api/');
INSERT INTO mod VALUES
    ('Aether 2', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Logo.jpg',
     'Aether 2 (Aether II) est un mod très complet pour Minecraft ajoutant un royaume opposé au Nether. L''Aether 2 est la suite de l''Aether 1. Beaucoup de choses ont changé depuis l''époque de l''Aether 1, mais il reste toujours une collaboration entre des personnes de différentes disciplines avec un objectif commun de créer une dimension nouvelle et unique.',
     'http://www.9minecraft.net/aether-2-mod');
INSERT INTO mod VALUES
    ('Animal Bikes', 'Minecraft',
     'http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-1024x576.jpg',
     'Animal Bikes Mod vous permet d''invoquer facilement des animaux et d''autres créatures chevauchables. Tous ces vélos ont des attributs/capacités spéciales. Pour créer ces vélos, vous aurez besoin d''une selle. Cette selle peut être trouvée dans les coffres des donjons ou être créée avec la recette ci-dessous. Ce mod fonctionne à la fois en solo et en multijoueur.',
     'http://www.9minecraft.net/animal-bikes-mod/');
INSERT INTO mod VALUES
    ('Applied Energistics 2', 'Minecraft',
     NULL,
     'Applied Energistics 2 (AE2) est un mod complet pour Minecraft qui introduit une approche unique de la gestion des stocks dans le jeu. Il présente un thème futuriste basé sur la technologie, centré sur le concept de l''utilisation de l''énergie et de la technologie pour convertir la matière en énergie et vice versa.',
     'https://minecraft.curseforge.com/projects/applied-energistics-2');

-- Table screenshot
INSERT INTO screenshot VALUES
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-1.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-2.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-3.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-4.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-5.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-7.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-8.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/03/Advanced-Rocketry-Mod-6.jpg',
                            'Advanced Rocketry', 'Minecraft'),
                           ('https://youtu.be/qzfAff5N-34;https://youtu.be/uJuz01ySMW8',
                            'Advanced Rocketry', 'Minecraft');
INSERT INTO screenshot VALUES
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-1.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-2.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-3.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-4.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-5.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-6.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-7.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-8.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-9.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-10.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Features-11.png',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-50.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-51.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-52.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-53.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-54.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-55.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-56.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-57.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-58.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-59.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-60.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-61.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/09/Advent-of-Ascension-Mod-Screenshots-62.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-1.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-2.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-3.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-4.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-5.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-6.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-7.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-8.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-9.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-10.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-11.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-12.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-13.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-14.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-15.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-16.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-17.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-18.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-19.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-20.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-21.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-22.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-23.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-24.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-25.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-26.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-27.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-28.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-29.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-30.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-31.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-32.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-33.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-34.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-35.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-36.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-37.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-38.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Screenshots-39.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-1.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-2.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-3.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-4.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-5.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-6.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-7.jpg',
                            'Advent of Ascension', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2016/08/Advent-of-Ascension-Mod-Crafting-Recipes-8.jpg',
                            'Advent of Ascension', 'Minecraft');
INSERT INTO screenshot VALUES
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Features-1.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-1.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-2.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-3.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-4.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-5.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-6.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-7.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-8.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-9.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-10.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-11.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-12.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/12/Aether-2-Mod-2018-Screenshots-13.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-1-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-2-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-3-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-4-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-5-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-6-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-7-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-8-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-9-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-10-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-11-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-12-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-13-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-14-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-15-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-16-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-17-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-18-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-19-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-20-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-21-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-22-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-23-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-24-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-25-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-26-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-27-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-28-1024x576.jpg',
                            'Aether 2', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/02/Aether-2-Mod-Screenshots-29-1024x576.jpg',
                            'Aether 2', 'Minecraft');
INSERT INTO screenshot VALUES
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-1-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-2-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-3-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-4-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-5-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-6-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-7-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-8-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-9-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-10-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-11-1024x576.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-12.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-13.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-14.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Screenshots-15.jpg',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-1.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-2.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Bunny.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Cow.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Ocelot.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Sheep.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Snow-Golem.png',
                            'Animal Bikes', 'Minecraft'),
                           ('http://www.9minecraft.net/wp-content/uploads/2017/01/Animal-Bikes-Mod-Crafting-Recipes-Squid.png',
                            'Animal Bikes', 'Minecraft');
INSERT INTO screenshot VALUES
    ('https://i.imgur.com/K02yDrh.png',
     'Applied Energistics 2', 'Minecraft');

-- Table _user
INSERT INTO _user VALUES
    ('LeVraiGG',
     'dee78c0da836c8f9130c2b7a0184e4f2c76b3896ad0ca7f0c54eeba00dea03a4', --mdp is loLmDr_01!
     TRUE);

-- Table moder
INSERT INTO moder VALUES ('Inconnu');
INSERT INTO moder VALUES ('LeVraiGG', 'LeVraiGG');
INSERT INTO moder VALUES ('zmaster587');
INSERT INTO moder VALUES ('Xolova');
INSERT INTO moder VALUES ('TheAetherTeam');
INSERT INTO moder VALUES ('Noppes');
INSERT INTO moder VALUES ('AlgorithmX2');
INSERT INTO moder VALUES ('thatsIch');
INSERT INTO moder VALUES ('FireBall1725');

-- Table comment
INSERT INTO comment (content, fk_mod_name, fk_mod_game_name, fk_user) VALUES
    ('Good mod! I would just change the name to ULTRA DIMENSIONAL SPACESHIP CREATOR. It would be great if you do that.',
     'Advanced Rocketry', 'Minecraft',
     'LeVraiGG');

-- Table note
INSERT INTO note VALUES
    ('Advanced Rocketry', 'Minecraft',
     'LeVraiGG',
     5);

-- Table mod_pack
INSERT INTO mod_pack VALUES
    ('Test', 'Minecraft',
     'https://yt3.ggpht.com/ytc/APkrFKb0lgUQ-yi2jMgEBm1KIYWql4MWPkGeZlqenrYatg=s600-c-k-c0x00ffffff-no-rj-rp-mo',
     'A test modpack. It contains all mods needed to use Advanced Rocketry properly (it means Forge, LibVulpes, and Advanced Rocketry).',
     'https://www.swisstransfer.com/d/e41185ca-8ce8-4228-965d-13f244119845');

-- Table mod_collection
INSERT INTO mod_collection VALUES
    ('Test', 'LeVraiGG',
     'C:\Users\tuxca\Documents\S3_BDR\Pratique\Projet\modPackTestSim\1.7.10', -- not very relative but w/e it just a test
     'https://yt3.ggpht.com/ytc/APkrFKb0lgUQ-yi2jMgEBm1KIYWql4MWPkGeZlqenrYatg=s600-c-k-c0x00ffffff-no-rj-rp-mo',
     'A test mod collection based on the modpack Test. It contains all mods needed to use Advanced Rocketry properly (it means Forge, LibVulpes, and Advanced Rocketry).',
     'Minecraft');

-- INSERT INTO FK/TR
-- Table genre_game
INSERT INTO genre_game VALUES
                           ('Minecraft', 'Sandbox'),
                           ('Minecraft', 'Adventure');
INSERT INTO genre_game VALUES
                           ('GameProposition1 (Adventure, Sandbox)', 'Adventure'),
                           ('GameProposition1 (Adventure, Sandbox)', 'Sandbox');
INSERT INTO genre_game VALUES
    ('GameProposition2 (Adventure)','Adventure');
INSERT INTO genre_game VALUES
    ('GameProposition3 (Sandbox)', 'Sandbox');
INSERT INTO genre_game VALUES
                           ('GameProposition4 (Serious, Christian)', 'Serious'),
                           ('GameProposition4 (Serious, Christian)', 'Christian');

-- Table game_version
INSERT INTO game_version VALUES
                             ('Minecraft', '1.0.0'),
                             ('Minecraft', '1.0.1'),
                             ('Minecraft', '1.1'),
                             ('Minecraft', '1.2.1'),
                             ('Minecraft', '1.2.2'),
                             ('Minecraft', '1.2.3'),
                             ('Minecraft', '1.2.4'),
                             ('Minecraft', '1.2.5'),
                             ('Minecraft', '1.3.1'),
                             ('Minecraft', '1.3.2'),
                             ('Minecraft', '1.4.2'),
                             ('Minecraft', '1.4.4'),
                             ('Minecraft', '1.4.5'),
                             ('Minecraft', '1.4.6'),
                             ('Minecraft', '1.4.7'),
                             ('Minecraft', '1.5'),
                             ('Minecraft', '1.5.1'),
                             ('Minecraft', '1.5.2'),
                             ('Minecraft', '1.6.1'),
                             ('Minecraft', '1.6.2'),
                             ('Minecraft', '1.6.4'),
                             ('Minecraft', '1.7.2'),
                             ('Minecraft', '1.7.4'),
                             ('Minecraft', '1.7.5'),
                             ('Minecraft', '1.7.6'),
                             ('Minecraft', '1.7.7'),
                             ('Minecraft', '1.7.8'),
                             ('Minecraft', '1.7.9'),
                             ('Minecraft', '1.7.10'),
                             ('Minecraft', '1.8'),
                             ('Minecraft', '1.8.1'),
                             ('Minecraft', '1.8.2'),
                             ('Minecraft', '1.8.3'),
                             ('Minecraft', '1.8.4'),
                             ('Minecraft', '1.8.5'),
                             ('Minecraft', '1.8.6'),
                             ('Minecraft', '1.8.7'),
                             ('Minecraft', '1.8.8'),
                             ('Minecraft', '1.8.9'),
                             ('Minecraft', '1.9'),
                             ('Minecraft', '1.9.1'),
                             ('Minecraft', '1.9.2'),
                             ('Minecraft', '1.9.3'),
                             ('Minecraft', '1.9.4'),
                             ('Minecraft', '1.10'),
                             ('Minecraft', '1.10.1'),
                             ('Minecraft', '1.10.2'),
                             ('Minecraft', '1.11'),
                             ('Minecraft', '1.11.1'),
                             ('Minecraft', '1.11.2'),
                             ('Minecraft', '1.12'),
                             ('Minecraft', '1.12.1'),
                             ('Minecraft', '1.12.2'),
                             ('Minecraft', '1.13'),
                             ('Minecraft', '1.13.1'),
                             ('Minecraft', '1.13.2'),
                             ('Minecraft', '1.14'),
                             ('Minecraft', '1.14.1'),
                             ('Minecraft', '1.14.2'),
                             ('Minecraft', '1.14.3'),
                             ('Minecraft', '1.14.4'),
                             ('Minecraft', '1.15'),
                             ('Minecraft', '1.15.1'),
                             ('Minecraft', '1.15.2'),
                             ('Minecraft', '1.16'),
                             ('Minecraft', '1.16.1'),
                             ('Minecraft', '1.16.2'),
                             ('Minecraft', '1.16.3'),
                             ('Minecraft', '1.16.4'),
                             ('Minecraft', '1.16.5'),
                             ('Minecraft', '1.17'),
                             ('Minecraft', '1.17.1'),
                             ('Minecraft', '1.18'),
                             ('Minecraft', '1.18.1'),
                             ('Minecraft', '1.18.2'),
                             ('Minecraft', '1.19'),
                             ('Minecraft', '1.19.1'),
                             ('Minecraft', '1.19.2'),
                             ('Minecraft', '1.19.3'),
                             ('Minecraft', '1.19.4'),
                             ('Minecraft', '1.20'),
                             ('Minecraft', '1.20.1'),
                             ('Minecraft', '1.20.2'),
                             ('Minecraft', '1.20.3'),
                             ('Minecraft', '1.21');
-- Table mod_version
INSERT INTO mod_version VALUES
                            ('Forge', 'Minecraft', '1.1'),
                            ('Forge', 'Minecraft', '1.2.3'),
                            ('Forge', 'Minecraft', '1.2.4'),
                            ('Forge', 'Minecraft', '1.2.5'),
                            ('Forge', 'Minecraft', '1.3.2'),
                            ('Forge', 'Minecraft', '1.4.0'),
                            ('Forge', 'Minecraft', '1.4.1'),
                            ('Forge', 'Minecraft', '1.4.2'),
                            ('Forge', 'Minecraft', '1.4.3'),
                            ('Forge', 'Minecraft', '1.4.4'),
                            ('Forge', 'Minecraft', '1.4.5'),
                            ('Forge', 'Minecraft', '1.4.6'),
                            ('Forge', 'Minecraft', '1.4.7'),
                            ('Forge', 'Minecraft', '1.5'),
                            ('Forge', 'Minecraft', '1.5.1'),
                            ('Forge', 'Minecraft', '1.5.2'),
                            ('Forge', 'Minecraft', '1.6.1'),
                            ('Forge', 'Minecraft', '1.6.2'),
                            ('Forge', 'Minecraft', '1.6.3'),
                            ('Forge', 'Minecraft', '1.6.4'),
                            ('Forge', 'Minecraft', '1.7.2'),
                            ('Forge', 'Minecraft', '1.7.10'),
                            ('Forge', 'Minecraft', '1.8'),
                            ('Forge', 'Minecraft', '1.8.8'),
                            ('Forge', 'Minecraft', '1.8.9'),
                            ('Forge', 'Minecraft', '1.9'),
                            ('Forge', 'Minecraft', '1.9.4'),
                            ('Forge', 'Minecraft', '1.10'),
                            ('Forge', 'Minecraft', '1.10.2'),
                            ('Forge', 'Minecraft', '1.11'),
                            ('Forge', 'Minecraft', '1.11.2'),
                            ('Forge', 'Minecraft', '1.12'),
                            ('Forge', 'Minecraft', '1.12.1'),
                            ('Forge', 'Minecraft', '1.12.2'),
                            ('Forge', 'Minecraft', '1.13.2'),
                            ('Forge', 'Minecraft', '1.14.2'),
                            ('Forge', 'Minecraft', '1.14.3'),
                            ('Forge', 'Minecraft', '1.14.4'),
                            ('Forge', 'Minecraft', '1.15'),
                            ('Forge', 'Minecraft','1.15.1'),
                            ('Forge', 'Minecraft', '1.15.2'),
                            ('Forge', 'Minecraft', '1.16.1'),
                            ('Forge', 'Minecraft', '1.16.2'),
                            ('Forge', 'Minecraft', '1.16.3'),
                            ('Forge', 'Minecraft', '1.16.4'),
                            ('Forge', 'Minecraft', '1.16.5'),
                            ('Forge', 'Minecraft', '1.17.1'),
                            ('Forge', 'Minecraft', '1.18'),
                            ('Forge', 'Minecraft', '1.18.1'),
                            ('Forge', 'Minecraft', '1.18.2'),
                            ('Forge', 'Minecraft', '1.19'),
                            ('Forge', 'Minecraft', '1.19.1'),
                            ('Forge', 'Minecraft', '1.19.2'),
                            ('Forge', 'Minecraft', '1.19.3'),
                            ('Forge', 'Minecraft', '1.19.4'),
                            ('Forge', 'Minecraft', '1.20'),
                            ('Forge', 'Minecraft', '1.20.1'),
                            ('Forge', 'Minecraft', '1.20.2');
INSERT INTO mod_version VALUES
                            ('LibVulpes', 'Minecraft', '1.7.10'),
                            ('LibVulpes', 'Minecraft', '1.10.2'),
                            ('LibVulpes', 'Minecraft', '1.11.2'),
                            ('LibVulpes', 'Minecraft', '1.12.2'),
                            ('LibVulpes', 'Minecraft', '1.16.5');
INSERT INTO mod_version VALUES
                            ('Advanced Rocketry', 'Minecraft', '1.7.10'),
                            ('Advanced Rocketry', 'Minecraft', '1.10.2'),
                            ('Advanced Rocketry', 'Minecraft', '1.11.2'),
                            ('Advanced Rocketry', 'Minecraft', '1.12.2'),
                            ('Advanced Rocketry', 'Minecraft', '1.16.5');
INSERT INTO mod_version VALUES
                            ('Advent of Ascension', 'Minecraft', '1.7.10'),
                            ('Advent of Ascension', 'Minecraft', '1.12.2'),
                            ('Advent of Ascension', 'Minecraft', '1.15.2'),
                            ('Advent of Ascension', 'Minecraft', '1.16.5'),
                            ('Advent of Ascension', 'Minecraft', '1.18.2'),
                            ('Advent of Ascension', 'Minecraft', '1.19'),
                            ('Advent of Ascension', 'Minecraft', '1.19.1'),
                            ('Advent of Ascension', 'Minecraft', '1.19.2'),
                            ('Advent of Ascension', 'Minecraft', '1.19.3'),
                            ('Advent of Ascension', 'Minecraft', '1.19.4'),
                            ('Advent of Ascension', 'Minecraft', '1.20.1');
INSERT INTO mod_version VALUES
    ('AdventureCraft', 'Minecraft', '1.6.4');
INSERT INTO mod_version VALUES
    ('Gilded Games Util', 'Minecraft', '1.7.10');
INSERT INTO mod_version VALUES
    ('Orbis API', 'Minecraft', '1.12.2');
INSERT INTO mod_version VALUES
                            ('Aether 2', 'Minecraft', '1.7.10'),
                            ('Aether 2', 'Minecraft', '1.10.2'),
                            ('Aether 2', 'Minecraft', '1.11.2'),
                            ('Aether 2', 'Minecraft', '1.12.2');
INSERT INTO mod_version VALUES
                            ('Animal Bikes', 'Minecraft', '1.4.6'),
                            ('Animal Bikes', 'Minecraft', '1.4.7'),
                            ('Animal Bikes', 'Minecraft', '1.5'),
                            ('Animal Bikes', 'Minecraft', '1.5.1'),
                            ('Animal Bikes', 'Minecraft', '1.5.2'),
                            ('Animal Bikes', 'Minecraft', '1.6.1'),
                            ('Animal Bikes', 'Minecraft', '1.6.2'),
                            ('Animal Bikes', 'Minecraft', '1.6.4'),
                            ('Animal Bikes', 'Minecraft', '1.7.2'),
                            ('Animal Bikes', 'Minecraft', '1.7.10'),
                            ('Animal Bikes', 'Minecraft', '1.8'),
                            ('Animal Bikes', 'Minecraft', '1.8.8'),
                            ('Animal Bikes', 'Minecraft', '1.8.9'),
                            ('Animal Bikes', 'Minecraft', '1.9'),
                            ('Animal Bikes', 'Minecraft', '1.10.2'),
                            ('Animal Bikes', 'Minecraft', '1.11.2'),
                            ('Animal Bikes', 'Minecraft', '1.12.2'),
                            ('Animal Bikes', 'Minecraft', '1.13.2'),
                            ('Animal Bikes', 'Minecraft', '1.14.4'),
                            ('Animal Bikes', 'Minecraft', '1.16.5');
INSERT INTO mod_version VALUES
                            ('Applied Energistics 2', 'Minecraft', '1.7.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.7.10'),
                            ('Applied Energistics 2', 'Minecraft', '1.10.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.12'),
                            ('Applied Energistics 2', 'Minecraft', '1.12.1'),
                            ('Applied Energistics 2', 'Minecraft', '1.12.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.15.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.16.1'),
                            ('Applied Energistics 2', 'Minecraft', '1.16.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.16.3'),
                            ('Applied Energistics 2', 'Minecraft', '1.16.4'),
                            ('Applied Energistics 2', 'Minecraft', '1.16.5'),
                            ('Applied Energistics 2', 'Minecraft', '1.17.1'),
                            ('Applied Energistics 2', 'Minecraft', '1.18'),
                            ('Applied Energistics 2', 'Minecraft', '1.18.1'),
                            ('Applied Energistics 2', 'Minecraft', '1.18.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.19'),
                            ('Applied Energistics 2', 'Minecraft', '1.19.1'),
                            ('Applied Energistics 2', 'Minecraft', '1.19.2'),
                            ('Applied Energistics 2', 'Minecraft', '1.19.3'),
                            ('Applied Energistics 2', 'Minecraft', '1.20.1');



-- Table mod_pack_version
INSERT INTO mod_pack_version VALUES
                                 ('1.7.10', 'Minecraft', 'Test'),
                                 ('1.10.2', 'Minecraft', 'Test'),
                                 ('1.11.2', 'Minecraft', 'Test'),
                                 ('1.12.2', 'Minecraft', 'Test'),
                                 ('1.16.5', 'Minecraft', 'Test');

-- Table mod_mod_pack
INSERT INTO mod_mod_pack VALUES
                             ('Forge', 'Minecraft', 'Minecraft', 'Test'),
                             ('LibVulpes', 'Minecraft', 'Minecraft', 'Test'),
                             ('Advanced Rocketry', 'Minecraft', 'Minecraft', 'Test');

-- Table mod_mod_collection
INSERT INTO mod_mod_collection VALUES
                                   ('Forge', 'Minecraft', 'Test', 'LeVraiGG'),
                                   ('LibVulpes', 'Minecraft', 'Test', 'LeVraiGG'),
                                   ('Advanced Rocketry', 'Minecraft', 'Test', 'LeVraiGG');

-- Table mod_impact
INSERT INTO mod_impact VALUES
    ('Forge', 'Minecraft', 'Library (API)');
INSERT INTO mod_impact VALUES
    ('LibVulpes', 'Minecraft', 'Library (API)');
INSERT INTO mod_impact VALUES
                           ('Advanced Rocketry', 'Minecraft', 'Car'),
                           ('Advanced Rocketry', 'Minecraft', 'Vehicle'),
                           ('Advanced Rocketry', 'Minecraft', 'Dimension'),
                           ('Advanced Rocketry', 'Minecraft', 'Biome');
INSERT INTO mod_impact VALUES
                           ('Advent of Ascension', 'Minecraft', 'Adventure'),
                           ('Advent of Ascension', 'Minecraft', 'RPG'),
                           ('Advent of Ascension', 'Minecraft', 'Decoration'),
                           ('Advent of Ascension', 'Minecraft', 'Furniture'),
                           ('Advent of Ascension', 'Minecraft', 'Dimension'),
                           ('Advent of Ascension', 'Minecraft', 'Biome'),
                           ('Advent of Ascension', 'Minecraft', 'Magic'),
                           ('Advent of Ascension', 'Minecraft', 'Monster'),
                           ('Advent of Ascension', 'Minecraft', 'Boss'),
                           ('Advent of Ascension', 'Minecraft', 'Animal'),
                           ('Advent of Ascension', 'Minecraft', 'Pet'),
                           ('Advent of Ascension', 'Minecraft', 'Armor'),
                           ('Advent of Ascension', 'Minecraft', 'Weapon'),
                           ('Advent of Ascension', 'Minecraft', 'Food'),
                           ('Advent of Ascension', 'Minecraft', 'Sword'),
                           ('Advent of Ascension', 'Minecraft', 'Blade'),
                           ('Advent of Ascension', 'Minecraft', 'Minigame'),
                           ('Advent of Ascension', 'Minecraft', 'Tool'),
                           ('Advent of Ascension', 'Minecraft', 'Utility');
INSERT INTO mod_impact VALUES
                           ('AdventureCraft', 'Minecraft', 'Mob'),
                           ('AdventureCraft', 'Minecraft', 'Creature');
INSERT INTO mod_impact VALUES
    ('Gilded Games Util', 'Minecraft', 'Library (API)');
INSERT INTO mod_impact VALUES
    ('Orbis API', 'Minecraft', 'Library (API)');
INSERT INTO mod_impact VALUES
                           ('Aether 2', 'Minecraft', 'Boss'),
                           ('Aether 2', 'Minecraft', 'Building'),
                           ('Aether 2', 'Minecraft', 'Structure'),
                           ('Aether 2', 'Minecraft', 'Dimension'),
                           ('Aether 2', 'Minecraft', 'Biome'),
                           ('Aether 2', 'Minecraft', 'Monster');
INSERT INTO mod_impact VALUES
                           ('Animal Bikes', 'Minecraft', 'Animal'),
                           ('Animal Bikes', 'Minecraft', 'Pet'),
                           ('Animal Bikes', 'Minecraft', 'Car'),
                           ('Animal Bikes', 'Minecraft', 'Vehicle');
INSERT INTO mod_impact VALUES
                           ('Applied Energistics 2', 'Minecraft', 'Tool'),
                           ('Applied Energistics 2', 'Minecraft', 'Energy'),
                           ('Applied Energistics 2', 'Minecraft', 'Item Transport'),
                           ('Applied Energistics 2', 'Minecraft', 'Ore'),
                           ('Applied Energistics 2', 'Minecraft', 'Technology'),
                           ('Applied Energistics 2', 'Minecraft', 'Machine'),
                           ('Applied Energistics 2', 'Minecraft', 'Automation'),
                           ('Applied Energistics 2', 'Minecraft', 'Auto-Crafting'),
                           ('Applied Energistics 2', 'Minecraft', 'Storage');


-- Table mod_pack_impact
INSERT INTO mod_pack_impact VALUES
                                ('Car', 'Minecraft', 'Test'),
                                ('Vehicle', 'Minecraft', 'Test'),
                                ('Dimension', 'Minecraft', 'Test'),
                                ('Biome', 'Minecraft', 'Test');

-- Table mod_moder
INSERT INTO mod_moder VALUES
    ('Inconnu', 'Minecraft', 'Forge');
INSERT INTO mod_moder VALUES
    ('zmaster587', 'Minecraft', 'LibVulpes');
INSERT INTO mod_moder VALUES
    ('zmaster587', 'Minecraft', 'Advanced Rocketry');
INSERT INTO mod_moder VALUES
    ('Xolova', 'Minecraft', 'Advent of Ascension');
INSERT INTO mod_moder VALUES
    ('Inconnu', 'Minecraft', 'AdventureCraft');
INSERT INTO mod_moder VALUES
    ('TheAetherTeam', 'Minecraft', 'Gilded Games Util');
INSERT INTO mod_moder VALUES
    ('TheAetherTeam', 'Minecraft', 'Orbis API');
INSERT INTO mod_moder VALUES
    ('TheAetherTeam', 'Minecraft', 'Aether 2');
INSERT INTO mod_moder VALUES
    ('Noppes', 'Minecraft', 'Animal Bikes');
INSERT INTO mod_moder VALUES
                          ('AlgorithmX2', 'Minecraft', 'Applied Energistics 2'),
                          ('thatsIch', 'Minecraft', 'Applied Energistics 2'),
                          ('FireBall1725', 'Minecraft', 'Applied Energistics 2');

-- Table mod_pack_moder
INSERT INTO mod_pack_moder VALUES
    ('LeVraiGG', 'Minecraft', 'Test');

-- Table mod_dependence
INSERT INTO mod_dependence VALUES
    ('LibVulpes', 'Minecraft', 'Forge', 'Minecraft');
INSERT INTO mod_dependence VALUES
                               ('Advanced Rocketry', 'Minecraft', 'Forge', 'Minecraft'),
                               ('Advanced Rocketry', 'Minecraft', 'LibVulpes', 'Minecraft');
INSERT INTO mod_dependence VALUES
    ('Advent of Ascension', 'Minecraft', 'Forge', 'Minecraft');
INSERT INTO mod_dependence VALUES
    ('Orbis API', 'Minecraft', 'Forge', 'Minecraft');
INSERT INTO mod_dependence VALUES
                               ('Aether 2', 'Minecraft', 'Forge', 'Minecraft'),
                               ('Aether 2', 'Minecraft', 'Gilded Games Util', 'Minecraft'),
                               ('Aether 2', 'Minecraft', 'Orbis API', 'Minecraft');
INSERT INTO mod_dependence VALUES
    ('Animal Bikes', 'Minecraft', 'Forge', 'Minecraft');
INSERT INTO mod_dependence VALUES
    ('Applied Energistics 2', 'Minecraft', 'Forge', 'Minecraft');

COMMIT;