package com.github.vcvitaly.learnwordsusingphrases.enumeration;

import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.FreeDictionaryApiService;
import com.github.vcvitaly.learnwordsusingphrases.service.impl.OxfordApiService;
import com.github.vcvitaly.learnwordsusingphrases.util.StringUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DefinitionApiServiceType.
 *
 * @author Vitalii Chura
 */
public enum DefinitionApiServiceType {

    FREE_DICTIONARY(FreeDictionaryApiService.class),
    OXFORD(OxfordApiService.class);

    @Getter
    private final String matchingServiceClassName;

    private static final Map<String, DefinitionApiServiceType> map = new HashMap<>();

    static {
        for (DefinitionApiServiceType type : DefinitionApiServiceType.values()) {
            map.put(type.matchingServiceClassName, type);
        }
    }

    DefinitionApiServiceType(Class<? extends DefinitionApiService> mathingServiceClass) {
        this.matchingServiceClassName = StringUtils.classNameToLowerCamelCase(mathingServiceClass.getSimpleName());
    }

    public static DefinitionApiServiceType getByBeanName(String beanName) {
        return Optional.ofNullable(map.get(beanName))
                .orElseThrow(() -> new IllegalArgumentException("No api service type found for bean name " + beanName));
    }
}
