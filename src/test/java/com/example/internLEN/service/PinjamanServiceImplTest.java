package com.example.internLEN.service;


import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.internLEN.Pinjaman.AddPinjamanRequest;
import com.example.internLEN.Pinjaman.PinjamanResponse;
import com.example.internLEN.PinjamanServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;



@SpringBootTest
public class PinjamanServiceImplTest {

    private ManagedChannel channel;
    private PinjamanServiceGrpc.PinjamanServiceBlockingStub stub;

    @BeforeEach
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        stub = PinjamanServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void tearDown() {
        channel.shutdown();
    }

    @Test
    public void testAddPinjaman() {
        
        AddPinjamanRequest request = AddPinjamanRequest.newBuilder()
                .setMahasiswaId(1)
                .setBukuId(1)
                .setTglPinjam("2024-08-01")
                .setTglBatasanPengembalian("2024-08-08")
                .build();
        
       
        try {
            PinjamanResponse response;
            response = stub.addPinjaman(request);
            assertThat(response).isNotNull();
        }catch(StatusRuntimeException e){
            fail("RPC Failed: " + e.getStatus());
        }
    }
    
}