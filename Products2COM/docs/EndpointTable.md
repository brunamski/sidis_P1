# Users

| Method | Endpoint                              | Description        |
|--------|---------------------------------------|--------------------|
| *POST* | http://localhost:8081/public/register | To register a user |
| *POST* | http://localhost:8081/public/login    | To login a user    |


# Products

| Method   | Endpoint                                                                  | Description                                               |
|----------|---------------------------------------------------------------------------|-----------------------------------------------------------|
| *GET*    | http://localhost:8081/api/public/product                                  | To get a catalog of the Products in the system            |
| *GET*    | http://localhost:8081/api/public/product/{sku}                            | To see the details about a specific Product in the system |
| *POST*   | http://localhost:8081/api/admin/product                                   | To add a Product to the system                            |
| *DELETE* | http://localhost:8081/api/products/id                                     | To remove a specific Product from the system              |
| *GET*    | http://localhost:8081/api/public/product/name/{name}                      | To search the catalog of products by product name         |
| *GET*    | http://localhost:8081/api/public/product/bar_code/{sku}                   | To search the catalog of products by bar code             |
| *GET*    | http://localhost:8081/api/public/product/rating/{sku}                     | To obtain the aggregated rating of a product              |
| *POST*   | http://localhost:8081/api/product/{sku}/image                             | Uploads an image                                          |
| *POST*   | http://localhost:8081/api/product/{sku}/images                            | Upload a set of images                                    |
| *GET*    | http://localhost:8081/api/product/{sku}/images/downloadFile/{fileName:.+} | Gets an image                                             |


# Reviews

| Method   | Endpoint                                                   | Description                                                                                             |
|----------|------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| *GET*    | http://localhost:8081/api/public/review/product/{sku}      | To obtain the reviews of a product. Sorted in reverse chronological publishing date                     |
| *GET*    | http://localhost:8081/api/public/review/vote/product/{sku} | To obtain the reviews of a product. Sorted by number of votes and reverse chronological publishing date |
| *POST*   | http://localhost:8081/api/review                           | To review and rate a product                                                                            |
| *DELETE* | http://localhost:8081/api/review/{id}/withdraw             | To withdraw one of my reviews                                                                           |
| *POST*   | http://localhost:8081/api/review/{id}/vote                 | To vote for a review                                                                                    |
| *GET*    | http://localhost:8081/api/review/pending                   | To obtain all pending reviews                                                                           |
| *GET*    | http://localhost:8081/api/review/status                    | To obtain all my reviews including their status                                                         |
| *PATCH*  | http://localhost:8081/api/review/{id}                      | To approve or reject a pending review                                                                   |
