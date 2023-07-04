package com.github.vcvitaly.learnwordsusingphrases.service.impl;

import org.junit.jupiter.api.Test;

/**
 * ImageGeneratorImplTest.
 *
 * @author Vitalii Chura
 */
class ImageGeneratorImplTest {

    private final ImageGeneratorImpl imageGenerator = new ImageGeneratorImpl();

    @Test
    void imageIsGenerated() {
        final var html = """
                <!doctype html>
                <html class="no-js" lang="">
                <head>
                  <meta charset="utf-8">
                  <title>My_word_template</title>
                  <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
                  <style>
                    body {
                      font-family: 'Quicksand', sans-serif;
                      margin: 30px;
                    }
                    .center {
                      margin-left: auto;
                      margin-right: auto;
                    }
                    .definition-bg {
                      background: bisque;
                    }
                    .example-bg {
                      background: azure;
                    }
                    /*table definition*/
                    table {
                      border-collapse: separate;
                      border-spacing: 0;
                      min-width: 350px;
                    }
                    table tr th,
                    table tr td {
                      border-right: 1px solid #bbb;
                      border-bottom: 1px solid #bbb;
                      padding: 5px;
                    }
                    table tr th:first-child,
                    table tr td:first-child {
                      border-left: 1px solid #bbb;
                    }
                    table tr th {
                      background: #eee;
                      border-top: 1px solid #bbb;
                      text-align: left;
                    }
                                
                    /* top-left border-radius */
                    table tr:first-child th:first-child {
                      border-top-left-radius: 6px;
                    }
                                
                    /* top-right border-radius */
                    table tr:first-child th:last-child {
                      border-top-right-radius: 6px;
                    }
                                
                    /* bottom-left border-radius */
                    table tr:last-child td:first-child {
                      border-bottom-left-radius: 6px;
                    }
                                
                    /* bottom-right border-radius */
                    table tr:last-child td:last-child {
                      border-bottom-right-radius: 6px;
                    }
                  </style>
                </head>
                <body>
                <div>
                  <table class="center">
                    <tr><th colspan="2">noun</th></tr>
                    <tr class="definition-bg"><td>Definition</td><td>an utterance of ‘hello’; a greeting</td></tr>
                    <tr class="example-bg"><td>Example</td><td>she was getting polite nods and hellos from people</td></tr>
                  </table>
                  <br>
                  <table class="center">
                    <tr><th colspan="2">verb</th></tr>
                    <tr class="definition-bg"><td>Definition</td><td>say or shout ‘hello’</td></tr>
                    <tr class="example-bg"><td>Example</td><td>I pressed the phone button and helloed</td></tr>
                    <tr class="definition-bg"><td>Definition</td><td>some other definition of ‘hello’</td></tr>
                    <tr class="example-bg"><td>Example</td><td>Some other example of hello as verb</td></tr>
                  </table>
                </div>
                </body>
                </html>
                """;

        imageGenerator.generateImage(html);
    }
}