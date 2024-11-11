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
 * Applicant
 */

@Generated(value = "org.openapitools.codegen.languages.JavaCamelServerCodegen", date = "2024-11-11T14:12:24.531904-05:00[America/New_York]", comments = "Generator version: 7.9.0")
public class Applicant {

  private Integer id;

  private BigDecimal limit;

  private Boolean approved;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<Date> limitUpdateDate = JsonNullable.<Date>undefined();

  private JsonNullable<@Size(max = 256) String> comment = JsonNullable.<String>undefined();

  private Applicant loan;

  public Applicant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Applicant(Integer id, BigDecimal limit, Boolean approved) {
    this.id = id;
    this.limit = limit;
    this.approved = approved;
  }

  public Applicant id(Integer id) {
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

  public Applicant limit(BigDecimal limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Get limit
   * @return limit
   */
  @NotNull @Valid 
  @Schema(name = "limit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("limit")
  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }

  public Applicant approved(Boolean approved) {
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

  public Applicant limitUpdateDate(Date limitUpdateDate) {
    this.limitUpdateDate = JsonNullable.of(limitUpdateDate);
    return this;
  }

  /**
   * Get limitUpdateDate
   * @return limitUpdateDate
   */
  @Valid 
  @Schema(name = "limitUpdateDate", example = "2016-08-29T09:12:33.001Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limitUpdateDate")
  public JsonNullable<Date> getLimitUpdateDate() {
    return limitUpdateDate;
  }

  public void setLimitUpdateDate(JsonNullable<Date> limitUpdateDate) {
    this.limitUpdateDate = limitUpdateDate;
  }

  public Applicant comment(String comment) {
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

  public Applicant loan(Applicant loan) {
    this.loan = loan;
    return this;
  }

  /**
   * Get loan
   * @return loan
   */
  @Valid 
  @Schema(name = "loan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loan")
  public Applicant getLoan() {
    return loan;
  }

  public void setLoan(Applicant loan) {
    this.loan = loan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Applicant applicant = (Applicant) o;
    return Objects.equals(this.id, applicant.id) &&
        Objects.equals(this.limit, applicant.limit) &&
        Objects.equals(this.approved, applicant.approved) &&
        equalsNullable(this.limitUpdateDate, applicant.limitUpdateDate) &&
        equalsNullable(this.comment, applicant.comment) &&
        Objects.equals(this.loan, applicant.loan);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, limit, approved, hashCodeNullable(limitUpdateDate), hashCodeNullable(comment), loan);
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
    sb.append("class Applicant {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    approved: ").append(toIndentedString(approved)).append("\n");
    sb.append("    limitUpdateDate: ").append(toIndentedString(limitUpdateDate)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    loan: ").append(toIndentedString(loan)).append("\n");
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

