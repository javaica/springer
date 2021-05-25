package com.github.javaica.springer.model;

public enum CodegenElementType {
    MODEL {
        @Override
        public String toString() {
            return "Model";
        }
    },
    REPOSITORY {
        @Override
        public String toString() {
            return "Repository";
        }
    },
    SERVICE {
        @Override
        public String toString() {
            return "Service";
        }
    },
    CONTROLLER {
        @Override
        public String toString() {
            return "Controller";
        }
    }
}
