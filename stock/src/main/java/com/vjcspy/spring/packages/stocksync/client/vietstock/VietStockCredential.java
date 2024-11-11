/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.client.vietstock;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VietStockCredential {
    private String sid, rvt, vtsUsrLg, usrTk, csrf;
}
