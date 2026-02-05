package com.beatgridmedia.assignment.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "api")
class ApiConfig {
    var normalizeGenres: Boolean = true
}
