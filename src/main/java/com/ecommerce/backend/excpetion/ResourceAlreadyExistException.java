package com.ecommerce.backend.excpetion;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistException extends RuntimeException{

    private String resourceName;

    public ResourceAlreadyExistException(String resourceName){
        super(String.format("%s is already present",resourceName));
        this.resourceName = resourceName;
    }

}
