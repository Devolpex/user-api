package com.eshop.userbackend.response.client;

import com.eshop.userbackend.dto.user.ClientDto;
import com.eshop.userbackend.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientPageResponse {
    private List<ClientDto> clients = new ArrayList<>();
    private int currentPage;
    private int totalPages;

}
