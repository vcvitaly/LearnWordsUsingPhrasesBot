package com.github.vcvitaly.learnwordsusingphrases.configuration;

import com.github.vcvitaly.learnwordsusingphrases.service.DefinitionApiService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.List;
import java.util.Objects;

/**
 * DefinitionServiceList.
 *
 * @author Vitalii Chura
 */
@RequiredArgsConstructor
public class DefinitionServiceList implements List<DefinitionApiService> {

    @Delegate
    private final List<DefinitionApiService> list;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefinitionServiceList that = (DefinitionServiceList) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
