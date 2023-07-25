package com.example.demo;

import com.microsoft.sqlserver.jdbc.SQLServerColumnEncryptionJavaKeyStoreProvider;
import com.microsoft.sqlserver.jdbc.SQLServerColumnEncryptionKeyStoreProvider;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HexFormat;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    ApplicationRunner initDb(JdbcTemplate jdbcTemplate) throws SQLServerException, FileNotFoundException {
        String keyAlias = "AlwaysEncryptedKey";
        String columnEncryptionKey = "MyCEK";
        String columnMasterKeyName = "MyCMK";
        String algorithm = "RSA_OAEP";
        String keyStoreLocation = ResourceUtils.getFile("classpath:keystore.jks").getPath();
        char[] keyStoreSecret = "mypassword".toCharArray();

        SQLServerColumnEncryptionKeyStoreProvider storeProvider =
                new SQLServerColumnEncryptionJavaKeyStoreProvider(keyStoreLocation,
                        keyStoreSecret);

        byte[] encryptedCEK = getEncryptedCEK(storeProvider, keyAlias, algorithm);


        return args -> {
            String createCEKSQL = "CREATE COLUMN ENCRYPTION KEY "
                    + columnEncryptionKey
                    + " WITH VALUES ( "
                    + " COLUMN_MASTER_KEY = "
                    + columnMasterKeyName
                    + " , ALGORITHM =  '"
                    + algorithm
                    + "' , ENCRYPTED_VALUE =  0x"
                    + HexFormat.of().formatHex(encryptedCEK)
                    + " ) ";

            jdbcTemplate.execute(createCEKSQL);
        };
    }

    private static byte[] getEncryptedCEK(SQLServerColumnEncryptionKeyStoreProvider storeProvider, String keyAlias,
                                          String algorithm) throws SQLServerException {
        String plainTextKey = "You need to give your plain text";

        // plainTextKey has to be 32 bytes with current algorithm supported
        byte[] plainCEK = plainTextKey.getBytes();

        // This will give us encrypted column encryption key in bytes
        return storeProvider.encryptColumnEncryptionKey(keyAlias, algorithm, plainCEK);
    }


}
