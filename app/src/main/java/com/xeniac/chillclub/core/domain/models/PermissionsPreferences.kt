package com.xeniac.chillclub.core.domain.models

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.xeniac.chillclub.core.domain.utils.CryptoHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

@Serializable
data class PermissionsPreferences(
    val notificationPermissionCount: Int = 0
)

object PermissionsPreferencesSerializer : Serializer<PermissionsPreferences> {

    override val defaultValue: PermissionsPreferences = PermissionsPreferences()

    override suspend fun readFrom(
        input: InputStream
    ): PermissionsPreferences = try {
        withContext(Dispatchers.IO) {
            val encryptedBytes = input.use { it.readBytes() }
            val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
            val decryptedBytes = CryptoHelper.decrypt(encryptedBytesDecoded)
            val decodedJsonString = decryptedBytes.decodeToString()

            Json.decodeFromString(decodedJsonString)
        }
    } catch (e: SerializationException) {
        throw CorruptionException(
            message = "Unable to read PermissionsPreferences",
            cause = e
        )
    }

    override suspend fun writeTo(
        t: PermissionsPreferences,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            val encodedJsonString = Json.encodeToString(t)
            val bytes = encodedJsonString.toByteArray()
            val encryptedBytes = CryptoHelper.encrypt(bytes)
            val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)

            output.use { it.write(encryptedBytesBase64) }
        }
    }
}