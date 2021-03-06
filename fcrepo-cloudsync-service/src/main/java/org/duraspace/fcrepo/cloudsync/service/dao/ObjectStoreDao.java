package org.duraspace.fcrepo.cloudsync.service.dao;

import org.duraspace.fcrepo.cloudsync.api.ObjectStore;
import org.duraspace.fcrepo.cloudsync.service.backend.StoreConnector;
import org.duraspace.fcrepo.cloudsync.service.util.StringUtil;
import com.github.cwilper.fcrepo.httpclient.HttpClientConfig;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ObjectStoreDao extends AbstractDao {

    private static final String CREATE_DDL =
              "CREATE TABLE ObjectStores (\n"
            + "  id INTEGER PRIMARY KEY NOT NULL GENERATED BY DEFAULT AS IDENTITY,\n"
            + "  name VARCHAR(256) NOT NULL,\n"
            + "  type VARCHAR(32) NOT NULL,\n"
            + "  data VARCHAR(1024) NOT NULL,\n"
            + "  CONSTRAINT ObjectStoreUniqueName unique (name))";

    public ObjectStoreDao(JdbcTemplate db) {
        super(db);
    }

    @Override
    public void initDb() {
        db.execute(CREATE_DDL);
    }

    public ObjectStore createObjectStore(ObjectStore objectStore)
            throws DuplicateKeyException {
        // normalize and validate fields
        if (StringUtil.normalize(objectStore.getId()) != null) {
            throw new IllegalArgumentException("Specifying the Object Store "
                    + "id during creation is not permitted");
        }
        objectStore.setName(StringUtil.validate("name", objectStore.getName(), 256));
        objectStore.setType(StringUtil.validate("type", objectStore.getType(), 32));
        objectStore.setData(StringUtil.validate("data", objectStore.getData(), 1024));
        StoreConnector.getInstance(objectStore, new HttpClientConfig()); // do type-specific validation
        String id = insert(
                "INSERT INTO ObjectStores (name, type, data) VALUES (?, ?, ?)",
                objectStore.getName(),
                objectStore.getType(),
                objectStore.getData());
        return getObjectStore(id);
    }

    public List<ObjectStore> listObjectStores() {
        return db.query("SELECT * FROM ObjectStores",
                new RowMapper<ObjectStore>() {
                    public ObjectStore mapRow(ResultSet rs, int i) throws SQLException {
                        return getObjectStore(rs);
                    }
                });
    }

    public ObjectStore getObjectStore(String id) {
        return db.query("SELECT * FROM ObjectStores WHERE id = ?",
                new ResultSetExtractor<ObjectStore>() {
                    public ObjectStore extractData(ResultSet rs)
                            throws SQLException {
                        if (rs.next()) {
                            return getObjectStore(rs);
                        } else {
                            return null;
                        }
                    }
                },
                Integer.parseInt(id));
    }

    private static ObjectStore getObjectStore(ResultSet rs) throws SQLException {
        ObjectStore o = new ObjectStore();
        o.setId("" + rs.getInt("id"));
        o.setName(rs.getString("name"));
        o.setType(rs.getString("type"));
        o.setData(rs.getString("data"));
        return o;
    }

    public void deleteObjectStore(String id) {
        db.update("DELETE FROM ObjectStores WHERE id = ?", Integer.parseInt(id));
    }

}