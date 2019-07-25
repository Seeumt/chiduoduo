package com.cumt.chiduoduo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：运行主类
 *
 */
@SpringBootApplication(scanBasePackages = {"com.cumt.chiduoduo"})
@MapperScan("com.cumt.chiduoduo.dao")
@EnableSwagger2
public class ChiduoduoApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
//    org.springframework.context.ConfigurableApplicationContext run =
    SpringApplication.run(ChiduoduoApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ChiduoduoApplication.class);
  }

  public void prin() {

  }
}

