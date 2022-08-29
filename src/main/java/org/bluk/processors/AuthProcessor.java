package org.bluk.processors;

import org.bluk.AuthPlugin;
import org.bluk.entities.PlayerConnection;
import org.bluk.processors.types.AbstractProcessor;
import org.bluk.processors.types.OnStateCallback;
import org.bluk.processors.types.ProcessorState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AuthProcessor implements AbstractProcessor {
    public AuthProcessor(PlayerConnection connection) {
        this.connection = connection;

        AuthPlugin.getInstance().getLogger().log(Level.INFO, "Start AuthProcessor");

        this.state = ProcessorState.DONE;
        this.callCallback(ProcessorState.DONE, new Response("test-token"));

        AuthPlugin.getInstance().getLogger().log(Level.INFO, "Auth processor done");
    };

    @Override
    public AuthProcessor onState(ProcessorState state, OnStateCallback callback) {
        // Creating new ArrayList (if not exists)
        this.callbacks.computeIfAbsent(state, k -> new ArrayList<>());

        // Adding this callback
        this.callbacks.get(state).add(callback);

        return this;
    }

    @Override
    public void stop() {};

    private void callCallback(ProcessorState state) {
        if (this.callbacks.get(state) != null) {
            this.callbacks.get(state).forEach(callback -> {
                callback.call(callback.arguments);
            });
        };
    };

    private void callCallback(ProcessorState state, Response response) {
        if (this.callbacks.get(state) != null) {
            this.callbacks.get(state).forEach(callback -> {
                callback.call(callback.arguments, response);
            });
        };
    };

    // AuthProcessor properties
    private final PlayerConnection connection;
    private final Map<ProcessorState, List<OnStateCallback>> callbacks = new HashMap<>();

    // AbstractProcessor properties
    private ProcessorState state;
    public ProcessorState getState() { return state; }
    public Boolean isProcessing() { return state == ProcessorState.PROCESSING; }

    // Processor response
    public static class Response {
        public Response(String token) {
            this.token = token;
        }

        public String token;
    }
}
