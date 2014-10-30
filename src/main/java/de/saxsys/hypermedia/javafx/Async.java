package de.saxsys.hypermedia.javafx;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Async extends Service<Void> {

    private final Runnable async;

    public Async(Runnable async) {
        this.async = async;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                async.run();
                return null;
            }
        };
    }

}
