package org.testadirapa.sesterzo.exceptions

class InvalidRegistrationException(processId: String, token: String) :
	Exception("Invalid registration $processId: $token")
