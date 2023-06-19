package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.client.FreeDictionaryApiClient;
import com.github.vcvitaly.learnwordsusingphrases.client.OxfordApiClient;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.FreeDictionaryApiService;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.OxfordApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static com.github.vcvitaly.learnwordsusingphrases.enumeration.DefinitionApiServiceType.OXFORD;
import static com.github.vcvitaly.learnwordsusingphrases.util.StringUtils.classNameToLowerCamelCase;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DefinitionFacadeConfigTest.
 *
 * @author Vitalii Chura
 */
@ExtendWith(MockitoExtension.class)
class DefinitionFacadeConfigFailureTest {

    @Mock
    private FreeDictionaryApiClient freeDictionaryApiClient;

    @Mock
    private OxfordApiClient oxfordApiClient;

    private final FreeDictionaryApiService freeDictionaryApiService =
            new FreeDictionaryApiService(freeDictionaryApiClient);

    private final OxfordApiService oxfordApiService =
            new OxfordApiService(new OxfordApiProperties(null, null), oxfordApiClient);

    private final DefinitionServiceOrderProperties definitionServiceOrderProperties =
            new DefinitionServiceOrderProperties(Collections.singletonList(OXFORD));

    @Test
    void anExceptionIsThrownUponUnfulfilledPropertiesList() {
        final var definitionFacadeConfig = new DefinitionFacadeConfig(
                Map.of(
                        classNameToLowerCamelCase(freeDictionaryApiService.getClass().getSimpleName()), freeDictionaryApiService,
                        classNameToLowerCamelCase(oxfordApiService.getClass().getSimpleName()), oxfordApiService
                ),
                definitionServiceOrderProperties
        );

        assertThatThrownBy(definitionFacadeConfig::definitionServiceList)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Definition service order property is missing [FREE_DICTIONARY] definition");
    }
}