package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * LoanRequest
 */

@Generated(value = "org.openapitools.codegen.languages.JavaCamelServerCodegen", date = "2024-11-11T14:12:24.531904-05:00[America/New_York]", comments = "Generator version: 7.9.0")
public class LoanRequest {

  private Integer applicantId;

  private BigDecimal amount;

  public LoanRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoanRequest(Integer applicantId, BigDecimal amount) {
    this.applicantId = applicantId;
    this.amount = amount;
  }

  public LoanRequest applicantId(Integer applicantId) {
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
  public Integer getapplicantId() {
    return applicantId;
  }

  public void setApplicantIs(Integer applicantId) {
    this.applicantId = applicantId;
  }

  public LoanRequest amount(BigDecimal amount) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoanRequest loanRequest = (LoanRequest) o;
    return Objects.equals(this.applicantId, loanRequest.applicantId) &&
        Objects.equals(this.amount, loanRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicantId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanRequest {\n");
    sb.append("    applicantId: ").append(toIndentedString(applicantId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

