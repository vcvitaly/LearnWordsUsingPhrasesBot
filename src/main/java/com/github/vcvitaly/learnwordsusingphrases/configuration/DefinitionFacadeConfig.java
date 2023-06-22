package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.enumeration.DefinitionApiServiceType;
import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static java.util.function.Predicate.not;

/**
 * DefinitionFacadeConfig.
 *
 * @author Vitalii Chura
 */
@Configuration
@Data
@RequiredArgsConstructor
public class DefinitionFacadeConfig {

    private final Map<String, DefinitionApiService> definitionApiServicesByClassNames;

    private final DefinitionServiceOrderProperties definitionServiceOrderProperties;

    @Bean
    public DefinitionServiceList definitionServiceList() {
        if (definitionServiceOrderProperties.services().size() != definitionApiServicesByClassNames.size()) {
            throw constructIllegalStateException(definitionApiServicesByClassNames, definitionServiceOrderProperties);
        }
        final var definitionApiServices = definitionServiceOrderProperties.services().stream()
                .map(svcType -> definitionApiServicesByClassNames.get(svcType.getMatchingServiceClassName()))
                .toList();
        return new DefinitionServiceList(definitionApiServices);
    }

    private IllegalStateException constructIllegalStateException(
            Map<String, DefinitionApiService> definitionApiServices,
            DefinitionServiceOrderProperties definitionServiceOrderProperties
    ) {
        final var missingServiceOrderDefinitions = definitionApiServices.keySet().stream()
                .map(DefinitionApiServiceType::getByBeanName)
                .filter(not(definitionServiceOrderProperties.services()::contains))
                .toList();
        return new IllegalStateException(
                String.format(
                        "Definition service order property is missing %s definition",
                        missingServiceOrderDefinitions
                )
        );
    }
}
