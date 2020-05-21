package com.ruoyi.fabric.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger常用注解列表：
 *   Api：修饰整个类，描述Controller的作用
 *   ApiOperation：描述一个类的一个方法，或者说一个接口
 *   ApiParam：单个参数描述
 *   ApiModel：用对象来接收参数
 *   ApiProperty：用对象接收参数时，描述对象的一个字段
 *   ApiResponse：HTTP响应其中1个描述
 *   ApiResponses：HTTP响应整体描述
 *   ApiIgnore：使用该注解忽略这个API
 *   ApiError ：发生错误返回的信息
 *   ApiImplicitParam：一个请求参数
 *   ApiImplicitParams：多个请求参数
 *
 * ConditionalOnProperty: 是否开启swagger，正式环境一般是需要关闭的.
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable",  havingValue = "true")
public class SwaggerConfig {

    /**
     * 配置swagger2的一些基本的内容，比如扫描的包等等
     * @return Docket
     */
    //
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mingbyte.dataex.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api文档的详细信息
     *   title : 页面标题
     *   contact : 创建人信息
     *   version : 版本号
     *   description : 描述
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mingbyte Fabric Gateway API.")  //
                .contact(new Contact("Mingbyte", "https://www.mingbyte.com/", "info@mingbyte.com"))
                .version("1.0")
                .description("API 描述")
                .build();
    }
}
