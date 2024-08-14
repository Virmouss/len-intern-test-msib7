package com.example.internLEN.service;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.internLEN.Buku.AddBukuRequest;
import com.example.internLEN.Buku.BukuResponse;
import com.example.internLEN.BukuServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;



@SpringBootTest
public class BukuServiceIntegrationTest {

    private ManagedChannel channel;
    private BukuServiceGrpc.BukuServiceBlockingStub stub;

    @BeforeEach
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        stub = BukuServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void tearDown() {
        channel.shutdown();
    }

    @Test
    public void testAddBuku() {
  
        AddBukuRequest request = AddBukuRequest.newBuilder()
                .setTitle("Test Title")
                .setPenulis("Test Author")
                .setKuantitas(5)
                .setTempatPenyimpanan("Shelf 1")
                .build();

        BukuResponse response;
        try {
            response = stub.addBuku(request);
            assertNotNull(response);
            assertEquals("Test Title", response.getTitle());
            assertEquals("Test Author", response.getPenulis());
            assertEquals(5, response.getKuantitas());
            assertEquals("Shelf 1", response.getTempatPenyimpanan());
        } catch (StatusRuntimeException e) {
            fail("RPC failed: " + e.getStatus());
        }
    }
}
