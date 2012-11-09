package org.duraspace.fcrepo.cloudsync.service.backend;

import com.github.cwilper.fcrepo.httpclient.MultiThreadedHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @author Andrew Woods
 *         Date: 10/14/12
 */
public class DuraCloudConnectorTest {

    private DuraCloudConnector connector;

    private URI spaceURI;
    private final String providerId = "2";
    private String prefix = null;
    private MultiThreadedHttpClient httpClient;

    private StoreConnector source;

    @Before
    public void setUp() throws Exception {
        // TODO: implement
    }

    @After
    public void tearDown() throws Exception {
        // TODO: implement
    }

    @Test
    public void testListObjects() throws Exception {
        // TODO: implement
    }

    @Test
    public void testGetObject() throws Exception {
        // TODO: implement
    }

    @Test
    public void testPutObject() throws Exception {
        // TODO: implement
    }

    @Test
    public void testGetContent() throws Exception {
        // TODO: implement
    }

    @Test
    public void testGetStream() throws Exception {
        // TODO: implement
    }

    @Test
    public void testClose() throws Exception {
        // TODO: implement
    }
}
