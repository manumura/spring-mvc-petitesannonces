/**
 * 
 */
package fr.manu.petitesannonces.persistence.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.manu.petitesannonces.dto.UserRole;
import fr.manu.petitesannonces.persistence.converter.Converter;
import fr.manu.petitesannonces.persistence.dao.UserRoleDao;
import fr.manu.petitesannonces.persistence.entities.UserRoleEntity;
import fr.manu.petitesannonces.persistence.exceptions.BusinessException;
import fr.manu.petitesannonces.persistence.exceptions.TechnicalException;
import fr.manu.petitesannonces.persistence.exceptions.impl.DaoException;
import fr.manu.petitesannonces.persistence.exceptions.impl.ServiceException;
import fr.manu.petitesannonces.persistence.services.UserRoleService;

/**
 * @author Manu
 *
 */
@Service("userRoleService")
public class UserRoleBusiness implements UserRoleService {

    @Autowired
    @Qualifier("userRoleDao")
    private UserRoleDao userRoleDao;

	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	private static final Logger logger = LoggerFactory.getLogger(UserRoleBusiness.class);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public UserRole get(final Long id) throws BusinessException, TechnicalException {
		
        logger.debug(">>>>> start : {} <<<<<", id);
		
		if (id == null) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
		}
		
		try {
            final UserRole role = converter.convert(userRoleDao.get(id), UserRole.class);
            logger.debug(">>>>> end : {} <<<<<", role);
			return role;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException : " + de.getMessage() + " <<<<<", de);
		}
	}

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public UserRole getByType(final String type) throws BusinessException, TechnicalException {
		
        logger.debug(">>>>> start : {} <<<<<", type);
		
		if (type == null || type.isEmpty()) {
            logger.error(">>>>> Illegal parameter <<<<<");
            return null;
		}
		
		try {
            final UserRole role =
                    converter.convert((UserRoleEntity) userRoleDao.getByType(type), UserRole.class);
            logger.debug(">>>>> end : {} <<<<<", role);
			return role;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException : " + de.getMessage() + " <<<<<", de);
        }
	}

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<UserRole> list() throws BusinessException, TechnicalException {
		
        logger.debug(">>>>> start <<<<<");
		
		try {
            final List<UserRole> listUserRole =
                    converter.convertList(userRoleDao.list(), UserRole.class);
            logger.debug(">>>>> end : {} <<<<<", listUserRole);
			return listUserRole;

        } catch (DaoException de) {
            logger.error(">>>>> DaoException : {} <<<<<", de.getMessage(), de);
            throw new ServiceException(">>>>> DaoException : " + de.getMessage() + " <<<<<", de);
        }
	}
}
