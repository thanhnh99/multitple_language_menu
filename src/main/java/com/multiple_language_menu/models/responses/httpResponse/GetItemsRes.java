package com.multiple_language_menu.models.responses.httpResponse;

import com.multiple_language_menu.models.responses.dataResponse.ResItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetItemsRes extends HttpResponse<ResItem> {
}
