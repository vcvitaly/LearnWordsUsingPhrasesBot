package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.ITTemplate;
import com.github.vcvitaly.learnwordsusingphrases.enumeration.DefinitionApiServiceType;
import com.github.vcvitaly.learnwordsusingphrases.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefinitionFacadeConfigTest.
 *
 * @author Vitalii Chura
 */
class DefinitionFacadeConfigTest extends ITTemplate {

    @Autowired
    private DefinitionServiceList definitionServiceList;

    @Autowired
    private DefinitionServiceOrderProperties orderProperties;

    @Test
    void correctNumberOfServicesIsAddedToTheList() {
        assertThat(definitionServiceList).hasSameSizeAs(orderProperties.services());
        final var actualBeanNames = definitionServiceList.stream()
                .map(svc -> StringUtils.classNameToLowerCamelCase(svc.getClass().getSimpleName()))
                .toList();
        final var expectedBeanNames = orderProperties.services().stream()
                .map(DefinitionApiServiceType::getMatchingServiceClassName)
                .toList();
        assertThat(actualBeanNames).containsExactlyElementsOf(expectedBeanNames);
    }
}
