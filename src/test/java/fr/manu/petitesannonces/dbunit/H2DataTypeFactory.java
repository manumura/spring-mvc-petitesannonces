/**
 *
----------------------------------------------------------------------------
Copyright © 2009 by Mobile-Technologies Limited. All rights reserved.
All intellectual property rights in and/or in the computer program and its related
documentation and technology are the sole Mobile-Technologies Limited' property.
This computer program is under Mobile-Technologies Limited copyright and cannot be in whole or in part
reproduced, sublicensed, leased, sold or
used in any form or by any means, including without limitation graphic,
electronic, mechanical,
photocopying, recording, taping or information storage and
retrieval systems without Mobile-Technologies Limited prior written consent. The
downloading, exporting or reexporting of this computer program or any related
documentation or technology is subject to any export rules, including US
regulations.
----------------------------------------------------------------------------
 */
package fr.manu.petitesannonces.dbunit;

import java.sql.Types;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

/**
 * @author emmanuel.mura
 *
 */
public class H2DataTypeFactory extends DefaultDataTypeFactory {

    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if (sqlType == Types.BOOLEAN) {
            return DataType.BOOLEAN;
        }

        return super.createDataType(sqlType, sqlTypeName);
    }
}
