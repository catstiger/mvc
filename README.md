# mvc
一个不超过5000行代码的，快速，简单，易用的MVC框架。

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
      
   </li>
   <li>
       写一个Service，哦，Controller，随便吧<br>
       
       @Domain //这个标注表示这个类可以响应HTTP请求，URL前缀为/user_service
       public class UserService {
           /**
           * @API 这个标注，表示这个方法可以响应HTTP请求，URL为/user_service/create,
             如果URL扩展名为空或者.json，则渲染JSON数据；如果为.do,.html,.htm，则重定向到/WEB-INF/wiews/user_service/create.jsp或者create.ftl
             优先使用ftl；如函数返回值为一个Map或者一个JavaBean，那么jsp或者ftl中的属性名称为Map的Key或者JavaBean的property名称；如果返回值为Collection或者
             Array，则以“list”作为属性名称；如果返回值为一个原始数据类型或者Wrapper类，或者String，则以data作为属性名称。
             @Param标注标明了函数参数与Http Request参数的名称对应关系，很多情况下，我们无法获得函数参数的名称。
           */
           @API
           public User create(@Param("id") Long id, @Param("name") String name) {
               User user = new User();
               user.setId(id);
               user.setName(name);
               
               return user;
           }
       }
   </li>
   <li>
       在浏览器中访问：http://IP:PORT/user_service/create？id=1试试吧。
   </li>
</ol>
<h1>说明</h1>
<ul>
     <li>
         @Domain用于标注一个类，使得这个类中的方法可以响应HTTP请求, @Domain参数：
         <ul>
             <li>
                 value, 这个类对应的URL，缺省为空字符串"",如果是缺省值，那么URL为类名小写，单词之间用下划线"_"分隔。例如，UserService对应/user_service
             </li>
             <li>
                 singleton, 仅当这个类不是Spring管理的类的情况下使用，表示这个类是否是单例的，缺省为true.
             </li>
             <li>
                 @Domain标注可以和Spring的@Component,@Service,@Repository联合使用，URL映射规则不变，但是类的实例从WebApplicationContext中获取。目前不支持
                 在xml文件中用&lt;bean/&gt;注册的类。
             </li>
         </ul>
     </li>
     <li>
         @API用于标注一个方法，使得被@Domain标注的类中的某个方法可以响应HTTP请求，@API的参数：
         <ul>
             <li>
                 value, 这个方法对应的URL片段，缺省为空字符串“”，在此情况下，URL片段为方法名小写，单词之间用下划线"_"分隔。这个值与@Domain的value值合并起来作为一个API的访问URL地址，例如：
                 
                {}
                 
                 上面代码中join方法的访问URL为:/employee_join_service/join
             </li>
             <li>
                 resolver，用于指向一个ResponseResolver接口的实现类，这个类用于处理执行结果。缺省的，系统根据URL后缀判断如何处理：
                 <ul>
                     <li>没有后缀或者.json后缀：将处理结果转换为JSON</li>
                     <li>.do,.action,.htm,.html，重定向到/WEB-INF/views（可以在web.xml中定制）下的一个JSP或者ftl文件，系统优先寻找ftl文件，这个比JSP快多了。</li>
                     <li>.txt,.text, 直接将结果转换为字符串，通常用于AJAX方式请求一个HTML片段。</li>
                 </ul>
             </li>
         </ul>
     </li>
</ul>


