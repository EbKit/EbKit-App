package io.ebkit.app

import android.content.pm.PackageInfo
import android.os.Build
import java.security.MessageDigest

fun ByteArray.digest(algorithm: String): ByteArray =
    MessageDigest.getInstance(algorithm).digest(this)

fun PackageInfo.getSignature(): String?{
    val apkSigners = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this@getSignature.signingInfo?.apkContentsSigners
    } else {
        this@getSignature.signatures
    }

    return apkSigners?.firstOrNull()?.toByteArray()?.digest(
        algorithm = "sha256",
    )?.toHexString(
        format = HexFormat.UpperCase,
    )
}