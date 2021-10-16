package com.application.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.application.core.Mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_email")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(doNotUseGetters = true)
@Builder
public class AccountsEmail extends BaseEntity {

  @Column(length = 50, nullable = false)
  private String email;
  
  @Column(length = 128, nullable = false, unique = true)
  private String token;
  
  @Column(length = 15, nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Mail type;
  
  @Column(insertable = false, updatable = false, columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP", name = "sent_on")
  private LocalDateTime sentTimestamp;
}
