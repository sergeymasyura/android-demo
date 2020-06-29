package com.masyura.demo.provider;

import com.masyura.demo.AssetReader;
import com.masyura.demo.model.Commit;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class GitHubGraphQlProviderUnitTest {

    private MockWebServer mockServer;
    private GitHubGraphQlProvider provider;
    private AssetReader assetReader = new AssetReader();

    @Before
    public void setUp() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();

        provider = new GitHubGraphQlProvider(mockServer.url("/").toString(), "apiToken", "repoOwner", "repoName");
    }

    @Test
    public void test_getFirstCommits() {
        TestObserver<List<Commit>> testObserver = new TestObserver<>();

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(assetReader.getText("json/sample_response.json"));

        mockServer.enqueue(mockResponse);

        provider.getFirstCommits(2).subscribe(testObserver);

        testObserver.awaitTerminalEvent(5, TimeUnit.SECONDS);
        testObserver.assertNoErrors();
        testObserver.assertValueCount(1);
        testObserver.assertValue(commits -> (commits.size() == 2) &&
                commits.get(0).getRevision().equals("c0b701b2415791cbd89c857e09b391332c7fce01") &&
                commits.get(1).getAuthor().getEmail().equals("jimtang@google.com"));
    }
}
