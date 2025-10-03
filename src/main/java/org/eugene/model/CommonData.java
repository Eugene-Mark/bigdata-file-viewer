package org.eugene.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonData {
  private String schema;
  private List<List<String>> data;
  private String name;
  private Map<String, String> columnToType;
}
