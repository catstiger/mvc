package com.github.catstiger.mvc.config;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.github.catstiger.mvc.annotation.Api;
import com.github.catstiger.mvc.service.ServiceProvider;

import strman.Strman;
@Component
public final class Initializer {
  private static Logger logger = LoggerFactory.getLogger(Initializer.class);
  /**
   * MvcServlet初始化的时候，需要配置scan_package,用于定义Service扫描basePackage
   */
  public static final String INIT_PARAM_BASE_PACKAGE = "basePackage";
  public static final String DEFAULT_BASE_PACKAGE = "service";

  public Initializer() {

  }
  /**
   * 扫描给定package下的所有类，找出提供Rest服务的类，将之对应的URL，类名，服务ID等信息保存在ApiResHolder中
   * @param basePackage 给出base package,如果为空字符串或者<code>null</code>，则采用缺省包名{@value #DEFAULT_BASE_PACKAGE}
   */
  public void loadApiResources(String basePackage) {
    if (StringUtils.isBlank(basePackage)) {
      basePackage = DEFAULT_BASE_PACKAGE;
    }
    logger.debug("Base package {}", basePackage);
    
    Set<Class<?>> apiClasses = getClasses(basePackage);
    for(Class<?> clazz : apiClasses) {
      List<ApiResource> apiReses = extractResources(clazz);
      for(ApiResource apiRes : apiReses) {
        ApiResHolder.getInstance().add(apiRes);
      }
    }
  }
  
  private List<ApiResource> extractResources(Class<?> clazz) {
    if(clazz == null) {
      return Collections.emptyList();
    }
    Api classApiAnn = clazz.getAnnotation(Api.class);  
    if(classApiAnn == null) {
      return Collections.emptyList();
    }
    
    String serviceId = getServiceId(clazz); //服务ID，Spring注解的beanId或者全限定类名
    if(StringUtils.isBlank(serviceId)) {
      return Collections.emptyList();
    }
    
    Boolean isSingleton = (!isSpringBean(clazz) && classApiAnn.singleton());
    String uriPrefix = classApiAnn.value(); //如果Aai定义了URI前缀，那么使用Api定义的
    
    if(StringUtils.isBlank(uriPrefix)) { //如果没有定义，则根据serviceId或者类名确定URL
      if(isSpringBean(clazz)) {
        uriPrefix = "/" + Strman.toSnakeCase(serviceId.replaceAll("\\\\|/", " "));
      } else {
        uriPrefix = "/" + Strman.toSnakeCase(clazz.getSimpleName());
      }
    }
    //去除结尾的/
    if(uriPrefix.endsWith("/")) {
      uriPrefix = Strman.removeRight(uriPrefix, "/");
    }
    
    Method[] methods = clazz.getMethods();
    List<ApiResource> apiReses = new ArrayList<ApiResource>(methods.length);
    
    for(Method method : methods) {
      Api methodApiAnn = method.getAnnotation(Api.class);
      if(methodApiAnn == null) {
        continue;
      }
      
      String uriSufix = classApiAnn.value(); //如果Aai定义了URI后缀，那么使用Api定义的
      if(StringUtils.isBlank(uriSufix)) {
        uriSufix = "/" + Strman.toSnakeCase(method.getName());
      }
      if(!uriSufix.startsWith("/")) {
        uriSufix = "/" + uriSufix;
      }
      if(uriSufix.endsWith("/")) {
        uriSufix = Strman.removeRight(uriSufix, "/");
      }
      
      ApiResource apiRes = new ApiResource();
      apiRes.setMethod(method);
      apiRes.setMethodName(method.getName());
      apiRes.setProviderType((isSpringBean(clazz)) ? ServiceProvider.SERVICE_PROVIDER_SPRING : ServiceProvider.SERVICE_PROVIDER_CREATE);
      apiRes.setServiceId(serviceId);
      apiRes.setSingleton(isSingleton);
      apiRes.setUriPrefix(uriPrefix);
      apiRes.setUriSufix(uriSufix);
      apiRes.setUri(uriPrefix + uriSufix);
      
      apiReses.add(apiRes);
    }
    
    return apiReses;
  }
  
  public static void main(String[] args) {
    System.out.println(Strman.toSnakeCase("userName"));
    System.out.println(Strman.toSnakeCase("user name"));
    System.out.println(Strman.toSnakeCase("user/name\\sam".replaceAll("\\\\|/", " ")));
  }
  
  
  private Boolean isSpringBean(Class<?> clazz) {
    return (clazz != null && (clazz.isAnnotationPresent(Component.class) ||clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class)));
  }
  
  private String getServiceId(Class<?> clazz) {
    String serviceId = null;
    if(clazz.isAnnotationPresent(Component.class)) {
      Component comAnn = clazz.getAnnotation(Component.class);
      serviceId = comAnn.value();
      if(StringUtils.isBlank(serviceId)) {
        serviceId = Strman.toCamelCase(clazz.getSimpleName());
      }
    } else if (clazz.isAnnotationPresent(Service.class)) {
      Service svrAnn = clazz.getAnnotation(Service.class);
      serviceId = svrAnn.value();
      if(StringUtils.isBlank(serviceId)) {
        serviceId = Strman.toCamelCase(clazz.getSimpleName());
      }
    } else if (clazz.isAnnotationPresent(Repository.class)) {
      Repository respAnn = clazz.getAnnotation(Repository.class);
      serviceId = respAnn.value();
      if(StringUtils.isBlank(serviceId)) {
        serviceId = Strman.toCamelCase(clazz.getSimpleName());
      }
    }
    
    if(null == serviceId) {
      serviceId = clazz.getName();
    }
    
    return serviceId;
  }

  private static Set<Class<?>> getClasses(String pack) {
    // 第一个class类的集合
    Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
    // 是否循环迭代
    boolean recursive = true;
    // 获取包的名字 并进行替换
    String packageName = pack;
    String packageDirName = packageName.replace('.', '/');
    // 定义一个枚举的集合 并进行循环来处理这个目录下的things
    Enumeration<URL> dirs;
    try {
      dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
      // 循环迭代下去
      while (dirs.hasMoreElements()) {
        // 获取下一个元素
        URL url = dirs.nextElement();
        // 得到协议的名称
        String protocol = url.getProtocol();
        // 如果是以文件的形式保存在服务器上
        if ("file".equals(protocol)) {
          // 获取包的物理路径
          String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
          // 以文件的方式扫描整个包下的文件 并添加到集合中
          findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
        } else if ("jar".equals(protocol)) {
          logger.debug("Scan jar file {}", url);
          // 如果是jar包文件
          JarFile jar;
          try {
            // 获取jar
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            // 从此jar包 得到一个枚举类
            Enumeration<JarEntry> entries = jar.entries();
            // 同样的进行循环迭代
            while (entries.hasMoreElements()) {
              // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
              JarEntry entry = entries.nextElement();
              String name = entry.getName();
              // 如果是以/开头的
              if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
              }
              // 如果前半部分和定义的包名相同
              if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if (idx != -1) {
                  packageName = name.substring(0, idx).replace('/', '.');
                }
                // 如果可以迭代下去 并且是一个包
                if ((idx != -1) || recursive) {
                  // 如果是一个.class文件 而且不是目录
                  if (name.endsWith(".class") && !entry.isDirectory()) {
                    // 去掉后面的".class" 获取真正的类名
                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                    try {
                      // 添加到classes
                      Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                      if(isApi(clazz)) {
                        logger.debug("Service found [{}]", clazz.getSimpleName());
                        classes.add(clazz);
                      }
                    } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                    }
                  }
                }
              }
            }
          } catch (IOException e) {
            // log.error("在扫描用户定义视图时从jar包获取文件出错");
            e.printStackTrace();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classes;
  }

  /**
   * 以文件的形式来获取包下的所有Class
   * 
   */
  private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
    // 获取此包的目录 建立一个File
    File dir = new File(packagePath);
    // 如果不存在或者 也不是目录就直接返回
    if (!dir.exists() || !dir.isDirectory()) {
      // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
      return;
    }
    // 如果存在 就获取包下的所有文件 包括目录
    File[] dirfiles = dir.listFiles(new FileFilter() {
      // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
      public boolean accept(File file) {
        return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
      }
    });
    // 循环所有文件
    for (File file : dirfiles) {
      // 如果是目录 则继续扫描
      if (file.isDirectory()) {
        findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
      } else {
        // 如果是java类文件 去掉后面的.class 只留下类名
        String className = file.getName().substring(0, file.getName().length() - 6);
        try {
          // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
          Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
          if(isApi(clazz)) {
            logger.debug("Service found [{}]", clazz.getSimpleName());
            classes.add(clazz);
          }
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private static Boolean isApi(Class<?> clazz) {
    if(clazz == null) {
      return null;
    }
    Api apiAnn = clazz.getAnnotation(Api.class);
    return apiAnn != null;
  }

}
