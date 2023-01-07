package com.example.projeto.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String sku;

    private Long userId;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false, length = 1000)
    private String text;
    @Column
    private String publishingDate = addDataTime();

    private Status status = Status.PENDING;
    @Column
    private String funFact = retrieveDataFromApi(publishingDate);

    protected Review() throws IOException {}


    public Review(String sku, int rating, String text) throws IOException {
        this.sku = sku;
        this.rating = rating;
        this.text = text;
    }

    public String retrieveDataFromApi(String publishingDate) throws IOException {
        String baseUrl = "http://www.numbersapi.com/";
        String url = baseUrl + publishingDate.substring(3,5) + "/" + publishingDate.substring(0,2) + "/date";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            return result;
        }
    }
    public String addDataTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        return this.publishingDate = formattedDate;
    }

    public void applyPatch(final Review newReview) {
        if (newReview.getStatus() != null) {
            setStatus(newReview.getStatus());
        }
    }

    public void applyPatchDTO(final ReviewDTOStatus newReview) {
        if (newReview.getStatus() != null) {
            setStatus(newReview.getStatus());
        }
    }
}
