package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.PrivateKey;

/**
 * A service instance registration is a request to register a service instance with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class ServiceInstanceRegistration extends ResourceInstanceRegistration<ServiceId, ServiceInstanceDescriptor> {

    /**
     * Creates a new service instance registration.
     *
     * @param descriptor the service instance descriptor.
     * @param privateKey the private key to use when creating the digital signature. The private key will not be stored
     *                   anywhere.
     */
    public ServiceInstanceRegistration(ServiceInstanceDescriptor descriptor, PrivateKey privateKey) {
        super(descriptor, privateKey);
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    ServiceInstanceRegistration(@JsonProperty(value = "descriptor", required = true) ServiceInstanceDescriptor descriptor,
                                @JsonProperty(value = "algorithm", required = true) String algorithm,
                                @JsonProperty(value = "signature", required = true) String signature) {
        super(descriptor, algorithm, signature);
    }
}
