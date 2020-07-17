package com.multiple_language_menu.models.responses.httpResponse;

import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.responses.dataResponse.ResCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoriesRes extends HttpResponse<ResCategory> {
}
