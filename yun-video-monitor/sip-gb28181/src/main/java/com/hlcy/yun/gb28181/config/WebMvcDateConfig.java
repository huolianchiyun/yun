package com.hlcy.yun.gb28181.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class WebMvcDateConfig implements WebMvcConfigurer {
    private static final DateTimeFormatter yyyy_mm_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new String2LocalDateConverter());
        registry.addConverter(new String2LocalDateTimeConverter());
        registry.addConverter(new String2LocalTimeConverter());
        registry.addConverter(new String2DateConverter());
    }

    static class String2LocalDateConverter implements Converter<String, LocalDate> {
        @Override
        public LocalDate convert(String s) {
            return LocalDate.parse(s, yyyy_mm_dd);
        }
    }

    static class String2LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String s) {
            return LocalDateTime.parse(s, yyyy_MM_dd_HH_mm_ss);
        }
    }

    static class String2LocalTimeConverter implements Converter<String, LocalTime> {
        @Override
        public LocalTime convert(String s) {
            return LocalTime.parse(s, HH_mm_ss);
        }
    }

    static class String2DateConverter implements Converter<String, Date> {
        @SneakyThrows
        @Override
        public Date convert(String s) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(s);
        }
    }
}
