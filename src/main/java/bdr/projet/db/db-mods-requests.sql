-- Simple request for each entity
SELECT *
FROM genre;
SELECT *
FROM game;
SELECT *
FROM version;
SELECT *
FROM mod;
SELECT *
FROM impact;
SELECT *
FROM moder;
SELECT *
FROM _user;
SELECT *
FROM mod_collection;
SELECT *
FROM mod_pack;
SELECT *
FROM note;
SELECT *
FROM comment;
SELECT *
FROM mod_collection_logger;
SELECT *
FROM LeVraiGG_view;

-- More complex request for entity
-- Number of games for each genre. Ordered by number of games max to min and by name a to z.
SELECT name, count(fk_genre) nbGames
FROM genre
LEFT JOIN genre_game gg
    ON genre.name = gg.fk_genre
GROUP BY name
ORDER BY nbGames DESC, name;

-- Number of genre, versions, and mods for each game. Ordered by  number of mods max to min and by name a to z.
SELECT game.*,
       count(gg) nbGenre,
       (SELECT count(gv)
           FROM game
           LEFT JOIN game_version gv
               ON game.name = gv.fk_game) nbVersion,
       (SELECT count(m)
           FROM game
           LEFT JOIN mod m
               ON game.name = m.fk_game) nbMods
FROM game
LEFT JOIN genre_game gg
ON game.name = gg.fk_game
GROUP BY game.name, game.logo, game.description, modfolder
ORDER BY nbMods DESC, game.name;

-- Number of games and mods for each version. Ordered by number of games max to min, number of mods max to min and version max to min (TODO but 1.10 comes before 1.2 sadly).
SELECT version.*,
    count(gv) nbGames,
    count(mv) nbMods
FROM version
LEFT JOIN game_version gv
    ON version.name = gv.fk_version
LEFT JOIN mod_version mv
    ON version.name = mv.fk_version
GROUP BY name
ORDER BY nbGames DESC, nbMods DESC, version.name DESC;

-- List mods with its dependencies mods, its screenshots, and its version and number of version. Ordered by mod name a to z.
SELECT mod.*,
       (SELECT string_agg(s.img_path, ';')
        FROM screenshot s
        WHERE mod.name = s.fk_mod_name
            AND mod.fk_game = s.fk_mod_game_name) screenshots,
       (SELECT string_agg(md.fk_dependence_name, ';')
        FROM mod_dependence md
        WHERE md.fk_mod_name = mod.name
            AND mod.fk_game = md.fk_mod_game_name) dependences,
       count(mv) nbVersion,
       string_agg(mv.fk_version, ';') versions
FROM mod
INNER JOIN game g
    ON g.name = mod.fk_game
INNER JOIN mod_version mv
    ON mv.fk_mod_name = mod.name
        AND mv.fk_mod_game_name = g.name
GROUP BY mod.name, fk_game, mod.logo, mod.description, downloadlink, nbdownload
ORDER BY name;

-- List number of mods for each impact. Ordered by number of mods max to min and name a to z.
SELECT i.*,
       count(mi) nbMods
FROM impact i
LEFT JOIN public.mod_impact mi
    ON i.name = mi.fk_impact
GROUP BY name
ORDER BY nbMods DESC, name;

-- List moders with their user (if not having one, null), their number of mods, and number of mod packs.
SELECT pseudo,
       u.password,
       (SELECT count(*)
           FROM mod_moder
           WHERE moder.pseudo = fk_moder) nbMods,
        (SELECT count(*)
           FROM mod_pack_moder
           WHERE moder.pseudo = fk_moder) nbModPacks
FROM moder
LEFT JOIN _user u
    ON u.name = moder.fk_user;

-- List mod collections of each user
SELECT u.name pseudo,
       mc.fk_game game,
       mc.name mod_collection,
       mc.logo,
       mc.relative_path_to_folder folder,
       mc.description
FROM _user u
LEFT JOIN mod_collection mc on u.name = mc.fk_user;

-- List mod collection with theirs mods, the number of modification done to it since added and the last modification date. Ordered by name a to z.
SELECT mc.*,
       get_mods_from_mod_collection(mc.name, mc.fk_user) mods,
       count(mcl) nbModification,
       max(mcl.version) last_modification_date
FROM mod_collection mc
LEFT JOIN mod_collection_logger mcl
    ON mc.name = mcl.fk_mod_collection_name
        AND mc.fk_user = mcl.fk_mod_collection_user_name
GROUP BY name, fk_user, mc.relative_path_to_folder, mc.logo, mc.description, mc.fk_game
ORDER BY name;

-- Mod packs listed with theirs mods and the moder who proposed it. Ordered by number of downloads max to min, number of mods max to min and by moder a to z.
SELECT mp.name,
       mp.fk_game game_name,
       mp.logo,
       mp.description,
       mp.downloadlink,
       mp.nbdownload,
       mpm.fk_moder proposed_by,
       string_agg(fk_mod_name, ';') mods
FROM mod_pack mp
LEFT JOIN mod_mod_pack mmp
    ON mmp.fk_mod_pack_name = mp.name
        AND mmp.fk_mod_pack_game_name = mp.fk_game
LEFT JOIN mod_pack_moder mpm
    ON mpm.fk_mod_pack_name = mp.name
        AND mpm.fk_mod_pack_game_name = mp.fk_game
GROUP BY mp.name, mp.fk_game, mp.name, mp.logo, mp.description, mp.downloadlink, mp.nbdownload, mpm.fk_moder
ORDER BY nbdownload DESC, count(fk_mod_name) DESC, proposed_by;

-- "Filter" request (this kind of request who will use a parameter to get information about one entity)
-- Select games with this gender and this version (Ordered by name).
SELECT game.*
FROM game
INNER JOIN game_version gv
    ON game.name = gv.fk_game
INNER JOIN genre_game gg
    ON game.name = gg.fk_game
WHERE gv.fk_version = :version
    AND gg.fk_genre = :genre
ORDER BY game.name;


-- Select mods of this game (Ordered by mod name)
SELECT m.*
FROM mod m
WHERE m.fk_game = :game
ORDER BY m.name;

-- Select mods of this moder
SELECT m.*
FROM mod m
INNER JOIN mod_moder mm
    ON mm.fk_mod_game_name = m.fk_game
        AND mm.fk_mod_name = m.name
WHERE mm.fk_moder = :modder;

-- Select mods who isn't in any mod pack and not in any mod collection (Ordered by name).
SELECT m.*
FROM mod m
LEFT JOIN mod_mod_pack mmp
    ON mmp.fk_mod_name = m.name
        AND mmp.fk_mod_game_name = m.fk_game
LEFT JOIN mod_mod_collection mmc
    ON mmc.fk_mod_name = m.name
        AND mmc.fk_mod_game_name = m.fk_game
WHERE mmp.fk_mod_pack_name IS NULL
    AND mmc.fk_mod_collection_name IS NULL
ORDER BY m.name;

-- Select "good" mods (note above 3) and "unknow" mods (note = null). Ordered by notes max to min, WITH NULL VALUES LAST (then oredered by number of downloads and number of comments).
SELECT m.*,
       n.value note,
       count(c) nbComments
FROM mod m
LEFT JOIN note n
    ON n.fk_mod_game_name = m.fk_game
        AND n.fk_mod_name = m.name
LEFT JOIN comment c
    ON c.fk_mod_game_name = m.fk_game
        AND c.fk_mod_name = m.name
WHERE n.value IS NULL
    OR n.value > 3
GROUP BY m.name, m.fk_game, m.logo, m.description, m.downloadlink, m.nbdownload, note
ORDER BY note DESC NULLS LAST, m.nbdownload DESC, nbComments DESC;

-- Select a user and his infos (_user content, mod collection of this user, and is he a moder?).
SELECT u.*,
       count(m) > 0 isModer,
       mc.fk_game game_name,
       mc.name,
       mc.logo,
       mc.description,
       mc.relative_path_to_folder
FROM _user u
LEFT JOIN mod_collection mc
    ON mc.fk_user = u.name
LEFT JOIN moder m
    ON m.fk_user = u.name
WHERE u.name = :pseudo
GROUP BY u.name, u.password, u.isadmin, game_name, mc.name, mc.logo, mc.description, mc.relative_path_to_folder;

-- Very complex request (No we won't use it, it just to show how strong we are)
-- just some aggregate values about the note of users
SELECT u.name,
       count(n) nbNote,
       avg(n.value) moyenne,
       mode() WITHIN GROUP (ORDER BY n.value),
       min(n.value),
       max(n.value)
FROM _user u
INNER JOIN note n
    ON n.fk_user = u.name
GROUP BY u.name
ORDER BY nbNote, moyenne, u.name;
-- Complexity it with some filter to get only the extreme people
-- (I don't think we can identify terrorist based on the notes they give for some mods on an app like ours but we never know)
WITH user_notes_stats AS (
    SELECT u.name,
       count(n) nbNote,
       avg(n.value) moyenne,
       mode() WITHIN GROUP (ORDER BY n.value) mode,
       min(n.value) min,
       max(n.value) max
    FROM _user u
    INNER JOIN note n
        ON n.fk_user = u.name
    GROUP BY u.name, n.value
    ORDER BY nbNote, moyenne, u.name)

(SELECT *
 FROM user_notes_stats
 WHERE moyenne < 2
    OR max < 3
    OR mode = 1) -- could use having but without 'with' but as we use this "stats" two time it's better like this
UNION
(SELECT *
 FROM user_notes_stats
 WHERE moyenne > 4
    OR max > 3
    OR mode = 6);

-- Games "played" by a user
SELECT DISTINCT u.name,
       g.name
FROM _user u
INNER JOIN mod_collection mc
    ON mc.fk_user = u.name
INNER JOIN game g
    ON mc.fk_game = g.name;

-- Uh, veryAnnoyingCustomer asked me to add a game proposition for each user depending of the top 3 reccuring genre of the games he play.
-- But this game proposition is limited to 3 games and must not be composed with a game he already, only "new" game.

WITH game_proposition(username, name, row_number) AS (
    SELECT
        u.name username,
        g.name AS name,
        row_number() OVER byUser row_number
    FROM _user u
    LEFT JOIN game g
        ON g.name NOT IN (
            SELECT -- SELECT the games the user play
                g.name
            FROM game g
            INNER JOIN mod_collection mc
                ON mc.fk_game = g.name
            WHERE mc.fk_user = u.name)

        WHERE g.name IN (
            SELECT fk_game
            FROM genre_game gg
            WHERE gg.fk_genre =ANY
                (SELECT  -- SELECT the 3 most recurring game genre for the user
                    _gg.fk_genre genre
                 FROM _user _u

                 INNER JOIN mod_collection _mc
                    ON _mc.fk_user = u.name
                 INNER JOIN game g -- the games played by the user
                    ON _mc.fk_game = g.name
                 INNER JOIN genre_game _gg -- the genres played by the user
                    ON _gg.fk_game = g.name

                 GROUP BY genre
                 ORDER BY count(_gg.fk_genre) DESC
                 LIMIT 3)
            GROUP BY gg.fk_game
            ORDER BY count(fk_game) DESC)

    WINDOW byUser AS (PARTITION BY u.name ORDER BY g.name)
    ORDER BY row_number
)

SELECT u.name username,
       (SELECT string_agg(name, ';') FROM game_proposition) gameProposition
FROM _user u
group by u.name
ORDER BY username;

-- Number of user for each game
SELECT game.name,
       count(mc) nbUser
FROM game
LEFT JOIN mod_collection mc
    ON mc.fk_game = game.name
GROUP BY game.name
ORDER BY nbUser DESC, game.name;

-- veryAnnoyingCustomer came back to ask me to do an affinity user-moder based on percentage (numberOfModOfThisModerTheUserUse/numberOfModsModerDid or 100% for the moder account with himself)
SELECT
    u.name AS username,
    m.pseudo AS moder,
    CASE
        WHEN m.pseudo = u.name THEN 100
        ELSE (
            SELECT -- Count number of mods, of this moder, the user use
                count(_mm) numberOfModsOfThisModerTheUserUse
            FROM _user _u
            LEFT JOIN mod_collection _mc
                ON _mc.fk_user = u.name
            INNER JOIN mod_mod_collection _mmc
                ON _mmc.fk_mod_collection_name = _mc.name
                    AND _mmc.fk_mod_collection_user_name = u.name
            INNER JOIN mod_moder _mm
                ON _mm.fk_mod_name = _mmc.fk_mod_name
                    AND _mm.fk_mod_game_name = _mmc.fk_mod_game_name
            WHERE _mm.fk_moder = m.pseudo
            GROUP BY _u.name)
            / (
            SELECT count(_mm) numberOfModsModerDid
            FROM moder _m
            LEFT JOIN mod_moder _mm
                ON _m.pseudo = _mm.fk_moder
            WHERE _m.pseudo = m.pseudo
            GROUP BY _m.pseudo)
            * 100
    END AS affinity --(numberOfModOfThisModerTheUserUse/numberOfModsModerDid) affinity
FROM _user u
CROSS JOIN moder m
ORDER BY username, affinity DESC, moder;

-- veryAnnoyingCustomer asked me a last thing, do a list of "unwanted user".
-- A unwanted user is a user who has at least one of his comments who has a word matching a "black" list.

SELECT u.name,
    (SELECT (upper(content) = content
        OR lower(content) LIKE '%kys%'
        OR lower(content) LIKE '%fuck%'
        OR lower(content) LIKE '%kill yourself%'
        OR lower(content) LIKE '%shit%'
        OR lower(content) LIKE '%banned_word%') -- here is just a list of example, the true list can be whatever we want to ban on chat
    FROM comment
    WHERE fk_user = u.name) is_unwanted,
    (SELECT count(*)
    FROM comment
    WHERE fk_user = u.name
        AND (upper(content) = content
        OR lower(content) LIKE '%kys%'
        OR lower(content) LIKE '%fuck%'
        OR lower(content) LIKE '%kill yourself%'
        OR lower(content) LIKE '%shit%'
        OR lower(content) LIKE '%banned_word%'))  numberOfUnwantedComment
FROM _user u;

-- All dependencies of a mod (recursive)
WITH RECURSIVE dependencies(dependency) AS (
        SELECT fk_dependence_name
        FROM mod_dependence d1
        WHERE fk_mod_game_name = :game
            AND fk_mod_name = :mod
    UNION
        SELECT fk_dependence_name
        FROM mod_dependence d2
        INNER JOIN dependencies d
            ON d2.fk_mod_game_name = :game
                AND fk_mod_name = d.dependency
)

SELECT *
FROM dependencies;