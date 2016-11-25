一个不超过5000行代码的，快速，简单，易用的MVC框架。
===========================================================================================

我们的目的是：
--------------------------------------------------------------------------------------------
* 让MVC回归其最初的目的。
* 因简单而快速，超过目前各种主流MVC。
* 零侵入，零配置，易于测试，并且`让开发者感觉不到MVC的存在`
* 可以单独使用，也可以和Spring整合使用。
* 飞快的json解析,也支持jsp和freemarker(推荐)。

我们的目的不是：
--------------------------------------------------------------------------------------------
* 各种标准，各种用法，20+ Annotations, 超强扩展.... SpringMVC做的足够了。
* Multipart, 上传这样的需求应该是单独的、通用的，而不是和业务混在一起，邮箱用过吧...
* 验证，简单的非空验证远远不能满足实际需要,复杂的验证框架做不到。验证应该交给业务逻辑来做，而不是框架。

Getting Started
--------------------------------------------------------------------------------------------
在此之前，请准备好：
* Java JDK 8
* Maven3

1.在您的POM中加入一个repository:
```XML
       <repository>
        <id>Honqun Nexus 3dp</id>
        <name>Honqun Repository</name>
        <url>http://115.28.55.60:8081/nexus/content/repositories/thirdparty/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
```		
2.POM中加入依赖 
```XML       
    <dependency>
		  <groupId>com.github.catstiger</groupId>
		  <artifactId>catstiger-mvc</artifactId>
		  <version>0.1</version>
		</dependency>
```
3.配置web.xml,加入MvcFilter:
```XML
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
```
4.写一个Service，哦，Controller，随便吧
```Java       
       @Domain //这个标注表示这个类可以响应HTTP请求，URL前缀为/user_service
       public class UserService {
           /**
           * 连同类对应的URL，访问这个方法的URL为/user_service/create
           */
           @API
           public User create(@Param("id") Long id, @Param("name") String name) {
               return null;
           }
       }
```
5.在浏览器中访问：http://IP:PORT/user_service/create？id=1试试吧。
  
手册
-------------------------------------------------------------------------------------
### URL映射规则
		类用`@Domain`标注，方法用`@API`标注，只有这样，方法才能响应一个URL。
* 被`@Domain`的类对应的URL为`类名(驼峰命名)小写，单词之间用_分隔`，例如：
 
 ```Java
 //这个类的URL前缀为/employee_join_service
 @Param 
 public class EmployeeJoinService{}
 ```
* 被`@API`标注的函数，对应的URL为：`类名URL + / + 方法URL`，方法URL为方法名小写单词之间用_分隔。例如：
 
 ```Java
 @Domain
 public class EmployeeJoinService{
    //访问这个方法的URL为/employee_join_service/join_on
    @API
    public Employee joinOn(@Param("emp") Employee emp) {
    }
 }
 ```
* `@Domain`和`@API`都可以自定义URL，他们的value参数用于覆盖缺省的URL规则：
```Java
 @Domain("/emp")
 public class EmployeeJoinService{
    //访问这个方法的URL为/emp/join
    @API("join")
    public Employee joinOn(@Param("emp") Employee emp) {
    }
 }
 ```

### 输入参数转换
* 能够自动从HTTP参数转换成各种常用的Java数据类型：
	* Primitive类型及其Wrapper
	* String
	* BigDecimal
	* BigInteger
	* java.util.Date,java.sql.Date,格式可以在web.xml中配置用inti-param配置（dateFormat）
	* 普通Java Bean
	* 由上述数据类型组成的数组。
	* java.util.List,java.util.Set,必须参数化

* 用@Param标注参数，可以自定义转换规则。
```Java
//MyConverterz实现了ValueConverter接口
public User queryByDate(@Param(value= "date", converter = MyConverter.class) Date date){} 
```
### 方法参数与请求参数对应关系

* 如果方法参数只有一个，并且是一个JavaBean，那么JavaBean的属性名称与parameter名称一一对应。
* 如果方法有多个参数，则需要用@Param指出参数名称和parameter名称的对应关系。
* 如果方法参数是一个JavaBean，而parameter需要与JavaBeann中的某个Beand的属性对应，则可以使用.分隔参数名称。例如：?user.dept.id=1,对应的是User对象的dept属性的id属性。
* 例如：
```Java
@Domain
public class UserService {
    /**
     * 对应的URL：/user_service/query_user?id=0&username=a&dept.name=tech
     */
    @API
    public List<User> queryUser(@Param("id") Long id, @Param("username") String username, @Param("dept") Dept dept){
    } 
}
```


### 解析响应

####缺省的解析规则（根据URL扩展名）
* URL没有扩展名或者扩展名为`.json`,直接输出JSON格式的数据。
* URL扩展名为`.text`,`.txt`, 则直接将方法执行的结果转换为字符串，渲染到HttpServletResponse中。
* URL扩展名为`.html`,`.htm`,`.do`,`.action`：
	* /WEB-INF/views/下，对应URL的目录中如果有ftl文件，则解析freemarker，并渲染到HttpServletResponse中。
	* 如果没有ftl文件，重定向d（forward）到/WEB-INF/views/下对应的jsp文件。
	* 例如：URL为/user_service/create.htm,对应的模板文件为/WEB-INF/views/user_service/create.ftl或者create.jsp
	* 可以在web.xml中配置模板文件的目录：
	```XML
	<filter>
	      <filter-name>MvcFilter</filter-name>
	      <filter-class>com.github.catstiger.mvc.MvcFilter</filter-class>
	      <init-param>
		  <param-name>pageFolder</param-name>
		  <param-value>/WEB-INF/ftl</param-value>
	      </init-param>
        </filter>
	```
####自定义解析规则
`@API`参数reolver指向一个ResponseResolver的实现类，即可实现自定义解析方式：
```Java
@API(resolver = MyResolver.class)
public User createUser(@Param("user") User model) {
}
```


