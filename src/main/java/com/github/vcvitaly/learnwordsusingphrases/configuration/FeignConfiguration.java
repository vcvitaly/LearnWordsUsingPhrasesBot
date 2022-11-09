package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.exception.DefinitionNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.vcvitaly.learnwordsusingphrases.util.IOUtil.fetchReaderData;

/**
 * FeignConfiguration.
 *
 * @author Vitalii Chura
 */
@Configuration
@EnableFeignClients({"com.github.vcvitaly.learnwordsusingphrases.client"})
public class FeignConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Slf4j
    public static class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            String message;
            try {
                message = fetchReaderData(response.body().asReader(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                return new DefinitionNotFoundException(message);
            }

            return new ErrorDecoder.Default().decode(methodKey, response);
        }
    }
}
