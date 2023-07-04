package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import com.github.vcvitaly.learnwordsusingphrases.service.ImageGenerator;
import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * ImageGeneratorImpl.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageGeneratorImpl implements ImageGenerator {

    @Override
    @SneakyThrows
    public File generateImage(String html) {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.loadHtml(html);
        imageGenerator.saveAsImage("img.png");

        return null;
    }
}
