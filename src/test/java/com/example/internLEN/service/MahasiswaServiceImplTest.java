package com.example.internLEN.service;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.internLEN.Mahasiswa.AddMahasiswaRequest;
import com.example.internLEN.Mahasiswa.MahasiswaResponse;
import com.example.internLEN.MahasiswaServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;



@SpringBootTest
public class MahasiswaServiceImplTest {

    private ManagedChannel channel;
    private MahasiswaServiceGrpc.MahasiswaServiceBlockingStub stub;

    @BeforeEach
    public void setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
        stub = MahasiswaServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    public void tearDown() {
        channel.shutdown();
    }

    @Test
    public void testAddMahasiswa() {
        
        AddMahasiswaRequest request = AddMahasiswaRequest.newBuilder()
                .setNama("Izzan Alfadhil")
                .setNim("2106944")
                .setJurusan("Teknik Komputer")
                .build();

        MahasiswaResponse response;
        try {
            response = stub.addMahasiswa(request);
            assertNotNull(response);
            assertEquals("Izzan Alfadhil", response.getNama());
            assertEquals("2106944", response.getNim());
            assertEquals("Teknik Komputer", response.getJurusan());
        }catch(StatusRuntimeException e){
            fail("RPC Failed: " + e.getStatus());
        }
    }
}
