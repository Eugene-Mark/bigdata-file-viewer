package org.eugene.persistent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.eugene.model.CommonData;
import org.eugene.model.TableMeta;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VirtualDB {
  @Getter
  private static VirtualDB instance = new VirtualDB();
  private CommonData commonData;
  private TableMeta tableMeta;
}
