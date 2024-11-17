package com.bookshelf.bookproject.config;

import com.bookshelf.bookproject.enums.EmailAddressType;
import com.bookshelf.bookproject.enums.EnumMapper;
import com.bookshelf.bookproject.enums.LocalNumberType;
import com.bookshelf.bookproject.enums.PhonePrefixType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.bookshelf.bookproject.enums.EnumKeys.*;

@Configuration
public class EnumConfig {
    @Bean
    public EnumMapper enumMapper() {
        return new EnumMapper(Map.of(
                PHONE_PREFIX_TYPE, PhonePrefixType.class,
                EMAIL_ADDRESS_TYPE, EmailAddressType.class,
                LOCAL_NUMBER_TYPE, LocalNumberType.class
        ));
    }
}
