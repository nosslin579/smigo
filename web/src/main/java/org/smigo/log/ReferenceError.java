package org.smigo.log;

class ReferenceError {
    private String message;
    private String stack;
    private String cause;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ReferenceError{" +
                "message='" + message + '\'' +
                ", stack='" + stack + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
