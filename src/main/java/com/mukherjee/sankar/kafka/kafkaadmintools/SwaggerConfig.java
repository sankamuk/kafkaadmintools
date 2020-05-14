package com.mukherjee.sankar.kafka.kafkaadmintools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
                .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

    private Predicate<String> postPaths() {
        return or(regex("/acl.*"),
                regex("/acl/*"),
                regex("/topics.*"),
                regex("/topics/*"),
                regex("/process.*"),
                regex("/process/*"),
                regex("/services.*"),
                regex("/services/*"));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Kafka Admin Tools API")
                .description("Kafka Admin Tools - Manage Apache Kafka Cluster")
                .termsOfServiceUrl("https://mukherjeesankar.wordpress.com/")
                .contact("sanmuk21@gmail.com").license("MIT License")
                .licenseUrl("sanmuk21@gmail.com").version("1.0").build();
    }

}
