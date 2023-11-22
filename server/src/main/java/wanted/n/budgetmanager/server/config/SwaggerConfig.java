package wanted.n.budgetmanager.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer { // http://localhost:8080/swagger-ui/

    /* Swagger Docket 설정을 생성하는 메서드 */
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("wanted.n")) // wanted.n 패키지 내 API 문서화
                .paths(PathSelectors.any()) // 모든 경로를 문서화
                .build()
                .apiInfo(apiInfo()) // API 정보를 설정
                .ignoredParameterTypes(Errors.class);
    }

    /* API 정보를 설정 하는 메서드 */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(" 예산 매니저 서비스 <Budget-Manager> REST API") // API 명세서 제목
                .description("Spring Boot를 이용한 REST API 프로젝트") // API 명세서 설명
                .version("0.0.1") // API 명세서 버전
                .build();
    }

    /* swagger-ui 페이지 연결 핸들러 설정 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
