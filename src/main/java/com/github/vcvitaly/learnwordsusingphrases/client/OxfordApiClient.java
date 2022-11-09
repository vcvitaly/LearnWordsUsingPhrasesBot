package com.github.vcvitaly.learnwordsusingphrases.client;

import com.github.vcvitaly.learnwordsusingphrases.dto.oxford.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OxfordApiClient.
 *
 * @author Vitalii Chura
 */
@FeignClient(name = "oxford-api-client", url = "${route.oxfordapi}")
public interface OxfordApiClient {

    String ENDPOINT = "/api/v2";

    @GetMapping(ENDPOINT + "/entries/{source_lang}/{word_id}")
    Response getWordDefinitionResponse(@RequestHeader(value = "app_id") String appId,
                                       @RequestHeader(value = "app_key") String appKey,
                                       @PathVariable(value = "source_lang") String sourceLang,
                                       @PathVariable(value = "word_id") String word,
                                       @RequestParam(value = "fields") String[] fields,
                                       @RequestParam(value = "strictMatch") Boolean strictMatch);
}
