package io.sunshower.wildfly.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import io.sunshower.test.common.SerializationAware;
import io.sunshower.test.common.SerializationTestCase;
import lombok.val;
import org.junit.jupiter.api.Test;

class ManagementRequestElementTest extends SerializationTestCase {

  ManagementRequestElementTest() {
    super(SerializationAware.Format.JSON, ManagementRequestElement.class);
  }

  @Test
  void ensureManagementRequestHasCorrectNumberOfDeployments() {
    val req =
        read(
            ClassLoader.getSystemResourceAsStream("management-response.json"),
            ManagementRequestElement.class);
    assertThat(req.getDeployments().size(), is(3));
  }
}
