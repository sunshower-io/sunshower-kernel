package io.sunshower.kernel.wildfly.model;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ModelsTest {

    @Test
    void ensureModelParsingWorksForVersion() {
        val a = Models.parse("test-wildfly-v3.war", null);
        assertThat(a.getVersion(), is("v3"));
    }

    @Test
    void ensureModelParsingWorksForCoordinates() {
        val a = Models.parse("test-wildfly-v3.war", null);
        assertThat(a.getName(), is("test-wildfly"));
    }




}