package com.wms.data.misuse;

import com.wms.data.Status;

import java.time.LocalDate;

public record Request(int id,int sender, int receiver, int productId, int quantity, LocalDate timestamp, Status status) {
}
