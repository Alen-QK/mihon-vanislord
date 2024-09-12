package mihon.domain.extensionrepo.service

import kotlinx.serialization.Serializable
import mihon.domain.extensionrepo.model.ExtensionRepo
import mihon.domain.extensionrepo.model.KavitaOpds

@Serializable
data class ExtensionRepoMetaDto(
    val meta: ExtensionRepoDto,
)

@Serializable
data class ExtensionRepoDto(
    val name: String,
    val shortName: String?,
    val website: String,
    val signingKeyFingerprint: String,
)

@Serializable
data class LoginResponse(val apiKey: String?)

fun ExtensionRepoMetaDto.toExtensionRepo(baseUrl: String): ExtensionRepo {
    return ExtensionRepo(
        baseUrl = baseUrl,
        name = meta.name,
        shortName = meta.shortName,
        website = meta.website,
        signingKeyFingerprint = meta.signingKeyFingerprint,
    )
}

fun LoginResponse.toOpds(): KavitaOpds {
    return KavitaOpds(
        Opds = "http://aijiangsb.com:7776/api/opds/${apiKey}"
    )
}
