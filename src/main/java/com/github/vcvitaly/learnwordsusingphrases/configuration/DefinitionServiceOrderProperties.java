package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.enumeration.DefinitionApiServiceType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.Delimiter;

import java.util.List;

/**
 * DefinitionServiceOrderProperties.
 *
 * @author Vitalii Chura
 */
@ConfigurationProperties("app.service-order")
public record DefinitionServiceOrderProperties(@Delimiter(";") List<DefinitionApiServiceType> services) {}
