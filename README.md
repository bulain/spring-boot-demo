# spring boot demo

具体示例可参考spring-boot项目spring-boot-samples
> https://github.com/spring-projects/spring-boot

# 特性如下
1. 独立运行的Spring项目
2. 内嵌Servlet容器
3. 提供starter简化Maven配置
4. 自动配置Spring 
5. 准生产的应用监控 
6. 无代码生成和xml配置

# 瘦身插件配置
1. 插件配置
```xml
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.bulain.mybatis.MybatisPlusApplication</mainClass>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot.experimental</groupId>
                    <artifactId>spring-boot-thin-layout</artifactId>
                    <version>${spring-boot-thin.version}</version>
                </dependency>
            </dependencies>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot.experimental</groupId>
            <artifactId>spring-boot-thin-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>resolve</id>
                    <goals>
                        <goal>resolve</goal>
                    </goals>
                    <inherited>false</inherited>
                </execution>
            </executions>
        </plugin>
    </plugins>
```

2. 启动项目
```bash
   cd mybatis-demo/target/thin/root
   java -Dthin.root=. -jar mybatis-demo-1.0.0-SNAPSHOT.jar
```

