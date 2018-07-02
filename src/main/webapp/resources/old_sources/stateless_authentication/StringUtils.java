package fr.manu.petitesannonces.web.security.statelessauthentication;

import com.google.common.base.Preconditions;

public final class StringUtils {

	private StringUtils() {
	}

	public static String checkNotBlank(String string) {
		Preconditions.checkArgument(string != null && string.trim().length() > 0);
		return string;
	}
}
