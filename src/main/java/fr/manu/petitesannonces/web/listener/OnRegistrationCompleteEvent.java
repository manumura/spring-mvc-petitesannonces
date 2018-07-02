package fr.manu.petitesannonces.web.listener;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import fr.manu.petitesannonces.dto.User;

/**
 * @author emmanuel.mura
 *
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private static final long serialVersionUID = -6933110514542085978L;

    private final String appUrl;
    private final Locale locale;
    private final User user;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    /**
     * @return the appUrl
     */
    public String getAppUrl() {
        return appUrl;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OnRegistrationCompleteEvent [appUrl=" + appUrl + ", locale=" + locale + ", user="
                + user + "]";
    }

}
