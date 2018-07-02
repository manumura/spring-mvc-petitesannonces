/**
 * 
 */
package fr.manu.petitesannonces.dto.enums;

/**
 * 
 * @author Manu
 *
 * @param <T> class
 */
@FunctionalInterface
public interface EnumeratedLabel<T> {

    /**
     * Return value of current enum
     * @return T
     */
    T getLabel();
}
