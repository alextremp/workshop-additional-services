package com.github.alextremp.loadrules.application;

public interface UseCase<I, O> {

  O execute(I request);
}
