package com.github.vcvitaly.learnwordsusingphrases.service;

import com.github.vcvitaly.learnwordsusingphrases.ITTemplate;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.FreeDictionaryApiService;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.MarkdownDefinitionFormattingService;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.OxfordApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DefinitionFacadeServiceCacheTest.
 *
 * @author Vitalii Chura
 */
class DefinitionFacadeServiceCacheTest extends ITTemplate {

    @MockBean
    private FreeDictionaryApiService freeDictionaryApiService;

    @MockBean
    private OxfordApiService oxfordApiService;

    @MockBean
    private MarkdownDefinitionFormattingService definitionFormattingService;

    @Autowired
    private DefinitionFacadeService definitionFacadeService;

    @Test
    void definitionApiServiceIsNotCalledWhenTheSameRequestIsExecutedTheSecondTime() {
        // Arrange
        final var word = "word";
        final var definitionDtos = singletonList(new DefinitionDto());
        when(freeDictionaryApiService.getDefinitions(word)).thenReturn(definitionDtos);
        when(oxfordApiService.getDefinitions(word)).thenReturn(definitionDtos);
        when(definitionFormattingService.getDefinitionsAsString(definitionDtos, word)).thenReturn("definition");

        // Act
        definitionFacadeService.getDefinitionsAsString(word);
        definitionFacadeService.getDefinitionsAsString(word);

        // Assert
        verify(definitionFormattingService).getDefinitionsAsString(definitionDtos, word);
    }
}
