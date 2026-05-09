package org.testadirapa.sesterzo.validators

val defaultNameValidator = CompositeValidator(
	NotBlankValidator,
	MaxLengthValidator(60),
)