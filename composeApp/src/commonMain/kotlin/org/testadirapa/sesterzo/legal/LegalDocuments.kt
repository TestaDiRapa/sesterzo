package org.testadirapa.sesterzo.legal

data class LegalSection(
	val heading: String?,
	val body: String,
)

data class LegalDocument(
	val title: String,
	val lastUpdated: String,
	val sections: List<LegalSection>,
)

object LegalDocuments {
	const val GITHUB_URL = "https://github.com/TestaDiRapa/sesterzo"

	val PrivacyPolicy = LegalDocument(
		title = "Privacy Policy",
		lastUpdated = "Last updated: July 11, 2026",
		sections = listOf(
			LegalSection(
				heading = null,
				body = "Sesterzo is a free, open-source budgeting application. This policy explains what data " +
					"the application handles, how it is protected, and what your rights are. The short version: " +
					"we collect the bare minimum needed to make the app work, your financial data is end-to-end " +
					"encrypted so we cannot read it, and your email address is never used for anything other " +
					"than registration and authentication.",
			),
			LegalSection(
				heading = "1. Data we collect",
				body = "Sesterzo only processes the following data:\n" +
					"• Email address — used as your account identifier and to deliver one-time authentication codes.\n" +
					"• Display name — the name or nickname you choose at registration, shown to people you share a budget with.\n" +
					"• Financial data — your budgets, entries, income sources, savings, and attachments. This data is " +
					"end-to-end encrypted on your device before it ever reaches our servers.\n" +
					"• Anonymous error reports — only if you explicitly opt in. You can enable or disable error " +
					"reporting at any time from the settings page.\n\n" +
					"We do not collect analytics, usage statistics, advertising identifiers, device fingerprints, " +
					"or location data. The application does not use tracking cookies.",
			),
			LegalSection(
				heading = "2. End-to-end encryption",
				body = "All financial data is encrypted on your device using keys that only you hold:\n" +
					"• Your private key is generated on your device and never leaves it unencrypted. It is not " +
					"transmitted to, or stored on, our servers in a readable form.\n" +
					"• The server only ever stores ciphertext. We are technically unable to read your budgets, " +
					"entries, or attachments.\n" +
					"• Sharing a budget with another person shares the corresponding encryption key directly with " +
					"that person's account; no one else — including us — gains access.\n" +
					"• If you lose your private key and your recovery sentence, your data cannot be recovered by " +
					"anyone, including us. Please back up your key as instructed in the app.",
			),
			LegalSection(
				heading = "3. How we use your email address",
				body = "Your email address is used exclusively for creating and identifying your account, and for " +
					"sending one-time codes to authenticate registration and login.\n\n" +
					"Your email address will never be used for marketing emails, newsletters, or any kind of email " +
					"campaign. It will never be sold, rented, or shared with third parties.",
			),
			LegalSection(
				heading = "4. Error reporting (opt-in)",
				body = "If you enable anonymous error reporting, technical error information is sent to help " +
					"improve the application. These reports are anonymous by design and never include your " +
					"financial data, which is encrypted before leaving your device. You can opt out at any time " +
					"in the settings page.",
			),
			LegalSection(
				heading = "5. Data sharing",
				body = "We do not share your data with third parties. The only external parties involved are the " +
					"infrastructure providers hosting the server, which store only encrypted content and the " +
					"minimal account data described above.",
			),
			LegalSection(
				heading = "6. Data retention and deletion",
				body = "Your account data is kept for as long as your account exists. You can request the deletion " +
					"of your account and all associated data at any time by opening an issue on the GitHub " +
					"repository or contacting the maintainer. Because financial data is stored encrypted, deleting " +
					"your keys already renders it permanently unreadable.",
			),
			LegalSection(
				heading = "7. Your rights",
				body = "Depending on your jurisdiction (e.g. GDPR in the European Union), you have the right to " +
					"access, rectify, export, and erase your personal data, and to object to or restrict its " +
					"processing. Given the minimal data collected, most of these rights can be exercised directly " +
					"in the app; for anything else, contact the maintainer via the GitHub repository.",
			),
			LegalSection(
				heading = "8. Children",
				body = "Sesterzo is not directed at children under 16 and does not knowingly collect data from them.",
			),
			LegalSection(
				heading = "9. Changes to this policy",
				body = "This policy may be updated from time to time. Material changes will be announced in the " +
					"application and reflected in the \"Last updated\" date above. The full history of this " +
					"document is available in the repository's version control.",
			),
			LegalSection(
				heading = "10. Contact",
				body = "For any privacy-related question, open an issue on the GitHub repository: $GITHUB_URL",
			),
		),
	)

	val TermsOfService = LegalDocument(
		title = "Terms of Service",
		lastUpdated = "Last updated: July 11, 2026",
		sections = listOf(
			LegalSection(
				heading = null,
				body = "These terms govern your use of Sesterzo, a free and open-source budgeting application. " +
					"By creating an account or using the application, you agree to these terms.",
			),
			LegalSection(
				heading = "1. The service",
				body = "Sesterzo is a personal budgeting tool that lets you track income, expenses, and savings, " +
					"and optionally share budgets with other people. It is offered free of charge, as a personal " +
					"project made available to others in the spirit of open source.",
			),
			LegalSection(
				heading = "2. No warranty",
				body = "Sesterzo is provided \"as is\" and \"as available\", without warranty of any kind, express " +
					"or implied, including but not limited to the warranties of merchantability, fitness for a " +
					"particular purpose, and non-infringement, consistent with sections 15 and 16 of the GNU " +
					"General Public License v3.0 under which the software is released.\n\n" +
					"In particular, there is no guarantee of continuous availability of the service or its " +
					"servers, preservation or integrity of stored data, or freedom from bugs or errors.\n\n" +
					"You are encouraged to keep your own backups of your encryption key and recovery sentence, " +
					"and not to rely on Sesterzo as your only record of financial information.",
			),
			LegalSection(
				heading = "3. Limitation of liability",
				body = "To the maximum extent permitted by applicable law, the maintainer shall not be liable for " +
					"any damages arising from the use of, or inability to use, the application — including loss " +
					"of data, loss of profits, or any indirect, incidental, special, or consequential damages — " +
					"even if advised of the possibility of such damages.",
			),
			LegalSection(
				heading = "4. Your responsibilities",
				body = "• Keep your keys safe. Your financial data is end-to-end encrypted with keys only you " +
					"hold. If you lose your private key and your recovery sentence, your data is permanently " +
					"unrecoverable — by design, no one can restore it for you.\n" +
					"• Keep your email account secure. Authentication relies on one-time codes sent to your email " +
					"address; anyone with access to your mailbox could access your account.\n" +
					"• Provide a valid email address you control.\n" +
					"• Use the service only for lawful purposes.",
			),
			LegalSection(
				heading = "5. Acceptable use",
				body = "You agree not to:\n" +
					"• attempt to gain unauthorized access to other users' accounts or data;\n" +
					"• disrupt, overload, or interfere with the operation of the service;\n" +
					"• use the service to store or distribute unlawful content;\n" +
					"• abuse the registration or authentication systems (e.g. automated account creation).\n\n" +
					"Accounts involved in abuse may be suspended or removed.",
			),
			LegalSection(
				heading = "6. Your data",
				body = "You retain all rights to the data you store in Sesterzo. Financial data is end-to-end " +
					"encrypted and cannot be read by the maintainer. You can stop using the service and request " +
					"deletion of your account at any time (see the Privacy Policy).",
			),
			LegalSection(
				heading = "7. Open source",
				body = "Sesterzo's source code is available at $GITHUB_URL under the GNU General Public License " +
					"v3.0. You are free to inspect the code, contribute, and self-host your own instance under " +
					"the terms of that license. These Terms of Service apply to the hosted service, not to " +
					"self-hosted instances.",
			),
			LegalSection(
				heading = "8. Changes to the service and to these terms",
				body = "The service — offered for free — may change, be interrupted, or be discontinued at any " +
					"time. These terms may be updated from time to time; material changes will be announced in " +
					"the application and reflected in the \"Last updated\" date above. Continued use of the " +
					"service after a change constitutes acceptance of the updated terms.",
			),
			LegalSection(
				heading = "9. Contact",
				body = "For any question about these terms, open an issue on the GitHub repository: $GITHUB_URL",
			),
		),
	)
}