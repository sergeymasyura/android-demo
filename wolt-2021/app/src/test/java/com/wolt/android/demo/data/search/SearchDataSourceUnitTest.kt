package com.wolt.android.demo.data.search

import com.wolt.android.demo.data.location.Location
import com.wolt.android.demo.util.AssetReader
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Test
import com.wolt.android.demo.data.Result.Error as ResultError
import com.wolt.android.demo.data.Result.Success as ResultSuccess


class SearchDataSourceUnitTest {

    @Test
    fun search_success() = runBlocking {
        val mockServer = MockWebServer()
        mockServer.start()

        val mockResponse: MockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(AssetReader().getText("json/response.json"))

        mockServer.enqueue(mockResponse)

        val searchDataSource = SearchDataSource(mockServer.url("/").toString())
        val location = Location(60.170187, 24.930599)

        val result = searchDataSource.search(location)
        assertThat(result, IsInstanceOf(ResultSuccess::class.java))
        val places = (result as ResultSuccess).data

        assertThat(places.count(), `is`(50))
        assertThat(places[0].id, `is`("56cc58bd0662b569bb5d2603"))
        assertThat(places[0].name, `is`("Kabuki"))
        assertThat(places[0].description, `is`("Helsingin vanhin japanilainen"))
        assertThat(
            places[0].imageUrl,
            `is`("https://prod-wolt-venue-images-cdn.wolt.com/56cc58bd0662b569bb5d2603/ef8b7594-386f-11ea-9d0f-0a58646005bc_Cover1.jpg")
        )

        assertThat(places[49].id, `is`("55685e4f38c3017fc2f26310"))
        assertThat(places[49].name, `is`("Fat Ramen Hietalahden Kauppahalli"))
        assertThat(places[49].description, `is`("First ramen restaurant in Helsinki!"))
        assertThat(
            places[49].imageUrl,
            `is`("https://prod-wolt-venue-images-cdn.wolt.com/55685e4f38c3017fc2f26310/3c6c22e724d4b245a78c28d6bf3b9c98")
        )

        mockServer.shutdown()
    }

    @Test
    fun search_http_failure() = runBlocking {
        val mockServer = MockWebServer()
        mockServer.start()

        val mockResponse = MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        mockServer.enqueue(mockResponse)

        val searchDataSource = SearchDataSource(mockServer.url("/").toString())
        val location = Location(60.170187, 24.930599)

        val result = searchDataSource.search(location)
        assertThat(result, IsInstanceOf(ResultError::class.java))

        mockServer.shutdown()
    }

    @Test
    fun search_status_fail() = runBlocking {
        val mockServer = MockWebServer()
        mockServer.start()

        val mockResponse: MockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(AssetReader().getText("json/response_status_fail.json"))

        mockServer.enqueue(mockResponse)

        val searchDataSource = SearchDataSource(mockServer.url("/").toString())
        val location = Location(60.170187, 24.930599)

        val result = searchDataSource.search(location)
        assertThat(result, IsInstanceOf(ResultError::class.java))

        mockServer.shutdown()
    }

    @Test
    fun search_invalid_json() = runBlocking {
        val mockServer = MockWebServer()
        mockServer.start()

        val mockResponse: MockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(AssetReader().getText("json/response_invalid.json"))

        mockServer.enqueue(mockResponse)

        val searchDataSource = SearchDataSource(mockServer.url("/").toString())
        val location = Location(60.170187, 24.930599)

        val result = searchDataSource.search(location)
        assertThat(result, IsInstanceOf(ResultError::class.java))

        mockServer.shutdown()
    }
}
