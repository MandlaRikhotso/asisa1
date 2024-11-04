package za.co.oldmutual.asisa;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

  @Bean
  public Docket apiDocumentation() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("za.co.oldmutual.asisa"))
        .paths(PathSelectors.regex("/api/.*")).build().apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo("OM ASISA PROJECT", "OM ASISA Standalone Application APIs", "1.0",
        "Terms of Service", new Contact("Our Team", "", ""), "OM ASISA REGISTERS", " ",
        Collections.emptyList());
  }

}
