package mihon.domain.extensionrepo.interactor

import mihon.domain.extensionrepo.service.ExtensionRepoService

class GetKavitaUserOpds(
    private val service: ExtensionRepoService,
) {
    suspend fun await(username: String, password: String): String {
//        if (username.isNullOrBlank() || password.isNullOrBlank()) {
//
//        }
        return service.fetchKavitaOpds(username, password)?.Opds ?: "Error"
    }
}
