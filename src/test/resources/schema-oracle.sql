
CREATE TABLE ALL_SEQUENCES
(
    SEQUENCE_OWNER VARCHAR2(30) NOT NULL,
    SEQUENCE_NAME  VARCHAR2(30) NOT NULL,
    MIN_VALUE      NUMBER,
    MAX_VALUE      NUMBER,
    INCREMENT_BY   NUMBER       NOT NULL,
    CYCLE_FLAG     VARCHAR2(1),
    ORDER_FLAG     VARCHAR2(1),
    CACHE_SIZE     NUMBER       NOT NULL,
    LAST_NUMBER    NUMBER       NOT NULL
);

CREATE TABLE ALL_SYNONYMS
(
    OWNER        VARCHAR2(30),
    SYNONYM_NAME VARCHAR2(30),
    TABLE_OWNER  VARCHAR2(30),
    TABLE_NAME   VARCHAR2(30),
    DB_LINK      VARCHAR2(128)
);

CREATE TABLE KS_STONAD_20
(
    K01_PERSONKEY      NUMBER(15, 0)                               NOT NULL,
    K20_IVERFOM_SEQ    VARCHAR2(6)                                 NOT NULL,
    K20_VIRKFOM_SEQ    VARCHAR2(6)                                 NOT NULL,
    K20_GRUPPE         CHAR(2),          -- NOT NULL,
    K20_BRUKERID       VARCHAR2(7),      -- NOT NULL,
    K20_TKNR           VARCHAR2(4),      -- NOT NULL,
    K20_REG_DATO       NUMBER(8, 0),     -- NOT NULL,
    K20_SOK_DATO       NUMBER(8, 0),     -- NOT NULL,
    K20_BLOKK          CHAR(1),          -- NOT NULL,
    K20_SAK_NR         CHAR(2),          -- NOT NULL,
    K20_TEKSTKODE      CHAR(2),          -- NOT NULL,
    K20_TOT_ANT_BARN   CHAR(2),          -- NOT NULL,
    K20_ANT_KS_BARN    CHAR(2),          -- NOT NULL,
    K20_EBET_FOM       VARCHAR2(6),      -- NOT NULL,
    K20_EBET_TOM       VARCHAR2(6),      -- NOT NULL,
    K20_OPPHOERT_IVER  VARCHAR2(6),      -- NOT NULL,
    K20_OPPHOERT_VFOM  VARCHAR2(6),      -- NOT NULL,
    K20_OPPHORSGRUNN   CHAR(1),          -- NOT NULL,
    K20_OMREGN         CHAR(1),          -- NOT NULL,
    K20_EOS            CHAR(1),          -- NOT NULL,
    K20_ADOPTIV_SAK    CHAR(1),          -- NOT NULL,
    K20_ANT_ADOP_BARN  CHAR(1),          -- NOT NULL,
    K20_OPPHOR_ADOPSAK VARCHAR2(6),      -- NOT NULL,
    K20_STATUS_X       CHAR(1),          -- NOT NULL,
    TK_NR              VARCHAR2(4), -- NOT NULL,
    F_NR               VARCHAR2(11)                           NOT NULL,
    OPPRETTET          TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    ENDRET_I_KILDE     TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    KILDE_IS           VARCHAR2(12)      DEFAULT ' '               NOT NULL,
    REGION             CHAR(1)           DEFAULT ' '               NOT NULL,
    ID_STND            NUMBER(15, 0)     DEFAULT NOT NULL,
    OPPDATERT          TIMESTAMP(6)      DEFAULT current_timestamp,
    DB_SPLITT          CHAR(2)           DEFAULT 'KS'
);

CREATE TABLE KS_BARN_10
(
    K01_PERSONKEY    NUMBER(15, 0)                               NOT NULL,
    K10_BARN_FNR     NUMBER(11, 0),     -- NOT NULL,
    K10_BA_IVER_SEQ  VARCHAR2(6)                                 NOT NULL,
    K10_BA_VFOM_SEQ  VARCHAR2(6)                                 NOT NULL,
    K10_BA_TOM_SEQ   VARCHAR2(6),       -- NOT NULL,
    K10_TIMER_PR_UKE CHAR(2),           -- NOT NULL,
    K10_STOTTETYPE   CHAR(2),           -- NOT NULL,
    TK_NR            VARCHAR2(4),  -- NOT NULL,
    F_NR             VARCHAR2(11), -- NOT NULL,
    OPPRETTET        TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    ENDRET_I_KILDE   TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    KILDE_IS         VARCHAR2(12) DEFAULT ' '               NOT NULL,
    REGION           CHAR(1)           DEFAULT ' '               NOT NULL,
    ID_BARN          NUMBER(15,0)            DEFAULT NOT NULL,
    OPPDATERT        TIMESTAMP(6)      DEFAULT current_timestamp,
    DB_SPLITT        CHAR(2)           DEFAULT 'KS'
);

CREATE TABLE KS_UTBETALING_30
(
    K01_PERSONKEY           NUMBER(15, 0)                               NOT NULL,
    K30_START_UTBET_MND_SEQ VARCHAR2(6)                                 NOT NULL,
    K30_VFOM_SEQ            VARCHAR2(6)                                 NOT NULL,
    K30_KONTONR             VARCHAR2(8),       -- NOT NULL,
    K30_UTBET_TYPE          CHAR(1),           -- NOT NULL,
    K30_GRUPPE              CHAR(2),           -- NOT NULL,
    K30_BRUKERID            VARCHAR2(7),       -- NOT NULL,
    K30_UTBET_FOM           VARCHAR2(6),       -- NOT NULL,
    K30_UTBET_TOM           VARCHAR2(6),       -- NOT NULL,
    K30_UTBETALT            CHAR(1),           -- NOT NULL,
    K30_BELOP               NUMBER(7, 0),      -- NOT NULL,
    K30_UTBET_DATO          NUMBER(8, 0),      -- NOT NULL,
    TK_NR                   VARCHAR2(4),  -- NOT NULL,
    F_NR                    VARCHAR2(11), -- NOT NULL,
    OPPRETTET               TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    ENDRET_I_KILDE          TIMESTAMP(6)      DEFAULT current_timestamp NOT NULL,
    KILDE_IS                VARCHAR2(12) DEFAULT ' '               NOT NULL,
    REGION                  CHAR(1)           DEFAULT ' '               NOT NULL,
    ID_UTBET                NUMBER(15,0)            DEFAULT NOT NULL,
    OPPDATERT               TIMESTAMP(6)      DEFAULT current_timestamp,
    DB_SPLITT               CHAR(2)           DEFAULT 'KS'
);