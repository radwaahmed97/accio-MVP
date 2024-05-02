package com.accio.api.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class DataStoreResource {
  public abstract int getId();

  @CreatedDate
  private Instant createdAt;
  @CreatedBy
  private String createdBy;

  @LastModifiedDate
  private Instant modifiedAt;

  @LastModifiedBy
  private String modifiedBy;


  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return getId() == ((DataStoreResource) o).getId();
  }

  @Override
  public final int hashCode() {
    return getId();
  }
}
