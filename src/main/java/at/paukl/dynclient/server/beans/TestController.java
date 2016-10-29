package at.paukl.dynclient.server.beans;

import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Paul Klingelhuber
 */
@RestController
public class TestController {
    private static final Logger LOG = getLogger(TestController.class);

    @RequestMapping("/test")
    public Object testResource() {
        return new ClassPathResource("test.response");
    }

}
