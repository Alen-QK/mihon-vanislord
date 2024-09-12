package mihon.domain.extensionrepo.service

import androidx.core.net.toUri
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.network.awaitSuccess
import eu.kanade.tachiyomi.network.parseAs
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import logcat.LogPriority
import mihon.domain.extensionrepo.model.ExtensionRepo
import mihon.domain.extensionrepo.model.KavitaOpds
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import tachiyomi.core.common.util.lang.withIOContext
import tachiyomi.core.common.util.system.logcat

class ExtensionRepoService(
    networkHelper: NetworkHelper,
    private val json: Json,
    private val kavitaJson: Json,
) {
    val client = networkHelper.client

    suspend fun fetchRepoDetails(
        repo: String,
    ): ExtensionRepo? {
        return withIOContext {
            val url = "$repo/repo.json".toUri()

            try {
                with(json) {
                    client.newCall(GET(url.toString()))
                        .awaitSuccess()
                        .parseAs<ExtensionRepoMetaDto>()
                        .toExtensionRepo(baseUrl = repo)
                }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e) { "Failed to fetch repo details" }
                null
            }
        }
    }

    suspend fun fetchKavitaOpds(
        username: String,
        password: String
    ):KavitaOpds? {
        return withIOContext {
            val url = "https://aijiangsb.com:7776/api/Account/login"
            val loginRequest = LoginRequest(username= username, password= password, apiKey = "")
            val bodyJson = Json.encodeToString(loginRequest)
            val requestBody = bodyJson.toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            try {
                with(kavitaJson) {
                    client.newCall(request)
                        .awaitSuccess()
                        .parseAs<LoginResponse>()
                        .toOpds()
                }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e) { "Failed to fetch repo details" }
                null
            }
        }
    }
}

@Serializable
data class LoginRequest(val username: String, val password: String, val apiKey: String)


