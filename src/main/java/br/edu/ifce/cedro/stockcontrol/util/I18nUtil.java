package br.edu.ifce.cedro.stockcontrol.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

public class I18nUtil {

    public static Locale getLocale(HttpServletRequest request) {
        if (request == null) {
            return Locale.forLanguageTag("pt-BR");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loc = Config.get(session, Config.FMT_LOCALE);
            if (loc instanceof Locale) {
                return (Locale) loc;
            }
            if (loc instanceof String) {
                return Locale.forLanguageTag((String) loc);
            }
        }
        return Locale.forLanguageTag("pt-BR");
    }

    public static String getMessage(HttpServletRequest request, String key, Object... args) {
        Locale locale = getLocale(request);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        String pattern = bundle.getString(key);
        if (args != null && args.length > 0) {
            return new MessageFormat(pattern, locale).format(args);
        }
        return pattern;
    }
}
