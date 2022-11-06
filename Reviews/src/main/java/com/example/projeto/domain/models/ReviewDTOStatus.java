package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTOStatus {
    public Long reviewId;

    public String sku;

    public int rating;

    public String text;

    public LocalDate publishingDate  = LocalDate.now();

    public String funFact = retrieveDataFromApi(publishingDate);

    public int numberOfVotes = 0;

    public Status status = Status.PENDING;

    protected ReviewDTOStatus() throws IOException {}

    public ReviewDTOStatus(Long reviewId, String sku, int rating, String text, LocalDate publishingDate, String funFact, Status status) throws IOException {
        this.reviewId = reviewId;
        this.sku = sku;
        this.rating = rating;
        this.text = text;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.status = status;
    }

    public String retrieveDataFromApi(LocalDate publishingDate) throws IOException {
        String baseUrl = "http://www.numbersapi.com/";
        String url = baseUrl + publishingDate.getMonthValue() + "/" + publishingDate.getDayOfMonth() + "/date";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            return result;
        }
    }
}
