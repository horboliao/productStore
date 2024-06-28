import clientServerApp.entity.Product;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    @Test
    void toJSON_ShouldReturnValidJSON() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("name", "Product Name");
        expectedJson.put("price", 10.99);
        expectedJson.put("amount", 5);
        expectedJson.put("description", "Product Description");
        expectedJson.put("producer", "Product Producer");
        expectedJson.put("group_id", 1);

        assertEquals(expectedJson.toString(), product.toJSON().toString());
    }

    @Test
    void toString_ShouldReturnFormattedString() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);

        String expectedString = "{ name: Product Name, price: 10.99, amount: 5, description: Product Description, producer: Product Producer, group_id: 1 }";

        assertEquals(expectedString, product.toString());
    }

    @Test
    void getName_ShouldReturnProductName() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals("Product Name", product.getName());
    }

    @Test
    void setName_ShouldUpdateProductName() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setName("New Product Name");
        assertEquals("New Product Name", product.getName());
    }

    @Test
    void getPrice_ShouldReturnProductPrice() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals(10.99, product.getPrice());
    }

    @Test
    void setPrice_ShouldUpdateProductPrice() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setPrice(9.99);
        assertEquals(9.99, product.getPrice());
    }

    @Test
    void getAmount_ShouldReturnProductAmount() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals(5, product.getAmount());
    }

    @Test
    void setAmount_ShouldUpdateProductAmount() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setAmount(10);
        assertEquals(10, product.getAmount());
    }

    @Test
    void getDescription_ShouldReturnProductDescription() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals("Product Description", product.getDescription());
    }

    @Test
    void setDescription_ShouldUpdateProductDescription() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setDescription("New Product Description");
        assertEquals("New Product Description", product.getDescription());
    }

    @Test
    void getProducer_ShouldReturnProductProducer() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals("Product Producer", product.getProducer());
    }

    @Test
    void setProducer_ShouldUpdateProductProducer() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setProducer("New Product Producer");
        assertEquals("New Product Producer", product.getProducer());
    }

    @Test
    void getGroup_id_ShouldReturnProductGroupId() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        assertEquals(1, product.getGroup_id());
    }

    @Test
    void setGroup_id_ShouldUpdateProductGroupId() {
        Product product = new Product("Product Name", 10.99, 5, "Product Description", "Product Producer", 1);
        product.setGroup_id(2);
        assertEquals(2, product.getGroup_id());
    }

}
