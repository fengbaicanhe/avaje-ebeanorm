package com.avaje.tests.json;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.text.json.EJson;
import com.avaje.tests.model.json.EBasicJsonMapBlob;
import com.avaje.tests.model.json.EBasicJsonMapVarchar;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestJsonMapBlob extends BaseTestCase {

  @Test
  public void testInsertUpdateDelete() throws IOException {

    String s0 = "{\"docId\":18,\"contentId\":\"asd\",\"active\":true,\"contentType\":\"pg-hello\",\"content\":{\"name\":\"rob\",\"age\":45}}";

    Map<String, Object> content = EJson.parseObject(s0);

    EBasicJsonMapBlob bean = new EBasicJsonMapBlob();
    bean.setName("one");
    bean.setContent(content);

    Ebean.save(bean);

    EBasicJsonMapBlob bean1 = Ebean.find(EBasicJsonMapBlob.class, bean.getId());

    assertEquals(bean.getId(), bean1.getId());
    assertEquals(bean.getName(), bean1.getName());
    assertEquals(bean.getContent().get("contentType"), bean1.getContent().get("contentType"));
    assertEquals(18L, bean1.getContent().get("docId"));

    bean1.setName("just change name");
    Ebean.save(bean1);
  }
}
