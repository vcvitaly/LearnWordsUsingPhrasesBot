package com.github.vcvitaly.learnwordsusingphrases.enumeration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DefinitionApiServiceTypeTest.
 *
 * @author Vitalii Chura
 */
class DefinitionApiServiceTypeTest {

    @ParameterizedTest
    @EnumSource(DefinitionApiServiceType.class)
    void typeIsReturnedByBeanName(DefinitionApiServiceType type) {
        assertThat(DefinitionApiServiceType.getByBeanName(type.getMatchingServiceClassName()))
                .isEqualTo(type);
    }
}
