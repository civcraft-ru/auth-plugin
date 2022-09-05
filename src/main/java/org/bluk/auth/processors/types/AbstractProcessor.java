package org.bluk.auth.processors.types;

public interface AbstractProcessor {
    ProcessorState getState();
    Boolean isProcessing();
    void stop();
    AbstractProcessor onState(ProcessorState state, OnStateCallback callback);
}
