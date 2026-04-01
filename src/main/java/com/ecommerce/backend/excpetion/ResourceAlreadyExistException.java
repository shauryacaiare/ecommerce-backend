package com.ecommerce.backend.excpetion;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistException extends RuntimeException{

    private String resourceName;
    private Object resourceValue;

    public ResourceAlreadyExistException(String resourceName){
        super(String.format("%s is already present",resourceName));
        this.resourceName = resourceName;
    }

    public ResourceAlreadyExistException(String resourceName,Object resourceValue){
        super(String.format("%s is already present. value %s",resourceName,resourceValue));
        this.resourceName = resourceName;
        this.resourceValue = resourceValue;
    }

}
