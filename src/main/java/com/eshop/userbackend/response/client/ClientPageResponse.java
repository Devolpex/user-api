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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<ClientDto> clients;
        private int currentPage;
        private int totalPages;

        public Builder clients(List<ClientDto> clients) {
            this.clients = clients;
            return this;
        }

        public Builder currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public ClientPageResponse build() {
            ClientPageResponse response = new ClientPageResponse();
            response.clients = this.clients;
            response.currentPage = this.currentPage;
            response.totalPages = this.totalPages;
            return response;
        }
    }
}
