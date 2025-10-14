package com.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo()).select().paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Inventory Management System")
                .description("Inventory Management System to handle the transaction info between different stakeHolders")
                .termsOfServiceUrl("https://github.com/chintamanand")
                .contact(new Contact("Chintam Anand","https://github.com/chintamanand","chintamanand56@gmail.com")).license("JavaInUse License")
                .licenseUrl("chintamanand56@gmail.com").version("1.0.0").build();
    }

}
