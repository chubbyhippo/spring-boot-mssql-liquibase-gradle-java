--changeset author:id1
CREATE TABLE [dbo].[book]
(
    [id]    [int] IDENTITY (1,1),
    [title] [nvarchar]
        ENCRYPTED WITH (ENCRYPTION_TYPE = RANDOMIZED,
    ALGORITHM = 'AEAD_AES_256_CBC_HMAC_SHA_256',
    COLUMN_ENCRYPTION_KEY = MyCEK
) NOT NULL);