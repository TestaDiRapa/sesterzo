package org.testadirapa.sesterzo.exceptions

enum class ExceptionLabel {
	CannotDecryptWithAesKey,
	CannotDecryptWithRsaKey,
	InvalidCaptcha,
	InvalidJwt,
	InvalidKeyFormat,
	InvalidRegistration,
	InvalidRegistrationParameters,
	InvalidPrivateKey,
	PublicKeyUpdateFailed,
	QuotaExceeded,
	RecoveryKeyExpired,
	RecoveryKeyNotFound,
	Unauthorized,
	UserNotFound,
	UserUpdateFailed
}