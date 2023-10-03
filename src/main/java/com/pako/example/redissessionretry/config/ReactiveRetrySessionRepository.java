package com.pako.example.redissessionretry.config;

import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ReactiveRetrySessionRepository implements ReactiveSessionRepository {
    ReactiveSessionRepository delegate;
    Retry retrySpec;

    public ReactiveRetrySessionRepository(ReactiveSessionRepository delegate, Retry retrySpec) {
        this.delegate = delegate;
        this.retrySpec = retrySpec;
    }

    @Override
    public Mono createSession() {
        return delegate.createSession();
    }

    @Override
    public Mono<Void> save(Session session) {
        return delegate.save(session).retryWhen(retrySpec);
    }

    @Override
    public Mono findById(String id) {
        return delegate.findById(id).retryWhen(retrySpec);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return delegate.deleteById(id).retryWhen(retrySpec);
    }
}
