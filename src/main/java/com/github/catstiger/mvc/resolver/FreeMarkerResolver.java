package com.github.catstiger.mvc.resolver;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.catstiger.mvc.config.ApiResource;
import com.github.catstiger.mvc.config.Initializer;
import com.github.catstiger.mvc.converter.ConverterFactory;
import com.github.catstiger.mvc.util.StringUtils;
import com.github.catstiger.mvc.util.WebUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public final class FreeMarkerResolver extends AbstractResponseResolver {
  private Configuration cfg;
  private static FreeMarkerResolver instance = null;
  
  private Map<String, Boolean> supportedUris = new ConcurrentHashMap<String, Boolean>(160);
  
  private FreeMarkerResolver() {
    Initializer init = Initializer.getInstance();
    
    cfg = new Configuration();
    cfg.setDateFormat(init.getDateFormat());
    cfg.setDateTimeFormat(init.getTimeFormat());
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateUpdateDelay(1);
    
    File root = new File(init.getRealPath() + init.getPageFolder());
    List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>(100);
    findTemplateLoaders(root, templateLoaders);
    TemplateLoader multiLoader = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[]{}));
    cfg.setTemplateLoader(multiLoader);
  }
  
  /**
   * 获取FreeMarkerResolver的实例，FreeMarkerResolver初始化Freemarker Configuration，由于Configuration对象必须是application-level 单例的
   * 因此，也确保FreeMarkerResolver是单例的。
   */
  public static FreeMarkerResolver getInstance() {
    if(instance == null) {
      instance = new FreeMarkerResolver();
    }
    
    return instance;
  }
  
  /**
   * 判断是否有处理某个API的Freemarker模板，如果有，返回true，否则返回false
   */
  public boolean isSupported(ApiResource apiResource) {
    if(supportedUris.containsKey(apiResource.getUri())) {
      return supportedUris.get(apiResource.getUri());
    }
    
    String tplName = apiResource.getUriSufix();
    if(tplName != null && tplName.startsWith("/")) {
      tplName = StringUtils.removeLeft(tplName, "/");
    }
    tplName = tplName + ".ftl";
    
    Template template;
    Boolean supported;
    try {
      template = cfg.getTemplate(tplName);
      supported = (template != null);
    } catch (Exception e) {
      supported = false;
    }
    supportedUris.put(apiResource.getUri(), supported);
    return supported;
  }
  

  @SuppressWarnings("unchecked")
  @Override
  public void resolve(HttpServletRequest request, HttpServletResponse response, ApiResource apiResource, Object model) {
    String tplName = apiResource.getUriSufix();
    if(tplName != null && tplName.startsWith("/")) {
      tplName = StringUtils.removeLeft(tplName, "/");
    }
    tplName = tplName + ".ftl";
    
    Template template;
    try {
      template = cfg.getTemplate(tplName);
    } catch (Exception e) {
      throw new RuntimeException("模板加载失败！", e);
    }
    
    Map<String, Object> dataModel = new HashMap<String, Object>();
    boolean isBean = true;
    
    if(model != null) {
      if(model instanceof Collection<?> || model.getClass().isArray()) {
        dataModel.put(ATTR_NAME_COLLECTION, model);
        isBean = false;
      } 
      else if(ConverterFactory.SIMPLE_CONVERTERS.containsKey(model.getClass())) {
        dataModel.put("data", model);
        isBean = false;
      }
      else if (model instanceof Map) { 
        dataModel = (Map<String, Object>) model;
        isBean = false;
      } 
    }
    
    String html;
    
    if(!isBean) {
      html = processTemplateIntoString(template, dataModel);
    } else {
      html = processTemplateIntoString(template, model);
    }
    
    WebUtils.render(response, html, "text/html");
  }
  
  private static String processTemplateIntoString(Template template, Object model) {

    StringWriter result = new StringWriter();
    try {
      template.process(model, result);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    } 
    return result.toString();
  }
  
  private static void findTemplateLoaders(File root, List<TemplateLoader> templateLoaders) {
    if(templateLoaders == null) {
      templateLoaders = new ArrayList<TemplateLoader>(100);
    }
    File[] folders = root.listFiles();
    for(File folder : folders) {
      if(folder != null && folder.exists() && folder.isDirectory()) {
        try {
          templateLoaders.add(new FileTemplateLoader(folder));
        } catch (IOException e) {
          e.printStackTrace();
        }
        findTemplateLoaders(folder, templateLoaders);
      }
    }
  }

}
