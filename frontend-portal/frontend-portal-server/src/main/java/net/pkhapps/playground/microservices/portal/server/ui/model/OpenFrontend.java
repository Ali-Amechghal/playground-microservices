package net.pkhapps.playground.microservices.portal.server.ui.model;

import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * TODO Document me
 */
public class OpenFrontend implements Serializable {

    private final UUID uuid;
    private final FrontendInstanceDescriptor instance;
    private final FrontendDescriptor frontend;
    private final Origin origin;
    private final Set<MessageListener> messageListeners = new HashSet<>();
    private final Set<NotificationListener> notificationListeners = new HashSet<>();

    public OpenFrontend(FrontendInstanceDescriptor instance, FrontendDescriptor frontend) {
        this.uuid = UUID.randomUUID();
        this.instance = Objects.requireNonNull(instance, "instance must not be null");
        this.frontend = Objects.requireNonNull(frontend, "frontend must not be null");
        if (!instance.getResourceId().equals(frontend.getId())) {
            throw new IllegalArgumentException("Instance has different resource ID than frontend");
        }
        this.origin = new Origin(instance.getClientUri());
    }

    public UUID getUuid() {
        return uuid;
    }

    public FrontendInstanceDescriptor getInstance() {
        return instance;
    }

    public FrontendDescriptor getFrontend() {
        return frontend;
    }

    /**
     * Returns the origin of the frontend. This is used to make sure that messages sent from the portal to the frontend
     * arrive at the correct destination.
     */
    public Origin getOrigin() {
        return origin;
    }

    public void notifyUser() {
        Set.copyOf(notificationListeners).forEach(NotificationListener::onNotification);
    }

    public void sendMessage(FrontendId sender, JsonValue message) {
        Objects.requireNonNull(sender, "sender must not be null");
        Objects.requireNonNull(message, "message must not be null");
        Set.copyOf(messageListeners).forEach(listener -> listener.onMessage(sender, message));
    }

    public Registration addMessageListener(MessageListener messageListener) {
        messageListeners.add(Objects.requireNonNull(messageListener, "messageListener must not be null"));
        return () -> messageListeners.remove(messageListener);
    }

    public Registration addNotificationListener(NotificationListener notificationListener) {
        notificationListeners.add(Objects.requireNonNull(notificationListener, "notificationListener must not be null"));
        return () -> notificationListeners.remove(notificationListener);
    }

    @FunctionalInterface
    public interface MessageListener extends Serializable {
        void onMessage(FrontendId sender, JsonValue message);
    }

    @FunctionalInterface
    public interface NotificationListener extends Serializable {
        void onNotification();
    }

    @Override
    public String toString() {
        return String.format("%s(uuid: [%s], instance: [%s]", getClass().getSimpleName(), uuid, instance);
    }
}
