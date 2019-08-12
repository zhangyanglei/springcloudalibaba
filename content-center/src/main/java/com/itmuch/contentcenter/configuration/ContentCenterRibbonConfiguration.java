package com.itmuch.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
@Configuration
public class ContentCenterRibbonConfiguration {

}
