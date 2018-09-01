package com.bulain.shiro.conf;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig {

	@Bean
	public FilterRegistrationBean<SiteMeshFilter> siteMeshFilter() {
		FilterRegistrationBean<SiteMeshFilter> fitler = new FilterRegistrationBean<SiteMeshFilter>();
		SiteMeshFilter siteMeshFilter = new SiteMeshFilter();
		fitler.setFilter(siteMeshFilter);
		return fitler;
	}

	static class SiteMeshFilter extends ConfigurableSiteMeshFilter {

		@Override
		protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
			builder
				.addDecoratorPath("/*", "/WEB-INF/jsp/decorators/layout.jsp")
				.addExcludedPath("/static/**")
				.addTagRuleBundle(new JsScriptTagRuleBundle())
				;
		}

	}

}
