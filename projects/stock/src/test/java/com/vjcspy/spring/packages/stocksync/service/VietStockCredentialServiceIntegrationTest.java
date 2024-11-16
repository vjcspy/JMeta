/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service;

import static org.junit.jupiter.api.Assertions.*;

import com.vjcspy.spring.application.Application;
import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class VietStockCredentialServiceIntegrationTest {
    @Autowired
    private VietStockCredentialService credentialService;

    @Test
    void testRetrieveCredentialsIntegration() {
        VietStockCredential credentials = credentialService.retrieveCredentials();

        // Kiểm tra rằng credential trả về không null và chứa các thông tin cơ bản
        assertNotNull(credentials);
        assertNotNull(credentials.getSid(), "SID không được null");
        assertNotNull(credentials.getRvt(), "RVT không được null");
        assertNotNull(credentials.getVtsUsrLg(), "vts_usr_lg không được null");
        assertNotNull(credentials.getUsrTk(), "usr_tk không được null");
        assertNotNull(credentials.getCsrf(), "CSRF không được null");

        System.out.println("SID: " + credentials.getSid());
        System.out.println("RVT: " + credentials.getRvt());
        System.out.println("vts_usr_lg: " + credentials.getVtsUsrLg());
        System.out.println("usr_tk: " + credentials.getUsrTk());
        System.out.println("CSRF: " + credentials.getCsrf());
    }
}
