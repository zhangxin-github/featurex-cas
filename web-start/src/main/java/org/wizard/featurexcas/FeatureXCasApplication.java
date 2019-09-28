package org.wizard.featurexcas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.wizard.web.WizardBootApplication;

@WizardBootApplication
@ComponentScan("org.wizard.**.controller")
@MapperScan({"org.wizard.**.dao","org.wizard.**.mapper"})
public class FeatureXCasApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureXCasApplication.class, args);
    }

}
