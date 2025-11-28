// package com.tms.Configuration.OpenAPI;

// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.License;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class SwaggerConfig {

//         @Bean
//         public OpenAPI customOpenAPI() {
//                 return new OpenAPI()
//                                 .info(new Info()
//                                                 .title("Tax Management System API")
//                                                 .version("v1.0")
//                                                 .description(
//                                                                 "API documentation for Tax Management System")
//                                                 .contact(new Contact()
//                                                                 .name("TMS System")
//                                                                 .url("https://tms.awashbank.com"))
//                                                 .license(new License()
//                                                                 .name("Apache 2.0")
//                                                                 .url("https://www.apache.org/licenses/LICENSE-2.0")));
//         }
// }
