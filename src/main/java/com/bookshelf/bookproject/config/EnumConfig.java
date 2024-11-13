package com.bookshelf.bookproject.config;

import com.bookshelf.bookproject.enums.EmailAddressType;
import com.bookshelf.bookproject.enums.EnumMapper;
import com.bookshelf.bookproject.enums.LocalNumberType;
import com.bookshelf.bookproject.enums.PhonePrefixType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.bookshelf.bookproject.enums.EnumKeys.*;

@Configuration
public class EnumConfig {
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put(PHONE_PREFIX_TYPE, PhonePrefixType.class);
        enumMapper.put(EMAIL_ADDRESS_TYPE, EmailAddressType.class);
        enumMapper.put(LOCAL_NUMBER_TYPE, LocalNumberType.class);

        return enumMapper;
    }
}
