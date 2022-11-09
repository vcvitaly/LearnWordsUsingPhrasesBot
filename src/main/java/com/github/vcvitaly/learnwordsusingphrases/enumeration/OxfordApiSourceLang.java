package com.github.vcvitaly.learnwordsusingphrases.enumeration;

/**
 * OxfordApiSourceLang.
 *
 * @author Vitalii Chura
 */
public enum OxfordApiSourceLang {

    EN_GB("en-gb");

    OxfordApiSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    private String sourceLang;

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }
}
