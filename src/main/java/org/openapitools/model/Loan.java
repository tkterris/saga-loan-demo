package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Loan
 */

@Generated(value = "org.openapitools.codegen.languages.JavaCamelServerCodegen", date = "2024-11-11T14:12:24.531904-05:00[America/New_York]", comments = "Generator version: 7.9.0")
public class Loan {

  private Integer id;

  private BigDecimal amount;

  private Integer applicantId;

  private Boolean approved;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<Date> loanRequestDate = JsonNullable.<Date>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<Date> loanCancelDate = JsonNullable.<Date>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<Date> loanApprovalDate = JsonNullable.<Date>undefined();

  private JsonNullable<@Size(max = 256) String> comment = JsonNullable.<String>undefined();

  public Loan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Loan(Integer id, BigDecimal amount, Integer applicantId, Boolean approved) {
    this.id = id;
    this.amount = amount;
    this.applicantId = applicantId;
    this.approved = approved;
  }

  public Loan id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * minimum: 1
   * maximum: 2147483647
   * @return id
   */
  @NotNull @Min(1) @Max(2147483647) 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Loan amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  @NotNull @Valid 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Loan applicantId(Integer applicantId) {
    this.applicantId = applicantId;
    return this;
  }

  /**
   * Get applicantId
   * minimum: 1
   * maximum: 2147483647
   * @return applicantId
   */
  @NotNull @Min(1) @Max(2147483647) 
  @Schema(name = "applicantId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("applicantId")
  public Integer getApplicantId() {
    return applicantId;
  }

  public void setApplicantId(Integer applicantId) {
    this.applicantId = applicantId;
  }

  public Loan approved(Boolean approved) {
    this.approved = approved;
    return this;
  }

  /**
   * Get approved
   * @return approved
   */
  @NotNull 
  @Schema(name = "approved", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("approved")
  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public Loan loanRequestDate(Date loanRequestDate) {
    this.loanRequestDate = JsonNullable.of(loanRequestDate);
    return this;
  }

  /**
   * Get loanRequestDate
   * @return loanRequestDate
   */
  @Valid 
  @Schema(name = "loanRequestDate", example = "2016-08-29T09:12:33.001Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loanRequestDate")
  public JsonNullable<Date> getLoanRequestDate() {
    return loanRequestDate;
  }

  public void setLoanRequestDate(JsonNullable<Date> loanRequestDate) {
    this.loanRequestDate = loanRequestDate;
  }

  public Loan loanCancelDate(Date loanCancelDate) {
    this.loanCancelDate = JsonNullable.of(loanCancelDate);
    return this;
  }

  /**
   * Get loanCancelDate
   * @return loanCancelDate
   */
  @Valid 
  @Schema(name = "loanCancelDate", example = "2016-08-29T09:12:33.001Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loanCancelDate")
  public JsonNullable<Date> getLoanCancelDate() {
    return loanCancelDate;
  }

  public void setLoanCancelDate(JsonNullable<Date> loanCancelDate) {
    this.loanCancelDate = loanCancelDate;
  }

  public Loan loanApprovalDate(Date loanApprovalDate) {
    this.loanApprovalDate = JsonNullable.of(loanApprovalDate);
    return this;
  }

  /**
   * Get loanApprovalDate
   * @return loanApprovalDate
   */
  @Valid 
  @Schema(name = "loanApprovalDate", example = "2016-08-29T09:12:33.001Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loanApprovalDate")
  public JsonNullable<Date> getLoanApprovalDate() {
    return loanApprovalDate;
  }

  public void setLoanApprovalDate(JsonNullable<Date> loanApprovalDate) {
    this.loanApprovalDate = loanApprovalDate;
  }

  public Loan comment(String comment) {
    this.comment = JsonNullable.of(comment);
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  @Size(max = 256) 
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public JsonNullable<@Size(max = 256) String> getComment() {
    return comment;
  }

  public void setComment(JsonNullable<String> comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Loan loan = (Loan) o;
    return Objects.equals(this.id, loan.id) &&
        Objects.equals(this.amount, loan.amount) &&
        Objects.equals(this.applicantId, loan.applicantId) &&
        Objects.equals(this.approved, loan.approved) &&
        equalsNullable(this.loanRequestDate, loan.loanRequestDate) &&
        equalsNullable(this.loanCancelDate, loan.loanCancelDate) &&
        equalsNullable(this.loanApprovalDate, loan.loanApprovalDate) &&
        equalsNullable(this.comment, loan.comment);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, applicantId, approved, hashCodeNullable(loanRequestDate), hashCodeNullable(loanCancelDate), hashCodeNullable(loanApprovalDate), hashCodeNullable(comment));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Loan {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
    sb.append("    approved: ").append(toIndentedString(approved)).append("\n");
    sb.append("    loanRequestDate: ").append(toIndentedString(loanRequestDate)).append("\n");
    sb.append("    loanCancelDate: ").append(toIndentedString(loanCancelDate)).append("\n");
    sb.append("    loanApprovalDate: ").append(toIndentedString(loanApprovalDate)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

