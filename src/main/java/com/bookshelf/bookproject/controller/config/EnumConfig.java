package com.bookshelf.bookproject.controller.config;

import com.bookshelf.bookproject.controller.enums.EmailAddressType;
import com.bookshelf.bookproject.controller.enums.EnumMapper;
import com.bookshelf.bookproject.controller.enums.PhonePrefixType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumConfig {
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("phonePrefixType", PhonePrefixType.class);
        enumMapper.put("emailAddressType", EmailAddressType.class);

        return enumMapper;
    }
}
