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
package fr.manu.petitesannonces.dto;

import java.io.Serializable;

import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author emmanuel.mura
 *
 */
public class Token implements Serializable {

    private static final long serialVersionUID = -3739501787818339390L;

    private Long id;

    private String token;

    private User user;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime expiryDate;

    public Token() {
        // Nothing to do here
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the expiryDate
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Token [id=" + id + ", token=" + token + ", user=" + user + ", expiryDate="
                + expiryDate + "]";
    }
}
