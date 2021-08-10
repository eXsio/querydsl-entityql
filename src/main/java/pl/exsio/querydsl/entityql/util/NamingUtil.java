package pl.exsio.querydsl.entityql.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class NamingUtil {

    public static String camelToUnderscore(String camel) {
        return camel.replaceAll("([^A-Z])([A-Z0-9])", "$1_$2") // standard replace
                .replaceAll("([A-Z]+)([A-Z0-9][^A-Z]+)", "$1_$2") // last letter after full uppercase.
                .replaceAll("([0-9]+)([a-zA-Z]+)", "$1_$2").toUpperCase();
    }

    public static String underscoreToCamel(String underscore) {
        String camel = StringUtils.remove(WordUtils.capitalizeFully(underscore, '_'), "_");
        return Character.toLowerCase(camel.charAt(0)) + camel.substring(1);
    }
}
