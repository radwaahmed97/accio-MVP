package com.accio.api.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageableUtil {
  private PageableUtil() {
  }

  public static <T> Page<T> getPage(List<T> list, Pageable pageable) {
    var size = list.size();
    if (pageable == null || pageable.isUnpaged()) {
      return new PageImpl<>(list, Pageable.unpaged(), size);
    }
    var start = (int) pageable.getOffset();
    var end = Math.min((start + pageable.getPageSize()), size);
    var subLlist = start < size ? list.subList(start, end) : List.<T>of();
    return new PageImpl<>(subLlist, pageable, size);
  }

}
