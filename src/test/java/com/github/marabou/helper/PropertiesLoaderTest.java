package com.github.marabou.helper;

import com.github.marabou.properties.PropertiesLoader;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Properties;

public class PropertiesLoaderTest {


    @Test
    public void testLoadProperties() throws Exception {

        // given
        PropertiesLoader propertiesLoader = new PropertiesLoader(new PathHelper());
        String propertiesContent = "foo=bar";
        Properties properties = propertiesLoader.loadProperties(new ByteArrayInputStream(propertiesContent.getBytes()));

        // then
        Assertions.assertThat(properties.getProperty("foo").equals("bar"));
    }
}