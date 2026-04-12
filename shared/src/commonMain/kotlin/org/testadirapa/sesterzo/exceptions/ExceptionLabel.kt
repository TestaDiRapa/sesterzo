package org.testadirapa.sesterzo.exceptions

enum class ExceptionLabel {
	BudgetNotFound,
	CannotDecryptWithAesKey,
	CannotDecryptWithRsaKey,
	CannotEncryptWithAesKey,
	ForbiddenInSpace,
	ImageTooLarge,
	InvalidCaptcha,
	InvalidJwt,
	InvalidKeyFormat,
	InvalidRegistration,
	InvalidRegistrationParameters,
	InvalidPrivateKey,
	MissingSpaceKey,
	PublicKeyUpdateFailed,
	QuotaExceeded,
	RecoveryKeyExpired,
	RecoveryKeyNotFound,
	Unauthorized,
	UserNotFound,
	UserUpdateFailed
}