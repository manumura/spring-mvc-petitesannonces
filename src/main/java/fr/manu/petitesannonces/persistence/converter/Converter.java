/**
 * 
 */
package fr.manu.petitesannonces.persistence.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

/**
 * @author Manu
 *
 */
@Component("converter")
public class Converter {

	/** Moteur de mapping */
	private DozerBeanMapper mapper;
	
	/** Nom du fichier applicatif chargé */
    private static final String APPLICATION_MAPPING = "app-mappings.xml";

	/**
	 * Récupère l'instance dozer
	 * 
	 * @return DozerBeanMapper
	 */
	private DozerBeanMapper getMapper() {

		// chargement de la configuration via Dozer
		if (this.mapper == null) {
			this.mapper = new DozerBeanMapper();
			
			final List<String> mappingFiles = Arrays.asList(APPLICATION_MAPPING);
			mapper.setMappingFiles(mappingFiles);
		}

		return this.mapper;
	}

	/**
	 * Convertit un objet src en un nouvel objet de type T
	 *
	 * @param src
	 *            objet source
	 * @param dst
	 *            type destination
	 * @param <T> class
	 * @return nouvel objet
	 */
	public <T> T convert(final Object src, final Class<T> dst) {

		final T res;

		if (src == null) {
			res = null;
		} else if (dst.isInstance(src)) {
			final T tmp = (T) src;
			res = tmp; // pas de conversion
		} else {
            final T tmp = getMapper().map(src, dst); // conversion
			res = tmp;
		}

		return res;
	}

	/**
	 * Copie un objet src dans un objet existant
	 *
	 * @param src
	 *            objet source
	 * @param dst
	 *            objet destination
	 * @param <T> class
	 * @return destination
	 */
	public <T> T convert(final Object src, T dst) {

	    T dstLocal = dst;
		if (src == null) {
		  dstLocal = null;
		} else {
			getMapper().map(src, dstLocal);
		}

		return dstLocal;
	}

	/**
	 * Convertit une liste d'objets src en une liste de nouveaux objets de type
	 * T
	 *
	 * @param src
	 *            objet source
	 * @param dst
	 *            type destination
	 * @param <T> class
	 * @return nouvel objet
	 */
	public <T> List<T> convertList(final List<?> src, final Class<T> dst) {

		final List<T> res;
		if (src == null) {
			res = null;
		} else {
			res = new ArrayList<T>(src.size());

			for (final Object o : src) {
				res.add(convert(o, dst));
			}
		}

		return res;
	}
}
