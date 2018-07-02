/**
 * 
 */
package fr.manu.petitesannonces.web.email;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.DataStoreUtils;

/**
 * @author Manu
 *
 */
// TODO http://grepcode.com/file/repo1.maven.org/maven2/com.google.http-client/google-http-client-jdo/1.16.0-rc/com/google/api/client/extensions/jdo/JdoDataStoreFactory.java
public class PersistentDataStoreFactory extends AbstractDataStoreFactory {

	@Override
	protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
		return new PersistentDataStore<V>(this, id);
	}

	static class PersistentDataStore<V extends Serializable> extends AbstractDataStore<V> {

		protected PersistentDataStore(DataStoreFactory dataStoreFactory, String id) {
			super(dataStoreFactory, id);
		}

		@Override
		public Set<String> keySet() throws IOException {
			return null;
		}

		@Override
		public Collection<V> values() throws IOException {
			return null;
		}

		@Override
		public V get(String key) throws IOException {
			return null;
		}

		@Override
		public DataStore<V> set(String key, V value) throws IOException {
			return null;
		}

		@Override
		public DataStore<V> clear() throws IOException {
			return null;
		}

		@Override
		public DataStore<V> delete(String key) throws IOException {
			return null;
		}

		@Override
		public String toString() {
			return DataStoreUtils.toString(this);
		}

	}
}
