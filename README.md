# Springer
Intellij IDEA plugin for creating template code of Spring web applications
***

# Table of contents 
***
1. [Download](#Download)
2. [Dependencies](#Dependencies)
3. [How to use](#How to use)

## Download
***
You can get it from Intellij IDEA Plugin Marketplace by writing **springer** and clicking **install** button 
or else by manually downloading it from [Jetbrains site](https://plugins.jetbrains.com/plugin/16910-springer)

## Dependencies
***
Basic (minimum) dependencies needed to use this plugin
* [Spring boot data jpa](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
* [Spring boot web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)

## How to use
***
1. Choose annotated with `@Entity` class with `@Id` field. 
   In other case generating would not work.
2. Click **Alt  + Insert** (on Windows OS) or **Code** button and then
   press on **Springer Codegen** .
3. Now you see a window with classes which you can implement and packages
   where you want to implement them.
4. Then you can either manually write package destination or choose it with relevant button.
5. If you choose to create all available classes, you can implement its methods by **Generate methods** button. 
6. Then you got opportunity to implement specific REST methods:
   * GET
   * POST
   * PUT
   * DELETE
