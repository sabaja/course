package com.course.command;

public abstract class BaseCommand<T, R> {
    protected abstract T doExecute();

    protected abstract void init(R element);
}
