package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotNull(message = "Category type is required (INCOME or EXPENSE)")
    private TransactionType type;
}
