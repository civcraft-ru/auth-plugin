package org.bluk.processors.types;

public interface AbstractProcessor {
    ProcessorState getState();
    Boolean isProcessing();
    void stop();
    AbstractProcessor onState(ProcessorState state, OnStateCallback callback);
}
