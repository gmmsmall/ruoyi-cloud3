package com.ruoyi.common.exception;

public class EmptyException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    protected final String message;

    public EmptyException(String message)
    {
        this.message = message;
    }

    public EmptyException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}