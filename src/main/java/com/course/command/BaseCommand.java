package com.course.command;

public abstract class BaseCommand<T> {
    abstract protected T doExecute();
}
