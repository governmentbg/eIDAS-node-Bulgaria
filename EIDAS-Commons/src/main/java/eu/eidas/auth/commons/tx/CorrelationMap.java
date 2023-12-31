/* 
#   Copyright (c) 2017 European Commission  
#   Licensed under the EUPL, Version 1.2 or – as soon they will be 
#   approved by the European Commission - subsequent versions of the 
#    EUPL (the "Licence"); 
#    You may not use this work except in compliance with the Licence. 
#    You may obtain a copy of the Licence at: 
#    * https://joinup.ec.europa.eu/page/eupl-text-11-12  
#    *
#    Unless required by applicable law or agreed to in writing, software 
#    distributed under the Licence is distributed on an "AS IS" basis, 
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
#    See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.eidas.auth.commons.tx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a correlation between 2 objects based on the id of the first object (called the correlation id).
 * <p>
 * It can be used to represent a transaction between the ServiceProvider and the eIDAS Connector.
 * <p>
 * This interface can be used to correlate incoming requests sent by the ServiceProvider to asynchronous responses
 * received from eIDAS ProxyServices.
 * <p>
 * The implementations should typically be based on a thread-safe map where the keys are the IDs of the SAML requests
 * generated by the eIDAS connector from the ServiceProvider requests and the values are these ServiceProvider requests
 * themselves.
 *
 * @param <T> the type of the object being stored in the CorrelationMap (e.g. a request).
 * @since 1.1
 */
public interface CorrelationMap<T> {

    /**
     * Retrieves the object stored under the given id.
     *
     * @param id the id
     * @return the object stored in the CorrelationMap for the given id or {@code null} if the id was not present or
     * expired.
     */
    @Nullable
    T get(@Nonnull String id);

    /**
     * Stores the given value under the given id in the CorrelationMap.
     *
     * @param id the id
     * @param value the object to store in the CorrelationMap for the given id, cannot be {@code null}.
     * @return the previous mapping, if any or {@code null} otherwise.
     */
    @Nullable
    T put(@Nonnull String id, @Nonnull T value);

    /**
     * Deletes the mapping for the given id if one existed.
     *
     * @param id the id
     * @return the existing mapping, if any or {@code null} otherwise.
     */
    @Nullable
    T remove(@Nonnull String id);
}
