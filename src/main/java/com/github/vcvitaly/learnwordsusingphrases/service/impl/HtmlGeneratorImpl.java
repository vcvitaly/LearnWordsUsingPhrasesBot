package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.exception.HtmlGenerationException;
import com.github.vcvitaly.learnwordsusingphrases.service.HtmlGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HtmlGeneratorImpl.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HtmlGeneratorImpl implements HtmlGenerator {

    private static final String INDEX_FILE = "index.ftlh";

    private final Configuration cfg;

    @Override
    @SneakyThrows
    public String generateHtml(List<DefinitionDto> definitionDtos) {
        Map<String, Object> root = new HashMap<>();
        root.put("definitionDtos", definitionDtos);

        Template temp = cfg.getTemplate(INDEX_FILE);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Writer out = new OutputStreamWriter(baos)) {
            temp.process(root, out);
            LOG.debug("Generated the html output for {}", definitionDtos);
            return baos.toString();
        } catch (TemplateException | IOException e) {
            throw new HtmlGenerationException("Could not generate the html output", e);
        }
    }
}
