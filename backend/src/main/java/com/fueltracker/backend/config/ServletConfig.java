package com.fueltracker.backend.config;

import com.fueltracker.backend.servlet.FuelStatsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<FuelStatsServlet> fuelStatsServlet() {
        ServletRegistrationBean<FuelStatsServlet> registrationBean = 
            new ServletRegistrationBean<>(new FuelStatsServlet(), "/servlet/fuel-stats");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}
