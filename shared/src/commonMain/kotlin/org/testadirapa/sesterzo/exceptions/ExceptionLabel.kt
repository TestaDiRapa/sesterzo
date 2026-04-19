package org.testadirapa.sesterzo.exceptions

enum class ExceptionLabel {
	BudgetNotFound,
	BudgetElementNotFound,
	CannotDecryptWithAesKey,
	CannotDecryptWithRsaKey,
	CannotEncryptWithAesKey,
	ExpenseDeletionFailed,
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
	SpaceNotFound,
	Unauthorized,
	UserNotFound,
	UserUpdateFailed
}