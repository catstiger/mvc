package com.github.catstiger.mvc.util;

import org.junit.Test;
import org.springframework.util.Assert;

public class StringUtilsTest {
  @Test
  public void testIsNumber() {
    Assert.isTrue(StringUtils.isNumber("9987744232"));
    Assert.isTrue(StringUtils.isNumber("-98787544332"));
    Assert.isTrue(StringUtils.isNumber("34.9995834"));
    Assert.isTrue(StringUtils.isNumber("-45.9954"));
    Assert.isTrue(StringUtils.isNumber("23245.8"));
    Assert.isTrue(StringUtils.isNumber("0x0085"));
    Assert.isTrue(!StringUtils.isNumber("79.34.45"));
    Assert.isTrue(!StringUtils.isNumber("99,685,434,343"));
    Assert.isTrue(!StringUtils.isNumber("--4454"));
    
  }
}
