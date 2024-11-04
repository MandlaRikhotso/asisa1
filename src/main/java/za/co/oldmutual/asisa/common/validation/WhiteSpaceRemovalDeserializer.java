package za.co.oldmutual.asisa.common.validation;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class WhiteSpaceRemovalDeserializer extends StdDeserializer<String> {

  public WhiteSpaceRemovalDeserializer() {
    this(null);
  }

  public WhiteSpaceRemovalDeserializer(Class<?> c) {
    super(c);
  }

  @Override
  public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return p.getText().trim();
  }


}
