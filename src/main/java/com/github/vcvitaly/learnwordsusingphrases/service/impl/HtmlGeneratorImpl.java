package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.github.vcvitaly.learnwordsusingphrases.dto.DefinitionDto;
import com.github.vcvitaly.learnwordsusingphrases.service.HtmlGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
public class HtmlGeneratorImpl implements HtmlGenerator {

    @Override
    @SneakyThrows
    public String generateHtml(List<DefinitionDto> definitionDtos) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(getFile("freemarker"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);


        Map<String, Object> root = new HashMap<>();
        root.put("definitionDtos", definitionDtos);

        Template temp = cfg.getTemplate("index.ftlh");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(baos);
        temp.process(root, out);

        String res = baos.toString();
        out.close();
        baos.close();
        return res;
    }

    private File getFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
