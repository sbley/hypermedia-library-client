package de.saxsys.hypermedia.javafx.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.saxsys.hypermedia.javafx.service.HalUtil;

public class HalUtilTest {

    @Test
    public void replaceParam() throws Exception {
        assertThat(HalUtil.replaceParam("http://localhost:8080/search?q={query}", "max"),
                is("http://localhost:8080/search?q=max"));
    }
}
