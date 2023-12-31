/*
 * Copyright (c) 2019 by European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/page/eupl-text-11-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */
package eu.eidas.auth.engine.core.impl;

import java.util.Map;

import eu.eidas.auth.engine.configuration.ProtocolEngineConfigurationException;
import eu.eidas.auth.engine.configuration.dom.KeyStoreSignatureConfigurator;
import eu.eidas.auth.engine.core.ProtocolSignerI;

import javax.annotation.Nullable;

/**
 * The base abstract class for implementations of {@link ProtocolSignerI} relying on a {@link java.security.KeyStore}.
 *
 * @since 1.1
 */
public abstract class KeyStoreProtocolSigner extends AbstractProtocolSigner {

    protected KeyStoreProtocolSigner(Map<String, String> properties, @Nullable String defaultPath) throws ProtocolEngineConfigurationException {
        super(new KeyStoreSignatureConfigurator().getSignatureConfiguration(properties, defaultPath));
    }
}
