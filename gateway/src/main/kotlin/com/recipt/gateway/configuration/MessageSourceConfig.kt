package com.recipt.gateway.configuration

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.*

@Configuration
class MessageSourceConfig {
    @Bean
    fun messageSource(): MessageSource {
        Locale.setDefault(Locale.KOREA)

        return ReloadableResourceBundleMessageSource().apply {
            setDefaultEncoding("UTF-8")
            setBasenames("classpath:messages/message-error")
        }
    }
}