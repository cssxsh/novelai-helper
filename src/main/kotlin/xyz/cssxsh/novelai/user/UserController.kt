package xyz.cssxsh.novelai.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import org.bouncycastle.crypto.digests.*
import org.bouncycastle.crypto.generators.*
import org.bouncycastle.crypto.params.*
import xyz.cssxsh.novelai.*

public class UserController(private val client: NovelAiClient) {
    public suspend fun register(email: String, text: String, giftkey: String, key: String, recaptcha: String): String {
        val body = CreateUserRequest(
            email = email,
            emailCleartext = text,
            giftkey = giftkey,
            key = key,
            recaptcha = recaptcha
        )
        val response = client.http.post("https://api.novelai.net/user/register") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        val login = response.body<SuccessfulLoginResponse>()
        client.config.token = login.accessToken
        return login.accessToken
    }

    private fun blake2b(size: Int, message: String): ByteArray {
        val blake2b = Blake2bDigest(size * 8)
        val data = message.toByteArray()
        blake2b.update(data, 0, data.size)
        val hash = ByteArray(blake2b.digestSize)
        blake2b.doFinal(hash, 0)

        return hash
    }

    private fun argon2(password: String, salt: ByteArray, size: Int): String {
        val argon2 = Argon2BytesGenerator()
        val parameters = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withIterations(2)
            .withMemoryAsKB(2000000 / 1024)
            .withParallelism(1)
            .build()
        argon2.init(parameters)

        val target = ByteArray(size)
        argon2.generateBytes(password.toByteArray(), target)

        return target.encodeBase64()
    }

    public suspend fun login(email: String, password: String): String {
        val salt = blake2b(16, password.substring(0, 6) + email + "novelai_data_access_key")
        val key = argon2(password, salt, 64).substring(0, 64)
        val body = LoginRequest(key = key)
        val response = client.http.post("https://api.novelai.net/user/login") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        val login = response.body<SuccessfulLoginResponse>()
        client.config.token = login.accessToken
        return login.accessToken
    }

    public suspend fun changeAccessKey(current: String, new: String, email: String): String {
        val body = ChangeAccessKeyRequest(currentAccessKey = current, newAccessKey = new, newEmail = email)
        val response = client.http.post("https://api.novelai.net/user/change-access-key") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        val login = response.body<SuccessfulLoginResponse>()
        client.config.token = login.accessToken
        return login.accessToken
    }

    public suspend fun sendEmailVerification(email: String) {
        val body = EmailVerificationStartRequest(email = email)
        val response = client.http.post("https://api.novelai.net/user/resend-email-verification") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        response.body<ByteArray>()
    }

    public suspend fun verifyEmail(token: String) {
        val body = EmailVerificationRequest(verificationToken = token)
        val response = client.http.post("https://api.novelai.net/user/verify-email") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
        response.body<ByteArray>()
    }

    public suspend fun information(): AccountInformation {
        val response = client.http.get("https://api.novelai.net/user/information")
        return response.body()
    }

    public suspend fun data(): UserAccountData {
        val response = client.http.get("https://api.novelai.net/user/data")
        return response.body()
    }

    public suspend fun priority(): Priority {
        val response = client.http.get("https://api.novelai.net/user/priority")
        return response.body()
    }

    public suspend fun giftkeys(): List<Any> {
        val response = client.http.get("https://api.novelai.net/user/giftkeys")
        val data = response.body<GiftKeysResponse>()
        return data.giftKeys
    }

    public suspend fun subscription(): Subscription {
        val response = client.http.get("https://api.novelai.net/user/subscription")
        return response.body()
    }

    public suspend fun keystore(): Keystore {
        val response = client.http.get("https://api.novelai.net/user/keystore")
        return response.body()
    }
}