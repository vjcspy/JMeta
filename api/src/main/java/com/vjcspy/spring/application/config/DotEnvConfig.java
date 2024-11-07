/* (C) 2024 */
package com.vjcspy.spring.application.config;

import com.vjcspy.spring.base.config.Env;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

    @Bean
    public Env env() {
        String currentDir = System.getProperty("user.dir");

        // Lấy tên môi trường từ biến ENV (ví dụ: dev, prod, local)
        String env = System.getenv("ENV") != null ? System.getenv("ENV") : "local";

        // Danh sách các file .env theo thứ tự ưu tiên
        List<String> fileNames = new ArrayList<>();

        // Nếu ENV được đặt, thêm file .env.{ENV} vào danh sách đầu tiên
        if (env != null) {
            fileNames.add(".env." + env);
        }

        // Thêm file .env mặc định
        fileNames.add(".env");

        // Tạo Env với danh sách các file cần load theo thứ tự
        return new Env(fileNames, currentDir);
    }
}
