/**
 * 
 */
package fr.manu.petitesannonces.web.converter;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import fr.manu.petitesannonces.web.constantes.Constantes;

/**
 * @author Manu
 *
 */
public class LocalDateEditor extends PropertyEditorSupport {

	@Override
    public void setAsText(String text) {
        LocalDate date = (StringUtils.isEmpty(text)) ? null
                : LocalDate.parse(text, DateTimeFormat.forPattern(Constantes.DATE_FORMAT));
        setValue(date);
	}

	@Override
    public String getAsText() {
		return (getValue() == null) ? null : ((LocalDate) getValue()).toString(Constantes.DATE_FORMAT);
	}
}
