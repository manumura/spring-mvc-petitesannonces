package fr.manu.petitesannonces.web.email;

import java.io.Serializable;
import java.util.List;

/**
 * @author emmanuel.mura
 *
 */
public class ClientCredentials implements Serializable {

    private static final long serialVersionUID = -5338973501217648798L;

    private String clientSecrets;

    private String clientId;

    private String credentialDatastore;

    private String dataStoreDirectory;

    private List<String> scopes;

    /**
     * @return the clientSecrets
     */
    public String getClientSecrets() {
        return clientSecrets;
    }

    /**
     * @param clientSecrets the clientSecrets to set
     */
    public void setClientSecrets(String clientSecrets) {
        this.clientSecrets = clientSecrets;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the credentialDatastore
     */
    public String getCredentialDatastore() {
        return credentialDatastore;
    }

    /**
     * @param credentialDatastore the credentialDatastore to set
     */
    public void setCredentialDatastore(String credentialDatastore) {
        this.credentialDatastore = credentialDatastore;
    }

    /**
     * @return the dataStoreDirectory
     */
    public String getDataStoreDirectory() {
        return dataStoreDirectory;
    }

    /**
     * @param dataStoreDirectory the dataStoreDirectory to set
     */
    public void setDataStoreDirectory(String dataStoreDirectory) {
        this.dataStoreDirectory = dataStoreDirectory;
    }

    /**
     * @return the scopes
     */
    public List<String> getScopes() {
        return scopes;
    }

    /**
     * @param scopes the scopes to set
     */
    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

}
