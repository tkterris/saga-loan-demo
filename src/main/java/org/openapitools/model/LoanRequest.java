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

  private Integer loanId;

  private BigDecimal amount;

  public LoanRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoanRequest(Integer loanId, BigDecimal amount) {
    this.loanId = loanId;
    this.amount = amount;
  }

  public LoanRequest loanId(Integer loanId) {
    this.loanId = loanId;
    return this;
  }

  /**
   * Get loanId
   * minimum: 1
   * maximum: 2147483647
   * @return loanId
   */
  @NotNull @Min(1) @Max(2147483647) 
  @Schema(name = "loanId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loanId")
  public Integer getLoanId() {
    return loanId;
  }

  public void setLoanId(Integer loanId) {
    this.loanId = loanId;
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
    return Objects.equals(this.loanId, loanRequest.loanId) &&
        Objects.equals(this.amount, loanRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loanId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanRequest {\n");
    sb.append("    loanId: ").append(toIndentedString(loanId)).append("\n");
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

