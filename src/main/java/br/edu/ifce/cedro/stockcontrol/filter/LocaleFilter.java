package br.edu.ifce.cedro.stockcontrol.filter;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            String lang = req.getParameter("lang");
            HttpSession session = req.getSession();
            if (lang != null) {
                Locale locale = resolveLocale(lang);
                if (locale != null) {
                    Config.set(session, Config.FMT_LOCALE, locale);
                    session.setAttribute("appLocale", locale.toLanguageTag());
                    session.setAttribute("appLocaleBase", base(lang));
                }
            }
            if (Config.get(session, Config.FMT_LOCALE) == null) {
                Locale defaultLocale = Locale.forLanguageTag("pt-BR");
                Config.set(session, Config.FMT_LOCALE, defaultLocale);
                session.setAttribute("appLocale", defaultLocale.toLanguageTag());
                session.setAttribute("appLocaleBase", "pt");
            }
        }
        chain.doFilter(request, response);
    }

    private Locale resolveLocale(String lang) {
        if ("en".equalsIgnoreCase(lang)) {
            return Locale.ENGLISH;
        }
        if ("pt".equalsIgnoreCase(lang) || "pt-BR".equalsIgnoreCase(lang)) {
            return Locale.forLanguageTag("pt-BR");
        }
        return null;
    }

    private String base(String lang) {
        if ("en".equalsIgnoreCase(lang)) return "en";
        return "pt";
    }
}
