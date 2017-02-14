package com.github.catstiger.mvc.resovler;

import org.springframework.stereotype.Service;

import com.github.catstiger.mvc.annotation.API;
import com.github.catstiger.mvc.annotation.Domain;
import com.github.catstiger.mvc.model.Band;

@Service
@Domain("/")
public class RootService {
  
  @API("index")
  public Band index() {
    return new Band();
  }
}
