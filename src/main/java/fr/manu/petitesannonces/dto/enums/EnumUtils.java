/**
 * 
 */
package fr.manu.petitesannonces.dto.enums;

/**
 * @author Manu
 *
 */
public class EnumUtils {

    private EnumUtils() {
        // Nothing to do here
    }

	/**
     * Get the enum value from the getValue method
     * /!\ Mandatory for the enum to implement the EnumeratedValue interface !
     * 
     * @param type Enum
     * @param <T> class
     * @param value value
     * @return T enum
     */
    public static final <T extends Enum<T>> T byValue(final Class<T> type, final Object value) {
        
        if (!EnumeratedValue.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("%s does not implements %s", type.getName(),
                    EnumeratedValue.class.getName()));
        }

        // values iteration
        for (T current : type.getEnumConstants()) {
            final EnumeratedValue<?> cur = (EnumeratedValue<?>) current;
            final Object checked = cur.getValue();

            if ((value == null && checked == null) || (value != null && value.equals(checked))) {
                return current;
            }
        }

        // return null if not found
        return null;
    }
    
    /**
     * Get the enum value from the getLabel method
     * /!\ Mandatory for the enum to implement the EnumeratedLabel interface !
     * 
     * @param type Enum
     * @param <T> class
     * @param label label
     * @return T enum
     */
    public static final <T extends Enum<T>> T byLabel(final Class<T> type, final Object label) {
        
        if (!EnumeratedLabel.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("%s does not implements %s", type.getName(),
                    EnumeratedLabel.class.getName()));
        }

        // values iteration
        for (T current : type.getEnumConstants()) {
            final EnumeratedLabel<?> cur = (EnumeratedLabel<?>) current;
            final Object checked = cur.getLabel();

            if ((label == null && checked == null) || (label != null && label.equals(checked))) {
                return current;
            }
        }

        // return null if not found
        return null;
    }
}
