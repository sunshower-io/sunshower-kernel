package io.sunshower.kernel.spring;

import io.sunshower.kernel.api.Fulfillment;
import org.springframework.stereotype.Component;

@Component
@Fulfillment(
        name = "test-fulfillment",
        extensionPoint = TestFulfillment.class
)
public class TestFulfillmentImpl implements TestFulfillment {}
