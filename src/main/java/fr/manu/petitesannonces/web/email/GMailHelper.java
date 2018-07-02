package fr.manu.petitesannonces.web.email;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import fr.manu.petitesannonces.web.controller.ApplicationController;

/**
 * @author emmanuel.mura
 */
public class GMailHelper {

	/** Application name. */
	private static final String APPLICATION_NAME = "petitesannonces";
	
	/** Application name. */
    private static final String USER_ID = "emmanuel.mura@gmail.com";

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static DataStoreFactory DATA_STORE_FACTORY;

	/** Directory to store user credentials for this application. */
	private static File DATA_STORE_DIR;

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	private static GoogleAuthorizationCodeFlow FLOW;

	/**
	 * Global instance of the scopes Check
	 * https://developers.google.com/gmail/api/auth/scopes for all available
	 * scopes
	 */
	private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, "email", GmailScopes.GMAIL_SEND);

	/**
	 * Email address of the user, or "me" can be used to represent the currently
	 * authorized user.
	 */
//	private static final String USER = "emmanuel.mura@gmail.com";

	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	static {
		// initialize static variables
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials"); 

			logger.debug("Data store : {}", DATA_STORE_DIR);

			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
			
			// Load client secrets.
			InputStream in = GMailHelper.class.getResourceAsStream("/client_secret.json");
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			logger.debug("Clients secrets : " + clientSecrets);
			
            // TODO : offline ? auto ?
            FLOW = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline") // online / offline
                            .setApprovalPrompt("auto") // force / auto
                            .build();

		} catch (GeneralSecurityException e) {
			logger.error(">>>>> GeneralSecurityException in GMailHelper init <<<<<", e);
		} catch (IOException e) {
			logger.error(">>>>> IOException in GMailHelper init <<<<<", e);
		}
	}

    private GMailHelper() {
        // Nothing to do here
    }

	public static Credential loadCredential() throws IOException {
		//TODO save credentials
		Credential credential = FLOW.loadCredential(USER_ID);

        // TODO
        if (credential != null) {
            credential.refreshToken();
            String accessToken = credential.getAccessToken();
            String refreshToken = credential.getRefreshToken();
            Long expirationTimeMillis = credential.getExpirationTimeMilliseconds();
        }

        logger.debug("Credentials : {}", credential);
		return credential;
	}

	public static AuthorizationRequestUrl getAuthorizationRequestUrl(final String redirectURI) throws IOException {
		AuthorizationCodeRequestUrl authorizationUrl = FLOW.newAuthorizationUrl().setRedirectUri(redirectURI);
        logger.debug("Authorization URL : {}", authorizationUrl);
		return authorizationUrl;
	}

	public static Gmail getGmailService() throws IOException {

		// Get credentials
		Credential credential = loadCredential();
		if (credential == null) {
			logger.debug("Credentials not found");
			return null;
		}

        logger.debug("Credentials : {}", credential);

		Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
		
        logger.debug("Gmail service : {}", service);
		
		return service;
	}

	public static Gmail getGmailService(final String redirectURI, final String code) throws IOException {

		GoogleTokenResponse response = FLOW.newTokenRequest(code).setRedirectUri(redirectURI).execute(); // GoogleOAuthConstants.OOB_REDIRECT_URI
		Credential credential = FLOW.createAndStoreCredential(response, USER_ID);

        logger.debug("Credentials : {}", credential);

		Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
		
        logger.debug("Gmail service : {}", service);
		
		return service;
	}

    // public GoogleCredential getGoogleCredential(final ClientCredentials credentials)
    // throws IOException {
    // // Use the default which is file-based; name and location are configurable
    // final DataStore<StoredCredential> datastore = getDataStore(
    // credentials.getCredentialDatastore(), credentials.getDataStoreDirectory());
    // return getGoogleCredential(datastore, credentials);
    // }
    //
    // public DataStore<StoredCredential> getDataStore(final String id,
    // final String dataStoreDirectory) throws IOException {
    // return new FileDataStoreFactory(new File(dataStoreDirectory)).getDataStore(id);
    // }

    // public GoogleCredential getGoogleCredential(final DataStore<StoredCredential> datastore,
    // final ClientCredentials authContext) throws IOException {
    // final GoogleCredential gCred;
    // final LocalServerReceiver localReceiver = new LocalServerReceiver();
    // final String accessToken;
    // final String refreshToken;
    //
    // try {
    // // Reads the client id and client secret from a file name passed in authContext
    // final GoogleClientSecrets gClientSecrets = GoogleClientSecrets
    // .load(new JacksonFactory(), new FileReader(authContext.getClientSecrets()));
    //
    // // Obtain tokens from credential in data store, or obtains a new one from
    // // Google if one doesn't exist
    // final StoredCredential sCred = datastore.get(authContext.getClientId());
    // if (sCred != null) {
    // accessToken = sCred.getAccessToken();
    // refreshToken = sCred.getRefreshToken();
    // logger.debug(
    // MessageFormat.format("Found credential for client {0} in data store {1}",
    // authContext.getClientId(), datastore.getId()));
    // } else {
    // // This flow supports installed applications
    // final GoogleAuthorizationCodeFlow flow =
    // new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
    // new JacksonFactory(), gClientSecrets, authContext.getScopes())
    // .setCredentialDataStore(datastore).setApprovalPrompt("auto")
    // .setAccessType("offline").build();
    // final Credential cred = new AuthorizationCodeInstalledApp(flow, localReceiver)
    // .authorize(authContext.getClientId());
    // accessToken = cred.getAccessToken();
    // refreshToken = cred.getRefreshToken();
    // logger.debug(MessageFormat.format(
    // "Created new credential for client {0} in data store {1}",
    // authContext.getClientId(), datastore.getId()));
    // }
    // gCred = new GoogleCredential.Builder().setClientSecrets(gClientSecrets)
    // .setJsonFactory(new JacksonFactory()).setTransport(new NetHttpTransport())
    // .build();
    // gCred.setAccessToken(accessToken);
    // gCred.setRefreshToken(refreshToken);
    // logger.debug(MessageFormat.format("Found credential {0} using {1}",
    // gCred.getRefreshToken(), authContext.toString()));
    // } finally {
    // localReceiver.stop();
    // }
    // return gCred;
    // }
}
