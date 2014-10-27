package de.saxsys.hypermedia.jsf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HalUtilTest {

    @Test
    public void replaceParam() throws Exception {
        assertThat(HalUtil.replaceParam("http://localhost:8080/search?q={query}", "max"),
                is("http://localhost:8080/search?q=max"));
    }
}
