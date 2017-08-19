package example.model;

import java.util.Map;

public interface Mail {
    String getSubject();
    Map<String, Object> getParams();
    String getFrom();
    String getTo();
    String getContentTemplate();
}
