package com.hlcy.yun.gb28181.service.params.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CatalogQueryParams extends QueryParams {
    public CatalogQueryParams() {
        super("Catalog");
    }
}
