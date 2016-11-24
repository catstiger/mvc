# mvc
一个不超过5000行代码的，快速，简单，易用的MVC框架。

<div>
<h1>我们的目的是：</h1>
    <ul>
        <li>让MVC回归其最初的目的。</li>
        <li>因简单而快速，超过目前各种主流MVC。</li>
        <li>零侵入，零配置，易于测试，并且<b>让开发者感觉不到MVC的存在</b></li>
        <li>可以单独使用，也可以和Spring整合使用。</li>
        <li>飞快的json解析,也支持jsp和freemarker(推荐)。</li>
    </ul>
<h1>我们的目的不是：</h1>
    <ul>
         <li>支持各种标准，各种用法，20+ Annotations, 超强扩展.... SpringMVC做的足够了。</li>
         <li>支持Multipart, 上传这样的需求应该是单独的、通用的，而不是和业务混在一起，邮箱用过吧...</li>
         <li>支持验证，简单的非空验证远远不能满足实际需要。验证应该交给业务逻辑来做，而不是框架。</li>
    </ul>
</div>
<div>
<strong>Getting Started</strong>

<ol>
   <li>
       在您的POM中加入一个repository:<br>
       
       <repository>
				 <id>Honqun Nexus</id>
				 <name>Honqun Repository</name>
				 <url>http://115.28.55.60:8081/nexus/content/groups/public/</url>
				 <snapshots>
				 	<enabled>false</enabled>
				 </snapshots>
			 </repository>
			
   </li>
   <li>
       加入依赖 <br>
       
       <dependency>
			     <groupId>catstiger.github.com</groupId>
			     <artifactId>catstiger-mvc</artifactId>
			     <version>0.1</version>
			 </dependency>
   </li>
   
   <li>
      配置web.xml,加入MvcFilter:<br>
      
      <filter>
          <filter-name>MvcFilter</filter-name>
          <filter-class>com.github.catstiger.mvc.MvcFilter</filter-class>
          <init-param>
              <!--您的类在哪个package下?类似spring 的component scan-->
              <param-name>basePackage</param-name>
              <param-value>org.honqun</param-value>
          </init-param>
      </filter>
      <filter-mapping>
          <filter-name>MvcFilter</filter-name>
          <url-pattern>/*</url-pattern>
      </filter-mapping>
      
   <li>
   <li>
       写一个Service，哦，Controller，随便吧<br>
       @Domain //这个标注表示这个类可以响应HTTP请求，URL前缀为/user_service
       public class UserService {
           /**
           * @API 这个标注，表示这个方法可以响应HTTP请求，URL为/user_service/create,
             如果URL扩展名为空或者.json，则渲染JSON数据；如果为.do,.html,.htm，则重定向到/WEB-INF/wiews/user_service/create.jsp或者create.ftl
             优先使用ftl
           */
           @API
           public User create(@Param("id") Long id, @Param("name") String name) {
               User user = new User();
               user.setId(id);
               user.setName(name);
               
               return user;
           }
       }
   <li>
</ol>
</div>
