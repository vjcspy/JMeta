// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.application.config

import com.vjcspy.spring.base.config.Env
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DotEnvConfig {
    @Bean
    fun env(): Env {
        val currentDir = System.getProperty("user.dir")

        // Lấy tên môi trường từ biến ENV (ví dụ: dev, prod, local)
        val env = System.getenv("ENV") ?: "dev"

        // Danh sách các file .env theo thứ tự ưu tiên
        val fileNames = mutableListOf<String>()

        // Thêm file .env mặc định
        fileNames.add(".env")

        // Nếu ENV được đặt, thêm file .{ENV}.env vào danh sách đầu tiên
        fileNames.add(".$env.env")

        // Tạo Env với danh sách các file cần load theo thứ tự
        return Env(fileNames, currentDir)
    }
}
