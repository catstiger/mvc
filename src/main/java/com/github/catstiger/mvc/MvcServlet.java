package com.github.catstiger.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.catstiger.mvc.config.Initializer;

public class MvcServlet extends HttpServlet {
  private static Logger logger = LoggerFactory.getLogger(MvcServlet.class);

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.debug("URL {}", request.getRequestURL());
    logger.debug("URI {}", request.getRequestURI());
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    System.out.println("Init...");
    Initializer initializer = new Initializer();
    initializer.loadApiResources(config);
  }

}
