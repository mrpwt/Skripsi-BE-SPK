package com.skripsi.spk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Aktifkan CORS di sini
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Disable CSRF (Wajib untuk API non-browser based session)
                .csrf(csrf -> csrf.disable())

                // 3. Izin Akses (Sementara permitAll dulu sesuai kode lama Anda)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    // 4. Bean Konfigurasi CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Izin Origin (URL Frontend Angular Anda)
        // Pastikan tidak ada typo di port 4200
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://spk-ti-ump.netlify.app/"));

        // Izin Method
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Izin Header (Termasuk Authorization untuk Token nanti)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Izinkan credentials (cookies/auth headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Terapkan konfigurasi di atas untuk semua path (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}