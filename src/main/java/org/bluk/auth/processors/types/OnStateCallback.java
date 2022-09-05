package org.bluk.auth.processors.types;

public class OnStateCallback {
    public OnStateCallback(Object ...args) {
        this.arguments = args;
    };

    public final Object[] arguments;

    public void call(Object ...args) {};
}
