package com.github.javaica.springer.model;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class GeneratorParams {

    GeneratorElementOptions modelOptions;
    GeneratorElementOptions repositoryOptions;
    GeneratorElementOptions serviceOptions;
    GeneratorElementOptions controllerOptions;

    public Optional<GeneratorElementOptions> getModelOptions() {
        return Optional.ofNullable(modelOptions);
    }

    public Optional<GeneratorElementOptions> getRepositoryOptions() {
        return Optional.ofNullable(repositoryOptions);
    }

    public Optional<GeneratorElementOptions> getServiceOptions() {
        return Optional.ofNullable(serviceOptions);
    }

    public Optional<GeneratorElementOptions> getControllerOptions() {
        return Optional.ofNullable(controllerOptions);
    }

    public boolean shouldGenerateModel() {
        return modelOptions != null;
    }

    public boolean shouldGenerateRepository() {
        return repositoryOptions != null;
    }

    public boolean shouldGenerateService() {
        return serviceOptions != null;
    }

    public boolean shouldGenerateController() {
        return controllerOptions != null;
    }
}
