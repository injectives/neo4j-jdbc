/*
 * Copyright (c) 2023-2024 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.jdbc.internal.bolt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.neo4j.driver.jdbc.internal.bolt.internal.SecurityPlanImpl;

public final class SecurityPlans {

	private SecurityPlans() {
	}

	public static SecurityPlan insecure() {
		return new SecurityPlanImpl(false, null, false, RevocationCheckingStrategy.NO_CHECKS);
	}

	public static SecurityPlan forSystemCASignedCertificates(boolean requiresHostnameVerification,
			RevocationCheckingStrategy revocationCheckingStrategy) throws GeneralSecurityException, IOException {
		var sslContext = configureSSLContext(Collections.emptyList(), revocationCheckingStrategy);
		return new SecurityPlanImpl(true, sslContext, requiresHostnameVerification, revocationCheckingStrategy);
	}

	private static SSLContext configureSSLContext(List<File> customCertFiles,
			RevocationCheckingStrategy revocationCheckingStrategy) throws GeneralSecurityException, IOException {
		var trustedKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustedKeyStore.load(null, null);

		if (!customCertFiles.isEmpty()) {
			// Certificate files are specified, so we will load the certificates in the
			// file
			loadX509Cert(customCertFiles, trustedKeyStore);
		}
		else {
			loadSystemCertificates(trustedKeyStore);
		}

		var pkixBuilderParameters = configurePKIXBuilderParameters(trustedKeyStore, revocationCheckingStrategy);

		var sslContext = SSLContext.getInstance("TLS");
		var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

		if (pkixBuilderParameters == null) {
			trustManagerFactory.init(trustedKeyStore);
		}
		else {
			trustManagerFactory.init(new CertPathTrustManagerParameters(pkixBuilderParameters));
		}

		sslContext.init(new KeyManager[0], trustManagerFactory.getTrustManagers(), null);

		return sslContext;
	}

	private static void loadSystemCertificates(KeyStore trustedKeyStore) throws GeneralSecurityException {
		// To customize the PKIXParameters we need to get hold of the default KeyStore, no
		// other elegant way available
		var tempFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tempFactory.init((KeyStore) null);

		// Get hold of the default trust manager
		var x509TrustManager = (X509TrustManager) Arrays.stream(tempFactory.getTrustManagers())
			.filter(X509TrustManager.class::isInstance)
			.findFirst()
			.orElse(null);

		if (x509TrustManager == null) {
			throw new CertificateException("No system certificates found");
		}
		else {
			// load system default certificates into KeyStore
			loadX509Cert(x509TrustManager.getAcceptedIssuers(), trustedKeyStore);
		}
	}

	public static void loadX509Cert(X509Certificate[] certificates, KeyStore keyStore) throws GeneralSecurityException {
		for (var i = 0; i < certificates.length; i++) {
			loadX509Cert(certificates[i], "neo4j.javadriver.trustedcert." + i, keyStore);
		}
	}

	public static void loadX509Cert(Certificate cert, String certAlias, KeyStore keyStore) throws KeyStoreException {
		keyStore.setCertificateEntry(certAlias, cert);
	}

	private static PKIXBuilderParameters configurePKIXBuilderParameters(KeyStore trustedKeyStore,
			RevocationCheckingStrategy revocationCheckingStrategy)
			throws InvalidAlgorithmParameterException, KeyStoreException {
		PKIXBuilderParameters pkixBuilderParameters = null;

		if (RevocationCheckingStrategy.requiresRevocationChecking(revocationCheckingStrategy)) {
			// Configure certificate revocation checking (X509CertSelector() selects all
			// certificates)
			pkixBuilderParameters = new PKIXBuilderParameters(trustedKeyStore, new X509CertSelector());

			// sets checking of stapled ocsp response
			pkixBuilderParameters.setRevocationEnabled(true);

			// enables status_request extension in client hello
			System.setProperty("jdk.tls.client.enableStatusRequestExtension", "true");

			if (revocationCheckingStrategy.equals(RevocationCheckingStrategy.VERIFY_IF_PRESENT)) {
				// enables soft-fail behaviour if no stapled response found.
				Security.setProperty("ocsp.enable", "true");
			}
		}
		return pkixBuilderParameters;
	}

	public static void loadX509Cert(List<File> certFiles, KeyStore keyStore)
			throws GeneralSecurityException, IOException {
		var certCount = 0; // The files might contain multiple certs
		for (var certFile : certFiles) {
			try (var inputStream = new BufferedInputStream(new FileInputStream(certFile))) {
				var certFactory = CertificateFactory.getInstance("X.509");

				while (inputStream.available() > 0) {
					try {
						var cert = certFactory.generateCertificate(inputStream);
						certCount++;
						loadX509Cert(cert, "neo4j.javadriver.trustedcert." + certCount, keyStore);
					}
					catch (CertificateException ex) {
						if (ex.getCause() != null && ex.getCause().getMessage().equals("Empty input")) {
							// This happens if there is whitespace at the end of the
							// certificate - we load one cert, and
							// then try and load a
							// second cert, at which point we fail
							return;
						}
						throw new IOException("Failed to load certificate from `" + certFile.getAbsolutePath() + "`: "
								+ certCount + " : " + ex.getMessage(), ex);
					}
				}
			}
		}
	}

}
