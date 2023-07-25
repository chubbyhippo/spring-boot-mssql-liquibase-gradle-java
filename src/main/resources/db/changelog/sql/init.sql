--changeset author:id1
CREATE COLUMN MASTER KEY [MyCMK]
    WITH
    (
    KEY_STORE_PROVIDER_NAME = N'MSSQL_JAVA_KEYSTORE',
    KEY_PATH = N'AlwaysEncryptedKey'
    );